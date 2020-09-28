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
    private boolean clipOk = false; // default is do NOT clip any of the image
    private boolean clipFromTopOnly = false;
    private boolean debug = false;

    public ImageScaler(int iw, int ih, int dw, int dh) {
        this.imageW = (double) iw;
        this.imageH = (double) ih;
        this.deviceW = (double) dw;
        this.deviceH = (double) dh;
        deviceRatio = deviceW / deviceH;
        imageRatio = imageW / imageH;
    }

    public final Rectangle scale() {
        double scaleW;
        double scaleH;
        boolean stretchWidthToFit;

        if (imageRatio > deviceRatio && clipOk) {
            stretchWidthToFit = false;
        } else if ((imageRatio > deviceRatio)) {
            stretchWidthToFit = true;
        } else if (clipOk) {
            stretchWidthToFit = true;
        } else {
            stretchWidthToFit = false;
        }
        System.out.println("stretchwidthtofit = " + stretchWidthToFit);

        if ((stretchWidthToFit)) {
            scaleW = deviceW;
            scaleH = scaleW / imageRatio;
            x = 0;
            if (clipFromTopOnly) {
                y = deviceH - scaleH;
            } else {
                y = (deviceH - scaleH) / 2;
            }
            if (debug) {
                System.out.println("Width Stretched to fit");
            }
        } else {
            scaleH = deviceH;
            scaleW = deviceH * imageRatio;
            y = 0;
            x = (deviceW - scaleW) / 2;
            if (debug) {
                System.out.println("Height Stretched to fit");
            }
        }

        if (!clipOk && (scaleH > deviceH || scaleW > deviceW)) {
            //can't happen!!
            throw new RuntimeException("ERROR in Scaler -- " + scaleW + "X" + scaleH);
        }
        System.out.println("IR, DR = " + imageRatio + ", " + deviceRatio);
        Rectangle r = new Rectangle((int) x, (int) y, (int) scaleW, (int) scaleH);
        return r;
    }

    public static void main(String[] args) throws Exception {
        int imageW = Integer.parseInt(args[0]);
        int imageH = Integer.parseInt(args[1]);
        int deviceW = Integer.parseInt(args[2]);
        int deviceH = Integer.parseInt(args[3]);
        boolean clipOK = Boolean.parseBoolean(args[4]);
        boolean clipTopOnly = Boolean.parseBoolean(args[5]);

        ImageScaler scaler = new ImageScaler(imageW, imageH, deviceW, deviceH);
        scaler.setClipOk(clipOK);
        scaler.setClipFromTopOnly(clipTopOnly);
        Rectangle scaled = scaler.scale();
        System.out.println("New Dimensions: " + scaled);
    }

    public void setClipOk(boolean clip) {
        this.clipOk = clip;
    }

    void setClipFromTopOnly(boolean b) {
        clipFromTopOnly = b;
    }

    void setDebug(boolean b) {
        debug = b;
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

