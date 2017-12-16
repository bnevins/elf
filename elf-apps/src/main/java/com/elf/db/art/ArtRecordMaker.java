package com.elf.db.art;

//import java.net.*;
//import java.sql.*;
import java.io.*;
import java.util.*;
import com.elf.util.*;

public class ArtRecordMaker
{
	public static void main(String args[])
	{
		if(args.length < 3)
			usage();
			
		int defaultRating = 0;			
		
		if(args.length > 3)
			defaultRating = Integer.parseInt(args[3]);			
			
		ArtRecordMaker gfi = new ArtRecordMaker(args[0], args[1], args[2], defaultRating);
		
		gfi.doIt();
	}

	////////////////////////////////////////////////////////////////////////////

	public ArtRecordMaker(String dirName, String cat, String subcat, int defaultRating)
	{
		Assertion.check(dirName.charAt(1) == ':' && dirName.charAt(2) == File.separatorChar);

		String drive = dirName.substring(0, 1);
		root		= new File(dirName);
		artMgr		= new ArtRecordMgr(drive, cat, subcat, defaultRating);
		//pr("dirname: " + dirName);
		//pr("root: " + root.getPath());
	}

	////////////////////////////////////////////////////////////////////////////

	private void doIt()
	{
		File[] dirs	= getDirs(root);
		
		for(int i = 0; dirs != null && i < dirs.length; i++)
		{
			File[] files = getJpegs(dirs[i]);
			int rate = getRating(dirs[i]);
			
			for(int j = 0; j < files.length; j++)
				if(rate >= 0)
					artMgr.append(files[j], rate);
				else
					artMgr.append(files[j]);
		}
		
		pr("**** Here are the Directories:");
		pr(dirs);
		//pr("\n" + artMgr);
		new ArtDBMaker(artMgr);
	}
	
	////////////////////////////////////////////////////////////////////////////

	private File[] getDirs(File f)
	{
		return f.listFiles(new FileFilter()
			{
				public boolean accept(File pathname)
				{
					if(pathname.isDirectory() == false)
						return false;

					if(pathname.getName().equalsIgnoreCase("indexes"))
						return false;

					if(pathname.getName().equalsIgnoreCase("best"))
						return false;

					return true;
				}
			});
	}
	
	////////////////////////////////////////////////////////////////////////////

	private int getRating(File dir)
	{
		int rate = -1;
		
		try
		{
			System.out.println("**************** " + dir + File.separator + "rating.dat");
			BufferedReader in = new BufferedReader(new FileReader(dir + File. separator + "rating.dat"));
			String s = in.readLine();
			
			if(s != null && s.length() > 0)
				rate = Integer.parseInt(s);
			
			if(rate < 0 || rate > 10)
				rate = -1;
		}
		catch(FileNotFoundException e)
		{
		}
		catch(IOException e)
		{
		}
		
		return rate;
	}

	////////////////////////////////////////////////////////////////////////////

	private File[] getJpegs(File f)
	{
		return f.listFiles(new FileFilter()
			{
				public boolean accept(File pathname)
				{
					if(pathname.isDirectory())
						return false;

					if(pathname.getName().toLowerCase().endsWith(".jpg"))
						return true;

					return false;
				}
			});
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	private static void usage()
	{
		pr("usage:  java ArtRecordMaker dirname category sub-category [default-rating]");
		pr("    default default-rating is zero....");
		System.exit(1);
	}
			
	////////////////////////////////////////////////////////////////////////////
	
	private static void pr(String s)
	{
		System.out.println(s);
	}
			
	////////////////////////////////////////////////////////////////////////////
	
	private static void pr(File[] ff)
	{
		for(int i = 0; i < ff.length; i++)
			pr(ff[i].getPath());
	}

	////////////////////////////////////////////////////////////////////////////

	private ArtRecordMgr	artMgr	= null;
	private File				root = null;
}

