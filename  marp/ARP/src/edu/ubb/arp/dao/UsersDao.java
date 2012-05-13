package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public interface UsersDao {

	public int createUser(String userName, byte[] password, String phoneNumber, String email, String resourceName,
			boolean active, String resourceGroupName) throws SQLException, DalException;

	public int changeUserName(String oldUserName, String newUserName) throws SQLException, DalException;
	
	public int changePassword(String userName, byte[] oldPassword, byte[] newPassword) throws SQLException, DalException;
	
	public int changeEmail(String userName, String newEmail) throws SQLException, DalException;
	
	public int changePhoneNumber(String userName, String newPhoneNumber) throws SQLException, DalException;
	
	public int changeResourceName(String userName, String newResourceName) throws SQLException, DalException;
	
	public void changeUserName(int userID, String newUserName) throws SQLException, DalException;
	
	public void changePassword(int userID, byte[] oldPassword, byte[] newPassword) throws SQLException, DalException;
	
	public void changeEmail(int userID, String newEmail) throws SQLException, DalException;
	
	public void changePhoneNumber(int userID, String newPhoneNumber) throws SQLException, DalException;
	
	public void changeResourceName(int userID, String newResourceName) throws SQLException, DalException;
	
	public int setActive(String userName, boolean active) throws SQLException, DalException;
	
	public void addUserToGroup(String userName, String groupName) throws SQLException, DalException;
	
	public void addUserToGroup(Users user, Groups group) throws SQLException, DalException;
	
	public void addUserToGroup(String userName, String[] groupName) throws SQLException, DalException;
	
	public void addUserToGroups(Users users, List<Groups> groups) throws SQLException, DalException;
	
	public void removeUserFromGroup(String userName, String groupName) throws SQLException, DalException;

	public Users loadUser(String userName) throws SQLException, DalException;
	
	public HashMap<String, Boolean> getAllActiveProjects(String userName) throws SQLException, DalException;
	
	public int checkUserNameAndPassword(String userName, byte[] password) throws SQLException;

}
