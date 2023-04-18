package com.lhs.taxcpcAdmin.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.EntityLogoMast;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysDefaultEntityClient;

public interface EntityMastService {



	Map<String, String> getDivisionList();

	EntityMast saveEntityDetailMast(EntityMast entity);

	String viewEntityDetails(String entity_code);


	long getEntityDetailsCount(FilterDTO filter);


	List<EntityMast> getEntityDetailsList(FilterDTO filter, String searchvalue, DataGridDTO dataGridDTO,Map<String, String> divisionList,List<EntityMast> entitylist, String recPerPage, long total );


	EntityMast getEntityEditList(String entity_code);

	public Map<String, String> getEntityNameList();
	
	public Map<String, String> getEntityNameList(String server_owner);

	List<EntityMast> getEntity();

	List<EntityMast> getEntityDataListFilter(String searchvalue);


	Map<String, String> getLogoList();

	void savelogo(EntityLogoMast entitylogo);


	List<EntityLogoMast> getList();

	Map<String, byte[]> getEntityLogo();

	String getfilename(String entity_code);


	Map<String, byte[]> getlogoNamebycode(String entity_code);

	Map<String, String> getEntityList();

	int getcount(String searchvalue);

	int getcountTable(String searchvalue);

	List<EntityMast> getEntityDataListFilterTable(String searchvalue);


	String saveDefaultEntry(LhssysDefaultEntityClient entity);


	List<LhssysDefaultEntityClient> getDefaultEntityList();

	Map<String, String> getAllDefaultClientCode();

	long getDefaultEntityDetailsCount(FilterDTO filter);


	List<LhssysDefaultEntityClient> getDefaultEntityDetailsList(FilterDTO filter, DataGridDTO dataGridDTO,
			List<LhssysDefaultEntityClient> defaultlist, String recPerPage, long total,String searchvalue);

	String viewDefaultEntityDetails(String client_code);

	int getcountTableDefault(String searchvalue,FilterDTO filter);

	List<LhssysDefaultEntityClient> getEntityDataListFilterTableDefault(String searchvalue);

	List<LhssysDefaultEntityClient> getEntityDataListFilterTableDefault(String searchvalue,String from_date,String to_date);


	LhssysDefaultEntityClient getDefaultEntityFromClientCode(String client_code);

	String deleteDefaultEntry(String client_code);



}
