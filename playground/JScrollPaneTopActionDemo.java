/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart.playground;

import java.awt.event.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

class JScrollPaneTopAction implements ActionListener {
  JScrollPane scrollPane;
  public JScrollPaneTopAction(JScrollPane scrollPane) {
    if (scrollPane == null) {
      throw new IllegalArgumentException(
        "JScrollPaneToTopAction: null JScrollPane");
    }
    this.scrollPane = scrollPane;
  }
  public void actionPerformed(ActionEvent actionEvent) {
    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
    JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
    verticalScrollBar.setValue(verticalScrollBar.getMinimum());
    horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
  }
}


public class JScrollPaneTopActionDemo {

  public static void main(String args[]) {
    JFrame frame = new JFrame("Tabbed Pane Sample");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel label = new JLabel("Label");
    label.setPreferredSize(new Dimension(1000, 1000));
    JScrollPane jScrollPane = new JScrollPane(label);

    JButton bn = new JButton("Move");
    
    bn.addActionListener(new JScrollPaneTopAction(jScrollPane));
    
    frame.add(bn, BorderLayout.SOUTH);
    frame.add(jScrollPane, BorderLayout.CENTER);
    frame.setSize(400, 150);
    frame.setVisible(true);
  }
}