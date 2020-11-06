package com.elf.raspberry;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/**
 * @author bnevins
 */
public class ImageSlider {

    //private static Frame mainFrame;
    private BufferStrategy bufferStrategy;
    //private static boolean debug = false;
    private Rectangle screenRec;
    private Rectangle imageRec;
    private Rectangle imageScaledRec;
    private BufferedImage bi1;
    private BufferedImage bi2;
    private BufferedImage bi1A;
    private BufferedImage bi2A;
    private Frame mainFrame;

    public ImageSlider() {
    }

    public void init(File image1, File image2) {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice device = env.getDefaultScreenDevice();
            GraphicsConfiguration gc = device.getDefaultConfiguration();
            mainFrame = new Frame(gc);
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(true);
            mainFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent ev) {
                    System.exit(0);
                }
            });

            device.setFullScreenWindow(mainFrame);
            mainFrame.createBufferStrategy(2);
            bufferStrategy = mainFrame.getBufferStrategy();
            screenRec = mainFrame.getBounds();
            bi1 = ImageIO.read(image1);
            bi2 = ImageIO.read(image2);
            //BufferedImage bi1A = new BufferedImage(0, 0, 0)
            imageRec = new Rectangle(bi1.getWidth(), bi1.getHeight());
            ImageScaler scaler = new ImageScaler(imageRec.width, imageRec.height,
                    screenRec.width, screenRec.height);
            scaler.setClipOk(true);
            scaler.setClipFromTopOnly(true);
            scaler.setDebug(false);
            imageScaledRec = scaler.scale();

            ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////
            bi1A = new BufferedImage(screenRec.width, screenRec.height, bi1.getType());
            Graphics2D g = (Graphics2D) bi1A.getGraphics();
            g.drawImage(bi1, imageScaledRec.x, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
            g.dispose();

            bi2A = new BufferedImage(screenRec.width, screenRec.height, bi2.getType());
            g = (Graphics2D) bi2A.getGraphics();
            g.drawImage(bi2, imageScaledRec.x, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
            g.dispose();

            ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////
            mainFrame.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mainFrame.dispose();
                    System.exit(0);
                }
            });
            mainFrame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    mainFrame.dispose();
                    System.exit(0);
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(ImageSlider.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public void paint() throws IOException {
        int screenHt = screenRec.height;
        int topOffset = imageScaledRec.y;
        int imageWidth = imageScaledRec.width;
        int imageHt = imageScaledRec.height;
        
        for (int i = 0; i < screenHt; i += 1) {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            // important!  draw bottom before first to clip off top of bottom image!!
            g.drawImage(bi2, 0, topOffset - i + screenHt, imageWidth, imageHt, null);
            g.drawImage(bi1, 0, topOffset - i, imageWidth, imageHt, null);
            bufferStrategy.show();
            g.dispose();
            try {
                Thread.sleep(10);
            } catch (Exception ex) {
                Logger.getLogger(ImageSlider.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void paintx() throws IOException {
        int ht = bi1A.getHeight();

        for (int i = 0; i < ht; i += 1) {
            Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
            g.drawImage(bi1A, 0, -i, null);
            g.drawImage(bi2A, 0, ht - i, null);
            bufferStrategy.show();
            g.dispose();
            try {
                //Thread.sleep(1);
            } catch (Exception ex) {
                Logger.getLogger(ImageSlider.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void main(String[] args) {
        final String home = System.getProperty("user.home").replace('\\', '/');
        File file1 = new File(home + "/tmp/bb/day.jpg");
        File file2 = new File(home + "/tmp/bb/night.jpg");
        SwingUtilities.invokeLater(() -> {
            try {
                ImageSlider is = new ImageSlider();
                is.init(file1, file2);
                is.paint();
            } catch (IOException ex) {
                Logger.getLogger(ImageSlider.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        });
    }
}
