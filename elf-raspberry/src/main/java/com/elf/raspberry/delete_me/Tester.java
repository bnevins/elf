/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry.delete_me;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author bnevins
 */
public class Tester {

    private JFrame jf;
public static void main(String[] args) {
    new Tester();
}
    public Tester() {

        //jframe
        jf = new JFrame();
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setUndecorated(true);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(jf);

        jf.add(new JLabel(new ImageIcon("E:/NetBeansProjects/Project/res/Test.png")));

    }

}    

