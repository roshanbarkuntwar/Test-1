package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.DocTranAttach;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcLinkMast;

public interface DocMgmtService {

	DocTran saveDocDetail(DocTran entity);

	Map<String, String> getDocTypeList();
	
	List<DocTran> getQuickDocList(String userCode);

	void saveDocAttachment(DocTranAttach docAttachEntity);

	public String deleteDocument(String doc_code);

	List<String> getImportantLinkTypeList(String user_type);

	String saveImpUrlDetail(LhssysTaxcpcLinkMast entity);

	List<LhssysTaxcpcLinkMast> getLinkDetailByType(String link_type);

	LhssysTaxcpcLinkMast getLinkDetailByCode(String linkCode);

	String deleteImpUrl(String linkCode);

	String viewLinkDetail(String linkCode);

	List<DocTran> getDocDetails(String user_code);

	public Long getDocDetailsCount(FilterDTO filter);

	public List<DocTran> getDocDetailsList(FilterDTO filter, DataGridDTO dataGridDTO, Map<String, String> docTypeList,
			
	Map<String, String> projectList, Map<String, String> userList, String recPerPage, long total);
	
	DocTran getDocDetailbydocCode(String doc_code);

	public String viewDocTran(String docCode);

	String getFilePath(String doc_code);
	
	List<DocTran> getDocDetailEntity();

	List<DocTranAttach> getDocDetailAttachList();


	String getDocAttachfile(String doc_code);

	DocTranAttach getdocCode(String doc_code);

	byte[] getfileData(String doc_code);

	int getcountTable(String doc_code_type, String doc_name, String user_code, String project_code);

	List<DocTran> getEntityListFilter(String doc_code_type, String doc_name, String user_code, String project_code);

	long getquickDocDetailsCount(FilterDTO filter,String userCode);

	List<DocTran> getQuickDocDetailsList(FilterDTO filter, DataGridDTO dataGridDTO, 
			Map<String, String> docTypeList, Map<String, String> menuList, String recPerPage, long total,String userCode);

	int getcountQuickTable(String doc_name);

	List<DocTran> getEntityListQuickFilter(String doc_name);




}
