/*
 * JTimer.java
 *
 * Created on February 17, 2007, 10:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.process;

/**
 *
 * @author bnevins
 */
public class JTimer
{
	public static void main(String[] args)
	{
		if(args.length <= 0 || args[0].length() <= 0)
			usage();
		try
		{
			ProcessManager pm = new ProcessManager(args);
			//ProcessBuilder pb = new ProcessBuilder(args);
			long start = System.nanoTime();
			//Process p = pb.start();
			pm.execute();
			//p.waitFor();
			long end = System.nanoTime();
			
			long nanos = end - start;
			String ts = makeTimeString(nanos);
			System.out.println("Time: " + ts);
		}
		catch(Exception e)
		{
		}
	}
	
	
	
	public static void usage()
	{
		System.out.println("Usage: JTimer command-line");
	}
	
	public static String makeTimeString(long nanos)
	{
		if(nanos < THOUSAND)
			return "" + nanos + " nsec";
		if(nanos < MILLION)
			return "" + (nanos / THOUSAND) + "." + (nanos % THOUSAND) + " usec";
		if(nanos < BILLION)
			return "" + (nanos / MILLION) + "." + ((nanos % MILLION) / THOUSAND) + " msec";
		if(nanos < 3 * NANOMIN)
			return "" + (nanos / BILLION) + "." + ( (nanos % BILLION) / 1000000) + " sec";
		return "" + (nanos / NANOMIN) + ":" + ((nanos % NANOMIN) / BILLION);
	}
	public static final long	THOUSAND = 1000;
	public static final long	MILLION = 1000000;
	public static final long	BILLION = 1000000000;
	public static final long	NANOMIN = 60 * BILLION;
	public static final long	NANOHOUR = 60 * NANOMIN;
}