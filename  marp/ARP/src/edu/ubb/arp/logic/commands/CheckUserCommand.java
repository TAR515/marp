package edu.ubb.arp.logic.commands;

import java.sql.SQLException;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.BllExceptions;
import edu.ubb.arp.logic.HashCoding;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 * checks if exists in the database a user with a give password
 */
public class CheckUserCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(CheckUserCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private UsersDao userDao = null;
	
	/**
	 * constructor 
	 * @param request contains the user name and password
	 */
	public CheckUserCommand(JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.userDao = instance.getUsersDao();
			this.request = request;
			
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}	
	}
	
	/**
	 * @return returns 1 if the user exists and an error message if not
	 */
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		String userName = null;
		String password = null;
		
		try {
			userName = getString(0,"username",request);
			password = getString(0,"password",request);
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				int errmsg = userDao.checkUserNameAndPassword(userName, HashCoding.hashString(password));
				
				if (errmsg <= 0) {
					response = setError(errmsg);
				} else {
					response = addInt("existsuser", errmsg, response);
				}
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				response = setError(0);
			} catch (BllExceptions e1) {
				logger.error(getClass().getName() + methodName + e1);
				response = setError(-1);
			}
		}
		
		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;
	}

}
