package edu.ubb.arp.logic.commands;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.BllExceptions;
import edu.ubb.arp.logic.HashCoding;

public class CheckUserCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(CheckUserCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private UsersDao userDao = null;
	
	
	public CheckUserCommand(JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.userDao = instance.getUsersDao();
			this.request = request;
			
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0, response);
		}
		
	}
	
	
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		
		String userName = getString(0,"username",request);
		String password = getString(1,"password",request);

		if (!errorCheck(response)) {

			try {
				int userExists = userDao.checkUserNameAndPassword(userName, HashCoding.hashString(password));
				addInt("existsuser", userExists, response);
			} catch (SQLException | BllExceptions e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				response = setError(0, response);
			}
		}
		
		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;
	}

}
