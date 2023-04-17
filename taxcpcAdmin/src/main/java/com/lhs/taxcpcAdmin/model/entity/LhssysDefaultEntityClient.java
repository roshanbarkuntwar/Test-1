package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lhssys_default_entity_client")
public class LhssysDefaultEntityClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "client_code", length = 15, nullable = false)
	private String client_code;
	
	@Column(name = "entity_code", length = 2)
	private String entity_code;

	@Column(name = "client_name", length = 100)
	private String client_name;
	
	
	@Column(name = "parent_code", length = 15, nullable = true)
	private String parent_code;
	
	@Column(name = "no_of_transaction", length = 50)
	private String no_of_transaction;
	
	@Column(name = "initiate_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date initiate_date;
	
	@Column(name = "closed_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date closed_date;
	
	
	@Column(name = "closed_remark", length = 4000)
	private String closed_remark;
	
	@Column(name = "lastupdate_from_fgs")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate_from_fgs;
	
	@Column(name = "lastupdate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	@Column(name = "flag" ,length=1)
	private String flag;
	
}
