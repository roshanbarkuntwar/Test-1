package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

@Service
@Transactional
public class DocTranRepositorySupport {

	@Autowired
	private DocTranRepository repository;

	public Long getDocTranBrowseCount(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(" and doc_mode = 'O'");
			if (filter != null) {
				if (filter.getDoc_name() != null && !filter.getDoc_name().equalsIgnoreCase("")) {
					System.out.println("Count Query== " + filter.getDoc_name());
					sb.append(" and doc_name='" + filter.getDoc_name() + "'");

				}
				if (filter.getDoc_type_code() != null && !filter.getDoc_type_code().equalsIgnoreCase("")) {
					sb.append(" and doc_type_code = '" + filter.getDoc_type_code() + "' ");
				}
				if (filter.getProject_code()!= null && !filter.getProject_code().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getProject_code());
					sb.append(" and project_code = '" + filter.getProject_code()+ "' ");
				}
				if (filter.getUser_code()!= null && !filter.getUser_code().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getUser_code());
					sb.append(" and user_code = '" + filter.getUser_code()+ "' ");
				}

			}

			String queryString = "select count(t) from DocTran t where 1=1 " + sb;
			System.out.println("Count Query==" + queryString);
			
			Query query = repository.getSession().createQuery(queryString);

			count = (Long) query.uniqueResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}


		return count;
	}

	
	  public List<DocTran> getDocTranBrowseList(FilterDTO filter,long minVal, long maxVal,Map<String, String> docTypeList,Map<String, String>projectList,Map<String, String>userList) {
	 
		// TODO Auto-generated method stub
		List<DocTran> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(" and doc_mode = 'O'");
			System.out.println("filter 1=====" + filter.toString());
			if (filter != null) {
				if (filter.getDoc_name() != null && !filter.getDoc_name().equalsIgnoreCase("")) {
					sb.append(" and lower(doc_name) like lower('%"+filter.getDoc_name()+"%')");
					System.out.println("filter 1=====" + filter.getDoc_name());
				}
				if (filter.getDoc_type_code() != null && !filter.getDoc_type_code().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getDoc_type_code());
					sb.append(" and doc_type_code = '" + filter.getDoc_type_code() + "' ");
				}
				if (filter.getProject_code()!= null && !filter.getProject_code().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getProject_code());
					sb.append(" and project_code = '" + filter.getProject_code()+ "' ");
				}
				if (filter.getUser_code()!= null && !filter.getUser_code().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getUser_code());
					sb.append(" and user_code = '" + filter.getUser_code()+ "' ");
				}

			}
			String queryString = " from DocTran t where 1=1" + sb;
			
              System.out.println("queryString==="+queryString);
              

			Query<DocTran> query = repository.getSession().createQuery(queryString, DocTran.class);

			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			
			
			list = (List<DocTran>) query.getResultList();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}


	public long getQuickDocTranBrowseCount(FilterDTO filter,String userCode) {
		Long count = 0l;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" and doc_mode = 'Q'");
			
			if (filter != null) {
				System.out.println("get Count Query== " + filter.getDoc_name());
				if (filter.getDoc_name() != null && !filter.getDoc_name().equalsIgnoreCase("")) {
					System.out.println("get Count Query== " + filter.getDoc_name());
					sb.append(" and doc_name='" + filter.getDoc_name() + "'");

				}
			}
			String queryString = "select count(*) from DocTran where   user_code='"+userCode+"'"+sb;

			System.out.println("Count Query==" + queryString);
			
			Query query = repository.getSession().createQuery(queryString);

			count = (Long) query.uniqueResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}


		return count;
	}


	public List<DocTran> getQuickDocTranBrowseList(FilterDTO filter, long minVal, long maxVal,
			 Map<String, String> docTypeList, Map<String, String> menuList,String userCode) {
		List<DocTran> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" and doc_mode = 'Q'");
			if (filter != null) {
				if (filter.getDoc_name() != null && !filter.getDoc_name().equalsIgnoreCase("")) {
					sb.append(" and lower(doc_name) like lower('%"+filter.getDoc_name()+"%')");
					System.out.println("filter 1=====" + filter.getDoc_name());
				}
			}
			
			String queryString = " from DocTran  where  user_code='"+userCode+"'"+sb;
    
              System.out.println("queryString==="+queryString);
              

			Query<DocTran> query = repository.getSession().createQuery(queryString, DocTran.class);

			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			
			
			list = (List<DocTran>) query.getResultList();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}

