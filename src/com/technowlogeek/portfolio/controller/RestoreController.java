package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.util.Util;
import com.technowlogeek.portfolio.vo.Stock;

@MultipartConfig
@WebServlet(urlPatterns = "/restore")
public class RestoreController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		Part filePart = request.getPart("restorefile");
		if(filePart==null || filePart.getSize()<1){
			sb.append("Please select a Portfolio backup file and upload");
		}
		if(sb.length()==0){
			InputStream fileContent = filePart.getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(fileContent, writer, "UTF-8");
			String theString = writer.toString();
			int row=0;
			String[] lines = theString.split(";\n");//System.getProperty("line.separator"));
			List<Stock> stocks = new ArrayList<Stock>();
			try{
				for(String line : lines){
					if(row==0){		
						row++;
						continue;
					}
					String[] tokens = line.split(",");
					Stock stock = new Stock();
					stock.setId(getStringValue(tokens[0]));
					stock.setExchange(getStringValue(tokens[1]));
					stock.setSymbol(getStringValue(tokens[2]));
					stock.setSymbolId(getStringValue(tokens[3]));
					stock.setName(getStringValue(tokens[4]));
					stock.setElevatedAveragePrice(Double.parseDouble(getNumberValue(tokens[5])));
					stock.setLastPrice(Double.parseDouble(getNumberValue(tokens[6])));
					stock.setGroup(getStringValue(tokens[7]));
					stock.setSector(getStringValue(tokens[8]));
					stock.setIndustry(getStringValue(tokens[9]));
					try{
						stock.setLastTradeDateTime(Timestamp.valueOf(getStringValue(tokens[10])));
					}catch(Exception pe){
						pe.printStackTrace();
					}
					stock.setOrders(Util.getOrderSet(getStringValue(tokens[11])));
					stock.setNotes(getStringValue(tokens[12]));
					stocks.add(stock);
				}
			
				if(PortfolioDAO.addAllStocks(stocks)){
					sb.append("Restore successful");
				}else{
					sb.append("Restore failed");
				}
			}catch(Exception pe){
				pe.printStackTrace();
				sb.append(pe.getMessage());
			}
		}
		request.setAttribute("response", sb.toString());
		RequestDispatcher rd = request.getRequestDispatcher("Home");
		rd.forward(request, response);
	}
	
	private String getStringValue(String val){
		if(val!=null){
			val = val.replaceAll("\"", "");
			val = val.replaceAll("\\\\n", "\n");
			val = val.replaceAll("&#44;", ",");
		}
		return val;
	}
	private String getNumberValue(String val){
		if(val!=null || !"".equals(val)){
			return val.replaceAll("\"", "");
		}
		return "0";
	}
}
