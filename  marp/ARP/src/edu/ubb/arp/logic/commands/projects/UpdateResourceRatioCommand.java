package edu.ubb.arp.logic.commands.projects;

import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;
import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.Command;

public class UpdateResourceRatioCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(UpdateResourceRatioCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ProjectsDao projectDao = null;
	
	public UpdateResourceRatioCommand (JSONArray request) {
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
		
		String projectName = null;
		String resourceName = null;
		ArrayList <Integer> ratio = null;
		ArrayList <Integer> week = null;
		
		try {
			projectName = getString(0,"projectname", request);
			resourceName = getString(0,"resourcename",request);
			ratio = new ArrayList<Integer>();
			week = new ArrayList<Integer>();
			for(int j = 0;j<getJSONArray(1, request).size();j++){
				ratio.add(getInt(j,"ratio", getJSONArray(1, request)));
				week.add(getInt(j,"week", getJSONArray(2, request)));
			}
			
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
				int resourceRatioUpdated = projectDao.updateResourceRatioInProject(projectName, resourceName, week, ratio);
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
