/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart.playground;

import java.awt.*;
import javax.swing.*;

public class StatusBarExample {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Status Bar Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        JLabel label = new JLabel("This is a label.");
        contentPane.add(label, BorderLayout.CENTER);

        StatusBar statusBar = new StatusBar();
        statusBar.setMessage("This is the status bar.");
        contentPane.add(statusBar, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class StatusBar extends JPanel {

    private JLabel messageLabel;

    public StatusBar() {
        messageLabel = new JLabel();
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(messageLabel);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}