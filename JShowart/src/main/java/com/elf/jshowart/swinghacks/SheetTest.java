package com.elf.jshowart.swinghacks;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

public class SheetTest extends Object 
    implements PropertyChangeListener {

    JOptionPane optionPane;
    AniSheetableJFrame frame;

    public static void main (String[] args) {
        new SheetTest();
    }

    public SheetTest () {
        frame = new AniSheetableJFrame ("Animated sheet test");
        // put an image in the frame's content pane
        ImageIcon icon = new ImageIcon ("E:/dev/elf/JShowart/src/main/java/com/elf/jshowart/swinghacks/keagy-lunch.png");
        JLabel label = new JLabel (icon);
        frame.getContentPane().add(label);
        // build JOptionPane dialog and hold onto it
        optionPane = new JOptionPane ("Do you want to save?",
                                      JOptionPane.QUESTION_MESSAGE,
                                      JOptionPane.YES_NO_OPTION);
        frame.pack();
        frame.setVisible(true);
        optionPane.addPropertyChangeListener (this);
        // pause for effect, then show the sheet
        try {Thread.sleep(1000);}
        catch (InterruptedException ie) {}
        JDialog dialog = 
            optionPane.createDialog (frame, "irrelevant");
        frame.showJDialogAsSheet (dialog);

    }

    public void propertyChange (PropertyChangeEvent event) {
        if (event.getPropertyName().equals (JOptionPane.VALUE_PROPERTY)) {
            System.out.println ("Selected option " + 
                                event.getNewValue());
            frame.hideSheet();
        }
    }

}
