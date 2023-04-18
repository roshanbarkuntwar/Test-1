package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lhs.taxcpcAdmin.model.entity.ViewDocType;

public interface ViewDocCodeRepository extends JpaRepository<ViewDocType, String>{

	@Query("select a.doc_type_name from ViewDocType a where a.doc_type_code = :code")
	String getCode(String code);

}
