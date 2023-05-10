/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

/**
 *
 * @author bnevins ALL static methods...
 */
public class Utils {

    static public void debug(String format, Object ... args) {
        if(UserPreferences.get().isDebug())
            System.out.printf("DEBUG: " + format + "\n", args);
    }
    
    static public Rectangle fitToWindow(Dimension windowDim, Dimension imageDim) {
        var prefs = UserPreferences.get();

        // first figure out width & height of the image
        if ((windowDim.width != imageDim.width || windowDim.height != imageDim.height) && prefs.isFitToWindow()) {
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

    static public String[] getArtFileExtensions() {
        return artFileExtensions;
    }

    static public String getArtFileExtensionsAsString() {
        StringBuilder sb = new StringBuilder();

        for (String ext : artFileExtensions)
            sb.append(ext).append(" ");

        return sb.toString();
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

    static public String getFileExtension(File f) {
        return getFileExtension(f.getName());
    }

    static public String getFileExtension(String fname) {
        int dot = fname.lastIndexOf('.');
        if (dot <= 0)
            return null;

        return (fname.substring(dot + 1));

    }

    static public void recursiveGetComponent(Container container, String leading) {
        Component[] components = container.getComponents();

        for (Component c : components) {
            try {
                System.out.println(leading + c.getClass() + "\n");
                recursiveGetComponent((Container) c, leading + "    ");
            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }

    static Point centerImageInWindow(Dimension vpDimension, Dimension iDimension) {

        int x = (vpDimension.width - iDimension.width) / 2;
        int y = (vpDimension.height - iDimension.height) / 2;
        return new Point(x > 0 ? x : 0, y > 0 ? y : 0);
    }

    static int compare(long n1, long n2) {
        // for sorting...
        return (n1 > n2) ? 1 : (n1 < n2) ? -1 : 0;
    }

    static void errorMessage(String msg) {
        errorMessage(msg, "ERROR");
    }

    static void errorMessage(String msg, String title) {
        JOptionPane.showMessageDialog(Globals.controller, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    static void successMessage(String msg) {
        successMessage(msg, "JShowArt");
    }

    static void successMessage(String msg, String title) {
        JOptionPane.showMessageDialog(Globals.controller, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    static void warningMessage(String msg) {
        warningMessage(msg, "JShowArt");
    }

    static void warningMessage(String msg, String title) {
        JOptionPane.showMessageDialog(Globals.controller, msg, title, JOptionPane.WARNING_MESSAGE);
    }

    static void warningMessage(Component owner, String msg, String title) {
        JOptionPane.showMessageDialog(Globals.controller, msg, title, JOptionPane.WARNING_MESSAGE);
    }

    // Is this point visible on any monitor?
    public static boolean isVisible(int x, int y) {

        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            Rectangle screen = gd.getDefaultConfiguration().getBounds();

            if (screen.contains(x, y))
                return true;
        }
        return false;
    }

    private Utils() {
    }

    private static final Pattern artFilePattern;
    private static final String[] artFileExtensions = {"bmp", "gif", "tif", "png", "jpg", "jpeg"};    
    
    static {
        //String regex = "([^\\s]+(\\.(?i)(bmp|gif|tif|png|jpg|jpeg))$)";
        String regex = "(.+\\.(bmp|gif|tif|png|jpg|jpeg)$)";
        artFilePattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
}
