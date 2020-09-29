package com.elf.raspberry;

import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.elf.util.OS;
/**
 *
 * @author bnevns
 */
public class SlowMover implements MouseListener, KeyListener {

    private static Frame mainFrame;
    private File megamoDir = new File("E:\\WORKING\\BayBridge\\20200921");
    private File piDir = new File("/home/pi/dev/data");
    private File picDir;
    private int which = 1;
    private BufferStrategy bufferStrategy;
    private Rectangle bounds;
    private File[] allFiles;
    private Point origin = new Point(0, 0);
    //private File pic = new File("E:\\dev\\elf\\data\\BB.jpg");
    //private File pic = new File("P:\\stills\\_collage\\uubest\\ray_lgh005005.jpg");
    //private File picDir = new File("P:\\stills\\_collage\\uubest");

    public static void main(String[] args) {
        new SlowMover().dome();
    }

    public void dome() {
        if(OS.isUnix())
            picDir = piDir;
        
        GraphicsDevice device = null;
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

            //doPrototype();
            doPrototype();
            //List<BufferedImage> images = threadedGetImages(files);

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            device.setFullScreenWindow(null);
            mainFrame.dispose();
        }
    }

    private void doPrototype() throws IOException, InterruptedException {
        Font font = new Font("Serif", Font.PLAIN, 24);
        BufferedImage bi = ImageIO.read(allFiles[0]);
            ImageScaler scaler = new ImageScaler(bi.getWidth(), bi.getHeight(),
                    bounds.width, bounds.height);
            
            scaler.setClipOk(which == 1 ? false :true);
            scaler.setClipFromTopOnly(which == 3? true : false);
            scaler.setDebug(true);
            Rectangle imageRec = scaler.scale();
            origin = new Point(imageRec.x, imageRec.y);
        while (true) {
            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.drawImage(bi, origin.x, origin.y,
                    imageRec.width, imageRec.height, mainFrame);
            g.setFont(font);
            g.setColor(Color.RED);
            String s = String.format(
                    "Canvas: %dx%d Image Size: %dX%d Scaled Size: %dX%d Origin: %d,%d",
                    bounds.width, bounds.height, bi.getWidth(), bi.getHeight(),
                    imageRec.width, imageRec.height, origin.x, origin.y);
            g.drawString(s, 100, 100);
            bufferStrategy.show();
            g.dispose();
            Thread.sleep(150);
        }
    }
    private void doPrototype3() throws IOException, InterruptedException {
        Font font = new Font("Serif", Font.PLAIN, 24);
        BufferedImage bi = ImageIO.read(allFiles[0]);
        while (true) {
            ImageScaler scaler = new ImageScaler(bi.getWidth(), bi.getHeight(),
                    bounds.width, bounds.height);
            
            scaler.setClipOk(which == 1 ? false :true);
            scaler.setClipFromTopOnly(which == 3? true : false);
            scaler.setDebug(true);
            Rectangle imageRec = scaler.scale();
            origin = new Point(imageRec.x, imageRec.y);

            int y = imageRec.y;
            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.drawImage(bi, origin.x, origin.y,
                    imageRec.width, imageRec.height, mainFrame);
            g.setFont(font);
            g.setColor(Color.RED);
            String s = String.format(
                    "Canvas: %dx%d Image Size: %dX%d Scaled Size: %dX%d Origin: %d,%d",
                    bounds.width, bounds.height, bi.getWidth(), bi.getHeight(),
                    imageRec.width, imageRec.height, origin.x, origin.y);
            g.drawString(s, 100, 100);
            bufferStrategy.show();
            g.dispose();
            Thread.sleep(500);
        }
    }

    private void doPrototype2() throws IOException, InterruptedException {
        Font font = new Font("Serif", Font.PLAIN, 24);
        for (File pic : allFiles) {
            BufferedImage bi = ImageIO.read(pic);
            ImageScaler scaler = new ImageScaler(bi.getWidth(), bi.getHeight(),
                    bounds.width, bounds.height);
            scaler.setClipOk(true);
            scaler.setClipFromTopOnly(true);
            scaler.setDebug(true);
            Rectangle imageRec = scaler.scale();
            origin = new Point(imageRec.x, imageRec.y);

            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.drawImage(bi, origin.x, origin.y,
                    imageRec.width, imageRec.height, mainFrame);
            g.setFont(font);
            g.setColor(Color.RED);
            String s = String.format(
                    "Canvas: %dx%d Image Size: %dX%d Scaled Size: %dX%d Origin: %d,%d",
                    bounds.width, bounds.height, bi.getWidth(), bi.getHeight(),
                    imageRec.width, imageRec.height, origin.x, origin.y);
            g.drawString(s, 100, 100);
            //g.drawString("Origin: " + origin.x + "," + origin.y, 100, 100);
            bufferStrategy.show();
            g.dispose();
            Thread.sleep(10000);
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

    private List<BufferedImage> unthreadedGetImages(File[] files) {
        long start = System.currentTimeMillis();
        List<BufferedImage> images = new ArrayList<BufferedImage>();
        for (File f : files) {
            try {
                images.add(ImageIO.read(f));

            } catch (IOException ex) {
                Logger.getLogger(SlowMover.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("NUMBER IMAGES = " + images.size());
        System.out.println("Total seconds = " + (double) (System.currentTimeMillis() - start) / 1000.0);
        return images;
    }

    public List<BufferedImage> threadedGetImages(File[] files) {
        long start = System.currentTimeMillis();
        List<BufferedImage> images = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(5);
        List<ImageLoadingTask> tasks = new ArrayList<>(files.length);
        for (File file : files) {
            tasks.add(new ImageLoadingTask(file));
        }
        try {
            List<Future<BufferedImage>> results = service.invokeAll(tasks);
            for (Future<BufferedImage> future : results) {
                images.add(future.get());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        service.shutdown();
        System.out.println("Total seconds = " + (double) (System.currentTimeMillis() - start) / 1000.0);

        return images;

    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.printf("keyTyped: %c", e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.printf("Key Pressed: %X\n", e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.printf("Key Released: %X\n", e.getKeyCode());
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                origin.y += 5;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                origin.y -= 5;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                origin.x += 5;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                origin.x -= 5;
                break;
            case KeyEvent.VK_SPACE:
                ++which;
                if(which > 3)
                    which = 0;
                break;
            case KeyEvent.VK_X:
            case KeyEvent.VK_Q:
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("MOUSE CLICKED");
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

    private static class ImageLoadingTask implements Callable<BufferedImage> {

        private File file;

        public ImageLoadingTask(File file) {
            this.file = file;
        }

        @Override
        public BufferedImage call() throws Exception {
            return ImageIO.read(file);
        }

    }
}
