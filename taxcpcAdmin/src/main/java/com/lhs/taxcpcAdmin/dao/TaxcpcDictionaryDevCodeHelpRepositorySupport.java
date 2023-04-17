package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;

@Service
@Transactional
public class TaxcpcDictionaryDevCodeHelpRepositorySupport {

	@Autowired
	private TaxcpcDictionaryDevCodeHelpRepository repository;
	
	@Autowired
	GlobalDoWorkExecuter executer;

	
	public Long getprojectDictBrowserCount(FilterDTO filter){
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		try {
			//System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getProject_code() != null && !filter.getProject_code().equalsIgnoreCase("")) {
					sb.append(" and project_code='"+filter.getProject_code()+"'");
				}
				if(filter.getDict_name() != null && !filter.getDict_name().equalsIgnoreCase("")) {
					sb.append(" and lower (keyword_title_question) like lower ('%"+filter.getDict_name()+"%')");			
				}
				
		     }
			System.out.println("SB APend ......."+sb);
			String queryString = "select count(t) from TaxcpcDictionaryDevCodeHelp t where 1=1"+sb ;
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

	public List<TaxcpcDictionaryDevCodeHelp> getprojectDictBrowserList(FilterDTO filter, long minVal, long maxVal, String entry_type) {
		// TODO Auto-generated method stub
		List<TaxcpcDictionaryDevCodeHelp> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		Long count = 0l;
		try {
			//System.out.println("filter 1====="+filter);
			if (filter != null) {
				if(filter.getProject_code() != null && !filter.getProject_code().equalsIgnoreCase("")) {
					sb.append(" and project_code='"+filter.getProject_code()+"'");
				}
				if(filter.getDict_name() != null && !filter.getDict_name().equalsIgnoreCase("")) {
					sb.append(" and lower (keyword_title_question) like lower ('%"+filter.getDict_name()+"%')");					
				}
				               
		     }
			
		//	System.out.println("SB APend ......."+sb);
			String QueryString = "select count(t) from TaxcpcDictionaryDevCodeHelp t where 1=1"+sb ;
			System.out.println("Count Query== "+QueryString);
			Query query = repository.getSession().createQuery(QueryString);
			
			//count = (Long) query.uniqueResult();	
			count = (Long) query.getSingleResult();
			System.out.println("Count......."+count);
			
			String queryString = "from TaxcpcDictionaryDevCodeHelp t where 1=1 "+sb ;
			System.out.println("Get Detail list Query== "+queryString);
			Query<TaxcpcDictionaryDevCodeHelp> Tuery = repository.getSession().createQuery(queryString, TaxcpcDictionaryDevCodeHelp.class);
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				Tuery.setFirstResult((int) minVal);
				System.out.println("minVal."+minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				Tuery.setMaxResults((int) maxVal);
				System.out.println("minVal."+maxVal);
			}
			list = (List<TaxcpcDictionaryDevCodeHelp>) Tuery.getResultList();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("minVal."+minVal);
		System.out.println("minVal."+maxVal);
		return list;
	}



}
