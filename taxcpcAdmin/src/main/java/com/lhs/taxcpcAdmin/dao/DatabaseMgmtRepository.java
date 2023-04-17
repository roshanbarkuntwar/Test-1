package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbDetails;

@Repository
public interface DatabaseMgmtRepository extends JpaRepository<LhssysDbDetails, String>,GenericCustomRepository<String, LhssysDbDetails>{

	@Query(" from LhssysDbDetails a where a.db_code = :db_code")
	public LhssysDbDetails getLhssysDbDetailDbCode(String db_code);

}
