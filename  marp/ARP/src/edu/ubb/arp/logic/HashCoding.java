package edu.ubb.arp.logic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import edu.ubb.arp.dao.jdbc.JdbcDaoFactory;
import edu.ubb.arp.exceptions.BllExceptions;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *
 */
public class HashCoding {
	private static final Logger logger = Logger.getLogger(JdbcDaoFactory.class);
	/**
	 * constructor
	 * @param str is the string to be hashed
	 * @return returns the hashed string
	 * @throws BllExceptions
	 */
	public static byte[] hashString(String str) throws BllExceptions {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			logger.debug("HashCoding" + ".hashString(String str) -> NO_SUCH_ALGORITHM_EXCEPTION");
			throw new BllExceptions(BllExceptions.NO_SUCH_ALGORITHM_EXCEPTION,e);
		}
        md.update(str.getBytes());
        return md.digest();
	}
}
