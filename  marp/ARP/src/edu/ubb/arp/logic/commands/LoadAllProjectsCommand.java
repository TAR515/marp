package edu.ubb.arp.logic.commands;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.ResourcesWorkingOnProject;
import edu.ubb.arp.exceptions.DalException;

public class LoadAllProjectsCommand extends BaseCommandOperations implements Command{
	private static final Logger logger = Logger.getLogger(LoadProjectsUserIsWorkingOnCommand.class);
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ProjectsDao projectsDao = null;
	
	
	public LoadAllProjectsCommand(JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.projectsDao = instance.getProjectsDao();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
		
	}
	
	
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		List<ResourcesWorkingOnProject> activeProjects = null;
		
		if (!errorCheck(response)) {
			try {
				activeProjects = projectsDao.getAllProjectsForManeger();
	
				Iterator<ResourcesWorkingOnProject> itr = activeProjects.iterator();
				ResourcesWorkingOnProject current = null;
				
				while(itr.hasNext()) {
					JSONObject project = new JSONObject();
					current = itr.next();
					
					project.put("projectid", current.getProjectID());
					project.put("projectname", current.getProjectName());
					project.put("openedstatus", current.isOpenedStatus());
					project.put("startweek", current.getStartWeek());
					project.put("deadline", current.getDeadLine());
					project.put("nextrelease", current.getNextRelease());
					project.put("statusname", current.getStatusName());
					
					response.add(project);
				}
					
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

