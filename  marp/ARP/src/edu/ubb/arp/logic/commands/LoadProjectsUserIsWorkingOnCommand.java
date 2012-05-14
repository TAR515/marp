package edu.ubb.arp.logic.commands;

import java.sql.SQLException;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;

public class LoadProjectsUserIsWorkingOnCommand extends BaseCommandOperations implements Command{
	private static final Logger logger = Logger.getLogger(LoadProjectsUserIsWorkingOnCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ProjectsDao projectsDao = null;
	
	
	public LoadProjectsUserIsWorkingOnCommand(JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.projectsDao = instance.getProjectsDao();
			this.request = request;
			
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
		
	}
	
	
	@Override
	public JSONArray execute() {
		return request;
		/*String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		String userName = null;
		List<Projects> activeProjects = null;
		
		try {
			userName = getString(0,"username",request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				activeProjects = projectsDao.getAllActiveProjects(userName);
	
				Set<String> s = activeProjects.keySet();
				Iterator<String> i = s.iterator();
				while(i.hasNext()) {
					Object key = i.next();
					Object value = activeProjects.get(key);
					
					JSONObject project = new JSONObject();
					
					project.put("projectname",key);
					project.put("isleader",value);
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
		return response;*/
	}

}