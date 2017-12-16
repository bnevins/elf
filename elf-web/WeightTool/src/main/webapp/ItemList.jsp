<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= request.getAttribute("title") %></title>
    </head>
    <body>
        <%@page import="java.util.*, com.elf.enterprise.weight.*"%>       
        <%  
            List<WeightItem> items = (List<WeightItem>)request.getAttribute("items");
            
            if(items == null)
                items = new ArrayList<WeightItem>();
            
            String message = (String)request.getAttribute("message");
            String itemTable = (String)request.getAttribute("itemTable");
            boolean su = request.isUserInRole("SUPER_USERS");
         %>
         
    <h1 align="center"><%= request.getAttribute("title") %></h1>

    <% if(message != null) { %>
        <p><b><%= message %></b><p>
    <% } %>

    <center>
    <form action="Controller" method="GET"> 
    
        <input type='submit' name='new' value='Add Todays Weight'>
        <input type="text" name="today" value="" size="8" />    
        <br><input type="submit" name="logout" value="Logout"  />
        <input type="submit" name='deanEdell' value="Dean Edell..." />
        <p>
        <%= itemTable%>
        <% if(su) { %>
        <p><input type="submit" name='administer' value="Administer Weight Tool" />
        <p><input type="submit" name='showAll' value="Show All People" />
        <% } %>
    </form>
    </center>
</body>
</html>
