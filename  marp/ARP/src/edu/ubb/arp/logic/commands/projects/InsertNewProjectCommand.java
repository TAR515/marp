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

public class InsertNewProjectCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(InsertNewProjectCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ProjectsDao projectDao = null;
	
	public InsertNewProjectCommand (JSONArray request) {
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
		
		String userName = null;
		String projectName = null;
		boolean openedStatus = false;
		int startWeek = 0;
		int deadline = 0;
		String nextRelease = null;
		String statusName = null;
		ArrayList <Integer> ratio = null;
		
		try {
			userName = getString(0,"username",request);
			projectName = getString(0,"projectname", request);
			openedStatus = getBool(0,"openedstatus", request);
			startWeek = getInt(0,"startweek", request);
			deadline = getInt(0,"deadline", request);
			nextRelease = getString(0,"nextrelease", request);
			statusName = getString(0,"statusname", request);
			ratio = new ArrayList<Integer>();
			for(int j = 0;j<getJSONArray(1, request).size();j++){
				ratio.add(getInt(j,"ratio", getJSONArray(1, request)));
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
				int projectCreated = projectDao.createProject(userName, ratio, projectName, openedStatus, startWeek, deadline, nextRelease, statusName);
				response = addInt("projectcreated", projectCreated, response);
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
