/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lhs.taxcpcAdmin.model.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author sakshi.bandhate
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_login_tran")
public class UserLoginTran implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3703040975927753253L;

	@Column(name = "session_seqno", nullable = false)
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "user_login_tran_seq"))
	@GeneratedValue(generator = "generator")
	@Id
	private Long session_seqno;
	@Column(name = "login_time", nullable = false)
	private Timestamp login_time;
	@Column(name = "logout_time", nullable = true)
	private Timestamp logout_time;
	@Column(name = "logout_method", length = 1, nullable = true)
	private String logout_method;
	@Column(name = "machine_name", length = 100, nullable = true)
	private String machine_name;
	@Column(name = "machine_ip", length = 24, nullable = true)
	private String machine_ip;
	@Column(name = "machine_browser", length = 200, nullable = true)
	private String machine_browser;
	@Column(name = "machine_os_name", length = 100, nullable = true)
	private String machine_os_name;
	@Column(name = "machine_other_info", length = 4000, nullable = true)
	private String machine_other_info;
	@Column(name = "user_code", length = 200, nullable = true)
	private String user_code;
	@Column(name = "lastupdate", nullable = true)
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag", length = 1, nullable = true)
	private String flag;
	
	@Transient
	private String user_name;
	
	@Transient
	private String login_id;
	
	@Transient
	private String login_count;
	
	@Transient
	private String last_login_time;
	
	@Transient
	private String no_of_login_count;

}
