package com.lhs.taxcpcAdmin.controller.master;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.ClientAppDetailsRepository;
import com.lhs.taxcpcAdmin.dao.ClientDetailsRepository;
import com.lhs.taxcpcAdmin.dao.EntityMastRepository;
import com.lhs.taxcpcAdmin.dao.ProjectMastRepository;
import com.lhs.taxcpcAdmin.dao.ViewApplicationTypeRepository;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.ViewApplicationType;
import com.lhs.taxcpcAdmin.service.ClientDetailsService;
import com.lhs.taxcpcAdmin.service.EntityMastService;
import com.lhs.taxcpcAdmin.service.ProjectMastService;

import net.bytebuddy.matcher.ModifierMatcher.Mode;

@Controller
public class ClientController {

	@Autowired
	private EntityMastRepository entityMastRepository;

	@Autowired
	ProjectMastService projectService;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	private ViewApplicationTypeRepository viewApplicationTypeRepository;

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;

	@Autowired
	private ClientAppDetailsRepository clientAppDetails;

	@Autowired
	private ProjectMastRepository projectMastRepository;

	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@Autowired
	private EntityMastService entityMastService;

	@RequestMapping("/clientDetailEntry")
	public String clientDetailEntry(Model model, HttpSession session) {

		session.setAttribute("ACTIVE_TAB", "MENU-016");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-024");

		Map<String, String> entityNameList = new HashMap<String, String>();
		Map<String, String> states = this.clientDetailsService.getAllStates(); // fetches all states from state_mast
																				// table
		String action = "save";
		try {
			entityNameList=entityMastService.getEntityList();
//			List<EntityMast> entity_names = this.entityMastRepository.findAll();
//			for (EntityMast e : entity_names) {
//				entityNameList.put(e.getEntity_code(), e.getEntity_name());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("entityNameList", entityNameList);
		model.addAttribute("states", states);
		model.addAttribute("action", action);

		return "pages/client/clientDetail";
	}

	@RequestMapping(value = "/viewclientDetailForm")
	@ResponseBody
	public String viewclientDetailForm(@ModelAttribute("clientdetail") ClientDetails clientDetail, BindingResult result,
			HttpSession session, Model map, @RequestParam(name = "client_mode", required = false) String client_mode) {

		String response = "error";
		String clientname = "";
		try {
			clientDetail.setLastupdate(new Date());
			session.setAttribute("id", clientDetail.getEntity_code());

			if (clientDetail != null) {
				// this.clientDetailsRepository.save(clientDetail);

				clientname = clientDetail.getClient_code();
				System.out.println("clientname....." + clientname);

				response = "success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("client_mode", client_mode);
		map.addAttribute("clientname", clientname);

		session.setAttribute("clientname", clientname);
		System.out.println("clientname....." + clientname);
		return response;
	}

	@RequestMapping(value = "/SaveclientDetail")
	@ResponseBody
	public String SaveclientDetail(@ModelAttribute("clientdetail") ClientDetails clientDetail, BindingResult result,
			HttpSession session, Model map, @RequestParam(name = "client_mode", required = false) String client_mode) {
		Map<String, String> applicationName = new HashMap<String, String>();

		String response = "error";
		String clientname = "";
		try {
			clientDetail.setLastupdate(new Date());
			session.setAttribute("id", clientDetail.getEntity_code());

			if (clientDetail != null) {
				this.clientDetailsRepository.save(clientDetail);

				clientname = clientDetail.getClient_code();
				System.out.println("clientname....." + clientname);

				response = "success";
			}
			System.out.println("ENtityNmae."+clientDetail.getEntity_code());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("client_mode", client_mode);
		map.addAttribute("clientname", clientname);
		map.addAttribute("entity_code", clientDetail.getEntity_code());
		
		session.setAttribute("client_mode", client_mode);
		session.setAttribute("entity_code", clientDetail.getEntity_code());
		session.setAttribute("clientname", clientname);
		System.out.println("clientname....." + clientname);
		return response;
	}

	@RequestMapping("/saveClientForm")
	@ResponseBody
	public String saveClientForm(ClientDetails entity, HttpSession session, Model map) {
		// System.out.println("inside save ==" + entity.getClient_code());
		String edit_client_code = "";
		String response = "error";
		try {
			if (entity != null) {
				response = clientDetailsService.saveClientDetail(entity);
				edit_client_code = entity.getClient_code();
			}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		session.setAttribute("edit_client_code", edit_client_code);
		System.out.println("edit_client_code....." + edit_client_code);

		return response;
	}

	@RequestMapping(value = "/applicationDetails")
	public String applicationDetails(Model model, HttpSession session, ClientAppDetails entity,
			@RequestParam(name = "action", required = false) String action,
			@RequestParam(name = "client_code", required = false) String client_code,
			@RequestParam(name = "client_mode", required = false) String client_mode,
			@RequestParam(name = "entity_code", required = false) String entity_code) {

		String value3 = "";

		System.out.println("client_code....." + client_code);
		System.out.println("action....." + action);
		String clientname = "";
		String entity_code1 = "";
		String Client_mode1 = "";
		clientname = (String) session.getAttribute("clientname");
		System.out.println("Client Name....." + clientname);
		// String clientconnected ="";
		if (strUtl.isNull(action)) {
			action = "save";
			
			entity_code1 = (String) session.getAttribute("entity_code");
			Client_mode1="Text";
			
		}
		String clientApp = "";
		Map<String, String> applicationList = new HashMap<>();
		List<ClientAppDetails> clientApp1 = new ArrayList<>();
		List<ClientDetails> clientList = new ArrayList<>();
		List<ClientAppDetails> client_app_Details = new ArrayList<>();
		List<ClientAppDetails> clientconnected = new ArrayList<>();
		try {
			applicationList = projectService.getAllApplicationCodeName();

			if (action.equalsIgnoreCase("edit")) {

				client_app_Details = clientDetailsService.getAllList(client_code);

				clientApp = clientDetailsService.getClientAppTypeList(client_code);
				clientApp1 = clientDetailsService.getEditClientAppList(client_code);
				// clientconnected = clientDetailsService.getAppClientEntry(entity,
				// client_code);
//				System.out.println("clientApp1...." + clientApp1);
//				System.out.println("clientApp...." + clientApp);
//				System.out.println("clientconnected...." + clientconnected);

//			session.setAttribute("clientconnected", clientconnected);
//			session.setAttribute("value3", value3);
			}
			if (action.equalsIgnoreCase("view")) {

				client_app_Details = clientDetailsService.getAllList(client_code);

				clientApp = clientDetailsService.getClientAppTypeList(client_code);
				clientApp1 = clientDetailsService.getEditClientAppList(client_code);

			}

			clientList = clientDetailsService.getClientList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("value3", value3);
		model.addAttribute("clientconnected", clientconnected);
		model.addAttribute("applicationList", applicationList);
		model.addAttribute("AppLication_type", clientApp);
		model.addAttribute("clientAppList", clientApp1);
		model.addAttribute("clientList", clientList);
		model.addAttribute("action", action);
		model.addAttribute("client_code", client_code);
		model.addAttribute("client_mode", client_mode);
		model.addAttribute("entity_code", entity_code);
		
		model.addAttribute("Client_mode1", Client_mode1);
		model.addAttribute("entity_code1", entity_code1);
		model.addAttribute("clientname", clientname);
//		System.out.println("clientList...." + clientList);
//		System.out.println("Client Name....."+clientname);

		return "pages/client/NewApplicationDetailhtml";

	}

	@RequestMapping("/getSearchApplicationDetail")
	public String getSearchApplicationDetail(HttpSession session, Model map, ClientAppDetails entity,
			@RequestParam(value = "app_type", required = false) String app_type,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "client_code", required = false) String client_code, ClientDetails clientdetail) {
		List<ClientAppDetails> clientApp1 = new ArrayList<>();

		if (action.equalsIgnoreCase("save")) {

			Object attribute = session.getAttribute("clientname");
			String clientcode = attribute.toString();
			System.out.println("clientcode.." +client_code);
		}
		String clientcode = "";
		List<ProjectMast> list = new ArrayList<ProjectMast>();
		Map<String, String> applicationList = new HashMap<>();

		try {
			if (action.equalsIgnoreCase("edit")) {
				clientApp1 = clientDetailsService.getEditClientAppList(client_code);

				System.out.println("clientApp1.........." + clientApp1);

				int size = clientApp1.size();

				for (int i = 0; i < size; i++) {
					String app_url = clientApp1.get(i).getApp_url();

					String[] splitedValue = app_url.split("[//]");
					String value1 = splitedValue[2];
					System.out.println("value1" + value1);
					clientApp1.get(i).setApp_url(value1);

				}

			}

			if (action.equalsIgnoreCase("view")) {
				clientApp1 = clientDetailsService.getEditClientAppList(client_code);

				System.out.println("clientApp1.........." + clientApp1);

				int size = clientApp1.size();

				for (int i = 0; i < size; i++) {
					String app_url = clientApp1.get(i).getApp_url();

					String[] splitedValue = app_url.split("[//]");
					String value1 = splitedValue[2];
					System.out.println("value1" + value1);
					clientApp1.get(i).setApp_url(value1);

				}

			}

			if (app_type.equalsIgnoreCase("Web")) {
				app_type = "apptype_03";
				list = projectService.getProjectListforClientForm(app_type);

			}
			if (app_type.equalsIgnoreCase("JAR")) {
				app_type = "apptype_02";
				list = projectService.getProjectListforClientForm(app_type);

			}
			if (app_type.equalsIgnoreCase("API")) {
				app_type = "apptype_01";
				list = projectService.getProjectListforClientForm(app_type);

			} else {
				list = projectService.getProjectListforClientForm(app_type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// map.addAttribute("value3", value3);
		// map.addAttribute("clientconnected", clientconnected);
		map.addAttribute("clientAppDeatilList", clientApp1);
		map.addAttribute("projectMast", list);
		map.addAttribute("action", action);

		session.setAttribute("clientcode", clientdetail.getClient_code());
		session.setAttribute("client_code", client_code);

		if (action.equalsIgnoreCase("save")) {
			return "pages/client/NewApplicationDetailhtml :: Ajaxappdetail";
		} else {
			return "pages/client/NewEditApplicationDetail :: Ajaxappeditdetail";
		}
	}

	@RequestMapping(value = "/SaveConnectedDatabase", method = RequestMethod.POST)
	@ResponseBody
	public String SaveConnectedDatabase(@ModelAttribute("clientdetail1") ClientDetails clientDetail,
			BindingResult result, HttpSession session, ClientAppDetails entity, Model map) {

		String client_code = "";
		client_code = (String) session.getAttribute("clientname");
		System.out.println("get client_code from session" + client_code);
		List<ClientAppDetails> clientApp1 = new ArrayList<>();
		String response = "error";
		String connected_db_username = entity.getConnected_db_username();
		String connected_db_pwd = entity.getConnected_db_pwd();
		String connected_db_sid = entity.getConnected_db_sid();
		String connected_port = entity.getConnected_port();
		String connected_db_remark = entity.getConnected_db_remark();
		try {

			clientApp1 = clientDetailsService.getEditClientAppList(client_code);

			Object attribute = session.getAttribute("clientname");
			entity.setClient_code(client_code);

			session.setAttribute("connected_db_username", connected_db_username);
			session.setAttribute("connected_db_pwd", connected_db_pwd);
			session.setAttribute("connected_db_sid", connected_db_sid);
			session.setAttribute("connected_port", connected_port);
			session.setAttribute("connected_db_remark", connected_db_remark);

			entity.setLastupdate(new Date());
			// this.clientDetailsRepository.save(entity);

			response = "success";

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("clientdetail1", clientApp1);
//		System.out.println("projectMast..............." + clientApp1);
		return response;
	}

	@RequestMapping(value = "/SaveApplicationDetails", method = RequestMethod.POST)
	@ResponseBody
	public String SaveApplicationDetails(ClientAppDetails clientDetail1, HttpSession session,
			@RequestParam(value = "project_name", required = false) String project_name,
			@RequestParam(value = "application_type", required = false) String application_type,
			@RequestParam(value = "protocol", required = false) String protocol,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "connected_db", required = false) String connected_db,
			@RequestParam(value = "action", required = false) String action) {

//		System.out.println("project_name......."+project_name);
//		System.out.println("application_type......."+application_type);
//		System.out.println("protocol......."+protocol);
//		System.out.println("url......."+url);
//		System.out.println("remark......."+remark);
//		System.out.println("connected_db......."+connected_db);
//		// action = "edit";
		String[] splitedValue = connected_db.split("[,]");
		String connected_db_username = splitedValue[0];
		String connected_db_pwd = splitedValue[1];
		String connected_db_sid = splitedValue[2];
		String connected_port = splitedValue[3];
		String connected_db_remark = splitedValue[4];

		String response = "error";

//		String connected_db_username = (String) session.getAttribute("connected_db_username");
//		String connected_db_pwd = (String) session.getAttribute("connected_db_pwd");
//		String connected_db_sid = (String) session.getAttribute("connected_db_sid");
//		String connected_port = (String) session.getAttribute("connected_port");
//		String connected_db_remark = (String) session.getAttribute("connected_db_remark");

		// String application_type = (String) session.getAttribute("application_type");
		// String app_name = (String) session.getAttribute("app_name");
		// String app_url = (String) session.getAttribute("app_url");

		// String url = clientDetail.getApp_url();
//		String[] splitedValue = url.split("[,]");
//		String value1 = splitedValue[0];
//		String value2 = splitedValue[1];
		String Value3 = protocol.concat(url);

		try {

			Object attribute = session.getAttribute("clientname");
			String client_code = attribute.toString();

			clientDetail1.setClient_code(client_code);

			clientDetail1.setApp_name(project_name);
			clientDetail1.setApplication_type(application_type);
			clientDetail1.setApp_url(Value3);
			clientDetail1.setConnected_db_username(connected_db_username);
			clientDetail1.setConnected_db_pwd(connected_db_pwd);
			clientDetail1.setConnected_db_sid(connected_db_sid);
			clientDetail1.setConnected_port(connected_port);
			clientDetail1.setConnected_db_remark(connected_db_remark);
			clientDetail1.setRemark(remark);

			clientDetail1.setClient_code(client_code);
			clientDetail1.setLastupdate(new Date());

			if (action.equalsIgnoreCase("edit")) {

				response = clientDetailsService.editClientAppWithNewValue(Value3, remark, client_code);
			} else {
				this.clientDetailsRepository.save(clientDetail1);
				response = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/clientDetailDashboardTable")
	public String clientDetailDashboardTable(Model map, HttpServletRequest request, FilterDTO filter,
			HttpSession session, @ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,
			@RequestParam(name = "client_mode", required = false) String client_mode) {

		// System.out.println("clientDetailDashboardTable.."+filter.getPage_mode()+"client_mode.."+client_mode);

		session.setAttribute("ACTIVE_TAB", "MENU-016");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-023");

		if (strUtl.isNull(filter.getPage_mode())) {
			if (strUtl.isNull(client_mode)) {
				client_mode = "Text";
			} else {
				client_mode = client_mode;
			}
		} else {
			client_mode = filter.getPage_mode();
		}

		dataGridDTO.setFilter(filter);
		List<ClientDetails> list = new ArrayList<ClientDetails>();
		Map<String, String> entityName = new HashMap<>();

		try {

			list = clientDetailsService.getClientDetailList();

			if (filter == null) {
				filter = new FilterDTO();

			}
			long total = 10l;
			total = clientDetailsService.getClientDetailsCount(filter);
			System.out.println("Total:1234" + total);

			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();

			} else {
				recPerPage = "10";

			}

			PaginatorManipulation manipulation = new PaginatorManipulation();

			PaginatorDTO paginator = manipulation.getPaginatorCount("clientLoginDetailGrid", total, recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			map.addAttribute("dataGridDTO", dataGridDTO);

			session.setAttribute("recPerPage", recPerPage);
			session.setAttribute("client_mode", client_mode);
		} catch (Exception e) {
			// TODO: handle exception
		}

		map.addAttribute("clientDetailGrid", list);
		map.addAttribute("entityName", entityName);
		map.addAttribute("client_mode", client_mode);

		return "pages/client/clientDetailDashboardTable";
	}

	@RequestMapping(value = "/clientLoginDetailGrid", method = RequestMethod.POST)
	public String clientLoginDetailGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		List<ClientDetails> list = new ArrayList<ClientDetails>();
		Map<String, String> entityName = new HashMap<>();

		String recPerPage = "";
		String client_mode = "";
		try {

			list = clientDetailsService.getClientBrowseList(filter, dataGridDTO, recPerPage);
			long pagenumber = dataGridDTO.getPaginator().getPageNumber();
			long total = 10l;
			total = clientDetailsService.getClientDetailsCount(filter);
			System.out.println("page_mode.." + total);
			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
			PaginatorManipulation manipulation = new PaginatorManipulation();
			// System.out.println("recPerPage" + recPerPage);
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);

			dataGridDTO.getPaginator().setPageNumber(pagenumber);
			client_mode = (String) session.getAttribute("client_mode");
			System.out.println("client_mode.........client_mode........." + client_mode);

			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("clientDetailGrid", list);
			map.addAttribute("entityName", entityName);
			map.addAttribute("client_mode", client_mode);

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("dataGridDTO", dataGridDTO);
		if (client_mode.equalsIgnoreCase("Text")) {

			return "pages/client/clientDetailDashboardTableGrid :: ajaxClientList";
		} else {

			return "pages/client/clientDetailDashboardTableGrid :: ajaxClientListCard";
		}

	}


	@RequestMapping("/editClientDetails")
	public String editClientDetails(HttpServletRequest request, Model map, HttpSession session,
			@RequestParam(name = "entity_code", required = false) String entity_code,
			@RequestParam(name = "client_code", required = false) String client_code,
			@RequestParam(name = "client_mode", required = false) String client_mode,
			@RequestParam(name = "pre", required = false) String pre) {
		String action = "edit";
		// System.out.println("client_mode......."+client_mode);
		List<ClientDetails> list = new ArrayList<>();
		ClientDetails clientDetail = new ClientDetails();
		Map<String, String> CodeFromName = new HashMap<>();
		Map<String, String> entityNameList = new HashMap<String, String>();
		Map<String, String> states = this.clientDetailsService.getAllStates();
		try {
			session.setAttribute("ACTIVE_SUB_TAB", "MENU-024");
			List<EntityMast> entity_names = this.entityMastRepository.findAll();
			for (EntityMast e : entity_names) {
				entityNameList.put(e.getEntity_code(), e.getEntity_name());
			}
			list = clientDetailsService.getClientDetailList();
			clientDetail = clientDetailsService.getClientDetailsFromEntityCode(client_code);
			CodeFromName = clientDetailsService.getAllCodeName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("clientDetailGrid", list);
		map.addAttribute("clientDetail", clientDetail);
		map.addAttribute("action", action);
		map.addAttribute("client_mode", client_mode);
		map.addAttribute("entity_code", entity_code);
		map.addAttribute("entityNameList", entityNameList);
		map.addAttribute("states", states);
		map.addAttribute("pre", pre);
		map.addAttribute("CodeFromName", CodeFromName);

		return "pages/client/editClientDetail";
	}

	@RequestMapping("/viewClientDetails")
	public String viewClientDetails(HttpServletRequest request, Model map, HttpSession session,
			@RequestParam(name = "entity_code", required = false) String entity_code,
			@RequestParam(name = "client_code", required = false) String client_code,
			@RequestParam(name = "client_mode", required = false) String client_mode) {
		String action = "view";

		System.out.println("Inside View Client Controller.........." + client_mode);
		List<ClientDetails> list = new ArrayList<>();
		ClientDetails clientDetail = new ClientDetails();
		Map<String, String> CodeFromName = new HashMap<>();
		Map<String, String> entityNameList = new HashMap<String, String>();
		Map<String, String> states = this.clientDetailsService.getAllStates();
		try {
			session.setAttribute("ACTIVE_SUB_TAB", "MENU-024");
//			List<EntityMast> entity_names = this.entityMastRepository.findAll();
//			for (EntityMast e : entity_names) {
//				entityNameList.put(e.getEntity_code(), e.getEntity_name());
//			}
			entityNameList=entityMastService.getEntityList();

			list = clientDetailsService.getClientDetailList();
			clientDetail = clientDetailsService.getClientDetailsFromEntityCode(client_code);
			CodeFromName = clientDetailsService.getAllCodeName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("clientDetailGrid", list);
		map.addAttribute("clientDetail", clientDetail);
		map.addAttribute("action", action);
		map.addAttribute("client_mode", client_mode);
		map.addAttribute("entity_code", entity_code);
		map.addAttribute("entityNameList", entityNameList);
		map.addAttribute("states", states);
		map.addAttribute("CodeFromName", CodeFromName);

		return "pages/client/editClientDetail";
	}

	@RequestMapping("deleteClientEntity")
	@ResponseBody
	public String deleteClientEntity(HttpServletRequest request, Model map,
			@RequestParam(name = "entity_code", required = false) String entity_code,
			@RequestParam(name = "client_code", required = false) String client_code,
			@RequestParam(name = "client_mode", required = false) String client_mode) {
		String response = "error";
		ClientDetails clientList = null;

		System.out.println("Client Code...." + client_code);
		try {
			if (client_code != null) {

				response = clientDetailsService.deleteClientDetail(client_code);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

//	@RequestMapping("/viewClientDetails")
//	@ResponseBody
//	public String viewClientDetails(ModelMap map,
//			@RequestParam(name = "entity_code", required = false) String entity_code,
//			@RequestParam(name = "client_code", required = false) String client_code) {
//		String response = "error";
//
//		try {
//			if (client_code != null) {
//				response = clientDetailsService.viewClientDetails(client_code);
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//		return response;
//
//	}

	@RequestMapping("/searchClientDashboardFilter")
	public String searchClientDashboardFilter(HttpSession session, Model map,
			@RequestParam(name = "clientName") String clientName) {
		List<ClientDetails> list = new ArrayList<ClientDetails>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		System.out.println("clientName..." + clientName);
		Map<String, String> entityName = new HashMap<>();
		// int total = clientDetailsRepository.getcount(clientName);
		int total = clientDetailsService.getcountText(clientName);
		System.out.println("total..." + total);
		System.out.println("page_mode.." + total);

		String recPerPage = (String) session.getAttribute("recPerPage");
		PaginatorManipulation manipulation = new PaginatorManipulation();

		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);
		try {
			list = clientDetailsService.getSearchClientDetailText(clientName);

		} catch (Exception e) {

		}
		map.addAttribute("clientDetailGrid", list);
		map.addAttribute("dataGridDTO", dataGridDTO);

		return "pages/client/clientDetailDashboardTableGrid :: ajaxClientListCard";
	}

	@RequestMapping("/searchClientDashboardCardFilter")
	public String searchClientDashboardCardFilter(HttpSession session, Model map,
			@RequestParam(name = "clientName") String clientName) {
		List<ClientDetails> list = new ArrayList<ClientDetails>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		System.out.println("clientName..." + clientName);
		Map<String, String> entityName = new HashMap<>();
		// int total = clientDetailsRepository.getcount(clientName);
		// int total = clientDetailsService.getcount(clientName);
		int total = clientDetailsService.getcountText(clientName);
		System.out.println("total..." + total);
		System.out.println("page_mode.." + total);

		String recPerPage = (String) session.getAttribute("recPerPage");
		PaginatorManipulation manipulation = new PaginatorManipulation();

		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);
		try {
			// list = clientDetailsService.getSearchClientDetail1(clientName);
			list = clientDetailsService.getSearchClientDetailText(clientName);
		} catch (Exception e) {

		}
		map.addAttribute("clientDetailGrid", list);
		map.addAttribute("dataGridDTO", dataGridDTO);

		return "pages/client/clientDetailDashboardTableGrid :: ajaxClientListCard";
	}

}