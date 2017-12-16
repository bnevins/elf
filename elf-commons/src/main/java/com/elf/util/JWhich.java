/**
 * @version 1.00 April 1, 2000
 * @author Byron Nevins
 */

package com.elf.util;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import java.util.logging.*;
import com.elf.io.*;

public class JWhich
{
	public static void main(String[] args)
	{
		try
		{
			if(args == null || args.length == 0)
			{
				usage();
				return;
			}

			int argNum = 0;

			JWhich jwhich;

			if(args[0].toLowerCase().startsWith("-c"))
			{
				if(args.length != 3)
				{
					usage();
					return;
				}

				jwhich = new JWhich(args[2], args[1]);
			}
	/*		else if(args[0].toLowerCase().startsWith("-f"))
			{
				if(args.length != 3)
				{
					usage();
					return;
				}
			}
	*/		
			else if(args[0].toLowerCase().startsWith("-j"))
			{
				// get all the .jars and .zip in this directory
				if(args.length != 3)
				{
					usage();
					return;
				}

				String cp = buildClasspathFromDir(args[1]);

				if(cp == null)
				{
					return;
				}
				jwhich = new JWhich(args[2], cp);
			}
			else
				jwhich = new JWhich(args[0]);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////
	
	public JWhich(String classname, String classpathArg)
	{
		this.classpathArg = classpathArg;
		ctor(classname);
	}

	///////////////////////////////////////////////////////////
	
	public JWhich(String classname)
	{
		ctor(classname);
	}

	///////////////////////////////////////////////////////////
	
	public String getResult()
	{
		return result;
	}

	///////////////////////////////////////////////////////////
	
	private void ctor(String classname)
	{
		this.classname = classname;

		//if(doExhaustive)
			//doReflect = false;

		initClasspath();
		fixClassname();
		String[] locations = findClass();
		
		pr("");

		if(locations == null || locations.length <= 0)
		{
			pr("Can't find class");
			return;
		}

		for(int i = 0; i < locations.length; i++)
			pr(classname + " located in " + locations[i]);
		
		//if(doReflect)
			//new Reflect(classname);
	}

	///////////////////////////////////////////////////////////
	
	private static void usage()
	{
		System.out.println("Usage:  java  " + JWhich.class.getName() + " [-c[lasspath] a_classpath] [-j[ars] pathWithJars] classname");
	}

	///////////////////////////////////////////////////////////
	
	private static String buildClasspathFromDir(String dirName) throws IOException
	{
		FileFinder ff = new FileExtFinder(dirName, new String[] { ".jar", ".zip"});
		List files = ff.getFiles();
		String[] jars = new String[files.size()];
		StringBuffer cp = new StringBuffer();
		
		int i = 0;
		
		for(Iterator it = files.iterator(); it.hasNext(); )
		{
			jars[i] = ((File)it.next()).getAbsolutePath();
			cp.append(jars[i]).append(File.pathSeparatorChar);
			++i;
		}

		System.out.println("Returning this as cp: " + cp);
		return cp.toString();
	}

	///////////////////////////////////////////////////////////
	
	private void initClasspath()
	{
		String cp;

		if(classpathArg == null)
			cp = System.getProperty("java.class.path");
		else
			cp = classpathArg;

		StringTokenizer		tokens	= new StringTokenizer(cp, File.pathSeparator, false);
		int					nTokens = tokens.countTokens();

		classpath = new String[nTokens];

		debug("" + nTokens + " tokens.");
		
		for(int i = 0; tokens.hasMoreTokens(); i++) 
		{
			String s = tokens.nextToken();
			debug(s);
			classpath[i] = s;
		}
     }

	///////////////////////////////////////////////////////////
	
	private void fixClassname()
	{
		// change as follows:
		// com.netscape.blizzard.foo -->  com\netscape\blizzard\foo
		// com/netscape/blizzard/foo -->  com\netscape\blizzard\foo
		// com/netscape\blizzard.foo -->  com\netscape\blizzard\foo

		debug("old classname: " + classname);
		jarClassname = classname;

		classname = classname.replace('.', File.separatorChar);

		if(File.separatorChar != '/')
			classname = classname.replace('/', File.separatorChar);
		
		if(File.separatorChar != '\\')
			classname = classname.replace('\\', File.separatorChar);
		
		// classnames in jars ALWAYS look like: com/foo/goo.class

		jarClassname	= jarClassname.replace('.', '/');
		jarClassname	= jarClassname.replace('\\', '/');
		
		classname		= classname		+ ".class";
		jarClassname	= jarClassname	+ ".class";

		debug("new classname: " + classname);
		debug("new jarClassname: " + jarClassname);
	}

	///////////////////////////////////////////////////////////
	
	private String[] findClass()
	{
		List<String> names = new ArrayList<String>();

		for(int i = 0; i < classpath.length; i++)
		{
			String path = classpath[i];

			if(findClass(path))
			{
				names.add(path);
				debug("FOUND IT:  " + path);
			}
		}

		int num = names.size();

		debug("Found it in " + num + " places");

		if(num <= 0)
		{
			return null;
		}

		String[] ss = new String[num];
		ss = names.toArray(ss);
		return ss;
	}

	///////////////////////////////////////////////////////////
	
	private boolean findClass(String path)
	{
		if(path.toLowerCase().endsWith(".jar"))
		{
			return findClassInJar(path);
		}

		File f = new File(path + File.separator + classname);
		debug("Looking for " + f);

		return f.exists();
	}

	///////////////////////////////////////////////////////////
	
	private boolean findClassInJar(String path)
	{
		ZipInputStream zin = null;

		try
		{
			zin = new ZipInputStream(new FileInputStream(path));
			ZipEntry entry;

			while((entry = zin.getNextEntry()) != null)
			{  
				String name = entry.getName();
				zin.closeEntry();

				if(name.equals(jarClassname))
				{
					zin.close();
					return true;
				}
			}
			zin.close();
		}
		catch(IOException e)
		{
			debug("" + e + "  " + path);
		}
	
		return false;
	}

	///////////////////////////////////////////////////////////

	private void debug(String s)
	{
		if(debug_)
			pr(s);
	}

	///////////////////////////////////////////////////////////
	
	private void pr(String s)
	{
		System.out.println(s);
		result += s;
	}

	///////////////////////////////////////////////////////////
	
	private String[]	classpath		= null;
	private String		classpathArg	= null;
	private String		classname		= null;
	private String		jarClassname	= null;
	private boolean		doReflect		= false;
	private boolean		doExhaustive	= true;
	private boolean		debug_			= false;
	private String		result			= new String();
}
