package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.DatabaseMgmtRepository;
import com.lhs.taxcpcAdmin.dao.DatabaseMgmtRepositorySupport;
import com.lhs.taxcpcAdmin.dao.LhssysDbObjectsRepositorySupport;
import com.lhs.taxcpcAdmin.dao.LhssysMainTablesRepository;
import com.lhs.taxcpcAdmin.dao.LhssysMainTablesRepositorySupport;
import com.lhs.taxcpcAdmin.dao.ProjectMastRepository;
import com.lhs.taxcpcAdmin.dao.UserMastRepositorySupport;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbDetails;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbObjects;
import com.lhs.taxcpcAdmin.model.entity.LhssysMainTables;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;


@Service
public class DatabaseMgmtServiceImpl implements DatabaseMgmtService {
	
	@Autowired
	GlobalDoWorkExecuter executer;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@Autowired
	ProjectMastRepository projectRepo;
	
	@Autowired
	DatabaseMgmtRepository databaserepo;
	
	@Autowired
	LhssysMainTablesRepository tablerepo;
	
	@Autowired 
	LhssysMainTablesRepositorySupport tablereposupport;
	
	@Autowired
	DatabaseMgmtRepositorySupport datamgtSupport;
	
	@Autowired
	LhssysDbObjectsRepositorySupport dbObjectSupport;
	
	@Override
	public List<LhssysDbObjects> getDatabaseUserList() {
					
            List<LhssysDbObjects> list = new ArrayList<>();
            String Query = "";

            try {

                    Query = "select * from lhs_taxcpc_db_objects where object_type = 'USER'";
            
        //    System.out.println("Query........"+Query);        
               list = executer.getLhssysDbObjectList(Query);

            } catch (Exception e) {
                    e.printStackTrace();
            }
            return list;
	}
	@Override
	public List<LhssysDbObjects> getDatabasePackageList() {
	
            List<LhssysDbObjects> list = new ArrayList<>();
            String Query = "";

            try {

                    Query = "select * from lhs_taxcpc_db_objects where object_type = 'PACKAGE'";
            
          //  System.out.println("Query........"+Query);        
               list = executer.getLhssysDbObjectList(Query);

            } catch (Exception e) {
                    e.printStackTrace();
            }
            return list;
    }

	@Override
	public List<LhssysDbObjects> getDatabaseSynonymList() {
		List<LhssysDbObjects> list = new ArrayList<>();
        String Query = "";

        try {

                Query = "select * from lhs_taxcpc_db_objects where object_type = 'SYNONYM'";
        
        // System.out.println("Query........"+Query);        
           list = executer.getLhssysDbObjectList(Query);

        } catch (Exception e) {
                e.printStackTrace();
        }
        return list;
	}
	@Override
	public List<LhssysDbDetails> getDatabaseDetailList() {
		List<LhssysDbDetails> entity = new ArrayList<>();
		try {
			entity = databaserepo.findAll();
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return entity;
	}
	
	@Override
	public String saveLhssysDbDetailsDetail(LhssysDbDetails entity) {
		
			String response = "error";
			try {
	                        
				databaserepo.save(entity);
				
				response = "success";
			} catch (Exception e) {
				// TODO: handle exception
				response = "error";
				e.printStackTrace();
			}
			
			return response;
		}

	@Override
	public LhssysDbDetails getLhssysDbDetailsEntity(String db_code) {
		LhssysDbDetails list = null;
		try {
			list = databaserepo.getLhssysDbDetailDbCode(db_code);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<LhssysMainTables> getDatabaseTableDetailList() {
		List<LhssysMainTables> entity = new ArrayList<>();
		
			String Query = "";

			try {

				Query = "select t.table_or_view_name,\r\n" + 
						"       t.object_type,\r\n" + 
						"       t.remark,\r\n" + 
						"       t.lastupdate,\r\n" + 
						"       t.object_code,\r\n" + 
						"       (select p.project_name  from project_mast p where t.project_code = p.project_code) project_code from lhs_taxcpc_main_tables t where 1=1";

				entity = executer.getLhssysMainTableList(Query);

			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return entity;
	}
	
	@Override
	public LhssysMainTables getLhssysMainTablesEntity(String object_code) {
		LhssysMainTables list = null;
		try {
			list = tablerepo.getLhssysMainTableCode(object_code);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
		
	}
	
	
	@Override
	public String saveLhssysMainTableDetails(LhssysMainTables entity) {

		String response = "error";
		try {
                        
			tablerepo.save(entity);
			
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			response = "error";
			e.printStackTrace();
		}
		
		return response;
	}
	@Override
	public List<LhssysDbDetails> getSearchDatabaseDetail(String type_of_db, String db_ip) {
		List<LhssysDbDetails> list = new ArrayList<>();
		
		String Query = "";

		try {

			Query = "select * from lhs_taxcpc_db_details where 1=1 ";

			
			  if (!type_of_db.isEmpty()) { 
				  Query = Query + "and type_of_db like '%" +type_of_db+ "%' "; 
				  } 

			  if (!db_ip.isEmpty()) { 
				  Query = Query +"and db_ip like'%" +db_ip+ "%'"; 
				  }

			list = executer.getLhssysDetailList(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<LhssysMainTables> getSearchDatabaseTableDetail(String table_or_view_name, String object_type, String project_code) {
        List<LhssysMainTables> list = new ArrayList<>();
		

		try {

			String Query = "SELECT b.*\r\n" + 
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
			
			
			//String Query = "select * from lhs_taxcpc_main_tables WHERE 1=1 ";

			  if (!table_or_view_name.isEmpty()) { 
				  Query = Query + "and lower(table_or_view_name) like lower ('%" +table_or_view_name+ "%' )"; 
				  } 

			  if (!object_type.isEmpty()) { 
				  Query = Query + "and lower(object_type) like lower ('%" +object_type+ "%')"; 
				  }
			  
			  if (!project_code.isEmpty()) { 
//				  String QueryString = "select p.project_name from project_mast p where p.project_code = '"+project_code+"' ";
//					String project_name = (String) executer.getSingleValueFromQuery(QueryString);
//					System.out.println("project_name..."+project_name);
				  Query = Query + " and project_code = '" +project_code+ "' "; 
				  } 
			  
			 Query = Query +") b where 1 = 1 AND SLNO BETWEEN 1 AND 10";
            System.out.println("Query123........"+Query);	
			list = executer.getLhssysMainTableList(Query);
            System.out.println("list in grid........"+list);	


		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<LhssysDbObjects> getSearchDatabaseUser(String db_object_name, String type_of_database, String status) {
         List<LhssysDbObjects> list = new ArrayList<>();
		
		String Query = "";

			try {

                Query = "select * from lhs_taxcpc_db_objects where  object_type = 'USER' and";
        
			  if (!db_object_name.isEmpty()) { 
				  Query = Query + " lower(db_object_name) like lower ('%" +db_object_name+"%' )"; 
				  } 

			  if (!type_of_database.isEmpty()) { 
				  Query = Query + " lower(type_of_database) like lower ('%" +type_of_database+"%')"; 
				  }
			  if (!status.isEmpty()) { 
				  Query = Query + " lower(status) like lower ('%" +status+"%')"; 
				  }
          System.out.println("Query of GridList........"+Query);	
			list = executer.getLhssysDbObjectList(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public List<LhssysDbObjects> getSearchDatabasePackage(String db_object_name, String status) {
List<LhssysDbObjects> list = new ArrayList<>();
		
		String Query = "";

			try {

                Query = "select * from lhs_taxcpc_db_objects where object_type = 'PACKAGE' ";
        
			  if (!db_object_name.isEmpty()) { 
				  Query = Query + "and lower(db_object_name) like lower ('%" +db_object_name+"%' )"; 
				  } 

			  if (!status.isEmpty()) { 
				  Query = Query + "and lower(status) like lower ('%" +status+"%')"; 
				  }
          // System.out.println("Query........"+Query);	
			list = executer.getLhssysDbObjectList(Query);

		//	System.out.println("list=="+list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public List<LhssysDbObjects> getSearchDatabaseSynonym(String db_object_name, String type_of_database,
			String status) {
      List<LhssysDbObjects> list = new ArrayList<>();
		
		String Query = "";

			try {

                Query = "select * from lhs_taxcpc_db_objects where object_type = 'SYNONYM' ";
        
			  if (!db_object_name.isEmpty()) { 
				  Query = Query + "and lower(db_object_name) like lower ('%" +db_object_name+"%' )"; 
				  } 
			  if (!type_of_database.isEmpty()) { 
				  Query = Query + "and lower(type_of_database) like lower ('%" +type_of_database+"%' )"; 
				  } 			  
			  if (!status.isEmpty()) { 
				  Query = Query + "and lower(status) like lower ('%" +status+"%')"; 
				  }
 //System.out.println("Query........"+Query);	
			list = executer.getLhssysDbObjectList(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public Map<String, String> getAllProjectCodeName() {
		
		  Map<String,String> projectList = new HashMap<>();
		  Map<String,String> NewprojectList = new HashMap<>();
	  try {
		  projectList = projectRepo.findAll() .stream()
	     .collect(Collectors.toMap(ProjectMast::getProject_code,ProjectMast::getProject_name));
		  
			NewprojectList=executer.sortTheList(projectList);	
			
	  } catch (Exception e) { 
		  // TODO: handle exception 
		  e.printStackTrace(); }
	  
	  return NewprojectList; 
	  }
	
	
	@Override
	public long getUserCount(FilterDTO filter) {
		return tablereposupport.getMainTableBrowseCount(filter);
	}
	
	@Override
	public List<LhssysMainTables> getDatabaseTableList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, Map<String, String> projectList) {

		List<LhssysMainTables> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();

		try {
			
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal() + 1;

				long maxVal = Long.parseLong(recPerPage) * dataGridDTO.getPaginator().getPageNumber();

			
//			long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
//					&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
//							? Long.parseLong(paginatorEntity.getRecordsPerPage())
//							: paginatorEntity.getTotal();

				list = tablereposupport.getDatabaseTableDetailList(filter, minVal, maxVal,projectList);
			} else {
				System.out.println("Object is null..");
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}	
		recPerPage = "";
		return list;
	}
	
	@Override
	public int getcountText(String table_or_view_name, String object_type, String project_code) {
	
		System.out.println("project code in count==="+project_code);
		int count= 0;
		try {
			
			String Query = "select count(*) from (select row_number() over (order by 1) SLNO, t.table_or_view_name,\r\n" + 
					"       t.object_type,\r\n" + 
					"       t.remark,\r\n" + 
					"       t.lastupdate,\r\n" + 
					"       t.object_code,\r\n" + 
					"       t.project_code from lhs_taxcpc_main_tables t ) where 1=1";

			
			  if (!table_or_view_name.isEmpty()) { 

				  Query = Query + " and lower(table_or_view_name) like lower ('%" +table_or_view_name+ "%' )"; 

				  } 

			  if (!object_type.isEmpty()) { 

				  Query = Query + " and object_type like'%" +object_type+ "%'"; 

				

				  }
			  
			  if (!project_code.isEmpty()) { 

				  Query = Query + " and project_code = '" +project_code+ "' "; 

				  } 
			  System.out.println("Query in count============"+Query);
			 count = executer.getRowCount(Query);
			  System.out.println("count============"+count);

			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}
	@Override
	public long getdatabaseManagementDetailsCount(FilterDTO filter) {
		// TODO Auto-generated method stub
		return datamgtSupport.getDatabaseMagmentCount(filter);
	}
	@Override
	public long getDatabaseManagementCount(FilterDTO filter) {
		return datamgtSupport.getDatabaseMagmentBrowserCount(filter);
	}
	@Override
	public List<LhssysDbDetails> getDatabaseManagementList(FilterDTO filter, DataGridDTO dataGridDTO,
			String recPerPage) {

		List<LhssysDbDetails> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		System.out.println("dataGridDTO.getPaginator()123..." + dataGridDTO.getPaginator());

		try {
			
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
			long minVal = paginatorEntity.getMinVal();
			
			long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();

				list = datamgtSupport.getDatabaseMangementBrowseList(filter, minVal, maxVal);
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
	public int getcountTableDatabase(String type_of_db, String db_ip) {
		String Query;
		int count=0;
		try {
			Query = "select count(*) from lhs_taxcpc_db_details where 1=1 ";

			
			  if (!type_of_db.isEmpty()) { 
				  Query = Query + "and type_of_db like '%" +type_of_db+ "%' "; 
				  } 

			  if (!db_ip.isEmpty()) { 
				  Query = Query +"and db_ip like'%" +db_ip+ "%'"; 
				  }
			  System.out.println("in 1st query=="+Query);
			  count =executer.getRowCount(Query);;
			  System.out.println("in 1st count=="+count);

			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}

	@Override
	public long getDatabaseUserCount(FilterDTO filter) {
		return dbObjectSupport.getDatabaseUserGridCount(filter);
	}
	
	@Override
	public long getDatabasePackageCount(FilterDTO filter) {
		return dbObjectSupport.getDatabasePackageDetailsBrowseCount(filter);
	}
	
	@Override
	public List<LhssysDbObjects> getDatabaseUserGridList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage,
			long total) {
		List<LhssysDbObjects> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();

		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {

				long minVal = paginatorEntity.getMinVal();

				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();

				list = dbObjectSupport.getDatabaseUserGridBrowseList(filter, minVal, maxVal);

			} else {
				//System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	
	@Override
	public List<LhssysDbObjects> getDatabasePackageDetailsList(FilterDTO filter, DataGridDTO dataGridDTO,
			String recPerPage, long total) {
		List<LhssysDbObjects> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();

		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {

				long minVal = paginatorEntity.getMinVal();

				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();

				list = dbObjectSupport.getDatabasePackageGridBrowseList(filter, minVal, maxVal);

			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	@Override
	public int getDatabaseUserCountObject(FilterDTO filter,String db_object_name, String type_of_database, String status) {
		String Query;
		int count=0;
		try {
			Query = "select count(*) from lhs_taxcpc_db_objects where object_type = 'USER' ";

			
			  if (!db_object_name.isEmpty()) { 
				  Query = Query + "and db_object_name like '%" +db_object_name+ "%' "; 
				  } 

			  if (!type_of_database.isEmpty()) { 
				  Query = Query +"and type_of_database like'%" +type_of_database+ "%'"; 
				  }
			  
			  if (!status.isEmpty()) { 
				  Query = Query +"and status like'%" +status+ "%'"; 
				  }
			  System.out.println(" query Count=="+Query);
			  count =executer.getRowCount(Query);;
			  System.out.println("in 1st count=="+count);

			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}
	@Override
	public int getDatabaseUserCountPackage(String db_object_name, String status) {
		String Query = "";
		int count=0;
		try {

            Query = "select count(*) from lhs_taxcpc_db_objects where object_type = 'PACKAGE' ";
    
		  if (!db_object_name.isEmpty()) { 
			  Query = Query + "and lower(db_object_name) like lower ('%" +db_object_name+"%' )"; 
			  } 

		  if (!status.isEmpty()) { 
			  Query = Query + "and lower(status) like lower ('%" +status+"%')"; 
			  }
		  
		  count =executer.getRowCount(Query);;
	}catch(Exception e) {
		e.printStackTrace();
	}
		return count;
		
	}
}
