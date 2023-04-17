package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;
import com.lhs.taxcpcAdmin.model.entity.LhssysMainTables;

@Repository
public interface LhssysMainTablesRepository extends JpaRepository<LhssysMainTables, String>,GenericCustomRepository<String, LhssysMainTables>{

		
	@Query(" from LhssysMainTables a where a.object_code = :object_code")
	public LhssysMainTables getLhssysMainTableCode(String object_code);

}
