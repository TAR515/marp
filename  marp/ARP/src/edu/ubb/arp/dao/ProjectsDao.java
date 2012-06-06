package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.dao.model.ResourcesWorkingOnProject;
import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *interface contains methods which work with the Projects
 */
public interface ProjectsDao {
	/**
	 * creates a new project
	 * @param projectName is the name of the new project
	 * @param openedStatus is the status of the new project (opened/closed) 
	 * @param startWeek is the starting week of the project
	 * @param endWeek is the end week of the project
	 * @param deadLine is the deadline of the project
	 * @param nextRelease is the projects next release version
	 * @param statusName is the current status of the project
	 * @param resourceID the id of the resource who is the leader of the new project
	 * @param insertRatio is the ratio with which the resource will be booked in the new project  
	 * @param requestRatio is the the ratio of the request for the resource
	 * @return returns the created projects id if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int createProject(String projectName, boolean openedStatus, int startWeek, int endWeek, int deadLine,
			String nextRelease, String statusName, int resourceID, List<Integer> insertRatio,
			List<Integer> requestRatio) throws SQLException, DalException;
	/**
	 * adds a resource to the project
	 * @param projectID is the id of the project in which the target resource is added
	 * @param senderResourceID is the id of the resource who adds the target resource to the project
	 * @param targetResourceID is the id of the resource which will be added to the project
	 * @param startWeek is the starting week from which the target resource will be booked
	 * @param endWeek is the last week where the resource will be booked 
	 * @param isLeader is true if the target resource will be leader in the project or false if not
	 * @param insertRatio is the ratio with which the resource will be booked in the new project
	 * @param requestRatio is the the ratio of the request for the resource
	 * @return returns the id of the project , the resource was added to if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addResourceToProject(int projectID, int senderResourceID, int targetResourceID, int startWeek, int endWeek,
			boolean isLeader, List<Integer> insertRatio, List<Integer> requestRatio) throws SQLException, DalException;
	/**
	 * sets the status of the project
	 * @param projectID is the id of the project which's openeStatus will be modified
	 * @param openStatus is the new status of the project
	 * @return returns the id of the project which's status was modified if there was no error , else returns an error message
	 * @throws SQLException  if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int setOpenStatus(int projectID, boolean openStatus) throws SQLException, DalException;
	/**
	 * sets the status of the project
	 * @param projectName is the name of the project which's openeStatus will be modified
	 * @param openStatus is the new status of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void setOpenStatus(String projectName, boolean openStatus) throws SQLException, DalException;
	/**
	 * sets the name of the project
	 * @param projectID is the id of the project which's name will be modified
	 * @param newProjectName is the new name of the project
	 * @return returns the id of the project which's name was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int setProjectName(int projectID, String newProjectName) throws SQLException, DalException;
	/**
	 * sets the name of the project
	 * @param oldProjectName is the name of the project which's name will be modified
	 * @param newProjectName newProjectName is the new name of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void setProjectName(String oldProjectName, String newProjectName) throws SQLException, DalException;
	/**
	 * sets the deadline of the project
	 * @param projectID is the id of the project which's deadline will be modified
	 * @param newDeadLine is the new deadline of the project
	 * @return returns the id of the project which's deadline was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int setDeadLine(int projectID, int newDeadLine) throws SQLException, DalException;
	/**
	 * sets the deadline of the project
	 * @param projectName is the name of the project which's deadline will be modified
	 * @param newDeadLine is the new deadline of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void setDeadLine(String projectName, int newDeadLine) throws SQLException, DalException;
	/**
	 * sets the next release version of the project
	 * @param projectID is the id of the project which's next release version will be modified
	 * @param newNextRelease is the new next release version of the project
	 * @return returns the id of the project which's next release version was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int setNextRelease(int projectID, String newNextRelease) throws SQLException, DalException;
	/**
	 * sets the next release version of the project
	 * @param projectName is the name of the project which's next release version will be modified
	 * @param newNextRelease is the new next release version of the project
	 * @throws SQLException  if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void setNextRelease(String projectName, String newNextRelease) throws SQLException, DalException;
	/**
	 * sets projects current status
	 * @param projectID is the id of the project which's current status will be modified
	 * @param newCurrentStatus is the new current status of the project
	 * @return  the id of the project which's current status was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int setCurrentStatus(int projectID, String newCurrentStatus) throws SQLException, DalException;
	/**
	 * sets projects current status
	 * @param projectName is the name of the project which's current status will be modified
	 * @param newCurrentStatus is the new current status of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void setCurrentStatus(String projectName, String newCurrentStatus) throws SQLException, DalException;
	/**
	 * removes a user from a project
	 * @param projectID is the id of the project , from which the user will be removed
	 * @param userID is the id of the user which will be removed from the project
	 * @param currentWeek is the week from which the user will be removed from the project
	 * @throws SQLException  if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void removeUserFromProject(int projectID, int userID, int currentWeek) throws SQLException, DalException;
	/**
	 * removes a user from a project
	 * @param projectName is the name of the project , from which the user will be removed
	 * @param userName is the name of the user which will be removed from the project
	 * @param currentWeek is the week from which the user will be removed from the project
	 * @return	returns the id of the removed user if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeUserFromProject(String projectName, String userName, int currentWeek) throws SQLException, DalException;
	/**
	 * removes a resource from a project
	 * @param projectName is the name of the project , from which the user will be removed
	 * @param resourceName is the name of the resource which will be removed from the project
	 * @param currentWeek is the week from which the resource will be removed from the project
	 * @return returns the id of the removed resource if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeResourceFromProject(String projectName, String resourceName, int currentWeek) throws SQLException,
			DalException;
	/**
	 * updates a resource's ratio
	 * @param projectID is the id of the project in which the target resource's ratio will modified
	 * @param senderResourceID is the id of the resource who modifies the target resource's ratio
	 * @param targetResourceID is the id of the resource which's ratio will be modified 
	 * @param startWeek is the start week from which the resource's ratio will be modified
	 * @param endWeek is the last week where resource's ratio will be modified  
	 * @param updateRatio is the new ratio 
	 * @param requestRatio is the ratio of the request for the target resource
	 * @return returns the id of the resource who's ratio was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateResourceRatioInProject(int projectID, int senderResourceID, int targetResourceID, int startWeek,
			int endWeek, List<Integer> updateRatio, List<Integer> requestRatio) throws SQLException, DalException;
	/**
	 * updates a resource's ratio and books the resource to which was the request for
	 * @param projectID is the id of the project in which the target resource's ratio will modified
	 * @param targetResourceID is the id of the resource which's ratio will be modified
	 * @param currentWeek is the week where the resource ratio will be modified
	 * @param requestID is the id of the request for the target resource
	 * @param updateRatio is the new ratio of the target resource
	 * @return returns the id of the resource who's ratio was modified if there was no error , else returns an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateResourceRatioWithRequest(int projectID,int targetResourceID,
			int currentWeek,int requestID,int updateRatio) throws SQLException, DalException;
	/**
	 * updates the user to leader or user in project
	 * @param projectName is the name of the projects in which the user will be updated to leader or user
	 * @param userName is the name of the user who will be updated to leader or user
	 * @param currentWeek is the week from which the user will be leader or user in the project
	 * @param isLeader is true if the user will be leader or false if not
	 * @return returns the id of the user who was modified to leader or user if there was no error , else returns an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateUserIsLeader(String projectName, String userName, int currentWeek, boolean isLeader) throws SQLException,
			DalException;
	/**
	 * returns the bookings of the resources in the project
	 * @param projectID is the id of the project which's resource booking will be loaded
	 * @return returns the id of the project which's resource booking was be loaded if there was no error , else returns an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<Booking> loadBooking(int projectID) throws SQLException, DalException;
	/**
	 * returns the projects , the user is working in
	 * @param userName is the name of the user , which's projects he is working in will be loaded
	 * @return returns the projects , the user is working in if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<ResourcesWorkingOnProject> getAllActiveProjects(String userName) throws SQLException, DalException;
	/**
	 * returns all projects 
	 * @return returns all projects 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<ResourcesWorkingOnProject> getAllProjectsForManeger() throws SQLException, DalException;
}
