package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.model.ResourcesWorkingOnProject;
import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;

public class ProjectsJdbcDao extends BaseDao implements ProjectsDao {

	public ProjectsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	public int createProject(String userName, List<Integer> ratio, String projectName, boolean openStatus, int startWeek,
			int deadLine, String nextRelease, String statusName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		int endWeek = ((deadLine - startWeek) > 24) ? startWeek + 24 : deadLine;

		try {
			connection = getConnection();

			errmsg = createJustProject(projectName, openStatus, startWeek, deadLine, nextRelease, statusName, connection);

			addUserToUserInProject(projectName, userName, Boolean.TRUE, connection);

			List<Integer> weeks = new ArrayList<Integer>();
			for (int i = startWeek; i <= endWeek; i++) {
				weeks.add(i);
			}

			addUserToBookingByUserName(projectName, userName, weeks, ratio, Boolean.TRUE, connection);

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

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}

	public void addUserToProject(String projectName, String userName, List<Integer> week, List<Integer> ratio, boolean isLeader)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		try {
			connection = getConnection();

			addUserToBookingByUserName(projectName, userName, week, ratio, isLeader, connection);

			addUserToUserInProject(projectName, userName, isLeader, connection);

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
	}

	public void addResourceToProject(String projectName, String resourceName, List<Integer> week, List<Integer> ratio)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		try {
			connection = getConnection();

			addResourcesToBookingByResourceName(projectName, resourceName, week, ratio, connection);
			
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
	}

	public void setOpenStatus(int projectID, boolean newOpenStatus) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_open_status_by_project_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setBoolean(stmt, paramIndex++, newOpenStatus);
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
	}

	public void setOpenStatus(String projectName, boolean newOpenStatus) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_open_status_by_project_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setBoolean(stmt, paramIndex++, newOpenStatus);
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
	}

	public void setProjectName(int projectID, String newProjectName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_name_by_project_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setString(stmt, paramIndex++, newProjectName);
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
	}

	public void setProjectName(String oldProjectName, String newProjectName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_name_by_project_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, oldProjectName);
			setString(stmt, paramIndex++, newProjectName);
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
	}

	public void setDeadLine(int projectID, int newDeadLine) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_deadline_by_project_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, newDeadLine);
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
	}

	public void setDeadLine(String projectName, int newDeadLine) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_deadline_by_project_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setInt(stmt, paramIndex++, newDeadLine);
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
	}

	public void setNextRelease(int projectID, String newNextRelease) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_next_release_version_by_project_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setString(stmt, paramIndex++, newNextRelease);
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
	}

	public void setNextRelease(String projectName, String newNextRelease) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_next_release_version_by_project_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, newNextRelease);
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
	}

	public void setCurrentStatus(int projectID, String newCurrentStatus) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_current_status_by_project_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setString(stmt, paramIndex++, newCurrentStatus);
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
	}

	public void setCurrentStatus(String projectName, String newCurrentStatus) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "set_project_current_status_by_project_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, newCurrentStatus);
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
	}

	public void removeUserFromProject(int projectID, int userID, int currentWeek) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "remove_user_from_project_by_user_id", 4);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, userID);
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
			closeSQLObjects(connection, null, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void removeUserFromProject(String projectName, String userName, int currentWeek) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "remove_user_from_project_by_user_name", 4);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, userName);
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
			closeSQLObjects(connection, null, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void removeResourceFromProject(String projectName, String resourceName, int currentWeek) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "remove_resource_from_project_by_resource_name", 4);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, resourceName);
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
			closeSQLObjects(connection, null, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void updateUserRatioInProject(String projectName, String userName, List<Integer> week, List<Integer> ratio)
			throws SQLException, DalException {
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
						updateUserRatioInBookingForMoreWeek(projectName, userName, fromWeek, w - 1, currentRatio,
								connection);
						currentRatio = r;
						fromWeek = w;
					}
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
	}

	public void updateResourceRatioInProject(String projectName, String resourceName, List<Integer> week, List<Integer> ratio)
			throws SQLException, DalException {
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
						updateResourceRatioInBookingForMoreWeek(projectName, resourceName, fromWeek, w - 1,
							currentRatio, connection);
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
			} catch(DalException e) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw e;
			} finally {
				closeSQLObjects(connection, null, null);
				logger.debug(getClass().getName() + methodName + "-> EXIT");
			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
	}

	public void updateUserIsLeader(String projectName, String userName, int currentWeek, boolean isLeader) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_in_project_to_leader_or_user", 5);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, userName);
			setInteger(stmt, paramIndex++, currentWeek);
			setBoolean(stmt, paramIndex++, isLeader);
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
	}

	// User

	/** Add a user to UserInProject table by projectid and userid.
	 * 
	 * @param projectID
	 * @param userID
	 * @param isLeader
	 * @return errmsg
	 * @throws SQLException */
	@SuppressWarnings("unused")
	private void addUserToUserInProject(int projectID, int userID, boolean isLeader, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "add_user_to_project_by_user_id", 4);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, userID);
			setBoolean(stmt, paramIndex++, isLeader);
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
	}

	/** Add a user to UserInProject table by projectname and username. This do not close the connection or the transaction!
	 * 
	 * @param projectID
	 * @param userID
	 * @param isLeader
	 * @return errmsg
	 * @throws SQLException */
	private void addUserToUserInProject(String projectName, String userName, boolean isLeader, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "add_user_to_project_by_user_name", 4);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, userName);
			setBoolean(stmt, paramIndex++, isLeader);
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
	}

	/** Insert into Booking table more coulumns (USER). This method dont close the connection or the transaction!
	 * 
	 * @param projectName
	 * @param userName
	 * @param week
	 * @param ratio
	 * @param isLeader
	 * @param con
	 * @return errmsg
	 * @throws SQLException */
	private void addUserToBookingByUserName(String projectName, String userName, List<Integer> week, List<Integer> ratio,
			boolean isLeader, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		if (week.size() == ratio.size() && week.size() != 0) {
			int currentRatio = ratio.get(0);
			int fromWeek = week.get(0);
			int r = 0;
			int w = 0;

			week.add(week.get(week.size() - 1) + 1);
			ratio.add(ratio.get(ratio.size() - 1) + 1);
			Iterator<Integer> weekIt = week.iterator();
			Iterator<Integer> ratioIt = ratio.iterator();

			while (weekIt.hasNext()) {
				r = ratioIt.next();
				w = weekIt.next();
				if (r != currentRatio) { // end of a part
					try {
						addUserToBookingForMoreWeekByUserName(projectName, userName, fromWeek, w - 1, currentRatio,
							isLeader, con);
					} catch(DalException e) {
						logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
						throw e;
					}
					currentRatio = r;
					fromWeek = w;
				}
			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
	}

	/** Add a User to Booking.(USER) This method have a start week and an end week. The ratio between these are equals. This method dont
	 * close the connection or the transaction!
	 * 
	 * @param projectName
	 * @param userName
	 * @param startWeek
	 * @param endWeek
	 * @param ratio
	 * @param isLeader
	 * @param con
	 * @return
	 * @throws SQLException */
	private void addUserToBookingForMoreWeekByUserName(String projectName, String userName, int startWeek, int endWeek, int ratio,
			boolean isLeader, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "add_resource_to_project_by_user_name_for_n_weeks", 7);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, userName);
			setInteger(stmt, paramIndex++, startWeek);
			setInteger(stmt, paramIndex++, endWeek);
			setInteger(stmt, paramIndex++, ratio);
			setBoolean(stmt, paramIndex++, isLeader);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg) + " " + startWeek
						+ " - " + endWeek);
				throw new DalException(errmsg);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	// Resource

	/** Add a resource to Booking for more weeks. (NOT USER)
	 * 
	 * @param projectID
	 * @param resourceID
	 * @param startWeek
	 * @param endWeek
	 * @param ratio
	 * @param isLeader
	 * @param con
	 * @return
	 * @throws SQLException */
	private void addResourceToBookingForMoreWeekByResourceID(int projectID, int resourceID, int startWeek, int endWeek, int ratio,
			Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "add_resource_to_project_by_resource_id_for_n_weeks", 7);

			int paramIndex = 1;
			setInteger(stmt, paramIndex++, projectID);
			setInteger(stmt, paramIndex++, resourceID);
			setInteger(stmt, paramIndex++, startWeek);
			setInteger(stmt, paramIndex++, endWeek);
			setInteger(stmt, paramIndex++, ratio);
			setBoolean(stmt, paramIndex++, false);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg) + " " + startWeek
						+ " - " + endWeek);
				throw new DalException(errmsg);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	/** Add a resource to booking for more weeks. (NOT USER)
	 * 
	 * @param projectID
	 * @param resourceID
	 * @param week
	 * @param ratio
	 * @param isLeader
	 * @param con
	 * @return
	 * @throws SQLException */
	@SuppressWarnings("unused")
	private void addResourcesToBookingByResourceID(int projectID, int resourceID, List<Integer> week, List<Integer> ratio,
			Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		if (week.size() == ratio.size() && week.size() != 0) {
			int currentRatio = ratio.get(0);
			int fromWeek = week.get(0);
			int r = 0;
			int w = 0;

			week.add(week.get(week.size() - 1) + 1);
			ratio.add(ratio.get(ratio.size() - 1) + 1);
			Iterator<Integer> weekIt = week.iterator();
			Iterator<Integer> ratioIt = ratio.iterator();

			while (weekIt.hasNext()) {
				r = ratioIt.next();
				w = weekIt.next();
				if (r != currentRatio) { // end of a part
					try {
						addResourceToBookingForMoreWeekByResourceID(projectID, resourceID, fromWeek, w - 1, currentRatio,
								con);
					} catch(DalException e) {
						logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
						throw e;
					}
					currentRatio = r;
					fromWeek = w;
				}
			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
	}

	private void addResourcesToBookingByResourceName(String projectName, String resourceName, List<Integer> week,
			List<Integer> ratio, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		if (week.size() == ratio.size() && week.size() != 0) {
			int currentRatio = ratio.get(0);
			int fromWeek = week.get(0);
			int r = 0;
			int w = 0;

			week.add(week.get(week.size() - 1) + 1);
			ratio.add(ratio.get(ratio.size() - 1) + 1);
			Iterator<Integer> weekIt = week.iterator();
			Iterator<Integer> ratioIt = ratio.iterator();

			while (weekIt.hasNext()) {
				r = ratioIt.next();
				w = weekIt.next();
				if (r != currentRatio) { // end of a part
					try {
						addResourceToBookingForMoreWeekByResourceName(projectName, resourceName, fromWeek, w - 1,
								currentRatio, con);
					} catch(DalException e) {
						logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
						throw e;
					}
					currentRatio = r;
					fromWeek = w;
				}
			}
		} else {
			logger.error(getClass().getName() + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
	}

	private void addResourceToBookingForMoreWeekByResourceName(String projectName, String resourceName, int startWeek,
			int endWeek, int ratio, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "add_resource_to_project_by_resource_name_for_n_weeks", 6);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, resourceName);
			setInteger(stmt, paramIndex++, startWeek);
			setInteger(stmt, paramIndex++, endWeek);
			setInteger(stmt, paramIndex++, ratio);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg) + " " + startWeek
						+ " - " + endWeek);
				throw new DalException(errmsg);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	private void updateUserRatioInBookingForMoreWeek(String projectName, String userName, int startWeek, int endWeek,
			int newRatio, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "update_user_ratio_in_project_for_n_weeks_by_user_name", 6);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, userName);
			setInt(stmt, paramIndex++, startWeek);
			setInt(stmt, paramIndex++, endWeek);
			setInt(stmt, paramIndex++, newRatio);
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
	}

	private void updateResourceRatioInBookingForMoreWeek(String projectName, String resourceName, int startWeek, int endWeek,
			int newRatio, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "update_resource_ratio_in_project_for_n_weeks_by_resource_name", 6);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, resourceName);
			setInt(stmt, paramIndex++, startWeek);
			setInt(stmt, paramIndex++, endWeek);
			setInt(stmt, paramIndex++, newRatio);
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
	}

	// Others

	/** Returns the resourceId of a user. This method dont close the connection or the transaction.
	 * 
	 * @param userName
	 * @return resourceID or errmsg.
	 * @throws SQLException */
	@SuppressWarnings("unused")
	private void getResourceIDFromUserName(String userName, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "load_resource_id_of_user_name", 2);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
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
	}

	/** Insert a project in the database. It do not insert in Booking or in UserInProject table. This dont close the connection or the
	 * transaction.
	 * 
	 * @param projectName
	 * @param openStatus
	 * @param startWeek
	 * @param deadLine
	 * @param nextRelease
	 * @param statusName
	 * @param con
	 * @return errmsg
	 * @throws SQLException */
	private int createJustProject(String projectName, boolean openStatus, int startWeek, int deadLine, String nextRelease,
			String statusName, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "new_project", 7);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			setBoolean(stmt, paramIndex++, openStatus);
			setInt(stmt, paramIndex++, startWeek);
			setInt(stmt, paramIndex++, deadLine);
			setString(stmt, paramIndex++, nextRelease);
			setString(stmt, paramIndex++, statusName);
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
	public List<Booking> loadBooking(int projectID) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		List<Booking> result = new ArrayList<Booking>();
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		ResultSet rs = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_booking_by_project_id", 2);

			int paramIndex = 1;
			setInteger(stmt, paramIndex++, projectID);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
			
			while (rs.next()) { 
				result.add(fillBooking(rs)); 
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return result;
	}
	
	protected Booking fillBooking(ResultSet rs) throws SQLException {
		Booking result = new Booking();

		result.setProjectID(getInt(rs, "ProjectID"));
		result.setResourceID(getInt(rs, "ResourceID"));
		result.setWeek(getInt(rs, "Week"));
		result.setRatio(getInt(rs, "Ratio"));
		result.setLeader(getBool(rs, "IsLeader"));

		return result;
	}
	
	public List<ResourcesWorkingOnProject> getAllActiveProjects(String userName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		List<ResourcesWorkingOnProject> resoult = null;

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			resoult = new ArrayList<ResourcesWorkingOnProject>();
			stmt = createProcedure(connection, "load_projects_user_is_working_on", 2);

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
				resoult.add(fillProjectsWithIsLeader(rs));
			}	
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return resoult;
	}

	
	protected ResourcesWorkingOnProject fillProjectsWithIsLeader(ResultSet rs) throws SQLException {
		ResourcesWorkingOnProject retValue = new ResourcesWorkingOnProject();
		
		retValue.setProjectID(getInt(rs, "ProjectID"));
		retValue.setProjectName(getString(rs, "ProjectName"));
		retValue.setOpenedStatus(getBool(rs, "OpenedStatus"));
		retValue.setStartWeek(getInt(rs, "StartWeek"));
		retValue.setDeadLine(getInt(rs, "Deadline"));
		retValue.setNextRelease(getString(rs, "NextRelease"));
		retValue.setStatusName(getString(rs, "StatusName"));
		retValue.setLeader(getBool(rs, "IsLeader"));
			
		return retValue;
	}
	
	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}



}
