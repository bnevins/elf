/*
 * NonJpegFileFinder.java
 *
 * Created on June 2, 2005, 10:05 PM
 */

package com.elf.io;

import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class NonJpegFileFinder extends FileFinder// implements FileFilter
{
	
	/** Creates a new instance of NonJpegFileFinder */
	public NonJpegFileFinder(String root)
	{
		super(root);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public boolean accept(File f)
	{
		if(f.isDirectory())
			return true;
		
		String name = f.getName().toLowerCase();
		
		if(name.endsWith(".jpg") || name.endsWith(".jpeg"))
			return false;

		return true;
	}
	public static void main(String[] args)
	{
		String root;
		
		if(args.length > 0)
			root = args[0];
		else
		{
			File f = new File(".");
			try
			{
				root = f.getCanonicalPath();
			}
			catch(Exception e)
			{
				root = f.getAbsolutePath();
			}
		}
			
		NonJpegFileFinder ff = new NonJpegFileFinder(root);
		try
		{
			List<File> list = ff.getFiles();
			
			for(File f : list)
				System.out.println(f);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
