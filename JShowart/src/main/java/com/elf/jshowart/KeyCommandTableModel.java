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
    private ArrayList<KeyCommandItem> items = new ArrayList<>();
    final Object[] longValues = {"Index", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "W", "Current File", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"};

    KeyCommandTableModel() {
        items.add(new KeyCommandItem("Index", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, "W", "Root", "_best"));
        items.add(new KeyCommandItem("Index", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, "F", "Root", "_best"));
        items.add(new KeyCommandItem("Index", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, "X", "Root", "_best"));
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
            System.out.println(items.get(row).getPrintString());
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    class KeyCommandItem {
        //    private final String[] COLUMN_NAMES = {"Type", "Ctrl", "Shift", "Alt", "Key", "Relative To", "Target"};

        String type;
        boolean ctrl;
        boolean shift;
        boolean alt;
        String key;
        String relativeTo;
        String target;

        public KeyCommandItem(String type, boolean ctrl, boolean shift, boolean alt, String key, String relativeTo, String target) {
            this.type = type;
            this.ctrl = ctrl;
            this.shift = shift;
            this.alt = alt;
            this.key = key;
            this.relativeTo = relativeTo;
            this.target = target;
        }

        private Object getColumn(int col) {
            return switch(col) {
                case 0: yield type;
                case 1: yield ctrl;
                case 2 : yield shift;
                case 3 : yield alt;
                case 4 : yield key;
                case 5 : yield relativeTo;
                case 6: yield target;
                default: throw new IllegalStateException("Illegal column number: " + col);
            };
        }

        private void setColumn(int col, Object value) {
            switch(col) {
                case 0 -> type = (String) value;
                case 1 -> ctrl = (Boolean) value;
                case 2 -> shift = (Boolean) value;
                case 3 -> alt = (Boolean) value;
                case 4 -> key = (String) value;
                case 5 -> relativeTo = (String) value;
                case 6 -> target = (String) value;
                default -> throw new IllegalStateException("Illegal column number: " + col);
            }
        }
        private String getPrintString() {
            //return String.format("%10s", longValues)
            return "    " + type + "  " + ctrl + "  " + shift + "  " + alt + "   " + key + "  " + relativeTo + "   " + target;
        }
    }
}
