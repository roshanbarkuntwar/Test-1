package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.UserMastRepository;
import com.lhs.taxcpcAdmin.dao.UserMastRepositorySupport;
import com.lhs.taxcpcAdmin.dao.UserRoleMastRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserMastServiceImpl implements UserMastService {

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	private UserMastRepository userRepo;
	
	@Autowired 
	private UserRoleMastRepository roleMastRepository;

	@Autowired
	private UserMastRepositorySupport userRepoSupport;

	@Autowired
	GlobalDoWorkExecuter executer;

	@Override
	public UserMast getUserByUserCode(String userCode) {
		UserMast user = null;
		try {
			user = userRepo.findById(userCode).orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public UserMast updateUserProfile(UserMast userMast) {

		UserMast updatedUser = null;
		try {
			if (userMast != null) {

				System.out.println("Profile user: " + userMast);
				Optional<UserMast> userByCode = userRepo.findById(userMast.getUser_code());
				if (userByCode.isPresent()) {
					UserMast dbUserMast = userByCode.get();

					if (!strUtl.isNull(userMast.getLoginId()))
						dbUserMast.setLoginId(userMast.getLoginId());

					if (!strUtl.isNull(userMast.getUser_name()))
						dbUserMast.setUser_name(userMast.getUser_name());

					if (!strUtl.isNull(userMast.getEmail()))
						dbUserMast.setEmail(userMast.getEmail());

					if (!strUtl.isNull(userMast.getPhone_no()))
						dbUserMast.setPhone_no(userMast.getPhone_no());

					if (!strUtl.isNull(userMast.getLoginPwd()))
						dbUserMast.setLoginPwd(userMast.getLoginPwd());

					updatedUser = userRepo.saveAndFlush(dbUserMast);
					System.out.println("Updated user: " + updatedUser);
					log.info("User profile have been updated successfully.");
				}
			}
		} catch (Exception e) {
			log.info("Error updating user profile.", e);
		}
		return updatedUser;

	}

	@Override
	public List<UserMast> getAllUserDetail() {
		// TODO Auto-generated method stub
		List<UserMast> userList = new ArrayList<UserMast>();
		try {
			userList = userRepo.findAll();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public String saveOrUpdateUser(UserMast userMastFormData) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			userRepo.save(userMastFormData);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String deleteUser(String user_code) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			userRepo.deleteById(user_code);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public Map<String, String> getAllUserCodeName() {
		// TODO Auto-generated method stub
		Map<String, String> userList = new HashMap<>();
		try {
			userList = userRepo.findAll().stream()
					.collect(Collectors.toMap(UserMast::getUser_code, UserMast::getUser_name));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public Map<String, String> getUserByDeptAndRole(String role_code) {
		// TODO Auto-generated method stub

		System.out.println("role_code=" + role_code);
		Map<String, String> userList = new HashMap<>();
		try {
			String queryStr = "";
			if (role_code.equalsIgnoreCase("TSTR")) {

				queryStr = "  select t.user_code, t.user_name from user_mast t, user_role_mast r WHERE "
						+ "t.role_code = r.role_code \r\n" + " and  R.ROLE_TYPE_CODE = 'TSTR'";
			}else if (role_code.equalsIgnoreCase("FUNC")) {

				queryStr = "  select t.user_code, t.user_name from user_mast t, user_role_mast r WHERE "
						+ "t.role_code = r.role_code \r\n" + " and  R.ROLE_TYPE_CODE = 'FUNC'";
			} 
			else {
				queryStr = "  select t.user_code, t.user_name from user_mast t, user_role_mast r WHERE "
						+ "t.role_code = r.role_code \r\n" + " and  R.ROLE_TYPE_CODE = 'DLPR'";
			}
			System.out.println("queryStr>>" + queryStr);
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				userList.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userList;
	}

	@Override
	public Long getUserCount(FilterDTO filter) {
		// TODO Auto-generated method stub
		return userRepoSupport.getUserTranBrowseCount(filter);
	}

	@Override
	public List<UserMast> getReqBrowseList(FilterDTO filter, DataGridDTO dataGridDTO) {
		// TODO Auto-generated method stub
		List<UserMast> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
//		System.out.println("dataGridDTO.getPaginator()..." + dataGridDTO.getPaginator());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
				
				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();
				

				list = userRepoSupport.getUserBrowseList(filter, minVal, maxVal);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public Map<String, String> getUserByUserType(String user_type) {
		// TODO Auto-generated method stub
		Map<String, String> userList = new HashMap<>();
		try {
			String queryStr = "select t.user_code, t.user_name t\r\n " + "  from user_mast t" + " where t.user_type = '"
					+ user_type + "'";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				userList.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userList;
	}

	
	@Override
	public Map<String, String> getUserByUser(String user_type) {
		// TODO Auto-generated method stub
		Map<String, String> userList = new HashMap<>();
		try {
			String queryStr =
					"select t.user_code,\n" +
							"       t.user_name || ' (' ||\n" + 
							"        (select v.role_name\n" + 
							"          from user_role_mast v\n" + 
							"         where t.role_code = v.role_code)||')' user_name from user_mast t\n" + 
							"         where t.user_type = 'LHS'";

			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				userList.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userList;
	}

	@Override
	public Map<String, String> getUserList() {
		Map<String, String> userList = new HashMap<>();
		try {
			String queryStr = "select user_code,user_name  from  user_mast";

			// System.out.println("queryStr="+queryStr);
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);

			for (Object[] obj : result) {
				userList.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userList;
	}

	@Override
	public String getRoleTypeByRoleCode(String role_code) {
		role_code=roleMastRepository.getRoleTypeByRoleCode1(role_code);
		return role_code;
	}
	
	@Override
	public UserMast getMenuList(String userCode, String Menu_code,UserMast entity) {
		System.out.println("Menu_code==="+Menu_code);
		Optional<UserMast> entitylist = this.userRepo.findById(userCode);
		UserMast  wishentity = entitylist.get();
		entitylist.get().setFavourite_menu(Menu_code);
		entity=userRepo.save(wishentity);
		return  entity;
	}

	



	@Override
	public Map<String, String> getlist() {
		
		Map<String, String> userList = new HashMap<>();
		
		try {
			String queryStr = "select user_code,favourite_menu from  user_mast ";

			// System.out.println("queryStr="+queryStr);
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);

			for (Object[] obj : result) {
				userList.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return userList;
	}

	@Override
	public String getUserCodeFromEmailId(String email) {
		System.out.println("email..."+email);
		String response = "error";
		String user_code ="";
		try {
			System.out.println("Response Is Success....."+response);
			userRepo.getEmailId(email);
			System.out.println("Response Is Success....."+response);
			user_code = userRepo.getEmailId(email);
			System.out.println("Response Is Success....."+user_code);
			
			
			if(user_code != null)
			{
			response = "success";
			System.out.println("Response In Side If...."+response);
			}else {
				response = "error";
			System.out.println("Response In Side else....."+response);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return response+"~"+user_code;
	}

	@Override
	public UserMast getEditbyuserCode(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getFavList() {
		Map<String, String> userList = new HashMap<>();
		try {
			userList = userRepo.findAll().stream().filter(c -> c.getFavourite_menu() != null)
					.collect(Collectors.toMap(UserMast::getUser_code, UserMast::getFavourite_menu));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return userList;
	}

//	@Override
//	public UserMast getUpdateMenuList(String userCode, String update_code, UserMast entity) {
//		Optional<UserMast> entitylist = this.userRepo.findById(userCode);
//		UserMast  wishentity = entitylist.get();
//		entitylist.get().setFavourite_menu(entitylist.get().getFavourite_menu()+update_code);
//		entity=userRepo.saveAndFlush(wishentity);
//		
//		return  entity;
//	}

	@Override
	public int getcount(String user_name, String user_type, String user_mode, String role_code, String user_status) {
		
		int count= 0;
		try {
			String Query = "select count(*) from user_mast where 1=1 ";
			

			if (!user_name.isEmpty()) {
				Query = Query + " and  lower(user_name)like lower('%" +user_name+ "%')";
			}
			if (!user_type.isEmpty()) {
				Query = Query + " and  lower(user_type)like lower('%" +user_type+ "%')";
			}
			if (!user_mode.isEmpty()) {
				Query = Query + " and  lower(user_mode)like lower('%" +user_mode+ "%')";
			}
			if (!role_code.isEmpty()) {
				Query = Query + " and  lower(role_code)like lower('%" +role_code+ "%')";
			}
			if (!user_status.isEmpty()) {
				Query = Query + " and  lower(user_status)like lower('%" +user_status+ "%')";
			}
			
			System.out.println("Query..."+Query);
			
			count = executer.getRowCount(Query);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}
	
	@Override
	public List<UserMast> getSearchUserDetails(String user_name, String user_type, String user_mode, String role_code, String user_status) {
	

		List<UserMast> list = new ArrayList<UserMast>();
		String Query = "";

		try {
	
			// Query = "select * from lhs_taxcpc_client_details where lower(entity_name)" ;
			Query = "select * from user_mast where 1=1 ";

			if (!user_name.isEmpty()) {
				Query = Query + " and  lower(user_name)like lower('%" +user_name+ "%')";
			}
			if (!user_type.isEmpty()) {
				Query = Query + " and  lower(user_type)like lower('%" +user_type+ "%')";
			}
			if (!user_mode.isEmpty()) {
				Query = Query + " and  lower(user_mode)like lower('%" +user_mode+ "%')";
			}
			if (!role_code.isEmpty()) {
				Query = Query + " and  lower(role_code)like lower('%" +role_code+ "%')";
			}
			if (!user_status.isEmpty()) {
				Query = Query + " and  lower(user_status)like lower('%" +user_status+ "%')";
			}
			
			System.out.println("Query..."+Query);
			list = executer.executeSQLQueryUserMastDeatils(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getRoleName(String role_code) {
		List<String> list = new ArrayList<>();
		try {
			String queryStr = "select short_role_name from user_role_mast  where role_code='"+role_code+"'";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object obj : result) {
				list.add((String) obj);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;
	
	}

	@Override
	public UserMast getDefaultMenuList(String userCode, String menu_code, UserMast entity) {
		Optional<UserMast> entitylist = this.userRepo.findById(userCode);
		UserMast wishentity = entitylist.get();
		entitylist.get().setFavourite_menu(menu_code);
		entity = userRepo.save(wishentity);
		return entity;
	}
}
