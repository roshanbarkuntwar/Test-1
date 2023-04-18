package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_mast")
public class ProjectMast implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "PROJECT_MAST_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "project_code" ,length=20,nullable = false)
	private String project_code;
	@Column(name = "project_name" ,length=50, unique = true)
	private String project_name;
	@Column(name = "project_info" ,length=500)
	private String project_info;
	@Column(name = "project_type" ,length=50)
	private String project_type;
	@Column(name = "application_type" ,length=500)
	private String application_type;
	@Column(name = "architecture_type_code" ,length=500)
	private String architecture_type_code;
	@Column(name = "frontend_type_code" ,length=500)
	private String frontend_type_code;
	@Column(name = "backend_type_code" ,length=500)
	private String backend_type_code;
	@Column(name = "database_type_code" ,length=500)
	private String database_type_code;
	@Column(name = "framework_type_code" ,length=1000)
	private String framework_type_code;
	@Column(name = "svn_link" ,length=500)
	private String svn_link;
	@Column(name = "war_filename" ,length=500)
	private String war_filename;
	@Column(name = "other_war_filename" ,length=500)
	private String other_war_filename;
	@Column(name = "domain_code" ,length=20)
	private String domain_code;
	@Column(name = "parent_code" ,length=2000)
	private String parent_code;
	@Column(name = "remark" ,length=2000)
	private String remark;
	@Column(name = "project_status" ,length=1)
	private String project_status;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	@Column(name = "connected_db_user" ,length=4000)
	private String connected_db_user;
	public void setCreatedby(String project_code) {
		// TODO Auto-generated method stub
		
	}
		
}
