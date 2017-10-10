package com.technowlogeek.portfolio.service;

import com.technowlogeek.portfolio.dao.PortfolioDAO;
import com.technowlogeek.portfolio.exception.PortfolioException;
import com.technowlogeek.portfolio.vo.Users;

public class LoginService {
	public static Users login(String username, String password){
		Users user = null;
		try{
			user = PortfolioDAO.login(new Users(username,password));
		}catch(PortfolioException pe){
			pe.printStackTrace();
		}
		return user;
	}
}
