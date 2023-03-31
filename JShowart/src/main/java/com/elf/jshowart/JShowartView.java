/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.elf.jshowart;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Rectangle;
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
    File imageFile;
    UserPreferences prefs = UserPreferences.get();

    /**
     * Creates new form JShowartView
     */
    public JShowartView() {
        
            //LayoutManager lm = getLayout();
        //Class c = lm.getClass();
    }

    // TODO set scroll size
    @Override
    public void paint(Graphics g) {
        if(image == null)
            return;
        
        Rectangle r = Utils.fitToWindow(new Dimension(getWidth(), getHeight()), new Dimension(image.getWidth(), image.getHeight()));
        g.drawImage(image, r.x, r.y, r.width, r.height, null);
        //g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, w, h, null);
    }

    void setImage(File f) {
        imageFile = f;
        
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(JShowartView.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO -- possible junk in window on initial draw.  This doesn't work -->invalidate();
        repaint();
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

   }
