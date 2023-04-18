package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

@Service
public interface UserMastService {

	public UserMast getUserByUserCode(String userCode);

	public UserMast updateUserProfile(UserMast userMast);
	
	List<UserMast> getAllUserDetail();

	String saveOrUpdateUser(UserMast userMastFormData);

	String deleteUser(String user_code);

	public Map<String, String> getAllUserCodeName();

	public Map<String, String> getUserByDeptAndRole( String role_name);	
	
//	----------------------------------------------------------------------

	public Long getUserCount(FilterDTO filter);

	public List<UserMast> getReqBrowseList(FilterDTO filter, DataGridDTO dataGridDTO);

	public Map<String, String> getUserByUserType(String user_type);

	public Map<String, String> getUserList();

	public String getRoleTypeByRoleCode(String role_code);
	
	public Map<String, String> getUserByUser(String user_type);

	public UserMast getMenuList(String userCode, String Menu_code,UserMast entity);

	public Map<String, String> getlist();
	
	public String getUserCodeFromEmailId(String email);

	public UserMast getEditbyuserCode(String userCode);

	public Map<String, String> getFavList();

	public int getcount(String user_name, String user_type, String user_mode, String role_code, String user_status);

	public List<UserMast> getSearchUserDetails(String user_name, String user_type, String user_mode, String role_code, String user_status);

	public List<String> getRoleName(String role_code);


      public UserMast getDefaultMenuList(String userCode, String menu_code, UserMast entity);



}
