package com.elf.graphics;

import com.elf.io.FileExtFinder;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * I'm RUSTY -- very simple old-fashioned code!
 *
 * @author bnevins
 */
class PNG2JPG {

    public static void main(String[] args) {
        File dir;
        boolean recursive = false;

        if (args.length > 1) {
            usage();
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("-r")) {
            recursive = true;
        }

        try {
            dir = new File(".").getCanonicalFile();
            File[] files = getFileList(dir, recursive);
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
    public static File[] getFileList(File root, boolean recursive) throws IOException{
        if (recursive) {
            FileExtFinder jf = new FileExtFinder(root.getAbsolutePath(), "png");
            List files = jf.getFiles();
            // Convert the list to an array of File objects
            return (File[]) files.toArray(new File[files.size()]);

        } else {
            return root.listFiles();
        }
    }

    //////////////////////////////////////////////////////////////	
    public static void usage() {
        System.out.println("\n\nUsage:\njava com.elf.graphics.PNG2JPG [-r]");
        System.out.println("\n PNG2JPG reads in all the png files in the current directory and writes out \nconverted jpg files with the same root name.");
        System.out.println("-r recursively converts files in all subdirectories as well.");
        System.exit(0);
    }

    //////////////////////////////////////////////////////////////	
}
