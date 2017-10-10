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

import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.service.UpdatePortfolio;
import com.technowlogeek.portfolio.service.ViewStock;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Stock;

@WebServlet(urlPatterns = "/showLatest")
public class ShowLatestController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);	
		String view = request.getParameter("view");
		RequestDispatcher rd = null;
		try {
			List<Stock> stocks = Util.getGoogleUpdate();
			if ("home".equals(view)) {
				Object[] values = ViewStock.processStocksForHomeView(stocks);
				List<Map<String,String>> stocksView = (List<Map<String,String>>)values[0];
				Map<String,Double> totalValues = (Map<String,Double>)values[1];
				Map<String,Double> sectorDataset = (Map<String,Double>)values[2];
				Map<String,Double> industryDataset = (Map<String,Double>)values[3];
				Map<String,Double> companyDataset = (Map<String,Double>)values[4];
				session.setAttribute("stocks", stocksView);
				session.setAttribute("totalValues", totalValues);
				session.setAttribute("sectorDataset", sectorDataset);
				session.setAttribute("industryDataset", industryDataset);
				session.setAttribute("companyDataset", companyDataset);
				rd = request.getRequestDispatcher("home");
			}
			if ("viewgains".equals(view)) {
				Object[] values = ViewStock.processAllGainStocks(stocks);
				List<Map<String,String>> stocksView = (List<Map<String,String>>)values[0];
				Map<String,Double> totalValues = (Map<String,Double>)values[1];
				session.setAttribute("stocks", stocksView);
				session.setAttribute("totalValues", totalValues);
				rd = request.getRequestDispatcher("viewgains");
			}
		} catch (PortfolioException pe) {
			request.setAttribute("response",
					"Problem getting latest update: " + pe.getMessage());
		}			
		rd.forward(request, response);
	}
}
