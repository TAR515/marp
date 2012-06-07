package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.RequestsDao;
import edu.ubb.arp.dao.model.Projects;
import edu.ubb.arp.dao.model.Requests;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *contains methods which work with the Requests
 */
public class RequestsJdbcDao extends BaseDao implements RequestsDao {
	public RequestsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}
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
	public int createNewRequest(int senderResourceID, int targetResourceID, int projectID, int week, int ratio) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "new_request", 6);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, senderResourceID);
			setInt(stmt, paramIndex++, targetResourceID);
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, week);
			setInt(stmt, paramIndex++, ratio);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
			connection.commit();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, null, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
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
	public int createNewRequest(int senderResourceID, int targetResourceID, int projectID, int week, int ratio, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "new_request", 6);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, senderResourceID);
			setInt(stmt, paramIndex++, targetResourceID);
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, week);
			setInt(stmt, paramIndex++, ratio);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}

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
	public int updateRequestRatioOfUser(List<Integer> week, List<Integer> ratio, String senderUserName,
			String targetUserName, String projectName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		if (week.size() == ratio.size() && week.size() != 0) {
			int currentRatio = ratio.get(0);
			int fromWeek = week.get(0);
			int r = 0;
			int w = 0;
			Connection connection = null;

			week.add(week.get(week.size() - 1) + 1);
			ratio.add(ratio.get(ratio.size() - 1) + 1);
			Iterator<Integer> weekIt = week.iterator();
			Iterator<Integer> ratioIt = ratio.iterator();

			try {
				connection = getConnection();

				while (weekIt.hasNext()) {
					r = ratioIt.next();
					w = weekIt.next();
					if (r != currentRatio) { // end of a part
						errmsg = updateRequestRatioOfUserForMoreWeeks(fromWeek, w - 1, currentRatio, senderUserName,
							targetUserName, projectName, connection);
						currentRatio = r;
						fromWeek = w;
					}
				}
				
				connection.commit();
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
			} catch(DalException e1) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw e1;
			} finally {
				closeSQLObjects(connection, null, null);
				logger.debug(getClass().getName() + methodName + "-> EXIT");
			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}
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
			String targetResourceName, String projectName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		if (week.size() == ratio.size() && week.size() != 0) {
			int currentRatio = ratio.get(0);
			int fromWeek = week.get(0);
			int r = 0;
			int w = 0;
			Connection connection = null;

			week.add(week.get(week.size() - 1) + 1);
			ratio.add(ratio.get(ratio.size() - 1) + 1);
			Iterator<Integer> weekIt = week.iterator();
			Iterator<Integer> ratioIt = ratio.iterator();

			try {
				connection = getConnection();

				while (weekIt.hasNext()) {
					r = ratioIt.next();
					w = weekIt.next();
					if (r != currentRatio) { // end of a part
						errmsg = updateRequestRatioOfResourceForMoreWeeks(fromWeek, w - 1, currentRatio, senderUserName,
							targetResourceName, projectName, connection);
						currentRatio = r;
						fromWeek = w;
					}
				}

				if (errmsg < 0) {
					logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
					throw new DalException(errmsg);
				}
				connection.commit();
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
			} catch (DalException e1) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw e1;
			} finally {
				closeSQLObjects(connection, null, null);
				logger.debug(getClass().getName() + methodName + "-> EXIT");
			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}
	/**
	 * removes requests from resource
	 * @param resourceID is the id of the resource from which the request will be removed
	 * @param requestID is the id of the request which will be removed from the resource
	 * @param projectID is the id of the project in which the request will be removed
	 * @return	returns the id of the request which was removed from the resource if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeRequestFromSomebody(int resourceID, int requestID, int projectID) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "remove_request_visibility_from_leader", 4);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, resourceID);
			setInt(stmt, paramIndex++, requestID);
			setInt(stmt, paramIndex++, projectID);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
			connection.commit();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
			closeSQLObjects(connection, null, stmt);
		}
		return errmsg;
	}
	/**
	 * removes expired requests
	 * @param currentWeek is the starting week from where the requests will be removed
	 * @return the number of the removed requests if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeExpiredRequests(int currentWeek) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "remove_request_visible_of_expired_requests", 2);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, currentWeek);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
			connection.commit();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
	
	// PRIVATE
	/**
	 * updates request ratio of user for more week 
	 * @param startWeek is the starting week from where the ratio will be updated
	 * @param endWeek is the last week where the ratio will be updated
	 * @param ratio is the new ratio of the request
	 * @param senderUserName is the name of the user who sends the request
	 * @param targetUserName is the name of the user the request is for
	 * @param projectName is the name of the project where the user is needed
	 * @param con is the connection to the database
	 * @return returns the target user's resourceID if there was no error , otherwise returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	private int updateRequestRatioOfUserForMoreWeeks(int startWeek, int endWeek, int ratio, String senderUserName,
			String targetUserName, String projectName, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "update_requests_ratio_of_user", 7);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, startWeek);
			setInt(stmt, paramIndex++, endWeek);
			setInt(stmt, paramIndex++, ratio);
			setString(stmt, paramIndex++, senderUserName);
			setString(stmt, paramIndex++, targetUserName);
			setString(stmt, paramIndex++, projectName);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
	/**
	 * updates request ratio of resource for more week o
	 * @param startWeek is the starting week from where the ratio will be updated
	 * @param endWeek is the last week where the ratio will be updated
	 * @param ratio is the new ratio of the request
	 * @param senderUserName is the name of the user who sends the request
	 * @param targetResourceName is the name of the resource the request is for
	 * @param projectName is the name of the project where the user is needed
	 * @param con is the connection to the database
	 * @return returns the target user's resourceID if there was no error , otherwise returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	private int updateRequestRatioOfResourceForMoreWeeks(int startWeek, int endWeek, int ratio, String senderUserName,
			String targetResourceName, String projectName, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "update_requests_ratio_of_resource", 7);
			int paramIndex = 1;
			setInt(stmt, paramIndex++, startWeek);
			setInt(stmt, paramIndex++, endWeek);
			setInt(stmt, paramIndex++, ratio);
			setString(stmt, paramIndex++, senderUserName);
			setString(stmt, paramIndex++, targetResourceName);
			setString(stmt, paramIndex++, projectName);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
	/**
	 * load requests visible to user
	 * @param userName is the name of the user who's visible requests will be loaded
	 * @return returns the id of the user 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<Requests> loadRequests(String userName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		List<Requests> resoult = null;

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_requests_visible_to_leader", 2);
			resoult = new ArrayList<Requests>();
			
			
			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();
			
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}

			while (rs.next()) {
				resoult.add(fillObject(rs));
			}
			connection.commit();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return resoult;
	}
	/**
	 * returns a Requests object containing the result of the stored procedure
	 * @param rs contains the result of the stored procedure
	 * @return returns a Requests object containing the result of the stored procedure
	 */
	protected Requests fillObject(ResultSet rs) throws SQLException {
		Requests retValue = new Requests();
		
		Resources sender = new Resources();
		Users senderUser = new Users();
		senderUser.setUserName(getString(rs, "SenderUserName"));
		sender.setResourceID(getInt(rs, "SenderID"));
		sender.setUsers(senderUser);
		
		Resources target = new Resources();
		target.setResourceID(getInt(rs, "ResourceID"));
		target.setResourceName(getString(rs, "TargetResourceName"));
		
		Projects project = new Projects();
		project.setProjectID(getInt(rs, "ProjectID"));
		
		retValue.setRequestID(getInt(rs, "RequestID"));
		retValue.setWeek(getInt(rs, "Week"));
		retValue.setRatio(getInt(rs, "Ratio"));
		retValue.setSender(sender);
		retValue.setResource(target);
		retValue.setProject(project);
		
		return retValue;
	}
}
