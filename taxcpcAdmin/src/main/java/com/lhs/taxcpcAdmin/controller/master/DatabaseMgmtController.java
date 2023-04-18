package com.lhs.taxcpcAdmin.controller.master;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbDetails;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbObjects;
import com.lhs.taxcpcAdmin.model.entity.LhssysMainTables;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.service.DatabaseMgmtService;


@Controller
public class DatabaseMgmtController {
	
	@Autowired
	DatabaseMgmtService databaseService;	
	
	@Autowired
	private LhsStringUtility strUtl;

	@RequestMapping(value = "/databaseDetails")
	public String databaseManagement(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		
		session.setAttribute("ACTIVE_TAB", "MENU-019");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-030");
		
	
	dataGridDTO.setFilter(filter);
	List<LhssysDbDetails> list = new ArrayList<LhssysDbDetails>();

	
	try {
		 list = databaseService.getDatabaseDetailList();
		 System.out.println("list====="+list);
		
	} catch (Exception e) {
	e.printStackTrace();	// TODO: handle exception
	}
	try {

		
		if (filter == null) {
			filter = new FilterDTO();

		}
		
		long total = 10l;
		total = databaseService.getdatabaseManagementDetailsCount(filter);
		System.out.println("Total:1234" + total);

		String recPerPage = "";
		if (dataGridDTO != null && dataGridDTO.getPaginator() != null
				&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
			recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();

		} else {
			recPerPage = "10";

		}

		PaginatorManipulation manipulation = new PaginatorManipulation();

		PaginatorDTO paginator = manipulation.getPaginatorCount("databaseManagementGrid", total, recPerPage);

		dataGridDTO.setFilter(filter);
		dataGridDTO.setPaginator(paginator);
		map.addAttribute("dataGridDTO", dataGridDTO);

		session.setAttribute("recPerPage", recPerPage);
		
	} catch (Exception e) {
		// TODO: handle exception
	}

	map.addAttribute("databaseDetailGrid", list);
	
	return "pages/databaseMgmt/databaseBrowser";
}

@RequestMapping(value = "/databaseManagementGrid", method = RequestMethod.POST)
public String databaseManagementGrid(Model map, HttpServletRequest request, HttpSession session,
		@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

	List<LhssysDbDetails> list = new ArrayList<LhssysDbDetails>();
	

	String recPerPage = "";
	
	try {
		dataGridDTO.setFilter(filter);
		recPerPage = (String) session.getAttribute("recPerPage");
		list = databaseService.getDatabaseManagementList(filter, dataGridDTO, recPerPage);
		System.out.println("list====="+list);
		long total = 10l;
		total = databaseService.getDatabaseManagementCount(filter);
		long slnoStartFrom = 0l;
		if (dataGridDTO != null) {
			if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
				slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
				slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
			}
			
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			long  pagenumber = dataGridDTO.getPaginator().getPageNumber();
			dataGridDTO.setPaginator(paginator);
			dataGridDTO.getPaginator().setPageNumber(pagenumber);
		
		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("databaseDetailGrid", list);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		return "pages/databaseMgmt/databaseBrowserGrid :: ajaxDatabaseManagementGrid";
		
}

	
	@RequestMapping(value = "/databaseEntry")
	public String databaseEntry(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		String action = "save";
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("action", action);
		
		return "pages/databaseMgmt/databaseEntry";
	}
	
	@RequestMapping("/saveDatabaseDetail")
	@ResponseBody
	public String saveDatabaseDetail(LhssysDbDetails entity) {
		
		String response = "error";
		String action = "";
		entity.setLastupdate(new Date());
		
		try {
			if (entity.getDb_code().isEmpty()) {
				action = "add";
			}
		
			response = databaseService.saveLhssysDbDetailsDetail(entity);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@RequestMapping("/editdatabaseEntry")
	public String editdatabaseEntry(HttpServletRequest request, Model map,
			@RequestParam(name = "db_code", required = false) String db_code) {
		String action = "edit";
		
		LhssysDbDetails LhssysDbDetailsEntity = new LhssysDbDetails();
		
		try { 
			if(db_code != null) {

			LhssysDbDetailsEntity = databaseService.getLhssysDbDetailsEntity(db_code);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("action", action);
		map.addAttribute("LhssysDbDetailsEntity", LhssysDbDetailsEntity);
		
		return "pages/databaseMgmt/databaseEntry";
	}
	
	@RequestMapping(value = "/databaseObjects") 
	  public String databaseObjects(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		session.setAttribute("ACTIVE_TAB", "MENU-019");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-031");
		List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();
		
		try {
			 list = databaseService.getDatabaseUserList();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	  try {
		 
		  if (filter == null) {
				filter = new FilterDTO();
			}

			long total = 10l;
			total = databaseService.getDatabaseUserCount(filter);
           System.out.println("count in controller==="+total);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("databaseUserGrid", total, recPerPage);
			
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			
			map.addAttribute("dataGridDTO", dataGridDTO);
			 map.addAttribute("databaseUserGrid", list);
	  } catch (Exception e) { e.printStackTrace(); }
	
	  return "pages/databaseMgmt/databaseUser";
	  }
	
	@RequestMapping(value = "/databaseUserGrid", method = RequestMethod.POST)
	public String databaseUserGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();
		String recPerPage = "";
		long total = 0l;

		try {
			total = databaseService.getDatabaseUserCount(filter);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	
		try {
			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");

			list = databaseService.getDatabaseUserGridList(filter, dataGridDTO, recPerPage, total);
			System.out.println(" controller list====="+list);
			long slnoStartFrom = 0l;
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				}
				
				PaginatorManipulation manipulation = new PaginatorManipulation();
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				long  pagenumber = dataGridDTO.getPaginator().getPageNumber();
				dataGridDTO.setPaginator(paginator);
				dataGridDTO.getPaginator().setPageNumber(pagenumber);

				
			}
			
			map.addAttribute("dataGridDTO", dataGridDTO);
			 map.addAttribute("databaseUserGrid", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "pages/databaseMgmt/databaseUserGrid  :: ajaxUserGrid";
	}

	
	  @RequestMapping(value = "/databasePackage") 
	  public String databasePackage(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		  List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();
		
	  try {
		  list = databaseService.getDatabasePackageList();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	  try {
		 
		  if (filter == null) {
				filter = new FilterDTO();
			}

			long total = 10l;
			total = databaseService.getDatabasePackageCount(filter);
            System.out.println("package count===="+total);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("databasePackageGrid", total, recPerPage);
			
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("databasePackageGrid", list);
	  } catch (Exception e) { 
		  e.printStackTrace(); 
		  }
	  
	
	  
	  return "pages/databaseMgmt/databasePackage";
	  }
	 
	  @RequestMapping(value = "/databasePackageGrid", method = RequestMethod.POST)
		public String databasePackageGrid(Model map, HttpServletRequest request, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

			//System.out.println("filterpppp=="+filter);
			 List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();

			String recPerPage = "";
			long total = 0l;

			try {
				total = databaseService.getDatabasePackageCount(filter);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		
			try {
				dataGridDTO.setFilter(filter);
				recPerPage = (String) session.getAttribute("recPerPage");
				 list = databaseService.getDatabasePackageDetailsList(filter, dataGridDTO, recPerPage, total);
				 System.out.println("list in package======="+list);
				long slnoStartFrom = 0l;
				if (dataGridDTO != null) {
					if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
						slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
						slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
					}
					
					PaginatorManipulation manipulation = new PaginatorManipulation();
					PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
					long  pagenumber = dataGridDTO.getPaginator().getPageNumber();
					dataGridDTO.setPaginator(paginator);
					dataGridDTO.getPaginator().setPageNumber(pagenumber);

					
				}
				
				map.addAttribute("dataGridDTO", dataGridDTO);
				map.addAttribute("databasePackageGrid", list);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return "pages/databaseMgmt/databasePackageGrid  :: ajaxDatabasePackageList";
		}

	  
//	  
//	  @RequestMapping(value = "/databaseSynonym") 
//	  
//	  public String databaseSynonym(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
//				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
//		  List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();
//	  try {
//		  list = databaseService.getDatabaseSynonymList();
//	  } catch (Exception e) { e.printStackTrace(); }
//	  
//	  map.addAttribute("databaseSynonymGrid", list);
//	  return "pages/databaseMgmt/databaseSynonym";
//	  }
//	  
	  
	  
	  @RequestMapping(value = "/databaseMainTable") 
	  public String databaseMainTable(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		  
		  session.setAttribute("ACTIVE_TAB", "MENU-019");
		  session.setAttribute("ACTIVE_SUB_TAB", "MENU-032");
		  
		  List<LhssysMainTables> list = new ArrayList<LhssysMainTables>();
		  Map<String, String> projectList = new HashMap<>();
		  
		  try {
			  list = databaseService.getDatabaseTableDetailList();
			  projectList = databaseService.getAllProjectCodeName();  
			
		} catch (Exception e) {
		e.printStackTrace();	// TODO: handle exception
		}
		  
	  try {
		  
		  
		  if (filter == null) {
				filter = new FilterDTO();
				System.out.println("filter1.." + filter);
			}
			long total = 10l;
			total = databaseService.getUserCount(filter);
			System.out.println("total in controller==="+total);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("databaseMainTableGrid", total, recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			session.setAttribute("recPerPage", recPerPage);

			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("databaseTableGrid", list);
			map.addAttribute("projectList", projectList);
		  
	  } catch (Exception e) { 
		  e.printStackTrace(); }
	  
	 
	  
	  return "pages/databaseMgmt/databaseTableBrowser";
	  }
	  
	  @RequestMapping(value = "/databaseMainTableGrid", method = RequestMethod.POST)
		public String databaseMainTableGrid(Model map, HttpServletRequest request, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {
		  
		  List<LhssysMainTables> list = new ArrayList<LhssysMainTables>();
		  Map<String, String> projectList = new HashMap<>();

		  String recPerPage = "";
		  try {
			 
			  //System.out.println("total123==="+total);
				 projectList = databaseService.getAllProjectCodeName();  

			 
			  
			  
		} catch (Exception e) {
		e.printStackTrace();	// TODO: handle exception
		}
		  
			try {
				dataGridDTO.setFilter(filter);
				long pagenumber = dataGridDTO.getPaginator().getPageNumber();
				long total = 10l;
				total = databaseService.getUserCount(filter);
				recPerPage = (String) session.getAttribute("recPerPage");
				list = databaseService.getDatabaseTableList(filter, dataGridDTO, recPerPage,projectList);
				System.out.println("list database......"+list);
				PaginatorManipulation manipulation = new PaginatorManipulation();
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);

				long slnoStartFrom = 0l;
				if (dataGridDTO != null) {
					if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
						slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
						slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
					}
				}

				dataGridDTO.setPaginator(paginator);
				dataGridDTO.getPaginator().setPageNumber(pagenumber);

				
				 map.addAttribute("databaseTableGrid", list);
				 map.addAttribute("projectList", projectList);
				 map.addAttribute("dataGridDTO", dataGridDTO);
				 map.addAttribute("slnoStartFrom", slnoStartFrom);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			 map.addAttribute("dataGridDTO", dataGridDTO);

			return "pages/databaseMgmt/databaseTableBrowserGrid :: ajaxDatabaseMainTableList";
		
		}

	  
	  @RequestMapping(value = "/databaseTableEntry") 
	  public String databaseTableEntry(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		  String action = "save";
		  Map<String, String> projectList = new HashMap<>();
	  try {
		    
		  projectList = databaseService.getAllProjectCodeName();
		  
	  } catch (Exception e) { e.printStackTrace(); }
	  
	  map.addAttribute("action", action);
	  map.addAttribute("projectList", projectList);
	  return "pages/databaseMgmt/databaseTableEntry";
	  }
	  

		@RequestMapping("/saveDatabaseMainTableDetail")
		@ResponseBody
		public String saveDatabaseMainTableDetail(LhssysMainTables entity) {
			
			String response = "error";
			String action = "";
			entity.setLastupdate(new Date());
			
			try {
				if (entity.getObject_code().isEmpty()) {
					action = "add";
				}
			
				response = databaseService.saveLhssysMainTableDetails(entity);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}
		
		@RequestMapping("/editdatabaseTableEntry")
		public String editdatabaseTableEntry(HttpServletRequest request, Model map,
				@RequestParam(name = "object_code", required = false) String object_code) {
			String action = "edit";
			
			LhssysMainTables LhssysMainTablesEntity = new LhssysMainTables();
			Map<String, String> projectList = new HashMap<>();
			try {

				LhssysMainTablesEntity = databaseService.getLhssysMainTablesEntity(object_code);
				projectList=databaseService.getAllProjectCodeName(); 
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			map.addAttribute("action", action);
			map.addAttribute("LhssysMainTablesEntity", LhssysMainTablesEntity);
			map.addAttribute("projectList", projectList);

			
			return "pages/databaseMgmt/databaseTableEntry";
		}
		
		@RequestMapping("/getSearchDatabaseDetail")
		public String SearchDatabaseDetail(HttpSession session, Model map, @RequestParam(name = "type_of_db") String type_of_db,
				@RequestParam(name = "db_ip") String db_ip) {

			List<LhssysDbDetails> list = new ArrayList<LhssysDbDetails>();
			int total = databaseService.getcountTableDatabase(type_of_db,db_ip);
			DataGridDTO dataGridDTO = new DataGridDTO();
			
			String recPerPage = (String) session.getAttribute("recPerPage");
			System.out.println("total=="+total);
			PaginatorManipulation manipulation = new PaginatorManipulation();

			try {
				list = databaseService.getSearchDatabaseDetail(type_of_db, db_ip);
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				dataGridDTO.setPaginator(paginator);
				
			
				map.addAttribute("dataGridDTO", dataGridDTO);
				
				System.out.println("list123====="+list);
			
			} catch (Exception e) { // TODO: handle exception e.printStackTrace();
            e.printStackTrace();
			}
			map.addAttribute("databaseDetailGrid", list);
			
			return "pages/databaseMgmt/databaseBrowserGrid :: ajaxDatabaseManagementGrid";
		}


		@RequestMapping("/getSearchDatabaseTableFilter")
		public String SearchDatabaseTableFilter(HttpSession session, Model map, @RequestParam(name = "table_or_view_name") String table_or_view_name,
				@RequestParam(name = "object_type") String object_type,
				@RequestParam(name = "project_code") String project_code) {
			
			System.out.println("project_code............"+project_code);

			List<LhssysMainTables> list = new ArrayList<LhssysMainTables>();
			int total = databaseService.getcountText(table_or_view_name, object_type, project_code);
			DataGridDTO dataGridDTO = new DataGridDTO();

			String recPerPage = (String) session.getAttribute("recPerPage");
			System.out.println("recPerPage in filter==="+recPerPage);
			PaginatorManipulation manipulation = new PaginatorManipulation();
			
		
			try {
				
				list = databaseService.getSearchDatabaseTableDetail(table_or_view_name, object_type, project_code);
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				dataGridDTO.setPaginator(paginator);
				map.addAttribute("dataGridDTO", dataGridDTO);
				map.addAttribute("databaseTableGrid", list);

			 System.out.println("list in filter======"+list);
			} catch (Exception e) { // TODO: handle exception e.printStackTrace();
            e.printStackTrace();
			}

 			return "pages/databaseMgmt/databaseTableBrowserGrid :: ajaxDatabaseMainTableList";

		}
		
		
		@RequestMapping("/getSearchDatabaseUserFilter")
		public String SearchDatabaseUserFilter(HttpSession session, Model map, @RequestParam(name = "db_object_name") String db_object_name,
				@RequestParam(name = "type_of_database") String type_of_database, @RequestParam(name = "status") String status,FilterDTO filter) {

			List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();
			DataGridDTO dataGridDTO = new DataGridDTO();
			String recPerPage = (String) session.getAttribute("recPerPage");
			PaginatorManipulation manipulation = new PaginatorManipulation();
			
		    int	total =  databaseService.getDatabaseUserCountObject(filter,db_object_name,type_of_database,status);
		    System.out.println("total in filter======="+total);
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			try {
				list = databaseService.getSearchDatabaseUser(db_object_name, type_of_database,status);
				System.out.println("list in filter=========="+list);
			
			} catch (Exception e) { // TODO: handle exception e.printStackTrace();
            e.printStackTrace();
			}
			map.addAttribute("databaseUserGrid", list);
			map.addAttribute("dataGridDTO", dataGridDTO);

			
			return "pages/databaseMgmt/databaseUserGrid  :: ajaxUserGrid";
		}
		
		@RequestMapping("/getSearchDatabasePackageFilter")
		public String SearchDatabasePackageFilter(HttpSession session, Model map, @RequestParam(name = "db_object_name") String db_object_name,
				 @RequestParam(name = "status") String status) {

			List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();
			DataGridDTO dataGridDTO = new DataGridDTO();
			String recPerPage = (String) session.getAttribute("recPerPage");
			PaginatorManipulation manipulation = new PaginatorManipulation();
		    int	total =  databaseService.getDatabaseUserCountPackage(db_object_name,status);

			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			try {
				list = databaseService.getSearchDatabasePackage(db_object_name,status);
			
			} catch (Exception e) { // TODO: handle exception e.printStackTrace();
            e.printStackTrace();
			}
			map.addAttribute("databasePackageGrid", list);
			map.addAttribute("dataGridDTO", dataGridDTO);

			return "pages/databaseMgmt/databasePackageGrid :: ajaxDatabasePackageList";
		}
		
		@RequestMapping("/getSearchDatabaseSynonymFilter")
		public String getSearchDatabaseSynonymFilter(HttpSession session, Model map, @RequestParam(name = "db_object_name") String db_object_name,
				@RequestParam(name = "type_of_database") String type_of_database, @RequestParam(name = "status") String status) {

			List<LhssysDbObjects> list = new ArrayList<LhssysDbObjects>();
		
			try {
				list = databaseService.getSearchDatabaseSynonym(db_object_name, type_of_database,status);
			
			} catch (Exception e) { // TODO: handle exception e.printStackTrace();

			}
			map.addAttribute("databaseUserGrid", list);
			
			return "pages/databaseMgmt/databaseSynonym :: ajaxDatabaseSynonymList";
		}
}
