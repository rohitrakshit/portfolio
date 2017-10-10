package com.technowlogeek.portfolio.service;

import java.util.HashMap;
import java.util.Map;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Stock;

public class EditStock {

	public static Stock getStock(String exchange, String symbol) {
		Stock stock = null;
		try {
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("exchange", exchange);
			queryMap.put("symbol", symbol);
			stock = PortfolioDAO.getStock(queryMap);
			stock.setCurrentQuantity(Util.getQuantity(stock.getOrders()));
		} catch (PortfolioException pe) {
			pe.printStackTrace();
		}
		return stock;
	}

}
