package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lhssys_parameters")
public class LhssysParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8123336669525353242L;

	@Id
	private Integer srno;
	@Column(name = "parameter_name", length = 30, nullable = false)
	private String parameter_name;
	@Column(name = "parameter_value", length = 200, nullable = true)
	private String parameter_value;
	@Column(name = "entity_code", length = 2, nullable = true)
	private String entity_code;
	@Column(name = "session_flag", length = 1, nullable = true)
	private String session_flag;
	@Column(name = "remark", length = 200, nullable = true)
	private String remark;

}
