package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "req_tran_attach")
@IdClass(ReqTranAttachId.class)
public class ReqTranAttach implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "slno", length = 20, nullable = false)
	private int slno;

	@Id
	@Column(name = "req_code", length = 50, nullable = false)
	private String req_code;

	@Column(name = "req_attach")
	private byte[] req_attach;

	@Column(name = "req_attach_name")
	private String req_attach_name;

	@Column(name = "last_update")
	private Date last_update;

	@Column(name = "flag")
	private String flag;

	@Transient
	private String base64;

	public String getBase64() {
		return this.base64 = DatatypeConverter.printBase64Binary(this.req_attach);
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}
