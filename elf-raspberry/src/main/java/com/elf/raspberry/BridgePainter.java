package com.elf.raspberry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
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
    private static boolean debug = false;

    BridgePainter(BufferStrategy theBufferStrategy) {
        bufferStrategy = theBufferStrategy;
    }

    void toggleDebug() {
        debug = !debug;
    }

    void paintBridge(File image, Rectangle imageRec, Rectangle imageScaledRec, Rectangle screenRec) {
        try {
            BufferedImage bi = ImageIO.read(image);
            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(imageScaledRec.x, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height);
            g.drawImage(bi, imageScaledRec.x, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
            Point origin = new Point(0, 100);
            drawText(g, image, origin, imageRec, imageScaledRec, screenRec);
            bufferStrategy.show();
            g.dispose();
        } catch (Exception e) {
            System.out.println("Got Exception: " + e);
        }
    }

    private void drawText(Graphics g, File image, Point origin, Rectangle imageRec, Rectangle imageScaledRec, Rectangle screenRec) {
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
        String s = image.getName();
        int fontHeight = (int)g.getFontMetrics().getStringBounds(s, g).getHeight();
        int y = origin.y;
        g.drawString("Filename: " + s, origin.x, origin.y);
        g.drawString("Image Size: " + imageRec.width + "X" + imageRec.height, origin.x, (y += fontHeight));
        g.drawString("Image Scaled Size: " + imageScaledRec.width + "X" + imageScaledRec.height, origin.x, (y += fontHeight));
        g.drawString("Image Scaled Origin: " + imageScaledRec.x + "," + imageScaledRec.y, origin.x, (y += fontHeight));
        g.drawString("Screen Size: " + screenRec.width + "X" + screenRec.height, origin.x, (y += fontHeight));

    }
}
