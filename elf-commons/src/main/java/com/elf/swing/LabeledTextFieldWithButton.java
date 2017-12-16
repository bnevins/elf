package com.elf.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** A TextField with an associated Label.
 *  <P>
 */

public class LabeledTextFieldWithButton extends JPanel 
{
	public LabeledTextFieldWithButton(String labelString, String buttonString, int textFieldSize, ActionListener listener) 
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel(labelString, Label.RIGHT);
		add(label);

		textField = new JTextField(textFieldSize);
		add(textField);
		
		button = new JButton(buttonString);
		button.addActionListener(listener);
		add(button);
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
	* The Button at the right side of the TextField.
	* @see #getLabel
	*/

	public JButton getButton() 
	{
		return(button);
	}
	
	private	JLabel		label;
	private JTextField	textField;
	private JButton		button;

	public static void main(String[] notUsed)
	{
		CloseableFrame cfr = new CloseableFrame("LabeledTextFieldWithButton Tester");
		Container cf = cfr.getContentPane();
		cf.setLayout(new GridLayout(4,1));
		LabeledTextFieldWithButton a1 = new LabeledTextFieldWithButton("Label here:", "Button", 20, null);
		LabeledTextFieldWithButton a2 = new LabeledTextFieldWithButton("Label 2 here:", "Button", 10, null);
		LabeledTextFieldWithButton a3 = new LabeledTextFieldWithButton("Label 3 here:", "Button", 30, null);
		LabeledTextFieldWithButton a4 = new LabeledTextFieldWithButton("Label 4 here:", "Button", 60, null);
		cf.add(a1);
		cf.add(a2);
		cf.add(a3);
		cf.add(a4);
		cfr.setSize(600, 700);
		cfr.setVisible(true);
	}

}
