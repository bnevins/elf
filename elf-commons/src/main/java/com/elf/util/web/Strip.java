/*
 * Strip.java
 *
 * Created on May 7, 2006, 1:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.web;

import java.util.*;
import org.xml.sax.*;
/**
 *
 * @author bnevins
 */
public class Strip
{
	public Strip(Attributes meta)
	{
		name = meta.getValue("name");
		path = meta.getValue("path");
		System.out.println("New STRIP: name: " + name + ", path: " + path);
	}

	///////////////////////////////////////////////////////////////////////////
	
	public void add(Pix pix)
	{
		pixes.add(pix);
	}

	///////////////////////////////////////////////////////////////////////////

	public List<Pix> getPix()
	{
		return pixes;
	}
	
	///////////////////////////////////////////////////////////////////////////

	public String getName()
	{
		return name;
	}
	
	///////////////////////////////////////////////////////////////////////////

	public String getPath()
	{
		return path;
	}

	///////////////////////////////////////////////////////////////////////////

	public String toString()
	{
		StringBuilder sb = new StringBuilder("   Strip ");
		sb.append("Name: [").append(name).append("], path: [").append(path).append("]\n");
		
		for(Pix pix : pixes)
			sb.append(pix);
		
		return sb.toString();
	}

	///////////////////////////////////////////////////////////////////////////
	
	private List<Pix>	pixes = new ArrayList<Pix>();
	private	String	name;
	private String	path;
	//private Project	project;
}
