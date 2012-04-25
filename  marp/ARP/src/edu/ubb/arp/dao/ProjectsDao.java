package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

public interface ProjectsDao {

	public int createProject(String userName, List<Integer> ratio, String projectName, boolean openStatus, int startWeek,
			int deadLine, String nextRelease, String statusName) throws SQLException;

	public int addUserToProject(String projectName, String userName, List<Integer> week, List<Integer> ratio, boolean isLeader)
			throws SQLException;

	public int addResourceToProject(String projectName, String resourceName, List<Integer> week, List<Integer> ratio)
			throws SQLException;

	public int setOpenStatus(int projectID, boolean openStatus) throws SQLException;

	public int setOpenStatus(String projectName, boolean openStatus) throws SQLException;

	public int setProjectName(int projectID, String newProjectName) throws SQLException;

	public int setProjectName(String oldProjectName, String newProjectName) throws SQLException;

	public int setDeadLine(int projectID, int newDeadLine) throws SQLException;

	public int setDeadLine(String projectName, int newDeadLine) throws SQLException;

	public int setNextRelease(int projectID, String newNextRelease) throws SQLException;

	public int setNextRelease(String projectName, String newNextRelease) throws SQLException;

	public int setCurrentStatus(int projectID, String newCurrentStatus) throws SQLException;

	public int setCurrentStatus(String projectName, String newCurrentStatus) throws SQLException;
	
	public int removeUserFromProject(int projectID, int userID, int currentWeek) throws SQLException;
	
	public int removeUserFromProject(String projectName, String userName, int currentWeek) throws SQLException;
	
	public int removeResourceFromProject(String projectName, String resourceName, int currentWeek) throws SQLException;
	
	public int updateUserRatioInProject(String projectName,String userName, List<Integer> week, List<Integer> ratio) throws SQLException;
	
	public int updateResourceRatioInProject(String projectName,String resourceName, List<Integer> week, List<Integer> ratio) throws SQLException;
	
	public int updateUserIsLeader(String projectName, String userName, int currentWeek, boolean isLeader) throws SQLException;

}
