/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.elf.jshowart;

import java.awt.*;
//import java.awt.image.BufferStrategy;
import javax.swing.*;

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
        /**
         *  Metal -- sucks
            Nimbus -- nice!
            CDE/Motif - very different
            Windows -- NICE!
            Windows Classic
         */
         try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(JShowartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(()
                -> {
            view = new JShowartView(); // this MUST be created before frame for setting up key handler?
            view.setBackground(Color.black);
            frame = new JShowartFrame();
            frame.setTitle("JShowArt");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            System.out.println("content pane == " + frame.getContentPane());
            JScrollPane jsp = new JScrollPane(view);
            view.setContainer(jsp);
            System.out.println("Frame Original Layout Manager:  " + frame.getLayout());
            frame.setLayout(new BorderLayout());
            frame.add(jsp, BorderLayout.CENTER);
            frame.setView(view);
            view.setVisible(true);
        });
    }
}
