package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.service.UpdatePortfolio;
import com.technowlogeek.portfolio.vo.Stock;

@WebServlet(urlPatterns = "/updatePortfolio")
public class UpdatePortfolioController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Stock> stocks = null;
		String view = request.getParameter("view");
		try {
			stocks = UpdatePortfolio.updatePortfolio();
			//generateCharts(stocks);
		} catch (PortfolioException pe) {
			request.setAttribute("response",
					"Problem updating portfolio " + pe.getMessage());
		}
		if ("home".equals(view)) {
			RequestDispatcher rd = request.getRequestDispatcher("Home");
			rd.forward(request, response);
		}
		if ("viewgains".equals(view)) {
			RequestDispatcher rd = request.getRequestDispatcher("viewGains");
			rd.forward(request, response);
		}
	}

	/*private void generateCharts(List<Stock> stocks) {
		if (stocks != null && stocks.size() > 0) {
			try {
				Map<String, Double> allocationMap = new HashMap<String, Double>();
				for (Stock stock : stocks) {
					Object sector = allocationMap.get(stock.getIndustry());
					int qty = stock.getCurrentQuantity();
					double price = stock.getAveragePrice();
					if (stock.getIndustry() != null
							&& !"".equals(stock.getIndustry()) && qty > 0
							&& price > 0.0) {
						if (sector != null) {
							allocationMap.put(stock.getIndustry(),
									(Double) sector + price * qty);
						} else {
							allocationMap.put(stock.getIndustry(), price * qty);
						}
					}
				}

				DefaultPieDataset data = new DefaultPieDataset();
				for (Map.Entry<String, Double> alloc : allocationMap.entrySet()) {
					data.setValue(alloc.getKey(), alloc.getValue());
				}
				JFreeChart chart = ChartFactory.createPieChart(
						"Sector Wise Portfolio Allocation", data, true, true,
						false);
				ChartRenderingInfo info = new ChartRenderingInfo(
						new StandardEntityCollection());
				String path = getServletContext().getRealPath(".");
				File file1 = new File(path.substring(0, path.indexOf("Portfolio"))
						+ "/jsp/secallocpiechart.png");
				ChartUtilities.saveChartAsPNG(file1, chart, 900, 600, info);

				data = new DefaultPieDataset();
				for (Stock stock : stocks) {
					int qty = stock.getCurrentQuantity();
					double price = stock.getAveragePrice();
					if (qty > 0 && price > 0.0) {
						data.setValue(stock.getSymbol(), qty * price);
					}
				}
				chart = ChartFactory.createPieChart(
						"Company Wise Portfolio Allocation", data, true, true,
						false);

				info = new ChartRenderingInfo(new StandardEntityCollection());
				file1 = new File(getServletContext().getRealPath(".")
						+ "/jsp/comallocpiechart.png");
				ChartUtilities.saveChartAsPNG(file1, chart, 900, 600, info);

			} catch (Exception e) {
			}
		}
	}*/
}
