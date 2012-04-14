package edu.ubb.arp.logic;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public class UserOperation {
	
	
	
	public Users getUser(String userName) {
		Users us = new Users();
		
		DaoFactory instance;
		try {
			instance = JdbcDaoFactory.getInstance();
			UsersDao emberekDAO = instance.getUsersDao();
			us = emberekDAO.loadUser(userName);
		} catch (DalException e) {
			System.out.println("Valami LOGIC LAYER: ");
			e.printStackTrace();
		}
		
		return us;
	}
	
	
}
