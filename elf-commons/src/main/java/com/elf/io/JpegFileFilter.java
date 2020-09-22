/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.io;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author bnevns
 */
public class JpegFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }
        return pathname.getName().toLowerCase().endsWith(".jpg");
    }

    public static void main(String[] args) {
        File dir = new File(args[0]);
        File[] files = dir.listFiles(new JpegFileFilter());
        for (File f : files) {
            System.out.println(f.getAbsolutePath());
        }
    }
}
