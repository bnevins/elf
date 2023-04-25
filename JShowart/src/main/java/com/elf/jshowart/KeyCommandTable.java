/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
public class KeyCommandTable extends JTable implements MouseListener, ActionListener {

    private final KeyCommandTableModel model;
    private JPopupMenu thePopupMenu;
    private int currentPopupRow = -1;

    public KeyCommandTable(KeyCommandTableModel theModel) {
        super(theModel);
        model = theModel;
        setupPopup();
    }

    private void setupPopup() {
        thePopupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Delete Row");
        thePopupMenu.add(deleteMenuItem);
        addMouseListener(this);
        deleteMenuItem.addActionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            currentPopupRow = rowAtPoint(e.getPoint());

            if (currentPopupRow >= 0) {
                thePopupMenu.show(this, e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("DELETE SELECTED for row# " + currentPopupRow);
        model.deleteRow(currentPopupRow);
        invalidate();
    }
}
