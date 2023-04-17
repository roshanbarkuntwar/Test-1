package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "bank_audit_attach")
public class BankAuditAttach implements java.io.Serializable  {
	
	//private static final long serialVersionUID = 4695058405143038093L;



private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "audit_mast_seq_id" ,nullable = false)
	private Integer audit_mast_seq_id;

	@Column(name = "audit_doc_attach", length = 20, nullable = true)
	private byte[] audit_doc_attach;
	
	@Column(name = "doc_attach_name")
	private String doc_attach_name;
	
	@Column(name = "audit_doc_resolve_attach", length =4000, nullable = true)
	private byte[] audit_doc_resolve_attach;
	
	@Column(name = "doc_resolve_attach_name")
	private String doc_resolve_attach_name;
	
	
	@Column(name = "lastupdate" )
//	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	
	@Column(name = "flag", length = 1, nullable = true)
	private String flag;


	

}



