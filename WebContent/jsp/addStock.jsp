<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Portfolio - Add stock</title>
</head>
<body bgcolor="GAINSBORO">
<div align="right">Welcome, <%=((String)session.getAttribute("username")) %> | <a href="logout">Logout</a></div>
<center>
<h2>PORTFOLIO</h2>
<% String resp = (String)request.getAttribute("response");
if(resp==null){
	resp = "";
}%>
<p>
<font size="4" color="red"><%=resp%></font>
</p>
<form action="addStock" method="post">
	<fieldset style="width:30%">
    <legend>Add Stock Form:</legend>
		<table border="1px" style="width: 300px;">
			<tr><td>Exchange</td><td>
				<c:set var="def" value='${addStock.exchange}'/>
				<select name="exchange">
				  <option value="NSE" ${def == 'NSE' ? 'selected="selected"' : ''}>NSE</option>
				  <option value="BSE" ${def == 'BSE' ? 'selected="selected"' : ''}>BSE</option>
				  <option value="MCX" ${def == 'MCX' ? 'selected="selected"' : ''}>MCX</option>
				</select>
			</td></tr>
			<tr><td>Symbol*</td><td><input type="text" name="symbol" value="${addStock.symbol}" style="width: 99%;"/></td></tr>
			<tr><td>Symbol ID</td><td><input type="text" name="symbol_id" value="${addStock.symbolId}" style="width: 99%;"/></td></tr>
			<tr><td>Name</td><td><input type="text" name="name" value="${addStock.name}" style="width: 99%;"/></td></tr>
			<tr><td>Group</td><td><input type="text" name="group" value="${addStock.group}" style="width: 99%;"/></td></tr>
			<tr><td>Sector</td>
			<td><c:set var="sect" value='${addStock.sector}'/>
				<select name="sector" id="sector" style="width: 100%;">
					<option value="">Select</option>
					<option value="Automotive" ${sect == 'Automotive' ? 'selected="selected"' : ''}>Automotive</option>
					<option value="Financial" ${sect == 'Financial' ? 'selected="selected"' : ''}>Financial</option>
					<option value="Cement & Construction" ${sect == 'Cement & Construction' ? 'selected="selected"' : ''}>Cement & Construction</option>
					<option value="Chemicals" ${sect == 'Chemicals' ? 'selected="selected"' : ''}>Chemicals</option>
					<option value="Conglomerates" ${sect == 'Conglomerates' ? 'selected="selected"' : ''}>Conglomerates</option>
					<option value="Consumer Durables" ${sect == 'Consumer Durables' ? 'selected="selected"' : ''}>Consumer Durables</option>
					<option value="Consumer Non-durables" ${sect == 'Consumer Non-durables' ? 'selected="selected"' : ''}>Consumer Non-durables</option>
					<option value="Engineering & Capital Goods" ${sect == 'Engineering & Capital Goods' ? 'selected="selected"' : ''}>Engineering & Capital Goods</option>
					<option value="Food & Beverages" ${sect == 'Food & Beverages' ? 'selected="selected"' : ''}>Food & Beverages</option>
					<option value="Information Technology" ${sect == 'Information Technology' ? 'selected="selected"' : ''}>Information Technology</option>
					<option value="Manufacturing" ${sect == 'Manufacturing' ? 'selected="selected"' : ''}>Manufacturing</option>
					<option value="Media & Entertainment" ${sect == 'Media & Entertainment' ? 'selected="selected"' : ''}>Media & Entertainment</option>
					<option value="Metals & Mining" ${sect == 'Metals & Mining' ? 'selected="selected"' : ''}>Metals & Mining</option>
					<option value="Miscellaneous" ${sect == 'Miscellaneous' ? 'selected="selected"' : ''}>Miscellaneous</option>
					<option value="Oil & Gas" ${sect == 'Oil & Gas' ? 'selected="selected"' : ''}>Oil & Gas</option>
					<option value="Pharmaceuticals" ${sect == 'Pharmaceuticals' ? 'selected="selected"' : ''}>Pharmaceuticals</option>
					<option value="Retail & Real Estate" ${sect == 'Retail & Real Estate' ? 'selected="selected"' : ''}>Retail & Real Estate</option>
					<option value="Services" ${sect == 'Services' ? 'selected="selected"' : ''}>Services</option>
					<option value="Telecommunication" ${sect == 'Telecommunication' ? 'selected="selected"' : ''}>Telecommunication</option>
					<option value="Tobacco" ${sect == 'Tobacco' ? 'selected="selected"' : ''}>Tobacco</option>
					<option value="Utilities" ${sect == 'Utilities' ? 'selected="selected"' : ''}>Utilities</option>
				</select>
			</td></tr>
			<tr><td>Industry</td><td><input type="text" name="industry" value="${addStock.industry}" style="width: 99%;"/></td></tr>
			<tr><td>Notes</td><td><textarea name="notes" style="width: 98%;">${addStock.notes}</textarea></td></tr>
			<tr><td align="center"><a href="Home"><input type="button" value="Home"/></a></td><td align="center"><input type="submit" value="Add Stock"/></td></tr>
		</table>
	</fieldset>
</form>
</center>
</body>
</html>