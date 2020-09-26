/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.image;

import com.elf.io.FileFinder;
import com.elf.io.JpegFileFilter;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author bnevns
 */
public class Tester2 {

    private static File imageFile = new File("C:\\tmp\\BB.jpg");
    private static File imageFile2 = new File("C:\\tmp\\aug09\\2020_08_09_141700.jpg");
    //private static String dirName = "P:/stills/_collage/ubest10";
    private static Frame mainFrame;
    private static Rectangle bounds;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphicsDevice device = null;
        if (args.length > 0) {
            imageFile = new File(args[0]);
        }
//String dirName = "C:\\tmp\\aug09";
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            device = env.getDefaultScreenDevice();
            GraphicsConfiguration gc = device.getDefaultConfiguration();
            mainFrame = new Frame(gc);
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(true);
            device.setFullScreenWindow(mainFrame);
            bounds = mainFrame.getBounds();
            mainFrame.createBufferStrategy(2);
            BufferStrategy bufferStrategy = mainFrame.getBufferStrategy();

            //for (int i = 0; i < files.size(); i++) {
            //for (int i = 0; i < 3; i++) {
            BufferedImage image = ImageIO.read(imageFile);
            Graphics g = bufferStrategy.getDrawGraphics();
            Dimension d = ImageUtils.scaleImage(image.getWidth(), image.getHeight(), (int) bounds.getWidth(), (int) bounds.getHeight());
            g.clearRect(0, 0, (int) bounds.getWidth(), (int) bounds.getHeight());
            g.drawImage(image, 0, 0, d.width, d.height, mainFrame);
            bufferStrategy.show();
            //g.dispose();
            Thread.sleep(5000);
            g.dispose();
            Thread.sleep(5000);

            //}
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            device.setFullScreenWindow(null);
            mainFrame.dispose();
        }
    }

}
