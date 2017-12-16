package com.elf.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** 
 * A TextField with an associated Label and 2 buttons.
 *  <P>
 */

public class LabeledTextFieldWith2Buttons extends JPanel 
{
	public LabeledTextFieldWith2Buttons(String labelString, String button1String, 
			String button2String, int textFieldSize, ActionListener listener) 
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel(labelString, Label.RIGHT);
		add(label);

		textField = new JTextField(textFieldSize);
		add(textField);
		
		button1 = new JButton(button1String);
		button1.addActionListener(listener);

		add(button1);
		
		button2 = new JButton(button2String);
		button2.addActionListener(listener);
		
		add(button2);
	}

	/** The Label at the left side of the LabeledTextField.
	*  To manipulate the Label, do:
	*  <PRE>
	*    LabeledTextField ltf = new LabeledTextField(...);
	*    ltf.getLabel().someLabelMethod(...);
	*  </PRE>
	*
	* @see #getTextField
	*/

	public JLabel getLabel() 
	{
		return(label);
	}

	/** The TextField at the right side of the
	*  LabeledTextField.
	*
	* @see #getLabel
	*/

	public JTextField getTextField() 
	{
		return(textField);
	}
	
	/** The TextField at the right side of the
	*  LabeledTextField.
	*
	* @see #getLabel
	*/

	public String getText() 
	{
		return(textField.getText());
	}

	/** 
	* The FirstButton at the right side of the TextField.
	* @see #getLabel
	*/

	public JButton getButton1() 
	{
		return(button1);
	}

	/** 
	* The Second Button at the right side of the TextField.
	* @see #getLabel
	*/

	public JButton getButton2() 
	{
		return(button2);
	}
	
	private	JLabel		label;
	private JTextField	textField;
	private JButton		button1;
	private JButton		button2;

	public static void main(String[] notUsed)
	{
		CloseableFrame cfr = new CloseableFrame("LabeledTextFieldWith2Buttons Tester");
		Container cf = cfr.getContentPane();
		cf.setLayout(new GridLayout(4,1));
		LabeledTextFieldWith2Buttons a1 = new LabeledTextFieldWith2Buttons("Label here:", "Button1", "Button2", 20, null);
		LabeledTextFieldWith2Buttons a2 = new LabeledTextFieldWith2Buttons("Label 2 here:", "Button1", "Button2", 10, null);
		LabeledTextFieldWith2Buttons a3 = new LabeledTextFieldWith2Buttons("Label 3 here:", "Button1", "Button2", 30, null);
		LabeledTextFieldWith2Buttons a4 = new LabeledTextFieldWith2Buttons("Label 4 here:", "Button1", "Button2", 60, null);
		cf.add(a1);
		cf.add(a2);
		cf.add(a3);
		cf.add(a4);
		cfr.setSize(600, 700);
		cfr.setVisible(true);
	}

}
