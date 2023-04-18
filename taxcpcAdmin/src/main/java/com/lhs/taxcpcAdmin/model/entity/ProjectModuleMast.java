package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_module_mast")
public class ProjectModuleMast implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "PROJECT_MODULE_MAST_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "module_code" ,length=20,nullable = false)
	private String module_code;
	
	@Column(name = "module_name" ,length=100)
	private String module_name;
	
	@Column(name = "remark" ,length=2000)
	private String remark;
	
	/*
	 * @Column(name = "module_info" ,length=500) private String module_info;
	 */
	
	@Column(name = "project_code" ,length=20,nullable = false)
	private String project_code;
	
	
	  @Column(name = "module_status" ,length=1) 
	  private String module_status;
	 
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	@Column(name = "flag" ,length=1)
	private String flag;

}
