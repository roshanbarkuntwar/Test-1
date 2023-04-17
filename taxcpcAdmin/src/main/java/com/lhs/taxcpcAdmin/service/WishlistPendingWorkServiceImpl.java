package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsDateUtility;
import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.ProjectMastRepository;
import com.lhs.taxcpcAdmin.dao.TaxcpcWishlistPendingWorkRepository;
import com.lhs.taxcpcAdmin.dao.TaxcpcWishlistPendingWorkRepositorySupport;
import com.lhs.taxcpcAdmin.dao.UserMastRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;

@Service
public class WishlistPendingWorkServiceImpl implements WishlistPendingWorkService{

	@Autowired
	TaxcpcWishlistPendingWorkRepository workRepo;
	
	@Autowired
    LhsDateUtility lhsDateUtility;
	
	@Autowired
	TaxcpcWishlistPendingWorkRepositorySupport wishRepSupp;
	
	@Autowired
	private LhsStringUtility strUtl;
	
	@Autowired 
	WishlistPendingWorkService wishservice;

	@Autowired
	GlobalDoWorkExecuter executer;
	
	@Autowired
	ProjectMastRepository projectMastRepository;
	
	
	@Autowired
	UserMastRepository UserMastRepo;
	
	@Autowired
	UserMastService user;
	
	@Override
	public String saveWorkDetail(TaxcpcWishlistPendingWork entity) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			entity.setStatus("N");
			entity.setLastupdate(new Date());
			workRepo.save(entity);

			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	
		@Override
	public TaxcpcWishlistPendingWork getWorkDetailByCode(Long workCode) {
		// TODO Auto-generated method stub
		TaxcpcWishlistPendingWork entity = new TaxcpcWishlistPendingWork();
		try {
			entity = workRepo.findById(workCode).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String deleteWork(Long workCode) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			workRepo.deleteById(workCode);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}
	
	public String getProjectName(String projectCode) {
		String Project_code="";
		try {
			Project_code = projectMastRepository.getProjectName(projectCode); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Project_code;
	}

	public List<String> getUserName(String userCode) {
		String[] Code;
		 String Query="";
		 Map<String, String> menuList = new LinkedHashMap<>();
			List<String> list = new ArrayList<>();

		try {
			    menuList =user.getAllUserCodeName();
			    Code=userCode.split("[,]");
		        for (int j = 0; j <= Code.length - 1; j++) {
		        for (Map.Entry<String, String> entry : menuList.entrySet()) {
				  if(Code[j].equalsIgnoreCase(entry.getKey())){
				  list.add(entry.getValue()); 
				  }
		         }
		        }	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;	
		
		
	}
	
	


	
	@Override
	public Long getworkDetailsCount(FilterDTO filter,String workType,DataGridDTO dataGridDTO) {
//		// TODO Auto-generated method stub
		
		return wishRepSupp.getWorkDetailsBrowseCount(filter,workType,dataGridDTO);
	}

	@Override
	public List<TaxcpcWishlistPendingWork> getworkDetailsList(DataGridDTO dataGridDTO, String recPerPage, long total,FilterDTO filter,String workType) {
		

		List<TaxcpcWishlistPendingWork> list = new ArrayList<>();
		
		PaginatorManipulation manipulation = new PaginatorManipulation();
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal();
		        long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) && !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal();
				list = wishRepSupp.getworkDetailsBrowseList(filter, minVal, maxVal,workType);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
		e.printStackTrace();	// TODO: handle exception
		}		
		return list;
	}



  @Override 
  public List<TaxcpcWishlistPendingWork> getWorkListOnType(String work_type) {
  
  List<TaxcpcWishlistPendingWork> list = new ArrayList<>(); 
  String Query="";
  try {
  
  Query="select t.work_code,\r\n" + 
  		"       t.work_type,\r\n" + 
  		"       t.work_nature,\r\n" + 
  		"       t.work_priority,\r\n" + 
  		"       (select p.project_name  from project_mast p where t.project_code = p.project_code) project_code,\r\n" + 
  		"       t.completion_date,\r\n" + 
  		"       (select u.user_name from user_mast u where t.user_code=u.user_code)user_code,\r\n" + 
  		"       t.work_description,\r\n" + 
  		"       t.status,\r\n" + 
  		"       t.lastupdate,\r\n" + 
  		"       t.external_link,\r\n" + 
  		"       t.remark,\r\n" + 
  		"       t.flag from TAXCPC_WISHLIST_PENDING_WORK t  ";
  
  
  try { 
	  Query=Query+"where t.work_type='"+work_type+"'";

	  }
  catch(Exception e) {
  } 
  
  list=executer.executeSQLQuery2(Query);
  
  
  } 
  catch (Exception e) { // TODO: handle
  e.printStackTrace(); }
  return list; }


@Override
public String addViewForm(TaxcpcWishlistPendingWork entity1, Long workcode,String remark) {
	String response = "error";
	try {
		Optional<TaxcpcWishlistPendingWork> entity = this.workRepo.findById(workcode);
		TaxcpcWishlistPendingWork wishentity = entity.get();
		entity.get().setRemark(remark);
		entity.get().setStatus("D");
		entity.get().setLastupdate(entity1.getLastupdate());
		entity1=workRepo.saveAndFlush(wishentity);
		response = "success";
	} catch (Exception e) {
		e.printStackTrace();
	}
	return response;
}


@Override
public int getcountTable(String work_type, String work_nature, String work_priority, String project_code,
		String status) {
	int count=0;
	try {
		
		String Query="select count(*) from taxcpc_wishlist_pending_work  where work_type='"+work_type+"'";
		
		if (!work_nature.isEmpty()) {
			Query = Query + " and  lower(work_nature)like lower('%" +work_nature+ "%')";
		}
		if (!work_priority.isEmpty()) {
			Query = Query + " and  lower(work_priority)like lower('%" +work_priority+ "%')";
		}
		if (!project_code.isEmpty()) {
			Query = Query + " and  lower(project_code)like lower('%" +project_code+ "%')";
		}
		if (!status.isEmpty()) {
			Query = Query + " and  lower(status)like lower('%" +status+ "%')";
		}
		count = executer.getRowCount(Query);
		
	} catch (Exception e) {
	e.printStackTrace();	// TODO: handle exception
	}
	// TODO Auto-generated method stub
	return count;
}


@Override
public List<TaxcpcWishlistPendingWork> getEntityListFilter(String work_type, String work_nature, String work_priority,
		String project_code, String status) {
	List<TaxcpcWishlistPendingWork> list = new ArrayList<>();
	String Query="";
	try {
		
 Query="select * from taxcpc_wishlist_pending_work  where work_type='"+work_type+"'";
		
		if (!work_nature.isEmpty()) {
			Query = Query + " and  lower(work_nature)like lower('%" +work_nature+ "%')";
		}
		if (!work_priority.isEmpty()) {
			Query = Query + " and  lower(work_priority)like lower('%" +work_priority+ "%')";
		}
		if (!project_code.isEmpty()) {
			Query = Query + " and  lower(project_code)like lower('%" +project_code+ "%')";
		}
		if (!status.isEmpty()) {
			Query = Query + " and  lower(status)like lower('%" +status+ "%')";
		}
		
      	list = executer.executeSQLQuery2(Query);

		
	} catch (Exception e) {
	e.printStackTrace();	// TODO: handle exception
	}
	
	return list;
}


}
 
