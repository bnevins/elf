<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= request.getAttribute("title").toString() %></title>
    </head>
    <body>

    <%@page import="java.util.*, com.elf.enterprise.auth.*"%>       
    <%
        User user = (User)request.getAttribute("user");
        String[] allRoles = (String[])request.getAttribute("roles");
        String un = (user == null) ? "" : user.getUserName();
        String fn = (user == null) ? "" : user.getFirstName();
        String ln = (user == null) ? "" : user.getLastName();
        String em = (user == null) ? "" : user.getEmail();
        String pw = (user == null) ? "" : user.getHashedPassword();
        
        String actionServlet = (String)request.getAttribute("actionServlet");
        
            if(actionServlet == null) // secured
                actionServlet = "AuthUpdateManager"; // secured
    %>

    <h1 align='center'><%= request.getAttribute("title").toString() %></h1>
    <form action="<%= actionServlet %>" method="GET">
    <table border='0' align="center"cellpadding='4' border='1'>
        <tr><td>User Name: <td><input type="text" name="un" value="<%= un %>" /></tr>
        <tr><td>Hashed Password: <td><input type="text" name="pw" readonly='readonly' value="<%= pw %>" /></tr>
        <tr><td>First Name: <td><input type="text" name="fn"  value="<%= fn %>" /></tr>
        <tr><td>Last Name: <td><input type="text" name="ln"  value="<%= ln %>" /></tr>
        <tr><td>Email: <td><input type="text" name="em"  value="<%= em %>" /></tr>
        <tr><td>Create New Role: <td><input type="text" name="newRole"  value="" /></tr>
        <tr><td>New Password: <td><input type="password" name="pw1"  value="" /></tr>
        <tr><td>New Password Again: <td><input type="password" name="pw2"  value="" /></tr>
    <%
        for(String role : allRoles)
        {
            String name = "rolename_" + role;
            
            String checked = "";
            
            if(user != null && user.isMember(role))
                checked =  "CHECKED";
    %>
                <tr><td><input type="checkbox" name="<%= name %>" value="ON" 
                <%= checked %>/> <%= role %></td></tr>
    <%
        }
    %>
        
    </table>
    <p>
    <center><input type='submit' align='center' name='useredit' value='Submit'/></center>
    </body>
</html>
