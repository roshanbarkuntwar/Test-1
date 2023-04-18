package com.lhs.taxcpcAdmin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.AppDetails;
import com.lhs.taxcpcAdmin.model.entity.ServerDetailModel;

public interface AppDetailsRepository  extends JpaRepository<AppDetails,Integer> , GenericCustomRepository<Integer,AppDetails>{

	@Query(value = "select *  from lhs_taxcpc_app_details t where server_id=:server_id", nativeQuery = true)
	List<AppDetails> getAppNameDetails(String server_id);

	@Query(value = "select a.server_id from LHS_TAXCPC_SERVER_DETAILS a where a.parent_server =:serverId", nativeQuery = true)
	List<String> getListOfId(String serverId);

	@Query(value = "select count(*) from lhs_taxcpc_app_details where server_id=:server_id", nativeQuery = true)
	String getDeleteAppCount(String server_id);

	@Query(value = "select t.app_code from lhs_taxcpc_app_details t where server_id=:server_id", nativeQuery = true)
	List<Integer> getAppCode(String server_id);

	@Query(value = "select * from lhs_taxcpc_app_details  where flag='A'", nativeQuery = true)
	List<AppDetails> findAllMethod();


	
//	@Query(value="select (select a.server_type_name\r\n" + 
//			"           from view_server_name a\r\n" + 
//			"          where a.server_type_code = v.app_server_name) server_type_name,\r\n" + 
//			"        (select a.server_type_code\r\n" + 
//			"           from view_server_name a\r\n" + 
//			"          where a.server_type_code = v.app_server_name) server_type_code\r\n" + 
//			"   from lhs_taxcpc_server_details v\r\n" + 
//			"  where v.app_server_name in\r\n" + 
//			"        (select t.app_server_name\r\n" + 
//			"           from lhs_taxcpc_server_details t\r\n" + 
//			"          where t.parent_server =:t.parent_server\r\n" + 
//			"          group by t.app_server_name)\r\n" + 
//			"    and v.parent_server is not null\r\n" + 
//			"  group by v.parent_server, v.app_server_name",nativeQuery = true )
//	Map<String, String> getAppNameList(String parent_server);

	

	

}
