package com.elf.image;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class BBTest {

    private static Color[] COLORS = new Color[]{
        Color.red, Color.blue, Color.green, Color.white, Color.black,
        Color.yellow, Color.gray, Color.cyan, Color.pink, Color.lightGray,
        Color.magenta, Color.orange, Color.darkGray};

    private static DisplayMode displayMode = new DisplayMode(3840, 2160, 32, 0);
    private static DisplayMode displayModeLaptop = new DisplayMode(1366, 768, 32, 0);
    private static File imageFile = new File("C:\\tmp\\Aug09\\2020_08_10_013110.jpg");
    private static File imageFile2 = new File("C:\\tmp\\aug09\\2020_08_09_141700.jpg");
    Frame mainFrame;
    BufferedImage image1, image2;

    public BBTest(int numBuffers, GraphicsDevice device) {
        try {
            try {
                image1 = ImageIO.read(imageFile);
                image2 = ImageIO.read(imageFile2);
            } catch (IOException ie) {
                javax.swing.JOptionPane.showMessageDialog(mainFrame, "Error reading image file", "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            GraphicsConfiguration gc = device.getDefaultConfiguration();
            mainFrame = new Frame(gc);
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(true);
            device.setFullScreenWindow(mainFrame);
            if (device.isDisplayChangeSupported()) {
                device.setDisplayMode(displayModeLaptop);
            }
            Rectangle bounds = mainFrame.getBounds();
            mainFrame.createBufferStrategy(numBuffers);
            BufferStrategy bufferStrategy = mainFrame.getBufferStrategy();
            for (float lag = 2000.0f; lag > 0.00000006f; lag = lag / 1.33f) {
                for (int i = 0; i < numBuffers; i++) {
                    Graphics g = bufferStrategy.getDrawGraphics();
                    if (!bufferStrategy.contentsLost()) {
                        if (i % 2 == 1) {
                            showImage(image1, g);
                            bufferStrategy.show();
                            g.dispose();
                        } else {

                            showImage(image2, g);
//                            g.setColor(COLORS[i]);
//                            g.fillRect(0, 0, bounds.width, bounds.height);
                            bufferStrategy.show();
                            g.dispose();
                        }
                    }
                    try {
                        Thread.sleep((int) lag);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            device.setFullScreenWindow(null);
        }
    }

    private void showImage(BufferedImage image, Graphics g) {
        g.drawImage(image, 0, 0, 1366, 768, mainFrame);

    }

    public static void main(String[] args) {
        try {
            int numBuffers = 2;
            if (args != null && args.length > 0) {
                numBuffers = Integer.parseInt(args[0]);
                if (numBuffers < 2 || numBuffers > COLORS.length) {
                    System.err.println("Must specify between 2 and "
                            + COLORS.length + " buffers");
                    System.exit(1);
                }
            }
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = env.getDefaultScreenDevice();
            BBTest test = new BBTest(numBuffers, device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
