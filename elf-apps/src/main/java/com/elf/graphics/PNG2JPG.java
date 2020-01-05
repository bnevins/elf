package com.elf.graphics;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * I'm RUSTY -- very simple old-fashioned code!
 *
 * @author bnevins
 */
class PNG2JPG {

    public static void main(String[] args) {
        File dir = null;
        if (args.length != 0) {
            usage();
        }
        try {
            dir = new File(".").getCanonicalFile();
            File[] files = dir.listFiles();
            for (File png : files) {
                if (png.isDirectory()) {
                    continue;
                }
                String pngName = png.getPath();
                if (!pngName.toLowerCase().endsWith(".png")) {
                    continue;
                }
                //System.out.println("GOT ONE: " + pngName);
                File jpg = new File(pngName.substring(0, pngName.length() - 3) + "jpg");
                //System.out.println("Convert to: " + jpg);
                JPEGUtils.PNG2JPEG(png, jpg);
            }
        } catch (IOException ex) {
            Logger.getLogger(PNG2JPG.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////////////////////////////	
    public static void usage() {
        System.out.println("\n\nUsage:\njava com.elf.graphics.PNG2JPG");
        System.out.println("\n PNG2JPG reads in all the png files in the current directory and writes out converted jpg files with the same root name.");
        System.exit(0);
    }

    //////////////////////////////////////////////////////////////	
}
