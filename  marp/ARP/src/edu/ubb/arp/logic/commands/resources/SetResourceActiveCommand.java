package edu.ubb.arp.logic.commands.resources;

import java.sql.SQLException;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;
import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.Command;
/**
 * 
 * @author VargaAdorjan
 *sets a resource's active to true or false , if active is false the deletes the booking of the resource from the current date
 *removes all request that were for this resource 
 */
public class SetResourceActiveCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(SetResourceActiveCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ResourcesDao resourceDao = null;
	
	/**
	 * constructor 
	 * @param request contains a resourceID , a currentWeek and a boolean value
	 */
	public SetResourceActiveCommand (JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.resourceDao = instance.getResourceDao();
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
		
		int resourceID = -1;
		boolean active = false;
		int currentWeek = -1;
		
		try {
			resourceID = getInt(0,"resourceid", request);
			currentWeek = getInt(0,"currentweek", request);
			active = getBool(0,"active", request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				int setActive = resourceDao.setActive(resourceID, active, currentWeek);
				response = addInt("setactive", setActive, response);
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
