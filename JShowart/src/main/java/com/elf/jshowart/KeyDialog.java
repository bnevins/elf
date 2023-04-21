/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 *
 * @author bnevins
 */
public class KeyDialog extends JPanel {

    private final boolean DEBUG = true;
    private final int TYPE_COLUMN_NUMBER = 0;
    private final String TYPES[] = { "Copy", "Move", "List", "Index", };

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
        setUpTypeColumn(table, table.getColumnModel().getColumn(TYPE_COLUMN_NUMBER));

        add(scrollPane);
    }

 
    public void setUpTypeColumn(JTable table, TableColumn typeColumn) {
        //Set up the editor for the Type cells.
        JComboBox comboBox = new JComboBox(TYPES);
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer
                = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        typeColumn.setCellRenderer(renderer);
    }

    class KeyCommandTableModel extends AbstractTableModel {

        private String[] columnNames = {"First Name",
            "Last Name",
            "Type",
            "# of Years",
            "Vegetarian"};
        private Object[][] data = {
            {"Copy","Kathy", "Smith",
                 new Integer(5), new Boolean(false)},
            {"Move","John", "Doe",
                 new Integer(3), new Boolean(true)},
            {"List","Sue", "Black",
                 new Integer(2), new Boolean(false)},
            {"Index","Jane", "White",
                 new Integer(20), new Boolean(true)},
            {"Copy","Joe", "Brown",
                 new Integer(10), new Boolean(false)}
        };

        public final Object[] longValues = {"Jane", "Kathy",
            "None of the above",
            new Integer(20), Boolean.TRUE};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
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
