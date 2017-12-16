package com.elf.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class JPEGUtils_sortofworks {

    /**
     * Reads an image in a file and writes it back out to another file.
     *
     * @param orig The name of image file.
     * @param copy The name of copy file. Will be created if necessary.
     */
    static void JPEG2JPEG(String fname, String indir, String outdir) {
        String in = indir + File.separator + fname;
        String out = outdir + File.separator + fname;

        if (out.toLowerCase().endsWith(".jpeg")) {
            out = out.substring(0, out.length() - 5).concat(".jpg");
        }

        JPEG2JPEG(in, out);
    }

    ///////////////////////////////////////////////////////////////////////////////
    static void JPEG2JPEG(String orig, String copy) {
        pr("Converting " + orig + " to " + copy);
        try {
            // Get the image from a file.
            Image inImage = new ImageIcon(orig).getImage();

            // Determine the sizes.
            int ht = inImage.getHeight(null);
            int width = inImage.getWidth(null);

            // Create an image buffer in which to paint on.
            BufferedImage outImage = new BufferedImage(width, ht, BufferedImage.TYPE_INT_RGB);

            // Set the scale.
            AffineTransform tx = new AffineTransform();

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(inImage, tx, null);
            g2d.dispose();
            ImageIO.write(outImage, "jpeg", new File(copy));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            new File(orig).delete();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    /**
     * Reads an image in a file and creates a thumbnail in another file.
     *
     * @param orig The name of image file.
     * @param thumb The name of thumbnail file. Will be created if necessary.
     * @param maxDim The width and height of the thumbnail must be maxDim pixels
     * or less.
     */
    static void createThumbnail(String orig, String thumb, String scaling) {
        try {
            double scale;
            // Get the image from a file.
            Image inImage = new ImageIcon(orig).getImage();

            // Determine the scale.
            // if it is an int -- that means pixels, if it is a double -- that means
            // a scaling factor...
            try {
                int maxDim = Integer.parseInt(scaling);
                scale = (double) maxDim / (double) inImage.getHeight(null);

                if (inImage.getWidth(null) > inImage.getHeight(null)) {
                    scale = (double) maxDim / (double) inImage.getWidth(null);
                }
            }
            catch (NumberFormatException e) {
                scale = Double.parseDouble(scaling);
            }

            // Determine size of new image. One of them
            // should equal maxDim.
            int scaledW = (int) (scale * inImage.getWidth(null));
            int scaledH = (int) (scale * inImage.getHeight(null));

            // Create an image buffer in which to paint on.
            BufferedImage outImage = new BufferedImage(scaledW, scaledH,
                    BufferedImage.TYPE_INT_RGB);

            // Set the scale.
            AffineTransform tx = new AffineTransform();
            tx.scale(scale, scale);

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(inImage, tx, null);
            g2d.dispose();
            ImageIO.write(outImage, "jpeg", new File(thumb));

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    ///////////////////////////////////////////////////////////////////////////////
    /**
     * Reads an image in a file and creates a thumbnail in another file.
     *
     * @param orig The name of image file.
     * @param thumb The name of thumbnail file. Will be created if necessary.
     * @param maxDim The width and height of the thumbnail must be maxDim pixels
     * or less.
     */
    static void rotate(String orig, String copy, String direction) {
        try {
            double theta;
            // Get the image from a file.
            Image loadedImage = new ImageIcon(orig).getImage();

            // Determine theta.

            if (direction.equals("-")) {
                theta = Math.toRadians(-90.0);
            }
            else {
                theta = Math.toRadians(90.0);
            }

            // Determine size of new image. One of them
            // should equal maxDim.
            int scaledW = (int) (loadedImage.getWidth(null));
            int scaledH = (int) (loadedImage.getHeight(null));

            // Create an image buffer in which to paint on.
            BufferedImage inImage = new BufferedImage(scaledW, scaledH,
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2 = inImage.createGraphics();
            g2.drawImage(loadedImage, 0, 0, null);

            BufferedImage outImage = new BufferedImage(scaledH, scaledW,
                    BufferedImage.TYPE_INT_RGB);

            AffineTransform tx = AffineTransform.getRotateInstance(
                    theta, scaledW / 2, scaledH / 2);

            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

            //op.filter(inImage, outImage);

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(inImage, op, 0, 0);
            g2d.dispose();
            ImageIO.write(outImage, "jpeg", new File(copy));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    ///////////////////////////////////////////////////////////////////////////////
    /**
     * Reads an image in a file and dumps info about it.
     *
     * @param name The name of image file.
     */
    static void dumpInfo(String name) {
        // Get the image from a file.
        Image inImage = new ImageIcon(name).getImage();

        int h = inImage.getHeight(null);
        int w = inImage.getWidth(null);
        System.out.println(name + " -- width: " + w + ", Height: " + h);
    }

    ///////////////////////////////////////////////////////////////////////////////
    private static void pr(String s) {
        System.out.println(s);
    }
}
