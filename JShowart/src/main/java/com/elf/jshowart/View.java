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
public class View extends JPanel {

    private BufferedImage image;
    private File prevImageFile = null;
    private UserPreferences prefs = UserPreferences.get();
    private Model model = Model.get();
    private JScrollPane parentPane;
    private Dimension preferredSize;
    private int shrinkFactor = 1;
    private int prevShrinkFactor = 1;

    /**
     * Creates new form JShowartView
     */
    public View() {
        System.out.println("JShowartView Layout Manager = " + getLayout());
        Globals.view = this;
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null)
            return;

        var vp = parentPane.getViewport();
        var vpDimension = new Dimension(vp.getWidth(), vp.getHeight());
        var iDimension = new Dimension(image.getWidth(), image.getHeight());

//        System.out.println("COLOR is: " + g.getColor());
        if (prefs.fitToWindow) {
            Rectangle r = Utils.fitToWindow(vpDimension, iDimension);
            setBounds(r.getBounds());
            preferredSize = new Dimension(r.width, r.height);
            g.drawImage(image, r.x, r.y, r.width, r.height, null);
        } else {
            // if smaller then window -- center it.  If larger put upper-left in viewport upper-left
            Point p = Utils.centerImageInWindow(vpDimension, iDimension);
            g.drawImage(image, p.x, p.y, null);
            preferredSize = new Dimension(image.getWidth(), image.getHeight());
        }
    }

    void setShrinkFactor(int newShrink) {
        shrinkFactor = newShrink;
        setupImage(model.curr());
    }

    void nextImage() {
        nextImage(1);
    }

    void prevImage() {
        prevImage(1);
    }

    void nextImage(int numForward) {
        setupImage(model.next(numForward));
    }

    void prevImage(int numBack) {
        setupImage(model.prev(numBack));
    }

    private void setupImage(File imageFile) {
        if (imageFile == null) {
            image = null;
            return;
        }

        // if we have only 1 file in artlib, don't waste time re-reading it -- unless shrinkFactor changed!
        if (imageFile.equals(prevImageFile) && shrinkFactor == prevShrinkFactor)
            return;

        prevImageFile = imageFile;
        prevShrinkFactor = shrinkFactor;

        try {
            image = ImageIO.read(imageFile);
        } catch (IOException ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            image = null;
        }

        // should never happen but we don't ever want to divide by zero!
        if (shrinkFactor == 0)
            shrinkFactor = 1;

        if (shrinkFactor != 1) {
            double scaleFactor = 1.0 / (double) shrinkFactor;
            image = getScaledImage(image, scaleFactor);
        }

        Globals.frame.setTitle("JShowArt    " + imageFile.getAbsolutePath() + getScaledUIForTitle());
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        //invalidate();
        resetScrollbars();
        parentPane.revalidate();
        repaint();
        // TODO -- possible junk in window on initial draw.  This doesn't work -->invalidate();
    }

    private String getScaledUIForTitle() {
        String base = "     Shrunk to ";

        // can't use double in a switch
        // TODO change to enum
        if (shrinkFactor == 2)
            return base + "Half Size";
        if (shrinkFactor == 4)
            return base + "Quarter Size";
        if (shrinkFactor == 8)
            return base + "Eighth Size";
        return "";
    }

    private void resetScrollbars() {
        JScrollBar verticalScrollBar = parentPane.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = parentPane.getHorizontalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMinimum());
        horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
    }

    // TODO automatically add default extension to filename if missing
    public void save() {
        String saveDialogTitle = "Image Save";

        if (image == null) {
            Utils.errorMessage("No files loaded.", saveDialogTitle);
            return;
        }

        File currentImageFile = model.curr();

        if (currentImageFile == null) {
            Utils.errorMessage("No files loaded.", saveDialogTitle); //can't happen because image is not null!
            return;
        }

        String ext = Utils.getFileExtension(currentImageFile.getName());

        if (isOkToOverwrite(currentImageFile)) {
            try {
                ImageIO.write(image, ext, currentImageFile);
            } catch (IOException ex) {
                Utils.errorMessage(ex.toString(), saveDialogTitle);
            }
            Utils.successMessage(currentImageFile.toString() + " saved successfully", saveDialogTitle);
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
            Utils.errorMessage("The image file extension must be one of these: " + Utils.getArtFileExtensionsAsString(), "Unknown Image Type");
            return;
        }
        prefs.previousSaveAsFileParent = outfile.getParentFile();

        if (outfile.exists() && !isOkToOverwrite(outfile))
            return;
        String saveDialogTitle = "Image Save As";
        try {
            ImageIO.write(theImage, Utils.getFileExtension(outfile), outfile);
        } catch (IOException ex) {
            Utils.errorMessage(ex.toString(), saveDialogTitle);
        }
        Utils.successMessage(outfile.toString() + " saved successfully", saveDialogTitle);
    }

    private boolean isOkToOverwrite(File f) {
        String question = "";
        if(shrinkFactor != 1)
            question = "WARNING This will save the file Shrunk by 1/" + shrinkFactor +"\n";
        
        question += "Overwrite " + f.getName();
        int selection = JOptionPane.showConfirmDialog(Globals.frame, question, "Overwrite File",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return selection == JOptionPane.YES_OPTION;
    }

    void rotate(int degrees) {
        if (image == null)
            return;

        image = getRotatedImage(image, degrees / 90);
        save();
        repaint();
    }

    public BufferedImage getRotatedImage(BufferedImage theImage, int quadrants) {

        int w0 = theImage.getWidth();
        int h0 = theImage.getHeight();
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

        BufferedImage transformedImage = opRotated.filter(theImage, null);
        return transformedImage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private BufferedImage getScaledImage(BufferedImage theImage, double scalingFactor) {
        if (theImage == null)
            return null;

        double dw = theImage.getWidth();
        double dh = theImage.getHeight();
        int width = (int) (dw * scalingFactor);
        int height = (int) (dh * scalingFactor);

        var scaledImage = new BufferedImage(width, height, theImage.getType());
        AffineTransform at = AffineTransform.getScaleInstance(scalingFactor, scalingFactor);
        AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        return ato.filter(theImage, scaledImage);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
   
    void saveCurrentSizeAs() {
        if (!prefs.fitToWindow || image == null) {
            // can't happen!  The menu item is supposed to be disabled
            Utils.errorMessage("Image is full size -- or there is no image loaded", "Save Current Size");
            return;
        }
        Rectangle r = Utils.fitToWindow(new Dimension(parentPane.getViewport().getWidth(), parentPane.getViewport().getHeight()), new Dimension(image.getWidth(), image.getHeight()));
        double scalingFactor = (double) r.width / (double) image.getWidth();
        var scaledImage = getScaledImage(image, scalingFactor);
        saveAs(scaledImage);
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
