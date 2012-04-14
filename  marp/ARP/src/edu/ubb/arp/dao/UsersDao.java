package edu.ubb.arp.dao;

import edu.ubb.arp.dao.model.Users;
import edu.ubb.arp.exceptions.DalException;

public interface UsersDao {

	public Users loadUser(String userName) throws DalException;
}
