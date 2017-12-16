/*
 * AuthCreationManager.java
 *
 * Created on September 28, 2007, 12:28 AM
 */

package admin;

import com.elf.enterprise.auth.UsersManager;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.prefs.*;
import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This is designed as a un-secure area.  If there is no DB yet -- the user can't
 * login yet.
 * @author bnevins
 * @version
 */
public class AuthCreationManager extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        String globalMessage = null;
        clearAttributes(request);
        
        String jndi = ServletHelper.getJndi(request, response);
        UsersManager umgr = UsersManager.getInstance(jndi);
        // 2. verify the DB -- maybe
        if(request.getParameter("verify") != null)
        {
            try
            {
                if(!umgr.verifyDB())
                {
                    String message = "There was an error verifying the DB.";
                    request.setAttribute("message", message);
                    request.setAttribute("verifyButton", Boolean.TRUE);
                    request.setAttribute("createButton", Boolean.TRUE);
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    rd.forward(request, response);
                    return;
                }
                else
                {
                    globalMessage = "The DB was successfully verified.";
                }
            } 
            catch(Exception e)
            {
                    String message = "There was an error verifying the DB.<br>" + e;
                    request.setAttribute("message", message);
                    request.setAttribute("verifyButton", Boolean.TRUE);
                    request.setAttribute("createButton", Boolean.TRUE);
                    RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                    rd.forward(request, response);
                    return;
            }
        }
        
        if(request.getParameter("create") != null)
        {
            try
            {
                // TODO add administrator account at same time.
                umgr.createSchema();
                String message = "Successfully created the DB tables";
                request.setAttribute("message", message);
                request.setAttribute("actionServlet", "AuthCreationManager");
                request.setAttribute("roles", new String[] { "USERS", "ADMINISTRATORS" });
                request.setAttribute("title", "Add Administrator");
                RequestDispatcher rd = request.getRequestDispatcher("EditUser.jsp");
                rd.forward(request, response);
                return;
            }
            catch(Exception e)
            {
                String message = "There was an error creating the DB: <br>" + e;
                request.setAttribute("message", message);
                request.setAttribute("verifyButton", Boolean.TRUE);
                request.setAttribute("createButton", Boolean.TRUE);
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
                return;
            }
        }

        if(globalMessage != null)
            request.setAttribute("message", globalMessage);
        else
            request.removeAttribute("message");
        
        request.setAttribute("recreateButton", Boolean.TRUE);
        request.setAttribute("adminButton", Boolean.TRUE);
        request.setAttribute("verifyButton", Boolean.TRUE);
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        
        if(request.getParameter("useredit") != null)
        {
            new ServletHelper().addUser(request, response);
            
            // now we can login, since a new account was created.
            response.sendRedirect("/AuthAdmin/AuthManager");
            return;
        }
        if(request.getParameter("recreate") != null)
        {
            response.sendRedirect("/AuthAdmin/AuthManager?recreate=true");
            return;
        }

        if(request.getParameter("admin") != null)
        {
            response.sendRedirect("/AuthAdmin/AuthManager");
            return;
        }

        rd.forward(request, response);
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

    private void clearAttributes(HttpServletRequest request)
    {
        request.removeAttribute("create");
        request.removeAttribute("recreate");
        request.removeAttribute("admin");
        request.removeAttribute("verify");
        request.removeAttribute("message");
    }
}
