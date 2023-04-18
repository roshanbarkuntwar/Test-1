package com.lhs.taxcpcAdmin.service;




import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhs.javautilities.LhsDateUtility;
import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.EntityLogoMastRepository;
import com.lhs.taxcpcAdmin.dao.EntityMastBORepository;
import com.lhs.taxcpcAdmin.dao.EntityMastRepository;
import com.lhs.taxcpcAdmin.dao.EntityMastRepositorySupport;
import com.lhs.taxcpcAdmin.dao.LhssysDefaultEntityClientRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.EntityLogoMast;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysDefaultEntityClient;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;


@Service
@Transactional
public class EntityMastServiceImpl implements EntityMastService {

	@Autowired
	private EntityMastRepository entityMastRepo;
	
	@Autowired
	private EntityMastBORepository entityBoRepo;
	
	@Autowired
	private EntityLogoMastRepository entityRepo;

	@Autowired
	private EntityMastRepositorySupport entitySupp;
	
	@Autowired
	GlobalDoWorkExecuter executer;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@Autowired
	private LhsDateUtility lhsDateUtility;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	LhssysDefaultEntityClientRepository defaultentityclient;
	
	@Override
	public List<EntityMast> getEntity() {
		 List<EntityMast> entityListType = new ArrayList<EntityMast>();
			try {
				
				entityListType = entityMastRepo.findAll();
			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return entityListType;

			
		}

	@Override
	public Map<String, String> getDivisionList() {
		Map<String, String> divisionlist = new HashMap<>();
		try {
			String queryStr = "select division_code , division_name  from view_division_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				divisionlist.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (divisionlist != null && divisionlist.size() > 0) ? divisionlist : null;
	}
	

	@Override
	public EntityMast saveEntityDetailMast(EntityMast entity) {
		String response = "error";
		EntityMast docEntity = new EntityMast();

		try {
			entity.setLastupdate(new Date());
			docEntity=entityMastRepo.save(entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return docEntity;
	}
	
	
	private String getDivisionName(String division) {
		
		String query="";
		String division_name="";
		try 
		{
			
			query="select d.division_name from view_division_type d where d.division_code='"+division+"'";
		//	String queryStr = "select t.role_name from user_role_mast t where t.role_code='" + role_code + "'";
			division_name = (String) executer.getSingleValueFromSQLQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return division_name ;
	}
	
	@Override
	public String viewEntityDetails(String entity_code) {
		StringBuilder sb = new StringBuilder();
		String division_code="";
		try {
			EntityMast entity = getEntityDetailByCode(entity_code);
			
			  division_code= getDivisionName(entity.getDivision());
			 

			
			if (entity != null) {
				

				if (entity.getEntity_code()!= null) {
					sb.append("<tr><td class=\"text-bold\">Entity Code </td><td>"+entity.getEntity_code()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Entity Code  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getEntity_name()!= null) {
					sb.append("<tr><td class=\"text-bold\">Entity Name  </td><td class=\"pre-wrap\">"+entity.getEntity_name()+"</td></tr>");			
				} else {
					sb.append("<tr><td class=\"text-bold\">Entity Name </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				
				if (division_code!= null) {
					sb.append("<tr><td class=\"text-bold\">Division </td><td>"+division_code+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Division </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getParent_code()!= null) {
					sb.append("<tr><td class=\"text-bold\">Parent Code  </td><td class=\"pre-wrap\">"+entity.getParent_code()+"</td></tr>");			
				} else {
					sb.append("<tr><td class=\"text-bold\">Parent Code </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				
				if (entity.getCity()!= null) {
					sb.append("<tr><td class=\"text-bold\">City </td><td >"+entity.getCity()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">City </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getPin()!= null) {
					sb.append("<tr><td class=\"text-bold\">PIN Code  </td><td >"+entity.getPin()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">PIN Code </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				
				if (entity.getPhone()!= null) {
					sb.append("<tr><td class=\"text-bold\">Mobile Number </td><td >"+entity.getPhone()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Mobile Number </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getEmail()!= null) {
					sb.append("<tr><td class=\"text-bold\">Mail Id </td><td>"+entity.getEmail()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Mail Id </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getWebsite()!= null) {
					sb.append("<tr><td class=\"text-bold\">Website  </td><td >"+entity.getWebsite()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Website </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getAdd3()!= null) {
					sb.append("<tr><td class=\"text-bold\">War File Name</td><td >"+entity.getAdd3()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">War File Name</td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getAdd1()!= null) {
					sb.append("<tr><td class=\"text-bold\">Address  </td><td >"+entity.getAdd1()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Address </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				
				
				
				if (entity.getCountry()!= null) {
					sb.append("<tr><td class=\"text-bold\">Country </td><td >"+entity.getCountry()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Country </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				
				if (entity.getCurrency_code()!= null) {
					sb.append("<tr><td class=\"text-bold\">Currency Code</td><td>"+entity.getCurrency_code()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Currency Code</td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getPrint_name()!= null) {
					sb.append("<tr><td class=\"text-bold\">Print Name/Short Name  </td><td >"+entity.getPrint_name()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Print Name/Short Name </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getDb_user()!= null) {
					sb.append("<tr><td class=\"text-bold\">DB_User </td><td >"+entity.getDb_user()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">DB_User </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getDb_user_pwd()!= null) {
					sb.append("<tr><td class=\"text-bold\">DB_Password </td><td >"+entity.getDb_user_pwd()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">DB_Password </td><td class=\"pre-wrap\"></td></tr>");
				}
			

				
			}
		} catch (Exception e) {
			// TODO: handle exception
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();		
	}


	

	

	private EntityMast getEntityDetailByCode(String entity_code) {
		EntityMast entity = new EntityMast();
		try {
			entity = entityMastRepo.findById(entity_code).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	
	

	@Override
	public long getEntityDetailsCount(FilterDTO filter) {
		
			return entitySupp.getEntityDetailsBrowseCount(filter);
		}

	
	@Override
	public List<EntityMast> getEntityDetailsList(FilterDTO filter,String  searchvalue,DataGridDTO dataGridDTO,Map<String, String> divisionList,List<EntityMast> entitylist, String recPerPage,long total ) {
		List<EntityMast> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();

				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();
				
				list = entitySupp.getEntityDetailsBrowseList(filter,searchvalue, minVal, maxVal,divisionList,entitylist);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
		return list;
	}

	
	
	@Override
	public EntityMast getEntityEditList(String entity_code) {
		EntityMast entity = new EntityMast();
		try {
			entity = entityMastRepo.findById(entity_code).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;	
		}

	
	@Override
	public Map<String, String> getEntityNameList() {

			Map<String, String> List = new HashMap<>();
			try {
				String queryStr = "select entity_code, entity_name from  entity_mast " ;
				List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
				for (Object[] obj : result) {
					List.put((String) obj[0],(String) obj[1]);
				}
			

			return (List != null && List.size() > 0) ? List : null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return List;
	
	}
	

	
	@Override
	public List<EntityMast> getEntityDataListFilter(String searchvalue){
		List<EntityMast> list = new ArrayList<>();
		String Query ="";
		try {
			
			
//			String Query="select entity_code, entity_name from entity_mast\r\n" + 
//					" where REGEXP_LIKE(lower(entity_code) || lower(entity_name),\r\n" + 
//					" '"+searchvalue +"','i')order by entity_code" ;
			
			Query = "select * from entity_mast where lower(entity_name)" ;
			
			  if (!searchvalue.isEmpty()) { 
				  Query = Query + "like lower('%" +searchvalue+ "%')"; 
				  } 
			
System.out.println("Query== "+Query);
			//list = executer.executeSQLQueryClientDeatils(Query);
					
			list = executer.executeSQLQueryEntity(Query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	

	

	@Override
	public Map<String, String> getLogoList() {
		Map<String, String> logolist = new HashMap<>();
		try {
		
		String queryStr = "select entity_code , logo  from view_entity_logo_mast";
		List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
				
		for (Object[] obj : result) {
			
			  ByteArrayOutputStream bos = new ByteArrayOutputStream();
		      ObjectOutputStream oos = new ObjectOutputStream(bos);
		      oos.writeObject(obj[1]);
		      oos.flush();
		     byte [] data = bos.toByteArray();
		     byte[] encodeBase64 = Base64.getEncoder().encode(data);
		     String base64Encoded = new String(encodeBase64, "UTF-8");
	        logolist.put((String) obj[0],(String) base64Encoded);
       
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (logolist != null && logolist.size() > 0) ? logolist : null;
	}
	

	@Override
	public void savelogo(EntityLogoMast entitylogo) {
		String response = "error";

		try {
			entityRepo.save(entitylogo);

			response = "success";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}



	@Override
	public Map<String, byte[]> getEntityLogo() {
		Map<String, byte[]> list = new HashMap<String, byte[]>();
		try {
			list = entityRepo.findAll()
					.stream()
					.filter(c -> c.getLogo() != null)
					.collect(Collectors.toMap(EntityLogoMast::getEntity_code,
							EntityLogoMast::getLogo));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	
	}
	

	@Override
	public List<EntityLogoMast> getList() {
		 List<EntityLogoMast> entityListType = new ArrayList<EntityLogoMast>();
			try {
				
				entityListType = entityRepo.findAll();
			
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return entityListType;
	}

	@Override
	public String getfilename(String entity_code) {
		String filePath = "";
		try {
			
			filePath = entityRepo.getfilename(entity_code);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return filePath;
	
	}
	
	

	
	@Override
	public Map<String, byte[]> getlogoNamebycode(String entity_code) {
		Map<String, byte[]>  filePath =null;
		try {
			
			filePath = entityRepo.getfileName(entity_code);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return filePath;
	
	}

	@Override

	public Map<String, String> getEntityNameList(String server_owner) {
		Map<String, String> list=  new HashMap<String, String>();
		if(server_owner.equalsIgnoreCase("bank")) {
			//list= entityMastRepo.getEntityNameList();
			list=entityMastRepo.findAll()
					.stream()
					.filter(s -> (s.getDivision().equalsIgnoreCase("bank") && s.getEntity_code() != "B0"))
					.collect(Collectors.toMap(EntityMast::getEntity_code,
							EntityMast::getEntity_name));
			
			if(list.containsKey("B0")){
				System.out.println(list.containsKey("B0"));
				list.remove("B0");
				
				System.out.println(list.containsKey("B0"));
				System.out.println(list.size());
			}
			
		}else {
			list=entityMastRepo.findAll()
					.stream()
					.collect(Collectors.toMap(EntityMast::getEntity_code,
							EntityMast::getEntity_name));
		}
		System.out.println(list.size());
		return list;

	

	}

	@Override
	public Map<String, String> getEntityList() {
		Map<String, String> List = new HashMap<>();
		try {
			String queryStr = "select entity_code, entity_name || '  (' || entity_code || ')' from  entity_mast " ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				List.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (List != null && List.size() > 0) ? List : null;
	}

	@Override
	public int getcount(String searchvalue) {
			int count= 0;
			try {
				String Query = "select count(*) from entity_mast where lower(entity_name) like lower('%"+searchvalue+"%')";
				 
					
				count = executer.getRowCount(Query);

				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return count;
		}

	@Override
	public int getcountTable(String searchvalue) {
		int count=0;
		try {
			String Query="select count(*) from entity_mast where lower (entity_code) || lower (entity_name) || lower (division) || lower (city) || lower (website) || lower (email) || lower (pin) || "
					+ "lower (phone) ||  lower (db_user) || lower (add3) like lower ('%"+searchvalue+"%')";
			
			count = executer.getRowCount(Query);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public List<EntityMast> getEntityDataListFilterTable(String searchvalue) {
		List<EntityMast> list = new ArrayList<>();
		String Query="";
		try {
			Query = "select * from entity_mast where  lower (entity_code) || lower (entity_name) || lower (division) || lower (city) || lower (website) || lower (email) || lower (pin) || lower (phone) || "
					+ " lower (db_user) || lower (add3) " ;
			
			  if (!searchvalue.isEmpty()) { 
				  Query = Query + "like lower('%" +searchvalue+ "%')"; 
				  } 
			
              System.out.println("Query== "+Query);
			//list = executer.executeSQLQueryClientDeatils(Query);
					
          	list = executer.executeSQLQueryEntity(Query);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return list;
	}


	@Override
	public String saveDefaultEntry(LhssysDefaultEntityClient entity) {
		String response = "error";
		try { 
			
			entityBoRepo.save(entity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
}


	@Override
	public List<LhssysDefaultEntityClient> getDefaultEntityList() {
		List<LhssysDefaultEntityClient> clientmastlist = new ArrayList<LhssysDefaultEntityClient>();
		try {
			
			clientmastlist = defaultentityclient.findAll();
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clientmastlist;
	}

	@Override
	public List<LhssysDefaultEntityClient> getDefaultEntityDetailsList(FilterDTO filter, DataGridDTO dataGridDTO,
			List<LhssysDefaultEntityClient> defaultlist, String recPerPage, long total,String searchvalue) {
		List<LhssysDefaultEntityClient> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();

				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();
				
				list = entitySupp.getDefaultEntityDetailsBrowseList(filter, minVal, maxVal,defaultlist);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
		return list;
	}

	private LhssysDefaultEntityClient getDefaultEntityDetailByCode(String client_code) {
		LhssysDefaultEntityClient entity = new LhssysDefaultEntityClient();
		try {
			entity = defaultentityclient.findById(client_code).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	
	@Override
	public String viewDefaultEntityDetails(String client_code) {
		StringBuilder sb = new StringBuilder();
		try {
			LhssysDefaultEntityClient entity = getDefaultEntityDetailByCode(client_code);
			
			 

			
			if (entity != null) {
				

				if (entity.getEntity_code()!= null) {
					sb.append("<tr><td class=\"text-bold\">Entity Code </td><td>"+entity.getEntity_code()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Entity Code  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getClient_code()!= null) {
					sb.append("<tr><td class=\"text-bold\">Client Code  </td><td class=\"pre-wrap\">"+entity.getClient_code()+"</td></tr>");			
				} else {
					sb.append("<tr><td class=\"text-bold\">Client Code </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getClient_name()!= null) {
					sb.append("<tr><td class=\"text-bold\">Client Name  </td><td class=\"pre-wrap\">"+entity.getClient_name()+"</td></tr>");			
				} else {
					sb.append("<tr><td class=\"text-bold\">Client Name </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				
				if (entity.getParent_code()!= null) {
					sb.append("<tr><td class=\"text-bold\">Parent Code </td><td >"+entity.getParent_code()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Parent Code </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getInitiate_date()!= null) {
					sb.append("<tr><td class=\"text-bold\">Initiate Date </td><td class=\"pre-wrap\">")
					.append(lhsDateUtility.getFormattedDate(entity.getInitiate_date(), "dd-MM-yyyy HH:mm:ss.SS"))
					.append("</td></tr>");	
					
				} else {
					sb.append("<tr><td class=\"text-bold\">Initiate Date </td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getNo_of_transaction()!= null) {
					sb.append("<tr><td class=\"text-bold\">No of Transaction </td><td >"+entity.getNo_of_transaction()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">No of Transaction</td><td class=\"pre-wrap\"></td></tr>");
				}
				
				
				if (entity.getLastupdate()!= null) {
					sb.append("<tr><td class=\"text-bold\">Last Update  </td><td class=\"pre-wrap\">")
					.append(lhsDateUtility.getFormattedDate(entity.getLastupdate(), "dd-MM-yyyy HH:mm:ss.SS"))
					.append("</td></tr>");	
			   } else {
				sb.append("<tr><td class=\"text-bold\">Lastupdate </td><td class=\"pre-wrap\"></td></tr>");
			   }
				if (entity.getClosed_date()!= null) {
					sb.append("<tr><td class=\"text-bold\">Closed Date </td><td class=\"pre-wrap\">")
					.append(lhsDateUtility.getFormattedDate(entity.getClosed_date(), "dd-MM-yyyy HH:mm:ss.SS"))
					.append("</td></tr>");	
				//	sb.append("<tr><td class=\"text-bold\">Closed Date </td><td >"+entity.getClosed_date()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Closed Date</td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getClosed_remark()!= null) {
					sb.append("<tr><td class=\"text-bold\">Closed Remark </td><td >"+entity.getClosed_remark()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Closed Remark</td><td class=\"pre-wrap\"></td></tr>");
				}
				
				if (entity.getLastupdate_from_fgs()!= null) {
					sb.append("<tr><td class=\"text-bold\">Lastupdate from FGS </td><td class=\"pre-wrap\">")
					.append(lhsDateUtility.getFormattedDate(entity.getLastupdate_from_fgs(), "dd-MM-yyyy HH:mm:ss.SS"))
					.append("</td></tr>");	
					//sb.append("<tr><td class=\"text-bold\">Lastupdate from FGS </td><td >"+entity.getLastupdate_from_fgs()+"</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Lastupdate from FGS</td><td class=\"pre-wrap\"></td></tr>");
				}
				

			}
		} catch (Exception e) {
			// TODO: handle exception
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();		
	}
	
	public int getcountTableDefault(String searchvalue,FilterDTO filter) {
		int count=0;
		StringBuilder sb = new StringBuilder();

		try {

			
			String Query="select count(*) from lhssys_default_entity_client  ";
			
			if(!searchvalue.isEmpty()) {
				Query = Query + "  where lower (entity_code) || lower (client_code) || lower (client_name) || lower (parent_code)"
						+ "|| lower (closed_remark) || lower (no_of_transaction)  like lower  ('%"+searchvalue+"%')";	
				
			}
			
			if(filter.getFrom_date1() != null || filter.getTo_date1() != null) {
				if (!filter.getFrom_date1().isEmpty() && !filter.getTo_date1().isEmpty()) {
					Query = Query +"where initiate_date BETWEEN to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR') AND to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')";
					
				}
				if (!filter.getFrom_date1().isEmpty()&& filter.getTo_date1().isEmpty()) {
					Query = Query + ("where to_date(initiate_date,'DD-MM-RRRR') = to_date('" + filter.getFrom_date1() + "','DD-MM-RRRR')");
					
				}
				if (!filter.getTo_date1().isEmpty() && filter.getFrom_date1().isEmpty()) {
					Query = Query +("where to_date(initiate_date,'DD-MM-RRRR') = to_date('" + filter.getTo_date1() + "','DD-MM-RRRR')");
				}
			}
			
			
			

			System.out.println("query in count==="+Query);
			count = executer.getRowCount(Query);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return count;
	}


	public List<LhssysDefaultEntityClient> getEntityDataListFilterTableDefault(String searchvalue,String from_date,String to_date) {
		List<LhssysDefaultEntityClient> list = new ArrayList<>();
		String Query="";
		try {

			 Query="select * from lhssys_default_entity_client ";
			 
			 if(!searchvalue.isEmpty()) {
					Query = Query + "  where lower (entity_code) || lower (client_code) || lower (client_name) || lower (parent_code)"
							+ "|| lower (closed_remark) || lower (no_of_transaction)  like lower  ('%"+searchvalue+"%')";	
					
				}
			 
			 
			 if (!from_date.isEmpty() && !to_date.isEmpty()) {
					Query = Query + " where initiate_date BETWEEN to_date('" + from_date + "','DD-MM-RRRR') AND to_date('"
							+ to_date + "','DD-MM-RRRR')";
				}
				if (!from_date.isEmpty() && to_date.isEmpty()) {
					Query = Query + "where to_date(initiate_date,'DD-MM-RRRR') = to_date('" + from_date + "','DD-MM-RRRR')";
				}
				if (!to_date.isEmpty() && from_date.isEmpty()) {
					Query = Query + "where to_date(initiate_date,'DD-MM-RRRR') = to_date('" + to_date + "','DD-MM-RRRR')";
				}
	
              System.out.println("Query== in service"+Query);
					
          	list = executer.executeSQLQueryDefaultEntity(Query);
			
		} catch (Exception e) {
		e.printStackTrace();	// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return list;
	}


	@Override
	public LhssysDefaultEntityClient getDefaultEntityFromClientCode(String client_code) {
		
		LhssysDefaultEntityClient list = null;
		try {
		//	list = clientmastb0.getClientDefaultList(client_code);
			list = defaultentityclient.findById(client_code).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Map<String, String> getAllDefaultClientCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getDefaultEntityDetailsCount(FilterDTO filter) {
		return entitySupp.getDefaultEntityDetailsBrowseCount(filter);

	}
	



	@Override
	public String deleteDefaultEntry(String client_code) {
		String response = "error";
		try {

			String query = "delete from lhssys_default_entity_client where client_code=:c_code";
			Query nativeQuery = entityManager.createNativeQuery(query, LhssysDefaultEntityClient.class);
			nativeQuery.setParameter("c_code", client_code);
			int i = nativeQuery.executeUpdate();
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<LhssysDefaultEntityClient> getEntityDataListFilterTableDefault(String searchvalue) {
		// TODO Auto-generated method stub
		return null;
	}

	
	


}
	


	
		
		
		
	
	
	



	



