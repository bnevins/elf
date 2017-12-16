/*
 * ElfClassLoader.java
 *
 * Created on June 22, 2003, 8:35 PM
 */

package com.elf.util;
import java.util.*;
import java.io.*;
import com.elf.io.*;
/**
 *
 * @author  bnevins
 */
public  class ElfClassLoader extends ClassLoader 
{
	public ElfClassLoader() 
	{
	}

	protected synchronized Class loadClass(String name, boolean resolve) throws java.lang.ClassNotFoundException 
	{
		// is it already loaded?
		Class clazz = (Class)classes.get(name);
		
		if(clazz == null)
		{
			try
			{
				return findSystemClass(name);
			}
			catch(ClassNotFoundException e)	
			{
			}
			catch(NoClassDefFoundError   e)	
			{
			}
			
			byte[] classBytes = loadClassBytes(name);
			
			if(classBytes == null)
				throw new ClassNotFoundException(name);
			
			clazz = defineClass(name, classBytes, 0, classBytes.length);

			if(clazz == null)
				throw new ClassNotFoundException(name);
			
			classes.put(name, clazz);
		}

		if(resolve)
			resolveClass(clazz);
		
		return clazz;
	}

	///////////////////////////////////////////////////////////////////////////

	private byte[] loadClassBytes(String name)
	{
		String dirname = "d:/temp/";
		String className = dirname + name.replace('.', '/') + ".class";
		
		FileInputStream in = null;
		
		try
		{
			in = new FileInputStream(className);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int ch;
			while((ch = in.read()) >= 0)
			{
				buffer.write((byte)ch);
			}
			in.close();
			return buffer.toByteArray();
		}
		catch(IOException e)
		{
			if(in != null)
			{
				try
				{
					in.close();
				}
				catch(IOException e2)
				{
				}
			}
			return null;
		}
	}	

	///////////////////////////////////////////////////////////////////////////

	private Map<String, Class>		classes = new HashMap<String, Class>();

	///////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{
		try
		{
			ClassLoader cl = new ElfClassLoader();
			Class clazz = cl.loadClass("junk.Test");
			Class clazz2 = cl.loadClass("com.elf.Chubster");
			Object o = clazz.newInstance();
			//ClassFinderGUI cf = new ClassFinderGUI("Yo dog!!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}


