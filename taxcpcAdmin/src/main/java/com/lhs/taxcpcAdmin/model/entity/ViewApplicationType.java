package com.lhs.taxcpcAdmin.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "view_application_type")
public class ViewApplicationType {
	
	@Id
	@Column(name = "application_type_code", length = 50)
	private String application_type_code;
	
	@Column(name = "application_type_name", length = 50)
	private String application_type_name;

	public String getApplication_type_code() {
		return application_type_code;
	}

	public void setApplication_type_code(String application_type_code) {
		this.application_type_code = application_type_code;
	}

	public String getApplication_type_name() {
		return application_type_name;
	}

	public void setApplication_type_name(String application_type_name) {
		this.application_type_name = application_type_name;
	}

	public ViewApplicationType() {
	}

	@Override
	public String toString() {
		return "ViewApplicationType [application_type_code=" + application_type_code + ", application_type_name="
				+ application_type_name + "]";
	}
}
