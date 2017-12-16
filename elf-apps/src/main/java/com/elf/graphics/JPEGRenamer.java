/*
 * JPEGRenamer.java
 *
 * Created on June 6, 2006, 11:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.graphics;

import com.elf.io.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class JPEGRenamer
{
	public JPEGRenamer(String dir) throws IOException
	{
		FileExtFinder ff = new FileExtFinder(dir, "JPG");
		List<File> files = ff.getFiles();
		
		for(File f : files)
		{
			f = f.getAbsoluteFile();
			String name = f.getName();
			File parent = f.getParentFile();
			name = name.substring(0, name.length() - 3);
			name += "jpg";
			File f2 = new File(parent, name);
			
			System.out.println("RENAME: from: " + f + ", to: " + f2);
			f.renameTo(f2);
		}
	}

	public static void main(String[] args)
	{
		String arg;
		
		if(args.length > 0)
			arg = args[0];
		else
			arg = ".";
		try
		{
			new JPEGRenamer("C:/temp/xxxx");
		} 
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	
	}
	
	private static void usage()
	{
		System.out.println("usage: JPEGRenamer dir");
		System.exit(1);
	}

}
