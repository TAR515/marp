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
	
	
	@Override
	public Users loadUser(String userName) throws DalException {
		logger.debug(getClass().getName() + ".loadUser() start");
		
		Users user = new Users();
		
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_user_by_name", 1);

			int paramIndex = 1;			
			setString(stmt, paramIndex++, userName);			
			stmt.execute();
			
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				user = (Users) fillObject(rs);
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + ".loadUser()");
			throw new DalException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName() + ".loadUser() exit");
		}	
		
		return user;
	}


	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Users retValue = new Users();
		
		retValue.setUserID(getInt(rs, "UserId"));
		retValue.setUserName(getString(rs,"UserName"));
		retValue.setEmail(getString(rs, "Email"));
		retValue.setHired(getBool(rs,"Hired"));
		retValue.setPhoneNumber(getString(rs,"PhoneNumber"));
		retValue.setPassword(null);
		retValue.setResource(null);
		
		return retValue;
	}

}
