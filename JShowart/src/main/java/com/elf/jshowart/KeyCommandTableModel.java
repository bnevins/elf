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
    final Object[] longValues = {"Index", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "Page Down", "Current File", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"};

    KeyCommandTableModel() {
        // populate from saved preferences...
        ArrayList<String> keyCommands = prefs.getKeyCommands();

        if (keyCommands.size() <= 0) {
            items.add(new KeyCommandItem("Index", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, "W", "Root", "_best"));

        } else {
            for (String keyCommand : keyCommands) {
                items.add(new KeyCommandItem(keyCommand));
            }
        }

        //items.add(new KeyCommandItem("Index", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, "F", "Root", "_best"));
        //items.add(new KeyCommandItem("Index", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, "X", "Root", "_best"));
    }

    public void saveToPrefs() {
        ArrayList<String> keyCommands = prefs.getKeyCommands();
        keyCommands.clear();
        for (KeyCommandItem item : items) {
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
        items.add(new KeyCommandItem());
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
        char USER_PREFS_DELIMITER = ':';

        public KeyCommandItem() {
            this("Copy", false, false, false, "P", "Root", "foof");
        }

        public KeyCommandItem(String userPrefsString) {
            String[] ss = userPrefsString.split(":");

            if (ss.length != 7)
                throw new IllegalArgumentException("Bad User Prefs Key Command String: " + userPrefsString);

            type = ss[0];
            ctrl = Boolean.valueOf(ss[1]);
            shift = Boolean.valueOf(ss[2]);
            alt = Boolean.valueOf(ss[3]);
            key = ss[4];
            relativeTo = ss[5];
            target = ss[6];
        }

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
            return switch (col) {
                case 0:
                    yield type;
                case 1:
                    yield ctrl;
                case 2:
                    yield shift;
                case 3:
                    yield alt;
                case 4:
                    yield key;
                case 5:
                    yield relativeTo;
                case 6:
                    yield target;
                default:
                    throw new IllegalStateException("Illegal column number: " + col);
            };
        }

        private void setColumn(int col, Object value) {
            switch (col) {
                case 0 ->
                    type = (String) value;
                case 1 ->
                    ctrl = (Boolean) value;
                case 2 ->
                    shift = (Boolean) value;
                case 3 ->
                    alt = (Boolean) value;
                case 4 ->
                    key = value.toString();
                case 5 ->
                    relativeTo = (String) value;
                case 6 ->
                    target = (String) value;
                default ->
                    throw new IllegalStateException("Illegal column number: " + col);
            }
        }

        private String getPrintString() {
            //return String.format("%10s", longValues)
            return "    " + type + "  " + ctrl + "  " + shift + "  " + alt + "   " + key + "  " + relativeTo + "   " + target;
        }

        private String createUserPrefsString() {
            // to save to User Preferences
            StringBuilder sb = new StringBuilder();
            sb.append(type).append(USER_PREFS_DELIMITER);
            sb.append(ctrl).append(USER_PREFS_DELIMITER);
            sb.append(shift).append(USER_PREFS_DELIMITER);
            sb.append(alt).append(USER_PREFS_DELIMITER);
            sb.append(key).append(USER_PREFS_DELIMITER);
            sb.append(relativeTo).append(USER_PREFS_DELIMITER);
            sb.append(target);
            return sb.toString();
        }

    }
}
