package com.lhs.taxcpcAdmin.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "view_onsite_req_issue_type")
public class ViewOnsiteReqIssueType {
	
	@Id
	@Column(name = "issue_type_code", nullable = true)
	private String issue_type_code;
	@Column(name = "issue_type_name", nullable = true)
	private String issue_type_name;
	@Column(name = "flag", nullable = true)
	private String flag;


}
