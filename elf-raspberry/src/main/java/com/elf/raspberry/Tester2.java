/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry;

/**
 *
 * @author bnevins
 */
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Tester2 extends Window {

    private BufferedImage pic;
    Dimension scaledSize;
    static Dimension screenSize;

    public static void main(String[] args) {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle r = ge.getMaximumWindowBounds();
        System.out.println("RECTANGLE: " + r);
        //screenSize = new Dimension(r.width, r.height);
        System.out.println("screenSize: " + screenSize);
        GraphicsDevice screen = ge.getDefaultScreenDevice();
        //screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (!screen.isFullScreenSupported()) {
            System.out.println("Full screen mode not supported");
            System.exit(1);
        }

        try {
            BufferedImage loadedpic = ImageIO.read(new File("p:/sample.jpg"));
            screen.setFullScreenWindow(new Tester2(loadedpic));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Tester2(BufferedImage pic) {
        super(new Frame());

        this.pic = pic;
        this.scaledSize = ImageUtils.scaleImage(pic.getWidth(), pic.getHeight(), screenSize.width, screenSize.height);

        System.out.printf("\nBefore: %dx%d, Scaled: %dx%d, Screen: %dx%d ",
                pic.getWidth(), pic.getHeight(),
                scaledSize.width, scaledSize.height,
                screenSize.width, screenSize.height);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
    }

    public void paint(Graphics g) {

        g.drawImage(pic, 0, 0, scaledSize.width, scaledSize.height, this);
    }
}
