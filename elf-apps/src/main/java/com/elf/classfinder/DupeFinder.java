/*
 * DupeFinder.java
 *
 * Created on February 22, 2006, 10:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.classfinder;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author bnevins
 */
public class DupeFinder
{
	public DupeFinder(String filename, String prefixToRemove) throws IOException, ClassNotFoundException
	{
		prefixLength = prefixToRemove.length();
		load(filename);
		filterClasses();
		cleanupJarNames();
		System.out.println(getHTML());
		//dump();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public DupeFinder(String dirName) throws IOException
	{
		prefixLength = dirName.length();
		classFinder = new ClassFinder(dirName);
		classFinder.findClasses();
		dupes = classFinder.getDupes();
		filterClasses();
		cleanupJarNames();
		System.out.println(getHTML());
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private String getHTML()
	{
		StringBuilder sb = new StringBuilder(getHTMLBegin());
		
		Set<Map.Entry<String,List<String>>> set = dupes.entrySet();
		
		for(Map.Entry<String,List<String>> entry : set)
		{
			String classname = entry.getKey();
			List<String> jars = entry.getValue();
			
			sb.append("<tr>\n");
			sb.append("<td>");
			sb.append(classname);
			sb.append("</td>");
			
			for(String jar : jars)
			{
				sb.append("<td>");
				sb.append(jar);
				sb.append("</td>");
			}
			
			sb.append("\n</tr>\n");
		}
		sb.append(getHTMLEnd());
		
		return sb.toString();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private String getHTMLBegin()
	{
		return 
			"<!-------------------------------------------------------------------\n" +
			"  -- This is machine-generated HTML.  There is no need to edit it.\n" +
			"<!-------------------------------------------------------------------\n" +
			"-->\n\n" +
			"<html>\n" +
			"<head><title>Duplicated Classes</title></head>\n" +
			"<body>\n" +
			"<h1 align=center>Duplicate Admin Classes</h1>" +
			"<h3>Maintained by <a href=\"mailto:byron.nevins@sun.com\">Byron Nevins</a></h3>" +
			"This HTML was generated: " + new Date() + "<HR>" +
			"<table border=\"2\" align=\"center\">\n" +
			"<tr>\n" +
			"<th>Class Name</th><th>Jar1</th><th>Jar2</th>\n";
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private String getHTMLEnd()
	{
		return
			"</table>\n" +
			"</body>\n" +
			"</html>\n\n";
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void filterClasses()
	{
		Set<Map.Entry<String,List<String>>> set = dupes.entrySet();
		
		Iterator<Map.Entry<String,List<String>>> it = set.iterator();
		
		while(it.hasNext())
		{
			Map.Entry<String,List<String>> entry = it.next();
			String classname = entry.getKey();
			boolean matches = false;
			
			for(String s : searchStrings)
			{
				if(classname.indexOf(s) >= 0)
				{
					matches = true;
					break;
				}
			}
			
			if(!matches)
				it.remove();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void dump()
	{
		Set<Map.Entry<String,List<String>>> set = dupes.entrySet();
		
		for(Map.Entry<String,List<String>> entry : set)
		{
			List<String> jars = entry.getValue();
			String line = "" + jars.size() + "," + entry.getKey();

			for(String jar : jars)
				line += "," + jar;

			System.out.println(line);
		}
		
		System.out.println("NUM: " + dupes.size());
	}
	///////////////////////////////////////////////////////////////////////////
	
	private void cleanupJarNames()
	{
		Set<Map.Entry<String,List<String>>> set = dupes.entrySet();
		
		for(Map.Entry<String,List<String>> entry : set)
		{
			List<String> jars = entry.getValue();
			List<String> newjars = new ArrayList<String>();
			
			for(String jarname : jars)
			{
				newjars.add(cleanFilename(jarname));
			}
			entry.setValue(newjars);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	private String cleanFilename(String s)
	{
		return s.replace('\\', '/').substring(prefixLength);
		/*
		s = s.replace('\\', '/');
		int index = s.lastIndexOf('/');
		
		if(index >= 0)
			s = s.substring(++index);
		
		return s;
		*/
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void load(String filename) throws IOException, ClassNotFoundException
	{
		classFinder = ClassFinder.unSerialize(filename);			
		dupes = classFinder.getDupes();
		//System.out.println("Number of dupes = " + dupes.size());
	}
	
	///////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{
		try
		{
			DupeFinder finder = new DupeFinder("Z:/ee/lib/");
			//DupeFinder finder = new DupeFinder("c:/temp/ee.ser", "Z:/ee/lib/");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////

	private ClassFinder classFinder;
	private SortedMap<String, List<String>> dupes;
	private int prefixLength;
	private static final String[] searchStrings = new String[]
	{
		".admin.",
		"com.sun.enterprise.management.",
		"com.sun.enterprise.config.",
		"com.sun.enterprise.jmx.kstat",
		".cli",
		"com.sun.enterprise.config.serverbeans.",
		"com.sun.enterprise.ee.nodeagent",
		"com.sun.enterprise.ee.tools.launcher",
		"com.sun.enterprise.ee.synchronization",
	};
	
	///////////////////////////////////////////////////////////////////////////

}
/*
<html>
<head><title>Dupes</title></head>
<body>

<table border="2" align="center">
<tr>
<th>Class Name</th><th>Jar1</th><th>Jar2</th>
<tr>
<td>com.sun.enterprise.ee.cli.commands.StartNodeAgentCommand</td><td>admin-cli-ee.jar </td><td>appserv-se.jar</td>
</tr>
<tr>
<td>zzzzzzzzzzzzzz.com.sun.enterprise.ee.cli.commands.StopNodeAgentCommand</td><td>admin-cli-ee.jar </td><td>appserv-se.jar</td>
</tr>
</table>

</body>

</html>
*/
