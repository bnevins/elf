/*
 * Classpath.java
 *
 * Created on September 28, 2001, 11:34 PM
 */

package com.elf.util;
//import com.elf.util.diagnostics.Reporter;
import java.util.*;

/**
 *
 * @author  bnevins
 * @version 
 */

public class Classpath
{
	public static List<String> getClasspathAsList()
	{
		String			cp		= getClasspathAsString();
		String			ps		= System.getProperty("path.separator");
		StringTokenizer st		= new StringTokenizer(cp, ps);
		List<String>	list	= new ArrayList<String>();
		
		while (st.hasMoreTokens()) 
		{
			list.add(st.nextToken());
		}
		return list;
	}
	
	public static List<String> getClasspathAsBatchCommands()
	{
		List<String>	from	= getClasspathAsList();
		List<String>	to		= new ArrayList<String>();
		
		boolean first = true;
		
		for(String s : from)
		{
			if(first)
			{
				to.add("set CLASSPATH=" + s);
				first = false;
			}
			else
				to.add("set CLASSPATH=%CLASSPATH%;" + s);
		}
		
		return to;
	}

	public static String getClasspathAsString()
	{
		return System.getProperty("java.class.path");
	}

	public static List<String> getClasspathAsSortedList()
	{
		List<String> list = getClasspathAsList();
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		return list;
	}
		
	
	public static void main(String[] args)
	{
		pr("******  CLASSPATH as String *******");
		pr(getClasspathAsString());
		pr("******  CLASSPATH as List *******");
		pr(getClasspathAsList());
		pr("******  CLASSPATH as Sorted List *******");
		pr(getClasspathAsSortedList());
		pr("******  CLASSPATH as Batch Commands *******");
		pr(getClasspathAsBatchCommands());
	}
	
	private static void pr(String s)
	{
		System.out.println(s);
	}
	
	private static void pr(List<String> c)
	{
		//for(Iterator iter = c.iterator(); iter.hasNext(); )
		for(String s : c)
		{
			pr(s);
			//pr((String)iter.next());
		}
	}
	
}
