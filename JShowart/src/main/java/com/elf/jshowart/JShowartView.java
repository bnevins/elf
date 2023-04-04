/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.logging.*;
import javax.imageio.*;
import javax.swing.*;

/**
 *
 * @author bnevins
 */
public class JShowartView extends JPanel implements KeyListener {

    private BufferedImage image;
    private File prevImageFile = null;
    private UserPreferences prefs = UserPreferences.get();
    private ArtLib artlib = ArtLib.get();
    private JScrollPane parentPane;
    private Dimension preferredSize;

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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null)
            return;
        System.out.println("View width = " + getWidth());
        System.out.println("Scrollpane width = " + parentPane.getWidth());
        System.out.println("Viewport width = " + parentPane.getViewport().getWidth());
        System.out.println("");

        if (prefs.fitToWindow) {
            Rectangle r = Utils.fitToWindow(new Dimension(parentPane.getViewport().getWidth(), parentPane.getViewport().getHeight()), new Dimension(image.getWidth(), image.getHeight()));
            System.out.println("Image Rectangle = " +r);
            System.out.println("Image width, height = " + image.getWidth() + ", " + image.getHeight());
            setBounds(r.getBounds());
            preferredSize=new Dimension(r.width, r.height);
            g.drawImage(image, r.x, r.y, r.width, r.height, null);
        } else {
            g.drawImage(image, 0, 0, null);
            preferredSize = new Dimension(image.getWidth(), image.getHeight());
        }
    }

    void nextImage() {
        File imageFile = artlib.next();

        if (imageFile == null) {
            image = null;
            return;
        }

        // if we have only 1 file in artlib, don't waste time re-reading it
        if (imageFile.equals(prevImageFile))
            return;

        prevImageFile = imageFile;

        try {
            image = ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(JShowartView.class.getName()).log(Level.SEVERE, null, ex);
            image = null;
        }

        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        //invalidate();
        resetScrollbars();
        parentPane.revalidate();
        repaint();
        // TODO -- possible junk in window on initial draw.  This doesn't work -->invalidate();
    }

    @Override
    public Dimension getPreferredSize() {
        if(preferredSize == null)
            return super.getPreferredSize();
        else
            return preferredSize;
    }
    void imagesReplaced() {
        // files were just opened
        image = null;
        prevImageFile = null;
        nextImage();
    }

    void setContainer(JScrollPane jsp) {
        parentPane = jsp;
    }

    private void resetScrollbars() {
        JScrollBar verticalScrollBar = parentPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = parentPane.getHorizontalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
        horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
    }

}
