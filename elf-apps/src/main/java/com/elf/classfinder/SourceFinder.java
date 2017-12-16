/*
 * SourceFinder.java
 *
 * Created on April 28, 2004, 11:13 PM
 */

package com.elf.classfinder;

import java.io.*;
import java.util.*;
import com.elf.io.*;

/**
 * Data Structures:
 * mountPoints -- a List
 * <ul><li>mount-point - java.io.File</li>
 * <li> List-of-Files - A List of FileInfo objects</li></ul>
 *   
 * 
 * @author  bnevins
 */
public class SourceFinder implements Serializable
{
	public SourceFinder(File root)
	{
		files = new TreeMap<String, List<String>>();
		this.root = FileUtils.safeGetCanonicalFile(root);
		findMountPoints();
		findFiles();
	}
	
	public void dump()
	{
		Set set = getFiles();

		for(Iterator it = set.iterator(); it.hasNext(); )
		{
			System.out.println("" + it.next());
		}
	}

	////////////////////////////////////////////////////////////////////////////
	
	public TreeMap<String, List<String>> getAll()
	{
		return files;
	}

	////////////////////////////////////////////////////////////////////////////
	
	public Set<String> getFiles()
	{
		return files.keySet();
	}

	///////////////////////////////////////////////////////////////////////////

	List getMountPoints()
	{
		return mountPointsList;
	}

	///////////////////////////////////////////////////////////////////////////

	List getMountPoints(String key)
	{
		return files.get(key);
	}

	///////////////////////////////////////////////////////////////////////////

	int getNumMountPoints()
	{
		return mountPointsList.size();
	}

	///////////////////////////////////////////////////////////////////////////

	int getNumAllFiles()
	{
		return numAllFiles;
	}
	
	///////////////////////////////////////////////////////////////////////////

	int getNumUniqueFiles()
	{
		return files.size();
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void findMountPoints()
	{
		try
		{
			System.out.println("Searching for mount-points");
			MountPointFinder fdf = new MountPointFinder(root.getPath());
			mountPointsList = fdf.getFiles();
			System.out.println("Number of mount-points: " + mountPointsList.size());
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////////////////
	
	private void findFiles()
	{
		try
		{
			numAllFiles = 0;
			
			for(Iterator it_mp = mountPointsList.iterator(); it_mp.hasNext(); )
			{
				String mp = FileUtils.safeGetCanonicalPath((File)it_mp.next());
				System.out.println("Searching for source files under mount-point: " + mp);
				FileExtFinder fef = new FileExtFinder(mp, ".java", ".properties");
				List list = fef.getFiles();
				numAllFiles += list.size();
				System.out.println("Found " + list.size() + " files in: " + mp);
				add(mp, list);
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////////////////
	
	private void add(String mp, List list)
	{
		// list -- ArrayList of java.io.File
		// add all the files (in list) to the master-tree
		// mp is the path of the mount-point it came from.
		// Expect duplicates.
		
		if(mp.length() > longestMPName)
			longestMPName = mp.length();
		
		for(Iterator it = list.iterator(); it.hasNext(); )
		{
			String className = toClass(mp, (File)it.next());
			List<String> mps = files.get(className);
			
			if(mps == null)
			{
				// first mp that contains this class
				mps = new ArrayList<String>();
			}
			
			mps.add(mp);
			files.put(className, mps);
		}
	}

	////////////////////////////////////////////////////////////////////////////
	
	private String toClass(String mp, File f)
	{
		// e.g. mp = "c:/foo/src/java"
		// e.g. f = "c:/foo/src/java/com/foo"
		String ret = FileUtils.safeGetCanonicalPath(f);
		
		ret = ret.substring(mp.length() + 1).replace('\\', '/').replace('/', '.');

		// get rid of ".java" but keep ".properties"
		if(ret.endsWith(".java"))
			ret = ret.substring(0, ret.length() - 5);
		
		return ret;
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		SourceFinder sf = new SourceFinder(new File(args[0]));
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	private File	root;
	private TreeMap<String, List<String>>	files;
	private List	mountPointsList;
	private int		longestMPName	= 0;
	private int		numAllFiles		= 0;
}
