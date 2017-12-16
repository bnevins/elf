
package com.elf.graphics;
import java.io.File;
/**
 *
 * @author  bnevins
 * @version $Revision: 1.1.1.1 $
 */
public class JPEGCleanAndReplace 
{
	public static void main(String[] args) 
	{
		if(args.length != 1)
			return;
		
		String originalName = args[0];
		
		if(originalName.length() < 5)
			return;
		
		File f = new File(originalName);
		
		if(!f.exists())
			return;
		
		String renamedName = originalName + ".bad"; 
		f.renameTo(new File(renamedName));
		
		JPEGUtils.JPEG2JPEG(renamedName, originalName);
		System.exit(0);
		
	}
}
