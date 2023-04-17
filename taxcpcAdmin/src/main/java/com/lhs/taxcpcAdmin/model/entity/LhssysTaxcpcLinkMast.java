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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lhssys_taxcpc_link_mast")
public class LhssysTaxcpcLinkMast implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "LHSSYS_TAXCPC_LINK_MAST_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "link_code" ,length=20,nullable = false)
	private String link_code;
	@Column(name = "link_type" ,length=20)
	private String link_type;
	@Column(name = "link" ,length=200)
	private String link;
	@Column(name = "link_description" ,length=2000)
	private String link_description;
	@Column(name = "user_code" ,length=20)
	private String user_code;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	
}
