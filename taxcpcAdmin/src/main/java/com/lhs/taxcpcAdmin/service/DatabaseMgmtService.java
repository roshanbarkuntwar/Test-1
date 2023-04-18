package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbDetails;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbObjects;
import com.lhs.taxcpcAdmin.model.entity.LhssysMainTables;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;


public interface DatabaseMgmtService {

	List<LhssysDbObjects> getDatabaseUserList();

	List<LhssysDbObjects> getDatabasePackageList();

	List<LhssysDbObjects> getDatabaseSynonymList();

	List<LhssysDbDetails> getDatabaseDetailList();

	String saveLhssysDbDetailsDetail(LhssysDbDetails entity);

	LhssysDbDetails getLhssysDbDetailsEntity(String db_code);

	List<LhssysMainTables> getDatabaseTableDetailList();

	LhssysMainTables getLhssysMainTablesEntity(String object_code);

	String saveLhssysMainTableDetails(LhssysMainTables entity);

	List<LhssysDbDetails> getSearchDatabaseDetail(String type_of_db, String db_ip);

	List<LhssysMainTables> getSearchDatabaseTableDetail(String table_or_view_name, String object_type, String project_code);

	List<LhssysDbObjects> getSearchDatabaseUser(String db_object_name, String type_of_database, String status);

	List<LhssysDbObjects> getSearchDatabasePackage(String db_object_name, String status);

	List<LhssysDbObjects> getSearchDatabaseSynonym(String db_object_name, String type_of_database, String status);

	Map<String, String> getAllProjectCodeName();

	long getUserCount(FilterDTO filter);

	List<LhssysMainTables> getDatabaseTableList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, Map<String, String> projectList);

	int getcountText(String table_or_view_name, String object_type, String project_code);

	long getdatabaseManagementDetailsCount(FilterDTO filter);

	long getDatabaseManagementCount(FilterDTO filter);

	List<LhssysDbDetails> getDatabaseManagementList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage);


	int getcountTableDatabase(String type_of_db, String db_ip);


	long getDatabasePackageCount(FilterDTO filter);

	long getDatabaseUserCount(FilterDTO filter);

	List<LhssysDbObjects> getDatabaseUserGridList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, long total);

	List<LhssysDbObjects> getDatabasePackageDetailsList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage, long total);


	int getDatabaseUserCountObject(FilterDTO filter, String db_object_name, String type_of_database, String status);

	int getDatabaseUserCountPackage(String db_object_name, String status);


}
