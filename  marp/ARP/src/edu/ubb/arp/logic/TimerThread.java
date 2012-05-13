package edu.ubb.arp.logic;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.DaoFactory;
import edu.ubb.arp.dao.RequestsDao;
import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.DalException;

public class TimerThread implements Runnable {
	private static final Logger logger = Logger.getLogger(JdbcDaoFactory.class);
	private int currentWeek;
	private Calendar startDate;
	private Calendar currentDate;
	DaoFactory instance;
	RequestsDao requests;

	public TimerThread() {
		startDate = new GregorianCalendar();
		startDate.set(2007,00,01); // Starting week, this is the 0rd week in the database.
		currentDate = new GregorianCalendar();

		currentWeek = weeksBetween(startDate.getTime(), currentDate.getTime());
	}

	public void run() {
		String methodName = "." + Thread.currentThread().getStackTrace()[1].getMethodName() + "() ";
		int errmsg = 0;
		logger.debug(getClass().getName() + methodName + "-> START");
		
		while (true) {
			try {
				Thread.sleep( 3600000 ); // sleep for 1 hour
				
				int weekBeetween = weeksBetween(startDate.getTime(),currentDate.getTime());

				if (currentWeek != weekBeetween) { //
					currentWeek = weekBeetween;
					instance = JdbcDaoFactory.getInstance();
					requests = instance.getRequestsDao();	
					
					requests.removeExpiredRequests(currentWeek);
					if (errmsg < 0) {
						logger.error(getClass().getName() + methodName + DalException.errCodeToMessage(errmsg));
					}
				}

			} catch (InterruptedException e) {
				logger.error(getClass().getName() + methodName + "InterruptedException: " + e);
				e.printStackTrace();
			} catch (DalException e) {
				logger.error(getClass().getName() + methodName + e.getErrorMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				logger.error(getClass().getName() + methodName + "SQL Exception: " + e);
				e.printStackTrace();
			}				
		}
	}

	private int weeksBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24 * 7));
	}
}