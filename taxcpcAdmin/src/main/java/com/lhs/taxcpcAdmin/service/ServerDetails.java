package com.lhs.taxcpcAdmin.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.AppDetails;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.ServerDetailModel;

public interface ServerDetails {

	public Map<String, String> getServerTypeList();

	public String addPhysicalServerEntry(ServerDetailModel serverDetailModel);

	public String addAppServerEntry(ServerDetailModel serverDetailModel, String server_id2);

	public String addAppDetailEntry(ServerDetailModel serverDetailModel, String server_id);

	public String addAppNameEntry(ServerDetailModel serverDetailModel, String serverId, String appName);

	public List<ServerDetailModel> getServerList();

	public List<ServerDetailModel> getServerListByServerType(String server_type);

	public List<ServerDetailModel> getServerListByServerId(String server_id, String server_type_code);

	public long getUserCount(FilterDTO filter);
	
	public long getUserCount(FilterDTO filter,String server_type);
	
	public List<ServerDetailModel> getServerBrowseList(FilterDTO filter,String server_type,HttpSession session, DataGridDTO dataGridDTO,
			String recPerPage);
					
	public List<ServerDetailModel> getServerBrowseList1(HttpSession session, DataGridDTO dataGridDTO,
			String recPerPage, String server_type);

	public String deleteServerById(String server_id, String server_type_code);

	public ServerDetailModel getServerById(String server_id);

	public List<String> getAppNameListById(String server_id);

	public Map<String, String> getServerIpList();

	public Map<String, String> getAppServerNameList();

	public Map<String, String> getDrivelist();

	public String getServerIDByServerIP(String serverIp);

	public String validateServerName(String configuration_type, String app_server_ip, String app_server_name);

	public String setPublicIp(String app_server_ip);

	public String saveAppNameEntry(AppDetails serverDetailModel);

	public String updatePhysicalServerEntry(ServerDetailModel serverDetailModel, String server_id);

	public List<AppDetails> getAppNameDetails(String server_id);

	public List<String> getListOfId(String serverId);

	public String validateAppName(String server_id);

	public String viewServerDetails(String server_id);

	public String getDeleteCount(String server_id);

	public String checkServerIp(String server_id);

	public String getDeleteAppCount(String server_id);

	public Map<String, String> getAppServerIpList(String serverip);

	public Map<String, String> getAppServerName(String parent_server);

	public Map<String,String> getAppNameList(String app_servername);

	public AppDetails saveAppDetail(AppDetails entity);

	public long getAppCount(FilterDTO filter);

	public List<AppDetails> getAppList();

	public List<AppDetails> getAppDetailsList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, long total);

	public AppDetails  getAppEditList(Integer app_code);


	public String viewAppDetails(Integer app_code);

	public int getcountTable(String entity_code, String application_type,String project_code, String search_entity );

	public List<AppDetails> getAppDataListFilterTable(String entity_code, String application_type,String project_code, String search_entity);

	public String deleteAppDocument(Integer app_code);

	public Map<String, String> setAppServerIP(String server_ip);

	public String getServerIDByServerIPandAppServerIP(String server_ip, String app_server_ip);

	public Map<String, String> setAppServer_IP(String server_ip);

	public Map<String, String> getServerIp_List();

	public List<String> getAppServerip(String app_server_ip);

	public List<String> getListOfId1(String server_id);

	public String getConfigTypeById(String string);

	Map<String, String> getServerIpListByConfigType(String configType);

	public Map<String, String> getAppServerIpList1();

	public Map<String, String> getServerIpList1();

	public List<ServerDetailModel> getServerFilterData(String configuration_type, String server_owner_name, String server_ip, String tag_name,
			String entity_code);

	public int getFilterCount(String configuration_type , String server_owner_name, String server_ip, String tag_name, String entity_code);

	public List<ServerDetailModel> getServerFilterDataGrid(String configuration_type, String server_owner_name,
			String server_ip, String tag_name, String entity_code, HttpSession session, DataGridDTO dataGridDTO,
			String recPerPage);



	

}
