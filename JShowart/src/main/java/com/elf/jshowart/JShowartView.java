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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class JShowartView extends JScrollPane implements KeyListener {

    private BufferedImage image;
    private File prevImageFile = null;
    private UserPreferences prefs = UserPreferences.get();
    private ArtLib artlib = ArtLib.get();

    /**
     * Creates new form JShowartView
     */
    public JShowartView() {
        System.out.println("Layout Manager = " + getLayout());
        Globals.view = this;
    }

    public void keyTyped(KeyEvent e) {
        //System.out.println("KEY TYPED" + e); 
        if (e.getKeyChar() == ' ') {
            System.out.println("SPACE PRESSED!!!!");
            nextImage();
        }
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println("KEY PRESSED:  " + e); 

    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("KEY RELEASED:  " + e); 
    }

    // TODO set scroll size
    @Override
    public void paint(Graphics g) {
        if(image == null)
            return;
        super.paint(g);
        Rectangle r = Utils.fitToWindow(new Dimension(getWidth(), getHeight()), new Dimension(image.getWidth(), image.getHeight()));
        g.drawImage(image, r.x, r.y, r.width, r.height, null);
        //g.drawImage(image, 0, 0, getWidth(), getHeight(), 0, 0, w, h, null);
    }

    void nextImage() {
        File imageFile = artlib.next();
        
        if(imageFile == null) {
            image = null;
            return;
        }

        // if we have only 1 file in artlib, don't waste time re-reading it
        if(imageFile.equals(prevImageFile))
            return;
        
        prevImageFile = imageFile;

        try {
            image = ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(JShowartView.class.getName()).log(Level.SEVERE, null, ex);
            image = null;
        }
        repaint();
        
        // TODO -- possible junk in window on initial draw.  This doesn't work -->invalidate();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    void imagesReplaced() {
        // files were just opened
        image = null;
        prevImageFile = null;
        nextImage();
    }

}
