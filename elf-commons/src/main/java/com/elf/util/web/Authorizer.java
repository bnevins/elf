/*
 * Authorizer.java
 *
 * Created on June 23, 2006, 12:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.util.web;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author bnevins
 */
public class Authorizer
{
	public Authorizer(HttpServletRequest Request, HttpServletResponse Response, String RealPassword)
	{
		request = Request;
		response = Response;
		realPassword = RealPassword;
		mangledPassword = mangle(realPassword);
		protectedTarget = request.getParameter(TARGET);
		requestPassword = request.getParameter(PASSWORD);
		requestURI = request.getRequestURI();
	}

	///////////////////////////////////////////////////////////////////////////

	public String checkAuthorized()
	{
		String pw = CookieUtils.getValue(request, PASSWORD, null);
		
		if(pw != null && pw.equals(mangledPassword))
			return "true";
		
		// no valid Cookie.  Check request params
		if(requestPassword != null && mangle(requestPassword).equals(mangledPassword))
		{
			savePassword();
			return "true";
		}

		// No authorization at all -- have them give it...
		return loginPage();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private void savePassword()
	{
		Cookie cookie = new LongLivedCookie(PASSWORD, mangledPassword);
		response.addCookie(cookie);
		response.setContentType("text/html;charset=UTF-8");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private String loginPage()
	{
		return HTML_1 + requestURI + HTML_2 + protectedTarget + HTML_3;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static String mangle(String s)
	{
		return new Integer(s.hashCode() * 31).toString();
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private					HttpServletRequest	request;
	private					HttpServletResponse response;
	private					String				realPassword;
	private					String				mangledPassword;
	private					String				protectedTarget;
	private					String				requestPassword;
	private					String				requestURI;
	private final static	String				PASSWORD	= "password";
	private final static	String				TARGET		= "target";
	private final static	String				HTML_1 =
	"<html><head><title>Authorization Page</title></head>" + 
    "<body BGCOLOR=\"#FDF5E6\"><center>" +
    "<h1>Please Login</h1>" +
    "<FORM ACTION=\"";

	private final static	String				HTML_2 =
    "\" METHOD=POST>\n" +
	"<FIELDSET><LEGEND></LEGEND>\n" +
    "Password:\n" +
    "<INPUT TYPE=\"PASSWORD\" NAME=\"password\"><BR><BR>\n" +
    "<INPUT TYPE=\"SUBMIT\" VALUE=\"Submit\">\n" +
	"<INPUT TYPE=\"HIDDEN\" NAME=\"target\" VALUE=\"";
	
	private final static	String				HTML_3 =
	"\"\n" +
	"</FIELDSET></FORM></center></body></html>\n";
}
