package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhs.javautilities.LhsDateUtility;
import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.LhssysTaxcpcDeploymentTranRepository;
import com.lhs.taxcpcAdmin.dao.LhssysTaxcpcDeploymentTranRepositorySupport;
import com.lhs.taxcpcAdmin.dao.ProjectMastRepository;
import com.lhs.taxcpcAdmin.dao.ProjectMenuMastRepository;
import com.lhs.taxcpcAdmin.dao.ProjectModuleMastRepository;
import com.lhs.taxcpcAdmin.dao.ReqTranAttachRepository;
import com.lhs.taxcpcAdmin.dao.ReqTranRepository;
import com.lhs.taxcpcAdmin.dao.ReqTranRepositorySupport;
import com.lhs.taxcpcAdmin.dao.UserMastRepository;
import com.lhs.taxcpcAdmin.dao.ViewIssueType;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;

import com.lhs.taxcpcAdmin.model.entity.BankAuditAttach;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;

import com.lhs.taxcpcAdmin.model.entity.DocTran;

import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.ReqTranAttach;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

@Service
public class RequisitionServiceImpl implements RequisitionService {

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	ReqTranRepository reqrepo;

	@Autowired
	ReqTranAttachRepository reqTranAttachRepo;

	@Autowired
	GlobalDoWorkExecuter executer;

	@Autowired
	UserRoleMastService userRoleMastService;

	@Autowired
	LhssysTaxcpcDeploymentTranRepository deploymentTranRepo;

	@Autowired
	LhsDateUtility lhsDateUtility;

	@Autowired
	ProjectMastRepository projectMastRepository;

	@Autowired
	ProjectModuleMastRepository moduleMastRepo;

	@Autowired
	UserMastRepository UserMastRepo;

	@Autowired
	ProjectMenuMastRepository projectMenuMastRepository;

	@Autowired
	ReqTranRepositorySupport reqTranRepositorySupport;

	@Autowired
	LhssysTaxcpcDeploymentTranRepository lhssysTaxcpcDeploymentTranRepository;

	@Autowired
	private LhssysTaxcpcDeploymentTranRepositorySupport lhssysTaxcpcDeploymentTranRepositorySupport;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private GlobalDoWorkExecuter execute;

	@Autowired
	private ViewIssueType viewRepo;

	@Override
	public String addNewRequisition(ReqTran reqEntity) {

		String response = "error";
		try {
			reqEntity.setReported_date(new Date());
			reqEntity.setLastupdate(new Date());
			reqrepo.save(reqEntity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("response.." + response);
		return response;
	}

	@Override
	public List<ReqTran> getReqEntryListByUser(String user_code) {
		List<ReqTran> reqList = new ArrayList<>();
		try {
//			reqList = reqrepo.findByReportedUser(user_code);
			reqList = reqrepo.findAll();
//			System.out.println("reqList "+reqList);
//					.stream()
//					.filter(p -> p.getReported_by().equalsIgnoreCase(user_code))
//					.sorted(Comparator.comparing(ReqTran::getReported_date).reversed())
//					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqList;
	}

	@Override
	public List<ReqTran> getReqApprovalListByUser(String user_code) {
		List<ReqTran> reqList = new ArrayList<>();
		try {
			reqList = reqrepo.findAll().stream()
//					.filter(p -> p.getReported_to().equalsIgnoreCase(user_code))
					.sorted(Comparator.comparing(ReqTran::getReported_date).reversed()).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqList;
	}

	@Override
	public List<ReqTran> getReqListForApproval(String role_code, String dept_str) {
		List<ReqTran> reqList = new ArrayList<>();
		System.out.println("role_code..." + role_code);
		try {
			if (!strUtl.isNull(role_code) && role_code.startsWith("MAN-")) {
				reqList = reqrepo.findByIssueType(dept_str);
				System.out.println("inside if condition...");
			} else {
				System.out.println("inside else condition...");
				reqList = reqrepo.findAllRequisition();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reqList;
	}

	@Override
	public Map<String, String> getReqErrorGroup() {
		Map<String, String> errorGroup = new HashMap<>();
		try {
			String queryStr = "select error_group, error_group_name from view_req_error_group";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				errorGroup.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (errorGroup != null && errorGroup.size() > 0) ? errorGroup : null;
	}

	@Override
	public List<String> getSampleDataFilter() {
		List<String> sampleDataFilter = new ArrayList<>();
		try {
			String queryStr = "select sample_data_filter_attr from view_sample_data_filter";
			List<Object[]> list = executer.executeSQLQueryAsList(queryStr);
			for (Object object : list) {
				sampleDataFilter.add((String) object);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sampleDataFilter;
	}

	@Override
	public String assignedRequisition(String req_code, String assigned_to, String approved_by,
			String approved_by_remark) {
		String result = "error";
		try {
			String queryStr = "update req_tran t \n" + " set t.approved_by = '" + approved_by + "',\n"
					+ "    t.approved_by_status = 'A',\n" + "    t.assigned_to_dev = '" + assigned_to + "',\n"
					+ "    t.approved_by_remark = '" + approved_by_remark + "',\n"
					+ "    t.assigned_dev_date = SYSDATE,\n" + "    t.lastupdate = SYSDATE, \n"
					+ "    t.current_req_status = 'CRS_001' \n" + " where t.req_code='" + req_code + "'";

//			System.out.println("queryStr>>"+queryStr);

			int update = executer.updateSQLQuery(queryStr);

			if (update > 0) {
				result = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<ReqTran> getAssignedRequisition(String user_code, String role_code) {
		List<ReqTran> reqList = new ArrayList<>();
		try {
			String role_name = userRoleMastService.getRoleNameByRoleCode(role_code);

			if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("QUALITY ANALYST")) {
				reqList = reqrepo.findByAssignedToQc(user_code);
			} else {
				reqList = reqrepo.findByAssignedToDev(user_code);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqList;
	}

	@Override
	public List<String> getViewSampleDataFilter(String reqType) {
		List<String> sampleList = new ArrayList<>();
//		System.out.println("reqType.." + reqType);
		try {
			String queryStr = "select sample_data_filter_attr from view_sample_data_filter WHERE FLAG IN ('B','"
					+ reqType + "')";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object obj : result) {
				sampleList.add((String) obj);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (sampleList != null && sampleList.size() > 0) ? sampleList : null;
	}

	@Override
	public String startRequisition(String req_code, String role_name, String reopened, String user_code) {
		String result = "error";
		try { 
			String updateStr = "";
			String current_status = "";
			if (role_name.equalsIgnoreCase("QUALITY ANALYST")) {
				updateStr = " t.testing_status = 'S',";
				current_status = "CRS_017";
			} else if (role_name.equalsIgnoreCase("CONSULTANT")) {

				current_status = "CRS_009";
				updateStr = " t.func_status = 'S',";
			} else {
				if (reopened.equals("RE")) { // RE = Reopend
					updateStr = " t.reopened_status = 'S',t.dev_status = 'S',";
					current_status = "CRS_012";
				} else if (reopened.equals("DF")) { // DF=deployFail
					updateStr = " t.dev_status = 'S',t.deploy_status='R',t.reopened_status = 'F',t.reinitiate_work_assigned_to='"
							+ user_code + "',";
					current_status = "CRS_010";
				} else if (reopened.equals("RI")) { // RI=Reinitiate
					updateStr = "t.reinitiate_pm_work_flag='S',t.REINITIATE_WORK_ASSIGNED_TO ='" + user_code + "',";
					current_status = "CRS_013";
				} else {
					updateStr = " t.dev_status = 'S',t.reopened_status = 'F',";
					current_status = "CRS_010";
				}
			}
			String queryStr = "update req_tran t \n" + " set " + updateStr.toString() + "\n"
					+ "    t.lastupdate = SYSDATE ,\n" + "    t.current_req_status = '" + current_status + "' \n"
					+ " where t.req_code='" + req_code + "'";

			int update = executer.updateSQLQuery(queryStr);

			if (update > 0) {
				result = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String closeReqByClient(String req_code, String remark, String user_type_role, String ReqClosed,
			String user_code) {
		String result = "error";
		try {

			StringBuilder updateStr = new StringBuilder();
			StringBuilder querySb = new StringBuilder();

			if (ReqClosed.equals("S")) {
				updateStr.append(" t.client_final_status = 'S',\n");
				updateStr.append(" t.final_status = 'S',\n");
				updateStr.append(" t.closure_remark = '" + remark + "',\n");
				updateStr.append(" t.closure_date = SYSDATE, \n");
				updateStr.append(" t.req_closed_by = '" + user_code + "',\n");
				updateStr.append(" t.current_req_status = 'CRS_006',\n");
				updateStr.append(" t.reinitiate_pm_work_flag = 'C',\n");

			} else {

				updateStr.append(" t.client_final_status = 'F',\n");
				updateStr.append(" t.final_status = 'F',\n");
				updateStr.append(" t.closure_remark = '" + remark + "',\n");
				updateStr.append(" t.closure_date = SYSDATE, \n");
				updateStr.append(" t.current_req_status = 'CRS_007',\n");

			}

			querySb.append("update req_tran t \n");
			querySb.append(" set ");
			querySb.append(updateStr.toString());
			querySb.append("  t.lastupdate = SYSDATE \n");
			querySb.append("  where t.req_code='" + req_code + "'");

			System.out.println("closeReqByClient...." + querySb.toString());

			int update = executer.updateSQLQuery(querySb.toString());

			if (update > 0) {
				result = "success";
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String closeReAssignedReq(String req_code, String remark, String user_type_role,
			String client_final_status) {
		String result = "error";
		try {
			System.out.println("remark..." + remark);
			StringBuilder updateStr = new StringBuilder();
			StringBuilder querySb = new StringBuilder();
			if (user_type_role.equals("DLPR")) {
				String dev_remark = "";
				dev_remark = reqrepo.getDevRemark(req_code);
				if (dev_remark == null || dev_remark.equalsIgnoreCase("null") || dev_remark.isEmpty()) {
					dev_remark = "";
				}
				String dev_Remark = dev_remark + remark + "###";
				System.out.println("dev_Remark.." + dev_Remark);
				updateStr.append(" t.dev_status = 'C',\n");
				updateStr.append(" t.dev_remark = '" + dev_Remark + "',\n");
				updateStr.append(" t.dev_close_date = SYSDATE, \n");
				updateStr.append(" t.REOPENED_STATUS = 'F',\n");
				updateStr.append(" t.current_req_status = 'CRS_005',\n");

			} else if (user_type_role.equals("PRHM")) {

				if (client_final_status == null) {
					String pm_work_remark = "DF-";
					pm_work_remark = reqrepo.getPmWorkRemark(req_code);
					System.out.println("pm_work_remark..." + pm_work_remark);
					if (pm_work_remark == null || pm_work_remark.equalsIgnoreCase("null") || pm_work_remark.isEmpty()) {
						pm_work_remark = "DF-";
					}
					String Pm_Remark = pm_work_remark + remark + "###";
					System.out.println("dev_Remark.." + Pm_Remark);
					updateStr.append(" t.dev_status = 'C',\n");
					updateStr.append(" t.final_status = 'S',");
					updateStr.append(" t.reinitiate_pm_work_flag = 'C',");
					updateStr.append(" t.deploy_status = 'S',\n");
					updateStr.append(" t.REINITIATE_PM_WORK_REMARK = '" + Pm_Remark + "',\n");
					updateStr.append(" t.current_req_status = 'CRS_003',\n");
				} else {
					String pm_work_remark = "RW-";
					pm_work_remark = reqrepo.getPmWorkRemark(req_code);
					System.out.println("pm_work_remark..." + pm_work_remark);
					if (pm_work_remark == null || pm_work_remark.equalsIgnoreCase("null") || pm_work_remark.isEmpty()) {
						pm_work_remark = "RW-";
					} else {
						pm_work_remark = pm_work_remark + "RW-";
					}
					String Pm_Remark = pm_work_remark + remark + "###";
					System.out.println("Pm_Remark..." + Pm_Remark);
					updateStr.append(" t.REINITIATE_PM_WORK_FLAG = 'C',\n");
					updateStr.append(" t.FINAL_STATUS = 'S',\n");
					updateStr.append(" t.REINITIATE_PM_WORK_REMARK = '" + Pm_Remark + "',\n");
					updateStr.append(" t.current_req_status = 'CRS_003',\n");
				}

			}
			querySb.append("update req_tran t \n");
			querySb.append(" set ");
			querySb.append(updateStr.toString());
			querySb.append("  t.lastupdate = SYSDATE \n");
			querySb.append("  where t.req_code='" + req_code + "'");

			System.out.println("closeReAssignedReq...." + querySb.toString());

			int update = executer.updateSQLQuery(querySb.toString());

			if (update > 0) {
				result = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String closeAssignedReq(String user_code, String req_code, String qcType, String assignedToQc,
			String devRemark, String deployType, String user_role, String deployTo, String user_type_role,
			String assignedDev, String Reopened) {
		String result = "error";
		int count = 1;
		String remark = "";
		String current_status = "";
		try {
			StringBuilder selfQcStr = new StringBuilder();
			StringBuilder updateStr = new StringBuilder();

			System.out.println("user_type_role.." + user_type_role);

			if (user_type_role.equalsIgnoreCase("DLPR")) {
				String dev_remark = "";
				dev_remark = reqrepo.getDevRemark(req_code);
				if (dev_remark == null || dev_remark.equalsIgnoreCase("null") || dev_remark.isEmpty()) {
					dev_remark = "";
				}
				String dev_Remark = dev_remark + devRemark + "###";
				System.out.println("dev_Remark.." + dev_Remark);
				updateStr.append(" t.dev_status = 'C',\n");
				updateStr.append(" t.dev_remark = '" + dev_Remark + "',\n");
				updateStr.append(" t.dev_close_date = SYSDATE, \n");
				updateStr.append(" t.assigned_to_qc = '" + assignedToQc + "',\n");
				updateStr.append(" t.assigned_qc_date = SYSDATE,\n");
				updateStr.append(" t.REOPENED_STATUS = 'F',\n");

				current_status = "CRS_002";

				if (qcType.equalsIgnoreCase("self")) {
					selfQcStr.append(" t.testing_status = 'C', t.testing_closed_date = SYSDATE, \n");
				} else {

				}
				if (deployType.equalsIgnoreCase("self")) {

					selfQcStr.append(" t.deploy_delivered_by = '" + user_code
							+ "', t.deploy_delivered_date = SYSDATE, t.deploy_type='S', t.deploy_status='C', t.final_status='S',\n");
					current_status = "CRS_003";

				}
				updateStr.append(" t.current_req_status = '" + current_status + "',\n");

			} else if (user_type_role.equalsIgnoreCase("TSTR")) {

				int count1 = reqrepo.getRemarkCount(req_code);
				System.out.println("count1.." + count1);
				String remark2 = "";
				remark2 = reqrepo.getReopenRemark(req_code);
				System.out.println("reopen remark.." + remark2);
				if (count1 >= 1) {
					count++;
				}
				if (remark2 == null || remark2 == "null" || !remark2.isEmpty()) {
					remark = "";
				}

				if (Reopened.equals("C")) {
					updateStr.append(" t.testing_remark = '" + devRemark + "',");

					selfQcStr.append(" t.testing_status = 'C', t.testing_closed_date = SYSDATE, \n");
					if (deployType.equalsIgnoreCase("self")) {

						selfQcStr.append(" t.deploy_delivered_by = '" + user_code
								+ "', t.deploy_delivered_date = SYSDATE, t.deploy_type='S',t.deploy_status='C', t.final_status='S',\n");

						updateStr.append(" t.current_req_status = 'CRS_003',\n");
					} else {
						selfQcStr.append("t.deploy_type='O', t.deploy_delivered_by = '" + deployTo + "', \n");
						selfQcStr.append("t.assigned_to_func='" + deployTo + "', t.assigned_func_date = sysdate, \n");
						updateStr.append(" t.current_req_status = 'CRS_014',\n");

					}

				} else {
					devRemark = remark + "RE" + count + "#" + devRemark + "###";
					System.out.println("devRemark..." + devRemark + "..COUNT..." + count);
					updateStr.append(" t.REOPENED_REMARK = '" + devRemark + "',");
					updateStr.append(" t.REOPENED_STATUS = 'T',");
					updateStr.append(" t.REMARK_COUNT = '" + count + "',");
					updateStr.append(" t.REOPENED_DEV_NAME = '" + assignedDev + "',");
					updateStr.append(" t.current_req_status = 'CRS_008',\n");

				}
			} else if (user_type_role.equalsIgnoreCase("FUNC")) {

				System.out.println("deployType.." + deployType);

				if (deployType.equals("S")) {
					updateStr.append(" t.func_remark = '" + devRemark + "',");
					updateStr.append(" t.final_status = 'S',");
					updateStr.append(" t.deploy_status = 'S',\n");
					updateStr.append(" t.func_status = 'C',\n");
					updateStr.append(" t.deploy_delivered_date = sysdate,\n");
					updateStr.append(" t.current_req_status = 'CRS_003',\n");

				} else {

					updateStr.append(" t.func_remark = '" + devRemark + "',");
					updateStr.append(" t.final_status = 'F',");
					updateStr.append(" t.dev_status = '',\n");
					updateStr.append(" t.deploy_status = 'F',\n");
					updateStr.append(" t.func_status = 'C',\n");
					updateStr.append(" t.deploy_fail_req_assigned_to = '10002',\n");
					updateStr.append(" t.current_req_status = 'CRS_016',\n");
				}

			} else if (user_type_role.equalsIgnoreCase("PRHM")) {

				updateStr.append(" t.dev_status = 'C',\n");
				updateStr.append(" t.final_status = 'S',");
				updateStr.append(" t.deploy_status = 'S',\n");
				updateStr.append(" t.dev_remark = '" + devRemark + "',\n");
				updateStr.append(" t.current_req_status = 'CRS_003',\n");
			}

			StringBuilder querySb = new StringBuilder();
			querySb.append("update req_tran t \n");
			querySb.append(" set ");
			querySb.append(updateStr.toString());
			querySb.append(selfQcStr.toString());
			querySb.append("  t.lastupdate = SYSDATE \n");
			querySb.append("  where t.req_code='" + req_code + "'");

			System.out.println("closeAssignedReq...." + querySb.toString());

			int update = executer.updateSQLQuery(querySb.toString());

			if (update > 0) {
				result = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String saveDeployment(LhssysTaxcpcDeploymentTran entity) {
		String response = "error";
		try {
			System.out.println("===" + entity.getServer_url());
			entity.setLastupdate(new Date());
			deploymentTranRepo.save(entity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public ReqTran getAllDataByReqCode(String reqCode) {
		ReqTran entity = new ReqTran();

		try {
			entity = reqrepo.findByReqCode(reqCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	public String getProjectName(String projectCode) {
		String projectName = "";
		try {
			projectName = projectMastRepository.getProjectName(projectCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectName;
	}

	public String getModuleName(String moduleCode) {
		String moduleName = "";
		try {
			moduleName = moduleMastRepo.getModuleName(moduleCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return moduleName;
	}

	public String getMenuName(String menuCode) {
		String menuName = "";
		try {
			menuName = projectMenuMastRepository.getMenuName(menuCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuName;
	}

	public String getAssignedByName(String reported_by) {
		String assignedBy = "";
		try {
			assignedBy = UserMastRepo.getReportedBy(reported_by);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return assignedBy;
	}

	public String getUserName(String userCode) {
		String userName = "";
		try {
			userName = UserMastRepo.getUserName(userCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userName;
	}

	public String getCurrentStatus(String status_code) {
		String current_status = "";
		try {
			current_status = reqrepo.getCurrentStatus(status_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return current_status;
	}

	@Override
	public List<ReqTran> getSearchData(String req_code, String from_date, String to_date, String project_name,
			String issue_type, String current_req_status, String req_type, String req_priority) {
		List<ReqTran> getSearchDataList = new ArrayList<>();

		System.out.println("from_date=" + from_date);
		System.out.println("to_date=" + to_date);
		String Query = "";

		try {

			Query = "\r\n" + "select v.req_code,\r\n" + "  "
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "   " + "    v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n" + "     " + "  v.issue_type, v.reopened_status,\r\n" + "    "
					+ "   v.issue_description,\r\n" + "       v.req_priority,\r\n" + "       v.parent_req_code,\r\n"
					+ "       v.req_error_group_str,\r\n" + "   " + " v.sample_data_filter_str,\r\n"
					+ "      v.reinitiate_work_assigned_to,\r\n" + "    "
					+ " v.reinitiate_pm_work_flag, v.reinitiate_pm_work_remark,\r\n" + "    " + "   v.approved_by,\r\n"
					+ "       v.approved_by_status,\r\n" + "    " + "   v.approved_by_remark,\r\n"
					+ "       v.assigned_to_dev,\r\n" + "     " + "  v.assigned_dev_date,\r\n" + "    "
					+ "   v.dev_status,\r\n" + "       v.dev_remark,\r\n" + "     " + "  v.dev_close_date,\r\n" + "   "
					+ "    v.assigned_to_qc,\r\n" + "       v.assigned_qc_date,\r\n" + "     "
					+ "  v.testing_status,\r\n" + "     " + "  v.testing_remark,\r\n"
					+ "       v.testing_closed_date,\r\n" + "     " + "  v.deploy_delivered_by,\r\n"
					+ "       v.deploy_delivered_date,\r\n" + "   " + " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n" + " 	  v.func_remark,\r\n" + "    v.closure_remark,\r\n"
					+ "       v.closure_date,\r\n" + "     " + "  v.req_closed_by,\r\n" + " 	  v.req_entered_mode,"
					+ "     " + "  v.lastupdate,\r\n"
					+ "       v.flag, v.final_status,v.client_final_status,  v.reopened_dev_name, v.deploy_fail_req_assigned_to,\r\n"
					+ "    "
					+ "   (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status \r\n"
					+ "  from REQ_TRAN v  WHERE 1=1" + "  ";

			if (!req_code.isEmpty()) {
				Query = Query + "and v.req_code='" + req_code + "' ";
			}
			if (!project_name.isEmpty()) {
				Query = Query + " and  v.project_name='" + project_name + "'";
			}
			if (!from_date.isEmpty() && !to_date.isEmpty()) {
				Query = Query + " and v.reported_date BETWEEN to_date('" + from_date + "','DD-MM-RRRR') AND to_date('"
						+ to_date + "','DD-MM-RRRR')";
			}
			if (!from_date.isEmpty() && to_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + from_date + "','DD-MM-RRRR')";
			}
			if (!to_date.isEmpty() && from_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + to_date + "','DD-MM-RRRR')";
			}
//			if ((from_date != null) && to_date.isEmpty()) {
//				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + from_date + "','DD-MM-RRRR')";
//			}
//			if (!(to_date != null) && from_date.isEmpty()) {
//				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + to_date + "','DD-MM-RRRR')";
//			}
			if (!issue_type.isEmpty()) {
				Query = Query + " and  v.issue_type='" + issue_type + "'";
			}

			if (!current_req_status.isEmpty()) {
				Query = Query + " and  v.current_req_status='" + current_req_status + "'";
			}
			if (!req_type.isEmpty()) {
				Query = Query + " and  v.req_type='" + req_type + "'";
			}
			if (!req_priority.isEmpty()) {
				Query = Query + " and  v.req_priority='" + req_priority + "'";
			}

			Query = Query + "order by v.reported_date desc";

			System.out.println("getSearchData...." + Query);
			getSearchDataList = executer.executeSQLQuery(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSearchDataList;
	}

	@Override
	public List<ReqTran> getSearchDataForClient(String req_code, String from_date, String to_date, String project_name,
			String issue_type, String current_req_status, String req_type, String req_priority) {
		List<ReqTran> getSearchDataList = new ArrayList<>();

		System.out.println("from_date=" + from_date);
		System.out.println("to_date=" + to_date);
		String Query = "";

		try {

			Query = "\r\n" + "select v.req_code,\r\n" + "  "
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "   " + "    v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n" + "     " + "  v.issue_type, v.reopened_status,\r\n" + "    "
					+ "   v.issue_description,\r\n" + "       v.req_priority,\r\n" + "       v.parent_req_code,\r\n"
					+ "       v.req_error_group_str,\r\n" + "   " + " v.sample_data_filter_str,\r\n"
					+ "      v.reinitiate_work_assigned_to,\r\n" + "    "
					+ " v.reinitiate_pm_work_flag, v.reinitiate_pm_work_remark,\r\n" + "    " + "   v.approved_by,\r\n"
					+ "       v.approved_by_status,\r\n" + "    " + "   v.approved_by_remark,\r\n"
					+ "       v.assigned_to_dev,\r\n" + "     " + "  v.assigned_dev_date,\r\n" + "    "
					+ "   v.dev_status,\r\n" + "       v.dev_remark,\r\n" + "     " + "  v.dev_close_date,\r\n" + "   "
					+ "    v.assigned_to_qc,\r\n" + "       v.assigned_qc_date,\r\n" + "     "
					+ "  v.testing_status,\r\n" + "     " + "  v.testing_remark,\r\n"
					+ "       v.testing_closed_date,\r\n" + "     " + "  v.deploy_delivered_by,\r\n"
					+ "       v.deploy_delivered_date,\r\n" + "   " + " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n" + " 	  v.func_remark,\r\n" + "    v.closure_remark,\r\n"
					+ "       v.closure_date,\r\n" + "     " + "  v.req_closed_by,\r\n" + " 	  v.req_entered_mode,"
					+ "     " + "  v.lastupdate,\r\n"
					+ "       v.flag, v.final_status,v.client_final_status,  v.reopened_dev_name, v.deploy_fail_req_assigned_to,\r\n"
					+ "    "
					+ "   (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status\r\n"
					+ "  from REQ_TRAN v  WHERE 1=1 and v.req_entered_mode='E' " + "  ";

			if (!req_code.isEmpty()) {
				Query = Query + "and v.req_code='" + req_code + "' ";
			}
			if (!project_name.isEmpty()) {
				Query = Query + " and  v.project_name='" + project_name + "'";
			}
			if (!from_date.isEmpty() && !to_date.isEmpty()) {
				Query = Query + " and v.reported_date BETWEEN to_date('" + from_date + "','DD-MM-RRRR') AND to_date('"
						+ to_date + "','DD-MM-RRRR')";
			}
			if (!from_date.isEmpty() && to_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + from_date + "','DD-MM-RRRR')";
			}
			if (!to_date.isEmpty() && from_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + to_date + "','DD-MM-RRRR')";
			}
//			if ((from_date != null) && to_date.isEmpty()) {
//				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + from_date + "','DD-MM-RRRR')";
//			}
//			if (!(to_date != null) && from_date.isEmpty()) {
//				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + to_date + "','DD-MM-RRRR')";
//			}
			if (!issue_type.isEmpty()) {
				Query = Query + " and  v.issue_type='" + issue_type + "'";
			}
			if (!current_req_status.isEmpty()) {
				if (current_req_status.equalsIgnoreCase("Work In Progress")) {
					Query = Query+ " and ( v.current_req_status = 'CRS_010' or v.current_req_status='CRS_002' or v.current_req_status='CRS_017'or v.current_req_status='CRS_014')";
				} else {
//					Query = Query + " and  v.current_req_status='" + current_req_status + "'";
				}
			}
			if (!req_type.isEmpty()) {
				Query = Query + " and  v.req_type='" + req_type + "'";
			}
			if (!req_priority.isEmpty()) {
				Query = Query + " and  v.req_priority='" + req_priority + "'";
			}
			Query = Query + " order by v.reported_date desc";

			System.out.println("getSearchData.." + Query);
			getSearchDataList = executer.executeSQLQuery(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getSearchDataList;
	}

	public List<ReqTran> getReqEntryList() {
		List<ReqTran> reqList = new ArrayList<>();

		String Query = "";
		try {
			Query = "\r\n" + "select v.req_code,\r\n" + "  "
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "   " + "    v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n" + "     " + "  v.issue_type,\r\n" + "    "
					+ "   v.issue_description,\r\n" + "       v.req_priority,\r\n" + "       v.parent_req_code,\r\n"
					+ "       v.req_error_group_str,\r\n" + "   " + " v.sample_data_filter_str,\r\n"
					+ "      v.reinitiate_work_assigned_to,\r\n" + "    "
					+ " v.reinitiate_pm_work_flag, v.reinitiate_pm_work_remark,\r\n" + "    " + "   v.approved_by,\r\n"
					+ "       v.approved_by_status,\r\n" + "    " + "   v.approved_by_remark,\r\n"
					+ "       v.assigned_to_dev,\r\n" + "     " + "  v.assigned_dev_date,\r\n" + "    "
					+ "   v.dev_status,\r\n" + "       v.dev_remark,\r\n" + "     " + "  v.dev_close_date,\r\n" + "   "
					+ "    v.assigned_to_qc,\r\n" + "       v.assigned_qc_date,\r\n" + "     "
					+ "  v.testing_status,\r\n" + "     " + "  v.testing_remark,\r\n"
					+ "       v.testing_closed_date,\r\n" + "     " + "  v.deploy_delivered_by,\r\n"
					+ "       v.deploy_delivered_date,\r\n" + "   " + " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n" + " 	  v.func_remark,\r\n" + "    v.closure_remark,\r\n"
					+ "       v.closure_date,\r\n" + "     " + "  v.req_closed_by, v.reopened_status,\r\n"
					+ " 	  v.req_entered_mode," + "     " + "  v.lastupdate,\r\n"
					+ "       v.flag, v.final_status,v.client_final_status,  v.reopened_dev_name, v.deploy_fail_req_assigned_to,\r\n"
					+ "    "
					+ "   (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status \r\n"
					+ "  from REQ_TRAN v  order by v.reported_date desc\r\n" + "  ";

			System.out.println("query==  >>>>" + Query);

			reqList = executer.executeSQLQuery(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqList;
		// return reqList;
	}

	@Override
	public List<ReqTran> getReqListForApproval1(String role_code, String dept_str) {

		List<ReqTran> reqList = new ArrayList<>();
		String Query = "";
		Query = "\r\n" + "select v.req_code,\r\n" + "  "
				+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
				+ "   " + "    v.reported_date,\r\n"
				+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
				+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
				+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
				+ "       v.req_type,\r\n" + "     " + "  v.issue_type,\r\n" + "    " + "   v.issue_description,\r\n"
				+ "       v.req_priority,\r\n" + "       v.parent_req_code,\r\n" + "       v.req_error_group_str,\r\n"
				+ "   " + " v.sample_data_filter_str,\r\n" + "      v.reinitiate_work_assigned_to,\r\n" + "    "
				+ " v.reinitiate_pm_work_flag, v.reinitiate_pm_work_remark,\r\n" + "    " + "   v.approved_by,\r\n"
				+ "       v.approved_by_status,\r\n" + "    " + "   v.approved_by_remark,\r\n"
				+ "       v.assigned_to_dev,\r\n" + "     " + "  v.assigned_dev_date,\r\n" + "    "
				+ "   v.dev_status,\r\n" + "       v.dev_remark,\r\n" + "     " + "  v.dev_close_date,\r\n" + "   "
				+ "    v.assigned_to_qc,\r\n" + "       v.assigned_qc_date,\r\n" + "     " + "  v.testing_status,\r\n"
				+ "     " + "  v.testing_remark,\r\n" + "       v.testing_closed_date,\r\n" + "     "
				+ "  v.deploy_delivered_by,\r\n" + "       v.deploy_delivered_date,\r\n" + "   "
				+ " 	  v.assigned_to_func,\r\n" + " 	  v.assigned_func_date,\r\n" + " 	  v.func_remark,\r\n"
				+ "    v.closure_remark,\r\n" + "       v.closure_date,v.reopened_status,\r\n" + "     "
				+ "  v.req_closed_by,\r\n" + " 	  v.req_entered_mode," + "     " + "  v.lastupdate,\r\n"
				+ "       v.flag, v.final_status,v.client_final_status,  v.reopened_dev_name, v.deploy_fail_req_assigned_to,\r\n"
				+ "    "
				+ "   (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status \r\n"
				+ "  from REQ_TRAN v " + "  ";

		try {

			if (!strUtl.isNull(role_code) && (role_code.startsWith("MAN-") || role_code.startsWith("ADM-"))) {
//				reqList = reqrepo.findByIssueType(dept_str);
				System.out.println("inside if condition...");
				Query = Query
						+ " where   v.req_entered_mode != 'O' and  current_req_status <> 'CRS_004' and v.approved_by_status is null order by v.reported_date desc";
			} else if (!strUtl.isNull(role_code) && role_code.startsWith("DEV-")) {

			} else {
				System.out.println("inside else condition...");
//				reqList = reqrepo.findAllRequisition();
				Query = Query
						+ "where  v.req_entered_mode != 'O' and  v.approved_by_status is null order by v.reported_date desc";
			}
			System.out.println("Query..123 " + Query);
			reqList = executer.executeSQLQuery(Query);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reqList;
		// return reqList;

	}

	@Override
	public List<ReqTran> getAssignedRequisition1(FilterDTO filter, String user_code, String role_code) {
		List<ReqTran> reqList = new ArrayList<>();
		String Query = "";
		StringBuilder sb = new StringBuilder();
		Query = "\r\n" + "select v.req_code,\r\n" + "  "
				+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
				+ "    v.reported_date,\r\n"
				+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
				+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
				+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
				+ "       v.req_type,\r\n" + "     " + "  v.issue_type,\r\n" + "    " + "   v.issue_description,\r\n"
				+ "       v.req_priority,\r\n" + "       v.parent_req_code,\r\n" + "       v.req_error_group_str,\r\n"
				+ "   " + "    v.sample_data_filter_str,\r\n" + "       v.REINITIATE_PM_WORK_FLAG,\r\n" + "    "
				+ "   v.REINITIATE_PM_WORK_REMARK, v.REINITIATE_WORK_ASSIGNED_TO,\r\n" + "    "
				+ "(select n.user_name  from user_mast n where n.user_code = v.approved_by) approved_by,\r\n"
				+ "       v.approved_by_status,\r\n" + "    " + "   v.approved_by_remark,\r\n"
				+ "       v.assigned_to_dev,\r\n" + "     " + "  v.assigned_dev_date,\r\n" + "    "
				+ "   v.dev_status,\r\n" + "       v.dev_remark,\r\n" + "     " + "  v.dev_close_date,\r\n" + "   "
				+ "    v.assigned_to_qc,\r\n" + "       v.assigned_qc_date,\r\n" + "     " + "  v.testing_status,\r\n"
				+ "     " + "  v.testing_remark,\r\n" + "       v.testing_closed_date,\r\n" + "     "
				+ "  v.deploy_delivered_by,\r\n" + "       v.deploy_delivered_date,\r\n" + "   "
				+ " 	  v.assigned_to_func,\r\n" + " 	  v.assigned_func_date,v.reopened_status,\r\n"
				+ " 	  v.func_remark,\r\n" + "    v.closure_remark,v.client_final_status ,\r\n"
				+ "       v.closure_date,\r\n" + "     " + "  v.req_closed_by,\r\n" + " 	  v.req_entered_mode,"
				+ "     " + "  v.lastupdate,v.deploy_fail_req_assigned_to,\r\n" + "       v.flag,\r\n" + "    "
				+ "   (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status "
				+ ", v.reopened_dev_name , v.func_status , v.final_status\r\n" + "     from REQ_TRAN v where 1=1  ";
		try {
			if (filter != null) {
				if (filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_name='" + filter.getProject_name() + "'");
				}
				if (filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='" + filter.getModule_name() + "'");
				}
				if (filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='" + filter.getMenu_name() + "'");
				}
				if (filter.getTo_date1() != null || filter.getFrom_date1() != null) {
					if (!filter.getFrom_date1().isEmpty()) {
						sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1()
								+ "','DD-MM-RRRR')");
					} else if (!filter.getTo_date1().isEmpty()) {
						sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1()
								+ "','DD-MM-RRRR')");
					}
				}
				if (filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='" + filter.getIssue_type() + "'");
				}
				if (filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='" + filter.getReq_priority() + "'");
				}
				if (filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='" + filter.getReq_type() + "'");
				}
			}
			Query = Query + sb;
			String role_name = userRoleMastService.getRoleNameByRoleCode(role_code);
//			System.out.println("role_name=" + role_name);

			if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("QUALITY ANALYST")) {
//				reqList = reqrepo.findByAssignedToQc(user_code);
				Query = Query + " and v.assigned_to_qc ='" + user_code + "' order by v.assigned_qc_date desc";
			} else if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("APP_DEVELOPER")) {
				Query = Query + " and (v.assigned_to_dev ='" + user_code
						+ "' and v.deploy_fail_req_assigned_to is null) or reopened_dev_name='" + user_code
						+ "' order by v.assigned_dev_date desc";
			} else {
//				reqList = reqrepo.findByAssignedToDev(user_code);
				Query = Query + " and (v.assigned_to_dev ='" + user_code + "' or  REINITIATE_WORK_ASSIGNED_TO='"
						+ user_code + "') order by v.assigned_dev_date desc";
			}

			System.out.println("Query from getAssignedRequisition1" + Query);
			reqList = executer.executeSQLQuery(Query);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reqList;
	}

	@Override
	public List<ReqTran> getReqApprovalDataFilter(String req_code, String from_date, String to_date,
			String project_name, String issue_type, String req_type, String req_priority) {
		List<ReqTran> getReqApprovalDataList = new ArrayList<>();
		String Query = "";
		try {
			Query = "\r\n" + "select v.req_code,\r\n" + "  "
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "   " + "    v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n" + "     " + "  v.issue_type,\r\n" + "    "
					+ "   v.issue_description,\r\n" + "       v.req_priority,\r\n" + "       v.parent_req_code,\r\n"
					+ "       v.req_error_group_str,\r\n" + "   " + " v.sample_data_filter_str,\r\n"
					+ "      v.reinitiate_work_assigned_to,\r\n" + "    "
					+ " v.reinitiate_pm_work_flag, v.reinitiate_pm_work_remark,\r\n" + "    " + "   v.approved_by,\r\n"
					+ "       v.approved_by_status,\r\n" + "    " + "   v.approved_by_remark,\r\n"
					+ "       v.assigned_to_dev,\r\n" + "     " + "  v.assigned_dev_date,\r\n" + "    "
					+ "   v.dev_status,\r\n" + "       v.dev_remark,\r\n" + "     " + "  v.dev_close_date,\r\n" + "   "
					+ "    v.assigned_to_qc,\r\n" + "       v.assigned_qc_date,\r\n" + "     "
					+ "  v.testing_status,\r\n" + "     " + "  v.testing_remark,\r\n"
					+ "       v.testing_closed_date,\r\n" + "     " + "  v.deploy_delivered_by,\r\n"
					+ "       v.deploy_delivered_date,\r\n" + "   " + " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n" + " 	  v.func_remark,\r\n" + "    v.closure_remark,\r\n"
					+ "       v.closure_date,\r\n" + "     " + "  v.req_closed_by,v.reopened_status,\r\n"
					+ " 	  v.req_entered_mode," + "     " + "  v.lastupdate,\r\n"
					+ "       v.flag, v.final_status,v.client_final_status, v.reopened_dev_name, v.deploy_fail_req_assigned_to,\r\n"
					+ "    "
					+ "   (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status \r\n"
					+ "  from REQ_TRAN v  WHERE 1=1" + "  ";
			if (!req_code.isEmpty()) {
				Query = Query + "and v.req_code='" + req_code + "' ";
			}
			if (!project_name.isEmpty()) {
				Query = Query + " and  v.project_name='" + project_name + "'";
			}
			if (!from_date.isEmpty() && !to_date.isEmpty()) {
				Query = Query + " and v.reported_date BETWEEN to_date('" + from_date + "','DD-MM-RRRR') AND to_date('"
						+ to_date + "','DD-MM-RRRR')";
			}
			if (!from_date.isEmpty() && to_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + from_date + "','DD-MM-RRRR')";
			}
			if (!to_date.isEmpty() && from_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + to_date + "','DD-MM-RRRR')";
			}
			if (!issue_type.isEmpty()) {
				Query = Query + " and  v.issue_type='" + issue_type + "'";
			}
			if (!req_type.isEmpty()) {
				Query = Query + " and  v.req_type='" + req_type + "'";
			}
			if (!req_priority.isEmpty()) {
				Query = Query + " and  v.req_priority='" + req_priority + "'";
			}
			Query = Query + "and v.approved_by_status is null order by v.reported_date desc";
			System.out.println("Query..11  " + Query);
			getReqApprovalDataList = executer.executeSQLQuery(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getReqApprovalDataList;
	}

	public List<ReqTran> getReqAssignedDataFilter(String req_code, String project_name, String req_type,
			String req_priority, String menu_name, String from_date, String to_date, String dev_status,
			String role_code, String user_code) {
//		System.out.println("req_priority.." + req_priority);
		List<ReqTran> reqList = new ArrayList<>();
		String Query = "";
		try {
			Query = "\r\n" + "select v.req_code,\r\n" + "  "
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "   " + "    v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n" + "     " + "  v.issue_type,\r\n" + "    "
					+ "   v.issue_description,\r\n" + "       v.req_priority,\r\n" + "       v.parent_req_code,\r\n"
					+ "       v.req_error_group_str,\r\n" + "   " + " v.sample_data_filter_str,\r\n"
					+ "      v.reinitiate_work_assigned_to,\r\n" + "    " + " v.reinitiate_pm_work_flag, "
					+ "v.reinitiate_pm_work_remark,\r\n" + "    "
					+ "(select n.user_name  from user_mast n where n.user_code = v.approved_by) approved_by,\r\n"
					+ "       v.approved_by_status,\r\n" + "    " + "   v.approved_by_remark,\r\n"
					+ "       v.assigned_to_dev,\r\n" + "     " + "  v.assigned_dev_date,\r\n" + "    "
					+ "   v.dev_status,\r\n" + "       v.dev_remark,\r\n" + "     " + "  v.dev_close_date,\r\n" + "   "
					+ "    v.assigned_to_qc,\r\n" + "       v.assigned_qc_date,\r\n" + "     "
					+ "  v.testing_status,\r\n" + "     " + "  v.testing_remark,\r\n"
					+ "       v.testing_closed_date,\r\n" + "     " + "  v.deploy_delivered_by,\r\n"
					+ "       v.deploy_delivered_date,\r\n" + "   " + " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n" + " 	  v.func_remark,\r\n" + "    v.closure_remark,\r\n"
					+ "       v.closure_date,\r\n" + "     " + "  v.req_closed_by,\r\n" + "     "
					+ "  v.lastupdate,\r\n" + "       v.flag, v.final_status," + "v.client_final_status,"
					+ " v.deploy_fail_req_assigned_to," + " v.reopened_status,\r\n" + "    "
					+ " 	  v.req_entered_mode," + " 	  v.reopened_dev_name,"
					+ "   (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status \r\n"
					+ "  from REQ_TRAN v  WHERE 1=1" + "  ";

			String role_name = userRoleMastService.getRoleNameByRoleCode(role_code);
			System.out.println("role_name==" + role_name);

			if (!req_code.isEmpty()) {
				Query = Query + "and v.req_code='" + req_code + "'";
			}
			if (!project_name.isEmpty()) {
				Query = Query + "and v.project_name='" + project_name + "'";
			}
			if (!req_type.isEmpty()) {
				Query = Query + "and v.req_type='" + req_type + "'";
			}
			if (!req_priority.isEmpty()) {
				Query = Query + "and v.req_priority='" + req_priority + "'";
			}
			if (!menu_name.isEmpty()) {
				Query = Query + "and v.menu_name='" + menu_name + "'";
			}
			if (!from_date.isEmpty() && !to_date.isEmpty()) {
				Query = Query + " and v.reported_date BETWEEN to_date('" + from_date + "','DD-MM-RRRR') AND to_date('"
						+ to_date + "','DD-MM-RRRR')";
			}
			if (!from_date.isEmpty() && to_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + from_date + "','DD-MM-RRRR')";
			}
			if (!to_date.isEmpty() && from_date.isEmpty()) {
				Query = Query + "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + to_date + "','DD-MM-RRRR')";
			}

			if (!dev_status.isEmpty()) {
				Query = Query + "and v.dev_status='" + dev_status + "'";
			}
			if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("Quality Analyst")) {
				Query = Query + "and v.assigned_to_qc ='" + user_code + "' order by v.reported_date desc ";

			}

			else if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("APP_DEVELOPER")) {
				Query = Query + "and v.assigned_to_dev ='" + user_code
						+ "'  and v.deploy_fail_req_assigned_to is null order by v.reported_date desc ";
			} else if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("CONSULTANT")) {

				Query = Query + "and v.assigned_to_func ='" + user_code + "' order by v.reported_date desc ";
			} else if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("MANAGER")) {
				Query = Query + "and (v.assigned_to_dev ='" + user_code + "'  or REINITIATE_WORK_ASSIGNED_TO ='"
						+ user_code + "' ) order by v.reported_date desc ";
			} else {
				Query = Query + "and v.assigned_to_dev ='" + user_code + "' order by v.reported_date desc ";

			}

//			if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("QUALITY ANALYST")) {
////				reqList = reqrepo.findByAssignedToQc(user_code);
//				Query = Query + "where v.assigned_to_qc ='" + user_code + "' order by v.assigned_qc_date desc";
//			}else if(!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("APP_DEVELOPER")) {
//				Query = Query + "where v.assigned_to_dev ='" + user_code + "' and v.deploy_fail_req_assigned_to is null order by v.assigned_dev_date desc";
//			}
//			else {
////				reqList = reqrepo.findByAssignedToDev(user_code);
//				Query = Query + "where v.assigned_to_dev ='" + user_code + "' order by v.assigned_dev_date desc";
//				
			System.out.println("Query from  impl....... " + Query);
//
			reqList = executer.executeSQLQuery(Query);
//
//		} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqList;
	}

	private String getParentReqDescription(String parentReqCode) {

		String issue_description = reqrepo.getParentReqDescription(parentReqCode);
		return issue_description;
	}

	private ReqTranAttach getAllReqTranAttachData(String reqCode) {
		ReqTranAttach entity = new ReqTranAttach();
		try {
			entity = reqTranAttachRepo.findByReqCode(reqCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public List<ReqTran> getReqEntryDashboardList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage,
			List<ReqTran> reqList) {
		List<ReqTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		System.out.println("dataGridDTO.getPaginator()..." + filter.getProject_code());
		System.out.println("filter1234.." + filter.getReq_code());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
//			System.out.println("paginatorEntity.getRecordsPerPage()).." + paginatorEntity.getRecordsPerPage());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
//				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();
				long maxVal = (!strUtl.isNull(recPerPage) && !recPerPage.equalsIgnoreCase("ALL"))
						? Long.parseLong(recPerPage)
						: paginatorEntity.getTotal();
//				System.out.println("minVal.." + minVal);
//				System.out.println("maxVal.." + maxVal);
				///////////////
				System.out.println("filter123.." + filter.getReq_code());
				list = reqTranRepositorySupport.getReqEntryDashboardList(filter, minVal, maxVal);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public List<ReqTran> getReqEntryDashboardListForClient(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage,
			List<ReqTran> reqList) {
		List<ReqTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		System.out.println("dataGridDTO.getPaginator() for client..." + filter.getProject_code());
		System.out.println("filter1234.." + filter.getReq_code());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
//			System.out.println("paginatorEntity.getRecordsPerPage()).." + paginatorEntity.getRecordsPerPage());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
//				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();
				long maxVal = (!strUtl.isNull(recPerPage) && !recPerPage.equalsIgnoreCase("ALL"))
						? Long.parseLong(recPerPage)
						: paginatorEntity.getTotal();
//				System.out.println("minVal.." + minVal);
//				System.out.println("maxVal.." + maxVal);
				///////////////
				System.out.println("filter123.." + filter.getReq_code());
				list = reqTranRepositorySupport.getReqEntryDashboardListForClient(filter, minVal, maxVal);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public List<ReqTran> getReqApprovalBrowseList(HttpSession session, FilterDTO filter, DataGridDTO dataGridDTO,
			String recPerPage, List<ReqTran> reqList) {
		List<ReqTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
//		System.out.println("dataGridDTO.getPaginator()..." + dataGridDTO.getPaginator());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
//			System.out.println("paginatorEntity.getRecordsPerPage()).." + paginatorEntity.getRecordsPerPage());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
//				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();
				long maxVal = (!strUtl.isNull(recPerPage) && !recPerPage.equalsIgnoreCase("ALL"))
						? Long.parseLong(recPerPage)
						: paginatorEntity.getTotal();
//				System.out.println("minVal.." + minVal);
//				System.out.println("maxVal.." + maxVal);

				UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
				list = reqTranRepositorySupport.getReqApprovalBrowseList(filter, minVal, maxVal,
						userMast.getRole_code(), userMast.getDept_str());
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public List<ReqTran> getReqAssignedBrowseList(HttpSession session, FilterDTO filter, DataGridDTO dataGridDTO,
			String recPerPage) {
		List<ReqTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
//		System.out.println("dataGridDTO.getPaginator()..." + dataGridDTO.getPaginator());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
//			System.out.println("paginatorEntity.getRecordsPerPage()).." + paginatorEntity.getRecordsPerPage());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
//				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();
				long maxVal = (!strUtl.isNull(recPerPage) && !recPerPage.equalsIgnoreCase("ALL"))
						? Long.parseLong(recPerPage)
						: paginatorEntity.getTotal();
//				System.out.println("minVal.." + minVal);
//				System.out.println("maxVal.." + maxVal);

				UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
				list = reqTranRepositorySupport.getReqAssignedBrowseList(filter, minVal, maxVal,
						userMast.getRole_code(), userMast.getUser_code(), userMast.getDept_str());
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public long getUserCount(FilterDTO filter, String type) {
		return reqTranRepositorySupport.getUserTranBrowseCount(filter, type);
	}

	@Override
	public long getUserCountForCount(FilterDTO filter, String type) {
		return reqTranRepositorySupport.getUserTranBrowseCountForClient(filter, type);
	}

	public long getCountOfAssignedReq(FilterDTO filter, String user_code, String role_Code) {
		return reqTranRepositorySupport.getCountOfAssignedReq(filter, user_code, role_Code);
	}

	public long getCountOfApprovedReq(FilterDTO filter, String user_code) {
		return reqTranRepositorySupport.getCountOfApprovedReq(filter, user_code);
	}

	@Override
	public String rejectRequisition(String req_code, String user_code) {
		String result = "error";
//		System.out.println("req_code>>>" + req_code);
		try {
			String queryStr = "update req_tran t \n " + " set current_req_status = 'CRS_004', \n"
					+ " approved_by_status = 'R' ,\n" + "  t.approved_by = '" + user_code + "'\n" + "where t.req_code='"
					+ req_code + "'";

//			System.out.println("queryStr>>" + queryStr);

			int update = executer.updateSQLQuery(queryStr);

			if (update > 0) {
				result = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	@Transactional
	public String deleteDocument(String seq_id) {
//		System.out.println("seq_id 1 = "+seq_id);
		String response = "error";
//		long seq_id1 = Long.parseLong(seq_id);
		try {
//			System.out.println("seq_id 2 = "+seq_id1);
//			deploymentTranRepo.deleteById1(seq_id1);
			String query = "delete from lhssys_taxcpc_deployment_tran t where t.seq_id =:seq_id1";
			Query nativeQuery = entityManager.createNativeQuery(query, LhssysTaxcpcDeploymentTran.class);
			nativeQuery.setParameter("seq_id1", seq_id);
			nativeQuery.executeUpdate();
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String viewDeploymentDetails(Long seq_id) {
		System.out.println("seq_id >>>> " + seq_id);
		StringBuilder sb = new StringBuilder();
		String project_code = "";
		String war_filename = "";
		String sample_data_filter_str = "";
		String remark = "";
		String server_url = "";
		String depl_type = "";

		LhssysTaxcpcDeploymentTran deploymentTran = getDeploymentDataListByCode(seq_id);
		project_code = getProjectName(deploymentTran.getProject_code());
		war_filename = deploymentTran.getWar_filename();
		sample_data_filter_str = deploymentTran.getSample_data_filter_str();
		remark = deploymentTran.getRemark();
		server_url = deploymentTran.getServer_url();
		depl_type = getDeplTypeName(deploymentTran.getDepl_code());
		String depl_code = deploymentTran.getDepl_code();
		System.out.println("depl_code===" + depl_code);

		try {
			if (deploymentTran != null) {

				if (project_code != null) {
					sb.append("<tr><td class=\"text-bold\">Application Name  </td><td>" + project_code + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Application Name  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (war_filename != null) {
					sb.append("<tr><td class=\"text-bold\">WAR File Name  </td><td class=\"pre-wrap\">" + war_filename
							+ "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">WAR File Name  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (sample_data_filter_str != null) {
					sb.append("<tr><td class=\"text-bold\">Database Details  </td><td class=\"pre-wrap\">"
							+ sample_data_filter_str + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Database Details  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (depl_type != null) {
					sb.append("<tr><td class=\"text-bold\">Deployment Type  </td><td class=\"pre-wrap\">" + depl_type
							+ "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Deployment Type  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (depl_code.contains("APP_WAR_FILE")) {
					if (server_url != null) {
						sb.append("<tr><td class=\"text-bold\">Server URL  </td><td class=\"pre-wrap\">" + server_url
								+ "</td></tr>");
					} else {
						sb.append("<tr><td class=\"text-bold\">Server URL  </td><td class=\"pre-wrap\"></td></tr>");
					}
				} else {
					if (server_url != null) {
						sb.append("<tr><td class=\"text-bold\">Server IP  </td><td class=\"pre-wrap\">" + server_url
								+ "</td></tr>");
					} else {
						sb.append("<tr><td class=\"text-bold\">Server IP  </td><td class=\"pre-wrap\"></td></tr>");
					}

				}

				if (deploymentTran.getLastupdate() != null) {
					sb.append("<tr><td class=\"text-bold\">Deployment Date  </td><td class=\"pre-wrap\">")
							.append(lhsDateUtility.getFormattedDate(deploymentTran.getLastupdate(), "dd-MM-yyyy"))
							.append("</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Deployment Date  </td><td class=\"pre-wrap\">")
							.append("</td></tr>");
				}
				if (remark != null) {
					sb.append(
							"<tr><td class=\"text-bold\">Remark  </td><td class=\"pre-wrap\">" + remark + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Remark  </td><td class=\"pre-wrap\"></td></tr>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getDeplTypeName(String depl_code) {
		String depl_code1 = "";
		try {
			String queryStr = "select depl_name from View_Deployment_Type t WHERE t.depl_code='" + depl_code + "'";
			depl_code1 = (String) executer.getSingleValueFromSQLQuery(queryStr);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return depl_code1;
	}

	public LhssysTaxcpcDeploymentTran getDeploymentDataListByCode(Long seq_id) {
		LhssysTaxcpcDeploymentTran deploymentTrany = new LhssysTaxcpcDeploymentTran();

		try {
			deploymentTrany = deploymentTranRepo.findBySeqId(seq_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deploymentTrany;
	}

	@Override
	public List<LhssysTaxcpcDeploymentTran> getDeploymentDataList() {

		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<>();

		String Query = "";
		try {
			Query = "select  t.seq_id,\r\n"
					+ "	(select j.project_name  from project_mast j where j.project_code = t.project_code) project_code,\r\n"
					+ " t.war_filename,\r\n" + " t.sample_data_filter_str,\r\n" + " t.remark,\r\n"
					+ " t.server_url,\r\n" + " t.lastupdate,\r\n" + " t.flag,\r\n"
					+ "(select j.depl_name  from view_deployment_type j where j.depl_code = t.depl_code) depl_code\r\n"
					+ " from lhssys_taxcpc_deployment_tran t" + " order by t.lastupdate desc";

			System.out.println("Query >> " + Query);

			list = executer.executeSQLQuery1(Query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<LhssysTaxcpcDeploymentTran> getDeploymentDataListFilter(String project_name, String depl_code,
			String war_file) {
		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<>();

		String Query = "";
		try {
			Query = "			select  t.seq_id,\r\n"
					+ "		(select j.project_name  from project_mast j where j.project_code = t.project_code) project_code,\r\n"
					+ "       t.war_filename,\r\n" + "       t.sample_data_filter_str,\r\n" + "       t.remark,\r\n"
					+ "       t.server_url,\r\n" + "       t.lastupdate,\r\n" + "       t.flag\r\n"
					+ "  		from lhssys_taxcpc_deployment_tran t " + " WHERE 1=1 ";

			if (!project_name.isEmpty()) {
				Query = Query + "and t.project_code='" + project_name + "'";
			}
			if (!depl_code.isEmpty()) {
				Query = Query + " and t.depl_code='" + depl_code + "'";
			}
			if (!war_file.isEmpty()) {
				Query = Query + " and  t.war_filename='" + war_file + "'";
			}
			Query = Query + " order by t.lastupdate desc";

			System.out.println("Query >> " + Query);

			list = executer.executeSQLQuery1(Query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public long getDeplCount(FilterDTO filter) {
		return lhssysTaxcpcDeploymentTranRepositorySupport.getDeplTranBrowseCount(filter);
	}

	@Override
	public List<LhssysTaxcpcDeploymentTran> getDeplDashboardList(FilterDTO filter, DataGridDTO dataGridDTO,
			String recPerPage) {
		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
//		System.out.println("dataGridDTO.getPaginator()..." + dataGridDTO.getPaginator());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
//			System.out.println("paginatorEntity.getRecordsPerPage()).." + paginatorEntity.getRecordsPerPage());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
//				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();
				long maxVal = (!strUtl.isNull(recPerPage) && !recPerPage.equalsIgnoreCase("ALL"))
						? Long.parseLong(recPerPage)
						: paginatorEntity.getTotal();
//				System.out.println("minVal.." + minVal);
//				System.out.println("maxVal.." + maxVal);
				///////////////
//				list = reqTranRepositorySupport.getReqEntryDashboardList(filter, minVal, maxVal);
				list = lhssysTaxcpcDeploymentTranRepositorySupport.getDeplDashboardList(filter, minVal, maxVal);
				////////
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<LhssysTaxcpcDeploymentTran> getDeplSearchData(String project_name, String depl_code, String war_file) {
		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<>();
		String Query = "";
		try {
			Query = "select  t.seq_id,\r\n"
					+ "  (select j.project_name  from project_mast j where j.project_code = t.project_code) project_code,\r\n"
					+ " t.war_filename,\r\n" + " t.sample_data_filter_str,\r\n" + " t.remark,\r\n"
					+ " t.server_url,\r\n" + " t.lastupdate,\r\n" + " t.flag,\r\n"
					+ "(select j.depl_name  from view_deployment_type j where j.depl_code = t.depl_code) depl_code\r\n"
					+ " from lhssys_taxcpc_deployment_tran t " + " WHERE 1=1 ";

			if (!project_name.isEmpty()) {
				Query = Query + "and t.project_code='" + project_name + "'";
			}
			if (!depl_code.isEmpty()) {
				Query = Query + "and t.depl_code='" + depl_code + "'";
			}
			if (!war_file.isEmpty()) {
				Query = Query + "and t.war_filename='" + war_file + "'";
			}

			Query = Query + " order by t.lastupdate desc";
			System.out.println("Query from  impl...." + Query);

			list = executer.executeSQLQuery7(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getFileNames() {
		List<String> list = new ArrayList<String>();

		try {
			list = reqTranAttachRepo.getFileName();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String viewLovDescription(String discription) {

		StringBuilder sb = new StringBuilder();

		sb.append("<div>");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-12\">");
		sb.append("<table  class=\"table details-table\">");
		sb.append("<tbody>");

		if (discription != null) {
			sb.append("<tr><td class=\"pre-wrap\">" + discription + "</td></tr>");
		}

		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	@Override

	public String viewReqDashboard(String reqCode, String role_type_code, String sub_menu_code) {

		StringBuilder sb = new StringBuilder();

		String ProjectName = "";
		String ModuleName = "";
		String menuName = "";
		String reported_by = "";
		String approved_to = "";
		String approved_to_qc = "";
		String dev_status = "";
		String qc_status = "";
		String deploy_status = "";
		String approved_by = "";
		String assigned_to_func = "";
		String req_type = "";
		String reopen_assign_to = "";
		String current_status = "";

		try {

			ReqTran entity = getAllDataByReqCode(reqCode);

			ProjectName = getProjectName(entity.getProject_name());
			ModuleName = getModuleName(entity.getModule_name());
			menuName = getMenuName(entity.getMenu_name());
			reported_by = getUserName(entity.getReported_by());
			approved_to = getUserName(entity.getAssigned_to_dev());
			approved_to_qc = getUserName(entity.getAssigned_to_qc());
			approved_by = getUserName(entity.getApproved_by());
			assigned_to_func = getUserName(entity.getAssigned_to_func());
			req_type = entity.getReq_type();
			reopen_assign_to = getUserName(entity.getReopened_dev_name());
			current_status = getCurrentStatus(entity.getCurrent_req_status());
			ReqTranAttach reqTranAttach = getAllReqTranAttachData(reqCode);

//          System.out.println("reqTranAttach=" + reqTranAttach.getReq_attach_name());

			List<Integer> listOfSlno = new ArrayList<>();

			listOfSlno = reqTranAttachRepo.getListOfslno(reqCode);


			List<String> listOfFileName = new ArrayList<>();

			listOfFileName = reqTranAttachRepo.getListOfFileName(reqCode);


			sb.append("<div>");

			sb.append("<div class=\"row\">");

			sb.append("<div class=\"col-md-12\">");

////////////                            

			if (entity != null) {

				if (role_type_code.equalsIgnoreCase("CLNT")) { // FOR CLIENT USER

					if (req_type.equalsIgnoreCase("Application")) { // req_type=Application

						sb.append("<div class=\"container\">");

						// start of first card

						sb.append("<div class=\"card\">"); // first card

						sb.append("<div class=\"card-header\">");

						sb.append("<a class=\"card-link\" href=\"#collapseOne\">Project Information </a>");

						sb.append(" </div>");

						sb.append("<div id=\"collapseOne\" class=\"collapse show\">");

						sb.append("<div class=\"card-body\">");

						sb.append("<table  class=\"table details-table\">");

						sb.append("<tbody>");

						sb.append("<tr>");

						if (ProjectName != null) {
							sb.append("<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td>"
									+ ProjectName + "</td>");
						} else {
							sb.append("<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td></td>");
						}

						if (ModuleName != null) {

							sb.append(

									"<td class=\"text-bold\" style=\"width:90px;\">Module Name  </td><td class=\"pre-wrap\">"

											+ ModuleName + "</td>");

						} else {

							sb.append(

									"<td class=\"text-bold\" style=\"width:90px;\">Module Name  </td><td class=\"pre-wrap\"></td>");

						}

						if (menuName != null) {

							sb.append(

									"<td class=\"text-bold\" style=\"width:80px;\">Menu Name  </td><td class=\"pre-wrap\">"

											+ menuName + "</td>");

						} else {

							sb.append(

									"<td class=\"text-bold\" style=\"width:80px;\">Menu Name  </td><td class=\"pre-wrap\"></td>");

						}

						sb.append("</tr>");

						sb.append("</tbody>");

						sb.append("</table>");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>"); // end of first card

						// start of second card

						sb.append("<div class=\"card\">");

						sb.append("<div class=\"card-header\">");

						sb.append("<a class=\"card-link\" href=\"#collapseTwo\">Requisition Information</a>");

						sb.append(" </div>");

						sb.append("<div id=\"collapseTwo\" class=\"collapse show\">");

						sb.append("<div class=\"card-body\">");

						sb.append("<table  class=\"table details-table\">");

						sb.append("<tbody>");

						if (entity.getReq_type() != null) {

							sb.append(
									"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\">"

											+ entity.getReq_type() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getReq_priority() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority  </td><td class=\"pre-wrap\">"

											+ entity.getReq_priority() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getIssue_type() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\">"

											+ entity.getIssue_type() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getReq_error_group_str() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Error Classification  </td><td class=\"pre-wrap\">"

											+ entity.getReq_error_group_str() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Error Classification  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getSample_data_filter_str() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\">"

											+ entity.getSample_data_filter_str() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\"</td></tr>");

						}

						if (entity.getIssue_description() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\">"

											+ entity.getIssue_description() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (reported_by != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\">"

											+ reported_by + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\"></td></tr>");

						}

						sb.append(

								"<tr><td class=\"text-bold\" style=\"width:100px;\">Reportde Date  </td><td class=\"pre-wrap\">")

								.append(lhsDateUtility.getFormattedDate(entity.getReported_date(), "dd-MM-yyyy"))

								.append("</td></tr>");

						if (entity.getCurrent_req_status() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\">"

											+ current_status + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\"></td></tr>");

						}
						if (entity.getClosure_remark() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\">"

											+ entity.getClosure_remark() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\"></td></tr>");

						}

						sb.append("</tbody>");

						sb.append("</table>");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>"); // end of second card

						// DOWNLOAD BUTTON

						sb.append("<input type=\"hidden\" name=\"req_code\" id=\"reqCode_sb\" value="

								+ entity.getReq_code() + " />");

						sb.append("<input type=\"hidden\"  id=\"req_type_sb\" value=" + entity.getReq_type() + " />");

						sb.append("<div class=\"card\">");

						sb.append("<div class=\"card-header\">");

						sb.append("<a class=\"card-link\" href=\"#collapsedwnld\">Attachments");

						for (int i = 0; i < listOfSlno.size(); i++) {

							sb.append("<input type=\"hidden\" name=\"slno\" th:id=\"slno~" + i + "\" value="

									+ listOfSlno.get(i) + " />");

							sb.append("<button th:type=\"button\"  id="

									+ entity.getReq_code() + "~" + listOfSlno.get(i)
									+ " class=\"btn btn-primary addon-btn filter-inner-btn\"\r\n"

									+ "title=\"Attachment Bulk-" + listOfFileName.get(i)

									+ "\" onclick=\"downloadSingleFile(this.id);\" style=\"float: right; margin-left: 40px;\"><i class=\"fa download\"></i></button>");

						}

						sb.append("</a>");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>");// container

					} else {// req_type=Deployment

						sb.append("<div class=\"container\">");

						// start of first card

						sb.append("<div class=\"card\">"); // first card

						sb.append("<div class=\"card-header\">");

						sb.append("<a class=\"card-link\" href=\"#collapseOne\">Project Information </a>");

						sb.append(" </div>");

						sb.append("<div id=\"collapseOne\" class=\"collapse show\">");

						sb.append("<div class=\"card-body\">");

						sb.append("<table  class=\"table details-table\">");

						sb.append("<tbody>");

						if (ProjectName != null) {

							sb.append("<tr><td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td>"

									+ ProjectName + "</td>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td></td></tr>");

						}

						sb.append("</tbody>");

						sb.append("</table>");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>"); // end of first card

						// start of second card

						sb.append("<div class=\"card\">");

						sb.append("<div class=\"card-header\">");

						sb.append("<a class=\"card-link\" href=\"#collapseTwo\">Requisition Information</a>");

						sb.append(" </div>");

						sb.append("<div id=\"collapseTwo\" class=\"collapse show\">");

						sb.append("<div class=\"card-body\">");

						sb.append("<table  class=\"table details-table\">");

						sb.append("<tbody>");

						if (entity.getReq_type() != null) {

							sb.append(
									"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\">"

											+ entity.getReq_type() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getReq_priority() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority  </td><td class=\"pre-wrap\">"

											+ entity.getReq_priority() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getIssue_type() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\">"

											+ entity.getIssue_type() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getSample_data_filter_str() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\">"

											+ entity.getSample_data_filter_str() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (entity.getIssue_description() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\">"

											+ entity.getIssue_description() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\"></td></tr>");

						}

						if (reported_by != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\">"

											+ reported_by + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\"></td></tr>");

						}

						sb.append(

								"<tr><td class=\"text-bold\" style=\"width:100px;\">Reportde Date  </td><td class=\"pre-wrap\">")

								.append(lhsDateUtility.getFormattedDate(entity.getReported_date(), "dd-MM-yyyy"))

								.append("</td></tr>");

						if (entity.getCurrent_req_status() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\">"

											+ current_status + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\"></td></tr>");

						}
						if (entity.getClosure_remark() != null) {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\">"

											+ entity.getClosure_remark() + "</td></tr>");

						} else {

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\"></td></tr>");

						}

						sb.append("</tbody>");

						sb.append("</table>");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>"); // end of second card

						// DOWNLOAD BUTTON

						sb.append("<input type=\"hidden\" name=\"req_code\" id=\"reqCode_sb\" value="
								+ entity.getReq_code() + " />");

						sb.append("<input type=\"hidden\"  id=\"req_type_sb\" value=" + entity.getReq_type() + " />");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>");

						sb.append("</div>");// container

					}

				} else { // FOR ALL OTHER USERS

					if (sub_menu_code.equalsIgnoreCase("MENU-018")) { // Requisition Approval

						if (req_type.equalsIgnoreCase("Application")) { // req_type=Application

							sb.append("<div class=\"container\">");

							// start of first card

							sb.append("<div class=\"card\">"); // first card

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseOne\">Project Information </a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseOne\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							sb.append("<tr>");

							if (ProjectName != null) {

								sb.append("<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td>"

										+ ProjectName + "</td>");

							} else {

								sb.append(

										"<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td></td>");

							}

							if (ModuleName != null) {

								sb.append(

										"<td class=\"text-bold\" style=\"width:90px;\">Module Name  </td><td class=\"pre-wrap\">"

												+ ModuleName + "</td>");

							} else {

								sb.append(

										"<td class=\"text-bold\" style=\"width:90px;\">Module Name  </td><td class=\"pre-wrap\"></td>");

							}

							if (menuName != null) {

								sb.append(

										"<td class=\"text-bold\" style=\"width:80px;\">Menu Name  </td><td class=\"pre-wrap\">"

												+ menuName + "</td></tr>");

							} else {

								sb.append(

										"<td class=\"text-bold\" style=\"width:80px;\">Menu Name  </td><td class=\"pre-wrap\"></td>");

							}

							sb.append("</tr>");

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of first card

							// start of second card

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseTwo\">Requisition Information</a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseTwo\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (entity.getReq_type() != null) {

								sb.append("<tr><td class=\"text-bold\">Requisition Type  </td><td class=\"pre-wrap\">"

										+ entity.getReq_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getReq_priority() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority  </td><td class=\"pre-wrap\">"

												+ entity.getReq_priority() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getParent_req_code() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Parent Requisition Code  </td><td class=\"pre-wrap\">"

												+ entity.getParent_req_code() + "</td></tr>");

							} else {

//                                                                                 sb.append("<tr><td class=\"text-bold\">Parent Requisition Code  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_description() != null && entity.getParent_req_code() != null) {

								String issue_description = getParentReqDescription(entity.getParent_req_code());

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Parent Requisition Description  </td><td class=\"pre-wrap\">"

												+ issue_description + "</td></tr>");

							} else {

//                                                                                 sb.append("<tr><td class=\"text-bold\">Parent Requisition Description  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_type() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getReq_error_group_str() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Error Classification  </td><td class=\"pre-wrap\">"

												+ entity.getReq_error_group_str() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Error Classification  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getSample_data_filter_str() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\">"

												+ entity.getSample_data_filter_str() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_description() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_description() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (reported_by != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\">"

												+ reported_by + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reportde Date  </td><td class=\"pre-wrap\">")

									.append(lhsDateUtility.getFormattedDate(entity.getReported_date(), "dd-MM-yyyy"))

									.append("</td></tr>");

							if (entity.getCurrent_req_status() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\">"

												+ current_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of second card

							// DOWNLOAD BUTTON

							sb.append("<input type=\"hidden\" name=\"req_code\" id=\"reqCode_sb\" value="

									+ entity.getReq_code() + " />");

							sb.append(
									"<input type=\"hidden\"  id=\"req_type_sb\" value=" + entity.getReq_type() + " />");

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapsedwnld\">Attachments");

							for (int i = 0; i < listOfSlno.size(); i++) {

								sb.append("<input type=\"hidden\" name=\"slno\" th:id=\"slno~" + i + "\" value="

										+ listOfSlno.get(i) + " />");

								sb.append("<button th:type=\"button\"  id="

										+ entity.getReq_code() + "~" + listOfSlno.get(i)
										+ " class=\"btn btn-primary addon-btn filter-inner-btn\"\r\n"

										+ "title=\"Attachment Bulk-" + listOfFileName.get(i)

										+ "\" onclick=\"downloadSingleFile(this.id);\" style=\"float: right;margin-left: 40px;\"><i class=\"fa download\"></i></button>");

							}

							sb.append("</a>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");// container

						} else {// req_type=Deployment

							sb.append("<div class=\"container\">");

							// start of first card

							sb.append("<div class=\"card\">"); // first card

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseOne\">Project Information </a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseOne\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (ProjectName != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td>"

												+ ProjectName + "</td>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td></td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of first card

							// start of second card

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseTwo\">Requisition Information</a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseTwo\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (entity.getReq_type() != null) {

								sb.append("<tr><td class=\"text-bold\">Requisition Type  </td><td class=\"pre-wrap\">"

										+ entity.getReq_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getReq_priority() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority  </td><td class=\"pre-wrap\">"

												+ entity.getReq_priority() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_type() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getSample_data_filter_str() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\">"

												+ entity.getSample_data_filter_str() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_description() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_description() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (reported_by != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\">"

												+ reported_by + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append(

									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reportde Date  </td><td class=\"pre-wrap\">")

									.append(lhsDateUtility.getFormattedDate(entity.getReported_date(), "dd-MM-yyyy"))

									.append("</td></tr>");

							if (entity.getCurrent_req_status() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\">"

												+ current_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of second card

							// DOWNLOAD BUTTON

							sb.append("<input type=\"hidden\" name=\"req_code\" id=\"reqCode_sb\" value="
									+ entity.getReq_code() + " />");

							sb.append(
									"<input type=\"hidden\"  id=\"req_type_sb\" value=" + entity.getReq_type() + " />");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");// container

						}

					} else { // Requisition Dashboard and Assigned

						if (req_type.equalsIgnoreCase("Application")) { // req_type=Application

							sb.append("<div class=\"container\">");

							// start of first card

							sb.append("<div class=\"card\">"); // first card

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseOne\">Project Information </a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseOne\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							sb.append("<tr>");

							if (ProjectName != null) {

								sb.append("<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td>"
										+ ProjectName

										+ "</td>");

							} else {

								sb.append(
										"<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td></td>");

							}

							if (ModuleName != null) {

								sb.append(
										"<td class=\"text-bold\" style=\"width:90px;\">Module Name  </td><td class=\"pre-wrap\">"

												+ ModuleName + "</td>");

							} else {

								sb.append(

										"<td class=\"text-bold\" style=\"width:90px;\">Module Name  </td><td class=\"pre-wrap\"></td>");

							}

							if (menuName != null) {

								sb.append(
										"<td class=\"text-bold\" style=\"width:80px;\">Menu Name  </td><td class=\"pre-wrap\">"

												+ menuName + "</td>");

							} else {

								sb.append(

										"<td class=\"text-bold\" style=\"width:80px;\">Menu Name  </td><td class=\"pre-wrap\"></td>");

							}

							sb.append("</tr>");

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of first card

							// start of second card

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseTwo\">Requisition Information</a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseTwo\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (entity.getReq_type() != null) {

								sb.append("<tr><td class=\"text-bold\">Requisition Type  </td><td class=\"pre-wrap\">"

										+ entity.getReq_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getReq_priority() != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority  </td><td class=\"pre-wrap\">"

												+ entity.getReq_priority() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\"style=\"width:100px;\">Priority </td><td class=\"pre-wrap\" ></td></tr>");

							}

							if (entity.getParent_req_code() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Parent Requisition Code  </td><td class=\"pre-wrap\">"

												+ entity.getParent_req_code() + "</td></tr>");

							} else {

//                                                                                 sb.append("<tr><td class=\"text-bold\">Parent Requisition Code  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_description() != null && entity.getParent_req_code() != null) {

								String issue_description = getParentReqDescription(entity.getParent_req_code());

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Parent Requisition Description  </td><td class=\"pre-wrap\">"

												+ issue_description + "</td></tr>");

							} else {

//                                                                                 sb.append("<tr><td class=\"text-bold\">Parent Requisition Description  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_type() != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getReq_error_group_str() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Error Classification  </td><td class=\"pre-wrap\">"

												+ entity.getReq_error_group_str() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Error Classification  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getSample_data_filter_str() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\">"

												+ entity.getSample_data_filter_str() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_description() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_description() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (reported_by != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\">"

												+ reported_by + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append(
									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reportde Date  </td><td class=\"pre-wrap\">")

									.append(lhsDateUtility.getFormattedDate(entity.getReported_date(), "dd-MM-yyyy"))

									.append("</td></tr>");

							if (approved_by != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By  </td><td class=\"pre-wrap\">"

												+ approved_by + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getApproved_by_remark() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By Remark  </td><td class=\"pre-wrap\">"

												+ entity.getApproved_by_remark() + "</td></tr>");

							} else {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By Remark </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getCurrent_req_status() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\">"

												+ current_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getClosure_remark() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\">"

												+ entity.getClosure_remark() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of second card

							// start of third card

							sb.append("<div id=\"accordion1\">");

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<div class=\"row\">");

							sb.append("<div class=\"collapseExampletwo\" style=\"margin-left: 15px;\">");

							sb.append(
									"<a class=\"card-link\" data-toggle=\"collapse\" href=\"#collapseThree\">Assign To Developer Team</a>");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append("</div>");

							sb.append("<div class=\"collapseExampletwo\">");

							sb.append(
									"<a class=\"card-link\" data-toggle=\"collapse\" href=\"#collapseFour\">Assign To QA Team</a>");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append("</div>");

							sb.append("<div class=\"collapseExampletwo\">");

							sb.append(
									"<a class=\"card-link\" data-toggle=\"collapse\" href=\"#collapseFive\">Assign To Deploy Team</a>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");

							// start of first collapse

							sb.append("<div id=\"collapseThree\" class=\"collapse\" data-parent=\"#accordion1\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (approved_to != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Developer  </td><td class=\"pre-wrap\">"
												+ approved_to + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Developer  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDev_status() != null) {

								dev_status = entity.getDev_status();

								if (dev_status.equalsIgnoreCase("S")) {

									dev_status = "Started";

								} else if (dev_status.equalsIgnoreCase("C")) {

									dev_status = "Closed";

								}

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Status  </td><td class=\"pre-wrap\">"

												+ dev_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDev_remark() != null) {
								String[] r = {};
								String remark = "";
								if (entity.getDev_remark().contains("###")) {
									r = entity.getDev_remark().split("###");
									remark = r[0];
								}

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Remark </td><td class=\"pre-wrap\">"
												+ remark + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDev_close_date() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Close Date  </td><td class=\"pre-wrap\">")

										.append(lhsDateUtility.getFormattedDate(entity.getDev_close_date(),
												"dd-MM-yyyy"))

										.append("</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Close Date  </td><td class=\"pre-wrap\">")

										.append("</td></tr>");

							}
							if (entity.getReopened_dev_name() != null) {
								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">Reopened Assign To </td><td class=\"pre-wrap\">"
												+ reopen_assign_to + "</td></tr>");
							} else {
								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">Reopened Assign To </td><td class=\"pre-wrap\"></td></tr>");
							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>"); // end of first collapse

							// start of second collapse

							sb.append("<div id=\"collapseFour\" class=\"collapse\" data-parent=\"#accordion1\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (approved_to_qc != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned to QC  </td><td class=\"pre-wrap\">"

												+ approved_to_qc + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned to QC  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getTesting_status() != null) {

								qc_status = entity.getTesting_status();

								if (qc_status.equalsIgnoreCase("S")) {

									qc_status = "Started";

								} else if (qc_status.equalsIgnoreCase("C")) {

									qc_status = "Closed";

								}

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Status  </td><td class=\"pre-wrap\">"

												+ qc_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getTesting_remark() != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Remark </td><td class=\"pre-wrap\">"

												+ entity.getTesting_remark() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getTesting_closed_date() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Closed Date  </td><td class=\"pre-wrap\">")

										.append(lhsDateUtility.getFormattedDate(entity.getTesting_closed_date(),
												"dd-MM-yyyy"))

										.append("</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Closed Date  </td><td class=\"pre-wrap\">")

										.append("</td></tr>");

							}
							if (entity.getReopened_remark() != null) {
								String[] r1 = {};
								String r2 = "";
								String[] r3 = {};
								String remark = "";
								if (entity.getReopened_remark().contains("RE1#")) {
									r1 = entity.getReopened_remark().split("RE1#");
									r2 = r1[1];
									if (r2.contains("###")) {
										r3 = r2.split("###");
										remark = r3[0];

									}
									System.out.println("rrrrrrrrrrr=" + remark);
								}
								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">Reopned Remark </td><td class=\"pre-wrap\">"
												+ remark + "</td></tr>");

							} else {
								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">Reopned Remark </td><td class=\"pre-wrap\"></td></tr>");
							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>"); // end of second collapse

							// start of third collapse

							sb.append("<div id=\"collapseFive\" class=\"collapse\" data-parent=\"#accordion1\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (entity.getAssigned_to_func() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Deploy Team </td><td class=\"pre-wrap\">"

												+ assigned_to_func + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Deploy Team  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDeploy_status() != null) {

								deploy_status = entity.getDeploy_status();

								if (deploy_status.equalsIgnoreCase("S")) {

									deploy_status = "Success";

								} else if (deploy_status.equalsIgnoreCase("F")) {

									deploy_status = "Fail";

								} else if (deploy_status.equalsIgnoreCase("R")) {

									deploy_status = "Restart";

								}

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Status </td><td class=\"pre-wrap\">"

												+ deploy_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getFunc_remark() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Remark </td><td class=\"pre-wrap\">"

												+ entity.getFunc_remark() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\"style=\"width:170px;\">Deploy Team Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDeploy_delivered_date() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Closed Date  </td><td class=\"pre-wrap\">")

										.append(lhsDateUtility.getFormattedDate(entity.getDeploy_delivered_date(),
												"dd-MM-yyyy"))

										.append("</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Closed Date  </td><td class=\"pre-wrap\">")

										.append("</td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");// end of third collapse

							sb.append("</div>");// accordion1

							// DOWNLOAD BUTTON

							sb.append(

									"<input type=\"hidden\" name=\"req_code\" id=\"reqCode_sb\" value="
											+ entity.getReq_code() + " />");

							sb.append(
									"<input type=\"hidden\"  id=\"req_type_sb\" value=" + entity.getReq_type() + " />");

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapsedwnld\">Attachments");

							for (int i = 0; i < listOfSlno.size(); i++) {

								sb.append("<input type=\"hidden\" name=\"slno\" th:id=\"slno~" + i + "\" value="

										+ listOfSlno.get(i) + " />");

								sb.append("<button th:type=\"button\"  id="

										+ entity.getReq_code() + "~" + listOfSlno.get(i)
										+ " class=\"btn btn-primary addon-btn filter-inner-btn\"\r\n"

										+ "title=\"Attachment Bulk-" + listOfFileName.get(i)

										+ "\" onclick=\"downloadSingleFile(this.id);\" style=\"float: right;margin-left: 40px;\"><i class=\"fa download\"></i></button>");

							}

							sb.append("</a>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");// container

						} else {// req_type=Deployment

							sb.append("<div class=\"container\">");

							// start of first card

							sb.append("<div class=\"card\">"); // first card

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseOne\">Project Information </a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseOne\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							sb.append("<tr>");

							if (ProjectName != null) {

								sb.append("<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td>"
										+ ProjectName

										+ "</td>");

							} else {

								sb.append(
										"<td class=\"text-bold\" style=\"width:90px;\">Application Name  </td><td></td>");

							}

							sb.append("</tr>");

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of first card

							// start of second card

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<a class=\"card-link\" href=\"#collapseTwo\">Requisition Information</a>");

							sb.append(" </div>");

							sb.append("<div id=\"collapseTwo\" class=\"collapse show\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (entity.getReq_type() != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\">"

												+ entity.getReq_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Requisition Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getReq_priority() != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority  </td><td class=\"pre-wrap\">"

												+ entity.getReq_priority() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Priority </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_type() != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_type() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Type  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getSample_data_filter_str() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\">"

												+ entity.getSample_data_filter_str() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Sample Data Filter  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getIssue_description() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\">"

												+ entity.getIssue_description() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Issue Description  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (reported_by != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\">"

												+ reported_by + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Reported By  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append(
									"<tr><td class=\"text-bold\" style=\"width:100px;\">Reportde Date  </td><td class=\"pre-wrap\">")

									.append(lhsDateUtility.getFormattedDate(entity.getReported_date(), "dd-MM-yyyy"))

									.append("</td></tr>");

							if (approved_by != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By  </td><td class=\"pre-wrap\">"

												+ approved_by + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getApproved_by_remark() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By Remark  </td><td class=\"pre-wrap\">"

												+ entity.getApproved_by_remark() + "</td></tr>");

							} else {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:100px;\">Approved By Remark </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getCurrent_req_status() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\">"

												+ current_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Current Requisition Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getClosure_remark() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\">"

												+ entity.getClosure_remark() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:100px;\">Client Closure Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>"); // end of second card

							// start of third card

							sb.append("<div id=\"accordion1\">");

							sb.append("<div class=\"card\">");

							sb.append("<div class=\"card-header\">");

							sb.append("<div class=\"row\">");

							sb.append("<div class=\"collapseExampletwo\" style=\"margin-left: 15px;\">");

							sb.append(
									"<a class=\"card-link\" data-toggle=\"collapse\" href=\"#collapseThree\">Assign To Developer Team</a>");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append("</div>");

							sb.append("<div class=\"collapseExampletwo\">");

							sb.append(
									"<a class=\"card-link\" data-toggle=\"collapse\" href=\"#collapseFour\">Assign To QA Team</a>");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append(
									"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");

							sb.append("</div>");

							sb.append("<div class=\"collapseExampletwo\">");

							sb.append(
									"<a class=\"card-link\" data-toggle=\"collapse\" href=\"#collapseFive\">Assign To Deploy Team</a>");

							sb.append(" </div>");

							sb.append(" </div>");

							sb.append(" </div>");

							// start of first collapse

							sb.append("<div id=\"collapseThree\" class=\"collapse\" data-parent=\"#accordion1\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (approved_to != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Developer  </td><td class=\"pre-wrap\">"

												+ approved_to + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Developer  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDev_status() != null) {

								dev_status = entity.getDev_status();

								if (dev_status.equalsIgnoreCase("S")) {

									dev_status = "Started";

								} else if (dev_status.equalsIgnoreCase("C")) {

									dev_status = "Closed";

								}

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Status  </td><td class=\"pre-wrap\">"

												+ dev_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDev_remark() != null) {
								String[] r = {};
								String remark = "";
								if (entity.getDev_remark().contains("###")) {
									r = entity.getDev_remark().split("###");
									remark = r[0];
								}

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Remark </td><td class=\"pre-wrap\">"
												+ remark + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDev_close_date() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Close Date  </td><td class=\"pre-wrap\">")

										.append(lhsDateUtility.getFormattedDate(entity.getDev_close_date(),
												"dd-MM-yyyy"))

										.append("</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Developer Close Date  </td><td class=\"pre-wrap\">")

										.append("</td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>"); // end of first collapse

							// start of second collapse

							sb.append("<div id=\"collapseFour\" class=\"collapse\" data-parent=\"#accordion1\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (approved_to_qc != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned to QC  </td><td class=\"pre-wrap\">"

												+ approved_to_qc + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned to QC  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getTesting_status() != null) {

								qc_status = entity.getTesting_status();

								if (qc_status.equalsIgnoreCase("S")) {

									qc_status = "Started";

								} else if (qc_status.equalsIgnoreCase("C")) {

									qc_status = "Closed";

								}

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Status  </td><td class=\"pre-wrap\">"

												+ qc_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getTesting_remark() != null) {

								sb.append(
										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Remark </td><td class=\"pre-wrap\">"

												+ entity.getTesting_remark() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getTesting_closed_date() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">QC Closed Date  </td><td class=\"pre-wrap\">")

										.append(lhsDateUtility.getFormattedDate(entity.getTesting_closed_date(),
												"dd-MM-yyyy"))

										.append("</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:width:170px;\">QC Closed Date  </td><td class=\"pre-wrap\">")

										.append("</td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>"); // end of second collapse

							// start of third collapse

							sb.append("<div id=\"collapseFive\" class=\"collapse\" data-parent=\"#accordion1\">");

							sb.append("<div class=\"card-body\">");

							sb.append("<table  class=\"table details-table\">");

							sb.append("<tbody>");

							if (entity.getAssigned_to_func() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Deploy Team </td><td class=\"pre-wrap\">"

												+ assigned_to_func + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Assigned To Deploy Team  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDeploy_status() != null) {

								deploy_status = entity.getDeploy_status();

								if (deploy_status.equalsIgnoreCase("S")) {

									deploy_status = "Success";

								} else if (deploy_status.equalsIgnoreCase("F")) {

									deploy_status = "Fail";

								} else if (deploy_status.equalsIgnoreCase("R")) {

									deploy_status = "Restart";

								}

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Status </td><td class=\"pre-wrap\">"

												+ deploy_status + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Status  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getFunc_remark() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Remark </td><td class=\"pre-wrap\">"

												+ entity.getFunc_remark() + "</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\"style=\"width:170px;\">Deploy Team Remark  </td><td class=\"pre-wrap\"></td></tr>");

							}

							if (entity.getDeploy_delivered_date() != null) {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Closed Date  </td><td class=\"pre-wrap\">")

										.append(lhsDateUtility.getFormattedDate(entity.getDeploy_delivered_date(),
												"dd-MM-yyyy"))

										.append("</td></tr>");

							} else {

								sb.append(

										"<tr><td class=\"text-bold\" style=\"width:170px;\">Deploy Team Closed Date  </td><td class=\"pre-wrap\">")

										.append("</td></tr>");

							}

							sb.append("</tbody>");

							sb.append("</table>");

							sb.append("</div>");

							sb.append("</div>");

							sb.append("</div>");// end of third collapse

							sb.append("</div>");// accordion1

							// DOWNLOAD BUTTON

							sb.append(

									"<input type=\"hidden\" name=\"req_code\" id=\"reqCode_sb\" value="
											+ entity.getReq_code() + " />");

							sb.append(
									"<input type=\"hidden\"  id=\"req_type_sb\" value=" + entity.getReq_type() + " />");

							sb.append("</div>");// container

						}

					}

				}

			}

			sb.append("</div>");// column

			sb.append("</div>");// rowdiv

			sb.append("</div>");// normal

		} catch (Exception e) {

			e.printStackTrace();

		}

		return sb.toString();

	}

	@Override
	public Map<String, String> getCurrent_status_list() {
		Map<String, String> statusList = new HashMap<>();
		Map<String, String> newStatusList = new HashMap<>();
		try {
			String queryStr = "select m.status_code, m.status_name from  view_current_req_status m where status_flag = 'I'";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				statusList.put((String) obj[0], (String) obj[1]);
			}

			newStatusList = execute.sortTheList(statusList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (newStatusList != null && newStatusList.size() > 0) ? newStatusList : null;
	}

	@Override
	public ReqTran saveOnsiteReqDetail(ReqTran entity) {
		String response = "error";
		ReqTran Entity = new ReqTran();
		try {

			Entity = reqrepo.save(entity);

			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			response = "error";
			e.printStackTrace();
		}

		return Entity;
	}

	@Override
	public void savelogo(ReqTranAttach reqTranAttachEntity) {
		String response = "error";

		try {
			reqTranAttachRepo.save(reqTranAttachEntity);

			response = "success";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

//onsite
	@Override
	public Map<String, String> getAllReqIssueTypeCodeName() {
		// TODO Auto-generated method stub
		Map<String, String> projectApplicationList = new HashMap<>();
		Map<String, String> newprojectApplicationList = new HashMap<>();
		try {
			String queryStr = "select issue_type_code, issue_type_name from view_onsite_req_issue_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectApplicationList.put((String) obj[0], (String) obj[1]);
			}

			newprojectApplicationList = execute.sortTheList(projectApplicationList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (newprojectApplicationList != null && newprojectApplicationList.size() > 0) ? newprojectApplicationList
				: null;
	}

	@Override
	public List<ReqTran> getOnsiteReq() {
		List<ReqTran> reqList = new ArrayList<>();

		String Query = "";
		try {
			Query = "select * from req_tran where req_entered_mode = 'O'";
			System.out.println("query==  >>>>" + Query);

			reqList = executer.executeSQLQuery(Query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// TODO Auto-generated method stub
		return reqList;
	}

	@Override
	public long getOnsiteDetailsCount(FilterDTO filter) {
		// TODO Auto-generated method stub
		return reqTranRepositorySupport.getOnsiteBrowseCount(filter);

	}

	@Override
	public List<ReqTran> getOnsiteList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, long total,
			Map<String, String> viewList) {
		List<ReqTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();

		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {

				long minVal = paginatorEntity.getMinVal();

				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();

				list = reqTranRepositorySupport.getDocTranBrowseList(filter, minVal, maxVal, viewList);

			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return list;
	}

	public String getReqCode(String Code) {
		String code = "";
		try {
			code = viewRepo.getCode(Code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	@Override
	public String viewOnsiteReq(String req_code) {

		StringBuilder sb = new StringBuilder();
		String ReqType = "";
		String User_code = "";
		String current_status = "";
		String issueType = "";
		try {

			ReqTran entity = getReqDetailbyCode(req_code);
			System.out.println("entity.." + entity);
			ReqType = getReqCode(entity.getReq_type());
			User_code = getUserName(entity.getReported_by());
			current_status = getCurrentStatus(entity.getCurrent_req_status());
			issueType = entity.getReq_type();
			System.out.println("issueType===" + issueType);
			sb.append("<div>");

			sb.append("<div class=\"row\">");

			sb.append("<div class=\"col-md-12\">");

			if (entity != null) {
				sb.append("<div class=\"container\">");

				// start of first card

				// sb.append("<div class=\"card\">"); // first card

				// sb.append("<div class=\"card-header\">");

				// sb.append("<a class=\"card-link\" href=\"#collapseOne\">Onsite Information
				// </a>");
				sb.append(" </a>");
				sb.append(" </div>");

				// sb.append("<div id=\"collapseOne\" class=\"collapse show\">");

				// sb.append("<div class=\"card-body\">");

				sb.append("<table  class=\"table details-table\">");

			if (entity.getSample_data_filter_str()!= null ) {
			sb.append("<tr><td class=\"text-bold\">Info About Issues</td><td class=\"pre-wrap\" style=\"white-space:pre-wrap;\">"+entity.getSample_data_filter_str()+"</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Info About Issues</td><td class=\"pre-wrap\"></td></tr>");
			}
			
			if (entity.getDeploy_delivered_date()!= null ) {
				sb.append("<tr><td class=\"text-bold\">Target Date</td><td class=\"pre-wrap\">")
				.append(lhsDateUtility.getFormattedDate(entity.getDeploy_delivered_date(), "dd-MM-yyyy "))
				.append("</td></tr>");
				}else {
				sb.append("<tr><td class=\"text-bold\">Target Date</td><td class=\"pre-wrap\"></td></tr>");
				}
			if (entity.getSample_data_filter_str()!= null ) {
			sb.append("<tr><td class=\"text-bold\">Info About Issues</td><td class=\"pre-wrap\" style=\"white-space:pre-wrap;\">"+entity.getSample_data_filter_str()+"</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Info About Issues</td><td class=\"pre-wrap\"></td></tr>");
			}
			
			if (entity.getDeploy_delivered_date()!= null ) {
				sb.append("<tr><td class=\"text-bold\">Target Date</td><td class=\"pre-wrap\">")
				.append(lhsDateUtility.getFormattedDate(entity.getDeploy_delivered_date(), "dd-MM-yyyy HH:mm:ss.SS"))
				.append("</td></tr>");
				}else {
				sb.append("<tr><td class=\"text-bold\">Target Date</td><td class=\"pre-wrap\"></td></tr>");
				}
				sb.append("<tbody>");

				sb.append("<tr>");

				if (entity.getReq_code() != null) {
					sb.append("<td class=\"text-bold\">Req. No.</td><td class=\"pre-wrap\" >" + entity.getReq_code()
							+ "</td>");
				} else {
					sb.append("<td class=\"text-bold\">Req. No.</td><td class=\"pre-wrap\"></td>");
				}

				sb.append("</tr>");

				if (entity.getReq_type() != null) {
					sb.append("<tr><td class=\"text-bold\">Type Of Issues</td><td class=\"pre-wrap\" >" + ReqType
							+ "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Type Of Issues</td><td class=\"pre-wrap\"></td></tr>");
				}

				if (entity.getReq_type().equalsIgnoreCase("isstype_01")) {
					sb.append("<tr><td class=\"text-bold\">Application name </td><td class=\"pre-wrap\">"
							+ entity.getProject_name() + "</td></tr>");
				} else if (entity.getReq_type().equalsIgnoreCase("isstype_02")) {
					sb.append("<tr><td class=\"text-bold\">Jar Name</td><td class=\"pre-wrap\">"
							+ entity.getProject_name() + "</td></tr>");
				} else if (entity.getReq_type().equalsIgnoreCase("isstype_03")) {
					sb.append("<tr><td class=\"text-bold\">Deployment</td><td class=\"pre-wrap\">"
							+ entity.getProject_name() + "</td></tr>");
				} else if (entity.getReq_type().equalsIgnoreCase("isstype_04")) {
					sb.append("<tr><td class=\"text-bold\">Encryption</td><td class=\"pre-wrap\">"
							+ entity.getProject_name() + "</td></tr>");
				}

				if (entity.getCurrent_req_status() != null) {
					sb.append("<tr><td class=\"text-bold\">Current Status</td><td class=\"pre-wrap\">" + current_status
							+ "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Current Status</td><td class=\"pre-wrap\" ></td></tr>");

				}

				if (User_code != null) {
					sb.append("<tr><td class=\"text-bold\">Reported  By </td><td class=\"pre-wrap\">" + User_code
							+ "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Reported By </td><td class=\"pre-wrap\"></td></tr>");
				}

				if (entity.getReported_date() != null) {
					sb.append("<tr><td class=\"text-bold\">Reported Date</td><td class=\"pre-wrap\">").append(
							lhsDateUtility.getFormattedDate(entity.getReported_date(), "dd-MM-yyyy HH:mm:ss.SS"))
							.append("</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Reported Date </td><td class=\"pre-wrap\"></td></tr>");
				}

				if (entity.getIssue_description() != null) {
					sb.append(
							"<tr><td class=\"text-bold\">Issue Description</td><td class=\"pre-wrap\"  style=\"white-space:pre-wrap;\">"
									+ entity.getIssue_description() + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Issue Description</td><td class=\"pre-wrap\"></td></tr>");
				}

				if (entity.getSample_data_filter_str() != null) {
					sb.append(
							"<tr><td class=\"text-bold\">Info About Issues</td><td class=\"pre-wrap\" style=\"white-space:pre-wrap;\">"
									+ entity.getSample_data_filter_str() + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Info About Issues</td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getDeploy_delivered_date() != null) {
					sb.append("<tr><td class=\"text-bold\">Target Date</td><td class=\"pre-wrap\">")
							.append(lhsDateUtility.getFormattedDate(entity.getDeploy_delivered_date(),
									"dd-MM-yyyy "))
							.append("</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Target Date</td><td class=\"pre-wrap\"></td></tr>");
				}

				if (entity.getClosure_remark() != null) {
					sb.append(
							"<tr><td class=\"text-bold\" >Closed Remark</td><td class=\"pre-wrap\" style=\"white-space:pre-wrap;\">"
									+ entity.getClosure_remark() + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Closed Remark</td><td class=\"pre-wrap\"></td></tr>");
				}

				sb.append("</tbody>");

				sb.append("</table>");

				sb.append("<tr>");
				sb.append("<td>");

				sb.append("<a class=\"card-link\" href=\"#collapsedwnld\">Attachments");

				sb.append("</a>");
				sb.append("</td>");
				sb.append("<td class=\"text-center\">");

				sb.append("<a style='text-align: center; justify-content: center; display: flex;'>");
				sb.append("<button th:type=\"button\" id=" + entity.getReq_code()

						+ " class=\"btn btn-primary addon-btn filter-inner-btn\"\r\n"

						+ "\" onclick=\"downloadOnsiteFile(this.id);\" style=\"float: right; margin-left: 40px;\"><i class=\"fa download\"></i></button>");

				sb.append("</a>");

				// sb.append("</div>");

				sb.append("</td>");
				sb.append("</tr>");

				}
				
			
		}
				

		 catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}

			
			
	private ReqTran getReqDetailbyCode(String req_code) {
		ReqTran entity = new ReqTran();
		try {
			entity = reqrepo.findById(req_code).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String ClosedOnsiteReq(String req_code) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public int getcountOnsiteTable(String req_type,  String deploy_delivered_date,String req_code,
//			String reported_date, String current_req_status) {
//		int count=0;
//		try {
//			
//			String Query="select count(*) from req_tran  where ";
//			
//			if (!req_type.isEmpty()) {
//				Query = Query + " and  lower(req_type)like lower('%" +req_type+ "%')";
//			}
//
//            if (!deploy_delivered_date.isEmpty()) {
//				
//				Query = Query + "and  to_date(deploy_delivered_date) = to_date('" + deploy_delivered_date + "','DD-MM-RRRR')";
//			
//			}
//            
//            if (!req_code.isEmpty()) {
//				Query = Query + " and  lower(req_code)like lower('%" +req_code+ "%')";
//			}
//			
////            if (!reported_date.isEmpty()) {
////				Query = Query + "and  to_date(reported_date1) = to_date('" + reported_date + "','DD-MM-RRRR')";
////			}
//			
//            if (!current_req_status.isEmpty()) {
//            	Query = Query + " and  lower(current_req_status)like lower('%" +current_req_status+ "%')";
//			}
//
//			System.out.println("Query inside count ====="+Query);
//	         count = executer.getRowCount(Query);
//			
//		} catch (Exception e) {
//		e.printStackTrace();	// TODO: handle exception
//		}
//		// TODO Auto-generated method stub
//		return count;
//	}
//
//	@Override
//	public List<ReqTran> getOnsiteListFilter(String req_type,  String deploy_delivered_date,String req_code,
//			String reported_date, String current_req_status) {
//		List<ReqTran> list = new ArrayList<>();
//		String Query="";
//		try {
//			
//	           Query="select * from req_tran  where req_entered_mode = 'O' ";
//			
//	           if (!req_type.isEmpty()) {
//					Query = Query + " and  lower(req_type)like lower('%" +req_type+ "%')";
//				}
//			
//				if (!deploy_delivered_date.isEmpty()) {
//					Query = Query + "and  to_date(deploy_delivered_date) = to_date('" + deploy_delivered_date + "','DD-MM-RRRR')";
//
//				}
//				
//				if (!req_code.isEmpty()) {
//					Query = Query + " and  lower(req_code)like lower('%" +req_code+ "%')";
//				}
//				
////	            if (!reported_date.isEmpty()) {
////					Query = Query + "and  to_date(reported_date) = to_date('" + reported_date + "','DD-MM-RRRR')";
////				}
//				
//	            if (!current_req_status.isEmpty()) {
//	            	Query = Query + " and  lower(current_req_status)like lower('%" +current_req_status+ "%')";
//				}
//				System.out.println("Query inside list ====="+Query);
//	      	list = executer.executeSQLQuery(Query);
//
//			
//		} catch (Exception e) {
//		e.printStackTrace();	// TODO: handle exception
//		}
//		
//		return list;
//	}

	@Override
	public String ClosedOnsiteReq(ReqTran entity1, String req_code, String remark) {
		String response = "error";
		try {
			Optional<ReqTran> entity = this.reqrepo.findById(req_code);
			ReqTran wishentity = entity.get();
			entity.get().setClosure_remark(remark);
			entity.get().setCurrent_req_status("CRS_019");
			entity.get().setLastupdate(entity1.getLastupdate());
			entity1 = reqrepo.saveAndFlush(wishentity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String StartWorkReq(ReqTran entity1, String req_code) {
		String response = "error";
		try {
			Optional<ReqTran> entity = this.reqrepo.findById(req_code);
			ReqTran wishentity = entity.get();
			entity.get().setCurrent_req_status("CRS_020");
			entity.get().setLastupdate(entity1.getLastupdate());
			entity1 = reqrepo.saveAndFlush(wishentity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	@Override
	public Map<String, String> getViewListIssue() {
		Map<String, String> issueList = new HashMap<>();
		try {
			String queryStr = "select issue_type_code, issue_type_name from view_onsite_req_issue_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				issueList.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (issueList != null && issueList.size() > 0) ? issueList : null;
	}

	@Override
	public Map<String, String> getstatusList() {
		Map<String, String> statusList = new HashMap<>();
		try {
			String queryStr = "select status_code, status_name from view_current_req_status  where status_flag = 'O'";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				statusList.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (statusList != null && statusList.size() > 0) ? statusList : null;
	}

	@Override
	public int getcountOnsiteTable(String req_type, String req_code, String current_req_status, String from_date,
			String to_date, String from_date_new, String to_date_new, FilterDTO filter) {
		int count = 0;
		try {

			String Query = "select count(*) from req_tran  where req_entered_mode = 'O'";

			if (!req_type.isEmpty()) {
				Query = Query + " and  lower(req_type)like lower('%" + req_type + "%')";
			}

			if (filter.getFrom_date1() != null || filter.getTo_date1() != null) {
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					Query = Query + "and  deploy_delivered_date BETWEEN to_date('" + filter.getFrom_date1()
							+ "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')";

				}
				if (!filter.getFrom_date1().isEmpty() && filter.getTo_date1().isEmpty()) {
					Query = Query + ("and to_date(deploy_delivered_date,'DD-MM-RRRR') = to_date('"
							+ filter.getFrom_date1() + "','DD-MM-RRRR')");

				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					Query = Query + ("and to_date(deploy_delivered_date,'DD-MM-RRRR') = to_date('"
							+ filter.getTo_date1() + "','DD-MM-RRRR')");
				}
			}

			if (!req_code.isEmpty()) {
				Query = Query + " and  lower(req_code)like lower('%" + req_code + "%')";
			}

//            if (!reported_date.isEmpty()) {
//				Query = Query + "and  to_date3(reported_date) = to_date3('" + reported_date + "','DD-MM-RRRR')";
//			}

			if (filter.getFrom_date2() != null || filter.getTo_date2() != null) {
				if (!filter.getFrom_date2().isEmpty() && !filter.getTo_date2().isEmpty()) {
					Query = Query + "reported_date BETWEEN to_date('" + filter.getFrom_date2()
							+ "','DD-MM-RRRR') AND to_date('" + filter.getTo_date2() + "','DD-MM-RRRR')";

				}
				if (!filter.getFrom_date2().isEmpty() && filter.getTo_date2().isEmpty()) {
					Query = Query + (" to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date2()
							+ "','DD-MM-RRRR')");

				}
				if (!filter.getTo_date2().isEmpty() && filter.getFrom_date2().isEmpty()) {
					Query = Query + (" to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date2()
							+ "','DD-MM-RRRR')");
				}
			}

			if (!current_req_status.isEmpty()) {
				Query = Query + " and  lower(current_req_status)like lower('%" + current_req_status + "%')";
			}

			System.out.println("Query inside count =====" + Query);
			count = executer.getRowCount(Query);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public List<ReqTran> getOnsiteListFilter(String req_type, String req_code, String current_req_status,
			String from_date, String to_date, String from_date_new, String to_date_new, FilterDTO filter) {
		List<ReqTran> list = new ArrayList<>();
		String Query = "";
		try {

			Query = "select * from req_tran  where req_entered_mode = 'O' ";

			if (!req_type.isEmpty()) {
				Query = Query + " and  lower(req_type)like lower('%" + req_type + "%')";
			}

			if (!from_date.isEmpty() && !to_date.isEmpty()) {
				Query = Query + "  and deploy_delivered_date BETWEEN to_date('" + from_date
						+ "','DD-MM-RRRR') AND to_date('" + to_date + "','DD-MM-RRRR')";
			}
			if (!from_date.isEmpty() && to_date.isEmpty()) {
				Query = Query + " and to_date(deploy_delivered_date,'DD-MM-RRRR') = to_date('" + from_date
						+ "','DD-MM-RRRR')";
			}
			if (!to_date.isEmpty() && from_date.isEmpty()) {
				Query = Query + " and to_date(deploy_delivered_date,'DD-MM-RRRR') = to_date('" + to_date
						+ "','DD-MM-RRRR')";
			}

			if (!req_code.isEmpty()) {
				Query = Query + " and  lower(req_code)like lower('%" + req_code + "%')";
			}

			if (!from_date_new.isEmpty() && !to_date_new.isEmpty()) {
				Query = Query + " reported_date BETWEEN to_date('" + from_date_new + "','DD-MM-RRRR') AND to_date('"
						+ to_date_new + "','DD-MM-RRRR')";
			}
			if (!from_date_new.isEmpty() && to_date_new.isEmpty()) {
				Query = Query + " to_date(reported_date,'DD-MM-RRRR') = to_date('" + from_date_new + "','DD-MM-RRRR')";
			}
			if (!to_date_new.isEmpty() && from_date_new.isEmpty()) {
				Query = Query + " to_date(reported_date,'DD-MM-RRRR') = to_date('" + to_date_new + "','DD-MM-RRRR')";
			}

			if (!current_req_status.isEmpty()) {
				Query = Query + " and  lower(current_req_status)like lower('%" + current_req_status + "%')";
			}
			System.out.println("Query inside list =====" + Query);
			list = executer.executeSQLQuery(Query);

		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}

		return list;
	}

}
