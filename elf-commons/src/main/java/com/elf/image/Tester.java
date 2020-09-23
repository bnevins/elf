/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.image;

import com.elf.io.FileFinder;
import com.elf.io.JpegFileFilter;
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
public class Tester {

    private static File imageFile = new File("C:\\tmp\\Aug09\\2020_08_10_013110.jpg");
    private static File imageFile2 = new File("C:\\tmp\\aug09\\2020_08_09_141700.jpg");
    static Frame mainFrame;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphicsDevice device = null;
        String dirName = "C:\\tmp\\aug09";
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            device = env.getDefaultScreenDevice();
            GraphicsConfiguration gc = device.getDefaultConfiguration();
            mainFrame = new Frame(gc);
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(true);
            device.setFullScreenWindow(mainFrame);
            Rectangle bounds = mainFrame.getBounds();
            mainFrame.createBufferStrategy(2);
            BufferStrategy bufferStrategy = mainFrame.getBufferStrategy();
            FileFinder ff = new FileFinder(dirName, new JpegFileFilter());
            List<File> files = ff.getFiles();

            /**
            BufferedImage images[] = new BufferedImage[50];
            for (int i = 0; i < 10; i++) {
                images[i] = ImageIO.read(files.get(i));
            }
            * ***/
            for (int i = 0; i < 10; i++) {
                BufferedImage image = ImageIO.read(files.get(i));
                Graphics g = bufferStrategy.getDrawGraphics();
                g.drawImage(image, 0, 0, bounds.width, bounds.height, mainFrame);
                bufferStrategy.show();
                g.dispose();
                Thread.sleep(100);
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            device.setFullScreenWindow(null);
            mainFrame.dispose();
        }
    }

}
