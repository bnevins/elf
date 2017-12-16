/*
 * CookieManager.java
 *
 * Created on November 4, 2007, 7:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.enterprise.web;

import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author bnevins
 */
public class CookieManager
{
    public CookieManager()
    {
    }
    
    public String getUserName(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        
        if(cookies != null)
            for(Cookie cookie : cookies)
                if(cookie.getName().equals(USERNAME))
                    return cookie.getValue();
        return "";
    }

    public String getPassword(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        if(cookies != null)
            for(Cookie cookie : cookies)
                if(cookie.getName().equals(PASSWORD))
                    return cookie.getValue();
        return "";
    }
    
    public void writeCookie(HttpServletResponse response, String user, String pw)
    {
		response.addCookie(new LongLivedCookie(USERNAME, user));
		response.addCookie(new LongLivedCookie(PASSWORD, pw));
    }        
    
    private final static String USERNAME = "USERNAME";
    private final static String PASSWORD = "PASSWORD";
}
