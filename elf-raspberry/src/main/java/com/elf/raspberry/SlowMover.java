package com.elf.raspberry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
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

/**
 *
 * @author bnevns
 */
public class SlowMover implements MouseListener {

    private static Frame mainFrame;
    private File picDir = new File("E:\\WORKING\\BayBridge\\20200921");
    private File pic = new File("E:\\dev\\elf\\data\\BB.jpg");

    public static void main(String[] args) {
        new SlowMover().dome();
    }
    private BufferStrategy bufferStrategy;
    private Rectangle bounds;
    private File[] allFiles;

    public void dome() {
        GraphicsDevice device = null;
        try {

            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            device = env.getDefaultScreenDevice();
            GraphicsConfiguration gc = device.getDefaultConfiguration();
            mainFrame = new Frame(gc);
            mainFrame.setUndecorated(true);
            mainFrame.setIgnoreRepaint(true);
            mainFrame.addMouseListener(this);
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

            File[] files = new File[25];
            for (int i = 0; i < files.length; i++) {
                files[i] = allFiles[i];
            }
            //doPrototype();
            doJunk();
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
        Font font = new Font("Serif", Font.PLAIN, 48);

        for (int i = 0; i < allFiles.length; i++) {
            BufferedImage bi = ImageIO.read(allFiles[i]);
            Graphics g = bufferStrategy.getDrawGraphics();
            ImageScaler scaler = new ImageScaler(bi.getWidth(), bi.getHeight(),
                    bounds.width, bounds.height);
            scaler.setClipOk(true);
            scaler.setClipFromTopOnly(true);
            Rectangle imageRec = scaler.scale();
            g.drawImage(bi, imageRec.x, imageRec.y,
                    imageRec.width, imageRec.height, mainFrame);
            g.setColor(Color.RED);
            g.setFont(font);
            g.drawString(allFiles[i].getName(), 400, 400);

            System.out.println("Image Scaler says: " + imageRec);
            bufferStrategy.show();
            g.dispose();
            Thread.sleep(500);
        }
    }

    private void doJunk() throws IOException, InterruptedException {
        Font font = new Font("Serif", Font.PLAIN, 24);
        BufferedImage bi = ImageIO.read(pic);
        ImageScaler scaler = new ImageScaler(bi.getWidth(), bi.getHeight(),
                bounds.width, bounds.height);
        scaler.setClipOk(true);
        scaler.setClipFromTopOnly(true);
        Rectangle imageRec = scaler.scale();
        int y = imageRec.y;
        while(y++ < -1) {
            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.drawImage(bi, imageRec.x, y,
                    imageRec.width, imageRec.height, mainFrame);
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("Y: " + y, 400, 400);
            bufferStrategy.show();
            g.dispose();
            Thread.sleep(150);
        }
        while(y-- > -200) {
            Graphics g = bufferStrategy.getDrawGraphics();
            g.clearRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g.drawImage(bi, imageRec.x, y,
                    imageRec.width, imageRec.height, mainFrame);
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("Y: " + y, 400, 400);
            bufferStrategy.show();
            g.dispose();
            Thread.sleep(150);
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
