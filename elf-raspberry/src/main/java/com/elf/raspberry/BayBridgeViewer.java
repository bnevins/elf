package com.elf.raspberry;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import com.elf.util.OS;
import javax.swing.*;

/**
 *
 * @author bnevns
 */
public class BayBridgeViewer implements MouseListener, KeyListener, ActionListener {

    private static Frame mainFrame;
    private final File dellDir = new File("C:\\tmp\\Aug09");
    private final File megamoDir = new File("E:\\WORKING\\BayBridge\\20200921");
    private final File megamoDir2 = new File("P:\\stills\\_collage\\ubest10");
    private final File piDir = new File("/home/pi/dev/bb_data");
    private File picDir;
    private volatile int currentImageNumber = 0;
    private BufferStrategy bufferStrategy;
    private Rectangle bounds;
    private File[] allFiles;
    private Point origin = new Point(0, 0);
    GraphicsDevice device;
    private static final boolean debug = true;
    Timer timer = null;
    private final int delay = 5000; // 5 seconds

    //private File pic = new File("E:\\dev\\elf\\data\\BB.jpg");
    //private File pic = new File("P:\\stills\\_collage\\uubest\\ray_lgh005005.jpg");
    //private File picDir = new File("P:\\stills\\_collage\\uubest");
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BayBridgeViewer().initialize();
        });
    }

    public void initialize() {
        if (OS.isUnix()) {
            picDir = piDir;
        } else if ("DELL7470".equals(System.getenv("COMPUTERNAME"))) {
            picDir = dellDir;
        } else if ("MEGAMO".equals(System.getenv("COMPUTERNAME"))) {
            picDir = megamoDir;
        } else {
            picDir = null;
        }

        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            device = env.getDefaultScreenDevice();
            GraphicsConfiguration gc = device.getDefaultConfiguration();
            mainFrame = new Frame(gc);
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(true);
            mainFrame.addMouseListener(this);
            mainFrame.addKeyListener(this);
            mainFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent ev) {
                    System.exit(0);
                }
            });
            timer = new Timer(delay, this);
            timer.setInitialDelay(0);
            device.setFullScreenWindow(mainFrame);
            bounds = mainFrame.getBounds();
            System.out.println("BOUNDS: " + bounds);
            mainFrame.createBufferStrategy(2);
            bufferStrategy = mainFrame.getBufferStrategy();
            allFiles = getFiles();
            currentImageNumber = 0;
            start();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void start() {
        timer.start();
    }

    private void stop() {
        timer.stop();
        device.setFullScreenWindow(null);
        mainFrame.dispose();
        System.exit(0);
    }

    private void restart() {
        if (timer != null) {
            timer.restart();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        paintBridge();
    }

    public void paintBridge() {
        try {
            checkCurrentImageNumber();
            BufferedImage bi = ImageIO.read(allFiles[currentImageNumber]);
            ImageScaler scaler = new ImageScaler(bi.getWidth(), bi.getHeight(),
                    bounds.width, bounds.height);
            setScalerOptions(scaler);
            Rectangle imageRec = scaler.scale();
            origin = new Point(imageRec.x, imageRec.y);
            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.drawImage(bi, origin.x, origin.y,
                    imageRec.width, imageRec.height, mainFrame);
            drawText(g);
            bufferStrategy.show();
            g.dispose();
            currentImageNumber++;
        } catch (Exception e) {
            System.out.println("Got Exception: " + e);
            timer.stop();
        }
    }

    private File[] getFiles() {
        File[] files = picDir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".jpg");
            }
        });

        if (files.length == 0) {
            throw new RuntimeException("Bad directory: " + picDir);
        }
        Arrays.sort(files);
        return files;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.printf("keyTyped: %c", e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            restart();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // note that doing a restart() will already cause a "+1" change to image number...
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                //origin.y += 5;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                //origin.y -= 5;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                currentImageNumber += 4;
                restart();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                currentImageNumber -= 6;
                restart();
                break;
            case KeyEvent.VK_SPACE:
                break;
            case KeyEvent.VK_X:
            case KeyEvent.VK_Q:
            case KeyEvent.VK_ESCAPE:
                stop();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mainFrame.dispose();
        System.exit(0);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        stop();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void setScalerOptions(ImageScaler scaler) {
        scaler.setClipOk(true);
        scaler.setClipFromTopOnly(true);
        scaler.setDebug(false);
    }

    private void checkCurrentImageNumber() {
        if (currentImageNumber >= allFiles.length) {
            currentImageNumber = 0;
        }
        if (currentImageNumber < 0) {
            currentImageNumber += allFiles.length; // it's a negative number!
        }
        if (currentImageNumber < 0) {
            currentImageNumber = 0;
        }
    }

    private void drawText(Graphics g) {
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
        g.drawString("" + currentImageNumber, bounds.width / 2, bounds.height / 2);

    }
}
