package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;


@Service
@Transactional
public class ClientTranRepositorySupport {
	
	@Autowired
	private ClientDetailsRepository repository;
	
	public Long getClientTranBrowseCount(FilterDTO filter){
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getFilter_name() != null && !filter.getFilter_name().equalsIgnoreCase("")) {
					sb.append(" and lower (entity_name) || lower (team_name) || lower (resp_person_name) || lower (resp_person_email_id) || lower (branch_city) || (resp_person_mobileno) like lower ('%"+filter.getFilter_name()+"%')");
				}
//				if(filter.getEntity_name() != null && !filter.getEntity_name().equalsIgnoreCase("")) {
//					sb.append(" and entity_name='"+filter.getEntity_name()+"'");
//				}
//				if(filter.getTeam_name() != null && !filter.getTeam_name().equalsIgnoreCase("")) {
//					sb.append(" and team_name='"+filter.getTeam_name()+"'");					
//				}
//				if(filter.getResp_person_name() != null && !filter.getResp_person_name().equalsIgnoreCase("")) {
//					sb.append(" and resp_person_name='"+filter.getResp_person_name()+"'");
//				}
//				if(filter.getResp_person_mobileno() != null && !filter.getResp_person_mobileno().equalsIgnoreCase("")) {
//					sb.append(" and resp_person_mobileno='"+filter.getResp_person_mobileno()+"'");
//				}
//				if(filter.getBranch_add1() != null && !filter.getBranch_add1().equalsIgnoreCase("")) {
//					sb.append(" and branch_add1='"+filter.getBranch_add1()+"'");					
//				}
     }
			System.out.println("SB Append ......."+sb);
			String queryString = "select count(*) from ClientDetails t where 1=1 "+sb ;
			System.out.println("Count Query== "+queryString);
			Query query = repository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("count.."+count);
		
		return count;
	}

	public List<ClientDetails> getClientBrowseList(FilterDTO filter, long minVal, long maxVal) {
		// TODO Auto-generated method stub
		
		System.out.println("inside ClientDetails "+filter.getPage_mode());
		List<ClientDetails> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);
		if (filter != null) {
			if(filter.getFilter_name() != null && !filter.getFilter_name().equalsIgnoreCase("")) {
				sb.append("and  lower (entity_name) || lower (team_name) || lower (resp_person_name) || lower (resp_person_email_id) || lower (branch_city) || (resp_person_mobileno) like lower('%"+filter.getFilter_name()+"%')");
			}
//				if(filter.getEntity_name() != null && !filter.getEntity_name().equalsIgnoreCase("")) {
//					sb.append(" and lower (entity_name) like lower('%"+filter.getEntity_name()+"%')");
//				}
//				if(filter.getTeam_name() != null && !filter.getTeam_name().equalsIgnoreCase("")) {
//					sb.append(" and team_name='"+filter.getTeam_name()+"'");					
//				}
//				if(filter.getResp_person_name() != null && !filter.getResp_person_name().equalsIgnoreCase("")) {
//					sb.append(" and resp_person_name='"+filter.getResp_person_name()+"'");
//				}
//				if(filter.getResp_person_mobileno() != null && !filter.getResp_person_mobileno().equalsIgnoreCase("")) {
//					sb.append(" and resp_person_mobileno ='"+filter.getResp_person_mobileno()+"'");
//				}
//				if(filter.getBranch_add1() != null && !filter.getBranch_add1().equalsIgnoreCase("")) {
//					sb.append(" and branch_add1='"+filter.getBranch_add1()+"'");					
//				}
	    }
			System.out.println("SB APend ......."+sb);
			String queryString = "from ClientDetails t where 1=1 "+sb ;
			
			Query<ClientDetails> query = repository.getSession().createQuery(queryString, ClientDetails.class);
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<ClientDetails>) query.getResultList();
		System.out.println("LIst>>>>>"+list);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}


}
