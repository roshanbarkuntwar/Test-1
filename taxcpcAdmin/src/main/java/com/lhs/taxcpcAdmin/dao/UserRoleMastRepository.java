package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;

@Repository
public interface UserRoleMastRepository extends JpaRepository<UserRoleMast, String>,GenericCustomRepository<String,UserRoleMast>{

	@Query("from UserRoleMast t where t.role_status = 'A'")
	List<UserRoleMast> getRoleList();

	@Query("select role_type_code from UserRoleMast t where t.role_code=:roleCode")
	String getRoleTypeByRoleCode1(String roleCode);
 
}
