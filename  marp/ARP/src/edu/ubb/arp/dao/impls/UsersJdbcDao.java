package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.ResourceTypes;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public class UsersJdbcDao extends BaseDao implements UsersDao {

	public UsersJdbcDao(DataSource dataSource, int maxResultSize) {
		super(dataSource, maxResultSize);
	}

	public UsersJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	public int createUser(String userName, byte[] password, String phoneNumber, String email, String resourceName,
			boolean active, String resourceGroupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "new_user", 8);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setByteList(stmt, paramIndex++, password);
			setString(stmt, paramIndex++, phoneNumber);
			setString(stmt, paramIndex++, email);
			setString(stmt, paramIndex++, resourceName);
			setBoolean(stmt, paramIndex++, active);
			setString(stmt, paramIndex++, resourceGroupName);
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

	public int changeUserName(String oldUserName, String newUserName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_name_by_user_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, oldUserName);
			setString(stmt, paramIndex++, newUserName);
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

	public int changePassword(String userName, byte[] oldPassword, byte[] newPassword) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_password_by_user_name", 4);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setByteList(stmt, paramIndex++, oldPassword);
			setByteList(stmt, paramIndex++, newPassword);
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

	public int changeEmail(String userName, String newEmail) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_email_by_user_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setString(stmt, paramIndex++, newEmail);
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

	public int changePhoneNumber(String userName, String newPhoneNumber) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_phonenumber_by_user_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setString(stmt, paramIndex++, newPhoneNumber);
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

	public int changeResourceName(String userName, String newResourceName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_resource_name_by_user_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setString(stmt, paramIndex++, newResourceName);
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

	public void changeUserName(int userID, String newUserName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_name_by_user_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, userID);
			setString(stmt, paramIndex++, newUserName);
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
	}

	public void changePassword(int userID, byte[] oldPassword, byte[] newPassword) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_password_by_user_id", 4);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, userID);
			setByteList(stmt, paramIndex++, oldPassword);
			setByteList(stmt, paramIndex++, newPassword);
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
	}

	public void changeEmail(int userID, String newEmail) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_email_by_user_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, userID);
			setString(stmt, paramIndex++, newEmail);
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
	}

	public void changePhoneNumber(int userID, String newPhoneNumber) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_phonenumber_by_user_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, userID);
			setString(stmt, paramIndex++, newPhoneNumber);
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
	}

	public void changeResourceName(int userID, String newResourceName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user_resource_name_by_user_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, userID);
			setString(stmt, paramIndex++, newResourceName);
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
	}

	public int setActive(String userName, boolean active) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "human_set_active", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setBoolean(stmt, paramIndex++, active);
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

	public void addUserToGroup(String userName, String groupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "add_user_to_group_by_user_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
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
	}

	public void addUserToGroup(Users user, Groups group) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "add_user_to_group_by_user_id", 3);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, user.getUserID());
			setString(stmt, paramIndex++, group.getGroupName());
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
	}

	public void addUserToGroup(String userName, String[] groupNames) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		try {
			int i = 0;
			while (errmsg >= 0 && i < groupNames.length) {
				addUserToGroup(userName, groupNames[i++]);
			}
		} catch(DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void addUserToGroups(Users user, List<Groups> groups) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		
		try {
			int i = 0;
			while (errmsg >= 0 && i < groups.size()) {
				addUserToGroup(user, groups.get(i++));
			}
		} catch(DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void removeUserFromGroup(String userName, String groupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "remove_user_from_group_by_user_name", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
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
	}

	/** @param userName
	 * @param password
	 * @return -inf - 0 if an error occours, 1 - if the username and password are correct and 2 if not.
	 * @throws SQLException */
	public int checkUserNameAndPassword(String userName, byte[] password) throws SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "check_user_name_and_password", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setByteList(stmt, paramIndex++, password);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeQuery();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				return errmsg;
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, null, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}

	/** @param userName
	 * @return null if the user do not exists or he/she doesn't work on any project else returns the projects he/she is working on.
	 * @throws SQLException */
	public HashMap<String, Boolean> getAllActiveProjects(String userName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		String projectName = null;
		Boolean isLeader = null;
		logger.debug(getClass().getName() + methodName + "-> START");
		HashMap<String, Boolean> resoult = new HashMap<String, Boolean>();

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
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
				projectName = getString(rs, "ProjectName");
				isLeader = getBool(rs, "IsLeader");
				resoult.put(projectName, isLeader);
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

	@Override
	public Users loadUser(String userName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Users user = new Users();

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_users_data_by_user_name", 2);

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
				user = (Users) fillObject(rs);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(0));
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}

		return user;
	}

	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Users retValue = new Users();
		Resources resourceValue = new Resources();
		ResourceTypes resourceTypesValue = new ResourceTypes();
		
		resourceTypesValue.setResourceTypesID(getInt(rs, "ResourceTypeID"));
		resourceTypesValue.setResourceTypeName(getString(rs, "ResourceTypeName"));
		
		resourceValue.setResourceID(getInt(rs, "resourceID"));
		resourceValue.setResourceName(getString(rs, "ResourceName"));
		resourceValue.setActive(getBool(rs, "Active"));
		resourceValue.setResourceTypes(resourceTypesValue);
		
		retValue.setUserID(getInt(rs, "UserId"));
		retValue.setUserName(getString(rs, "UserName"));
		retValue.setEmail(getString(rs, "Email"));
		retValue.setPhoneNumber(getString(rs, "PhoneNumber"));
		retValue.setPassword(null);
		retValue.setResource(resourceValue);

		return retValue;
	}
}
