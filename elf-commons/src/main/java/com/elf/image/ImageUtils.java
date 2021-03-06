/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.image;

import com.elf.util.StringUtils;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageUtils {

    private ImageUtils() {
    }

    public static final Dimension getDimensions(String filename) {
        if (!StringUtils.ok(filename)) {
            throw new IllegalArgumentException("Bad filename)");
        }
        return getDimensions(new File(filename));
    }

    public static final Dimension getDimensions(File file) {
        BufferedImage bimg;
        Dimension dimensions;

        try {
            bimg = ImageIO.read(file);
            dimensions = new Dimension(bimg.getWidth(), bimg.getHeight());
        } catch (IOException ex) {
            dimensions = new Dimension(0, 0);
        }
        return dimensions;
    }

    public static final Dimension scaleImage(int imageWidth, int imageHeight, int newWidth, int newHeight) {
        // Make sure the aspect ratio is maintained, so the image is not distorted
        double thumbRatio = (double) newWidth / (double) newHeight;
        double aspectRatio = (double) imageWidth / (double) imageHeight;

        if (thumbRatio < aspectRatio) {
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newWidth = (int) (newHeight * aspectRatio);
        }
        return new Dimension(newWidth, newHeight);
    }
    public static void main(String[] args) {
        for (String arg : args) {
            Dimension d = getDimensions(arg);
            System.out.printf("%s: %dX%d\n", arg, d.width, d.height);
        }
    }
}
