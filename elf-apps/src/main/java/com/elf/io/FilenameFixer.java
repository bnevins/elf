/*
 * Youtube files frequently have unicode characters which can confuse apps.
 * This program will look for such files and change all non-ASCII characters to
 * an ascii character ("_")
*/
package com.elf.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class FilenameFixer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    
        File dir = FileUtils.safeGetCanonicalFile(new File("."));
        //System.out.println("dir: " + dir);
        FilenameFixer ff = null;
        try {
            ff = new FilenameFixer(dir);
        } catch (IOException ex) {
            Logger.getLogger(FilenameFixer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        ff.fix();
    }
   
    FilenameFixer(File dir) throws IOException {
        root = dir;
        FileFinder ff = new FileFinder(root.getPath());
        files = ff.getFiles();
    }

    private void fix() {
        System.out.println("****  NUM FILES:  " + files.size());
        int n = 0;
        for(File oldFile : files) {
            //System.out.println("FILE: " + f.getAbsolutePath());
            File newFile = convertNonAscii(oldFile);
            if(newFile != null) {
                System.out.printf("rename \"%s\" %s\n", oldFile.getPath(), newFile.getName());
                oldFile.renameTo(newFile);
            }
        }
    }
    private File convertNonAscii(File f) {
        char[] name = f.getName().toCharArray();
        boolean changed = false;
        
        for(int i = 0; i < name.length; i++) {
            if(isNonAscii(name[i]) || name[i] == '\'' || (convertSpacesToo && isSpace(name[i])) ) {
                changed = true;
                name[i] = '_';
            }
        }

        if(changed)
            return new File(f.getParentFile(), new String(name));

        return null;
    }
    private final static boolean isNonAscii(char c) {
        return c > 127;
    }
    private final static boolean isSpace(char c) {
        return c == ' ';
    }
    
    private final File root;
    private final List<File> files;
    private final boolean convertSpacesToo = true;
}
