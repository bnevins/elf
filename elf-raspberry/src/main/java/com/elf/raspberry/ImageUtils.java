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

    /**
     * public static Image scaleImage(Image image, int screenWidth, int screenHeight) {
        int picWidth = image.getWidth(null);
        int picHeight = image.getHeight(null);

        if (picWidth <= 0 || picHeight <= 0) {
            throw new RuntimeException("add Lazy calculation of image dimensions!");
        }
        Dimension d = scaleImage(picWidth, picHeight, screenWidth, screenHeight);
        return image.getScaledInstance(d.width, d.height, Image.SCALE_DEFAULT);
    }
*/
    public static Dimension scaleImage(boolean fillScreen, int picWidth, int picHeight, int screenWidth, int screenHeight) {
        double doubleWidth = picWidth;
        double doubleHeight = picHeight;
        Dimension dimensions = new Dimension(picWidth, picHeight);
        System.out.printf("scaleImage: pic: %dX%d, screen: %dX%d\n", picWidth, picHeight, screenWidth, screenHeight);
        if (picWidth <= 0 || picHeight <= 0) {
            throw new RuntimeException("add Lazy calculation of image dimensions!");
        }
        double xRatio = (double) screenWidth / doubleWidth;
        double yRatio = (double) screenHeight / doubleHeight;
        // whichever is LESS is the "limiting" dimension
        if (xRatio == 1.0 && yRatio == 1.0) {
            return dimensions;
        }
        double scaleFactor;
        if (xRatio <= yRatio) {
            if (fillScreen) {
                scaleFactor = yRatio;
            } else {
                scaleFactor = xRatio;
            }
        } else {
            if (fillScreen) {
                scaleFactor = xRatio;
            } else {
                scaleFactor = yRatio;
            }
        }

        dimensions.width = (int) (doubleWidth * scaleFactor);
        dimensions.height = (int) (doubleHeight * scaleFactor);

        System.out.println(
                "scaleImage calculated: " + dimensions);
        return dimensions;
    }
}
