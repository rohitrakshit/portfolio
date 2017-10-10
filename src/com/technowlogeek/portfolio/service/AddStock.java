package com.technowlogeek.portfolio.service;

import java.util.List;
import java.util.Scanner;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.vo.Stock;

public class AddStock {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter stock Exchange: ");
		String exchange = sc.nextLine();
		System.out.println("Please enter stock Symbol: ");
		String symbol = sc.nextLine();
		List<Stock> stockList = GoogleFinance.getStockQuote(exchange + ":"
				+ symbol);
		Stock stock = (stockList != null ? stockList.get(0) : new Stock());

		System.out.println("Please enter stock Name: ");
		stock.setName(sc.nextLine());
		System.out.println("Please enter quantity: ");
		stock.setCurrentQuantity(Integer.parseInt(sc.nextLine()));
		System.out.println("Please enter average cost price: ");
		stock.setAveragePrice(Double.parseDouble(sc.nextLine()));
		try {
			if(PortfolioDAO.addStock(stock)){
				System.out.println("Add stock successful for "+stock.getSymbol());
			}else{
				System.out.println("Add stock failed "+stock.getSymbol());
			}
		} catch (PortfolioException pe) {
			pe.printStackTrace();
		}
	}

}
