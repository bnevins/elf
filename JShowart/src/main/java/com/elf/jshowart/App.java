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
public class App {

    private static Controller frame;
    private static View view;

    public App() {
        Globals.app = this;
    }

    public static void main(String args[]) {

        App app = new App();
       

        EventQueue.invokeLater(()
                -> {
            view = new View(); // this MUST be created before frame for setting up key handler?
            view.setBackground(Color.black);
            frame = new Controller();
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
