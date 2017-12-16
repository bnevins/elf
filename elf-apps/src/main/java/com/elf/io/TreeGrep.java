/*
 * TreeGrep.java
 *
 * Created on Oct 19, 2007, 9:36:04 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author bnevins
 */
public class TreeGrep {

    TreeGrep(Map<String, String> params, List<String> operands) throws IOException {
        try {
            String types = params.get("ext");
            exact = params.get("exact");

            if (types.equalsIgnoreCase("all"))
                filetypes = null; // null means ALL files
            else
                filetypes = params.get("ext").toString().split(",");

            ignoreCase = Boolean.parseBoolean(params.get("ic"));
            regexp = Boolean.parseBoolean(params.get("regexp"));
            prevLine = params.get("prevline");

            if (ignoreCase && prevLine != null)
                prevLine = prevLine.toLowerCase();

            String search = operands.get(0);

            if (ignoreCase)
                searchString = search.toLowerCase();
            else
                searchString = search;

            File f = SmartFile.sanitize(new File(params.get("dir")));
            filenameOnly = Boolean.parseBoolean(params.get("filenameonly"));
            findAndGrep(f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    TreeGrep(File f, String types, String search) throws IOException
    {
    filetypes = types.split(",");

    if(ignoreCase)
    searchString = search.toLowerCase();
    else
    searchString = search;

    findAndGrep(f);
    }
     */
    void findAndGrep(File dir) throws IOException {
        if (!dir.isDirectory())
            throw new IOException(dir + " is NOT a directory!");

        File[] files = dir.listFiles();

        for (File curr : files) {
            if (curr.isDirectory())
                findAndGrep(curr);
            else if (exact != null) {
                if (curr.getName().equals(exact))
                    grep(curr);
            }
            else if (filetypes == null)
                grep(curr);
            else
                for (String type : filetypes) {
                    if (curr.getName().endsWith("." + type))
                        grep(curr);
                }
        }
    }

    private void grep(File f) throws IOException {
        //System.out.println("TRYING: " + f);
        LineNumberReader reader = new LineNumberReader(new FileReader(f));
        String line;
        String line2;
        boolean prevlineMatch = false;
        boolean isPrev = (prevLine != null);  // convenience...

        while ((line = reader.readLine()) != null) {
            if (ignoreCase)
                line2 = line.toLowerCase();
            else
                line2 = line;

            if (match(line2)) {
                boolean match = true;

                if (isPrev && !prevlineMatch)
                    match = false;

                if (match && filenameOnly) {
                    System.out.println(f);
                    prevlineMatch = false;
                    break;
                }
                if (match)
                    System.out.println(f + "[" + reader.getLineNumber() + "]:\n" + line);
            }

            if (isPrev)
                prevlineMatch = line2.indexOf(prevLine) >= 0;
        }
    }

    private boolean match(String s) {
        if (regexp)
            return s.matches(searchString);
        else
            return StringUtils.ok(s) && s.indexOf(searchString) >= 0;
    }

    private void processArgs(String... args) {
        if (args.length == 0 || (args.length == 1 && args[0].indexOf("help") >= 0)) {
        }

    }

    public static void main(String[] args) {
        if (args.length == 0)
            usage();
        try {
            long start = System.currentTimeMillis();
            ArgProcessor proc = new ArgProcessor(argDescriptions, args);
            Map<String, String> params = proc.getParams();
            List<String> operands = proc.getOperands();
            TreeGrep tg = new TreeGrep(params, operands);
            System.out.println("Time: " + (System.currentTimeMillis() - start) + " msec");
        }
        catch (IOException ex) {
            Logger.getLogger(TreeGrep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void usage() {
        System.out.println("TreeGrep Usage -- TreeGrep params searchstring");
        System.out.println("Use \"-x all\" for ALL file types.");
        System.out.println("");
        System.out.println(Arg.toHelp(argDescriptions));
        System.exit(1);
    }
    private String[] filetypes;
    private String searchString;
    private final static int BUFFER_SIZE = 1000000;
    private boolean ignoreCase = true;
    private boolean regexp = false;
    private boolean filenameOnly = false;
    private String exact;
    private String prevLine;
    private final static Arg[] argDescriptions = new Arg[]{
        new BoolArg("regexp", "r", false, "Regular Expression"),
        new Arg("dir", "d", ".", "Search Directory Root"),
        new Arg("ext", "x", "java", "File Extensions"),
        new Arg("exact", "e", false, "Exact Filename"),
        new BoolArg("ic", null, true, "Case Insensitive"),
        new BoolArg("filenameonly", "f", false, "Return Filenames Only"),
        new Arg("prevline", "p", false, "Find on previous line"),};
}
