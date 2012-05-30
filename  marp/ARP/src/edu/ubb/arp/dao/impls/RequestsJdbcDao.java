package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.RequestsDao;
import edu.ubb.arp.exceptions.DalException;

public class RequestsJdbcDao extends BaseDao implements RequestsDao {
	public RequestsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

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
	
	
	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
