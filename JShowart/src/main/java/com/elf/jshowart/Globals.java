/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.beans.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author bnevins
 */
public class Globals {

    private Globals() {
    }

    static App app;
    static Controller controller;
    static View view;
    private static final JFileChooser openFileChooser;
    private static final JFileChooser saveFileChooser;
    private static final ImagePreview imagePreview;

    public static JFileChooser setupAndGetOpenFileChooser() {

        openFileChooser.setCurrentDirectory(UserPreferences.get().getPreviousOpenFileParent());
        openFileChooser.setSelectedFile(new File(""));

        return openFileChooser;
    }

    public static JFileChooser setupAndGetSaveAsFileChooser() {

        saveFileChooser.setCurrentDirectory(UserPreferences.get().getPreviousSaveAsFileParent());

        return saveFileChooser;
    }
    static FileNameExtensionFilter[] filters = {
        // the first filter will be the one set!
        new FileNameExtensionFilter("All Supported Types", "bmp", "gif", "jpg", "jpeg", "png", "tif", "tiff"),
        new FileNameExtensionFilter("JPEG images", "jpg", "jpeg"),
        new FileNameExtensionFilter("Bitmap images", "bmp", "wbmp"),
        new FileNameExtensionFilter("GIF images", "gif"),
        new FileNameExtensionFilter("PNG images", "png"),
        new FileNameExtensionFilter("TIFF images", "tif", "tiff"),};

    static {
        /**
         * Metal -- sucks Nimbus -- nice! CDE/Motif - very different Windows -- NICE! Windows Classic
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Controller.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        saveFileChooser = new JFileChooser();
        openFileChooser = new JFileChooser();

        int numFilters = filters.length;

        // set the first one -- All Image Types -- then add the other ones
        openFileChooser.setFileFilter(filters[0]);
        openFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        openFileChooser.setMultiSelectionEnabled(true);
        imagePreview = new ImagePreview(openFileChooser);
        openFileChooser.setAccessory(imagePreview);
        saveFileChooser.setFileFilter(filters[0]);
        saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        saveFileChooser.setMultiSelectionEnabled(false);

        for (int i = 1; i < numFilters; i++) {
            openFileChooser.addChoosableFileFilter(Globals.filters[i]);
            saveFileChooser.addChoosableFileFilter(Globals.filters[i]);
        }
    }

    static class ImagePreview extends JPanel implements PropertyChangeListener {

        private Image img;
        private int width = 350;
        private JFileChooser jfc;
        
        ImagePreview(JFileChooser aJFC) {
            jfc = aJFC;
            Dimension sz = new Dimension(width, width);
            setPreferredSize(sz);
            jfc.addPropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            try {
                System.out.println("updating " + evt);
                File file = jfc.getSelectedFile();
                updateImage(file);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }

        public void updateImage(File file) throws IOException {
            if (file == null) {
                return;
            }

            img = ImageIO.read(file);
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            // fill the background
            g.setColor(Color.gray);
            g.fillRect(0, 0, getWidth(), getHeight());

            if (img != null) {
                // calculate the scaling factor
                int w = img.getWidth(null);
                int h = img.getHeight(null);
                int side = Math.max(w, h);
                double scale = (double) width / (double) side;
                w = (int) (scale * (double) w);
                h = (int) (scale * (double) h);

                // draw the image
                g.drawImage(img, 0, 0, w, h, null);

                // draw the image dimensions
                String dim = w + " x " + h;
                g.setColor(Color.black);
                g.drawString(dim, 31, 196);
                g.setColor(Color.white);
                g.drawString(dim, 30, 195);

            } else {

                // print a message
                g.setColor(Color.black);
                g.drawString("Not an image", 30, 100);
            }
        }

    }
}
