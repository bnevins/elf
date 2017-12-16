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
        <title>JSP Page</title>
    </head>
    <body>
    <%@page import="java.util.*, com.elf.enterprise.auth.*, admin.*"%>       

    <h1>JSP Page</h1>
<%
       try
        {
            int             keyFromUser = Integer.parseInt(request.getParameter("signupKey"));
            String          userid = request.getUserPrincipal().getName();
            UsersManager    umgr = UsersManager.getInstance(ServletHelper.getJndi(request, response));
            umgr.validateUser(userid, keyFromUser);
 %>
        <h2>Congratulations!  Your account is ready for use.</h2>
        <A HREF="/ToDoList">ToDoList</a><br>
        <A HREF="/DeanEdell">Dean Edell Archives</a><br>
 <%
        }
        catch(Exception e)
        {
 %>
        <h2>Oops!  There was a problem:</h2>
        <b><%= e.toString() %></b>
 <%
        }
%>

    </body>
</html>
