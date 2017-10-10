package com.technowlogeek.portfolio.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.service.GoogleFinance;
import com.technowlogeek.portfolio.vo.Order;
import com.technowlogeek.portfolio.vo.Stock;

public class Util {
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		try {
			BigDecimal bd = new BigDecimal(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.getMessage());
		}
		return 0.0;
	}

	public static String getValue(String val) {
		if ("BSE".equals(val)) {
			return "BOM";
		}
		return val;
	}
	
	public static String getEnvironment() {
		String val = "local";
		//String val = "openshift";
		return val;
	}

	public static Set<Order> getOrderSet(String ordersStr) {
		final Set<Order> orderSet = new TreeSet<Order>();
		if(ordersStr!=null && !"".equals(ordersStr)){
			try {
	
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
	
				DefaultHandler handler = new DefaultHandler() {
	
					Order order = null;
					boolean price = false;
					boolean quantity = false;
					boolean datetime = false;
	
					public void startElement(String uri, String localName,
							String qName, Attributes attributes)
							throws SAXException {
						if ("order".equals(qName)) {
							order = new Order();
							order.setId(Long.parseLong(attributes.getValue(0)));
						}
	
						if (qName.equalsIgnoreCase("price")) {
							price = true;
						}
	
						if (qName.equalsIgnoreCase("quantity")) {
							quantity = true;
						}
	
						if (qName.equalsIgnoreCase("datetime")) {
							datetime = true;
						}
					}
	
					public void endElement(String uri, String localName,
							String qName) throws SAXException {
	
						if ("order".equals(qName)) {
							orderSet.add(order);
						}
					}
	
					public void characters(char ch[], int start, int length)
							throws SAXException {
						if (price) {
							order.setPrice(Double.parseDouble(new String(ch, start, length)));
							price = false;
						}
	
						if (quantity) {
							order.setQuantity(Integer.parseInt(new String(ch, start, length)));
							quantity = false;
						}
	
						if (datetime) {
							order.setDatetime(Timestamp.valueOf(new String(ch, start, length)));
							datetime = false;
						}
					}
	
				};
	
				saxParser.parse(new InputSource(new StringReader(ordersStr)),
						handler);
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orderSet;
	}
	
	public static int getQuantity(String orderXml){
		int quantity = 0;
		if(orderXml!=null){
			Set<Order> orders = Util.getOrderSet(orderXml);
			for(Order order : orders){
				quantity = quantity + order.getQuantity();
			}
		}
		return quantity;
	}
	
	public static int getQuantity(Set<Order> orderSet){
		int quantity = 0;
		if(orderSet!=null){
			for(Order order : orderSet){
				quantity = quantity + order.getQuantity();
			}
		}
		return quantity;
	}
	
	public static double getAveragePrice(Set<Order> orderSet){
		double avgPrice = 0.0;
		long totalQty = 0;
		if(orderSet!=null){
			for(Order order : orderSet){
				totalQty = totalQty + order.getQuantity();
				avgPrice = avgPrice + order.getQuantity()*order.getPrice();
			}
			if(avgPrice!=0 && totalQty!=0){
				avgPrice = round(avgPrice/totalQty,2);
			}
		}
		return avgPrice;
	}
	
	public static List<Stock> getGoogleUpdate() throws PortfolioException{
		List<Stock> stocks = null;
		List<Map<String,StringBuilder>> symbolsList = new ArrayList<Map<String,StringBuilder>>();
		Map<String, StringBuilder> symbols = new HashMap<String, StringBuilder>();
		Map<String, Stock> currentStocks = new HashMap<String, Stock>();
		Map<String, Stock> bomStocks = new HashMap<String, Stock>();
		try {
			stocks = PortfolioDAO.getAllStocks();
			if(stocks!=null){
				int count = 0;
				for (Stock stock : stocks) {
					count++;
					if(count>100){
						symbolsList.add(symbols);
						symbols = new HashMap<String, StringBuilder>();
						count=0;
					}
					stock.setCurrentQuantity(Util.getQuantity(stock.getOrders()));
					StringBuilder exchange = symbols.get(stock.getExchange());
					String symbol = null;
					if (exchange != null) {
						if("BOM".equals(stock.getExchange())||"BSE".equals(stock.getExchange())){
							exchange.append(",");
							exchange.append(Util.getValue(stock.getExchange()));
							exchange.append(":");
							symbol = stock.getSymbolId();
							exchange.append(symbol);
							bomStocks.put(Util.getValue(
									stock.getExchange()) + ":" + symbol, stock);
						}else{
							exchange.append(",");
							exchange.append(Util.getValue(stock.getExchange()));
							exchange.append(":");
							symbol = stock.getSymbol();
							exchange.append(symbol);
							currentStocks.put(Util.getValue(
									stock.getExchange()) + ":" + symbol, stock);
						}
					} else {
						if("BOM".equals(stock.getExchange())||"BSE".equals(stock.getExchange())){
							symbol = stock.getSymbolId();
							symbols.put(stock.getExchange(),
									new StringBuilder(Util.getValue(stock.getExchange())+":"+symbol));
							bomStocks.put(Util.getValue(
									stock.getExchange()) + ":" + symbol, stock);
						}else{
							symbol = stock.getSymbol();
							symbols.put(stock.getExchange(),
								new StringBuilder(Util.getValue(stock.getExchange())+":"+symbol));
							currentStocks.put(Util.getValue(
									stock.getExchange()) + ":" + symbol, stock);
						}
					}
				}
				if(symbols.size()>0){
					symbolsList.add(symbols);
				}
				for(Map<String,StringBuilder> sym : symbolsList){
					for (Map.Entry<String, StringBuilder> stocksEntry : sym.entrySet()) {
						List<Stock> googleStocks = GoogleFinance
								.getStockQuote(stocksEntry.getValue().toString());
						if(googleStocks!=null){
							for (Stock stock : googleStocks) {
								Stock currentStock = null;
								if("BOM".equals(stock.getExchange())||"BSE".equals(stock.getExchange())){
									currentStock = bomStocks.get(stock.getExchange()
											+ ":" + stock.getSymbol());
								}else{
									currentStock = currentStocks.get(stock.getExchange()
										+ ":" + stock.getSymbol());
								}
								if(currentStock==null){
									throw new PortfolioException(stock.getExchange()
											+ ":" + stock.getSymbol()+" Problem updating portfolio.");
								}
								if(currentStock.getElevatedAveragePrice()<stock.getLastPrice()){
									Set<Order> orderSet = stock.getOrders();
									Order secondLastOrder = null;
									Order lastOrder = null;
									if(orderSet!=null){
										Iterator<Order> itr = orderSet.iterator();
										while(itr.hasNext()){
											secondLastOrder = lastOrder;
											lastOrder = itr.next();
										}
									}
									if(secondLastOrder==null){
										currentStock.setElevatedAveragePrice(stock.getLastPrice());
									}else if(secondLastOrder.getPrice()<stock.getLastPrice()){
										currentStock.setElevatedAveragePrice(stock.getLastPrice());
									}
								}
								currentStock.setLastPrice(stock.getLastPrice());
								currentStock.setLastTradeDateTime(stock.getLastTradeDateTime());
							}
						}else{
							throw new PortfolioException("Unable to fetch data from Google Finance.");
						}
					}
				}
			}
		} catch (PortfolioException pe) {
			throw pe;
		}
		return stocks;
	}
	
	/*public static double getInitialPrice(String orderXml){
		double initialPrice = 0.0;
		List<Map<String,String>> orders = Util.parseOrders(orderXml);
		long initial = 0;
		Map<String,String> initialOrder = null;
		if(orders.size()>1){
			try{
				for(Map<String,String> order : orders){
					long temp = Long.parseLong(order.get("id"));
					if(initial < temp){
						if(initial == 0){
							initialOrder = order;
							initial = temp;
						}
						continue;
					 }else{
						 initialOrder = order;
					 }
				}
				initialPrice = (initialOrder!=null?Double.parseDouble(initialOrder.get("price")):0.0);
			}catch(NumberFormatException nfe){}
		}else{
			if(orders.size()>=1){
				initialPrice = (orders.get(0).get("price")!=null?Double.parseDouble(orders.get(0).get("price")):0.0);
			}
		}
		return initialPrice;
	}
	
	public static double getLatestPrice(String orderXml){
		double latestPrice = 0.0;
		List<Map<String,String>> orders = Util.parseOrders(orderXml);
		long latest = 0;
		Map<String,String> latestOrder = null;
		if(orders.size()>1){
			try{
				for(Map<String,String> order : orders){
					long temp = Long.parseLong(order.get("id"));
					if(latest < temp){
						latestOrder = order;
						latest = temp;
					 }else{
						 latestOrder = order;
					 }
				}
				latestPrice = (latestOrder!=null?Double.parseDouble(latestOrder.get("price")):0.0);
			}catch(NumberFormatException nfe){}
		}else{
			if(orders.size()>=1){
				latestPrice = (orders.get(0).get("price")!=null?Double.parseDouble(orders.get(0).get("price")):0.0);
			}
		}
		return latestPrice;
	}
	
	public static double getSecondLatestPrice(String orderXml){
		double secondLatestPrice = 0.0;
		List<Map<String,String>> orders = Util.parseOrders(orderXml);
		long latest = 0;
		long secondLatest = 0;
		Map<String,String> latestOrder = null;
		Map<String,String> secondLatestOrder = null;
		if(orders.size()>1){
			try{
				for(Map<String,String> order : orders){
					long temp = Long.parseLong(order.get("id"));
					if(latest < temp){
						secondLatestOrder = latestOrder;
						latestOrder = order;
						secondLatest = latest;
						latest = temp;
					 }else if(secondLatest < temp){
						 secondLatestOrder = order;
					 }
				}
				secondLatestPrice = (secondLatestOrder!=null?Double.parseDouble(secondLatestOrder.get("price")):0.0);
			}catch(NumberFormatException nfe){}
		}
		return secondLatestPrice;
	}*/
	
	public static String getOrdersXmlString(Set<Order> orderSet){
		StringBuilder xmlString = new StringBuilder();
		if(orderSet!=null){
			xmlString.append("<orders>");
			for(Order order : orderSet){
				xmlString.append("<order id='"+order.getId()+"'>");
				xmlString.append("<price>"+order.getPrice()+"</price>");
				xmlString.append("<quantity>"+order.getQuantity()+"</quantity>");
				xmlString.append("<datetime>"+order.getDatetime().toString()+"</datetime>");
				xmlString.append("</order>");
			}
			xmlString.append("</orders>");
		}
		return xmlString.toString();
	} 
}
