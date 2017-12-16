/*
 * PropertyFile.java
 *
 * Created on October 28, 2004, 3:46 AM
 */

package com.elf.classfinder;

import java.io.*;

/**
 *
 * @author  bnevins
 * Contains a property and the file that it lives in.
 */

public class PropertyFile implements Comparable, Serializable
{
	public PropertyFile(File f, String key, String value)
	{
		this.file		= f;
		this.key	= key;
		this.value	= value;
	}

	///////////////////////////////////////////////////////////////////////////
	
	public boolean equals(Object obj)
	{
		PropertyFile pf = (PropertyFile)obj;
		
		return file.equals(pf.file) && key.equals(pf.key) && value.equals(pf.value);
	}
	
	///////////////////////////////////////////////////////////////////////////

	public int compareTo(Object obj)
	{
		PropertyFile pf = (PropertyFile)obj;
		
		int ret = key.compareTo(pf.key);
		
		if(ret == 0)
			ret = file.compareTo(pf.file);

		if(ret == 0)
			ret = value.compareTo(pf.value);
		
		return ret;
		
	}
	
	///////////////////////////////////////////////////////////////////////////

	public boolean matches(String s)
	{
		return key.indexOf(s) >= 0 || value.indexOf(s) >= 0;
	}
	
	///////////////////////////////////////////////////////////////////////////

	public String toString()
	{
		return file.getPath() + "\nKEY: " + key + "\nVALUE: " + value;
	}
	
	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] notUsed)
	{
		PropertyFile pf1 = new PropertyFile(new File("D:/TEMP/junk.txt"), "a", "");
		PropertyFile pf2 = new PropertyFile(new File("d:/tmp/junk.txt"), "a", "");
		
		System.out.println("Compare to: " + pf1.compareTo(pf2));
		System.out.println("Equals: " + pf1.equals(pf2));
	}
	
	///////////////////////////////////////////////////////////////////////////

	public File		file;
	public String	key;
	public String	value;
}
