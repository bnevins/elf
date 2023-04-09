/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.logging.*;
import javax.imageio.*;
import javax.swing.*;
import static java.awt.event.KeyEvent.*;
import java.awt.geom.*;

/**
 *
 * @author bnevins
 */
public class View extends JPanel implements KeyListener {

    private BufferedImage image;
    private File prevImageFile = null;
    private UserPreferences prefs = UserPreferences.get();
    private Model artlib = Model.get();
    private JScrollPane parentPane;
    private Dimension preferredSize;

    /**
     * Creates new form JShowartView
     */
    public View() {
        System.out.println("JShowartView Layout Manager = " + getLayout());
        Globals.view = this;
        //addKeyListener(this); doesn't work!!
    }

    public void keyTyped(KeyEvent e) {
        //System.out.println("KEY TYPED" + e); 

    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //System.out.println("KEY PRESSED == " + key);
        switch (key) {
            case VK_SPACE, VK_RIGHT ->
                nextImage();
            case VK_LEFT ->
                prevImage();
            //case KeyEvent.VK_ESCAPE ->
            //Globals.frame.toggleFullScreen();
        }
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("KEY RELEASED:  " + e); 
    }

    // TODO set scroll size
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null)
            return;
//        System.out.println("View width = " + getWidth());
//        System.out.println("Scrollpane width = " + parentPane.getWidth());
//        System.out.println("Viewport width = " + parentPane.getViewport().getWidth());
//        System.out.println("");
//        System.out.println("COLOR is: " + g.getColor());
        if (prefs.fitToWindow) {
            Rectangle r = Utils.fitToWindow(new Dimension(parentPane.getViewport().getWidth(), parentPane.getViewport().getHeight()), new Dimension(image.getWidth(), image.getHeight()));
            //System.out.println("Image Rectangle = " +r);
            //System.out.println("Image width, height = " + image.getWidth() + ", " + image.getHeight());
            setBounds(r.getBounds());
            preferredSize = new Dimension(r.width, r.height);
            g.drawImage(image, r.x, r.y, r.width, r.height, null);
        } else {
            g.drawImage(image, 0, 0, null);
            preferredSize = new Dimension(image.getWidth(), image.getHeight());
        }
    }

    void nextImage() {
        setupImage(artlib.next());
    }

    void prevImage() {
        setupImage(artlib.prev());
    }

    private void setupImage(File imageFile) {
        if (imageFile == null) {
            image = null;
            return;
        }

        // if we have only 1 file in artlib, don't waste time re-reading it
        if (imageFile.equals(prevImageFile))
            return;

        prevImageFile = imageFile;

        try {
            image = ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            image = null;
        }

        Globals.frame.setTitle("JShowArt    " + imageFile.getAbsolutePath());
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        //invalidate();
        resetScrollbars();
        parentPane.revalidate();
        repaint();
        // TODO -- possible junk in window on initial draw.  This doesn't work -->invalidate();
    }

    @Override
    public Dimension getPreferredSize() {
        if (preferredSize == null)
            return super.getPreferredSize();
        else
            return preferredSize;
    }

    void imagesReplaced() {
        // files were just opened
        image = null;
        prevImageFile = null;
        nextImage();
    }

    void setContainer(JScrollPane jsp) {
        parentPane = jsp;
    }

    private void resetScrollbars() {
        JScrollBar verticalScrollBar = parentPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = parentPane.getHorizontalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
        horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
    }

    public void save() {
        String saveDialogTitle = "Image Save";

        if (image == null) {
            errorMessage("No files loaded.", saveDialogTitle);
            return;
        }

        File currentImageFile = artlib.curr();

        if (currentImageFile == null) {
            errorMessage("No files loaded.", saveDialogTitle); //can't happen because image is not null!
            return;
        }

        String ext = Utils.getFileExtension(currentImageFile.getName());

        if (isOkToOverwrite(currentImageFile)) {
            try {
                ImageIO.write(image, ext, currentImageFile);
            } catch (IOException ex) {
                errorMessage(ex.toString(), saveDialogTitle);
            }
            successMessage(currentImageFile.toString() + " saved successfully", saveDialogTitle);
        }
    }

    public void saveAs() {
        saveAs(image);
    }

    private void saveAs(BufferedImage theImage) {

        var chooser = Globals.setupAndGetSaveAsFileChooser();
        if (chooser.showSaveDialog(Globals.frame) != JFileChooser.APPROVE_OPTION)
            return;

        File outfile = chooser.getSelectedFile();

        if (!Utils.isArtFile(outfile)) {
            errorMessage("The image file extension must be one of these: " + Utils.getArtFileExtensionsAsString(), "Unknown Image Type");
            return;
        }
        prefs.previousSaveAsFileParent = outfile.getParentFile();

        if (outfile.exists() && !isOkToOverwrite(outfile))
            return;
        String saveDialogTitle = "Image Save As";
        try {
            ImageIO.write(theImage, Utils.getFileExtension(outfile), outfile);
        } catch (IOException ex) {
            errorMessage(ex.toString(), saveDialogTitle);
        }
        successMessage(outfile.toString() + " saved successfully", saveDialogTitle);
    }

    private boolean isOkToOverwrite(File f) {
        int selection = JOptionPane.showConfirmDialog(Globals.frame, "Overwrite " + f.getName(), "Overwrite File",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return selection == JOptionPane.YES_OPTION;
    }

    private void errorMessage(String msg, String title) {
        JOptionPane.showMessageDialog(Globals.frame, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    private void successMessage(String msg, String title) {
        JOptionPane.showMessageDialog(Globals.frame, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    void rotate(int degrees) {
        if (image == null)
            return;

        image = rotateImage(image, degrees / 90);
        save();
        repaint();
    }

    public BufferedImage rotateImage(BufferedImage image, int quadrants) {

        int w0 = image.getWidth();
        int h0 = image.getHeight();
        int centerX = w0 / 2;
        int centerY = h0 / 2;

        if (quadrants % 4 == 1) {
            centerX = centerY;
        } else if (quadrants % 4 == 3) {
            centerY = centerX;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToQuadrantRotation(quadrants, centerX, centerY);
        AffineTransformOp opRotated = new AffineTransformOp(affineTransform,
                AffineTransformOp.TYPE_BILINEAR);

        BufferedImage transformedImage = opRotated.filter(image, null);
        return transformedImage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    void showScaled(double scalingFactor) {
        if (image == null)
            return;

        double dw = image.getWidth();
        double dh = image.getHeight();
        int width = (int) (dw * scalingFactor);
        int height = (int) (dh * scalingFactor);
        
        var scaledImage = new BufferedImage(width, height, image.getType());
        AffineTransform at = AffineTransform.getScaleInstance(scalingFactor, scalingFactor);
        AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaledImage = ato.filter(image, scaledImage);
        image = scaledImage;
        String scaledMessage = " -- TEMPORARILY SCALED";
        String title = Globals.frame.getTitle();
        if(title.endsWith(scaledMessage))
            title += " AGAIN";
        else if(title.endsWith(" AGAIN"))
            title += " AND AGAIN";
        else
            title += scaledMessage;
            
        repaint();
        Globals.frame.setTitle(title);
        //saveAs(scaledImage);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    void saveCurrentSize() {
        if (!prefs.fitToWindow || image == null) {
            // TODO: Simply disable the menu item!
            errorMessage("Image is full size -- or there is no image loaded", "Save Current Size");
            return; 
       }
        Rectangle r = Utils.fitToWindow(new Dimension(parentPane.getViewport().getWidth(), parentPane.getViewport().getHeight()), new Dimension(image.getWidth(), image.getHeight()));
        double scalingFactor = (double) r.width / (double) image.getWidth();
        showScaled(scalingFactor);
    }
    boolean hasImageLoaded() {
        // used for enabling/disabling menu items
        return image != null;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                     CODE DUMP 
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//    public static BufferedImage rotateImageTest(BufferedImage theImage, int quadrants) {
//
//        int w0 = theImage.getWidth();
//        int h0 = theImage.getHeight();
//        int w1 = w0;
//        int h1 = h0;
//        int centerX = w0 / 2;
//        int centerY = h0 / 2;
//
//        if (quadrants % 2 == 1) {
//            w1 = h0;
//            h1 = w0;
//        }
//
//        if (quadrants % 4 == 1) {
//            centerX = h0 / 2;
//            centerY = h0 / 2;
//        } else if (quadrants % 4 == 3) {
//            centerX = w0 / 2;
//            centerY = w0 / 2;
//        }
//
//        AffineTransform affineTransform = new AffineTransform();
//        affineTransform.setToQuadrantRotation(quadrants, centerX, centerY);
//        AffineTransformOp opRotated = new AffineTransformOp(affineTransform,
//                AffineTransformOp.TYPE_BILINEAR);
//        BufferedImage transformedImage = new BufferedImage(w1, h1,
//                theImage.getType());
//        transformedImage = opRotated.filter(theImage, transformedImage);
//
//        return transformedImage;
//
//    }
//    void rotatex(int degrees) {
//        if (image == null)
//            return;
//        int w = image.getWidth();
//        int h = image.getHeight();
//
//        var transform = AffineTransform.getRotateInstance(Math.toRadians(degrees), w / 2, h / 2);
//        var op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
//        image = op.filter(image, null);
//        repaint();
//
//        /**
//         * var newImage = new BufferedImage(h, w, image.getType()); Graphics2D g2 = newImage.createGraphics(); g2.rotate(Math.toRadians(degrees), w/2, h/2);
//         * g2.drawImage(image, null, 0, 0); image=newImage; **
//         */
//    }


}
