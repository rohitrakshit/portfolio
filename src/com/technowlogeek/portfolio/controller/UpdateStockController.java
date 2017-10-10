package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.service.GoogleFinance;
import com.technowlogeek.portfolio.service.UpdateStock;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Stock;

@WebServlet(urlPatterns = "/updateStock")
public class UpdateStockController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		Stock existingStock = (Stock)session.getAttribute("stock");
		StringBuilder sb = new StringBuilder();
		Stock formData = new Stock();
		String currExchange = request.getParameter("curr_exchange");
		String currSymbol = request.getParameter("curr_symbol");	
		formData.setExchange(request.getParameter("exchange"));
		if("DELETE".equals(formData.getExchange())){
			sb.append(UpdateStock.deleteStock(currExchange,currSymbol));
			RequestDispatcher rd = request.getRequestDispatcher("/editstock");
			request.setAttribute("response", sb.toString());
			rd.forward(request, response);
		}else{
			formData.setSymbol(request.getParameter("symbol"));
			String value = request.getParameter("symbol_id");
			formData.setSymbolId(((value!=null&&!"".equals(value))?value:""));
			value = request.getParameter("name");
			formData.setName((value!=null||!"".equals(value)?value:""));
			value = request.getParameter("group");
			formData.setGroup(((value!=null&&!"".equals(value)&&value.length()<3)?value:""));
			value = request.getParameter("sector");
			formData.setSector(((value!=null&&!"".equals(value))?value:""));
			value = request.getParameter("industry");
			formData.setIndustry(((value!=null&&!"".equals(value))?value:""));
			value = request.getParameter("notes");
			formData.setNotes(((value!=null&&!"".equals(value))?value:""));
			formData.setCurrentQuantity(Util.getQuantity(existingStock.getOrders()));
			
			if(formData.getExchange()==null || formData.getExchange().length()<2){
				sb.append("Please provide proper exchange name. ");
			}
			if(formData.getSymbol()==null || formData.getSymbol().length()<2){
				sb.append("Please provide proper symbol name. ");
			}
			if(formData.getSymbol()==null || formData.getSymbol().length()<2){
				sb.append("Please provide proper stock name. ");
			}
		
			if(sb.length()==0){
				List<Stock> stockList = null;
				try{
					stockList = GoogleFinance.getStockQuote(Util.getValue(formData.getExchange()) + ":"
						+ ("BSE".equals(formData.getExchange())?formData.getSymbolId():formData.getSymbol()));
				}catch(Exception e){
					
				}
				if(stockList!=null && stockList.get(0)!=null){
					Stock stock = stockList.get(0);
					stock.setExchange(formData.getExchange().toUpperCase());
					stock.setSymbol(formData.getSymbol().toUpperCase());
					stock.setSymbolId(formData.getSymbolId());
					stock.setName(formData.getName());
					stock.setCurrentQuantity(formData.getCurrentQuantity());
					stock.setAveragePrice(formData.getAveragePrice());
					stock.setGroup(formData.getGroup());
					stock.setSector(formData.getSector());
					stock.setIndustry(formData.getIndustry());
					stock.setNotes(formData.getNotes());
					
					value = request.getParameter("resetAvg");
					if("resetAvgPrice".equals(value)){
						stock.setElevatedAveragePrice(existingStock.getAveragePrice());
					}else{
						stock.setElevatedAveragePrice(existingStock.getElevatedAveragePrice());
					}
					stock.setOrders(existingStock.getOrders());
					
					try {
						if(UpdateStock.updateStock(stock)){
							sb.append("Stock update successful for "+stock.getSymbol());
							request.setAttribute("stock", stock);
						}else{
							sb.append("Stock update failed "+stock.getSymbol());
						}
					} catch (PortfolioException pe) {
						sb.append(pe.getMessage());
					}
					RequestDispatcher rd = request.getRequestDispatcher("/editstock");
					request.setAttribute("response", sb.toString());
					rd.forward(request, response);
				}else{
					request.setAttribute("stock", formData);
					sb.append("Stock fetch failed from google. Please check input and your network connection.");
					RequestDispatcher rd = request.getRequestDispatcher("/editstock");
					request.setAttribute("response", sb.toString());
					rd.forward(request, response);
				}
			}else{
				Stock original = new Stock();
				original.setExchange(currExchange);
				original.setSymbol(currSymbol);
				request.setAttribute("stock", original);
				RequestDispatcher rd = request.getRequestDispatcher("/editstock");
				request.setAttribute("response", sb.toString());
				rd.forward(request, response);
			}
		}
	}
}
