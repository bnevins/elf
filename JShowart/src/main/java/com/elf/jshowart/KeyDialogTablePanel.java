/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author bnevins
 */
public class KeyDialogTablePanel extends JPanel {

    private UserPreferences prefs = UserPreferences.get();
    private final int FILE_OPERATION_COLUMN = 0;
    private final int KEY_COLUMN = 4;
    private final int RELATIVE_TO_COLUMN = 5;
    private final String FILE_OPERATION_TYPES[] = {"Copy", "Move", "List", "Index",};
    private final String RELATIVE_TO_ITEMS[] = {"Root", "Current File", "Absolute",};
    private final KeyCode KEY_CODES[] = KeyCode.getKeyCodes();

    private KeyCommandTableModel model;
    private KeyCommandTable table;

    public KeyDialogTablePanel() {
        super(new GridLayout(1, 0));
        initComponents();

    }

    public void setUpTypeColumn(JTable table, TableColumn typeColumn) {
        JComboBox comboBox = new JComboBox(FILE_OPERATION_TYPES);
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));
        typeColumn.setCellRenderer(getToolTipRenderer());
    }

    public void setUpRelativeToColumn(JTable table, TableColumn typeColumn) {
        var comboBox = new JComboBox(RELATIVE_TO_ITEMS);
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));
        typeColumn.setCellRenderer(getToolTipRenderer());
    }

    public void setUpKeyColumn(JTable table, TableColumn typeColumn) {
        var comboBox = new JComboBox(KEY_CODES);
        comboBox.addActionListener(event -> {
            int row = table.getSelectedRow();
            System.out.println("COMBO BOX ACTION!!!!! ROW = " + row);
        });
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));
        typeColumn.setCellRenderer(getToolTipRenderer());
    }

    private DefaultTableCellRenderer getToolTipRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        return renderer;
    }

    private void initColumnSizes(JTable table) {
        //var model = (KeyCommandTableModel) table.getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer
                = table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < longValues.length; i++) {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(
                    null, column.getHeaderValue(),
                    false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            Class<?> clazz = model.getColumnClass(i);
            Object longestValue = longValues[i];
            comp = table.getDefaultRenderer(clazz).getTableCellRendererComponent(table, longestValue, false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            if (prefs.isDebug()) {
                System.out.println("Initializing width of column "
                        + i + ". "
                        + "headerWidth = " + headerWidth
                        + "; cellWidth = " + cellWidth);
            }

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

// Temporary for testing
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Key Commands");
        System.out.println("Layout Manager: " + frame.getLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        var newContentPane = new KeyDialogTablePanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        frame.setLocation(-3800, 275);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    void addRow() {
        model.addRow();
    }

    void saveKeyCommands() {
        model.saveToPrefs();
    }

    private void initComponents() {
        model = new KeyCommandTableModel();
        table = new KeyCommandTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        //setupPopup(table);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Set up column sizes.
        initColumnSizes(table);
        //Fiddle with the Type column's cell editors/renderers.
        setUpTypeColumn(table, table.getColumnModel().getColumn(FILE_OPERATION_COLUMN));
        setUpRelativeToColumn(table, table.getColumnModel().getColumn(RELATIVE_TO_COLUMN));
        setUpKeyColumn(table, table.getColumnModel().getColumn(KEY_COLUMN));
        add(scrollPane);
    }

    ListSelectionModel getSelectionModel() {
        return table.getSelectionModel();
    }

    int[] getSelectedRows() {
        return table.getSelectedRows();
    }

    void deleteSelectedRows() {
        table.deleteSelectedRows();
    }
}
