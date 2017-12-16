/*
 * JarFinderTester.java
 *
 * Created on May 25, 2003, 11:04 AM
 */

package com.elf.io;
import java.io.*;
import java.util.*;
import com.elf.util.Console;
import com.elf.util.StringUtils;

/**
 *
 * @author  bnevins
 */
public class FileFinderTester 
{
	public FileFinderTester() 
	{
		String s;
		
		while(StringUtils.ok(s = Console.readLine("eXt search, Dir search: [x|d]: ")))
		{
			if(s.equals("x"))
				testExtensionSearch();
			else if(s.equals("d"))
				testDirSearch();
			
			System.out.println("\n");
		}
	}

	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) 
	{
		new FileFinderTester();
		System.exit(0);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void testExtensionSearch()
	{
		try
		{
			final String defFN = "/f:/dev/elf/src/java/com/elf";
			final String defExt = ".java";
			
			String fn = Console.readLine("Directory or archive [" + defFN + "]:");
			String ex = Console.readLine("File Extension [" + defExt + "]:");
			
			if(!StringUtils.ok(fn))
				fn = defFN;

			if(!StringUtils.ok(ex))
				ex = defExt;
			
			FileExtFinder jf = new FileExtFinder(fn, ex);
			List files = jf.getFiles();
			System.out.println("" + files.size() + " matching files found");

			for(Iterator it = files.iterator(); it.hasNext(); )
			{
				//File f = (File)it.next();
				System.out.println(it.next());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////

	public static void testDirSearch() 
	{
		try
		{
			final String defFN = "/d:/jws/appserv-core";
			final String defDir = "src";
			
			String fn  = Console.readLine("Directory or archive [" + defFN + "]:");
			String dir = Console.readLine("Dir name [" + defDir + "]:");
			
			if(!StringUtils.ok(fn))
				fn = defFN;

			if(!StringUtils.ok(dir))
				dir = defDir;
			
			FileDirFinder jf = new FileDirFinder(fn, dir);
			List files = jf.getFiles();
			System.out.println("" + files.size() + " matching files found");

			for(Iterator it = files.iterator(); it.hasNext(); )
			{
				//File f = (File)it.next();
				System.out.println(it.next());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
}