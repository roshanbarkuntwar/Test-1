/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author akash.dev
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entity_mast")
public class EntityMast implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4695058405143038093L;

	
	
	
	
	@Id
	@Column(name = "entity_code", length = 2, nullable = false)
	private String entity_code;
	@Column(name = "entity_name", length = 100, nullable = false)
	private String entity_name;
	@Column(name = "parent_code", length = 2, nullable = true)
	private String parent_code;
	@Column(name = "codetype", length = 1, nullable = true)
	private String codetype;
	@Column(name = "user_code", length = 8, nullable = true)
	private String user_code;
	@Column(name = "flag", length = 1, nullable = true)
	private String flag;
	@Column(name = "eccno", length = 20, nullable = true)
	private String eccno;
	@Column(name = "rangeno", length = 30, nullable = true)
	private String rangeno;
	@Column(name = "range_name", length = 60, nullable = true)
	private String range_name;
	@Column(name = "rangeadd1", length = 40, nullable = true)
	private String rangeadd1;
	@Column(name = "rangeadd2", length = 40, nullable = true)
	private String rangeadd2;
	@Column(name = "division", length = 60, nullable = true)
	private String division;
	@Column(name = "collector", length = 60, nullable = true)
	private String collector;
	@Column(name = "add1", length = 4000, nullable = true)
	private String add1;
	@Column(name = "add2", length = 40, nullable = true)
	private String add2;
	@Column(name = "add3", length = 100, nullable = true)
	private String add3;
	@Column(name = "city", length = 20, nullable = true)
	private String city;
	@Column(name = "pin", length = 6, nullable = true)
	private String pin;
	@Column(name = "district", length = 20, nullable = true)
	private String district;
	@Column(name = "country", length = 25, nullable = true)
	private String country;
	@Column(name = "phone", length = 100, nullable = true)
	private String phone;
	@Column(name = "email", length = 100, nullable = true)
	private String email;
	@Column(name = "panno", length = 20, nullable = true)
	private String panno;
	@Column(name = "stno", length = 50, nullable = true)
	private String stno;
	@Column(name = "cstno", length = 50, nullable = true)
	private String cstno;
	@Column(name = "regd_office", length = 50, nullable = true)
	private String regd_office;
	@Column(name = "regn_no", length = 50, nullable = true)
	private String regn_no;
	@Column(name = "tariff_no", length = 50, nullable = true)
	private String tariff_no;
	@Column(name = "plano", length = 50, nullable = true)
	private String plano;
	@Column(name = "pf_no", length = 11, nullable = true)
	private String pf_no;
	@Column(name = "esic_no", length = 15, nullable = true)
	private String esic_no;
	@Column(name = "stdcode", length = 10, nullable = true)
	private String stdcode;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastupdate", nullable = false)
	private Date lastupdate;
	@Column(name = "fax", length = 50, nullable = true)
	private String fax;
	@Column(name = "tanno", length = 11, nullable = true)
	private String tanno;
	@Column(name = "factory_add", length = 100, nullable = true)
	private String factory_add;
	@Column(name = "fa_acc_year", length = 5, nullable = true)
	private String fa_acc_year;
	@Column(name = "currency_code", length = 3, nullable = true)
	private String currency_code;
	@Column(name = "print_name", length = 50, nullable = true)
	private String print_name;
	@Column(name = "acc_code", length = 5, nullable = true)
	private String acc_code;
	@Column(name = "default_godown_code", length = 5, nullable = true)
	private String default_godown_code;
	@Column(name = "default_batchno", length = 25, nullable = true)
	private String default_batchno;
	@Column(name = "default_ref_slno", length = 25, nullable = true)
	private String default_ref_slno;
	@Column(name = "default_dept_code", length = 5, nullable = true)
	private String default_dept_code;
	@Column(name = "default_cost_code", length = 5, nullable = true)
	private String default_cost_code;
	@Column(name = "iec_no", length = 15, nullable = true)
	private String iec_no;
	@Column(name = "bin_no", length = 15, nullable = true)
	private String bin_no;
	@Column(name = "cinno", length = 50, nullable = true)
	private String cinno;
	@Column(name = "website", length = 100, nullable = true)
	private String website;
	@Column(name = "db_user", length = 15, nullable = true)
	private String db_user;
	@Column(name = "db_user_pwd", length = 15, nullable = true)
	private String db_user_pwd;
	
	
	@Transient
	transient private MultipartFile file_logo;



	
}// end class
