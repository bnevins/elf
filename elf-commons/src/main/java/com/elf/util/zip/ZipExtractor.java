/* Byron Nevins, April 2000
 * ZipExtractor -- A utility class for exploding archive (zip) files.
 */

package com.elf.util.zip;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.elf.util.logging.Reporter;

///////////////////////////////////////////////////////////////////////////////

/**
 * ZipExtractor is a utility class for extracting files from a zip archive
 */
public class ZipExtractor
{
	/**
	 * Initialize a ZipExtractor object
	 * @param zipFilename The path of the archive file to extract
	 * @param explodeDirName The directory to extract the files into.
	 * @throws com.elf.util.zip.ZipFileException If anything goes wrong
	 */
	public ZipExtractor(String zipFilename, String explodeDirName) throws ZipFileException
	{
		this(new File(zipFilename), new File(explodeDirName));
	}
	/**
	 * Initialize a ZipExtractor object
	 * @param inStream The stream of the archive file to extract
	 * @param explodeDirName The directory to extract the files into.
	 * @throws com.elf.util.zip.ZipFileException If anything goes wrong
	 */
	public ZipExtractor(InputStream inStream, String explodeDirName) throws ZipFileException
	{
		this(inStream, new File(explodeDirName));
	}
	
	/**
	 * Initialize a ZipExtractor object
	 * @param zipFile The File object of the archive file to extract
	 * @param explodeDir The File object corresponding to the directory to extract the files into.
	 * @throws com.elf.util.zip.ZipFileException If anything goes wrong
	 */
	public ZipExtractor(File zipFile, File explodeDir) throws ZipFileException
	{
		checkZipFile(zipFile);
		FileInputStream fis = null;
		
		try
		{
			fis = new FileInputStream(zipFile);
		}
		catch(IOException e)
		{
			throw new ZipFileException(e);
		}
		
		ctor(fis, explodeDir);
	}
	
	/**
	 * Initialize a ZipExtractor object
	 * @param inStream The stream of the archive file to extract
	 * @param explodeDir The File object corresponding to the directory to extract the files into.
	 * @throws com.elf.util.zip.ZipFileException If anything goes wrong
	 */
	public ZipExtractor(InputStream inStream, File explodeDir) throws ZipFileException
	{
		ctor(inStream, explodeDir);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Extract all files in the archive, creating any needed directories
	 * @throws com.elf.util.zip.ZipFileException If anything goes wrong
	 * @return An Array of filenames that were extracted
	 */
	public ArrayList<String> explode() throws ZipFileException
	{
		return explode(new String[0]);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Extract all files matching the given extensions in the archive, creating any needed directories
	 * @param extensionsToExplode An array of file extensions - only files with these extensions will be extracted
	 * @throws com.elf.util.zip.ZipFileException If anything goes wrong
	 * @return An Array of filenames that were extracted
	 */
	public ArrayList<String> explode(String[] extensionsToExplode) throws ZipFileException
	{
		return explode(extensionsToExplode, true);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Extract all files matching the given extensions in the archive, creating any needed directories, possibly creating directories
	 * @param extensionsToExplode An array of file extensions - only files with these extensions will be extracted
	 * @param createDirectories Create all directories found in the archive if true
	 * @throws com.elf.util.zip.ZipFileException If anything goes wrong
	 * @return An Array of filenames that were extracted
	 */
	public ArrayList<String> explode(String[] extensionsToExplode, boolean createDirectories) throws ZipFileException
	{
		files = new ArrayList<String>();
		ZipInputStream zin = null;
		
		try
		{
			zin = zipStream;
			ZipEntry ze;
			
			while( (ze = zin.getNextEntry()) != null )
			{
				String filename = ze.getName();
				
				/*
				if(isManifest(filename))
				{
					continue;	// don't bother with manifest file...
				}
				 */
				
				File fullpath = null;
				
				if(isDirectory(filename))
				{
					if(createDirectories == false)
						continue;
					// just a directory -- make it and move on...
					fullpath = new File(explodeDir, filename.substring(0, filename.length() - 1));
					fullpath.mkdir();
					continue;
				}
				if(extensionsToExplode != null && extensionsToExplode.length > 0)
				{
					if(!extensionMatches(filename, extensionsToExplode))
						continue;
				}
				fullpath = new File(explodeDir, filename);
				File newDir	= fullpath.getParentFile();
				
				if(createDirectories)
				{
					if(newDir.mkdirs())
					{	// note:  it returns false if dir already exists...
						Reporter.fine("Created new directory:  " + newDir);//NOI18N
					}
					
					if(fullpath.delete())	// wipe-out pre-existing files
						Reporter.info("deleted pre-existing file: " + fullpath); //NOI18N
				}
				else
				{
					// strip the path from filename
					// e.g. com/elf/goo/x.jar --> explodeDir/x.jar
					filename = fullpath.getName();
				}
				
				FileOutputStream os = getOutputStream(filename);
				
				if(os == null)	// e.g. if we asked to write to a directory instead of a file...
					continue;
				
				int totalBytes = 0;
				
				for(int numBytes = zin.read(buffer); numBytes > 0; numBytes = zin.read(buffer))
				{
					os.write(buffer, 0, numBytes);
					totalBytes += numBytes;
				}
				os.close();
				Reporter.fine("Wrote " + totalBytes + " to " + filename);//NOI18N
				files.add(filename);
			}
		}
		catch(IOException e)
		{
			throw new ZipFileException(e);
		}
		finally
		{
			Reporter.fine("Closing zin...");//NOI18N
			try
			{
				zin.close();
			}
			catch(IOException e)
			{
				throw new ZipFileException("Got an exception while trying to close Jar input stream: " + e);//NOI18N
			}
		}
		Reporter.info("Successfully Exploded ZipFile"  + " to " + explodeDir.getPath());//NOI18N
		return files;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Get the list of filenames that were extracted
	 * @return The list of filenames that were extracted
	 */
	public ArrayList<String> getFileList()
	{
		if(files != null)
			return files;
		
		return null;
	}
	
	/***********************************************************************
	 * /******************************** Private ******************************
	 * /***********************************************************************/
	
	private void ctor(InputStream inStream, File anExplodeDir) throws ZipFileException
	{
		insist(anExplodeDir != null);
		explodeDir = anExplodeDir;
		
		try
		{
			zipStream = new ZipInputStream(inStream);
			checkExplodeDir();
		}
		catch(Throwable t)
		{
			throw new ZipFileException(t.toString());
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private boolean isDirectory(String s)
	{
		char c = s.charAt(s.length() - 1);
		
		return c== '/' || c == '\\';
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private void checkZipFile(File zipFile) throws ZipFileException
	{
		insist(zipFile != null);
		
		String zipFileName = zipFile.getPath();
		
		insist( zipFile.exists(),		"zipFile (" + zipFileName + ") doesn't exist" );//NOI18N
		insist( !zipFile.isDirectory(), "zipFile (" + zipFileName + ") is actually a directory!" );//NOI18N
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private void checkExplodeDir() throws ZipFileException
	{
		String explodeDirName = explodeDir.getPath();
		
		// just in case...
		explodeDir.mkdirs();
		
		insist(explodeDir.exists(),			"Target Directory doesn't exist: "		+ explodeDirName );//NOI18N
		insist(explodeDir.isDirectory(),	"Target Directory isn't a directory: "	+ explodeDirName );//NOI18N
		insist(explodeDir.canWrite(),		"Can't write to Target Directory: "		+ explodeDirName );//NOI18N
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean isSpecial(String filename)
	{
		return filename.toUpperCase().startsWith(specialDir.toUpperCase());
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private FileOutputStream getOutputStream(String filename) throws ZipFileException
	{
		//Assertion.check(explodeDir, "Programmer Error -- need to setup explodeDir");//NOI18N
		File f = new File(explodeDir, filename);
		
		if(f.isDirectory())
		{
			Reporter.warning("Weird!  A directory is listed as an entry in the jar file -- skipping...");//NOI18N
			return null;
		}
		
		try
		{
			return new FileOutputStream(f);
		}
		catch(FileNotFoundException e)
		{
			throw new ZipFileException("filename: " + f.getPath() + "  " + e);
		}
		catch(IOException e)
		{
			throw new ZipFileException(e);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean isManifest(String filename)
	{
		if(filename.toLowerCase().endsWith("manifest.mf"))//NOI18N
			return false;
		
		return false;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean extensionMatches(String filename, String[] exts)
	{
		for(int i = 0; i < exts.length; i++)
		{
			String ext = "." + exts[i];
			
			if(filename.toLowerCase().endsWith(ext.toLowerCase()))
				return true;
		}
		return false;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////////////                                                   //////////////////////////
	////////////    Internal Error-Checking Stuff                  //////////////////////////
	////////////                                                   //////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private static void pr(String s)
	{
		System.out.println( s );
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private static void insist(String s) throws ZipFileException
	{
		if( s == null || s.length() < 0 )
			throw new ZipFileException();
		else
			return;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private static void insist(String s, String mesg) throws ZipFileException
	{
		if( s == null || s.length() < 0 )
			throw new ZipFileException( mesg );
		else
			return;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private static void insist(boolean b) throws ZipFileException
	{
		if( !b )
			throw new ZipFileException();
		else
			return;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private static void insist(boolean b, String mesg) throws ZipFileException
	{
		if( !b )
			throw new ZipFileException( mesg );
		else
			return;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{
		if(args.length != 2)
		{
			System.out.println("\nhen yUsage: ZipExtractor zip-file-name dir-name");
			System.exit(1);
		}
		try
		{
			ZipExtractor zip = new ZipExtractor(args[0], args[1]);
			
			pr("" + zip);
			zip.explode();
		}
		catch(ZipFileException e)
		{
			pr("ZipFileException: " + e);//NOI18N
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	private					File			explodeDir		= null;
	private					ArrayList<String>		files			= null;
	private static final	String			specialDir		= "META-INF/";//NOI18N
	private					byte[]			buffer			= new byte[16384];
	private					ZipInputStream	zipStream		= null;
}

