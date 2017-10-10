package com.technowlogeek.portfolio.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Order;
import com.technowlogeek.portfolio.vo.Stock;

public class ViewStock {
	public static Object[] getAllStocks(){
		try{
			return processStocksForHomeView(PortfolioDAO.getAllStocks());
		}catch(PortfolioException pe){
			pe.printStackTrace();
		}
		return null;
	}
	
	public static Object[] getAllGainStocks() {
		try{
			return processAllGainStocks(PortfolioDAO.getAllStocks());
		}catch(PortfolioException pe){
			pe.printStackTrace();
		}
		return null;
	}

	public static Object[] processStocksForHomeView(List<Stock> stocks) {
		List<Map<String,String>> stockReportMapList = null;
		try{
			Object[] values = new Object[5];
			if(stocks!=null){
				stockReportMapList = new ArrayList<Map<String,String>>();
				double totalCompanyCount = 0;
				double totalStockCount = 0;
				double totalCostValue = 0.0;
				double totalElevatedCostValue = 0.0;
				double totalCurrentValue = 0.0;
				double totalNextAverageCost = 0.0;
				for(Stock stock : stocks){
					Set<Order> orderSet  = stock.getOrders();
					stock.setCurrentQuantity(Util.getQuantity(orderSet));
					Map<String,String> stockReportMap = new HashMap<String,String>();
					double costValue = 0.0;
					double eap = stock.getElevatedAveragePrice();
					double elevatedCostValue = 0.0;
					double currentValue = 0.0;
					double lossAmount = 0.0;
					double nextAveragePrice = 0.0;
					int currQty = stock.getCurrentQuantity();

					stockReportMap.put("exchange", stock.getExchange());
					stockReportMap.put("symbol", stock.getSymbol());
					stockReportMap.put("name", stock.getName());
					stockReportMap.put("currentQuantity", String.valueOf(currQty));
					stockReportMap.put("averagePrice", String.valueOf(stock.getAveragePrice()));
					stockReportMap.put("elevatedAveragePrice", String.valueOf(eap));
					costValue = (currQty>0?stock.getAveragePrice()*currQty:0);
					stockReportMap.put("costValue",String.valueOf(Util.round(costValue,2)));
					totalCostValue = totalCostValue+costValue;
					elevatedCostValue = (currQty>0?eap*currQty:0);
					stockReportMap.put("elevatedCostValue",String.valueOf(Util.round(elevatedCostValue,2)));
					totalElevatedCostValue = totalElevatedCostValue+elevatedCostValue;
					stockReportMap.put("lastPrice",String.valueOf(Util.round(stock.getLastPrice(),2)));
					currentValue = (currQty>0?stock.getLastPrice()*currQty:0);
					stockReportMap.put("currentValue",String.valueOf(Util.round(currentValue,2)));
					totalCurrentValue = totalCurrentValue+currentValue;
					lossAmount = currentValue-elevatedCostValue;
					stockReportMap.put("lossAmount",String.valueOf(Util.round(lossAmount,2)));
					stockReportMap.put("lossPercent",String.valueOf((elevatedCostValue!=0.0?Util.round(((lossAmount/elevatedCostValue)*100),2):0)));
					if(currQty==0){
						stockReportMap.put("lossPercent",String.valueOf((eap!=0.0?Util.round((((stock.getLastPrice()-eap)/eap)*100),2):0)));
					}
					
					int count = 0;
					int nextAvgPercent = 10;
					double initialPrice = 0.0;
					double latestPrice = 0.0;
					double secondLatestPrice = 0.0;
					Iterator<Order> itr = orderSet.iterator();
					while(itr.hasNext()){
						double price = itr.next().getPrice();
						if(count==0){
							initialPrice = price;
						}else{
							nextAvgPercent = nextAvgPercent + 2;
						}
						secondLatestPrice = latestPrice;
						latestPrice = price;
						count++;
					}
					
					if(eap > initialPrice){
						nextAveragePrice = eap-(eap*nextAvgPercent/100);
					}else{
						nextAveragePrice = latestPrice-(latestPrice*nextAvgPercent/100);
					}
					stockReportMap.put("nextAveragePrice",String.valueOf(Util.round(nextAveragePrice,2)));
					
					stockReportMap.put("nextCostValue",String.valueOf(Util.round((currQty>0?nextAveragePrice*currQty:0),2)));
					totalNextAverageCost = totalNextAverageCost + ((currQty>0?nextAveragePrice*currQty:0));
					
					
					stockReportMap.put("nextsell", String.valueOf(Util.round((secondLatestPrice!=0.0?secondLatestPrice:latestPrice+(latestPrice*15/100)),2)));
					stockReportMap.put("nextSellDesc", prepareNextSellHtml(stock.getOrders()));
					
					stockReportMap.put("notes", stock.getNotes());
					stockReportMap.put("lastTrade", stock.getLastTradeDateTime().toString());
					
					stockReportMap.put("actionHtml", prepareActionHtml(stock));
					
					stockReportMapList.add(stockReportMap);
					totalCompanyCount++;
					totalStockCount = totalStockCount + stock.getCurrentQuantity();
				}
				Map<String,Double> totalValues = new HashMap<String,Double>();
				totalValues.put("totalCompanyCount", totalCompanyCount);
				totalValues.put("totalStockCount", totalStockCount);
				totalValues.put("totalCostValue", Util.round(totalCostValue,2));
				totalValues.put("totalElevatedCostValue",Util.round(totalElevatedCostValue,2));
				totalValues.put("totalCurrentValue", Util.round(totalCurrentValue,2));
				double totalLossAmount = totalCurrentValue - totalCostValue;
				double totalElevatedLossAmount = totalCurrentValue - totalElevatedCostValue;
				totalValues.put("totalLossAmount", Util.round(totalLossAmount,2));
				totalValues.put("totalElevatedLossAmount", Util.round(totalElevatedLossAmount,2));
				totalValues.put("totalNextAverageCost", Util.round(totalNextAverageCost,2));
				totalValues.put("totalLossPercent",Util.round((totalLossAmount/totalCostValue)*100,2));
				totalValues.put("totalElevatedLossPercent",Util.round((totalElevatedLossAmount/totalElevatedCostValue)*100,2));
				values[0] = stockReportMapList;
				values[1] = totalValues;
				List<Map<String,Double>> chartDataset = generateCharts(stocks);
				values[2] = chartDataset.get(0);
				values[3] = chartDataset.get(1);
				values[4] = chartDataset.get(2);
			}
			return values;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object[] processAllGainStocks(List<Stock> stocks) {
		List<Map<String,String>> stockReportMapList = null;
		try{
			Object[] values = new Object[2];
			if(stocks!=null){
				stockReportMapList = new ArrayList<Map<String,String>>();
				double totalCompanyCount = 0;
				double totalStockCount = 0;
				double totalCostValue = 0.0;
				double totalCurrentValue = 0.0;
				double totalProfitAmount = 0.0;
				for(Stock stock : stocks){
					Set<Order> orderSet  = stock.getOrders();
					stock.setCurrentQuantity(Util.getQuantity(orderSet));
					if(stock.getLastPrice()>stock.getAveragePrice() && stock.getCurrentQuantity()>0){
						Map<String,String> stockReportMap = new HashMap<String,String>();
						double costValue = 0.0;
						double currentValue = 0.0;
						double profitAmount = 0.0;
						int currQty = stock.getCurrentQuantity();
	
						stockReportMap.put("exchange", stock.getExchange());
						stockReportMap.put("symbol", stock.getSymbol());
						stockReportMap.put("name", stock.getName());
						stockReportMap.put("currentQuantity", String.valueOf(currQty));
						stockReportMap.put("averagePrice", String.valueOf(stock.getAveragePrice()));
						costValue = Util.round((currQty>0?stock.getAveragePrice()*currQty:0),2);
						stockReportMap.put("costValue",String.valueOf(costValue));
						totalCostValue = totalCostValue+costValue;
						stockReportMap.put("lastPrice",String.valueOf(Util.round(stock.getLastPrice(),2)));
						currentValue = Util.round((currQty>0?stock.getLastPrice()*currQty:0),2);
						stockReportMap.put("currentValue",String.valueOf(currentValue));
						totalCurrentValue = totalCurrentValue+currentValue;
						profitAmount = Util.round(currentValue-costValue,2);
						stockReportMap.put("profitAmount",String.valueOf(profitAmount));
						stockReportMap.put("profitPercent",String.valueOf((costValue!=0.0?Util.round(((profitAmount/costValue)*100),2):0)));
						
						double latestPrice = 0.0;
						double secondLatestPrice = 0.0;
						Iterator<Order> itr = orderSet.iterator();
						while(itr.hasNext()){
							secondLatestPrice = latestPrice;
							latestPrice = itr.next().getPrice();
						}
						stockReportMap.put("nextsell", String.valueOf(Util.round((secondLatestPrice!=0.0?secondLatestPrice:latestPrice+(latestPrice*15/100)),2)));
						
						stockReportMap.put("notes", stock.getNotes());
						stockReportMap.put("lastTrade", stock.getLastTradeDateTime().toString());
						stockReportMap.put("actionHtml", prepareActionHtml(stock));
						stockReportMapList.add(stockReportMap);
						
						totalCompanyCount++;
						totalStockCount = totalStockCount + stock.getCurrentQuantity();
					}
				}
				Map<String,Double> totalValues = new HashMap<String,Double>();
				totalValues.put("totalCompanyCount", totalCompanyCount);
				totalValues.put("totalStockCount", totalStockCount);
				totalValues.put("totalCostValue", Util.round(totalCostValue,2));
				totalValues.put("totalCurrentValue", Util.round(totalCurrentValue,2));
				totalProfitAmount = totalCurrentValue - totalCostValue;
				totalValues.put("totalProfitAmount", Util.round(totalProfitAmount,2));
				totalValues.put("totalProfitPercent",Util.round((totalProfitAmount/totalCostValue)*100,2));
				values[0] = stockReportMapList;
				values[1] = totalValues;
			}
			return values;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static String prepareActionHtml(Stock stock){
		StringBuilder sb = new StringBuilder();
		sb.append("<strong>");
		sb.append(stock.getExchange());
		sb.append(":");
		sb.append(stock.getSymbol());
		sb.append(" (");
		sb.append((stock.getName()!=null||!"".equals(stock.getName())?stock.getName():stock.getSymbol()));
		sb.append(")</strong><br/>");
		sb.append((stock.getSector()!=null?stock.getSector():"")+" "+(stock.getIndustry()!=null?stock.getIndustry():"")+"<br/>");
		sb.append("<a href='editStock?req=edit&exchange=");
		sb.append(stock.getExchange());
		sb.append("&symbol=");
		sb.append((stock.getSymbol()!=null?stock.getSymbol().replaceAll("&", "%26"):""));
		sb.append("'><font size='10'>Edit</font></a> &nbsp; | &nbsp; <a href='editStock?req=buy&exchange=");
		sb.append(stock.getExchange());
		sb.append("&symbol=");
		sb.append((stock.getSymbol()!=null?stock.getSymbol().replaceAll("&", "%26"):""));
		sb.append("'><font size='10'>Buy</font></a> &nbsp; | &nbsp; <a href='editStock?req=sell&exchange=");
		sb.append(stock.getExchange());
		sb.append("&symbol=");
		sb.append((stock.getSymbol()!=null?stock.getSymbol().replaceAll("&", "%26"):""));
		sb.append("'><font size='10'>Sell</font></a>");
		return sb.toString();
	}
	
	private static String prepareNextSellHtml(Set<Order> orderSet){
		StringBuilder sb = new StringBuilder();
		if(orderSet!=null){
			for(Order order : orderSet){
				sb.append("<p>");
				sb.append("Bought ");
				sb.append(order.getQuantity());
				sb.append(" @ ");
				sb.append(order.getPrice());
				sb.append(" on ");
				Timestamp ts = order.getDatetime();
				sb.append(ts.toLocaleString());//ts.getDate()+"-"+ts.getMonth()+"-"+ts.getYear()+" "+ts.getHours()+" Hours");
				sb.append("</p>");
			}
		}
		return sb.toString();
	}
	
	private static List<Map<String,Double>> generateCharts(List<Stock> stocks) {
		List<Map<String,Double>> chartDataset = new ArrayList<Map<String,Double>>();
		if (stocks != null && stocks.size() > 0) {
			try {
				Map<String, Double> allocationMap = new HashMap<String, Double>();
				for (Stock stock : stocks) {
					Object sector = allocationMap.get(stock.getSector());
					int qty = stock.getCurrentQuantity();
					double price = stock.getAveragePrice();
					if (stock.getSector() != null
							&& !"".equals(stock.getSector()) && qty > 0
							&& price > 0.0) {
						if (sector != null) {
							allocationMap.put(replaceAmp(stock.getSector()),
									(Double) sector + price * qty);
						} else {
							allocationMap.put(stock.getSector(), price * qty);
						}
					}
				}
				chartDataset.add(allocationMap);
				
				allocationMap = new HashMap<String, Double>();
				for (Stock stock : stocks) {
					Object sector = allocationMap.get(stock.getIndustry());
					int qty = stock.getCurrentQuantity();
					double price = stock.getAveragePrice();
					if (stock.getIndustry() != null
							&& !"".equals(stock.getIndustry()) && qty > 0
							&& price > 0.0) {
						if (sector != null) {
							allocationMap.put(replaceAmp(stock.getIndustry()),
									(Double) sector + price * qty);
						} else {
							allocationMap.put(stock.getIndustry(), price * qty);
						}
					}
				}
				chartDataset.add(allocationMap);


				allocationMap = new HashMap<String, Double>();
				for (Stock stock : stocks) {
					int qty = stock.getCurrentQuantity();
					double price = stock.getAveragePrice();
					if (qty > 0 && price > 0.0) {
						allocationMap.put(replaceAmp(stock.getSymbol()), qty * price);
					}
				}
				chartDataset.add(allocationMap);
			} catch (Exception e) {
			}
		}
		return chartDataset;
	}
	
	private static String replaceAmp(String str){
		return str.replaceAll("&", "&amp;");
	}
}
