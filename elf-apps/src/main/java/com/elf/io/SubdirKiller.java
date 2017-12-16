/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.io;

import com.elf.util.StringUtils;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class SubdirKiller {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length <= 0 || !StringUtils.ok(args[0])) {
            usage();
            System.exit(1);
        } else if (args.length > 1) {
            dryRun = true;
        }

        try {
            doit(args[0]);
        } catch (IOException ex) {
            Logger.getLogger(SubdirKiller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void usage() {
        System.out.println("Usage:  SubdirKiller name [--dry-run] \n" +
                "All subdirs named 'name' will be whacked.\n" +
                "--dry-run does not really delete.");
    }

    private static void doit(String name) throws IOException {
        FileFinder ff = new FileDirFinder(".");
        List<File> files = ff.getFiles();

        for (File f : files) {
            if (f.getName().equals(name)) {
                if (dryRun) {
                    System.out.println("Match: " + f);
                } else {
                    FileUtils.whack(f);
                    System.out.println("Hosed down: " + f);
                }
            }

        }

        if (dryRun) {
            System.out.println("No directories were really deleted.");
        }
    }
    private static boolean dryRun = false;
}
