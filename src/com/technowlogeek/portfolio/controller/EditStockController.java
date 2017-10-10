package com.technowlogeek.portfolio.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.technowlogeek.portfolio.service.EditStock;
import com.technowlogeek.portfolio.vo.Stock;

@WebServlet(urlPatterns = "/editStock")
public class EditStockController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		StringBuilder sb = new StringBuilder();
		String exchange = request.getParameter("exchange");
		String symbol = request.getParameter("symbol");
		if(exchange==null || exchange.length()<1){
			sb.append("Please provide proper exchange value. ");
		}
		if(symbol==null || symbol.length()<1){
			sb.append("Please provide proper symbol value. ");
		}
		if(sb.length()==0){
			Stock stock = EditStock.getStock(exchange,symbol);
			session.setAttribute("stock", stock);
			if("edit".equals(request.getParameter("req"))){
				RequestDispatcher rd = request.getRequestDispatcher("editstock");
				rd.forward(request, response);
			}else if("buy".equals(request.getParameter("req"))){
				request.setAttribute("req", "buy");
				RequestDispatcher rd = request.getRequestDispatcher("buysell");
				rd.forward(request, response);
			}else if("sell".equals(request.getParameter("req"))){
				request.setAttribute("req", "sell");
				RequestDispatcher rd = request.getRequestDispatcher("buysell");
				rd.forward(request, response);
			}
		}else{
			request.setAttribute("response", sb.toString());
			RequestDispatcher rd = request.getRequestDispatcher("Home");
			rd.forward(request, response);
		}
	}
}
