package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.ReqTranAttach;

public interface RequisitionService {

	String addNewRequisition(ReqTran reqEntity);

	List<ReqTran> getReqEntryListByUser(String user_code);

	List<ReqTran> getReqApprovalListByUser(String user_code);

	List<ReqTran> getReqListForApproval(String role_code, String dept_str);

	Map<String, String> getReqErrorGroup();

	List<String> getSampleDataFilter();

	String assignedRequisition(String req_code, String assigned_to, String approved_by, String approved_by_remark);

	List<ReqTran> getAssignedRequisition(String user_code, String role_code);

	List<String> getViewSampleDataFilter(String reqType);

	String startRequisition(String req_code, String role_name , String reopened, String user_code);
	
	String closeReAssignedReq(String req_code, String remark,String user_type_role,String client_final_status);
	
	String closeReqByClient(String req_code, String remark,String user_type_role,String ReqClosed,String user_code);

	String closeAssignedReq(String user_code, String req_code, String qcType, String assignedToQc, String devRemark,
			String deployType, String user_role, String deployTo,  String user_type_role, String assignedDev, String Reopened);

	String saveDeployment(LhssysTaxcpcDeploymentTran entity);

	String viewReqDashboard(String reqCode,String role_type_code, String sub_menu_code);

	List<ReqTran> getSearchData(String req_code, String from_date, String to_date, String project_name,
			String issue_type, String current_req_status,String req_type,String req_priority);
	
	List<ReqTran> getSearchDataForClient(String req_code, String from_date, String to_date, String project_name,
			String issue_type, String current_req_status,String req_type,String req_priority);

	List<ReqTran> getReqEntryList();

	List<ReqTran> getReqListForApproval1(String role_code, String dept_str);

	List<ReqTran> getAssignedRequisition1(FilterDTO filter,String user_code, String role_code);

	List<ReqTran> getReqApprovalDataFilter(String req_code,
			String from_date, String to_date, String project_name, String issue_type, String req_type, String req_priority);

	List<ReqTran> getReqAssignedDataFilter(String req_code,String project_name,String req_type,String req_priority,String menu_name,String from_date,String to_date,String dev_status, String role_code, String user_code);


	List<ReqTran> getReqEntryDashboardList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, List<ReqTran> reqList);
	
	List<ReqTran> getReqEntryDashboardListForClient(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, List<ReqTran> reqList);

	List<ReqTran> getReqApprovalBrowseList(HttpSession session, FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage,List<ReqTran> reqList);

	List<ReqTran> getReqAssignedBrowseList(HttpSession session, FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage);

	long getUserCount(FilterDTO filter, String type);
	
	long getUserCountForCount(FilterDTO filter, String type);
	
	long getCountOfAssignedReq(FilterDTO filter,String user_code, String role_Code);
	
	long getCountOfApprovedReq(FilterDTO filter,String user_code);

	String rejectRequisition(String req_code, String user_code);

	String deleteDocument(String seq_id);
	
	String viewDeploymentDetails(Long seq_id);
	
	List<LhssysTaxcpcDeploymentTran> getDeploymentDataList();

	List<LhssysTaxcpcDeploymentTran> getDeploymentDataListFilter(String project_name, String depl_code,String war_file);

	long getDeplCount(FilterDTO filter);

	List<LhssysTaxcpcDeploymentTran> getDeplDashboardList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage);

	List<LhssysTaxcpcDeploymentTran> getDeplSearchData(String project_name, String depl_code, String war_file);

	public List<String> getFileNames();
	
    String viewLovDescription(String description);

	Map<String, String> getCurrent_status_list();

	ReqTran saveOnsiteReqDetail(ReqTran entity);

	void savelogo(ReqTranAttach reqTranAttachEntity);

	Map<String, String> getAllReqIssueTypeCodeName();

	List<ReqTran> getOnsiteReq();

	long getOnsiteDetailsCount(FilterDTO filter);

	List<ReqTran> getOnsiteList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, long total, Map<String, String> viewList);

	String viewOnsiteReq(String req_code);

	String ClosedOnsiteReq(String req_code);

	

	//List<ReqTran> getOnsiteListFilter(String req_type,  String deploy_delivered_date);

	String ClosedOnsiteReq(ReqTran entity1, String req_code, String remark);


	String StartWorkReq(ReqTran entity1, String req_code);

	Map<String, String> getViewListIssue();

	

	Map<String, String> getstatusList();

	int getcountOnsiteTable(String req_type, String req_code,  String current_req_status,
			String from_date, String to_date,String from_date_new, String to_date_new,FilterDTO filter);

	List<ReqTran> getOnsiteListFilter(String req_type, String req_code,  String current_req_status,
			String from_date, String to_date,String from_date_new, String to_date_new,FilterDTO filter);


}
