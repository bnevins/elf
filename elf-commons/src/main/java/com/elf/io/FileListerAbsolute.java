/*
 * FileListerAbsolute.java
 *
 * Created on November 11, 2001, 12:09 AM
 */

package com.elf.io;
import java.io.*;
import java.util.*;
import com.elf.util.*;

/**
 *
 * @author  bnevins
 * @version
 */
public class FileListerAbsolute extends FileLister
{
	public FileListerAbsolute(File f)
	{
		super(f);
	}
	
	protected boolean relativePath()
	{
		return false;
	}
}




