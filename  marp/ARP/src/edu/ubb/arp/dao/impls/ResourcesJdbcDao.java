package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import edu.ubb.arp.dao.ResourcesDao;
import edu.ubb.arp.dao.model.ResourceTypes;
import edu.ubb.arp.dao.model.Resources;
import edu.ubb.arp.exceptions.DalException;

public class ResourcesJdbcDao extends BaseDao implements ResourcesDao {

	public ResourcesJdbcDao(DataSource dataSource) {
		super(dataSource);
	}

	public ResourcesJdbcDao(DataSource dataSource, int maxResultSize) {
		super(dataSource, maxResultSize);
	}

	public void createResource(Resources resource) throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		logger.debug(getClass().getName() + methodName + "-> START");

		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "new_resource", 5);

			int paramIndex = 1;
			setString(stmt, paramIndex++, resource.getResourceName());
			setBoolean(stmt, paramIndex++, resource.isActive());
			setString(stmt, paramIndex++, resource.getResourceTypes().getResourceTypeName());
			setString(stmt, paramIndex++, resource.getGroups().get(0).getGroupName());
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
	public void createResource(String resourceName, boolean active, String resourceTypeName, String resourceGroupName)
			throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
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
	public void updateResource(String oldResourceName, boolean oldActive, String oldResourceTypeName,
			String oldResourceGroupName, String newResourceName, boolean newActive, String newResourceTypeName,
			String newResourceGroupName) throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
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

	public void setActive(String resourceName, boolean active) throws DalException {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
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

	/*
	 * Kell ra tarolt eljaras
	 * 
	 * @Override public Resources LoadResourceByID(int id) throws DalException { logger.debug(getClass().getName() +
	 * ".LoadResourceByID() -> START");
	 * 
	 * Resources retvalue = new Resources(); Connection connection = null; java.sql.CallableStatement stmt = null; ResultSet rs = null;
	 * try { connection = getConnection(); stmt = createProcedure(connection, "load_resource_by_id", 1);
	 * 
	 * int paramIndex = 1; setInteger(stmt, paramIndex++, id);
	 * 
	 * rs = stmt.executeQuery(); while (rs.next()) { retvalue = (Resources) fillObject(rs); } } catch (SQLException e) {
	 * logger.error(getClass().getName() + ".LoadResourceByID() -> NO_CONNECTION"); throw new DalException(DalException.NO_CONNECTION,
	 * e); } finally { closeSQLObjects(connection, rs, stmt); logger.debug(getClass().getName() + ".LoadResourceByID() -> EXIT"); }
	 * 
	 * return retvalue; }
	 */
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

	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Resources resource = new Resources();

		resource.setResourceID(getLong(rs, "ResourceID").intValue());
		resource.setResourceName(getString(rs, "ResourceName"));
		resource.setActive(getBool(rs, "Active"));
		ResourceTypes rt = new ResourceTypes();

		rt.setResourceTypesID(getLong(rs, "ResourceTypeID").intValue());
		rt.setResourceTypeName(getString(rs, "ResourceTypeName"));
		resource.setResourceTypes(rt);

		return resource;
	}

}
