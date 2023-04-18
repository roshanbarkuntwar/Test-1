package com.lhs.taxcpcAdmin.controller.master;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.util.Calendar;
import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.ProjectMastRepository;
import com.lhs.taxcpcAdmin.dao.ProjectModuleMastRepository;
import com.lhs.taxcpcAdmin.dao.ReqTranAttachRepository;
import com.lhs.taxcpcAdmin.dao.ReqTranRepository;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;

import com.lhs.taxcpcAdmin.model.entity.BankAuditAttach;

import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.DocTranAttach;

import com.lhs.taxcpcAdmin.model.entity.LhsTaxcpcDashPortletConfig;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.ReqTranAttach;
import com.lhs.taxcpcAdmin.model.entity.ReqTranAttachId;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;
import com.lhs.taxcpcAdmin.service.ProjectMastService;
import com.lhs.taxcpcAdmin.service.RequisitionService;
import com.lhs.taxcpcAdmin.service.UserMastService;

@PropertySource("classpath:application.properties")
@Controller
public class RequisitionController {
	@Autowired
	RequisitionService reqService;
	@Autowired
	UserMastService userService;
	@Autowired
	ProjectMastService projectService;
	@Autowired
	ProjectMastRepository projectMastRepository;
	@Autowired
	ProjectModuleMastRepository projectModuleMastRepository;
	@Autowired
	private LhsStringUtility strUtl;
	@Autowired
	private ReqTranRepository reqrepo;
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private ReqTranAttachRepository reqTranAttachRepository;
	
	   @Autowired
	    private Environment env;

	public RequisitionController(RequisitionService reqService, UserMastService userService,
			ProjectMastService projectService) {
		super();
		this.reqService = reqService;
		this.userService = userService;
		this.projectService = projectService;
	}

	// main Dashboard
	@RequestMapping("/reqEntryDetail")
	public String reqEntryDetail(HttpServletRequest request, Model map, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		session.setAttribute("ACTIVE_TAB", "MENU-001");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-002");
		
		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> current_status_list = new HashMap<>();
		String type="";
		try {
			reqList = reqService.getReqEntryList();
//			System.out.println("filter.." + filter);
			if (filter == null) {
				filter = new FilterDTO();
//				System.out.println("filter1.." + filter);
			}
			long total = 10l;
//			total = userService.getUserCount(filter);
			total = reqService.getUserCount(filter,type);
			System.out.println("total11.." + total);
			String recPerPage = "";
//			&& ! strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
//			System.out.println("inside controller..");
//			System.out.println("recPerPage.." + recPerPage);
//			System.out.println("total.." + total);

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("reqEntryDataGrid", total, recPerPage);

			session.setAttribute("recPerPage", recPerPage);
//			session.setAttribute("userCode", );
			
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			appNameList = projectService.getProjectList1();
			current_status_list=reqService.getCurrent_status_list();

			System.out.println("current_status_list..." + current_status_list);

			map.addAttribute("dataGridDTO11", dataGridDTO);
			map.addAttribute("reqEntryGrid", reqList);
			map.addAttribute("appNameList", appNameList);
			map.addAttribute("current_status_list", current_status_list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "pages/requisition/reqEntryDetail";
	}

	@RequestMapping("/clientReqEntryDetail")
	public String clientReqEntryDetail(HttpServletRequest request, Model map, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		session.setAttribute("ACTIVE_TAB", "MENU-001");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-003");
		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();
		String type="";
		try {
			reqList = reqService.getReqEntryList();
			System.out.println("reqList..>>>" + reqList);
			if (filter == null) {
				filter = new FilterDTO();
//				System.out.println("filter1.." + filter);
			}
			long total = 10l;
//			total = userService.getUserCount(filter);
			total = reqService.getUserCountForCount(filter,type);
			System.out.println("total11..........." + total);
			String recPerPage = "";
//			&& ! strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
			System.out.println("inside controller..");
//			System.out.println("recPerPage.." + recPerPage);
//			System.out.println("total.." + total);

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("clientReqEntryDataGrid", total, recPerPage);

			session.setAttribute("recPerPage", recPerPage);
//			session.setAttribute("userCode", );

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			appNameList = projectService.getProjectList1();

			System.out.println("reqList...><><>" + reqList);

			map.addAttribute("dataGridDTO11", dataGridDTO);
			map.addAttribute("reqEntryGrid", reqList);
			map.addAttribute("appNameList", appNameList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "pages/requisition/clientReqEntryDetails";
	}

	
	@RequestMapping(value = "/reqEntryDataGrid", method = RequestMethod.POST)
	public String reqEntryDataGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {
		String recPerPage = "";
		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();

		try {
//			reqList = reqService.getReqEntryList();
			System.out.println("dataGridDTO11.."+dataGridDTO.getPaginator().getTotalRecords());
			System.out.println("filter.req_code..."+ filter.getReq_code());
			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
//			List<UserMast> userMastGrid = userService.getUserBrowseList(filter, dataGridDTO); 
			
			
//			filter.setFrom_date1(session.getAttribute("from_date").toString());
//			filter.setTo_date1(session.getAttribute("to_date").toString());
			List<ReqTran> reqEntryGrid = reqService.getReqEntryDashboardList(filter, dataGridDTO, recPerPage, reqList);

//			System.out.println("reqGrid===" + reqEntryGrid);
			long slnoStartFrom = 0l;
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				}
			}
			System.out.println("RecordsPerPage.." + dataGridDTO.getPaginator().getRecordsPerPage());

			appNameList = projectService.getProjectList1();

			map.addAttribute("reqEntryGrid", reqEntryGrid);
			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("appNameList", appNameList);
			System.out.println("dataGridDTO222.."+dataGridDTO.getPaginator().getTotalRecords());
		} catch (Exception e) { // TODO: handle exception
			e.printStackTrace();
		}

		return "pages/requisition/reqEntryDataGrid  :: ajaxReqList";
	}
	
	@RequestMapping(value = "/clientReqEntryDataGrid", method = RequestMethod.POST)
	public String clientReqEntryDataGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {
		String recPerPage = "";
		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();

		try {
//			reqList = reqService.getReqEntryList();

			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
//			List<UserMast> userMastGrid = userService.getUserBrowseList(filter, dataGridDTO); 
//			filter.setFrom_date1(session.getAttribute("from_date").toString());
//			filter.setTo_date1(session.getAttribute("to_date").toString());
			List<ReqTran> reqEntryGrid = reqService.getReqEntryDashboardListForClient(filter, dataGridDTO, recPerPage, reqList);
			System.out.println("reqGrid===" + reqEntryGrid);
			long slnoStartFrom = 0l;
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				}
			}
			System.out.println("RecordsPerPage.." + dataGridDTO.getPaginator().getRecordsPerPage());

			appNameList = projectService.getProjectList1();

			map.addAttribute("reqEntryGrid", reqEntryGrid);
			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("appNameList", appNameList);
		} catch (Exception e) { // TODO: handle exception
			e.printStackTrace();
		}

		return "pages/requisition/clientReqEntryDataGrid  :: ajaxReqList";
	}

	// filter
	@RequestMapping(value = "/getSearchData", method = RequestMethod.POST)
	public String getSearchData(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@Param(value = "req_code, from_date, to_date, project_name, issue_type, current_req_status,req_type,req_priority, reported_by") String req_code,
			String from_date, String to_date, String project_name, String issue_type, String current_req_status,
			String req_type, String req_priority) {

		System.out.println("req_code.."+req_code);
		List<ReqTran> list = new ArrayList<ReqTran>();
		Map<String, String> appNameList = new HashMap<>();
		String type="";
		try {
			if (filter == null) {
				filter = new FilterDTO();
			}
			
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			long total = 10l;
//			total = userService.getUserCount(filter);
			total = reqService.getUserCount(filter,type);
			System.out.println("total2221..." + total);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			appNameList = projectService.getProjectList1();
			PaginatorManipulation manipulation = new PaginatorManipulation();
			recPerPage = (String) session.getAttribute("recPerPage");
			
			
			PaginatorDTO paginator = manipulation.getPaginatorCount("reqEntryDataGrid", total, recPerPage);

			System.out.println("recPerPage.."+recPerPage);
			list = reqService.getSearchData(req_code, from_date, to_date, project_name, issue_type, current_req_status,
					req_type, req_priority);

//			System.out.println("list size.." + list.size());
			System.out.println(paginator.getTotalRecords());
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

//			System.out.println("dataGridDTO.." + dataGridDTO);
//			reqList = reqService.getReqEntryList();
//			appNameList = projectService.getProjectList1();

			
//			map.addAttribute("reqEntryGrid", reqList);
//			map.addAttribute("reqEntryGrid", list);
//			map.addAttribute("appNameList", appNameList);
			System.out.println("dataGridDTO"+dataGridDTO.getPaginator().getTotalRecords());
			System.out.println("dataGridDTO"+dataGridDTO.getPaginator().getRecordsPerPage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("reqEntryGrid", list);
		map.addAttribute("appNameList", appNameList);

		return "pages/requisition/reqEntryDataGrid  :: ajaxReqList";
	}
	
	@RequestMapping(value = "/getSearchDataForClient", method = RequestMethod.POST)
	public String getSearchDataForClient(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@Param(value = "req_code, from_date, to_date, project_name, issue_type, current_req_status,req_type,req_priority") String req_code,
			String from_date, String to_date, String project_name, String issue_type, String current_req_status,
			String req_type, String req_priority) {

		List<ReqTran> list = new ArrayList<ReqTran>();
		Map<String, String> appNameList = new HashMap<>();
		String type = "";
		try {
			if (filter == null) {
				filter = new FilterDTO();
			}
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			long total = 10l;
//			total = userService.getUserCount(filter);
			total = reqService.getUserCountForCount(filter,type);
			System.out.println("total222..." + total);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			appNameList = projectService.getProjectList1();
			PaginatorManipulation manipulation = new PaginatorManipulation();
			recPerPage = (String) session.getAttribute("recPerPage");
			PaginatorDTO paginator = manipulation.getPaginatorCount("clientReqEntryDataGrid", total, recPerPage);

			list = reqService.getSearchDataForClient(req_code, from_date, to_date, project_name, issue_type, current_req_status,
					req_type, req_priority);

//			System.out.println("list size.." + list.size());
			System.out.println(paginator.getTotalRecords());
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

//			System.out.println("dataGridDTO.." + dataGridDTO);
//			reqList = reqService.getReqEntryList();
//			appNameList = projectService.getProjectList1();

			map.addAttribute("dataGridDTO", dataGridDTO);
//			map.addAttribute("reqEntryGrid", reqList);
//			map.addAttribute("reqEntryGrid", list);
//			map.addAttribute("appNameList", appNameList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("reqEntryGrid", list);
		map.addAttribute("appNameList", appNameList);

		return "pages/requisition/clientReqEntryDataGrid  :: ajaxReqList";
	}

	@RequestMapping("/reqEntry")
	public String reqEntry(HttpSession session, ModelMap modelmap) {
		session.setAttribute("ACTIVE_TAB", "MENU-001");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-004");
		UserMast userMast = new UserMast();
		List<ReqTran> list = new ArrayList<>();
		Map<String, String> userList = new HashMap<>();
		Map<String, String> errorGroup = new HashMap<>();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> projectModuleList = new HashMap<>();
		Map<String, String> projectMenuList = new HashMap<>();
//		Map<String, String> errorCodeList = new HashMap<>();
		try {
			list = reqrepo.findAll();
			System.out.println("list=="+list);
			userMast = (UserMast) session.getAttribute("LOGIN_USER");
			userList = userService.getAllUserCodeName();
			errorGroup = reqService.getReqErrorGroup();
//			sampleDataFilter = reqService.getSampleDataFilter();
//			projectList = projectService.getProjectList1();
//			projectModuleList = projectService.getModuleList1();
//			projectMenuList = projectService.getProjectMenuList1();

			projectList = projectService.getProjectListWithStatus();
			projectModuleList = projectService.getModuleListWithStatus();
			projectMenuList = projectService.getProjectMenuListWithStatus();
//			errorCodeList = projectService.getErrorCodeList();
//			System.out.println("projectList.."+projectList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelmap.addAttribute("reqList",list);
		modelmap.addAttribute("sessionUser", userMast);
		modelmap.addAttribute("userList", userList);

		modelmap.addAttribute("errorGroup", errorGroup);
		modelmap.addAttribute("projectList", projectList);
		modelmap.addAttribute("projectModuleList", projectModuleList);
		modelmap.addAttribute("projectMenuList", projectMenuList);
//		modelmap.addAttribute("sampleDataFilter",sampleDataFilter);
		return "pages/requisition/reqEntry";
	}

	@RequestMapping("/addNewRequisition")
	@ResponseBody
	public String addNewRequisition(ReqTran reqEntity, HttpSession session) {
		String response = "error";
		String fileName = "";
		
		String maxCount = reqrepo.getMaxCount();
		System.out.println("maxCount=" + maxCount);
		
		ReqTranAttach reqTranAttach= new ReqTranAttach();
		try {
			if (reqEntity != null) {
				
				UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
				if(userMast.getUser_type().equalsIgnoreCase("FGS")) {
					reqEntity.setReq_entered_mode("E");
				}else if(userMast.getUser_type().equalsIgnoreCase("LHS")) {
					reqEntity.setReq_entered_mode("I");
				}
				
				reqEntity.setCurrent_req_status("CRS_015");
				reqEntity.setLastupdate(new Date());
				response = reqService.addNewRequisition(reqEntity);
				session.setAttribute("ACTIVE_SUB_TAB", "MENU-002"); 
				
				if (response.equalsIgnoreCase("success") && reqEntity.getDoc_file1() != null) {
					
					maxCount = reqrepo.getMaxCount();
					System.out.println("size=="+reqEntity.getDoc_file1().size());
					
					for(int i=1; i<= reqEntity.getDoc_file1().size() -1; i++) {
						System.out.println(reqEntity.getDoc_file1().get(i-1).getOriginalFilename()); 
						 
						reqTranAttach.setLast_update(new Date()); 
						reqTranAttach.setSlno(i); 
						reqTranAttach.setReq_code(maxCount);
						reqTranAttach.setReq_attach(reqEntity.getDoc_file1().get(i-1).getBytes());
						reqTranAttach.setReq_attach_name(reqEntity.getDoc_file1().get(i-1).getOriginalFilename());
						
						reqTranAttachRepository.save(reqTranAttach);
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response = response + "," + maxCount;
		
		return response;
	}

	@RequestMapping("/getSampleDataFilter")
	@ResponseBody
	public List<String> getSampleDataFilter(@RequestParam("reqType") String reqType) {
		List<String> response = new ArrayList<>();
		try {
			response = reqService.getViewSampleDataFilter(reqType);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("response.."+response);
		return response;
	}

	// main approval
	@RequestMapping("/reqApprovalDashBoard")
	public String reqApprovalDashBoard(HttpSession session, ModelMap modelmap, HttpServletRequest request,
			FilterDTO filter, @ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {

		session.setAttribute("ACTIVE_TAB", "MENU-001");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-018");
		
		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> assignToList = new HashMap<>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> projectModuleList = new HashMap<>();
		Map<String, String> projectMenuList = new HashMap<>();
		String recPerPage = "";
		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			reqList = reqService.getReqListForApproval1(userMast.getRole_code(), userMast.getDept_str());
//			String [] roles= {"App_Developer,DB_Developer,UI_Developer};
			assignToList = userService.getUserByDeptAndRole("App_Developer");
//			System.out.println("assignToList>>"+assignToList);

			if (filter == null) {
				filter = new FilterDTO();
			}
			long total = 10l;
			total = reqService.getCountOfApprovedReq(filter,userMast.getUser_code());

			if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("reqApprovalGridData", total, recPerPage);
			session.setAttribute("recPerPage", recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			modelmap.addAttribute("dataGridDTO", dataGridDTO);

			System.out.println("paginator..." + paginator);

			appNameList = projectService.getProjectList1();
			projectModuleList = projectService.getAllProjectModuleName();
			projectMenuList = projectService.getAllProjectMenuName();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("reqList.."+reqList);
		modelmap.addAttribute("reqApprovalGrid", reqList);
		modelmap.addAttribute("assignToList", assignToList);
		modelmap.addAttribute("appNameList", appNameList);
		modelmap.addAttribute("projectModuleList", projectModuleList);
		modelmap.addAttribute("projectMenuList", projectMenuList);

		return "pages/requisition/reqApprovalDetail";

	}

	@RequestMapping(value = "/reqApprovalGridData", method = RequestMethod.POST)
	public String reqApprovalGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> assignToList = new HashMap<>();
		Map<String, String> projectModuleList = new HashMap<>();
		Map<String, String> projectMenuList = new HashMap<>();
		String recPerPage = "";

		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			reqList = reqService.getReqListForApproval1(userMast.getRole_code(), userMast.getDept_str());
			assignToList = userService.getUserByDeptAndRole("App_Developer");

			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
			List<ReqTran> reqApprovalGrid = reqService.getReqApprovalBrowseList(session, filter, dataGridDTO,
					recPerPage, reqList);

			long slnoStartFrom = 0l;
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				}
			}
			appNameList = projectService.getProjectList1();
			projectModuleList = projectService.getAllProjectModuleName();
			projectMenuList = projectService.getAllProjectMenuName();

//			map.addAttribute("reqList", reqList);
			map.addAttribute("appNameList", appNameList);

			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("reqApprovalGrid", reqApprovalGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);

		} catch (Exception e) { // TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("assignToList", assignToList);
//		map.addAttribute("${reqApprovalGrid", reqList);
		map.addAttribute("appNameList", appNameList);
		map.addAttribute("projectModuleList", projectModuleList);
		map.addAttribute("projectMenuList", projectMenuList);

		return "pages/requisition/reqApprovalDataGrid :: ajaxReqApprovalList";

	}

	// filter
//	@RequestMapping(value = "/getReqApprovalDataFilter", method = RequestMethod.POST)
	@RequestMapping("/getReqApprovalDataFilter")
	public String getReqApprovalData(Model modelmap, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@Param(value = "req_code, from_date, to_date, project_name, issue_type, req_type, req_priority") String req_code,
			String from_date, String to_date, String project_name, String issue_type, String req_type,
			String req_priority) {

		List<ReqTran> list = new ArrayList<ReqTran>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> projectModuleList = new HashMap<>();
		Map<String, String> projectMenuList = new HashMap<>();
		String type ="Approval";
		try {
			if (filter == null) {
				filter = new FilterDTO();
			}
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			long total = 10l;
//			total = userService.getUserCount(filter);
			total = reqService.getUserCount(filter, type);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			recPerPage = (String) session.getAttribute("recPerPage");
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("reqEntryDataGrid", total, recPerPage);

			list = reqService.getReqApprovalDataFilter(req_code, from_date, to_date, project_name, issue_type, req_type,
					req_priority);

			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			appNameList = projectService.getProjectList1();
			projectModuleList = projectService.getAllProjectModuleName();
			projectMenuList = projectService.getAllProjectMenuName();

//				reqList = reqService.getReqEntryList();
//				appNameList = projectService.getAllProjectCodeName();

			modelmap.addAttribute("dataGridDTO", dataGridDTO);
//				
		} catch (Exception e) {
			e.printStackTrace();
		}

		modelmap.addAttribute("reqApprovalGrid", list);
		modelmap.addAttribute("appNameList", appNameList);
		modelmap.addAttribute("projectModuleList", projectModuleList);
		modelmap.addAttribute("projectMenuList", projectMenuList);

		return "pages/requisition/reqApprovalDataGrid :: ajaxReqApprovalList";

	}

	// main Assigned
	@RequestMapping("/reqAssignedDetail")
	public String reqAssignedDetail(HttpServletRequest request, Model map, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {
		
		session.setAttribute("ACTIVE_TAB", "MENU-001");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-029");

//		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> projectModuleList = new HashMap<>();
		Map<String, String> projectMenuList = new HashMap<>();

		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			map.addAttribute("user_code", userMast.getUser_code());
//			reqList = reqService.getAssignedRequisition1(userMast.getUser_code(), userMast.getRole_code());

			if (filter == null) {
				filter = new FilterDTO();
//				System.out.println("filter1.." + filter);
			}
			long total = 10l;
//			total = userService.getUserCount(filter);
//			total = reqService.getUserCount(filter);
			total = reqService.getCountOfAssignedReq(filter,userMast.getUser_code(), userMast.getRole_code());
			String recPerPage = "";
//			&& ! strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
//			System.out.println("inside controller..");
//			System.out.println("recPerPage.." + recPerPage);
			System.out.println("total.." + total);

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("reqAssignedDataGrid", total, recPerPage);
			session.setAttribute("recPerPage", recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			appNameList = projectService.getProjectList1();
			projectModuleList = projectService.getAllProjectModuleName();
			projectMenuList = projectService.getAllProjectMenuName();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		map.addAttribute("reqAssignedGrid", reqList);
		map.addAttribute("appNameList", appNameList);
		map.addAttribute("projectModuleList", projectModuleList);
		map.addAttribute("projectMenuList", projectMenuList);
		
		map.addAttribute("dataGridDTO", dataGridDTO);
		System.out.println("dataGridDTO..."+dataGridDTO.getPaginator().getTotalRecords());
//			map.addAttribute("reqEntryGrid", reqList);
		map.addAttribute("appNameList", appNameList);

		return "pages/requisition/reqAssignedDetail";
	}

	@RequestMapping(value = "/reqAssignedDataGrid", method = RequestMethod.POST)
	public String reqAssignedDataGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		List<ReqTran> reqList = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> assignToList = new HashMap<>();
		Map<String, String> projectModuleList = new HashMap<>();
		Map<String, String> projectMenuList = new HashMap<>();
		String recPerPage = "";

		try {

			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			reqList = reqService.getAssignedRequisition1(filter,userMast.getUser_code(), userMast.getRole_code());
			assignToList = userService.getUserByDeptAndRole("App_Developer");

//			System.out.println("assignToList.." + assignToList);
			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
			List<ReqTran> reqAssignedGrid = reqService.getReqAssignedBrowseList(session, filter, dataGridDTO, recPerPage);

			long slnoStartFrom = 0l;
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				}
			}
			
			appNameList = projectService.getProjectList1();
			projectModuleList = projectService.getAllProjectModuleName();
			projectMenuList = projectService.getAllProjectMenuName();

			// map.addAttribute("reqList", reqList);
			map.addAttribute("appNameList", appNameList);
			map.addAttribute("user_code", userMast.getUser_code());

			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("reqAssignedGrid", reqAssignedGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);

		} catch (Exception e) { // TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("assignToList", assignToList);

//		map.addAttribute("reqAssignedGrid", reqList);
		map.addAttribute("appNameList", appNameList);
		map.addAttribute("projectModuleList", projectModuleList);
		map.addAttribute("projectMenuList", projectMenuList);

		return "pages/requisition/reqAssignedDataGrid :: ajaxReqAssignedList";
	}

	// filter
	@RequestMapping(value = "/getReqAssignedDataFilter", method = RequestMethod.POST)
	public String getReqAssignedDataFilter(Model modelmap, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,
			@Param(value = "req_code,project_name, req_type,req_priority, menu_name, from_date,to_date,dev_status") String req_code,
			String project_name, String req_type, String req_priority, String menu_name, String from_date,
			String to_date, String dev_status) {
		List<ReqTran> list = new ArrayList<ReqTran>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> projectModuleList = new HashMap<>();
		Map<String, String> projectMenuList = new HashMap<>();
		String type="Assigned";
		try {
			if (filter == null) {
				filter = new FilterDTO();
			}
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			long total = 10l;
//			total = userService.getUserCount(filter);
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			total = reqService.getCountOfAssignedReq(filter,userMast.getUser_code(),userMast.getRole_code());
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			
//			System.out.println("inside getReqAssignedData...");
			list = reqService.getReqAssignedDataFilter(req_code, project_name, req_type, req_priority, menu_name,
					from_date, to_date, dev_status, userMast.getRole_code(), userMast.getUser_code());

			recPerPage = (String) session.getAttribute("recPerPage");
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("reqEntryDataGrid", total, recPerPage);

			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);

			appNameList = projectService.getProjectList1();
			projectModuleList = projectService.getAllProjectModuleName();
			projectMenuList = projectService.getAllProjectMenuName();

			modelmap.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelmap.addAttribute("reqAssignedGrid", list);

		modelmap.addAttribute("appNameList", appNameList);
		modelmap.addAttribute("projectModuleList", projectModuleList);
		modelmap.addAttribute("projectMenuList", projectMenuList);

		return "pages/requisition/reqAssignedDataGrid :: ajaxReqAssignedList";
	}

	@RequestMapping("/reqApprovalByAuth")
	@ResponseBody
	public String reqApproval(HttpSession session, @RequestParam("reqCode") String req_code,
			@RequestParam("assignedTo") String assigned_to,
			@RequestParam("approved_by_remark") String approved_by_remark) {
		String response = "error";
		UserMast userMast = new UserMast();
		try {
			userMast = (UserMast) session.getAttribute("LOGIN_USER");
			response = reqService.assignedRequisition(req_code, assigned_to, userMast.getUser_code(),
					approved_by_remark);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/reqRejectById")
	@ResponseBody
	public String reqRejectById(HttpSession session, @RequestParam("reqCode") String req_code1) {
		System.out.println("req_code1..... " + req_code1);
		String response = "error";
		UserMast userMast = new UserMast();
		try {

			userMast = (UserMast) session.getAttribute("LOGIN_USER");
			System.out.println("user_code.....  " + userMast.getUser_code());
//			response = reqService.assignedRequisition(req_code, assigned_to, userMast.getUser_code());
			response = reqService.rejectRequisition(req_code1, userMast.getUser_code());
//			System.out.println("response.." + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/startAssignedReq")
	@ResponseBody
	public String startAssignedReq(HttpSession session, @RequestParam("req_code") String req_code,
			 @RequestParam("reopened") String reopened) {
		String response = "error";
		
		System.out.println("reopened..."+reopened);
		UserRoleMast roleEntity = new UserRoleMast();
		UserMast userMast = new UserMast();
		try {
			userMast = (UserMast) session.getAttribute("LOGIN_USER");
			roleEntity = (UserRoleMast) session.getAttribute("USER_ROLE");
			if (roleEntity != null) {
				response = reqService.startRequisition(req_code, roleEntity.getRole_name(), reopened, userMast.getUser_code());
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/getUserOfDeptQc")
	@ResponseBody
	public Map<String, String> getUserOfDeptQc(@RequestParam("role_code") String role_code) {
		Map<String, String> list = new HashMap<>();
		try {
			String dept_str = "QC";
			list = userService.getUserByDeptAndRole(role_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	} 

//	@RequestMapping("/closeAssignedRequisition")
//	@ResponseBody
//	public String closeAssignedReq(HttpSession session, @RequestParam("req_code") String req_code,
//			@RequestParam("qcType") String qcType, @RequestParam("assignedToQc") String assignedToQc,
//			@RequestParam("devRemark") String devRemark, @RequestParam("deployType") String deployType,
//			@RequestParam("deployTo") String deployTo,@RequestParam("assignDev") String assignDev,
//			@RequestParam("reopened") String reopened) {
//		String response = "error";
//		UserMast userMast = new UserMast();
//		UserRoleMast roleEntity = new UserRoleMast();
//		try {
//			userMast = (UserMast) session.getAttribute("LOGIN_USER");
//			roleEntity = (UserRoleMast) session.getAttribute("USER_ROLE");
//
//			response = reqService.closeAssignedReq(userMast.getUser_code(), req_code, qcType, assignedToQc, devRemark,
//					deployType, roleEntity.getRole_name(),deployTo , roleEntity.getRole_type_code(),  assignDev, reopened);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}

	
	@RequestMapping("/closeAssignedRequisition")
	@ResponseBody
	public String closeAssignedReq(HttpSession session, @RequestParam("req_code") String req_code,
			@RequestParam("qcType") String qcType, @RequestParam("assignedToQc") String assignedToQc,
			@RequestParam("devRemark") String devRemark, @RequestParam("deployType") String deployType,
			@RequestParam("deployTo") String deployTo, @RequestParam("assignDev") String assignDev,
			@RequestParam("reopened") String reopened) {
		String response = "error";
		UserMast userMast = new UserMast();
		UserRoleMast roleEntity = new UserRoleMast();

		try { 
			userMast = (UserMast) session.getAttribute("LOGIN_USER");
			roleEntity = (UserRoleMast) session.getAttribute("USER_ROLE");

			Date lastupdate;
			lastupdate = reqrepo.getlastUpdateDate(req_code);
			System.out.println("date...." + lastupdate);
			Date localDate = new Date();
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String d1 = sdformat.format(lastupdate);
			String d2 = sdformat.format(localDate);
			System.out.println("The date 1 is: " + d1);
			System.out.println("The date 2 is: " + d2);

			Calendar cal = Calendar.getInstance();
			cal.setTime(lastupdate);
			cal.add(Calendar.MINUTE, 10);
			String newTime = sdformat.format(cal.getTime());
			d1 = newTime;

			if (d1.compareTo(d2) < 0) {
				response = reqService.closeAssignedReq(userMast.getUser_code(), req_code, qcType, assignedToQc,
						devRemark, deployType, roleEntity.getRole_name(), deployTo, roleEntity.getRole_type_code(),
						assignDev, reopened);
			} else {
				response = "delay";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	

	@RequestMapping("/closeReAssignedRequisition")
	@ResponseBody
	public String closeReAssignedReq(HttpSession session, @RequestParam("req_code") String req_code,
			@RequestParam("remark") String remark) {
		
		System.out.println("remark.."+remark+"..req_code.."+req_code);
		String response = "error";
		UserMast userMast = new UserMast();
		UserRoleMast roleEntity = new UserRoleMast();
		String client_final_status ="";
		try {
			userMast = (UserMast) session.getAttribute("LOGIN_USER");
			roleEntity = (UserRoleMast) session.getAttribute("USER_ROLE");
			client_final_status	= reqrepo.getClientFinalStatus(req_code);
			System.out.println("client_final_status.."+client_final_status);
			response = reqService.closeReAssignedReq(req_code, remark,roleEntity.getRole_type_code(),client_final_status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@RequestMapping("/closedRequisitionByClient")
	@ResponseBody
	public String closedRequisitionByClient(HttpSession session, @RequestParam("req_code") String req_code,
			@RequestParam("remark") String remark, @RequestParam("ReqClosed") String ReqClosed) {
		
		System.out.println("remark.."+remark+"..req_code.."+req_code);
		String response = "error";
		UserMast userMast = new UserMast();
		UserRoleMast roleEntity = new UserRoleMast();
		try {
			userMast = (UserMast) session.getAttribute("LOGIN_USER");
			roleEntity = (UserRoleMast) session.getAttribute("USER_ROLE");
			
			response = reqService.closeReqByClient(req_code, remark,roleEntity.getRole_type_code(),ReqClosed,userMast.getUser_code());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	


	@RequestMapping("/deploymentEntry")
	public String depolymentEntry(ModelMap map) {
		Map<String, String> projectList = new HashMap<>();
		List<String> sampleDataFilter = new ArrayList<>();
		Map<String, String> depl_type_list = new HashMap<>();
//		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<LhssysTaxcpcDeploymentTran>();
		try {

			projectList = projectService.getProjectList1();
			sampleDataFilter = reqService.getViewSampleDataFilter("");
			depl_type_list = projectService.getDeplTypeList();
//			list = projectService.getDeploymentDataList();
//			System.out.println("depl_type_list.." + depl_type_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		map.addAttribute("sampleDataFilter", sampleDataFilter);
//		map.addAttribute("deploymentGrid", list);
//		System.out.println("list>> " + list);
		map.addAttribute("projectList", projectList);
		map.addAttribute("depl_type_list", depl_type_list);
		return "pages/deployment/deploymentEntry";
	}

	// openViewBox deployment
	@RequestMapping("/deploymentEntryDetails")
	@ResponseBody
	public String deploymentEntryDetails(ModelMap map, @RequestParam("seq_id") Long seq_id) {
//			System.out.println("inside controller..");
		System.out.println("seq_id = "+seq_id);
		String response = "error";
		try {
			if (seq_id != null) {
				response = reqService.viewDeploymentDetails(seq_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("response.." + response);
		return response;

	}

	@RequestMapping("/saveDeployment")
	@ResponseBody
	public String saveDeployment(LhssysTaxcpcDeploymentTran entity) {
		String response = "error";
		try {
			response = reqService.saveDeployment(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	// openViewBox requisition
	@RequestMapping("/viewReqDashboard")
	@ResponseBody
	public String viewWorkDetail(@RequestParam(name = "reqCode") String reqCode, HttpSession session) {
		String response = "error";
		UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
		String role_code = userMast.getRole_code();
		String role_type_code= userService.getRoleTypeByRoleCode(role_code);
		String sub_menu_code = (String) session.getAttribute("ACTIVE_SUB_TAB");
System.out.println("req_code="+reqCode);
//		List<String> list = new ArrayList();
		try {
			if (reqCode != null) {
				
//				list=reqService.getFileNames();
//				for (int i=0 ; i>list.size(); i++) {
//					System.out.println("list=="+list);
//				}
				
				response = reqService.viewReqDashboard(reqCode, role_type_code,sub_menu_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("response.." + response);
		return response;
	}

	@RequestMapping(value = "/getProjectModule1")
	@ResponseBody
	public Map<String, String> getProjectModule(@RequestParam("projectCode") String projectCode) {
		Map<String, String> moduleList = new HashMap<>();
		try {
//			System.out.println("projectCode");
//			moduleList = projectService.getModulesOnProjectCode(projectCode);
			moduleList = projectService.getModuleListWithStatus(projectCode);
//			moduleList= projectService.getProjectList1();

			Collection<String> values = moduleList.values();
			System.out.println("moduleList" + moduleList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return moduleList;
	}

	@RequestMapping("/getProjectMenu1")
	@ResponseBody
	public Map<String, String> getProjectMenu(@RequestParam("projectCode") String projectCode,
			@RequestParam("moduleCode") String moduleCode) {
		Map<String, String> menuList = new HashMap<>();
		try {
//			System.out.println("projectCode" + projectCode);
//			System.out.println("moduleCode" + moduleCode);
//			menuList = projectService.getMenuOnProjectCode(projectCode, moduleCode);
			menuList = projectService.getProjectMenuListWithStatus(projectCode,moduleCode);
//			System.out.println("menuList " + menuList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
	}


	@RequestMapping(value = "/deleteDocuments")
	public @ResponseBody String deleteUser(@RequestParam(name = "seq_id") String seq_id) {
		String response = "error";
		try {
//			System.out.println("seq_id = " + seq_id);
//			response = docMgmtService.deleteDocument(doc_code);
			response = reqService.deleteDocument(seq_id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@RequestMapping("downloadFile")
	public String downloadFile(ReqTranAttach entity, HttpServletResponse response,
			HttpSession session, @RequestParam(name = "req_code") String req_code,
			 @RequestParam(name = "slno") String slno)throws Exception {
		System.out.println("req_code="+req_code);
		System.out.println("slno="+slno);
		
		String username = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");

		byte b[] = entity.getReq_attach();
		try {

			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.14:1521/orcl", username, password);
			Statement stmt = con.createStatement();
			String query = "select req_attach_name, req_attach from req_tran_attach where req_code='"+ req_code+"'and slno="+slno+"";
			System.out.println("downloadFile query.."+query);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				File blobFile = new File(rs.getString("req_attach_name"));
				Blob blob = rs.getBlob("req_attach");
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
	
	// main Dashboard
	@RequestMapping("/deploymentDetail")
	public String deploymentDetail(HttpServletRequest request, Model map, FilterDTO filter, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,
			@Param(value = "project_name, depl_code, war_file") String project_name, String depl_code, String war_file) {
		session.setAttribute("ACTIVE_TAB", "MENU-013");

		Map<String, String> projectList = new HashMap<>();
		Map<String, String> depl_type_list = new HashMap<>();
		List<LhssysTaxcpcDeploymentTran> deplList = new ArrayList<LhssysTaxcpcDeploymentTran>();
		try {
			deplList = reqService.getDeploymentDataList();
			System.out.println("deplList>>"+deplList);
			
			if (filter == null) {
				filter = new FilterDTO();
			}
			long total = 10l;
			total = reqService.getDeplCount(filter);
			System.out.println("total.." + total);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("deplDataGrid", total, recPerPage);

			session.setAttribute("recPerPage", recPerPage);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			System.out.println("filter.." + dataGridDTO.getPaginator().getTotalRecords());

			projectList = projectService.getProjectList1();
			depl_type_list = projectService.getDeplTypeList();
			
			map.addAttribute("deploymentGrid", deplList);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("projectList", projectList);
			map.addAttribute("depl_type_list", depl_type_list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "pages/deployment/deploymentDetail";
	}

	@RequestMapping(value = "/deplDataGrid", method = RequestMethod.POST)
	public String deplDataGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {
		String recPerPage = "";
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> depl_type_list = new HashMap<>();
//		List<LhssysTaxcpcDeploymentTran> deplList = new ArrayList<LhssysTaxcpcDeploymentTran>();

		try {
			long total = 10l;
			long  pagenumber = dataGridDTO.getPaginator().getPageNumber();
			total = reqService.getDeplCount(filter);
			System.out.println("total.." + total);
//			deplList = deplServie.getDeploymentDataList();

			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
//			List<UserMast> userMastGrid = userService.getUserBrowseList(filter, dataGridDTO); 
			List<LhssysTaxcpcDeploymentTran> entryGrid = reqService.getDeplDashboardList(filter, dataGridDTO, recPerPage);
//			System.out.println("reqGrid===" + reqEntryGrid);
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
			
			System.out.println("RecordsPerPage.." + dataGridDTO.getPaginator().getRecordsPerPage());

			projectList = projectService.getProjectList1();
			depl_type_list = projectService.getDeplTypeList();

//			map.addAttribute("deploymentGrid", deplList);
			map.addAttribute("deploymentGrid", entryGrid);
			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("projectList", projectList);
			map.addAttribute("depl_type_list", depl_type_list);
		} catch (Exception e) { 
			e.printStackTrace();
		}

		return "pages/deployment/deploymentDetailGrid :: ajaxDeploymentList";
	}
	

	// openViewBox lov description
		@RequestMapping("/viewLovDescription")
		@ResponseBody
		public String viewLovDescription(@RequestParam(name = "discription") String discription) {
			String response = "error";
			try {
				if (discription != null) {
					
					response = reqService.viewLovDescription(discription);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println("response.." + response);
			return response;
		}
		
		@RequestMapping(value = "/statisticalDashboard")
		public String statisticalDashboard ( Model map) {
			
			List<ReqTran> list = new ArrayList<>();
			List<ReqTran> waiting_for_approval_list = null;
			List<ReqTran> closed_by_lhs_list = null;
			List<ReqTran> closed_by_fgs_list = null;
			List<ReqTran> reinitiated_list = null;
			List<ReqTran> rejected_list =null;
			List<ReqTran> work_in_progress_list =null;
			
			Map<String,Integer> req_count_list = new LinkedHashMap<String,Integer>();
			try {
//				list = reqrepo.findAll();
				list = reqrepo.getListOfEntries();
				
				if(list != null) {
					if(list.size() > 0) {
						waiting_for_approval_list = list.stream().filter(c ->  c.getCurrent_req_status().equalsIgnoreCase("CRS_015")).collect(Collectors.toList());
						closed_by_lhs_list = list.stream().filter(c ->  c.getCurrent_req_status().equalsIgnoreCase("CRS_003")) .collect(Collectors.toList());
						
						closed_by_fgs_list = list.stream().filter(c ->  c.getCurrent_req_status().equalsIgnoreCase("CRS_006")) .collect(Collectors.toList());
						
						reinitiated_list  = list.stream().filter(c ->  c.getCurrent_req_status().equalsIgnoreCase("CRS_007")) .collect(Collectors.toList());
						
						rejected_list = list.stream().filter(c -> c.getCurrent_req_status().equalsIgnoreCase("CRS_004")) .collect(Collectors.toList());
						
						work_in_progress_list = list.stream()
																.filter(c ->  !c.getCurrent_req_status().equalsIgnoreCase("CRS_015")) 
																.filter(c -> !c.getCurrent_req_status().equalsIgnoreCase("CRS_003"))
																.filter(c -> !c.getCurrent_req_status().equalsIgnoreCase("CRS_006"))
																.filter(c ->  !c.getCurrent_req_status().equalsIgnoreCase("CRS_007"))
																.filter(c ->  !c.getCurrent_req_status().equalsIgnoreCase("CRS_004"))
																
														.collect(Collectors.toList());
						
						
						req_count_list.put("Waiting For Approval", waiting_for_approval_list.size());
						req_count_list.put("Work In Progress", work_in_progress_list.size());
						req_count_list.put("Closed By LHS Team", closed_by_lhs_list.size());
						req_count_list.put("Closed By FGS Team", closed_by_fgs_list.size());
						req_count_list.put("Re-Initiated", reinitiated_list.size());
						req_count_list.put("Rejected",rejected_list.size());
						req_count_list.put("Total Requisitions", list.size());
						
						 System.out.println("req_count_list==>"+req_count_list);
					}
					
					map.addAttribute("total_req_list", list.size());
					if(list.size() == 0) {
						map.addAttribute("req_count_list", 0);
						map.addAttribute("waiting_for_approval_list", 0);
						map.addAttribute("work_in_progress_list",0);
						map.addAttribute("closed_by_lhs_list", 0);
						map.addAttribute("closed_by_fgs_list", 0);
						map.addAttribute("reinitiated_list", 0);
						map.addAttribute("rejected_list", 0);
					}else{
						map.addAttribute("req_count_list", req_count_list);
						map.addAttribute("waiting_for_approval_list", waiting_for_approval_list.size());
						map.addAttribute("work_in_progress_list", work_in_progress_list.size());
						map.addAttribute("closed_by_lhs_list", closed_by_lhs_list.size());
						map.addAttribute("closed_by_fgs_list", closed_by_fgs_list.size());
						map.addAttribute("reinitiated_list", reinitiated_list.size());
						map.addAttribute("rejected_list", rejected_list.size());
					}
					
				} 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("list.size=="+list.size());
			
			
			
			
			return "/pages/requisition/statisticalDashboard";
		}
		
		
		@RequestMapping(value = "/clientStatisticalDashboard")
		public String clientStatisticalDashboard ( Model map) {
			
			List<ReqTran> list = new ArrayList<>();
			List<ReqTran> waiting_for_approval_list = null;
			List<ReqTran> closed_by_lhs_list = null;
			List<ReqTran> closed_by_fgs_list = null;
			List<ReqTran> reinitiated_list = null;
			List<ReqTran> rejected_list =null;
			List<ReqTran> work_in_progress_list =null;
			
			Map<String,Integer> req_count_list = new LinkedHashMap<String,Integer>();
			try {
				list = reqrepo.getClientReq();
				
				
				if(list != null) {
					if(list.size() > 0) {
						waiting_for_approval_list = list.stream().filter(c -> c.getCurrent_req_status().equalsIgnoreCase("CRS_015")) .collect(Collectors.toList());
						
						closed_by_lhs_list = list.stream().filter(c -> c.getCurrent_req_status().equalsIgnoreCase("CRS_003")) .collect(Collectors.toList());
						
						closed_by_fgs_list = list.stream().filter(c -> c.getCurrent_req_status().equalsIgnoreCase("CRS_006")) .collect(Collectors.toList());
						
						reinitiated_list  = list.stream().filter(c -> c.getCurrent_req_status().equalsIgnoreCase("CRS_007")) .collect(Collectors.toList());
						
						rejected_list = list.stream().filter(c -> c.getCurrent_req_status().equalsIgnoreCase("CRS_004")) .collect(Collectors.toList());
						
						work_in_progress_list = list.stream()
																.filter(c -> !c.getCurrent_req_status().equalsIgnoreCase("CRS_015")) 
																.filter(c -> !c.getCurrent_req_status().equalsIgnoreCase("CRS_003"))
																.filter(c -> !c.getCurrent_req_status().equalsIgnoreCase("CRS_006"))
																.filter(c -> !c.getCurrent_req_status().equalsIgnoreCase("CRS_007"))
																.filter(c -> !c.getCurrent_req_status().equalsIgnoreCase("CRS_004"))
																
														.collect(Collectors.toList());
						
						
						req_count_list.put("Waiting For Approval", waiting_for_approval_list.size());
						req_count_list.put("Work In Progress", work_in_progress_list.size());
						req_count_list.put("Closed By LHS Team", closed_by_lhs_list.size());
						req_count_list.put("Closed By FGS Team", closed_by_fgs_list.size());
						req_count_list.put("Re-Initiated", reinitiated_list.size());
						req_count_list.put("Rejected",rejected_list.size());
						req_count_list.put("Total Requisitions", list.size());
						
						 System.out.println("req_count_list==>"+req_count_list);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			map.addAttribute("req_count_list", req_count_list);
			map.addAttribute("total_req_list", list.size());
			map.addAttribute("waiting_for_approval_list", waiting_for_approval_list.size());
			map.addAttribute("work_in_progress_list", work_in_progress_list.size());
			map.addAttribute("closed_by_lhs_list", closed_by_lhs_list.size());
			map.addAttribute("closed_by_fgs_list", closed_by_fgs_list.size());
			map.addAttribute("reinitiated_list", reinitiated_list.size());
			map.addAttribute("rejected_list", rejected_list.size());
			
			
			return "/pages/requisition/clientStatisticalDashboard";
		}
	
		@RequestMapping("/onsiteReqDash")
		public String onsiteReqDash(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,@RequestParam(name = "req_code", required = false) String req_code) {
			
			
			session.setAttribute("ACTIVE_TAB", "MENU-001");
			session.setAttribute("ACTIVE_SUB_TAB", "MENU-038");
			
			    System.out.println("req_code==="+req_code);
				Map<String, String> projectList = new HashMap<>();
				Map<String, String> viewList = new HashMap<>();
				Map<String, String> userList = new HashMap<>();
				List<ReqTran> onsiteReqList = new ArrayList<>();
				Map<String, String> statusList = new HashMap<>();
				String userCode=null;


				try {

					UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
				    userCode = userMast.getUser_code();
					projectList = projectService.getProjectList1();
					onsiteReqList=reqService.getOnsiteReq();
					userList = userService.getUserByUserType("LHS");
					viewList=reqService.getViewListIssue();
					statusList=reqService.getstatusList();
                    System.out.println("onsiteReqList=="+onsiteReqList);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				try {

					if (filter == null) {
						filter = new FilterDTO();
					}

					long total = 10l;
					total = reqService.getOnsiteDetailsCount(filter);
					//System.out.println("total1======"+total);
					String recPerPage = "";
					if (dataGridDTO != null && dataGridDTO.getPaginator() != null
							&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
						recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
					} else {
						recPerPage = "10";
					}

					PaginatorManipulation manipulation = new PaginatorManipulation();
					PaginatorDTO paginator = manipulation.getPaginatorCount("onsiteReqGrid", total, recPerPage);
					
					dataGridDTO.setFilter(filter);
					dataGridDTO.setPaginator(paginator);
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		
				map.addAttribute("projectList", projectList);
				map.addAttribute("onsiteReqList", onsiteReqList);
				map.addAttribute("req_code", req_code);
				map.addAttribute("viewList", viewList);
				map.addAttribute("userList", userList);
				map.addAttribute("statusList", statusList);



			return "pages/requisition/OnsiteReqDash";
		}
		
		@RequestMapping("/onsiteReqGrid")
		public String onsiteReqGrid(Model map, HttpServletRequest request, FilterDTO filter, HttpSession session,
				@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO) {

				Map<String, String> projectList = new HashMap<>();
				List<ReqTran> onsiteReqList = new ArrayList<>();
				Map<String, String> viewList = new HashMap<>();
				Map<String, String> userList = new HashMap<>();
				Map<String, String> statusList = new HashMap<>();

				List<ReqTran> onsiteReqListGrid=null;
				String userCode=null;
				String recPerPage = "";
				long total = 10l;
				try {

					UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
				    userCode = userMast.getUser_code();
					projectList = projectService.getProjectList1();
					onsiteReqList=reqService.getOnsiteReq();
					total = reqService.getOnsiteDetailsCount(filter);
					viewList=reqService.getViewListIssue();
					userList = userService.getUserByUserType("LHS");
					statusList=reqService.getstatusList();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				try {
					
					dataGridDTO.setFilter(filter);
					recPerPage = (String) session.getAttribute("recPerPage");
				    onsiteReqListGrid = reqService.getOnsiteList(filter, dataGridDTO, recPerPage, total,viewList);
					System.out.println("onsiteReqListGrid====="+onsiteReqListGrid);
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
				}catch (Exception e) {
				e.printStackTrace();
			}
				
				map.addAttribute("onsiteReqListGrid", onsiteReqListGrid);
				map.addAttribute("projectList", projectList);
				map.addAttribute("onsiteReqList", onsiteReqList);
				map.addAttribute("viewList", viewList);
				map.addAttribute("userList", userList);
				map.addAttribute("statusList", statusList);


				
			return "pages/requisition/OnsiteReqDashDataGrid :: ajaxOnsiteList";
		}
		
		
		@RequestMapping("/viewOnsiteDetail")
		@ResponseBody
		public String viewOnsiteDetail(@RequestParam(name = "Req_code") String Req_code) {

			String response = "error";
			try {
				if (Req_code != null) {
					response = reqService.viewOnsiteReq(Req_code);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return response;
		}

		@RequestMapping(value = "Onsitefilter", method = RequestMethod.POST)
		public String Onsitefilter(Model map,@Param(value = "req_type") String req_type,
				//@Param(value = "deploy_delivered_date") String deploy_delivered_date,
				@Param(value = "from_date") String from_date,
				@Param(value = "to_date") String to_date,
				@Param(value = "req_code") String req_code,
				@Param(value = "from_date_new") String from_date_new,
				@Param(value = "to_date_new") String to_date_new,
				@Param(value = "current_req_status") String current_req_status,
			   HttpSession session,FilterDTO filter) {
			
			//System.out.println("from_date=="+from_date);
			//System.out.println("to_date=="+to_date);

			List<ReqTran> onsiteReqListGrid = new ArrayList<ReqTran>();
			Map<String, String> viewList = new HashMap<>();
			Map<String, String> userList = new HashMap<>();
			Map<String, String> statusList = new HashMap<>();

			DataGridDTO dataGridDTO = new DataGridDTO();
			filter.setFrom_date1(from_date);
			filter.setTo_date1(to_date);
			String recPerPage = (String) session.getAttribute("recPerPage");
			int total = reqService.getcountOnsiteTable(req_type,req_code,current_req_status,from_date,to_date,from_date_new,to_date_new,filter);

			System.out.println("total====="+total);
			PaginatorManipulation manipulation = new PaginatorManipulation();
			try {
				viewList=reqService.getViewListIssue();
				userList = userService.getUserByUserType("LHS");
				statusList=reqService.getstatusList();

				onsiteReqListGrid = reqService.getOnsiteListFilter(req_type,req_code,current_req_status,from_date,to_date,from_date_new,to_date_new,filter);
				System.out.println("onsiteReqListGrid in filter====="+onsiteReqListGrid);

				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				dataGridDTO.setPaginator(paginator);
				
			
				map.addAttribute("dataGridDTO", dataGridDTO);
			} catch (Exception e) {
				e.printStackTrace(); // TODO: handle exception
			}

			
			map.addAttribute("onsiteReqListGrid", onsiteReqListGrid);
			map.addAttribute("viewList", viewList);
			map.addAttribute("userList", userList);
			map.addAttribute("statusList", statusList);
			return "pages/requisition/OnsiteReqDashDataGrid :: ajaxOnsiteList";
		}
		    
		
		                                      
		
		
		@RequestMapping("/closedOnsiteRemark")
		@ResponseBody
		public String closedOnsiteRemark(ReqTran entity, HttpSession httpSession, Model map,@RequestParam(name = "req_code") String req_code,@RequestParam(name = "remark") String remark) {
			String response = "error";
			ReqTran entity1 = new ReqTran();
			try {
		       response = reqService.ClosedOnsiteReq(entity1, req_code,remark);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		
		
		

		@RequestMapping("/StartWork")
		@ResponseBody
		public String StartWork(ReqTran entity, HttpSession httpSession, Model map,@RequestParam(name = "req_code") String req_code) {
			String response = "error";
			ReqTran entity1 = new ReqTran();
			try {
		       response = reqService.StartWorkReq(entity1, req_code);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		
		
		@RequestMapping("/onsiteReqEntry")
		public String onsiteReqEntry(Model map) {
			String action = "save";
			Map<String, String> reqIssueType = new HashMap<>();
			ReqTranAttach reqTranAttachEntity = new ReqTranAttach();

			try {
				reqIssueType = reqService.getAllReqIssueTypeCodeName();
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.addAttribute("reqIssueType", reqIssueType);
			map.addAttribute("action", action);
			map.addAttribute("reqTranAttachEntity", reqTranAttachEntity);


			return "pages/requisition/OnsiteReqEntry";
		}
		
		@RequestMapping("/saveOnsiteReqDetail")
		@ResponseBody
		public String saveOnsiteReqDetail(ReqTran entity,  HttpSession session, Model map) {
			




			ReqTranAttach reqTranAttachEntity = new ReqTranAttach();
			
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
		    String userCode = userMast.getUser_code();
		    System.out.println("Usercode."+userCode);
			String response = "error";
			String action = "";
			String maxCount = "";

			//entity.setReq_type(req_type);
			//entity.setIssue_description(issue_description);
			//entity.setSample_data_filter_str(sample_data_filter_str);
			//entity.setDeploy_delivered_date(deploy_delivered_date);

			Date targetdate= entity.getDeploy_delivered_date();
			entity.setDeploy_delivered_date(targetdate);
			entity.setLastupdate(new Date());
			entity.setReported_date(new Date());
			entity.setReported_by(userCode);
			entity.setReq_entered_mode("O");
			entity.setCurrent_req_status("CRS_018");

			try {
				System.out.println("File name.."+entity.getProject_name());
				String attachfilenm = entity.getOnsite_req_att().getOriginalFilename();
				byte[] attachbfile = entity.getOnsite_req_att().getBytes();
				
				if (entity.getReq_code().isEmpty()) {
					action = "add";
				}
				
				ReqTran savedDoc = reqService.saveOnsiteReqDetail(entity);
				//System.out.println("savedDoc====="+savedDoc);
				reqTranAttachEntity.setLast_update(new Date());
				reqTranAttachEntity.setReq_code(savedDoc.getReq_code());
				reqTranAttachEntity.setReq_attach_name(attachfilenm);
				reqTranAttachEntity.setReq_attach(attachbfile);
				
				reqService.savelogo(reqTranAttachEntity);
				// System.out.println("call controller2");
				// response = reqService.saveOnsiteReqDetail(entity);
				// System.out.println("call controller3");
				
				response = "success";
				if (response.equalsIgnoreCase("success")) {
					
					maxCount = reqrepo.getMaxCount();
					System.out.println("size=="+maxCount);
				}
			
		response = response + "," + maxCount;
		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("response..."+response);
			map.addAttribute("req_code", entity.getReq_code());
			return response;
		}
		
		@RequestMapping("downloadOnsiteFile")
		public String downloadOnsiteFile(ReqTranAttach entity, HttpServletResponse response,
				HttpSession session, @RequestParam(name = "req_code") String req_code)throws Exception {
			System.out.println("req_code="+req_code);
			String username = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");
			byte b[] = entity.getReq_attach();
			try {
				Class.forName("oracle.jdbc.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.14:1521/orcl", username, password);
				Statement stmt = con.createStatement();
				String query = "select req_attach_name, req_attach from req_tran_attach where req_code='"+ req_code+"'";
				System.out.println("downloadFile query.."+query);
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					File blobFile = new File(rs.getString("req_attach_name"));
					Blob blob = rs.getBlob("req_attach");
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
}




