package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lhs.taxcpcAdmin.model.entity.ViewOnsiteReqIssueType;


	



public interface ViewIssueType  extends JpaRepository<ViewOnsiteReqIssueType, String>{
	
	@Query("select a.issue_type_name from ViewOnsiteReqIssueType a where a.issue_type_code = :code")
	String getCode(String code);

}
