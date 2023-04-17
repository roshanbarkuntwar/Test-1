package com.lhs.taxcpcAdmin.controller.master;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.print.DocFlavor.STRING;
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
import com.lhs.taxcpcAdmin.dao.AppDetailsRepository;
import com.lhs.taxcpcAdmin.dao.ServerDetailsRepository;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.AppDetails;
import com.lhs.taxcpcAdmin.model.entity.BankAuditAttach;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.EntityLogoMast;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;
import com.lhs.taxcpcAdmin.model.entity.ServerDetailModel;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.service.EntityMastService;
import com.lhs.taxcpcAdmin.service.ProjectMastService;
import com.lhs.taxcpcAdmin.service.ServerDetails;
import com.sun.net.httpserver.Authenticator.Success;
import com.zaxxer.hikari.util.SuspendResumeLock;

@Controller
public class ServerController {

	@Autowired
	private ServerDetails serverDetails;

	@Autowired
	private EntityMastService entityMastService;

	@Autowired
	private ServerDetailsRepository serverDetailsRepository;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	private AppDetailsRepository appDetailsRepo;

	@Autowired
	ProjectMastService projectService;

//1
	@RequestMapping("/serverDetailEntry")
	public String serverDetailEntry(Model map, @RequestParam(name = "server_id", required = false) String server_id,
			@RequestParam(name = "server_type_code", required = false) String server_type_code, HttpSession session) {

		session.setAttribute("ACTIVE_TAB", "MENU-017");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-022");

		List<ServerDetailModel> serverlist = new ArrayList<ServerDetailModel>();
		Map<String, String> serverTypeList = new HashMap<String, String>();
		Map<String, String> entityNameList = new HashMap<String, String>();
		Map<String, String> serverIpList = new HashMap<String, String>();
		Map<String, String> appServerName = new HashMap<String, String>();
		Map<String, String> driveList = new HashMap<String, String>();
		ServerDetailModel serverDetailModel = new ServerDetailModel();

		String action = "save";

		try {
			serverTypeList = serverDetails.getServerTypeList();
			entityNameList = entityMastService.getEntityList();
			serverIpList = serverDetails.getServerIpList();
			appServerName = serverDetails.getAppServerNameList();
			driveList = serverDetails.getDrivelist();
			System.out.println("server_type_code==" + server_type_code);

			if (server_id != null) {
				action = "edit";
				serverDetailModel = serverDetails.getServerById(server_id);

			} else {
				serverlist = serverDetailsRepository.findAll();
				map.addAttribute("serverDetailModel", serverlist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("serverIpList................" + serverIpList);
		map.addAttribute("serverDetailModel", serverDetailModel);
		map.addAttribute("driveList", driveList);
		map.addAttribute("appServerName", appServerName);
		map.addAttribute("serverIpList", serverIpList);
		map.addAttribute("serverTypeList", serverTypeList);
		map.addAttribute("entityNameList", entityNameList);
		map.addAttribute("action", action);
		map.addAttribute("server_type_code_edit", server_type_code);

		return "pages/server/serverEntry";
	}

//Physical Server
	@RequestMapping("/physicalServerEntry")
	@ResponseBody
	public String physicalServerEntry(ServerDetailModel serverDetailModel, HttpSession session,
			@RequestParam(name = "server_id", required = false) String server_id) {
		String response = "error";
		System.out.println("here");
		try {

			if (strUtl.isNull(server_id)) { // save entry
				response = serverDetails.addPhysicalServerEntry(serverDetailModel);
				System.out.println("1");
			} else { // update
				response = serverDetails.updatePhysicalServerEntry(serverDetailModel, server_id);
				System.out.println("2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

// APP /Web /Db Server--> 
	@RequestMapping("/appServerDetailEntry")
	@ResponseBody
	public String appServerDetailEntry(ServerDetailModel serverDetail, HttpSession httpSession, Model map,
			@RequestParam(name = "server_id", required = false) String server_id) {
		String response = "error";

		System.out.println("server_id...>>" + server_id);
		ServerDetailModel serverDetailModel = new ServerDetailModel();
		String server_id2 = serverDetail.getServer_id();
		System.out.println("serverDetail>>>>>>>>>>" + serverDetail);
		String[] server_ip =null;
		try {
			if (!server_id2.isEmpty()) {// update

				serverDetailModel = serverDetails.getServerById(server_id2);
				response = serverDetails.addAppServerEntry(serverDetail, server_id2);

			} else {// save

				String serverIp = serverDetail.getServer_ip_config();
				if (serverIp.contains(",")) {
					serverIp = serverIp.replace(",", "").trim();
					System.out.println("serverIpppppp" + serverIp);
				}
				if(serverIp.contains("-")) {
					 server_ip = serverIp.split("-");
					 serverIp = server_ip[0];
				}
				String severID = serverDetails.getServerIDByServerIP(serverIp.trim());
				System.out.println("severID==" + severID);
//				serverDetailModel = serverDetails.getServerById(severID);

				response = serverDetails.addAppDetailEntry(serverDetail, severID);

				String max = serverDetailsRepository.getMaxCount();
				httpSession.setAttribute("serverId", max);
				map.addAttribute("server_id", max);

//				response = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("serverDetailModel", serverDetailModel);
		return response;
	}

	@RequestMapping(value = "/getAppServerListWithStatus", method = RequestMethod.GET)
	public @ResponseBody String getAppServerListWithStatus(@RequestParam("server_id") String server_id,
			@RequestParam("server_type_code") String server_type_code, Model map) {
		String appName = "";
		System.out.println("server_id>>" + server_id);
		System.out.println("server_type_code>>" + server_type_code);
		int tableCount = 0;
		StringBuilder sb = new StringBuilder();
		try {
//			List<ServerDetailModel> serverListByServerId = serverDetails.getServerListByServerId(server_id,server_type_code);
//			System.out.println("serverListByServerId=="+serverListByServerId);

//			String var= serverDetails.validateAppName(server_id);
			List<String> listOfid = new ArrayList<String>();

			List<ServerDetailModel> serverListByServerId = new ArrayList<>();
			listOfid = serverDetails.getListOfId1(server_id);
			String config_type = "";
			int num = 0;

//			String config_type =serverDetails.getConfigTypeById(var);
//			System.out.println("val.........."+var);
			if (server_type_code.equalsIgnoreCase("PSC")) {
				sb.append("<table id=\"appServerTable_" + tableCount + "\" class=\"table\">");
				sb.append("<thead>");
				sb.append("<tr>");
				sb.append("<th>Sr. No</th>");
				sb.append("<th>Server Type</th>");
				sb.append("<th>App Server</th>");
				sb.append("<th>Tag name</th>");
				sb.append("<th>Port</th>");
				sb.append("<th>Action</th>");
				sb.append("</tr>");
				sb.append("</thead>");
				sb.append("<tbody>");
				int count = 0;
//				while(! strUtl.isNull(var)) {
				if (listOfid.isEmpty()) {
					sb.append("<tr>");
					sb.append("<td colspan=\"6\" class=\"text-center\">No Record Found</td>");
					sb.append("</tr>");
				} else {

					for (int i = 0; i < listOfid.size(); i++) {

						config_type = serverDetails.getConfigTypeById(listOfid.get(i));
						System.out.println("config_type....." + config_type);
						serverListByServerId = serverDetails.getServerListByServerId(listOfid.get(i), config_type);
						System.out.println("serverListByServerId==========" + serverListByServerId);
						System.out.println("i....." + i);

						if (!strUtl.isNull(config_type)
								&& (config_type.equalsIgnoreCase("ASC") || config_type.equalsIgnoreCase("WEBC"))) {

							for (ServerDetailModel appServerObj : serverListByServerId) {
								System.out.println("ii......." + i);

								if (!strUtl.isNull(appServerObj.getApp_server_name())
										&& appServerObj.getApp_server_name().equalsIgnoreCase("JBS")) {
									appName = "JBOSS";
								} else if (!strUtl.isNull(appServerObj.getApp_server_name())
										&& appServerObj.getApp_server_name().equalsIgnoreCase("WBL")) {
									appName = "WEB LOGIC";
								} else if (!strUtl.isNull(appServerObj.getApp_server_name())
										&& appServerObj.getApp_server_name().equalsIgnoreCase("TMC")) {
									appName = "TOMCAT";
								}
								if (config_type.equalsIgnoreCase("ASC")) {
									config_type = "App Server";
								} else if (config_type.equalsIgnoreCase("WEBC")) {
									config_type = "Web Server";
								} else if (config_type.equalsIgnoreCase("DBC")) {
									config_type = "Database Server";
								}

								count++;
								sb.append("<tr>");
								sb.append("<td class=\"text-center\">" + count + "</td>");
								sb.append("<td >" + config_type + "</td>");
								sb.append("<td class=\"server-name\">" + appName + "</td>");
								sb.append("<td id=\"tag_name" + count + "\">" + appServerObj.getApp_server_tag_name()
										+ "</td>");
								sb.append("<td id=\"appServerPort_" + count + "\">" + appServerObj.getApp_server_port()
										+ "</td>");
								sb.append("<input type=\"hidden\" id=\"type_code\" value=\""
										+ appServerObj.getServer_type_code() + "\">");
//							sb.append("<td class=\"app-names\">" + appServerObj.getApp_name() + "</td>");
								sb.append("<td <span th:text=\"${'' + item.tag_name}\"\r\n"
										+ "											style=\"font-weight: bold;\" title=\"Tag Name\"></span> <span title=\"Edit\"\r\n"
										+ "											onclick=\"editAppServerDetails('"
										+ appServerObj.getServer_id() + "');\">\r\n"
										+ "											<i class=\"fa fa-pencil edit-button \"\r\n"
										+ "											style=\"background-color: #14de9b; color: #fff; padding: 2px 2px; border-radius: 5px\"></i>\r\n"
										+ "										</span> <span\r\n"
										+ "											onclick=\"deleteAppServerDetails('"
										+ appServerObj.getServer_id() + "');\">\r\n"
										+ "											<i class=\"fa fa-trash delete-button\"\r\n"
										+ "											style=\"background-color: #ef4009; color: #fff; padding: 2px 2px; border-radius: 5px\"\r\n"
										+ "											title=\"Delete\"></i>\r\n"
										+ "										</span></td>");
								sb.append("</tr>");
							}
//						
						} else if (!strUtl.isNull(config_type) && config_type.equalsIgnoreCase("DBC")) {
							for (ServerDetailModel appServerObj : serverListByServerId) {
								System.out.println("dbc");

								count++;
								sb.append("<tr>");
								sb.append("<td class=\"text-center\">" + count + "</td>");
								sb.append("<td >Database Server</td>");
								sb.append("<td class=\"server-name\"> </td>");
								sb.append("<td id=\"tag_name" + count + "\">" + appServerObj.getTag_name() + "</td>");
								sb.append("<td id=\"appServerPort_" + count + "\"></td>");
								sb.append("<input type=\"hidden\" id=\"type_code\" value=\""
										+ appServerObj.getServer_type_code() + "\">");
//							sb.append("<td class=\"app-names\">" + appServerObj.getApp_name() + "</td>");
								sb.append("<td <span th:text=\"${'' + item.tag_name}\"\r\n"
										+ "											style=\"font-weight: bold;\" title=\"Tag Name\"></span> <span title=\"Edit\"\r\n"
										+ "											onclick=\"editAppServerDetails('"
										+ appServerObj.getServer_id() + "');\">\r\n"
										+ "											<i class=\"fa fa-pencil edit-button \"\r\n"
										+ "											style=\"background-color: #14de9b; color: #fff; padding: 2px 2px; border-radius: 5px\"></i>\r\n"
										+ "										</span> <span\r\n"
										+ "											onclick=\"deleteAppServerDetails('"
										+ appServerObj.getServer_id() + "');\">\r\n"
										+ "											<i class=\"fa fa-trash delete-button\"\r\n"
										+ "											style=\"background-color: #ef4009; color: #fff; padding: 2px 2px; border-radius: 5px\"\r\n"
										+ "											title=\"Delete\"></i>\r\n"
										+ "										</span></td>");
								sb.append("</tr>");
							}
						}
					}
				}
//				}
				sb.append(" </tbody>");
				sb.append("</table>");
			} else if (serverListByServerId != null
					&& (server_type_code.equalsIgnoreCase("ASC") || server_type_code.equalsIgnoreCase("WEBC"))) {
				
				 serverListByServerId = serverDetails.getServerListByServerId(server_id,server_type_code);
				
				sb.append("<table id=\"appServerTable_" + tableCount + "\" class=\"table\">");
				sb.append("<thead>");
				sb.append("<tr>");
				sb.append("<th>Sr. No</th>");
				if (server_type_code.equalsIgnoreCase("ASC")) {
					sb.append("<th>App Server</th>");
				} else {
					sb.append("<th>Web Server</th>");
				}
				sb.append("<th>Tag name</th>");
				sb.append("<th>Port</th>");
				sb.append("<th>Action</th>");
				sb.append("</tr>");
				sb.append("</thead>");
				sb.append("<tbody>");
				int count = 0;

				for (ServerDetailModel appServerObj : serverListByServerId) {

					if (appServerObj.getApp_server_name().equalsIgnoreCase("JBS")) {
						appName = "JBOSS";
					} else if (appServerObj.getApp_server_name().equalsIgnoreCase("WBL")) {
						appName = "WEB LOGIC";
					} else if (appServerObj.getApp_server_name().equalsIgnoreCase("TMC")) {
						appName = "TOMCAT";
					}

					count++;
					sb.append("<tr>");
					sb.append("<td class=\"text-center\">" + count + "</td>");
					sb.append("<td class=\"server-name\">" + appName + "</td>");
					sb.append("<td id=\"tag_name" + count + "\">" + appServerObj.getApp_server_tag_name() + "</td>");
					sb.append("<td id=\"appServerPort_" + count + "\">" + appServerObj.getApp_server_port() + "</td>");
					sb.append("<input type=\"hidden\" id=\"type_code\" value=\"" + appServerObj.getServer_type_code()
							+ "\">");
//					sb.append("<td class=\"app-names\">" + appServerObj.getApp_name() + "</td>");
					sb.append("<td <span th:text=\"${'' + item.tag_name}\"\r\n"
							+ "											style=\"font-weight: bold;\" title=\"Tag Name\"></span> <span title=\"Edit\"\r\n"
							+ "											onclick=\"editAppServerDetails('"
							+ appServerObj.getServer_id() + "');\">\r\n"
							+ "											<i class=\"fa fa-pencil edit-button \"\r\n"
							+ "											style=\"background-color: #14de9b; color: #fff; padding: 2px 2px; border-radius: 5px\"></i>\r\n"
							+ "										</span> <span\r\n"
							+ "											onclick=\"deleteAppServerDetails('"
							+ appServerObj.getServer_id() + "');\">\r\n"
							+ "											<i class=\"fa fa-trash delete-button\"\r\n"
							+ "											style=\"background-color: #ef4009; color: #fff; padding: 2px 2px; border-radius: 5px\"\r\n"
							+ "											title=\"Delete\"></i>\r\n"
							+ "										</span></td>");
					sb.append("</tr>");
				}
				sb.append(" </tbody>");
				sb.append("</table>");
			}

			else if (serverListByServerId != null && server_type_code.equalsIgnoreCase("DBC")) {
				
				 serverListByServerId = serverDetails.getServerListByServerId(server_id,server_type_code);
				
				sb.append("<table id=\"appServerTable_" + tableCount + "\" class=\"table\">");
				sb.append("<thead>");
				sb.append("<tr>");
				sb.append("<th>Sr. No</th>");
				sb.append("<th>Installed Database</th>");
				sb.append("<th>Installed Database Tool</th>");
				sb.append("</tr>");
				sb.append("</thead>");
				sb.append("<tbody>");
				int count = 0;
				for (ServerDetailModel appServerObj : serverListByServerId) {
					count++;
					sb.append("<tr>");
					sb.append("<td  class=\"text-center\">" + count + "</td>");
					sb.append("<td class=\"Installed-Database\">" + appServerObj.getInstalled_db() + "</td>");
					sb.append("<td id=\"Installed-Database-tool" + count + "\">" + appServerObj.getInstalled_db_tool()
							+ "</td>");
					sb.append("</tr>");
				}
				sb.append(" </tbody>");
				sb.append("</table>");
			}
			map.addAttribute("var_count", listOfid.get(0));
		} catch (Exception e) {
//            	log.info("JAXBException:GetAppServerListWithStatus----->"+e);
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String encodeURIComponent(String s) {
		String result;
		try {
			result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			result = s;
		}
		return result;
	}

	@RequestMapping("/serverDetailDashboard")
	public String serverDetailDashboard(HttpSession session, ModelMap map, HttpServletRequest request,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,
			@RequestParam(name = "server_type", required = false) String server_type ,FilterDTO filter) {
		System.out.println("server_type=" + server_type );
//		String server_type1 = server_type;
		List<ServerDetailModel> serverlist = new ArrayList<ServerDetailModel>();
		Map<String, String> entityList = new HashMap<>();
		String recPerPage = "";
		session.setAttribute("ACTIVE_TAB", "MENU-017");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-021");
		long total = 0l;
		try {
//			if(strUtl.isNull(server_type1)) {
//				server_type1="ALL";
//			}
			dataGridDTO.setFilter(filter);
			if (filter == null) {
				System.out.println("Filter Is Null");
				filter = new FilterDTO();
			}
			
			//total = serverDetails.getUserCount(filter,server_type);

			entityList = entityMastService.getEntityList();
			if (server_type.equalsIgnoreCase("PSC")) {
				
				total = serverDetails.getUserCount(filter,server_type);

				
				serverlist = serverDetails.getServerList();
				System.out.println("serverlist>>>>>" + serverlist);
				System.out.println("total....11.." + total);
				if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
					recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
				} else {
					recPerPage = "10";
				}
				PaginatorManipulation manipulation = new PaginatorManipulation();
				session.setAttribute("recPerPage", recPerPage);

				session.setAttribute("server_type", server_type);
				PaginatorDTO paginator = manipulation.getPaginatorCount("serverDetailGridData", total, recPerPage);

				dataGridDTO.setPaginator(paginator);
				map.addAttribute("dataGridDTO1", dataGridDTO);
				System.out.println("dataGridDTO.." + dataGridDTO.getPaginator().getTotalRecords());

			} else if (server_type.equalsIgnoreCase("ASC") || server_type.equalsIgnoreCase("DBC")
					|| server_type.equalsIgnoreCase("WEBC")) {
				serverlist = serverDetails.getServerListByServerType(server_type);
				System.out.println("serverlist==>>" + serverlist);

				total = serverDetails.getUserCount(filter,server_type);
				System.out.println("total>>" + total);
				if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
					recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
				} else {
					recPerPage = "10";
				}
				PaginatorManipulation manipulation = new PaginatorManipulation();

				session.setAttribute("recPerPage", recPerPage);
				session.setAttribute("server_type", server_type);
				PaginatorDTO paginator = manipulation.getPaginatorCount("serverDetailGridData", total, recPerPage);

				paginator.setTotalRecords(total);
				dataGridDTO.setPaginator(paginator);
				System.out.println("dataGridDTO>>>>" + dataGridDTO);
//				dataGridDTO.setPaginator(paginator.setTotalRecords(total));
				map.addAttribute("dataGridDTO", dataGridDTO);
				//map.addAttribute("dataGridDTO1", dataGridDTO);
				map.addAttribute("serverGrid", serverlist);

			}
			map.addAttribute("entityList", entityList);
			map.addAttribute("server_type_code_edit", server_type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("serverGrid", serverlist);
		map.addAttribute("total", total);
		return "pages/server/serverDetail";
	}

	@RequestMapping(value = "/serverDetailGridData", method = RequestMethod.POST)
	public String serverDetailGridData(Model map, HttpServletRequest request, HttpSession session,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO , FilterDTO filter) {
		String recPerPage = "";
		Long total = 0l;
		dataGridDTO.setFilter(filter);
		try {
			System.out.println("dataGridDTO....>>" + dataGridDTO.getPaginator().getTotalRecords());
			recPerPage = (String) session.getAttribute("recPerPage");
			String server_type = (String) session.getAttribute("server_type");
			System.out.println("server_type>?>" + server_type);
			total = serverDetails.getUserCount(filter ,server_type);

			if (server_type.equalsIgnoreCase("PSC")) {
				
				List<ServerDetailModel> serverGrid = serverDetails.getServerBrowseList(filter,server_type, session,
						dataGridDTO, recPerPage);
				System.out.println("serverGridserverGridserverGridserverGrid------------------->"+serverGrid);

				total = serverDetails.getUserCount(filter ,server_type);
				System.out.println("total....1234." + total);
				if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
					recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
				} else {
					recPerPage = "10";
				}
				PaginatorManipulation manipulation = new PaginatorManipulation();
				session.setAttribute("recPerPage", recPerPage);

				session.setAttribute("server_type", server_type);
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);

				dataGridDTO.setPaginator(paginator);
				map.addAttribute("dataGridDTO", dataGridDTO);

				System.out.println("dataGridDTO?>>?>???>>>>" + dataGridDTO.getPaginator().getTotalRecords());
//				long slnoStartFrom = 0l;
//				if (dataGridDTO != null) {
//					if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
//						slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
//					}
//				}
				map.addAttribute("serverGrid", serverGrid);
				map.addAttribute("server_type_code_edit", server_type);
			} else if (server_type.equalsIgnoreCase("ASC") || server_type.equalsIgnoreCase("DBC")
					|| server_type.equalsIgnoreCase("WEBC")) {
				List<ServerDetailModel> serverGrid = serverDetails.getServerBrowseList1(session, dataGridDTO,
						recPerPage, server_type);
				total = serverDetails.getUserCount( filter,server_type);
				System.out.println("total....1234." + total);
				if (dataGridDTO != null && dataGridDTO.getPaginator() != null) {
					recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
				} else {
					recPerPage = "10";
				}
				PaginatorManipulation manipulation = new PaginatorManipulation();
				session.setAttribute("recPerPage", recPerPage);

				session.setAttribute("server_type", server_type);
				PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);

				dataGridDTO.setPaginator(paginator);
//				long slnoStartFrom = 0l;
//				if (dataGridDTO != null) {
//					if (dataGridDTO.getPaginator().getPageNumber() > 1l) {
//						slnoStartFrom = slnoStartFrom * (dataGridDTO.getPaginator().getPageNumber() - 1);
//					}
//				}
				map.addAttribute("server_type_code_edit", server_type);
				map.addAttribute("serverGrid", serverGrid);
				map.addAttribute("dataGridDTO", dataGridDTO);
			} else {
System.out.println("......");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "pages/server/serverDetail :: ajaxServerList";
	}

	@RequestMapping("/deleteServer")
	@ResponseBody
	public String deleteServer(@RequestParam(name = "server_id") String server_id,
			@RequestParam(name = "server_type_code", required = false) String server_type_code) {
		String response = "error";
		try {
			if (server_type_code.equalsIgnoreCase("null")) {

				response = serverDetails.deleteServerById(server_id, server_type_code);

			} else {
				response = serverDetails.deleteServerById(server_id, server_type_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/deleteServerTemp")
	@ResponseBody
	public String deleteServerTemp(@RequestParam(name = "server_id", required = false) String server_id,
			@RequestParam(name = "server_type_code", required = false) String server_type_code) {
		String response = "";
		try {
			if (server_type_code.equalsIgnoreCase("null")) {

				response = serverDetails.getDeleteCount(server_id);

			} else {
				response = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/deleteAppServer")
	@ResponseBody
	public String deleteAppServer(@RequestParam(name = "server_id") String server_id,
			@RequestParam(name = "server_type_code", required = false) String server_type_code) {
		String response = "error";
		try {
			if (server_type_code.equalsIgnoreCase("null")) {

				response = serverDetails.deleteServerById(server_id, server_type_code);// Physical server

			} else {
				response = serverDetails.deleteServerById(server_id, server_type_code);// app server
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/deleteAppServerTemp")
	@ResponseBody
	public String deleteAppServerTemp(@RequestParam(name = "server_id", required = false) String server_id,
			@RequestParam(name = "server_type_code", required = false) String server_type_code) {
		String response = "";
		try {
			if (server_type_code.equalsIgnoreCase("null")) {
				response = "0";
			} else {

				response = serverDetails.getDeleteAppCount(server_id);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/editServerDetails")
	public String editServerDetails(HttpSession session, ModelMap map,
			@RequestParam(name = "server_id", required = false) String server_id) {

		List<ServerDetailModel> serverlist = new ArrayList<ServerDetailModel>();
		Map<String, String> serverTypeList = new HashMap<String, String>();
		Map<String, String> entityNameList = new HashMap<String, String>();

		ServerDetailModel serverDetailModel = new ServerDetailModel();
		try {

			serverlist = serverDetails.getServerList();
			serverTypeList = serverDetails.getServerTypeList();
//			entityNameList = entityMastService.getEntityNameList();

			if (server_id != null) {
				serverDetailModel = serverDetails.getServerById(server_id);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("serverlist", serverlist);
		map.addAttribute("serverTypeList", serverTypeList);
		map.addAttribute("entityNameList", entityNameList);

		map.addAttribute("serverDetailModel", serverDetailModel);
//		modelmap.addAttribute("entityList", entityList);
//		modelmap.addAttribute("action",action);

		return "pages/server/serverEntry";

	}

	@RequestMapping(value = "/getEntityList")
	@ResponseBody
	public Map<String, String> getEntityList(@RequestParam("server_owner") String server_owner) {
		Map<String, String> list = new HashMap<>();
		try {

			list = entityMastService.getEntityNameList(server_owner);

			System.out.println("list><>>" + list);

			list = entityMastService.getEntityNameList(server_owner);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@RequestMapping(value = "validateServerName")
	@ResponseBody
	public String validateServerName(
			@RequestParam(name = "configuration_type", required = false) String configuration_type,
			@RequestParam(name = "app_server_ip", required = false) String app_server_ip,
			@RequestParam(name = "app_server_name", required = false) String app_server_name) {
		String response = "";

		try {
			response = serverDetails.validateServerName(configuration_type, app_server_ip, app_server_name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@RequestMapping(value = "setPublicIp")
	@ResponseBody
	public String setPublicIp(@RequestParam(name = "app_server_ip", required = false) String app_server_ip) {
		String response = "";

		try {
			response = serverDetails.setPublicIp(app_server_ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	// openViewBox Server Details
	@RequestMapping("/viewServerDetails")
	@ResponseBody
	public String viewServerDetails(@RequestParam(name = "server_id") String server_id, HttpSession session) {
		String response = "error";

		try {
			if (server_id != null) {

				response = serverDetails.viewServerDetails(server_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("response.." + response);
		return response;
	}

	@RequestMapping("/checkServerIp")
	@ResponseBody
	public String checkServerIp(@RequestParam(name = "server_id") String server_id, HttpSession session) {
		String response = "error";
		try {
			if (server_id != null) {

				response = serverDetails.checkServerIp(server_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("response.." + response);
		return response;
	}

	
	@RequestMapping(value = "serverDetailFilter", method = RequestMethod.POST)
	public String serverDetailFilter(Model map,  @Param(value = "configuration_type") String configuration_type,
			@Param(value = "server_owner_name") String server_owner_name,
			@Param(value = "server_ip") String server_ip,
			@Param(value = "tag_name") String tag_name, 
			@Param(value = "entity_code") String entity_code,
			HttpSession session ,FilterDTO filter) {

		 System.out.println("configuration_type.............."+configuration_type);
//		List<ServerDetailModel> appGrid = new ArrayList<ServerDetailModel>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		
		List<ServerDetailModel> serverGrid = new ArrayList<ServerDetailModel>();
		Map<String, String> entityList = new HashMap<>();

		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = 0;
//		int total = serverDetails.getcountTable(entity_code, application_type, project_code, search_entity);
		 total = serverDetails.getFilterCount(configuration_type,server_owner_name, server_ip, tag_name, entity_code);
		 System.out.println("total===="+total);
		PaginatorManipulation manipulation = new PaginatorManipulation();
		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);
		session.setAttribute("recPerPage", recPerPage);
		session.setAttribute("server_type", configuration_type);

		try {
			entityList = entityMastService.getEntityList();
			
			serverGrid = serverDetails.getServerFilterData(configuration_type,server_owner_name, server_ip, tag_name, entity_code );
//			appGrid = serverDetails.getAppDataListFilterTable(entity_code, application_type, project_code,search_entity);
			System.out.println("serverGrid1...................>>>>>>" + serverGrid);

			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		map.addAttribute("serverGrid", serverGrid);
		map.addAttribute("entityList", entityList);
		
		//System.out.println("dataGridDTO....>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );

		return "pages/server/serverDetail :: ajaxServerList";
	}
	
	
	
	
	///////////////////////////////////////////// sushma

	@RequestMapping(value = "/setAppServerIP")
	@ResponseBody
	public Map<String, String> setAppServerIP(@RequestParam("server_ip") String server_ip, HttpSession session) {
		Map<String, String> AppServerlist = new HashMap<>();

		System.out.println("app_servername......" + server_ip);
		try {

			AppServerlist = serverDetails.setAppServerIP(server_ip);

			System.out.println("list of App ..." + AppServerlist);

			Collection<String> values = AppServerlist.values();
			session.setAttribute("AppServerlist", AppServerlist);
			session.setAttribute("server_ip", server_ip);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return AppServerlist;
	}

	@RequestMapping(value = "/setAppName")
	@ResponseBody
	public Map<String, String> setAppName(@RequestParam("app_servername") String app_servername ,HttpSession session) {
		Map<String, String> Applist = new HashMap<>();

		System.out.println("app_servername......" +app_servername );
		try {

			Applist = serverDetails.getAppNameList(app_servername);

			System.out.println("list of App ..." + Applist);

			Collection<String> values = Applist.values();
			session.setAttribute("app_servername", app_servername);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Applist;
	}

	@RequestMapping("/appDetailEntry")
	public String appDetailEntry(Model map, HttpSession session,
			@RequestParam(name = "app_code", required = false) Integer app_code,
			@RequestParam(name = "View", required = false) String View) {
		session.setAttribute("ACTIVE_TAB", "MENU-017");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-040");
		String action = "save";
		Map<String, String> entityList = new HashMap<>();
		Map<String, String> applicationList = new HashMap<>();
		Map<String, String> serverIpList = new HashMap<String, String>();
		Map<String, String> appServerName = new HashMap<String, String>();
		Map<String, String> appServerIP = new HashMap<String, String>();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> appServerport = new HashMap<String, String>();

		String url1 = null;
		String url2 = null;
		String local = null;
		String Public = null;
		String Port = null;
		String serverip = null;
		String app_server_ip = null;
		AppDetails AppEntity = new AppDetails();
		try {

			serverip = (String) session.getAttribute("server_ip");
              System.out.println("serverip..........."+serverip); 
			entityList = entityMastService.getEntityList();
			applicationList = projectService.getAllApplicationCodeName();
			serverIpList = serverDetails.getServerIpList1();
			
			app_server_ip=AppEntity.getApp_server_ip();
			appServerIP = serverDetails.getAppServerIpList1();

			appServerName = serverDetails.getAppServerNameList();
			projectList = projectService.getProjectList1();

			appServerport = projectService.getAppserverPort(serverip);
			for (Entry<String, String> entry : appServerport.entrySet()) {
				Port = entry.getValue();
			}

			if (app_code != null) {
				action = "edit";
				System.out.println("app_code...." + app_code);

				AppEntity = serverDetails.getAppEditList(app_code);
				appServerIP = serverDetails.getAppServerIpList(serverip);

			System.out.println("AppEntity..............."+AppEntity);

				if (AppEntity.getLocal_app_url() != null) {
					local = AppEntity.getLocal_app_url();
					String result[] = local.split("//");
					url1 = result[result.length - 1];
					System.out.println("url1......" + url1);

				}

				if (AppEntity.getPublic_app_url() != null) {
					Public = AppEntity.getPublic_app_url();
					String result[] = Public.split("//");
					url2 = result[result.length - 1];
					System.out.println("url2......" + url2);
				}
//				
				app_server_ip = AppEntity.getApp_server_ip();

			}

			// System.out.println("app_server_ip....."+app_server_ip);
			// System.out.println("Port....."+Port);

			session.setAttribute("app_code", app_code);

		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		map.addAttribute("AppEntity", AppEntity);
		map.addAttribute("action", action);
		map.addAttribute("entityList", entityList);
		map.addAttribute("applicationList", applicationList);
		map.addAttribute("serverIpList", serverIpList);
		map.addAttribute("appServerName", appServerName);
		map.addAttribute("appServerIP", appServerIP);
		map.addAttribute("projectList", projectList);
		map.addAttribute("View", View);
		map.addAttribute("url1", url1);
		map.addAttribute("url2", url2);
		map.addAttribute("app_server_ip", app_server_ip);
		map.addAttribute("Port", Port);

		return "pages/server/appDetailEntry";
	}

	@RequestMapping("/appDetailDashboard")
	public String appDetailDashboard(HttpSession session, ModelMap map, HttpServletRequest request, FilterDTO filter,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO,
			@RequestParam(name = "mode", required = false) String mode) {

		session.setAttribute("ACTIVE_TAB", "MENU-017");
		session.setAttribute("ACTIVE_SUB_TAB", "MENU-039");
		Map<String, String> entityNameList = new HashMap<String, String>();
		List<AppDetails> applist = new ArrayList<AppDetails>();
		Map<String, String> applicationList = new HashMap<>();
		Map<String, String> serverIpList = new HashMap<String, String>();
		Map<String, String> appServerIP = new HashMap<String, String>();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> appServerName = new HashMap<String, String>();

		if (strUtl.isNull(mode)) {
			mode = "Text";
		}

		dataGridDTO.setFilter(filter);

		try {

			if (filter == null) {
				System.out.println("Filter Is Null");
				filter = new FilterDTO();
			}

			long total = 10l;
			total = serverDetails.getAppCount(filter);
			// System.out.println("total....."+total);

			entityNameList = entityMastService.getEntityNameList();
			applicationList = projectService.getAllApplicationCodeName();
			serverIpList = serverDetails.getServerIpList();
			//appServerIP = serverDetails.getAppServerIpList();
			projectList = projectService.getProjectList1();
			appServerName = serverDetails.getAppServerNameList();

			applist = serverDetails.getAppList();

			System.out.println("applist....." + applist);

			String recPerPage = "";
			if (dataGridDTO != null && dataGridDTO.getPaginator() != null
					&& !strUtl.isNull(dataGridDTO.getPaginator().getRecordsPerPage())) {
				recPerPage = dataGridDTO.getPaginator().getRecordsPerPage();
			} else {
				recPerPage = "10";
			}

			PaginatorManipulation manipulation = new PaginatorManipulation();
			PaginatorDTO paginator = manipulation.getPaginatorCountDoc("appDetailDashboardGrid", total, recPerPage);

			dataGridDTO.setPaginator(paginator);
			session.setAttribute("mode", mode);

		} catch (Exception e) {
			e.printStackTrace();
		}
		map.addAttribute("applicationList", applicationList);
		map.addAttribute("entityNameList", entityNameList);
		map.addAttribute("dataGridDTO", dataGridDTO);
		map.addAttribute("mode", mode);
		map.addAttribute("applist", applist);
		map.addAttribute("serverIpList", serverIpList);
		map.addAttribute("appServerIP", appServerIP);
		map.addAttribute("projectList", projectList);
		map.addAttribute("appServerName", appServerName);

		return "pages/server/appDetailDashboard";
	}

	@RequestMapping(value = "/appDetailDashboardGrid", method = RequestMethod.POST)
	public String appDetailDashboardGrid(HttpSession session, ModelMap modelmap,
			@ModelAttribute("dataGridDTO") DataGridDTO dataGridDTO, FilterDTO filter) {

		dataGridDTO.setFilter(filter);

		Map<String, String> entityNameList = new HashMap<String, String>();
		List<AppDetails> applist = new ArrayList<AppDetails>();
		List<AppDetails> appGrid = new ArrayList<AppDetails>();
		Map<String, String> applicationList = new HashMap<>();
		Map<String, String> serverIpList = new HashMap<String, String>();
		Map<String, String> appServerIP = new HashMap<String, String>();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> appServerName = new HashMap<String, String>();

		String recPerPage = "";
		long total = 0l;
		String mode = "";

		try {
			// total = serverDetails.getAppCount(filter);
			applicationList = projectService.getAllApplicationCodeName();
			entityNameList = entityMastService.getEntityNameList();
			serverIpList = serverDetails.getServerIpList();
			//appServerIP = serverDetails.getAppServerIpList();
			projectList = projectService.getProjectList1();
			appServerName = serverDetails.getAppServerNameList();

			applist = serverDetails.getAppList();
			System.out.println("projectList...." + projectList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			dataGridDTO.setFilter(filter);
			mode = (String) session.getAttribute("mode");

			long pagenumber = dataGridDTO.getPaginator().getPageNumber();
			recPerPage = (String) session.getAttribute("recPerPage");

			appGrid = serverDetails.getAppDetailsList(filter, dataGridDTO, recPerPage, total);
			System.out.println("appGrid....." + appGrid);

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
			modelmap.addAttribute("dataGridDTO", dataGridDTO);
			modelmap.addAttribute("mode", mode);
			modelmap.addAttribute("slnoStartFrom", slnoStartFrom);
			modelmap.addAttribute("appGrid", appGrid);
			modelmap.addAttribute("applicationList", applicationList);
			modelmap.addAttribute("entityNameList", entityNameList);
			modelmap.addAttribute("serverIpList", serverIpList);
			modelmap.addAttribute("appServerIP", appServerIP);
			modelmap.addAttribute("projectList", projectList);
			modelmap.addAttribute("appServerName", appServerName);

		} catch (Exception e) {
			// TODO: handle exceptio
			e.printStackTrace();
		}

		if (mode.equalsIgnoreCase("Text")) {
			return "pages/server/appDetailDashboardGrid ::ajaxAppList";
		} else {
			return "pages/server/appDetailDashboardGrid:: ajaxAppListCard";
		}

	}

	@RequestMapping("/saveAppDetail")
	@ResponseBody
	public String saveAppDetail(AppDetails entity, HttpSession httpsession) {

		
		System.out.println("entity............."+entity);
		
		entity.setUrl1(entity.getIp1());
		String serverip1 = entity.getServer_ip();
		String appserverip1 = entity.getApp_server_ip();
		String appserverip=null;
		String serverip=null;
		if(serverip1 != null) {
			  String result[] = serverip1.split("-");
			  serverip =result[0].trim();
			  System.out.println("serverip............."+serverip);
			// entity.setIp1(appserverip1);
		}
		
		String serverid = serverDetails.getServerIDByServerIPandAppServerIP(serverip, appserverip);
		System.out.println("serverid......" + serverid);
		String response = "error";
		try {
			if (entity != null) {

               	entity.setServer_id(serverid);
				entity.setLastupdate(new Date());
				entity.setFlag("A");
				entity.setApp_server_ip(appserverip1);
				entity.setLocal_app_url(entity.getProtocol1() + entity.getUrl1());
				entity.setPublic_app_url(entity.getProtocol2() + entity.getPublic_app_url());
				AppDetails savedDoc = serverDetails.saveAppDetail(entity);
				response = "success";

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return response;

	}

	@RequestMapping("/UpdateAppDetail")
	@ResponseBody
	public String UpdateAppDetail(AppDetails entity, Model map, HttpSession httpSession) {

		String response = "error";
		Integer app_code = (Integer) httpSession.getAttribute("app_code");
		System.out.println("update app_code....." + app_code);

		
		entity.setUrl1(entity.getIp1());

		try {
			entity.setLocal_app_url(entity.getProtocol1() + entity.getUrl1());
			entity.setPublic_app_url(entity.getProtocol2() + entity.getPublic_app_url());
			entity.setApp_code(app_code);
			entity.setLastupdate(new Date());
			entity.setFlag("A");
			AppDetails savedDoc = serverDetails.saveAppDetail(entity);
			response = "success";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping("/AppViewDetails")
	@ResponseBody
	public String AppViewDetails(ModelMap map, @RequestParam("app_code") Integer app_code) {
		String response = "error";
		System.out.println("app_code..." + app_code);
		try {
			if (app_code != null) {
				response = serverDetails.viewAppDetails(app_code);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;

	}

	@RequestMapping(value = "appTextFilter", method = RequestMethod.POST)
	public String appTextFilter(Model map, @Param(value = "entity_code") String entity_code,
			@Param(value = "application_type") String application_type,
			@Param(value = "search_entity") String search_entity, @Param(value = "project_code") String project_code,
			@Param(value = "mode") String mode, HttpSession session) {

		// System.out.println("mode.............."+mode);
		List<AppDetails> appGrid = new ArrayList<AppDetails>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> applicationList = new HashMap<>();
		Map<String, String> entityNameList = new HashMap<String, String>();

		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = serverDetails.getcountTable(entity_code, application_type, project_code, search_entity);
		// System.out.println("total===="+total);
		PaginatorManipulation manipulation = new PaginatorManipulation();
		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);

		try {
			projectList = projectService.getProjectList1();
			applicationList = projectService.getAllApplicationCodeName();
			entityNameList = entityMastService.getEntityNameList();

			appGrid = serverDetails.getAppDataListFilterTable(entity_code, application_type, project_code,
					search_entity);
			System.out.println("appGrid.." + appGrid);

			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}

		map.addAttribute("appGrid", appGrid);
		map.addAttribute("projectList", projectList);
		map.addAttribute("applicationList", applicationList);
		map.addAttribute("entityNameList", entityNameList);

		return "pages/server/appDetailDashboardGrid:: ajaxAppListCard";
	}

	@RequestMapping(value = "appCardFilter", method = RequestMethod.POST)
	public String appCardFilter(Model map, @Param(value = "entity_code") String entity_code,
			@Param(value = "application_type") String application_type,
			@Param(value = "search_entity") String search_entity, @Param(value = "project_code") String project_code,
			@Param(value = "mode") String mode, HttpSession session) {

		// System.out.println("mode.............."+mode);
		List<AppDetails> appGrid = new ArrayList<AppDetails>();
		DataGridDTO dataGridDTO = new DataGridDTO();
		Map<String, String> projectList = new HashMap<>();
		Map<String, String> entityNameList = new HashMap<String, String>();
		Map<String, String> applicationList = new HashMap<>();

		String recPerPage = (String) session.getAttribute("recPerPage");
		int total = serverDetails.getcountTable(entity_code, application_type, project_code, search_entity);
		// System.out.println("total===="+total);
		PaginatorManipulation manipulation = new PaginatorManipulation();
		PaginatorDTO paginator = manipulation.getPaginatorCount("", total, recPerPage);
		dataGridDTO.setPaginator(paginator);

		try {
			projectList = projectService.getProjectList1();
			entityNameList = entityMastService.getEntityNameList();
			applicationList = projectService.getAllApplicationCodeName();
			appGrid = serverDetails.getAppDataListFilterTable(entity_code, application_type, project_code,
					search_entity);

			System.out.println("appGrid.." + appGrid);
			map.addAttribute("dataGridDTO", dataGridDTO);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}

		map.addAttribute("appGrid", appGrid);
		map.addAttribute("projectList", projectList);
		map.addAttribute("entityNameList", entityNameList);
		map.addAttribute("applicationList", applicationList);

		return "pages/server/appDetailDashboardGrid:: ajaxAppListCard";
	}

	@RequestMapping(value = "/deleteAppDocument")
	public @ResponseBody String deleteAppDocument(@RequestParam(name = "app_code") Integer app_code) {
		String response = "error";
		try {
			if (app_code != null) {
				response = serverDetails.deleteAppDocument(app_code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}


}
