/*
 * FileExtFinder.java
 *
 * Created on May 25, 2003, 11:04 AM
 */

package com.elf.io;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 *
 * @author  bnevins
 */
public class FileDirFinder extends FileFinder
{
	public FileDirFinder(String rootDir) 
	{
		super(rootDir,  new JustDirs());
		
		if(!(new File(rootDir)).isDirectory())
			throw new IllegalArgumentException("You can only call FileDirFinder with a Directory argument.  This isn't a directory: " + rootDir);
        
        wantDirs = true;
	}

	///////////////////////////////////////////////////////////////////////////

	public FileDirFinder(String rootDir, String dirName) 
	{
		super(rootDir,  new JustDirs());
		dirname = dirName;
        wantDirs = true;
	}

	///////////////////////////////////////////////////////////////////////////

	public FileDirFinder(String rootDir, String dirName, String parentDir) 
	{
		super(rootDir,  new JustDirs());
		dirname = dirName;
		parentDirName = parentDir;
        wantDirs = true;
	}

	///////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<File> getFiles() throws IOException
	{
		List<File> files = super.getFiles();	// *ALL* of the directories
		
		if(dirname == null)
			return files;
		
		List<File> keep = new ArrayList<File>();
		
		for(File f : files)
		{
			if(f.isDirectory() && f.getName().equals(dirname))
			{
				if(parentDirName != null)
				{
					File parent = f.getParentFile();
					
					if(parent != null && parent.getName().equals(parentDirName))
					{
						keep.add(f);
					}
				}
				else
				{
					keep.add(f);
				}
			}
		}
		
		return keep;
	}

	///////////////////////////////////////////////////////////////////////////

	private String dirname;
	private String parentDirName;
	
	///////////////////////////////////////////////////////////////////////////

	private static class JustDirs implements FileFilter
    {
		public boolean accept(File f)
		{
			if(f.isDirectory())
				return true;

			return false;
		}
	}
}

