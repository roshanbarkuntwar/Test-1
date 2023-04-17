package com.lhs.taxcpcAdmin.controller.master;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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


import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.BankAuditAttach;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;
import com.lhs.taxcpcAdmin.model.entity.ReqTranAttach;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.service.EntityMastService;
import com.lhs.taxcpcAdmin.service.LhssysTaxcpcBankAuditService;
import com.lhs.taxcpcAdmin.service.ProjectMastService;



@Controller
public class ApplicationAuditController {
	
	
	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	private EntityMastService entityMastService;
	
//	@Autowired
//	private LhssysTaxcpcBankAuditService lhssysTaxcpcBankAuditService;
	
	

	@Autowired
	private ProjectMastService projectService;
	
	@Autowired 
	private LhssysTaxcpcBankAuditService bankAuditService;
	
	 @Autowired
	private Environment env;
	
	
	@RequestMapping(value = "AppAuditDiscription")
	public String AppAuditDiscription(HttpSession session) {
		
		session.setAttribute("ACTIVE_TAB", "MENU-033");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-034");
		
		return "./pages/AppAudit/AppAuditDiscription";
	}
	
	
	
	
//	@RequestMapping(value = "appAuditDashboardFilter")
//	public String appAuditDashboardFilter(@RequestParam(value = "entity_code",required=false) String entity_code,
//			@RequestParam(value = "project_name",required=false) String project_name,
//			@RequestParam(value = "audit_type",required=false) String audit_type, HttpSession session,ModelMap map) {
//		
//            List<LhssysTaxcpcBankAuditMast> entityGrid = new ArrayList<LhssysTaxcpcBankAuditMast>();
//		
//		DataGridDTO dataGridDTO = new DataGridDTO();
//		
//		String recPerPage = (String) session.getAttribute("recPerPage");
//		int total = bankAuditService.getCountTable(entity_code,project_name,audit_type);
//		
//		PaginatorManipulation manipulation = new PaginatorManipulation();
//		
//		
//		try {
//			
//			entityGrid = bankAuditService.getApplicationListFilter(entity_code,project_name,audit_type);
//			System.out.println("entityGrid.."+entityGrid);
//			
//			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
//			dataGridDTO.setPaginator(paginator);
//			
//		
//			map.addAttribute("dataGridDTO", dataGridDTO);
//		} catch (Exception e) {
//			e.printStackTrace(); // TODO: handle exception
//		}
//		
//		return "pages/AppAudit/appAuditDashboardGrid :: ajaxAppAudit";
//	}
//	
	
	
	
	@RequestMapping(value ="appAuditDashboard")
	public String appAuditDashboard(HttpSession session, ModelMap map, HttpServletRequest request, FilterDTO filter,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,
			@Param(value = "entity_code") String entity_code ,
			@Param(value = "audit_type") String audit_type,
			@Param(value = "project_name") String project_name) {
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> entityNameList = new HashMap<String, String>();
		List<LhssysTaxcpcBankAuditMast> list = new ArrayList<>();
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-034");
		try {
//			list = lhssysTaxcpcBankAuditService.getAppAuditList();
			
			appNameList = projectService.getProjectList1();
			entityNameList = entityMastService.getEntityList(); 
			
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			if (filter == null) {
				filter = new FilterDTO();
				filter.setAudit_type(audit_type);
//				System.out.println("filter1.." + filter);
			}
			filter.setAudit_type(audit_type);
			filter.setProject_code(project_name);
			
			long total = 10l;
			total = bankAuditService.getCount(filter);
			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}
			
//			System.out.println("inside controller..");
//			System.out.println("recPerPage.." + recPerPage);
			System.out.println("total.." + total);
			session.setAttribute("project_name", filter.getProject_code());
			session.setAttribute("audit_type", filter.getAudit_type());
			
			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("appAuditDashboardGrid", total, recPerPage);
			session.setAttribute("recPerPage", recPerPage);
			
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("appAuditGrid", list);
		map.addAttribute("entityNameList", entityNameList);
		map.addAttribute("appNameList", appNameList);
		map.addAttribute("dataGridDTO", dataGridDTO);

		return "pages/AppAudit/appAuditDashboard";
	}
	
	@RequestMapping(value = "/appAuditDashboardGrid", method = RequestMethod.POST)
	public String appAuditDashboardGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {
		List<LhssysTaxcpcBankAuditMast> list = new ArrayList<>();
		Map<String, String> appNameList = new HashMap<>();
		Map<String, String> entityNameList = new HashMap<String, String>();
		
		
		String audit_type = (String) session.getAttribute("audit_type");
		String project_name = (String) session.getAttribute("project_name");
		filter.setAudit_type(audit_type);
		filter.setProject_code(project_name);
		
		String recPerPage = "";

		try {
			long total = 10l;
			total = bankAuditService.getCount(filter);
//			if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
//				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
//			} else {
//				recPerPage = "10";
//			}
//			System.out.println("inside controller..");
//			System.out.println("recPerPage.." + recPerPage);
			System.out.println("total..>>" + total);
//			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			
			appNameList = projectService.getProjectList1();
			entityNameList =entityMastService.getEntityList();
			
			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");
			list = bankAuditService.getAppBroseList(session, filter, dataGridDTO,recPerPage);

			
//			System.out.println("list===>"+list);
			long slnoStartFrom = 0l;
			long  pagenumber = dataGridDTO.getPaginator().getPageNumber();
			if (dataGridDTO != null) {
				if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
					slnoStartFrom = Long.parseLong(dataGridDTO.getPaginator().getRecordsPerPage());
					slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
				}
				PaginatorManipulation manipulation = new PaginatorManipulation();
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
				session.setAttribute("recPerPage", recPerPage);
				
//				dataGridDTO.setFilter(filter);
				dataGridDTO.setPaginator(paginator);
				dataGridDTO.getPaginator().setPageNumber(pagenumber);
			}

			
			
			map.addAttribute("slnoStartFrom", slnoStartFrom);
//			map.addAttribute("dataGridDTO", dataGridDTO);
			System.out.println("dataGridDTO...../...."+dataGridDTO.getPaginator().getTotalRecords());

		} catch (Exception e) { 
			e.printStackTrace();
		}
//		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("appAuditGrid", list);
		map.addAttribute("appNameList", appNameList);
		map.addAttribute("entityNameList", entityNameList);
		map.addAttribute("dataGridDTO", dataGridDTO);

		return "pages/AppAudit/appAuditDashboardGrid :: ajaxAppAudit";
	} 
	
		// openViewBox 
		@RequestMapping("/viewAppAuditDashboard")
		@ResponseBody
		public String viewAppAuditDashboard(@RequestParam(name = "seq_id") Integer seq_id, HttpSession session) {
			String response = "error";
			System.out.println("seq_id=>>>>>"+seq_id);
			try {
				if (seq_id != null) {
					
					response = bankAuditService.viewAppAuditDashboard(seq_id);
				}
			} catch (Exception e) { 
				e.printStackTrace();
			}
			return response;
		}

//////////////////////
		
		@RequestMapping(value ="AppAuditEntry", method = RequestMethod.GET)
		public String AppAuditEntry(Model model, HttpSession session) {
			
			try {
				session.setAttribute("ACTIVE_TAB", "MENU-033");
				session.setAttribute("ACTIVE_SUB_TAB", "MENU-035");
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}

			return "pages/AppAudit/AppSec";
		}

		

		@RequestMapping(value ="AppSecEntry",method = RequestMethod.GET)

		public String AppSecEntry(Model map,@RequestParam(name = "seq_id", required = false) Integer seq_id,HttpSession session) {

			
			String action = "save";
			
			Map<String, String> entityList = new HashMap<>();
			Map<String, String> projectList = new HashMap<>();

			String docfile = "";
			String fileattach=" ";
			LhssysTaxcpcBankAuditMast appList=new LhssysTaxcpcBankAuditMast();
			try {
				entityList=entityMastService.getEntityList();
				projectList = projectService.getProjectList1();
				if(seq_id !=null) {
					action = "edit";
					appList = bankAuditService.getEditList(seq_id);
					docfile=bankAuditService.getfileName(seq_id);
					fileattach=bankAuditService.getfile(seq_id);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			session.setAttribute("action", action);
			session.setAttribute("seq_id", seq_id);
			
			map.addAttribute("entityList", entityList);
			map.addAttribute("appList", appList);
			map.addAttribute("action", action);
			map.addAttribute("docfile", docfile);
			map.addAttribute("seq_id", seq_id);
			map.addAttribute("fileattach", fileattach);
			map.addAttribute("projectList", projectList);

			return "pages/AppAudit/AppSecEntry";
		}
		
		

		@RequestMapping(value ="OtherEntry",method = RequestMethod.GET)
		public String OtherEntry(Model map,@RequestParam(name = "seq_id", required = false) Integer seq_id,HttpSession session) {
			
			String action = "save";
			Map<String, String> entityList = new HashMap<>();
			Map<String, String> projectList = new HashMap<>();

			String docfile = "";
			String fileattach=" ";
			LhssysTaxcpcBankAuditMast appList=new LhssysTaxcpcBankAuditMast();
			try {
				if(seq_id !=null) {
					action = "edit";
					appList = bankAuditService.getEditList(seq_id);
					docfile=bankAuditService.getfileName(seq_id);
					fileattach=bankAuditService.getfile(seq_id);
				}
				entityList=entityMastService.getEntityList();
				projectList = projectService.getProjectList1();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			session.setAttribute("seq_id", seq_id);

			
			map.addAttribute("entityList", entityList);
			map.addAttribute("appList", appList);
			map.addAttribute("action", action);
			map.addAttribute("docfile", docfile);
			map.addAttribute("fileattach", fileattach);
			map.addAttribute("projectList", projectList);

			return "pages/AppAudit/OtherEntryForm";
		}
		
		

		@RequestMapping(value ="vulnerableEntry", method = RequestMethod.GET)

		public String vulnerableEntry(Model model, @RequestParam(name = "seq_id", required = false) Integer  seq_id,
       HttpSession session) {
			System.out.println("welcome to vulnerableEntry"+seq_id);
			String action = "save";
			String docfile = "";
			
			LhssysTaxcpcBankAuditMast AuditEntity = new LhssysTaxcpcBankAuditMast();
			Map<String, String> entityNameList = new HashMap<>();
			Map<String, String> projectName = new HashMap<>();
			try {
				entityNameList = entityMastService.getEntityList();
				projectName = bankAuditService.getProjectMast(); 
				if(seq_id != null) {
				
				action = "edit";
				AuditEntity = bankAuditService.getAuditDetails(seq_id);
				docfile=bankAuditService.getfileName(seq_id);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			model.addAttribute("projectName", projectName);
			model.addAttribute("action", action);
			model.addAttribute("bankAuditEntity", AuditEntity);
			model.addAttribute("entityNameList", entityNameList);
			model.addAttribute("docfile", docfile);
			session.setAttribute("action", action);
			session.setAttribute("seq_id", seq_id);
			return "pages/AppAudit/VulnerabalEntry";
		}


		
	@RequestMapping("/saveAuditForm")
	@ResponseBody
	public String saveAuditForm(LhssysTaxcpcBankAuditMast entity, HttpSession httpsession) throws IOException {

		String response = "error";
	    String	 action =(String)	httpsession.getAttribute("action");
		BankAuditAttach bankentity = new BankAuditAttach();
		
		String attachfilenm = entity.getDoc_file().getOriginalFilename();
		byte[] attachbfile = entity.getDoc_file().getBytes();
		
		
		UserMast userMast = (UserMast) httpsession.getAttribute("LOGIN_USER");
		String UserName=userMast.getUser_name();
		try {
			entity.setLastupdate(new Date());
			entity.setAudit_type("APS");
			entity.setAudit_resolve_status("U");
			entity.setAudit_resolve_by(UserName);
			
			if(!attachfilenm.isEmpty()) {
				entity.setFlag("U");
			}else {
				
			}
			LhssysTaxcpcBankAuditMast savedDoc = bankAuditService.saveTaxcpcBankAuditMast(entity);
			
			String file = entity.getDoc_file().getOriginalFilename();
			byte[] bfile = entity.getDoc_file().getBytes();
			        bankentity.setLastupdate(savedDoc.getLastupdate());
			        bankentity.setAudit_mast_seq_id(entity.getSeq_id());
					bankentity.setDoc_attach_name(file);
					bankentity.setAudit_doc_attach(bfile);
					bankAuditService.savelogo(bankentity);
					response = "success";
					

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return response;
	}


	

	@RequestMapping("/saveOtherForm")
	@ResponseBody
	public String saveOtherForm(LhssysTaxcpcBankAuditMast entity, HttpSession httpsession) throws IOException {

		String response = "error";
		UserMast userMast = (UserMast) httpsession.getAttribute("LOGIN_USER");
		String UserName=userMast.getUser_name();
		BankAuditAttach bankentity = new BankAuditAttach();
		String attachfilenm = entity.getDoc_file().getOriginalFilename();
		byte[] attachbfile = entity.getDoc_file().getBytes();
		
		try {
			entity.setLastupdate(new Date());
			entity.setAudit_type("OT");
			entity.setAudit_resolve_status("U");
			entity.setAudit_resolve_by(UserName);
			
			if(!attachfilenm.isEmpty()) {
				entity.setFlag("U");
			}else {
				
			}
			LhssysTaxcpcBankAuditMast savedDoc = bankAuditService.saveTaxcpcBankAuditMastOther(entity);
			String file = entity.getDoc_file().getOriginalFilename();
			byte[] bfile = entity.getDoc_file().getBytes();
			
			     bankentity.setLastupdate(savedDoc.getLastupdate());

			        bankentity.setAudit_mast_seq_id(entity.getSeq_id());

					bankentity.setDoc_attach_name(file);
					bankentity.setAudit_doc_attach(bfile);
					bankAuditService.savelogo(bankentity);
					response = "success";
					
				System.out.println("savedDoc========"+savedDoc);

      } catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return response;
	}


		@RequestMapping("/saveVADetail")
		@ResponseBody
		public String saveVADetail(LhssysTaxcpcBankAuditMast entity, Model map,HttpSession session) throws IOException {

			System.out.println("entity========"+entity);
			String response = "error";
		    String	 action =(String)	session.getAttribute("action");
			BankAuditAttach bankentity = new BankAuditAttach();
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			String UserName=userMast.getUser_name();
			

			String attachfilenm = entity.getDoc_file().getOriginalFilename();
			byte[] attachbfile = entity.getDoc_file().getBytes();
			
			try {
				entity.setLastupdate(new Date());
				
				entity.setAudit_type("VA");
				entity.setAudit_resolve_status("U");
				entity.setAudit_resolve_by(UserName);
				

				if(!attachfilenm.isEmpty()) {
					entity.setFlag("U");
				}else {
					
				}
				LhssysTaxcpcBankAuditMast savedDoc = bankAuditService.saveTaxcpcBankAuditMast(entity);
				
				String file = entity.getDoc_file().getOriginalFilename();
				byte[] bfile = entity.getDoc_file().getBytes();
				
				        bankentity.setLastupdate(savedDoc.getLastupdate());
				        bankentity.setAudit_mast_seq_id(savedDoc.getSeq_id());
						bankentity.setDoc_attach_name(file);
						bankentity.setAudit_doc_attach(bfile);
						bankAuditService.savelogo(bankentity);
						response = "success";
						
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}


		@RequestMapping(value ="bestPracticeEntry", method = RequestMethod.GET)

		public String bestPracticeEntry(Model model,HttpSession session,@RequestParam(name = "seq_id", required = false) Integer seq_id) {


			
			String action = "save";
			String docfile = "";
			LhssysTaxcpcBankAuditMast list = new LhssysTaxcpcBankAuditMast();
			try {
				if(seq_id != null) {
					
					action = "edit";
					list = bankAuditService.getAuditDetails(seq_id);
					docfile=bankAuditService.getfileName(seq_id);
					}
				//list = bankAuditService.getBankAuditMastList();
				
			} catch (Exception e) {
				
			}
			model.addAttribute("action", action);
			model.addAttribute("bankAuditEntity", list);
			model.addAttribute("docfile", docfile);
			session.setAttribute("seq_id", seq_id);
			return "pages/AppAudit/BestPracticeEntry";
		}


		@RequestMapping("/saveBestPracticeDetail")
		@ResponseBody
		public String saveBestPracticeDetail(LhssysTaxcpcBankAuditMast entity, Model map,HttpSession session) {

			System.out.println("entity========"+entity);
			String response = "error";
		    String	 action =(String)	session.getAttribute("action");
			BankAuditAttach bankentity = new BankAuditAttach();
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			String UserName=userMast.getUser_name();
			try {
				entity.setLastupdate(new Date());
				
				entity.setAudit_type("BP");
				entity.setAudit_resolve_status("U");
				entity.setAudit_resolve_by(UserName);
				LhssysTaxcpcBankAuditMast savedDoc = bankAuditService.saveTaxcpcBankAuditMast(entity);
				

				

				String file = entity.getDoc_file().getOriginalFilename();
				byte[] bfile = entity.getDoc_file().getBytes();

				
				        bankentity.setLastupdate(savedDoc.getLastupdate());
				        bankentity.setAudit_mast_seq_id(savedDoc.getSeq_id());
						bankentity.setDoc_attach_name(file);
						bankentity.setAudit_doc_attach(bfile);
						bankAuditService.savelogo(bankentity);
						response = "success";
		
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}


	@RequestMapping("/UpdateAppSecForm")
	@ResponseBody
	public String UpdateAppSecForm(LhssysTaxcpcBankAuditMast entity, Model map,HttpSession httpSession) {
		
		 String response = "error";
		    Integer	seq_id =(Integer)httpSession.getAttribute("seq_id");
		 LhssysTaxcpcBankAuditMast entity1 = new LhssysTaxcpcBankAuditMast();
		 
			BankAuditAttach bankentity = new BankAuditAttach();
			
			bankentity=bankAuditService.getentitylistbycode(seq_id);

		try {
	       response = bankAuditService.UpdateForm(entity1,seq_id,entity,bankentity);
	       
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	

	
	//////Sneha
	
	@RequestMapping(value="sourceCodeReview")
	public String sourceCodeReview( Model model, @RequestParam(name = "seq_id", required = false) Integer seq_id,
			@RequestParam(name = "linktype", required = false) String linktype,
			@RequestParam(name = "Type", required = false) String Type) 
	{
		
		String action = "";
		String docfilenm="";
		System.out.println("seq_id.."+seq_id);
		Map<String, String> appList = new HashMap<>();
		Map<String, String> entityList = new HashMap<>();
		//List<LhssysTaxcpcBankAuditMast> list= new ArrayList<>();
		LhssysTaxcpcBankAuditMast entity= new LhssysTaxcpcBankAuditMast();
	
		try {
		//	list=bankAuditService.getSourceCodeList();
			 entityList=entityMastService.getEntityList();
			 appList=projectService.getProjectListWithStatus();
			 
			if(seq_id != null) {
				
				action="edit";
				entity=bankAuditService.getEditSourceCodeList(seq_id);
				//appList = bankAuditService.getEditList(seq_id);
				docfilenm=bankAuditService.getfileName(seq_id);
				Type="SC";
//			
				System.out.println("document=="+docfilenm);
				}else {
					System.out.println("else");

					Type="SC";
					action = "save";
					
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	   model.addAttribute("docfilenm", docfilenm);
		model.addAttribute("entityList", entityList);
		model.addAttribute("appList", appList);
		model.addAttribute("action", action); 
		model.addAttribute("entity", entity); 
		model.addAttribute("linktype", linktype);
		model.addAttribute("Type", Type);

		return "pages/AppAudit/SourceCodeReview";
	
	}
	
	


@RequestMapping("/updateReviewForm")
@ResponseBody
public String updateReviewForm(LhssysTaxcpcBankAuditMast entity, Model map, HttpSession httpSession) {
	
	    String response = "error";
	    System.out.println("inside update field...");
	    //Integer	 seq_id =(Integer)httpSession.getAttribute("seq_id");
	    UserMast userMast = (UserMast) httpSession.getAttribute("LOGIN_USER");
		String UserCode = userMast.getUser_code();
	    
	    System.out.println("seq_id.."+entity.getSeq_id());
	    System.out.println("LastUpdate....."+entity.getLastupdate());
//	    LhssysTaxcpcBankAuditMast entity1 = new LhssysTaxcpcBankAuditMast();
	 
		BankAuditAttach bankentity = new BankAuditAttach();
		bankentity=bankAuditService.getReviewEntitylistbycode(entity.getSeq_id());
		
			
	try 
	{
       response = bankAuditService.updateReviewForm(UserCode,entity.getSeq_id(),entity,bankentity);
       System.out.println("List==="+response);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return response;
}

@ResponseBody
@RequestMapping("/saveReviewDetails")
public String saveReviewDetails(LhssysTaxcpcBankAuditMast entity)
{
	System.out.println("Welcome to review List :"+entity);
	
	LhssysTaxcpcBankAuditMast entityList = new LhssysTaxcpcBankAuditMast();
	 
	String response="error";
	try
	{
		BankAuditAttach attachentity = new BankAuditAttach();
		

		String attachfilenm = entity.getDoc_file().getOriginalFilename();
		byte[] attachbfile = entity.getDoc_file().getBytes();
		if(!attachfilenm.isEmpty()) {
			entity.setFlag("U");
		}else {
			
		}
		entityList = bankAuditService.saveSRCAuditDetail(entity);
 		System.out.println("entityList=="+entityList);
 		
		attachentity.setLastupdate(new Date() );
		attachentity.setAudit_mast_seq_id(entity.getSeq_id());
		attachentity.setDoc_attach_name(attachfilenm);
		attachentity.setAudit_doc_attach(attachbfile);

		bankAuditService.saveattachDetail(attachentity);

//		System.out.println("List Of data========="+ saveddocs);
		response="success";
		
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	return response;
}


@RequestMapping("downloadResolveAttach")
public String downloadResolveAttach(BankAuditAttach entity, HttpServletResponse response,
		HttpSession session, @RequestParam(name = "seq_id") String seq_id)throws Exception {
	System.out.println("seq_id="+seq_id);
	
	String username = env.getProperty("spring.datasource.username");
	String password = env.getProperty("spring.datasource.password");

	byte b[] = entity.getAudit_doc_attach();
	try {

		Class.forName("oracle.jdbc.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.14:1521/orcl", username, password);
		Statement stmt = con.createStatement();
		String query = "select doc_attach_name, audit_doc_attach from bank_audit_attach where audit_mast_seq_id='"+ seq_id+"'";
		System.out.println("downloadFile query.."+query);
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			File blobFile = new File(rs.getString("doc_attach_name"));
			Blob blob = rs.getBlob("audit_doc_attach");
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


@RequestMapping("downloadUnresolveAttach")
public String downloadUnresolveAttach(BankAuditAttach entity, HttpServletResponse response,
		HttpSession session, @RequestParam(name = "seq_id") String seq_id)throws Exception {
	System.out.println("seq_id="+seq_id);
	
	String username = env.getProperty("spring.datasource.username");
	String password = env.getProperty("spring.datasource.password");

	byte b[] = entity.getAudit_doc_attach();
	try {

		Class.forName("oracle.jdbc.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.14:1521/orcl", username, password);
		Statement stmt = con.createStatement();
		String query = "select doc_resolve_attach_name, audit_doc_resolve_attach from bank_audit_attach where audit_mast_seq_id='"+ seq_id+"'";
		System.out.println("downloadFile query.."+query);
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			File blobFile = new File(rs.getString("doc_resolve_attach_name"));
			Blob blob = rs.getBlob("audit_doc_resolve_attach");
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

