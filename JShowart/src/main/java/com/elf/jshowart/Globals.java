/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elf.jshowart;

import javax.swing.*;
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
    static final JFileChooser chooser;

    static FileNameExtensionFilter[] filters = {
        // the first filter will be the one set!
        new FileNameExtensionFilter("All Supported Types", "bmp", "gif", "jpg", "jpeg", "png", "tif", "tiff"),
        new FileNameExtensionFilter("JPEG images", "jpg", "jpeg"),
        new FileNameExtensionFilter("Bitmap images", "bmp", "wbmp"),
        new FileNameExtensionFilter("GIF images", "gif"),
        new FileNameExtensionFilter("PNG images", "png"),
        new FileNameExtensionFilter("TIFF images", "tif", "tiff"),};

    static {
         /**
         *  Metal -- sucks
            Nimbus -- nice!
            CDE/Motif - very different
            Windows -- NICE!
            Windows Classic
         */
         try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(JShowartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);

        int numFilters = filters.length;

        // set the first one -- All Image Types -- then add the other ones
        chooser.setFileFilter(filters[0]);

        for (int i = 1; i < numFilters; i++)
            chooser.addChoosableFileFilter(Globals.filters[i]);
    }
}
