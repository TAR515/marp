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
 *add a resource to a project
 */
public class AddResourceToprojectCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(AddResourceToprojectCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ProjectsDao projectDao = null;

	/**
	 * constructor 
	 * @param request contains the data needed to add the resource to the project
	 */
	public AddResourceToprojectCommand(JSONArray request) {
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
	 * @return returns the id of the inserted resource if there was no error , otherwise an error message
	 */
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");

		int projectID = -1;
		int senderResourceID = -1;
		int targetResourceID = -1;
		int startWeek = -1;
		int endWeek = -1;
		boolean isLeader = false;
		ArrayList<Integer> insertRatio = null;
		ArrayList<Integer> requestRatio = null;

		try {
			projectID = getInt(0, "projectid", request);
			senderResourceID = getInt(0, "senderresourceid", request);
			targetResourceID = getInt(0, "targetresourceid", request);
			startWeek = getInt(0, "startweek", request);
			endWeek = getInt(0, "endweek", request);
			isLeader = getBool(0, "isleader", request);
			insertRatio = new ArrayList<Integer>();
			requestRatio = new ArrayList<Integer>();

			for (int j = 0; j < getJSONArray(1, request).size(); j++) {
				requestRatio.add(getInt(j, "requestratio", getJSONArray(1, request)));
				insertRatio.add(getInt(j, "insertratio", getJSONArray(1, request)));
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
				int resourceAddedToProject = projectDao.addResourceToProject(projectID, senderResourceID, targetResourceID,
						startWeek, endWeek, isLeader, insertRatio, requestRatio);
				response = addInt("resourceaddedtoproject", resourceAddedToProject, response);
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
