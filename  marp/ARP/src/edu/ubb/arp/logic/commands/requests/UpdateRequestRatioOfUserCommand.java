package edu.ubb.arp.logic.commands.requests;

import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.RequestsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;
import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.Command;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *updates request ratios of a user
 */
public class UpdateRequestRatioOfUserCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(UpdateRequestRatioOfUserCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private RequestsDao requestDao = null;
	
	/**
	 * 
	 * @param request contains all necessary data o update request ratios
	 */
	public UpdateRequestRatioOfUserCommand (JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.requestDao = instance.getRequestsDao();
			this.request = request;
			
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
		
	}
	/**
	 * @return returns the modified requests id or an error message
	 */
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		
		String senderUserName = null;
		String targetUserName = null;
		String projectName = null;
		ArrayList <Integer> ratio = null;
		ArrayList <Integer> week = null;
		
		try {
			senderUserName = getString(0, "username", request);
			targetUserName = getString(0,"targetusername",request);
			projectName = getString(0,"projectname", request);
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
				int ratioUpdated = requestDao.updateRequestRatioOfUser(week, ratio, senderUserName, targetUserName, projectName);
				response = addInt("ratioupdated", ratioUpdated, response);
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
