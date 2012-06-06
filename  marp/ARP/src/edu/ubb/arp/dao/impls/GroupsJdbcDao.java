package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import edu.ubb.arp.dao.GroupsDao;
import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan
 *contains methods which work with the Groups 
 */
public class GroupsJdbcDao extends BaseDao implements GroupsDao {
	/**
	 * constructor
	 * @param dataSource
	 */
	public GroupsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}
	/**
	 * 
	 * @param groupName the name of the group to create
	 * @return returns an error message if there was an error , otherwise returns the id of the created group
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int createGroup(String groupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "new_group", 2);

			int paramIndex = 1;
			setString(stmt, paramIndex++, groupName);
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
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
	/**
	 * 
	 * @param oldGroupName the name of the group to modify
	 * @param newGroupName the new name of the group
	 * @return returns an error message if there was an error , otherwise returns the id of the modified group
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateGroup(String oldGroupName, String newGroupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_group", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, oldGroupName);
			setString(stmt, paramIndex++, newGroupName);
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
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
	/**
	 * 
	 * @param groupName the name of the group to delete
	 * @return returns an error message if there was an error , otherwise returns the id of the deleted group
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int deleteGroup(String groupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "delete_group", 2);

			int paramIndex = 1;
			setString(stmt, paramIndex++, groupName);
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
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}

	/**
	 * 
	 * @param resourceId is the id of the resource , which's groups i want to load
	 * @return	the groups of the resource 
	 * @throws SQLException if there is no connection
	 */
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		ArrayList<Groups> groups = new ArrayList<Groups>();

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_group_by_res_id", 1);

			int paramIndex = 1;
			setInteger(stmt, paramIndex++, resourceId);

			rs = stmt.executeQuery();
			while (rs.next()) {
				groups.add((Groups) fillObject(rs));
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return groups;
	}

	/**
	 * 
	 * @param resourceId resourceId is the id of the resource , which's groups i want to load
	 * @return the group of the resource 
	 * @throws SQLException if there is no connection
	 */ 
	public Groups loadGroupByResourceId(Integer resourceId) throws SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");
		Groups group = new Groups();

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_group_by_res_id", 1);

			int paramIndex = 1;
			setInteger(stmt, paramIndex++, resourceId);

			rs = stmt.executeQuery();
			while (rs.next()) {
				group = (Groups) fillObject(rs);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return group;
	}

	/**
	 * @param rs is the result from the stored procedure
	 * @return returns an object which contains results from stored procedure
	 */
	protected Object fillObject(ResultSet rs) throws SQLException {
		Groups groups = new Groups();

		groups.setGroupID(getLong(rs, "GroupID").intValue());
		groups.setGroupName(getString(rs, "GroupName"));

		return groups;
	}
}
