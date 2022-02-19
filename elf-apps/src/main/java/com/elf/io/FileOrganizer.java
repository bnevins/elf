/*
 */
package com.elf.io;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
//import com.elf.util.StringUtils;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class FileOrganizer {


    private final String root;
    private final String oldPrefix;
    private final String newPrefix;
    
    public static void main(String[] args) {
        if (args.length < 3) {
            //TODO Arg Processor
            System.out.println("Usage FileOrganizer root-dir prefix new-prefix");
            System.exit(1);
        }
        try {
            FileOrganizer fo = new FileOrganizer(args[0], args[1], args[2]);
            fo.rename();
        } catch (IOException ex) {
            Logger.getLogger(FileOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private FileOrganizer(String aroot,  String aoldPrefix, String anewPrefix) {
        root = aroot;
        oldPrefix = aoldPrefix;
        newPrefix = anewPrefix;
    }

  ////////////////////////////////////////////////////////////////////////////
    
    private List<File> getJpgs() throws IOException 
    {
        FileFinder ff = new FileFinder(root, new FileExtFilter(FileInfo.JPG));
        return ff.getFiles();
    }
    
    private static void dump(List<File> jpgs) {
    jpgs.forEach(
            (temp) -> { System.out.println(temp); });    
    }    

    private void rename() throws IOException {
        List<File> jpgs = getJpgs();
        ListIterator<File> it = jpgs.listIterator();
        int prefixLength = oldPrefix.length();
        Set<String> newDirs = new HashSet<String>();
        
        while(it.hasNext()) {
            File f = it.next();
            String oldName = f.getName().toLowerCase();
            if(!oldName.startsWith(oldPrefix))
                continue;
            String oldNameNoPrefix = oldName.substring(prefixLength);
            if(!Character.isDigit(oldNameNoPrefix.charAt(0)))
                throw new RuntimeException("No start with num");
            
            TwoNums nums = parseNums(oldNameNoPrefix);
            //System.out.printf("oldname: %s   %d   %d\n", oldName, nums.one, nums.two);
            String newName = String.format("%s%03d-%03d.jpg", newPrefix, nums.one, nums.two);
            String dirName = String.format("%03d", nums.one);
            // only make the directory once!!
            if(!newDirs.contains(dirName)) {
                newDirs.add(dirName);
                System.out.println("mkdir " + dirName);
            }
            System.out.printf("move %s %s\\%s\n", oldName, dirName, newName);
            
            //System.out.println("RENAME: " + oldName + " to: " + newName);
        }
    }

    private TwoNums parseNums(String oldName) {
        // format: 123-456
        int dash = oldName.indexOf('-');
        if(dash < 0)
            throw new RuntimeException("No dash");
        int dot = oldName.indexOf('.');
        if(dot < 0)
            throw new RuntimeException("No dot");
        
        String firstNum = oldName.substring(0, dash);
        String secondNum = oldName.substring(dash + 1, dot);
        return new TwoNums(firstNum, secondNum);
    }
    private class TwoNums {
        int one, two;
        TwoNums(int a, int b) {
            one = a;
            two = b;
        }
        TwoNums(String a, String b) {
            one = Integer.parseInt(a);
            two = Integer.parseInt(b);
        }
    }
}
