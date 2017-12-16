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
public class PageMaker extends HttpServlet {
    private String subtitle;
    private static final String SMALL_IMAGE_PREFIX = "tn_small_";
    private static final int SMALL_IMAGE_PREFIX_LENGTH = SMALL_IMAGE_PREFIX.length();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // subtitle fills with junk somehow!
        subtitle = "";
        // yank info from request...
        boolean debug = Boolean.parseBoolean(request.getParameter("debug"));
        String title = request.getParameter("title");
        String rootName = request.getParameter("root");

        // setup response
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(ServletUtils.DOCTYPE); // ghastly boilerplate
        out.println("<HTML>");

        File rootFile = new File(DOCROOT, rootName);

        if (debug) {
            printDebug(out, "Root Directory = " + rootFile);
        }

        if (!rootFile.isDirectory()) {
            out.println("<BODY><H1>ERROR: ");
            out.println(rootFile.toString() + " is not a directory.");
            out.println("</H1></BODY>");
            out.println("</HTML>");
            return;
        }

        // the try/catch/finally should print errors OK
        try {
            if (!ok(title)) {
                title = getTitle(rootFile);
            }

            out.println("<HEAD><TITLE>" + title + "</TITLE></HEAD>");
            out.println("<BODY BGCOLOR=\"#FDF5E6\">");
            out.println("<h1 align=\"center\">" + title + "</h1>");

            if (StringUtils.ok(subtitle)) {
                out.println("<h3 align=\"left\">" + subtitle + "</h3>");
            }

            File[] areas = rootFile.listFiles(new FileFilter() {

                public boolean accept(File f) {
                    return f.isDirectory();
                }
            });

            if (areas == null || areas.length <= 0) {
                throw new PageMakerException("No subdirectories in " + rootFile);
            }

            if (debug) {
                printDebug(out, "File Areas: " + Arrays.toString(areas));
            }

            for (File f : areas) {
                doArea(f, rootName, out);
            }

        }
        catch (PageMakerException e) {
            out.println("ERROR: " + e.getMessage());
        }
        finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }

    private void doArea(File area, String rootName, PrintWriter out) {
        String imageNameBegin = "/images/" + rootName + "/" + area.getName() + "/";
        File[] files = area.listFiles(new FileFilter() {

            public boolean accept(File f) {
                return f.getName().startsWith("tn_small_");
            }
        });

        out.println(getFormattedTitle(area));
        out.println("<TABLE CELLPADDING=\"2\">");

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            File parent = f.getParentFile();
            File picFile = new File(parent, "tn_med_" + f.getName().substring(9));

            String thumb = imageNameBegin + f.getName();
            String pic = imageNameBegin + picFile.getName();

            //print("<A HREF=\"" + pic + "\">");
            if ((i % 5) == 0) {
                if (i != 0) {
                    out.println("</TR>");
                }

                out.println("<TR>");
            }
            out.println("<TD WIDTH=\"200\" HEIGHT=\"150\"><P ALIGN=Center>");

            // TEMP TEMP TEMP TEMP
            // TEMP TEMP TEMP TEMP
            // TEMP TEMP TEMP TEMP
            // TEMP TEMP TEMP TEMP
           out.println("<A HREF=\"SubPageMaker?home=fuckoff&next=nexthere&prev=prevhere&"
                   + "current=" + pic + "\">");
           
            // TEMP TEMP TEMP TEMP
            // TEMP TEMP TEMP TEMP
            // TEMP TEMP TEMP TEMP
            // TEMP TEMP TEMP TEMP

            //out.println("<A HREF=\"" + pic + "\">");
           
            
            out.println("<IMG SRC=\"" + thumb + "\">");
            //print("<IMG WIDTH=\"200\" HEIGHT=\"150\" SRC=\"" + thumb + "\">");
            out.println("</A></TD>");
        }

        out.println("</TR>");
        out.println("</TABLE>");
        out.println("<HR>");
    }

    /**
     * Be sure and return full HTML -- not just the name.
     *
     * @param area the directory
     * @return the title
     */
    private String getFormattedTitle(File area) {

        StringBuilder sb = new StringBuilder();
        sb.append("<h2 ALIGN=\"CENTER\">");
        sb.append("<FONT COLOR=\"RED\">");
        sb.append(getTitle(area));
        sb.append("</FONT></H2>\n");
        sb.append(getFormattedSubTitle(area));

        return sb.toString();
    }

    private String getFormattedSubTitle(File area) {
        if (StringUtils.ok(subtitle)) {
            return "<H3>" + subtitle + "</H3>\n";
        }
        else {
            return "";
        }
    }

    /**
     * See if there is a special file with the title. Otherwise just use the
     * directory name. Be sure and return plain text. The formatting is done
     * elsewhere.
     *
     * @param area the directory
     * @return the title
     */
    private String getTitle(File area) {
        String title = readTitle(area);

        if (!ok(title)) {
            title = area.getName();
        }

        return title;
    }

    private String readTitle(File dir) {
        File f = new File(dir, TITLE_FILE);

        if (!f.isFile()) {
            return null;
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(f));
            // TODO only first line is used...
            String title = reader.readLine();
            // use the second line.  Very crude...  readLine returns null...
            subtitle = reader.readLine();
            return title;
        }
        catch (IOException ex) {
            // impossible!!!
            return null;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ex) {
                    //ignore
                }
            }
        }
    }

    private static void printDebug(PrintWriter out, String s) {
        out.println("<h3>DEBUG: " + s + "</h3>");
    }

    private static boolean ok(String s) {
        return s != null && s.length() > 0;
    }
    private static final String imageRoot = "images";
    private static final int pixRowLength = 5;
    private static final int pixWidth = 200;
    private static final int pixHeight = 150;
    private static final String TITLE_FILE = "title.txt";
    private static final String INSTANCE_ROOT_KEY = "com.sun.aas.instanceRoot";
    private static final File DOCROOT = SmartFile.sanitize(
            new File(new File(System.getProperty(INSTANCE_ROOT_KEY)), "docroot/images"));

    private static class PageMakerException extends Exception {

        PageMakerException(String s) {
            super("ERROR: " + s);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
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
