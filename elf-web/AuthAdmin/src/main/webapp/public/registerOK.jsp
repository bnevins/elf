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
        <title>JSP Page</title>
    </head>
    <body>

    <h1>New Account Setup</h1>
    Your new account has been setup.  A URL has been emailed to you.  To finish registration
    you have to click on the link in your email (<%= request.getParameter("em") %>).
    <p>
    Actually, just kidding.  You don't HAVE to do that -- but someday you will so you may
    as well get on with it.<br>
        <A href="/">Home</A> 
    </body>
</html>
