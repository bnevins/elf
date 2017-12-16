package com.elf.classfinder;
import java.io.*;
import java.util.*;
/*
 * WordParser.java
 *
 * Created on May 24, 2005, 1:46 PM
 */

/**
 *
 * @author bnevins
 */
public class WordParser
{
	public WordParser(File f, TreeMap<String, List<File>> Words) throws IOException
	{
		file = f;
		words = Words;
		parse();
		
	}
	
	///////////////////////////////////////////////////////////////////////////

	private void parse() throws IOException
	{
		LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
		
		//words = new ArrayList<String>();
		String line = null;
		for(line = reader.readLine(); line != null; line = reader.readLine())
		{
			String[] ww = parseWords(line);
			
			for(String word : ww)
			{
				List<File> files = words.get(word);
				
				if(files == null)
				{
					files = new ArrayList<File>();
					words.put(word, files);
				}

				files.add(file);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	private String[] parseWords(String line)
	{
		String[] w = line.split("\\s");
		return w;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	
	///////////////////////////////////////////////////////////////////////////
	
	private File							file;
	private TreeMap<String, List<File>>	words;
}
