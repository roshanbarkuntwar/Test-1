package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
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
//import org.omg.CORBA.TRANSIENT;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doc_tran")
public class DocTran implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "DOC_TRAN_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "doc_code" ,length=20,nullable = false)
	private String doc_code;
	@Column(name = "doc_type_code" ,length=50)
	private String doc_type_code;
    @Column(name = "doc_name" ,length=100)
	private String doc_name;
	@Column(name = "google_drive_flag" ,length=1)
	private String google_drive_flag;
	@Column(name = "local_drive_flag" ,length=1)
	private String local_drive_flag;
	@Column(name = "database_flag" ,length=1)
	private String database_flag;
	@Column(name = "local_drive_path" ,length=200)
	private String local_drive_path;
	@Column(name = "google_drive_path" ,length=200)
	private String google_drive_path;
	@Column(name = "remark" ,length=4000)
	private String remark;
	@Column(name = "doc_mode" ,length=1)
	private String doc_mode;
	@Column(name = "doc_data" ,length=2000)
	private String doc_data;
	@Column(name = "refer_link" ,length=50)
	private String refer_link;
	@Column(name = "entry_date" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date entry_date;
	@Column(name = "user_code" ,length=20)
	private String user_code;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	@Column(name = "project_code" ,length=20)
	private String project_code;
	
	
	@Transient
	transient private MultipartFile doc_file;

}
