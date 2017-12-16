/*
 * StringFinder.java
 *
 * Created on May 24, 2005, 12:56 PM
 */

package com.elf.classfinder;

import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class StringFinder
{
	public StringFinder(String root) throws IOException
	{
		SourceFinder					sof = new SourceFinder(new File(root));
		TreeMap<String, List<String>>	tree = sof.getAll();
		List<File>						ff = flatten(tree);
		
		TreeMap<String, List<File>> wordTree = new TreeMap<String, List<File>>();

		for(File f : ff)
		{
			System.out.println(f);
			new WordParser(f, wordTree);
		}
		System.out.println("************************* WORDS ******************");
		Set<String> words = wordTree.keySet();
		System.out.println("Number of words: " + words.size());
		
		for(String word : words)
			System.out.println(word);
	}
	
	///////////////////////////////////////////////////////////////////////////

	List<File> flatten(TreeMap<String, List<String>> tree)
	{
		List<File>	flatfiles = new ArrayList<File>();
		Set<Map.Entry<String, List<String>>> entries = tree.entrySet();
		
		for(Map.Entry<String, List<String>> entry : entries)
		{
			String			name = entry.getKey();
			List<String>	mps  = entry.getValue();
			
			for(String mp : mps)
			{
				flatfiles.add(makeFile(mp, name));
			}
		}
		return flatfiles;
		/*
		 Set<String> classnames = tree.keySet();
		
		for(String classname : classnames)
		{
			List<String> mps = tree.get(classname);
			System.out.println("*****  key: " + classname + " ***********");
			for(String s2 : mps)
				System.out.println("MP: " + s2);
			break;
		}
		*/
	}
	
	///////////////////////////////////////////////////////////////////////////

	private File makeFile(String mp, String name)
	{
		name = name.replace('.', '/');

		if(name.endsWith("/properties"))
		{
			name = name.substring(0, name.length() - 11);
			name += ".properties";
		}
			
		File mpf = new File(mp);
		File f = new File(mpf, name);
		
		if(!f.exists())
		{
			f = new File(mpf, name + ".java");

			if(!f.exists())
				throw new RuntimeException("File doesn't exist: " + f);
		}
		
		return f;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{
		if(args.length <= 0)
			args = new String[] { "C:/temp/admin-ee" };
		try
		{
			new StringFinder(args[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
