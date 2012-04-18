package edu.ubb.arp.dao;

import java.util.List;

import edu.ubb.arp.dao.model.Groups;
import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public interface UsersDao {

	public int createUser(String userName, byte[] password, String phoneNumber, String email, String resourceName,
			boolean active, String resourceGroupName) throws DalException;	

	public int changeUserName(String oldUserName, String newUserName) throws DalException;
	
	public int changePassword(String userName, byte[] oldPassword, byte[] newPassword) throws DalException;
	
	public int changeEmail(String userName, String newEmail) throws DalException;
	
	public int changePhoneNumber(String userName, String newPhoneNumber) throws DalException;
	
	public int changeResourceName(String userName, String newResourceName) throws DalException;
	
	public int changeUserName(int userID, String newUserName) throws DalException;
	
	public int changePassword(int userID, byte[] oldPassword, byte[] newPassword) throws DalException;
	
	public int changeEmail(int userID, String newEmail) throws DalException;
	
	public int changePhoneNumber(int userID, String newPhoneNumber) throws DalException;
	
	public int changeResourceName(int userID, String newResourceName) throws DalException;
	
	public int setActive(String userName, boolean active) throws DalException;
	
	public int addUserToGroup(String userName, String groupName) throws DalException;
	
	public int addUserToGroup(Users user, Groups group) throws DalException;
	
	public int addUserToGroup(String userName, String[] groupName) throws DalException;
	
	public int addUserToGroups(Users users, List<Groups> groups) throws DalException;
	
	public int removeUserFromGroup(String userName, String groupName) throws DalException;

	public Users loadUser(String userName) throws DalException;
	

}
