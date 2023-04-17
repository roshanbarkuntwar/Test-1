package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;



public class ReqTranAttachId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "slno")
	private int slno;
	
	@Column(name = "req_code")
	private String req_code;
	
	public ReqTranAttachId() {
		
	}
	
	public ReqTranAttachId(int slno, String req_code) {
		super();
		this.slno = slno;
		this.req_code = req_code;
	}
	

}
