package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.service.BuySellStock;
import com.technowlogeek.portfolio.vo.Order;
import com.technowlogeek.portfolio.vo.Stock;

@WebServlet(urlPatterns = "/buysellStock")
public class BuySellController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		StringBuilder sb = new StringBuilder();
		Stock formData = new Stock();
		int lastQty = 0;
		int qty = 0;
		double price = 0.0;
		Timestamp datetime = null;
		
		String value = request.getParameter("exchange");
		if(value!=null && !"".equals(value)){
			formData.setExchange(value.toUpperCase());
		}else{
			sb.append("Please provide proper exchange name. ");
		}
		
		value = request.getParameter("symbol");
		if(value!=null && !"".equals(value.toUpperCase())){
			formData.setSymbol(value);
		}else{
			sb.append("Please provide proper symbol name. ");
		}
		
		value = request.getParameter("stockName");
		if(value!=null && !"".equals(value)){
			formData.setName(value);
		}else{
			sb.append("Please provide proper name. ");
		}
		
		value = request.getParameter("qty");
		if(value!=null && !"".equals(value)){
			try{
				qty = Integer.parseInt(value);
			}catch(NumberFormatException nfe){}
			if(qty<1){
				sb.append("Please provide positive integer for quantity. ");
			}
		}else{
			sb.append("Please provide proper quantity. ");
		}
		
		value = request.getParameter("last_qty");
		if(value!=null && !"".equals(value)){
			try{
				lastQty = Integer.parseInt(value);
				formData.setCurrentQuantity(lastQty);
			}catch(NumberFormatException nfe){}
		}
		
		formData.setNotes(request.getParameter("notes"));
		
		String req = request.getParameter("submit");
		Stock stock = (Stock)session.getAttribute("stock");
		if(stock!=null && stock.getExchange()!=null && stock.getExchange().equals(formData.getExchange()) && stock.getSymbol()!=null && stock.getSymbol().equals(formData.getSymbol())){
			if("BUY".equals(req)){
				value = request.getParameter("price");
				if(value!=null && !"".equals(value)){
					try{
						price = Double.parseDouble(value);
					}catch(NumberFormatException nfe){}
					if(price>0){
						formData.setLastPrice(price);
					}else{
						sb.append("Please provide positive number for price. ");
					}
				}else{
					sb.append("Please provide proper price. ");
				}
				
				value = request.getParameter("datetime");
				if(value!=null && !"".equals(value.toUpperCase())){
					try{
						datetime = Timestamp.valueOf(value);
					}catch(Exception e){
						e.printStackTrace();
						sb.append("Date and time format is not proper. ");
					}
				}else{
					sb.append("Please provide proper date and time. ");
				}
				if(sb.length()<1){
					Order order = new Order();
					order.setPrice(price);
					order.setQuantity(qty);
					order.setDatetime(datetime);
					stock.setNotes(formData.getNotes());
					try {
						if(BuySellStock.buyStock(stock,order)){
							sb.append("Buy successful "+qty+" quantity of "+formData.getSymbol());
							stock.setCurrentQuantity(lastQty+qty);
							request.setAttribute("stock", stock);
						}else{
							sb.append("Stock buy failed "+formData.getSymbol());
						}
					} catch (PortfolioException pe) {
						sb.append(pe.getMessage());
					}
					request.setAttribute("response", sb.toString());
				}else{
					request.setAttribute("qty", qty);
					request.setAttribute("price", price);
					request.setAttribute("datetime", datetime);
					request.setAttribute("response", sb.toString());
				}
				session.setAttribute("stock", stock);
			}else if("SELL".equals(req)){
				if(sb.length()<1){
					try {
						stock = (Stock)session.getAttribute("stock");
						if(stock.getCurrentQuantity()<qty){
							throw new PortfolioException("Cannot sell more than available quantity. ");
						}
						if(BuySellStock.sellStock(stock,qty)){
							sb.append("Sell successful "+qty+" quantity of "+formData.getSymbol());
							stock.setCurrentQuantity(lastQty-qty);
							session.setAttribute("stock", stock);
						}else{
							sb.append("Stock sell failed "+formData.getSymbol());
						}
					} catch (PortfolioException pe) {
						sb.append(pe.getMessage());
					}
					request.setAttribute("response", sb.toString());
				}else{
					request.setAttribute("response", sb.toString());
				}
			}else{
				request.setAttribute("response", "Processing failed. Please check input and your network connection.");
			}
		}else{
			request.setAttribute("response", "Something went wrong in server, please try again.");
		}
		request.setAttribute("lastQty", lastQty);
		request.setAttribute("qty", qty);
		request.setAttribute("price", price);
		request.setAttribute("datetime", datetime);
		request.setAttribute("req", (req!=null?req.toLowerCase():"buy"));
		RequestDispatcher rd = request.getRequestDispatcher("/buysell");
		rd.forward(request, response);
	}
}
