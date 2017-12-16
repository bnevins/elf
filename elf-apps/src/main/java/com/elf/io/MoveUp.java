/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.io;

import java.io.*;
import java.util.*;
import java.util.ArrayList;

/**
 *
 * @author bnevins
 */
public class MoveUp {

    public static void main(String[] args) {
        if(args.length > 0)
            testRun = true;
        MoveUp mu = new MoveUp();
    }

    public MoveUp() {
        root = SmartFile.sanitize(new File("."));
        setup();
    }


    private void setup() {
        getSubdirs();
        getRootFiles();
        getFilesToMove();
        moveFiles();
    }

    private void moveFiles() {
        for(File f : files) {
            //System.out.println("move " + f);
            File newFile = new File(root, f.getName());
            System.out.println("FROM: " + f + "    TO: " + newFile);

            if(!testRun)
                f.renameTo(newFile);
        }
    }

    private void getSubdirs() {
        subdirs = root.listFiles(new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory();
            }
        });

        for (File f : subdirs) {
            debug(f.toString());
        }

    }

    private void getRootFiles() {
        // we can not have any duplicate filenames.  keep a list of ALL filenames
        // and error out if there is a duplicate.

        File[] nondirs = root.listFiles(new FileFilter() {

            public boolean accept(File f) {
                return !f.isDirectory();
            }
        });

        for (File f : nondirs) {
            debug("nondir: " + f);
            checkAndAdd(f.getName());
        }
    }

    private void getFilesToMove() {
        for(File dir : subdirs) {
            getFilesToMove(dir);
        }
    }

    private void getFilesToMove(File dir) {
        File[] nondirs = dir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                return !f.isDirectory();
            }
        });

        for(File f : nondirs) {
            checkAndAdd(f.getName());
            files.add(f);
        }
    }

    private void debug(String s) {
        if (debug) {
            System.out.println(s);
        }
    }

    private void checkAndAdd(String fname) {
        if (!filenames.add(fname)) {
            throw new RuntimeException("Dupe: " + fname);
        }
    }
    private boolean debug = true;
    private File root;
    private List<File> files = new ArrayList<File>();
    private Set<String> filenames = new HashSet<String>();
    File[] subdirs;
    private static boolean testRun = false;
}

