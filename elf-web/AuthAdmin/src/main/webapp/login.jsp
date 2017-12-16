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
        <title>Login</title>
    </head>
    <body>

    <h1 align="center">Please Login</h1>

    <form method="POST" action="j_security_check">
        <table>
        <tr><td>User name:</td><td><input type="text" name="j_username" /></td></tr>
        <tr><td>Password:</td><td><input type="password" name="j_password" /></td></tr>
        <tr><td><input type="submit" value="Login" /></td></td></tr>
        </table>
    </form>
    </body>
</html>
