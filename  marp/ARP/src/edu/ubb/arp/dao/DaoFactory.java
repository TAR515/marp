package edu.ubb.arp.dao;

import java.sql.SQLException;

import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;

public abstract class DaoFactory {

	public static DaoFactory getInstance() throws SQLException {
		return new JdbcDaoFactory();
	}

	/** Abstract method witch returns a UserDao.
	 * 
	 * @return UserDao */
	public abstract UsersDao getUsersDao();
	
	public abstract ResourcesDao getResourceDao();

	public abstract GroupsDao getGroupsDao();

	public abstract ProjectsDao getProjectsDao();
	
	public abstract RequestsDao getRequestsDao();

}
