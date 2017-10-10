<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
		<title>Portfolio - Home</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
		<script src="jsp/portfolio.js"></script>
		<script src="jsp/jquery.sortElements.js"></script>
		<link rel="stylesheet" href="jsp/portfolio.css" />
	</head>
	<body>
<table width="100%">
	<tr>
		<td align="left"><table border="0"><tr>
			<td><a href="addstock"><input type="button" value="Add Stock"/></a></td>
			<td><a href="viewGains"><input type="button" value="View Gains"/></a></td>
			<td><a href="showLatest?view=home"><input type="button" value="Show Latest"/></a></td>
			<td class="notes">
				<div id="myModal" class="modal">
					<div class="modal-content">
						<a href="sectorallocationchart"><input type="button" value="Sector Allocation"/></a> | <a href="industryallocationchart"><input type="button" value="Industry Allocation"/></a> | <a href="companyallocationchart"><input type="button" value="Company Allocation"/></a>
					</div>
				</div>
				<a href="#"><input type="button" value="View Allocation"/></a>
			</td>
			<td class="notes">
				<div id="myModal" class="modal">
					<div class="modal-content">
						<table border="1px" cellpadding="0px">
							<tr>
								<td><a href="updatePortfolio?view=home"><input type="button" value="Update Portfolio"/></a></td>
								<td width="50%" align="center"><a href="backup"><input type="button" value="Backup"/></a></td>
								<td><form action="restore" method="post" enctype="multipart/form-data">
										<input type="file" name="restorefile"><input type="submit" value="Restore"/>
									</form>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<a href="#"><input type="button" value="Manage"/></a>
			</td>
			<td><input type="button" id="bottom" value="Bottom"/></td>
			<td><input type="text" id="searchbox" placeholder="Search" onfocus="if (this.placeholder == 'Search') {this.placeholder = '';}" onblur="if (this.placeholder == '') {this.placeholder = 'Search';}" style="width: 50px;"/></td>
			<td><label><input type="checkbox" id="onlyNonZeroes" />Hide</label></td>
			</tr></table>
		</td>
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
<table id="maintable" border="1px"  style="background-color:WHITESMOKE; font-size:25px;">
	<thead>
		<tr style="background-color:GRAY;color:AQUA;">
			<th id="symbol_col">Symbol</th>
			<th id="qty_col">Qty</th>
			<th id="avgprice_col">Avg Price</th>
			<th id="cost_col">Cost</th>
			<th id="lastprice_col">Last Price</th>
			<th id="value_col">Value</th>
			<th id="loss_col">Loss</th>
			<th id="percent_col">%</th>
			<th id="nbuy_col">Next Buy</th>
			<th id="ncost_col">Next Cost</th>
			<th id="nsell_col">Next Sell</th>
		</tr>
	</thead>
	<tbody id="maincontent">
		<c:forEach items="${stocks}" var="item">
		<fmt:parseNumber var="i" type="number" value="${item.lastPrice - item.averagePrice}" />
		<fmt:parseNumber var="j" type="number" value="${item.lastPrice - item.nextsell}" />
		<fmt:parseNumber var="k" type="number" value="${item.lastPrice - item.nextAveragePrice}" />
			<tr style="${i < 0 ? 'color:#C11B17;' : ''}${j >= 0 ? 'background-color:#7FFFD4;' : ''}${item.currentQuantity == '0' ? 'background-color:BEIGE;' : ''}${k <= 0 ? 'background-color:#FFDFDD;' : ''}">
				<td class="notes" style="white-space: nowrap; overflow: hidden; max-width:100px;">
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
				<span class="curr_qty_spn">${item.currentQuantity}</span></td>
				<td class="more_info" title='${item.averagePrice}'>${item.elevatedAveragePrice}</td><td class="more_info" title='${item.costValue}'>${item.elevatedCostValue}</td>
				<td class="more_info" title='${item.lastTrade}'>${item.lastPrice}</td><td>${item.currentValue}</td><td>${item.lossAmount}</td><td>${item.lossPercent}</td>
				<td>${item.nextAveragePrice}</td><td>${item.nextCostValue}</td>
				<td class="notes">
					<div id="myModal" class="modal">
						<div class="modal-content">
							${item.nextSellDesc}
						</div>
					</div>
					${item.nextsell}
				</td>
		</c:forEach>
	</tbody>
	<fmt:formatNumber var="tcc" value="${totalValues.totalCompanyCount}"  maxFractionDigits="0" />
	<fmt:formatNumber var="tsc" value="${totalValues.totalStockCount}"  maxFractionDigits="0" />
	<tr style="background-color:GRAY;">
		<td class="notes">
			<div id="myModal" class="modal">
				<div class="modal-content">
					<a href="sectorallocationchart">Sector Allocation</a> | <a href="companyallocationchart">Company Allocation</a>
				</div>
			</div>
			<span id="totalComapanyCount">${tcc}</span>
		</td>
		<td colspan="2">${tsc}</td>
		<td class="more_info" title='${totalValues.totalCostValue}'>${totalValues.totalElevatedCostValue}</td>
		<td></td>
		<td>${totalValues.totalCurrentValue}</td>
		<td class="more_info" title='${totalValues.totalLossAmount}'>${totalValues.totalElevatedLossAmount}</td>
		<td class="more_info" title='${totalValues.totalLossPercent}'>${totalValues.totalElevatedLossPercent}</td>
		<td></td>
		<td>${totalValues.totalNextAverageCost}</td>
		<td colspan="2"></td>
	</tr>
</table>
<table cellpadding="10px">
	<tr>
	<td><a href="addstock"><input type="button" value="Add Stock"/></a></td>
	<td><a href="viewGains"><input type="button" value="View Gains"/></a></td>
	<td><a href="showLatest?view=home"><input type="button" value="Show Latest"/></a></td>
	<td class="notes">
		<div id="myModal" class="modal">
			<div class="modal-content">
				<a href="sectorallocationchart"><input type="button" value="Sector Allocation"/></a> | <a href="companyallocationchart"><input type="button" value="Company Allocation"/></a>
			</div>
		</div>
		<a href="#"><input type="button" value="View Allocation"/></a>
	</td>
	<td><input type="button" id="top" value="Top"/></td>
	</tr>
</table>
</center>
<div class='button_up' id='button_up' style='display:none;'></div>
<div class='button_down' id='button_down' style='display:none;'></div>
</body>
</html>