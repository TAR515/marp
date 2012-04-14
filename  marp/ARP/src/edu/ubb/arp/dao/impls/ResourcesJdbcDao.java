package edu.ubb.arp.dao.impls;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public Resources LoadResourceByID(int id) throws DalException {
		Resources retvalue = new Resources();
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_resource_by_id", 1);

			int paramIndex = 1;			
			setInteger(stmt, paramIndex++, id);			
			stmt.execute();
			
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				retvalue = (Resources) fillObject(rs);
			}
		} catch (SQLException e) {
			throw new DalException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName()+".loadUser() exit");
		}	
		
		return retvalue;
	}

	@Override
	protected Object fillObject(ResultSet rs) throws SQLException {
		Resources resource = new Resources();

		resource.setResourceID(getLong(rs, "ResourceID").intValue());
		resource.setResourceName(getString(rs,"ResourceName"));
		ResourceTypes rt = new ResourceTypes();
		
		rt.setResourceTypesID(getLong(rs,"ResourceTypeID").intValue());
		rt.setResourceTypeName(getString(rs,"ResourceTypeName"));
		resource.setResourceTypes(rt);
		
		return resource;
	}

	@Override
	public List<Resources> loadResourcesByGroup(Integer groupId) throws DalException {
		List<Resources> retvalue = new ArrayList<Resources>() ;
		Connection connection = null;
		java.sql.CallableStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			stmt = createProcedure(connection, "load_resource_by_group_id", 1);

			int paramIndex = 1;			
			setInteger(stmt, paramIndex++, groupId);			
			stmt.execute();
			
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				retvalue.add((Resources) fillObject(rs));
			}
		} catch (SQLException e) {
			throw new DalException(e);
		} finally {
			closeSQLObjects(connection, rs, stmt);
			logger.debug(getClass().getName()+".loadResourcesByGroup() exit");
		}	
		
		return retvalue;
	}
		
	
}
