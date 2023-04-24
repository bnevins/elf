/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author bnevins
 */
public class KeyDialog extends JDialog {

    private JPanel topPanel;
    private JButton addRowButton;
    private KeyDialogTable keyTable;

    // for testing...
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new KeyDialog(null, true).setVisible(true);
            }
        });
    }

    public KeyDialog(JFrame frame, boolean modal) {
        super(frame, modal);
        initComponents();
        initEvents();
        setTitle("Key Commands");
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void initComponents() {
        topPanel = new JPanel();
        addRowButton = new JButton("Add New Key Binding");
        topPanel.setPreferredSize(new Dimension(500, 50));
        topPanel.add(addRowButton);
        keyTable = new KeyDialogTable();

        add(keyTable, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //setLocation(-3800, 275);
    }

    private void initEvents() {
        addRowButton.addActionListener(event -> keyTable.addRow());
    }
}
