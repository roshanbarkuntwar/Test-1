package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import com.lhs.taxcpcAdmin.model.entity.UserMenuMast;

public interface UserMenuMastService {

	Map<String, String> getMenuCodeNameList();

	List<UserMenuMast> getMenuListOnUserRole(String assigned_menu);

	List<UserMenuMast> getMenuList();

	Map<String, String> getMenuCodeNameList1();

	List<UserMenuMast> getMenuListFilter(String searchvalue);

	Map<String, String> getFavriteMenuList();

	Map<String, String> getMenuUrl();



}
