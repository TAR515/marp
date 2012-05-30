package edu.ubb.arp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.exceptions.DalException;

public interface RequestsDao {

	public int createNewRequest(int senderUserID, int targetResourceID, int projectID, int week, int ratio) throws SQLException,
			DalException;

	public int createNewRequest(int senderResourceID, int targetResourceID, int projectID, int week, int ratio, Connection con)
			throws SQLException, DalException;

	public int updateRequestRatioOfUser(List<Integer> week, List<Integer> ratio, String senderUserName, String targetUserName,
			String projectName) throws SQLException, DalException;

	public int updateRequestRatioOfResource(List<Integer> week, List<Integer> ratio, String senderUserName,
			String targetResourceName, String projectName) throws SQLException, DalException;

	public int removeRequestFromSomebody(int resourceID, int requestID, int projectID) throws SQLException, DalException;

	public int removeExpiredRequests(int currentWeek) throws SQLException, DalException;

}
