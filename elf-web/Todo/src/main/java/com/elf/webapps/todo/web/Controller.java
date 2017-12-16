 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.webapps.todo.web;

import com.elf.enterprise.auth.User;
import com.elf.enterprise.db.UsersDB;
import com.elf.webapps.todo.db.ToDoDBManager;
import com.elf.webapps.todo.model.Globals;
import com.elf.webapps.todo.model.ToDoItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Byron Nevins February 26, 2012 * @WebServlet(name = "Todo",
 * urlPatterns = {"/Todo"}, initParams = { @WebInitParam(name = "param1", value
 * = "value1"), @WebInitParam(name = "param2", value = "value2") })
 */
public class Controller extends HttpServlet {
    private static final String FALSE = Boolean.FALSE.toString();
    private static final String TRUE = Boolean.TRUE.toString();
    private static final Boolean DEFAULT_SHOW_CLOSED = Boolean.TRUE;
    private static final Boolean DEFAULT_SHOW_ALL_USERS = Boolean.FALSE;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            ToDoDBManager mgr = new ToDoDBManager();

            // logout
            if (handleParametersLeaveImmediately(request, response, session, mgr)) {
                return;
            }

            // fill request with data...
            fillRequest(request, session, mgr);

            // add, admin, close etc.
            if (!handleMainParameters(request, response, session, mgr)) {

                // todo todo todo
                // todo todo todo
                // todo todo todo
                // todo todo todo
                // todo todo todo
                // need to figure out the flow a bit better!
                fillRequest(request, session, mgr);
                // trouble: some of the code called in handleMain... above may have
                // changed DB -- but not the local "items"


                RequestDispatcher rd = request.getRequestDispatcher("jsp/ItemList.jsp");
                rd.forward(request, response);
            }
        }
        catch (NamingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillRequest(HttpServletRequest request, HttpSession session, ToDoDBManager mgr)
            throws NamingException, SQLException {

        String su = FALSE;
        getUsers(request, session); // side-effect is stuffing it into attributes for jsp files

        if (request.isUserInRole("AdminRole")) {
            su = TRUE;
        }

        boolean b = request.isUserInRole("TodoUserRole");

        // if it isn't in the ctx, then add it...
        if (session.getAttribute("showClosed") == null) {
            session.setAttribute("showClosed", DEFAULT_SHOW_CLOSED);
        }

        if (session.getAttribute("showAllUsers") == null) {
            session.setAttribute("showAllUsers", DEFAULT_SHOW_ALL_USERS);
        }

        List<ToDoItem> items = null;
        refreshItems(request, session, mgr);
        request.setAttribute("title", makeTitle(request, session));
        request.setAttribute("private", FALSE);
        request.setAttribute("su", su);
    }

    private void refreshItems(HttpServletRequest request, HttpSession session, ToDoDBManager mgr) throws NamingException, SQLException {
        if (isShowAllUsers(session)) {
            request.setAttribute("items", mgr.getItems());
        }
        else {
            request.setAttribute("items", mgr.getItems(request));
        }
    }

    private boolean handleParametersLeaveImmediately(HttpServletRequest request,
            HttpServletResponse response, HttpSession session, ToDoDBManager mgr)
            throws IOException, ServletException, NamingException, SQLException {

        if (request.getParameter("logout") != null) {
            request.getSession().invalidate();
            response.sendRedirect("/Todo/Controller");
            return true;
        }
        return false;
    }

    private boolean handleMainParameters(HttpServletRequest request,
            HttpServletResponse response, HttpSession session, ToDoDBManager mgr)
            throws IOException, ServletException, NamingException, SQLException {
        Map<String, String[]> params = request.getParameterMap();

        if (params.isEmpty()) {
            return false;
        }

        String[] ss = params.get("toggleShowClosed");

        if (ss != null) {
            if (ss[0].toLowerCase().trim().startsWith("show")) {
                session.setAttribute("showClosed", Boolean.TRUE);
            }
            else {
                session.setAttribute("showClosed", Boolean.FALSE);
            }
            return false;
        }

        ss = params.get("toggleShowAllUsers");

        if (ss != null) {
            if (ss[0].toLowerCase().trim().startsWith("show all")) {
                session.setAttribute("showAllUsers", Boolean.TRUE);
            }
            else {
                session.setAttribute("showAllUsers", Boolean.FALSE);
            }
            return false;
        }

        if (params.get("new") != null) {
            refreshItems();
            RequestDispatcher rd = request.getRequestDispatcher("jsp/NewItem.jsp");
            rd.forward(request, response);
            return true;
        }

        // TODO get rid of this ?!?
        if (params.get("admin") != null) {
            // get ALL the items -- ignore the "private" attribute...
            refreshItems();
            RequestDispatcher rd = request.getRequestDispatcher("admin/admin.jsp");
            rd.forward(request, response);
            return true;
        }

        handlePerItemParameters(request, params, mgr);
        handleNewItem(request, params, mgr);
        return false;
    }

    private void handlePerItemParameters(HttpServletRequest request, Map<String, String[]> params, ToDoDBManager mgr) throws NamingException, SQLException {
        // key = showhide25, value[0] = "Reopen" or "Close"  i.e. value is what user sees on button
        // key = delete25 , value == WHO CARES?
        int item = -1;

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String buttonName = entry.getKey();

            if (buttonName.startsWith("showhide")) {
                item = Integer.parseInt(buttonName.substring(8));
                String buttonText = entry.getValue()[0];

                if (buttonText.equals("Reopen")) {
                    mgr.reopenItem(item);
                }
                else { // "Close"
                    mgr.closeItem(item);
                }
                refreshItems();
                return;
            }
            else if (buttonName.startsWith("delete")) {
                item = Integer.parseInt(buttonName.substring(6));
                mgr.delete(item);
                refreshItems();
                return;
            }
        }
    }

    private void handleNewItem(HttpServletRequest request, Map<String, String[]> params,
            ToDoDBManager mgr) throws NamingException, SQLException {
        // description is a parameter iff a new item was just "gui-created" by the caller...
        String[] ss = params.get("description");

        // need a description!
        if (ss == null || ss.length <= 0) {
            return;
        }

        String desc = ss[0];
        String assigner = request.getUserPrincipal().getName();
        String assignee = assigner;
        //may or may not be set... 
        ss = params.get("assignee");

        if (ss != null && ss.length == 1) {
            assignee = ss[0];
        }

        ToDoItem item = new ToDoItem(desc, assigner, assignee);
        mgr.addItem(item);
    }

    private boolean isShowAllUsers(HttpSession session) {
        Object o = session.getAttribute("showAllUsers");

        return o != null && (Boolean) o;
    }

    private String makeTitle(HttpServletRequest request, HttpSession session) throws NamingException, SQLException {
        if (isShowAllUsers(session)) {
            return "All Todo List Items";
        }
        User[] users = getUsers(request, session);
        String me = request.getUserPrincipal().getName();
        
        if(!ok(me))
            return "ERROR -- NOT LOGGED IN!!";
        
        //tod handle names ending in 's'
        for (User user : users) {
            if(me.equals(user.getUserName()))
                return user.getFirstName() + "'s Todo Items";
        }
        return me + "'s Todo Items";
    }

    private String getFirstName(HttpServletRequest request) {
        return request.getUserPrincipal().getName() + "'s ToDo List";
    }

    // TODO ??????
    private void refreshItems() {
    }
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
        return "Main Controller Servlet for ToDo";
    }// </editor-fold>

    private User[] getUsers(HttpServletRequest request, HttpSession session) throws NamingException, SQLException {
        // why refetch the list of users all the time?
        // do  it after sessions expire...
        User[] users = new User[0]; // overkill
        if (session.getAttribute("users") == null) {
            UsersDB usersdb = new UsersDB(Globals.jndi);
            users = usersdb.read();
            session.setAttribute("users", users);
        }
        else {
            users = (User[]) session.getAttribute("users");
        }

        return users;
    }

    private boolean ok(String s) {
        return s != null && !s.isEmpty();
    }
}
