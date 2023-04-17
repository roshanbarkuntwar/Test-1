package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.LhssysDefaultEntityClient;

@Repository
public interface LhssysDefaultEntityClientRepository extends JpaRepository<LhssysDefaultEntityClient, String>,GenericCustomRepository<String,LhssysDefaultEntityClient>{

	
	@Query(" from LhssysDefaultEntityClient a where a.client_code = :client_code")
	LhssysDefaultEntityClient getClientDefaultList(String client_code);
}
