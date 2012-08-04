/***********************************************************

 The information in this document is proprietary
 to VeriSign and the VeriSign Product Development.
 It may not be used, reproduced or disclosed without
 the written approval of the General Manager of
 VeriSign Product Development.

 PRIVILEGED AND CONFIDENTIAL
 VERISIGN PROPRIETARY INFORMATION
 REGISTRY SENSITIVE INFORMATION

 Copyright (c) 2007 VeriSign, Inc.  All rights reserved.

 ***********************************************************/

package com.golookon.gametheory.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.golookon.gametheory.entity.BaseEntity;



/**
 * @author ppattapu
 * 
 */
@Repository
@Transactional
public class BaseDaoImpl implements BaseDao {

	@PersistenceContext
	protected EntityManager em;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.verisign.vap.dao.BaseDao#load(java.lang.Class, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public <T extends BaseEntity> T load(Class<T> clazz, long id)
			throws DataAccessException {
		return this.em.find(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.verisign.vap.dao.BaseDao#remove(java.lang.Object)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <T extends BaseEntity> void remove(T object)
			throws DataAccessException {
		T managed = em.merge(object);
		em.remove(managed);
		em.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.verisign.vap.dao.BaseDao#merge(java.lang.Object)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <T extends BaseEntity> T merge(T object)
			throws DataAccessException {
		T merged = em.merge(object);
		em.flush();
		return merged;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.verisign.vap.dao.BaseDao#persist(java.lang.Object)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <T extends BaseEntity> void persist(T object)
	throws DataAccessException {
		em.persist(object);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <T extends BaseEntity> List<T> find(String query, Object... args)
			throws DataAccessException {
		return null;
	}

//	@Override
//	@Transactional(propagation=Propagation.REQUIRED)
//	@SuppressWarnings("unchecked")
//	public <T extends BaseEntity> List<T> findAll(Class<T> clazz) throws DataAccessException {
//		Query query = em.createNamedQuery(clazz.getSimpleName() + ".findAll");
//		List<T> results = query.getResultList();
//		if (results == null) {
//			results = new ArrayList<T>();
//		}
//		return results;
//	}
//	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> findAll(Class<T> clazz) throws DataAccessException {
		System.out.println("SELECT t FROM " + clazz.getName() + " t");
		Query query = em.createQuery("SELECT t FROM " + clazz.getName() + " t");
		List<T> results = query.getResultList();
		if (results == null) {
			results = new ArrayList<T>();
		}
		return results;
	}
	
}
