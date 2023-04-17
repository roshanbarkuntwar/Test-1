package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;

@Service
@Transactional
public class LhssysTaxcpcDeploymentTranRepositorySupport {

	@Autowired
	private LhssysTaxcpcDeploymentTranRepository deploymentTran;
	
	
	
	public long getDeplTranBrowseCount(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 2====="+filter);
			if (filter != null) {
				
				if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
					sb.append(" and project_code='"+filter.getProject_name()+"'");
				}
				if(filter.getDepl_code() != null && !filter.getDepl_code().equalsIgnoreCase("")) {
					sb.append(" and depl_code='"+filter.getDepl_code()+"'");
				}
				if(filter.getWar_filename() != null && !filter.getWar_filename().equalsIgnoreCase("")) {
//					sb.append(" and war_filename='"+filter.getWar_filename()+"'");
					sb.append(" and lower (t.war_filename) like lower ('%"+filter.getWar_filename()+"%')");
				}
		     }
			String queryString = "select count(t) from LhssysTaxcpcDeploymentTran t where 1=1 "+sb ;
			System.out.println("Count Query== "+queryString);
			Query query = deploymentTran.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	 
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	System.out.println("count.."+count);
		return count;
	}

	public List<LhssysTaxcpcDeploymentTran> getDeplDashboardList(FilterDTO filter, long minVal, long maxVal) {
		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter.getWar_filename());
			if (filter != null) {
				if (filter != null) {
					
					if(filter.getProject_name() != null && !filter.getProject_name().equalsIgnoreCase("")) {
						sb.append(" and project_code='"+filter.getProject_name()+"'");
					}
					if(filter.getDepl_code() != null && !filter.getDepl_code().equalsIgnoreCase("")) {
						sb.append(" and depl_code='"+filter.getDepl_code()+"'");
					}
					if(filter.getWar_filename() != null && !filter.getWar_filename().equalsIgnoreCase("")) {
//						sb.append(" and war_filename='"+filter.getWar_filename()+"'");
						sb.append(" and lower (t.war_filename) like lower ('%"+filter.getWar_filename()+"%')");
					}
		     }
			}
//			String queryString = "from ReqTran t where 1=1 "+sb ;
			String queryString = "select  t.seq_id,\r\n" + 
					"	(select j.project_name  from project_mast j where j.project_code = t.project_code) project_code,\r\n" + 
					" t.war_filename,\r\n" + 
					" t.sample_data_filter_str,\r\n" + 
					" t.remark,\r\n" + 
					" t.server_url,\r\n" + 
					" t.lastupdate,\r\n" + 
					" t.flag,\r\n" + 
					"(select j.depl_name  from view_deployment_type j where j.depl_code = t.depl_code) depl_code\r\n" + 
					" from lhssys_taxcpc_deployment_tran t where 1=1 "+sb+" order by t.lastupdate desc ";
			
//				
			System.out.println("Get Detail list Query== "+queryString);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<LhssysTaxcpcDeploymentTran> query = deploymentTran.getSession().createNativeQuery(queryString, LhssysTaxcpcDeploymentTran.class);
			
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
//			System.out.println("query..."+query);
			list = (List<LhssysTaxcpcDeploymentTran>) query.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
