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
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 * updates a resource's ratio in a project
 */
public class UpdateResourceRatioCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(UpdateResourceRatioCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ProjectsDao projectDao = null;
	
	/**
	 * constructor
	 * @param request contains the necessary data to update the ratio of the resource
	 */
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
	/**
	 * @return return the id of the resource which's ratio was updated or an error message
	 */
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		
		int projectID = -1;
		int targetResourceID = -1;
		int senderResourceID = -1;
		int startWeek = -1;
		int endWeek = -1;
		ArrayList <Integer> updateRatio = null;
		ArrayList <Integer> requestRatio = null;
		
		try {
			projectID = getInt(0,"projectid", request);
			targetResourceID = getInt(0,"targetresourceid",request);
			senderResourceID = getInt(0,"senderresourceid", request);
			startWeek = getInt(0,"startweek", request);
			endWeek = getInt(0,"endweek", request);
			updateRatio = new ArrayList<Integer>();
			requestRatio = new ArrayList<Integer>();
			
			for(int j = 0;j<getJSONArray(1, request).size();j++){
				updateRatio.add(getInt(j,"updateratio", getJSONArray(1, request)));
				requestRatio.add(getInt(j,"requestratio", getJSONArray(1, request)));
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
				int resourceRatioUpdated = projectDao.updateResourceRatioInProject(projectID, senderResourceID, targetResourceID, startWeek, endWeek, updateRatio, requestRatio);
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
