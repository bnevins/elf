<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="com.elf.webapps.todo.model.ToDoItem"%>
<%@page import="com.elf.enterprise.auth.User"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add a To-Do Item</title>
    </head>
    <body>
        <%
            User[] users = (User[]) session.getAttribute("users");

            if (users == null) {
                users = new User[0];
            }

            int numUsers = users.length;
        %>

        <h1>Add a To-Do Item</h1>

        <form action="Controller" method="POST"> 
            <p>Description: <input type="text" name="description" value="" size="80" />
                <%
                    if (!Boolean.parseBoolean((String) request.getAttribute("private"))) {
                %>
            <p>assigned To: <select name="assignee">
                    <%
                        String me = request.getUserPrincipal().getName();

                        for (int i = 0; i < numUsers; i++) {
                            String username = users[i].getUserName();
                            String line = "<option id=\"" + username + "\"";

                            // make me the default assignee
                            if (username.equals(me)) {
                                line += " SELECTED=\"selected\"";
                            }


                            line += ">" + username + "</option>";
                    %>    
                    <%= line%>
                    <% }%>
                </select>
                <%
                    }
                %>
            <P><INPUT TYPE=SUBMIT> <INPUT TYPE=RESET>     
        </form>
    </body>
</html>
