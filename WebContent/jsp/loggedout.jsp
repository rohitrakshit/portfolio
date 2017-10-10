<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Portfolio - Loggedout</title>
</head>
<body bgcolor="GAINSBORO">
<div align="right">Welcome! Please login to continue...</div>
<center>
<h2>PORTFOLIO</h2>
<p>
Thank you <%=request.getAttribute("username") %> for using Portfolio. Please click on <a href='/Portfolio'>Login</a> to use again.
</p>
</center>
</body>
</html>