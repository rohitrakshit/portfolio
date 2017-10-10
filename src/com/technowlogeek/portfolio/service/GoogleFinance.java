package com.technowlogeek.portfolio.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.technowlogeek.portfolio.vo.Stock;

public class GoogleFinance {
	//"http://www.google.com/finance/info?q=NASDAQ:GOOG"; 
	private static final String googleFinanceApiUrl = "https://finance.google.com/finance/info?client=ig&q=";
	private static final NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
	
	public static List<Stock> getStockQuote(String exchangeSymbol) {
		List<Stock> stockList = null;
		try {
			URL url = new URL(googleFinanceApiUrl + exchangeSymbol.replaceAll("&", "%26"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder sb = new StringBuilder();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			output = sb.toString();
			JSONArray jsonArray = new JSONArray(output.substring(output.indexOf('['), output.length()));
			if(jsonArray.length()>0){
				stockList = new ArrayList<Stock>();
			}
			for (Object json : jsonArray) {
				JSONObject jsonObj = (JSONObject) json;
				Stock stock = new Stock();
				try{
					stock.setId(String.valueOf(jsonObj.get("id")));
					stock.setSymbol(String.valueOf(jsonObj.get("t")));
					stock.setSymbolId(String.valueOf(jsonObj.get("t")));
					stock.setExchange(String.valueOf(jsonObj.get("e")));
					String value = ((String)jsonObj.get("l")).replaceAll("\\+", "");
					stock.setLastPrice(format.parse(!"".equals(value)?value:"0").doubleValue());
					value = ((String)jsonObj.get("lt_dts")).replace("T", " ");
					value = value.replace("Z", "");
					stock.setLastTradeDateTime(Timestamp.valueOf(value));
					stockList.add(stock);
				}catch(ParseException pe){
					pe.printStackTrace();
				}
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stockList;
	}

}
