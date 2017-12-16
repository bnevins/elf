package com.elf.graphics;

class JPEGInfo
{
    public static void main(String[] args) 
	{
		if(args.length < 1)
			usage();
		
		for(int i = 0; i < args.length; i++)
			JPEGUtils.dumpInfo(args[i]);

		System.exit(0);
    }

	//////////////////////////////////////////////////////////////	
	
	public static void usage()
	{
		System.out.println("\n\nUsage:\njava com.elf.graphics.JPEGInfo filename1 filename2 filename3 ...");
		System.out.println("\nJPEGInfo prints out the dimensions, in pixels, of the input image...");
		System.exit(0);
	}

	//////////////////////////////////////////////////////////////	
}	

