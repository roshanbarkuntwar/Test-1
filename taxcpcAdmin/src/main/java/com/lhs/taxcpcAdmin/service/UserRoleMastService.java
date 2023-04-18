package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserMenuMast;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;

public interface UserRoleMastService {

	Map<String, String> getRoleCodeNameList();
	
	Map<String, String> getDeptNameList();

	List<UserRoleMast> getUserRoleDetail();

	UserRoleMast getRoleByRoleCode(String role_code);

	String saveOrUpdateRole(UserRoleMast userRoleFormData);

	List<String> getDashboardList();


	Long getUserRoleCount(FilterDTO filter);

	String getRoleNameByRoleCode(String role_code);


	String viewUserDetail(String role_code);

	Map<String, String> RoleTypeList();

	List<UserRoleMast> getUserRoleBrowseList(FilterDTO filter, DataGridDTO dataGridDTO, Map<String, String> menuList,
			Map<String, String> roleTypeList,long count);

	String getEntityNamebyCode(String entity_code);

	String  getEntityList(String entity_code);


}
