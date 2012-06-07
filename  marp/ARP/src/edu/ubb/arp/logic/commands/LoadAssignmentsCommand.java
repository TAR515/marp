package edu.ubb.arp.logic.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *loads booking of the user given in the request 
 */
public class LoadAssignmentsCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(LoadAssignmentsCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private UsersDao userDao = null;
	
	/**
	 * constructor
	 * @param request is the request that came from the client containing a user name and a week
	 */
	public LoadAssignmentsCommand (JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.userDao = instance.getUsersDao();
			this.request = request;
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
		
	}
	/**
	 * @return returns the booking of the given user (from the current week to current week + 3)
	 */
	@Override
	public JSONArray execute()  {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		List<Booking> result = new ArrayList<Booking>();
		String keys[] = new String[5];
		int elements[] = new int[4];
		Boolean element = new Boolean(false);
		Booking booking = new Booking();
		
		String userName = null;
		int currentWeek = -1;
		
		try {
			userName = getString(0,"username", request);
			currentWeek = getInt(0,"currentweek", request);
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		}
		if (!errorCheck(response)) {
			try {
					result = userDao.LoadAssignments(userName,currentWeek);
					Iterator<Booking> it = result.iterator();
					while (it.hasNext()) {
						booking = it.next();
						keys[0] = "resourceid";
						keys[1] = "projectid";
						keys[2] = "week";
						keys[3] = "ratio";
						keys[4] = "isleader";
						elements[0] = booking.getResourceID();
						elements[1] = booking.getProjectID();
						elements[2] = booking.getWeek();
						elements[3] = (int) booking.getRatio();
						element = booking.isLeader();
	
						response = addMoreIntAndOneBool(keys, elements, element, response);
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
