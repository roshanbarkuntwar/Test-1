package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "lhs_taxcpc_db_details")
public class LhssysDbDetails {
	
private static final long serialVersionUID = 1L;


	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "LHSSYS_DB_DETAIL_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "db_code", length = 100, nullable = false)
	private String db_code;
	
	@Column(name = "type_of_db", length = 100, nullable = false)
	private String type_of_db;
	
	@Column(name = "db_port", length = 100 , nullable = false)
	private String db_port;
	
	@Column(name = "db_sid", length = 100, nullable = false)
	private String db_sid;
	
	@Column(name = "db_ip", length = 100, nullable = false)
	private String db_ip;
	
	
	@Column(name = "remark", length = 4000, nullable = false)
	private String remark;
	
	@Column(name = "tag_name", length = 4000, nullable = false)
	private String tag_name;
	
    @Column(name = "lastupdate")
	private Date lastupdate;

}
