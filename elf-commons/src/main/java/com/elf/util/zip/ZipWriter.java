/* Byron Nevins, April 2000
 * ZipWriter -- A utility class for writing zip files.
 * Updated to JDK 1.5, March 2005
 */

package com.elf.util.zip;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.elf.util.logging.Reporter;
import com.elf.util.ContainerHelper;
import com.elf.io.FileListerRelative;


/**
 * A class for creating Zip archives from files on disk
 */
public class ZipWriter
{
    /**
     * Creates an instance - call write() to write the archive
     * @param zipFilename The path of the zip file to create
     * @param dirName The name of the directory to archive
     * @throws com.elf.util.zip.ZipFileException if anything goes wrong
     */
    public ZipWriter(String zipFilename, String dirName) throws ZipFileException
    {
		this(zipFilename, dirName, null);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Creates an instance - call write() to write the archive
     * @param theZipFilename The path of the zip file to create
     * @param theDirName The name of the directory to archive
     * @param fileList A list of all the files to be archived - the name is <strong>relative</strong> to theDirName. E.g. if the dirname is 
     * <i>c:/tmp</i> and the files are C:/tmp/foo and C:/tmp/goo, then the array should be "foo", "goo"
     * @throws com.elf.util.zip.ZipFileException if anything goes wrong
     */
    public ZipWriter(String theZipFilename, String theDirName, String[] fileList) throws ZipFileException
    {
		zipFilename		= theZipFilename;
		dirName			= theDirName;

		try
		{
			File source = new File(dirName);
			dirName = source.getAbsolutePath();
			dirName = dirName.replace('\\', '/');	// all UNIX-style filenames...

			// we need the dirname to end in a '/'
			if(!dirName.endsWith("/"))
			dirName += "/";

			checkSource(source);
			checkTarget();
			checkFileList(fileList);
		}
		catch(Exception e)
		{
			throw new ZipFileException(e);
		}
    }
    
    /**
     * Write the zip file out
     * @throws com.elf.util.zip.ZipFileException if anything goes wrong
     */
    public void write()  throws ZipFileException
    {
		try
		{
			zipStream = new ZipOutputStream(new FileOutputStream(zipFilename));

			for(String s : fileList)
			{
				addEntry(s);
			}
		}
		catch(ZipFileException z)
		{
			throw z;
		}
		catch(Exception e)
		{
			throw new ZipFileException(e);
		}
		finally
		{
			try
			{
				if(zipStream != null)
					zipStream.close();
			}
			catch(Exception e)
			{
			}
		}
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void addEntry(String entryName)  throws ZipFileException, IOException
    {
		int					totalBytes	= 0;
		FileInputStream		in			= new FileInputStream(dirName + entryName);
		ZipEntry			ze			= new ZipEntry(entryName);

		zipStream.putNextEntry(ze);

		for(int numBytes = in.read(buffer); numBytes > 0; numBytes = in.read(buffer))
		{
			zipStream.write(buffer, 0, numBytes);
			totalBytes += numBytes;
		}

		zipStream.closeEntry();
		Reporter.finer("Wrote " + entryName + " to Zip File.  Wrote " + totalBytes + " bytes.");
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public String toString()
    {
		String s = "Zip File Name: " + zipFilename + "\n";
		s += "Directory Name: " + dirName + "\n";
		s += "***** File Contents *********\n";
		s += ContainerHelper.toOneString(fileList);
	
		return s;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void checkFileList(String[] files)
    {
		if(files != null)
		{
			for(int i = 0; i < files.length; i++)
				files[i] = files[i].replace('\\', '/');	// just in case...

			fileList = files;
		}
		else
			createFileList();
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    void createFileList()
    {
		FileListerRelative lister = new FileListerRelative(new File(dirName));
		setFileList(lister.getFiles());
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    void setFileList(String[] fl)
    {
		fileList = fl;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    String getDirName()
    {
		return dirName;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
	private void checkSource(File f) throws ZipFileException
	{
		// let's make sure the directory exists!
		if(!f.exists() || !f.isDirectory())
			throw new ZipFileException("Source directory (" + f + ") doesn't exist");
	}
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
	private void checkTarget() throws ZipFileException
	{
		File f = new File(zipFilename);
		
		if(f.exists())
		{
			Reporter.warning("Target file already exists.  I deleted it (" + f + ")");
			return;
		}
		try
		{
			f.createNewFile();
			f.delete();
		}
		catch(IOException ioe)
		{
			throw new ZipFileException("Can't create the target zip file (" + f + ")", ioe);
		}
		
	}
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private static void usage()
    {
		System.out.println("usage: java com.elf.util.zip.ZipWriter zip-filename directory-name");
		System.exit(1);
	}
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args)
    {
		Reporter.setLevel(java.util.logging.Level.INFO);

		if(args == null || args.length != 2)
			usage();

		try
		{
			ZipWriter zw = new ZipWriter(args[0], args[1]);
			zw.write();
			Reporter.fine("" + zw);
		}
		catch(ZipFileException e)
		{
			e.printStackTrace();
			Reporter.severe("ZipFileException: " + e);
			System.exit(0);
		}
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////
    
    private					String			zipFilename		= null;
    private					String			dirName			= null;
    private					ZipOutputStream zipStream		= null;
    private					String[]		fileList		= null;
    private					byte[]			buffer			= new byte[16384];
}
