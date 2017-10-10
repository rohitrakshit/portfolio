package com.technowlogeek.portfolio.vo;

import java.sql.Timestamp;

public class Order implements Comparable<Object>{
	private long id;
	private double price;
	private int quantity;
	private Timestamp datetime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Timestamp getDatetime() {
		return datetime;
	}
	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", price=" + price + ", quantity="
				+ quantity + ", datetime=" + datetime + "]";
	}
	@Override
	public int compareTo(Object o) {
		int returnVal = 0;
		if(o!=null && o instanceof Order){
			Order order = (Order)o;
			if(this.id>order.getId()){
				returnVal = 1;
			}else if(this.id<order.getId()){
				returnVal = -1;
			}else{
				returnVal = 0;
			}
		}
		return returnVal;
	}
}
