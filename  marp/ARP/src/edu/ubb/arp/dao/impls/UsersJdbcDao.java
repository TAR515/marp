package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import edu.ubb.arp.dao.UsersDao;
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
			boolean active, String resourceGroupName) throws DalException {
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
		return errmsg;
	}

	public int updateUser(String oldUserName, byte[] oldPassword, String oldGroup, String newUserName, byte[] newPassword,
			String newPhoneNumber, String newEmail, String newResourceName, boolean newActive, String newGroup)
			throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_user", 11);

			int paramIndex = 1;
			setString(stmt, paramIndex++, oldUserName);
			setByteList(stmt, paramIndex++, oldPassword);
			setString(stmt, paramIndex++, oldGroup);
			setString(stmt, paramIndex++, newUserName);
			setByteList(stmt, paramIndex++, newPassword);
			setString(stmt, paramIndex++, newPhoneNumber);
			setString(stmt, paramIndex++, newEmail);
			setString(stmt, paramIndex++, newResourceName);
			setBoolean(stmt, paramIndex++, newActive);
			setString(stmt, paramIndex++, newGroup);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			errmsg = stmt.getInt("Oerrmsg");
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
		return errmsg;
	}

	public int setActive(String userName, boolean active) throws DalException {
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
		return errmsg;
	}

	@Override
	public Users loadUser(String userName) throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");

		Users user = new Users();

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_user_by_name", 1);

			int paramIndex = 1;
			setString(stmt, paramIndex++, userName);

			rs = stmt.executeQuery();
			while (rs.next()) {
				user = (Users) fillObject(rs);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(-1));
			throw new DalException(-1, e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}

		return user;
	}

	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Users retValue = new Users();

		retValue.setUserID(getInt(rs, "UserId"));
		retValue.setUserName(getString(rs, "UserName"));
		retValue.setEmail(getString(rs, "Email"));
		retValue.setPhoneNumber(getString(rs, "PhoneNumber"));
		retValue.setPassword(null);
		retValue.setResource(null);

		return retValue;
	}

}
