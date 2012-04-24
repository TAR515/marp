package edu.ubb.arp.dao;

import java.sql.SQLException;
import java.util.List;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.Users;

public interface UsersDao {

	public int createUser(String userName, byte[] password, String phoneNumber, String email, String resourceName,
			boolean active, String resourceGroupName) throws SQLException;

	public int changeUserName(String oldUserName, String newUserName) throws SQLException;
	
	public int changePassword(String userName, byte[] oldPassword, byte[] newPassword) throws SQLException;
	
	public int changeEmail(String userName, String newEmail) throws SQLException;
	
	public int changePhoneNumber(String userName, String newPhoneNumber) throws SQLException;
	
	public int changeResourceName(String userName, String newResourceName) throws SQLException;
	
	public int changeUserName(int userID, String newUserName) throws SQLException;
	
	public int changePassword(int userID, byte[] oldPassword, byte[] newPassword) throws SQLException;
	
	public int changeEmail(int userID, String newEmail) throws SQLException;
	
	public int changePhoneNumber(int userID, String newPhoneNumber) throws SQLException;
	
	public int changeResourceName(int userID, String newResourceName) throws SQLException;
	
	public int setActive(String userName, boolean active) throws SQLException;
	
	public int addUserToGroup(String userName, String groupName) throws SQLException;
	
	public int addUserToGroup(Users user, Groups group) throws SQLException;
	
	public int addUserToGroup(String userName, String[] groupName) throws SQLException;
	
	public int addUserToGroups(Users users, List<Groups> groups) throws SQLException;
	
	public int removeUserFromGroup(String userName, String groupName) throws SQLException;

	public Users loadUser(String userName) throws SQLException;
	

}
