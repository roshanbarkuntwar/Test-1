package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;


	@Repository
	public interface ClientAppDetailsRepository
	extends JpaRepository<ClientAppDetails, String>,GenericCustomRepository<String, ClientAppDetails>{
		
		
		@Query(" select a.application_type from ClientAppDetails a where a.client_code = :client_code")
		public String getApplicationType(String client_code);
		
		@Query(" select a.app_name from ClientAppDetails a where a.client_code = :client_code")
		public String getApplicationName(String client_code);
		
		@Query(" select a.app_url from ClientAppDetails a where a.client_code = :client_code")
		public String getApplicationUrl(String client_code);
		
		@Query(" select a.connected_db_username from ClientAppDetails a where a.client_code = :client_code")
		public String getConnected_db_username(String client_code);
		
		@Query(" select a.connected_db_pwd from ClientAppDetails a where a.client_code = :client_code")
		public String getConnected_db_pwd(String client_code);
		
		@Query(" select a.connected_db_sid from ClientAppDetails a where a.client_code = :client_code")
		public String getConnected_db_sid(String client_code);
		
		@Query(" select a.connected_port from ClientAppDetails a where a.client_code = :client_code")
		public String getConnected_port(String client_code);
		
		@Query(" select a.connected_db_remark from ClientAppDetails a where a.client_code = :client_code")
		public String getConnected_db_remark(String client_code);

		@Query(value="select a.application_type from Lhs_Taxcpc_Client_App_Details a where a.client_code = :client_code",nativeQuery = true)
		public List<ClientAppDetails> getEditapplication(String client_code);

		@Query(value="select * from Lhs_Taxcpc_Client_App_Details a where a.client_code = :client_code",nativeQuery = true)
		public List<ClientAppDetails> getApplicationDetils(String client_code);


}
