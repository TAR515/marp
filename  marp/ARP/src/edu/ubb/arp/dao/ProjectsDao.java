package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;

public interface ProjectsDao {

	public void createProject(String userName, List<Integer> ratio, String projectName, boolean openStatus, int startWeek,
			int deadLine, String nextRelease, String statusName) throws SQLException, DalException;

	public void addUserToProject(String projectName, String userName, List<Integer> week, List<Integer> ratio, boolean isLeader)
			throws SQLException, DalException;

	public void addResourceToProject(String projectName, String resourceName, List<Integer> week, List<Integer> ratio)
			throws SQLException, DalException;

	public void setOpenStatus(int projectID, boolean openStatus) throws SQLException, DalException;

	public void setOpenStatus(String projectName, boolean openStatus) throws SQLException, DalException;

	public void setProjectName(int projectID, String newProjectName) throws SQLException, DalException;

	public void setProjectName(String oldProjectName, String newProjectName) throws SQLException, DalException;

	public void setDeadLine(int projectID, int newDeadLine) throws SQLException, DalException;

	public void setDeadLine(String projectName, int newDeadLine) throws SQLException, DalException;

	public void setNextRelease(int projectID, String newNextRelease) throws SQLException, DalException;

	public void setNextRelease(String projectName, String newNextRelease) throws SQLException, DalException;

	public void setCurrentStatus(int projectID, String newCurrentStatus) throws SQLException, DalException;

	public void setCurrentStatus(String projectName, String newCurrentStatus) throws SQLException, DalException;
	
	public void removeUserFromProject(int projectID, int userID, int currentWeek) throws SQLException, DalException;
	
	public void removeUserFromProject(String projectName, String userName, int currentWeek) throws SQLException, DalException;
	
	public void removeResourceFromProject(String projectName, String resourceName, int currentWeek) throws SQLException, DalException;
	
	public void updateUserRatioInProject(String projectName,String userName, List<Integer> week, List<Integer> ratio) throws SQLException, DalException;
	
	public void updateResourceRatioInProject(String projectName,String resourceName, List<Integer> week, List<Integer> ratio) throws SQLException, DalException;
	
	public void updateUserIsLeader(String projectName, String userName, int currentWeek, boolean isLeader) throws SQLException, DalException;
	
	public List<Booking> loadBooking(int projectID) throws SQLException;

}
