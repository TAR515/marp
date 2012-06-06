package edu.ubb.arp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.dao.model.Requests;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan
 *interface contains methods which work with the Requests
 */
public interface RequestsDao {
	/**
	 * creates a new request
	 * @param senderUserID is the id of the resource who sends the request
	 * @param targetResourceID is the id of the resource for which the request will be sent for 
	 * @param projectID is the id of the project in which the target resource is needed
	 * @param week is the week in which the target resource is needed
	 * @param ratio is the ratio of the resource the request is for
	 * @return returns the created requests id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int createNewRequest(int senderUserID, int targetResourceID, int projectID, int week, int ratio) throws SQLException,
			DalException;
	/**
	 * creates a new request
	 * @param senderResourceID is the id of the resource who sends the request
	 * @param targetResourceID is the id of the resource for which the request will be sent for
	 * @param projectID is the id of the project in which the target resource is needed
	 * @param week is the week in which the target resource is needed
	 * @param ratio is the ratio of the resource the request is for
	 * @param con is the connection to the database
	 * @return returns the created requests id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int createNewRequest(int senderResourceID, int targetResourceID, int projectID, int week, int ratio, Connection con)
			throws SQLException, DalException;
	/**
	 * updates request ratio's 
	 * @param week is the week in which the target user is needed
	 * @param ratio is the ratio of the resource the request is for
	 * @param senderUserName is the name of the user who sends the request
	 * @param targetUserName is the name of the user for which the request's ratio will be updated
	 * @param projectName is the name of the project where the target user is needed
	 * @return returns the modified requests id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateRequestRatioOfUser(List<Integer> week, List<Integer> ratio, String senderUserName, String targetUserName,
			String projectName) throws SQLException, DalException;
	/**
	 * updates request ratio's 
	 * @param week is the week in which the target resource is needed
	 * @param ratio is the week in which the target resource is needed
	 * @param senderUserName  is the name of the user who sends the request
	 * @param targetResourceName is the name of the resource for which the request's ratio will be updated
	 * @param projectName is the name of the project where the target user is needed
	 * @return returns the modified requests id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateRequestRatioOfResource(List<Integer> week, List<Integer> ratio, String senderUserName,
			String targetResourceName, String projectName) throws SQLException, DalException;
	/**
	 * removes requests from resource
	 * @param resourceID is the id of the resource from which the request will be removed
	 * @param requestID is the id of the request which will be removed from the resource
	 * @param projectID is the id of the project in which the request will be removed
	 * @return	returns the id of the request which was removed from the resource if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeRequestFromSomebody(int resourceID, int requestID, int projectID) throws SQLException, DalException;
	/**
	 * removes expired requests
	 * @param currentWeek is the starting week from where the requests will be removed
	 * @return the number of the removed requests if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeExpiredRequests(int currentWeek) throws SQLException, DalException;
	/**
	 * load requests visible to user
	 * @param userName is the name of the user who's visible requests will be loaded
	 * @return returns the id of the user 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<Requests> loadRequests(String userName) throws SQLException, DalException;

}
