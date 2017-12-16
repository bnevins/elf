/*
 * AuthManager.java
 *
 * Created on September 25, 2007, 11:58 PM
 */
package admin;

import com.elf.enterprise.auth.User;
import com.elf.enterprise.auth.UsersManager;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bnevins
 * @version
 */
public class AuthManager extends HttpServlet {
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (ServletHelper.handleLogout(request, response)) {
            return;
        }

        String jndi = ServletHelper.getJndi(request, response);
        UsersManager umgr = UsersManager.getInstance(jndi);

        if (handleParams(request, response, umgr)) {
            return;
        }

        User[] users = umgr.getUsers(true);
        request.setAttribute("users", users);
        RequestDispatcher rd = request.getRequestDispatcher("ManageSecurity.jsp");
        rd.forward(request, response);
        //return;
    }

    private boolean handleParams(HttpServletRequest request, HttpServletResponse response, UsersManager umgr) throws ServletException, IOException {
        // here is the real type of the map:  org.apache.catalina.util.ParameterMap

        Map params = request.getParameterMap();

        if (params == null) {
            return false;
        }

        Set<Map.Entry> entries = params.entrySet();
        Set<String> keys = (Set<String>) params.keySet();

        // TODO error handling...
        if (request.getParameter("verify") != null) {
            //boolean ok = umgr.verifyDB();
            boolean ok = true;
            request.setAttribute("verifyResults", new Boolean(ok));
            request.setAttribute("message", "???????????ZZZZZZZZZZZZZ????????????");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
            return true;
        }


        if (request.getParameter("recreate") != null) {
            // TODO Error Proc.
            try {
                umgr.createSchema();
                request.removeAttribute("actionServlet");
                request.setAttribute("roles", new String[]{"USERS", "ADMINISTRATORS"});
                request.setAttribute("title", "Add Administrator");
                RequestDispatcher rd = request.getRequestDispatcher("EditUser.jsp");
                rd.forward(request, response);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        if (handleDelete(keys, umgr)) {
            return false;
        }

        return handleEdit(keys, umgr, request, response);
    }

    private boolean handleDelete(Set<String> keys, UsersManager umgr) {
        for (String key : keys) {
            if (key.startsWith("delete_")) {
                String un = key.substring(7);
                umgr.deleteUser(un);
                return true;
            }
        }
        return false;
    }

    private boolean handleEdit(Set<String> keys, UsersManager umgr,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("roles", umgr.getAllRoles(true));
        request.setAttribute("title", "Edit User or Add New User");
        if (request.getParameter("newUser") != null) {
            request.setAttribute("title", "Add New User");
            RequestDispatcher rd = request.getRequestDispatcher("EditUser.jsp");
            rd.forward(request, response);
            return true;
        }

        for (String key : keys) {
            if (key.startsWith("edit_")) {
                String un = key.substring(5);
                User[] users = umgr.getUsers(true);

                for (User user : users) {
                    if (user.getUserName().equals(un)) {
                        request.setAttribute("title", "Edit Profile for " + un);
                        request.setAttribute("user", user);
                        RequestDispatcher rd = request.getRequestDispatcher("EditUser.jsp");
                        rd.forward(request, response);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>

    private boolean ok(String s) {
        return s != null && s.length() > 0;
    }
}
