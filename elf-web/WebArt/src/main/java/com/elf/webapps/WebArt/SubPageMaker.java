package com.elf.webapps.WebArt;

import com.elf.io.SmartFile;
import com.elf.util.StringUtils;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import com.elf.util.web.ServletUtils;

/**
 *
 * @author bnevins
 */

public class SubPageMaker extends HttpServlet {
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // yank info from request...
        boolean debug = Boolean.parseBoolean(request.getParameter("debug"));

        
        debug = true;
        
        
        String prev = request.getParameter("prev");
        String next = request.getParameter("next");
        String home = request.getParameter("home");
        String current = request.getParameter("current");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(ServletUtils.DOCTYPE); // ghastly boilerplate
        out.println("<HTML>");
/*
        if (!rootFile.isDirectory()) {
            out.println("<BODY><H1>ERROR: ");
            out.println(rootFile.toString() + " is not a directory.");
            out.println("</H1></BODY>");
            out.println("</HTML>");
            return;
        }
*/
        // the try/catch/finally should print errors OK
        try {
            out.println("<HEAD><TITLE>" + current + "</TITLE></HEAD>");
            out.println("<BODY BGCOLOR=\"#FDF5E6\">");
            out.println("<h1 align=\"center\">" + current + "</h1>");
        if (debug) {
            printDebug(out, "prev = " + prev);
            printDebug(out, "next = " + next);
            printDebug(out, "current = " + current);
            printDebug(out, "home = " + home);
            printDebug(out, "debug = " + debug);
        }
            
            out.println("<IMG SRC=\"" + current + "\">");
        }
        catch (Exception e) {
            out.println("ERROR: " + e.getMessage());
        }
        finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }

    private void printNavigationButtons(PrintWriter out) {
        out.println("<center>\n");
        out.println("<form action=\"SubPageMaker\" method=\"GET\">"); 
    } 
    private static void printDebug(PrintWriter out, String s) {
        out.println("<h3>DEBUG: " + s + "</h3>");
    }

    private static boolean ok(String s) {
        return s != null && s.length() > 0;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
/*
 *
private static void printDirs(PrintWriter out, HttpServletRequest request) {
printDebug(out, "Servlet pix at " + request.getContextPath());
printDebug(out, "REAL PATH = " + getServletConfig().getServletContext().getRealPath("").replace('\\', '/'));
printDebug(out, "PATH for \"\" --> " + new File("").getAbsolutePath().replace('\\', '/'));
}
 */
