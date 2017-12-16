/*
 * ActiveTail.java
 *
 * Created on May 20, 2005, 7:54 PM
 */

package com.elf.io;

import com.elf.interfaces.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class ActiveTail implements Runnable
{
	public ActiveTail(TextAppender appen, String fname, int SleepMsec)
	{
		appender = appen;
		file = new File(fname);
		
		if(SleepMsec > 200)
			sleepMsec = SleepMsec;
	}
	
	///////////////////////////////////////////////////////////////////////////

	public ActiveTail(TextAppender appen, String fname)
	{
		this(appen, fname, 2000);
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void run()
	{
		try
		{
			byte[] bytes = new byte[65536];
			long len =  file.length();
			String fname = file.getAbsolutePath();
			
			if(len <= 0)
			{
				
			}
			else
			{

				RandomAccessFile raf = new RandomAccessFile(file, "r");

				pos = 0L;

				// just show 500 bytes at the end of the file...
				if(len > BYTES_OF_OLD_STUFF)
					pos = len - BYTES_OF_OLD_STUFF;

				raf.seek(pos);

				//String s = "**********************    " + fname + "    **********************\n";
				String s = "";

				for(int nbytes = raf.read(bytes); nbytes > 0; nbytes = raf.read(bytes))
				{
					s += new String(bytes, 0, nbytes);
				}

				appender.append(s);
				//System.out.println(s);
				pos = raf.getFilePointer();
				raf.close();
			}
			while(true)
			{
				Thread.sleep(sleepMsec);
			
				if(!file.exists())
					continue;
				
				long newLastmod = file.lastModified();
				
				if(newLastmod <= lastmod)
				{
					continue;
				}
				
				lastmod = newLastmod;
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				
				raf.seek(pos);
				
				String s = "";
				//String s = "**********************    " + fname + "    **********************\n";
				
				for(int nbytes = raf.read(bytes); nbytes > 0; nbytes = raf.read(bytes))
				{
					s += new String(bytes, 0, nbytes);
				}
				
				appender.append(s);
				//System.out.println(s);
				pos = raf.getFilePointer();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args)
	{
		Integer sleep = 0;
		if(args.length <= 0)
			args = myargs;
	
		for(String arg : args)
		{
			System.out.println("Arg: " + arg);
		}
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].startsWith("--sleep"))
			{
				try{ sleep = new Integer(args[++i]); }
				catch(Exception e) {  }
				continue;
			}
			System.out.println("arg#" + i + ": " + args[i]);
			System.out.println("Filename: " + new File(args[i]).getName());
			new Thread(new ActiveTail(new DefaultTextAppender(), args[i], sleep), new File(args[i]).getName()).start();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private File				file;
	private long				lastmod = 0;
	private long				pos		= 0;
	private int					sleepMsec = 2000;
	private List<Thread>		threads = new ArrayList<Thread>();
	private TextAppender			appender;
	private static final int	BYTES_OF_OLD_STUFF = 500;
	private static String[] myargs = new String[] { "--sleep",  "400", "c:/tmp/foo.txt", "c:/tmp/foo" };
}

class DefaultTextAppender implements TextAppender
{
	public void append(String s)
	{
		// default appender
		System.out.println(s);
	}
	public void wrap(boolean b)
	{
	}
}