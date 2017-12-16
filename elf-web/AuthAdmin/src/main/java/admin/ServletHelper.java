/*
 * ServletHelper.java
 *
 * Created on September 28, 2007, 11:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package admin;

import com.elf.enterprise.auth.User;
import com.elf.enterprise.auth.UsersManager;
import com.elf.enterprise.db.DBHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This class is used to get the add-new-user code here.
 * It is called "unsecured" when they create a brand-new DB and need to add themself
 * so the login mechanism can work for further admin.
 * I.e. this code is called from Secure and Unsecure servlets...
 * @author bnevins
 */
public class ServletHelper
{
    public ServletHelper()
    {
    }
    
    static boolean handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if(request.getParameter("logout") != null)
        {
            request.getSession().invalidate();
            response.sendRedirect("index.jsp");
            return true;
        }
        else
            return false;
    }
    
    public static String getJNDIPrefs()
    {
        // note these prefs are not saved per-user
        Preferences prefs = Preferences.userNodeForPackage(admin.ServletHelper.class);
        return prefs.get("jndi", "jdbc/bnevins");
    }
    
    public static  void setJNDIPrefs(String s)
    {
        Preferences prefs = Preferences.userNodeForPackage(admin.ServletHelper.class);
        prefs.put("jndi", s);
    }   
    
    public static String getJndi(HttpServletRequest request, HttpServletResponse response)
    {
        // 1 its in the request as a parameter
        HttpSession session = request.getSession();
        String jndi = request.getParameter("jndi");
        
        if(ok(jndi))
        {
            session.setAttribute("jndi", jndi);
            setJNDIPrefs(jndi);
            return jndi;
        }
        
        // 2. it's in the session
        jndi = (String)session.getAttribute("jndi");
        
        if(ok(jndi))
        {
            setJNDIPrefs(jndi);
            return jndi;
        }
        
        //3. Use prefs
        jndi = getJNDIPrefs();

        if(ok(jndi))
            return jndi;
        
        //4. use the global value
        return Globals.jndi;
    }
    
    void addUser(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String jndi = getJndi(request, response);
        UsersManager umgr = UsersManager.getInstance(jndi);
        User[] users = umgr.getUsers(true);
        
        String[] allRoles = umgr.getAllRoles(false);
        List<String> userRolesList = new ArrayList<String>();
        
        // look for checked roles
        for(String role : allRoles)
        {
            // the parameter is only here if they checked the role...
            if(request.getParameter("rolename_" + role) != null)
                userRolesList.add(role);
        }
        
        // look for a new role
        
        String newRole = request.getParameter("newRole");
        
        if(newRole != null && newRole.length() > 0)
            userRolesList.add(newRole);
        
        String[] userRoles = userRolesList.toArray(new String[userRolesList.size()]);
        
        String un = request.getParameter("un");
        String fn = request.getParameter("fn");
        String ln = request.getParameter("ln");
        String em = request.getParameter("em");
        String pw1 = request.getParameter("pw1");
        String pw2 = request.getParameter("pw2");
        String newPassword = null;
        
        if(pw1 != null && pw2 != null && pw1.equals(pw2) && pw1.length() > 0)
            newPassword = pw1;
        
        User user = findUser(un, users);
        
        if(user != null)
        {
            user.setFirstName(fn);
            user.setLastName(ln);
            user.setEmail(em);
            user.setRoles(userRoles);
            
            if(newPassword != null)
            {
                user.setHashedPassword(DBHelper.hashPassword(newPassword));
            }
            umgr.updateUser(user);
        }
        else
        {
            // new user
            // TODO Error processing
            umgr.addUser(un, newPassword, fn, ln, em, userRoles);
        }
        return;
    }

    private User findUser(String un, User[] users)
    {
        for(User user : users)
        {
            if(user.getUserName().equals(un))
                return user;
        }
        return null;
    }

    private static boolean ok(String s)
    {
        return s != null && s.length() > 0;
    }
}
