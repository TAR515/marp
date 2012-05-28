package edu.ubb.arp.logic.commands.resources;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;
import edu.ubb.arp.logic.commands.BaseCommandOperations;
import edu.ubb.arp.logic.commands.Command;

public class LoadResourceEngagedCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(LoadResourceEngagedCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	List<Integer> result = null;
	private ResourcesDao resourceDao = null;
	
	public LoadResourceEngagedCommand (JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.resourceDao = instance.getResourceDao();
			this.request = request;
			this.result = new ArrayList<Integer>();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
		
	}
	
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		
		int resourceID = 0;
		int startWeek = 0;
		int endWeek = 0;
		
		try {
			resourceID = getInt(0,"resourceid", request);
			startWeek = getInt(0,"startweek", request);
			endWeek = getInt(0,"endweek", request);
			
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			System.out.println("illegal state exception");
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				result = resourceDao.loadResourceEngages(resourceID, startWeek, endWeek);

				Iterator<Integer> it = result.iterator();
				while (it.hasNext()) {
					addInt("ratio", it.next(), response);
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
