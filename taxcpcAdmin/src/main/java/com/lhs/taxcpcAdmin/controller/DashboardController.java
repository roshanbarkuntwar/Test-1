package com.lhs.taxcpcAdmin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.LhsTaxcpcDashPortletConfigRepo;
import com.lhs.taxcpcAdmin.dao.UserMastRepository;
import com.lhs.taxcpcAdmin.dao.UserMenuMastRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhsTaxcpcDashPortletConfig;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserMenuMast;
import com.lhs.taxcpcAdmin.service.UserLoginTranService;
import com.lhs.taxcpcAdmin.service.UserMastService;
import com.lhs.taxcpcAdmin.service.UserMenuMastService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DashboardController {

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	UserMenuMastService menuService;

	@Autowired
	UserMastService userMastService;

	@Autowired
	private UserMastRepository userRepo;

	@Autowired
	private UserLoginTranService clientLoginTranService;

	@Autowired
	private GlobalDoWorkExecuter dowork;
	@Autowired
	private LhsTaxcpcDashPortletConfigRepo portletrepo;
	@Autowired
	private UserMenuMastRepository userMenuMastRepo;

	@RequestMapping(value = "/dash-admin", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboardAdm(HttpSession session,
			@RequestParam(name = "Menu_code", required = false) String Menu_code, ModelMap map,
			@RequestParam(name = "newmenu", required = false) String newmenu) {

           System.out.println("Menu_code in controller==="+Menu_code);


		UserMast entity = new UserMast();
		UserMast favEntity = new UserMast();
		List<LhsTaxcpcDashPortletConfig> KPI_list = null;
		List<LhsTaxcpcDashPortletConfig> NTFC_list = null;
		List<LhsTaxcpcDashPortletConfig> TopClient_list = null;
		List<LhsTaxcpcDashPortletConfig> WebLink_list = null;
		List<LhsTaxcpcDashPortletConfig> welcomeMessage=null;
		List<UserMenuMast> favmenuList=null;
		String message="";
		try {
			session.setAttribute("ACTIVE_TAB", "dashboard");

			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			String userCode = userMast.getUser_code();
			//String menu_code="MENU-003#MENU-009#MENU-023#MENU-020#MENU-021";

			if (Menu_code != null) {
				favEntity = userMastService.getMenuList(userCode, Menu_code, entity);
			}

			/*
			 * else { favEntity = userMastService.getDefaultMenuList(userCode, menu_code,
			 * entity); }
			 */
			List<UserMenuMast> UserMenuMastlist=null;
			String favourite_menu_arr[];
		  	UserMast userMastObj = userMastService.getUserByUserCode(userCode);
			String favmenu = userMastObj.getFavourite_menu()!= null ?userMastObj.getFavourite_menu():"";
			favourite_menu_arr = favmenu.split("#");
			UserMenuMastlist = userMenuMastRepo.findAll();
			List<String> clearedRegistrationIdList = Arrays.asList(favourite_menu_arr);
			favmenuList = UserMenuMastlist.stream()
		            .filter(s -> clearedRegistrationIdList.stream().anyMatch(i -> i.equalsIgnoreCase(s.getMenu_code())))
		            .collect(Collectors.toList());
		 	
			List<LhsTaxcpcDashPortletConfig> dashPortletConfigList = portletrepo.findAll();

			//System.out.println("dashPortletConfigList size--" + dashPortletConfigList.size());
			Comparator<LhsTaxcpcDashPortletConfig> PORTLET_SEQ_NO = Comparator
					.comparing(LhsTaxcpcDashPortletConfig::getPORTLET_SEQ_NO);

			if (dashPortletConfigList != null) {
				if (dashPortletConfigList.size() > 0) {
					
					
					KPI_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getSTATUS().matches("A"))
							.filter(c -> c.getPORTLET_TYPE().matches("KPI")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					//KPI_list =KPI_list.subList(0, 6);
					// System.out.println("java paramter True False value");
					//KPI_list.forEach(s -> System.out.println(s.toString()));

					

					NTFC_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getSTATUS().matches("A"))
							.filter(c -> c.getPORTLET_TYPE().matches("NTFC")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					//NTFC_list=NTFC_list.subList(0, 3);
					// System.out.println("java parameter Text Field value");
					//NTFC_list.forEach(s -> System.out.println(s.toString()));

					TopClient_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getSTATUS().matches("A"))
							.filter(c -> c.getPORTLET_TYPE().matches("TopClient")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					// System.out.println("oracle parameter True False value");
					//TopClient_list.forEach(s -> System.out.println(s.toString()));

					WebLink_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getSTATUS().matches("A"))
							.filter(c -> c.getPORTLET_TYPE().matches("WebLink")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					// System.out.println("oracle parameter Text Field value");
					//WebLink_list.forEach(s -> System.out.println(s.toString()));
					
					welcomeMessage = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getSTATUS().matches("A"))
							.filter(c -> c.getPORTLET_TYPE().matches("welcm_msg")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					message=welcomeMessage.get(0).getPORTLET_VALUE();
					
					

				}
			}

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		System.out.println("list of kpi"+KPI_list);
		map.addAttribute("KPI_list", KPI_list);
		map.addAttribute("NTFC_list", NTFC_list);
		map.addAttribute("TopClient_list", TopClient_list);
		map.addAttribute("WebLink_list", WebLink_list);
		map.addAttribute("favmenuList", favmenuList);
		map.addAttribute("message",message);
		map.addAttribute("Menu_code",Menu_code);
		

		return "pages/dashboard/dashboard-adm";
	}


	@RequestMapping(value = "/dash-adminnn", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboardAdmmm(HttpSession session,
			@RequestParam(name = "Menu_code", required = false) String Menu_code, ModelMap map,
			@RequestParam(name = "update_code", required = false) String update_code) {

		Map<String, String> userList = new HashMap<>();
		String Code[] = null;
		UserMast entity = new UserMast();
		UserMast favEntity = new UserMast();
		List<String> list = new ArrayList<>();
		Map<String, String> List = new HashMap<>();
		Map<String, String> maplink = new HashMap<>();
		List<String> list1 = new ArrayList<>();
		Map<String, String> dash_link = new HashMap<>();
		List<LhsTaxcpcDashPortletConfig> KPI_list = null;
		List<LhsTaxcpcDashPortletConfig> NTFC_list = null;
		List<LhsTaxcpcDashPortletConfig> TopClient_list = null;
		List<LhsTaxcpcDashPortletConfig> WebLink_list = null;
		
		try {
			session.setAttribute("ACTIVE_TAB", "dashboard");

			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			String userCode = userMast.getUser_code();
			userList = menuService.getFavriteMenuList();
			
			if (Menu_code != null) {
				favEntity = userMastService.getMenuList(userCode, Menu_code, entity);
			}

			List = userMastService.getlist();
			
			if(List!=null) {
				
			maplink = menuService.getMenuUrl();
			for (Map.Entry<String, String> entry : List.entrySet()) {
				if(entry.getValue() !=null) {
				if (userCode.equalsIgnoreCase(entry.getKey())) {
					Code = entry.getValue().split("#");
				}
				}
			}
			}
			
			if(Code!=null) {
			for (int j = 0; j <= Code.length - 1; j++) {
				for (Map.Entry<String, String> entry : userList.entrySet()) {
					if (Code[j].equalsIgnoreCase(entry.getKey())) {
						list.add(entry.getValue());
						for (Map.Entry<String, String> entitylink : maplink.entrySet()) {
							if (Code[j].equalsIgnoreCase(entitylink.getKey())) {
								list1.add(entitylink.getKey());
								dash_link.put(entry.getValue(), entitylink.getValue());

							}
						}
					}
				}

			}
			}
			
			List<LhsTaxcpcDashPortletConfig> dashPortletConfigList = portletrepo.findAll();

			//System.out.println("dashPortletConfigList size--" + dashPortletConfigList.size());
			Comparator<LhsTaxcpcDashPortletConfig> PORTLET_SEQ_NO = Comparator
					.comparing(LhsTaxcpcDashPortletConfig::getPORTLET_SEQ_NO);

			if (dashPortletConfigList != null) {
				if (dashPortletConfigList.size() > 0) {
					KPI_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getPORTLET_TYPE().matches("KPI")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					KPI_list =KPI_list.subList(0, 6);
					// System.out.println("java paramter True False value");

					//KPI_list.forEach(s -> System.out.println(s.toString()));

					NTFC_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getPORTLET_TYPE().matches("NTFC")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					NTFC_list=NTFC_list.subList(0, 3);
					// System.out.println("java parameter Text Field value");
					//NTFC_list.forEach(s -> System.out.println(s.toString()));

					TopClient_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getPORTLET_TYPE().matches("TopClient")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					// System.out.println("oracle parameter True False value");
					//TopClient_list.forEach(s -> System.out.println(s.toString()));

					WebLink_list = dashPortletConfigList.stream().filter(c -> c.getPORTLET_TYPE() != null)
							.filter(c -> c.getPORTLET_TYPE().matches("WebLink")).sorted(PORTLET_SEQ_NO)
							.collect(Collectors.toList());
					// System.out.println("oracle parameter Text Field value");
					//WebLink_list.forEach(s -> System.out.println(s.toString()));
					
					

				}
			}

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		map.addAttribute("dash_link", dash_link);
		map.addAttribute("list", list);
		map.addAttribute("KPI_list", KPI_list);
		map.addAttribute("NTFC_list", NTFC_list);
		map.addAttribute("TopClient_list", TopClient_list);
		map.addAttribute("WebLink_list", WebLink_list);

		return "pages/dashboard/dashboard-adm";
	}


	@RequestMapping(value = "/dash-man-db-app", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboardMan(HttpSession session, ModelMap map) {
		Map<String, String> List = new HashMap<>();
		Map<String, String> maplink = new HashMap<>();
		Map<String, String> dash_link = new HashMap<>();
		List<String> list = new ArrayList<>();
		String Code[] = null;
		Map<String, String> userList = new HashMap<>();
		List<String> list1 = new ArrayList<>();

		try {

			session.setAttribute("ACTIVE_TAB", "dashboard");

			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			String userCode = userMast.getUser_code();
			userList = menuService.getFavriteMenuList();
			List = userMastService.getlist();
			maplink = menuService.getMenuUrl();

			for (Map.Entry<String, String> entry : List.entrySet()) {
				if (userCode.equalsIgnoreCase(entry.getKey())) {
					Code = entry.getValue().split("#");
				}
			}
			for (int j = 0; j <= Code.length - 1; j++) {
				for (Map.Entry<String, String> entry : userList.entrySet()) {
					if (Code[j].equalsIgnoreCase(entry.getKey())) {
						list.add(entry.getValue());
						for (Map.Entry<String, String> entitylink : maplink.entrySet()) {
							if (Code[j].equalsIgnoreCase(entitylink.getKey())) {
								list1.add(entitylink.getValue());
								dash_link.put(entry.getValue(), entitylink.getValue());
							}
						}
					}

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		map.addAttribute("dash_link", dash_link);

		return "pages/dashboard/dashboard-man";
	}

	@RequestMapping(value = "/dash-dev-tes", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboardDev(HttpSession session, @RequestParam(name = "list", required = false) String list,
			ModelMap map) {

		try {
			session.setAttribute("ACTIVE_TAB", "dashboard");

		} catch (Exception e) {
			// TODO: handle exception
			
		}

		return "pages/dashboard/dashboard-dev";
	}

	@RequestMapping(value = "/dash-con-cli-user-md", method = { RequestMethod.GET, RequestMethod.POST })
	public String dashboardCli(HttpSession session) {
		try {
			session.setAttribute("ACTIVE_TAB", "dashboard");

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "pages/dashboard/dashboard-cli";
	}

	@RequestMapping(value = "/processGlobalParams", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String processParams(HttpSession session, HttpServletRequest request) {
		String returnResult = "SUCCESS";

//		ViewClientMast clientMast = (ViewClientMast) session.getAttribute("WORKING_USER");
		UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");

//		lsysParametersService.setSessionValuesForLsysParams(session);

		Long clienLoginSeq = clientLoginTranService.save(userMast, request);
		session.setAttribute("CLIENT_LOGIN_SEQ", clienLoginSeq);

		return returnResult;
	}

	@RequestMapping("/setActiveTabInSession")
	public @ResponseBody void setActiveTabInSession(HttpSession session,
			@RequestParam(name = "menu_code", required = false) String menu_code,
			@RequestParam(name = "submenu_code", required = false) String submenu_code) {

//	System.out.println("In setActiveTabInSession ==="+menu_code+"----"+submenu_code);

		session.setAttribute("ACTIVE_TAB", menu_code);
		session.setAttribute("ACTIVE_SUB_TAB", submenu_code);

	}

	@RequestMapping(value = "/getAllFormElement")
	public String getAllFormElement(HttpSession session) {
		try {
//			session.setAttribute("ACTIVE_TAB", "dashboard");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "getallFormElement";
	}

}
