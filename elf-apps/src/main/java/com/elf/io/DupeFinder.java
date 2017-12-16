/*
 * DupeFinder.java
 *
 * Created on December 16, 2007, 10:52 AM
 *
 */

package com.elf.io;

import java.io.*;

/**
 *
 * @author bnevins
 */
public class DupeFinder
{
    public DupeFinder(String rootString)
    {
        root = FileUtils.safeGetCanonicalPath(new File(rootString));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    public void printDupes() throws IOException
    {
        FileFinder ff = new FileFinder(root);
        ff.printDupes();
    }
    public static void main(String[] args)
    {
        if(args.length < 1)
        {
            System.out.println("USAGE DupeFinder dir-root");
            System.exit(1);
        }
        DupeFinder dup = new DupeFinder(args[0]);
        try
        {
            dup.printDupes();
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    
    private String root;
}
