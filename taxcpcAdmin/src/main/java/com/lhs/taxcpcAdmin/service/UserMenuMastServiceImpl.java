package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.UserMenuMastRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.model.entity.UserMenuMast;

@Service
public class UserMenuMastServiceImpl implements UserMenuMastService{
	
	@Autowired
	UserMenuMastRepository menuRepo;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	GlobalDoWorkExecuter executer;
	
	@Override
	public Map<String, String> getMenuCodeNameList() {
		// TODO Auto-generated method stub
		Map<String, String> menuList = new LinkedHashMap<>();
		List<UserMenuMast> list = new ArrayList<UserMenuMast>();
		try {
			
			list = menuRepo.findAll();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return menuList;
	}
	
	@Override
	public Map<String, String> getMenuCodeNameList1() {
		Map<String, String> menulist = new HashMap<>();

		// TODO Auto-generated method stub
		try {
			String queryStr = "select menu_code , menu_name  from user_menu_mast";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				menulist.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (menulist != null && menulist.size() > 0) ? menulist : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserMenuMast> getMenuListOnUserRole(String assigned_menu) {
		// TODO Auto-generated method stub
		List<UserMenuMast> menulist = new ArrayList<UserMenuMast>();
		try {
			if (!strUtl.isNull(assigned_menu)) {
				String[] menu = assigned_menu.split("#");
				StringBuilder menuSb = new StringBuilder();
				for (int i = 0; i < menu.length; i++) {
					menuSb.append("'"+menu[i]+"'").append(",");
				}
				String menuCode = menuSb.toString();
				menuCode = menuCode.substring(0, menuCode.length() - 1);
				
				
				String sqlQuery = "select * from user_menu_mast a \r\n"
						+ " where a.menu_code IN ("+menuCode+") \r\n"
						+ " and a.menu_status = 'A' \r\n"
						+ " start with a.parent_code is null \r\n" 
						+ " connect by prior a.menu_code = a.parent_code";
				
				Query query = entityManager.createNativeQuery(sqlQuery, UserMenuMast.class);
				menulist = query.getResultList();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return menulist;
	}

	@Override
	public List<UserMenuMast> getMenuList() {
		List<UserMenuMast> list = new ArrayList<UserMenuMast>();
		try {
			list=menuRepo.findAll();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public List<UserMenuMast> getMenuListFilter(String searchvalue) {
		List<UserMenuMast> list = new ArrayList<>();
        System.out.println("searchvalue==="+searchvalue);

	try {
		
		//String Query=" SELECT menu_name FROM user_menu_mast WHERE menu_code IN ("+code+")" ;
		//System.out.println("Query 1111>> " + Query);

		String Query="select menu_name ,menu_code from user_menu_mast where";
		if (!searchvalue.isEmpty()) {
			Query = Query + "  lower(menu_name)like lower('%" +searchvalue+ "%')";
		}
				
		System.out.println("Query >> " + Query);
		
		list = executer.executeSQLQueryMenuList(Query);
		
		System.out.println("list===="+list);
		

	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
	}

	@Override
	public Map<String, String> getFavriteMenuList() {
		Map<String, String> menulist = new HashMap<>();

		// TODO Auto-generated method stub
		try {
			String queryStr = "select menu_code,menu_name  from user_menu_mast";
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
	public Map<String, String> getMenuUrl() {
		Map<String, String> menulist = new HashMap<>();

		try {
			String queryStr = "select menu_code , menu_url  from user_menu_mast";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				menulist.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (menulist != null && menulist.size() > 0) ? menulist : null;
	}

	
	

}
