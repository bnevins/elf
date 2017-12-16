package com.elf.util.web;
import javax.servlet.http.*;
/*
 * LongLivedCookie.java
 *
 * Created on May 25, 2006, 1:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author bnevins
 */
public class LongLivedCookie extends Cookie
{
	public LongLivedCookie(String name, String value)
	{
		super(name, value);
		setMaxAge(SECONDS_PER_YEAR);
	}
	
	private final static int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;
}
