/*
 * PropertyFinderUI.java
 *
 * Created on November 11, 2004, 1:20 AM
 */

package com.elf.classfinder;
import com.elf.util.Console;
import java.io.*;

/**
 *
 * @author  bnevins
 */
public class PropertyFinderUI
{
	public PropertyFinderUI()
	{
	}

	///////////////////////////////////////////////////////////////////////////

	private static void usage()
	{
		System.out.println("USAGE:");
		System.exit(1);
	}
	
	///////////////////////////////////////////////////////////////////////////

	private static void write(String root, String out) throws IOException
	{
		finder= new PropertyFinder();
		finder.create(root);
		finder.write(new File(out));
		System.out.println("Wrote " + finder.size() + " properties from " + root + " to " + out);
	}
	
	///////////////////////////////////////////////////////////////////////////

	private static void read(String in) throws IOException, ClassNotFoundException
	{
		finder= new PropertyFinder();
		finder.read(new File(in));
		System.out.println("Read " + finder.size() + " properties from " + in);
	}
	
	///////////////////////////////////////////////////////////////////////////

	private static void handleArgs() throws IOException, ClassNotFoundException
	{
		if(args[0].equals("write"))
		{
			if(args.length != 3)
				usage();
			
			write(args[1], args[2]);
			System.exit(0);
		}
		else if(args[0].equals("read"))
		{
			if(args.length != 2)
				usage();

			read(args[1]);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] ss)
	{
		try
		{
			args = ss;

			if(args.length > 0)
				handleArgs();

			else
				usage();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static String[]			args;
	private static PropertyFinder	finder;
}
