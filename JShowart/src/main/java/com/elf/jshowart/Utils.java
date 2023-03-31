/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bnevins ALL static methods...
 */
public class Utils {

    static public Rectangle fitToWindow(Dimension windowDim, Dimension imageDim) {
        var prefs = UserPreferences.get();

        // first figure out width & height of the image
        if ((windowDim.width != imageDim.width || windowDim.height != imageDim.height) && prefs.stretch) {
            // this makes the code MUCH more readable than a bunch of inline casts!!
            double dWindowWidth = windowDim.width;
            double dWindowHeight = windowDim.height;
            double dImageWidth = imageDim.width;
            double dImageHeight = imageDim.height;

            double xRatio = dWindowWidth / dImageWidth;
            double yRatio = dWindowHeight / dImageHeight;
            double ratio = xRatio <= yRatio ? xRatio : yRatio;
            imageDim.width = (int) (dImageWidth * ratio);
            imageDim.height = (int) (dImageHeight * ratio);
        }
        // now figure out the where to put image in the windowDim...
        Point origin = new Point(0, 0);

        if (imageDim.width < windowDim.width)
            origin.x = (windowDim.width - imageDim.width) / 2;

        if (imageDim.height < windowDim.height)
            origin.y = (windowDim.height - imageDim.height) / 2;

        return new Rectangle(origin, imageDim);
    }

    static public boolean isArtFile(String fname) {
        Matcher matcher = artFilePattern.matcher(fname);
        return matcher.matches();
    }
    static public boolean isArtFile(Path p) {
        return isArtFile(p.toFile());
    }
    static public boolean isArtFile(File f) {
        return isArtFile(f.getAbsolutePath());
    }

    static final Pattern artFilePattern;

    static {
        //String regex = "([^\\s]+(\\.(?i)(bmp|gif|tif|png|jpg|jpeg))$)";
        String regex = "(.+\\.(bmp|gif|tif|png|jpg|jpeg)$)";
        artFilePattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    private Utils() {
    }

}
