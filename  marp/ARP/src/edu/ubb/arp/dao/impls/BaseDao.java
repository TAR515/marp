package edu.ubb.arp.dao.impls;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import edu.ubb.arp.exceptions.DalException;

public abstract class BaseDao {
	protected Logger logger = Logger.getLogger(BaseDao.class);

	protected DataSource dataSource;
	protected int maxResultSize = 1000;

	public BaseDao(DataSource dataSource, int maxResultSize) {
		this.dataSource = dataSource;
		this.maxResultSize = maxResultSize;
		logger = Logger.getLogger(getClass());
	}

	public BaseDao(DataSource dataSource) {
		this.dataSource = dataSource;
		logger = Logger.getLogger(getClass());
	}

	protected void setString(CallableStatement stmt, int pos, String value)
			throws SQLException {
		if (value != null) {
			logger.debug("setString(" + pos + ", " + value + ")");
			stmt.setString(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.VARCHAR)");
			stmt.setNull(pos, Types.VARCHAR);
		}
	}
	
	protected void setByteList(CallableStatement stmt, int pos, byte[] value)
			throws SQLException {
		if (value != null) {
			logger.debug("setBytes(" + pos + ", " + value + ")");
			stmt.setBytes(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.INTEGER - Byte[] -)");
			stmt.setNull(pos, Types.INTEGER);
		}
	}

	protected void setArray(CallableStatement stmt, int pos, Array value)
			throws SQLException {
		if (value != null) {
			logger.debug("setString(" + pos + ", " + value + ")");
			stmt.setArray(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.VARCHAR)");
			stmt.setNull(pos, Types.VARCHAR);
		}
	}

	protected void setTimestamp(CallableStatement stmt, int pos, Timestamp value)
			throws SQLException {
		if (value != null) {
			logger.debug("setTimestamp(" + pos + ", " + value + ")");
			stmt.setTimestamp(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.TIMESTAMP)");
			stmt.setNull(pos, Types.TIMESTAMP);
		}
	}

	protected void setTimestamp(CallableStatement stmt, int pos, Calendar value)
			throws SQLException {
		if (value != null) {
			logger.debug("setTimestamp(" + pos + ", " + value + ")");
			stmt.setTimestamp(pos, new Timestamp(value.getTimeInMillis()));
		} else {
			logger.debug("setNull(" + pos + ", Types.TIMESTAMP)");
			stmt.setNull(pos, Types.TIMESTAMP);
		}
	}

	protected void setTimestamp(CallableStatement stmt, int pos,
			java.util.Date value) throws SQLException {
		if (value != null) {
			logger.debug("setTimestamp(" + pos + ", " + value + ")");
			stmt.setTimestamp(pos, new Timestamp(value.getTime()));
		} else {
			logger.debug("setNull(" + pos + ", Types.TIMESTAMP)");
			stmt.setNull(pos, Types.TIMESTAMP);
		}
	}

	protected void setTimestamp(CallableStatement stmt, String col,
			Timestamp value) throws SQLException {
		if (value != null) {
			logger.debug("setTimestamp(" + col + ", " + value + ")");
			stmt.setTimestamp(col, value);
		} else {
			logger.debug("setNull(" + col + ", Types.TIMESTAMP)");
			stmt.setNull(col, Types.TIMESTAMP);
		}
	}

	protected void setTimestamp(CallableStatement stmt, String col,
			Calendar value) throws SQLException {
		if (value != null) {
			logger.debug("setTimestamp(" + col + ", " + value + ")");
			stmt.setTimestamp(col, new Timestamp(value.getTimeInMillis()));
		} else {
			logger.debug("setNull(" + col + ", Types.TIMESTAMP)");
			stmt.setNull(col, Types.TIMESTAMP);
		}
	}

	protected void setTimestamp(CallableStatement stmt, String col,
			java.util.Date value) throws SQLException {
		if (value != null) {
			logger.debug("setTimestamp(" + col + ", " + value + ")");
			stmt.setTimestamp(col, new Timestamp(value.getTime()));
		} else {
			logger.debug("setNull(" + col + ", Types.TIMESTAMP)");
			stmt.setNull(col, Types.TIMESTAMP);
		}
	}

	protected void setDate(CallableStatement stmt, int pos, java.util.Date value)
			throws SQLException {
		if (value != null) {
			logger.debug("setDate(" + pos + ", " + value + ")");
			stmt.setDate(pos, new java.sql.Date(value.getTime()));
		} else {
			logger.debug("setNull(" + pos + ", Types.DATE)");
			stmt.setNull(pos, Types.DATE);
		}
	}

	protected void setDate(CallableStatement stmt, int pos, Calendar value)
			throws SQLException {
		if (value != null) {
			logger.debug("setDate(" + pos + ", " + value + ")");
			stmt.setDate(pos, new java.sql.Date(value.getTimeInMillis()));
		} else {
			logger.debug("setNull(" + pos + ", Types.DATE)");
			stmt.setNull(pos, Types.DATE);
		}
	}

	protected void setInteger(CallableStatement stmt, int pos, Integer value)
			throws SQLException {
		if (value != null) {
			logger.debug("setInteger(" + pos + ", " + value + ")");
			stmt.setInt(pos, value.intValue());
		} else {
			logger.debug("setNull(" + pos + ", Types.INTEGER)");
			stmt.setNull(pos, Types.INTEGER);
		}
	}
	
	protected void setInt(CallableStatement stmt, int pos, int value)
			throws SQLException {
		logger.debug("setInteger(" + pos + ", " + value + ")");
		stmt.setInt(pos, value);
	}

	protected void setDouble(CallableStatement stmt, int pos, Double value)
			throws SQLException {
		if (value != null) {
			logger.debug("setDouble(" + pos + ", " + value + ")");
			stmt.setDouble(pos, value.doubleValue());
		} else {
			logger.debug("setNull(" + pos + ", Types.DOUBLE)");
			stmt.setNull(pos, Types.DOUBLE);
		}
	}

	protected void setDouble(CallableStatement stmt, int pos, double value)
			throws SQLException {
		logger.debug("setDouble(" + pos + ", " + value + ")");
		stmt.setDouble(pos, value);
	}

	protected void setBoolean(CallableStatement stmt, int pos, Boolean value)
			throws SQLException {
		if (value != null) {
			logger.debug("setBoolean(" + pos + ", " + value + ")");
			//stmt.setString(pos, value.booleanValue() ? "Y" : "N");
			stmt.setBoolean(pos, value);
		} else {
			logger.debug("setNull(" + pos + ", Types.VARCHAR)");
			stmt.setNull(pos, Types.VARCHAR);
		}
	}

	protected void setBoolean(CallableStatement stmt, int pos, boolean value)
			throws SQLException {
		logger.debug("setBoolean(" + pos + ", " + value + ")");
		setBoolean(stmt, pos, Boolean.valueOf(value));
	}

	protected void setLong(CallableStatement stmt, int pos, Long value)
			throws SQLException {
		if (value != null) {
			logger.debug("setLong(" + pos + ", " + value + ")");
			stmt.setLong(pos, value.longValue());
		} else {
			logger.debug("setNull(" + pos + ", Types.INTEGER)");
			stmt.setNull(pos, Types.INTEGER);
		}
	}

	public boolean asBoolean(String chr) {
		return "Y".equals(chr);
	}

	public static Calendar setTimeToMin(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, c.getMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getMinimum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getMinimum(Calendar.MILLISECOND));
		return c;
	}

	public static Calendar setTimeToMax(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, c.getMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getMaximum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getMaximum(Calendar.MILLISECOND));
		return c;
	}

	public static Date calendarToDate(Calendar c) {
		Date d = null;
		if (c != null) {
			d = c.getTime();
		}
		return d;
	}

	public static Calendar dateToCalendar(Date date) {
		Calendar retVal = null;
		if (date != null) {
			retVal = new GregorianCalendar();
			retVal.setTime(date);
		}
		return retVal;
	}

	protected CallableStatement createProcedure(Connection connection, String name, int argsNum) throws SQLException {

		StringBuffer sb = new StringBuffer(10);

		for (int i = 0; i < argsNum; i++) {

			if (i < (argsNum - 1)) {
				sb.append("?, ");
			} else {
				sb.append("?");
			}
		}

		return connection.prepareCall("{ call "
				+ name
				+ "(" + sb.toString() + ") }");
	}

	protected CallableStatement createFunction(Connection connection,
			String name, int argsNum) throws SQLException {

		StringBuffer sb = new StringBuffer(10);

		for (int i = 0; i < argsNum; i++) {

			if (i < (argsNum - 1)) {
				sb.append("?, ");
			} else {
				sb.append("?");
			}
		}

		return connection.prepareCall("{ call ? := "
				+ name
				+ "(" + sb.toString() + ") }");
	}

	protected Connection getConnection() throws DalException {
		try {
			Connection conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			return conn;
		} catch (Exception e) {
			throw new DalException(-1, e);
		}
	}

	protected void closeSQLObjects(Connection connection, ResultSet rs,
			CallableStatement stmt) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setMaxResultSize(int value) {
		maxResultSize = value;
	}

	public int getMaxResultSize() {
		return maxResultSize;
	}

	protected abstract Object fillObject(ResultSet rs)
			throws SQLException;

	protected Long getLong(ResultSet rs, String columnName) throws SQLException {
		Long retVal = null;
		Long value = rs.getLong(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}

	protected Integer getInt(ResultSet rs, String columnName) throws SQLException {
		Integer retVal = null;
		Integer value = rs.getInt(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}
	
	protected String getString(ResultSet rs, String columnName)
			throws SQLException {
		String retVal = null;
		String value = rs.getString(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}

	protected Boolean getBool(ResultSet rs, String columnName)
			throws SQLException {
		Boolean retVal = null;
		Boolean value = rs.getBoolean(columnName);
		if (!rs.wasNull()) {
			retVal = value;
		}
		return retVal;
	}
	
	protected Calendar getTimestamp(ResultSet rs, String columnName)
			throws SQLException {
		Calendar retVal = null;
		Timestamp value = rs.getTimestamp(columnName);
		if (!rs.wasNull()) {
			retVal = asCalendar(value);
		}
		return retVal;
	}

	protected Calendar asCalendar(Timestamp value) {
		if (value == null) {
			return null;
		}
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(value.getTime());

		return result;
	}

	protected Calendar asCalendar(Date value) {
		if (value == null) {
			return null;
		}
		Calendar result = Calendar.getInstance();
		result.setTimeInMillis(value.getTime());

		return result;
	}

}
