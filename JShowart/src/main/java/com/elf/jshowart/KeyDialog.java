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
public class KeyDialog extends JPanel {

    private final boolean DEBUG = true;
    private final int FILE_OPERATION_COLUMN = 0;
    private final int KEY_COLUMN = 4;
    private final String FILE_OPERATION_TYPES[] = { "Copy", "Move", "List", "Index", };
    private final String[] COLUMN_NAMES = {"Type", "Ctrl", "Shift", "Alt", "Key", "RelativeTo", "Target", };

    public KeyDialog() {
        super(new GridLayout(1, 0));

        JTable table = new JTable(new KeyCommandTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Set up column sizes.
        //initColumnSizes(table);
        //Fiddle with the Type column's cell editors/renderers.
        setUpTypeColumn(table, table.getColumnModel().getColumn(FILE_OPERATION_COLUMN));

        add(scrollPane);
    }

 
    public void setUpTypeColumn(JTable table, TableColumn typeColumn) {
        //Set up the editor for the Type cells.
        JComboBox comboBox = new JComboBox(FILE_OPERATION_TYPES);
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        typeColumn.setCellRenderer(renderer);
    }

    class KeyCommandTableModel extends AbstractTableModel {

        private Object[][] data = {
            {"Copy", "false","false","false", KeyEvent.VK_F, "Root", "_best"},
        };

        public final Object[] longValues = {"Jane", "Kathy",
            "None of the above",
            new Integer(20), Boolean.TRUE};

        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return COLUMN_NAMES[col];
        }

        public Object getValueAt(int row, int col) {
            if(col == KEY_COLUMN) {
                // Virtual Keys are just a number.  Return a String...
                Integer key = (Integer)data[row][col];
                return KeyEvent.getKeyText(key);
            }
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }


        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
//            if (col < 2) {
//                return false;
//            } else {
                return true;
//            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value
                        + " (an instance of "
                        + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        var newContentPane = new KeyDialog();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        frame.setLocation(-3800, 275);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
