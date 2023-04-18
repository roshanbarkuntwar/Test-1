package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.service.UserRoleMastService;


@Service
@Transactional

public class ReqTranRepositorySupport {

	@Autowired
	ReqTranRepository reqTranRepository;
	@Autowired
	private LhsStringUtility strUtl;
	@Autowired
	UserRoleMastService userRoleMastService;

	public List<ReqTran> getReqEntryDashboardList(FilterDTO filter, long minVal, long maxVal) {
		List<ReqTran> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter.getReq_code());
			if (filter != null) {

				if(!filter.getReq_code().isEmpty() && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and v.req_code='"+filter.getReq_code()+"'");

				}

				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and v.reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
				if(filter.getDev_status() != null && !filter.getDev_status().equalsIgnoreCase("")) {
					sb.append(" and dev_status='"+filter.getDev_status()+"'");
				}
				if(filter.getCurrent_req_status() != null && !filter.getCurrent_req_status().equalsIgnoreCase("")) {
					System.out.println("filter.getCurrent_req_status()"+filter.getCurrent_req_status());
					sb.append("  and v.current_req_status='"+filter.getCurrent_req_status()+"'");
				}
		     }
			System.out.println("sb..."+sb);
//			String queryString = "from ReqTran t where 1=1 "+sb ;
			String queryString = "select v.req_code,\r\n" 
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "   	  v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n"
					+ " 	  v.issue_type,\r\n" 
					+ "   	  v.issue_description,\r\n" 
					+ "       v.req_priority,\r\n" 
					+ "       v.parent_req_code,\r\n"
					+ "       v.req_error_group_str,\r\n" 
					+ "   	  v.sample_data_filter_str,\r\n"
					+ " 	  v.approved_by,\r\n" 
					+ "       v.approved_by_status,\r\n" 
					+ "  	  v.approved_by_remark,\r\n" 
					+ "       v.assigned_to_dev,\r\n" 
					+ " 	  v.assigned_dev_date,\r\n"  
					+ "		  v.dev_status,\r\n" 
					+ "       v.dev_remark,\r\n"
					+ "		  v.dev_close_date,\r\n"
					+ "    	  v.assigned_to_qc,\r\n"
					+ "       v.assigned_qc_date,\r\n" 
					+ "  	  v.testing_status,\r\n" 
					+ "  	  v.testing_remark,\r\n" 
					+ "       v.testing_closed_date,\r\n"
					+ "		  v.deploy_delivered_by,\r\n" 
					+ "       v.deploy_delivered_date,\r\n" 
					+ "  	  v.closure_remark,\r\n" 
					+ "       v.closure_date,\r\n" 
					+ "		  v.req_closed_by,\r\n"
					+ "		  v.lastupdate,\r\n" 
					+ "       v.flag,\r\n" 
					+ "    	  (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status, \r\n"
					+ "		  v.final_status , \r\n"
					+ "		  v.reopened_status , \r\n"
					+ "		  v.deploy_status , \r\n"
					+ "		  v.deploy_type , \r\n"
					+ " 	  v.REINITIATE_WORK_ASSIGNED_TO,\r\n"
					+ " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n"
					+ " 	  v.func_remark,\r\n"
					+ " 	  v.func_status,\r\n"
					+ " 	  v.REINITIATE_PM_WORK_FLAG,\r\n"
					+ " 	  v.REINITIATE_PM_WORK_REMARK,\r\n"
					+ " 	  v.client_final_status,"
					+ " 	  v.req_entered_mode,"
					+ " 	  v.reopened_dev_name,"
					+ "		v.reopened_remark,"
					+"		  v.deploy_fail_req_assigned_to	"
					+ "       from REQ_TRAN v  where 1=1 "+sb+""
					+ "		  and req_entered_mode != 'O'order by v.reported_date desc\r\n"  ;
			
			
//			 queryString = queryString+"from ReqTran t where 1=1 "+sb ;
			System.out.println("Get Detail list Query=11==== "+queryString);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<ReqTran> query = reqTranRepository.getSession().createNativeQuery(queryString, ReqTran.class);
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<ReqTran>) query.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ReqTran> getReqEntryDashboardListForClient(FilterDTO filter, long minVal, long maxVal) {
		List<ReqTran> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(!filter.getReq_code().isEmpty() && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and v.req_code='"+filter.getReq_code()+"'");
				}

				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and v.reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
				if(filter.getDev_status() != null && !filter.getDev_status().equalsIgnoreCase("")) {
					sb.append(" and dev_status='"+filter.getDev_status()+"'");
				}
				if(filter.getCurrent_req_status() != null && !filter.getCurrent_req_status().equalsIgnoreCase("")) {
					System.out.println("filter.getCurrent_req_status()"+filter.getCurrent_req_status());
					if(filter.getCurrent_req_status().equalsIgnoreCase("Work In Progress")) {
						sb.append("and ( v.current_req_status = 'CRS_010' or v.current_req_status='CRS_002' or v.current_req_status='CRS_017'or v.current_req_status='CRS_014')");
					}else {
					sb.append("  and v.current_req_status='"+filter.getCurrent_req_status()+"'");
					}
				}
		     }
			System.out.println("sb..."+sb);
//			String queryString = "from ReqTran t where 1=1 "+sb ;
			String queryString = "select v.req_code,\r\n" 
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "   	  v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n"
					+ " 	  v.issue_type,\r\n" 
					+ "   	  v.issue_description,\r\n" 
					+ "       v.req_priority,\r\n" 
					+ "       v.parent_req_code,\r\n"
					+ "       v.req_error_group_str,\r\n" 
					+ "   	  v.sample_data_filter_str,\r\n"
					+ " 	  v.approved_by,\r\n" 
					+ "       v.approved_by_status,\r\n" 
					+ "  	  v.approved_by_remark,\r\n" 
					+ "       v.assigned_to_dev,\r\n" 
					+ " 	  v.assigned_dev_date,\r\n"  
					+ "		  v.dev_status,\r\n" 
					+ "       v.dev_remark,\r\n"
					+ "		  v.dev_close_date,\r\n"
					+ "    	  v.assigned_to_qc,\r\n"
					+ "       v.assigned_qc_date,\r\n" 
					+ "  	  v.testing_status,\r\n" 
					+ "  	  v.testing_remark,\r\n" 
					+ "       v.testing_closed_date,\r\n"
					+ "		  v.deploy_delivered_by,\r\n" 
					+ "       v.deploy_delivered_date,\r\n" 
					+ "  	  v.closure_remark,\r\n" 
					+ "       v.closure_date,\r\n" 
					+ "		  v.req_closed_by,\r\n"
					+ "		  v.lastupdate,\r\n" 
					+ "       v.flag,\r\n" 
					+ "    	  (select h.status_name from  view_current_req_status h where h.status_code= v.current_req_status)current_req_status, \r\n"
					+ "		  v.final_status , \r\n"
					+ "		  v.reopened_status , \r\n"
					+ "		  v.deploy_status , \r\n"
					+ "		  v.deploy_type , \r\n"
					+ " 	  v.REINITIATE_WORK_ASSIGNED_TO,\r\n"
					+ " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n"
					+ " 	  v.func_remark,\r\n"
					+ " 	  v.func_status,\r\n"
					+ " 	  v.REINITIATE_PM_WORK_FLAG,\r\n"
					+ " 	  v.REINITIATE_PM_WORK_REMARK,\r\n"
					+ " 	  v.client_final_status,"
					+ " 	  v.req_entered_mode,"
					+ " 	  v.reopened_dev_name,"
					+ " 	  v.reopened_remark,"
					+"		  v.deploy_fail_req_assigned_to	"
					
					+ "       from REQ_TRAN v  where 1=1 and v.req_entered_mode='E' "+sb+""
					
					+ "		  order by v.reported_date desc\r\n"  ;
			
			
//			 queryString = queryString+"from ReqTran t where 1=1 "+sb ;
			System.out.println("Get Detail list Query=11== "+queryString);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<ReqTran> query = reqTranRepository.getSession().createNativeQuery(queryString, ReqTran.class);
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<ReqTran>) query.getResultList();
			System.out.println("list..."+list);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ReqTran> getReqApprovalBrowseList(FilterDTO filter, long minVal, long maxVal, String role_code, String dept_str) {
		List<ReqTran> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
//			System.out.println("filter 1====="+filter);
			
			if (filter != null) {
				if(!filter.getReq_code().isEmpty() && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and v.req_code='"+filter.getReq_code()+"'");
				}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}			
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and v.reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
//				 System.out.println("filter insider..."+filter);
		     }
			
//			String queryString = "from ReqTran t where 1=1 "+sb ;
			String queryString =  "select v.req_code,\r\n" + "  "
					+ " 	(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "    v.reported_date,\r\n"
					+ "       (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       (select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       (select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       v.req_type,\r\n" + "     " 
					+ "  v.issue_type,\r\n" + "    " 
					+ "   v.issue_description,\r\n"
					+ "       v.req_priority,\r\n" 
					+ "       v.parent_req_code,\r\n" 
					+ "       v.req_error_group_str,\r\n"
					+ "   " + "    v.sample_data_filter_str,\r\n" 
					+ "       v.reinitiate_pm_work_flag,\r\n" + "    "
					+ "   v.reinitiate_pm_work_remark, v.reinitiate_work_assigned_to,\r\n" + "    " 
					+ "   v.approved_by,\r\n" 
					+ "       v.approved_by_status,\r\n"
					+ "    " + "   v.approved_by_remark,\r\n" 
					+ "       v.assigned_to_dev,\r\n" + "     "
					+ "  v.assigned_dev_date,\r\n" + "    " 
					+ "   v.dev_status,\r\n" 
					+ "       v.dev_remark,\r\n" + "     "
					+ "  v.dev_close_date,\r\n" + "   " 
					+ "    		v.assigned_to_qc,\r\n" 
					+ "       v.assigned_qc_date,\r\n"
					+ "       v.testing_status,\r\n" 
					+ "       v.testing_remark,\r\n"
					+ "       v.testing_closed_date,\r\n" + "     " 
					+ " 	  v.deploy_delivered_by,\r\n"
					+ "       v.deploy_delivered_date,\r\n" + "   " 
					+ "       v.closure_remark,\r\n"
					+ "       v.closure_date,\r\n" + "     " 
					+ " 	  v.req_closed_by,\r\n" + "     " 
					+ "  	  v.lastupdate,\r\n"
					+ "       v.flag,\r\n" + "    " 
					+ " 	  (select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status ,\r\n"
					+ "		  v.final_status , \r\n"
					+ "		  v.reopened_status , \r\n"
					+ "		  v.deploy_status , \r\n"
					+ "		  v.deploy_type , \r\n"
					+ "		  v.deploy_fail_req_assigned_to	,"
					+ " 	  v.assigned_to_func,\r\n"
					+ " 	  v.assigned_func_date,\r\n"
					+ " 	  v.func_remark,\r\n"
					+ " 	  v.func_status,\r\n"
					+ " 	  v.req_entered_mode,"
					+ " 	  v.reopened_dev_name,"+ " 	  v.reopened_remark,"
					+ " 	  v.client_final_status"
					+ "     from REQ_TRAN v where 1=1 "+sb+"";
			
//		//////	
			if (!strUtl.isNull(role_code) && role_code.startsWith("MAN-") && role_code.startsWith("ADM-")) {
//				reqList = reqrepo.findByIssueType(dept_str);
				System.out.println("inside if condition...");
				queryString = queryString 
						+ "' and  req_entered_mode != 'O' and  current_req_status <> 'CRS_004' and v.approved_by_status is null order by v.reported_date desc";
			} else {
				System.out.println("inside else condition...");
//				reqList = reqrepo.findAllRequisition();
				queryString = queryString + " and req_entered_mode != 'O'  and v.approved_by_status is null order by v.reported_date desc";
			}
			
			System.out.println("Get Detail list Query== "+queryString);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<ReqTran> query = reqTranRepository.getSession().createNativeQuery(queryString, ReqTran.class);
			
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
//			System.out.println("query...11.."+query);
			list = (List<ReqTran>) query.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ReqTran> getReqAssignedBrowseList(FilterDTO filter, long minVal, long maxVal, String role_code, String user_code , String dept_str) {
		List<ReqTran> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
//			System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(!filter.getReq_code().isEmpty() && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and v.req_code='"+filter.getReq_code()+"'");
				}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}
				
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and v.reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
				if(filter.getDev_status() != null && !filter.getDev_status().equalsIgnoreCase("")) {
					System.out.println("status=="+filter.getDev_status());
					sb.append(" and dev_status='"+filter.getDev_status()+"'");
				}
		     }
//			String queryString = "from ReqTran t where 1=1 "+sb ;
			String queryString =  "select v.req_code,\r\n" 
					+ " 		(select i.user_name from user_mast i where v.reported_by =  i.user_code) reported_by,\r\n"
					+ "  	  	v.reported_date,\r\n"
					+ "       	(select j.project_name  from project_mast j where j.project_code = v.project_name) project_name,\r\n"
					+ "       	(select  k.module_name from project_module_mast k where k.module_code = v.module_name) module_name,\r\n"
					+ "       	(select  m.menu_name from project_menu_mast m where m.menu_code = v.menu_name) menu_name,\r\n"
					+ "       	v.req_type,\r\n" 
					+ "  		v.issue_type,\r\n" 
					+ "   		v.issue_description,\r\n"
					+ "       	v.req_priority,\r\n" 
					+ "       	v.parent_req_code,\r\n" 
					+ "       	v.req_error_group_str,\r\n"
					+ "    		v.sample_data_filter_str,\r\n" 
					+ "       	v.REINITIATE_PM_WORK_FLAG,\r\n" 
					+ "   		v.REINITIATE_PM_WORK_REMARK,\r\n" 
					+ "			(select n.user_name  from user_mast n where n.user_code = v.approved_by) approved_by,\r\n"
					+ "       	v.approved_by_status,\r\n" 
					+ "   		v.approved_by_remark,\r\n"
					+ "       	v.assigned_to_dev,\r\n" 
					+ "  		v.assigned_dev_date,\r\n" 
					+ "   		v.dev_status,\r\n" 
					+ "       	v.dev_remark,\r\n" 
					+ "  		v.dev_close_date,\r\n" 
					+ "    		v.assigned_to_qc,\r\n" 
					+ "       	v.assigned_qc_date,\r\n" 
					+ "  		v.testing_status,\r\n"
					+ "			v.testing_remark,\r\n" 
					+ "       	v.testing_closed_date,\r\n" 
					+ "  		v.deploy_delivered_by,\r\n" 
					+ "       	v.deploy_delivered_date,\r\n" 
					+ "    		v.closure_remark,\r\n" 
					+ "       	v.closure_date,\r\n"  
					+ "  		v.req_closed_by,\r\n"
					+ "  		v.lastupdate,\r\n" 
					+ "       	v.flag,\r\n" 
					+ "   		(select m.status_name from  view_current_req_status m where m.status_code= v.current_req_status)current_req_status ,\r\n"
					+ "		  	v.final_status , \r\n"
					+ "		  	v.reopened_status , \r\n"
					+ "		  	v.deploy_status , \r\n"
					+ "		  	v.deploy_type , \r\n"
					+ " 	 	v.REINITIATE_WORK_ASSIGNED_TO, \r\n"
					+ " 	    v.assigned_to_func,\r\n"
					+ " 	    v.assigned_func_date,\r\n"
					+ " 	    v.func_remark,\r\n"
					+ " 	    v.func_status,\r\n"
					+ "		    v.deploy_fail_req_assigned_to	,"
					+ " 	    v.req_entered_mode,"
					+ " 	  	v.client_final_status,"
					+ " 	  	v.reopened_dev_name,"
					+ " 	  v.reopened_remark"
					+ "     	from REQ_TRAN v  where 1=1 "+sb+"";
			
			String role_name = userRoleMastService.getRoleNameByRoleCode(role_code);
//			System.out.println("inside getReqAssignedBrowseList.. user_code = "+user_code);
			
			if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("QUALITY ANALYST")) {
//				reqList = reqrepo.findByAssignedToQc(user_code);
				queryString = queryString + " and v.assigned_to_qc ='" + user_code + "' order by v.assigned_qc_date desc";
//				queryString = queryString + "where v.assigned_to_qc ='" + user_code + "'"+"and v.issue_type = '"+dept_str+"' order by v.reported_date desc";
			} else if (!strUtl.isNull(role_name) && role_name.equalsIgnoreCase("CONSULTANT")) {
//				reqList = reqrepo.findByAssignedToQc(user_code);
				queryString = queryString + " and v.assigned_to_func ='" + user_code + "' and v.testing_status='C' order by v.assigned_func_date desc";
//				queryString = queryString + "where v.assigned_to_qc ='" + user_code + "'"+"and v.issue_type = '"+dept_str+"' order by v.reported_date desc";
			}
			else if (!strUtl.isNull(role_name) && role_name.contains("DEVELOPER")) {
//				reqList = reqrepo.findByAssignedToQc(user_code);
				queryString = queryString + " and (v.assigned_to_dev ='" + user_code + "' or reopened_dev_name='"+user_code+"') order by v.assigned_dev_date desc";
//				queryString = queryString + "where v.assigned_to_qc ='" + user_code + "'"+"and v.issue_type = '"+dept_str+"' order by v.reported_date desc";
			}
			else {
//				reqList = reqrepo.findByAssignedToDev(user_code);
				

				queryString = queryString + "and (v.assigned_to_dev ='" + user_code + "' or v.reinitiate_work_assigned_to='"+user_code+"'  or v.deploy_fail_req_assigned_to = '"+user_code+"')order by v.assigned_dev_date desc";

//				queryString = queryString + "where v.assigned_to_dev ='" + user_code + "'"+"and v.issue_type = '"+dept_str+"' order by v.reported_date desc";
			}
		
//			queryString = queryString+"and v.issue_type = '"+dept_str+"' order by v.reported_date desc";
			System.out.println("Get Detail list Query==.. "+queryString);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<ReqTran> query = reqTranRepository.getSession().createNativeQuery(queryString, ReqTran.class);
			
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
//			System.out.println("query..."+query);
			list = (List<ReqTran>) query.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Long getUserTranBrowseCount(FilterDTO filter , String type){
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("getUserTranBrowseCount filter 2====="+filter);
			if (filter != null) {
				if(filter.getReq_code() != null && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and req_code='"+filter.getReq_code()+"'");
				}
//				if (filter.getTo_date1() != null || filter.getFrom_date1() != null ) {
//					if(!filter.getFrom_date1().isEmpty()) {
//						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
//					}else if(!filter.getTo_date1().isEmpty()){
//						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
//					}
//				}
//				if (filter.getTo_date1() != null && filter.getFrom_date1() == null) {
//					sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
//				}
				if(filter.getFrom_date1() != null || filter.getTo_date1() != null) {
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
			}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
				if(filter.getDev_status() != null && !filter.getDev_status().equalsIgnoreCase("")) {
					sb.append(" and dev_status='"+filter.getDev_status()+"'");
				}
				if(filter.getCurrent_req_status() != null && !filter.getCurrent_req_status().equalsIgnoreCase("")) {
					sb.append(" and current_req_status='"+filter.getCurrent_req_status()+"'");
				}
				
		     }
			if(type.equalsIgnoreCase("Approval")) {
				sb.append("and approved_by_status is null");
			}
			String queryString = "select count(t) from ReqTran t where 1=1  and req_entered_mode != 'O' "+sb ;
			System.out.println("Count Query==1234== "+queryString);
			Query query = reqTranRepository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	System.out.println("count.."+count);
		return count;
	}
	
	
	public Long getUserTranBrowseCountForClient(FilterDTO filter , String type){
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("getUserTranBrowseCount filter 2====="+filter);
			if (filter != null) {
				if(filter.getReq_code() != null && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and req_code='"+filter.getReq_code()+"'");
				}
//				if (filter.getTo_date1() != null || filter.getFrom_date1() != null ) {
//					if(!filter.getFrom_date1().isEmpty()) {
//						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
//					}else if(!filter.getTo_date1().isEmpty()){
//						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
//					}
//				}
//				if (filter.getTo_date1() != null && filter.getFrom_date1() == null) {
//					sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
//				}
				if(filter.getFrom_date1() != null || filter.getTo_date1() != null) {
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
			}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
				if(filter.getDev_status() != null && !filter.getDev_status().equalsIgnoreCase("")) {
					sb.append(" and dev_status='"+filter.getDev_status()+"'");
				}
				if(filter.getCurrent_req_status() != null && !filter.getCurrent_req_status().equalsIgnoreCase("")) {
					if(filter.getCurrent_req_status().equalsIgnoreCase("Work In Progress")) {
						sb.append(" and ( t.current_req_status = 'CRS_010' or t.current_req_status='CRS_002' or t.current_req_status='CRS_017'or t.current_req_status='CRS_014')");
					}else {
					sb.append(" and current_req_status='"+filter.getCurrent_req_status()+"'");
					}
				}
		     }
			if(type.equalsIgnoreCase("Approval")) {
				sb.append("and approved_by_status is null");
			}
			String queryString = "select count(t) from ReqTran t where 1=1 and req_entered_mode='E' "+sb ;
			System.out.println("Count Query==1234== "+queryString);
			Query query = reqTranRepository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	System.out.println("count.."+count);
		return count;
	}

	public Long getCountOfAssignedReq(FilterDTO filter,String user_code, String role_Code) {
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("getCountOfAssignedReq filter 2====="+filter);
			if (filter != null) {
				if(filter.getReq_code() != null && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and req_code='"+filter.getReq_code()+"'");
				}
//				if (filter.getTo_date1() != null || filter.getFrom_date1() != null ) {
//					if(!filter.getFrom_date1().isEmpty()) {
//						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
//					}else if(!filter.getTo_date1().isEmpty()){
//						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
//					}
//				}
				if(filter.getFrom_date1() != null || filter.getTo_date1() != null) {
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				}
				if (filter.getReported_date() != null) {
					sb.append( "and to_date(v.reported_date,'DD-MM-RRRR') = to_date('" + filter.getReported_date() + "','DD-MM-RRRR')");
				}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
				if(filter.getDev_status() != null && !filter.getDev_status().equalsIgnoreCase("")) {
					sb.append(" and dev_status='"+filter.getDev_status()+"'");
				}
		     }
			System.out.println("role_Code.."+role_Code);
			if(role_Code.equals("DEV-0004")) {
				sb.append("and (assigned_to_dev='"+user_code+"' or reopened_dev_name='"+user_code+"')");
			}
			if(role_Code.equals("TES-0005")) {
				sb.append(" and assigned_to_qc ='" + user_code + "'");
			}
			if(role_Code.equals("MAN-0003")) {
				sb.append(" and (assigned_to_dev ='"+user_code+"' or REINITIATE_WORK_ASSIGNED_TO ='"+user_code+"')");
			}
			String queryString = "select count(*) from ReqTran  where 1=1 "+sb;
			System.out.println("getCountOfAssignedReq== "+queryString);
			
			Query query = reqTranRepository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
			System.out.println("count.."+count);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public long getCountOfApprovedReq(FilterDTO filter,String user_code) {
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("getCountOfAssignedReq filter 2====="+filter);
			if (filter != null) {
				if(filter.getReq_code() != null && !filter.getReq_code().equalsIgnoreCase("")) {
					sb.append(" and req_code='"+filter.getReq_code()+"'");
				}
				if (filter.getTo_date1() != null || filter.getFrom_date1() != null ) {
					if(!filter.getFrom_date1().isEmpty()) {
						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					}else if(!filter.getTo_date1().isEmpty()){
						sb.append( "and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					}
				}
				
				if(filter.getFrom_date1() != null || filter.getTo_date1() != null) {
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and reported_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				}
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_name='"+filter.getProject_name()+"'");
				}
				if(filter.getModule_name() != null && !filter.getModule_name().equalsIgnoreCase("")) {
					sb.append(" and module_name='"+filter.getModule_name()+"'");
				}
				if(filter.getMenu_name() != null && !filter.getMenu_name().equalsIgnoreCase("")) {
					sb.append(" and menu_name='"+filter.getMenu_name()+"'");
				}
				if(filter.getIssue_type() != null && !filter.getIssue_type().equalsIgnoreCase("")) {
					sb.append(" and issue_type='"+filter.getIssue_type()+"'");					
				}
				if(filter.getReq_priority() != null && !filter.getReq_priority().equalsIgnoreCase("")) {
					sb.append(" and req_priority='"+filter.getReq_priority()+"'");
				}
				if(filter.getReq_type() != null && !filter.getReq_type().equalsIgnoreCase("")) {
					sb.append(" and req_type='"+filter.getReq_type()+"'");
				}
				if(filter.getDev_status() != null && !filter.getDev_status().equalsIgnoreCase("")) {
					sb.append(" and dev_status='"+filter.getDev_status()+"'");
				}
		     }
			String queryString = "select count(t) from ReqTran t where 1=1 "+sb +"and current_req_status <> 'CRS_004' and approved_by_status is null";
//			System.out.println("Count Query== "+queryString);
			Query query = reqTranRepository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
		
	}

	
	//onsite
	public long getOnsiteBrowseCount(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb = new StringBuilder();
		try {
			if (filter != null) {

				if (filter.getReq_type()!= null && !filter.getReq_type().equalsIgnoreCase("")) {
					System.out.println("Count issue Query== " + filter.getIssue_type());
					sb.append(" and req_type = '" + filter.getReq_type() + "' ");
				}
				
			


				
				if (filter.getReq_code()!= null && !filter.getReq_code().equalsIgnoreCase("")) {
				System.out.println("Count Query== " + filter.getReq_code());
				sb.append(" and req_code='" + filter.getReq_code() + "'");

			   }
				
	
				if (filter.getCurrent_req_status()!= null && !filter.getCurrent_req_status().equalsIgnoreCase("")) {
					System.out.println("Count Query== " + filter.getCurrent_req_status());
					sb.append(" and current_req_status='" + filter.getCurrent_req_status() + "'");
				   }

			}

			String queryString = "select count(t) from  ReqTran t where req_entered_mode = 'O' "+sb;
			System.out.println("Count Query==" + queryString);
			
			Query query = reqTranRepository.getSession().createQuery(queryString);

			count = (Long) query.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}

	public List<ReqTran> getDocTranBrowseList(FilterDTO filter, long minVal, long maxVal,Map<String, String> viewList) {
		List<ReqTran> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {
			System.out.println("filter 1=====" + filter.toString());
			if (filter != null) {

				if (filter.getReq_type()!= null && !filter.getReq_type().equalsIgnoreCase("")) {
					System.out.println("Count issue Query== " + filter.getIssue_type());
					sb.append(" and req_type = '" + filter.getReq_type() + "' ");
				}
	
				
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and deploy_delivered_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(deploy_delivered_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(deploy_delivered_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				
				if (filter.getReq_code1()!= null && !filter.getReq_code1().equalsIgnoreCase("")) {
					System.out.println("Count Query== " + filter.getReq_code1());
					sb.append(" and req_code='" + filter.getReq_code1() + "'");

				   }
					

					if (filter.getCurrent_req_status()!= null && !filter.getCurrent_req_status().equalsIgnoreCase("")) {
						System.out.println("Count Query== " + filter.getCurrent_req_status());
						sb.append(" and current_req_status='" + filter.getCurrent_req_status() + "'");

					   }
				
					if (!filter.getFrom_date2().isEmpty() && !filter.getTo_date2().isEmpty()) {
						sb.append("and reported_date BETWEEN to_date('" + filter.getFrom_date2() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date2() + "','DD-MM-RRRR')");
						
					}
					if (!filter.getFrom_date2().isEmpty()&& filter.getTo_date2().isEmpty()) {
						sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date2() + "','DD-MM-RRRR')");
						
					}
					if (!filter.getTo_date2().isEmpty() && filter.getFrom_date2().isEmpty()) {
						sb.append("and to_date(reported_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date2() + "','DD-MM-RRRR')");
					}


			}
			String queryString = "from ReqTran t where req_entered_mode = 'O' "+sb+"order by reported_date desc";
			
              System.out.println("queryString==="+queryString);
              

			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);

			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			
			
			list = (List<ReqTran>) query.getResultList();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}
}

