/*
 * LongLivedCookie.java
 *
 * Created on November 10, 2007, 9:40 PM
 *
 */

package com.elf.enterprise.web;

import javax.servlet.http.*;

/**
 *
 * @author bnevins
 */
public class LongLivedCookie  extends Cookie
{
	public LongLivedCookie(String name, String value)
	{
		super(name, value);
		setMaxAge(SECONDS_PER_YEAR);
	}
	
	private final static int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;
}
