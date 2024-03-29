package edu.ubb.arp.logic.commands.users;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.BllExceptions;
import edu.ubb.arp.exceptions.DalException;
import edu.ubb.arp.logic.HashCoding;
import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.Command;
import net.sf.json.JSONArray;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *inserts a new user into the database
 */
public class InsertNewUserCommand extends BaseCommandOperations implements Command{
	private static final Logger logger = Logger.getLogger(InsertNewUserCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private UsersDao userDao = null;
	
	/**
	 * constructor
	 * @param request contains all necessary data to create a new user
	 */
	public InsertNewUserCommand (JSONArray request) {
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
	 * @return returns the id of the created user or an error message 
	 */
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		
		String userName = null;
		String password = null;
		String phoneNumber = null;
		String email = null;
		String resourceName = null;
		boolean active = false;
		String resourceGroupName = null; 
		
		try {
			userName = getString(0,"targetusername",request);
			password = getString(0,"targetpassword",request);
			phoneNumber = getString(0,"phonenumber",request);
			email = getString(0,"email",request);
			resourceName = getString(0,"resourcename",request);
			active = getBool(0, "active", request);
			resourceGroupName = getString(0,"resourcegroupname",request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				int userInserted = userDao.createUser(userName,  HashCoding.hashString(password), phoneNumber, email, resourceName, active, resourceGroupName);
				response = addInt("userinserted", userInserted, response);
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				response = setError(0);
			} catch (BllExceptions e) {
				logger.error(getClass().getName() + methodName + e);
				response = setError(-1);
			} catch (DalException e) {
				logger.error(getClass().getName() + methodName + e.getErrorMessage());
				response = setError(e.getErrorCode());
			}
		}
		
		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;

	}
}
