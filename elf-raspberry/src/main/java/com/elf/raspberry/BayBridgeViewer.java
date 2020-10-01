package com.elf.raspberry;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.elf.util.OS;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

/**
 *
 * @author bnevns
 */
public class BayBridgeViewer implements MouseListener, KeyListener {

    private static Frame mainFrame;
    private File dellDir = new File("C:\\tmp\\Aug09");
    private File megamoDir = new File("E:\\WORKING\\BayBridge\\20200921");
    private File megamoDir2 = new File("P:\\stills\\_collage\\ubest10");
    private File piDir = new File("/home/pi/dev/bb_data");
    private File picDir;
    private volatile int currentImageNumber = 0;
    private BufferStrategy bufferStrategy;
    private Rectangle bounds;
    private File[] allFiles;
    private Point origin = new Point(0, 0);
    private BayBridgeViewerTask viewerTask;
    GraphicsDevice device;
    private static final boolean debug = true;

    //private File pic = new File("E:\\dev\\elf\\data\\BB.jpg");
    //private File pic = new File("P:\\stills\\_collage\\uubest\\ray_lgh005005.jpg");
    //private File picDir = new File("P:\\stills\\_collage\\uubest");
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BayBridgeViewer().initialize();
            }
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

            device.setFullScreenWindow(mainFrame);
            bounds = mainFrame.getBounds();
            System.out.println("BOUNDS: " + bounds);
            mainFrame.createBufferStrategy(2);
            bufferStrategy = mainFrame.getBufferStrategy();
            allFiles = getFiles();
            currentImageNumber = 0;
            viewerTask = new BayBridgeViewerTask(this);
            viewerTask.execute();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void doBayBridge() throws IOException, InterruptedException {
        System.out.println("doBayBridge: in swing thread: " + SwingUtilities.isEventDispatchThread());
        try {
            while (true) {
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
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            System.out.println("Closed with this: " + e);
        } finally {
            //device.setFullScreenWindow(null);
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
            ++currentImageNumber;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
                currentImageNumber += 5;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                currentImageNumber -= 5;
                break;
            case KeyEvent.VK_SPACE:
                break;
            case KeyEvent.VK_X:
            case KeyEvent.VK_Q:
            case KeyEvent.VK_ESCAPE:
                viewerTask.cancel(true);
                mainFrame.dispose();
                System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mainFrame.dispose();
        System.exit(0);
    }

    @Override
    public void mousePressed(MouseEvent e) {
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
        if(!debug)
            return;
        Font font = new Font("Serif", Font.PLAIN, 48);
        g.setFont(font);
        g.setColor(Color.RED);
        //String s = String.format(
        //"Filename: %s   Canvas: %dx%d Image Size: %dX%d Scaled Size: %dX%d Origin: %d,%d",
        //allFiles[which].getName(), bounds.width, bounds.height, bi.getWidth(), bi.getHeight(),
        //imageRec.width, imageRec.height, origin.x, origin.y);
        g.drawString("" + currentImageNumber, bounds.width / 2, bounds.height / 2);

    }

    class BayBridgeViewerTask extends SwingWorker<Object, Object> {

        private final BayBridgeViewer viewer;

        public BayBridgeViewerTask(BayBridgeViewer viewer) {
            this.viewer = viewer;
        }

        @Override
        protected Object doInBackground() throws Exception {
            viewer.doBayBridge();
            device.setFullScreenWindow(null);
            mainFrame.dispose();
            System.exit(0);
            return null;
        }
    }
}
