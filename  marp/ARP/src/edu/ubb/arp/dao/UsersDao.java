package edu.ubb.arp.dao;

import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public interface UsersDao {

	public int createUser(String userName, byte[] password, String phoneNumber, String email, String resourceName,
			boolean active, String resourceGroupName) throws DalException;

	public int updateUser(String oldUserName, byte[] oldPassword, String oldGroup, String newUserName, byte[] newPassword, String newPhoneNumber,
			String newEmail, String newResourceName, boolean newActive, String newGroup) throws DalException;

	public int setActive(String userName, boolean active) throws DalException;

	public Users loadUser(String userName) throws DalException;
}
