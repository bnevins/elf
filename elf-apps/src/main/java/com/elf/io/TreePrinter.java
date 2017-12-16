/*
 * TreeExecutor.java
 *
 * Created on August 16, 2006, 1:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.io;

import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class TreePrinter
{
    public TreePrinter(String Root)
    {
        root = new File(Root);
    }
    
    ///////////////////////////////////////////////////////////////////////////

    public void printAll()  throws IOException    
    {
        findAndPrint(root);
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private void findAndPrint(File dir) throws IOException    
    {
        if(!dir.isDirectory())
		{
             throw new IOException(dir + " is NOT a directory!");
        }

		File[] files = dir.listFiles();
		
		for(File curr : files)
		{
			if(curr.isDirectory())
			{
                if(printDirsOnly)
                    printOne(curr);
                    
				findAndPrint(curr);
			}
            else
            {
                if(!printDirsOnly)
                    printOne(curr);
            }
		}
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private void printOne(File f) throws IOException
    {
        ++numFiles;
        System.out.println(f.getAbsolutePath());
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private static void usage()
    {
        System.out.println("USAGE:  java com.elf.io.TreePrinter [-d] root-dir-path");
        System.out.println("-d means directories only");
        
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args)
    {
        String root;

        if(args.length == 1)
        {
            printDirsOnly = false;
            root = args[0];
        }
        else if(args.length == 2)
        {
            printDirsOnly = true;
            root = args[1];
        }
        else
        {
            usage();
            return;
        }
        
        try
        {
            TreePrinter tp = new TreePrinter(root);
            tp.printAll();
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
            usage();
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private File        root;
    private int         numFiles;
    //private int         fileIndex;
    private static boolean     printDirsOnly = true;
}
