/*
 * JarFinder.java
 *
 * Created on May 25, 2003, 11:04 AM
 */

package com.elf.io;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/** base class for finding files in a file system or an
 * archive file.
 *
 * @author bnevins
 * @version 1.0
 */
public class FileFinder implements FileFilter
{
    /**
     * This constructor is for FileExtFinder
     * @param rootDir The root of the file system or an archive file
     * @param ext An array of extensions.  The dot is advisable, as it does a String.endsWith() to check.
     */    
    FileFinder(String rootDir, String... ext) 
	{
		setRoot(rootDir);
		setExtensions(ext);
	}

	///////////////////////////////////////////////////////////////////////////

	public FileFinder(String rootDir) 
	{
		setRoot(rootDir);
	}
	
	///////////////////////////////////////////////////////////////////////////

	public FileFinder(String rootDir, FileFilter ff) 
	{
		setRoot(rootDir);
        setFilter(ff);
	}

	///////////////////////////////////////////////////////////////////////////

	public FileFinder() 
	{
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public void setRoot(String rootDir)
	{
        root = new File(rootDir);

		if(!root.exists())
			throw new IllegalArgumentException("Bad Directory Argument: " + rootDir);
	}
	
	///////////////////////////////////////////////////////////////////////////
	
    public void setExtensions(String[] ext) 
	{
		extensions = ext;
		
		for(String s : extensions)
			s = s.toLowerCase();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public void setFilter(FileFilter ff)
	{
		filter = ff;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public boolean accept(File f)
	{
        if(filter != null)
            return filter.accept(f);
        
		return acceptInternal(f);
	}

	///////////////////////////////////////////////////////////////////////////

	public List<File> getFiles() throws IOException
	{
		if(fileList == null)
		{
			fileList = new ArrayList<File>(500);
			find();
		}
		
		return fileList;
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void printDupes() throws IOException
	{
        List<File[]> dupes = getDupes();
        
        for(File[] files : dupes)
        {
            System.out.println("DUPES for " + files[0].getName());
            
            for(File f : files)
                System.out.println("     " + f);
        }
    }	
	
	///////////////////////////////////////////////////////////////////////////

	public List<File[]> getDupes() throws IOException
	{
        countDupes();
        List<File[]> dupes = new ArrayList<File[]>();
        
        Set<Map.Entry<String,List<File>>> set = dupesCountMap.entrySet();
		
		for(Iterator<Map.Entry<String,List<File>>> it = set.iterator(); it.hasNext(); )
		{
			Map.Entry<String,List<File>> elem	= it.next();
			List<File> files = elem.getValue();
			int num = files.size();
            
			if(num > 1)
			{
                File[] fileArray = new File[num];
                int i = 0;
				for(File f : files)
				{
                    fileArray[i++] = f;
                }
                dupes.add(fileArray);
			}
		}
        return dupes;
	}	
	
	///////////////////////////////////////////////////////////////////////////

	private boolean acceptInternal(File f)
	{
		if(f.isDirectory())
			return true;
		
		String name = f.getName();
		return acceptInternal(name);
	}

	
	///////////////////////////////////////////////////////////////////////////

	private boolean acceptInternal(String name)
	{
		if(extensions == null)
		{
			// null extensions means accept EVERYTHING
			return true;
		}
		
		for(int i = 0; i < extensions.length; i++)
		{
			if(name.toLowerCase().endsWith(extensions[i]))
				return true;
		}
		
		return false;
	}

	///////////////////////////////////////////////////////////////////////////

	private void find() throws IOException
	{
		assert fileList != null;
		find(root);
		//System.out.println("" + fileList.size() + " matching files found.");
	}
	
	///////////////////////////////////////////////////////////////////////////

	private void countDupes() throws IOException
	{
		// create a Map of (small) filename :: ArrayList of File objects
        getFiles();
		dupesCountMap.clear();
		
		for(File f : fileList)
		{
			String	key		= f.getName();
			List<File> files = dupesCountMap.get(key);
			
			if(files == null)
			{
				// first one
				files = new ArrayList<File>();
				dupesCountMap.put(key, files);
			}
			
			files.add(f);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	private void find(File dir) throws IOException
	{
		if(!dir.isDirectory())
		{
			find(new ZipInputStream(new FileInputStream(dir)));
			return;
		}
		
		File[] files = dir.listFiles(this);
		
		for(int i = 0; files != null && i < files.length; i++)
		{
			// we go in here for EVERY directory in the tree.  If
			// we have an official filter -- it may be a dir-accepting filter!
			if(files[i].isDirectory())
			{
				if(filter != null && filter.accept(files[i]) && wantDirs)
					fileList.add(files[i]);

				find(files[i]);
			}
			else
			{
				fileList.add(files[i]);	// actual file
				//System.out.println(files[i]);
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	private void find(ZipInputStream zin)
	{
		try
		{
			ZipEntry entry;

			while((entry = zin.getNextEntry()) != null)
			{  
				String name = entry.getName();
				
				if(!name.endsWith("/") && acceptInternal(name))
				{
					fileList.add(new File(name));
				}
				
				zin.closeEntry();
			}
			zin.close();
		}
		catch(IOException e)
		{
		}
	}

	///////////////////////////////////////////////////////////////////////////

	private	File							root;
	private	List<File>						fileList		= null;
	private SortedMap<String,List<File>>	dupesCountMap	= new TreeMap<String,List<File>>();
	private String[]						extensions		= null;
	private FileFilter						filter          = null;
    boolean                                 wantDirs        = false;
    // TODO do I ALWAYS not wantDirs???
    
	///////////////////////////////////////////////////////////////////////////
}

