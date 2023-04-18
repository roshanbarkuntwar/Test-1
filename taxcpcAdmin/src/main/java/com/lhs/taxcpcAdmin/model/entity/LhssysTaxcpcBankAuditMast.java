package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lhssys_taxcpc_bank_audit_mast")
public class LhssysTaxcpcBankAuditMast implements java.io.Serializable  {
	

  private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "seq_id" ,nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer seq_id;
	
	@Column(name = "entity_code", length = 2, nullable = true)
	private String entity_code;
	
	@Column(name = "audit_type", length = 20, nullable = true)
	private String audit_type;
	
	@Column(name = "audit_name", length = 50, nullable = true)
	private String audit_name;
	
	@Column(name = "audit_description", length =4000, nullable = true)
	private String audit_description;
	
	@Column(name = "audit_ext_link", length =500, nullable = true)
	private String audit_ext_link;
	
	@Column(name = "audit_applied_server_ip", length =50, nullable = true)
	private String audit_applied_server_ip;
	
	@Column(name = "audit_applied_region", length =50, nullable = true)
	private String audit_applied_region;
	
	@Column(name = "audit_applied_app_name", length =20, nullable = true)
	private String audit_applied_app_name;

	@Column(name = "other_info", length = 400, nullable = true)
	private String other_info;
	
	
	@Column(name = "other_info1", length = 4000, nullable = true)
	private String other_info1;
	
	@Column(name = "other_info2", length = 4000, nullable = true)
	private String other_info2;

	@Column(name = "audit_resolve_status", length = 5, nullable = true)
	private String audit_resolve_status;
	
	@Column(name = "audit_resolve_by", length =8, nullable = true)
	private String audit_resolve_by;
	
	@Column(name = "audit_resolve_remark", length = 4000, nullable = true)
	private String audit_resolve_remark;
	
	@Column(name = "audit_resolve_reference", length = 4000, nullable = true)
	private String audit_resolve_reference;
	
	@Column(name = "no_of_occurences", length = 100, nullable = true)
	private Integer no_of_occurences;
	
	@Column(name = "user_code", length = 8, nullable = true)
	private String user_code;
	
	@Column(name = "other_categaory", length = 255, nullable = true)
	private String other_categaory;
	
	
	@Column(name = "lastupdate" )
//	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	
	@Column(name = "flag", length = 1, nullable = true)
	private String flag;

	@Transient
	transient private MultipartFile doc_file;

	@Transient
	transient private MultipartFile file_attach;
	
	
	
}


