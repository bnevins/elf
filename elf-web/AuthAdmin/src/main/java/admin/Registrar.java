/*
 * Registrar.java
 *
 * Created on October 6, 2007, 8:48 PM
 */

package admin;

import com.elf.enterprise.auth.*;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author bnevins
 * @version
 */
public class Registrar extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        String jndi = ServletHelper.getJndi(request, response);
        String err = null;
        response.setContentType("text/html;charset=UTF-8");
        String un = request.getParameter("username");
        String pw1 = request.getParameter("pw1");
        String pw2 = request.getParameter("pw2");
        String fn = request.getParameter("firstname");
        String ln = request.getParameter("lastname");
        String em = request.getParameter("email");
        UsersManager umgr = UsersManager.getInstance(jndi);

        if(!ok(un))
            err = "Missing Username";
        if(!ok(pw1) || !ok(pw2))
        {
            if(err != null)
                err += " and Password";
            else
                err = "Missing Password";
        }
        if(!ok(em))
        {
            if(err != null)
                err += " and Email Address";
            else
                err = "Missing Email Address";
        }
        
        if(err == null && !pw1.equals(pw2))
        {
            err = "The passwords didn't match.";
        }
        if(err != null)
        {
            request.setAttribute("err", err);
            RequestDispatcher rd = request.getRequestDispatcher("public/register.jsp");
            rd.forward(request, response);
            return;
        }

        if(userExists(umgr, un))
        {
            err = "That username (" + un + ") is already taken.";
            request.setAttribute("err", err);
            request.setAttribute("clearUserName", true);
            RequestDispatcher rd = request.getRequestDispatcher("public/register.jsp");
            rd.forward(request, response);
            return;
        }
        
        try
        {
            umgr.addUser(un, pw1, fn, ln, em, new String[] {"USERS"} );
            RequestDispatcher rd = request.getRequestDispatcher("public/registerOK.jsp");
            rd.forward(request, response);
            return;
        } 
        catch (Exception ex)
        {
            err = "ERROR ADDING USER: \n" + ex.toString();
            request.setAttribute("err", err);
            RequestDispatcher rd = request.getRequestDispatcher("public/register.jsp");
            rd.forward(request, response);
            return;
        } 
    }
    private boolean ok(String s)
    {
        return s != null && s.length() > 0;
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Short description";
    }
    // </editor-fold>

    private boolean userExists(UsersManager umgr, String un)
    {
        User[] users = umgr.getUsers(true);
        
        for(User user : users)
            if(user.getUserName().equals(un))
                return true;
        
        return false;
    }

}
