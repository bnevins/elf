/*
 * IndexChecker.java
 *
 * Created on February 9, 2004, 10:43 AM
 */
package com.elf.showart;

import com.elf.io.FileUtils;
import com.elf.util.logging.Reporter;
import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class IndexChecker {
    public IndexChecker(File dir) throws IOException {
        rootDir = dir;
        indexDir = FileUtils.safeGetCanonicalFile(new File(dir, "index"));

        if (!FileUtils.safeIsDirectory(dir)) {
            throw new IOException("Directory doesn't exist: " + dir);
        }
        if (!FileUtils.safeIsDirectory(indexDir)) {
            throw new IOException("Directory doesn't exist: " + indexDir);
        }
    }

    public String check() {
        String ret = "";
        getData();
        System.out.println("Number of subdirs: " + dirList.size() + ", Number of index files: " + indexList.size());

        // what dirs are not indexed?
        ArrayList<String> noIndex = new ArrayList<String>(dirList);
        noIndex.removeAll(indexList);
        Collections.sort(noIndex);

        // what indexes have no dir for them?
        ArrayList<String> noDir = new ArrayList<String>(indexList);
        noDir.removeAll(dirList);

        System.out.println("\n**** Directories not indexed ****");

        if (noIndex.size() <= 0) {
            System.out.println("** PERFECT MATCH ****");
        }

        for (Iterator it = noIndex.iterator(); it.hasNext();) {
            String s = (String) it.next();
            System.out.println(s);
        }
        System.out.println("\n**** Index jpeg with no dir ****");

        if (noDir.size() <= 0) {
            System.out.println("** PERFECT MATCH ****");
        }

        for (Iterator it = noDir.iterator(); it.hasNext();) {
            String s = (String) it.next();
            System.out.println(s);
        }


        return "";
    }

    private void getData() {
        File[] dirs = rootDir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory() && f.getName().compareToIgnoreCase("index") != 0) {
                    //dirList.add(f.getName().toLowerCase());
                    return true;
                }
                return false;
            }
        });

        File[] indexFiles = indexDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });

        dirList = new ArrayList<String>();
        indexList = new ArrayList<String>();

        for (int i = 0; i < dirs.length; i++) {
            String s = dirs[i].getName().toLowerCase();
            dirList.add(s);
            //System.out.println("Adding <dir>: " + s);
        }
        for (int i = 0; i < indexFiles.length; i++) {
            String s = indexFiles[i].getName();
            int index = s.lastIndexOf('.');
            s = s.substring(0, index).toLowerCase();
            indexList.add(s);
            //System.out.println("index file: " + s);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length <= 0) {
                args = new String[]{"."};
            }
            for (String dir : args) {
                File f = FileUtils.safeGetCanonicalFile(new File(dir));
                Reporter.info("Checking index for " + f);
                IndexChecker ic = new IndexChecker(f);
                ic.check();
            }
        }
        catch (Exception e) {
            Reporter.severe("" + e);
        }
    }
    private File rootDir;
    private File indexDir;
    private List<String> dirList;
    private List<String> indexList;
}
