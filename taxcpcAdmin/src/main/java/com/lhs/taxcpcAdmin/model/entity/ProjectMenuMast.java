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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_menu_mast")
public class ProjectMenuMast implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "PROJECT_MENU_MAST_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "menu_code" ,length=20,nullable = false)
	private String menu_code;
	
	@Column(name = "menu_name" ,length=100)
	private String menu_name;
	
	@Column(name = "menu_description" ,length=50)
	private String menu_description;
	
	
	@Column(name = "module_code" ,length=20,nullable = false)
	private String module_code;
	
	@Column(name = "project_code" ,length=20,nullable = false)
	private String project_code;
	
	@Column(name = "parent_menu_code" ,length=50)
	private String parent_menu_code;

	@Column(name = "sub_menu_type" ,length=50)
	private String sub_menu_type;
	
	@Column(name = "menu_type" ,length=200)
	private String menu_type;
	
	@Column(name = "menu_status" ,length=1)
	private String menu_status;
	
	
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	@Column(name = "flag" ,length=1)
	private String flag;
}
