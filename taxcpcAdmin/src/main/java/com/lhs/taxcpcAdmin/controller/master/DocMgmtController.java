package com.lhs.taxcpcAdmin.controller.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.DocTranAttach;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcLinkMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.service.DocMgmtService;
import com.lhs.taxcpcAdmin.service.ProjectMastService;
import com.lhs.taxcpcAdmin.service.UserMastService;
@PropertySource("classpath:application.properties")
@Controller
public class DocMgmtController {

	@Autowired
	DocMgmtService docMgmtService;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	ProjectMastService projectService;

	@Autowired
	UserMastService userService;
	
	@Autowired
    private Environment env;

	@RequestMapping("/docEntry")
	public String docEntry(HttpSession session, ModelMap modelmap,
			@RequestParam(value = "mode", required = false) String mode,
			@Param("doc_code,local_path,database_path,username") String doc_code, String local_path,
			String database_path, String username, DocTran entity) {

		String action = "save";
		Map<String, String> docTypeList = new HashMap<>();
		Map<String, String> userList = new HashMap<>();
		Map<String, String> projectList = new HashMap<>();
		String filePath = "";
		String file = "";
		String docfile = "";
		byte[] datafile = null;
		String local_path_flag = local_path;
		session.setAttribute("local_path_flag", local_path_flag);
		String data_path_flag = database_path;
		session.setAttribute("data_path_flag", data_path_flag);
		String page = "pages/docMgmt/docEntry";
		DocTran docTranEntity = new DocTran();
		try {

			docTypeList = docMgmtService.getDocTypeList();
			userList = userService.getUserByUserType("LHS");
			projectList = projectService.getProjectList1();

			if (!strUtl.isNull(mode) && mode.equalsIgnoreCase("Q")) {
				page = "pages/docMgmt/quickDocEntry";
			}

			if (!strUtl.isNull(doc_code)) {
				action = "edit";
				docTranEntity = docMgmtService.getDocDetailbydocCode(doc_code);
				filePath = docMgmtService.getFilePath(doc_code);
				if (filePath != null && local_path.equalsIgnoreCase("T")) {

					Path p = Paths.get(filePath);
					file = p.getFileName().toString();

				} else if (database_path.equalsIgnoreCase("T")) {

					docfile = docMgmtService.getDocAttachfile(doc_code);
					datafile = docMgmtService.getfileData(doc_code);

				} else if (local_path.equalsIgnoreCase("T") && database_path.equalsIgnoreCase("T")) {
					filePath = docMgmtService.getFilePath(doc_code);
					Path p = Paths.get(filePath);
					file = p.getFileName().toString();
					docfile = docMgmtService.getDocAttachfile(doc_code);
				}

				if (local_path.equalsIgnoreCase("T")) {
					modelmap.addAttribute("local", true);
				} else {
					modelmap.addAttribute("local", false);
				}
				if (database_path.equalsIgnoreCase("T")) {
					modelmap.addAttribute("data", true);
				} else {
					modelmap.addAttribute("data", false);
				}
			}

			session.setAttribute("doc_code", doc_code);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		modelmap.addAttribute("action", action);
		modelmap.addAttribute("docTranEntity", docTranEntity);
		modelmap.addAttribute("docTypeList", docTypeList);
		modelmap.addAttribute("userList", userList);
		modelmap.addAttribute("projectList", projectList);
		modelmap.addAttribute("file", file);
		modelmap.addAttribute("docfile", docfile);
		modelmap.addAttribute("database_path", database_path);
		modelmap.addAttribute("local_path", local_path);
		modelmap.addAttribute("datafile", datafile);

		return page;
	}

	@RequestMapping("/quickDocDetail")
	public String quickDocDetail(HttpSession session, ModelMap modelmap,@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		session.setAttribute("ACTIVE_TAB", "MENU-005");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-006");
		
		List<DocTran> quickDocList = new ArrayList<DocTran>();
		Map<String, String> docTypeList = new HashMap<>();
		Map<String, String> MenuList = new HashMap<>();
		UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
		String userCode = userMast.getUser_code();

		try {
			quickDocList = docMgmtService.getQuickDocList(userCode);
			docTypeList = docMgmtService.getDocTypeList();
			MenuList = userService.getAllUserCodeName();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		try {

			if (filter == null) {
				filter = new FilterDTO();
			}

			long total = 0l;
			total = docMgmtService.getquickDocDetailsCount(filter,userCode);

			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage ="8";
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCountDoc("quickDocDetailsGrid", total, recPerPage);
			//System.out.println("paginator=="+paginator);

			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			

			
		modelmap.addAttribute("dataGridDTO", dataGridDTO);
		modelmap.addAttribute("MenuList", MenuList);
		modelmap.addAttribute("docTypeList", docTypeList);
		modelmap.addAttribute("quickDocList", quickDocList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "pages/docMgmt/quickDocDetail";
	}

	
	
	
	@RequestMapping("/quickDocDetailsGrid")
	public String quickDocDetailsGrid(HttpSession session, ModelMap modelmap,@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		List<DocTran> quickDocList = new ArrayList<DocTran>();
		Map<String, String> docTypeList = new HashMap<>();
		Map<String, String> MenuList = new HashMap<>();
		UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
		String userCode = userMast.getUser_code();
         String recPerPage="";
         long  total = 0l;
		try {
			quickDocList = docMgmtService.getQuickDocList(userCode);

			docTypeList = docMgmtService.getDocTypeList();
			MenuList = userService.getAllUserCodeName();
			total = docMgmtService.getquickDocDetailsCount(filter,userCode);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		try {
			dataGridDTO.setFilter(filter);
			List<DocTran> docTranGrid = docMgmtService.getQuickDocDetailsList(filter, dataGridDTO, docTypeList,
					MenuList, recPerPage, total,userCode);
			
			
			modelmap.addAttribute("quickDocList", quickDocList);
			modelmap.addAttribute("datalist", docTranGrid);
			modelmap.addAttribute("dataGridDTO", dataGridDTO);
			modelmap.addAttribute("MenuList", MenuList);
			modelmap.addAttribute("docTypeList", docTypeList);
			modelmap.addAttribute("quickDocList", quickDocList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		

		return "pages/docMgmt/quickDocDetailsGrid ::ajaxQuickDocList";
	}

	
	
	@RequestMapping(value = "quickDetailFilter", method = RequestMethod.POST)
	public String docDetailFilter(Model map,@Param(value = "doc_name") String doc_name,HttpSession session) {
		
		List<DocTran> entityGrid = new ArrayList<DocTran>();
		
		DataGridDTO dataGridDTO = new DataGridDTO();
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = docMgmtService.getcountQuickTable(doc_name);
		
		PaginatorManipulation manipulation = new PaginatorManipulation();
		
		
		try {
			
			entityGrid = docMgmtService.getEntityListQuickFilter(doc_name);
			//System.out.println("entityGrid.."+entityGrid);
			
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			
		
			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}

		map.addAttribute("entityGrid", entityGrid);
		
		return "pages/docMgmt/quickDocDetailsGrid ::ajaxQuickDocList";
	}
	
	@RequestMapping("/docDetailss")
	public String docDetail(HttpSession session, ModelMap map) {
		List<DocTran> datalist = new ArrayList<>();
		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			datalist = docMgmtService.getDocDetails(userMast.getUser_code());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("datalist", datalist);
		return "pages/docMgmt/docDetail";
	}

	@RequestMapping(value = "/saveDocDetail")
	public @ResponseBody String saveDocDetail(DocTran entity, HttpSession session) {
		
		String Doc_Code = "";
		String response = "error";
		try {
			if (entity != null) {

				if (!entity.getDoc_mode().equalsIgnoreCase("Q")) {
					if (entity.getDoc_file() != null) {
						if (!strUtl.isNull(entity.getLocal_drive_flag())
								&& entity.getLocal_drive_flag().equalsIgnoreCase("T")) {
							String fileName = entity.getDoc_file().getOriginalFilename();
							Doc_Code = (String) session.getAttribute("doc_code");
							String getfileNameFromDb = "";
							
							if (strUtl.isNull(fileName)) {
								getfileNameFromDb = docMgmtService.getFilePath(Doc_Code);
							}

							SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

							String currDate = dateFormat.format(new Date());

							String filepath = "C:\\TaxCPC Document" + File.separator + currDate;
							if (strUtl.isNull(getfileNameFromDb)) {
								Boolean filesave = fileUpload(entity.getDoc_file(), filepath, fileName);

								if (filesave) {
									entity.setLocal_drive_path(filepath + File.separator + fileName);

								}

							} else {

								entity.setLocal_drive_path(getfileNameFromDb);
							}

						} else if (!strUtl.isNull(entity.getGoogle_drive_flag())
								&& entity.getGoogle_drive_flag().equalsIgnoreCase("T")) {

						} else if (!strUtl.isNull(entity.getDatabase_flag())
								&& entity.getDatabase_flag().equalsIgnoreCase("T")) {

						}

						entity.setEntry_date(new Date());
						entity.setLastupdate(new Date());

						DocTran savedDoc = docMgmtService.saveDocDetail(entity);

						String doc_code = savedDoc.getDoc_code();
						DocTranAttach docAttachEntity = new DocTranAttach();
						docAttachEntity = docMgmtService.getdocCode(doc_code);
						String file = entity.getDoc_file().getOriginalFilename();

						if (docAttachEntity == null) {
							if (savedDoc != null) {
								if (!strUtl.isNull(entity.getDatabase_flag())
										&& entity.getDatabase_flag().equalsIgnoreCase("T")) {
									DocTranAttach docEntity = new DocTranAttach();
									if (entity.getDoc_file().getBytes() != null) {
										docEntity.setDoc_attach(entity.getDoc_file().getBytes());
									}
									docEntity.setDoc_code(savedDoc.getDoc_code());
									docEntity.setDoc_attach_name(file);
									docMgmtService.saveDocAttachment(docEntity);
								}
								response = "success";
							}
						} else {
							if (savedDoc != null) {
								if (!strUtl.isNull(entity.getDatabase_flag())
										&& entity.getDatabase_flag().equalsIgnoreCase("T")) {

									if (entity.getDoc_file().getBytes() != null) {
										docAttachEntity.setDoc_attach(entity.getDoc_file().getBytes());
									}

									if (!strUtl.isNull(savedDoc.getDoc_code())) {
										docAttachEntity.setDoc_code(savedDoc.getDoc_code());
									}
									if (!strUtl.isNull(docAttachEntity.getDoc_attach_code())) {
										docAttachEntity.setDoc_attach_code(docAttachEntity.getDoc_attach_code());
									}

									if (!strUtl.isNull(file)) {
										docAttachEntity.setDoc_attach_name(file);
									}

									String filename = entity.getDoc_file().getOriginalFilename();
									if (!strUtl.isNull(filename)) {
										docMgmtService.saveDocAttachment(docAttachEntity);
									}
								}
								response = "success";
							}
						}
					}
				} else {
					entity.setEntry_date(new Date());
					entity.setLastupdate(new Date());
					DocTran savedDoc = docMgmtService.saveDocDetail(entity);

					if (savedDoc != null) {
						response = "success";
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;

	}

	private Boolean fileUpload(MultipartFile file, String filepath, String fileName) {
		Boolean msg = false;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		File path = new File(filepath);
		try {
			if (!path.exists()) {
				path.mkdirs();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		File newFile = new File(filepath + File.separator + fileName);
		try {
			inputStream = file.getInputStream();

			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[5000];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			msg = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
		return msg;
	}

	
	@RequestMapping(value = "/deleteDocument")
	public @ResponseBody String deleteUser(@RequestParam(name = "doc_code") String doc_code) {
		String response = "error";
		try {
			if (doc_code != null) {
				response = docMgmtService.deleteDocument(doc_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/impUrlDetailByType")
	public String impUrlDetailByType(Model map, @RequestParam(name = "link_type", required = false) String link_type,HttpSession session) {
		List<LhssysTaxcpcLinkMast> impLinkList = new ArrayList<>();
		try {
			impLinkList = docMgmtService.getLinkDetailByType(link_type);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("link_type", link_type);
		map.addAttribute("impLinkList", impLinkList);
		session.setAttribute("link_type", link_type);

		return "pages/importantUrl/importantUrlDetail";
	}
	
	@RequestMapping("/importantUrl")
	public String importantUrl(HttpSession session, Model map) {
		session.setAttribute("ACTIVE_TAB", "MENU-014");

		List<String> linkTypeList = new ArrayList<>();
		String linkType = (String) session.getAttribute("link_type");
		String userCode = null;
		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			 userCode = userMast.getUser_code();
			linkTypeList = docMgmtService.getImportantLinkTypeList(userMast.getUser_type());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.addAttribute("linkTypeList", linkTypeList);
		map.addAttribute("linkType", linkType);
		map.addAttribute("userCode", userCode);

		return "pages/importantUrl/importantUrl";
	}

	@RequestMapping("/importantUrlEntry")
	public String importantUrlEntry(HttpSession session, Model map,
			@RequestParam(name = "linkCode", required = false) String linkCode) {
		List<String> linkTypeList = new ArrayList<>();
		String action = "save";
		LhssysTaxcpcLinkMast entity = null;

		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			linkTypeList = docMgmtService.getImportantLinkTypeList(userMast.getUser_type());
			if (!strUtl.isNull(linkCode)) {
				action = "edit";
				entity = new LhssysTaxcpcLinkMast();
				entity = docMgmtService.getLinkDetailByCode(linkCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("linkTypeList", linkTypeList);
		map.addAttribute("linkEntity", entity);
		map.addAttribute("action", action);
		return "pages/importantUrl/importantUrlEntry";
	}

	

	@RequestMapping("/saveImpUrlDetail")
	@ResponseBody
	public String saveImpUrlDetail(LhssysTaxcpcLinkMast entity) {
		String response = "error";
		try {
			if (entity != null) {
				response = docMgmtService.saveImpUrlDetail(entity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/deleteLink")
	@ResponseBody
	public String deleteImpUrl(@RequestParam(name = "linkCode") String linkCode) {
		String response = "error";
		try {
			if (linkCode != null) {
				response = docMgmtService.deleteImpUrl(linkCode);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/viewLinkDetail")
	@ResponseBody
	public String viewLinkDetail(@RequestParam(name = "linkCode") String linkCode) {

		String response = "error";
		try {
			if (linkCode != null) {
				response = docMgmtService.viewLinkDetail(linkCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/docDetail")
	public String userDetails(HttpServletRequest request, Model map,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter,HttpSession session) {

		session.setAttribute("ACTIVE_TAB", "MENU-005");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-007");
		Map<String, String> docTypeList = new HashMap<>();
		Map<String, String> userList = new HashMap<>();
		Map<String, String> projectList = new HashMap<>();
		List<DocTranAttach> docTranAttachList = new ArrayList<>();
		List<DocTran> docTranEntityList = new ArrayList<>();
	

		String userCode=null;

		try {

			docTypeList = docMgmtService.getDocTypeList();
			userList = userService.getUserByUserType("LHS");
			projectList = projectService.getProjectList1();
			docTranEntityList = docMgmtService.getDocDetailEntity();
			docTranAttachList = docMgmtService.getDocDetailAttachList();
			
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
		    userCode = userMast.getUser_code();
		

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		try {

			if (filter == null) {
				filter = new FilterDTO();
			}

			long total = 10l;
			total = docMgmtService.getDocDetailsCount(filter);

			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCount("dacDetailsGrid", total, recPerPage);
			
			dataGridDTO.setFilter(filter);
			dataGridDTO.setPaginator(paginator);
			

			map.addAttribute("docTypeList", docTypeList);
			map.addAttribute("dataGridDTO", dataGridDTO);
			map.addAttribute("projectList", projectList);
			map.addAttribute("userList", userList);
			map.addAttribute("docTranEntityList", docTranEntityList);
			map.addAttribute("docTranAttachList", docTranAttachList);
			map.addAttribute("userCode", userCode);


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "pages/docMgmt/docDetail";
	}

	@RequestMapping(value = "/dacDetailsGrid", method = RequestMethod.POST)
	public String userLoginDetailsGrid(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		System.out.println("filterpppp=="+filter);
		Map<String, String> docTypeList = new HashMap<>();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> userList = new HashMap<>();

		String recPerPage = "";
		long total = 0l;

		try {
			docTypeList = docMgmtService.getDocTypeList();
			total = docMgmtService.getDocDetailsCount(filter);
			projectList = projectService.getProjectList1();
			userList = userService.getUserList();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	
		try {
			dataGridDTO.setFilter(filter);
			recPerPage = (String) session.getAttribute("recPerPage");

			List<DocTran> docTranGrid = docMgmtService.getDocDetailsList(filter, dataGridDTO, docTypeList, projectList,
					userList, recPerPage, total);
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
			map.addAttribute("docTypeList", docTypeList);
			map.addAttribute("projectList", projectList);
			map.addAttribute("userList", userList);
			map.addAttribute("slnoStartFrom", slnoStartFrom);
			map.addAttribute("datalist", docTranGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "pages/docMgmt/docDetailsDataGrid  :: ajaxDocList";
	}

	
	@RequestMapping(value = "docDetailFilter", method = RequestMethod.POST)
	public String docDetailFilter(Model map,@Param(value = "doc_code_type") String doc_code_type,
			@Param(value = "doc_name") String doc_name,@Param(value = "user_code") String user_code,
			@Param(value = "project_code") String project_code,HttpSession session) {
		
		List<DocTran> entityGrid = new ArrayList<DocTran>();
		
		DataGridDTO dataGridDTO = new DataGridDTO();
		
		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = docMgmtService.getcountTable(doc_code_type,doc_name,user_code,project_code);
		
		PaginatorManipulation manipulation = new PaginatorManipulation();
		
		
		try {
			
			entityGrid = docMgmtService.getEntityListFilter(doc_code_type,doc_name,user_code,project_code);
			System.out.println("entityGrid.."+entityGrid);
			
			PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
			dataGridDTO.setPaginator(paginator);
			
		
			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}

		map.addAttribute("entityGrid", entityGrid);
		
		return "pages/docMgmt/docDetailsDataGrid  :: ajaxDocList";
	}
	
	@RequestMapping("/viewDocTran")
	@ResponseBody
	public String viewWorkDetail(@RequestParam(name = "docCode") String docCode) {
		String response = "error";
		try {
			if (docCode != null) {
				response = docMgmtService.viewDocTran(docCode);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}
	
	

	@RequestMapping("downloadSingleFile")
	public String downloadSingleFile(HttpServletResponse response, HttpSession session,
			@RequestParam(name = "folderpath") String folderpath, @RequestParam("filename") String filename)
			throws Exception {
		String fname = filename;
		String filepath = folderpath;
		File file = new File(filepath, fname);
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fname, "UTF-8"));
		InputStream input = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		byte[] buff = new byte[1024];
		int index = 0;
		while ((index = input.read(buff)) != -1) {
			out.write(buff, 0, index);
			out.flush();
		}
		out.close();
		input.close();
		return filepath;
	}

	
	@RequestMapping("downloadDatabaseFile")
	public String downloadSingleFile(DocTranAttach entity, HttpServletResponse response, HttpSession session,
			@RequestParam(name = "doc_code") String doc_code) throws Exception {
		byte b[] = entity.getDoc_attach();
		try {
			String username = env.getProperty("spring.datasource.username");
			String password = env.getProperty("spring.datasource.password");

			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.100.14:1521/orcl", username, password);
			Statement stmt = con.createStatement();
			String query = "SELECT doc_attach,doc_attach_name FROM doc_tran_attach WHERE doc_code = " +doc_code;
			System.out.println("Query.............." + query);
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				File blobFile = new File(rs.getString("doc_attach_name"));
				Blob blob = rs.getBlob("doc_attach");
				InputStream in = blob.getBinaryStream();
				int length = in.available();
				byte[] blobBytes = new byte[length];
				in.read(blobBytes);
				byte barr[] = blob.getBytes(1, (int) blob.length());
				response.setHeader("Content-Disposition", "attachment;fileName=" + blobFile.getName());
				ServletOutputStream out = response.getOutputStream();
				out.write(barr);
				out.close();
				rs.close();
				stmt.close();
				con.close();
			}
		} catch (Exception e) {
		}
		return "blob";

	}
}