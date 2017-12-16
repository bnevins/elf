/*
 * RemoveBlankLines.java
 *
 * Created on November 23, 2007, 11:25 PM
 *
 */

package com.elf.io;

import java.io.*;

/**
 *
 * @author bnevins
 */
public class RemoveBlankLines
{
    public static void main(String[] args)
    {
        if(args.length < 1)
        {
            System.out.println("Usage RemoveBlankLines filename");
            System.exit(1);
        }
        try
        {
            File f = FileUtils.removeBlankLines(new File(args[0]));
            System.out.println("File: " + f);
        } 
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
