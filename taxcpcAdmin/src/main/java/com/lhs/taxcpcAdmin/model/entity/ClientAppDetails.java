package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lhs_taxcpc_client_app_details")
public class ClientAppDetails implements java.io.Serializable  {
		
	private static final long serialVersionUID = 1L;
		
		@Id
		@Column(name = "client_code", length = 100, nullable = false)
		private String client_code;
		
		@Column(name = "application_type", length = 50, nullable = true)
		private String application_type;
		
		@Column(name = "app_name", length = 60, nullable = true)
		private String app_name;
		
		@Column(name = "app_url", length = 100, nullable = true)
		private String app_url;
		
		@Column(name = "connected_db_username", length = 50, nullable = true)
		private String connected_db_username;
		
		@Column(name = "connected_db_pwd", length = 50, nullable = true)
		private String connected_db_pwd;
		
		@Column(name = "connected_db_sid", length = 50, nullable = true)
		private String connected_db_sid;
		
		@Column(name = "connected_port", length = 50, nullable = true)
		private String connected_port;
		
		@Column(name = "connected_db_remark", length = 50, nullable = true)
		private String connected_db_remark;
		
		@Column(name = "lastupdate" )
//		@Temporal(javax.persistence.TemporalType.DATE)
		private Date lastupdate;
		
		
		@Column(name = "flag", length = 1, nullable = true)
		private String flag;
		

		@Column(name = "remark", length = 250, nullable = true)
		private String remark;
		
}
