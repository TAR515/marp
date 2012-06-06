package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.datastructures.Booking;
import edu.ubb.arp.exceptions.DalException;
/**
 * 
 * @author VargaAdorjan , TurdeanArnoldRobert
 *interface contains methods which work with the Users
 */
public interface UsersDao {
	/**
	 * creates a new user
	 * @param userName is the name of the user which will be created
	 * @param password is the password of the user which will be created
	 * @param phoneNumber is the phone number of the  user which will be created
	 * @param email is the email of the  user which will be created
	 * @param resourceName is the resource name of the user which will be created
	 * @param active is true (user is hired)
	 * @param resourceGroupName is the name of the group in which the user will be added
	 * @return the id of the created user if there was no error , otherwise an error message 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int createUser(String userName, byte[] password, String phoneNumber, String email, String resourceName,
			boolean active, String resourceGroupName) throws SQLException, DalException;
	/**
	 * changes the user's name
	 * @param oldUserName is the old user name of the user
	 * @param newUserName is the new user name of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int changeUserName(String oldUserName, String newUserName) throws SQLException, DalException;
	/**
	 * changes the password of the user
	 * @param userName is the name of the user who's password will be changed
	 * @param oldPassword is the old password of the user
	 * @param newPassword is the new password of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int changePassword(String userName, byte[] oldPassword, byte[] newPassword) throws SQLException, DalException;
	/**
	 * changes the email of the user
	 * @param userName is the name of the user who's email will be changed
	 * @param newEmail is the new email of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int changeEmail(String userName, String newEmail) throws SQLException, DalException;
	/**
	 * changes the phone number of the user 
	 * @param userName is the name of the user who's email will be changed
	 * @param newPhoneNumber is the new phone number of the user
	 * @return  the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int changePhoneNumber(String userName, String newPhoneNumber) throws SQLException, DalException;
	/**
	 * changes user's resource name
	 * @param userName is the name of the user who's resource name will be changed
	 * @param newResourceName is the new resource name of the user
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int changeResourceName(String userName, String newResourceName) throws SQLException, DalException;
	/**
	 * changes the name of the user 
	 * @param userID is the id of the user who's user name will be changed
	 * @param newUserName is the new user name of the user
	 * @return return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void changeUserName(int userID, String newUserName) throws SQLException, DalException;
	/**
	 * changes user's password 
	 * @param userID is the id of the user who's user name will be changed
	 * @param oldPassword is the old password of the user 
	 * @param newPassword is the new password of the user 
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void changePassword(int userID, byte[] oldPassword, byte[] newPassword) throws SQLException, DalException;
	/**
	 * changes users's email
	 * @param userID is the id of the user who's user name will be changed
	 * @param newEmail is the new email of the user 
	 * @throws SQLException SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void changeEmail(int userID, String newEmail) throws SQLException, DalException;
	/**
	 * changes user's phone number
	 * @param userID is the id of the user who's user name will be changed
	 * @param newPhoneNumber is the new phone number of the user
	 * @throws SQLException SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void changePhoneNumber(int userID, String newPhoneNumber) throws SQLException, DalException;
	/**
	 * changes user's resource name
	 * @param userID is the id of the user who's user name will be changed
	 * @param newResourceName is the new resource name of the user
	 * @throws SQLException SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public void changeResourceName(int userID, String newResourceName) throws SQLException, DalException;
	/**
	 * sets user active attribute to active(true/false)
	 * @param userName is the name of the user who's active attribute will be updated
	 * @param active is true if the user will be hired or false if fired 
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int setActive(String userName, boolean active) throws SQLException, DalException;
	/**
	 * adds user to a group
	 * @param userName is the name of the user to add to the group
	 * @param groupName is the name of the group to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroup(String userName, String groupName) throws SQLException, DalException;
	/**
	 * adds user to a group
	 * @param user is the user to add to the group
	 * @param group is the group to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroup(Users user, Groups group) throws SQLException, DalException;
	/**
	 * adds user to multiple groups
	 * @param userName is the name of the user to add to the group
	 * @param groupName is a list of groups to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroup(String userName, String[] groupName) throws SQLException, DalException;
	/**
	 * adds user to multiple groups
	 * @param users user is the user to add to the group
	 * @param groups is a list of groups to which the user will be added
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int addUserToGroups(Users users, List<Groups> groups) throws SQLException, DalException;
	/**
	 * removes the user from a the group
	 * @param userName is the name of the user who will be removed from the group
	 * @param groupName is name of the group from which the user will removed from
	 * @return the id of the user if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public int removeUserFromGroup(String userName, String groupName) throws SQLException, DalException;
	/**
	 * loads users attributes
	 * @param userName is the name of the user who's attributes will be loaded
	 * @return returns users attributes if there was no error , otherwise an error message
	 * @throws SQLException if there is no connection
	 * @throws DalException  if a stored procedure returns an error message
	 */
	public Users loadUser(String userName) throws SQLException, DalException;
	/**
	 * checks if exists the given user with the given password
	 * @param userName is the name of the user which will be checked if exists 
	 * @param password is the password of the user which will be checked if exists 
	 * @return returns 1 if exists , an error message if not 
	 * @throws SQLException  if there is no connection
	 */
	public int checkUserNameAndPassword(String userName, byte[] password) throws SQLException;
	/**
	 * loads the booking of the user from current week for 4 weeks
	 * @param userName is the name of the user who's booking will be loaded
	 * @param currentWeek is the starting week from which the booking will be loaded 
	 * @return returns the booking of the user from current week for 4 weeks
	 * @throws SQLException if there is no connection
	 * @throws DalException if a stored procedure returns an error message
	 */
	public List<Booking> LoadAssignments(String userName, int currentWeek) throws SQLException, DalException;

}
