package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.RequestsDao;
import edu.ubb.arp.exceptions.DalErrorMessages;

public class RequestsJdbcDao extends BaseDao implements RequestsDao {
	public RequestsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	public int createNewRequest(List<Integer> week, List<Integer> ratio, String senderUserName,
			String targetResourceName, String projectName, Connection con) throws SQLException {
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
						errmsg = createNewRequestForMoreWeeks(fromWeek, w - 1, currentRatio, senderUserName, targetResourceName, projectName, connection);
						if (errmsg < 0) {
							logger.error(getClass().getName() + methodName + DalErrorMessages.errCodeToMessage(errmsg));
							return errmsg;
						}
						currentRatio = r;
						fromWeek = w;
					}
				}
				
				if (errmsg < 0) {
					logger.error(getClass().getName() + methodName + DalErrorMessages.errCodeToMessage(errmsg));
					return errmsg;
				}
				connection.commit();
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
			} finally {
				closeSQLObjects(connection, null, null);
				logger.debug(getClass().getName() + methodName + "-> EXIT");
			}
		} else {
			logger.error(getClass().getName() + methodName + DalErrorMessages.errCodeToMessage(-5));
			return -5; // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}
	
	private int createNewRequestForMoreWeeks(int startWeek, int endWeek, int ratio, String senderUserName,
			String targetResourceName, String projectName, Connection con) throws SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "new_requests", 7);

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
				logger.error(getClass().getName() + methodName + DalErrorMessages.errCodeToMessage(errmsg));
				return errmsg;
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
