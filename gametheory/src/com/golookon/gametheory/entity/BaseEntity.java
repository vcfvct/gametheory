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

package com.golookon.gametheory.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * @author leon
 *
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

	/**
	 * This is the identifier for all the domain objects.
	 */
	@Id 
	@GeneratedValue
	protected Long id;
	
	public BaseEntity() {
	}

	public BaseEntity(Long id) {
		super();
		this.id = id;
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * obj must be not null and of the same class.
	 * Then check its id.
	 * @return true if obj is not null, and obj and this are of the same class, and their ids are equal;
	 * otherwise false.
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ( obj == null )	return false;
		
		if ( !(obj instanceof BaseEntity) )	return false;

		// obj.class is a subclass of this.class, or
		// this.class is a subclass of obj.class?
		if ( !obj.getClass().isAssignableFrom( getClass() ) &&
			 !getClass().isAssignableFrom( obj.getClass() ) )	
			return false;

		return isEmpty() ? ((BaseEntity)obj).isEmpty() : getId().equals( ((BaseEntity)obj).getId() );
	}

	/**
	 * @return getId() 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return isEmpty() ? 0 : getId().intValue();
	}


	@Transient
	public boolean isEmpty() {
		return getId() == null || getId().intValue() == 0;
	}
	
	@Transient
	public boolean isNew() {
		return (getId() == null);
	}

}
