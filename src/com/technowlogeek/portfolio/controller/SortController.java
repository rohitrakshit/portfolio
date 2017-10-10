package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/sort")
public class SortController extends HttpServlet {
	public Comparator<Map<String, String>> symbolComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return m1.get("symbol").compareTo(m2.get("symbol"));
	    }
	};
	public Comparator<Map<String, String>> currentQuantityComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        int num1 = Integer.parseInt(m1.get("currentQuantity"));
	        int num2 = Integer.parseInt(m2.get("currentQuantity"));
	        System.out.println(num1+" = "+num2);
	        if(num1<num2){
	        	return -1;
	        }else if(num1==num2){
	        	return 0;
	        }else if(num1>num2){
	        	return 1;
	        }
	        return 0;
	    }
	};
	public Comparator<Map<String, String>> elevatedAveragePriceComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("elevatedAveragePrice")).compareTo(Double.valueOf(m2.get("elevatedAveragePrice")));
	    }
	};
	public Comparator<Map<String, String>> averagePriceComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("averagePrice")).compareTo(Double.valueOf(m2.get("averagePrice")));
	    }
	};
	public Comparator<Map<String, String>> costValueComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("costValue")).compareTo(Double.valueOf(m2.get("costValue")));
	    }
	};
	public Comparator<Map<String, String>> lastPriceComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("lastPrice")).compareTo(Double.valueOf(m2.get("lastPrice")));
	    }
	};
	public Comparator<Map<String, String>> currentValueComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("currentValue")).compareTo(Double.valueOf(m2.get("currentValue")));
	    }
	};
	public Comparator<Map<String, String>> lossAmountComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("lossAmount")).compareTo(Double.valueOf(m2.get("lossAmount")));
	    }
	};
	public Comparator<Map<String, String>> lossPercentComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("lossPercent")).compareTo(Double.valueOf(m2.get("lossPercent")));
	    }
	};
	public Comparator<Map<String, String>> profitAmountComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("profitAmount")).compareTo(Double.valueOf(m2.get("profitAmount")));
	    }
	};
	public Comparator<Map<String, String>> profitPercentComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("profitPercent")).compareTo(Double.valueOf(m2.get("profitPercent")));
	    }
	};
	public Comparator<Map<String, String>> nextAveragePriceComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("nextAveragePrice")).compareTo(Double.valueOf(m2.get("nextAveragePrice")));
	    }
	};
	public Comparator<Map<String, String>> nextCostValueComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	        return Double.valueOf(m1.get("nextCostValue")).compareTo(Double.valueOf(m2.get("nextCostValue")));
	    }
	};
	public Comparator<Map<String, String>> nextSellComparator = new Comparator<Map<String, String>>() {
	    public int compare(Map<String, String> m1, Map<String, String> m2) {
	    	int value = 0;
	    	try{
	    		value = Double.valueOf(m1.get("nextsell")).compareTo(Double.valueOf(m2.get("nextsell")));
	    	}catch(NumberFormatException nfe){}
	        return value;
	    }
	};
	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		HttpSession session = request.getSession();
		String sortby = (String) request.getParameter("sortby");
		String view = (String) request.getParameter("view");
		List<Map<String,String>> reportViewMap = (List<Map<String,String>>)session.getAttribute("stocks");
		if(reportViewMap!=null && reportViewMap.size()>0){
			switch (sortby) {
			case "symbol":
				symbolComparator = Collections.reverseOrder(symbolComparator);
				Collections.sort(reportViewMap, symbolComparator);
				break;			
			case "qntty":
				currentQuantityComparator = Collections.reverseOrder(currentQuantityComparator);
				Collections.sort(reportViewMap, currentQuantityComparator);
				break;
			case "eavgprc":
				elevatedAveragePriceComparator = Collections.reverseOrder(elevatedAveragePriceComparator);
				Collections.sort(reportViewMap, elevatedAveragePriceComparator);
				break;
			case "avgprc":
				averagePriceComparator = Collections.reverseOrder(averagePriceComparator);
				Collections.sort(reportViewMap, averagePriceComparator);
				break;
			case "cstval":
				costValueComparator = Collections.reverseOrder(costValueComparator);
				Collections.sort(reportViewMap, costValueComparator);
				break;
			case "lstprc":
				lastPriceComparator = Collections.reverseOrder(lastPriceComparator);
				Collections.sort(reportViewMap, lastPriceComparator);
				break;
			case "crrval":
				currentValueComparator = Collections.reverseOrder(currentValueComparator);
				Collections.sort(reportViewMap, currentValueComparator);
				break;
			case "losamt":
				lossAmountComparator = Collections.reverseOrder(lossAmountComparator);
				Collections.sort(reportViewMap, lossAmountComparator);
				break;
			case "lospnt":
				lossPercentComparator = Collections.reverseOrder(lossPercentComparator);
				Collections.sort(reportViewMap, lossPercentComparator);
				break;
			case "pftamt":
				profitAmountComparator = Collections.reverseOrder(profitAmountComparator);
				Collections.sort(reportViewMap, profitAmountComparator);
				break;
			case "pftpnt":
				profitPercentComparator = Collections.reverseOrder(profitPercentComparator);
				Collections.sort(reportViewMap, profitPercentComparator);
				break;
			case "navgprc":
				nextAveragePriceComparator = Collections.reverseOrder(nextAveragePriceComparator);
				Collections.sort(reportViewMap, nextAveragePriceComparator);
				break;
			case "navgcst":
				nextCostValueComparator = Collections.reverseOrder(nextCostValueComparator);
				Collections.sort(reportViewMap, nextCostValueComparator);
				break;
			case "nxtsll":
				nextSellComparator = Collections.reverseOrder(nextSellComparator);
				Collections.sort(reportViewMap, nextSellComparator);
				break;
			}
		}
		if("home".equals(view)){
			RequestDispatcher rd = request.getRequestDispatcher("home");
			rd.forward(request, response);
		}
		if("viewgains".equals(view)){
			RequestDispatcher rd = request.getRequestDispatcher("viewgains");
			rd.forward(request, response);
		}
	}
}
