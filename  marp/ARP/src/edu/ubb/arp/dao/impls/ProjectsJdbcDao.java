package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.ProjectsDao;
import edu.ubb.arp.dao.RequestsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.dao.model.ResourcesWorkingOnProject;
import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;

public class ProjectsJdbcDao extends BaseDao implements ProjectsDao {

	public ProjectsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	public int createProject(String projectName, boolean openedStatus, int startWeek, int endWeek, int deadLine,
			String nextRelease, String statusName, int resourceID, List<Integer> insertRatio,
			List<Integer> requestRatio) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;

		try {
			connection = getConnection();

			errmsg = createJustProject(projectName, openedStatus, startWeek, deadLine, nextRelease, statusName, connection);

			addResourceToProject(errmsg, resourceID, resourceID, startWeek, endWeek, true, insertRatio, requestRatio);

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

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}

	public int addResourceToProject(int projectID, int senderResourceID, int targetResourceID, int startWeek, int endWeek,
			boolean isLeader, List<Integer> insertRatio, List<Integer> requestRatio) throws SQLException, DalException {
		int currentWeek = startWeek;
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		if (insertRatio.size() == requestRatio.size() && insertRatio.size() != 0) {
			Connection connection = null;

			Iterator<Integer> updateRatioIterator = insertRatio.iterator();
			Iterator<Integer> requestRatioIterator = requestRatio.iterator();
			int currentInsertRatio = -1;
			int currentRequestRatio = -1;
			DaoFactory instance = JdbcDaoFactory.getInstance();
			RequestsDao requestDao = instance.getRequestsDao();

			try {
				connection = getConnection();
				boolean insertedYet = false;
				while (updateRatioIterator.hasNext()) {
					currentInsertRatio = updateRatioIterator.next();
					currentRequestRatio = requestRatioIterator.next();

					if (currentInsertRatio > 0) {
						if (!insertedYet) {
							insertedYet = true;
							errmsg = addResourceToUsersInProjects(projectID, targetResourceID, isLeader, connection);
						}
						addResourceToBooking(projectID, targetResourceID, currentWeek, currentInsertRatio, isLeader, connection);
					}

					if (currentRequestRatio > 0) {
						requestDao.createNewRequest(senderResourceID, targetResourceID, projectID, currentWeek,
								currentRequestRatio, connection);
					}
					currentWeek++;
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
			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}

	public int addResourceToProject(int projectID, int senderResourceID, int targetResourceID, int startWeek, int endWeek,
			boolean isLeader, List<Integer> insertRatio, List<Integer> requestRatio, Connection con) throws SQLException,
			DalException {
		int currentWeek = startWeek;
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		if (insertRatio.size() == requestRatio.size() && insertRatio.size() != 0) {
			Connection connection = null;

			Iterator<Integer> updateRatioIterator = insertRatio.iterator();
			Iterator<Integer> requestRatioIterator = requestRatio.iterator();
			int currentInsertRatio = -1;
			int currentRequestRatio = -1;
			DaoFactory instance = JdbcDaoFactory.getInstance();
			RequestsDao requestDao = instance.getRequestsDao();

			try {
				connection = con;
				boolean insertedYet = false;
				while (updateRatioIterator.hasNext()) {
					currentInsertRatio = updateRatioIterator.next();
					currentRequestRatio = requestRatioIterator.next();

					if (currentInsertRatio > 0) {
						if (!insertedYet) {
							insertedYet = true;
							errmsg = addResourceToUsersInProjects(projectID, targetResourceID, isLeader, connection);
						}
						addResourceToBooking(projectID, targetResourceID, currentWeek, currentInsertRatio, isLeader, connection);
					}

					if (currentRequestRatio > 0) {
						requestDao.createNewRequest(senderResourceID, targetResourceID, projectID, currentWeek,
								currentRequestRatio, connection);
					}
					currentWeek++;
				}
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
			} catch (DalException e1) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw e1;
			} finally {

			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}

	public int setOpenStatus(int projectID, boolean newOpenStatus) throws SQLException, DalException {
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
		return errmsg;
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

	public int setProjectName(int projectID, String newProjectName) throws SQLException, DalException {
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
		return errmsg;
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

	public int setDeadLine(int projectID, int newDeadLine) throws SQLException, DalException {
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
		return errmsg;
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

	public int setNextRelease(int projectID, String newNextRelease) throws SQLException, DalException {
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
		return errmsg;
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

	public int setCurrentStatus(int projectID, String newCurrentStatus) throws SQLException, DalException {
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
		return errmsg;
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

	public int removeUserFromProject(String projectName, String userName, int currentWeek) throws SQLException, DalException {
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
		return errmsg;
	}

	public int removeResourceFromProject(String projectName, String resourceName, int currentWeek) throws SQLException,
			DalException {
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
		return errmsg;
	}

	public int updateResourceRatioInProject(int projectID, int senderResourceID, int targetResourceID, int startWeek,
			int endWeek, List<Integer> updateRatio, List<Integer> requestRatio) throws SQLException, DalException {
		int currentWeek = startWeek;
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		if (updateRatio.size() == requestRatio.size() && updateRatio.size() != 0) {
			Connection connection = null;

			Iterator<Integer> updateRatioIterator = updateRatio.iterator();
			Iterator<Integer> requestRatioIterator = requestRatio.iterator();
			int currentUpdateRatio = -1;
			int currentRequestRatio = -1;
			DaoFactory instance = JdbcDaoFactory.getInstance();
			RequestsDao requestDao = instance.getRequestsDao();

			try {
				connection = getConnection();

				while (updateRatioIterator.hasNext()) {
					currentUpdateRatio = updateRatioIterator.next();
					currentRequestRatio = requestRatioIterator.next();

					if (currentUpdateRatio > 0) {
						updateResourceRatioInBooking(projectID, targetResourceID, currentWeek, currentUpdateRatio, connection);
					}

					if (currentRequestRatio > 0) {
						requestDao.createNewRequest(senderResourceID, targetResourceID, projectID, currentWeek,
								currentRequestRatio, connection);
					}
					currentWeek++;
				}
				connection.commit();
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
			} catch (DalException e) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw e;
			} finally {
				closeSQLObjects(connection, null, null);
			}
		} else {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-5));
			throw new DalException(-5); // WRONG_PARAMETERS
		}

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}

	public int updateUserIsLeader(String projectName, String userName, int currentWeek, boolean isLeader) throws SQLException,
			DalException {
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
		return errmsg;
	}

	/** Insert into Booking table. This method dont close the connection or the transaction!
	 * 
	 * @param projectName
	 * @param userName
	 * @param week
	 * @param ratio
	 * @param isLeader
	 * @param con
	 * @return errmsg
	 * @throws SQLException */
	private void addResourceToBooking(int projectID, int targetResourceID, int week, int ratio, boolean isLeader, Connection con)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "resource_to_booking", 6);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, targetResourceID);
			setInt(stmt, paramIndex++, week);
			setInt(stmt, paramIndex++, ratio);
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

	private int addResourceToUsersInProjects(int projectID, int targetResourceID, boolean isLeader, Connection con)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "add_resource_to_users_in_projects", 4);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, targetResourceID);
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
		return errmsg;
	}

	// Resource

	private int updateResourceRatioInBooking(int projectID, int resourceID, int currentWeek, int newRatio, Connection con)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "update_resource_ratio", 5);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, resourceID);
			setInt(stmt, paramIndex++, currentWeek);
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
		return errmsg;
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

	public int getProjectID(String projectName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_project_id", 2);

			int paramIndex = 1;
			setString(stmt, paramIndex++, projectName);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
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
