/*
 * LoginHelper.java
 *
 * Created on November 10, 2007, 9:05 PM
 *
 */

package com.elf.enterprise.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author bnevins
 */
public class LoginHelper
{
    public LoginHelper()
    {
        
    }
    
    public void processRequest(HttpServletRequest request, HttpServletResponse response, String caller)
    throws ServletException, IOException
    {
        CookieManager mgr = new CookieManager();
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();

        if(request.getParameter("j_username") != null)
        {
            // called the second time...
            user = request.getParameter("j_username");
            password = request.getParameter("j_password");
            mgr.writeCookie(response, user, password);
            callIntoGlassFish = true;
        }
        else
        {
            user = mgr.getUserName(request);
            password = mgr.getPassword(request);
            
            if(ok(user) && ok(password))
                callIntoGlassFish = true;
            else
                callIntoGlassFish = false;
        }
        
        if(callIntoGlassFish)
            action = "j_security_check";
        else
            action = caller;
        
        printHTML1();
        printHTML2();
        out.close();
    }

    private void printHTML1()
    {
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        out.println("<title>Login</title>");
        out.println("</head>");
        out.println("<body>");
    }
    
    private void printHTML2()
    {
        out.println("<h1 align='center'>Please Login</h1>");
        out.println("<center>");
        out.println("<form method='POST' action='" + action + "'>");
        out.println("<table>");
        out.println("<tr><td>User name:</td><td><input type='text' name='j_username' value='" + user + "'/></td></tr>");
        out.println("<tr><td>Password:</td><td><input type='password' name='j_password' value='" + password + "'/></td></tr>");
        out.println("<tr><td><input type='submit' value='Login' /></td></td></tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("<h1 align='center'>Or create a new account</h1>");
        out.println("<form action='/AuthAdmin/Registration' method='POST'>");
        out.println("<input type='submit' value='Register' />");
        out.println("</form>");
        out.println("</center>");
        out.println("</body>");
        out.println("</html>");
    }

    private boolean ok(String s)
    {
        return s != null && s.length() > 0;
    }
    private PrintWriter out;
    private String user = "";
    private String password = "";
    private String action;
    private boolean callIntoGlassFish;
    
}
