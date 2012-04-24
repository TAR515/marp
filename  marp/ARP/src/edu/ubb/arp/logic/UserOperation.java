package edu.ubb.arp.logic;

import java.sql.SQLException;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.Users;

public class UserOperation {
	
	
	
	public Users getUser(String userName) {
		Users us = new Users();
		
		DaoFactory instance;
		try {
			instance = JdbcDaoFactory.getInstance();
			UsersDao emberekDAO = instance.getUsersDao();
			us = emberekDAO.loadUser(userName);
			
		} catch (SQLException e) {
			System.out.println("Valami LOGIC LAYER: ");
			e.printStackTrace();
		}
		

		return us;
	}
	
	
}
