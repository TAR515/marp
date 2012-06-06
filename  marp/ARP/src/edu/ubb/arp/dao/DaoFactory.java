package edu.ubb.arp.dao;

import java.sql.SQLException;

import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *Abstract class which contains abstract methods returning a Dao
 */
public abstract class DaoFactory {

	public static DaoFactory getInstance() throws SQLException {
		return new JdbcDaoFactory();
	}

	/** Abstract method which returns a UserDao.
	 * 
	 * @return UserDao */
	public abstract UsersDao getUsersDao();
	/**
	 * Abstract method which returns a ResourceDao.
	 * @return ResourceDao
	 */
	public abstract ResourcesDao getResourceDao();
	/**
	 * Abstract method which returns a GroupsDao.
	 * @return GroupsDao
	 */
	public abstract GroupsDao getGroupsDao();
	/**
	 * Abstract method which returns a ProjectsDao.
	 * @return ProjectsDao
	 */
	public abstract ProjectsDao getProjectsDao();
	/**
	 * Abstract method which returns a RequestsDao.
	 * @return RequestsDao
	 */
	public abstract RequestsDao getRequestsDao();

}
