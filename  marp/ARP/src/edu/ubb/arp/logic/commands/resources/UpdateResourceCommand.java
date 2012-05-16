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

public class UpdateResourceCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(UpdateResourceCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ResourcesDao resourceDao = null;
	
	public UpdateResourceCommand (JSONArray request) {
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
		
		String oldResourceName = null;
		boolean oldActive = false;
		String oldResourceTypeName = null;
		String oldResourceGroupName = null;
		String newResourceName = null;
		boolean newActive = false;
		String newResourceTypeName = null;
		String newResourceGroupName = null;
		
		try {
			oldResourceName = getString(0,"oldresourcename", request);
			oldActive = getBool(0,"oldactive", request);
			oldResourceTypeName = getString(0,"oldresourcetypename", request);
			oldResourceGroupName = getString(0,"oldresourcegroupname", request);
			newResourceName = getString(0,"newresourcename", request);
			newActive = getBool(0,"newactive", request);
			newResourceTypeName = getString(0,"newresourcetypename", request);
			newResourceGroupName = getString(0,"newresourcegroupname", request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				int setActive = resourceDao.updateResource(oldResourceName, oldActive, oldResourceTypeName, oldResourceGroupName, newResourceName, newActive, newResourceTypeName, newResourceGroupName);
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
