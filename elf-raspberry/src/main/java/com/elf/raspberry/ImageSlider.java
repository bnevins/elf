/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class ImageSlider {

    private final BufferedImage curr;
    private final BufferedImage next;
    private final BufferStrategy bufferStrategy;
    private final Rectangle screenRec;
    private final Rectangle imageScaledRec;
    private int delay = 5;
    
    public ImageSlider(BufferStrategy bufferStrategy, BufferedImage curr, BufferedImage next, Rectangle screenRec, Rectangle imageScaledRec) {
        this.bufferStrategy = bufferStrategy;
        this.curr = curr;
        this.next = next;
        this.screenRec = screenRec;
        this.imageScaledRec = imageScaledRec;
    }
    public ImageSlider(int delay, BufferStrategy bufferStrategy, BufferedImage curr, BufferedImage next, Rectangle screenRec, Rectangle imageScaledRec) {
        this(bufferStrategy, curr, next, screenRec, imageScaledRec);
        this.delay = delay;
    }

    public void paint() throws IOException {

        for (int i = 0; i < screenRec.height; i += 1) {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            // important!  draw bottom before first to clip off top of bottom image!!
            g.drawImage(next, 0, imageScaledRec.y - i + screenRec.height, imageScaledRec.width, imageScaledRec.height, null);
            g.drawImage(curr, 0, imageScaledRec.y - i, imageScaledRec.width, imageScaledRec.height, null);
            bufferStrategy.show();
            g.dispose();
            try {
                Thread.sleep(delay);
            } catch (Exception ex) {
                Logger.getLogger(ImageSliderTester.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
