/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.elf.jshowart;

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
        Globals.app = this;
    }

    public static void main(String args[]) {

        JShowartApp app = new JShowartApp();

        EventQueue.invokeLater(()
                -> {
            view = new JShowartView(); // this MUST be created before frame for setting up key handler?
            frame = new JShowartFrame();
            frame.setTitle("JShowArt");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.getContentPane().add(view);
            frame.setView(view);
            view.setVisible(true);
        });
    }
}
