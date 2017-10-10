package com.technowlogeek.portfolio.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.technowlogeek.portfolio.service.LoginService;
import com.technowlogeek.portfolio.vo.Users;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6469650396385089080L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		StringBuffer sb = new StringBuffer();
		if (username == null || username.length() < 4) {
			sb.append("Username should have atleast 6 characters long and shouldn't contain any special characters. ");
		}
		if (password == null || password.length() < 8) {
			sb.append("Password should have atleast 8 characters long and must have alphnumeric and special characters. ");
		}
		if(sb.length()==0){
			Users user = LoginService.login(username, password);
			if(user!=null){
				HttpSession session = request.getSession();
				int ch = username.charAt(0);
				if(ch>96 && ch<123){
					username = username.replace(username.charAt(0), (char)(username.charAt(0)-32));
				}
				session.setAttribute("username", username);
				session.setAttribute("permission", user.getPermission());
				response.sendRedirect("Home");
			}else{
				sb.append("Invalid username/password. Please try again.");
				RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
				request.setAttribute("response", ""+sb.toString());
				rd.forward(request, response);
			}
		}else{
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
			request.setAttribute("response", ""+sb.toString());
			rd.forward(request, response);
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.sendRedirect("/Portfolio");
	}
}
