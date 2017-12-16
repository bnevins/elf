/*
 * ItunesDupeCleaner.java
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

/**
 * @author bnevins
 */

public class ItunesDupeCleaner
{
    public static void main(String[] args)
    {
        if(args.length == 0)
            usage();
        try {
            long start = System.currentTimeMillis();
            ArgProcessor proc = new ArgProcessor(argDescriptions, args);
            Map<String, String> params = proc.getParams();
            List<String> operands = proc.getOperands();

            ItunesDupeCleaner cleaner = new ItunesDupeCleaner(params, operands);
            System.out.println("Time: " + (System.currentTimeMillis() - start) + " msec");
        }
        catch (IOException ex) {
            Logger.getLogger(TreeGrep.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    public ItunesDupeCleaner(Map<String, String> args, List<String> operands) throws IOException
    {
        if(operands.size() != 1)
            usage();

        dryRun = Boolean.parseBoolean(args.get("dry-run"));
        dump = Boolean.parseBoolean(args.get("dump"));
        String root =  FileUtils.safeGetCanonicalPath(new File(operands.get(0)));
        FileFinder ff = new FileFinder(root, new DupeFilter());
        allFiles = ff.getFiles();
        generateGroups();
        
        if(dump)
            dump();
        else
            delete();
    }

    ////////////////////////////////////////////////////////////////////////////

    private static void usage()
    {
        System.out.println("java " + ItunesDupeCleaner.class.getName() + "  params directory-root");
        System.out.println("");
        System.out.println(Arg.toHelp(argDescriptions));
        System.exit(1);
    }

    ////////////////////////////////////////////////////////////////////////////

    private void generateGroups() {
        for(File dupe : allFiles) {
            // dupe matches " [1-9].mp3"
            File root = generateRoot(dupe);

            // if it does not exist it probably is a song that happens to end
            // with that pattern -- so just ignore it.
            if(!root.isFile())
                continue;
            
            List<File> list = dupeGroups.get(root);
            
            if(list == null) {
                list = new ArrayList<File>();
                dupeGroups.put(root, list);
            }
            
            list.add(dupe);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private File generateRoot(File f) {
        // "foo 1.mp3"  ===>  "foo.mp3"
        
        String s = f.getPath(); // root was canonicalized -- this should have a good path!!
        s = s.substring(0, s.length() - 6) + ".mp3";   // allow NPE

        return new File(s);
    }

    private void dump() {
        Set<Map.Entry<File, List<File>>> entries = dupeGroups.entrySet();

        for(Map.Entry<File, List<File>> entry : entries) {
            File root = entry.getKey();
            List<File> others = entry.getValue();
            int num = others.size() + 1;
            long[] lens = new long[num];
            int i = 0;
            lens[i++] = root.length();

            for(File f : others) {
                lens[i++] =  f.length();
            }
            String lensString = "";
            for(long l : lens) {
                lensString += ", " + l;
            }
            // whew!!
            String s = "REM " + root.getName() + lensString;
            System.out.println(s);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private void delete() {
        Set<Map.Entry<File, List<File>>> entries = dupeGroups.entrySet();

        for(Map.Entry<File, List<File>> entry : entries) {
            File root = entry.getKey();
            List<File> others = entry.getValue();
            int num = others.size() + 1;
            File[] files = new File[num];
            int i = 0;
            files[i++] = root;

            for(File f : others) {
                files[i++] = f;
            }

            keepLargest(files);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private void keepLargest(File[] files) {
        File biggest = null;
        long biggestLen = -1;
        int  biggestIndex = -1;

        for(int i = 0; i < files.length; i++) {
            long len = files[i].length();

            if(len > biggestLen) {
                biggest = files[i];
                biggestLen = len;
                biggestIndex = i;
            }
        }

        // delete the smaller dupes
        for(int i = 0; i < files.length; i++) {
            if(biggestIndex != i) {
                System.out.println("del " + files[i].getName());
                
                if(!dryRun)
                    files[i].delete();
            }
        }

        // rename the last one standing -- if necessary

        if(biggestIndex != 0) {
            System.out.println("MOVE " + files[biggestIndex].getName() + "  " + files[0].getName());

            if(!dryRun)
                files[biggestIndex].renameTo(files[0]);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////

    private static class DupeFilter implements FileFilter {
        @Override
        public boolean accept(File f) {

            if(f.isDirectory())
                return true;
            
            String s = f.getName().toLowerCase();

            if(!s.endsWith(".mp3"))
                return false;

            int len = s.length();

            if(len < 7) // "x 1.mp3"
                return false;

            s = s.substring(len - 6, len - 4);

            if(!s.startsWith(" "))
                return false;

            char c = s.charAt(1);

            if(c >= '1' && c <= '9')
                return true;

            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private final static Arg[] argDescriptions = new Arg[]
    {
        new BoolArg("dry-run", null, false, "Dry Run, suitable for collecting to a batch file"),
        new BoolArg("dump", null, false, "Dump lots of info"),
    };

    private boolean ignoreCase = true;
    private boolean dryRun = false;
    private boolean dump = false;
    Map<File, List<File>> dupeGroups = new HashMap<File, List<File>>();
    List<File> allFiles;
}

