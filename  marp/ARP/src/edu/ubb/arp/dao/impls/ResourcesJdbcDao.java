package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.ResourceTypes;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public class ResourcesJdbcDao extends BaseDao implements ResourcesDao {

	public ResourcesJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	public ResourcesJdbcDao(DataSource dataSource, int maxResultSize) {
		super(dataSource, maxResultSize);
	}

	@Override
	public int createResource(String resourceName, boolean active, String resourceTypeName, String resourceGroupName)
			throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "new_resource", 5);

			int paramIndex = 1;
			setString(stmt, paramIndex++, resourceName);
			setBoolean(stmt, paramIndex++, active);
			setString(stmt, paramIndex++, resourceTypeName);
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

	@Override
	public int updateResource(String oldResourceName, boolean oldActive, String oldResourceTypeName,
			String oldResourceGroupName, String newResourceName, boolean newActive, String newResourceTypeName,
			String newResourceGroupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "update_resource", 9);

			int paramIndex = 1;
			setString(stmt, paramIndex++, oldResourceName);
			setBoolean(stmt, paramIndex++, oldActive);
			setString(stmt, paramIndex++, oldResourceTypeName);
			setString(stmt, paramIndex++, oldResourceGroupName);
			setString(stmt, paramIndex++, newResourceName);
			setBoolean(stmt, paramIndex++, newActive);
			setString(stmt, paramIndex++, newResourceTypeName);
			setString(stmt, paramIndex++, newResourceGroupName);
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

	public int setActive(String resourceName, boolean active) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "resource_set_active", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, resourceName);
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

	public int addResourceToGroup(String resourceName, String groupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "add_resource_to_group", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, resourceName);
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

	public void addResourceToGroup(Resources resource, Groups group) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		try {
			addResourceToGroup(resource.getResourceName(), group.getGroupName());
		} catch (DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void addResourceToGroups(String resourceName, String[] groupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		try {
			int i = 0;
			while (errmsg >= 0 && i < groupName.length) {
				addResourceToGroup(resourceName, groupName[i++]);
			}
		} catch (DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public void addResourceToGroups(Resources resource, List<Groups> groups) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		try {
			int i = 0;
			while (errmsg >= 0 && i < groups.size()) {
				addResourceToGroup(resource, groups.get(i++));
			}
		} catch (DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
	}

	public int removeResourceFromGroup(String resourceName, String groupName) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "remove_resource_from_group", 3);

			int paramIndex = 1;
			setString(stmt, paramIndex++, resourceName);
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

	public void removeResourceFromGroup(Resources resource, Groups group) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");

		try {
			removeResourceFromGroup(resource.getResourceName(), group.getGroupName());
		} catch (DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}

	}

	/*
	 * Kell ra tarolt eljaras!
	 * 
	 * @Override public List<Resources> loadResourcesByGroup(Integer groupId) throws DalException { logger.debug(getClass().getName() +
	 * ".loadResourcesByGroup() -> START"); List<Resources> retvalue = new ArrayList<Resources>(); Connection connection = null;
	 * java.sql.CallableStatement stmt = null; ResultSet rs = null; try { connection = getConnection(); stmt =
	 * createProcedure(connection, "load_resource_by_group_id", 1);
	 * 
	 * int paramIndex = 1; setInteger(stmt, paramIndex++, groupId);
	 * 
	 * rs = stmt.executeQuery(); while (rs.next()) { retvalue.add((Resources) fillObject(rs)); } } catch (SQLException e) {
	 * logger.error(getClass().getName() + ".loadResourcesByGroup() -> NO_CONNECTION"); throw new
	 * DalException(DalException.NO_CONNECTION, e); } finally { closeSQLObjects(connection, rs, stmt);
	 * logger.debug(getClass().getName() + ".loadResourcesByGroup() -> EXIT"); }
	 * 
	 * return retvalue; }
	 */

	public List<Resources> loadResources(List<Integer> resourceID) throws DalException, SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		List<Resources> result = new ArrayList<Resources>();
		Connection connection = null;
		logger.debug(getClass().getName() + methodName + "-> START");
		
		try {
			connection = getConnection();
			
			Iterator<Integer> it = resourceID.iterator();
			
			while(it.hasNext()) {
				result.add(loadResource(it.next(),connection));
			}
		} catch(SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		}catch (DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			closeSQLObjects(connection, null, null);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return result;
	}
	
	
	private Resources loadResource(int resourceID, Connection con) throws SQLException, DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		Resources result = null;
		logger.debug(getClass().getName() + methodName + "-> START");
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = con;
			stmt = createProcedure(connection, "load_resource_by_resource_id", 2);

			int paramIndex = 1;
			setInt(stmt, paramIndex++, resourceID);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}
			
			while (rs.next()) { 
				result = (Resources) fillObject(rs); 
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} finally {
			closeSQLObjects(null, rs, stmt);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return result;
	}
	
	
	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Resources resource = new Resources();

		resource.setResourceID(getLong(rs, "ResourceID").intValue());
		resource.setResourceName(getString(rs, "ResourceName"));
		resource.setActive(getBool(rs, "Active"));
		
		ResourceTypes rt = new ResourceTypes();
		rt.setResourceTypeName(getString(rs, "ResourceTypeName"));
		resource.setResourceTypes(rt);
		
		try {
			Users users = new Users();
			users.setUserName(getString(rs, "UserName"));
			resource.setUsers(users);
		} catch(SQLException e) {
			resource.setUsers(null);
		}
		
		return resource;
	}

}
