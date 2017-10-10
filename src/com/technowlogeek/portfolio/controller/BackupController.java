package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Stock;

@WebServlet(urlPatterns = "/backup")
public class BackupController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;
	
	private static final String DEFAULT_SEPARATOR = ",";
    private static final String DEFAULT_QUOTE = "\"";
    
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		try{
			List<Stock> stocks = PortfolioDAO.getAllStocks();
			sb.append(DEFAULT_QUOTE+"Id"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Exchange"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Symbol"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"SymbolId"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Name"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"ElevatedAveragePrice"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"LastPrice"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Group"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Sector"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Industry"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"LastTradeDateTime"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Orders"+DEFAULT_QUOTE+DEFAULT_SEPARATOR
					+DEFAULT_QUOTE+"Notes"+DEFAULT_QUOTE+";\n");
			if(stocks!=null){
				for(Stock stock : stocks){
					sb.append(DEFAULT_QUOTE+getValue(stock.getId())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getExchange())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getSymbol())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getSymbolId())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getName())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+stock.getElevatedAveragePrice()+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+stock.getLastPrice()+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getGroup())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getSector())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getIndustry())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getLastTradeDateTime().toString())+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					String orderStr = Util.getOrdersXmlString(stock.getOrders());
					orderStr = orderStr.substring(orderStr.indexOf("<orders>"), orderStr.length());
					sb.append(DEFAULT_QUOTE+orderStr.trim()+DEFAULT_QUOTE);
					sb.append(DEFAULT_SEPARATOR);
					sb.append(DEFAULT_QUOTE+getValue(stock.getNotes())+DEFAULT_QUOTE+';');
					sb.append("\n");
				}
			}
		}catch(PortfolioException pe){
			pe.printStackTrace();
		}
		response.setContentType("APPLICATION/OCTET-STREAM");   
		response.setHeader("Content-Disposition","attachment; filename=\"" + "portfolio_backup.csv" + "\"");   
		PrintWriter out = response.getWriter();             
		out.write(sb.toString());     
		out.close();     
	}
	
	private static String getValue(String str){
		if(str==null){
			str = "";
		}else{
			str = str.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n");
			str = str.replaceAll(",", "&#44;");
		}
		return str;
	}
}
