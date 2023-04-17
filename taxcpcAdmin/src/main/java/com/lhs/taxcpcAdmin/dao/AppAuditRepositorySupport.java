package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;

@Service
@Transactional
public class AppAuditRepositorySupport {

	@Autowired
	AppAuditRepository appAuditRepository;
	

	public long getCountOfBankAudit(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 2====="+filter);
			if (filter != null) {
				
				if (filter.getEntity_code() != null && !filter.getEntity_code().equalsIgnoreCase("")) {
					sb.append(" and entity_code='" + filter.getEntity_code() + "'");
				}
				if (filter.getAudit_type() != null && !filter.getAudit_type().equalsIgnoreCase("")) {
					if (filter.getAudit_type().equalsIgnoreCase("ALL")) {

					} else {
						sb.append(" and t.audit_type='" + filter.getAudit_type() + "'");
					}
				}
//				if(filter.getSearch_value() != null && !filter.getSearch_value().equalsIgnoreCase("")) {
//					sb.append(" and lower (t.entity_code) || lower (t.audit_type) || lower (t.audit_name) || lower (t.audit_resolve_status) like lower ('%"+filter.getSearch_value()+"%')");
//					
//				}
				if(filter.getProject_code() != null && !filter.getProject_code().equalsIgnoreCase("")) {
					sb.append(" and audit_applied_app_name='"+ filter.getProject_code() +"'");
				}
		     }
			String queryString = "select count(t) from LhssysTaxcpcBankAuditMast t where 1=1 "+sb ;
			System.out.println("Count Query== "+queryString);
			Query query = appAuditRepository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	 
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	System.out.println("count.."+count);
		return count;
	}

	public List<LhssysTaxcpcBankAuditMast> getAppBroseList(FilterDTO filter, long minVal, long maxVal) {
		List<LhssysTaxcpcBankAuditMast> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);
			if (filter != null) {
				
					
					if (filter.getEntity_code() != null && !filter.getEntity_code().equalsIgnoreCase("")) {
						sb.append(" and entity_code='" + filter.getEntity_code() + "'");
					}
					if (filter.getAudit_type() != null && !filter.getAudit_type().equalsIgnoreCase("")) {
						if (filter.getAudit_type().equalsIgnoreCase("ALL")) {

						} else {
							sb.append(" and t.audit_type='" + filter.getAudit_type() + "'");
						}
					}
//					if(filter.getSearch_value() != null && !filter.getSearch_value().equalsIgnoreCase("")) {
//						sb.append(" and lower (t.entity_code) || lower (t.audit_type) || lower(t.audit_resolve_status) || lower(t.audit_applied_app_name) || lower (t.audit_name) || lower(t.no_of_occurences) || lower (t.audit_resolve_status) like lower ('%"+filter.getSearch_value()+"%')");
//						
//					}
					if(filter.getProject_code() != null && !filter.getProject_code().equalsIgnoreCase("")) {
						sb.append(" and audit_applied_app_name='"+ filter.getProject_code() +"'");
					}
		     
			}
			String queryString = "select  t.seq_id,\r\n" + 
					"        (select a.entity_name from entity_mast a where a.entity_code=t.entity_code)entity_code,\r\n" + 
					"        t.audit_type,\r\n" + 
					"        t.audit_name,\r\n" + 
					"        t.audit_description,\r\n" + 
					"        t.audit_ext_link,\r\n" + 
					"        t.audit_applied_server_ip,\r\n" + 
					"        t.audit_applied_region,\r\n" + 
					"        (select a.project_name from project_mast a where a.project_code=t.audit_applied_app_name)audit_applied_app_name,\r\n" + 
					"        t.other_info,\r\n" +
					"        t.other_info1,\r\n" +
					"        t.other_info2,\r\n" +
					"        t.audit_resolve_status,\r\n" + 
					"        t.audit_resolve_by,\r\n" + 
					"        t.audit_resolve_remark,\r\n" + 
					"        t.audit_resolve_reference,\r\n" + 
					"        t.user_code,\r\n" + 
					"        t.no_of_occurences,\r\n" +
					"        t.other_categaory,\r\n" +
					"        t.lastupdate,\r\n" + 
					"        t.flag from lhssys_taxcpc_bank_audit_mast t where 1=1 "+sb+" order by t.lastupdate desc ";
			
//				
			System.out.println("Get Detail list Query== "+queryString);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<LhssysTaxcpcBankAuditMast> query = appAuditRepository.getSession().createNativeQuery(queryString, LhssysTaxcpcBankAuditMast.class);
			
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			System.out.println("query..."+query);
			list = (List<LhssysTaxcpcBankAuditMast>) query.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
