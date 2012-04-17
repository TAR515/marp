package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import edu.ubb.arp.dao.GroupsDao;
import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.exceptions.DalException;

public class GroupsJdbcDao extends BaseDao implements GroupsDao {

	public GroupsJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	public void createGroup(String groupName) throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
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
			int errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg, methodName + DalException.errCodeToMessage(errmsg));
			}
			connection.commit();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-1));
			throw new DalException(-1, e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void updateGroup(String oldGroupName, String newGroupName) throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
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
			int errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg, methodName + DalException.errCodeToMessage(errmsg));
			}
			connection.commit();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-1));
			throw new DalException(-1, e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void deleteGroup(String groupName) throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
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
			int errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg, methodName + DalException.errCodeToMessage(errmsg));
			}
			connection.commit();
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-1));
			throw new DalException(-1, e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	@Override
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws DalException {
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
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-1));
			throw new DalException(-1, e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}

		return groups;
	}

	@Override
	public Groups loadGroupByResourceId(Integer resourceId) throws DalException {
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
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-1));
			throw new DalException(-1, e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return group;
	}

	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Groups groups = new Groups();

		groups.setGroupID(getLong(rs, "GroupID").intValue());
		groups.setGroupName(getString(rs, "GroupName"));

		return groups;
	}
}
