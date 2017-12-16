/*
 * Project.java
 *
 * Created on May 7, 2006, 1:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.web;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.*;


/**
 *
 * @author bnevins
 */
public class Project
{
	public Project(String filename) throws MalformedURLException, SAXException, ParserConfigurationException, IOException
	{
		xmlURL = new File(filename).toURI().toURL();
		read();
	}
	
	///////////////////////////////////////////////////////////////////////////

	public Project(URL theURL) throws MalformedURLException, SAXException, ParserConfigurationException, IOException
	{
		xmlURL = theURL;
		read();
	}
	
	///////////////////////////////////////////////////////////////////////////

	public void read() throws SAXException, ParserConfigurationException, IOException
	{
		PictureHandler ph = new PictureHandlerImpl(this);
		PictureParser pp = new PictureParser(ph, null);
		pp.parse(xmlURL);
	}

	///////////////////////////////////////////////////////////////////////////

	public String toString()
	{
		StringBuilder sb = new StringBuilder("Project ");
		sb.append("Name: [").append(name).append("], path: [").append(path).append("]\n");
		
		for(Strip strip : strips)
			sb.append(strip);
		
		return sb.toString();
	}

	///////////////////////////////////////////////////////////////////////////

	void setAttributes(Attributes meta)
	{
		name = meta.getValue("name");
		path = meta.getValue("path");
	}

	///////////////////////////////////////////////////////////////////////////

	void add(Strip strip)
	{
		strips.add(strip);
	}
	///////////////////////////////////////////////////////////////////////////
	
	private			List<Strip>	strips = new ArrayList<Strip>();
	private			String		path;
	private			String		name;
	private			URL			xmlURL;
}
