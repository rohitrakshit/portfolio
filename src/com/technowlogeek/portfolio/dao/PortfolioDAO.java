package com.technowlogeek.portfolio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.util.ConnectionUtil;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Stock;
import com.technowlogeek.portfolio.vo.Users;

public class PortfolioDAO {
	public static boolean addStock(Stock stock) throws PortfolioException{
		boolean resp = false;
		try {
			Connection con = ConnectionUtil.getConnection();
			String sql = "INSERT INTO stock(exchange,symbol,name,id,last_price,symbol_id,sgroup,sector,industry,elev_avrg_price,lt_date,orders,notes) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, stock.getExchange());
			ps.setString(2, stock.getSymbol());
			ps.setString(3, stock.getName());
			ps.setString(4, stock.getId());
			ps.setDouble(5, stock.getLastPrice());
			ps.setString(6, stock.getSymbolId());
			ps.setString(7, stock.getGroup());
			ps.setString(8, stock.getSector());
			ps.setString(9, stock.getIndustry());
			ps.setDouble(10, stock.getAveragePrice());
			ps.setTimestamp(11, new Timestamp(stock.getLastTradeDateTime().getTime()));
			ps.setString(12, Util.getOrdersXmlString(stock.getOrders()));
			ps.setString(13, stock.getNotes());
			int exec = ps.executeUpdate();
			if (exec > 0) {
				resp = true;
			}
			ConnectionUtil.closeConnection(null, ps, con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortfolioException(e.getStackTrace().toString());
		}
		return resp;
	}
	
	public static boolean addAllStocks(List<Stock> stocks) throws PortfolioException{
		boolean resp = false;
		try {
			Connection con = ConnectionUtil.getConnection();
			String sql = "INSERT INTO stock(exchange,symbol,name,id,last_price,symbol_id,sgroup,sector,industry,elev_avrg_price,lt_date,orders,notes) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			for(Stock stock : stocks){
				ps.setString(1, stock.getExchange());
				ps.setString(2, stock.getSymbol());
				ps.setString(3, stock.getName());
				ps.setString(4, stock.getId());
				ps.setDouble(5, stock.getLastPrice());
				ps.setString(6, stock.getSymbolId());
				ps.setString(7, stock.getGroup());
				ps.setString(8, stock.getSector());
				ps.setString(9, stock.getIndustry());
				ps.setDouble(10, stock.getElevatedAveragePrice());
				ps.setTimestamp(11, new Timestamp(stock.getLastTradeDateTime().getTime()));
				ps.setString(12, Util.getOrdersXmlString(stock.getOrders()));
				ps.setString(13, stock.getNotes());
				ps.addBatch();
			}
			int[] exec = ps.executeBatch();
			if (exec.length == stocks.size()) {
				resp = true;
			}
			ConnectionUtil.closeConnection(null, ps, con);
		} catch (Exception e) {
			throw new PortfolioException(e.getStackTrace().toString());
		}
		return resp;
	}
	
	public static boolean updateAllStocks(List<Stock> stocks) throws PortfolioException{
		boolean resp = false;
		try {
			Connection con = ConnectionUtil.getConnection();
			String sql = "UPDATE stock SET elev_avrg_price=?,last_price=?,lt_date=? WHERE exchange=? and symbol=?";
			PreparedStatement ps = con.prepareStatement(sql);
			for(Stock stock : stocks){
				ps.setDouble(1, stock.getElevatedAveragePrice());
				ps.setDouble(2, stock.getLastPrice());
				ps.setTimestamp(3, new Timestamp(stock.getLastTradeDateTime().getTime()));
				ps.setString(4, stock.getExchange());
				ps.setString(5, stock.getSymbol());
				ps.addBatch();
			}
			int[] exec = ps.executeBatch();
			if (exec.length == stocks.size()) {
				resp = true;
			}else{
				System.out.println("some of the updates failed.");
			}
			ConnectionUtil.closeConnection(null, ps, con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortfolioException(e.getStackTrace().toString());
		}
		return resp;
	}
	
	public static boolean updateStock(Stock stock) throws PortfolioException{
		boolean resp = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ConnectionUtil.getConnection();
			String sql = "UPDATE stock SET symbol_id=?,name=?,elev_avrg_price=?,last_price=?,sgroup=?,sector=?,industry=?,lt_date=?,orders=?,notes=? WHERE exchange=? and symbol=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, stock.getSymbolId());
			ps.setString(2, stock.getName());
			ps.setDouble(3, stock.getElevatedAveragePrice());
			ps.setDouble(4, stock.getLastPrice());
			ps.setString(5, stock.getGroup());
			ps.setString(6, stock.getSector());
			ps.setString(7, stock.getIndustry());
			ps.setTimestamp(8, stock.getLastTradeDateTime());
			ps.setString(9, Util.getOrdersXmlString(stock.getOrders()));
			ps.setString(10, stock.getNotes());
			ps.setString(11, stock.getExchange());
			ps.setString(12, stock.getSymbol());
			int exec = ps.executeUpdate();
			if (exec > 0) {
				resp = true;
			}else{
				throw new PortfolioException("Stock Update Failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortfolioException(e.getStackTrace().toString());
		}finally{
			ConnectionUtil.closeConnection(null, ps, con);
		}
		return resp;
	}
	
	public static Stock getStock(Map<String,String> searchStr) throws PortfolioException{
		Stock stock = null;
		try {
			Connection con = ConnectionUtil.getConnection();
			String sql = "SELECT id,exchange,symbol,symbol_id,name,elev_avrg_price,last_price,sgroup,sector,industry,lt_date,orders,notes from stock where exchange=? and symbol=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, searchStr.get("exchange"));
			ps.setString(2, searchStr.get("symbol"));
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				stock = new Stock();
				stock.setId(rs.getString(1));
				stock.setExchange(rs.getString(2));
				stock.setSymbol(rs.getString(3));
				stock.setSymbolId(rs.getString(4));
				stock.setName(rs.getString(5));
				stock.setElevatedAveragePrice(rs.getDouble(6));
				stock.setLastPrice(rs.getDouble(7));
				stock.setGroup(rs.getString(8));
				stock.setSector(rs.getString(9));
				stock.setIndustry(rs.getString(10));
				stock.setLastTradeDateTime(rs.getTimestamp(11));
				stock.setOrders(Util.getOrderSet(rs.getString(12)));
				stock.setNotes(rs.getString(13));
				stock.setAveragePrice(Util.getAveragePrice(stock.getOrders()));
			}
			ConnectionUtil.closeConnection(null, ps, con);
		} catch (Exception e) {
			throw new PortfolioException(e.getStackTrace().toString());
		}
		return stock;
	}
	
	public static List<Stock> getAllStocks() throws PortfolioException{
		List<Stock> stocks = null;
		try {
			Connection con = ConnectionUtil.getConnection();
			String sql = "SELECT id,exchange,symbol,symbol_id,name,elev_avrg_price,last_price,sgroup,sector,industry,lt_date,orders,notes from stock";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				stocks = new ArrayList<Stock>();
				do{
					Stock stock = new Stock();
					stock.setId(rs.getString(1));
					stock.setExchange(rs.getString(2));
					stock.setSymbol(rs.getString(3));
					stock.setSymbolId(rs.getString(4));
					stock.setName(rs.getString(5));
					stock.setElevatedAveragePrice(rs.getDouble(6));
					stock.setLastPrice(rs.getDouble(7));
					stock.setGroup(rs.getString(8));
					stock.setSector(rs.getString(9));
					stock.setIndustry(rs.getString(10));
					stock.setLastTradeDateTime(rs.getTimestamp(11));
					stock.setOrders(Util.getOrderSet(rs.getString(12)));
					stock.setNotes(rs.getString(13));
					stock.setAveragePrice(Util.getAveragePrice(stock.getOrders()));
					stocks.add(stock);
				}while(rs.next());
			}
			ConnectionUtil.closeConnection(null, ps, con);
		} catch (Exception e) {
			throw new PortfolioException(e.getStackTrace().toString());
		}
		return stocks;
	}
	
	public static Users login(Users user) throws PortfolioException{
		try {
			Connection con = ConnectionUtil.getConnection();
			String sql = "SELECT permission FROM users WHERE username=? and password=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user.setPassword("*********");
				user.setPermission(rs.getString(1));
			}else{
				user = null;
			}
			ConnectionUtil.closeConnection(null, ps, con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortfolioException(e.getStackTrace().toString());
		}
		return user;
	}
	
	public static String getSymbolId(String symbol) throws PortfolioException{
		String symbolId = null;
		try {
			Connection con = ConnectionUtil.getConnection();
			String sql = "SELECT symbol FROM stock WHERE symbol_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, symbol);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				symbolId = rs.getString(1);
			}else
			ConnectionUtil.closeConnection(null, ps, con);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortfolioException(e.getStackTrace().toString());
		}
		return symbolId;
	}

	public static String deleteStock(String exchange, String symbol) throws PortfolioException{
		String resp = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ConnectionUtil.getConnection();
			String sql = "DELETE FROM stock WHERE exchange=? and symbol=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, exchange);
			ps.setString(2, symbol);
			int exec = ps.executeUpdate();
			if (exec > 0) {
				resp = exchange+":"+symbol+" Deleted successfully.";
			}else{
				resp = exchange+":"+symbol+" Failed to delete.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PortfolioException(e.getStackTrace().toString());
		}finally{
			ConnectionUtil.closeConnection(null, ps, con);
		}
		return resp;
	}
}
