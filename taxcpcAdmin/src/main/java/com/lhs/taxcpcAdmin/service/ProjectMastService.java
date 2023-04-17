package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectMenuMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectModuleMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

public interface ProjectMastService {


	Map<String, String> getAllProjectCodeName();
	
	Map<String, String> getAllProjectModuleName();
	
	Map<String, String> getAllProjectMenuName();

	Map<String, String> getModulesOnProjectCode(String projectCode);

	List<ProjectMast> getProjectList();
	
	
	List<ProjectMast> getprojectDetailsFromCode(String parentCode);
	
	List<ProjectModuleMast> getProjectModuleList();
	
	List<ProjectMenuMast> getProjectMenuList();

	String saveProjectDetail(ProjectMast entity);
	
	String saveProjectModule(ProjectModuleMast entity);
	
	String saveProjectMenu(ProjectMenuMast entity);
	
	ProjectMast getProjectDetailsFromProjectCode(String parentCode);
	
	ProjectModuleMast getModuleDetailsFromProjectCode(String moduleCode);
	
	ProjectMenuMast getMenuDetailsFromProjectCode(String menu_code);
	
	String deleteProjectDetail(ProjectMast entity);
	
	String deleteModuleDetails(ProjectModuleMast entity);
	
	String deleteMenuDetails(ProjectMenuMast entity);

	Map<String, String> getMenuOnProjectCode(String projectCode, String moduleCode);

	String saveProjectDictionary(TaxcpcDictionaryDevCodeHelp entity);

	TaxcpcDictionaryDevCodeHelp getDictionaryDataOnEntryType(Integer seq_id);
	

	String deleteProjectDictionary(Integer seq_id);

	
	List<ProjectModuleMast> getSearch(String modname, String projcode, String projstat);

	Map<String, String> getAllProjectType();

	Map<String, String> getAllFramework();

	Map<String, String> getAllDatabase();

	Map<String, String> getAllMenuName();

	Map<String, String> getAllMenuType();

	Map<String, String> getAllSubMenuType();
	
	Map<String, String> getAllParentMenuName( );

	String viewProjectMenuView(String menu_code);
	
	ProjectMenuMast getProjectMenuDataonEntryType(String menu_code);

	String viewProjectModuleView(String module_code);
	
	ProjectModuleMast getProjectModuleDataonEntryType(String module_code);

	String viewProjectView(String proj);
	
	ProjectMast getProjectDataonEntryType(String project_code);


	Map<String, String> getProjectList1();

	Map<String, String> getModuleList1();
	
	Map<String, String> getProjectMenuList1();

	Map<String, String> getAllprojectArchitecture();

	Map<String, String> getAllprojectBackend();

	Map<String, String> getAllprojectDatabase();

	Map<String, String> getAllprojectFrontend();

	Map<String, String> getAllprojectFramework();

	Map<String, String> getAllApplicationCodeName();

	Map<String, String> getErrorCodeList();

	Map<String, String> getAllProjectInfoName();

	
	List<ProjectMast> getSearchProjectDetail(String projname, String status);

	Map<String, String> getDeplTypeList();

	

	Map<String, String> getProjectListWithStatus();

   Map<String, String> getModuleListWithStatus();


	Map<String, String> getModuleListWithStatus(String projectCode);
	
	Map<String, String> getProjectMenuListWithStatus(String projectCode, String moduleCode);

	List<ProjectMenuMast> getSearchProjectMenuDetail(String menuname,  String menuStatus,String project_name,String module_name);

	Map<Integer, String> getAllProjectFileName();

	String getWarFileNameOnProjectCode(String projectCode);

	Map<String, String> getProjectMenuListWithStatus();

	List<ProjectMast> getProjectListforClientForm(String app_type);

	Map<String, String> getAllConnDatabaseUser();

	long getprojectDictionaryCount(FilterDTO filter);

	List<TaxcpcDictionaryDevCodeHelp> getprojectDictionaryBrowserList(FilterDTO filter, DataGridDTO dataGridDTO, String entry_type);

	int getDictionarycount(String project_code, String dict_name);

	List<TaxcpcDictionaryDevCodeHelp> getSearchProjectDictionary(String project_code, String dict_name);

	String viewProjectDictionary(Integer seq_id);

	List<TaxcpcDictionaryDevCodeHelp> getProjectDictionaryDetailList(String entry_type);

	String getfileName(Integer seq_id);

	Map<String, String> getSubMenuCodeNameList();

	Map<String, String> getAllParentMenuName1(String project_code);

	Map<String, String> getArchitectureList(String application_type);

	Map<String, String> getSvnLink();

	Map<String, String> getAppserverPort(String serverip);

	

}
