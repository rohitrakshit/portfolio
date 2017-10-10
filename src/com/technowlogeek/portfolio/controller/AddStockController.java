package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.service.GoogleFinance;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Order;
import com.technowlogeek.portfolio.vo.Stock;

@WebServlet(urlPatterns = "/addStock")
public class AddStockController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		StringBuilder sb = new StringBuilder();
		String exchange = request.getParameter("exchange");
		String symbol = request.getParameter("symbol");
		Stock formData = new Stock();
		formData.setExchange(exchange);
		formData.setSymbol(symbol);
		String value = request.getParameter("symbol_id");
		formData.setSymbolId(((value!=null&&!"".equals(value))?value:""));
		value = request.getParameter("name");
		formData.setName(((value!=null&&!"".equals(value))?value:""));
		value = request.getParameter("group");
		formData.setGroup(((value!=null&&!"".equals(value)&&value.length()<3)?value:""));
		value = request.getParameter("sector");
		formData.setSector(((value!=null&&!"".equals(value))?value:""));
		value = request.getParameter("industry");
		formData.setIndustry(((value!=null&&!"".equals(value))?value:""));
		value = request.getParameter("notes");
		formData.setNotes(((value!=null&&!"".equals(value))?value:""));
		
		if(exchange==null || exchange.length()<2){
			sb.append("Please provide proper exchange name. ");
		}
		if(symbol==null || symbol.length()<2){
			sb.append("Please provide proper symbol name. ");
		}
		if(sb.length()==0){
			List<Stock> stockList = null;
			try{
				stockList = GoogleFinance.getStockQuote(Util.getValue(exchange) + ":"
					+ symbol);
			}catch(Exception e){
				
			}
			if(stockList!=null && stockList.get(0)!=null){
				Stock stock = stockList.get(0);
				stock.setExchange(exchange.toUpperCase());
				stock.setSymbol(symbol.toUpperCase());
				stock.setName(formData.getName());
				stock.setCurrentQuantity(formData.getCurrentQuantity());
				stock.setAveragePrice(stock.getLastPrice());
				stock.setGroup(formData.getGroup());
				stock.setSector(formData.getSector());
				stock.setIndustry(formData.getIndustry());
				stock.setOrders(new TreeSet<Order>());
				stock.setNotes(formData.getNotes());
				if(stock.getLastPrice()>stock.getAveragePrice()){
					stock.setElevatedAveragePrice(stock.getLastPrice());
				}else{
					stock.setElevatedAveragePrice(stock.getAveragePrice());
				}
				try {
					if(PortfolioDAO.addStock(stock)){
						sb.append("Add stock successful for "+stock.getSymbol());
					}else{
						sb.append("Add stock failed "+stock.getSymbol());
						request.setAttribute("addStock", stock);
					}
				} catch (PortfolioException pe) {
					sb.append(pe.getMessage());
				}
				RequestDispatcher rd = request.getRequestDispatcher("addstock");
				request.setAttribute("response", sb.toString());
				rd.forward(request, response);
			}else{
				sb.append("Stock fetch failed from google. Please check input and your network connection.");
				RequestDispatcher rd = request.getRequestDispatcher("addstock");
				request.setAttribute("addStock", formData);
				request.setAttribute("response", sb.toString());
				rd.forward(request, response);
			}
		}else{
			RequestDispatcher rd = request.getRequestDispatcher("addstock");
			request.setAttribute("addStock", formData);
			request.setAttribute("response", sb.toString());
			rd.forward(request, response);
		}
	}
}
