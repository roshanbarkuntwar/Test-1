package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.model.entity.ProjectMenuMast;


@Repository
public interface ProjectMenuMastRepository extends JpaRepository<ProjectMenuMast, String> {

	
//	@Query("from ProjectMenuMast a where a.menu_code = :menu_code")
//	ProjectMenuMast getprojectMenuOnProjectCode(String menu_code);

	@Query("from ProjectMenuMast a where a.module_code = :moduleCode and a.project_code = :projectCode")
	List<ProjectMenuMast> getMenusOnModuleCode(String projectCode, String moduleCode);

	@Query(value = "select * from Project_Menu_Mast where menu_code = ?1 and menu_type = ?2 and sub_menu_type = ?3 and menu_status = ?4", nativeQuery = true)
	List<ProjectMenuMast> searchAllProjectMenuDetail(String menuname, String menutype, String submenutype, String menuStatus);

	@Query("select a.menu_name from ProjectMenuMast a where a.menu_code =:menuCode")
	String getMenuName(String menuCode);

	
	 
}
