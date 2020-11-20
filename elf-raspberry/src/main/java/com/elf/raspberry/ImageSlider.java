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
    private int columnRowIncrement = 1;

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

    public void slideRight() throws IOException {
        slideHorizontal(true);
    }

    public void slideLeft() throws IOException {
        slideHorizontal(false);
    }

    public void slideUp() throws IOException {
        slideVertical(true);
    }

    public void slideDown() throws IOException {
        slideVertical(false);
    }
    //////////////////////////////////////////////////////////////////////////////

    private void slideVertical(boolean up) throws IOException {

        for (int i = 0; i <= screenRec.height; i += columnRowIncrement) {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            if (up) {
                // important!  draw next before curr to clip off top of next image!!
                g.drawImage(next, 0, imageScaledRec.y - i + screenRec.height, imageScaledRec.width, imageScaledRec.height, null);
                g.drawImage(curr, 0, imageScaledRec.y - i, imageScaledRec.width, imageScaledRec.height, null);
            } else { // down
                // important!  draw curr before next to clip off top of next image!!
                g.drawImage(curr, 0, imageScaledRec.y + i, imageScaledRec.width, imageScaledRec.height, null);
                g.drawImage(next, 0, imageScaledRec.y + i - screenRec.height, imageScaledRec.width, imageScaledRec.height, null);
            }
            bufferStrategy.show();
            g.dispose();
            try {
                Thread.sleep(delay);
            } catch (Exception ex) {
                Logger.getLogger(ImageSliderTester.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void slideHorizontal(boolean right) throws IOException {

        for (int i = 0; i <= screenRec.width; i += columnRowIncrement) {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            // important!  draw bottom before first to clip off top of bottom image!!
            if (right) {
                g.drawImage(curr, i, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
                g.drawImage(next, i - screenRec.width, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
            } else {
                g.drawImage(curr, -i, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
                g.drawImage(next, screenRec.width - i, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
            }
            bufferStrategy.show();
            g.dispose();
            try {
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            } catch (Exception ex) {
                Logger.getLogger(ImageSliderTester.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
