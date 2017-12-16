package com.elf.graphics;

class JPEGRotater
{
    public static void main(String[] args) 
	{
		if(args.length < 3)
			usage();

        JPEGUtils.rotate(args[0], args[1], args[2]);
    }

	//////////////////////////////////////////////////////////////	
	
	public static void usage()
	{
		System.out.println("\n\nUsage:\njava com.elf.graphics.JPEGResizer input-filename output-filename scaling-factor");
		System.out.println("\nJPEGResizer reads in the input-file, resizes it and writes the resulting image to the output-filename.");
		System.out.println("The scaling factor can be either an integer or a floating point number (i.e. contains a dot).");
		System.out.println("Integer scaling factor:  The longest dimension of the new image will be this integer pixels.");
		System.out.println("Floating point scaling factor:  The dimensions of the image will be multiplied by this factor.");
		System.out.println("\nExamples -- assume the input-file is 1800x1200 pixels:");
		System.out.println("\nJPEGResizer filename 0.5 --> creates a 900x600 image");
		System.out.println("\nJPEGResizer filename 2.0 --> creates a 3600x2400 image");
		System.out.println("\nJPEGResizer filename 450 --> creates a 450x300 image");
		System.exit(0);
	}

	//////////////////////////////////////////////////////////////	
}	

