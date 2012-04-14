package edu.ubb.arp.dao.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.GroupsDao;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.impls.GroupsJdbcDao;
import edu.ubb.arp.dao.impls.ResourcesJdbcDao;
import edu.ubb.arp.dao.impls.UsersJdbcDao;
import edu.ubb.arp.exceptions.DalException;

public class JdbcDaoFactory extends DaoFactory{
	
	private static final Logger logger = Logger.getLogger(JdbcDaoFactory.class);
	
	private DataSource ds = null;
	
	public JdbcDaoFactory() throws DalException {
		Context initContext;
		try {
			initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/ARPDB");
		} catch (NamingException e) {
			logger.error("error while getting datasource",e);
			throw new DalException(DalException.NO_CONNECTION, "valami", e);
		}
	}
	
	
	public UsersDao getEmberekDAO() {
		return new UsersJdbcDao(ds);
	}


	@Override
	public UsersDao getUsersDao() {
		return new UsersJdbcDao(ds);
	}


	@Override
	public ResourcesDao getResourceDao() {
		return new ResourcesJdbcDao(ds);
	}


	@Override
	public GroupsDao getGroupsDao() {
		return new GroupsJdbcDao(ds);
	}
}
