/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageScaler {

    private final double imageW;
    private final double imageH;
    private final double deviceW;
    private final double deviceH;
    private final double deviceRatio;
    private final double imageRatio;
    private double x;
    private double y;
    private boolean noBars = false;

    public ImageScaler(int iw, int ih, int dw, int dh) {
        this.imageW = (double) iw;
        this.imageH = (double) ih;
        this.deviceW = (double) dw;
        this.deviceH = (double) dh;
        deviceRatio = deviceW / deviceH;
        imageRatio = imageW / imageH;
    }

    public final Rectangle scale() {
        boolean tall = (imageRatio < 1.0);
        double scaleW;
        double scaleH;
        if (noBars) {
            if (tall) { // width-priority
                scaleW = deviceW;
                scaleH = scaleW / imageRatio;
                x = 0;
                y = (scaleH - deviceH) / 2;

            } else { // height-priority
                scaleH = deviceH;
                scaleW = scaleH * imageRatio;
                y = 0;
                x = (scaleW - deviceW) / 2;
            }

        } else { // bars means entire pic shown with blank bars
            if (tall) {
                scaleH = deviceH;
                scaleW = scaleH * imageRatio;
                y = 0;
            } else {
                scaleW = deviceW;
                scaleH = scaleW / imageRatio;
                x = 0;
            }
            x = (deviceW - scaleW) / 2.0;
            y = (deviceH - scaleH) / 2.0;
        }
        return new Rectangle((int) x, (int) y, (int) scaleW, (int) scaleH);
    }

    public static void main(String[] args) throws Exception {
        int imageW = Integer.parseInt(args[0]);
        int imageH = Integer.parseInt(args[1]);
        int deviceW = Integer.parseInt(args[2]);
        int deviceH = Integer.parseInt(args[3]);
        boolean fillDevice = Boolean.parseBoolean(args[4]);

        ImageScaler scaler = new ImageScaler(imageW, imageH, deviceW, deviceH);
        scaler.setNoBars(fillDevice);
        Rectangle scaled = scaler.scale();
        System.out.println("New Dimensions: " + scaled);
    }

    public void setNoBars(boolean noBars) {
        this.noBars = noBars;
    }
}
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @param image The image to be scaled
 * @param imageType Target image type, e.g. TYPE_INT_RGB
 * @param newWidth The required width
 * @param newHeight The required width
 *
 * @return The scaled image
 */
//    public static BufferedImage scaleImage(BufferedImage image, int imageType,
//            int newWidth, int newHeight) {
//        // Make sure the aspect ratio is maintained, so the image is not distorted
//        double thumbRatio = (double) newWidth / (double) newHeight;
//        int imageWidth = image.getWidth(null);
//        int imageHeight = image.getHeight(null);
//        double aspectRatio = (double) imageWidth / (double) imageHeight;
//
//        if (thumbRatio < aspectRatio) {
//            newHeight = (int) (newWidth / aspectRatio);
//        } else {
//            newWidth = (int) (newHeight * aspectRatio);
//        }
//
//        // Draw the scaled image
//        BufferedImage newImage = new BufferedImage(newWidth, newHeight,
//                imageType);
//        Graphics2D graphics2D = newImage.createGraphics();
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);
//
//        return newImage;
//    }

