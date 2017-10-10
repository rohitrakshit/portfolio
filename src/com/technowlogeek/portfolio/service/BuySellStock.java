package com.technowlogeek.portfolio.service;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.vo.Order;
import com.technowlogeek.portfolio.vo.Stock;

public class BuySellStock {

	public static boolean buyStock(Stock existingStock, Order order) throws PortfolioException {
		Set<Order> orderSet = existingStock.getOrders();
		Iterator<Order> itr = orderSet.iterator();
		Order lastOrder = null;
		while(itr.hasNext()){
			lastOrder = itr.next();
		}
		if(existingStock.getElevatedAveragePrice()<order.getPrice()){
			existingStock.setElevatedAveragePrice(order.getPrice());
		}
		Timestamp datetime = null;
		if(lastOrder==null){
			int id = (existingStock.getExchange()+existingStock.getSymbol()).hashCode();
			order.setId((id>0?id:(id *= -1)));
			existingStock.getOrders().add(order);
		}else{
			order.setId(lastOrder.getId()+1);
			datetime = lastOrder.getDatetime();
			if(datetime!=null && datetime.before(order.getDatetime())){
				existingStock.getOrders().add(order);
			}else{
				throw new PortfolioException("New order date cannot be earlier than last order. Last order date:"+datetime.toString());
			}
		}
		return PortfolioDAO.updateStock(existingStock);
	}

	public static boolean sellStock(Stock stock, int qty) throws PortfolioException {
		Stock existingStock = EditStock.getStock(stock.getExchange(),
				stock.getSymbol());
		
		while(true){
			Iterator<Order> itr = existingStock.getOrders().iterator();
			Order latestOrder = null;
			while(itr.hasNext()){
				latestOrder = itr.next();
			}
			if(latestOrder!=null){
				if(latestOrder.getQuantity()>qty){
					latestOrder.setQuantity(latestOrder.getQuantity()-qty);
					break;
				}else{
					qty = qty - latestOrder.getQuantity();
					existingStock.getOrders().remove(latestOrder);
				}
				if(latestOrder.getQuantity()==0){
					existingStock.getOrders().remove(latestOrder);
				}
				if(qty==0){
					break;
				}
			}
		}
		return PortfolioDAO.updateStock(existingStock);
	}

	private static String addXmlNode(String ordersStr, double price, int qty, Timestamp datetime) {
		String updatedxml = null;
		try {
			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(new InputSource(
					new StringReader(ordersStr)));
			Element rootNode = doc.getRootElement();

			Element order = new Element("order");
			Attribute attr = new Attribute("id", String.valueOf(datetime.getTime()));
			order.setAttribute(attr);
			Element priceEle = new Element("price").setText(String.valueOf(price));
			Element quantity = new Element("quantity").setText(String
					.valueOf(qty));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Element datetimeEle = new Element("datetime").setText(dateFormat.format(datetime));
			order.addContent(priceEle);
			order.addContent(quantity);
			order.addContent(datetimeEle);
			rootNode.addContent(order);
			updatedxml = new XMLOutputter().outputString(doc).replaceAll("\"", "'");		
		} catch (IOException io) {
			io.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return updatedxml;
	}
	
	private static String removeXmlNode(String ordersStr, long did) {
		String updatedxml = null;
		try {
			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(new InputSource(
					new StringReader(ordersStr)));
			Element rootNode = doc.getRootElement();

			List<Element> orderList = rootNode.getChildren();
			for(Element order : orderList){
				long id = order.getAttribute("id").getLongValue();
				if(did==id){
					rootNode.removeContent(order);
				}
			}
			
			updatedxml = new XMLOutputter().outputString(doc);		
		} catch (IOException io) {
			io.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return updatedxml;
	}
	
	private static String reduceQuantity(String ordersStr, int qty) {
		String updatedxml = null;
		Document doc = null;
		try {
			SAXBuilder builder = new SAXBuilder();

			Element latestOrder = null;
			while(true){
				doc = (Document) builder.build(new InputSource(
						new StringReader(ordersStr)));
				Element rootNode = doc.getRootElement();
				
				List<Element> orderList = rootNode.getChildren();
				
				long latest = 0;
				for(Element order : orderList){
					long id = order.getAttribute("id").getLongValue();
					if(latest<id){
						latest = id;
						latestOrder = order;
					}
				}
				int quantity = Integer.parseInt(latestOrder.getChild("quantity").getText());
				if(quantity>qty){
					latestOrder.getChild("quantity").setText(String.valueOf(quantity-qty));
					break;
				}else{
					qty = qty - quantity;
					ordersStr = removeXmlNode(ordersStr,latest);
				}
			}			
			
			updatedxml = new XMLOutputter().outputString(doc);		
		} catch (IOException io) {
			io.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return updatedxml;
	}
}
