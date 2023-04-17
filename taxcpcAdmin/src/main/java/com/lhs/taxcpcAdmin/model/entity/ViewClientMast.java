/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "view_client_mast")
public class ViewClientMast implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "client_code", length = 15, nullable = false)
	private String client_code;
	@Column(name = "client_name", length = 100, nullable = true)
	private String client_name;
	@Column(name = "entity_code", length = 2, nullable = true)
	private String entity_code;
	@Column(name = "parent_code", length = 15, nullable = true)
	private String parent_code;
	@Column(name = "g_parent_code", length = 15, nullable = true)
	private String g_parent_code;
	@Column(name = "sg_parent_code", length = 15, nullable = true)
	private String sg_parent_code;
	@Column(name = "ssg_parent_code", length = 15, nullable = true)
	private String ssg_parent_code;
	@Column(name = "sssg_parent_code", length = 15, nullable = true)
	private String sssg_parent_code;
	@Column(name = "remark", length = 240, nullable = true)
	private String remark;
	@Column(name = "initiate_date", nullable = true)
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date initiate_date;
	@Column(name = "client_type_code", length = 5, nullable = true)
	private String client_type_code;
	@Column(name = "client_type_name", length = 26, nullable = true)
	private String client_type_name;
	@Column(name = "client_catg_code", length = 5, nullable = true)
	private String client_catg_code;
	@Column(name = "client_catg_name", length = 31, nullable = true)
	private String client_catg_name;
	@Column(name = "client_status", length = 1, nullable = true)
	private String client_status;
	@Column(name = "closed_date", nullable = true)
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date closed_date;
	@Column(name = "approvedby", length = 8, nullable = true)
	private String approvedby;
	@Column(name = "approveddate", nullable = true)
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date approveddate;
	@Column(name = "reference_remark", length = 240, nullable = true)
	private String reference_remark;
	@Column(name = "panno", length = 10, nullable = true)
	private String panno;
	@Column(name = "tanno", length = 12, nullable = true)
	private String tanno;
	@Column(name = "login_id", length = 100, nullable = true)
	private String login_id;
	@Column(name = "login_pwd", length = 100, nullable = true)
	private String login_pwd;
	@Column(name = "web1_login_id", length = 100, nullable = true)
	private String web1_login_id;
	@Column(name = "web1_login_pwd", length = 100, nullable = true)
	private String web1_login_pwd;
	@Column(name = "deductor_pin", length = 6, nullable = true)
	private String deductor_pin;
	@Column(name = "deductor_state_code", length = 2, nullable = true)
	private String deductor_state_code;
	@Column(name = "deductor_panno", length = 10, nullable = true)
	private String deductor_panno;
	@Column(name = "code_level", nullable = true)
	private String code_level;
	@Column(name = "login_pwd_expiry_days", length = 4, nullable = true)
	private String login_pwd_expiry_days;
	@Column(name = "closed_remark", length = 4000, nullable = true)
	private String closed_remark;
	@Column(name = "bank_branch_code", length = 15, nullable = true)
	private String bank_branch_code;
	@Column(name = "is_client_parent_record")
	private String is_client_parent_record;
	@Column(name = "is_client_tran_level")
	private String is_client_tran_level;
	@Column(name = "is_client_login_level")
	private String is_client_login_level;
	@Column(name = "client_parent_code_str")
	private String client_parent_code_str;
	@Column(name = "client_level_type")
	private String client_level_type;

	@Column(name = "temp_zen_deductor_id")
	private String temp_zen_deductor_id;

	private String add_right;
	private String edit_right;
	private String delete_right;
	private String query_right;
	private String print_right;
	private String special_right;
	private String vauation_right;
	private String approved_right;
}// end class
