package com.elf.db.art;

//import java.net.*;
//import java.sql.*;
import java.io.*;
import java.util.*;
import com.elf.util.*;

////////////////////////////////////////////////////////////////////////////


class ArtRecordMgr extends Vector<ArtRecord>
{
	public String toString()
	{
		String s = "**** ArtRecordMgr Dump\n";

		//s += "drive: [" + drive + "],  category: " + cat + ",  sub category:  " + subcat + "\n";

		int len = size();
		start();
		
		while(hasNext())
			s += next().toString() + "\n";
		
		return s;
	}	

	////////////////////////////////////////////////////////////////////////////
	
	ArtRecordMgr(String drive, String cat, String subcat, int defaultRating)
	{
		this.drive	= drive;
		this.cat	= cat;
		this.subcat	= subcat;
		this.defaultRating	= defaultRating;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	void append(File f)
	{
		add(new ArtRecord(drive, cat, subcat, f, defaultRating));
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	void append(File f, int rate)
	{
		add(new ArtRecord(drive, cat, subcat, f, rate));
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	String[] getUniqueFieldValues(String what)
	{
		HashSet<String> set = new HashSet<String>();
		
		for(start(); hasNext(); )
		{
			if(what.equals("cat"))
				set.add(next().cat);
			else if(what.equals("subcat"))
				set.add(next().subcat);
			else if(what.equals("drive"))
				set.add(next().drive);
			else if(what.equals("path"))
				set.add(next().path);
			else
				Assertion.check(false, "unknown parameter to getUniqueFieldValues(): " + what);
		}
			
		Iterator iter = set.iterator();
		String[] ss = new String[set.size()];
		int i = 0;
		
		while(iter.hasNext())
		{
			String s = (String)iter.next();
			ss[i++] = s;
		}
		return ss;
	}
				
	////////////////////////////////////////////////////////////////////////////
	
	private ArtRecord fetch(int i)
	{
		Assertion.check(i < size());

		return (ArtRecord)elementAt(i);
	}


	////////////////////////////////////////////////////////////////////////////
	
	 void start()
	{
		index = -1;
	}

	////////////////////////////////////////////////////////////////////////////
	
	 ArtRecord next()
	{
		Assertion.check(hasNext());
		
		return fetch(++index);
	}
	////////////////////////////////////////////////////////////////////////////
	
	 boolean hasNext()
	{
		if(index < size() - 1)
			return true;
		
		return false;
	}

	////////////////////////////////////////////////////////////////////////////

	private int index = -1;
	private String	cat;
	private String	subcat;
	private String	drive;
	private int		defaultRating;
}

