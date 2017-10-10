package com.technowlogeek.portfolio.service;

import java.util.List;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Stock;

public class UpdatePortfolio {

	public static List<Stock> updatePortfolio() throws PortfolioException{
		List<Stock> stocks = null;
		try{
			stocks = Util.getGoogleUpdate();
			if(!PortfolioDAO.updateAllStocks(stocks)){
				stocks.removeAll(stocks);
			}
		} catch (PortfolioException pe) {
			throw pe;
		}
		return stocks;
	}
}
