/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.JShowart;

import javax.swing.*;
import java.awt.BorderLayout;

/**
 *
 * @author bnevins
 */
public class AboutDialog extends JDialog{
   public AboutDialog(JFrame owner)
   {
      super(owner, "About JShowArt", true);

      // add HTML label to center

      add(
         new JLabel(
            "<html><h1><i>Core Java</i></h1><hr>By Cay Horstmann</html>"),
         BorderLayout.CENTER);

      // OK button closes the dialog

      JButton ok = new JButton("OK");
      ok.addActionListener(event -> setVisible(false));

      // add OK button to southern border

      JPanel panel = new JPanel();
      panel.add(ok);
      add(panel, BorderLayout.SOUTH);

      pack();
   }
}
