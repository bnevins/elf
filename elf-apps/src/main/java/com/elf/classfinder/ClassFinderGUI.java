/*
 * ClassFinderGUI.java
 *
 * Created on June 14, 2003, 8:36 PM
 */

package com.elf.classfinder;

import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import com.elf.swing.*;
import com.elf.interfaces.*;
import com.elf.io.*;
import com.elf.util.StringUtils;

/**
 *
 * @author  bnevins
 */
public class ClassFinderGUI 
		extends CloseableFrame 
		implements	Runnable, Interruptible, ActionListener, ListSelectionListener
{
	public ClassFinderGUI(String title) 
	{
		super(title);
		initFrame();
		contentPane.add(makeInputPanel(), BorderLayout.NORTH);
		contentPane.add(makeClassListPanel(), BorderLayout.CENTER);
		contentPane.add(makeJarListPanel(), BorderLayout.SOUTH);
		persist = new SimplePersistence(this, false);
		loadProps();
		showGUI();
	}
	
	///////////////////////////////////////////////////////////////////////////
	/////////   Implemented Interface Methods    //////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	public void actionPerformed(ActionEvent event) 
	{
		Object o = event.getSource();
		
		if(o == serializeOut.getButton2()) 
		{
			// write serialized
			try
			{
				classFinder.serialize(serializeOut.getText());
				info("Wrote data.");
			}
			catch(Exception e)
			{
				error("Error serializing: " + e);
			}
		}
		else if(o == serializeOut.getButton1()) 
		{
			// read serialized
			try
			{
				classFinder = ClassFinder.unSerialize(serializeOut.getText());			
				fillData();
				info("Read data.");
				classFinder.getDupes();
			}
			catch(Exception e)
			{
				error("Error unserializing: " + e);
			}
		}
		else if(o == jarRoot.getButton())
		{
			findClasses();
		}
		else if(o == srcRoot.getButton())
		{
			findSource();
		}
		else if(o == textOut.getButton())
		{
			writeClassFinderAsText(textOut.getText());
		}
		else if(o == classFilter.getButton())
		{
			fillData();
		}
		else if(o == helpButton)
		{
			help();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	
	public void run() 
	{
	}

	///////////////////////////////////////////////////////////////////////////
	
	public boolean isInterrupted() 
	{
		// TODO -- Add a cancel button...
		return false;
	}

	///////////////////////////////////////////////////////////////////////////
	
	public void processWindowEvent(WindowEvent event) 
	{
		storeProps();
		super.processWindowEvent(event); // Graceful shutdown...
  }

	///////////////////////////////////////////////////////////////////////////
	
	public void valueChanged(ListSelectionEvent e) 
	{
		if(e.getValueIsAdjusting() == true)
			return;
		
		jarListModel.clear();
		
		int index = classList.getSelectedIndex();
		
		if (index >= 0) 
		{
			//Selection, update the jars list...
			List jars = classFinder.getJars((String)classListModel.get(index));
			
			for(Iterator it = jars.iterator(); it.hasNext(); )
				jarListModel.addElement((String)it.next());
		}
	}

	///////////////////////////////////////////////////////////////////////////
	/////////   Private Methods    ////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private void initFrame()
	{
		setBackground(Color.lightGray);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(20, 30));
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private JPanel makeInputPanel()
	{
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(6, 1));
		jarRoot			= new LabeledTextFieldWithButton("Root of Jar-Directory To Search:",	"Search", 50, this);
		srcRoot			= new LabeledTextFieldWithButton("Root of Source-Directory To Search:", "Search", 50, this);
		textOut			= new LabeledTextFieldWithButton("Text File Output:", "Write", 50, this);
		serializeOut	= new LabeledTextFieldWith2Buttons("Serialized File Output:", "Read", "Write", 50, this);
		classFilter		= new LabeledTextFieldWithButton("Classname Filter:", "Filter This Mess", 50, this);
		
		inputPanel.add(jarRoot);
		inputPanel.add(srcRoot);
		inputPanel.add(textOut);
		inputPanel.add(serializeOut);
		inputPanel.add(classFilter);
		inputPanel.add(makeStatsLabel());
		return inputPanel;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private JLabel makeStatsLabel()
	{
		statsLabel = new JLabel();
		statsLabel.setHorizontalAlignment(JLabel.CENTER);
		statsLabel.setFont(labelFont);

		return statsLabel;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private JPanel makeClassListPanel()
	{
		JPanel classListPanel = new JPanel();
		classListPanel.setLayout(new BorderLayout());
		
		JPanel labelPanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Classes", JLabel.CENTER);
		label.setFont(headingFont);
		labelPanel.add(label, BorderLayout.CENTER);
		helpButton = new JButton("Help");
		helpButton.addActionListener(this);
		labelPanel.add(helpButton, BorderLayout.EAST);
		
		classListModel = new DefaultListModel();

		//for(int i = 0; i < 300; i++)
			//classListModel.addElement("sasdjl.aslkdjlk..asdlj" + i);

		classList = new JList(classListModel);
		classList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		classList.addListSelectionListener(this);
		
		JScrollPane scrollPane = new JScrollPane(classList);
		
		classListPanel.add(labelPanel, BorderLayout.NORTH);
		classListPanel.add(scrollPane, BorderLayout.CENTER);

		return classListPanel;
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	
	private JPanel makeJarListPanel()
	{
		JPanel jarListPanel = new JPanel();
		jarListPanel.setLayout(new BorderLayout());

		JLabel label = new JLabel("Jars that contain the selected class", JLabel.CENTER);
		label.setFont(headingFont);
		
		jarListModel = new DefaultListModel();

		//for(int i = 0; i < 300; i++)
			//jarListModel.addElement("sasdjl.aslkdjlk..asdlj" + i);

		jarList = new JList(jarListModel);
		
		JScrollPane scrollPane = new JScrollPane(jarList);
		
		jarListPanel.add(label, BorderLayout.NORTH);
		jarListPanel.add(scrollPane, BorderLayout.CENTER);

		return jarListPanel;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void showGUI()
	{
		setSize(900, 800);
		//pack();
		setVisible(true);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void fillData()
	{
		classListModel.clear();
		jarListModel.clear();
		
		if(classFinder == null)
			return;
		
		Set set = classFinder.getClasses();
		String filter = classFilter.getText();
		boolean hasFilter = StringUtils.ok(filter);
		numFilteredClasses = 0;
		
		for(Iterator it = set.iterator(); it.hasNext(); )
		{
			String className = (String)it.next();
			
			if(hasFilter && className.indexOf(filter) < 0)
				continue;
			
			++numFilteredClasses;
			classListModel.addElement(className);
		}
		
		fillStats();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void fillStats()
	{
		assert classFinder != null;
		int nUnique = classFinder.getNumUniqueClasses();
		int nAll	= classFinder.getNumAllClasses();
		int nJars	= classFinder.getNumJars();
		
		String stats = "Number of Jars: " + nJars + "   Number of ALL classes in ALL Jars: " + nAll + "   Number of unique classes: " + nUnique
				+ "  Number of filtered classes: " + numFilteredClasses;
		
		statsLabel.setText(stats);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void serializeClassFinder(String filename)
	{
		try
		{
			File f = new File(filename);
			File parent = f.getParentFile();
			
			if(parent != null && !parent.exists())
				parent.mkdirs();
			
			FileOutputStream out = new FileOutputStream(f);
			ObjectOutputStream s = new ObjectOutputStream(out);
			s.writeObject(classFinder);
			s.flush();
			s.close();
			info("Serialized Data to " + f.getAbsolutePath());
		}
		catch(Exception e)
		{
			error("" + e);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void unSerializeClassFinder(String filename)
	{
		try
		{
			FileInputStream in = new FileInputStream(filename);
			ObjectInputStream s = new ObjectInputStream(in);
			classFinder = (ClassFinder)s.readObject();
			s.close();
			fillData();
			info("Read Serialized Data from " + new File(filename).getAbsolutePath());
		}
		catch(Exception e)
		{
			error("" + e);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void writeClassFinderAsText(String filename)
	{
		try
		{
			File f = new File(filename);
			f = f.getCanonicalFile();
			File parent = f.getParentFile();
			
			if(parent != null && !parent.exists())
				parent.mkdirs();
			
			classFinder.write(filename);
			info("Wrote human-readable data to " + filename);
		}
		catch(Exception e)
		{
			error("" + e);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void findClasses()
	{
		// wait and make sure all is OK before setting "classFinder"!!
		try 
		{ 
			String filename = jarRoot.getText();
			if(!FileUtils.safeIsDirectory(filename))
			{
				error(filename + " is not a valid directory.");
				return;
			}
			
			ClassFinder cf = new ClassFinder(filename);
			cf.findClasses();
			classFinder = cf;
			fillData();
		}
		catch(Exception e)
		{
			error("" + e);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void findSource()
	{
		// wait and make sure all is OK before setting "classFinder"!!
		try 
		{ 
			String filename = srcRoot.getText();
			if(!FileUtils.safeIsDirectory(filename))
			{
				error(filename + " is not a valid directory.");
				return;
			}
			
			SourceFinder cf = new SourceFinder(new File(filename));
			//cf.findClasses();
			//classFinder = cf;
			//fillData();
		}
		catch(Exception e)
		{
			error("" + e);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void loadProps()
	{
		getProp("jarRoot",		jarRoot.getTextField());
		getProp("srcRoot",		srcRoot.getTextField());
		getProp("textOut",		textOut.getTextField());
		getProp("classFilter",	classFilter.getTextField());
		getProp("serializeOut",	serializeOut.getTextField());
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void storeProps()
	{
		persist.clear();
		setProp("jarRoot",			jarRoot.getText());
		setProp("srcRoot",			srcRoot.getText());
		setProp("textOut",			textOut.getText());
		setProp("classFilter",		classFilter.getText());
		setProp("serializeOut",		serializeOut.getText());
		persist.store();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void setProp(String key, String value)
	{
		if(StringUtils.ok(key) && StringUtils.ok(value))
			persist.setProperty(key, value);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void getProp(String key, JTextField field)
	{
		if(StringUtils.ok(key))
		{
			String value = persist.getProperty(key);
			
			if(StringUtils.ok(value))
				field.setText(value);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void help()
	{
		JOptionPane.showMessageDialog(this, helpMessage, getTitle() + " -- Help", JOptionPane.INFORMATION_MESSAGE); 
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void error(String s)
	{
		JOptionPane.showMessageDialog(this, s, getTitle() + " -- Error", JOptionPane.ERROR_MESSAGE); 
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void info(String s)
	{
		JOptionPane.showMessageDialog(this, s, getTitle() + " -- Information", JOptionPane.INFORMATION_MESSAGE); 
	}

	///////////////////////////////////////////////////////////////////////////
	////////////////   Variables    ///////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	private	ClassFinder						classFinder = null;
	private Container						contentPane;
	private	JPanel							inputPanel;
	private	LabeledTextFieldWithButton		jarRoot;
	private	LabeledTextFieldWithButton		srcRoot;
	private	LabeledTextFieldWithButton		textOut;
	private	LabeledTextFieldWithButton		classFilter;
	private	LabeledTextFieldWith2Buttons	serializeOut;
	private JLabel							statsLabel;
	private	static final Font				labelFont		= new Font("Serif",		 Font.BOLD, 14);
	private	static final Font				headingFont		= new Font("SansSerif",  Font.BOLD, 18);
	private	static final Font				textFont		= new Font("Monospaced", Font.BOLD, 12);
	private JList							classList;						
	private JList							jarList;						
	private JButton							helpButton;
	private DefaultListModel				classListModel;
	private DefaultListModel				jarListModel;
	private	SimplePersistence				persist;
	private int								numFilteredClasses = 0;
	private final static String helpMessage =
"ClassFinderGUI is a tool for finding and displaying class files that are inside jar files.\n" +
"Enter the top-level directory in the \"Root of Directory to Search\" field.\n" +
"Press Search.  The tool will ferret out every jar file in the directory tree, and then create a list\n" +
"of all the .class files found in the jars.  ClassFinderGUI will remember all the jar files that each .class file\n" +
"is located in.  Select a class file and these jar files will appear in the jar list.\n" +
"\nIf you are searching a huge file system that doesn't change often, you can save the results by entering a filename\n" + 
"in the Serialized Output field and pressing the write button.  Press the read button to read in the persisted results at any time\n" +
"\nYou can also save a human-readable file with the same information -- enter a filename in the \"Text File Output\" fiels and press write\n" +
"\nYou can filter the class names by entering a String in the \"Classname Filter\" field.  Press the Filter This Mess button and only\n" +
"classes that contain that String will be shown.\n" +
"\nThe GUI persists all the fields that you enter text into for the next invocation to save you keystrokes and hassle.  The persistence file\n" +
"is located in the same directory as the class and is named persistence.properties.  Unless you're running it from a jar in which case\n"+
"it will be in <java-temp-dir>/com.elf.io.properties\n\n\n" +
"Byron Nevins, June 2003";

 /*
hierarchy,
find every jar file and pull out the name of every class in
every jar
file.  The classes are then sorted and displayed in a listbox.
When you
click on a class name another listbox will display every jar
that the
class was found in.  You can filter the class names with a
simple string
too (e.g. "ant." or "deployment" or "com.sun.ent")

note:  A Search of a mega-filesystem like jws/publish takes a
LONG
(1-2minutes) time.  You can serialize it and use it much faster
the next time.  It'll be pretty clear how to do that when you
see the
screen (I hope!)


-- I attached the jar file.  You run it like so:

java -cp elf.jar ClassFinderGUI.jar
	
*/	
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) 
	{
		ClassFinderGUI cfg = new ClassFinderGUI("Class Finder");
	}
}
