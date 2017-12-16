/*
 * ReadFileDigestsAndDupeCheck.java
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
public class ReadFileDigestsAndDupeCheck
{
    public static void main(String[] args)
    {
        if(args.length == 2)
        {
            try
            {
                new ReadFileDigestsAndDupeCheck(args[0], args[1]);
                //System.out.println("Look in digests.out, names.out");
            } 
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        else
            System.out.println("USAGE: MakeFileDigests root-dir digest-file");
    }

    ///////////////////////////////////////////////////////////////////////////

    public ReadFileDigestsAndDupeCheck(String rootString, String digestFileName) throws IOException
    {
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
        digestFile = new File(digestFileName);
        readDigestFile();
        //makeInfos();
        dumpInfos();
    }
    
    ////////////////////////////////////////////////////////////////////////////

    private void makeInfos() throws IOException
    {
        FileFinder ff = new FileFinder(root);
        List<File> files = ff.getFiles();
    }

    ///////////////////////////////////////////////////////////////////////////

    private void readDigestFile() throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(digestFile));
        infos = new ArrayList<FileInfo>();
        String s;
        
        while((s = reader.readLine()) != null)
        {
            String[] ss = s.split(":::");
            
            if(ss == null || ss.length != 2)
                continue;
            
            infos.add(new FileInfo(new File(ss[0]), ss[1]));
        }
    }
        
    ///////////////////////////////////////////////////////////////////////////

    private void dumpInfos() throws FileNotFoundException
    {
        PrintWriter pwDupes = new PrintWriter(root + "/names_dupes.out");
        
        FileInfo prev = null;
        boolean inDupes = false;
        Collections.sort(infos, new NameSorter());
        for(FileInfo info : infos)
        {
           String name = info.getName();
           
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

        pwDupes.close();

        pwDupes = new PrintWriter(root + "/digests_dupes.out");
        PrintWriter pwDels = new PrintWriter(root + "/rmdupes.bat");
        Collections.sort(infos, new DigestSorter());
        prev = null;
        inDupes = false;
        
        for(FileInfo info : infos)
        {
           String digest = info.getDigest();
           
           if(prev != null && digest.equals(prev.getDigest()))
           {
               // got a dupe!
               inDupes = true;
               pwDupes.println(prev.getDigest() +  sepString + prev.getPath()); 
               pwDels.println("del " + "\"" + info.getPath() + "\"");
           }
           else if(prev != null && inDupes)
           {
               pwDupes.println(prev.getDigest() + sepString + prev.getPath() + "\n"); 
               inDupes = false;
           }
           prev = info;
        }
        
        if(inDupes)
               pwDupes.println(prev.getDigest() + sepString + prev.getPath()); 
            
        pwDupes.close();
        pwDels.close();
    }

    ///////////////////////////////////////////////////////////////////////////
    

    private String root;
    private File outFile;
    private List<FileInfo> infos;
    private File digestFile;
    private final static String sepString = ":::";

    ///////////////////////////////////////////////////////////////////////////
    
    private static class FileInfo implements Comparable
    {
        FileInfo(File f, String d)
        {
            file = f;
            digest = d;
        }
        
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
        public String toString()
        {
            return digest + " " + file;
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

        public int compareTo(Object o)
        {
            
            return file.compareTo(((FileInfo)o).file);
        }
        
        private File file;
        private String digest;
    }

    ///////////////////////////////////////////////////////////////////////////
    
    private static class NameSorter implements Comparator<FileInfo>
    {
        public int compare(FileInfo o1, FileInfo o2)
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

    ///////////////////////////////////////////////////////////////////////////
    
    private static class DigestSorter implements Comparator<FileInfo>
    {
        public int compare(FileInfo o1, FileInfo o2)
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
