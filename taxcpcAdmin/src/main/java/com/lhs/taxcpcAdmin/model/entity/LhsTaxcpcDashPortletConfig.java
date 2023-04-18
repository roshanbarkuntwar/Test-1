package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LHS_TAXCPC_DASH_PORTLET_CONFIG")
@IdClass(LhsTaxcpcDashPortletConfigID.class)
public class LhsTaxcpcDashPortletConfig implements Serializable {
	
	@Id
	@Column(name = "PORTLET_SEQ_NO", length = 8, nullable = false)
	private Long PORTLET_SEQ_NO;
	@Id
	@Column(name = "PORTLET_TYPE", length = 10, nullable = false)
	private String PORTLET_TYPE;
	@Column(name = "PORTLET_HEADER", length = 100, nullable = true)
	private String PORTLET_HEADER;
	@Column(name = "PORTLET_VALUE", length = 500, nullable = true)
	private String PORTLET_VALUE;
	@Column(name = "STATUS", length = 1, nullable = true)
	private String STATUS;
	@Column(name = "FLAG", length = 1, nullable = true)
	private String FLAG;
	@Column(name = "PORTLET_DESIGN", length = 500, nullable = true)
	private String PORTLET_DESIGN;
	@Column(name = "USER_CODE", length = 8, nullable = true)
	private String USER_CODE;
	@Column(name = "LASTUPDATE",  nullable = true)
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date LASTUPDATE;
	@Column(name = "DATA_REFRESH_TIMESTAMP", nullable = true)
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date DATA_REFRESH_TIMESTAMP;
	
	
	
	
}
