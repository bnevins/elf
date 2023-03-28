/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.elf.JShowart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JScrollPane;

/**
 *
 * @author bnevins
 */
public class JShowartView extends JScrollPane {

    BufferedImage image;
    //String imageName = "Q:\\P\\_stills\\_sbest\\0007.jpg";
    String imageName = "C:/Users/bnevins/Desktop/wallpaper/HeartB_Jensen_3925.jpg";

    /**
     * Creates new form JShowartView
     */
    public JShowartView() {
        //initComponents();
        try {
            image = ImageIO.read(new File(imageName));
        } catch (IOException ex) {
            Logger.getLogger(JShowartView.class.getName()).log(Level.SEVERE, null, ex);
        }
            LayoutManager lm = getLayout();
        Class c = lm.getClass();
    }

    @Override
    public void paint(Graphics g) {
        int h = image.getHeight();
        int w = image.getWidth();

        //g.drawImage(image, 0, 0, null);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, w, h, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

   }
