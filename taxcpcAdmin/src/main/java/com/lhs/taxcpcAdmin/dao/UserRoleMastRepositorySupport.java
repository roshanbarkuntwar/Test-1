package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;

@Service
@Transactional
public class UserRoleMastRepositorySupport {
	
	@Autowired
	private UserRoleMastRepository repository;

	public Long getUserTranBrowseCount(FilterDTO filter) {
		// TODO Auto-generated method stub
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			//System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getRole_name()!= null && !filter.getRole_name().equalsIgnoreCase("")) {
				    sb.append(" and lower(role_name) like lower('%"+filter.getRole_name()+"%')");

					}
				if(filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					sb.append(" and role_status='"+filter.getStatus()+"'");
				}
               
		     }
				
			String queryString = "select count(t) from UserRoleMast t where 1=1 "+sb ;
//			System.out.println("Count Query== "+queryString);
			Query query = repository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return count;
	}

	public List<UserRoleMast> getUserRoleBrowseList(FilterDTO filter, long minVal, long maxVal,
			Map<String, String> menuList, Map<String, String> roleTypeList) {
		// TODO Auto-generated method stub
		List<UserRoleMast> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			if (filter != null) {
				if(filter.getRole_code() != null && !filter.getRole_code().equalsIgnoreCase("")) {
					sb.append(" and role_code='"+filter.getRole_code()+"'");
				}
				if(filter.getRole_name() != null && !filter.getRole_name().equalsIgnoreCase("")) {
					sb.append(" and lower(role_name) like lower('%"+filter.getRole_name()+"%')");

				}
			   if(filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					sb.append(" and role_status='"+filter.getStatus()+"'");
				}
               
		     }
			
			String queryString = "from UserRoleMast t where 1=1 "+sb ;
			Query<UserRoleMast> query = repository.getSession().createQuery(queryString, UserRoleMast.class);
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<UserRoleMast>) query.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	

}
