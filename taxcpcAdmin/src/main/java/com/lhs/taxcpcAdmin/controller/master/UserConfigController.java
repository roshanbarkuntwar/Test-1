package com.lhs.taxcpcAdmin.controller.master;

import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.UserMenuMastRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;
import com.lhs.taxcpcAdmin.service.EntityMastService;
import com.lhs.taxcpcAdmin.service.UserMastService;
import com.lhs.taxcpcAdmin.service.UserMenuMastService;
import com.lhs.taxcpcAdmin.service.UserRoleMastService;

/**
 * @author sakshi.bandhate
 *
 */

@Controller
public class UserConfigController {

	@Autowired
	UserMastService userService;

	@Autowired
	UserRoleMastService roleService;

	@Autowired
	UserMenuMastService menuService;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	UserMenuMastRepository usermenu;
	
	
	@Autowired
	private EntityMastService entityMastService;
	
	@Autowired
	private GlobalDoWorkExecuter executer;
	
	@RequestMapping("/userDetails")
	public String userDetails(HttpServletRequest request, Model map, FilterDTO filter,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,HttpSession session) {
		
		session.setAttribute("ACTIVE_TAB", "MENU-026");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-027");
		
		Map<String, String> roleList = new LinkedHashMap<>();
		//Map<String, String> deptList = new LinkedHashMap<>();
		System.out.println("filter.." + filter);
		try {
			if (filter == null) {
				filter = new FilterDTO();
				System.out.println("filter1.." + filter);
			}
			long total = 10l;
			total = userService.getUserCount(filter);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("userLoginDetailsGrid", total, recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			map.addAttribute("dataGridDTO", dataGridDTO);
			session.setAttribute("recPerPage", recPerPage);

			roleList = roleService.getRoleCodeNameList();
			//deptList = roleService.getDeptNameList();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("roleList", roleList);
		//map.addAttribute("deptList", deptList);

		return "pages/userConfig/userDetails";
	}

	@RequestMapping(value = "/userLoginDetailsGrid", method = RequestMethod.POST)
	public String userLoginDetailsGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {
	

		Map<String, String> roleList = new LinkedHashMap<>();
	//	Map<String, String> deptList = new LinkedHashMap<>();
		try {
			
			String recPerPage = (String) session.getAttribute("recPerPage");
			List<UserMast> userMastGrid = userService.getReqBrowseList(filter, dataGridDTO);
			long  pagenumber = dataGridDTO.getPaginator().getPageNumber();
			long total = 10l;
			total = userService.getUserCount(filter);
			System.out.println("page_mode.."+total);
			dataGridDTO.setFilter(filter);
			
			PaginatorManipulation manipulation = new PaginatorManipulation();
			// System.out.println("recPerPage" + recPerPage);
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			
			dataGridDTO.getPaginator().setPageNumber(pagenumber);
			
			roleList = roleService.getRoleCodeNameList();
		//	deptList = roleService.getDeptNameList();

			
			map.addAttribute("userMastGrid", userMastGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("roleList", roleList);
		//	map.addAttribute("deptList", deptList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "pages/userConfig/userDetailsDataGrid  :: ajaxUserList";
	}

	@RequestMapping("/searchUserConfigDetail")
	public String searchUserConfigDetail(HttpSession session, Model map,
			@RequestParam(name = "user_name") String user_name,@RequestParam(name = "user_type") String user_type,@RequestParam(name = "user_mode") String user_mode,
			@RequestParam(name = "role_code") String role_code,@RequestParam(name = "user_status") String user_status) {
		List<UserMast> list = new ArrayList<UserMast>();
		Map<String, String> roleList = new LinkedHashMap<>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		//System.out.println("clientName..." + clientName);
		Map<String, String> entityName = new HashMap<>();
		//int total = clientDetailsRepository.getcount(clientName);
		int total = userService.getcount(user_name,user_type,user_mode,role_code,user_status);
		System.out.println("total..."+total);
		System.out.println("page_mode.."+total);
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		PaginatorManipulation manipulation = new PaginatorManipulation();
		
		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);
		try {
			list = userService.getSearchUserDetails(user_name,user_type,user_mode,role_code,user_status);
			roleList = roleService.getRoleCodeNameList();
		} catch (Exception e) {

		}
		map.addAttribute("userMastGrid", list);
		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("roleList", roleList);
		
		return "pages/userConfig/userDetailsDataGrid  :: ajaxUserList";
	}

	@RequestMapping("/addNewUser")
	public String addNewUser(HttpServletRequest request, Model map,
			@RequestParam(name = "user_code", required = false) String user_code,@RequestParam(name = "entity_code", required = false) String entity_code) {
		String action = "save";
		UserMast userEntity = new UserMast();
		Map<String, String> roleList = new LinkedHashMap<>();
		Map<String, String> deptList = new LinkedHashMap<>();
		Map<String, String> entityList = new LinkedHashMap<>();
	String List=" ";

		try {
			roleList = roleService.getRoleCodeNameList();
			deptList = roleService.getDeptNameList();
			 entityList=entityMastService.getEntityNameList();

			//List=roleService.getEntityNamebyCode(entity_code);
			//System.out.println("List==="+List);
			if (!strUtl.isNull(user_code)) {
				action = "edit";
				userEntity = userService.getUserByUserCode(user_code);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("userEntity", userEntity);
		map.addAttribute("action", action);
		map.addAttribute("roleList", roleList);
		map.addAttribute("deptList", deptList);
		map.addAttribute("List", List);
		map.addAttribute("entityList", entityList);


		return "pages/userConfig/userEntry";
	}

        @RequestMapping(value = "saveOrUpdateUser", method = { RequestMethod.GET, RequestMethod.POST }) 
		public @ResponseBody String saveOrUpdateUser(UserMast userMastFormData, HttpSession session) {
		String response = "error";
	
		System.out.println("userMastFormData==="+userMastFormData);
		try {
			if(userMastFormData.getEdit_right().equalsIgnoreCase("0,1")) {
				
				userMastFormData.setEdit_right("1");
			}
		 if(userMastFormData.getAdd_right().equalsIgnoreCase("0,1")) 
			{
				userMastFormData.setAdd_right("1");
			}
			 if(userMastFormData.getDelete_right().equalsIgnoreCase("0,1"))
			{
				userMastFormData.setDelete_right("1");
			}
			
			UserMast loginUser = (UserMast) session.getAttribute("LOGIN_USER");
			// System.out.println("userMastFormData.."+userMastFormData.getDept_str());
			// System.out.println("userMastFormData
			// role_code.."+userMastFormData.getRole_code());
			userMastFormData.setCreated_by(loginUser.getUser_code());
			if (strUtl.isNull(userMastFormData.getUser_code())) {
				userMastFormData.setCreated_date(new Date());
			}
			userMastFormData.setLastupdate(new Date());
			response = userService.saveOrUpdateUser(userMastFormData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@DeleteMapping(value = "/deleteUser")
	public @ResponseBody String deleteUser(@RequestParam(name = "user_code") String user_code) {
		String response = "error";
		try {
			response = userService.deleteUser(user_code);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/roleDetails")
	public String roleDetails(HttpServletRequest request, Model map, FilterDTO filter,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,HttpSession session) {
		
		
		session.setAttribute("ACTIVE_TAB", "MENU-026");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-028");
		
		Map<String, String> menuList = new LinkedHashMap<>();
		Map<String, String> roleTypeList = new LinkedHashMap<>();

		try {

			if (filter == null) {
				filter = new FilterDTO();
			}
			long total = 10l;
			total = roleService.getUserRoleCount(filter);
			
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("userRoleDetailsGrid", total, recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			map.addAttribute("dataGridDTO", dataGridDTO);


			menuList = menuService.getMenuCodeNameList();
			roleTypeList=roleService.RoleTypeList();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		map.addAttribute("menuList", menuList);
		map.addAttribute("roleTypeList", roleTypeList);
		map.addAttribute("dataGridDTO", dataGridDTO);

		return "pages/userConfig/userRoleDetails";
	}

	@RequestMapping(value = "/userRoleDetailsGrid", method = RequestMethod.POST)
	public String userRoleDetailsGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {
		Map<String, String> menuList = new LinkedHashMap<>();
		Map<String, String> roleTypeList = new LinkedHashMap<>();
		String recPerPage = (String) session.getAttribute("recPerPage");

		try {

			long total = 10l;
			total = roleService.getUserRoleCount(filter);
			menuList = menuService.getMenuCodeNameList();
			roleTypeList=roleService.RoleTypeList();

			dataGridDTO.setFilter(filter);


			List<UserRoleMast> roleGrid = roleService.getUserRoleBrowseList(filter, dataGridDTO, menuList, roleTypeList, total);


			long slnoStartFrom = 0l;
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				}
							
				PaginatorManipulation manipulation = new PaginatorManipulation();
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				dataGridDTO.setPaginator(paginator);
			}

			
			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("roleTypeList", roleTypeList);
			map.addAttribute("userRoleGrid", roleGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("menuList", menuList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "pages/userConfig/userRoleDetailsDataGrid  :: ajaxRoleList";
	}



	@RequestMapping("/addNewRole")
	public String addNewRole(HttpServletRequest request, Model map,
			@RequestParam(name = "role_code", required = false) String role_code) {
		String action = "save";
		UserRoleMast roleEntity = new UserRoleMast();
		Map<String, String> menuList = new LinkedHashMap<>();
		Map<String, String> menulist = new LinkedHashMap<>();
		List<String> dashboardList = new ArrayList<>();
		Map<String, String> roleTypeList = new LinkedHashMap<>();

		try {
			menuList = menuService.getMenuCodeNameList1();
			menulist = executer.sortList(menuList);
			roleTypeList=roleService.RoleTypeList();
			dashboardList = roleService.getDashboardList();
			if (!strUtl.isNull(role_code)){
				action = "edit";
				roleEntity = roleService.getRoleByRoleCode(role_code);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("roleEntity", roleEntity);
		map.addAttribute("action", action);
		map.addAttribute("menuList", menuList);
		map.addAttribute("roleTypeList", roleTypeList);
    	map.addAttribute("menulist", menulist);


		map.addAttribute("menulist", menulist);

		map.addAttribute("dashboardList", dashboardList);
		return "pages/userConfig/userRoleEntry";
	}

	@RequestMapping(value = "/saveOrUpdateRole", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveOrUpdateRole(UserRoleMast userRoleFormData, HttpSession session) {

		String response = "error";
		try {
			UserMast loginUser = (UserMast) session.getAttribute("LOGIN_USER");
			userRoleFormData.setCreatedby(loginUser.getUser_code());
			response = roleService.saveOrUpdateRole(userRoleFormData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/viewUserDetail")
	@ResponseBody
	public String viewUserDetail(@RequestParam(name = "role_code") String role_code) {
		String response = "error";
		try {
			if (role_code != null) {
				response = roleService.viewUserDetail(role_code);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}


	
	@RequestMapping(value = "/getEntityName")
	@ResponseBody
	public String getEntityName(@RequestParam("entity_code") String entity_code,Model map) {
		System.out.println("entity_code=="+entity_code);
		 String entityList ="";
		try {
			entityList = roleService.getEntityList(entity_code);

			//Collection<String> values = entityList.values();
			System.out.println("entityList==" + entityList);
			
			map.addAttribute("entityList", entityList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityList;
	}
}


