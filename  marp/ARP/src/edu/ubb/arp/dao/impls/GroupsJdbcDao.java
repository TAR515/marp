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

	@Override
	public ArrayList<Groups> loadGroupsByResourceId(Integer resourceId) throws DalException {
		ArrayList<Groups> groups = new ArrayList<Groups>();
		
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_group_by_res_id", 1);

			int paramIndex = 1;			
			setInteger(stmt, paramIndex++, resourceId);			
			stmt.execute();
			
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				groups.add((Groups) fillObject(rs));
			}
		} catch (SQLException e) {
			throw new DalException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName()+".loadUser() exit");
		}	
		
		return groups;
	}

	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Groups groups = new Groups();
		
		groups.setGroupID(getLong(rs, "GroupID").intValue());
		groups.setGroupName(getString(rs, "GroupName"));
		
		return groups;
	}

	@Override
	public Groups loadGroupByResourceId(Integer resourceId) throws DalException {
		Groups group = new Groups();
		
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_group_by_res_id", 1);

			int paramIndex = 1;			
			setInteger(stmt, paramIndex++, resourceId);			
			stmt.execute();
			
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				group = (Groups) fillObject(rs);
			}
		} catch (SQLException e) {
			throw new DalException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName()+".loadUser() exit");
		}	
		
		return group;
	}

}
