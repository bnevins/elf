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
public class ImageSliderTester {

    //private static Frame mainFrame;
    private BufferStrategy bufferStrategy;
    //private static boolean debug = false;
    private Rectangle screenRec;
    private Rectangle imageRec;
    private Rectangle imageScaledRec;
    private BufferedImage bi1;
    private BufferedImage bi2;
    private Frame mainFrame;

    public ImageSliderTester() {
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
            mainFrame.createBufferStrategy(4);
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
            Logger.getLogger(ImageSliderTester.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public void paint() throws IOException {
        try {
            ImageSlider slider = new ImageSlider(0, bufferStrategy, bi1, bi2, screenRec, imageScaledRec);
            slider.slideDown();
            Thread.sleep(100);
            slider.slideRight();
            Thread.sleep(100);
            slider.slideUp();
            Thread.sleep(100);
            slider.slideLeft();
        } catch (InterruptedException ex) {
            Logger.getLogger(ImageSliderTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        final String home = System.getProperty("user.home").replace('\\', '/');
        File file1 = new File(home + "/tmp/bb/day.jpg");
        File file2 = new File(home + "/tmp/bb/night.jpg");
        SwingUtilities.invokeLater(() -> {
            try {
                ImageSliderTester is = new ImageSliderTester();
                is.init(file1, file2);
                is.paint();
            } catch (IOException ex) {
                Logger.getLogger(ImageSliderTester.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        });
    }
}
