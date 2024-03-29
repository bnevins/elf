/*
 * File:           PictureHandlerImpl.java
 * Date:           May 7, 2006  1:15 AM
 *
 * @author  bnevins
 * @version generated by NetBeans XML module
 */
package com.elf.util.web;

import org.xml.sax.*;
public class PictureHandlerImpl implements PictureHandler
{
	public PictureHandlerImpl(Project aProject)
	{
		project = aProject;
	}
	public static final boolean DEBUG = false;

	public void handle_pix(final Attributes meta) throws SAXException
	{
		if (DEBUG) System.err.println("handle_pix()");
		strip.add(new Pix(meta));
	}

	public void start_strip(final Attributes meta) throws SAXException
	{
		if (DEBUG) System.err.println("start_strip()");
		strip = new Strip(meta);
		project.add(strip);
	}

	public void end_strip() throws SAXException
	{
		if (DEBUG) System.err.println("end_strip()");
		strip = null;
	}

	public void start_project(final Attributes meta) throws SAXException
	{
		if (DEBUG) System.err.println("start_project: " + meta);
		project.setAttributes(meta);
	}

	public void end_project() throws SAXException
	{
		if (DEBUG) System.err.println("end_project()");
		project = null;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private Project project;
	private Strip	strip;
}
