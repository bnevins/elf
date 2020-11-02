/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.raspberry;

import java.awt.Rectangle;

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
    private static boolean debug = false;

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
        debug("stretchwidthtofit = " + stretchWidthToFit);

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
                debug("Width Stretched to fit");
            }
        } else {
            scaleH = deviceH;
            scaleW = deviceH * imageRatio;
            y = 0;
            x = (deviceW - scaleW) / 2;
            debug("Height Stretched to fit");
        }

        if (!clipOk && (scaleH > deviceH || scaleW > deviceW)) {
            //can't happen!!
            throw new RuntimeException("ERROR in Scaler -- " + scaleW + "X" + scaleH);
        }
        debug("IR, DR = " + imageRatio + ", " + deviceRatio);
        Rectangle r = new Rectangle((int) x, (int) y, (int) scaleW, (int) scaleH);
        return r;
    }

    public void setClipOk(boolean clip) {
        this.clipOk = clip;
    }

    public void setClipFromTopOnly(boolean b) {
        clipFromTopOnly = b;
    }

    public void setDebug(boolean b) {
        debug = b;
    }

    private static void debug(String string) {
        if (debug) {
            System.out.println("DEBUG:: " + string);
        }
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
        debug("New Dimensions: " + scaled);
    }
}
