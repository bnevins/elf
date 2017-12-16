package com.elf.graphics;

import java.io.*;
import com.elf.util.*;

class JPEGCleaner 
{
	public static void main(String[] args) 
	{
		if(args.length < 2)
			usage();

		JPEGCleaner jc = new JPEGCleaner(args[0], args[1]);
	}

	//////////////////////////////////////////////////////////////	
	
	public static void usage()
	{
		System.out.println("Usage:\njava com.elf.graphics.JPEGCleaner input-directory output-directory");
		System.out.println("\nJPEGCleaner reads in image files with the extensions jpeg, jpg and gif from the input-directory.");
		System.out.println("It writes the files back out in a standard format, with the same filename, to the output-directory.");
		System.exit(0);
	}

	//////////////////////////////////////////////////////////////	
	
	JPEGCleaner(String inputDir, String outputDir)
	{
		Assertion.check(inputDir);		
		Assertion.check(outputDir);		
		File in = new File(inputDir);
		File out = new File(outputDir);
		out.mkdirs();

		Assertion.check(in.exists() && in.isDirectory());		
		Assertion.check(out.exists() && out.isDirectory());		

		String[] files = in.list(new JPEGFileFilter());

		for(int i = 0; i < files.length; i++)
		{
			JPEGUtils.JPEG2JPEG(files[i], inputDir, outputDir);
		}
		System.exit(0);
	}

	//////////////////////////////////////////////////////////////	
	
	private class JPEGFileFilter implements FilenameFilter
	{
		public boolean accept(File dir, String name) 
		{ 
			name = name.toLowerCase();
			
			if(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif"))
				return true;
			
			return false;
		}
	}

	//////////////////////////////////////////////////////////////	
}

