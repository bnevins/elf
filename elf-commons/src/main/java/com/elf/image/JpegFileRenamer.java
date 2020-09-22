package com.elf.image;

import com.elf.io.FileExtFinder;
import com.elf.io.FileFinder;
import com.elf.io.JpegFileFilter;
import com.elf.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TODO: 1. add cmdline option for prefix
 */
public class JpegFileRenamer {

    boolean verbose = false;
    boolean doRename = true;
    private static int renameCount = 0;
    private final String prefix;

    private static void usage() {
        System.out.println("JpegFileRenamer [-n][-v] directory-root\n"
                + "-n  Don't do the rename\n-v  Verbose output");
        System.exit(1);
    }

    private JpegFileRenamer(boolean doFileSystemChanges, boolean verbose, String prefix) {
        this.doRename = doFileSystemChanges;
        this.verbose = verbose;
        this.prefix = prefix;
    }

    public void renameFile(File f) {
        JpegReader reader = new JpegReader(f);
        String newName = reader.getFilenameFromTimestamp();
        if (newName == null) {
            throw new RuntimeException("Bad filename (" + newName + ") generated from: " + f);
        }
        if (StringUtils.ok(prefix)) {
            newName = prefix + newName;
        }

        File dir = f.getAbsoluteFile().getParentFile();
        File newFile = new File(dir, newName);

        if (doRename) {
            if (f.getAbsoluteFile().equals(newFile)) {
                if (verbose) {
                    System.out.println("No need to rename itself:" + newFile);
                }
                return;
            }
            if (!f.renameTo(newFile)) {
                System.out.println("Could NOT Rename: " + f + " TO: " + newFile);
            } else // it went OK
            {
                renameCount++;
            }
        } else {
            // no renaming -- so they want output!
            verbose = true;
        }

        if (verbose) {
            System.out.println("Rename: " + f + " TO: " + newFile);
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                usage();
            }
            boolean doFileSystemChanges = true;
            boolean verbose = false;
            String dirName = null;

            // 2 args are accepted "-n" will print the renames but not do them
            // -v means verbose output
            for (String arg : args) {
                if (arg.equals("-n")) {
                    doFileSystemChanges = false;
                } else if (arg.equals("-v")) {
                    verbose = true;
                } else {
                    dirName = arg;
                }
            }

            if (dirName == null) {
                usage();
            }

            FileFinder ff = new FileFinder(dirName, new JpegFileFilter());
            List<File> files = ff.getFiles();

            JpegFileRenamer renamer = new JpegFileRenamer(doFileSystemChanges, verbose, "BB_");
            for (File f : files) {
                renamer.renameFile(f);
            }
            System.out.printf("%d files renamed in directory %s\n", renameCount, dirName);

        } catch (IOException ex) {
            Logger.getLogger(JpegFileRenamer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
