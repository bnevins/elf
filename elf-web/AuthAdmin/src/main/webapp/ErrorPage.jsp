<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
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
        <title>Error</title>
    </head>
    <body>

    <h1 align='center'>Error Encountered</h1>

    Here is the Exception: <I><%= exception %>.
    <p>
        <pre>
            <% exception.printStackTrace(new java.io.PrintWriter(out)); %>
        </pre>
    </body>
</html>
