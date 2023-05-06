/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 *
 * @author bnevins TODO add cancel capability and button
 */
public class KeyDialog extends JDialog {

    private JPanel topPanel;
    private JPanel bottomPanel;
    private JButton addRowButton;
    private KeyDialogTablePanel keyTablePanel;
    private JTextField rootDirField;
    private JButton rootDirButton;
    private JButton deleteSelectedRowsButton;
    private JButton okButton;

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
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        initComponents();
        initEvents();
        setTitle("Key Commands");
        pack();
        setLocationRelativeTo(frame);

        setVisible(true);
        System.out.println("root button: " + rootDirButton.getBounds());
    }

    private void initComponents() {
        // panels
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        keyTablePanel = new KeyDialogTablePanel();
        
        // buttons
        addRowButton = new JButton("Add");
        deleteSelectedRowsButton = new JButton("Delete Rows");
        deleteSelectedRowsButton.setEnabled(false);
        okButton = new JButton("OK");
        rootDirButton = new JButton("...");
        
        // textfields
        rootDirField = new JTextField(30);
        
        // add items
        topPanel.add(new JLabel("Root Folder:"));
        topPanel.add(rootDirField);
        topPanel.add(rootDirButton);
        topPanel.add(addRowButton);
        topPanel.add(deleteSelectedRowsButton);
        bottomPanel.add(okButton);
        this.add(keyTablePanel, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // misc
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initEvents() {
        addRowButton.addActionListener(event -> keyTablePanel.addRow());
        deleteSelectedRowsButton.addActionListener(event -> keyTablePanel.deleteSelectedRows());
        okButton.addActionListener(event -> 
        {
            // dispose does NOT cause windowClosing beloe to fire.  TODO make this more elegant
            keyTablePanel.saveKeyCommands(); 
            dispose();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                keyTablePanel.saveKeyCommands();
            }
        });
        keyTablePanel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] selectedRows = keyTablePanel.getSelectedRows();

                if (selectedRows.length <= 0)
                    deleteSelectedRowsButton.setEnabled(false);
                else
                    deleteSelectedRowsButton.setEnabled(true);
                //for (int i = 0; i < selectedRows.length; i++) {
                //System.out.println("Selected row: " + selectedRows[i]);
            }
        });
    }
}
