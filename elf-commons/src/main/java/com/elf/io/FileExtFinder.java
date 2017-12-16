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
public class FileExtFinder extends FileFinder
{
	public FileExtFinder() 
	{
	}
	
	///////////////////////////////////////////////////////////////////////////

	public FileExtFinder(String rootDir) 
	{
		super(rootDir, (String[])null);
	}

	///////////////////////////////////////////////////////////////////////////

	public FileExtFinder(String rootDir, String... ext) 
	{
        super(rootDir, ext);
    }
}

