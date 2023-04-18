package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.UserMenuMastRepository;
import com.lhs.taxcpcAdmin.dao.UserRoleMastRepository;
import com.lhs.taxcpcAdmin.dao.UserRoleMastRepositorySupport;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;

import groovy.transform.Undefined.EXCEPTION;

@Service
public class UserRoleMastServiceImpl implements UserRoleMastService {

	@Autowired
	UserRoleMastRepository roleRepo;

	@Autowired
	UserMenuMastRepository usermenu;

	@Autowired
	UserRoleMastRepositorySupport roleRepoSupport;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	GlobalDoWorkExecuter executer;
	
	@Autowired
	UserMenuMastService userservice;

	@Autowired
	EntityManager entityManager;

	

	@Override
	public Map<String, String> getRoleCodeNameList() {
		// TODO Auto-generated method stub
		Map<String, String> roleList = new HashMap<>();
		Map<String, String> newrolelist = new LinkedHashMap<>();
		try {
			try {
				String queryStr = "select role_code, role_name  from  user_role_mast";
				List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
				for (Object[] obj : result) {
					roleList.put((String) obj[0], (String) obj[1]);
				}
				newrolelist = executer.sortTheList(roleList);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return (newrolelist != null && newrolelist.size() > 0) ? newrolelist : null;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return newrolelist;
	}

	@Override
	public Map<String, String> getDeptNameList() {
		Map<String, String> menulist = new LinkedHashMap<>();

		try {
			String queryStr = "select T.DEPT_type_code,T.DEPT_type_name from VIEW_DEPT_MAST t WHERE T.DEPT_type_status ='A'";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				menulist.put((String) obj[0], (String) obj[1]);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (menulist != null && menulist.size() > 0) ? menulist : null;
	}

	@Override
	public List<UserRoleMast> getUserRoleDetail() {
		// TODO Auto-generated method stub
		List<UserRoleMast> list = new ArrayList<UserRoleMast>();
		try {
			list = roleRepo.findAll().stream().sorted(Comparator.comparing(UserRoleMast::getLastupdate).reversed())
					.collect(Collectors.toList());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public UserRoleMast getRoleByRoleCode(String role_code) {
		// TODO Auto-generated method stub
		UserRoleMast roleEntity = new UserRoleMast();
		try {
			roleEntity = roleRepo.findById(role_code).orElse(null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return roleEntity;
	}

	@Override
	public String saveOrUpdateRole(UserRoleMast userRoleFormData) {

		// TODO Auto-generated method stub
		String response = "error";
		try {
			System.out.println("mi ith aaho ");
			if (userRoleFormData != null && strUtl.isNull(userRoleFormData.getRole_code())) {
				String query = "select UPPER(substr('" + userRoleFormData.getRole_name() + "', 0, 3)) || '-' ||\r\n"
						+ "      LPAD(max(to_number(regexp_substr(t.role_code, '\\d+') + 1)), 4, '0') id\r\n"
						+ "  from user_role_mast t";

				String roleCode = (String) executer.getSingleValueFromSQLQuery(query);
				System.out.println("QUERY>>>>>>>>>>" + query);
				if (!strUtl.isNull(roleCode)) {
					userRoleFormData.setRole_code(roleCode);
				}
			}

			userRoleFormData.setLastupdate(new Date());
			roleRepo.save(userRoleFormData);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<String> getDashboardList() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		try {
			String queryStr = "select dashboard_name from view_dashboard_type where status='A'";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object obj : result) {
				list.add((String) obj);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (list != null && list.size() > 0) ? list : null;
	}

	@Override
	public List<UserRoleMast> getUserRoleBrowseList(FilterDTO filter, DataGridDTO dataGridDTO,Map<String, String> menuList,
			Map<String, String> roleTypeList,long count) {
		// TODO Auto-generated method stub
		List<UserRoleMast> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();

				list = roleRepoSupport.getUserRoleBrowseList(filter, minVal, maxVal,menuList,roleTypeList);

			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public Long getUserRoleCount(FilterDTO filter) {
		// TODO Auto-generated method stub
		return roleRepoSupport.getUserTranBrowseCount(filter);
	}

	@Override
	public String getRoleNameByRoleCode(String role_code) { // TODO Auto-generated method stub
		String role_name = "";
		try {
			String queryStr = "select t.role_name from user_role_mast t where t.role_code='" + role_code + "'";
			role_name = (String) executer.getSingleValueFromSQLQuery(queryStr);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return role_name;
	}

	public Map<String, String> getMenuCodeNameList() {
		// TODO Auto-generated method stub
		Map<String, String> roleList = new LinkedHashMap<>();
		List<UserRoleMast> list = new ArrayList<UserRoleMast>();
		try {
			list = roleRepo.getRoleList();
			if (list != null && list.size() > 0) {
				roleList = list.stream()
						.collect(Collectors.toMap(UserRoleMast::getRole_code, UserRoleMast::getRole_name));
			}


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return roleList;
	}

	@Override
	public String viewUserDetail(String role_code) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		Map<String, String> menuList = new LinkedHashMap<>();
		String Role_code_type = "";
		List<String> menu_name;

		try {
			UserRoleMast entity = getRoleByRoleCode(role_code);
			Role_code_type = getRoleCodeName(entity.getRole_type_code());
			menuList = getRoleCodeNameList();
			menu_name = getMenuName(entity.getAssigned_menu());

			if (entity != null) {

				sb.append("<tr><td class=\"text-bold\">Role Type Code:</td><td class=\"text-wrap\">" + Role_code_type
						+ "</td></tr>");
				sb.append("<tr><td class=\"text-bold\">Role Name:</td><td class=\"text-wrap\">" + entity.getRole_name()
						+ "</td></tr>");
				sb.append("<tr><td class=\"text-bold\">Assigned Menu:</td><td class=\"text-wrap\">" + menu_name
						+ "</td></tr>");
				sb.append("<tr><td class=\"text-bold\">Assigned Dashboard:</td><td class=\"text-wrap\">"
						+ entity.getAssigned_dashboard() + "</td></tr>");

				if (entity.getRole_status().equalsIgnoreCase("A")) {
					sb.append(
							"<tr><td class=\"text-bold\">Role Status  : </td><td class=\"pre-wrap\">Active</td></tr>");
				} else {
					sb.append(
							"<tr><td class=\"text-bold\">Role Status  : </td><td class=\"pre-wrap\">Inactive</td></tr>");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}

	private List<String> getMenuName(String assigned_menu) {
		String[] Code;
		String Query = "";
		Map<String, String> menuList = new LinkedHashMap<>();
		List<String> list = new ArrayList<>();
		try {


			menuList = userservice.getMenuCodeNameList1();

			Code = assigned_menu.split("[#]");
			for (int j = 0; j <= Code.length - 1; j++){
				//System.out.println("splited----------" + Code[j]);

				for (Map.Entry<String, String> entry : menuList.entrySet()) {

					if (Code[j].equalsIgnoreCase(entry.getKey())) {
						list.add(entry.getValue());
					}

				}
			}

		}catch(EXCEPTION e) {
			e.printStackTrace();
			
			// TODO: handle exception
		}
		return list;

	}

	private String getRoleCodeName(String role_type_code) {
		String query = "";
		String role_name = "";
		try {

			query = "select t.role_type_name from VIEW_ROLE_TYPE t WHERE t.role_type_code ='" + role_type_code + "'";
			role_name = (String) executer.getSingleValueFromSQLQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return role_name;
	}

	@Override
	public Map<String, String> RoleTypeList() {
		Map<String, String> list = new LinkedHashMap<>();
		try {
			String queryStr = "select t.role_type_code,t.role_type_name from VIEW_ROLE_TYPE t WHERE t.role_type_status ='A'";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				list.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (list != null && list.size() > 0) ? list : null;

	}

	@Override
	public String getEntityNamebyCode(String entity_code) {
		String query = "";
		String Entity_Name = "";
		try {

			query = "select entity_name from entity_mast where entity_code ='" + entity_code + "'";
			Entity_Name = (String) executer.getSingleValueFromSQLQuery(query);

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		return Entity_Name;
	}

	@Override
	public String  getEntityList(String entity_code) {
		Map<String, String> List = new HashMap<>();
		String Entity_Name = "";
		String queryStr="";
		try {
		    queryStr = "select entity_name from entity_mast where entity_code ='" + entity_code + "' ";
			Entity_Name = (String) executer.getSingleValueFromSQLQuery(queryStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Entity_Name;
	}
}
