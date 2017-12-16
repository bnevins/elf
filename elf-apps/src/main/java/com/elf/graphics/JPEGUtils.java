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

class JPEGUtils {

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
     * @param scaling If it's an integer -- it's maximum pixel size. If it's a
     * double it means scaling factor. be maxDim pixels or less.
     */
    static void createThumbnail(String orig, String thumb, String scaling) {
        try {
            Image inImage = new ImageIcon(orig).getImage();
            double scale = calcScale(inImage, scaling);
            createThumbnail(inImage, thumb, scale);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    /**
     * Reads an image in a file and creates a thumbnail in another file.
     *
     * @param orig The name of image file.
     * @param thumb The name of thumbnail file. Will be created if necessary.
     * @param width the width of the thumbnail - set either height or width to 0
     * @param height the height of the thumbnail - set either height or width to
     * 0
     * @throws IllegalArgumentException if neither height or width is zero, if
     * they are negative, etc.
     */
    static void createThumbnail(String orig, String thumb, String widthS, String heightS) {
        String err = "Bad width and/or height.  One of them must be zero and the other must be a positive integer: "
                + "width: " + widthS + ", height: " + heightS;

        int width, height;

        try {
            width = Integer.parseInt(widthS);
            height = Integer.parseInt(heightS);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(err);
        }

        if ((width != 0 && height != 0) || (width == 0 && height == 0) || width < 0 || height < 0) {
            throw new IllegalArgumentException(err);
        }
        try {
            Image inImage = new ImageIcon(orig).getImage();
            double scale = calcScale(inImage, width, height);
            createThumbnail(inImage, thumb, scale);
        }
        catch (Exception e) {
            e.printStackTrace();
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
    private static void createThumbnail(Image inImage, String thumb, double scale) {
        try {
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

            // JPEG-encode the image and write to file.

            ImageIO.write(outImage, "jpeg", new File(thumb));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    private static double calcScale(Image image, String scaling) {
        // Determine the scale.
        // if it is an int -- that means pixels, if it is a double -- that means
        // a scaling factor...

        double scale;

        try {
            int maxDim = Integer.parseInt(scaling);
            scale = (double) maxDim / (double) image.getHeight(null);

            if (image.getWidth(null) > image.getHeight(null)) {
                scale = (double) maxDim / (double) image.getWidth(null);
            }
        }
        catch (NumberFormatException e) {
            scale = Double.parseDouble(scaling);
        }

        return scale;
    }
    ///////////////////////////////////////////////////////////////////////////////

    private static double calcScale(Image image, int w, int h) {
        double scale;

        if (w == 0 && h > 0) {
            scale = (double) h / (double) image.getHeight(null);
        }
        else if (h == 0 && w > 0) {
            scale = (double) w / (double) image.getWidth(null);
        }
        else {
            throw new IllegalArgumentException("Bad width and height");
        }

        return scale;
    }

    ///////////////////////////////////////////////////////////////////////////////
    static void rotate(String orig, String copy, String direction) {
        try {
            double theta;
            // Get the image from a file.
            Image loadedImage = new ImageIcon(orig).getImage();

            // Determine theta.

            if (direction.equals("-")) {
                theta = Math.toRadians(-90.0);
            }
            else if (direction.equals("+")) {
                theta = Math.toRadians(90.0);
            }
            else {
                theta = Math.PI;
            }

            // Determine size of new image. One of them
            // should equal maxDim.
            int scaledW = (int) (loadedImage.getWidth(null));
            int scaledH = (int) (loadedImage.getHeight(null));
            double originX = ((double) scaledW) / 2.0;
            double originY = ((double) scaledH) / 2.0;

            //AffineTransform tx = AffineTransform.getRotateInstance(theta, 0,0);
            AffineTransform tx = AffineTransform.getRotateInstance(theta, originX, originY);
            tx.translate(250, 250);
            // Set the scale.
            //AffineTransform tx = new AffineTransform();
            //tx.rotate(theta, originX, originY);

            BufferedImage outImage = new BufferedImage(scaledH, scaledW,
                    BufferedImage.TYPE_INT_RGB);

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(loadedImage, tx, null);
            g2d.dispose();
            ImageIO.write(outImage, "jpeg", new File(copy));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    static void rotate_old2(String orig, String copy, String direction) {
        try {
            double theta;
            // Get the image from a file.
            Image loadedImage = new ImageIcon(orig).getImage();

            // Determine theta.

            if (direction.equals("-")) {
                theta = Math.toRadians(-90.0);
            }
            else if (direction.equals("+")) {
                theta = Math.toRadians(90.0);
            }
            else {
                theta = Math.PI;
            }

            // Determine size of new image. One of them
            // should equal maxDim.
            int scaledW = (int) (loadedImage.getWidth(null));
            int scaledH = (int) (loadedImage.getHeight(null));
            double originX = ((double) scaledW) / 2.0;
            double originY = ((double) scaledH) / 2.0;

            AffineTransform tx = AffineTransform.getRotateInstance(theta, originX, originY);
            //tx.translate(100,0);
            // Set the scale.
            //AffineTransform tx = new AffineTransform();
            //tx.rotate(theta, originX, originY);

            BufferedImage outImage = new BufferedImage(scaledW, scaledH,
                    BufferedImage.TYPE_INT_RGB);

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(loadedImage, tx, null);
            g2d.dispose();
            ImageIO.write(outImage, "jpeg", new File(copy));
        }
        catch (IOException e) {
            e.printStackTrace();
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
    static void rotate_old(String orig, String copy, String direction) {
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

    //////////////////////////////////////////////////////////////	
    public static void main(String[] args) {
        if (args.length < 3) {
            System.exit(2);
        }

        JPEGUtils.rotate(args[0], args[1], args[2]);
    }
}
