package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;


@Service
@Transactional
public class UserMastRepositorySupport {
	
	@Autowired
	private UserMastRepository repository;
	
	@Autowired
	GlobalDoWorkExecuter executer;

	
	public Long getUserTranBrowseCount(FilterDTO filter){
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			//System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getUser_name() != null && !filter.getUser_name().equalsIgnoreCase("")) {
					sb.append(" and  lower(user_name) like lower('%"+filter.getUser_name()+"%')");
				}
				if(filter.getUser_type() != null && !filter.getUser_type().equalsIgnoreCase("")) {
					sb.append(" and user_type='"+filter.getUser_type()+"'");					
				}
				if(filter.getUser_mode() != null && !filter.getUser_mode().equalsIgnoreCase("")) {
					sb.append(" and user_mode='"+filter.getUser_mode()+"'");
				}
				if(filter.getRole_code() != null && !filter.getRole_code().equalsIgnoreCase("")) {
					sb.append(" and role_code='"+filter.getRole_code()+"'");
				}
				if(filter.getDept_str() != null && !filter.getDept_str().equalsIgnoreCase("")) {
					sb.append(" and dept_str='"+filter.getDept_str()+"'");					
				}
				if(filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					sb.append(" and user_status='"+filter.getStatus()+"'");
				}
		     }
			System.out.println("SB APend ......."+sb);
			String queryString = "select count(t) from UserMast t where 1=1"+sb ;
			System.out.println("Count Query== "+queryString);
			Query query = repository.getSession().createQuery(queryString);
			
			//count = (Long) query.uniqueResult();	
			count = (Long) query.getSingleResult();
			System.out.println("Count......."+count);
		}catch(Exception e) {
			e.printStackTrace();
		}
	System.out.println("count.."+count);
		return count;
	}

	public List<UserMast> getUserBrowseList(FilterDTO filter, long minVal, long maxVal) {
		// TODO Auto-generated method stub
		List<UserMast> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		Long count = 0l;
		try {
			//System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getUser_name() != null && !filter.getUser_name().equalsIgnoreCase("")) {
					sb.append(" and lower(user_name) like lower('%"+filter.getUser_name()+"%')");
				}
				if(filter.getUser_type() != null && !filter.getUser_type().equalsIgnoreCase("")) {
					sb.append(" and user_type='"+filter.getUser_type()+"'");					
				}
				if(filter.getUser_mode() != null && !filter.getUser_mode().equalsIgnoreCase("")) {
					sb.append(" and user_mode='"+filter.getUser_mode()+"'");
				}
				if(filter.getRole_code() != null && !filter.getRole_code().equalsIgnoreCase("")) {
					sb.append(" and role_code='"+filter.getRole_code()+"'");
				}
				if(filter.getDept_str() != null && !filter.getDept_str().equalsIgnoreCase("")) {
					sb.append(" and dept_str='"+filter.getDept_str()+"'");					
				}
				if(filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					sb.append(" and user_status='"+filter.getStatus()+"'");
				}
               
		     }
			
		//	System.out.println("SB APend ......."+sb);
			String QueryString = "select count(t) from UserMast t where 1=1"+sb ;
			System.out.println("Count Query== "+QueryString);
			Query query = repository.getSession().createQuery(QueryString);
			
			//count = (Long) query.uniqueResult();	
			count = (Long) query.getSingleResult();
			System.out.println("Count......."+count);
			
			String queryString = "from UserMast t where 1=1 "+sb ;
			System.out.println("Get Detail list Query== "+queryString);
			Query<UserMast> Tuery = repository.getSession().createQuery(queryString, UserMast.class);
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				Tuery.setFirstResult((int) minVal);
				System.out.println("minVal."+minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				Tuery.setMaxResults((int) maxVal);
				System.out.println("minVal."+maxVal);
			}
			list = (List<UserMast>) Tuery.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("minVal."+minVal);
		System.out.println("minVal."+maxVal);
		return list;
	}


}
