/*
 * FileExtFinder.java
 *
 * Created on May 25, 2003, 11:04 AM
 */
package com.elf.io;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 *
 * @author  bnevins
 */
public class LeafDirFinder extends FileFinder {

    public LeafDirFinder(File rootDir0) {
         super(rootDir0.getPath(), new JustDirs());
        rootDir = rootDir0;

        if (!rootDir.isDirectory())
            throw new IllegalArgumentException("You can only call LeafDirFinder with a Directory argument.  This isn't a directory: " + rootDir);

        wantDirs = true;
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public List<File> getFiles() throws IOException {
        List<File> files = super.getFiles();	// *ALL* of the directories
        files.add(rootDir);
        List<File> keep = new ArrayList<File>();

        for (File f : files) {
            // only keep directories that have no subdirs
            if (isLeaf(f))
                keep.add(f);
        }

        return keep;
    }

    private boolean isLeaf(File dir) {
        File[] files = dir.listFiles();

        for (File f : files) {
            if (f.isDirectory())
                return false;
        }
        return true;
    }
    ///////////////////////////////////////////////////////////////////////////

    private static class JustDirs implements FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory())
                return true;

            return false;
        }
    }
    private final File rootDir;
}

