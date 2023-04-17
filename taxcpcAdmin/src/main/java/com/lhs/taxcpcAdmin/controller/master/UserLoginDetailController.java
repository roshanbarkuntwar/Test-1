package com.lhs.taxcpcAdmin.controller.master;

import java.util.ArrayList;
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

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.service.UserLoginTranService;
import com.lhs.taxcpcAdmin.service.UserMastService;

@Controller
public class UserLoginDetailController {

	@Autowired
	UserLoginTranService loginService;

	@Autowired
	UserMastService UserloginService;

	@Autowired
	private LhsStringUtility strUtl;

	public UserLoginDetailController(UserLoginTranService loginService) {
		super();
		this.loginService = loginService;
	}

	@RequestMapping("/userLoginDetail")
	public String userLoginDetail(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,
			@RequestParam(name = "login_id", required = false) String login_id,
			@RequestParam(name = "user_code", required = false) String user_code,
			@RequestParam(name = "user_name", required = false) String user_name) {

		session.setAttribute("ACTIVE_TAB", "MENU-025");
		dataGridDTO.setFilter(filter);
		List<UserLoginTran> list = new ArrayList<UserLoginTran>();
		try {

			if (filter == null) {

				filter = new FilterDTO();
			}
			long total = 10l;

			total = loginService.getLoginDetailsCount(filter);
			System.out.println("Total....." + total);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();

			} else {
				recPerPage = "10";
			}
			PaginatorManipulation manipulation = new PaginatorManipulation();

			PaginatorDTO paginator = manipulation.getPaginatorCount("userLoginDetailGrid", total, recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			map.addAttribute("dataGridDTO", dataGridDTO);

			session.setAttribute("recPerPage", recPerPage);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "pages/userDetail/userLoginDetail";
	}

	@RequestMapping(value = "/userLoginDetailGrid", method = RequestMethod.POST)
	public String userLoginDetailGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		Map<Long, String> loginList = new LinkedHashMap<>();
		String recPerPage = "";

		List<UserLoginTran> newloginGrid = new ArrayList<UserLoginTran>();

		try {

			long total = 10l;
			total = loginService.getLoginDetailsCount(filter);

			dataGridDTO.setFilter(filter);

			recPerPage = (String) session.getAttribute("recPerPage");

			newloginGrid = loginService.getUserLoginBrowseList(filter, dataGridDTO, recPerPage);
			long pagenumber = dataGridDTO.getPaginator().getPageNumber();

			PaginatorManipulation manipulation = new PaginatorManipulation();

			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);

			dataGridDTO.setPaginator(paginator);
			dataGridDTO.getPaginator().setPageNumber(pagenumber);

			map.addAttribute("loginGrid", newloginGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("loginList", loginList);
			// session.removeAttribute(recPerPage);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return "pages/userDetail/userLoginDetailGrid :: ajaxRoleList";

	}

	@RequestMapping("/searchLoginDetail")
	public String searchLoginDetail(HttpSession session, Model map,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@RequestParam(name = "login_id") String login_id, @RequestParam(name = "user_name") String user_name,
			@RequestParam(name = "user_code") String user_code) {

		List<UserLoginTran> list = new ArrayList<UserLoginTran>();

		int total = loginService.getcount(login_id, user_name, user_code);
		
		String recPerPage = (String) session.getAttribute("recPerPage");

		PaginatorManipulation manipulation = new PaginatorManipulation();

		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);
		try {
		
			list = loginService.getSearchUserLoginDetail(login_id, user_name, user_code);

		} catch (Exception e) {

		}

		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("loginGrid", list);
		// map.addAttribute("loginList", loginList);
		return "pages/userDetail/userLoginDetailGrid :: ajaxRoleList";
	}

	@RequestMapping("/userLoginListCase")
	public String userLoginListCase(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,
			@RequestParam(name = "userCode", required = false) String user_code,
			@RequestParam(name = "login_time", required = false) String login_time) {

		System.out.println("User_code...." + user_code);

		if (strUtl.isNull(user_code)) {
			user_code = filter.getUser_code();
		}

		dataGridDTO.setFilter(filter);

		try {

			if (filter == null) {

				filter = new FilterDTO();
			}
			long total = 10l;
			total = loginService.getLoginDetailsListCount(filter, user_code);

			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("userLoginListCaseGrid", total, recPerPage);
			paginator.setTotalRecords(total);
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			System.out.println("get dataGridDTO.setPaginatordataGridDTO.setPaginator--"
					+ dataGridDTO.getPaginator().getTotalRecords());

			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("totalcount", total);
			session.setAttribute("recPerPage", recPerPage);
			session.setAttribute("user_code", user_code);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("user_code", user_code);
		map.addAttribute("dataGridDTO", dataGridDTO);

		return "pages/userDetail/userLoginListCase";
	}

	@RequestMapping(value = "/userLoginListCaseGrid")
	public String userLoginListCaseGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@RequestParam(name = "userCode", required = false) String user_code,
			@RequestParam(name = "login_time", required = false) String login_time) {

		Map<Long, String> loginList = new LinkedHashMap<>();
		String recPerPage = "";
		String userCode = "";

		user_code = (String) session.getAttribute("user_code");
		List<UserLoginTran> loginGrid = new ArrayList<UserLoginTran>();
		try {

			long pagenumber = dataGridDTO.getPaginator().getPageNumber();

			long total = 10l;
			total = loginService.getLoginDetailsListCount(filter, user_code);
			System.out.println("Total:" + total);

			recPerPage = (String) session.getAttribute("recPerPage");

			loginGrid = loginService.getUserLoginDetailBrowserList(filter, dataGridDTO, recPerPage, user_code);

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

			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("loginGrid", loginGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("loginList", loginList);
			// session.removeAttribute(recPerPage);
		} catch (Exception e) {

			e.printStackTrace();
		}
		map.addAttribute("user_code", user_code);
		map.addAttribute("dataGridDTO", dataGridDTO);

		return "pages/userDetail/userLoginListCaseGrid :: ajaxUserLoginList";
}

	
	// searchLoginListCase
	@RequestMapping("/searchLoginListCase")
	public String searchLoginListCase(HttpSession session, Model map,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@RequestParam(name = "login_time") String login_time,
			@RequestParam(name = "user_code") String user_code) {

		List<UserLoginTran> loginGrid = new ArrayList<UserLoginTran>();
	
		int total = loginService.getcountUserList(login_time, user_code);

		String recPerPage = (String) session.getAttribute("recPerPage");
	
		PaginatorManipulation manipulation = new PaginatorManipulation();

		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);
		try {
			loginGrid = loginService.getSearchUserList(login_time, user_code);
			
		} catch (Exception e) {

		}

		map.addAttribute("user_code", user_code);
		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("loginGrid", loginGrid);
		
		return "pages/userDetail/userLoginListCaseGrid :: ajaxUserLoginList";
	}
	  
	 
}
