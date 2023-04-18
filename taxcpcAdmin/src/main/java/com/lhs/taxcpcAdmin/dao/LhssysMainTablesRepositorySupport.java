package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhssysMainTables;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;


@Service
@Transactional
public class LhssysMainTablesRepositorySupport {

	@Autowired
	private LhssysMainTablesRepository repository;
	
	
	@Autowired
	GlobalDoWorkExecuter executer;

	public long getMainTableBrowseCount(FilterDTO filter) {
		Long count = 0l;
				StringBuilder sb= new StringBuilder();
	  		try {
        			//System.out.println("filter 1====="+filter);
     			if (filter != null) {
        				if(filter.getTable_or_view_name() != null && !filter.getTable_or_view_name().equalsIgnoreCase("")) {
        					sb.append(" and  lower(table_or_view_name) like lower('%"+filter.getTable_or_view_name()+"%')");
        				}
        				if(filter.getObject_type() != null && !filter.getObject_type().equalsIgnoreCase("")) {
        					sb.append(" and object_type='"+filter.getObject_type()+"'");					
        				}
        				if(filter.getProject_code() != null && !filter.getProject_code().equalsIgnoreCase("")) {
        				sb.append(" and project_code ='"+filter.getProject_code()+"'");
        				}
        				
        		     }
        			System.out.println("SB APend ......."+sb);
        			String queryString = "select count(t) from LhssysMainTables t where 1=1"+sb ;
        			System.out.println("Count Query== "+queryString);
        			Query query = repository.getSession().createQuery(queryString);
        			
        			// count = (Long) query.uniqueResult();	
        			count = (Long) query.getSingleResult();
        			System.out.println("Count......."+count);
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
        	System.out.println("count.."+count);
        		return count;  	
        		
        		
	}

	public List<LhssysMainTables> getDatabaseTableDetailList(FilterDTO filter, long minVal, long maxVal, Map<String, String> projectList) {
		
		List<LhssysMainTables> list = new ArrayList<>();
	      
		String queryString = "";
		StringBuilder sb= new StringBuilder();
		try {
			System.out.println("filter 1====="+filter);

				
				//queryString = "from LhssysMainTables t where 1=1 "+sb ;
//				queryString =  "SELECT b.* FROM (select row_number() over (order by 1) SLNO,\r\n" + 
//						"       t.table_or_view_name,\r\n" + 
//						"       t.object_type,\r\n" + 
//						"       t.remark,\r\n" + 
//						"       t.lastupdate,\r\n" + 
//						"       t.object_code,\r\n" + 
//						"       (select p.project_name  from project_mast p where t.project_code = p.project_code) project_code from lhs_taxcpc_main_tables t) b WHERE 1=1"; 
//						"       AND slno BETWEEN "+minVal+" AND "+maxVal+"";
				
			
			queryString = "SELECT b.*\r\n" + 
					"  FROM (select row_number() over(order by 1) SLNO,\r\n" + 
					"       t.table_or_view_name,\r\n" + 
					"       t.object_type,\r\n" + 
					"       t.remark,\r\n" + 
					"       t.lastupdate,\r\n" + 
					"       t.object_code,\r\n" + 
					"       (select p.project_name\r\n" + 
					"          from project_mast p\r\n" + 
					"         where t.project_code = p.project_code) project_code\r\n" + 
					"  from lhs_taxcpc_main_tables t where 1=1 ";
			System.out.println("queryString====="+queryString);
				if (filter != null) {
					if(filter.getTable_or_view_name() != null && !filter.getTable_or_view_name().equalsIgnoreCase("")) {
						queryString= queryString.concat(" and  lower(table_or_view_name) like lower('%"+filter.getTable_or_view_name()+"%')");
    				}
    				if(filter.getObject_type() != null && !filter.getObject_type().equalsIgnoreCase("")) {
    					queryString= queryString.concat(" and object_type='"+filter.getObject_type()+"'");					
    				}
    				
    				System.out.println("to check filter value====="+filter.getProject_code());
    				if(filter.getProject_code() != null && !filter.getProject_code().equalsIgnoreCase("")) {
    					
    					queryString= queryString.concat(" and project_code='"+filter.getProject_code()+"'");
    				}
//	               
			     }

				queryString= queryString.concat(") b where 1 = 1 AND SLNO BETWEEN "+minVal+" AND "+maxVal+"");
				//queryString= queryString.concat(") b where 1 = 1 ");

				System.out.println("queryString in support======"+queryString);

				list = executer.getLhssysMainTableList(queryString);
				 //list = (List<LhssysMainTables>) query.getResultList();
			System.out.println("LIst>>>>>"+list);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return list;
	}

	
}
