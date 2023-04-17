package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbDetails;

@Service
@Transactional
public class DatabaseMgmtRepositorySupport {
	
	@Autowired
	private DatabaseMgmtRepository repository;

	public long getDatabaseMagmentBrowserCount(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getType_of_db() != null && !filter.getType_of_db().equalsIgnoreCase("")) {
					sb.append(" and lower (type_of_db) like lower('%"+filter.getType_of_db()+"%')");
				}
				if(filter.getDb_ip() != null && !filter.getDb_ip().equalsIgnoreCase("")) {
					sb.append(" and lower (db_ip) like lower('%"+filter.getDb_ip()+"%')");					
				}
     }
			System.out.println("SB Append ......."+sb);
			String queryString = "select count(*) from LhssysDbDetails t where 1=1 "+sb ;
			System.out.println("Count Query== "+queryString);
			Query query = repository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("count.."+count);
		
		return count;
	}
	
	public List<LhssysDbDetails> getDatabaseMangementBrowseList(FilterDTO filter, long minVal, long maxVal) {

		System.out.println("inside LhssysDbDetails "+filter.getPage_mode());
		List<LhssysDbDetails> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);
		if (filter != null) {

				if(filter.getType_of_db() != null && !filter.getType_of_db().equalsIgnoreCase("")) {
					sb.append(" and lower (type_of_db) like lower('%"+filter.getType_of_db()+"%')");
				}
				if(filter.getDb_ip() != null && !filter.getDb_ip().equalsIgnoreCase("")) {
					sb.append(" and lower (db_ip) like lower('%"+filter.getDb_ip()+"%')");					
				}

	    }
			System.out.println("SB APend ......."+sb);
			String queryString = "from LhssysDbDetails t where 1=1 "+sb ;
			
			Query<LhssysDbDetails> query = repository.getSession().createQuery(queryString, LhssysDbDetails.class);
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<LhssysDbDetails>) query.getResultList();
		System.out.println("LIst>>>>>"+list);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;

	}

	public long getDatabaseMagmentCount(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getType_of_db() != null && !filter.getType_of_db().equalsIgnoreCase("")) {
					sb.append(" and lower (type_of_db) like lower('%"+filter.getType_of_db()+"%')");
				}
				if(filter.getDb_ip() != null && !filter.getDb_ip().equalsIgnoreCase("")) {
					sb.append(" and lower (db_ip) like lower('%"+filter.getDb_ip()+"%')");					
				}
     }
			System.out.println("SB Append ......."+sb);
			String queryString = "select count(*) from LhssysDbDetails t where 1=1 "+sb ;
			System.out.println("Count Query== "+queryString);
			Query query = repository.getSession().createQuery(queryString);
			
			count = (Long) query.uniqueResult();	
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("count.."+count);
		
		return count;
	}

}
