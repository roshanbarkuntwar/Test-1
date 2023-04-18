package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
*
* @author sushma.manusmare
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entity_logo_mast")
public class EntityLogoMast {
	@Id
	@Column(name = "entity_code" ,length=20,nullable = false)
	private String entity_code;
	
	@Lob
	@Column(name = "logo")
	private byte[] logo;
	
	@Column(name = "user_code" ,length=20,nullable = false)
	private String user_code;
	
	@Column(name = "logo_name" ,length=50,nullable = false)
	private String logo_name;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastupdate", nullable = false)
	private Date lastupdate;
	
	
	@Transient
	private String base64;

	public String getBase64() {
		return this.base64 = DatatypeConverter.printBase64Binary(this.logo);
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
	

}
