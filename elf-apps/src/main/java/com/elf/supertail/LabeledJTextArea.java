/*
 * LabeledJTextArea.java
 *
 * Created on May 22, 2005, 3:37 PM
 */

package com.elf.supertail;

import com.elf.interfaces.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
public class LabeledJTextArea extends JPanel implements TextAppender
{
	public LabeledJTextArea(String name)
	{
		setLayout(new BorderLayout());
		add(new JLabel(name), "North");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		add(scroll, "Center");
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void append(String s)
	{
		area.append(s);
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void wrap(boolean what)
	{
		area.setLineWrap(what);
         scroll.validate();
	}
	
	///////////////////////////////////////////////////////////////////////////

	private JTextArea	area;
	private	JScrollPane	scroll;
}
