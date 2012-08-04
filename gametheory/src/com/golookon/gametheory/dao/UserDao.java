package com.golookon.gametheory.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.golookon.gametheory.entity.User;


public interface UserDao extends BaseDao {
	public List<User> getAllUsers() throws DataAccessException;
	public User findUserByName(String userName) throws DataAccessException;


}
