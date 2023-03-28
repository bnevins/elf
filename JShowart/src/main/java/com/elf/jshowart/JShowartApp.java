/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.elf.JShowart;

import java.awt.*;
//import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author bnevins
 */
public class JShowartApp {

    private static JShowartFrame frame;
    private static JShowartView view;

    public JShowartApp() {

    }

    public static void main(String args[]) {

        JShowartApp app = new JShowartApp();

        EventQueue.invokeLater(()
                -> {
            frame = new JShowartFrame();
            frame.setTitle("JShowArt");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            view = new JShowartView();
            frame.getContentPane().add(view);
            frame.setView(view);
            view.setVisible(true);
        });
    }
}
