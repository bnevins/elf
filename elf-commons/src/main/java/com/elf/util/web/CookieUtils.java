/*
 * CookieUtils.java
 *
 * Created on May 25, 2006, 1:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.web;

import javax.servlet.http.*;

/**
 *
 * @author bnevins
 */
public class CookieUtils
{
 public static Cookie get(HttpServletRequest request, String name)
	{
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null)
			for(Cookie cookie : cookies)
				if(name.equals(cookie.getName()))
					return cookie;
			
		return null;
	}

	public static String getValue(HttpServletRequest request, String name, String defaultValue)
	{
		Cookie cookie = get(request, name);
		
		if(cookie != null)
			return cookie.getValue();
		
		return defaultValue;
	}
 }
