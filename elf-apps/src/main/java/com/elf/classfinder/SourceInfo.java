/*
 * SourceInfo.java
 *
 * Created on April 29, 2004, 12:00 AM
 */

package com.elf.classfinder;

import java.io.*;

/**
 *
 * @author  bnevins
 */
public class SourceInfo
{
	
	/** Creates a new instance of SourceInfo */
	public SourceInfo(File root, File file)
	{
		// e.g. root = "c:/foo/src/java"
		// e.g. file = "c:/foo/src/java/com/foo"
		mountPoint = root.getPath();
		String filename = file.getPath();
		
		filename = filename.substring(mountPoint.length() + 1);
		// now filename is "com/foo"
		
		filename = filename.replace('/', '.');
		// now filename = "com.foo"
		
		longClassName = filename.replace('\\', '.');
		
		shortClassName = file.getName();
		
		// get rid of ".java" but keep ".properties"
		
		if(shortClassName.endsWith(".java"))
		{
			shortClassName = shortClassName.substring(0, shortClassName.length() - 5);
		}

		if(longClassName.endsWith(".java"))
		{
			longClassName = shortClassName.substring(0, shortClassName.length() - 5);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	public String toString()
	{
		return mountPoint + "," + shortClassName +"," + longClassName;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	String mountPoint;
	String longClassName;
	String shortClassName;
}
