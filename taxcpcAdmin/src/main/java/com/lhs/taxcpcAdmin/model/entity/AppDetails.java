package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

import lombok.Data;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "LHS_TAXCPC_APP_DETAILS")
public class AppDetails  implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id 
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "lhssys_app_details_seq"))
	@GeneratedValue(generator = "generator")
	@Column(name = "app_code")
	private Integer app_code;
	
	@Column (name = "server_id")
	private String server_id;
	
	@Column (name = "app_name")
	private String app_name;
	
	@Column (name = "local_app_url")
	private String local_app_url;
	
	@Column (name = "public_app_url")
	private String public_app_url;
	
	@Column (name = "app_remark")
	private String app_remark;
	
	@Column (name = "entity_code")
	private String entity_code;
	
	@Column (name = "remark1")
	private String remark1;
	
	@Column (name = "remark2")
	private String remark2;
	
	@Column (name = "server_ip")
	private String server_ip;
	
	@Column (name = "app_server_ip")
	private String app_server_ip;
	
	@Column (name = "app_server_name")
	private String app_server_name;
	
	@Column (name = "application_type")
	private String application_type;
	
	@Column (name = "jar_name")
	private String jar_name;
	
	
	@Column (name = "jar_parameter")
	private String jar_parameter;
	
	@Column (name = "project_code")
	private String project_code;
	
	
	@Column (name = "lastupdate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	@Column (name = "flag")
	private String flag;
	

	@Transient
	transient private String protocol1;
	
	@Transient
	transient private String protocol2;
	
	@Transient
	transient private String ip1;
	
	@Transient
	transient private String ip2;
	
	@Transient
	transient private String port1;
	
	@Transient 
	transient private String port2;
	
	@Transient 
	transient private String domain_name;
	
	@Transient 
	transient private String url1;


	


}
