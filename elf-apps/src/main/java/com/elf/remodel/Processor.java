/*
 * Processor.java
 *
 * Created on June 10, 2006, 12:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.remodel;

import com.elf.graphics.ThumbnailMaker;
import java.awt.Dimension;
import java.io.*;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class Processor
{
	public Processor(String rootBigName, String rootThumbName) throws ProcessorException
	{
		rootBig			= new File(rootBigName);
		rootThumb		= new File(rootThumbName);
		
		setupDirs();
		
		for(File f : areas)
			doArea(f);
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void doArea(File area) throws ProcessorException
	{
		// create the thumbnails for the area!
		File thumbArea = new File(rootThumb, area.getName());
		File[] files = area.listFiles(new JPGFilter());
		debug(Arrays.toString(files));
		for(File f : files)
		{
			String imageName = f.getAbsolutePath();
			String thumbNameMedium = new File(thumbArea, "tn_med_" + f.getName()).getAbsolutePath();
			String thumbNameSmall = new File(thumbArea, "tn_small_" + f.getName()).getAbsolutePath();
			debug("Processing: " + imageName + ", " + thumbNameMedium + ", " + thumbNameSmall);
			
			System.out.println("Creating " + thumbNameMedium + "...");
			ThumbnailMaker maker = new ThumbnailMaker(imageName, thumbNameMedium);
			maker.create(medium);
			System.out.println("Creating " + thumbNameSmall + "...");
			maker = new ThumbnailMaker(imageName, thumbNameSmall);
			maker.create(small);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void setupDirs() throws ProcessorException
	{
		setupRootDirs();
		setupAreaDirs();
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void setupRootDirs() throws ProcessorException
	{
		if(!rootBig.exists())
			throw new ProcessorException("Big Image Dir does not exist: " + rootBig);
		
		if(!rootThumb.exists())
		{
			File parent = rootThumb.getParentFile();
			
			if(!parent.exists())
				throw new ProcessorException("Parent of thumbdir does not exist: " + parent);
			
			rootThumb.mkdir();
			
			if(!rootThumb.exists())
				throw new ProcessorException("Can not create thumb dir: " + rootThumb);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void setupAreaDirs() throws ProcessorException
	{
		// an "area" is a subdir 
		// for each subdir in the 'big' root, make sure there is a 
		// corresponding one in the 'thumb' root
		
		areas = rootBig.listFiles(new FileFilter()
			{
				public boolean accept(File f)
				{
					return f.isDirectory();
				}
			});
		
		// sanity check
		if(areas.length < 1)
			throw new ProcessorException("Bad area list: " + Arrays.toString(areas));

			for(File area : areas)
			{
				File thumbArea = new File(rootThumb, area.getName());

				if(!thumbArea.exists())
					thumbArea.mkdir();
				
				if(!thumbArea.exists())
					throw new ProcessorException("Can not create thumb subdir: " + thumbArea);
			}
			
	}

	///////////////////////////////////////////////////////////////////////////
	
	private void debug(String s)
	{
		if(_debug)
		{
			System.out.println("DEBUG: " + s);
		}
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
            if(args.length != 2)
            {
                System.out.println("Usage: Processor big-root-dir thumb-root-dir");
                System.exit(1);
            }
            try
            {
                new Processor(args[0], args[1]);
                //new Processor("C:/pe/domains/domain1/docroot/pix/remodel", "C:/pe/domains/domain1/docroot/images/remodel");
            } 
            catch (ProcessorException ex)
            {
                    ex.printStackTrace();
            }
	}

	///////////////////////////////////////////////////////////////////////////
	
	//private					String		rootBigName		= "C:/pe/domains/domain1/docroot/pix/remodel";
	//private					String		rootThumbName	= "C:/pe/domains/domain1/docroot/images/remodel";
	private					File[]		areas;
	private					File		rootBig;
	private					File		rootThumb;
	private final static	boolean		_debug			= false;
	private final static	Dimension	small			= new Dimension(200, 150);
	private final static	double		medium			= 0.5;
	
	
	private static class JPGFilter implements FilenameFilter
	{
		public boolean accept(File dir, String name)
		{
			boolean isJPG = name.toLowerCase().endsWith(".jpg");
			boolean isThumb = name.toLowerCase().startsWith("tn_");
			
			return isJPG && !isThumb;
		}
	}
			
}
