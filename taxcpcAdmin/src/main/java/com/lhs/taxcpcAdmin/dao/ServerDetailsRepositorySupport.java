package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.AppDetails;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.ServerDetailModel;

@Service
@Transactional
public class ServerDetailsRepositorySupport {

	@Autowired
	private ServerDetailsRepository serverDetailsRepository;
	
	@Autowired
	private AppDetailsRepository appDetailsRepo;
	
	@Autowired
	private LhsStringUtility strUtl;
 
	public long getUserTranBrowseCount(FilterDTO filter,String server_type) { 
		Long count = 0l;
		StringBuilder sb= new StringBuilder();
		Long queryString = 0l;
		try {
			System.out.println("getserver type---" + server_type);
           
			if (filter != null) {
				
//				if (filter.getConfiguration_type() != null && !filter.getConfiguration_type().equalsIgnoreCase("")) {
//					System.out.println("Count Query== " + filter.getConfiguration_type());
//					sb.append(" and configuration_type='" + filter.getConfiguration_type() + "'");
//
//				}

				if (filter.getEntity_code() != null && !filter.getEntity_code().equalsIgnoreCase("")) {
					System.out.println("Count Query== " + filter.getEntity_code());
					sb.append(" and entity_code='" + filter.getEntity_code() + "'");

				}
				if (filter.getTag_name()!= null && !filter.getTag_name().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getTag_name());
					sb.append(" and  tag_name = '" + filter.getTag_name() + "' ");
				}
				if (filter.getServer_owner_name()!= null && !filter.getServer_owner_name().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getServer_owner_name());
					sb.append(" and Server_owner_name = '" + filter.getServer_owner_name()+ "' ");
				}
			}
			String queryString1 = "select count(t) from ServerDetailModel t where configuration_type='"+server_type+"'";
			queryString1=queryString1+sb;
			System.out.println("Count Query IN server ==" + queryString1);

			Query query = serverDetailsRepository.getSession().createQuery(queryString1);
			count = (Long) query.uniqueResult();
			
			System.out.println("count in server controllreercgcxvcs...."+count);

			
			//String queryString1 ="select count(t) from TaxcpcWishlistPendingWork t where work_type='"+workType+"'";


			//String queryString = "select count(t) from TaxcpcWishlistPendingWork t where 1=1"+sb;
			
			
			//count = (Long) query.getSingleResult();
			//queryString = serverDetailsRepository.getCount(server_type);

//			System.out.println("Count ="+queryString);
			//count = queryString;
//			Query query =  serverDetailsRepository.getSession().createQuery(queryString);

//			count = (Long) query.uniqueResult();	
//			System.out.println("c="+count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("count..." + count);
		return count;
	}

	public List<ServerDetailModel> getReqApprovalBrowseList(String server_type, long minVal, long maxVal,FilterDTO filter) {
		List<ServerDetailModel> list = new ArrayList<>();
		StringBuilder sb= new StringBuilder();
		try {
			String queryString = ""; 
			System.out.println("server_type..>" + server_type);
			
			
//			if(filter.getConfiguration_type() != null) {
//				sb.append(" and configuration_type='"+filter.getConfiguration_type()+"'");
//			}
			if (filter != null) {

				if (filter.getEntity_code() != null && !filter.getEntity_code().equalsIgnoreCase("")) {
					System.out.println("Count Query== " + filter.getEntity_code());
					sb.append(" and entity_code='" + filter.getEntity_code() + "'");

				}
				if (filter.getTag_name()!= null && !filter.getTag_name().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getTag_name());
					sb.append(" and  tag_name = '" + filter.getTag_name() + "' ");
				}
				if (filter.getServer_owner_name()!= null && !filter.getServer_owner_name().equalsIgnoreCase("")) {
					System.out.println("filter 1=====" + filter.getServer_owner_name());
					sb.append(" and Server_owner_name = '" + filter.getServer_owner_name()+ "' ");
				}
			}
			
			
//			if (server_type.equalsIgnoreCase("ALL")) {
			String queryString1 = "select * from lhs_taxcpc_server_details t where configuration_type='"+server_type+"'";
			queryString1=queryString1+sb;
//				queryString = "select * from lhs_taxcpc_server_details "+sb;
//			} else {
//
//				queryString = "select * from LHS_TAXCPC_SERVER_DETAILS t WHERE t.configuration_type='PSC' order by lastupdate desc";
//			}
//			 queryString ="select * from ServerDetailModel  "+sb;

			System.out.println("Get Detail list Query==>> " + queryString1);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<ServerDetailModel> query = serverDetailsRepository.getSession().createNativeQuery(queryString1,
					ServerDetailModel.class);

			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
//			System.out.println("query...11.."+query);/projectEntry
			list = (List<ServerDetailModel>) query.getResultList();
			System.out.println("list>..>>>>>>>" + list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ServerDetailModel> getServerBrowseList1(String server_type, long minVal, long maxVal) {
		List<ServerDetailModel> list = new ArrayList<>();
//		StringBuilder sb= new StringBuilder();
		String queryString = "";
		try {
			queryString = "select a.entity_code,\r\n" 
					+ "       a.configuration_type,\r\n" 
					+ "       a.server_type_code,\r\n"
					+ "       a.parent_server,\r\n" 
					+ "       a.server_owner_name,\r\n" 
					+ "       a.server_ip,\r\n"
					+ "       a.public_ip,\r\n" 
					+ "       a.host_name,\r\n" 
					+ "       a.server_os,\r\n"
					+ "       a.remote_username,\r\n" 
					+ "       a.remote_pwd,\r\n" 
					+ "       a.mapped_drive,\r\n"
					+ "       a.tag_name,\r\n" 
					+ "       a.installed_db,\r\n" 
					+ "       a.server_remark,\r\n"
					+ "       (select t.server_type_name from VIEW_SERVER_NAME t where t.server_type_code=a.app_server_name) app_server_name,\r\n" +
					"       a.app_server_ip,\r\n" 
					+ "       a.app_server_port,\r\n"
					+ "       a.app_server_console_add,\r\n" 
					+ "       a.app_server_log_path,\r\n"
					+ "       a.app_server_username,\r\n" 
					+ "       a.app_server_pwd,\r\n"
					+ "       a.app_server_tag_name,\r\n" 
					+ "       a.app_server_remark,\r\n"
					+ "       a.lastupdate,\r\n" 
					+ "       a.flag,\r\n" 
					+ "       a.installed_db_tool,\r\n"
					+ "       a.server_id\r\n" 
					+ "  from lhs_taxcpc_server_details a "
					+" where a.configuration_type='" + server_type
					+ "' order by a.lastupdate desc";
//			if (!server_type.isEmpty()) {
//				queryString = queryString +" where a.configuration_type='" + server_type
//						+ "' order by a.lastupdate desc";
//			}
//			queryString = "select * from lhs_taxcpc_server_details t where t.configuration_type='" + server_type
//					+ "'  order by t.lastupdate desc";
			System.out.println("Get Detail list Query=====> " + queryString);
//			Query<ReqTran> query = reqTranRepository.getSession().createQuery(queryString, ReqTran.class);
			Query<ServerDetailModel> query = serverDetailsRepository.getSession().createNativeQuery(queryString,
					ServerDetailModel.class);

			if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
				query.setFirstResult((int) minVal);
			}
			if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
				query.setMaxResults((int) maxVal);
			}
			list = (List<ServerDetailModel>) query.getResultList();
			System.out.println("list>>" + list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	
	
	/// application detail 
	
	
public long getAppBrowseCount(FilterDTO filter) {
	System.out.println("filter 1====="+filter);

	Long count = 0l;
	StringBuilder sb = new StringBuilder();
	
	try {
			if (filter != null) {
				
				if (filter.getEntity_code() != null && !filter.getEntity_code().equalsIgnoreCase("")) {
					System.out.println("filter 1========" + filter.getEntity_code());
					sb.append(" and entity_code='" + filter.getEntity_code() + "'");
				}
				
				if (filter.getApplication_type()!= null && !filter.getApplication_type().equalsIgnoreCase("")) {
					System.out.println("filter 1========" + filter.getApplication_type());
					sb.append(" and application_type = '" + filter.getApplication_type() + "' ");
				}
				
				
				if (filter.getProject_code()!= null && !filter.getProject_code().equalsIgnoreCase("")) {
					System.out.println("filter 1========" + filter.getProject_code());
					sb.append(" and project_code = '" + filter.getProject_code()+ "' ");
				}
				

			if(filter.getSearch_entity() != null && !filter.getSearch_entity().equalsIgnoreCase("")) {
				System.out.println("search entity filter 2====="+filter.getSearch_entity());
				sb.append(" and lower(entity_code) || lower(application_type) || lower(project_code) || lower(server_ip) || lower(app_server_ip) || lower(app_server_name)||lower(jar_name)||lower(public_app_url)||lower(local_app_url) like lower ('%"+filter.getSearch_entity()+"%')");
			}
 
			}
			
			String queryString = "select count(t) from AppDetails t where flag='A'"+sb;

		System.out.println("Count Query IN APP ==" + queryString);

		Query query = appDetailsRepo.getSession().createQuery(queryString);
		count = (Long) query.uniqueResult();
		//System.out.println("count in app...."+count);
		//count = (Long) query.getSingleResult();
	} catch (Exception e) {
		e.printStackTrace();
	}

	System.out.println("count==" + count);

	return count;

	}

public List<AppDetails> getAppDetailsBrowseList(FilterDTO filter, long minVal, long maxVal) {
	List<AppDetails> list = new ArrayList<>();
	StringBuilder sb = new StringBuilder();
	try {

		if (filter != null) {

			if (filter.getEntity_code() != null && !filter.getEntity_code().equalsIgnoreCase("")) {
				System.out.println("Count Query== " + filter.getEntity_code());
				sb.append(" and entity_code='" + filter.getEntity_code() + "'");

			}
			if (filter.getApplication_type()!= null && !filter.getApplication_type().equalsIgnoreCase("")) {
				System.out.println("filter 1=====" + filter.getApplication_type());
				sb.append(" and application_type = '" + filter.getApplication_type() + "' ");
			}
			if (filter.getProject_code()!= null && !filter.getProject_code().equalsIgnoreCase("")) {
				System.out.println("filter 1=====" + filter.getProject_code());
				sb.append(" and project_code = '" + filter.getProject_code()+ "' ");
			}
			

		if(filter.getSearch_entity() != null && !filter.getSearch_entity().equalsIgnoreCase("")) {
			System.out.println("search entity filter 2====="+filter.getSearch_entity());

			//sb.append(" and lower(entity_code) || lower(application_type) || lower(project_code) || lower(server_ip) || lower(app_server_ip) || lower(app_server_name)||lower(jar_name)||lower(public_app_url)||lower(local_app_url) like lower ('%"+filter.getSearch_entity()+"%')");
			sb.append(						"and ((lower(entity_code) || lower(application_type) ||\n" +
					"      lower(project_code) || lower(server_ip) || lower(app_server_ip) ||\n" + 
					"      lower(app_server_name) || lower(jar_name) ||\n" + 
					"      lower(public_app_url) || lower(local_app_url)) like lower('%"+filter.getSearch_entity()+"%')\n" + 
					"     or\n" + 
					"       (select lower(entity_name) from EntityMast b where b.entity_code = a.entity_code)  ||\n" + 
					"       (select lower( application_type_name ) from ViewApplicationType c where c.application_type_code =a.application_type) ||\n" + 
					"       (select lower( project_name ) from ProjectMast d where d.project_code =a.project_code) ||\n" + 
					"       (select lower( server_type_name ) from ViewServerName g where g.server_type_code=a.app_server_name) like LOWER('%"+filter.getSearch_entity()+"%')\n" + 
					"    )");
		}
		}

		String queryString = "from AppDetails a  where flag='A'"+sb;

		System.out.println("Get Detail list Query== " + queryString);

		Query<AppDetails> query = appDetailsRepo.getSession().createQuery(queryString, AppDetails.class);
		
		if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
			query.setFirstResult((int) minVal);
		}
		if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
			query.setMaxResults((int) maxVal);
		}
		
		list = (List<AppDetails>) query.getResultList();
		

	} catch (Exception e) {
		e.printStackTrace();
	}

	return list;

}

public List<ServerDetailModel> getServerFilterDataGridList(String configuration_type, String server_owner_name,
		String server_ip, String tag_name, String entity_code, long minVal, long maxVal) {
	
	List<ServerDetailModel> list = new ArrayList<>();
	
	String queryString = ""; 
	try {

		queryString = "select a.entity_code,\r\n" 
				+ "       a.configuration_type,\r\n" 
				+ "       a.server_type_code,\r\n"
				+ "       a.parent_server,\r\n" 
				+ "       a.server_owner_name,\r\n" 
				+ "       a.server_ip,\r\n"
				+ "       a.public_ip,\r\n" 
				+ "       a.host_name,\r\n" 
				+ "       a.server_os,\r\n"
				+ "       a.remote_username,\r\n" 
				+ "       a.remote_pwd,\r\n" 
				+ "       a.mapped_drive,\r\n"
				+ "       a.tag_name,\r\n" 
				+ "       a.installed_db,\r\n" 
				+ "       a.server_remark,\r\n"
				+ "       (select t.server_type_name from VIEW_SERVER_NAME t where t.server_type_code=a.app_server_name) app_server_name,\r\n" +
				"       a.app_server_ip,\r\n" 
				+ "       a.app_server_port,\r\n"
				+ "       a.app_server_console_add,\r\n" 
				+ "       a.app_server_log_path,\r\n"
				+ "       a.app_server_username,\r\n" 
				+ "       a.app_server_pwd,\r\n"
				+ "       a.app_server_tag_name,\r\n" 
				+ "       a.app_server_remark,\r\n"
				+ "       a.lastupdate,\r\n" 
				+ "       a.flag,\r\n" 
				+ "       a.installed_db_tool,\r\n"
				+ "       a.server_id\r\n" 
				+ "  from lhs_taxcpc_server_details a where 1=1  ";
		
		if(!strUtl.isNull(configuration_type)){
			queryString = queryString + " and a.configuration_type='"+configuration_type+"'";
		}if(!strUtl.isNull(server_owner_name)){
			queryString = queryString + " and a.server_owner_name='"+server_owner_name+"'";
		}if(!strUtl.isNull(server_ip)){
			queryString = queryString + " and a.server_ip='"+server_ip+"'";
		}if(!strUtl.isNull(tag_name)){
			queryString = queryString + " and a.tag_name='"+tag_name+"'";
		}if(!strUtl.isNull(entity_code)){
			queryString = queryString + " and a.entity_code='"+entity_code+"'";
		}
	System.out.println("Get Detail list Query==>> " + queryString);
	Query<ServerDetailModel> query = serverDetailsRepository.getSession().createNativeQuery(queryString,ServerDetailModel.class);

	if (minVal >= Integer.MIN_VALUE && minVal <= Integer.MAX_VALUE) {
		query.setFirstResult((int) minVal);
	}
	if (maxVal >= Integer.MIN_VALUE && maxVal <= Integer.MAX_VALUE) {
		query.setMaxResults((int) maxVal);
	}
//	System.out.println("query...11.."+query);
	list = (List<ServerDetailModel>) query.getResultList();
	System.out.println("list>..>" + list);

} catch (Exception e) {
	e.printStackTrace();
}
return list;
}


}
