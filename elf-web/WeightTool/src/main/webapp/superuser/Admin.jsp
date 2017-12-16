<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Weight Tool Administration</title>
    </head>
    <body>

        <h1 align='center'>Weight Tool Administration</h1>
        
        <%
            String message = (String)request.getAttribute("message");
            if(message != null) { 
        %>
        <p><b><%= message %></b><p>
        <% 
            } 
        %>

        <%@page import="java.util.*, com.elf.enterprise.weight.*"%>       

    <form action='Admin' method="POST">
            Backup Filename: <input type="text"   name="backup_file" value="C:/temp/weight-tool-bu.txt" size="24" />
            <br>
            <input type="submit" value="Verify DB" name="verify" />
            <input type="submit" value="Clear DB" name="cleardb" />
            <input type="submit" value="Logout" name="logout" />
            <input type="submit" value="Backup" name="backup" />
            <input type="submit" value="Restore" name="restore" />
            <input type="submit" value="Exit Admin" name="exitadmin" />
    </form>
        <HR>
        <H2 align='center'>Subversion Revision 97, February 6, 2011</H2>
    </body>
</html>
