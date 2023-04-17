package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;


public interface WishlistPendingWorkService{
	
	
	

	String saveWorkDetail(TaxcpcWishlistPendingWork entity);
	
	 List<TaxcpcWishlistPendingWork> getWorkListOnType(String work_type);
		  	
	
	//List<TaxcpcWishlistPendingWork> findByWorkType(String work_type);

	TaxcpcWishlistPendingWork getWorkDetailByCode(Long workCode);

	String deleteWork(Long workCode);

	Long getworkDetailsCount(FilterDTO filter,String workType,DataGridDTO dataGridDTO);
	
	List<TaxcpcWishlistPendingWork> getworkDetailsList(DataGridDTO dataGridDTO, String recPerPage, long total,FilterDTO filter,String workType);

	String addViewForm(TaxcpcWishlistPendingWork entity1, Long workcode,String remark);

	int getcountTable(String work_type, String work_nature, String work_priority, String project_code, String status);

	List<TaxcpcWishlistPendingWork> getEntityListFilter(String work_type, String work_nature, String work_priority,
			String project_code, String status);



	

}
