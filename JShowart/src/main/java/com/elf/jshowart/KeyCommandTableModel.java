/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;

/**
 *
 * @author bnevins characters accepted for Key --> 0-9 == 48-57, A-Z == 65-90
 */
class KeyCommandTableModel extends AbstractTableModel {

    private UserPreferences prefs = UserPreferences.get();
    private final String[] COLUMN_NAMES = {"Type", "Ctrl", "Shift", "Alt", "Key", "Relative To", "Target"};
    // static final int KEY_COLUMN = 4;
    private ArrayList<KeyCommand> items = new ArrayList<>();
    final Object[] longValues = {"Index", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "Page Down", "Current File", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"};

    KeyCommandTableModel() {
        // populate from saved preferences...
        ArrayList<String> keyCommands = prefs.getKeyCommands();

        if (keyCommands.size() <= 0) {
            items.add(new KeyCommand("Index", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, "W", "Root", "_best"));

        } else {
            for (String keyCommand : keyCommands) {
                items.add(new KeyCommand(keyCommand));
            }
        }
    }

    public void saveToPrefs() {
        ArrayList<String> keyCommands = prefs.getKeyCommands();
        keyCommands.clear();
        for (KeyCommand item : items) {
            keyCommands.add(item.createUserPrefsString());
        }
        // persist NOW!
        prefs.write();
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public int getRowCount() {
        return items.size();
    }

    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    public Object getValueAt(int row, int col) {
        return items.get(row).getColumn(col);
    }

    public void addRow() {
        items.add(new KeyCommand("Copy", false, false, false, "P", "Root", "foof"));
        int newRowNum = items.size() - 1;
        fireTableRowsInserted(newRowNum, newRowNum);
    }

    public void deleteRow(int row) {
        fireTableRowsDeleted(row, row);
        items.remove(row);
    }
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the boolean columns would contain text ("true"/"false"),
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
        if (prefs.isDebug()) {
            System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of " + value.getClass() + ")");
        }
        items.get(row).setColumn(col, value);
        fireTableCellUpdated(row, col);
        if (prefs.isDebug()) {
            System.out.println("New value of data:");
            printDebugData();
        }
    }

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();
        for (int row = 0; row < numRows; row++) {
            System.out.print("    row " + row + ":");
            System.out.println(items.get(row).toString());
            System.out.println();
        }
        System.out.println("--------------------------");
    }
}
