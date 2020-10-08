/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author bnevins
 */
class BridgePainter {

    private BufferStrategy bufferStrategy;
    private static final boolean debug = true;

    BridgePainter(BufferStrategy theBufferStrategy) {
        bufferStrategy = theBufferStrategy;
    }

    void paintBridge(File image, Rectangle imageRec) {
        try {
            BufferedImage bi = ImageIO.read(image);
            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(imageRec.x, imageRec.y, imageRec.width, imageRec.height);
            g.drawImage(bi, imageRec.x, imageRec.y, imageRec.width, imageRec.height, null);
            Point origin = new Point(0, 100);
            drawText(g, image, origin);
            bufferStrategy.show();
            g.dispose();

        } catch (Exception e) {
            System.out.println("Got Exception: " + e);
        }
    }

    private void drawText(Graphics g, File image, Point origin) {
        if (!debug) {
            return;
        }
        Font font = new Font("Serif", Font.PLAIN, 48);
        g.setFont(font);
        g.setColor(Color.RED);
        //String s = String.format(
        //"Filename: %s   Canvas: %dx%d Image Size: %dX%d Scaled Size: %dX%d Origin: %d,%d",
        //allFiles[which].getName(), bounds.width, bounds.height, bi.getWidth(), bi.getHeight(),
        //imageRec.width, imageRec.height, origin.x, origin.y);
        g.drawString("" + image.getName(), origin.x, origin.y);

    }
}
