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

/**
*
* @author sakshi.bandhate
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_role_mast")
public class UserRoleMast implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "role_code" ,length=20,nullable = false)
	private String role_code;
	@Column(name = "role_name" ,length=100)
	private String role_name;
	@Column(name = "short_role_name" ,length=50)
	private String short_role_name;
	@Column(name = "assigned_menu" ,length=500)
	private String assigned_menu;
	@Column(name = "assigned_dashboard" ,length=50)
	private String assigned_dashboard;
	@Column(name = "role_status" ,length=1)
	private String role_status;
	@Column(name = "createdby" ,length=15)
	private String createdby;
	@Column(name = "role_type_code" ,length=5)
	private String role_type_code;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	
	
}
