/*
 * Admin.java
 *
 * Created on October 28, 2007, 8:29 PM
 */

package com.elf.enterprise.weight.web;

import com.elf.enterprise.weight.*;
import com.elf.enterprise.weight.db.*;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import javax.naming.NamingException;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author bnevins
 * @version
 */
public class Admin extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        WeightDB db = new WeightDB();
        List<WeightItem> items;

        if(request.getParameter("exitadmin") != null)
        {
            response.sendRedirect("/WeightTool/Controller");
            return;
        }
        if(request.getParameter("logout") != null)
        {
            request.getSession().invalidate();
            response.sendRedirect("/WeightTool"); 
            return;
        }

        if(request.getParameter("cleardb") != null)
        {
            try
            {
                db.createSchema(true);
            }
            catch(Exception e)
            {
                request.setAttribute("message", "Error creating tables: " + e);
            }
        }
        if(request.getParameter("verify") != null)
        {
            if(db.verifySchema())
            {
                request.setAttribute("message", "DB is OK");
            }
            else
            {                
                try
                {
                    db.createSchema(false);
                } 
                catch(Exception ex)
                {
                    request.setAttribute("message", "Error Verifying the DB: " + ex);
                }
            }
        }

        if(request.getParameter("backup") != null)
        {
            if(db.verifySchema())
            {
                String fname = request.getParameter("backup_file");

                if(fname == null || fname.isEmpty())
                    fname = "c:/incoming/weight_bu.ser";

                File f = new File(fname);
                db.backup(f);
                request.setAttribute("message", "DB backed-up to " + f);
            }
            else
            {
                request.setAttribute("message", "Error verifying the DB");
            }
        }

        if(request.getParameter("restore") != null)
        {
            try
            {
                db.createSchema(true);
                String fname = request.getParameter("backup_file");

                if (fname == null || fname.isEmpty()) {
                    fname = "c:/incoming/weight_bu.csv";
                }

                File f = new File(fname);
                int num = db.restore(f);
                request.setAttribute("message", "Restored " + num + " records from " + f);
            }
            catch(Exception e)
            {
                request.setAttribute("message", "Error creating tables: " + e);
            }
        }

        try
        {
            items = db.getItems();
        } 
        catch(Exception ex)
        {
            request.setAttribute("message", ex.toString());
            items = new ArrayList<WeightItem>();
        }
        
        request.setAttribute("items", items);

        RequestDispatcher rd = request.getRequestDispatcher("/superuser/Admin.jsp");
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
}
