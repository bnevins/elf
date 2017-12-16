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
        <title>Registration</title>
    </head>
    <body>

    <h1 align="center">Please Register for your FREE Account</h1>
    <% 
        String un = request.getParameter("username");
        String pw = request.getParameter("password");
        String fn = request.getParameter("firstname");
        String ln = request.getParameter("lastname");
        String em = request.getParameter("email");
    
        if(un == null || request.getAttribute("clearUserName") != null) un = "";
        if(fn == null) fn = "";
        if(ln == null) ln = "";
        if(pw == null) pw = "";
        if(em == null) em = "";
        String err = (String)request.getAttribute("err");
        
        if(err != null)   
            out.println("<h3 align='center' style='color: red'>" + err + "</h3>");
        %>
    <form action="Registrar" method="POST">
        <table align='center'>
        <tr><td>*User name:</td>    <td><input type="text"      name="username"     value='<%= un %>'/></td></tr>
        <tr><td>*Password:</td>     <td><input type="password"   name="pw1"     value='<%=pw%>'/></td></tr>
        <tr><td>*Password Again:</td>     <td><input type="password"   name="pw2"     value='<%=pw%>'/></td></tr>
        <tr><td>First Name:</td>    <td><input type="text"      name="firstname"    value='<%=fn%>'/></td></tr>
        <tr><td>Last Name:</td>     <td><input type="text"       name="lastname"     value='<%=ln%>'/></td></tr>
        <tr><td>*Email:</td>         <td><input type="text"           name="email"        value='<%=em%>'/></td></tr>
        <tr><td><input type="submit" value="Create My Account!" /></td></td></tr>
        </table>
    </form>
    </body>
</html>
