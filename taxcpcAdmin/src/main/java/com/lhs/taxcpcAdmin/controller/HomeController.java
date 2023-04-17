/**
 * 
 */
package com.lhs.taxcpcAdmin.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.UserMastRepository;
import com.lhs.taxcpcAdmin.dao.UserMenuMastRepository;
import com.lhs.taxcpcAdmin.model.Assessment;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserMenuMast;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;
import com.lhs.taxcpcAdmin.service.LhssysParametersService;
import com.lhs.taxcpcAdmin.service.LoginService;
import com.lhs.taxcpcAdmin.service.UserLoginTranService;
import com.lhs.taxcpcAdmin.service.UserMastService;
import com.lhs.taxcpcAdmin.service.UserMenuMastService;
import com.lhs.taxcpcAdmin.service.UserRoleMastService;
import com.lhs.taxcpcAdmin.utilities.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gaurav.khanzode
 *
 */
@Controller
//@RequestMapping("/")
@Slf4j
public class HomeController {

	final String ACCESS_DENIED_MSG = "Denying access for the user. Please check the pre-login configuration to get login to the application.";

//	@Autowired
//	@Qualifier("dataSourceFilePath")
//	private String dataSourceFilePath;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	private DateTimeUtil dateTimeUtl;

	@Autowired
	private UserMastService userMastService;

	@Autowired
	private UserLoginTranService clientLoginService;

	@Autowired
	private LhssysParametersService lsysParametersService;
	
	@Autowired
	private UserMastRepository userRepo;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	UserMenuMastService menuService;
	
	@Autowired
	UserRoleMastService roleService;

	@Autowired
	BuildProperties buildProperties;
	
	@Autowired
	private UserMenuMastRepository userMenuMastRepo;
	
//	@GetMapping
//	public String preLoginCheck(ModelMap model) {
//		String returnView = "redirect:/login";
//
//		Optional<Path> optPath = Optional.ofNullable(Paths.get(dataSourceFilePath));
//		if (optPath.isPresent()) {
//			Path parentPath = optPath.get().getParent();
//
//			try {
//				log.info("Checking if site is under maintenance");
//
//				Path preLoginConfigFilePath = Paths.get(parentPath + File.separator +
//						"TDI_PRE_LOGIN_CONFIG" + File.separator + "TAXCPC_UM_CONFIG" + File.separator + "site_under_maintenance.txt");
//
//				if (Files.exists(preLoginConfigFilePath)) {
//					try (InputStream configStream = Files.newInputStream(preLoginConfigFilePath);) {
//
//						Properties preLoginConfigProps = new Properties();
//						preLoginConfigProps.load(configStream);
//
//						Object feature = preLoginConfigProps.get("feature");
//						String whetherUnderMtnc = (feature != null) ? feature.toString() : "";
//
//						if (!strUtl.isNull(whetherUnderMtnc) && "T".equalsIgnoreCase(whetherUnderMtnc)) {
//							returnView = "/maintenance";
//							model.addAttribute("umMessage", preLoginConfigProps.get("message"));
//
//							log.info("Site maintenance info:");
//							log.info("Site um flag: {}", whetherUnderMtnc);
//							log.info("Site um message: {}", preLoginConfigProps.get("message"));
//
//							return returnView;
//
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
	
//					}
//				} else {
//					log.info("File does not exists: {}", preLoginConfigFilePath);
//					log.info(ACCESS_DENIED_MSG);
//					return "/accessDenied";
//				}
//			} catch (Exception e) {
//				log.info("Error in pre login checking: ", e.getMessage());
//				e.printStackTrace();
//			}
//
//			if (loginService.checkIfLicenseKeyExpired(parentPath)) {
//				returnView = "/licenseExpiry";
//				return returnView;
//			}
//		}
//
//		return returnView;
//	}// end pre-login checkup method

	@GetMapping(value = { "/", "/login" })
	public String login(HttpSession session, ModelMap map, @RequestParam(value = "email", required = false) String email, UserMast entity) {
 //System.out.println("email............"+email);
 //System.out.println("entity.........///"+entity.getEmail());
		String appLastBuildTimestamp = "";
		try {
			appLastBuildTimestamp = loginService.getAppLastBuildTimestamp(buildProperties.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("appLastBuildTimestamp", appLastBuildTimestamp);

		return "login";
	}// login method end

	@RequestMapping(value = "/auth", method = { RequestMethod.GET, RequestMethod.POST })
	public String authenticate(HttpSession session, RedirectAttributes redirectAttr, HttpServletRequest request) {
		String redirectAtion = "redirect:/login";

		Stream<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream();

		List<GrantedAuthority> authList = authorities.collect(Collectors.toList());
		String user_code = "", user_status = "";

		for (GrantedAuthority authority : authList) {
			String role = authority.getAuthority();

			if (!strUtl.isNull(role) && role.contains("UserCode :")) {
				user_code = role.replaceAll("UserCode :", "").trim();
				
			} else if (!strUtl.isNull(role) && role.contains("UserStatus :")) {
				user_status = role.replaceAll("UserStatus :", "").trim();

			}
		} // loop end

		UserMast loginUser = null;
		if (!strUtl.isNull(user_code)) {
			try {
				loginUser = this.userMastService.getUserByUserCode(user_code);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (loginUser != null && user_status.equalsIgnoreCase("A")) {

//			ViewClientMast client = viewClientMastService.getClientById(client_code);
			UserRoleMast roleEntity = roleService.getRoleByRoleCode(loginUser.getRole_code());
		
			List<UserMenuMast> userMenu = menuService.getMenuListOnUserRole(roleEntity.getAssigned_menu());
			
//			if (client != null && !strUtl.isNull(client.getApprovedby()) && client.getApproveddate() != null) {
				
				this.setDefaultLoginAssessment(loginUser, session);
//				session.setAttribute("WORKING_USER", client);
				session.setAttribute("LOGIN_USER", loginUser);
				session.setAttribute("USER_ROLE", roleEntity);
				session.setAttribute("SESSION_TIMESTAMP", dateTimeUtl.get_sysdate("dd MMM yyyy hh:mm:ss a"));
				session.setAttribute("MATH_RANDOM", java.lang.Math.random());
				session.setAttribute("ASSIGNED_MENU", userMenu);

				redirectAtion = "redirect:/"+roleEntity.getAssigned_dashboard();
//			} else {
//				redirectAttr.addFlashAttribute("errorMessage",
//						"You are not an approved user, So Kindly check your email for verification or contact Admin.");
//			}
		} else {
			redirectAttr.addFlashAttribute("errorMessage", "Invalid Login ID or Password provided.");
		}
		return redirectAtion;
	}// authenticate method end

	@RequestMapping(value = "/invalid-login", method = { RequestMethod.GET, RequestMethod.POST })
	public String invalidLogin(HttpSession session, RedirectAttributes redirectAttrs) {

		try {
			session.invalidate();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		log.info("Inside /invalid-login mapping.... An Invalid login attemp found.");
		redirectAttrs.addFlashAttribute("errorMessage", "Invalid Login ID or Password provided.");
		return "redirect:/login";
	}// invalidLogin method end

	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	public String logout(HttpSession session) {

		try {
//			Assessment assessment = (Assessment) session.getAttribute("ASSESSMENT");
//			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");

			Long clientSessionId = (Long) session.getAttribute("CLIENT_LOGIN_SEQ");
			if (clientSessionId != null) {
				UserLoginTran logoutClientLoginTran = clientLoginService
						.fetchByClientSessionSeq(clientSessionId);
				logoutClientLoginTran.setLogout_time(new Timestamp(new Date().getTime()));
				logoutClientLoginTran.setLogout_method("P");

				clientLoginService.update(logoutClientLoginTran);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		session.invalidate();
		return "redirect:/login";
	}// logout method end

	private void setDefaultLoginAssessment(UserMast userMast, HttpSession session) {
		try {
			Assessment asmt = new Assessment();

//			asmt.setAccYear(!strUtl.isNull(userMast.getAcc_year()) ? userMast.getAcc_year() : "20-21");
//			asmt.setQuarterNo(
//					!strUtl.isNull(userMast.getDefault_quarter_no()) ? Integer.valueOf(userMast.getDefault_quarter_no())
//							: 1);
//			asmt.setTdsTypeCode(
//					!strUtl.isNull(userMast.getDefault_tds_type_code()) ? userMast.getDefault_tds_type_code() : "24Q");

//			String month = "";
//			switch (asmt.getQuarterNo()) {
//			case 1:
//				month = "APR";
//				break;
//			case 2:
//				month = "JUL";
//				break;
//			case 3:
//				month = "OCT";
//				break;
//			case 4:
//				month = "JAN";
//				break;
//			}
//			asmt.setTdsTypeCode(
//					!strUtl.isNull(userMast.getDefault_tds_type_code()) ? userMast.getDefault_tds_type_code() : "24Q");
//			asmt.setMonth(month);
//
//			asmt.setQuarterFromDate(dateTimeUtl.getQuarterFromDate(asmt.getAccYear(), asmt.getQuarterNo()));
//			asmt.setQuarterToDate(dateTimeUtl.getQuarterToDate(asmt.getAccYear(), asmt.getQuarterNo()));
			asmt.setYearBegDate(dateTimeUtl.getYearBegDate(asmt.getAccYear()));
			asmt.setYearEndDate(dateTimeUtl.getYearEndDate(asmt.getAccYear()));

			session.setAttribute("ASSESSMENT", asmt);

			log.info("Default Login Assessment: {} > {} > {} > {}", asmt.getAccYear(), asmt.getQuarterNo(),
					asmt.getTdsTypeCode(), asmt.getMonth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// assessment object binding

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String errorCallback(ModelMap map) {

		String appLastBuildTimestamp = "";
		try {
			appLastBuildTimestamp = loginService.getAppLastBuildTimestamp(buildProperties.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("appLastBuildTimestamp", appLastBuildTimestamp);
		return "error";
	}

	@RequestMapping(value = "/getAllFormElements", method = RequestMethod.GET)
	public String allElements() {
		return "getAllFormElement";
	}// global elements

	@RequestMapping(value = "/userProfile", method = RequestMethod.GET)
	public String userProfile(HttpSession session, ModelMap model) {
		List<String> List = new ArrayList<>();
		String returnResult = "pages/userProfile";

//		ViewClientMast loginClient1 = (ViewClientMast) session.getAttribute("WORKING_USER");
		UserMast loginUser1 = (UserMast) session.getAttribute("LOGIN_USER");
		 List=userMastService.getRoleName(loginUser1.getRole_code());
	     String Role_name = List.toString().replace("[", "").replace("]", "");
	     
//		Object roll_code=(UserMast) session.getAttribute("LOGIN_USER");;
//		session.setAttribute("roll_code", roll_code);

//		model.addAttribute("loginClient", loginClient1);
		model.addAttribute("loginUser", loginUser1);
		model.addAttribute("Role_name", Role_name);

		return returnResult;
	}// end method

	@RequestMapping(value = "/userProfileUpdate", method = RequestMethod.POST)
	public @ResponseBody String userProfileUpdate(HttpSession session, ModelMap model,
			@ModelAttribute("loginUser") UserMast loginUser) {

		String returnResult = "error";
		try {
			UserMast updateUserProfile = userMastService.updateUserProfile(loginUser);
			if (updateUserProfile != null) {
				session.setAttribute("LOGIN_USER", updateUserProfile);
				returnResult = "success";
			}
		} catch (Exception e) {
			returnResult = "error";
		}
		return returnResult;
	}// end method
	
	
	
	@RequestMapping(value = "/addFavMenu", method = RequestMethod.GET)
	public String addFavMenu(HttpSession session, ModelMap model,@Param(value = "code1") String code1) {
		List<UserMenuMast> List = new ArrayList<UserMenuMast>();
		List<UserMenuMast> favmenuList=null;
        String action="save";
		UserMast Entity = new UserMast();
	
		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			String userCode = userMast.getUser_code();
			List=menuService.getMenuList();
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
			
			
			
			if (!strUtl.isNull(userCode)) {
				action = "edit";
				Entity=userMastService.getEditbyuserCode(userCode);
				
			}
		} catch (Exception e) {
		e.printStackTrace();	// TODO: handle exception
		}
		
		model.addAttribute("List", List);
		model.addAttribute("action", action);
		model.addAttribute("favmenuList", favmenuList);

		return "pages/addFavMenu";

	}// end method
	
	
	@RequestMapping(value = "/SearchMenuList", method = RequestMethod.POST)
	public String SearchMenuList(Model map, @Param(value = "searchvalue") String searchvalue,HttpSession session,
			@RequestParam(value = "code1", required = false) String code1,@RequestParam(value = "code2", required = false) String code2) {
		
		
		
		List<UserMenuMast> List = new ArrayList<UserMenuMast>();
		List<UserMenuMast> list = new ArrayList<UserMenuMast>();
		List<UserMenuMast> UserMenuMastlist=null;
		List<UserMenuMast> favmenuList=null;
		List<String> newlist=new ArrayList();
		String favmenu=null;
		String app_url=null;
		String substr2=null;
		try {
			UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
			String userCode = userMast.getUser_code();
			UserMast userMastObj = userMastService.getUserByUserCode(userCode);
			favmenu = userMastObj.getFavourite_menu()!= null ?userMastObj.getFavourite_menu():"";
		    String[] favourite_menu_arr = favmenu.split("#");
			UserMenuMastlist = userMenuMastRepo.findAll();
			List<String> clearedRegistrationIdList = Arrays.asList(favourite_menu_arr);
			favmenuList = UserMenuMastlist.stream()
		            .filter(s -> clearedRegistrationIdList.stream().anyMatch(i -> i.equalsIgnoreCase(s.getMenu_code())))
		            .collect(Collectors.toList());
			List = menuService.getMenuListFilter(searchvalue);
			System.out.println("code1==="+code1);

			if(code1 != null) {
				substr2 = code1.substring(9,code1.length()-1);
				System.out.println("substr2=="+substr2);

			}
			
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		
		map.addAttribute("List", List);
		map.addAttribute("favmenu", favmenu);
		map.addAttribute("searchvalue", searchvalue);
		map.addAttribute("substr2", substr2);

		return "pages/addFavMenu :: ajaxListCard ";

	}
	
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	 @ResponseBody
    public String forgetPassword(Model map, @RequestParam(value = "email", required = false) String email, UserMast entity, HttpSession session) {
		// System.out.println("Email............"+email);
		String response1 = "";
		String response = "error";
		//List<UserMast> List = new ArrayList<UserMast>();
		try {
			response1 = userMastService.getUserCodeFromEmailId(email);
			 String[] split = response1.split("[~]");
			 
				String value1 = split[0];
				String value2 = split[1];
				response = value1;
				
			// System.out.println("response............"+response);
			
			session.setAttribute("value2", value2);
			
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		return response;

	}

   
	@RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
	 @ResponseBody
	public String checkPassword(String userCode,Model map,HttpSession session, @RequestParam(value = "new_password", required = false) String new_password,
		   @RequestParam(value = "confirm_password", required = false) String confirm_password, UserMast entity) {
    	userCode = (String) session.getAttribute("value2");
    	
//    	System.out.println("userCode............"+userCode);
//    	System.out.println("New_password............"+new_password);
//    	System.out.println("confirm_password............"+confirm_password);
    	
		String response = "error";
		
		try {
			
			if(new_password.equalsIgnoreCase(confirm_password)) {
				System.out.println("inside second controleer");
				Optional<UserMast> entitylist = this.userRepo.findById(userCode);
				UserMast wishentity = entitylist.get();
				entitylist.get().setLoginPwd(new_password);
				
				entity=userRepo.save(wishentity);
				response = "success";
			}else {
				response = "error";
			}
		
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		return response;

	}
	
	@GetMapping(value = { "/forgotPassword" })
//	@RequestMapping(value = {"/","/forgotPassword"}, method = RequestMethod.GET)
//	 @ResponseBody
	public String forgotPassword(HttpSession session, ModelMap map, @RequestParam(value = "email", required = false) String email, UserMast entity) {
 //System.out.println("email............"+email);
 //System.out.println("entity.........///"+entity.getEmail());
		String appLastBuildTimestamp = "";
		try {
			appLastBuildTimestamp = loginService.getAppLastBuildTimestamp(buildProperties.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("appLastBuildTimestamp", appLastBuildTimestamp);

		return "/forgotPassword";
	}// login method end
	
	
	@GetMapping(value = {"/changePassword" })
//	@RequestMapping(value = {"/changePassword"}, method = RequestMethod.POST)
//	 @ResponseBody
	public String changePassword(String userCode,Model map,HttpSession session, @RequestParam(value = "new_password", required = false) String new_password,
			   @RequestParam(value = "confirm_password", required = false) String confirm_password) {
		String appLastBuildTimestamp = "";
		try {
			appLastBuildTimestamp = loginService.getAppLastBuildTimestamp(buildProperties.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		map.addAttribute("appLastBuildTimestamp", appLastBuildTimestamp);

		return "/changePassword";
	}// login method end
	


	 @RequestMapping(value = "/lastLoginDetail") 
	 public String lastLoginUser(Model map, HttpSession session)
	  { 
		  List<UserLoginTran> loginGridLast = new ArrayList<UserLoginTran>();
	  
	  UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER"); 
	  String usercode = userMast.getUser_code();
	  
	  System.out.println("User Code======" + usercode); 
	  loginGridLast =loginService.getUserLoginLastDetail(usercode);
	  
	 
	  
	  System.out.println("Details................." + loginGridLast);
	  
	  map.addAttribute("loginGridlast", loginGridLast);
	  
	  return "pages/userDetail/lastLoginUserDetail"; }
}
