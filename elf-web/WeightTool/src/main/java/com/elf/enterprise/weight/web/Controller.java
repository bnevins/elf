/*
 * Controller.java
 *
 * Created on October 28, 2007, 9:07 PM
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
public class Controller extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        try
        {
            String user = request.getUserPrincipal().getName();
            WeightDB db = new WeightDB();
            List<WeightItem> items = null;
            if(request.getParameter("logout") != null)
            {
                request.getSession().invalidate();
                response.sendRedirect("/WeightTool"); 
                return;
            }

            if(request.getParameter("admin") != null || request.getParameter("administer") != null)
            {
                response.sendRedirect("/WeightTool/superuser/Admin"); 
                return;
            }

            else if(request.getParameter("deanEdell") != null )
            {
                response.sendRedirect("/DeanEdell");
                return;
            }

            else if(request.getParameter("today") != null && 
                    request.getParameter("new") != null)
            {
                double wt = Double.parseDouble(request.getParameter("today"));
                db.addItem(wt, user);
            }

            /*
            if(request.getParameter("showAll") != null)
                items = db.getItems();
            else
             */
                items = db.getItems(user);
            
            request.setAttribute("title", getTitle(request));
            //request.setAttribute("items", items);
            request.setAttribute("itemTable", prepareTable(items));
        } 
        catch(SQLException ex)
        {
            request.setAttribute("message", ex.toString());
        } 
        catch (NamingException ex)
        {
            request.setAttribute("message", ex.toString());
        }
        catch (NumberFormatException ex)
        {
            request.setAttribute("message", ex.toString());
        }

        RequestDispatcher rd = request.getRequestDispatcher("ItemList.jsp");
        rd.forward(request, response);
    }

    private void appendLine(StringBuilder sb, String string) {
        sb.append(string).append(EOL);
    }

    private String prepareTable(List<WeightItem> items)
    {
        // This is too messy to do in JSP!
        StringBuilder sb = new StringBuilder();
        int numCols = 4;
        int numItems = items.size();
        int numRows = numItems / numCols;

        if( (numItems % numCols) != 0)
            ++numRows;

        appendLine(sb, "<table cellpadding='4' border='1'>");
        appendLine(sb, "<tr>");

        for(int i = 0; i < numCols; i++)
        {
            appendLine(sb, "<th>Date</th>");
            appendLine(sb, "<th>Weight</th>");
        }

        appendLine(sb, "</tr>");
        
        for(int row = 0; row < numRows; row++)
        {
            sb.append("<tr>");
            for(int col = 0; col < numCols; col++)
            {
                int which = (col * numRows) + row;
                if(which < items.size())
                {
                    WeightItem item = items.get(which);
                    
                    if(debug)
                    {
                        sb.append("<td>").append("" + which).append("</td>");
                        sb.append("<td>").append("" + col + "," + row).append("</td>");
                    }
                    else
                    {
                        double wt = item.getWeight();
                        sb.append("<td>").append(item.getDateString()).append("</td>");
                        sb.append("<td bgcolor='");
                        sb.append(Globals.getColor(wt, 170.0, 5.0));
                        sb.append("'>");
                        sb.append("<b>").append("<span style='color:");
                        sb.append("black");
                        sb.append("'>").append(wt).append("</span></b></td>");
                    }
                }
            }
            appendLine(sb, "</tr>");
        }
        appendLine(sb, "</table>");
        if(debug)
        {
            appendLine(sb, "</table>");
            appendLine(sb, "<h2>nrow=" +
                    numRows +
                    ", cols=" +
                    numCols +
                    ", total=" +
                    items.size() +
                    "</h2>");
        }
        return sb.toString();
    }

    private String getTitle(HttpServletRequest request)
    {
        String userid = request.getUserPrincipal().getName();
        
        if(!ok(userid))
            return "Weight List";
        
        return "Private Weight List for " + userid;
    }
    
    private boolean ok(String s)
    {
        return s != null && s.length() > 0;
    }

    private final static String EOL = System.getProperty("line.separator");
    private final static boolean debug = false;
    
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
