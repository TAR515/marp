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
 *inserts a new resource into the database
 */
public class InsertNewResourceCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(InsertNewResourceCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ResourcesDao resourceDao = null;
	
	/**
	 * constructor
	 * @param request contains a resourceName , a resourceTypeName , a resourceGroupName and a boolean active  
	 */
	public InsertNewResourceCommand (JSONArray request) {
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
	
	/**
	 * @return returns ErrorCode if the returned value is lower than 0 | Created resource id if the returned value is bigger than 0
	 */
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		
		String resourceName = null;
		boolean active = false;
		String resourceTypeName = null;
		String resourceGroupName = null;
		
		try {
			resourceName = getString(0,"resourcename", request);
			active = getBool(0,"active",request);
			resourceTypeName = getString(0,"resourcetypename", request);
			resourceGroupName = getString(0,"resourcegroupname", request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				int resourceCreated = resourceDao.createResource(resourceName, active, resourceTypeName, resourceGroupName);
				response = addInt("resourcecreated", resourceCreated, response);
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
