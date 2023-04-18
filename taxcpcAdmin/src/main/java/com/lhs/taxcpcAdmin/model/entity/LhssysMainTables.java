package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
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
@Table(name = "lhs_taxcpc_main_tables")
public class LhssysMainTables  implements Serializable {
	
	private static final long serialVersionUID = 1L;


	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "LHSSYS_MAIN_TABLES_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "object_code", length = 10, nullable = false)
	private String object_code;
	
	@Column(name = "table_or_view_name", length = 100, nullable = false)
	private String table_or_view_name;
	
	@Column(name = "object_type", length = 100 , nullable = false)
	private String object_type;
	
	@Column(name = "project_code", length = 100 , nullable = false)
	private String project_code;
	
	@Column(name = "remark", length = 1000, nullable = false)
	private String remark;
	
    @Column(name = "lastupdate")
	private Date lastupdate;

}
