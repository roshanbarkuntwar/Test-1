package com.lhs.taxcpcAdmin.dao;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lhs.taxcpcAdmin.model.entity.EntityLogoMast;


public interface EntityLogoMastRepository extends JpaRepository<EntityLogoMast, String>{

	@Query(value="select logo_name from entity_logo_mast where entity_code=:entity_code" ,nativeQuery=true)
	String getfilename(String entity_code);
	
	@Query("from EntityLogoMast t where t.entity_code =:entity_code")
	EntityLogoMast getentitybycode(String entity_code);
	
	@Query(value="select logo_name,logo from entity_logo_mast where entity_code=:entity_code" ,nativeQuery=true)
	Map<String, byte[]> getfileName(String entity_code);

	
}
