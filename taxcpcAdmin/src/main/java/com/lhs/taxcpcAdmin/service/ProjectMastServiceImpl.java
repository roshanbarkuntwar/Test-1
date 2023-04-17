package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lhs.javautilities.LhsDateUtility;
import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.LhssysTaxcpcDeploymentTranRepository;
import com.lhs.taxcpcAdmin.dao.ProjectMastRepository;
import com.lhs.taxcpcAdmin.dao.ProjectMenuMastRepository;
import com.lhs.taxcpcAdmin.dao.ProjectModuleMastRepository;
import com.lhs.taxcpcAdmin.dao.TaxcpcDictionaryDevCodeHelpRepository;
import com.lhs.taxcpcAdmin.dao.TaxcpcDictionaryDevCodeHelpRepositorySupport;
import com.lhs.taxcpcAdmin.dao.UserMastRepositorySupport;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectMenuMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectModuleMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;

import groovy.transform.Undefined.EXCEPTION;

@Service
public class ProjectMastServiceImpl implements ProjectMastService{


	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	ProjectMastRepository projectRepo;
	
	@Autowired
	ProjectModuleMastRepository projectModuleRepo;
	
	@Autowired
	ProjectMenuMastRepository projectMenuRepo;
	
	@Autowired
	ProjectMastService projectMenuService;
	
	@Autowired
	GlobalDoWorkExecuter executer;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@Autowired
	TaxcpcDictionaryDevCodeHelpRepository projectDictionaryRepo;
	
	@Autowired
	private TaxcpcDictionaryDevCodeHelpRepositorySupport projectDictRepoSupport;
	
	@Autowired
	LhssysTaxcpcDeploymentTranRepository lhssysTaxcpcDeploymentTranRepository  ;
	
	@Autowired
	LhsDateUtility lhsDateUtility;

	@Autowired
	private GlobalDoWorkExecuter execute;
	
	  @Override public Map<String, String> getAllProjectInfoName() {
	  
	  Map<String, String> projectInfo = new HashMap<>();
	  Map<String,String> NewprojectInfo = new HashMap<>();
	  try { 
		  String queryStr = "select project_code, project_info from project_mast"; 
		  List<Object[]> result = executer.executeSQLQueryAsList(queryStr); 
		  for (Object[] obj : result) {
			  projectInfo.put((String) obj[0], (String) obj[1]);
	  }
		  NewprojectInfo=execute.sortTheList(projectInfo);  
	  } catch (Exception ex) {
	  ex.printStackTrace(); }
	  
	  return (NewprojectInfo != null && NewprojectInfo.size() > 0) ? NewprojectInfo : null;
	  }
	 
	
	  @Override 
	  public Map<String, String> getAllProjectCodeName() 
	  { 
		  Map<String,String> projectList = new HashMap<>();
		  Map<String,String> NewprojectList = new HashMap<>();
	  try {
		  projectList = projectRepo.findAll() .stream()
	     .collect(Collectors.toMap(ProjectMast::getProject_code,ProjectMast::getProject_name));
		  
			NewprojectList=execute.sortTheList(projectList);	
			
	  } catch (Exception e) { 
		  // TODO: handle exception 
		  e.printStackTrace(); }
	  
	  return NewprojectList; 
	  }
	 

	@Override
	public Map<String, String> getModulesOnProjectCode(String projectCode) {
		// TODO Auto-generated method stub
		Map<String,String> moduleList = new HashMap<>();
		List<ProjectModuleMast> list = new ArrayList<ProjectModuleMast>();
		try {
			list = projectModuleRepo.getModulesOnProjectCode(projectCode);
			System.out.println("list==="+list);
			if (list != null && list.size() > 0) {
				moduleList =  list.stream()
			            .collect(Collectors.toMap(ProjectModuleMast::getModule_code,
		            						ProjectModuleMast::getModule_name));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("moduleList.."+moduleList);
		return moduleList;
	}


	@Override
	public String saveProjectDetail(ProjectMast entity) {
		String response = "error";
		try {
                        
			projectRepo.save(entity);
			
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			response = "error";
			e.printStackTrace();
		}
		
		return response;
	}
	public String saveProjectMenu(ProjectMenuMast entity) {
	
		String response = "error";
	//	System.out.println("entity.."+entity.getMenu_code());
		try {
			projectMenuRepo.save(entity);
			//System.out.println("response......"+response);
			response = "success";
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String saveProjectModule(ProjectModuleMast entity) {
		
		String response = "error";
		//System.out.println("entity.."+entity.getModule_name());
		try {
			projectModuleRepo.save(entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}
	

	public List<ProjectModuleMast> getProjectModuleList() {
		
		List<ProjectModuleMast> list = new ArrayList<>();
		String Query = "";

		try {

			Query = "select   pm.module_code, pm.module_name,\r\n" + ""
					+ "       pm.remark,\r\n" + ""
					+ "       pm.lastupdate,\r\n" + ""
					+ "       pm.flag,\r\n" + ""
					+ "       pm.module_status,\r\n" + ""
					+ "      (select p.project_name  from project_mast p where pm.project_code = p.project_code) project_code\r\n"
					+ "" + "  from  project_module_mast pm\r\n" + ""
					+ "WHERE 1=1 ";

			list = executer.executeSQLQuery3(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<ProjectMenuMast> getProjectMenuList() {
		List<ProjectMenuMast> list = new ArrayList<ProjectMenuMast>();
		String Query = "";

		try {

			Query = "select pm.menu_code,\r\n" + 
					"       pm.menu_name,\r\n" + 
					"       pm.menu_description,\r\n" + 
					"       (select p.module_name from project_module_mast p where pm.module_code = p.module_code) module_code,\r\n" + 
					"       (select e.project_name from project_mast e where pm.project_code = e.project_code) project_code,\r\n" + 
					"       (select f.menu_name from project_menu_mast f where pm.parent_menu_code = f.menu_code)parent_menu_code,\r\n" + 
					"       (select g.menu_name from project_menu_mast g where pm.sub_menu_type = g.menu_code)sub_menu_type,\r\n" + 
					"       pm.menu_type,\r\n" + 
					"       pm.menu_status,\r\n" + 
					"       pm.lastupdate,\r\n" + 
					"       pm.flag\r\n" + 
					"  from project_menu_mast pm\r\n" + 
					" WHERE 1 = 1";
		
			list = executer.executeSQLQuery5(Query);
		System.out.println("Query.........."+Query);	

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	} 
	
	
	public List<ProjectMast> getprojectDetailsFromCode(String parentCode){
		List<ProjectMast> list = new ArrayList<ProjectMast>();
		try {
			//list = projectRepo
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ProjectMast getProjectDetailsFromProjectCode(String parentCode) {
		// TODO Auto-generated method stub
		
		ProjectMast list = null;
		try {
			list = projectRepo.getprojectMastOnProjectCode(parentCode);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
		
	}

	@Override
	public String deleteProjectDetail(ProjectMast entity) {
		// TODO Auto-generated method stub
		String response = "error";
		//System.out.println("entity.."+entity.getModule_name());
		try {
			projectRepo.delete(entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("response.."+response);
		return response;
	}

	@Override
	public ProjectModuleMast getModuleDetailsFromProjectCode(String moduleCode) {
		// TODO Auto-generated method stub
		ProjectModuleMast list = null;
		try {
			list = projectModuleRepo.findById(moduleCode).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	
	}

	@Override
	public String deleteModuleDetails(ProjectModuleMast entity) {
		// TODO Auto-generated method stub
		String response = "error";
		//System.out.println("entity.."+entity.getModule_name());
		try {
			projectModuleRepo.delete(entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("response.."+response);
		return response;
	}

	
	@Override
	public ProjectMenuMast getMenuDetailsFromProjectCode(String menu_code) {
		// TODO Auto-generated method stub
		ProjectMenuMast list = null;
		try {
			list = projectMenuRepo.findById(menu_code).get();
			//list = projectMenuRepo.getprojectMenuOnProjectCode(menu_code);
	//		System.out.println("list.."+list.getMenu_code());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	
	}

	@Override
	public String deleteMenuDetails(ProjectMenuMast entity) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			System.out.println("Menu_code..........."+entity.getMenu_code());
			String MenuCode = entity.getMenu_code();
			
			System.out.println("MenuCode......."+MenuCode);
			
			String query = "update Project_Menu_Mast a set  a.parent_menu_code  = '' where a.parent_menu_code = :MenuCode";
			Query nativeQuery = entityManager.createNativeQuery(query, ClientAppDetails.class);
			nativeQuery.setParameter("MenuCode", MenuCode);
			//nativeQuery.executeUpdate();

			projectMenuRepo.delete(entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("response.."+response);
		return response;
	}

	@Override
	public Map<String, String> getMenuOnProjectCode(String projectCode, String moduleCode) {
		// TODO Auto-generated method stub
		Map<String,String> menuList = new HashMap<>();
		List<ProjectMenuMast> list = new ArrayList<ProjectMenuMast>();
		try {
			list = projectMenuRepo.getMenusOnModuleCode(projectCode, moduleCode);
			menuList =  list.stream()
		            .collect(Collectors.toMap(ProjectMenuMast::getMenu_code,
		            		ProjectMenuMast::getMenu_name));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return menuList;
	}

	@Override
	public String saveProjectDictionary(TaxcpcDictionaryDevCodeHelp entity) {
		String response = "error";
		try {
			projectDictionaryRepo.save(entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("response.."+response);
		return response;

	}

	@Override
	public List<TaxcpcDictionaryDevCodeHelp> getProjectDictionaryDetailList(String entry_type) {
		// TODO Auto-generated method stub
		List<TaxcpcDictionaryDevCodeHelp> list = new ArrayList<>();
		try {
			list = projectDictionaryRepo.findByEntrytype(entry_type);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}



	@Override
	public TaxcpcDictionaryDevCodeHelp getDictionaryDataOnEntryType(Integer seq_id) {
		// TODO Auto-generated method stub
		TaxcpcDictionaryDevCodeHelp entity = new TaxcpcDictionaryDevCodeHelp();
		try {
			entity = projectDictionaryRepo.findById(seq_id).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String deleteProjectDictionary(Integer seq_id) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			projectDictionaryRepo.deleteById(seq_id);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String viewProjectDictionary(Integer seq_id) {
		String ProjectName="";
//	System.out.println("filetype........."+filetype);
		StringBuilder sb = new StringBuilder();
		try {
			TaxcpcDictionaryDevCodeHelp entity = getDictionaryDataOnEntryType(seq_id);
			ProjectName = getProjectName(entity.getProject_code());
			if (entity != null) {

		//	sb.append("<tr><td class=\"text-bold\">Sequence Id : </td><td>"+entity.getSeq_id() +"</td></tr>");
		//	sb.append("<tr><td class=\"text-bold\">Entry Type : </td><td>"+entity.getEntry_type()+"</td></tr>");
			
				if (ProjectName != null ) {	
					sb.append("<tr><td class=\"text-bold\">Project Name  </td><td class=\"pre-wrap\">"+ProjectName+"</td></tr>");
					}else {
						sb.append("<tr><td class=\"text-bold\">Project Name  </td><td class=\"pre-wrap\"></td></tr>");
						}
				if (entity.getKeyword_title_question() != null ) {	
					sb.append("<tr><td class=\"text-bold\">Keyword_title_question  </td><td class=\"pre-wrap\">"+entity.getKeyword_title_question()+"</td></tr>");
					}else {
						sb.append("<tr><td class=\"text-bold\">Keyword_title_question  </td><td class=\"pre-wrap\"></td></tr>");
						}
				if (entity.getDescription() != null ) {	
					sb.append("<tr><td class=\"text-bold\">Description  </td><td class=\"pre-wrap\" >"+entity.getDescription()+"</td></tr>");
					}else {
						sb.append("<tr><td class=\"text-bold\">Description  </td><td class=\"pre-wrap\"></td></tr>");
						}
//				if (entity.getLink() != null ) {	
//					sb.append("<tr><td class=\"text-bold\">Link  </td><td class=\"pre-wrap\">"+entity.getLink()+" &nbsp;&nbsp;<i class=\"fa fa-external-link text-primary\" onclick=\"OpenLink();\"></i></td></tr>");
//					}else {
//						sb.append("<tr><td class=\"text-bold\">Link  </td><td class=\"pre-wrap\"></td></tr>");
//						}
		
			sb.append("<input type=\"hidden\" name=\"seq_id\" id=\"seq_id_dy\" value="+entity.getSeq_id()+" />");
			sb.append("<input type=\"hidden\" name=\"link\" id=\"link_dy\" value="+entity.getLink()+" />");
			sb.append("<input type=\"hidden\" name=\"file\" id=\"file_name\" value="+entity.getAttachment_name()+" />");
//			sb.append("<input type=\"hidden\" name=\"filetype\" id=\"filetype\" value="+filetype+" />");
			
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}

	

	@Override
	public String getWarFileNameOnProjectCode(String projectCode) {
		// TODO Auto-generated method stub
		String response = "";
		try {
			response = projectRepo.getWarFileName(projectCode);
//			System.out.println("getWarFileName======"+response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;

	}	
	
	public String getModuleName(String moduleCode) {
        String moduleName = "";
        try {
        	moduleName = projectModuleRepo.getModuleName(moduleCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return moduleName;
}
	
	
	@Override
	public Map<String, String> getAllProjectMenuName() {
		// TODO Auto-generated method stub
		Map<String,String> projectmenuList = new HashMap<>();
		try {
			projectmenuList = projectMenuRepo.findAll()
					.stream()
					.collect(Collectors.toMap(ProjectMenuMast::getMenu_code,
							ProjectMenuMast::getMenu_name));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return projectmenuList;
	}
	
	@Override
	public Map<Integer, String> getAllProjectFileName() {
		// TODO Auto-generated method stub
		Map<Integer, String> projectfilenameList = new HashMap<>();
		
		try {
			projectfilenameList = projectDictionaryRepo.findAll()
					.stream()
					.collect(Collectors.toMap(TaxcpcDictionaryDevCodeHelp::getSeq_id,TaxcpcDictionaryDevCodeHelp::getAttachment_name));
		
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("projectmoduleList.."+projectmoduleList);
		return projectfilenameList;
	}
	
	@Override
	public Map<String, String> getAllProjectModuleName() {
		// TODO Auto-generated method stub
		Map<String,String> projectmoduleList = new HashMap<>();
		Map<String,String> newprojectmoduleList = new HashMap<>();
		try {
			projectmoduleList = projectModuleRepo.findAll()
					.stream()
					.collect(Collectors.toMap(ProjectModuleMast::getModule_code,ProjectModuleMast::getModule_name));
			
			newprojectmoduleList =execute.sortTheList(projectmoduleList);	
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
//		System.out.println("projectmoduleList.."+projectmoduleList);
		return newprojectmoduleList;
	}

	@Override
	public Map<String, String> getErrorCodeList() {
		// TODO Auto-generated method stub
		Map<String, String> errorCodeList = new HashMap<>();
		try {
			String queryStr = "select error_group, error_group_name from VIEW_REQ_ERROR_GROUP";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				errorCodeList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (errorCodeList != null && errorCodeList.size() > 0) ? errorCodeList : null;

		
	}

	@Override
	public List<ProjectModuleMast> getSearch(String modname, String projcode, String projstat) {
		
		List<ProjectModuleMast> list = new ArrayList<>();
		String Query = "";

		try {

			Query = "select   pm.module_code, pm.module_name,\r\n" + ""
					+ "       pm.remark,\r\n" + ""
					+ "       pm.lastupdate,\r\n" + ""
					+ "       pm.flag,\r\n" + ""
					+ "       pm.module_status,\r\n" + ""
					+ "      (select p.project_name  from project_mast p where pm.project_code = p.project_code) project_code\r\n"
					+ "" + "  from  project_module_mast pm\r\n" + ""
					+ "WHERE 1=1 ";

			  if (!projcode.isEmpty()) { 
				  Query = Query + "and pm.project_code='" +projcode + "' "; 
				  } 
			  if (!modname.isEmpty()) { 
				  Query = Query +" and  pm.module_code='" + modname + "'"; 
				  }
			  if (!projstat.isEmpty()) { 
				  Query = Query +" and  pm.module_status='" + projstat + "'"; 
				  }
// System.out.println("query.."+Query);			 
			
			list = executer.executeSQLQuery3(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
  public List<ProjectMast> getSearchProjectDetail(String projname, String status)
  {
		List<ProjectMast> list = new ArrayList<>();
		String Query = "";

		try {

			Query = "select   pm.project_code, pm.project_name, pm.project_info, pm.project_type,\r\n" + 
					"              (select p.application_type_name  from view_application_type p where pm.application_type = p.application_type_code) application_type,\r\n" + 
					"                (select e.architecture_type_name  from view_architecture_type e where pm.architecture_type_code = e.architecture_type_code) architecture_type_code,\r\n" + 
					"                (select a.frontend_type_name  from view_frontend_type a where pm.frontend_type_code = a.frontend_type_code) frontend_type_code,\r\n" + 
					"                (select b.backend_type_name  from view_backend_type b where pm.backend_type_code = b.backend_type_code) backend_type_code,\r\n" + 
					"               (select c.database_type_name  from view_database_type c where pm.database_type_code = c.database_type_code) database_type_code,\r\n" + 
					"                (select d.framework_type_name  from view_framework_type d where pm.framework_type_code  = d.framework_type_code ) framework_type_code,\r\n" + 
					"                      pm.svn_link,\r\n" + 
					"                      pm.war_filename,\r\n" + 
					"                        pm.other_war_filename,\r\n" + 
					"                       pm.domain_code,\r\n" + 
					"       pm.parent_code,  pm.project_status,\r\n" + 
					"                      pm.remark,\r\n" + 
					"                        pm.lastupdate,\r\n" + 
					"                       pm.flag,\r\n" + 
					"                pm.connected_db_user \r\n" + 
					"         from  project_mast pm\r\n" + 
					"           WHERE 1=1";

			
			  if (!projname.isEmpty()) { 
				  Query = Query + "and pm.project_code='" +projname + "' "; 
				  } 
//			  if (!projtype.isEmpty()) { 
//				  Query = Query +" and  pm.application_type='" +projtype + "'"; 
//				  }
//			  if (!framework.isEmpty()) { 
//				  Query = Query +" and  pm.framework_type_code='" +framework + "'"; 
//				  }
//			  if (!database.isEmpty()) { 
//				  Query = Query +" and  pm.database_type_code='" +database + "'"; 
//				  }
			  if (!status.isEmpty()) { 
				  Query = Query +" and  pm.project_status='" +status + "'"; 
				  }
// System.out.println("Query........"+Query);	
			list = executer.executeSQLQuery4(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	  @Override 
	  public List<ProjectMenuMast> getSearchProjectMenuDetail(String menuname, String menuStatus,String project_name,String module_Name)
	  { 
		  List<ProjectMenuMast> list = new ArrayList<ProjectMenuMast>();
			String Query = "";

			try {

				Query = "select pm.menu_code,\r\n" + 
						"       pm.menu_name,\r\n" + 
						"       pm.menu_description,\r\n" + 
						"       (select p.module_name from project_module_mast p where pm.module_code = p.module_code) module_code,\r\n" + 
						"       (select e.project_name from project_mast e where pm.project_code = e.project_code) project_code,\r\n" + 
						"       (select f.menu_name from project_menu_mast f where pm.parent_menu_code = f.menu_code)parent_menu_code,\r\n" + 
						"       (select g.menu_name from project_menu_mast g where pm.sub_menu_type = g.menu_code)sub_menu_type,\r\n" + 
						"       pm.menu_type,\r\n" + 
						"       pm.menu_status,\r\n" + 
						"       pm.lastupdate,\r\n" + 
						"       pm.flag\r\n" + 
						"  from project_menu_mast pm\r\n" + 
						" WHERE 1 = 1 ";
				
				 if (!project_name.isEmpty()) { 
					  Query = Query + " and pm.project_code='" +project_name+ "' "; 
					  } 
				  if (!menuname.isEmpty()) { 
					// Query = Query + " and pm.menu_code='" +module_name+ "' "; 

					Query = Query + " and  lower(menu_code)like lower('%" + menuname + "%')";
					//Query = Query + " and  lower(menu_code)like lower('%" + module_name + "%')";

					  } 
//				  if (!menutype.isEmpty()) { 
//					  Query = Query +" and  pm.menu_code='" +menutype+ "'"; 
//					  }
//				  if (!submenutype.isEmpty()) { 
//					  Query = Query +" and  pm.menu_code='" +submenutype+ "'"; 
//					  }
				  if (!menuStatus.isEmpty()) { 
					  Query = Query +" and  pm.menu_status='" +menuStatus+ "'"; 
					  }
				  if (!module_Name.isEmpty()) { 
						Query = Query + " and  lower(module_code)like lower('%" + module_Name + "%')";
					  }
                System.out.println("Query Project........."+Query);
				list = executer.executeSQLQuery5(Query);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		} 


	@Override
	public Map<String, String> getAllProjectType() {
		// TODO Auto-generated method stub
		Map<String, String> projectType = new HashMap<>();
		try {
			
			  projectType = projectRepo.findAll().stream()
			  .collect(Collectors.toMap(ProjectMast::getProject_code,ProjectMast::getProject_type));
			 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return projectType;
	}
	
	@Override
	public Map<String, String> getAllprojectArchitecture() {
		// TODO Auto-generated method stub
		Map<String, String> projectArchitectureList = new HashMap<>();
		Map<String, String> newprojectArchitectureList = new HashMap<>();
		try {
			String queryStr = "select architecture_type_code, architecture_type_name from view_architecture_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectArchitectureList.put((String) obj[0], (String) obj[1]);
			}
			
			newprojectArchitectureList=execute.sortTheList(projectArchitectureList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (newprojectArchitectureList != null && newprojectArchitectureList.size() > 0) ? newprojectArchitectureList : null;
	}
	
	@Override
	public Map<String, String> getAllprojectBackend() {
		// TODO Auto-generated method stub
		Map<String, String> projectBackendList = new HashMap<>();
		Map<String, String> newprojectBackendList = new HashMap<>();
		try {
			String queryStr = "select backend_type_code, backend_type_name from view_backend_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectBackendList.put((String) obj[0], (String) obj[1]);
			}
			
			newprojectBackendList=execute.sortTheList(projectBackendList);;	
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (newprojectBackendList != null && newprojectBackendList.size() > 0) ? newprojectBackendList : null;
	}
	
	@Override
	public Map<String, String> getAllprojectDatabase() {
		// TODO Auto-generated method stub
		Map<String, String> projectDatabaseList = new HashMap<>();
		Map<String, String> newprojectDatabaseList = new HashMap<>();
		try {
			String queryStr = "select database_type_code, database_type_name from view_database_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectDatabaseList.put((String) obj[0], (String) obj[1]);
			}
		
			newprojectDatabaseList=execute.sortTheList(projectDatabaseList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (newprojectDatabaseList != null && newprojectDatabaseList.size() > 0) ? newprojectDatabaseList : null;
	}
	
	@Override
	public Map<String, String> getAllprojectFrontend() {
		// TODO Auto-generated method stub
		Map<String, String> projectFrontendList = new HashMap<>();
		Map<String, String> newprojectFrontendList = new HashMap<>();
		try {
			String queryStr = "select frontend_type_code, frontend_type_name from view_frontend_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectFrontendList.put((String) obj[0], (String) obj[1]);
			}
			
			newprojectFrontendList=execute.sortTheList(projectFrontendList);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (newprojectFrontendList != null && newprojectFrontendList.size() > 0) ? newprojectFrontendList : null;
	}
	
	
	@Override
	public Map<String, String> getAllApplicationCodeName() {
		// TODO Auto-generated method stub
		Map<String, String> projectApplicationList = new HashMap<>();
		Map<String, String> newprojectApplicationList = new HashMap<>();
		try {
			String queryStr = "select application_type_code, application_type_name from view_application_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectApplicationList.put((String) obj[0], (String) obj[1]);
			}
			
			newprojectApplicationList=execute.sortTheList(projectApplicationList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (newprojectApplicationList != null && newprojectApplicationList.size() > 0) ? newprojectApplicationList : null;
	}
	
	@Override
	public Map<String, String> getAllprojectFramework() {
		// TODO Auto-generated method stub
		Map<String, String> projectFrameworkList = new HashMap<>();
		Map<String, String> newprojectFrameworkList = new HashMap<>();
		try {
			String queryStr = "select framework_type_code, framework_type_name from view_framework_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectFrameworkList.put((String) obj[0], (String) obj[1]);
			}
			newprojectFrameworkList=execute.sortTheList(projectFrameworkList);	
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (newprojectFrameworkList != null && newprojectFrameworkList.size() > 0) ? newprojectFrameworkList : null;
	}
	
		

	@Override
	public Map<String, String> getAllFramework() {
		
		Map<String, String> projectFramework = new HashMap<>();
		try {
			projectFramework = projectRepo.findAll().stream()
					.collect(Collectors.toMap(ProjectMast::getFramework_type_code, ProjectMast::getFramework_type_code));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return projectFramework;
	}

	
	@Override
	public Map<String, String> getAllDatabase() {
	
		Map<String, String> projectDatabase = new HashMap<>();
		try {
			projectDatabase = projectRepo.findAll().stream()
					.collect(Collectors.toMap(ProjectMast::getDatabase_type_code, ProjectMast::getDatabase_type_code));
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return projectDatabase;
	}
	
	
	@Override
	public Map<String, String> getAllMenuName() {
		Map<String, String> menuName = new HashMap<>();
		Map<String, String> newmenuName = new HashMap<>();
		try {
			menuName = projectMenuRepo.findAll().stream()
					.collect(Collectors.toMap(ProjectMenuMast::getMenu_code, ProjectMenuMast::getMenu_name));
			newmenuName = execute.sortTheList(menuName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return newmenuName;
	}

	@Override
	public Map<String, String> getAllMenuType() {
		Map<String, String> menuType = new HashMap<>();
		Map<String, String> newmenuType = new HashMap<>();
		try {
			//userList = userRepo.findAll().stream().filter(c -> c.getFavourite_menu() != null)
		menuType = projectMenuRepo.findAll().stream().filter(c -> c.getMenu_type() != null)
				.collect(Collectors.toMap(ProjectMenuMast::getMenu_code, ProjectMenuMast::getMenu_type));
			
			newmenuType = execute.sortTheList(menuType);
	
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return newmenuType;
	}

	@Override
	public Map<String, String> getAllSubMenuType() {
		Map<String, String> submenuType = new HashMap<>();
		Map<String, String> newsubmenuType = new HashMap<>();
		try {
			submenuType = projectMenuRepo.findAll().stream()
					.collect(Collectors.toMap(ProjectMenuMast::getMenu_code, ProjectMenuMast::getMenu_name));
			newsubmenuType = execute.sortTheList(submenuType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return newsubmenuType;
	}
	
	
	@Override
	public Map<String, String> getAllParentMenuName() {
		Map<String, String> parentmenu = new HashMap<>();
	Map<String, String> newparentmenucode = new HashMap<>();
		try {
			
			parentmenu = projectMenuRepo.findAll().stream()
				.collect(Collectors.toMap(ProjectMenuMast::getMenu_code, ProjectMenuMast::getMenu_name));
			
	      newparentmenucode=execute.sortTheList(parentmenu);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return newparentmenucode;
	}

	
	@Override
	public Map<String, String> getAllParentMenuName1(String project_code) {
		Map<String, String> parentmenu = new HashMap<>();
		Map<String, String> newparentmenucode = new HashMap<>();
		try {
			String queryStr = "select menu_code, menu_name from project_menu_mast where  project_code='"+project_code+"'";
			System.out.println("queryStr..."+queryStr);
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				parentmenu.put((String) obj[0], (String) obj[1]);
			}
			
			newparentmenucode=execute.sortTheList(parentmenu);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return newparentmenucode;
	}


	
//	@Override
//	public Map<String, String> getAllParentMenuName() {
//		Map<String, String> parentmenu = new HashMap<>();
//	//Map<String, String> newparentmenucode = new HashMap<>();
//	try {
//		String queryStr = "select menu_name, parent_menu_code from project_menu_mast  where parent_menu_code is null and sub_menu_type is null" ;
//		List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
//		if(result.get(1) != null) {
//			
//			
//		for (Object[] obj : result) {
//			parentmenu.put((String) obj[0],(String) obj[1]);
//		}
//		}
//		//newparentmenucode = execute.sortTheList(parentmenu);
//	} catch (Exception ex) {
//		ex.printStackTrace();
//	}
//
//	return (parentmenu != null && parentmenu.size() > 0) ? parentmenu : null;
//}

	
	
	@Override
	public Map<String, String> getProjectList1() {
		Map<String, String> projectList = new HashMap<>();
		try {
			String queryStr = "select project_code, project_name  from  project_mast" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectList.put((String) obj[0],(String) obj[1]);
			}
		}catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (projectList != null && projectList.size() > 0) ? projectList : null;
	}
	

	@Override
	public Map<String, String> getModuleList1() {
		
		Map<String, String> projectmoduleList = new HashMap<>();
		try {
			String queryStr = "select module_code, module_name  from  project_module_mast" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectmoduleList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (projectmoduleList != null && projectmoduleList.size() > 0) ? projectmoduleList : null;
	}

	@Override
	public Map<String, String> getProjectMenuList1() {
		
		Map<String, String> projectMenuList = new HashMap<>();
		try {
			String queryStr = "select menu_code, menu_name  from  project_menu_mast " ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectMenuList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (projectMenuList != null && projectMenuList.size() > 0) ? projectMenuList : null;
	}
	
	
	@Override
	public String viewProjectMenuView(String menu_code) {
		//System.out.println("menu_code..." + menu_code);
		StringBuilder sb = new StringBuilder();
		String ProjectName="";
		String ModuleName="";
		String ParentName="";
		List<String> SubMenuName;
		try {
			ProjectMenuMast entity = getProjectMenuDataonEntryType(menu_code);
			ProjectName = getProjectName(entity.getProject_code());
			ModuleName = getModuleName(entity.getModule_code());
			ParentName = getParentName(entity.getParent_menu_code());
			SubMenuName = getSubMenuName(entity.getSub_menu_type().replace("#", ","));
			
			if (entity != null) {
		if (entity.getMenu_code() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Menu Code </td><td class=\\\"pre-wrap\\\">"+entity.getMenu_code()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Menu Code </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getMenu_name() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Menu Name </td><td class=\\\"pre-wrap\\\">"+entity.getMenu_name()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Menu Name </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getMenu_description() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Menu Description </td><td class=\\\"pre-wrap\\\">"+entity.getMenu_description()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Menu Description </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (ModuleName != null ) {	
		sb.append("<tr><td class=\"text-bold\">Module Name </td><td class=\"pre-wrap\">"+ModuleName+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Module Name </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (ProjectName != null ) {	
		sb.append("<tr><td class=\"text-bold\">Project Name </td><td class=\"pre-wrap\">"+ProjectName+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Project Name </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (ParentName != null ) {	
		sb.append("<tr><td class=\"text-bold\">Parent Menu Name </td><td class=\"pre-wrap\">"+ParentName+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Parent Menu Name </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (SubMenuName != null ) {	
		sb.append("<tr><td class=\"text-bold\">Sub Menu Name </td><td class=\"pre-wrap\">"+SubMenuName+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Sub Menu Name </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getMenu_type() != null ) {	
		sb.append("<tr><td class=\"text-bold\">Menu type </td><td class=\"pre-wrap\">"+entity.getMenu_type()+"</td></tr>");
		}else {
			sb.append("<tr><td class=\"text-bold\">Menu type </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getMenu_status() != null ) {	
			if (entity.getMenu_status().equalsIgnoreCase("A")) {
				sb.append("<tr><td class=\"text-bold\">Menu Status </td><td class=\"pre-wrap\">Active</td></tr>");
				}else {
				sb.append("<tr><td class=\"text-bold\">Menu Statuss </td><td class=\"pre-wrap\">Inactive</td></tr>");
				}
				}else {
				sb.append("<tr><td class=\"text-bold\">Menu Status  </td><td class=\"pre-wrap\"></td></tr>");
				}

//		sb.append("<tr><td class=\"text-bold\">Last Update : </td><td class=\"pre-wrap\">").append(lhsDateUtility.getFormattedDate(entity.getLastupdate(), "dd-MM-yyyy")).append("</td></tr>");
//		sb.append("<tr><td class=\"text-bold\">Flag : </td><td class=\"pre-wrap\">"+entity.getFlag()+"</td></tr>");

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}

	private List<String> getSubMenuName(String sub_menu_type) {
		
		System.out.println("sub_menu_type......."+sub_menu_type);
		String[] Code;
		String Query = "";
		Map<String, String> menuList = new LinkedHashMap<>();
		List<String> list = new ArrayList<>();
		try {

if(sub_menu_type != null) {
			menuList = projectMenuService.getSubMenuCodeNameList();

			Code = sub_menu_type.split("[,]");
			for (int j = 0; j <= Code.length - 1; j++){
				//System.out.println("splited----------" + Code[j]);

				for (Map.Entry<String, String> entry : menuList.entrySet()) {

					if (Code[j].equalsIgnoreCase(entry.getKey())) {
						list.add(entry.getValue());
					}

				}
			}
}
		}catch(EXCEPTION e) {
			e.printStackTrace();
			
			// TODO: handle exception
		}
		return list;

	}

	@Override
	public Map<String, String> getSubMenuCodeNameList() {
		Map<String, String> menulist = new HashMap<>();

		// TODO Auto-generated method stub
		try {
			String queryStr = "select menu_code , menu_name  from project_menu_mast";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				menulist.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (menulist != null && menulist.size() > 0) ? menulist : null;
	}

	
	public String getProjectName(String projectCode) {
        String projectName = "";
        try {
                projectName = projectRepo.getProjectName(projectCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return projectName;
}

	public String getParentName(String menuCode) {
        String parentName = "";
        try {
        	parentName = projectRepo.getParentName(menuCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return parentName;
}
	
//	public String getSubMenuName(String menuCode) {
//        String subMenuName = "";
//        try {
//        	subMenuName = projectRepo.getSubMenuName(menuCode);
//        } catch (Exception e) {
//                e.printStackTrace();
//        }
//        return subMenuName;
//}
	
	@Override
	public ProjectMenuMast getProjectMenuDataonEntryType(String menu_code) {
		// TODO Auto-generated method stub
		ProjectMenuMast entity = new ProjectMenuMast();
		try {
			entity = projectMenuRepo.findById(menu_code).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String viewProjectModuleView(String module) {
		//System.out.println("module_code..." + module);
		StringBuilder sb = new StringBuilder();
		String ProjectName = "";
		try {
			ProjectModuleMast entity = getProjectModuleDataonEntryType(module);
			ProjectName = getProjectName(entity.getProject_code());
			if (entity != null) {
		if (entity.getModule_code() != null ) {		
		sb.append("<tr><td class=\"text-bold\">Module Code </td><td class=\\\"pre-wrap\\\">"+entity.getModule_code()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Module Code </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (entity.getModule_name() != null ) {
		sb.append("<tr><td class=\"text-bold\">Module Name </td><td class=\\\"pre-wrap\\\">"+entity.getModule_name()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Module Name </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (entity.getRemark() != null ) {
		sb.append("<tr><td class=\"text-bold\">Remark  </td><td class=\"pre-wrap\">"+entity.getRemark()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Remark  </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (ProjectName != null ) {
		sb.append("<tr><td class=\"text-bold\">Project Name </td><td class=\"pre-wrap\">"+ProjectName+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Project Name  </td><td class=\"pre-wrap\"></td></tr>");
		}
//        sb.append("<tr><td class=\"text-bold\">Last Update : </td><td class=\"pre-wrap\">").append(lhsDateUtility.getFormattedDate(entity.getLastupdate(),"dd-MM-yyyy")).append("</td></tr>");
//		sb.append("<tr><td class=\"text-bold\">Flag : </td><td class=\"pre-wrap\">"+entity.getFlag()+"</td></tr>");
		if (entity.getModule_status() != null) {
			if (entity.getModule_status().equalsIgnoreCase("A")) {
			sb.append("<tr><td class=\"text-bold\">Module Status   </td><td class=\"pre-wrap\">Active</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Module Statuss  </td><td class=\"pre-wrap\">Inactive</td></tr>");
			}
			}else {
			sb.append("<tr><td class=\"text-bold\">Module Status   </td><td class=\"pre-wrap\"></td></tr>");
			}
//		sb.append("<tr><td class=\"text-bold\">Module Status : </td><td class=\"pre-wrap\">"+entity.getModule_status() != null && entity.getModule_status().equals("A") ? "Active" : "Inactive"+"</td></tr>");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public ProjectModuleMast getProjectModuleDataonEntryType(String module_code) {
		// TODO Auto-generated method stub
		ProjectModuleMast entity = new ProjectModuleMast();
		try {
			entity = projectModuleRepo.findById(module_code).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	
	@Override
	public ProjectMast getProjectDataonEntryType(String project_code) {
		// TODO Auto-generated method stub
		ProjectMast entity = new ProjectMast();
		try {
			entity = projectRepo.findById(project_code).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}
	

	
	
	@Override
	public String viewProjectView(String project_code) {
		String ProjectName = "";
 	List<String> NewList;
 	List<String> NewApplicationList=null;
 	List<String> NewFrontendList=null;
 	List<String> NewFrameworkList=null;
 	List<String> NewDatabaseList=null;
 	List<String> NewBackendList=null;
 	
		StringBuilder sb = new StringBuilder();
	
		try {
			
			ProjectMast entity = getProjectDataonEntryType(project_code);
			
			ProjectName = getProjectName(entity.getProject_code());

			NewList = getCode(entity.getArchitecture_type_code());
			NewApplicationList = getApplicationCode(entity.getApplication_type());
			
		      NewApplicationList = getApplicationCode(entity.getApplication_type());
			
		      if(entity.getFrontend_type_code() !=null) {
				 	NewFrontendList = getFrontendCode(entity.getFrontend_type_code().replace("#", ","));
	  
		      }
	
		       if(entity.getFramework_type_code() !=null) {
				 	NewFrameworkList = getFrameworkCode(entity.getFramework_type_code().replace("#", ","));
   
		       }
		       
		       if(entity.getDatabase_type_code() !=null) {
				 	NewDatabaseList = getDatabaseCode(entity.getDatabase_type_code().replace("#", ","));
	   
		       }
		       if(entity.getBackend_type_code() != null) {
				 	NewBackendList = getBackendCode(entity.getBackend_type_code().replace("#", ","));
	   
		       }

		 	if (entity != null) {
				if (entity.getProject_code() != null ) {
					sb.append("<tr><td class=\"text-bold\">Project Code  </td><td class=\\\"pre-wrap\\\" >"+entity.getProject_code()+"</td></tr>");
					}else {
					sb.append("<tr><td class=\"text-bold\">Project Code  </td><td class=\"pre-wrap\"></td></tr>");
					}
				if (entity.getProject_name() != null ) {
					sb.append("<tr><td class=\"text-bold\">Project Name  </td><td class=\\\"pre-wrap\\\">"+entity.getProject_name()+"</td></tr>");
					}else {
					sb.append("<tr><td class=\"text-bold\">Project Name  </td><td class=\"pre-wrap\"></td></tr>");
					}
		if (entity.getProject_info() != null ) {
		sb.append("<tr><td class=\"text-bold\">Project Information  </td><td class=\\\"pre-wrap\\\">"+entity.getProject_info()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Project Information  </td><td class=\"pre-wrap\"></td></tr>");
		}

		if (NewApplicationList != null ) {
		sb.append("<tr><td class=\"text-bold\">Application Type  </td><td class=\\\"pre-wrap\\\">"+NewApplicationList+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Application Type  </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (NewList != null ) {
			sb.append("<tr><td class=\"text-bold\">Architecture  </td><td class=\"pre-wrap\">"+NewList+"</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Architecture  </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (NewFrontendList != null ) {
			sb.append("<tr><td class=\"text-bold\">Front End  </td><td class=\"pre-wrap\">"+NewFrontendList+"</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Front End  </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (NewBackendList != null ) {
			sb.append("<tr><td class=\"text-bold\">Back End   </td><td class=\"pre-wrap\">"+NewBackendList+"</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Back End   </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (NewDatabaseList != null ) {
			sb.append("<tr><td class=\"text-bold\">Database  </td><td class=\\\"pre-wrap\\\">"+NewDatabaseList+"</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Database  </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (NewFrameworkList != null ) {
			sb.append("<tr><td class=\"text-bold\">Framework  </td><td class=\\\"pre-wrap\\\">"+NewFrameworkList+"</td></tr>");
			}else {
			sb.append("<tr><td class=\"text-bold\">Framework  </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getSvn_link() != null ) {
		sb.append("<tr><td class=\"text-bold\">SVN Link  </td><td class=\\\"pre-wrap\\\">"+entity.getSvn_link()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">SVN Link  </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (entity.getWar_filename() != null ) {
		sb.append("<tr><td class=\"text-bold\">War Filename  </td><td class=\\\"pre-wrap\\\">"+entity.getWar_filename()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">War Filename </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (entity.getOther_war_filename() != null ) {
		sb.append("<tr><td class=\"text-bold\">Other War Filename  </td><td class=\"pre-wrap\">"+entity.getOther_war_filename()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Other War Filename  </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (entity.getDomain_code() != null ) {
		sb.append("<tr><td class=\"text-bold\">Domain Code  </td><td class=\"pre-wrap\">"+entity.getDomain_code()+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Domain Code  </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (ProjectName != null ) {
		sb.append("<tr><td class=\"text-bold\">Parent Code  </td><td class=\"pre-wrap\">"+ProjectName+"</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Parent Code  </td><td class=\"pre-wrap\"></td></tr>");
		}
		if (entity.getRemark() != null) {
			sb.append("<tr><td class=\"text-bold\">Remark </td><td class=\"pre-wrap\">"+entity.getRemark()+"</td></tr>");
			}
			else {
			sb.append("<tr><td class=\"text-bold\">Remark </td><td class=\"pre-wrap\"></td></tr>");
			}
		if (entity.getProject_status() != null) {
		if (entity.getProject_status().equalsIgnoreCase("A")) {
		sb.append("<tr><td class=\"text-bold\">Project Status </td><td class=\"pre-wrap\">Active</td></tr>");
		}else {
		sb.append("<tr><td class=\"text-bold\">Project Status </td><td class=\"pre-wrap\">Inactive</td></tr>");
		}
		}else {
		sb.append("<tr><td class=\"text-bold\">Project Status </td><td class=\"pre-wrap\"></td></tr>");
		}
		

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private List<String> getCode(String architecture_type_code) {
		 String[] Code;
		 String Query="";
		 Map<String, String> menuList = new LinkedHashMap<>();
			List<String> list = new ArrayList<>();

		try {
			    menuList = getAllprojectArchitecture();
			    Code=architecture_type_code.split("[,]");
		        for (int j = 0; j <= Code.length - 1; j++) {
		        for (Map.Entry<String, String> entry : menuList.entrySet()) {
				  if(Code[j].equalsIgnoreCase(entry.getKey())){
				  list.add(entry.getValue()); 
				  }
		         }
		        }	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	private List<String> getApplicationCode(String application_type) {
		 String[] Code;
		 String Query="";
		 Map<String, String> menuList = new LinkedHashMap<>();
			List<String> list = new ArrayList<>();

		try {
			    menuList = getAllApplicationCodeName();
			    Code=application_type.split("[,]");
		        for (int j = 0; j <= Code.length - 1; j++) {
		        for (Map.Entry<String, String> entry : menuList.entrySet()) {
				  if(Code[j].equalsIgnoreCase(entry.getKey())){
				  list.add(entry.getValue()); 
				  }
		         }
		        }	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	private List<String> getFrontendCode(String frontend_type) {
		 String[] Code;
		 String Query="";
		 Map<String, String> menuList = new LinkedHashMap<>();
			List<String> list = new ArrayList<>();

		try {
			    menuList = getAllprojectFrontend();
			    Code=frontend_type.split("[,]");
		        for (int j = 0; j <= Code.length - 1; j++) {
		        for (Map.Entry<String, String> entry : menuList.entrySet()) {
				  if(Code[j].equalsIgnoreCase(entry.getKey())){
				  list.add(entry.getValue()); 
				  }
		         }
		        }	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	private List<String> getFrameworkCode(String application_type) {
		 String[] Code;
		 String Query="";
		 Map<String, String> menuList = new LinkedHashMap<>();
			List<String> list = new ArrayList<>();

		try {
			    menuList = getAllprojectFramework();
			    Code=application_type.split("[,]");
		        for (int j = 0; j <= Code.length - 1; j++) {
		        for (Map.Entry<String, String> entry : menuList.entrySet()) {
				  if(Code[j].equalsIgnoreCase(entry.getKey())){
				  list.add(entry.getValue()); 
				  }
		         }
		        }	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	private List<String> getDatabaseCode(String database_type) {
		 String[] Code;
		 String Query="";
		 Map<String, String> menuList = new LinkedHashMap<>();
			List<String> list = new ArrayList<>();

		try {
			    menuList = getAllprojectDatabase();
			    Code=database_type.split("[,]");
		        for (int j = 0; j <= Code.length - 1; j++) {
		        for (Map.Entry<String, String> entry : menuList.entrySet()) {
				  if(Code[j].equalsIgnoreCase(entry.getKey())){
				  list.add(entry.getValue()); 
				  }
		         }
		        }	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	private List<String> getBackendCode(String backend_type) {
		 String[] Code;
		 String Query="";
		 Map<String, String> menuList = new LinkedHashMap<>();
			List<String> list = new ArrayList<>();

		try {
			    menuList = getAllprojectBackend();
			    Code=backend_type.split("[,]");
		        for (int j = 0; j <= Code.length - 1; j++) {
		        for (Map.Entry<String, String> entry : menuList.entrySet()) {
				  if(Code[j].equalsIgnoreCase(entry.getKey())){
				  list.add(entry.getValue()); 
				  }
		         }
		        }	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public String getArchitectureName(String architectureCode) {
        String ArchitectureName = "";
        try {
        	ArchitectureName = projectRepo.getArchitectureName(architectureCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return ArchitectureName;
}
	public String getFrontendName(String FrontendCode) {
        String FrontendName = "";
        try {
        	FrontendName = projectRepo.getFrontendName(FrontendCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return FrontendName;
}
	public String getBackendName(String BackendCode) {
        String BackendName = "";
        try {
        	BackendName = projectRepo.getBackendName(BackendCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return BackendName;
}
	public String getDatabaseName(String DatabaseCode) {
        String DatabaseName = "";
        try {
        	DatabaseName = projectRepo.getDatabaseName(DatabaseCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return DatabaseName;
}
	public String getFrameworkName(String FrameworkCode) {
        String FrameworkName = "";
        try {
        	FrameworkName = projectRepo.getFrameworkName(FrameworkCode);
        } catch (Exception e) {
                e.printStackTrace();
        }
        return FrameworkName;
}


	@Override
	public Map<String, String> getDeplTypeList() {
		Map<String, String> depl_type_list = new HashMap<>();
		
		try {
			String queryStr = "select depl_code, depl_name  from  VIEW_DEPLOYMENT_TYPE " ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				depl_type_list.put((String) obj[0], (String) obj[1]);
			}
			depl_type_list=execute.sortTheList(depl_type_list);	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
//		System.out.println("depl_type_list>>"+depl_type_list);

		return (depl_type_list != null && depl_type_list.size() > 0) ? depl_type_list : null;
	}


	@Override
    public List<ProjectMast> getProjectList() {
            // TODO Auto-generated method stub
            //List<ProjectMast> list = new ArrayList<ProjectMast>();
            List<ProjectMast> list = new ArrayList<>();
            String Query = "";

            try {

                    Query = "select pm.project_code, pm.project_name, pm.project_info, pm.project_type,\r\n" + 
                                    "              (select p.application_type_name  from view_application_type p where pm.application_type = p.application_type_code) application_type,\r\n" + 
                                    "                (select e.architecture_type_name  from view_architecture_type e where pm.architecture_type_code = e.architecture_type_code) architecture_type_code,\r\n" + 
                                    "                (select a.frontend_type_name  from view_frontend_type a where pm.frontend_type_code = a.frontend_type_code) frontend_type_code,\r\n" + 
                                    "                (select b.backend_type_name  from view_backend_type b where pm.backend_type_code = b.backend_type_code) backend_type_code,\r\n" + 
                                    "               (select c.database_type_name  from view_database_type c where pm.database_type_code = c.database_type_code) database_type_code,\r\n" + 
                                    "                (select d.framework_type_name  from view_framework_type d where pm.framework_type_code  = d.framework_type_code ) framework_type_code,\r\n" + 
                                    "                      pm.svn_link,\r\n" + 
                                    "                      pm.war_filename,\r\n" + 
                                    "                        pm.other_war_filename,\r\n" + 
                                    "                       pm.domain_code,\r\n" + 
                                    "       pm.parent_code,  pm.project_status,\r\n" + 
                                    "                      pm.remark,\r\n" + 
                                    "                        pm.lastupdate,\r\n" + 
                                    "                       pm.flag,\r\n" + 
                                    "                pm.connected_db_user \r\n" + 
                                    "         from  project_mast pm \r\n" + 
                                    "           WHERE 1=1";
            
// System.out.println("Query........"+Query);        
                    list = executer.executeSQLQuery4(Query);

            } catch (Exception e) {
                    e.printStackTrace();
            }
            return list;
    }


	@Override
	public Map<String, String> getProjectListWithStatus() {
		Map<String, String> projectList = new HashMap<>();
		try {
			String queryStr = "select project_code, project_name  from  project_mast where project_status='A'" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (projectList != null && projectList.size() > 0) ? projectList : null;
	}


	@Override
	public Map<String, String> getModuleListWithStatus() {
		Map<String, String> projectmoduleList = new HashMap<>();
		try {
			String queryStr = "select module_code, module_name  from  project_module_mast where module_status='A'" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectmoduleList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (projectmoduleList != null && projectmoduleList.size() > 0) ? projectmoduleList : null;
	}


	@Override
	public Map<String, String> getProjectMenuListWithStatus() {

		Map<String, String> projectMenuList = new HashMap<>();
		try {
			String queryStr = "select menu_code, menu_name  from  project_menu_mast where menu_status='A'" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectMenuList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (projectMenuList != null && projectMenuList.size() > 0) ? projectMenuList : null;
	}


	@Override
	public Map<String, String> getModuleListWithStatus(String projectCode) {
		Map<String, String> projectmoduleList = new HashMap<>();
		try {
			String queryStr = " select module_code, module_name from project_module_mast a , project_mast b\r\n" + 
					" where a.project_code = '"+projectCode + 
					"' and  b.project_status='A'\r\n" + 
					" and a.module_status='A'" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectmoduleList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (projectmoduleList != null && projectmoduleList.size() > 0) ? projectmoduleList : null;
	}


//	@Override
//	public Map<String, String> getProjectMenuListWithStatus(String projectCode, String moduleCode) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Map<String, String> getProjectMenuListWithStatus(String projectCode, String moduleCode) {

		Map<String, String> projectMenuList = new HashMap<>();
		try {
			String queryStr = " select menu_code, menu_name from project_menu_mast a ,project_module_mast b \r\n" + 
					" where a.module_code = '"+moduleCode+"'and b.module_code='"+moduleCode+"' and  b.module_status='A'\r\n" + 
					" and a.menu_status='A'" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectMenuList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (projectMenuList != null && projectMenuList.size() > 0) ? projectMenuList : null;
	}


	@Override
	public List<ProjectMast> getProjectListforClientForm(String app_type) {
	//	System.out.println("Application Type.........."+app_type);
		 List<ProjectMast> list = new ArrayList<>();
         String Query = "";

         try {

                 Query = "select pm.project_code,\r\n" + 
                 		"       pm.project_info,\r\n" + 
                 		"       pm.project_type,\r\n" + 
                 		"       pm.architecture_type_code,\r\n" + 
                 		"       pm.frontend_type_code,\r\n" + 
                 		"       pm.backend_type_code,\r\n" + 
                 		"       pm.database_type_code,\r\n" + 
                 		"       pm.framework_type_code,\r\n" + 
                 		"       pm.svn_link,\r\n" + 
                 		"       pm.war_filename,\r\n" + 
                 		"       pm.other_war_filename,\r\n" + 
                 		"       pm.domain_code,\r\n" + 
                 		"       pm.parent_code,\r\n" + 
                 		"       pm.project_status,\r\n" + 
                 		"       pm.remark,\r\n" + 
                 		"       pm.lastupdate,\r\n" + 
                 		"       pm.flag,\r\n" + 
                 		"       pm.connected_db_user,\r\n" + 
                 		"       pm.project_name,\r\n" + 
                 		"       (select p.application_type_name\r\n" + 
                 		"          from view_application_type p\r\n" + 
                 		"         where pm.application_type = p.application_type_code) application_type\r\n" + 
                 		"  from project_mast pm\r\n" + 
                 		" where pm.application_type = '"  +app_type+ "'";
         
        //       System.out.println("Query........"+Query);        
                 list = executer.executeSQLQuery4(Query);
// System.out.println("list............"+list);
         } catch (Exception e) {
                 e.printStackTrace();
         }
         return list;
	}


	@Override
    public Map<String, String> getAllConnDatabaseUser() {
            HashMap<String, String> processtype = new HashMap<>();
            try {
                    String sqlQuery = "select db_object_name from lhs_taxcpc_db_objects where object_type ='USER'";

                   
                    ArrayList<ArrayList<String>> queryList = executer.executeSQLQueryAsListOfList(sqlQuery);
                    if (queryList != null && !queryList.isEmpty()) {
                            queryList.forEach(header -> {
                                    processtype.put(header.get(0), header.get(0));
                            });
                           
                            return processtype;
                    }
                    
            } catch (NullPointerException e) {
    			e.printStackTrace();
    		}catch (Exception e) {
                    e.printStackTrace();
            }
            return null;
    }


	@Override
	public long getprojectDictionaryCount(FilterDTO filter) {
		
		return projectDictRepoSupport.getprojectDictBrowserCount(filter);
	}


	@Override
	public List<TaxcpcDictionaryDevCodeHelp> getprojectDictionaryBrowserList(FilterDTO filter,
			DataGridDTO dataGridDTO, String entry_type) {
		List<TaxcpcDictionaryDevCodeHelp> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
//		System.out.println("dataGridDTO.getPaginator()..." + dataGridDTO.getPaginator());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
				
				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();
				

				list = projectDictRepoSupport.getprojectDictBrowserList(filter, minVal, maxVal, entry_type);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}


	@Override
	public int getDictionarycount(String project_code, String dict_name) {
		int count= 0;
		try {
			String Query = "select count(*) from taxcpc_dictionary_dev_codehelp where 1=1 ";
			

			if (!project_code.isEmpty()) {
				Query = Query + " and project_code='" +project_code+ "'";
			}
			if (!dict_name.isEmpty()) {
				Query = Query + " and lower (keyword_title_question) like lower('%" +dict_name+ "%')";
			}
			
			System.out.println("Query..."+Query);
			
			count = executer.getRowCount(Query);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}


	@Override
	public List<TaxcpcDictionaryDevCodeHelp> getSearchProjectDictionary(String project_code, String dict_name) {


		List<TaxcpcDictionaryDevCodeHelp> list = new ArrayList<TaxcpcDictionaryDevCodeHelp>();
		String Query = "";

		try {
	
			Query = "select * from taxcpc_dictionary_dev_codehelp where 1=1 ";

			if (!project_code.isEmpty()) {
				Query = Query + " and project_code= '"+project_code+"'";
			}
			if (!dict_name.isEmpty()) {
				Query = Query + "and lower (keyword_title_question) like lower('%" +dict_name+ "%')";
			}
			
			System.out.println("Query..."+Query);
		//	list = executer.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}



	@Override

	public String getfileName(Integer seq_id) {
		//Long seq_id;

		String filePathList = "";
		try {

			filePathList = projectDictionaryRepo.getFileName(seq_id);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return filePathList;
	}


	@Override
	public Map<String, String> getArchitectureList(String application_type) {
		Map<String, String> archList = new HashMap<>();
		try {
			String queryStr = null;

			if(!strUtl.isNull(application_type) && application_type.equalsIgnoreCase("apptype_01")) {
		    queryStr = "select architecture_type_code, architecture_type_name from view_architecture_type  where status_flag = 'A'";
		    	
			}else if(!strUtl.isNull(application_type) && application_type.equalsIgnoreCase("apptype_02")) {
			    queryStr = "select architecture_type_code, architecture_type_name from view_architecture_type  where status_flag = 'J'" ;

			}else if(!strUtl.isNull(application_type) && application_type.equalsIgnoreCase("apptype_03")) {
			    queryStr = "select architecture_type_code, architecture_type_name from view_architecture_type  where status_flag = 'W'" ;

			}
			System.out.println("query in bview...."+queryStr);
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				archList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (archList != null && archList.size() > 0) ? archList : null;
	}


	@Override
	public Map<String, String> getSvnLink() {
		Map<String, String> projectMenuList = new HashMap<>();
		try {
			String queryStr = "select parameter_name , parameter_value from lhssys_parameters where parameter_name='SVN_PROJECT'" ;
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				projectMenuList.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (projectMenuList != null && projectMenuList.size() > 0) ? projectMenuList : null;
	}


	@Override
	public Map<String, String> getAppserverPort(String serverip) {
		Map<String, String> serverport = new HashMap<>();
		try {
			String queryStr = "select server_ip, app_server_port from lhs_taxcpc_server_details where server_ip='"+serverip+"' AND  configuration_type='ASC' AND app_server_port is not null" ;
			System.out.println("query for port........"+queryStr);
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				serverport.put((String) obj[0],(String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (serverport != null && serverport.size() > 0) ? serverport : null;
	}


	
	
}
