/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author bnevins
 */
public class KeyDialog extends JDialog {

    private JPanel topPanel;
    private JButton addRowButton;
    private KeyDialogTablePanel keyTable;
    private JTextField rootDirField;


// for testing...
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new KeyDialog(null, true).setVisible(true);
            }
        });
    }
    private JButton rootDirButton;
    public KeyDialog(JFrame frame, boolean modal) {
        super(frame, modal);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        initComponents();
        initEvents();
        setTitle("Key Commands");
        pack();
        setLocationRelativeTo(frame);
        
        WindowAdapter adapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                keyTable.saveKeyCommands();
            }
        };

        // Add the WindowAdapter object to the frame.
        addWindowListener(adapter);
        
        
        
        setVisible(true);
        System.out.println("root button: " + rootDirButton.getBounds());
    }

    private void initComponents() {
        topPanel = new JPanel();
        addRowButton = new JButton("Add New Key Binding");
        //topPanel.setPreferredSize(new Dimension(500, 50));
        rootDirField = new JTextField(30);
        rootDirButton = new JButton("...");
        //rootDirButton.setPreferredSize(new Dimension(20, 15));
        topPanel.add(new JLabel("Root Folder:"));
        topPanel.add(rootDirField);
        topPanel.add(rootDirButton);
        topPanel.add(addRowButton);
        keyTable = new KeyDialogTablePanel();

        add(keyTable, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //setLocation(-3800, 275);
    }

    private void initEvents() {
        addRowButton.addActionListener(event -> keyTable.addRow());
    }
}
