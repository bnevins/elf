package com.elf.raspberry;

import java.util.List;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import com.elf.util.OS;
import java.nio.file.Path;
import java.util.Collections;
import javax.swing.*;

/**
 *
 * @author bnevns
 */
public class BayBridgeViewer implements MouseListener, KeyListener, ActionListener {

    private static Frame mainFrame;
    private final String dellDir = "C:\\tmp\\BB";
    private final String megamoDir = "E:\\WORKING\\BayBridge";
    private final String piDir = "/mnt/Photos/BayBridge";
    private final String macDir = "/Users/bnevins/tmp/BB";
    private String picDir;
    private volatile int currentImageNumber = 0;
    private BufferStrategy bufferStrategy;
    private List<Path> allFiles;
    GraphicsDevice device;
    private static final boolean debug = true;
    Timer timer = null;
    private final int delay = 5000; // 5 seconds
    private BridgePainter painter;
    private Rectangle imageScaledRec;
    private Rectangle imageRec;
    private Rectangle screenRec;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BayBridgeViewer().initialize();
        });
    }

    public void initialize() {
        if (System.getProperty("os.name").startsWith("Mac")) {
            picDir = macDir;
        } else if (OS.isUnix()) {
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
            screenRec = mainFrame.getBounds();
            System.out.println("BOUNDS: " + screenRec);
            mainFrame.createBufferStrategy(2);
            bufferStrategy = mainFrame.getBufferStrategy();
            allFiles = getFiles();
            currentImageNumber = 0;

            painter = new BridgePainter(bufferStrategy);
            // ALL photos are exactly the same size so do this just ONCE!
            BufferedImage bi = ImageIO.read(allFiles.get(0).toFile());
            imageRec = new Rectangle(bi.getWidth(), bi.getHeight());
            ImageScaler scaler = new ImageScaler(imageRec.width, imageRec.height,
                    screenRec.width, screenRec.height);
            setScalerOptions(scaler);
            imageScaledRec = scaler.scale();
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
            painter.paintBridge(allFiles.get(currentImageNumber).toFile(), imageRec, imageScaledRec, screenRec);
            currentImageNumber++;
        } catch (Exception e) {
            System.out.println("Got Exception: " + e);
            timer.stop();
        }
    }

    private List<Path> getFiles() throws IOException {
        java.util.List<Path> paths = com.elf.raspberry.FileLister.getFiles(picDir);

        if (paths.isEmpty()) {
            throw new IOException("Bad directory: " + picDir);
        }
        Collections.shuffle(paths);
        return paths;
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
                int jump = 4;
                if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
                    jump = 1000;
                }
                currentImageNumber += jump;
                restart();
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                currentImageNumber -= 6;
                restart();
                break;
            case KeyEvent.VK_SPACE:
                break;
            case KeyEvent.VK_D:
                painter.toggleDebug();
                --currentImageNumber; // this results in SAME pic getting refreshed
                timer.restart();
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
        if (currentImageNumber >= allFiles.size()) {
            currentImageNumber = 0;
        }
        if (currentImageNumber < 0) {
            currentImageNumber += allFiles.size(); // it's a negative number!
        }
        if (currentImageNumber < 0) {
            currentImageNumber = 0;
        }
    }
}
