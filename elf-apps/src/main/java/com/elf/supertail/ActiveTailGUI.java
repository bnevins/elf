/*
 * ActiveTailGUI.java
 *
 * Created on May 22, 2005, 2:35 PM
 */

package com.elf.supertail;

import com.elf.interfaces.*;
import com.elf.io.ActiveTail;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
public class ActiveTailGUI extends JFrame implements ActionListener
{
	public ActiveTailGUI(String[] fnames) 
	{
		files		= new File			[fnames.length];
		threads		= new Thread		[fnames.length];
		appenders	= new TextAppender	[fnames.length];
		
		for(int i = 0; i < fnames.length; i++)
		{
			files[i] = new File(fnames[i]);
		}
		
		Container cp = getContentPane();
		cp.add(makeButtonPanel(),	"South");
		cp.add(makeTailPanel(),		"Center");
		
		for(int i = 0; i < threads.length; i++)
		{
			ActiveTail at = new ActiveTail(appenders[i], files[i].getAbsolutePath());
			threads[i] = new Thread(at, files[i].getName());
		}
		setTitle("Super Tail");
		setSize(1000, 300);
		addWindowListener(new WindowAdapter()
		{  public void windowClosing(WindowEvent e)
		{  System.exit(0);
		}
		} );

	}
	
	///////////////////////////////////////////////////////////////////////////

	public void start()
	{
		for(Thread t : threads)
			t.start();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent evt)
   {  
		Object source = evt.getSource();
		Boolean wrap = null;
		
		if(source == wrapButton)
			wrap = true;
		else if (source == noWrapButton)
			wrap = false;
		
		if(wrap != null)
		{
			for(TextAppender app : appenders)
				app.wrap(wrap);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	private JPanel makeButtonPanel()
	{
		JPanel p = new JPanel();
		wrapButton = new JButton("Wrap");
		p.add(wrapButton);
		wrapButton.addActionListener(this);

		noWrapButton = new JButton("No wrap");
		p.add(noWrapButton);
		noWrapButton.addActionListener(this);
		
		return p;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private JPanel makeTailPanel()
	{
		JPanel panel = new JPanel();
		
		int num = appenders.length;
		
		if(num < 4)
			panel.setLayout(new GridLayout(num, 1));
		else
			panel.setLayout(new GridLayout(2, 2));
			
		
		for(int i = 0; i < appenders.length; i++)
		{
			LabeledJTextArea area = new LabeledJTextArea(files[i].getAbsolutePath());
			appenders[i] = area;
			panel.add(area);
		}
		//panel.add(new LabeledJTextArea("foobar"));
		//panel.add(new LabeledJTextArea("bolongo"));

		return panel;
		
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	  
	private Thread[]		threads;
	private File[]			files;
	private TextAppender[]	appenders;
	private JButton			noWrapButton;
	private JButton			wrapButton;
	
	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args)
	{
		if(args.length <= 0 || args.length > 4)
		{
			System.out.println("usage: fn1 fn2 ... fn4 ");
			System.exit(1);
		}
		
		ActiveTailGUI gui = new ActiveTailGUI(args);
		gui.setVisible(true);
		gui.start();
	}
	
	///////////////////////////////////////////////////////////////////////////


}
