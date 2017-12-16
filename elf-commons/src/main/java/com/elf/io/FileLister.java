/*
 * foo.java
 *
 * Created on November 11, 2001, 12:09 AM
 */

package com.elf.io;
import java.io.*;
import java.util.*;

/**
 * Traverses a directory tree and gathers all of the filenames
 * @author bnevins
 * @version 1.0
 */
public abstract class FileLister
{
	FileLister(File root)
	{
		mainRoot = root;
		fileList = new ArrayList<File>();
	}

	/**
	 * Implement this method in a subclass in order to identify whether relative or 
	 * absolute filenames are needed.
	 * @return Return true for relative paths, false for absolute 
	 * paths
	 */
	abstract protected boolean relativePath();
	
	/**
	 * Get an array of the filenames
	 * @return An array of all the filenames.  The names can be relative or absolute.
	 */
	public String[] getFiles()
	{
		getFilesInternal(mainRoot);
		String[] files = new String[fileList.size()];
		
		if(files.length <= 0)
			return files;

		int len = 0;
		
		if(relativePath())
			len = mainRoot.getPath().length() + 1;
		
		for(int i = 0; i < files.length; i++)
		{
			files[i] = fileList.get(i).getPath().substring(len).replace('\\', '/');
		}
		
		Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
		return files;
	}
	
	
	/**
	 * 
	 * @param root 
	 */
	public void getFilesInternal(File root)
	{
		File[] files = root.listFiles();
		
		if(files == null)
			return;
		//for(int i = 0; i < files.length; i++)
		for(File f : files)
		{
			if(f.isDirectory())
				getFilesInternal(f);
			else
				fileList.add(f);	// actual file
		}
	}
		
	private	ArrayList<File>	fileList	= null;
	private File			mainRoot	= null;
}



