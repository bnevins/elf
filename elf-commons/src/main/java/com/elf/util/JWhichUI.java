/*
 * JWhichUI.java
 *
 * Created on December 2, 2000, 3:09 PM
 */

package com.elf.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 *
 * @author  administrator
 * @version 
 */

class JWhichUI extends JFrame implements ActionListener
{  
	public JWhichUI()
	{  
		addButtonPanel();
		addTextPanel();

		setTitle(title);
		setSize(900, 300);

		addWindowListener(new WindowAdapter()
		{  
			public void windowClosing(WindowEvent e)
			{  
				System.exit(0);
			}
		} );

		setVisible(true);
	}

	////////////////////////////////////////////////////////
	
	public void pr(String s)
	{
		//textArea.setText(textArea.getText() + s + "\n");//NOI18N
		textArea.append(s + "\n");//NOI18N
	}

	//////////////////////////////////////////////////////////////
	
	private void addButtonPanel()
	{
		JPanel panel = new JPanel();
		searchButton = new JButton("Search");//NOI18N
		panel.add(searchButton);
		searchButton.addActionListener(this);
		Dimension d = searchButton.getPreferredSize();
		d.setSize(450, d.getHeight());
		searchString = new JTextField();
		searchString.setPreferredSize(d);
		panel.add(searchString);
		searchString.addActionListener(this);
		
		getContentPane().add(panel, "South");//NOI18N
	}
	
	//////////////////////////////////////////////////////////////
	
	private void addTextPanel()
	{
		textArea	= new JTextArea(800, 50);
		scrollPane	= new JScrollPane(textArea);
		getContentPane().add(scrollPane, "Center");//NOI18N
	}
	
	//////////////////////////////////////////////////////////////
	
	public void actionPerformed(ActionEvent evt)
	{  
		Object source = evt.getSource();
	
		if(source == searchButton)
		{
			String what = searchString.getText();
			
			if(what == null || what.length() <= 0)
				return;
			
			JWhich jw = new JWhich(what);
			pr(jw.getResult());
		}
	}

	//////////////////////////////////////////////////////////////

	public static void main(String[] args)
	{
		JWhichUI jwui = new JWhichUI();
		jwui.setVisible(true);
	}
	
	//////////////////////////////////////////////////////////////

	private					JButton		searchButton;   
	private					JTextField	searchString;
	private					JTextArea	textArea;
	private					JScrollPane	scrollPane;
	private final static	String		title		= "JWhich -- Class Finder";//NOI18N
}

