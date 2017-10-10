<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Portfolio - Buy/Sell stock</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
		<script src="jsp/portfolio.js"></script>
		<script src="jsp/datetimepicker_css.js"></script>
		<script>
		$(document).ready(function() {
		    $('#buy').change(function() {
		        if(this.checked) {
		            $('#submit').val("BUY");
		            $('#qtytd').html("BuyQuantity");
		            var result_style = document.getElementById('qtytr').style;
		            result_style.display = 'table-row';
		            var result_style = document.getElementById('dttr').style;
		            result_style.display = 'table-row';
		        }
		    });
		    $('#sell').change(function() {
		        if(this.checked) {
		            $('#submit').val("SELL");
		            $('#qtytd').html("SellQuantity");
		            var result_style = document.getElementById('qtytr').style;
		            result_style.display = 'none';
		            var result_style = document.getElementById('dttr').style;
		            result_style.display = 'none';
		        }
		    });
		});
		</script>
		<link rel="stylesheet" href="jsp/portfolio.css" />
	</head>
<body bgcolor="GAINSBORO">
<div align="right">Welcome, <%=((String)session.getAttribute("username")) %> | <a href="logout">Logout</a></div>
<center>
<% String resp = (String)request.getAttribute("response");
if(resp==null){
	resp = "";
}%>
<p>
<font size="4" color="red"><%=resp%></font>
</p>
<form action="buysellStock" method="post">
	<fieldset style="width:30%;">
    <legend>Buy/Sell Stock:</legend>
		<table border="1px">
			<tr>
				<td colspan="2" align="center">Buy:
					<c:set var="def" value='${requestScope.req}'/>
					<input type="radio" id="buy" name="buysell" onclick="changeButtonVal()" ${def == 'buy' ? 'checked' : ''} value="buy"/> | Sell:
					<input type="radio" id="sell" name="buysell" onclick="changeButtonVal()" ${def == 'sell' ? 'checked' : ''} value="sell"/>
				</td>
			</tr>
			<tr><td>Name</td><td><input type="text" id="stockName" name="stockName" value='${stock.name}' readonly/></td></tr>
			<tr><td>Symbol</td><td><input type="text" name="symbol" value='${stock.symbol}' readonly/></td></tr>
			<tr><td>Current Qty</td><td><input type="text" name="last_qty" value='${stock.currentQuantity}' readonly/></td></tr>
			<tr><td id="qtytd">${def == 'buy' ? 'BuyQuantity' : 'SellQuantity'}</td><td><input type="text" name="qty" value='${requestScope.qty}'/></td></tr>
			<tr id="qtytr" style="display:${def == 'buy' ? 'table-row' : 'none'};"><td>Price</td><td><input type="text" name="price"  value='${requestScope.price}'/></td></tr>
			<tr id="dttr" style="display:${def == 'buy' ? 'table-row' : 'none'};"><td>DateTime</td><td><input type="text" name="datetime" id="datetime" onclick="NewCssCal ('datetime','yyyyMMdd','dropdown',true,'24',true,'past')" value='${requestScope.datetime}'/></td></tr>
			<tr><td>Notes</td><td><textarea name="notes">${stock.notes}</textarea></td></tr>
			<tr><td align="center"><a href="Home"><input type="button" value="Home"/></a></td><td align="center"><input type="submit" name="submit" id="submit" value="${def == 'buy' ? 'BUY' : 'SELL'}"></td></tr>
		</table>
		<input type="hidden" name="exchange" value='${stock.exchange}'/>
		<input type="hidden" name="symbol" value='${stock.symbol}'/>
	</fieldset>
</form>
</center>
</body>
</html>