package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.ProjectModuleMast;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

@Repository
public interface ProjectModuleMastRepository extends JpaRepository<ProjectModuleMast, String> {

	@Query("from ProjectModuleMast a where a.project_code = :projectCode")
	List<ProjectModuleMast> getModulesOnProjectCode(String projectCode);
	
	@Query("from ProjectModuleMast a where a.module_code = :moduleCode")
	ProjectModuleMast getprojectModuleOnProjectCode(String moduleCode);
	
	@Query("select module_code,module_name from ProjectModuleMast ")
	ArrayList<ArrayList<String>>  executeSQLQueryAsListOfList();
	
//	 @Query(value="select * from Project_Module_Mast where module_code = ?1 or select * from project_mast where project_c = ?2", nativeQuery=true)
//	  public List<ProjectModuleMast> search(String modname, String projcode);

	  @Query("select a.module_name from ProjectModuleMast a where a.module_code = :moduleCode")
    	String getModuleName(String moduleCode);


	
}
