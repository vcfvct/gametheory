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

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.golookon.gametheory.entity.BaseEntity;




/**
 * @author ppattapu
 *
 */
public interface BaseDao {
	
	/**
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public <T extends BaseEntity> void remove(T object) throws DataAccessException;
	
	/**
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public <T extends BaseEntity> T merge(T object) throws DataAccessException;

	/**
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public <T extends BaseEntity> void persist(T object) throws DataAccessException;
	
	/**
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public <T extends BaseEntity> T load(Class<T> clazz, long id) throws DataAccessException;
	
	/**
	 * Find.
	 * 
	 * @param query the query
	 * @param args the args
	 * 
	 * @return the list< t>
	 */
	public <T extends BaseEntity> List<T> find (String query , Object...args);

	/**
	 * Find All.
	 * 
	 * @param query the query
	 * @param args the args
	 * 
	 * @return the list< t>
	 */
	public <T extends BaseEntity> List<T> findAll (Class<T> clazz);
	
}
