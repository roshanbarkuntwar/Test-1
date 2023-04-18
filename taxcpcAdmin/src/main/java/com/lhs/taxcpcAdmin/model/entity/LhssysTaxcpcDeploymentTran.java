package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lhssys_taxcpc_deployment_tran")
public class LhssysTaxcpcDeploymentTran implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "seq_id" ,nullable = false)
	private Long seq_id;
	@Column(name = "project_code" ,length=20)
	private String project_code;
	@Column(name = "war_filename" ,length=50)
	private String war_filename;
	@Column(name = "sample_data_filter_str" ,length=200)
	private String sample_data_filter_str;
	@Column(name = "remark" ,length=500)
	private String remark;
	@Column(name = "server_url" ,length=50)
	private String server_url;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	@Column(name = "depl_code" ,length=20)
	private String depl_code;
	
}
