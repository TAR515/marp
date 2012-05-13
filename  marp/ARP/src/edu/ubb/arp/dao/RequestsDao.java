package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.exceptions.DalException;

public interface RequestsDao {

	public void createNewRequestForUser(List<Integer> week, List<Integer> ratio, String senderUserName, String targetUserName,
			String projectName) throws SQLException, DalException;
	
	public void createNewRequestForResource(List<Integer> week, List<Integer> ratio, String senderUserName,
			String targetResourceName, String projectName) throws SQLException, DalException;
	
	public void updateRequestRatioOfUser(List<Integer> week, List<Integer> ratio, String senderUserName,
			String targetUserName, String projectName) throws SQLException, DalException;
	
	public void updateRequestRatioOfResource(List<Integer> week, List<Integer> ratio, String senderUserName,
			String targetResourceName, String projectName) throws SQLException, DalException;
	
	public void removeRequestFromSomebody(int resourceID, int requestID, int projectID) throws SQLException, DalException;
	
	public void removeExpiredRequests(int currentWeek) throws SQLException, DalException;
	
}
