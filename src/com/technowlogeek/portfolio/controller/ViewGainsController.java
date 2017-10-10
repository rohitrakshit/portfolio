package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.technowlogeek.portfolio.service.ViewStock;

@WebServlet(urlPatterns = "/viewGains")
public class ViewGainsController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("stocks");
		Object[] values = ViewStock.getAllGainStocks();
		List<Map<String,String>> stocks = (List<Map<String,String>>)values[0];
		Map<String,Double> totalValues = (Map<String,Double>)values[1];
		session.setAttribute("stocks", stocks);
		session.setAttribute("totalValues", totalValues);
		RequestDispatcher rd = request.getRequestDispatcher("/viewgains");
		rd.forward(request, response);
	}
}
