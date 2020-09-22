package com.elf.image;

import com.elf.io.FileExtFinder;
import com.elf.io.FileFinder;
import com.elf.io.JpegFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevns
 */
public class JpegFileRenamer {

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                usage();
            }
            FileFinder ff = new FileFinder(args[0], new JpegFileFilter());
            List<File> files = ff.getFiles();

            for (File f : files) {
                new JpegFileRenamer().renameFile(f);
            }

        } catch (IOException ex) {
            Logger.getLogger(JpegFileRenamer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void usage() {
        System.out.println("JpegFileRenamer Root-Dir");
        System.exit(1);
    }

    public void renameFile(File f) {
        JpegReader reader = new JpegReader(f);
        String newName = reader.getFilenameFromTimestamp();

        if (newName == null) {
            throw new RuntimeException("Bad filename: " + newName);
        }

        File dir = f.getAbsoluteFile().getParentFile();
        File newFile = new File(dir, newName);
        f.renameTo(newFile);
        //System.out.println("Rename: " + f + " TO: " + newFile);
    }

}
