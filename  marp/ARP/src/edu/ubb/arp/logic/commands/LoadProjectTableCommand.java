package edu.ubb.arp.logic.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;

public class LoadProjectTableCommand extends BaseCommandOperations implements Command {
	private static final Logger logger = Logger.getLogger(LoadUserDataCommand.class);
	private JSONArray request = null;
	private JSONArray response = null;
	private DaoFactory instance = null;
	private List<Booking> bookings = null;
	private List<Resources> resources = null;
	private ProjectsDao projectsDao = null;
	private ResourcesDao resourcesDao = null;
	
	
	public LoadProjectTableCommand(JSONArray request) {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		
		try {
			this.response = new JSONArray();
			this.instance = JdbcDaoFactory.getInstance();
			
			this.projectsDao = instance.getProjectsDao();
			this.resourcesDao = instance.getResourceDao();
			
			this.request = request;
			
			this.bookings = new ArrayList<Booking>();
			this.resources = new ArrayList<Resources>();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			response = setError(0);
		}
	}
	
	@Override
	public JSONArray execute() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		int projectID = -1;
		
		try {
			projectID = getInt(0, "projectid", request);	
		} catch (IllegalStateException e) {
			logger.error(getClass().getName() + methodName + e);
			response = setError(-1);
		}
		
		if (!errorCheck(response)) {
			try {
				bookings = projectsDao.loadBooking(projectID);
				
				Iterator<Booking> itr = bookings.listIterator();
				Booking current = null;
				Set<Integer> resourceIDSet = new HashSet<Integer>();
				JSONArray bookingsArray = new JSONArray();
				
				while(itr.hasNext()) {
					JSONObject o = new JSONObject();
					current = itr.next();
					
					o.put("projectid", current.getProjectID());
					o.put("resourceid", current.getResourceID());
					o.put("week", current.getWeek());
					o.put("ratio", current.getRatio());
					o.put("isleader", current.isLeader());
					
					bookingsArray.add(o);
					resourceIDSet.add(current.getResourceID());
				}
				
				List<Integer> resourceIDList = new ArrayList<Integer>(resourceIDSet);
				Collections.sort(resourceIDList);
				
				resources = resourcesDao.loadResources(resourceIDList);
				
				Iterator<Resources> itr2 = resources.listIterator();
				Resources currentResource = null;
				JSONArray resourcesArray = new JSONArray();
				
				while(itr2.hasNext()) {
					JSONObject o = new JSONObject();
					currentResource = itr2.next();
					
					o.put("resourceid", currentResource.getResourceID());
					o.put("resourcename", currentResource.getResourceName());
					o.put("isactive", currentResource.isActive());
					o.put("resourcetypename", currentResource.getResourceTypes().getResourceTypeName());
					
					if (currentResource.getUsers() != null) {
						o.put("username", currentResource.getUsers().getUserName());
					}
					
					resourcesArray.add(o);
				}
				
				response.add(bookingsArray);
				response.add(resourcesArray);
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
