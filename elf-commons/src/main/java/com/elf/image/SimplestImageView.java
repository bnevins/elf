/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.image;

import java.io.File;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
 *
 * @author bnevns
 */
public class SimplestImageView extends Frame {

    private static File imageFile = new File("C:\\tmp\\Aug09\\2020_08_10_013110.jpg");
    private static File imageFile2 = new File("C:\\tmp\\aug09\\2020_08_09_141700.jpg");

    /**
     * @param args the command line arguments
     */
    public SimplestImageView() {
        try {
            BufferedImage img = ImageIO.read(imageFile);
            BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.getGraphics();
            g.drawImage(img, 0, 0, null);
        } catch (IOException e) {
            System.out.println("Image could not be read");
//            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SimplestImageView viewer = new SimplestImageView();
        viewer.setVisible(true);
//        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice device = env.getDefaultScreenDevice();
//        GraphicsConfiguration gc = device.getDefaultConfiguration();
//        Frame f = new Frame(gc);
//        Rectangle bounds = gc.getBounds();
//        BufferedImage image = ImageIO.read(imageFile);
//        g.drawImage(image, 0, 0, bounds.width, bounds.height, f);
//        System.out.println("ounds: " + bounds);        
//f.setLocation(10 + bounds.x, 10 + bounds.y);
    }
//BufferedImage img = ImageIO.read(imageSrc);
//int w = img.getWidth(null);
//int h = img.getHeight(null);
//BufferedImage bi = new
//    BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//Graphics g = bi.getGraphics();
//g.drawImage(img, 0, 0, null);
//
///*
// * Create a rescale filter op that makes the image
// * 50% opaque.
// */
//float[] scales = { 1f, 1f, 1f, 0.5f };
//float[] offsets = new float[4];
//RescaleOp rop = new RescaleOp(scales, offsets, null);
//
///* Draw the image, applying the filter */
//g2d.drawImage(bi, rop, 0, 0);
//     * 
//     * 
//     
//     */

}
