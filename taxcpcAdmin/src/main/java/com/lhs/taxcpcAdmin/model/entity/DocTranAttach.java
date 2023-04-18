package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "doc_tran_attach")
public class DocTranAttach implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "DOC_TRAN_ATTACH_SEQ"))
	@GeneratedValue(generator = "generator")
	
	@Column(name = "doc_attach_code" ,length=20,nullable = false)
	private String doc_attach_code;
	
	@Column(name = "doc_attach" )
	private byte[] doc_attach;
	
	@Column(name = "doc_code" ,length=20,nullable = false)
	private String doc_code;
	
	@Column(name = "doc_attach_name" )
    private String doc_attach_name;
    
	
	
	@Transient
	private String base64;

	public String getBase64() {
		return this.base64 = DatatypeConverter.printBase64Binary(this.doc_attach);
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

}
