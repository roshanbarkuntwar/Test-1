package com.lhs.taxcpcAdmin.model.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lhs_taxcpc_db_objects")
public class LhssysDbObjects  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "db_object_name", length = 128)
	private String db_object_name;
	
	@Column(name = "db_password", length = 20 , nullable = false)
	private String db_password;
	
	@Column(name = "type_of_database", length = 128)
	private String type_of_database;
	
	@Column(name = "db_objects_use", length = 1000)
	private String db_objects_use;
	
	@Column(name = "object_type", length = 23)
	private String object_type;
	
	@Column(name = "remark", length = 4000)
	private String remark;
	
	@Column(name = "status", length = 1000)
	private String status;
	
    @Column(name = "last_ddl_time")
	private Date last_ddl_time;
	
}
