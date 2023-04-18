package com.lhs.taxcpcAdmin.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

@Repository
public interface UserMastRepository extends JpaRepository<UserMast, String>,GenericCustomRepository<String,UserMast> {

	public UserMast findByLoginId(String loginId);
	
	@Query("select a.user_name from UserMast a where a.user_code = :userCode")
	String getUserName(String userCode);

	@Query("select t.user_name from UserMast t where t.user_code = :reported_by")
	public String getReportedBy(String reported_by);

	@Query("select t.user_code from UserMast t where t.email = :email")
	public String getEmailId(String email);

	public UserMast save(Optional<UserMast> menuEntity);


	
}
