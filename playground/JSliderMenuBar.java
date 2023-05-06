/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart.playground;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class JSliderMenuBar {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("JSlider MenuBar");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Create the menu bar
                JMenuBar menuBar = new JMenuBar();

                // Create the menu
                JMenu menu = new JMenu("Options");
                menuBar.add(menu);

                // Create the slider
                JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
                slider.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        // Get the current value of the slider
                        int value = slider.getValue();

                        // Update the text label
                        JLabel label = (JLabel) menu.getMenuComponent(0);
                        label.setText("Value: " + value);
                    }
                });

                // Add the slider to the menu
                menu.add(slider);

                // Add the menu bar to the frame
                frame.setJMenuBar(menuBar);

                // Pack the frame and make it visible
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}