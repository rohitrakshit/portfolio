<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Portfolio - Login</title>
</head>
<body bgcolor="GAINSBORO">
<div align="right">Welcome! Please login to continue...</div>
<center>
<h2>PORTFOLIO</h2>
<% String resp = (String)request.getAttribute("response");
if(resp==null){
	resp = "";
}%>
<p>
<%=resp%>
</p>
<form action="login" method="post">
	<fieldset style="width:30%">
    <legend>Login Form:</legend>
		<table>
			<tr><td><input type="text" name="username"/></td></tr>
			<tr><td><input type="password" name="password"/></td></tr>
			<tr><td align="center"><input type="submit" value="Login"/></td></tr>
		</table>
	</fieldset>
</form>
</center>
</body>
</html>