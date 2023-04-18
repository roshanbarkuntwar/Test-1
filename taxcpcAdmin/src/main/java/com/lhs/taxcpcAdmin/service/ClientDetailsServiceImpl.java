package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.ClientAppDetailsRepository;
import com.lhs.taxcpcAdmin.dao.ClientDetailsRepository;
import com.lhs.taxcpcAdmin.dao.ClientTranRepositorySupport;
import com.lhs.taxcpcAdmin.dao.UserLoginTranRepositorySupport;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ServerDetailModel;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;


@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@Autowired
	GlobalDoWorkExecuter executer; 
	
	@Autowired
	private ClientTranRepositorySupport clientRepoSupport;
	
	@Autowired
	ClientDetailsRepository  clientRepo;
	
	@Autowired
	ClientAppDetailsRepository  clientAppRepo;
	
	@Override
	public Map<String, String > getAllStates() {
		
		try {
			String query = "select state_code, state_name from state_mast a";
			Query q = entityManager.createNativeQuery(query);
			
			Map<String, String> map=new HashMap<String, String>();
			
			@SuppressWarnings("unchecked")
			List<Object[]> list = q.getResultList();
			for (Object[] obj : list) {
			     String code = (String) obj[0];
			     String name = (String) obj[1];
			     map.put(code, name);
			}
			System.out.println(map);
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<ClientDetails> getClientDetailList() {
		List<ClientDetails> clientlist = new ArrayList<ClientDetails>();
		try {
			clientlist = clientRepo.findAll();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return clientlist;
	}

	@Override
	public ClientDetails getClientDetailsFromEntityCode(String client_code) {
		ClientDetails list = null;
		//System.out.println("client_code......."+client_code);
		try {
			list = clientRepo.findById(client_code).get();
			
		} catch (Exception e) {
			
		}
		return list;
	}
	
	
	@Override
	@Transactional
	public String deleteClientDetail(String client_code) {
		
		String response = "error";
		System.out.println("client Code........."+client_code);
		try {
			if(!strUtl.isNull(client_code)) {
				String query = "delete from Lhs_Taxcpc_Client_App_Details a where a.client_code = :client_code";
				Query nativeQuery = entityManager.createNativeQuery(query, ClientAppDetails.class);
				nativeQuery.setParameter("client_code", client_code);
				nativeQuery.executeUpdate();

			clientRepo.deleteById(client_code);
	
			response = "success";
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
//		System.out.println("response.."+response);
		return response;
	}

	@Override
	public String viewClientDetails(String client_code){
		StringBuilder sb = new StringBuilder();
		System.out.println("Client Code......."+client_code);
		try {
			ClientDetails entity = getClientDetailsDataonEntryType(client_code);
		    String newentity = getApplicationCode(client_code);
		    String name = getApplicationName(client_code);
		    String url = getApplicationUrl(client_code);
		    String username = getConnected_db_username(client_code);
		    String pwd = getConnected_db_pwd(client_code);
		    String sid = getConnected_db_sid(client_code);
		    String port = getConnected_port(client_code);
		    String remark = getConnected_db_remark(client_code);
			String Branch_name = getBranchName(entity.getBranch_state_code());
			if (entity != null) {
		if (entity.getEntity_code() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Entity Code </td><td class=\\\"pre-wrap\\\">"+entity.getEntity_code()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Entity Code </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getEntity_name() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Entity Name</td><td class=\\\"pre-wrap\\\">"+entity.getEntity_name()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Entity Name</td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getTeam_name() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Team Name</td><td class=\\\"pre-wrap\\\">"+entity.getTeam_name()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Team Name</td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getResp_person_name() != null) {	
		sb.append("<tr><td class=\"text-bold\">Responsible Person's Name</td><td class=\"pre-wrap\">"+entity.getResp_person_name()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Responsible Person's Name</td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getResp_person_desig() != null) {	
		sb.append("<tr><td class=\"text-bold\">Responsible Person's Designation</td><td class=\"pre-wrap\">"+entity.getResp_person_desig()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Responsible Person's Designation</td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getResp_person_mobileno() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Responsible Person's Mobile Number</td><td class=\"pre-wrap\">"+entity.getResp_person_mobileno()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Responsible Person's Mobile Number</td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getResp_person_email_id() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Responsible Person's Email Id</td><td class=\"pre-wrap\">"+entity.getResp_person_email_id()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Responsible Person's Email Id</td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getResp_person_address() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Responsible Person's Address</td><td class=\"pre-wrap\">"+entity.getResp_person_address()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Responsible Person's Address</td><td class=\"pre-wrap\"></td></tr>");
			}

		if (entity.getBranch_address() != null ) {	
			sb.append("<tr><td class=\"text-bold\">Branch Address</td><td class=\"pre-wrap\">"+entity.getBranch_address()+"</td></tr>");
			}else {
				sb.append("<tr><td class=\"text-bold\">Branch Address</td><td class=\"pre-wrap\"></td></tr>");
				}

			if (entity.getBranch_pin() != null ) {	
				sb.append("<tr><td class=\"text-bold\">Branch Pin</td><td class=\"pre-wrap\">"+entity.getBranch_pin()+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Branch Pin</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (entity.getBranch_city() != null ) {	
				sb.append("<tr><td class=\"text-bold\">Branch City</td><td class=\"pre-wrap\">"+entity.getBranch_city()+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Branch City</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (Branch_name != null ) {	
				sb.append("<tr><td class=\"text-bold\">Branch State</td><td class=\"pre-wrap\">"+Branch_name+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Branch State</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (newentity != null ) {	
				sb.append("<tr><td class=\"text-bold\">Application Type</td><td class=\"pre-wrap\">"+newentity+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Application Type</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (name != null ) {	
				sb.append("<tr><td class=\"text-bold\">Application Name</td><td class=\"pre-wrap\">"+name+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Application Name</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (url != null ) {	
				sb.append("<tr><td class=\"text-bold\">Application Url</td><td class=\"pre-wrap\">"+url+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Application Url</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (username != null ) {	
				sb.append("<tr><td class=\"text-bold\">Connected Database Username</td><td class=\"pre-wrap\">"+username+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Connected Database Username</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (pwd != null ) {	
				sb.append("<tr><td class=\"text-bold\">Connected Database Password</td><td class=\"pre-wrap\">"+pwd+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Connected Database Password</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (sid != null ) {	
				sb.append("<tr><td class=\"text-bold\">Connected Database System ID</td><td class=\"pre-wrap\">"+sid+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Connected Database System ID</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (port != null ) {	
				sb.append("<tr><td class=\"text-bold\">Connected Database Port</td><td class=\"pre-wrap\">"+port+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Connected Database Port</td><td class=\"pre-wrap\"></td></tr>");
					}
			if (remark != null ) {	
				sb.append("<tr><td class=\"text-bold\">Connected Database Remark</td><td class=\"pre-wrap\">"+remark+"</td></tr>");
				}else {
					sb.append("<tr><td class=\"text-bold\">Connected Database Remark</td><td class=\"pre-wrap\"></td></tr>");
					}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}



	private String getBranchName(String branch_state_code) {
		String branchName="";
		try {
			 branchName = clientRepo.getBranchName(branch_state_code);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return branchName;
	}

	private String getApplicationCode(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getApplicationType(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}
	
	private String getApplicationName(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getApplicationName(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}
	
	private String getApplicationUrl(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getApplicationUrl(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}
	
	private String getConnected_db_username(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getConnected_db_username(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}

	private String getConnected_db_pwd(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getConnected_db_pwd(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}
	
	private String getConnected_db_sid(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getConnected_db_sid(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}
	
	private String getConnected_port(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getConnected_port(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}
	private String getConnected_db_remark(String client_code) {
		 String entity1 = "";
		try {
			entity1 = clientAppRepo.getConnected_db_remark(client_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity1;
	}
	
	@Override
	public ClientDetails getClientDetailsDataonEntryType(String entity_code) {
		// TODO Auto-generated method stub
		ClientDetails entity = new ClientDetails();
		try {
			entity = clientRepo.findById(entity_code).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}


	@Override
	public Map<String, String> getEntityName() {
	 
		  Map<String,String> entityNameList = new HashMap<>();
		  Map<String,String> NewEntityNameListList = new HashMap<>();
	  try {
		  entityNameList = clientRepo.findAll() .stream()
	     .collect(Collectors.toMap(ClientDetails::getEntity_code,ClientDetails::getEntity_name));
		  
		  NewEntityNameListList = executer.sortTheList(entityNameList);	
			
	  } catch (Exception e) { 
		  // TODO: handle exception 
		  e.printStackTrace(); }
	  
	  return NewEntityNameListList; 
	  }

	@Override
	public List<ClientDetails> getSearchClientDetail(String clientName) {
	  
		List<ClientDetails> list = new ArrayList<ClientDetails>();
		String Query = "";

		try {
	
			Query = "select entity_code,\r\n" + 
					"        entity_name,\r\n" + 
					"        team_name,\r\n" + 
					"        resp_person_name,\r\n" + 
					"        resp_person_mobileno,\r\n" + 
					"        branch_city\r\n" + 
					"   from lhs_taxcpc_client_details\r\n" + 
					"  where REGEXP_LIKE(lower(entity_code) || lower(entity_name) ||\r\n" + 
					"                    lower(team_name)   || lower(resp_person_name) ||\r\n" + 
					"                    lower(branch_city) || resp_person_mobileno,\r\n" ;
			
			  if (!clientName.isEmpty()) { 
				  Query = Query + "('" +clientName+ "'),\r\n" + 
				  		"                    'i')\r\n" + 
				  		"  order by entity_code "; 
				  } 
		//	System.out.println("Query........."+Query);

			list = executer.executeSQLQueryClientDeatils(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	} 

	@Override
	public List<ClientDetails> getSearchClientDetail1(String clientName) {
	

		List<ClientDetails> list = new ArrayList<ClientDetails>();
		String Query = "";

		try {
	
			Query = "select * from lhs_taxcpc_client_details where lower(entity_name)" ;
			
			  if (!clientName.isEmpty()) { 
				  Query = Query + "like lower('%" +clientName+ "%')"; 
				  } 
			

			list = executer.executeSQLQueryClientDeatils(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	


	@Override
	public Map<String, String> getAllCodeName() {
		Map<String, String> projectApplicationList = new HashMap<>();
		Map<String, String> newprojectApplicationList = new HashMap<>();
		try {
			String queryStr = "select entity_code, entity_name from entity_mast";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectApplicationList.put((String) obj[0], (String) obj[1]);
			}
			
			newprojectApplicationList=executer.sortTheList(projectApplicationList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (newprojectApplicationList != null && newprojectApplicationList.size() > 0) ? newprojectApplicationList : null;
	}
	
	@Override
	public String saveClientDetail(ClientDetails entity) {
		String response = "error";
		try {
			entity.setLastupdate(new Date());
			clientRepo.save(entity);
			System.out.println("save===="+entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return response;
	}
	
	@Override
	public Long getClientDetailsCount(FilterDTO filter) {
		// TODO Auto-generated method stub
		return clientRepoSupport.getClientTranBrowseCount(filter);
	}

	@Override
	public List<ClientDetails> getClientBrowseList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage) {

		List<ClientDetails> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		System.out.println("dataGridDTO.getPaginator()123..." + dataGridDTO.getPaginator());

		try {
			
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
			long minVal = paginatorEntity.getMinVal();
			
			long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();

				list = clientRepoSupport.getClientBrowseList(filter, minVal, maxVal);
			} else {
				//System.out.println("Object is null..");
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}	
		recPerPage ="";
		return list;
	}
	

	@Override
	public String saveClientDatabase(ClientDetails clientDetail) {
		String response = "error";
		try {
			clientRepo.save(clientDetail);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("response.."+response);
		return response;

	}

	@Override
	public ClientAppDetails getClientAppDetailsType(String client_code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientAppDetails> getClientAppList() {
		List<ClientAppDetails> list = new ArrayList<>();
		try {
			list = clientAppRepo.findAll();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
	}
	
	@Override
	public List<ClientAppDetails> getEditClientAppList(String client_code) {
	//	String list="";
		List<ClientAppDetails> list = new ArrayList<>();
		
		String Query="";
		try {
			Query = "select * from Lhs_Taxcpc_Client_App_Details where client_code='" + client_code + "'";
			//Query = "select  application_type from Lhs_Taxcpc_Client_App_Details where client_code='" + client_code + "'";
			// System.out.println("query......."+Query);
			list = executer.executeSQLQueryClientAppDetils(Query);
			// list =clientAppRepo.getApplicationDetils(client_code);
			System.out.println("List....."+list);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
	}

	@Override
	public String getClientAppTypeList(String client_code) {
	String list="";
		
		String Query="";
		try {
			// Query = "select * from Lhs_Taxcpc_Client_App_Details where client_code='" + client_code + "'";
			
			Query = "select Distinct application_type from Lhs_Taxcpc_Client_App_Details where client_code='" + client_code + "'";
			 System.out.println("query......."+Query);
			 list = (String)executer.getSingleValueFromSQLQuery(Query);
			// list = executer.executeSQLQueryClientAppDetils(Query);
			System.out.println("Inside Get Application Name"+list);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
	}
	
	@Override
	public List<ClientDetails> getClientList() {
		List<ClientDetails> list1 = new ArrayList<>();
		try {
			list1 = clientRepo.findAll();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list1;
	}

	@Override
	public List<ClientAppDetails> getAppClientEntry(ClientAppDetails clientappDetail, String client_code) {
		List<ClientAppDetails> newedit = new ArrayList<>();
		List<ClientAppDetails> server_Detail = new ArrayList<>();
		
		
		try {
			String Query = "select * from Lhs_Taxcpc_Client_App_Details where client_code='" + client_code + "'";
			
		    System.out.println("query......."+Query);
		    server_Detail = executer.executeSQLQueryClientAppDetils(Query);

			//Optional<ClientAppDetails> server_Detail = this.clientAppRepo.findById(client_code);
			
			System.out.println("server_Detail...."+server_Detail);
			
			String db_user_name = server_Detail.get(0).getConnected_db_username();
			String db_password = server_Detail.get(0).getConnected_db_pwd();
			String db_sid = server_Detail.get(0).getConnected_db_sid();
			String db_port = server_Detail.get(0).getConnected_port();
			String db_remark = server_Detail.get(0).getConnected_db_remark();
			String app_url = server_Detail.get(0).getApp_url();
			
			String db_user_name1 = server_Detail.get(1).getConnected_db_username();
			String db_password1 = server_Detail.get(1).getConnected_db_pwd();
			String db_sid1 = server_Detail.get(1).getConnected_db_sid();
			String db_port1 = server_Detail.get(1).getConnected_port();
			String db_remark1 = server_Detail.get(1).getConnected_db_remark();
			String app_url1 = server_Detail.get(1).getApp_url();
			
			// newedit = db_user_name+","+db_password+","+db_sid+","+db_port+","+db_remark;
			 System.out.println("newedit....."+newedit);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server_Detail;
	}

	@Override
	public int getcount(String clientName) {
		
		int count= 0;
		try {
			String Query = "select count(*) from lhs_taxcpc_client_details where 1=1 lower(entity_name) like lower('%"+clientName+"%')";
			
			count = executer.getRowCount(Query);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}

	
	@Override
	public int getcountText(String clientName) {
		
		int count= 0;
		try {
			//String Query = "select count(*) from lhs_taxcpc_client_details where lower(entity_name) like lower('%"+clientName+"%')";
			String Query = "select count(*) from lhs_taxcpc_client_details where 1=1 and lower (entity_name) || lower (team_name) || lower (resp_person_name) || lower (resp_person_email_id) || lower (branch_city) || (resp_person_mobileno) like lower('%"+clientName+"%')";

			count = executer.getRowCount(Query);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}
	
	@Override
	public List<ClientDetails> getSearchClientDetailText(String clientName) {
	

		List<ClientDetails> list = new ArrayList<ClientDetails>();
		String Query = "";

		try {
	
			// Query = "select * from lhs_taxcpc_client_details where lower(entity_name)" ;
			Query = "select * from lhs_taxcpc_client_details where 1=1 and lower (entity_name) || lower (team_name) || lower (resp_person_name) || lower (resp_person_email_id) || lower (branch_city) || (resp_person_mobileno)";
			  if (!clientName.isEmpty()) { 
				  Query = Query + "like lower('%" +clientName+ "%')"; 
				  } 
			
System.out.println("Query........"+Query);
			list = executer.executeSQLQueryClientDeatils(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ClientAppDetails> getAllList(String client_code) {
        List<ClientAppDetails> list = new ArrayList<>();
		
		String Query="";
		try {
			Query = "select * from Lhs_Taxcpc_Client_App_Details where client_code='" + client_code + "'";
			//Query = "select  application_type from Lhs_Taxcpc_Client_App_Details where client_code='" + client_code + "'";
		 System.out.println("query......."+Query);
			list = executer.executeSQLQueryClientAppDetils(Query);
			
			System.out.println("List....."+list);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;	
	}

	@Override
	public String editClientAppWithNewValue(String value3, String remark, String client_code) {
		String result = "error";
		try {
			String Query = "update Lhs_Taxcpc_Client_App_Details set app_url= '" + value3 + "', remark='" + remark + "' where client_code='" + client_code + "'";
			System.out.println("Query.."+Query);
			int update = executer.updateSQLQuery(Query);

			if (update > 0) {
				result = "success";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
}
