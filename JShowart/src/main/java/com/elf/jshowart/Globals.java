/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import javax.swing.filechooser.*;

/**
 *
 * @author bnevins
 */
public class Globals {

    private Globals() {

    }
    static JShowartApp app;
    static JShowartFrame frame;
    static JShowartView view;
    static FileNameExtensionFilter[] filters = {
        // the first filter will be the one set!
        new FileNameExtensionFilter("All Supported Types", "bmp", "gif", "jpg", "jpeg", "png", "tif", "tiff"),
        new FileNameExtensionFilter("JPEG images", "jpg", "jpeg"),
        new FileNameExtensionFilter("Bitmap images", "bmp", "wbmp"),
        new FileNameExtensionFilter("GIF images", "gif"),
        new FileNameExtensionFilter("PNG images", "png"),
        new FileNameExtensionFilter("TIFF images", "tif", "tiff"),};
}
