package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;

import javax.persistence.Column;

public class LhsTaxcpcDashPortletConfigID implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "PORTLET_SEQ_NO")
	private Long PORTLET_SEQ_NO;
	
	@Column(name = "PORTLET_TYPE")
	private String PORTLET_TYPE;
	
	public LhsTaxcpcDashPortletConfigID() {
		
	}
	
	public LhsTaxcpcDashPortletConfigID(Long PORTLET_SEQ_NO, String PORTLET_TYPE) {
		super();
		this.PORTLET_SEQ_NO = PORTLET_SEQ_NO;
		this.PORTLET_TYPE = PORTLET_TYPE;
	}
	

}