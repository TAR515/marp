package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

public interface ProjectsDao {

	public int createProject(String userName, List<Integer> ratio, String projectName, boolean openStatus, int startWeek,
			int deadLine, String nextRelease, String statusName) throws SQLException;

	public int addUserToProject(String projectName, String userName, List<Integer> week, List<Integer> ratio, boolean isLeader) throws SQLException;
	
	public int addResourceToProject(String projectName, String resourceName, List<Integer> week, List<Integer> ratio) throws SQLException;

}
