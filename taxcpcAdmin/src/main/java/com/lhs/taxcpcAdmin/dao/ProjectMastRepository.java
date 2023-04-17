package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.model.entity.ProjectMast;

@Repository
public interface ProjectMastRepository extends JpaRepository<ProjectMast, String>{

	@Query(" from ProjectMast a where a.project_code = :projectCode")
	public ProjectMast getprojectMastOnProjectCode(String projectCode);

	@Query("select a.war_filename from ProjectMast a where a.project_code = :projectCode")
	public String getWarFileName(String projectCode);

	@Query("select a.project_name from ProjectMast a where a.project_code = :projectCode")
	public String getProjectName(String projectCode);

	 @Query("select a.architecture_type_name from ViewArchitectureType a where a.architecture_type_code = :architectureCode") 
	  public String getArchitectureName(String architectureCode);
	
	  @Query("select a.backend_type_name from ViewBackendType a where a.backend_type_code = :backendCode") 
	  public String getBackendName(String backendCode);
	

	  @Query("select a.frontend_type_name from ViewFrontendType a where a.frontend_type_code = :frontendCode") 
	  public String getFrontendName(String frontendCode);

	  @Query("select a.database_type_name from ViewDatabaseType a where a.database_type_code = :databaseCode") 
	  public String getDatabaseName(String databaseCode);

	  @Query("select a.framework_type_name from ViewFrameworkType a where a.framework_type_code = :frameworkCode") 
	  public String getFrameworkName(String frameworkCode);
	  
	  @Query(value ="select * from Project_Mast p where p.application_type = :app_type", nativeQuery = true )
	  public List<ProjectMast> findByApplicationType(String app_type);

	  @Query("select a.menu_name from ProjectMenuMast a where a.menu_code = :menuCode")
	  public String getParentName(String menuCode);

	/*
	 * @Query("select a.menu_name from ProjectMenuMast a where a.parent_menu_code = :menuCode"
	 * ) public String getSubMenuName(String menuCode);
	 */
}
