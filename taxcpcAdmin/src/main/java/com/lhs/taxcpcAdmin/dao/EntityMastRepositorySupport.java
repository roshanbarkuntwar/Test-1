package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysDefaultEntityClient;

@Service
@Transactional
public class EntityMastRepositorySupport {

	@Autowired
	private EntityMastRepository repository;

	public long getEntityDetailsBrowseCount(FilterDTO filter) {
		
		
		System.out.println("filter 1====="+filter);

		Long count = 0l;
		StringBuilder sb = new StringBuilder();
		try {

			if (filter != null) {
				
				
				if(filter.getSearch_entity() != null && !filter.getSearch_entity().equalsIgnoreCase("")) {
					System.out.println("out clicnt");
					System.out.println("filter 2====="+filter);

					sb.append(" and lower (entity_code) || lower (entity_name) || lower (division) || lower (city) || lower (website) || lower (email) || lower (db_user) || lower (pin) || lower (phone) ||  lower (add3) like lower ('%"+filter.getSearch_entity()+"%')");
				}
                 
				
				

			}


			String queryString = "select count(*) from EntityMast t where 1=1 "+sb;
			System.out.println("Count Query==" + queryString);

			Query query = repository.getSession().createQuery(queryString);
			count = (Long) query.uniqueResult();
			//count = (Long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("count==" + count);

		return count;

	}

	public List<EntityMast> getEntityDetailsBrowseList(FilterDTO filter, String searchvalue, long minVal, long maxVal,Map<String, String> divisionList,
			List<EntityMast> entitylist) {
		List<EntityMast> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {

			if (filter != null) {
				
				if(filter.getSearch_entity() != null && !filter.getSearch_entity().equalsIgnoreCase("")) {
					System.out.println("filter 2====="+filter);

					sb.append(" and lower (entity_code) || lower (entity_name) || lower (division) || lower (city) || lower (website) || lower (email) || lower (pin) || lower (phone) ||  lower (db_user) || lower (add3) like lower ('%"+filter.getSearch_entity()+"%')");
				}


			}

			String queryString = "from EntityMast e where 1=1"+sb;

			System.out.println("Get Detail list Query== " + queryString);

			Query<EntityMast> query = repository.getSession().createQuery(queryString, EntityMast.class);
			
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<EntityMast>) query.getResultList();
			
			System.out.println("list="+list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

//	public long getEntityDetailsBrowseCountCard(DataGridDTO dataGridDTO) {
//		Long count = 0l;
//		String queryString = "select count(t) from EntityMast t where 1=1 " ;
//		System.out.println("Count Query==" + queryString);
//
//		Query query = repository.getSession().createQuery(queryString);
//
//		count = (Long) query.getSingleResult();
//		
//		System.out.println("count=="+count);
//		
//		return count;
//	}
//
//	public List<EntityMast> getEntityDetailsBrowseListCard(long minVal, long maxVal, Map<String, String> divisionList,
//			List<EntityMast> entitylist) {
//		
//		List<EntityMast> list = new ArrayList<>();
//
//		StringBuilder sb = new StringBuilder();
//		try {
//
//
//			String queryString = "from EntityMast e where 1=1"+sb;
//
//			System.out.println("Get Detail list Query== " + queryString);
//
//			Query<EntityMast> query = repository.getSession().createQuery(queryString, EntityMast.class);
//
//			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
//				query.setFirstResult((int) minVal);
//			}
//			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
//				query.setMaxResults((int) maxVal);
//			}
//			list = (List<EntityMast>) query.getResultList();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//
//	}

	public long getDefaultEntityDetailsBrowseCount(FilterDTO filter) {

		Long count = 0l;
		StringBuilder sb = new StringBuilder();
		try {

			if (filter != null) {
				
				
				if(filter.getSearch_entity() != null && !filter.getSearch_entity().equalsIgnoreCase("")) {
					System.out.println("filter 2====="+filter);

					sb.append(" and lower (entity_code) || lower (client_code) || lower (client_name) || lower (parent_code) "
							+ "|| lower (closed_remark)  || lower (no_of_transaction) like lower ('%"+filter.getSearch_entity()+"%')");
				}
				
				
				
				
			}

			String queryString = "select count(*) from LhssysDefaultEntityClient t where 1=1"+sb;
			System.out.println("Count Query==" + queryString);

			Query query = repository.getSession().createQuery(queryString);
			count = (Long) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.out.println("count==" + count);

		return count;
	}

	public List<LhssysDefaultEntityClient> getDefaultEntityDetailsBrowseList(FilterDTO filter, long minVal, long maxVal,
			List<LhssysDefaultEntityClient> defaultlist) {
		List<LhssysDefaultEntityClient> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		try {

			if (filter != null) {
				
				if(filter.getSearch_entity() != null && !filter.getSearch_entity().equalsIgnoreCase("")) {
				sb.append(" and lower (entity_code) || lower (client_code) || lower (client_name) || lower (parent_code) || "
						+ " lower (closed_remark) || lower (no_of_transaction) like lower ('%"+filter.getSearch_entity()+"%')");
				}
				
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					sb.append("and initiate_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					sb.append("and to_date(initiate_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					sb.append("and to_date(initiate_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
				


			}

			String queryString = "from LhssysDefaultEntityClient where 1=1 "+sb;

			System.out.println("Get Detail list Query== ==" + queryString);

			Query<LhssysDefaultEntityClient> query = repository.getSession().createQuery(queryString, LhssysDefaultEntityClient.class);
			
			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<LhssysDefaultEntityClient>) query.getResultList();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
