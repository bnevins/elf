/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.io;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Byron Nevins
 */
public class FileMoverByDate {
    public static void main(String[] args) {
        if (args.length < 1) {
            //TODO Arg Processor
            System.out.println("Usage FileMoverByDate x[--prefix xyz root-dir");
            System.exit(1);
        }
        try {
            int index = 0;

            if (args[0].equals("--prefix")) {
                FileInfo.setPrefix(args[1]);
                index = 2;
            }

            FileMoverByDate fm = new FileMoverByDate(args[index]);

            if ((args.length - index) >= 1) {
                fm.setup(args[index]);
                fm.organize();
            }
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }

    public FileMoverByDate(String rootdir) {
        rootDir = rootdir;
    }

    private List<File> getJpgs() throws IOException {
        FileFinder ff = new FileFinder(rootDir, new FileExtFilter(FileInfo.JPG));
        return ff.getFiles();
    }

    private void setup(String string) throws IOException {
        FileFinder ff = new FileFinder(rootDir, new FileExtFilter(FileInfo.JPG));
        files = ff.getFiles();

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
    final String rootDir;
}
