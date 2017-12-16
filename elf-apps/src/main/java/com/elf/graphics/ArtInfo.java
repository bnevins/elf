package com.elf.graphics;

import java.awt.Image;
import javax.swing.ImageIcon;

class ArtInfo
{
    public static void main(String[] args) 
	{
		for(int i = 0; i < args.length; i++)
		{
			dumpInfo(args[i]);
		}
		System.exit(0);
    }

    /**
     * Reads an image in a file and dumps info about it.
     * @param name The name of image file.
     */
    public static void dumpInfo(String name)
	{
		// Get the image from a file.
        Image inImage = new ImageIcon(name).getImage();

		int h = inImage.getHeight(null);
		int w = inImage.getWidth(null);
		System.out.println(name + " -- width: " + w + ", Height: " + h);				
    }
}
    
