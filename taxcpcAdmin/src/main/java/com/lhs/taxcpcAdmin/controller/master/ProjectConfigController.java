package com.lhs.taxcpcAdmin.controller.master;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.google.common.net.MediaType;
import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.TaxcpcDictionaryDevCodeHelpRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectMenuMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectModuleMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.service.ProjectMastService;

import lombok.var;
@PropertySource("classpath:application.properties")
@Controller
public class ProjectConfigController {

	@Autowired
	ProjectMastService projectService;

	@Autowired
	GlobalDoWorkExecuter executer;

	@Autowired
	EntityManager entityManager;
	
	@Autowired 
	TaxcpcDictionaryDevCodeHelpRepository dicRepo;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@Autowired
    private Environment env;


	public ProjectConfigController(ProjectMastService projectService) {
		super();
		this.projectService = projectService;
	}

	@RequestMapping("/projectDetail")
	public String projectDetail(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {

		
		session.setAttribute("ACTIVE_TAB", "MENU-008");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-009");

		
		List<ProjectMast> list = new ArrayList<ProjectMast>();
		Map<String, String> projectInfo = new HashMap<>();
		Map<String, String> projectList = new HashMap<>();
//		Map<String, String> applicationList = new HashMap<>();
//	    Map<String, String> projectFrameworkList = new HashMap<>();
//		Map<String, String> projectDatabaseList = new HashMap<>();
//		Map<String, String> projectArchitectureList = new HashMap<>();
		try {
			list = projectService.getProjectList();
			projectInfo = projectService.getAllProjectInfoName();
			projectList = projectService.getAllProjectCodeName();
//			applicationList = projectService.getAllApplicationCodeName();
//			projectFrameworkList = projectService.getAllprojectFramework();
//			projectDatabaseList = projectService.getAllprojectDatabase();
//			projectArchitectureList = projectService.getAllprojectArchitecture();

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("projectDetailGrid", list);
		map.addAttribute("projectList", projectList);
		map.addAttribute("projectInfo", projectInfo);
//		map.addAttribute("applicationList", applicationList);
//		map.addAttribute("projectFrameworkList", projectFrameworkList);
//		map.addAttribute("projectDatabaseList", projectDatabaseList);
//		map.addAttribute("projectArchitectureList", projectArchitectureList);
		return "pages/projectMgmt/projectDetail";
	}

	@RequestMapping(value = "/getArchitecture_type")
	@ResponseBody
	public Map<String, String> getArchitecture_type(@RequestParam("application_type") String application_type,HttpSession session) {
		Map<String, String> projectArchitectureList = new HashMap<>();
             System.out.println("application_type....."+application_type);
		try {

			projectArchitectureList = projectService.getArchitectureList(application_type);
			
            System.out.println("ArchList....."+projectArchitectureList);

			Collection<String> value = projectArchitectureList.values();

			// System.out.println("moduleList" + moduleList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectArchitectureList;
	}
	
	
	@RequestMapping("/projectEntry")
	public String projectEntry(Model map) {
		String action = "save";
		Map<String, String> projectInfo = new HashMap<>();
		Map<String, String> parentCodeList = new HashMap<>();
		Map<String, String> connUserList = new HashMap<>();
		Map<String, String> applicationList = new HashMap<>();
		Map<String, String> projectArchitectureList = new HashMap<>();
		Map<String, String> projectBackendList = new HashMap<>();
		Map<String, String> projectDatabaseList = new HashMap<>();
		Map<String, String> projectFrontendList = new HashMap<>();
		Map<String, String> projectFrameworkList = new HashMap<>();
		Map<String, String> svn_link = new HashMap<>();

		try {
			parentCodeList = projectService.getAllProjectCodeName();
			projectInfo = projectService.getAllProjectInfoName();
			connUserList = projectService.getAllConnDatabaseUser();
			applicationList = projectService.getAllApplicationCodeName();
			projectArchitectureList = projectService.getAllprojectArchitecture();
			projectBackendList = projectService.getAllprojectBackend();
			projectDatabaseList = projectService.getAllprojectDatabase();
			projectFrontendList = projectService.getAllprojectFrontend();
			projectFrameworkList = projectService.getAllprojectFramework();
			svn_link=projectService.getSvnLink();
			System.out.println("svn_link...."+svn_link);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("action", action);
		map.addAttribute("parentCodeList", parentCodeList);
		map.addAttribute("connUserList", connUserList);
		map.addAttribute("projectInfo", projectInfo);
		map.addAttribute("applicationList", applicationList);
		map.addAttribute("projectArchitectureList", projectArchitectureList);
		map.addAttribute("projectBackendList", projectBackendList);
		map.addAttribute("projectDatabaseList", projectDatabaseList);
		map.addAttribute("projectFrontendList", projectFrontendList);
		map.addAttribute("projectFrameworkList", projectFrameworkList);
		map.addAttribute("svn_link", svn_link);


		return "pages/projectMgmt/projectEntry";
	}

	@RequestMapping("/projectModuleDetailsEntry")
	public String projectModuleDetailsEntry(Model map) {
		String action = "save";
		Map<String, String> projectList = new HashMap<>();
		try {
			projectList = projectService.getAllProjectCodeName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("action", action);
		map.addAttribute("projectList", projectList);
		return "pages/projectMgmt/projectModuleEntry";
	}

	
	@RequestMapping(value = "/getProjectModuleFromProjectName")
	@ResponseBody
	public Map<String, String> getProjectModuleFromProjectName(@RequestParam("projectCode") String projectCode,HttpSession session) {
		Map<String, String> moduleList = new HashMap<>();
		Map<String, String> projectParentMenu = new HashMap<>();

		try {
			session.setAttribute("projectCode", projectCode);

//			System.out.println("projectCode");
			moduleList = projectService.getModuleListWithStatus(projectCode);
			
			projectParentMenu = projectService.getAllParentMenuName1(projectCode);

//			moduleList= projectService.getProjectList1();

			Collection<String> value = moduleList.values();
			Collection<String> values = projectParentMenu.values();

			// System.out.println("moduleList" + moduleList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return moduleList;
	}
	
	@RequestMapping("/projectMenuDetailsEntry")
	public String projectMenuDetailsEntry(Model map,HttpSession session) {
		String project_code  = (String) session.getAttribute("projectCode");

		String action = "save";
		Map<String, String> projectmoduleList = new HashMap<>();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> projectParentMenu = new HashMap<>();
		Map<String, String> submenuType = new HashMap<>();
		try {
			projectmoduleList = projectService.getModuleListWithStatus();
			projectList = projectService.getProjectListWithStatus();
			projectParentMenu = projectService.getAllParentMenuName();

			submenuType = projectService.getAllSubMenuType();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("action", action);
		map.addAttribute("projectmoduleList", projectmoduleList);
		map.addAttribute("projectList", projectList);
		map.addAttribute("projectParentMenu", projectParentMenu);
		map.addAttribute("submenuType", submenuType);
		
		return "pages/projectMgmt/projectMenuEntry";
	}
	
	
	
	@RequestMapping("/saveProjectDetail")
	@ResponseBody
	public String saveProjectDetail(ProjectMast entity) {
		System.out.println("entity in project..."+entity);
		// System.out.println("call controller1");
		// System.out.println("PROJECT MAST ENTITY"+entity);
		String response = "error";
		String action = "";
		entity.setLastupdate(new Date());
		entity.setDomain_code("TDS");
		String project_type=null;
		String Architechture_type=null;
		String Frontend_code=null;
		String Backend_code=null;
		String database_code=null;
		String Framework=null;
		String link=null;
		   if(!strUtl.isNull(entity.getProject_type())) {
				 project_type=entity.getProject_type().replace(",", "#");
		   }
		   if(!strUtl.isNull(entity.getArchitecture_type_code())) {
			 Architechture_type=entity.getArchitecture_type_code().replace(",", "#");
		   } 
		   if(!strUtl.isNull(entity.getFrontend_type_code())) {
		 Frontend_code=entity.getFrontend_type_code().replace(",", "#");
	   
		   }
		   if(!strUtl.isNull(entity.getBackend_type_code())) {
				Backend_code=entity.getBackend_type_code().replace(",", "#"); 
	   
		   }
		   if(!strUtl.isNull(entity.getDatabase_type_code())) {
			database_code=entity.getDatabase_type_code().replace(",", "#"); 
   
		   }
		   if(!strUtl.isNull(entity.getFramework_type_code())) {
			Framework=entity.getFramework_type_code().replace(",", "#"); 
   
		   }
		   
		   if(!strUtl.isNull(entity.getSvn_link())) {
			   link=entity.getSvn_link().replace(",", "");
		   }
		  
		
		try {
			if (entity.getProject_code().isEmpty()) {
				action = "add";
			}
			
			
			// System.out.println("call controller2");
			entity.setProject_type(project_type);
			entity.setArchitecture_type_code(Architechture_type);
		    entity.setFrontend_type_code(Frontend_code);
			entity.setBackend_type_code(Backend_code);
			entity.setDatabase_type_code(database_code);
			entity.setFramework_type_code(Framework);
			entity.setSvn_link(link);
			response = projectService.saveProjectDetail(entity);
			
			// System.out.println("call controller3");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("saveProjectMenu")
	@ResponseBody
	public String saveProjectMenuDetail(ProjectMenuMast entity) {
		// System.out.println("entity" + entity);
		String response = "error";
		String action = "";
		entity.setLastupdate(new Date());
		String sub_menu=null;
		  if(!strUtl.isNull(entity.getSub_menu_type())) {
			  sub_menu=entity.getSub_menu_type().replace(",", "#"); 
			  System.out.println("sub_menu...."+sub_menu);
	   
			   }
		
		try {
			if (entity.getMenu_code().isEmpty()) {
				action = "add";
			}
			entity.setSub_menu_type(sub_menu);
          response = projectService.saveProjectMenu(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("saveProjectModuleDetail")
	@ResponseBody
	public String saveProjectModuleDetail(ProjectModuleMast entity) {
		String response = "error";
		String action = "";
		entity.setLastupdate(new Date());
		if (entity.getModule_code().isEmpty()) {
			action = "add";
		}
		try {
			response = projectService.saveProjectModule(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/projectModuleEntry")
	public String projectModuleEntry(Model map) {
		Map<String, String> projectList = new HashMap<>();
		try {
			projectList = projectService.getAllProjectCodeName();
		} catch (Exception e) {

			e.printStackTrace();
		}

		map.addAttribute("projectList", projectList);
		return "pages/projectMgmt/projectModuleDetails";
	}

//	@RequestMapping("saveProjectModule")
//	@ResponseBody
//	public String saveProjectModule(ProjectModuleMast entity) {
//		String response = "error";
//		entity.setLastupdate(new Date());
//		try {
//			response = projectService.saveProjectModule(entity);
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		return response;
//	}

	@RequestMapping("/getProjectModule")
	@ResponseBody
	public Map<String, String> getProjectModule(@RequestParam("projectCode") String projectCode) {
		Map<String, String> moduleList = new HashMap<>();
		try {

			moduleList = projectService.getModulesOnProjectCode(projectCode);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return moduleList;
	}

	@RequestMapping("/getProjectMenu")
	@ResponseBody
	public Map<String, String> getProjectMenu(@RequestParam("projectCode") String projectCode,
			@RequestParam("moduleCode") String moduleCode) {
		Map<String, String> menuList = new HashMap<>();
		try {

			menuList = projectService.getMenuOnProjectCode(projectCode, moduleCode);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return menuList;
	}

	@RequestMapping("/getWarFileName")
	@ResponseBody
	public String getWarFileName(@RequestParam("projectCode") String projectCode) {
		String warFilename = null;
		try {
			warFilename = projectService.getWarFileNameOnProjectCode(projectCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return warFilename;
	}

	@RequestMapping("/ModuleEntryForm")
	public String ModuleEntryForm(HttpServletRequest request, Model map, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		
		session.setAttribute("ACTIVE_TAB", "MENU-008");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-010");
		
		List<ProjectModuleMast> list = new ArrayList<>();
		Map<String, String> moduleNameList = new HashMap<>();
		Map<String, String> projectCodeList = new HashMap<>();

		try {
			list = projectService.getProjectModuleList();
			moduleNameList = projectService.getAllProjectModuleName();
			projectCodeList = projectService.getAllProjectCodeName();

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("projectModuleDetailGrid", list);
		map.addAttribute("moduleNameList", moduleNameList);
		map.addAttribute("projectCodeList", projectCodeList);

		return "pages/projectMgmt/projectModuleDetails";
	}

	@RequestMapping("/projectMenuDetails")
	public String projectMenuDetails(HttpServletRequest request, Model map, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		
		session.setAttribute("ACTIVE_TAB", "MENU-008");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-011");
		
		List<ProjectMenuMast> list = new ArrayList<ProjectMenuMast>();
		Map<String, String> menuName = new HashMap<>();
		Map<String, String> menuType = new HashMap<>();
		Map<String, String> submenuType = new HashMap<>();
		Map<String, String> projectCodeList = new HashMap<>();
		Map<String, String> module_name = new HashMap<>();

		try {
			list = projectService.getProjectMenuList();
			menuName = projectService.getAllMenuName();
			menuType = projectService.getAllMenuType();
			submenuType = projectService.getAllSubMenuType();
			projectCodeList = projectService.getAllProjectCodeName();
			module_name=projectService.getModuleList1();
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("projectCodeList", projectCodeList);
		map.addAttribute("projectMenuDetail", list);
		map.addAttribute("menuName", menuName);
		map.addAttribute("menuType", menuType);
		map.addAttribute("submenuType", submenuType);
		map.addAttribute("module_name", module_name);

		return "pages/projectMgmt/projectMenuDetails";
	}

	@RequestMapping("/editprojectEntry")
	public String editprojectEntry(HttpServletRequest request, Model map,
			@RequestParam(name = "project_code", required = false) String project_code) {
		String action = "edit";

		ProjectMast projectEntity = new ProjectMast();
		Map<String, String> connUserList = new HashMap<>();
		Map<String, String> parentCodeList = new HashMap<>();
		Map<String, String> projectInfo = new HashMap<>();
		Map<String, String> applicationList = new HashMap<>();
		Map<String, String> projectArchitectureList = new HashMap<>();
		Map<String, String> projectBackendList = new HashMap<>();
		Map<String, String> projectDatabaseList = new HashMap<>();
		Map<String, String> projectFrontendList = new HashMap<>();
		Map<String, String> projectFrameworkList = new HashMap<>();
		try {

			projectEntity = projectService.getProjectDetailsFromProjectCode(project_code);
			connUserList = projectService.getAllConnDatabaseUser();
			parentCodeList = projectService.getProjectList1();
			projectInfo = projectService.getAllProjectInfoName();
			applicationList = projectService.getAllApplicationCodeName();
			projectArchitectureList = projectService.getAllprojectArchitecture();
			projectBackendList = projectService.getAllprojectBackend();
			projectDatabaseList = projectService.getAllprojectDatabase();
			projectFrontendList = projectService.getAllprojectFrontend();
			projectFrameworkList = projectService.getAllprojectFramework();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("action", action);
		map.addAttribute("projectEntity", projectEntity);
		map.addAttribute("parentCodeList", parentCodeList);
		map.addAttribute("connUserList", connUserList);
		map.addAttribute("applicationList", applicationList);
		map.addAttribute("projectArchitectureList", projectArchitectureList);
		map.addAttribute("projectBackendList", projectBackendList);
		map.addAttribute("projectDatabaseList", projectDatabaseList);
		map.addAttribute("projectFrontendList", projectFrontendList);
		map.addAttribute("projectFrameworkList", projectFrameworkList);
		map.addAttribute("projectInfo", projectInfo);

		return "pages/projectMgmt/projectEntry";
	}

	@RequestMapping("/editProjectModule")
	public String editProjectModule(HttpServletRequest request, Model map,
			@RequestParam(name = "module_code", required = false) String module_code) {
		String action = "edit";
		String projectName = "";
		List<ProjectModuleMast> list = new ArrayList<>();
		ProjectModuleMast projectModule = new ProjectModuleMast();
		Map<String, String> projectList = new HashMap<>();
		try {
			list = projectService.getProjectModuleList();
			projectModule = projectService.getModuleDetailsFromProjectCode(module_code);
			projectList = projectService.getProjectList1();
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("projectModuleDetailGrid", list);
		map.addAttribute("action", action);
		map.addAttribute("moduleEntity", projectModule);
		map.addAttribute("projectList", projectList);
		return "pages/projectMgmt/projectModuleEntry";
	}

	@RequestMapping("/editProjectMenu")
	public String editProjectMenu(HttpServletRequest request, Model map,
			@RequestParam(name = "menu_code", required = false) String menu_code) {
		String action = "edit";
		ProjectMenuMast projectMenuMast = new ProjectMenuMast();
		System.out.println("menu_code.." + menu_code);
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> projectmoduleList = new HashMap<>();
		Map<String, String> projectParentMenu = new HashMap<>();
		Map<String, String> submenuType = new HashMap<>();
		try {
			projectMenuMast = projectService.getMenuDetailsFromProjectCode(menu_code);
			projectList = projectService.getAllProjectCodeName();
			projectmoduleList = projectService.getAllProjectModuleName();
			projectParentMenu = projectService.getAllParentMenuName();
			submenuType = projectService.getAllSubMenuType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("action", action);
		map.addAttribute("projectMenuDetail", projectMenuMast);
		map.addAttribute("projectList", projectList);
		map.addAttribute("projectmoduleList", projectmoduleList);
		map.addAttribute("projectParentMenu", projectParentMenu);
		map.addAttribute("submenuType", submenuType);
		return "pages/projectMgmt/projectMenuEntry";

	}

	@RequestMapping("deleteprojectEntry")
	@ResponseBody
	public String deleteprojectEntry(HttpServletRequest request, Model map,
			@RequestParam(name = "project_code", required = false) String project_code) {
		String response = "error";
		ProjectMast projectmastList = null;

		try {
			projectmastList = (ProjectMast) projectService.getProjectDetailsFromProjectCode(project_code);
			response = projectService.deleteProjectDetail(projectmastList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	@RequestMapping("deleteprojectModuleEntry")
	@ResponseBody
	public String deleteprojectModuleEntry(HttpServletRequest request, Model map,
			@RequestParam(name = "module_code", required = false) String module_code) {
		String response = "error";
		ProjectModuleMast projectModule = null;
		try {
			projectModule = (ProjectModuleMast) projectService.getModuleDetailsFromProjectCode(module_code);
			response = projectService.deleteModuleDetails(projectModule);
		} catch (Exception e) {

		}
		return response;
	}

	@RequestMapping("deleteprojectMenuEntry")
	@ResponseBody
	public String deleteprojectMenuEntry(HttpServletRequest request, Model map,
			@RequestParam(name = "menu_code", required = false) String menu_code) {
		String response = "error";
		ProjectMenuMast projectMenu = null;
		try {
			projectMenu = projectService.getMenuDetailsFromProjectCode(menu_code);
			response = projectService.deleteMenuDetails(projectMenu);
		} catch (Exception e) {

		}
		return response;
	}

//	@RequestMapping("/projectDictionary")
//	public String projectDictionary(Model map, TaxcpcDictionaryDevCodeHelp entity, HttpSession session) {
//		session.setAttribute("ACTIVE_TAB", "MENU-008");
//		session.setAttribute("ACTIVE_SUB_TAB", "MENU-012");
//		List<TaxcpcDictionaryDevCodeHelp> projectDictList = new ArrayList<>();
//		String entry_type = "PD";
//		String filetype = (String) session.getAttribute("ext");
//		Map<String, String> projectList1 = new HashMap<>();
//
//		try {
//			projectDictList = projectService.getProjectDictionaryDetail(entry_type);
//			projectList1 = projectService.getProjectList1();
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		map.addAttribute("projectname", projectList1);
//		map.addAttribute("projectDictList", projectDictList);
//		map.addAttribute("filetype", filetype);
//		return "pages/projectDictionary/projectDictionaryDetail";
//	}
	
	@RequestMapping("/projectDictionary")
	public String projectDictionary(HttpServletRequest request, Model map, FilterDTO filter,TaxcpcDictionaryDevCodeHelp entity,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,HttpSession session) {
	
		session.setAttribute("ACTIVE_TAB", "MENU-008");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-012");
		
		List<TaxcpcDictionaryDevCodeHelp> projectDictList = new ArrayList<>();
		String entry_type = "PD";
		String filetype = (String) session.getAttribute("ext");
		Map<String, String> projectList1 = new HashMap<>();
		try {
			if (filter == null) {
				filter = new FilterDTO();
				System.out.println("filter1.." + filter);
			}
			projectDictList = projectService.getProjectDictionaryDetailList(entry_type);
			projectList1 = projectService.getProjectList1();
			long total = 10l;
			total = projectService.getprojectDictionaryCount(filter);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("projectDictionaryDetailGrid", total, recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			map.addAttribute("dataGridDTO", dataGridDTO);
			session.setAttribute("recPerPage", recPerPage);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("projectname", projectList1);
		map.addAttribute("projectDictList", projectDictList);
		map.addAttribute("filetype", filetype);
		
		return "pages/projectDictionary/projectDictionaryDetail";
	}

	@RequestMapping(value = "/projectDictionaryDetailGrid", method = RequestMethod.POST)
	public String projectDictionaryDetailGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		Map<String, String> projectList1 = new HashMap<>();
		List<TaxcpcDictionaryDevCodeHelp> projectDictList = new ArrayList<>();
		String entry_type = "PD";
		String filetype = (String) session.getAttribute("ext");
		
		try {
			
			String recPerPage = (String) session.getAttribute("recPerPage");
			projectDictList = projectService.getprojectDictionaryBrowserList(filter, dataGridDTO, entry_type);
			projectList1 = projectService.getProjectList1();
		long  pagenumber = dataGridDTO.getPaginator().getPageNumber();
			long total = 10l;
			total = projectService.getprojectDictionaryCount(filter);
			System.out.println("page_mode.."+total);
			dataGridDTO.setFilter(filter);
			
			PaginatorManipulation manipulation = new PaginatorManipulation();
			// System.out.println("recPerPage" + recPerPage);
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			
			dataGridDTO.getPaginator().setPageNumber(pagenumber);
			
			session.setAttribute("recPerPage", recPerPage);
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("projectname", projectList1);
		map.addAttribute("projectDictList", projectDictList);
		//map.addAttribute("filetype", filetype);
		
		return "pages/projectDictionary/projectDictionaryDetailGrid  :: ajaxprojectDictionary";
	}

	@RequestMapping("/searchProjectDictionary")
	public String searchProjectDictionary(HttpSession session, Model map,
			@RequestParam(name = "project_code") String project_code,@RequestParam(name = "dict_name") String dict_name) {
		List<TaxcpcDictionaryDevCodeHelp> list = new ArrayList<TaxcpcDictionaryDevCodeHelp>();
		
		DataGridDTO dataGridDTO = new DataGridDTO();
	
		int total = projectService.getDictionarycount(project_code,dict_name);
		System.out.println("total..."+total);
		System.out.println("page_mode.."+total);
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		PaginatorManipulation manipulation = new PaginatorManipulation();
		
		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);
		try {
			list = projectService.getSearchProjectDictionary(project_code,dict_name);
			
		} catch (Exception e) {

		}
		map.addAttribute("projectDictList", list);
		map.addAttribute("dataGridDTO", dataGridDTO);
	
		
		return "pages/projectDictionary/projectDictionaryDetailGrid  :: ajaxprojectDictionary";
	}
	
	

	@RequestMapping("/projectDictionaryEntry")
	public String projectDictionaryEntry(Model map, HttpSession session, @RequestParam(name = "seq", required = false) Integer seq_id) {
		Map<String, String> projectList = new HashMap<>();
		TaxcpcDictionaryDevCodeHelp entity = new TaxcpcDictionaryDevCodeHelp();
		
		String action = "save";
		String docfile = "";
		try {
			projectList = projectService.getAllProjectCodeName();
			
			if (seq_id != null) {
				action = "edit";
				entity = projectService.getDictionaryDataOnEntryType(seq_id);
				docfile=projectService.getfileName(seq_id);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 session.setAttribute("action", action);
		 session.setAttribute("seq_id", seq_id);
		map.addAttribute("projectList", projectList);
		map.addAttribute("entry_type", "PD");
		map.addAttribute("projectDictGrid", entity);
		map.addAttribute("action", action);
		map.addAttribute("docfile", docfile);
		return "pages/projectDictionary/projectDictionaryEntry";
	}

	@RequestMapping("saveProjectDictionary")
	@ResponseBody
	public String saveProjectDictionary(TaxcpcDictionaryDevCodeHelp entity, Model map, HttpSession session) {
		String fileName;
		String response = "error";
		Integer	seq_id =(Integer)session.getAttribute("seq_id");
		String action = (String) session.getAttribute("action");

		try {
			if (entity != null) {
				System.out.println("entity==" + entity.getKeyword_title_question());
						
				if (!strUtl.isNull(entity.getDoc_file().getOriginalFilename())) {
					
					System.out.println("not nullll"+entity.getDoc_file().getOriginalFilename());
				String file = entity.getDoc_file().getOriginalFilename();
				byte[] bfile = entity.getDoc_file().getBytes();

				 entity.setAttachment_name(file);
				entity.setAttachment(bfile);
			}
			if (action.equalsIgnoreCase("edit")) {
              if (strUtl.isNull(entity.getDoc_file().getOriginalFilename())) {
            	  Optional<TaxcpcDictionaryDevCodeHelp> entitylist = this.dicRepo.findById(seq_id);
					System.out.println("not nullll"+entitylist.get().getAttachment_name());
				String file = entitylist.get().getAttachment_name();
				byte[] bfile = entitylist.get().getAttachment();

				 entity.setAttachment_name(file);
				entity.setAttachment(bfile);
			}
			}
				entity.setLastupdate(new Date());
				entity.setEntry_type("PD");
				response = projectService.saveProjectDictionary(entity);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return response;
	}

	@RequestMapping("deleteDictionary")
	@ResponseBody
	public String deleteDictionary(@RequestParam(name = "seq") Integer seq_id) {
		String response = "error";
		try {
			if (seq_id != null) {
				response = projectService.deleteProjectDictionary(seq_id);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/viewDictionaryDetail")
	@ResponseBody
	public String viewDictionaryDetail(TaxcpcDictionaryDevCodeHelp entity, @RequestParam(name = "seq") Integer seq_id,
			HttpSession session, Model map) {

		List<TaxcpcDictionaryDevCodeHelp> projectDictList = new ArrayList<>();
		String entry_type = "PD";
		String response = "error";
		// Map<Long, String> projectfilename = new HashMap<>();
		List<Long> list = new ArrayList<>();
		List<String> list1 = new ArrayList<>();
//		String[] file;
//		String filetype = "";
//		String ext="";
		try {
			//projectDictList = projectService.getProjectDictionaryDetail(seq_id);
			if (seq_id != null) {

				response = projectService.viewProjectDictionary(seq_id);

				entity = projectService.getDictionaryDataOnEntryType(seq_id);
			}
				//projectfilename = projectService.getAllProjectFileName();

//				for (Map.Entry<Long, String> m : projectfilename.entrySet()) {
//					list.add(m.getKey());
//					list1.add(m.getValue());
//			
//				
//				int count = list.size();
//		
//        				if((m.getKey()).equals(seq_id)) {
//						String fName = m.getValue();
//						String[] splitedValue = fName.split("[.]");
//					
//					 ext = splitedValue[1];
//					
//						session.setAttribute("ext", ext);
//        				}
//                  }response = projectService.viewProjectDictionary(seq_id, ext);
//			}
//			filetype = (String) session.getAttribute("ext");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
	//	map.addAttribute("projectDictList", projectDictList);
		map.addAttribute("projectDictGrid", entity);

		return response; 
	}
	


	@RequestMapping("/viewprojectMenuDetails")
	@ResponseBody
	public String viewprojectMenuDetails(@RequestParam(name = "menu") String menu) {

		String response = "error";
		try {
			if (menu != null) {
				response = projectService.viewProjectMenuView(menu);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/viewprojectModuleDetails")
	@ResponseBody
	public String viewprojectModuleDetails(@RequestParam(name = "module") String module) {

		String response = "error";
		try {
			if (module != null) {
				response = projectService.viewProjectModuleView(module);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/viewprojectDetail")
	@ResponseBody
	public String viewprojectDetail(@RequestParam(name = "proj") String proj) {

		String response = "error";
		try {
			if (proj != null) {
				response = projectService.viewProjectView(proj);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/getSearch")
	public String getSearch(HttpSession session, Model map, @RequestParam(name = "modname") String modname,
			@RequestParam(name = "projcode") String projcode, @RequestParam(name = "projstat") String projstat) {

		List<ProjectModuleMast> list = new ArrayList<ProjectModuleMast>();
		Map<String, String> moduleNameList = new HashMap<>();
		Map<String, String> projectCodeList = new HashMap<>();
		try {
			list = projectService.getSearch(modname, projcode, projstat);
			moduleNameList = projectService.getAllProjectModuleName();
			projectCodeList = projectService.getAllProjectCodeName();

		} catch (Exception e) {
		}

		map.addAttribute("projectModuleDetailGrid", list);
		map.addAttribute("moduleNameList", moduleNameList);
		map.addAttribute("projectCodeList", projectCodeList);

		return "pages/projectMgmt/projectModuleDetails :: ajaxModuleList";
	}


	@RequestMapping("/getSearchProjectDetail")
	public String SearchProjectDetail(HttpSession session, Model map, @RequestParam(name = "projname") String projname,
			@RequestParam(name = "status") String status) {

		List<ProjectMast> list = new ArrayList<ProjectMast>();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> applicationList = new HashMap<>();
		Map<String, String> projectFrameworkList = new HashMap<>();
		Map<String, String> projectDatabaseList = new HashMap<>();
		Map<String, String> projectArchitectureList = new HashMap<>();
		try {
			list = projectService.getSearchProjectDetail(projname, status);
			projectArchitectureList = projectService.getAllprojectArchitecture();
			projectList = projectService.getAllProjectCodeName();
			applicationList = projectService.getAllApplicationCodeName();
			projectFrameworkList = projectService.getAllprojectFramework();
			projectDatabaseList = projectService.getAllprojectDatabase();

		} catch (Exception e) { // TODO: handle exception e.printStackTrace();

		}
		map.addAttribute("projectDetailGrid", list);
		map.addAttribute("projectList", projectList);
		map.addAttribute("applicationList", applicationList);
		map.addAttribute("projectFrameworkList", projectFrameworkList);
		map.addAttribute("projectDatabaseList", projectDatabaseList);
		map.addAttribute("projectArchitectureList", projectArchitectureList);
		return "pages/projectMgmt/projectDetail :: ajaxRoleList";
	}

	@RequestMapping("/getSearchProjectMenuDetail")
	public String SearchProjectMenuDetail(HttpSession session, Model map,
			@RequestParam(name = "menuname") String menuname, 
			@RequestParam(name = "menuStatus") String menuStatus,@RequestParam(name = "project_name") String project_name,@RequestParam(name = "module_Name") String module_Name)

	{
		System.out.println("menuname"+menuname); 
		System.out.println("module_Name"+module_Name);
//		System.out.println("menuStatus"+menuStatus);

		List<ProjectMenuMast> list = new ArrayList<ProjectMenuMast>();
		Map<String, String> MenuName = new HashMap<>();
		Map<String, String> MenuType = new HashMap<>();
		// Map<String, String> SubmenuType = new HashMap<>();
		try {
			list = projectService.getSearchProjectMenuDetail(menuname, menuStatus,project_name,module_Name);
			MenuName = projectService.getAllMenuName();
			MenuType = projectService.getAllMenuType();
			// SubmenuType = projectService.getAllSubMenuType();

		} catch (Exception e) { // TODO: handle exception e.printStackTrace();

		}
		map.addAttribute("projectMenuDetail", list);
		map.addAttribute("menuName", MenuName);
	    map.addAttribute("menuType", MenuType);
		// map.addAttribute("submenuType", SubmenuType);

		return "pages/projectMgmt/projectMenuDetails :: ajaxMenuList";
	}

	@RequestMapping("getdownloadSingleFile")
	public String downloadSingleFile(TaxcpcDictionaryDevCodeHelp entity, HttpServletResponse response,
			HttpSession session, @RequestParam(name = "seq_id") String seq_id) throws Exception {

		byte b[] = entity.getAttachment();
		try {
			String username = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");

			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.14:1521/orcl", username, password);
			Statement stmt = con.createStatement();
			String query = "SELECT attachment, attachment_name FROM taxcpc_dictionary_dev_codehelp WHERE seq_id = "
					+ seq_id;
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				File blobFile = new File(rs.getString("attachment_name"));
				Blob blob = rs.getBlob("attachment");
				InputStream in = blob.getBinaryStream();
				long length = in.available();
				byte[] blobBytes = new byte[(int) length];
				in.read(blobBytes);
				byte barr[] = blob.getBytes(1, (int) blob.length());
				response.setHeader("Content-Disposition", "attachment; filename=" + blobFile.getName());
				ServletOutputStream out = response.getOutputStream();
				out.write(barr);
				out.close();
				in.close();
				rs.close();
				stmt.close();
				con.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}

	@RequestMapping("getattachmentImage")
	public BufferedImage attachmentImage(TaxcpcDictionaryDevCodeHelp entity, Model map, HttpServletResponse response,
			HttpSession session, @RequestParam(name = "fileextention") String fileextention) throws Exception {
		
		System.out.println("fileextention" +fileextention);

		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}
