package edu.ubb.arp.logic.commands.projects;

import java.sql.SQLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;
import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.Command;

public class UpdateResourceRatioWithRequestCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(UpdateResourceRatioWithRequestCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ProjectsDao projectDao = null;
	
	public UpdateResourceRatioWithRequestCommand (JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.projectDao = instance.getProjectsDao();
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
		
		int projectID = -1;
		int targetResourceID = -1;
		int requestID = -1;
		int currentWeek = -1;
		int updateRatio = -1;
		
		try {
			projectID = getInt(0,"projectid", request);
			targetResourceID = getInt(0,"targetresourceid",request);
			requestID = getInt(0, "requestid", request);
			currentWeek = getInt(0,"currentweek", request);
			updateRatio = getInt(0,"updateratio", request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		} catch (JSONException e) {
			logger.error(getClass().getName() + methodName + e);
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				int resourceRatioUpdated = projectDao.updateResourceRatioWithRequest(projectID, targetResourceID, currentWeek, requestID, updateRatio);
				response = addInt("resourceratioupdated", resourceRatioUpdated, response);
			}
			catch (DalException e) {
				logger.error(getClass().getName() + methodName + e.getErrorMessage());
				response = setError(e.getErrorCode());
			}
			catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				response = setError(0);
			}
		}
		
		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;

	}
}
