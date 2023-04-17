package com.lhs.taxcpcAdmin.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Immutable
@Table(name = "view_division_type")
public class ViewDivisionType {
	@Id
	@Column(name = "division_code", nullable = true)
	private String division_code;
	@Column(name = "division_name", nullable = true)
	private String division_name;	

}
