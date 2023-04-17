package com.lhs.taxcpcAdmin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.ServerDetailModel;

@Repository
public interface ServerDetailsRepository extends JpaRepository<ServerDetailModel, String>, GenericCustomRepository<String,ServerDetailModel>{

	

	@Query(value="select count(*) from lhs_taxcpc_server_details ", nativeQuery=true)
	Long getCount();

	@Query(value="select count(*) from lhs_taxcpc_server_details where configuration_type=:server_type", nativeQuery=true)
	Long getCount(String server_type);
	
	@Query(value="select count(*) from lhs_taxcpc_server_details where configuration_type=:server_type ", nativeQuery=true)
	Long getCountApp(String server_type);
	
	@Query(value="select count(*) from lhs_taxcpc_server_details where configuration_type='PSC'", nativeQuery=true)
	Long getCountPhysical();
	
	@Query(value = "select t.server_id , t.server_ip from lhs_taxcpc_server_details t" , nativeQuery = true)
	Map<Long, String> getServerIp();

//	@Query(value = "select a.parent_server from(\r\n" + 
//			"select t.parent_server from LHS_TAXCPC_SERVER_DETAILS t WHERE t.server_type_code ='APS'  and t.app_server_ip is null \r\n" + 
//			"and t.server_ip =:serverIp\r\n" + 
//			"group by t.parent_server) a ", nativeQuery = true)
	
	@Query(value = "\r\n"
			+ "select a.server_id from(\r\n"
			+ "select t.server_id from LHS_TAXCPC_SERVER_DETAILS t WHERE  \r\n"
			+ " t.server_ip =:serverIp and t.configuration_type='PSC'\r\n"
			+ "group by t.server_id) a ", nativeQuery = true)
	String getServerId(String serverIp);

	@Query(value = " select t.server_id from lhs_taxcpc_server_details t where t.server_ip=:app_server_ip and  t.app_server_name=:app_server_name and t.configuration_type=:configuration_type", nativeQuery = true)
	String validateServerName(String configuration_type, String app_server_ip, String app_server_name);

	@Query(value = "select  max (server_id) from LHS_TAXCPC_SERVER_DETAILS", nativeQuery = true)
	String getMaxCount();

	
	@Query(value = "select t.public_ip from lhs_taxcpc_server_details t where t.server_ip=:server_ip  and t.public_ip is not null \r\n" + 
			"group by t.public_ip", nativeQuery = true)
	String setPublicIp(String server_ip);


	@Query(value = "select a.server_type_code from lhs_taxcpc_server_details a where a.parent_server=:server_id and rownum=1", nativeQuery = true)
	String getServerTypeCode(String server_id); 

	@Query(value = " select a.server_id from LHS_TAXCPC_SERVER_DETAILS a where a.parent_server=:server_id", nativeQuery = true)
	String getValidateAppName(String server_id);
	
	@Query(value = "select * from LHS_TAXCPC_SERVER_DETAILS t where t.server_id=:server_id ", nativeQuery = true)
	ServerDetailModel findByServerId(String server_id);


	@Query(value = "select t.server_id from LHS_TAXCPC_SERVER_DETAILS t WHERE  t.app_server_name is not null AND t.server_id =:parent_id", nativeQuery = true)
	List<String> getListOfID(String parent_id);

	@Query(value = " select count(*) from LHS_TAXCPC_SERVER_DETAILS t WHERE t.server_type_code ='APS' AND t.app_server_name is not null AND t.parent_server =:server_id", nativeQuery = true)
	String getDeleteCount(String server_id);

	@Query(value = "delete from lhs_taxcpc_server_details where server_id in\r\n" + 
			"                  (select a.server_id from lhs_taxcpc_server_details a\r\n" + 
			"                     where a.parent_server =:server_id OR a.server_id =:server_id OR PARENT_SERVER = NULL)" , nativeQuery = true)
	void deleteByServerId(String server_id);

	@Query(value = "select t.server_id from lhs_taxcpc_server_details t where t.server_ip=:server_id and rownum =1", nativeQuery = true)
	String checkServerIp(String server_id);

	@Query(value =" SELECT L.CONFIGURATION_TYPE FROM LHS_TAXCPC_SERVER_DETAILS L WHERE L.SERVER_ID=:type", nativeQuery = true )
	String getConfigTypeById(String type);
	
	@Query(value = "select a.server_id from LHS_TAXCPC_SERVER_DETAILS a where a.parent_server =:serverId", nativeQuery = true)
	List<String> getListOfId1(String serverId);
	
	

	
	


}
