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
public class TreeExecutor
{
    public TreeExecutor(String Root, String... Commands)
    {
        root = new File(Root);
        
        fileIndex = Commands.length;
        command = new String[fileIndex + 1];
        System.arraycopy(Commands, 0, command, 0, fileIndex);
    }
    
    ///////////////////////////////////////////////////////////////////////////

    public void execAll()  throws IOException    
    {
        findAndExec(root);
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private void findAndExec(File dir) throws IOException    
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
                if(execDirsToo)
                    System.out.println("DIR DIR DIR!!!!!");execOne(curr);
                    
				findAndExec(curr);
			}
            else
            {
                execOne(curr);
            }
		}
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private void execOne(File f) throws IOException
    {
        ++numFiles;
        command[fileIndex] = f.getAbsolutePath();
        Runtime.getRuntime().exec(command);
        System.out.println("EXEC: " + Arrays.toString(command));
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private static void usage()
    {
        System.out.println("USAGE:  java com.elf.io.TreeExecutor root-dir-path command1 command2 ...");
        System.out.println("Each of the commands will be executed as specified except for appending 1 filename.");
        System.out.println("This is repeated for every file in the tree.");
        System.out.println("NOTE: The command will also be run on the subdirectories themselves.");
        System.out.println("\nEXAMPLE:");;
        System.out.println("java com.elf.io.TreeExecutor C:/sjsas touch -t 200608151200.00");
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args)
    {
        if(args.length < 2)
        {
            usage();
            return;
        }

        int numargs = args.length;
        String[] newArgs = new String[numargs - 1];
        System.arraycopy(args, 1, newArgs, 0, numargs - 1);
        
        TreeExecutor exec = new TreeExecutor(args[0], newArgs);
        
        try
        {
            exec.execAll();
        } 
        catch (IOException ex)
        {
            ex.printStackTrace();
            usage();
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////

    private File        root;
    private String[]    command;
    private int         numFiles;
    private int         fileIndex;
    private boolean     execDirsToo = true;
}
