package edu.ubb.arp.logic.commands;

import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public class LoadUserDataCommand extends BaseCommandOperations implements Command{
	private static final Logger logger = Logger.getLogger(LoadUserDataCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private UsersDao userDao = null;
	private Users user= null;
	
	
	public LoadUserDataCommand(JSONArray request) {
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
	
	
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		String userName = null;
		
		try {
			userName = getString(0,"username",request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				user = userDao.loadUser(userName);
				
				JSONObject userData = new JSONObject();
				userData.put("userid", user.getUserID());
				userData.put("username", user.getUserName());
				userData.put("userphonenumber", user.getPhoneNumber());
				userData.put("useremail", user.getEmail());	
				userData.put("userresourcename", user.getResource().getResourceName());
				
				response.add(userData);	
			} catch (DalException e) {
				logger.error(getClass().getName() + methodName + e.getErrorMessage());
				response = setError(e.getErrorCode());
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				response = setError(0);
			} 
		}
		
		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;
	}

}
