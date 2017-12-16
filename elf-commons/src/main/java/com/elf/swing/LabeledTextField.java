package com.elf.swing;

import java.awt.*;
import javax.swing.*;

/** A TextField with an associated Label.
 *  <P>
 */

public class LabeledTextField extends Panel 
{
	public LabeledTextField(String labelString,	Font labelFont,	int textFieldSize,	Font textFont) 
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		label = new JLabel(labelString, Label.RIGHT);
		
		if (labelFont != null)
			label.setFont(labelFont);
		
		add(label);
		textField = new JTextField(textFieldSize);
		
		if (textFont != null)
			textField.setFont(textFont);
		
		add(textField);
	}

	public LabeledTextField(String labelString,	String textFieldString) 
	{
		this(labelString, null, textFieldString, textFieldString.length(), null);
	}

	public LabeledTextField(String labelString,	int textFieldSize) 
	{
		this(labelString, null, textFieldSize, null);
	}

	public LabeledTextField(String labelString,	Font labelFont,	String textFieldString,	int textFieldSize,	Font textFont) 
	{
		this(labelString, labelFont,
		textFieldSize, textFont);
		textField.setText(textFieldString);
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
	
	private JLabel label;
	private JTextField textField;

}
