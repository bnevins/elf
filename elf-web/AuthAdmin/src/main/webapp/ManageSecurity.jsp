<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JDBC Authentication Management</title>
    </head>
    <body>
    <%@page import="java.util.*, com.elf.enterprise.auth.*, admin.ServletHelper"%>       
    <%
        User[] users = (User[])request.getAttribute("users");
    %>
    <h1 align='center'>JDBC Authentication Management</h1>
    <form action="AuthManager" method="GET">
    <h2 align='center'>Data Source: <%= ServletHelper.getJndi(request, response) %></h2>
    
    <center>
        <input type='submit' name='newUser' value='New User...'></td>            
        <input type='submit' name='logout' value='Logout'></td>            
    <p>
    <table  align="center"cellpadding='4' border='1'>
        <tr>
            <th>Username</th>
            <th>Hashed Password</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Roles</th>
            <th>Signup Key</th>
        </tr>
    <%
        for(User user : users)
        {
            String[] roles = user.getRoles();
            String role = "";
            boolean firstRole = true;
            for(int i = 0; i < roles.length; i++)
            {
                if(i > 0)
                    role += ", ";
                    
                role += roles[i];
            }
    %>
    <tr>
        <td><%= user.getUserName() %></td>
        <td><%= user.getHashedPassword() %></td>
        <td><%= user.getFirstName() %></td>
        <td><%= user.getLastName() %></td>
        <td><%= user.getEmail() %></td>
        <td><%= role %></td>
        <td><%= user.getSignupId() %></td>
        <td><input type='submit' name='<%="delete_" + user.getUserName()%>' value='Delete'></td>            
        <td><input type='submit' name='<%="edit_" + user.getUserName()%>' value='Edit...'></td>            
    </tr>
    <%
        }
    %>
    
    </body>
</html>
