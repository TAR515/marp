package edu.ubb.arp.logic.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.Resources;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *loads all active resources from the database 
 */
public class LoadAllActiveResourcesCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(LoadAllActiveResourcesCommand.class);
	private JSONArray response = null;
	private DaoFactory instance = null;
	private ResourcesDao resourceDao = null;
	
	/**
	 * constructor 
	 */
	public LoadAllActiveResourcesCommand (JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			this.resourceDao = instance.getResourceDao();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
		
	}
	/**
	 * @return returns all active resources in a JSONArray
	 */
	@Override
	public JSONArray execute()  {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		List<Resources> result = new ArrayList<Resources>();
		String keys[] = new String[2];
		int intElement = -1;
		String stringElement = new String();
		Resources resources = new Resources();
			try{
				result = resourceDao.LoadAllActiveResources();
				Iterator<Resources> it = result.iterator();
				while(it.hasNext()) {
					resources = it.next();
					keys[0] = "resourceid";
					keys[1] = "resourcename";
					intElement = resources.getResourceID();
					stringElement = resources.getResourceName();
					response = addIntAndString(keys, intElement, stringElement, response);
				}

			}
			catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				response = setError(0);
			}
		
		
		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return response;

	}
}
