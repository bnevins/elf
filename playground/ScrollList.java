/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart.playground;

import javax.swing.*;
import java.awt.*;

public class ScrollList extends JFrame {

    JScrollPane scrollpane;

    public ScrollList() {
        super("JScrollPane Demonstration");
        setSize(300, 200);
        String categories[] = {"Household", "Office", "Extended Family",
            "Company (US)", "Company (World)", "Team",
            "Will", "Birthday Card List", "High School",
            "Country", "Continent", "Planet"};
        JList list = new JList(categories);
        scrollpane = new JScrollPane(list);
        getContentPane().add(scrollpane, BorderLayout.CENTER);
    }

    public static void main(String args[]) {

        EventQueue.invokeLater(()
                -> {
            ScrollList sl = new ScrollList();
            sl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sl.setVisible(true);
        });
    }
}
