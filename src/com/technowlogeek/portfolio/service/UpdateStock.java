package com.technowlogeek.portfolio.service;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.vo.Stock;

public class UpdateStock {

	public static boolean updateStock(Stock stock) throws PortfolioException{
		return PortfolioDAO.updateStock(stock);
	}

	public static String deleteStock(String exchange, String symbol) {
		try{
			return PortfolioDAO.deleteStock(exchange, symbol);
		}catch(PortfolioException pe){
			pe.printStackTrace();
		}
		return "Problem processing your request.";
	}
}
