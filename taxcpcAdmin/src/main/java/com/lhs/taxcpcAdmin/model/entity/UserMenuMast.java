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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_menu_mast")
public class UserMenuMast implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "menu_code" ,length=20,nullable = false)
	private String menu_code;
	@Column(name = "menu_name" ,length=50)
	private String menu_name;
	@Column(name = "menu_url" ,length=500)
	private String menu_url;
	@Column(name = "parent_code" ,length=50)
	private String parent_code;
	@Column(name = "menu_seq" ,length=50)
	private String menu_seq;
	@Column(name = "menu_status" ,length=1)
	private String menu_status;
	@Column(name = "para_value1" ,length=50)
	private String para_value1;
	@Column(name = "para_value2" ,length=50)
	private String para_value2;
	@Column(name = "para_name1" ,length=50)
	private String para_name1;
	@Column(name = "para_name2" ,length=50)
	private String para_name2;
	@Column(name = "createdby" ,length=15)
	private String createdby;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	
}
