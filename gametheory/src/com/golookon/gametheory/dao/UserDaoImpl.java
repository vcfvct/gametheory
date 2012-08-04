package com.golookon.gametheory.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.golookon.gametheory.entity.User;

@Repository
@Transactional
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<User> getAllUsers() throws DataAccessException {
		return findAll(User.class);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public User findUserByName(String userName) throws DataAccessException {
		try {
			User user = (User) em.createNamedQuery("User.findByUsername").setParameter("username", userName).getSingleResult();
			return user;
		} catch (NoResultException nre) {
			return null;
		}
	}

}
