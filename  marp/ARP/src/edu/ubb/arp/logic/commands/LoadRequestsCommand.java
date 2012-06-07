package edu.ubb.arp.logic.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.RequestsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.Requests;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *loads requests
 */
public class LoadRequestsCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(LoadAssignmentsCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private RequestsDao requestsDao = null;
	private List<Requests> result = null;
	
	/**
	 * constructor 
	 * @param request contains a user name
	 */
	public LoadRequestsCommand (JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.requestsDao = instance.getRequestsDao();
			this.request = request;
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
		
	}
	
	/**
	 * @return returns the requests which can be seen by the given user
	 */
	@Override
	public JSONArray execute()  {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		
		String userName = null;
		Requests currentRequest = null;
		int elements[] = null;
		String keys[] = null;
		String names[] = null;
		
		try {
			userName = getString(0,"username", request);
			currentRequest = new Requests();
			result = new ArrayList<Requests>();
			elements = new int[6];
			keys = new String[8];
			names = new String[2];
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		}
		if (!errorCheck(response)) {
			try {
				result = requestsDao.loadRequests(userName);
				Iterator<Requests> it = result.iterator();
					
				keys[0] = "requestid";
				keys[1] = "week";
				keys[2] = "ratio";
				keys[3] = "senderid";
				keys[4] = "resourceid";
				keys[5] = "projectid";
				keys[6] = "senderusername";
				keys[7] = "resourceresourcename";
				
				while(it.hasNext()) {
					currentRequest = it.next();
					elements[0] = currentRequest.getRequestID();
					elements[1] = currentRequest.getWeek();
					elements[2] = currentRequest.getRatio();
					elements[3] = currentRequest.getSender().getResourceID();
					elements[4] = currentRequest.getResource().getResourceID();
					elements[5] = currentRequest.getProject().getProjectID();
					names[0] = currentRequest.getSender().getUsers().getUserName();
					names[1] = currentRequest.getResource().getResourceName();
					response = addMoreIntAndString(keys, elements, names, response);
				}
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
