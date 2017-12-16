/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.io;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Byron Nevins
 */
public class DirCreator {
    public static void main(String[] args) {
        if (args.length < 2) {
            //TODO Arg Processor
            System.out.println("Usage DirCreator root-dir number_leading_chars");
            System.exit(1);
        }
        try {
            int num = Integer.parseInt(args[0]);
            DirCreator fm = new DirCreator(num);
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }

    

    public DirCreator(int num) {
        try {
            leadingChars = num;

            for (File f : getJpgs()) {
                System.out.println(f);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(DirCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<File> getJpgs() throws IOException {
        FileFinder ff = new FileFinder(".", new FileExtFilter(FileInfo.JPG));
        return ff.getFiles();
    }

    private void organize() {
        for (File f : files) {
            Date date = new Date(f.lastModified());
            int day = date.getDate();
            int month = date.getMonth() + 1;  // Humans like 1-based
            String s = String.format("2012%02d%02d", month, day);
            System.out.printf("mkdir %s\n", s);
            System.out.printf("move %s %s\n", f.getPath(), s);
            //Calendar c;
            //if(monthday > 31)
            //monthday = 1;
            //System.out.println("INT DAY = " + s);
            //System.out.println("f.lastModified();
        }
    }
    /////////////////////////////////////////////////
    private List<File> files;
    private int leadingChars;
}
