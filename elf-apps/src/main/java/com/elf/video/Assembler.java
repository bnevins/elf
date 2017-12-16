/*
 * Assembler.java
 *
 * Created on November 22, 2000, 12:52 AM
 */

package com.elf.video;

import java.io.*;
import java.util.*;

/**
 *
 * @author  bnevins
 * @version 
 */
public class Assembler
{
	public Assembler(String s) 
	{
		this(s, false);
	}
	
	///////////////////////////////////////////////////////////////////
	
	public Assembler(String s, boolean DoDelete) 
	{
		setRootName(s);
		doDelete = DoDelete;
	}
	
	///////////////////////////////////////////////////////////////////
	
	public void setRootName(String s)
	{
		rootname = s;
	}
	
	///////////////////////////////////////////////////////////////////
	
	public void preview()
	{
		no = true;
		assemble();
	}
	
	///////////////////////////////////////////////////////////////////
	
	public void assemble()
	{
		File curr = new File(".");
		String[] files = curr.list(new AssemblerFilter());
		
		if(files == null || files.length <= 0)
		{
			System.out.println("No matching files: " + rootname);
			return;
		}

		ArrayList<String> list = new ArrayList<String>();

		for(String s : files)
		{
			list.add(s);
			System.out.println(s);
		}

		Collections.sort(list);
		System.out.println("\n\nAfter Sorting: " + list.size() + "\n");

		for(Iterator iter = list.iterator(); iter.hasNext(); )
		{
			System.out.println("" + iter.next());
		}
		
		if(no)
		{
			System.out.println("Total Number of files: " + list.size());
			return;
		}		
		try
		{
			byte[] buf = new byte[200000];
			int total = 0;

			FileOutputStream os = new FileOutputStream(rootname);

			for(Iterator iter = list.iterator(); iter.hasNext(); )
			{
				String name = (String)iter.next();

				System.out.println("Processing: " + name);
			
				FileInputStream is = new FileInputStream(name);

				while(true)
				{
					int len = is.read(buf);

					if(len < 0)
						break;

					total += len;
					os.write(buf, 0, len);
				}
				is.close();
			}
			os.close();
			System.out.println("wrote " + total + " bytes...");
			
			if(doDelete)
			{
				// only do the deletes if everything above went OK...
				for(String s : files)
				{
					File f = new File(s).getAbsoluteFile();
					
					if(!f.delete())
						System.out.println("Couldn't delete: " + f);
				}
				System.out.println("Deleted " + files.length + " input files.");
			}
		}
		catch(IOException e)
		{
			System.out.println("Exception: " + e);
		}
	}
	
	///////////////////////////////////////////////////////////////////

	public static void main(String[] args)
	{
		int		nargs		= args.length;
		int		firstArg	= 0;
		boolean doDelete	= false;
		boolean no = false;
		
		if(nargs < 1)
			usage();
		
		if(args[0].equals("-d"))
		{
			doDelete = true;
			firstArg = 1;
			
			if(nargs < 2)
				usage();
		}

		if(args[0].equals("-n"))
		{
			no = true;
			firstArg = 1;
			
			if(nargs < 2)
				usage();
		}
		
		for(int i = firstArg; i < nargs; i++)
		{
			Assembler a = new Assembler(args[i], doDelete);
			
			if(no)
				a.preview();
			else
				a.assemble();
		}
	}
	
	///////////////////////////////////////////////////////////////////

	public static void usage()
	{
		System.out.println("Usage: assembler [-d] file-root1 file-root2 ...");
		System.out.println("-d ==> delete input files");
		System.exit(1);
	}
	
	///////////////////////////////////////////////////////////////////
	
	private String	rootname	= null;
	private boolean doDelete	= false;
	private boolean no			= false;
	
	private class AssemblerFilter implements FilenameFilter
	{
		public boolean accept(File dir, String name)
		{
			if(name.toLowerCase().startsWith(rootname.toLowerCase()))
				return true;

			return false;
		}
	}

		
}

