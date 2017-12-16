/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * Header Notice in each file and include the License file 
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.  
 * If applicable, add the following below the CDDL Header, 
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information: 
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */

/*
 * OS.java
 *
 * Created on December 8, 2001, 5:48 PM
 */

package com.elf.util;

import java.io.*;

/**
 *
 * @author  bnevins
 * @version
 */
public class OS
{
	private OS()
	{
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isWindows()
	{
		return File.separatorChar == '\\';
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isUNIX()
	{
		return File.separatorChar == '/';
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isUnix()
	{
		// convenience method...
		return isUNIX();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isSun()
	{
		return isName("sun");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isSolaris10()
	{
		return isSun() && isVersion("5.10");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isSunSparc()
	{
		return isName("sun") && isArch("sparc");
	}
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isSunX86()
	{
		return isName("sun") && isArch("x86");
	}

	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isLinux()
	{
		return isName("linux");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isDarwin()
	{
		return isName("Mac OS X");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static boolean isWindowsForSure()
	{
		return isName("windows") && isWindows();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static boolean isArch(String name)
	{
		String archname = System.getProperty("os.arch");
		
		if(archname == null || archname.length() <= 0)
			return false;
		
		// case insensitive compare...
		archname= archname.toLowerCase();
		name= name.toLowerCase();
		
		if(archname.indexOf(name) >= 0)
			return true;
		
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static boolean isName(String name)
	{
		String osname = System.getProperty("os.name");
		
		if(osname == null || osname.length() <= 0)
			return false;
		
		// case insensitive compare...
		osname	= osname.toLowerCase();
		name	= name.toLowerCase();
		
		if(osname.indexOf(name) >= 0)
			return true;
		
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static boolean isVersion(String version)
	{
		String osversion = System.getProperty("os.version");
		
		if(osversion == null || osversion.length() <= 0 || version == null || version.length() <= 0 )
			return false;
		
		if(osversion.equals(version))
			return true;
		
		return false;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static final String WINDOWS_BATCH_FILE_EXTENSION = ".bat";
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[])
	{
		System.out.println("os.version = "					+ System.getProperty("os.version"));
		System.out.println("os.name = "						+ System.getProperty("os.name"));
		System.out.println("os.arch = "						+ System.getProperty("os.arch"));
		System.out.println("isUNIX() returned: "			+ isUNIX());
		System.out.println("isWindows() returned: "			+ isWindows());
		System.out.println("isWindowsForSure() returned: "	+ isWindowsForSure());
		System.out.println("isSun() returned: "				+ isSun());
		System.out.println("isLinux() returned: "			+ isLinux());
		System.out.println("isSunX86() returned: "			+ isSunX86());
		System.out.println("isSunSparc() returned: "		+ isSunSparc());
		System.out.println("isDarwin() returned: "			+ isDarwin());
		System.out.println("isSolaris10() returned: "		+ isSolaris10());
	}
}
