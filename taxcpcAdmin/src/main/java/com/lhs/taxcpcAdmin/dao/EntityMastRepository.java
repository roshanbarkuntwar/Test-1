package com.lhs.taxcpcAdmin.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;

@Repository
public interface EntityMastRepository extends JpaRepository<EntityMast, String>,GenericCustomRepository<Long,EntityMast> {

	
	@Query(value = "select t.entity_name || ' (' || t.entity_code || ')' from entity_mast t  where t.entity_code=:entity_code", nativeQuery = true)
	String getEntityName(String entity_code);

	

}
 