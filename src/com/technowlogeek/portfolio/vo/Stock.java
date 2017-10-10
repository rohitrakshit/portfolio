package com.technowlogeek.portfolio.vo;

import java.sql.Timestamp;
import java.util.Set;

public class Stock {
	private String id;
	private String name;
	private String symbol;
	private String symbolId;
	private String exchange;
	private double averagePrice;
	private double elevatedAveragePrice;
	private double lastPrice;
	private String group;
	private String sector;
	private String industry;
	private Timestamp lastTradeDateTime;
	private Set<Order> orders;
	private String notes;
	
	private int currentQuantity;
	private double nextSell;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getSymbolId() {
		return symbolId;
	}
	public void setSymbolId(String symbolId) {
		this.symbolId = symbolId;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public double getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(double averagePrice) {
		this.averagePrice = averagePrice;
	}
	public double getElevatedAveragePrice() {
		return elevatedAveragePrice;
	}
	public void setElevatedAveragePrice(double elevatedAveragePrice) {
		this.elevatedAveragePrice = elevatedAveragePrice;
	}
	public double getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public Timestamp getLastTradeDateTime() {
		return lastTradeDateTime;
	}
	public void setLastTradeDateTime(Timestamp lastTradeDateTime) {
		this.lastTradeDateTime = lastTradeDateTime;
	}
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getCurrentQuantity() {
		return currentQuantity;
	}
	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}
	public double getNextSell() {
		return nextSell;
	}
	public void setNextSell(double nextSell) {
		this.nextSell = nextSell;
	}
	@Override
	public String toString() {
		return "Stock [id=" + id + ", name=" + name + ", symbol=" + symbol
				+ ", symbolId=" + symbolId + ", exchange=" + exchange
				+ ", averagePrice=" + averagePrice + ", elevatedAveragePrice="
				+ elevatedAveragePrice + ", lastPrice=" + lastPrice
				+ ", group=" + group + ", sector=" + sector +", industry=" + industry
				+ ", lastTradeDateTime=" + lastTradeDateTime + ", orders="
				+ orders + ", notes=" + notes + ", currentQuantity="
				+ currentQuantity + ", nextSell=" + nextSell + "]";
	}
}
