<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
		<title>Portfolio - View Gains</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
		<script src="jsp/portfolio.js"></script>
		<link rel="stylesheet" href="jsp/portfolio.css" />
	</head>
	<body bgcolor="GAINSBORO">

<table border="0" width="100%">
	<tr>
		<td colspan="11" align="left"><a href="Home"><input type="button" value="Back To Home"/></a> | <a href="showLatest?view=viewgains"><input type="button" value="Show Latest"/></a></td>
		<td align="right">Welcome, <%=((String)session.getAttribute("username")) %> | <a href="logout">Logout</a></td>
	</tr>
</table>
<center>
<% String resp = (String)request.getAttribute("response");
if(resp==null){
	resp = "";
}%>
<p>
<font size="4" color="red"><%=resp%></font>
</p>
<p>P.S: Try to have a glance of the weekly chart for the below stocks and reduce holding quantity if uptrend broke.</p>
<table border="1px"  style="background-color:WHITESMOKE; font-size:25px">
	<tr style="background-color:GRAY;color:AQUA;">
		<th><a href="sort?sortby=symbol&view=viewgains">Symbol</a></th>
		<th><a href="sort?sortby=qntty&view=viewgains">Qty</a></th>
		<th><a href="sort?sortby=avgprc&view=viewgains">Avg Price</a></th>
		<th><a href="sort?sortby=cstval&view=viewgains">Cost</a></th>
		<th><a href="sort?sortby=lstprc&view=viewgains">Last Price</a></th>
		<th><a href="sort?sortby=crrval&view=viewgains">Value</a></th>
		<th><a href="sort?sortby=pftamt&view=viewgains">Profit</a></th>
		<th><a href="sort?sortby=pftpnt&view=viewgains">%</a></th>
		<th><a href="sort?sortby=nxtsll&view=viewgains">Next Sell</a></th>
	</tr>
	<c:forEach items="${stocks}" var="item">
		<tr style="color:green">
			<td class="notes">
				<div id="myModal" class="modal">
					<div class="modal-content">
						${item.actionHtml}
					</div>
				</div>
				${item.symbol}
			</td>
			<td class="notes">
				<div id="myModal" class="modal">
					<div class="modal-content">
						${item.notes}
					</div>
				</div>
				${item.currentQuantity}
			</td>
			<td>${item.averagePrice}</td>
			<td>${item.costValue}</td>
			<td class="more_info" title='${item.lastTrade}'>${item.lastPrice}</td>
			<td>${item.currentValue}</td>
			<td>${item.profitAmount}</td>
			<td>${item.profitPercent}</td>
			<td>${item.nextsell}</td>
		</tr>
	</c:forEach>
	<fmt:formatNumber var="tcc" value="${totalValues.totalCompanyCount}"  maxFractionDigits="0" />
	<fmt:formatNumber var="tsc" value="${totalValues.totalStockCount}"  maxFractionDigits="0" />
	<tr style="background-color:GRAY;color:WHITE;">
		<td>${tcc}</td><td colspan="2">${tsc}</td>
		<td>${totalValues.totalCostValue}</td>
		<td></td>
		<td>${totalValues.totalCurrentValue}</td>
		<td>${totalValues.totalProfitAmount}</td>
		<td>${totalValues.totalProfitPercent}</td>
		<td colspan="2"></td>
	</tr>
	<tr>
		<td colspan="11" align="center"><a href="Home"><input type="button" value="Back To Home"/></a> | <a href="showLatest?view=viewgains"><input type="button" value="Show Latest"/></a></td>
	</tr>
</table>
</center>
<div class='button_up' id='button_up' style='display:none;'></div>
<div class='button_down' id='button_down' style='display:none;'></div>
</body>
</html>