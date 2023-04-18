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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "lhs_taxcpc_server_details")

public class ServerDetailModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GenericGenerator(name = "generator", strategy = "sequence-identity")
	@GeneratedValue(generator = "generator")
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "server_id")
	private String server_id;
	
	@Column(name = "entity_code")
	private String entity_code;

	@Column(name = "configuration_type")
	private String configuration_type;
	
	@Column(name = "server_type_code")
	private String server_type_code;
	
	@Column(name = "server_owner_name")
	private String server_owner_name;
	
	@Column(name = "parent_server")
	private String parent_server;
	
	@Column(name = "server_ip")
	private String server_ip;
	
	@Column(name = "public_ip")
	private String public_ip;
	
	@Column(name = "host_name")
	private String host_name;
	
	@Column(name = "server_os")
	private String server_os;
	
	@Column(name = "remote_username")
	private String remote_username;
	
	@Column(name = "remote_pwd")
	private String remote_pwd;
	
	@Column(name = "mapped_drive")
	private String mapped_drive;
	
	@Column(name = "tag_name")
	private String tag_name;
	
	@Column(name = "installed_db")
	private String installed_db;
	
	@Column(name = "installed_db_tool")
	private String installed_db_tool;
	
	@Column(name = "server_remark")
	private String server_remark;
	
	@Column(name = "app_server_name")
	private String app_server_name;
	
	@Column(name = "app_server_ip")
	private String app_server_ip;
	
	@Column(name = "app_server_port")
	private String app_server_port;
	
	
	@Column(name = "app_server_console_add")
	private String app_server_console_add;
	
	@Column(name = "app_server_log_path")
	private String app_server_log_path;
	
	@Column(name = "app_server_username")
	private String app_server_username;
	
	@Column(name = "app_server_pwd")
	private String app_server_pwd;
	
	@Column(name = "app_server_tag_name")
	private String app_server_tag_name;
	
	@Column(name = "app_server_remark")
	private String app_server_remark;
	
	@Column(name = "lastupdate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	@Column(name = "flag")
	private String flag;
	
	@Transient
	private String server_ip_config;
	
	
	@Transient
	private String hidden_config_type;
	
	
	
}
