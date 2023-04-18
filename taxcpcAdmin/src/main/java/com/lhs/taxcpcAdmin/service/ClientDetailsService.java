package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectMenuMast;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;

@Service
public interface ClientDetailsService {
	
	public Map<String, String> getAllStates();

	public List<ClientDetails> getClientDetailList();

	public ClientDetails getClientDetailsFromEntityCode(String entity_code);

	
	String deleteClientDetail(String entity_code);

	ClientDetails getClientDetailsDataonEntryType(String entity_code);

	public Map<String, String> getEntityName();

	public List<ClientDetails> getSearchClientDetail(String clientName);

	List<ClientDetails> getSearchClientDetail1(String clientName);

	public Map<String, String> getAllCodeName();

	public String saveClientDetail(ClientDetails entity);

	public Long getClientDetailsCount(FilterDTO filter);

	public List<ClientDetails> getClientBrowseList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage);

	public String saveClientDatabase(ClientDetails clientDetail);

	public String viewClientDetails(String entity_code);

	ClientAppDetails getClientAppDetailsType(String client_code);

	public List<ClientAppDetails> getClientAppList();

	public List<ClientDetails> getClientList();

	public List<ClientAppDetails> getEditClientAppList(String client_code);

    public String getClientAppTypeList(String client_code);


	public List<ClientAppDetails> getAppClientEntry(ClientAppDetails entity, String client_code);

	public int getcount(String clientName);
	

	public int getcountText(String clientName);

	public List<ClientDetails> getSearchClientDetailText(String clientName);

	public List<ClientAppDetails> getAllList(String client_code);

	public String editClientAppWithNewValue(String value3, String remark, String client_code);

	

}
