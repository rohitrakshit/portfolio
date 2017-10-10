<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Portfolio - Company allocation</title>
<!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <link rel="stylesheet" href="jsp/portfolio.css" />
    <script type="text/javascript">

      // Load the Visualization API and the corechart package.
      google.charts.load('current', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.charts.setOnLoadCallback(drawChart);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        data.addRows([
<%
	Map<String,Double> companyDataset = (Map<String,Double>)session.getAttribute("companyDataset");
	StringBuilder sb = new StringBuilder();
	if(companyDataset!=null){
		for (Map.Entry<String, Double> alloc : companyDataset.entrySet()) {
			sb.append("['");
			sb.append(alloc.getKey());
			sb.append("',");
			sb.append(alloc.getValue());
			sb.append("],");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
	}
%>
        <%=sb.toString()%>
        ]);

        // Set chart options
        var options = {	'title':'Sector Wise Allocation Chart',
		        		'width':'1200',
		                'height':'700',
		                'is3D': 'true',
             	   		'backgroundColor': {'fill':'transparent' },
             	  		'legend': { 'position': 'labeled'}
             	   		};
 			options.chartArea = { left: '1%', top: '0%', width: "100%", height: "100%" };

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>
</head>
<body bgcolor="GAINSBORO">
	<table border="0" width="100%" cellpadding="0"><tr><td align="left"><a href="Home"><input type="button" value="Back To Home"/></a> | <a href="sectorallocationchart"><input type="button" value="Sector Allocation"/></a> | <a href="industryallocationchart"><input type="button" value="Industry Allocation"/></a></td><td align="right">Welcome, <%=((String)session.getAttribute("username")) %> | <a href="logout">Logout</a></td></tr></table>
	<% String resp = (String)request.getAttribute("response");
		if(resp==null){
			resp = "";
	}%>
	<center>
	<!--Div that will hold the pie chart-->
    <div id="chart_div"></div>
	<p><a href="Home"><input type="button" value="Back To Home"/></a> | <a href="sectorallocationchart"><input type="button" value="Sector Allocation"/></a> | <a href="industryallocationchart"><input type="button" value="Industry Allocation"/></a></p>
	</center>
</body>
</html>