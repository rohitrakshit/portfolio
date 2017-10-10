package com.technowlogeek.portfolio.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter {
	
	Map<String,Boolean> validURL = new HashMap<String,Boolean>();
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		validURL.put("/Portfolio/jsp/datetimepicker_css.js",true);
		validURL.put("/Portfolio/jsp/jquery.sortElements.js",true);
		validURL.put("/Portfolio/jsp/portfolio.css",true);
		validURL.put("/Portfolio/jsp/portfolio.js",true);
		validURL.put("/Portfolio/Home",true);
		validURL.put("/Portfolio/home",true);
		validURL.put("/Portfolio/addstock",true);
		validURL.put("/Portfolio/addStock",true);
		validURL.put("/Portfolio/backup",true);
		validURL.put("/Portfolio/restore",true);
		validURL.put("/Portfolio/showLatest",true);
		validURL.put("/Portfolio/updatePortfolio",true);
		validURL.put("/Portfolio/updateStock",true);
		validURL.put("/Portfolio/editStock",true);
		validURL.put("/Portfolio/sort",true);
		validURL.put("/Portfolio/editstock",true);
		validURL.put("/Portfolio/loggedout",true);
		validURL.put("/Portfolio/viewgains",true);
		validURL.put("/Portfolio/viewGains",true);
		validURL.put("/Portfolio/buysell",true);
		validURL.put("/Portfolio/buysellStock",true);
		validURL.put("/Portfolio/sectorallocationchart",true);
		validURL.put("/Portfolio/industryallocationchart",true);
		validURL.put("/Portfolio/companyallocationchart",true);
		validURL.put("/Portfolio/index",true);
		validURL.put("/Portfolio/logout",true);
		validURL.put("/Portfolio/login",true);
		validURL.put("/Portfolio/",true);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();
		String path = ((HttpServletRequest) request).getRequestURI();
		if(validURL.get(path)!=null){
			if (path.startsWith("/Portfolio/index")
					|| path.startsWith("/Portfolio/login") || path.startsWith("/Portfolio/logout")) {
				chain.doFilter(request, response); // Just continue chain.
			} else {
				if(path.equals("/Portfolio/") && (session.getAttribute("username") != null
						|| session.getAttribute("permission") != null)){
					response.sendRedirect("home");
				}
				if (session.getAttribute("username") == null
						|| session.getAttribute("permission") == null) {
					if(!path.equals("/Portfolio/")){
						request.setAttribute("response", ""
								+ "Invalid session, cannot process your request, please login.");
					}
					RequestDispatcher rd = request
							.getRequestDispatcher("/");
					rd.forward(request, response);
				} else {
					chain.doFilter(request, response);
				}
			}
		}else{
			response.sendRedirect("/");
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
