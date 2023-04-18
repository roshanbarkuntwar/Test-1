package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;

@Service
@Transactional
public class TaxcpcWishlistPendingWorkRepositorySupport {

	@Autowired
	private TaxcpcWishlistPendingWorkRepository repository;

	public Long getWorkDetailsBrowseCount(FilterDTO filter, String workType, DataGridDTO dataGridDTO) {

		Long count = 0l;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();

		try {
//			if (filter.getWork_type() != null) {
//				sb.append(" and work_type='" + filter.getWork_type() + "'");
//			          }
			if (filter != null) {
				if (filter.getWork_nature() != null && !filter.getWork_nature().equalsIgnoreCase("")) {
					System.out.println("getWork_nature=" + filter.getWork_nature());
					sb.append(" and work_nature='" + filter.getWork_nature() + "'");

				}

				if (filter.getWork_priority() != null && !filter.getWork_priority().equalsIgnoreCase("")) {
					System.out.println("work_priority=" + filter.getWork_priority());
					//sb.append(" and lower(work_priority) like lower('%"+filter.getWork_priority()+"%')");
					sb.append(" and work_priority='" + filter.getWork_priority() + "'");


				}

				if (filter.getProject_code() != null && !filter.getProject_code().equalsIgnoreCase("")) {
					System.out.println("project_code=" + filter.getProject_code());
					sb.append(" and project_code = '" + filter.getProject_code() + "' ");
				}

				if (filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					System.out.println("getStatus()=" + filter.getStatus());
					sb.append(" and status = '" + filter.getStatus() + "' ");
				}

				if (filter.getUser_code() != null && !filter.getUser_code().equalsIgnoreCase("")) {
					System.out.println("User_code=" + filter.getUser_code());
					sb.append("and User_code= '" + filter.getUser_code() + "' ");
				}
			}

			
			String queryString1 ="select count(t) from TaxcpcWishlistPendingWork t where work_type='"+workType+"'";


			//String queryString = "select count(t) from TaxcpcWishlistPendingWork t where 1=1"+sb;
			
			queryString1=queryString1+sb;

			System.out.println("Count Query== " + queryString1);

			Query query = repository.getSession().createQuery(queryString1);

			count = (Long) query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("count==" + count);

		return count;

	}
	
	
	public List<TaxcpcWishlistPendingWork> getworkDetailsBrowseList(FilterDTO filter, long minVal, long maxVal,String workType) {
		// TODO Auto-generated method stub
		

	     List<TaxcpcWishlistPendingWork> list = new ArrayList<>();
		 StringBuilder sb= new StringBuilder();
		try {
		
			if(filter.getWork_type() != null) {
				sb.append(" and work_type='"+filter.getWork_type()+"'");
			}

			if (filter != null) {

				if(filter.getWork_nature() != null && !filter.getWork_nature().equalsIgnoreCase("")) {
					sb.append(" and work_nature='"+filter.getWork_nature()+"'");
					
				}
				if(filter.getWork_priority() != null && !filter.getWork_priority().equalsIgnoreCase("")) {
					sb.append(" and lower(work_priority) like lower('%"+filter.getWork_priority()+"%')");

				}
				if(filter.getProject_code()!= null && !filter.getProject_code().equalsIgnoreCase("")) {
					
					sb.append(" and t.project_code = '"+filter.getProject_code()+"'");					
				}
				
			 if(filter.getUser_code()!= null && !filter.getUser_code().equalsIgnoreCase("")) {
					
					sb.append(" and User_code = '"+filter.getUser_code()+"' ");					
				}
				
			 if (filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {

					sb.append(" and lower(status) like lower('%"+filter.getStatus()+"%')");
				}	
               
			}
			
		     
			String queryString ="from TaxcpcWishlistPendingWork t where 1=1"+sb;
			

			System.out.println("Get Detail list Query== "+queryString);

			Query<TaxcpcWishlistPendingWork> query = repository.getSession().createQuery(queryString, TaxcpcWishlistPendingWork.class);

			
			
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			
			list =(List<TaxcpcWishlistPendingWork>)query.getResultList();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
	


