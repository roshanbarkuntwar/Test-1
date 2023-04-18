package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "view_entity_logo_mast")
public class ViewEntityLogoMast implements Serializable {
	/**
	 * @author sushma.manusmare
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "entity_code" ,length=20,nullable = false)
	private String entity_code;
	
	@Column(name = "logo")
	private byte[] logo;
	
	@Transient
	private String base64;

	public String getBase64() {
		return this.base64 = DatatypeConverter.printBase64Binary(this.logo);
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
	
	
	
	
	
}
