/*
 * ThumbnailManager.java
 *
 * Created on May 16, 2005, 9:32 PM
 */

package com.elf.graphics;

import com.elf.io.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author bnevins
 */
public class ThumbnailManager_1
{

	public static void main(String[] args)
	{
		if(args.length <= 0)
		{
			System.out.println("Usage ThumbnailManager src-dir sizeOrScale");
			//args = new String[] { "c:/data/pix/0307", "300" };
			System.exit(1);
		}
		
		ThumbnailManager_1 mgr = new ThumbnailManager_1(new File(args[0]), args[1], true);
		mgr.make();
		System.out.println("Number of Files processed: " + numProcessed);
	}
	///////////////////////////////////////////////////////////////////////////
	
	public ThumbnailManager_1(File Src, String Size, boolean Recursive)
	{
		src = Src;
		sizeOrScale = Size;
		recursive = Recursive;
	}

	///////////////////////////////////////////////////////////////////////////
	
	public void make()
	{
		getFiles();
		for(File f : files)
		{
			doOne(f);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void doOne(File f)
	{
		++numProcessed;
		File parent = f.getParentFile();
		String	name = f.getName();
		String tn_name = "tn_" + name;
		File tn = new File(parent, tn_name);
		
		if(tn.exists())
		{
			tn.delete();
		}
		
		System.out.println("Processing: " + tn);
		Thumbnail.createThumbnail(f.getAbsolutePath(), tn.getAbsolutePath(), sizeOrScale);
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void getFiles()
	{
		files = null;
		
		try
		{
			if(recursive)
			{
				FileExtFinder ff = new FileExtFinder(src.getAbsolutePath(), ".jpg");
				files = ff.getFiles();
				
				for(File f : files)
					System.out.println(f);
				
				// filter out any existing thumbnails
				for(Iterator<File> it = files.iterator(); it.hasNext(); )
				{
					File f = it.next();
					if(f.getName().startsWith("tn_"))
						it.remove();
				}
			}
			else
			{
			// todo
			}
		}
		catch(IOException e)
		{
			// todo
		}
	}
	//JPEGUtils.createThumbnail(src, dest, "300");
	//JPEGUtils.createThumbnail(src, dest, "0.5");

	///////////////////////////////////////////////////////////////////////////
	

	///////////////////////////////////////////////////////////////////////////
	private	File		src;
	private String		sizeOrScale;
	private boolean		recursive;
	private	List<File>	files;
	private static int	numProcessed = 0;
}
