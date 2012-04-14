package edu.ubb.arp.dao;

import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;

public abstract class DaoFactory {
	
	public static DaoFactory getInstance() throws DalException {
		return new JdbcDaoFactory();
	}

	public abstract UsersDao getUsersDao();
	public abstract ResourcesDao getResourceDao();
	public abstract GroupsDao getGroupsDao();
	
}
