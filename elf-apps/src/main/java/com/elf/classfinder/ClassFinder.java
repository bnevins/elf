/*
 * FileExtFinder.java
 *
 * Created on May 25, 2003, 11:04 AM
 */

package com.elf.classfinder;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.elf.util.StringUtils;
import com.elf.util.Console;
import com.elf.io.*;

/**
 *
 * @author  bnevins
 */
public class ClassFinder implements Serializable
{
	public ClassFinder(String rootDir) 
	{
		this.rootDir = rootDir;
	}

	///////////////////////////////////////////////////////////////////////////

	public static ClassFinder unSerialize(String filename) throws IOException, ClassNotFoundException
	{
		FileInputStream		in	= new FileInputStream(filename);
		ObjectInputStream	s	= new ObjectInputStream(in);
		ClassFinder			cf	= (ClassFinder)s.readObject();
		s.close();
		return cf;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public void serialize(String filename) throws IOException, ClassNotFoundException
	{
		File f = new File(filename);
		File parent = f.getParentFile();

		if(parent != null && !parent.exists())
			parent.mkdirs();

		FileOutputStream out = new FileOutputStream(f);
		ObjectOutputStream s = new ObjectOutputStream(out);
		s.writeObject(this);
		s.flush();
		s.close();
	}

	///////////////////////////////////////////////////////////////////////////

	List<File> getJars()
	{
		return jarList;
	}

	///////////////////////////////////////////////////////////////////////////

	List<String> getJars(String key)
	{
		return classes.get(key);
	}

	///////////////////////////////////////////////////////////////////////////

	Set<String> getClasses()
	{
		return classes.keySet();
	}

	///////////////////////////////////////////////////////////////////////////

	int getNumJars()
	{
		return jarList.size();
	}

	///////////////////////////////////////////////////////////////////////////

	int getNumAllClasses()
	{
		return numAllClasses;
	}
	///////////////////////////////////////////////////////////////////////////

	int getNumUniqueClasses()
	{
		return classes.size();
	}

	///////////////////////////////////////////////////////////////////////////

	SortedMap<String, List<String>> getDupes()
	{
		// classes is a Map of classname -> ArrayList of jarnames that contain it.
		// go through the Map and find all classes with > 1 containing jar
		
		SortedMap<String, List<String>> dupes = new TreeMap<String, List<String>>();
		Set<Map.Entry<String,List<String>>> set = classes.entrySet();
		
		for(Map.Entry<String,List<String>> entry : set)
		{
			String key = entry.getKey();
			
			if(key.indexOf('$') >= 0)
				continue;	// ignore nested classes
				
			List<String> theJars = entry.getValue();
			
			int num = theJars.size();
			
			if(num > 1)
			{
				dupes.put(key, theJars);
				/*
				String line = "" + num + "," + entry.getKey();
				
				for(String jar : theJars)
					line += "," + jar;
				
				System.out.println(line);
				 **/
			}
		}
		
		return dupes;
	}

	///////////////////////////////////////////////////////////////////////////

	void findClasses() throws IOException
	{
		numAllClasses = 0;
		FileExtFinder fef = new FileExtFinder(rootDir, ".jar");
		System.out.println("Getting list of jars...");
		jarList = fef.getFiles();
		System.out.println("" + jarList.size() + " jars found");
		
		//for(Iterator it = jarList.iterator(); it.hasNext(); )
        for(File f : jarList)
		{
			//File f = (File)it.next();
			String name = f.getAbsolutePath();
			System.out.println("Processing " + name + " ...");
			fef = new FileExtFinder(name, ".class");
			List<File> list = fef.getFiles();
            //System.out.println("FEF.getFiles() --> " + list.get(0).getClass());
			numAllClasses += list.size();
			System.out.println("Found " + list.size() + " classes");
			add(name, list);
		}
	}

	///////////////////////////////////////////////////////////////////////////

	void write(String outFilename) throws IOException
	{
		OutputStream    outs    = new FileOutputStream(outFilename);
		PrintWriter     pw      = new PrintWriter(outs, false);
		
		Set<Map.Entry<String, List<String>>> set = classes.entrySet();
		int totalNumberClasses = 0;
		
		for(Map.Entry<String, List<String>> en : set)
		{
			String          className	= en.getKey();
			List<String>    theJars		= en.getValue();
			String          jarnames	= "";
			boolean         firstName  = true;
            
			for(String jarname : theJars)
			{
				//int pad = longestJarName + 1;
				//jarnames += StringUtils.padRight((String)itj.next(), pad);
				
				if(firstName)
					firstName = false;
				else
					jarnames += ",";
				
				jarnames += jarname;
			}
			pw.println(className + "," + theJars.size() + "," + jarnames);
			//pw.println(StringUtils.padRight(className, longestClassName + 1) + StringUtils.padRight("" + theJars.size(), 4) + jarnames);
			totalNumberClasses += theJars.size();
		}
		pw.close();
		System.out.println("Total number of unique class names: " + classes.size());
		System.out.println("Total number of class files: " + totalNumberClasses);
	}

	///////////////////////////////////////////////////////////////////////////

	private void add(String jar, List<File> list)
	{
		// add all the classes (in list) to the master-tree
		// jar is the path of the jar it came from.
		// Expect PLENTY of duplicates.
		
		if(jar.length() > longestJarName)
			longestJarName = jar.length();
		
        for(File f : list)
		{
			String s            = f.getPath();
			String className    = toClass(s);
			
			List<String> jars = classes.get(className);
			
			if(jars == null)
			{
				// first jar that contains this class
				jars = new ArrayList<String>();
			}
			
			jars.add(jar);
			classes.put(className, jars);
		}
	}

	///////////////////////////////////////////////////////////////////////////

	private String toClass(String fname)
	{
		fname = fname.substring(0, fname.length() - 6);
		fname = fname.replace('\\', '/').replace('/', '.');
		
		int len = fname.length();
		
		if(len > longestClassName)
			longestClassName = len;
		
		return fname;
	}

	///////////////////////////////////////////////////////////////////////////

	private static void usage()
	{
		System.out.println("Usage: java com.elf.classfinder.ClassFinder searchpath textfileoutput");
		System.out.println("Enter the root-directory of the file system that contains jar files and");
		System.out.println("I will write a list of EVERY class in every jar in textfileoutput.");
		System.exit(1);
	}

	///////////////////////////////////////////////////////////////////////////

	private String	rootDir;
	private List<File>	jarList				= null;
	TreeMap<String, List<String>>	classes				= new TreeMap<String, List<String>>();
	private int		longestClassName	= 0;
	private int		longestJarName		= 0;
	private int		numAllClasses		= 0;

	///////////////////////////////////////////////////////////////////////////


	public static void main(String[] args) 
	{
		if(args.length < 2)
			usage();
		
		ClassFinder cf = new ClassFinder(args[0]);
		
		try
		{
			cf.findClasses();
			cf.write(args[1]);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		System.exit(0);
	}
}

/*
		Console.readLine("Start Timing serializing tree");
		FileOutputStream out = new FileOutputStream("d:/temp/serialized.txt");
		ObjectOutputStream s = new ObjectOutputStream(out);
		s.writeObject(classes);
		s.flush();
		s.close();
		Console.readLine("DONE!!!!");
		
*/

