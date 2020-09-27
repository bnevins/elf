package com.elf.raspberry;
import java.awt.Frame;
import java.awt.Graphics;
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
public class Viewer implements MouseListener {

    private static File imageFile = new File("/home/pi/dev/apod.jpg");
    private static File imageFile2 = new File("/home/pi/dev/crab.jpg");
    //private static File imageFile2 = new File("C:\\tmp\\aug09\\2020_08_09_141700.jpg");
    static Frame mainFrame;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Viewer().dome();
    }

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
            Rectangle bounds = mainFrame.getBounds();
            System.out.println("BOUNDS: " + bounds);
            mainFrame.createBufferStrategy(2);
            BufferStrategy bufferStrategy = mainFrame.getBufferStrategy();
            File[] allFiles = getFiles();

            File[] files = new File[25];
            for (int i = 0; i < files.length; i++) {
                files[i] = allFiles[i];
            }

            //List<BufferedImage> images = threadedGetImages(files);
            while (true) {
                for (int i = 0; i < allFiles.length; i++) {
                    BufferedImage bi = ImageIO.read(allFiles[i]);
                    Graphics g = bufferStrategy.getDrawGraphics();
                    g.drawImage(bi, 0, 0, bounds.width, bounds.height, mainFrame);
                    bufferStrategy.show();
                    g.dispose();
                    //Thread.sleep(350);
                    //Thread.sleep(5000);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            device.setFullScreenWindow(null);
            mainFrame.dispose();
        }
    }

    private static File[] getFiles() {
        File dir = new File("/home/pi/dev/data");
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".jpg");
            }
        });
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
                Logger.getLogger(Viewer.class
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
