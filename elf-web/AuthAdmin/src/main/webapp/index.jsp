<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*, admin.*"%>       

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JDBC Authentication Management</title>
    </head>
    <body>
    <h1 align='center'>JDBC Authentication Management</h1>
<p> You must have a jdbc resource and a poolname already defined.  
Make sure you have the property "connectionAttributes" equal to ";create=true"
    <%  
        String message = (String)request.getAttribute("message");
        if(message != null)
        {
            out.println("<p align='center'>" + message + "</p>");
        }
        
        String jndi = admin.ServletHelper.getJndi(request, response);
    %>


        <form action="AuthCreationManager" method="GET">
            <center>
                JNDI name for the database:  <input type="text" name="jndi" value="<%= jndi %>" /> 
                <p>
                <input type="submit" name="verify" value="Verify Database and Schema" />
                <%  if(request.getAttribute("adminButton") != null) {
                %>
                <br>
                <input type="submit" name='admin' value="Administer DB" />
                
                <%  
                } 
                
                    
                  if(request.getAttribute("createButton") != null) {
                %>
                    
                <br>
                <input type="submit" name="create" value="Create Empty Database" />
                <%  
                } 
                
                  if(request.getAttribute("recreateButton") != null) {
                %>
                <br>
                <input type="submit" name="recreate" value="Clear Database" />
                <%  
                } 
                %>
            </center>
    </form>


</body>
</html>
