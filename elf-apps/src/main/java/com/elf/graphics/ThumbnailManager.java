/*
 * ThumbnailManager.java
 *
 * Created on June 4, 2006, 9:32 PM
 */

package com.elf.graphics;

import com.elf.io.*;
import java.awt.Dimension;
import java.util.*;
import java.io.*;

/**
 *
 * @author bnevins
 */
public class ThumbnailManager
{
	public ThumbnailManager(ThumbnailMakerInfo Info)
	{
        info = Info;
        info.validate();
    }

	///////////////////////////////////////////////////////////////////////////
	
	public void make()
	{
		getFiles();

        for(File f : files)
		{
			doOne(f);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void getFiles()
	{
		files = null;
		
		try
		{
			if(info.getRecursive())
			{
				FileExtFinder ff = new FileExtFinder(info.getRootDir(), ".jpg");
				files = ff.getFiles();
				
				// filter out any existing thumbnails
				for(Iterator<File> it = files.iterator(); it.hasNext(); )
				{
					File f = it.next();
					if(f.getName().startsWith("tn_"))
						it.remove();
				}
			}
			else
			{
                File f = new File(info.getRootDir());
                File[] fileArray = f.listFiles(new FilenameFilter()
                    {
                        public boolean accept(File dir, String name)
                        {
                            return (name.endsWith(".jpg") || name.endsWith(".JPG")) && !name.startsWith("tn");
                        }

                    });
                
                files = new ArrayList<File>();
                
                for(File ff : fileArray)
                    files.add(ff);
			}
    
            for(File f : files)
                System.out.println(f);
		}
		catch(IOException e)
		{
			// todo
		}
	}
	///////////////////////////////////////////////////////////////////////////
	
	private void doOne(File f)
	{
		++numProcessed;
		File parent = f.getParentFile();
		String	name = f.getName();
		String tn_name = info.getPrefix() + name;
		File tn = new File(parent, tn_name);
		
		if(tn.exists())
		{
            if(info.getOverwrite())
                tn.delete();
            else
            {
                System.out.println("NO OVERWRITE -- not writing: " + tn_name);
                return;
            }
		}
		
		System.out.println("Processing: " + tn);
		ThumbnailMaker maker = new ThumbnailMaker(f.getAbsolutePath(), tn.getAbsolutePath());
        
        if(info.getScaleType() == ThumbnailMakerInfo.Scales.MAX_DIM)
        {
            maker.create(info.getNumber1AsInt());
        }
        else if(info.getScaleType() == ThumbnailMakerInfo.Scales.SCALE)
            maker.create(info.getNumber1AsDouble());
        else if(info.getScaleType() == ThumbnailMakerInfo.Scales.WIDTH_HEIGHT)
            maker.create(info.getNumber1AsInt(), info.getNumber2AsInt());
        else if(info.getScaleType() == ThumbnailMakerInfo.Scales.BOUNDING_BOX)
            maker.create(new Dimension(info.getNumber1AsInt(), info.getNumber2AsInt()));
	}

    ///////////////////////////////////////////////////////////////////////////

    private	List<File>	files;
	private static int	numProcessed = 0;
    private ThumbnailMakerInfo info;
}
