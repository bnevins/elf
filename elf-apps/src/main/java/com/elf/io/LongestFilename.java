/*
 * LongestFilename.java
 *
 * Created on December 16, 2007, 12:26 PM
 *
 */

package com.elf.io;

import com.elf.db.DerbyException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class LongestFilename
{
    public static void main(String[] args)
    {
        if(args.length == 2)
        {
            try
            {
                new LongestFilename(args[0], args[1]);
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
            System.out.println("USAGE: MakeFileDigests root-dir out-file");
    }

    ///////////////////////////////////////////////////////////////////////////

    public LongestFilename(String rootString, String outFileName) throws Exception
    {
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
        doit(outFileName);
    }
    
    ////////////////////////////////////////////////////////////////////////////

    private void doit(String fn) throws Exception
    {
        PrintWriter pw = new PrintWriter(fn);
        //Set set = new TreeSet<FileLength>(100);
        
        FileFinder ff = new FileFinder(root);
        List<File> files = ff.getFiles();
       
        for(File f : files)
        {
            sort(f);
        }
        pw.printf("Longest Path = [%d] %s\nLongest Name = [%d] %s", 
            longestPath.getPath().length(),
            longestPath.getPath(),
            longestName.getName().length(),
            longestName.getPath());
        System.out.printf("\n\n***************\n\nLongest Path = [%d] %s\nLongest Name = [%d] %s", 
            longestPath.getPath().length(),
            longestPath.getPath(),
            longestName.getName().length(),
            longestName.getPath());
        pw.close();
    }

    ///////////////////////////////////////////////////////////////////////////

    private void sort(File f)
    {
        int plen = f.getPath().length();
        int nlen = f.getName().length();
        
        int prevplen = (longestPath == null) ? 0 : longestPath.getPath().length();
        int prevnlen = (longestName == null) ? 0 : longestName.getName().length();
        
        if(plen > prevplen)
        {
            longestPath = f;
            System.out.println("LONG PATH: " + f.getPath());
        }
        
        if(nlen > prevnlen)
        {
            longestName = f;
            System.out.println("LONG NAME: " + f.getName());
        }
    }
    
    private String root;
    private File longestPath, longestName;
}
