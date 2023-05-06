/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart.playground;

/**
 *
 * @author bnevins
 */
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class Main {
  public static void main(String[] argv) throws Exception {
    JTable table = new JTable();
    TableColumn col = table.getColumnModel().getColumn(0);
    col.setCellEditor(new MyTableCellEditor());
  }
}

class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor {

  JComponent component = new JTextField();

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
      int rowIndex, int vColIndex) {

    ((JTextField) component).setText((String) value);

    return component;
  }

  public Object getCellEditorValue() {
    return ((JTextField) component).getText();
  }
}