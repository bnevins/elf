/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.elf.jshowart.playground;

import com.elf.jshowart.*;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
public class TestComboBox {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestComboBox cb = new TestComboBox();
        var box = new JComboBox(cb.ws);
        JFrame frame = new JFrame("CB Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Object o = box.getSelectedItem();
        System.out.println(o);
        JPanel panel = new JPanel();
        panel.add(box);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    whatever[] ws;
    
    public TestComboBox() {
        var w1 = new whatever("key1", "val1");
        var w2 = new whatever("key2", "val2");
        var w3 = new whatever("key3", "val3");
        
        ws = new whatever[] {w1,w2,w3};
    }
    
    class whatever {

        String key;
        String value;

        public whatever(String s1, String s2) {
            key = s1;
            value = s2;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
}
