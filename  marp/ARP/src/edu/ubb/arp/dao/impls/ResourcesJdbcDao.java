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
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *contains methods which work with the Resources
 */
public class ResourcesJdbcDao extends BaseDao implements ResourcesDao {
	/**
	 * constructor 
	 * @param dataSource
	 */
	public ResourcesJdbcDao(DataSource dataSource) {
		super(dataSource);
	}
	/**
	 * constructor
	 * @param dataSource
	 * @param maxResultSize
	 */
	public ResourcesJdbcDao(DataSource dataSource, int maxResultSize) {
		super(dataSource, maxResultSize);
	}

	/** Creates a new resource witch can't be a human(user).
	 * 
	 * @param resourceName
	 *            can be same with a user resource name but must be different from an another non human resource name
	 * @param active
	 *            can be set as true or false, false indicates that the resource aren't working
	 * @param resourceTypeName
	 *            indicates what kind of resource is it
	 * @param resourceGroupName
	 *            every resource must be at least in one group
	 * @return ErrorCode if the returned value is lower than 0 | Created resource id if the returned value is bigger than 0
	 * @throws DalException
	 *             The exception contains ErrorCode and ErrorMessage of the problem that arises */
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

	/**
	 * updates a resource's attributes
	 * @param oldResourceName is the old name of the resource 
	 * @param oldActive is the old active attribute of the resource
	 * @param oldResourceTypeName is the old resource type name attribute of the resource
	 * @param oldResourceGroupName is the old group name attribute of the resource
	 * @param newResourceName is the new name of the resource
	 * @param newActive is the new active attribute of the resource
	 * @param newResourceTypeName is the new resource type name attribute of the resource
	 * @param newResourceGroupName is the new group name attribute of the resource
	 * @return returns the resource's id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int updateResource(String oldResourceName, boolean oldActive, String oldResourceTypeName, String oldResourceGroupName,
			String newResourceName, boolean newActive, String newResourceTypeName, String newResourceGroupName)
			throws SQLException, DalException {
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
	/**
	 * sets a resource active attribute to true/false
	 * @param resourceName is the name of the resource which's active attribute will be updated
	 * @param active is true if the resource is hired , false if fired
	 * @return returns the resource's id  if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * adds a resource to a group
	 * @param resourceName is the name of the resource to add to the group
	 * @param groupName is the name of the group to which the resource will be added
	 * @return returns the resource's id if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * adds  a resource to a group
	 * @param resource is the resources which will be added to the group
	 * @param group is the group to which the resource will be added
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * adds a resource to multiple groups
	 * @param resourceName is the name of the resource which will be added to the groups
	 * @param groupNames is a list a groups in which the resource will added to
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * adds a resource to multiple groups
	 * @param resource is the resources which will be added to the group
	 * @param groups is a list a groups in which the resource will added to
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * removes a resource from a group
	 * @param resourceName is the name of the resource which will be removed from a group
	 * @param groupName is the name of the group from which the resource will be removed from 
	 * @return return the id of the resource if there was no error else returns an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * removes a resource from a group
	 * @param resource is the resource  which will be removed from a group
	 * @param group is the group from which the resource will be removed from 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
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
	/**
	 * 
	 * @param resourceID is a list of resources which which's attributes will be loaded 
	 * @return a list of resources if there was no error else returns an error message
	 * @throws DalException if there is no connection
	 * @throws SQLException if a stored procedure returns an error message
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

			while (it.hasNext()) {
				result.add(loadResource(it.next(), connection));
			}
		} catch (SQLException e) {
			logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
			throw new SQLException(getClass().getName() + methodName + "SQL Exception: ", e);
		} catch (DalException e) {
			logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
			throw e;
		} finally {
			closeSQLObjects(connection, null, null);
			logger.debug(getClass().getName() + methodName + "-> EXIT");
		}
		return result;
	}

	/**
	 * 
	 * @param resourceID is the id of the resource which's booking will be loaded
	 * @param startWeek is the start week from where the booking will be loaded
	 * @param endWeek is the last week of the loaded booking of the resource
	 * @param projectName is the name of the project in which the resource's booking will be loaded 
	 * @param action is insert if the resource will be inserted in booking , update if the resource's ratio will be updated or new if a new project will be created
	 * @return returns the booking of the resource from start week to end week in all projects he works in if there was no error , otherwise an error message 
	 * @throws DalException  if there is no connection
	 * @throws SQLException if a stored procedure returns an error message
	 */
	public List<Integer> loadResourceTotalEngages(int resourceID, int startWeek, int endWeek, String projectName, String action)
			throws DalException, SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		List<Integer> result = new ArrayList<Integer>();
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		ResultSet rs = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_resource_ratio", 6);
			
			System.out.println(startWeek);
			System.out.println(endWeek);
			System.out.println(resourceID);
			System.out.println(projectName);
			int paramIndex = 1;
			setInteger(stmt, paramIndex++, resourceID);
			setInteger(stmt, paramIndex++, startWeek);
			setInteger(stmt, paramIndex++, endWeek);
			setString(stmt, paramIndex++, projectName);
			setString(stmt, paramIndex++, action);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}

			while (rs.next()) {
				result.add(getInt(rs, "ratio"));
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
	 * 
	 * @param resourceID is the id of the resource which's booking in the current project  will be loaded 
	 * @param startWeek is the start week from where the booking will be loaded
	 * @param endWeek is the last week of the loaded booking of the resource
	 * @param projectName is the name of the project in which the resource's booking will be loaded 
	 * @return returns the booking of the resource from start week to end week in the project if there was no error , otherwise an error message 
	 * @throws DalException if there is no connection
	 * @throws SQLException if a stored procedure returns an error message
	 */
	public List<Integer> loadResourceCurrentProjectEngages(int resourceID, int startWeek, int endWeek, String projectName)
			throws DalException, SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		List<Integer> result = new ArrayList<Integer>();
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		ResultSet rs = null;
		java.sql.CallableStatement stmt = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_resource_ratio_in_project", 5);

			int paramIndex = 1;
			setInteger(stmt, paramIndex++, resourceID);
			setInteger(stmt, paramIndex++, startWeek);
			setInteger(stmt, paramIndex++, endWeek);
			setString(stmt, paramIndex++, projectName);
			stmt.registerOutParameter(paramIndex++, java.sql.Types.INTEGER);

			rs = stmt.executeQuery();
			errmsg = stmt.getInt("Oerrmsg");
			if (errmsg < 0) {
				logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
				throw new DalException(errmsg);
			}

			while (rs.next()) {
				result.add(getInt(rs, "cpratio"));
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
	 * 
	 * @return all resources names and id's
	 * @throws SQLException if a stored procedure returns an error message 
	 */
	public List<Resources> LoadResources() throws SQLException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		List<Resources> result = new ArrayList<Resources>();
		logger.debug(getClass().getName() + methodName + "-> START");
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_resources", 0);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result.add((Resources) fillJustResourceIDAndName(rs));
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
	/**
	 * returns a Resources object containing the result of the stored procedure
	 * @param rs contains the result of the stored procedure
	 * @return returns a Resources object containing the result of the stored procedure
	 * @throws SQLException if a stored procedure returns an error message
	 */
	private Resources fillJustResourceIDAndName(ResultSet rs) throws SQLException {
		Resources resource = new Resources();

		resource.setResourceID(getLong(rs, "ResourceID").intValue());
		resource.setResourceName(getString(rs, "ResourceName"));
		return resource;
	}
	/**
	 * loads the resource's attributes
	 * @param resourceID is the id of the resource to be load
	 * @param con is the connection to the database
	 * @return  returns the resource's attributes
	 * @throws SQLException if a stored procedure returns an error message
	 * @throws DalException if a stored procedure returns an error message
	 */
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

	/**
	 * returns an object filled with the stored procedures result
	 * @param rs contains the stored procedures result
	 * @return returns an object filled with the stored procedures result
	 * 
	 */
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
		} catch (SQLException e) {
			resource.setUsers(null);
		}

		return resource;
	}

}
