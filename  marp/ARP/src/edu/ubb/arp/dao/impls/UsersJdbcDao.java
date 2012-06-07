package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.UsersDao;
import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.ResourceTypes;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *contains methods which work with the Users
 */
public class UsersJdbcDao extends BaseDao implements UsersDao {
	/**
	 * constructor
	 * @param dataSource
	 * @param maxResultSize
	 */
	public UsersJdbcDao(DataSource dataSource, int maxResultSize) {
		super(dataSource, maxResultSize);
	}
	/**
	 * constructor
	 * @param dataSource
	 */
	public UsersJdbcDao(DataSource dataSource) {
		super(dataSource);
	}
	/**
	 * creates a new user
	 * @param userName is the name of the user which will be created
	 * @param password is the password of the user which will be created
	 * @param phoneNumber is the phone number of the  user which will be created
	 * @param email is the email of the  user which will be created
	 * @param resourceName is the resource name of the user which will be created
	 * @param active is true (user is hired)
	 * @param resourceGroupName is the name of the group in which the user will be added
	 * @return the id of the created user if there was no error , otherwise an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes the user's name
	 * @param oldUserName is the old user name of the user
	 * @param newUserName is the new user name of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */	
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
	/**
	 * changes the password of the user
	 * @param userName is the name of the user who's password will be changed
	 * @param oldPassword is the old password of the user
	 * @param newPassword is the new password of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes the email of the user
	 * @param userName is the name of the user who's email will be changed
	 * @param newEmail is the new email of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes the phone number of the user 
	 * @param userName is the name of the user who's email will be changed
	 * @param newPhoneNumber is the new phone number of the user
	 * @return  the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes user's resource name
	 * @param userName is the name of the user who's resource name will be changed
	 * @param newResourceName is the new resource name of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes the name of the user 
	 * @param userID is the id of the user who's user name will be changed
	 * @param newUserName is the new user name of the user
	 * @return return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes user's password 
	 * @param userID is the id of the user who's user name will be changed
	 * @param oldPassword is the old password of the user 
	 * @param newPassword is the new password of the user 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes users's email
	 * @param userID is the id of the user who's user name will be changed
	 * @param newEmail is the new email of the user 
	 * @throws SQLException SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes user's phone number
	 * @param userID is the id of the user who's user name will be changed
	 * @param newPhoneNumber is the new phone number of the user
	 * @throws SQLException SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * changes user's resource name
	 * @param userID is the id of the user who's user name will be changed
	 * @param newResourceName is the new resource name of the user
	 * @throws SQLException SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * sets user active attribute to active(true/false)
	 * @param userName is the name of the user who's active attribute will be updated
	 * @param active is true if the user will be hired or false if fired 
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * adds user to a group
	 * @param userName is the name of the user to add to the group
	 * @param groupName is the name of the group to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroup(String userName, String groupName) throws SQLException, DalException {
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
		return errmsg;
	}
	/**
	 * adds user to a group
	 * @param user is the user to add to the group
	 * @param group is the group to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroup(Users user, Groups group) throws SQLException, DalException {
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
		return errmsg;
	}
	/**
	 * adds user to multiple groups
	 * @param userName is the name of the user to add to the group
	 * @param groupName is a list of groups to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroup(String userName, String[] groupNames) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		try {
			int i = 0;
			while (errmsg >= 0 && i < groupNames.length) {
				errmsg = addUserToGroup(userName, groupNames[i++]);
			}
		} catch(DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
	/**
	 * adds user to multiple groups
	 * @param users user is the user to add to the group
	 * @param groups is a list of groups to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroups(Users user, List<Groups> groups) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		
		try {
			int i = 0;
			while (errmsg >= 0 && i < groups.size()) {
				errmsg = addUserToGroup(user, groups.get(i++));
			}
		} catch(DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return errmsg;
	}
	/**
	 * removes the user from a the group
	 * @param userName is the name of the user who will be removed from the group
	 * @param groupName is name of the group from which the user will removed from
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeUserFromGroup(String userName, String groupName) throws SQLException, DalException {
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
		return errmsg;
	}

	/**
	 * checks if exists the given user with the given password
	 * @param userName is the name of the user which will be checked if exists 
	 * @param password is the password of the user which will be checked if exists 
	 * @return returns 1 if exists , an error message if not 
	 * @throws SQLException  if there is no connection
	 */
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

	/**
	 * loads users attributes
	 * @param userName is the name of the user who's attributes will be loaded
	 * @return returns users attributes if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException  if a stored procedure returns an error message
	 */
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
	
	/**
	 * returns an object filled with the stored procedures result
	 * @param rs contains the stored procedures result
	 * @return returns an object filled with the stored procedures result
	 * 
	 */
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

	/**
	 * loads the booking of the user from current week for 4 weeks
	 * @param userName is the name of the user who's booking will be loaded
	 * @param currentWeek is the starting week from which the booking will be loaded 
	 * @return returns the booking of the user from current week for 4 weeks
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<Booking> LoadAssignments(String userName, int currentWeek)
			throws SQLException, DalException {
		
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		List<Booking> booking = new ArrayList<Booking>();

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_assignments", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);
			setInt(stmt, paramIndex++, currentWeek);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();
			
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
			while (rs.next()) {
				//System.out.println(getInt(rs, "ProjectID"));
				booking.add(fillBooking(rs));
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(0));
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}

		return booking;
	}
	/**
	 * returns an object filled with the stored procedures result
	 * @param rs contains the stored procedures result
	 * @return returns an object filled with the stored procedures result
	 * 
	 */
	protected Booking fillBooking(ResultSet rs) throws SQLException {
		Booking retValue = new Booking();
		
		retValue.setProjectID(getInt(rs, "ProjectID"));
		retValue.setResourceID(getInt(rs, "ResourceID"));
		retValue.setWeek(getInt(rs, "Week"));
		retValue.setRatio(getInt(rs, "Ratio"));
		retValue.setLeader(getBool(rs, "IsLeader"));
		return retValue;
	}
}
