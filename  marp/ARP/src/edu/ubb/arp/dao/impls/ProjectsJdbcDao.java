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
/**
 * 
 * @author VargaAdorjan
 *contains methods which work with the Projects
 */
public class ProjectsJdbcDao extends BaseDao implements ProjectsDao {
	/**
	 * constructor
	 * @param dataSource
	 */
	public ProjectsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}
	/**
	 * creates a new project
	 * @param projectName is the name of the new project
	 * @param openedStatus is the status of the new project (opened/closed) 
	 * @param startWeek is the starting week of the project
	 * @param endWeek is the end week of the project
	 * @param deadLine is the deadline of the project
	 * @param nextRelease is the projects next release version
	 * @param statusName is the current status of the project
	 * @param resourceID the id of the resource who is the leader of the new project
	 * @param insertRatio is the ratio with which the resource will be booked in the new project  
	 * @param requestRatio is the the ratio of the request for the resource
	 * @return returns the created projects id if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
			addResourceToProject(errmsg, resourceID, resourceID, startWeek, endWeek, true, insertRatio, requestRatio, connection);
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
	/**
	 * adds a resource to the project
	 * @param projectID is the id of the project in which the target resource is added
	 * @param senderResourceID is the id of the resource who adds the target resource to the project
	 * @param targetResourceID is the id of the resource which will be added to the project
	 * @param startWeek is the starting week from which the target resource will be booked
	 * @param endWeek is the last week where the resource will be booked 
	 * @param isLeader is true if the target resource will be leader in the project or false if not
	 * @param insertRatio is the ratio with which the resource will be booked in the new project
	 * @param requestRatio is the the ratio of the request for the resource
	 * @return returns the id of the project , the resource was added to if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
						errmsg = addResourceToBooking(projectID, targetResourceID, currentWeek, currentInsertRatio, isLeader, connection);
					}

					if (currentRequestRatio > 0) {
						errmsg = requestDao.createNewRequest(senderResourceID, targetResourceID, projectID, currentWeek,
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
	/**
	 * adds a resource to the project
	 * @param projectID is the id of the project in which the target resource is added
	 * @param senderResourceID is the id of the resource who adds the target resource to the project
	 * @param targetResourceID is the id of the resource which will be added to the project
	 * @param startWeek is the starting week from which the target resource will be booked
	 * @param endWeek is the last week where the resource will be booked 
	 * @param isLeader is true if the target resource will be leader in the project or false if not
	 * @param insertRatio is the ratio with which the resource will be booked in the new project
	 * @param requestRatio is the the ratio of the request for the resource
	 * @param con is the connection to the database
	 * @return returns the id of the project , the resource was added to if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the status of the project
	 * @param projectID is the id of the project which's openeStatus will be modified
	 * @param openStatus is the new status of the project
	 * @return returns the id of the project which's status was modified if there was no error , else returns an error message
	 * @throws SQLException  if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the status of the project
	 * @param projectName is the name of the project which's openeStatus will be modified
	 * @param openStatus is the new status of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the name of the project
	 * @param projectID is the id of the project which's name will be modified
	 * @param newProjectName is the new name of the project
	 * @return returns the id of the project which's name was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the name of the project
	 * @param oldProjectName is the name of the project which's name will be modified
	 * @param newProjectName newProjectName is the new name of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the deadline of the project
	 * @param projectID is the id of the project which's deadline will be modified
	 * @param newDeadLine is the new deadline of the project
	 * @return returns the id of the project which's deadline was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the deadline of the project
	 * @param projectName is the name of the project which's deadline will be modified
	 * @param newDeadLine is the new deadline of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the next release version of the project
	 * @param projectID is the id of the project which's next release version will be modified
	 * @param newNextRelease is the new next release version of the project
	 * @return returns the id of the project which's next release version was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets the next release version of the project
	 * @param projectName is the name of the project which's next release version will be modified
	 * @param newNextRelease is the new next release version of the project
	 * @throws SQLException  if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets projects current status
	 * @param projectID is the id of the project which's current status will be modified
	 * @param newCurrentStatus is the new current status of the project
	 * @return  the id of the project which's current status was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets projects current status
	 * @param projectName is the name of the project which's current status will be modified
	 * @param newCurrentStatus is the new current status of the project
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * removes a user from a project
	 * @param projectID is the id of the project , from which the user will be removed
	 * @param userID is the id of the user which will be removed from the project
	 * @param currentWeek is the week from which the user will be removed from the project
	 * @throws SQLException  if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * removes a user from a project
	 * @param projectName is the name of the project , from which the user will be removed
	 * @param userName is the name of the user which will be removed from the project
	 * @param currentWeek is the week from which the user will be removed from the project
	 * @return	returns the id of the removed user if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * removes a resource from a project
	 * @param projectName is the name of the project , from which the user will be removed
	 * @param resourceName is the name of the resource which will be removed from the project
	 * @param currentWeek is the week from which the resource will be removed from the project
	 * @return returns the id of the removed resource if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * updates a resource's ratio and books the resource to which was the request for
	 * @param projectID is the id of the project in which the target resource's ratio will modified
	 * @param targetResourceID is the id of the resource which's ratio will be modified
	 * @param currentWeek is the week where the resource ratio will be modified
	 * @param requestID is the id of the request for the target resource
	 * @param updateRatio is the new ratio of the target resource
	 * @return returns the id of the resource who's ratio was modified if there was no error , else returns an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateResourceRatioWithRequest(int projectID, int targetResourceID, int currentWeek,
			 int requestID, int updateRatio) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

			Connection connection = null;
			
			try {
				connection = getConnection();

					if (updateRatio > 0) {
						errmsg = updateResourceRatioInBookingForRequest(projectID, targetResourceID, currentWeek, updateRatio, requestID, connection);
					}

					currentWeek++;
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

		logger.debug(getClass().getName() + methodName + "-> EXIT");
		return errmsg;
	}
	/**
	 * updates a resource's ratio
	 * @param projectID is the id of the project in which the target resource's ratio will modified
	 * @param senderResourceID is the id of the resource who modifies the target resource's ratio
	 * @param targetResourceID is the id of the resource which's ratio will be modified 
	 * @param startWeek is the start week from which the resource's ratio will be modified
	 * @param endWeek is the last week where resource's ratio will be modified  
	 * @param updateRatio is the new ratio 
	 * @param requestRatio is the ratio of the request for the target resource
	 * @return returns the id of the resource who's ratio was modified if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * updates the user to leader or user in project
	 * @param projectName is the name of the projects in which the user will be updated to leader or user
	 * @param userName is the name of the user who will be updated to leader or user
	 * @param currentWeek is the week from which the user will be leader or user in the project
	 * @param isLeader is true if the user will be leader or false if not
	 * @return returns the id of the user who was modified to leader or user if there was no error , else returns an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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

	/** 
	 * Insert into Booking table. This method don't close the connection or the transaction!
	 * @param projectID is the id of the project in which the target resource will be added
	 * @param targetResourceID is the id of the resource which will be added to the project 
	 * @param week is the week in which the resource will be added 
	 * @param ratio is the ratio with which the resource will be added
	 * @param isLeader is true if the resource will be leader in the project or false if not
	 * @param con is the connection to the database
	 * @return the id of the resource if there was no error , otherwise an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	private int addResourceToBooking(int projectID, int targetResourceID, int week, int ratio, boolean isLeader, Connection con)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "add_resource_to_booking", 6);

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
		return errmsg;
	}
	/** 
	 *  add resource to UsersInProjects table
	 * @param projectID is the id of the project in which the resource will be added 
	 * @param targetResourceID is the id of the resource which will be added to to the UsersInProjects table
	 * @param isLeader is true if the resource will be leader in the project or false if not
	 * @param con is the connection to the database
	 * @return returns the id of the added resource if there was no error , otherwise an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/** 
	 * updates resource's ratio in project
	 * @param projectID is the id of the project in which the resource's ratio will be updated
	 * @param resourceID is the id of resource which's resource's ratio will be updated
	 * @param currentWeek is the week in which the resource's ratio will be updated
	 * @param newRatio is the new ratio of the resource
	 * @param con is the connection to the database
	 * @return returns the id of the added resource if there was no error , otherwise an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * updates a resource's ratio and books the resource to which was the request for
	 * @param projectID is the id of the project in which the resource's ratio will be updated
	 * @param resourceID is the id of the resource who's ratio will be updated
	 * @param currentWeek is the week in which the resource's ratio will be updated
	 * @param newRatio is the new ratio of the resource
	 * @param requestID is the id of the request for the target resource
	 * @param con is the connection to the database
	 * @return return the id of the added resource if there was no error , otherwise an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	private int updateResourceRatioInBookingForRequest(int projectID, int resourceID, int currentWeek, int newRatio,int requestID, Connection con)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "update_resource_ratio_for_request", 6);

			int paramIndex = 1;
			
			setInt(stmt, paramIndex++, projectID);
			setInt(stmt, paramIndex++, resourceID);
			setInt(stmt, paramIndex++, currentWeek);
			setInt(stmt, paramIndex++, newRatio);
			setInt(stmt, paramIndex++, requestID);
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

	/** 
	 * Returns the resourceId of a user. This method dont close the connection or the transaction.
	 * @param userName is the name if the user who's resourceid will be returned
	 * @return resourceID or errmsg.
	 * @throws SQLException if there is no connection
	 *  */
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
	 * @param projectName is the  name of the project which will be created
	 * @param openedStatus is the status of the new project (opened/closed) 
	 * @param startWeek is the starting week of the project
	 * @param deadLine is the deadline of the project
	 * @param nextRelease is the projects next release version
	 * @param statusName is the current status of the project
	 * @param con is the connection to the database
	 * @return errmsg
	 * @throws SQLException if there is no connection
	 * */
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

	/**
	 * returns the bookings of the resources in the project
	 * @param projectID is the id of the project which's resource booking will be loaded
	 * @return returns the id of the project which's resource booking was be loaded if there was no error , else returns an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * fills Booking with the stored procedure's result
	 * @param rs  is the stored procedure's result 
	 * @return returns the result of the  stored procedure
	 * @throws SQLException if there is no connection
	 */
	protected Booking fillBooking(ResultSet rs) throws SQLException {
		Booking result = new Booking();

		result.setProjectID(getInt(rs, "ProjectID"));
		result.setResourceID(getInt(rs, "ResourceID"));
		result.setWeek(getInt(rs, "Week"));
		result.setRatio(getInt(rs, "Ratio"));
		result.setLeader(getBool(rs, "IsLeader"));

		return result;
	}
	/**
	 * returns the projects , the user is working in
	 * @param userName is the name of the user , which's projects he is working in will be loaded
	 * @return returns the projects , the user is working in if there was no error , else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * returns all projects 
	 * @return returns all projects 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<ResourcesWorkingOnProject> getAllProjectsForManeger() throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		List<ResourcesWorkingOnProject> resoult = null;

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			resoult = new ArrayList<ResourcesWorkingOnProject>();
			stmt = createProcedure(connection, "load_projects", 0);

			rs = stmt.executeQuery();
			while (rs.next()) {
				resoult.add(fillProjects(rs));
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
	/**
	 * return the id of the project
	 * @param projectName is the name of the project which's projectID will be returned
	 * @return returns the id of the project 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * return the result of the stored procedure
	 * @param rs contains the result of the stored procedure
	 * @return returns the result of the stored procedure
	 * @throws SQLException if there is no connection
	 */
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
	/**
	 * return the result of the stored procedure
	 * @param rs contains the result of the stored procedure
	 * @return returns the result of the stored procedure
	 * @throws SQLException if there is no connection
	 */
	protected ResourcesWorkingOnProject fillProjects(ResultSet rs) throws SQLException {
		ResourcesWorkingOnProject retValue = new ResourcesWorkingOnProject();

		retValue.setProjectID(getInt(rs, "ProjectID"));
		retValue.setProjectName(getString(rs, "ProjectName"));
		retValue.setOpenedStatus(getBool(rs, "OpenedStatus"));
		retValue.setStartWeek(getInt(rs, "StartWeek"));
		retValue.setDeadLine(getInt(rs, "Deadline"));
		retValue.setNextRelease(getString(rs, "NextRelease"));
		retValue.setStatusName(getString(rs, "StatusName"));
		retValue.setLeader(false);
		
		return retValue;
	}
	
	/**
	 * erre meg vissza kell terni 
	 */
	protected Object fillObject(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
