package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lhs_taxcpc_client_details")
public class ClientDetails implements java.io.Serializable  {
	
	private static final long serialVersionUID = 4695058405143038093L;
	
	@Column(name = "entity_code", length = 2, nullable = false)
	private String entity_code;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "CLIENT_DETAILS_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "client_code", length = 100, nullable = false)
	private String client_code;
	
	@Column(name = "entity_name", length = 50, nullable = true)
	private String entity_name;
	
	@Column(name = "team_name", length = 50, nullable = true)
	private String team_name;
	
	@Column(name = "resp_person_name", length =100, nullable = true)
	private String resp_person_name;
	
	@Column(name = "resp_person_desig", length =100, nullable = true)
	private String resp_person_desig;
	
	@Column(name = "resp_person_mobileno", length =15, nullable = true)
	private String resp_person_mobileno;
	
	@Column(name = "resp_person_email_id", length = 250, nullable = true)
	private String resp_person_email_id;
	
	@Column(name = "resp_person_address", length =4000, nullable = true)
	private String resp_person_address;
	
	@Column(name = "resp_person_pin", length =6, nullable = true)
	private String resp_person_pin;
	
	@Column(name = "resp_person_city", length = 1000, nullable = true)
	private String resp_person_city;
	
	@Column(name = "resp_person_state_code", length = 2, nullable = true)
	private String resp_person_state_code;
	
	@Column(name = "branch_address", length =4000, nullable = true)
	private String branch_address;
	
	@Column(name = "branch_pin", length = 6, nullable = true)
	private String branch_pin;
	
	@Column(name = "branch_city", length = 1000, nullable = true)
	private String branch_city;
	
	@Column(name = "branch_state_code", length = 2, nullable = true)
	private String branch_state_code;
	
	@Column(name = "lastupdate" )
//	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	
	
	@Column(name = "flag", length = 1, nullable = true)
	private String flag;
	

}
