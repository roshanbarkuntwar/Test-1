package com.lhs.taxcpcAdmin.controller.master;

import java.util.ArrayList;
import java.util.Base64;
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
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;
import com.lhs.taxcpcAdmin.service.ProjectMastService;
import com.lhs.taxcpcAdmin.service.UserMastService;
import com.lhs.taxcpcAdmin.service.WishlistPendingWorkService;

@Controller
public class WishlistPendingWorkController {

	@Autowired
	ProjectMastService projectService;
	
	@Autowired
	UserMastService userService;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@Autowired
	WishlistPendingWorkService workService;
	
	
	
	
	
	@RequestMapping("/workEntry")
	public String workEntry(Model map, @RequestParam(name = "workCode", required = false) Long workCode, 
			@RequestParam(name = "Action", required = false) String Action,
			@RequestParam(name = "mark", required = false) String mark,@RequestParam(name = "status", required = false) String status) {
 		
		Map<String,String> projectList = new HashMap<>();
		Map<String,String> userList = new HashMap<>();
		Map<String,String> projectname = new HashMap<>();
		String action = "save";
		
		TaxcpcWishlistPendingWork entity = new TaxcpcWishlistPendingWork();
		
		try {
			projectList = projectService.getAllProjectCodeName();
			projectname = projectService.getProjectList1();
			userList = userService.getUserByUser("LHS");
			if (workCode != null) {
				action = "edit";
				entity = workService.getWorkDetailByCode(workCode);
				
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("projectList",projectList);
		map.addAttribute("userList",userList);
		map.addAttribute("workEntity",entity);
		map.addAttribute("action",action);
		map.addAttribute("Action",Action);
		map.addAttribute("projectname",projectname);
		map.addAttribute("mark",mark);
		map.addAttribute("status",status);
		
		return "pages/wishlistPendingWork/wishlistPendingWorkEntry";	
	}
	
	
	@RequestMapping(value = "workDetailfilter", method = RequestMethod.POST)
	public String workDetailfilter(Model map,@Param(value = "work_type") String work_type,
			@Param(value = "work_nature") String work_nature,@Param(value = "work_priority") String work_priority,
			@Param(value = "project_code") String project_code,@Param(value = "status") String status,HttpSession session) {
		
		List<TaxcpcWishlistPendingWork> entityGrid = new ArrayList<TaxcpcWishlistPendingWork>();
		
		DataGridDTO dataGridDTO = new DataGridDTO();
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = workService.getcountTable(work_type,work_nature,work_priority,project_code,status);
		PaginatorManipulation manipulation = new PaginatorManipulation();
		try {
			
			entityGrid = workService.getEntityListFilter(work_type,work_nature,work_priority,project_code,status);
			
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			
		
			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}

		map.addAttribute("entityGrid", entityGrid);
		
		return "pages/wishlistPendingWork/wishlistPendingDataGrid :: ajaxDocList";
	}
	    
	
	@RequestMapping(value = "/workDetail", method = { RequestMethod.GET, RequestMethod.POST })
	public String workDetail(HttpServletRequest request, Model map,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,HttpSession session,
			@RequestParam(name = "workType", required = false) String workType) {

		
		session.setAttribute("ACTIVE_TAB", "MENU-015");
		
		Map<String,String> projectList = new HashMap<>();
		List<TaxcpcWishlistPendingWork> list = new ArrayList<>();
		Map<String,String> userList = new HashMap<>();
		dataGridDTO.setFilter(filter);
		try {
		if (strUtl.isNull(workType)) {
				workType="W";
			    filter.setWork_type(workType);
			}
			
			if (!strUtl.isNull(workType)) {
				System.out.println("workType------>:" + workType);
			    
			}
			
			session.setAttribute("workType", workType);
			projectList = projectService.getAllProjectCodeName();
			list = workService.getWorkListOnType(workType);
			userList = userService.getUserByUserType("LHS");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		try {

			if (filter == null) {
				filter = new FilterDTO();
			}
			long total = 0l;
            total = workService.getworkDetailsCount(filter,workType,dataGridDTO);
			String recPerPage = "";
			
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("workDetailsGrid", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			
			filter.setWork_type(workType);
			map.addAttribute("dataGridDTO", dataGridDTO);
			session.setAttribute("recPerPage", recPerPage);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("projectList",projectList);
		map.addAttribute("workList",list);
		map.addAttribute("workType",workType);
		map.addAttribute("userList",userList);
		map.addAttribute("projectList",projectList);

		return "pages/wishlistPendingWork/wishlistPendingWorkDetail";
	}

	
	
	@RequestMapping(value = "/workDetailsGrid", method = RequestMethod.POST)
	public String userLoginDetailsGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,TaxcpcWishlistPendingWork entity){
		  
		dataGridDTO.setFilter(filter);
		String recPerPage = "";
		long total = 0l;
		List<TaxcpcWishlistPendingWork> list = new ArrayList<>();
		Map<String,String> projectList = new HashMap<>();
		Map<String,String> userList = new HashMap<>();
	     String	 workType =(String)	session.getAttribute("workType");
		try {

			list = workService.getWorkListOnType(workType);
			total = workService.getworkDetailsCount(filter,workType,dataGridDTO);
			projectList = projectService.getAllProjectCodeName();
			userList = userService.getUserByUserType("LHS");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		    try {
				dataGridDTO.setFilter(filter);
				recPerPage = (String) session.getAttribute("recPerPage");
				long  pagenumber = dataGridDTO.getPaginator().getPageNumber();

			List<TaxcpcWishlistPendingWork> docTranGrid = workService.getworkDetailsList(dataGridDTO,recPerPage,total,filter,workType);
			long slnoStartFrom = 0l;
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					recPerPage = (String) session.getAttribute("recPerPage");
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);

				}
				
				PaginatorManipulation manipulation = new PaginatorManipulation();
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				dataGridDTO.setPaginator(paginator);
				dataGridDTO.getPaginator().setPageNumber(pagenumber);
				
			}

			
			map.addAttribute("slnoStartFrom", slnoStartFrom);
		    map.addAttribute("workList", docTranGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("projectList", projectList);
			map.addAttribute("userList", userList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "pages/wishlistPendingWork/wishlistPendingDataGrid :: ajaxDocList";
	}
	

	
	
	
	@RequestMapping("/saveWishlistPendingWork")
	@ResponseBody
	public String saveWorkDetail(TaxcpcWishlistPendingWork entity) {
		String response="error";
		String user_code=null;
		 if(!strUtl.isNull(entity.getUser_code())) {
			 user_code=entity.getUser_code().replace(",", "#"); 
		 }
		try {
			if (entity != null) {
				entity.setUser_code(user_code);
				response = workService.saveWorkDetail(entity);
			}			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;		
	}
	
	
	@RequestMapping("/deleteWork")
	@ResponseBody
	public String deleteWork(@RequestParam(name = "workCode") Long workCode) {
		String response="error";
		try {
			if (workCode != null) {
				response = workService.deleteWork(workCode);
			}			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;		
	}
	
	
	

	
	@RequestMapping("/SaveViewForm")
	@ResponseBody
	public String SaveViewForm(TaxcpcWishlistPendingWork entity, HttpSession httpSession, Model map,@RequestParam(name = "workCode") Long workCode,@RequestParam(name = "remark") String remark) {
		String response = "error";
		TaxcpcWishlistPendingWork entity1 = new TaxcpcWishlistPendingWork();
		try {
	       response = workService.addViewForm(entity1, workCode,remark);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}

