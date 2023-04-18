package com.lhs.taxcpcAdmin.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;

@Repository
public interface ClientDetailsRepository 
extends JpaRepository<ClientDetails, String>,GenericCustomRepository<String, ClientAppDetails>
{

	void save(ClientAppDetails entity);

	@Query(value= "select a.state_name from State_Mast a where a.state_code = :branch_state_code", nativeQuery = true)
	public String getBranchName(String branch_state_code);
	
	
	@Query(value= "select count(*) from lhs_taxcpc_client_details t where 1=1  and lower (entity_name) || lower (team_name) || lower (resp_person_name) || lower (branch_city) || (resp_person_mobileno) like lower ('%:client_name%')", nativeQuery = true)
	public int getcount(String client_name);

	
	

}
