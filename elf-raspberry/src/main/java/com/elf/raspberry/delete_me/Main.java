/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry.delete_me;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author bnevns
 */
public class Main extends JFrame {
    // this line is needed to avoid serialization warnings  
    private static final long serialVersionUID = 1L;
 
    Image screenImage; // downloaded image  
    int w, h; // Display height and width 
    Dimension newSize;
 
    // Program entry 
    public static void main(String[] args) throws Exception { 
        if (args.length < 1) // by default program will load AnyExample logo 
            new Main("http://www.anyexample.com/i/logo.gif"); 
        else 
            new Main(args[0]); // or first command-line argument 
    } 
 
    // Class constructor  
    Main(String source) throws MalformedURLException { 
 
        // Exiting program on window close 
        addWindowListener(new WindowAdapter() { 
            public void windowClosing(WindowEvent e) { 
                System.exit(0);
            } 
        });
 
        // Exitig program on mouse click 
//        addMouseListener(new MouseListener() { 
//            public void mouseClicked(MouseEvent e) { System.exit(0); } 
//            public void mousePressed(MouseEvent e) {} 
//            public void mouseReleased(MouseEvent e) {} 
//            public void mouseEntered(MouseEvent e) {} 
//            public void mouseExited(MouseEvent e) {} 
//        } 
//        );
 
        // remove window frame  
        this.setUndecorated(true);
 
        // window should be visible 
        this.setVisible(true);
 
        // switching to fullscreen mode 
        GraphicsEnvironment.getLocalGraphicsEnvironment().
        getDefaultScreenDevice().setFullScreenWindow(this);
 
        // getting display resolution: width and height 
        w = this.getWidth();
        h = this.getHeight();
        System.out.println("Display resolution: " + String.valueOf(w) + "x" + String.valueOf(h));
 
        // loading image  
        if (source.startsWith("http://")) // http:// URL was specified 
            screenImage = Toolkit.getDefaultToolkit().getImage(new URL(source));
        else 
            screenImage = Toolkit.getDefaultToolkit().getImage(source); // otherwise - file 
        
        //newSize = ImageUtils.scaleImage(screenImage.getWidth(this), screenImage.getHeight(this), getWidth(), getHeight());
        System.out.println("After processing: " + newSize.width + "X" + newSize.height);
    } 
 
    public void paint (Graphics g) { 
        //System.out.println("screenImage: " + screenImage);
        if (screenImage != null && newSize != null) // if screenImage is not null (image loaded and ready) 
            g.drawImage(screenImage, // draw it  
                        0, //    w/2 - screenImage.getWidth(this) / 2, // at the center  
                        0, // h/2 - screenImage.getHeight(this) / 2, // of screen 
                        newSize.width,
                        newSize.height,
                        this);
            // to draw image at the center of screen 
            // we calculate X position as a half of screen width minus half of image width 
            // Y position as a half of screen height minus half of image height 
          
    } 
 
 public void paint2(Graphics g) {
     // Draw the scaled image
//        BufferedImage newImage = new BufferedImage(newWidth, newHeight,
//                imageType);
//        Graphics2D graphics2D = newImage.createGraphics();
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);

 }
} 
 

