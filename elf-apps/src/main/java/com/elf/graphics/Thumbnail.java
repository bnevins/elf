package com.elf.graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class Thumbnail
{
	public static void main(String[] args)
	{
		if(args.length != 3)
		{
			System.out.println("USAGE: com.elf.graphics.Thumbnail original-file thumb-file scaling");
			System.exit(-1);
		}
		createThumbnail(args[0], args[1], args[2]);
	}
	
	/**
	 * Reads an image in a file and creates a thumbnail in another file.
	 * @param orig The name of image file.
	 * @param thumb The name of thumbnail file.  Will be created if necessary.
	 * @param maxDim The width and height of the thumbnail must
	 *               be maxDim pixels or less.
	 */
	public static void createThumbnail(String orig, String thumb, String scaling)
	{
		try
		{
			double scale ;
			// Get the image from a file.
			Image inImage = new ImageIcon(orig).getImage();
			
			// Determine the scale.
			// if it is an int -- that means pixels, if it is a double -- that means
			// a scaling factor...
			// May 2006 -- Added Max-width, Max-Height.  i.e say you want the thumbnail
			// to be exactly 150 pixels tall -- enter H150 as the scale argument
			// for a width of 150 pixels use W150
			
			char letter = scaling.charAt(0);
			
			switch(letter)
			{
				case 'H':
				case 'W':
					scaling = scaling.substring(1);
					break;
				default: letter = 'N';	// no preference
			}
			
			try
			{
				int maxDim = Integer.parseInt(scaling);

				if(letter == 'H')
					scale = (double)maxDim/(double)inImage.getHeight(null);

				else if(letter == 'W')
					scale = (double)maxDim/(double)inImage.getWidth(null);
				
				else
				{
					scale = (double)maxDim/(double)inImage.getHeight(null);
					if (inImage.getWidth(null) > inImage.getHeight(null))
					{
						scale = (double)maxDim/(double)inImage.getWidth(null);
					}
				}
			}
			catch(NumberFormatException e)
			{
				scale = Double.parseDouble(scaling);
			}
			
			// Determine size of new image. One of them
			// should equal maxDim.
			int scaledW = (int)(scale*inImage.getWidth(null));
			int scaledH = (int)(scale*inImage.getHeight(null));
			
			// Create an image buffer in which to paint on.
			BufferedImage outImage = new BufferedImage(scaledW, scaledH,
					BufferedImage.TYPE_INT_RGB);
			
			// Set the scale.
			AffineTransform tx = new AffineTransform();
			
			// If the image is smaller than the desired image size,
			// don't bother scaling.
			if (scale < 1.0d)
			{
				tx.scale(scale, scale);
			}
			
			// Paint image.
			Graphics2D g2d = outImage.createGraphics();
			g2d.drawImage(inImage, tx, null);
			g2d.dispose();
                        ImageIO.write(outImage, "jpeg", new File(thumb));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

