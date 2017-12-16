<%@page import="java.util.List"%>
<%@page import="com.elf.webapps.todo.model.ToDoItem"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= request.getAttribute("title") %></title>
        <script src="sorttable.js"></script>
    </head>
    <body>
        <%
            List<ToDoItem> items = (List<ToDoItem>)request.getAttribute("items");
            boolean isPrivate = Boolean.parseBoolean((String)request.getAttribute("private"));
            boolean su = Boolean.parseBoolean((String)request.getAttribute("su"));
            boolean showClosed = (Boolean)session.getAttribute("showClosed");
            String showClosedButtonText;
            
            if(showClosed)
                showClosedButtonText = "Hide Closed Items";
            else
                showClosedButtonText = "Show Closed Items";
            
            boolean showAllUsers = false;
            Object o = session.getAttribute("showAllUsers");
            
            if(o != null)
                showAllUsers = (Boolean)o;

            String showAllUsersText;
            
            if(showAllUsers)
                showAllUsersText = "Show Just My Items";
            else
                showAllUsersText = "Show All Items";
         %>
         
    <h1 align="center"><%= request.getAttribute("title") %></h1>
    <form action="Controller" method="GET"> 
    <input type='submit' name='toggleShowClosed' value='<%= showClosedButtonText %>'> 
    <input type='submit' name='toggleShowAllUsers' value='<%= showAllUsersText %>'> 
    <input type='submit' name='new' value='Add Item...'>
    <input type="submit" name="logout" value="Logout"  />
    <% if(su) { %>
    <p><input type="submit" name='admin' value="Administer ToDo" />
    <% } %>
    
    
    <!--
    /* Sortable tables */
table.sortable thead {
    background-color:#eee;
    color:#666666;
    font-weight: bold;
    cursor: default;
} -->
    
    <table bgcolor="#eee" class="sortable" align="center" cellpadding='4' border='1'>
        <tr>
            <th>Description</th>
            <th>Date Opened</th>
            <% if(!isPrivate) { %>
                <th>Assigned By</th>
                <th>Assigned To</th>
            <% } %>

            <th>Date Closed</th>
        </tr>
        <% for(int j = 0; j < items.size(); j++)
            {
                    ToDoItem item = items.get(j);
                    
                    if(!showClosed && item.isClosed())
                        continue;
        %>

            <tr>
            <td><%= item.getDescription()%></td>
            <td><%= item.getDateOpenedString()%></td>
            <% if(!isPrivate) { %>
            <td><%= item.getAssignedBy()%></td>
            <td><%= item.getAssignedTo()%></td>
            <% } %>
            <td align='center'><%= item.getDateClosedString()%></td>
            <td align='center'><input type='submit'  name='<%="showhide" + item.getId()%>' value='<%=item.getButtonText()%>'></td>
            <td><input type='submit' name='<%="delete" + item.getId()%>' value='Delete'></td>
            </tr>
            <% } %>
    </table>
    </form>
</body>
</html>
