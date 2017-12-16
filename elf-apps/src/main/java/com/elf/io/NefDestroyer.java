/*
 * FileRenamer.java
 *
 * Created on December 14, 2007, 1:30 AM
 *
 */
package com.elf.io;

import com.elf.args.Arg;
import com.elf.args.ArgProcessor;
import com.elf.args.BoolArg;
import com.elf.util.StringUtils;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NefDestroyer {

    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
            ArgProcessor proc = new ArgProcessor(argDescriptions, args);
            Map<String, String> params = proc.getParams();
            List<String> operands = proc.getOperands();
            String root = null;

            if (operands == null || operands.size() <= 0) {
                root = ".";
            } else {
                root = operands.get(0);
            }

            NefDestroyer fr = new NefDestroyer(root);
            fr.doit();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public NefDestroyer(String rootString) {
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
    }

    ////////////////////////////////////////////////////////////////////////////
    private void doit() throws IOException {
        nefs = getNefs();
        jpgs = getJpgs();

        // sort by filename
        Collections.sort(nefs);
        Collections.sort(jpgs);
        crunch();



        System.out.printf("%d NEFs and %d JPGs\n", nefs.size(), jpgs.size());
        System.out.println("" + matched.size() + " Matches...");
        System.out.println("" + orphanJpgs.size() + " Orphaned JPEGs...");
        System.out.println("" + orphanNefs.size() + " Orphaned NEFs...");
        System.out.println("" + matchedDifferentDirs.size() + " nef::jpg pairs in different directories...");
        System.out.println("" + matchedSameDirs.size() + "  nef::jpg pairs  in the same directory...");
        handleCommands();
    }

    ////////////////////////////////////////////////////////////////////////////

    private void handleCommands() {
        if(console == null) {
            System.out.println("NO CONSOLE attached!");
            return;
        }

        String reply = console.readLine(prompt);
        int cmd = -1;

        if(!StringUtils.ok(reply))
            reply = "-1";

        try {
            cmd = Integer.parseInt(reply, 16);
        }
        catch(Exception e) {
            // ignore
        }
        System.out.println("COMMAND: " + cmd);

        switch(cmd) {
            case 0:
                showDetails(); break;
            case 1:
                moveNefs(); break;
            case 2:
                deleteOrphanedNefs(); break;
            default:
                return;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private void deleteOrphanedNefs() {
         System.out.println("&&&&&&&   CUT  HERE  &&&&&&&&&&");

        for(FileAndName fan : orphanNefs) {
            System.out.printf("DEL \"%s\"\n", fan.f);
        }
        System.out.println("&&&&&&&   CUT  HERE  &&&&&&&&&&");
    }
    ////////////////////////////////////////////////////////////////////////////

    private void moveNefs() {
        System.out.println("&&&&&&&   CUT  HERE  &&&&&&&&&&");

        for(FileAndNamePair pair : matchedDifferentDirs) {
            File newPath = new File(pair.jpg.f.getParentFile(), pair.nef.f.getName());
            System.out.printf("MOVE \"%s\" \"%s\"\n", pair.nef.f, newPath);
        }

        System.out.println("&&&&&&&   CUT  HERE  &&&&&&&&&&");
    }


    ////////////////////////////////////////////////////////////////////////////

    private void showDetails() {
        System.out.println("*** Orphans ***");
        for (FileAndName fan : orphanJpgs) {
            System.out.println(fan.f);
        }
        for (FileAndName fan : orphanNefs) {
            System.out.println(fan.f);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private List<FileAndName> getNefs() throws IOException {
        FileFinder ff = new FileFinder(root, new FileExtFilter(FileInfo.NEF));
        List<File> flist = ff.getFiles();
        List<FileAndName> falist = new ArrayList<FileAndName>();

        for (File f : flist) {
            falist.add(new FileAndName(f));
        }
        return falist;
    }

    ////////////////////////////////////////////////////////////////////////////
    private List<FileAndName> getJpgs() throws IOException {
        FileFinder ff = new FileFinder(root, new FileExtFilter(FileInfo.JPG));
        List<File> flist = ff.getFiles();
        List<FileAndName> falist = new ArrayList<FileAndName>();

        for (File f : flist) {
            falist.add(new FileAndName(f));
        }
        return falist;
    }

    private static String getNameNoExtension(File f) {
        String s = f.getName();
        s = s.substring(0, s.length() - 4);
        //System.out.println("NAME: " + s + " ==> " + s2);
        return s;
    }

    private static void usage() {
        System.out.println("NefDestroyer Usage");
        System.out.println("");
        System.out.println(Arg.toHelp(argDescriptions));
        System.exit(1);
    }
    ////////////////////////////////////////////////////////////////////////////
    private final static Arg[] argDescriptions = new Arg[]{
        new BoolArg("recursive", "r", true, "Recursive"), //new Arg("dir", "d", ".", "Search Directory Root"),
    };

    ////////////////////////////////////////////////////////////////////////////
    private void crunch() {
        boolean found = false;

        for (FileAndName nef : nefs) {
            found = false;

            for (FileAndName jpg : jpgs) {
                if (nef.name.equals(jpg.name)) {
                    found = true;
                    FileAndNamePair pair = new FileAndNamePair(jpg, nef);
                    matched.add(pair);
                    //System.out.println("MATCH: " + pair);
                    break;
                }
            }
            if (!found) {
                orphanNefs.add(nef);
            }
        }

        if (jpgs.size() != matched.size()) {
            crunchOrphanJpgs();
        }

        crunchMatches();

    }

    ////////////////////////////////////////////////////////////////////////////
    private void crunchOrphanJpgs() {
        boolean found = false;

        for (FileAndName jpg : jpgs) {
            found = false;

            for (FileAndNamePair pair : matched) {
                if (jpg.name.equals(pair.jpg.name)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                orphanJpgs.add(jpg);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private void crunchMatches() {
        for (FileAndNamePair pair : matched) {
            File jf = pair.jpg.f.getParentFile();
            File nf = pair.nef.f.getParentFile();
            File nf_parent = nf.getParentFile();

            if (jf.equals(nf) || jf.equals(nf_parent)) {
                matchedSameDirs.add(pair);
            } else {
                matchedDifferentDirs.add(pair);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private final String root;
    private List<FileAndName> nefs;
    private List<FileAndName> jpgs;
    private List<FileAndNamePair> matched = new ArrayList<FileAndNamePair>();
    private List<FileAndNamePair> matchedDifferentDirs = new ArrayList<FileAndNamePair>();
    private List<FileAndNamePair> matchedSameDirs = new ArrayList<FileAndNamePair>();
    private List<FileAndName> orphanJpgs = new ArrayList<FileAndName>();
    private List<FileAndName> orphanNefs = new ArrayList<FileAndName>();

    private static final boolean batch = true;
    private static final Console console = System.console();
    private static final String prompt =
        "\n\n*****  Main Menu *****\n\n" +
        "0 Show Details\n" +
        "1 Move all nefs to the matching jpg's parent directory\n" +
        "2 Delete all Orphaned NEFs\n" +
        "X Exit\n";

    ////////////////////////////////////////////////////////////////////////////
    private static class FileAndName implements Comparable<FileAndName> {

        public FileAndName(File f) {
            this.f = f;
            name = NefDestroyer.getNameNoExtension(f);
        }

        @Override
        public int compareTo(FileAndName o) {
            // sort on the name -- not the path!
            return name.compareTo(o.name);
        }
        private File f;
        private String name;
    }

    ////////////////////////////////////////////////////////////////////////////
    private static class FileAndNamePair {

        public FileAndNamePair(FileAndName jpg, FileAndName nef) {
            this.jpg = jpg;
            this.nef = nef;
        }

        @Override
        public String toString() {
            return "name: " + jpg.name + ", jpg: " + jpg.f + ", nef: " + nef.f;
        }
        private FileAndName jpg;
        private FileAndName nef;
    }
    ////////////////////////////////////////////////////////////////////////////

    /*
    private static class FilenameNoExtensionComparator implements Comparator<File>
    {
    public int compare(File f1, File f2)
    {
    try {
    String s1 = getNameNoExtension(f1);
    String s2 = getNameNoExtension(f2);
    return s1.compareTo(s2);
    } catch (IOException ex) {
    Logger.getLogger(NefDestroyer.class.getName()).log(Level.SEVERE, null, ex);
    System.exit(1);
    return 1;
    }
    }
    }
     */
}

