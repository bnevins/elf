/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author bnevins
 */
public class ImageUtils {

    public static Image scaleImage(Image image, int screenWidth, int screenHeight) {
        int picWidth = image.getWidth(null);
        int picHeight = image.getHeight(null);

        if (picWidth <= 0 || picHeight <= 0) {
            throw new RuntimeException("add Lazy calculation of image dimensions!");
        }
        Dimension d = scaleImage(picWidth, picHeight, screenWidth, screenHeight);
        return image.getScaledInstance(d.width, d.height, Image.SCALE_DEFAULT);
    }

    public static Dimension scaleImage(int picWidth, int picHeight, int screenWidth, int screenHeight) {
        double doubleWidth = picWidth;
        double doubleHeight = picHeight;
        Dimension dimensions = new Dimension(picWidth, picHeight);
        System.out.printf("scaleImage: pic: %dX%d, screen: %dX%d\n", picWidth, picHeight, screenWidth, screenHeight );
        if (picWidth <= 0 || picHeight <= 0) {
            throw new RuntimeException("add Lazy calculation of image dimensions!");
        }
        double xRatio = (double) screenWidth / doubleWidth;
        double yRatio = (double) screenHeight / doubleHeight;
        // whichever is LESS is the "limiting" dimension
        if (xRatio >= 1.0 && yRatio > 1.0) {
            ; // return dimensions;
        } else if (xRatio <= yRatio) {
            dimensions.width = (int) (doubleWidth * xRatio);
            dimensions.height = (int) (doubleHeight * xRatio);
        } else { // yRatio is smaller
            dimensions.width = (int) (doubleWidth * yRatio);
            dimensions.height = (int) (doubleHeight * yRatio);
        }
        System.out.println("scaleImage calculated: " + dimensions);
        return dimensions;
    }

    public static BufferedImage scaleImage(BufferedImage image, int imageType,
            int screenWidth, int screenHeight) {
        // Make sure the aspect ratio is maintained, so the image is not distorted
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        Dimension newSize = scaleImage(imageWidth, imageHeight, screenWidth, screenHeight);
        // Draw the scaled image
        BufferedImage newImage = new BufferedImage(newSize.width, newSize.height, imageType);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, newSize.width, newSize.height, null);

        return newImage;
    }

}
