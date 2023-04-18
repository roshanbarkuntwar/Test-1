package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbObjects;

@Service
@Transactional
public class LhssysDbObjectsRepositorySupport {

	@Autowired
	private LhssysDbObjectsRepository repository;

	
	public long getDatabasePackageDetailsBrowseCount(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb = new StringBuilder();

		try {
			
			if (filter != null) {
				System.out.println("get Count Query== " + filter.getDb_object_name());
				if (filter.getDb_object_name() != null && !filter.getDb_object_name().equalsIgnoreCase("")) {
					sb.append(" and lower(db_object_name) like lower('%"+filter.getDb_object_name()+"%')");
					System.out.println("filter 1=====" + filter.getDb_object_name());
				}
				if (filter != null) {
					if (filter.getType_of_database()!= null && !filter.getType_of_database().equalsIgnoreCase("")) {
						sb.append(" and lower(type_of_database) like lower('%"+filter.getType_of_database()+"%')");
						System.out.println("filter 1=====" + filter.getType_of_database());
					}
				}
				if (filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					sb.append(" and lower(status) like lower('%"+filter.getStatus()+"%')");
					System.out.println("filter 1=====" + filter.getStatus());
				}
			}
			String queryString = "select count(*) from LhssysDbObjects where object_type = 'PACKAGE'"+sb;

			System.out.println("Count Query==" + queryString);
			
			Query query = repository.getSession().createQuery(queryString);

			count = (Long) query.uniqueResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}


		return count;
	}
	
	public List<LhssysDbObjects> getDatabasePackageGridBrowseList(FilterDTO filter, long minVal, long maxVal) {
		List<LhssysDbObjects> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		try {
			
			if (filter != null) {
				if (filter.getDb_object_name() != null && !filter.getDb_object_name().equalsIgnoreCase("")) {
					sb.append(" and lower(db_object_name) like lower('%"+filter.getDb_object_name()+"%')");
					System.out.println("filter 1=====" + filter.getDb_object_name());
				}
				if (filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					sb.append(" and lower(status) like lower('%"+filter.getStatus()+"%')");
					System.out.println("filter 1=====" + filter.getStatus());
				}
			}
			
			String queryString = " from LhssysDbObjects where object_type = 'PACKAGE'"+sb;
    
              System.out.println("queryString==="+queryString);
              

			Query<LhssysDbObjects> query = repository.getSession().createQuery(queryString, LhssysDbObjects.class);

			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			
			
			list = (List<LhssysDbObjects>) query.getResultList();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long getDatabaseUserGridCount(FilterDTO filter) {
		Long count = 0l;
		StringBuilder sb = new StringBuilder();

		try {
						
			if (filter != null) {
				System.out.println("get Count Query== " + filter.getDoc_name());
				if (filter.getDb_object_name() != null && !filter.getDb_object_name().equalsIgnoreCase("")) {
					System.out.println("get Count Query== " + filter.getDb_object_name());
					sb.append(" and lower(db_object_name) like lower('%" + filter.getDb_object_name() + "%')");

				}if (filter.getType_of_database() != null && !filter.getType_of_database().equalsIgnoreCase("")) {
					System.out.println("get Count Query== " + filter.getType_of_database());
					sb.append(" and lower(type_of_database) like lower('%" + filter.getType_of_database() + "%')");

				}
				
					if (filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
						sb.append(" and lower(status) like lower('%"+filter.getStatus()+"%')");
						System.out.println("filter 1=====" + filter.getStatus());
					}
				
			}
			String queryString = "select count(*) from LhssysDbObjects where  object_type = 'USER'"+sb;

			System.out.println("Count Query==" + queryString);
			
			Query query = repository.getSession().createQuery(queryString);

			count = (Long) query.uniqueResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}


		return count;
	}
	
	public List<LhssysDbObjects> getDatabaseUserGridBrowseList(FilterDTO filter, long minVal, long maxVal) {
		List<LhssysDbObjects> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		try {
			
			
			
			if (filter != null) {
				if (filter.getDb_object_name()!= null && !filter.getDb_object_name().equalsIgnoreCase("")) {
					sb.append(" and lower(db_object_name) like lower('%"+filter.getDb_object_name()+"%')");
					System.out.println("filter 1=====" + filter.getDb_object_name());
				}
				if (filter.getType_of_database()!= null && !filter.getType_of_database().equalsIgnoreCase("")) {
					sb.append(" and lower(type_of_database) like lower('%"+filter.getType_of_database()+"%')");
					System.out.println("filter 1=====" + filter.getType_of_database());
				}
				if (filter.getStatus() != null && !filter.getStatus().equalsIgnoreCase("")) {
					sb.append(" and lower(status) like lower('%"+filter.getStatus()+"%')");
					System.out.println("filter 1=====" + filter.getStatus());
				}
			}
			
			
			String queryString = " from LhssysDbObjects  where  object_type = 'USER'" +sb;
    
              System.out.println("queryString==="+queryString);
              

			Query<LhssysDbObjects> query = repository.getSession().createQuery(queryString, LhssysDbObjects.class);

			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			
			
			list = (List<LhssysDbObjects>) query.getResultList();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	

	
}
