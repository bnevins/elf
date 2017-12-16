/*
 * PropertyFinder.java
 *
 * Created on October 28, 2004, 3:36 AM
 */

package com.elf.classfinder;

import com.elf.io.*;
import java.io.*;
import java.net.*;
import java.util.*;
import com.elf.util.Console;

/**
 *
 * @author  bnevins
 */
public class PropertyFinder
{
	public PropertyFinder()
	{
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void create(String root) throws IOException
	{
		db = new ArrayList<PropertyFile>();
		
		FileExtFinder fef = new FileExtFinder(root, "LocalStrings.properties");
		List files = fef.getFiles();

		for(Iterator it = files.iterator(); it.hasNext(); )
		{
			File f = (File)it.next();
			System.out.println("Processing: " + f);
			createOne(f);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void read(String resource) throws IOException, ClassNotFoundException
	{
		Class				clazz = getClass();
		InputStream			in = clazz.getResourceAsStream(resource);
		read(in);
		in.close();
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void read(File f) throws IOException, ClassNotFoundException
	{
		InputStream in = new FileInputStream(f);
		read(in);
		in.close();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public void read(InputStream is) throws IOException, ClassNotFoundException
	{
		ObjectInputStream s	= new ObjectInputStream(is);
		Object o = s.readObject();
		db	= (ArrayList<PropertyFile>)(o);
		s.close();
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void write(File f) throws IOException
	{
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f));
		os.writeObject(db);
		os.close();
	}

	///////////////////////////////////////////////////////////////////////////

	public List matches(String s)
	{
		List<PropertyFile> list = new ArrayList<PropertyFile>();
		for(Iterator it = db.iterator(); it.hasNext(); )
		{
			PropertyFile pf = (PropertyFile)it.next();
			
			if(pf.matches(s))
				list.add(pf);
		}
		return list;
	}
	
	///////////////////////////////////////////////////////////////////////////

	public int size()
	{
		if(db == null)
			return 0;
		
		return db.size();
	}

	//////////////////////////////////////////////////////////////////////////

	private void createOne(File f)
	{
		try
		{  
			// load the properties from disk
			Properties props = new Properties();
			InputStream in = new FileInputStream(f);
			props.load(in);
			in.close();
			
			// fetch the properties
			Set propset = props.entrySet();
			
			for(Iterator it = propset.iterator(); it.hasNext(); )
			{
				Map.Entry	entry	= (Map.Entry)it.next();
				String		key		= (String)entry.getKey();
				String		value	= (String)entry.getValue();
				
				// add it to the db
				db.add(new PropertyFile(f, key, value));
			}
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args)
	{
		try
		{
			PropertyFinder pf = new PropertyFinder();
			pf.create("D:/jwsee/admin");
			pf.write(new File("D:/elf/classes/pf.ser"));
			System.out.println("Number: " + pf.db.size());
			pf = new PropertyFinder();
			pf.read("/pf.ser");
			System.out.println("READ in - Number: " + pf.db.size());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private List<PropertyFile> db;
}
