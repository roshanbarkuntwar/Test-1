package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "taxcpc_dictionary_dev_codehelp")
public class TaxcpcDictionaryDevCodeHelp implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "seq_id" ,nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer seq_id;
	@Column(name = "entry_type" ,length=2)
	private String entry_type;
	@Column(name = "project_code" ,length=20)
	private String project_code;
	@Column(name = "keyword_title_question" ,length=100)
	private String keyword_title_question;
	@Column(name = "description" ,length=500)
	private String description;
	@Column(name = "link" ,length=2000)
	private String link;
	@Column(name = "attachment" )
	private byte[] attachment;
	@Column(name = "user_code" ,length=20)
	private String user_code;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	@Column(name = "attachment_name" ,length=2000)
	private String attachment_name;
	
	/*
	 * @Transient private MultipartFile doc_file;
	 */
	@Transient
	transient private MultipartFile doc_file;

}
