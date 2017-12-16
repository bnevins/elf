/*
 * MakeFileDigestsAndDupeCheck.java
 *
 * Created on December 16, 2007, 12:26 PM
 *
 */

package com.elf.io;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class MakeFileDigestsAndDupeCheck
{
    public MakeFileDigestsAndDupeCheck(String rootString) throws IOException
    {
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
        makeInfos();
        dumpInfos();
    }
    
    ////////////////////////////////////////////////////////////////////////////

    private void makeInfos() throws IOException
    {
        FileFinder ff = new FileFinder(root);
        List<File> files = ff.getFiles();
        infos = new FileInfo[files.size()];
        
        int i = 0;
        for(File f : files)
        {
            infos[i++] = new FileInfo(f);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    private void dumpInfos() throws FileNotFoundException
    {
        PrintWriter pw = new PrintWriter(root + "/names.out");
        PrintWriter pwDupes = new PrintWriter(root + "/names_dupes.out");
        
        FileInfo prev = null;
        boolean inDupes = false;
        Arrays.sort(infos, new NameSorter());
        for(FileInfo info : infos)
        {
           String name = info.getName();
           pw.println(name + sepString + info.getPath() + sepString + info.getDigest()); 
           
           if(prev != null && name.equals(prev.getName()))
           {
               // got a dupe!
               inDupes = true;
               pwDupes.println(prev.getName() + sepString + prev.getPath() + sepString + prev.getDigest()); 
           }
           else if(prev != null && inDupes)
           {
               pwDupes.println(prev.getName() + sepString + prev.getPath() + sepString + prev.getDigest()); 
               inDupes = false;
           }
           prev = info;
        }
        if(inDupes)
            pwDupes.println(prev.getName() + sepString + prev.getPath() + sepString + prev.getDigest()); 

        pw.close();
        pwDupes.close();

        pw = new PrintWriter(root + "/digests.out");
        pwDupes = new PrintWriter(root + "/digests_dupes.out");
        Arrays.sort(infos, new DigestSorter());
        prev = null;
        inDupes = false;
        
        for(FileInfo info : infos)
        {
           String digest = info.getDigest();
           pw.println(info.getName() + sepString + info.getPath() + sepString + info.getDigest()); 
           
           if(prev != null && digest.equals(prev.getDigest()))
           {
               // got a dupe!
               inDupes = true;
               pwDupes.println(prev.getDigest() + sepString + prev.getDigest() + sepString + prev.getPath()); 
           }
           else if(prev != null && inDupes)
           {
               pwDupes.println(prev.getDigest() + sepString + prev.getDigest() + sepString + prev.getPath()); 
               inDupes = false;
           }
           prev = info;
        }
        
        if(inDupes)
               pwDupes.println(prev.getDigest() + sepString + prev.getDigest() + sepString + prev.getPath()); 
            
        pw.close();
        pwDupes.close();
    }
    
    ///////////////////////////////////////////////////////////////////////////

    public static void main(String[] args)
    {
        if(args.length == 1)
        {
            try
            {
                new MakeFileDigestsAndDupeCheck(args[0]);
                System.out.println("Look in digests.out, names.out");
            } 
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        else
            System.out.println("USAGE: MakeFileDigests root-dir");
    }
    
    private String root;
    private File outFile;
    private FileInfo[] infos;
    private final static String sepString = ":::";
    
    private static class FileInfo
    {
        FileInfo(File f)
        {
            file = f;
            try
            {
                digest = new FileDigester(file.getPath()).getDigestStringCompact();
            } 
            catch (NoSuchAlgorithmException ex)
            {
                // can't happen!
                digest = ex.getMessage();
            }
        }
        
        String getName()
        {
            return file.getName();
        }
        
        String getPath()
        {
            return file.getPath();
        }
        
        String getDigest()
        {
            return digest;
        }
        
        private File file;
        private String digest;
    }

    private static class NameSorter implements Comparator<FileInfo>
    {
        public int compare(MakeFileDigestsAndDupeCheck.FileInfo o1, MakeFileDigestsAndDupeCheck.FileInfo o2)
        {
            // sort 1 = name, 2 = digest
            int ret = o1.getName().compareTo(o2.getName());
            
            if(ret == 0)
            {
                ret = o1.getDigest().compareTo(o2.getDigest());
            }
            return ret;
        }
    }

    private static class DigestSorter implements Comparator<FileInfo>
    {
        public int compare(MakeFileDigestsAndDupeCheck.FileInfo o1, MakeFileDigestsAndDupeCheck.FileInfo o2)
        {
            // sort 1 = digest, 2 = name
            int ret = o1.getDigest().compareTo(o2.getDigest());
            
            if(ret == 0)
            {
                ret = o1.getName().compareTo(o2.getName());
            }
            
            return ret;
        }
    }

}
