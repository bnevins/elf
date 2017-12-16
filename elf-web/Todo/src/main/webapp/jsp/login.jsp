<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>

    <%@page import="java.util.*, com.elf.enterprise.web.CookieManager"%>       
    <%
        CookieManager cm = new CookieManager();
        String user = cm.getUserName(request);
        String password = cm.getPassword(request);
    
        if(user != null && password != null)
        {
            /*
            request.setAttribute("j_username", user);
            request.setAttribute("j_password", password);
            RequestDispatcher rd = request.getRequestDispatcher("/WeightTool/j_security_check");
            rd.forward(request, response);
            return;
            */
        }
    %>
    <h1 align="center">Please Login</h1>
    <center>
    <form method="POST" action="j_security_check">
    <table>
    <tr><td>User name:</td><td><input type="text" name="j_username" value="<%= user %>"/></td></tr>
    <tr><td>Password:</td><td><input type="password" name="j_password" value="<%= password %>" /></td></tr>
    <tr><td><input type="checkbox" name="remember" value="ON" />Remember Me</td></tr>
    <tr><td><input type="submit" value="Login" /></td></td></tr>
    </table>
    </form>
    <h1 align="center">Or create a new account</h1>
    <form action="/AuthAdmin/Registration" method="POST">
        <input type="submit" value="Register" />
    </form>
    </center>
    </body>
</html>
