package com.elf.db.art;

import java.io.*;
import java.util.*;
import com.elf.util.*;

////////////////////////////////////////////////////////////////////////////

class ArtRecord
{
	ArtRecord(String drive, String cat, String subcat, File file, int rating)
	{
		this.drive		= drive;
		this.cat		= cat;
		this.subcat		= subcat;
		this.fileName	= file.getName();
		this.rating	= rating;
		parsePath(file);
	}

	////////////////////////////////////////////////////////////////////////////

	private void parsePath(File f)
	{
		Assertion.check(f);
		File parent = f.getParentFile();
		Assertion.check(parent);
		String parentName = parent.getName();
		
		if(isRating(parentName))
		{
			rating = Integer.parseInt(parentName);
			parent = parent.getParentFile();
			Assertion.check(parent);
		}

		path = parent.getPath().substring(3);

		StringTokenizer st = new StringTokenizer(parent.getName());
     
		if(st.hasMoreTokens())
			firstName = st.nextToken();

		if(st.hasMoreTokens())
			lastName = st.nextToken();
		else
			lastName = firstName;
	}
	
	////////////////////////////////////////////////////////////////////////////

	public String toString()
	{
		final String q  = "'";
		final String qc = "', '";

		String s = q + fileName + qc + drive + qc + path + qc + cat + qc + subcat + qc + firstName + qc + lastName + qc + rating + q;
		return s;
	}
	
	////////////////////////////////////////////////////////////////////////////

	private boolean isRating(String s)
	{
		Assertion.check(s);
		
		int len = s.length();
		
		if(len == 1 && isDigit(s.charAt(0)))
			return true;
		
		if(len == 2 && isDigit(s.charAt(0)) && isDigit(s.charAt(1)))
			return true;
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////

	private boolean isDigit(char c)
	{
		return c >= '0' && c <= '9';
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	String	drive;
	String	cat;
	String	subcat = "";
	String	fileName;
	int		rating	= 0;
	String	path;
	String	firstName = "";
	String	lastName = "";
}

////////////////////////////////////////////////////////////////////////////

