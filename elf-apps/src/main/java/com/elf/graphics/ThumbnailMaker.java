package com.elf.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ThumbnailMaker {

    public ThumbnailMaker(String ImageName, String ThumbName) {
        inImage = new ImageIcon(ImageName).getImage();
        thumbName = ThumbName;
        imageName = ImageName;
        setup();
    }

    ///////////////////////////////////////////////////////////////////////////
    public ThumbnailMaker(URL imageURL, String ThumbName) {
        Image inImage = new ImageIcon(imageURL).getImage();

        thumbName = ThumbName;
        setup();
    }

    ///////////////////////////////////////////////////////////////////////////
    // bounding box
    public void create(Dimension d) {

        // todo: sanity check dimensions
        double maxW = d.getWidth();
        double maxH = d.getHeight();
        double scaleW = maxW / dW;
        double scaleH = maxH / dH;

        // go with the SMALLER scale;
        double scale = scaleH;

        if (scaleW < scaleH) {
            scale = scaleW;
        }

        int width = (int) (scale * dW);
        int height = (int) (scale * dH);

        create(width, height);
    }

    ///////////////////////////////////////////////////////////////////////////
    // double --> scale
    public void create(double scale) {
        // todo: sanity check scale

        int width = (int) (scale * dW);
        int height = (int) (scale * dH);

        create(width, height);
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * make the longest dimension equal to max
     */
    public void create(int max) {

        // todo: sanity check max
        double scale;
        double dmax = (double) max;
        int width = 0;
        int height = 0;

        if (iW >= iH) {
            scale = dmax / dW;
            width = max;
            height = (int) (scale * dH);
        }
        else {
            scale = dmax / dH;
            height = max;
            width = (int) (scale * dW);
        }

        create(width, height);
    }

    ///////////////////////////////////////////////////////////////////////////
    public void create(int width, int height) {
        try {
            double scale = (double) width / (double) iW;

            // todo: ht scale must be app. equal
            // Create an image buffer in which to paint on.
            BufferedImage outImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);

            // Set the scale.
            AffineTransform tx = new AffineTransform();

            // If the image is smaller than the thumbnail image size,
            // don't bother scaling.
            if (scale < 1.0d) {
                tx.scale(scale, scale);
            }

            // Paint image.
            Graphics2D g2d = outImage.createGraphics();

            g2d.drawImage(inImage, tx, null);
            g2d.dispose();
            ImageIO.write(outImage, "jpeg", new File(thumbName));
            setTimestamp();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        File f = new File("").getAbsoluteFile();
        System.out.println(f.getPath());
        String[] files = f.list();

        for (String s : files) {
            if (s.startsWith("tn_") || !s.endsWith(".JPG")) {
                continue;
            }

            ThumbnailMaker tnm = new ThumbnailMaker(f.getPath() + "/" + s, f.getPath() + "/" + "tn_" + s);

            tnm.create(new Dimension(200, 150));
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    private void setup() {
        iW = inImage.getWidth(null);
        iH = inImage.getHeight(null);
        dW = (double) iW;
        dH = (double) iH;

        // todo -- if either are zero throw Exception!
    }

    ///////////////////////////////////////////////////////////////////////////
    private void setTimestamp() {
        new File(thumbName).setLastModified(new File(imageName).lastModified());
    }
    ///////////////////////////////////////////////////////////////////////////
    private double dH;
    private double dW;
    private int iH;
    private int iW;
    private Image inImage;
    private String thumbName;
    private String imageName;
}
