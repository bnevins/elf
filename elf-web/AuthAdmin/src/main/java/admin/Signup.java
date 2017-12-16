package admin;
/*
 * Signup.java
 *
 * Created on October 6, 2007, 2:17 PM
 */

import com.elf.enterprise.auth.User;
import com.elf.enterprise.auth.UsersManager;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author bnevins
 * @version
 */
public class Signup extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Signup</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet Signup at " + request.getContextPath () + "</h1>");

        try
        {
            int             keyFromUser = Integer.parseInt(request.getParameter("signupKey"));
            String          userid = request.getUserPrincipal().getName();
            UsersManager    umgr = UsersManager.getInstance(ServletHelper.getJndi(request, response));
            umgr.validateUser(userid, keyFromUser);
            //User[]          users = umgr.getUsers(true);
            //User            user = findUser(users, userid);
        }
        catch(Exception e)
        {
            out.println("<p><b>Error: " + e + "</b></p>");
            out.println("</body>");
            out.println("</html>");
            out.close();
            return;
        }
        out.println("<h2>Congrats</h2>");
        out.println("</body>");
        out.println("</html>");
        out.close();
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

    /*
    private User findUser(User[] users, String userid)
    {
        for(User u : users)
            if(u.getUserName().equals(userid))
                return u;
        
        return null;
    }
     **/
}
