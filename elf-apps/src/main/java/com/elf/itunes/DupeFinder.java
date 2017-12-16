/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.itunes;

import com.elf.args.Arg;
import com.elf.args.ArgProcessor;
import com.elf.args.BoolArg;
import com.elf.io.LeafDirFinder;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bnevins
 */
public class DupeFinder {

    public static void main(String[] args) {
        if (args.length == 0)
            usage();
        try {
            long start = System.currentTimeMillis();
            ArgProcessor proc = new ArgProcessor(argDescriptions, args);
            Map<String, String> params = proc.getParams();
            List<String> operands = proc.getOperands();
            DupeFinder df = new DupeFinder(params, operands);
            System.out.println("Time: " + (System.currentTimeMillis() - start) + " msec");
        } catch (Exception ex) {
            Logger.getLogger(DupeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DupeFinder(Map<String, String> params, List<String> operands) throws IOException {
        verbose = Boolean.parseBoolean(params.get("verbose"));
        delete = Boolean.parseBoolean(params.get("delete"));
        ignoreCase = !Boolean.parseBoolean(params.get("noic"));
        ignoreLeadingNumbers = !Boolean.parseBoolean(params.get("noignoretracknums"));
        File root = new File(operands.get(0));
        LeafDirFinder fdf = new LeafDirFinder(root);
        List<File> leaves = fdf.getFiles();

        for (File dir : leaves) {
            handleAlbum(dir);
        }

        System.out.printf("Keep: %d, Dupe: %d\n", keeps.size(), dupes.size());

        if (verbose)
            for (File f : keeps) {
                System.out.printf("KEEP: %s\n", f);
            }

        for (File f : dupes) {
            if (verbose)
                System.out.println("DUPE: " + f);

            if (delete)
                f.delete();
        }

        if (!delete)
            writeDeleteScript();
    }

    private void handleAlbum(File dir) {
        // e.g. "01 Foobarski.mp3" and ""01 Foobarski 1.mp3""
        set.clear();
        File[] songsArray = dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".mp3");
            }
        });
        // sort them so that "01foo.mp3" is ahead of "01foo1.mp3"
        sort(songsArray);
        List<File> songs = new ArrayList<File>();

        for (File f : songsArray) {
            songs.add(f);
        }

        do {
            handleNextGroup(songs);
        } while (songs.size() > 0);
    }

    private void handleNextGroup(List<File> songs) {
        if (songs.size() <= 0)
            return;

        File firstSong = songs.get(0);
        songs.remove(0);
        keeps.add(firstSong);

        String rootName = firstSong.getName();
        rootName = massageName(rootName.substring(0, rootName.length() - 4));
        int len = rootName.length();
        Iterator<File> it = songs.iterator();

        while (it.hasNext()) {
            File song = it.next();
            String name = song.getName();

            if (name.length() < len + 4)
                return;

            name = massageName(song.getName().substring(0, len));

            if (name.equals(rootName)) {
                dupes.add(song);
                it.remove();
            } else
                return;
        }
    }

    private void sort(File[] songs) {
        Arrays.sort(songs, new Comparator() {

            public int compare(Object o1, Object o2) {

                String name1 = ((File) o1).getName();
                String name2 = ((File) o2).getName();

                name1 = name1.substring(0, name1.length() - 4);
                name2 = name2.substring(0, name2.length() - 4);

                if (ignoreCase) {
                    name1 = name1.toLowerCase();
                    name2 = name2.toLowerCase();
                }
                return name1.compareTo(name2);
            }
        });
    }

    private void writeDeleteScript() throws FileNotFoundException {
        File outfile = new File("delete_dupes.bat");
        PrintWriter pw = new PrintWriter(outfile);

        pw.printf("REM ---\nREM ---  Computer Generated script from %s\nREM ---\n", getClass().getName());

        for (File f : dupes)
            pw.printf("del \"%s\"\n", f.getAbsolutePath());

        pw.close();
        System.out.println("Wrote script to " + outfile.getAbsolutePath());
    }

    private static void usage() {
        System.out.println("DupeFinder Usage:  DupeFinder Root-Path\n");
        System.out.println(Arg.toHelp(argDescriptions));
        System.exit(1);
    }
    private final static Arg[] argDescriptions = new Arg[]{
        new BoolArg("verbose", "v", false, "Verbose"),
        new BoolArg("delete", "d", false, "Delete the files"),
        new BoolArg("noic", "i", false, "No ignore-case"),
        new BoolArg("noignoretracknums", "j", false, "No ignore leading track numbers"),
        new Arg("ext", "x", "mp3", "File Extensions"),};
    private final List<File> dupes = new ArrayList<File>();
    private final List<File> keeps = new ArrayList<File>();
    Set<String> set = new TreeSet<String>();
    private final boolean verbose;
    private final boolean delete;
    private final boolean ignoreCase;
    private final boolean ignoreLeadingNumbers;

    private String massageName(String name) {
        if (ignoreCase)
            name = name.toLowerCase();
        if(ignoreLeadingNumbers) {
            // TODO
        }
        return name;
    }
}
