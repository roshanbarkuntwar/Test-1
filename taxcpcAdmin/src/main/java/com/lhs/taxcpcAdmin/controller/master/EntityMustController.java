package com.lhs.taxcpcAdmin.controller.master;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.EntityLogoMast;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysDefaultEntityClient;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.service.EntityMastService;


@Controller
public class EntityMustController {

	@Autowired
	EntityMastService entityMastService;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	GlobalDoWorkExecuter executer;

	
	@RequestMapping("/entityEntry")
	public String entityEntry(HttpSession session, ModelMap modelmap,
			@RequestParam(name = "entity_code", required = false) String entity_code,
			@RequestParam(name = "mode", required = false) String mode) {
		Map<String, String> divisionList = new HashMap<>();
		Map<String, byte[]> List = new HashMap<>();
		Map<String, String> maplist = new HashMap<>();
		List<String> list = new ArrayList<>();
		Map<String, byte[]> logo = new HashMap<>();
		String action = "save";
		String file = "";
		EntityMast entityList = new EntityMast();
		try {
			divisionList = entityMastService.getDivisionList();
			List = entityMastService.getEntityLogo();
			String encodeBase64;
			List<String> base64Encoded = new ArrayList<String>();
			for (Entry<String, byte[]> entry : List.entrySet()) {
				byte[] byt = entry.getValue();
				encodeBase64 = Base64.getEncoder().encodeToString(byt);
				maplist.put(entry.getKey(), encodeBase64);
				base64Encoded.add(encodeBase64);
			}
			modelmap.addAttribute("contentImage", base64Encoded);
			if (entity_code != null) {
				action = "edit";
				entityList = entityMastService.getEntityEditList(entity_code);
				divisionList = entityMastService.getDivisionList();
				file = entityMastService.getfilename(entity_code);
				logo = entityMastService.getlogoNamebycode(entity_code);
				for (Entry<String, String> entry : maplist.entrySet()) {
					if (entity_code.equalsIgnoreCase(entry.getKey())) {
						list.add(entry.getValue());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		modelmap.addAttribute("divisionList", divisionList);
		modelmap.addAttribute("entityList", entityList);
		modelmap.addAttribute("action", action);
		modelmap.addAttribute("file", file);
		modelmap.addAttribute("maplist", maplist);
		modelmap.addAttribute("list", list);
		modelmap.addAttribute("mode", mode);
		modelmap.addAttribute("logo", logo);

		return "pages/Entityform/entityEntryForm";

	}

	@RequestMapping("/saveEntityForm")
	@ResponseBody
	public String saveEntityForm(EntityMast entity, HttpSession httpsession) {

		String response = "error";
		try {
			if (entity != null) {
				entity.setLastupdate(new Date());
				EntityMast savedDoc = entityMastService.saveEntityDetailMast(entity);
				EntityLogoMast entitylogo = new EntityLogoMast();
				String file = entity.getFile_logo().getOriginalFilename();
				byte[] logo = entity.getFile_logo().getBytes();

				if (savedDoc != null) {
					entitylogo.setLastupdate(savedDoc.getLastupdate());

					if (savedDoc.getEntity_code() != null) {
						entitylogo.setEntity_code(savedDoc.getEntity_code());
					}
					if (!strUtl.isNull(file)) {
						entitylogo.setLogo_name(file);
					}
					if (entity.getFile_logo().getBytes() != null) {
						entitylogo.setLogo(logo);
					}

					if (!strUtl.isNull(file)) {

						entityMastService.savelogo(entitylogo);
					}
				}
				response = "success";
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return response;

	}

	
	@RequestMapping(value = "entitySearchFilter", method = RequestMethod.POST)
	public String entitySearchFilter(Model map, @Param(value = "searchvalue") String searchvalue,HttpSession session) {
		
		List<EntityMast> entityGrid = new ArrayList<EntityMast>();
		Map<String, byte[]> List = new HashMap<>();
		Map<String, String> maplist = new HashMap<>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = entityMastService.getcount(searchvalue);
		//System.out.println("total===="+total);
		PaginatorManipulation manipulation = new PaginatorManipulation();
		try {
			
			entityGrid = entityMastService.getEntityDataListFilter(searchvalue);
			System.out.println("entityGrid.."+entityGrid);
			List = entityMastService.getEntityLogo();
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			String encodeBase64;
			List<String> base64Encoded = new ArrayList<String>();

			for (Entry<String, byte[]> entry : List.entrySet()) {
				byte[] byt = entry.getValue();
				encodeBase64 = Base64.getEncoder().encodeToString(byt);
				maplist.put(entry.getKey(), encodeBase64);
				base64Encoded.add(encodeBase64);
			}

			map.addAttribute("contentImage", base64Encoded);
			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		session.setAttribute("searchvalue", searchvalue);

		map.addAttribute("entityGrid", entityGrid);
		map.addAttribute("maplist", maplist);
		
		return "pages/Entityform/entityDataGrid:: ajaxEntityListCard";

	}

	
	@RequestMapping("/entityDetail")
	public String entityDetail(Model modelmap, EntityMast entity,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter, HttpSession session,
			HttpServletRequest request,@RequestParam(name = "mode", required = false) String mode) throws UnsupportedEncodingException {
		
		session.setAttribute("ACTIVE_TAB", "MENU-020");
		//session.setAttribute("ACTIVE_TAB", "MENU-033");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-036");
		

		if (strUtl.isNull(mode)) {
			mode = "Text";
		}
		dataGridDTO.setFilter(filter);
		Map<String, String> divisionList = new HashMap<>();
		List<EntityMast> entitylist = new ArrayList<EntityMast>();
		Map<String, byte[]> List = new HashMap<>();
		Map<String, String> maplist = new HashMap<>();
		try {
			divisionList = entityMastService.getDivisionList();
			entitylist = entityMastService.getEntity();
			List = entityMastService.getEntityLogo();
			String encodeBase64;
			List<String> base64Encoded = new ArrayList<String>();
			for (Entry<String, byte[]> entry : List.entrySet()) {
				byte[] byt = entry.getValue();
				encodeBase64 = Base64.getEncoder().encodeToString(byt);
				if (encodeBase64 != null) {
					maplist.put(entry.getKey(), encodeBase64);
				}
				base64Encoded.add(encodeBase64);
			}

			
			
			modelmap.addAttribute("contentImage", base64Encoded);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		try {

			if (filter == null) {
				System.out.println("Filter Is Null");
				filter = new FilterDTO();
			}
          // System.out.println("filter"+filter);
			long total = 10l;
			total = entityMastService.getEntityDetailsCount(filter);
			String recPerPage = "";
			System.out.println("total Records"+total);

			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("entityDetailsGrid", total, recPerPage);
			
			dataGridDTO.setPaginator(paginator);
			session.setAttribute("mode", mode);

			modelmap.addAttribute("dataGridDTO", dataGridDTO);
			modelmap.addAttribute("divisionList", divisionList);
			modelmap.addAttribute("entitylist", entitylist);
			modelmap.addAttribute("List", List);
			modelmap.addAttribute("maplist", maplist);
			modelmap.addAttribute("mode", mode);

		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "pages/Entityform/entityDetail";
	}



	@RequestMapping(value = "/entityDetailsGrid", method = RequestMethod.POST)
	public String entityDetailsGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@RequestParam(name = "entity_code", required = false) String entity_code) {

		dataGridDTO.setFilter(filter);
		Map<String, String> divisionList = new HashMap<>();
		List<EntityMast> entitylist = new ArrayList<EntityMast>();
		List<EntityMast> entityGrid = new ArrayList<EntityMast>();
		Map<String, byte[]> List = new HashMap<>();
		Map<String, String> maplist = new HashMap<>();
		String recPerPage = "10";
		long total = 0l;
		String View = "";
        String mode="";
        String  searchvalue="";
		try {
			total = entityMastService.getEntityDetailsCount(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
			divisionList = entityMastService.getDivisionList();
			entitylist = entityMastService.getEntity();
			List = entityMastService.getEntityLogo();

			String encodeBase64;
			List<String> base64Encoded = new ArrayList<String>();

			for (Entry<String, byte[]> entry : List.entrySet()) {

				byte[] byt = entry.getValue();
				encodeBase64 = Base64.getEncoder().encodeToString(byt);
				if (encodeBase64 != null) {
					maplist.put(entry.getKey(), encodeBase64);
				}
				maplist.put(entry.getKey(), encodeBase64);
				base64Encoded.add(encodeBase64);

			}
			map.addAttribute("contentImage", base64Encoded);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			dataGridDTO.setFilter(filter);
			mode = (String) session.getAttribute("mode");
			long  pagenumber = dataGridDTO.getPaginator().getPageNumber();

			searchvalue = (String) session.getAttribute("searchvalue");
			recPerPage = (String) session.getAttribute("recPerPage");

			entityGrid = entityMastService.getEntityDetailsList(filter,searchvalue,dataGridDTO,divisionList,entitylist,
					recPerPage,total);

			recPerPage = (String) session.getAttribute("recPerPage");
			long slnoStartFrom = 0l;

			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				
			}	
				PaginatorManipulation manipulation = new PaginatorManipulation();
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				dataGridDTO.setPaginator(paginator);
				dataGridDTO.getPaginator().setPageNumber(pagenumber);
		
		}
				
			map.addAttribute("entityGrid", entityGrid);
			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("divisionList", divisionList);
			map.addAttribute("entitylist", entitylist);
			map.addAttribute("maplist", maplist);
			map.addAttribute("mode", mode);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	
			if (mode.equalsIgnoreCase("Text")){
				return "pages/Entityform/entityDataGrid :: ajaxEntityList";
			} else {
				return "pages/Entityform/entityDataGrid:: ajaxEntityListCard";
			}
		
	}

	
	
	@RequestMapping(value = "entityDetailFilter", method = RequestMethod.POST)
	public String entityDetailFilter(Model map, @Param(value = "searchvalue") String searchvalue,HttpSession session) {
		
		List<EntityMast> entityGrid = new ArrayList<EntityMast>();
		Map<String, byte[]> List = new HashMap<>();
		Map<String, String> maplist = new HashMap<>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = entityMastService.getcountTable(searchvalue);
		//System.out.println("total===="+total);
		PaginatorManipulation manipulation = new PaginatorManipulation();
		
		
		try {
			entityGrid = entityMastService.getEntityDataListFilterTable(searchvalue);
			System.out.println("entityGrid.."+entityGrid);
			List = entityMastService.getEntityLogo();
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			String encodeBase64;
			List<String> base64Encoded = new ArrayList<String>();

			for (Entry<String, byte[]> entry : List.entrySet()) {
				byte[] byt = entry.getValue();
				encodeBase64 = Base64.getEncoder().encodeToString(byt);
				maplist.put(entry.getKey(), encodeBase64);
				base64Encoded.add(encodeBase64);
			}

			map.addAttribute("contentImage", base64Encoded);
			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		session.setAttribute("searchvalue", searchvalue);

		map.addAttribute("entityGrid", entityGrid);
		map.addAttribute("maplist", maplist);
		
		return "pages/Entityform/entityDataGrid:: ajaxEntityListCard";

	}

	@RequestMapping(value = "entityDefaultDetailFilter", method = RequestMethod.POST)
	public String entityDefaultDetailFilter(Model map, @Param(value = "searchvalue") String searchvalue,
			@RequestParam(value = "from_date",required = false) String from_date
			,@RequestParam(value = "to_date",required = false) String to_date,HttpSession session,FilterDTO filter) {

		List<LhssysDefaultEntityClient> defaultGrid = new ArrayList<LhssysDefaultEntityClient>();


		
		DataGridDTO dataGridDTO = new DataGridDTO();
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		
		
		
		try {
			
			if (filter == null) {
				filter = new FilterDTO();
			}
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			
			int total = entityMastService.getcountTableDefault(searchvalue,filter);
			
			//System.out.println("total===="+total);
			
			PaginatorManipulation manipulation = new PaginatorManipulation();
			
			defaultGrid = entityMastService.getEntityDataListFilterTableDefault(searchvalue,from_date,to_date);
			
			System.out.println("defaultGrid.."+defaultGrid);
			
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		session.setAttribute("searchvalue", searchvalue);
		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("defaultGrid", defaultGrid);
		
		return "pages/Entityform/defaultEntityDetailsGrid:: ajaxDeFaultEntityListCard";

	}

	
	@RequestMapping("/defaultentityDetail")
	public String defaultEntityDetail(Model modelmap, EntityMast entity,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter, HttpSession session,
			HttpServletRequest request,@RequestParam(name = "mode", required = false) String mode) throws UnsupportedEncodingException {

		session.setAttribute("ACTIVE_TAB", "MENU-020");
		//session.setAttribute("ACTIVE_TAB", "MENU-033");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-037");
		
		if (strUtl.isNull(mode)) {
			mode = "Text";
		}
		dataGridDTO.setFilter(filter);
		List<LhssysDefaultEntityClient> defaultlist = new ArrayList<LhssysDefaultEntityClient>();
		try {
			defaultlist = entityMastService.getDefaultEntityList();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {

			if (filter == null) {
				System.out.println("Filter Is Null");
				filter = new FilterDTO();
			}
			
       //System.out.println("filter"+filter);
			long total = 10l;
			total = entityMastService.getDefaultEntityDetailsCount(filter);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("defaultEntityDetailsGrid", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			session.setAttribute("mode", mode);
			modelmap.addAttribute("dataGridDTO", dataGridDTO);
			modelmap.addAttribute("defaultlist", defaultlist);
			modelmap.addAttribute("mode", mode);

		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "pages/Entityform/defaultentityDetail";
	}



	@RequestMapping(value = "/defaultEntityDetailsGrid", method = RequestMethod.POST)
	public String defaultEntityDetailsGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@RequestParam(name = "entity_code", required = false) String entity_code) {

		dataGridDTO.setFilter(filter);
		List<LhssysDefaultEntityClient> defaultlist = new ArrayList<LhssysDefaultEntityClient>();
		List<LhssysDefaultEntityClient> defaultGrid = new ArrayList<LhssysDefaultEntityClient>();
		 String  searchvalue="";
		Map<String, String> maplist = new HashMap<>();
		String recPerPage = "10";
		long total = 0l;
		String View = "";
        String mode="";
		try {
			total = entityMastService.getDefaultEntityDetailsCount(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
			defaultlist = entityMastService.getDefaultEntityList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			dataGridDTO.setFilter(filter);
			mode = (String) session.getAttribute("mode");
			
			searchvalue = (String) session.getAttribute("searchvalue");

			long  pagenumber = dataGridDTO.getPaginator().getPageNumber();

			recPerPage = (String) session.getAttribute("recPerPage");

			defaultGrid = entityMastService.getDefaultEntityDetailsList(filter, dataGridDTO,  defaultlist,
					recPerPage,total,searchvalue);
			
			
			//System.out.println("defaultGrid===="+defaultGrid);
                  
			long slnoStartFrom = 0l;

			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				
			}
				PaginatorManipulation manipulation = new PaginatorManipulation();
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				dataGridDTO.setPaginator(paginator);
				dataGridDTO.getPaginator().setPageNumber(pagenumber);
             }
				
			map.addAttribute("defaultGrid", defaultGrid);
			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("defaultlist", defaultlist);
			map.addAttribute("maplist", maplist);
			map.addAttribute("mode", mode);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			if (mode.equalsIgnoreCase("Text")){
				return "pages/Entityform/defaultEntityDetailsGrid :: ajaxDeFaultEntityList";
			} else {
				return "pages/Entityform/defaultEntityDetailsGrid:: ajaxDeFaultEntityListCard";
			}
		
	}

	@RequestMapping("/entityEntryDetails")
	@ResponseBody
	public String entityEntryDetails(ModelMap map, @RequestParam("entity_code") String entity_code) {
		String response = "error";

		try {
			if (entity_code != null) {
				response = entityMastService.viewEntityDetails(entity_code);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;

	}

	
	@RequestMapping("/defaultEntityEntry")
	public String defaultEntityEntry(Model map) {
		String action = "save";
	
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("action", action);
		
		return "pages/Entityform/defaultEntityEntry";
	}


	@RequestMapping("/saveDefaultEntryDetail")
	@ResponseBody
	public String saveDefaultEntryDetail(LhssysDefaultEntityClient entity, ModelMap map) {
				
		String response = "error";
		String action = "";
	
		try {
		
			if (entity.getClient_code().isEmpty()) {
				action = "save";
			}
			entity.setEntity_code("B0");
			entity.setLastupdate_from_fgs(new Date());
			entity.setLastupdate(new Date());
			response = entityMastService.saveDefaultEntry(entity);


		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}


	@RequestMapping("/DefaultentityEntryDetails")
	@ResponseBody
	public String DefaultentityEntryDetails(ModelMap map, @RequestParam("client_code") String client_code) {
		String response = "error";

		try {
			if (client_code != null) {
				response = entityMastService.viewDefaultEntityDetails(client_code);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;

	}


	@RequestMapping("/editDefaultEntry")
	public String editDefaultEntry(HttpServletRequest request, Model map,
			@RequestParam(name = "client_code", required = false) String client_code) {
		String action = "edit";

	//	System.out.println("client_code..........."+client_code);
		
		LhssysDefaultEntityClient list = new LhssysDefaultEntityClient();
		
		try {

			list = entityMastService.getDefaultEntityFromClientCode(client_code);
			
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("action", action);
		map.addAttribute("clientEntity", list);
		
		
		return "pages/Entityform/defaultEntityEntry";
	}

	
	@RequestMapping(value = "/deleteDefaultEntry")
	public @ResponseBody String deleteDefaultEntry(@RequestParam(name = "client_code") String client_code) {
		String response = "error";
		try {

			response = entityMastService.deleteDefaultEntry(client_code);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
