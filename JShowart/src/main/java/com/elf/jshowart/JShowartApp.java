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

    public JShowartApp(int numBuffers, GraphicsDevice gd) {


    }

    public static void main(String args[]) {
        
        EventQueue.invokeLater(() ->
         {
            JFrame frame = new JShowartFrame();
            frame.setTitle("JShowArt");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
    }
}
