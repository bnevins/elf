/*
 * Pix.java
 *
 * Created on May 7, 2006, 1:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.web;
import org.xml.sax.*;

/**
 *
 * @author bnevins
 */
public class Pix
{
	public Pix(Attributes meta)
	{
		name = meta.getValue("name");
		path = meta.getValue("path");
		caption = meta.getValue("caption");
		System.out.println("New PIX: name: " + name + ", path: " + path + ", caption: " + caption);
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

	public String getCaption()
	{
		return caption;
	}

	///////////////////////////////////////////////////////////////////////////

	public String toString()
	{
		StringBuilder sb = new StringBuilder("      Pix ");
		sb.append("Name: [").append(name).append("], path: [").append(path);
		sb.append("], caption: [").append(caption).append("]\n");
		
		return sb.toString();
	}

	///////////////////////////////////////////////////////////////////////////
	
	private String	path;
	private String	name;
	private String	caption;
	//private Strip	strip;
}
