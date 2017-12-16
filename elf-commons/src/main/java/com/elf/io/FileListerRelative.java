/*
 * foo.java
 *
 * Created on November 11, 2001, 12:09 AM
 */

package com.elf.io;
import java.io.*;
import java.util.*;


/**
 *
 * @author  bnevins
 * @version 
 */
public class FileListerRelative extends FileLister
{
	public FileListerRelative(File f)
	{
		super(f);
	}
	
	protected boolean relativePath()
	{
		return true;
	}
}




