/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart.swinghacks;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.beans.*;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.*;
import javax.imageio.*;
import java.io.*;

public class ImagePreview extends JPanel implements PropertyChangeListener {
    private JFileChooser jfc;
    private Image img;
    private int width = 350;
    
    public ImagePreview(JFileChooser jfc) {
        this.jfc = jfc;
        Dimension sz = new Dimension(width,width);
        setPreferredSize(sz);
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            System.out.println("updating " + evt);
            File file = jfc.getSelectedFile();
            updateImage(file);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void updateImage(File file) throws IOException {
        if(file == null) {
            return;
        }
        
        img = ImageIO.read(file);
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        // fill the background
        g.setColor(Color.gray);
        g.fillRect(0,0,getWidth(),getHeight());
        
        if(img != null) {
            // calculate the scaling factor
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            int side = Math.max(w,h);
            double scale = (double)width/(double)side;
            w = (int)(scale * (double)w);
            h = (int)(scale * (double)h);
            
            // draw the image
            g.drawImage(img,0,0,w,h,null);
            
            // draw the image dimensions
            String dim = w + " x " + h;
            g.setColor(Color.black);
            g.drawString(dim,31,196);
            g.setColor(Color.white);
            g.drawString(dim,30,195);
            
        } else {
            
            // print a message
            g.setColor(Color.black);
            g.drawString("Not an image",30,100);
        }
    }
    
    
    
    public static void main(String[] args) {
        JFileChooser jfc = new JFileChooser("c:/Users/bnevins/Desktop/wallpaper");
        ImagePreview preview = new ImagePreview(jfc);
        jfc.addPropertyChangeListener(preview);
//        jfc.addWindowListener(new WindowAdapter() {
//         @Override
//            public void windowClosing(WindowEvent e) {
//                System.out.println("FileChooser window is closing.");   
//            }});
        jfc.setAccessory(preview);
        jfc.showOpenDialog(null);
    }
}

