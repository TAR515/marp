package edu.ubb.arp.dao;

import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public interface UsersDao {

	public void createUser(String userName,byte[] password, String phoneNumber,String email,
            String resourceName,boolean active,String resourceGroupName) throws DalException;

	public void setActive(String resourceName, boolean active) throws DalException;
	public Users loadUser(String userName) throws DalException;
}
