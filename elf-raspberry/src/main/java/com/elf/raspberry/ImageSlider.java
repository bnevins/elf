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
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.drawImage(bi1, imageScaledRec.x, imageScaledRec.y, imageScaledRec.width, imageScaledRec.height, null);
        bufferStrategy.show();
        g.dispose();
    }

    public static void main(String[] args) {
        File file1 = new File("C:/Users/biz/tmp/bb/20201030/BB_2020_1030_1458.jpg");
        File file2 = new File("C:/Users/biz/tmp/bb/20201030/BB_2020_1031_0208.jpg");
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
