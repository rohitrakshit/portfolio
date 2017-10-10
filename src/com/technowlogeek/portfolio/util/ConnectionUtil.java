package com.technowlogeek.portfolio.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.sql.ResultSet;

public class ConnectionUtil {
	/*
	 * PhpMyAdmin Root User: admin7QGyPC8 Root Password: JXbWxJaivDCg URL:
	 * https://portfolio-technowlogeek.rhcloud.com/phpmyadmin/
	 */

	private static String dbURL = "jdbc:mysql://127.8.9.2:3306/portfolio";
	private static String uname = "admin7QGyPC8";
	private static String passwd = "JXbWxJaivDCg";

	static {
		if("local".equals(Util.getEnvironment())){
			dbURL = "jdbc:mysql://localhost:3306/portfolio";
			uname = "rohit"; 
			passwd = "rohit";
		}
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'portfolio'";
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/",
					uname, passwd);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (!rs.next()) {
				closeConnection(rs, ps, conn);
				conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/", uname, passwd);
				sql = "CREATE DATABASE IF NOT EXISTS portfolio";
				ps = conn.prepareStatement(sql);
				if (ps.execute()) {
					System.out.println("New database (portfolio) created.");
				}
			}
			closeConnection(rs, ps, conn);
			conn = DriverManager.getConnection(dbURL, uname, passwd);
			sql = "CREATE TABLE IF NOT EXISTS stock(id varchar(50),name varchar(50),"
					+ "symbol varchar(50),symbol_id varchar(30),exchange varchar(10),"
					+ "avrg_price double(9,2),elev_avrg_price double(9,2),"
					+ "last_price double(9,2),stock_group varchar(2),"
					+ "stock_industry varchar(50),lt_date date,orders varchar(2000), notes varchar(1000),"
					+ "primary key(symbol,exchange))";
			ps = conn.prepareStatement(sql);
			if (ps.execute()) {
				System.out.println("New table (stock) created.");
			}
			sql = "CREATE TABLE IF NOT EXISTS users(username varchar(30),"
					+ "password varchar(30),permission varchar(20))";
			ps = conn.prepareStatement(sql);
			if (ps.execute()) {
				System.out.println("New table (users) created.");
			}
			sql = "INSERT INTO Users values('rohit','maw4$Twgtt','WRITE')";
			sql = "INSERT INTO Users (username, password, permission)"
					+" SELECT * FROM (SELECT 'rohit', 'maw4$Twgtt', 'WRITE') AS tmp"
					+" WHERE NOT EXISTS ("
					+" SELECT username FROM Users WHERE username = 'rohit'"
					+" ) LIMIT 1;";
			ps = conn.prepareStatement(sql);
			if (ps.executeUpdate() > 0) {
				System.out.println("User rohit added in database.");
			}
			sql = "CREATE TABLE IF NOT EXISTS symbol(nse varchar(30),"
					+ "bse varchar(30), name varchar(50), primary key(nse))";
			ps = conn.prepareStatement(sql);
			if (ps.execute()) {
				System.out.println("New table (symbol) created.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(rs, ps, conn);
		}
	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbURL, uname, passwd);
			/*
			 * conn = DriverManager.getConnection(
			 * "jdbc:mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/portfolio"
			 * , "admin7QGyPC8", "JXbWxJaivDCg");
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(ResultSet rs, Statement st,
			Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (Exception e) {
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
			}
		}
	}
}
