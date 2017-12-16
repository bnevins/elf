/*
 * MountPointFinder.java
 *
 * Created on April 27, 2004, 12:21 AM
 */

package com.elf.io;

/**
 *
 * @author  bnevins
 */
public class MountPointFinder extends FileDirFinder
{
	public MountPointFinder(String root)
	{
		super(root, "java", "src");
	}
}
