package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserRoleMast;

@Repository
public interface UserLoginTranRepository
		extends JpaRepository<UserLoginTran, Long>, GenericCustomRepository<String, UserLoginTran> {

	@Query("from UserLoginTran")
	List<UserLoginTran> findByReportedUser(String user_code);

//	@Query(value = "select * from user_login_tran where session_seqno = ?1 or machine_name = ?2 or machine_ip = ?3 or user_code= ?4", nativeQuery = true)
//	public List<UserLoginTran> search(String rowid, String macid, String macip, String userid);

	@Query("from UserLoginTran t where t.flag = 'D'")
	List<UserLoginTran> getMenuList();

	
	  @Query(value = "select * from user_login_tran where rownum <= 10 ",nativeQuery=true)
	  public List<UserLoginTran> getListBetweenMinToMaxValue();

	
	  @Query(value = "select count(1) FROM (select row_number() over (order by 1) SLNO,\r\n" + 
	  		"                      a.login_id login_id,\r\n" + 
	  		"                      a.user_code user_code,\r\n" + 
	  		"                      a.user_name user_name,\r\n" + 
	  		"                      max(a.login_time) last_login_time,\r\n" + 
	  		"                      count(1) no_of_login_count\r\n" + 
	  		"                 from (select v.user_code,\r\n" + 
	  		"                              b.user_name,\r\n" + 
	  		"                              b.login_id,\r\n" + 
	  		"                              v.login_time,\r\n" + 
	  		"                             count(v.login_time) over(partition by v.login_time) as a_login_time\r\n" + 
	  		"                        from user_login_tran v,user_mast b WHERE b.user_code = v.user_code\r\n" + 
	  		"                        group by v.user_code, v.login_time,b.login_id,b.user_name) a \r\n" + 
	  		"                        group by a.user_code,a.user_name,a.login_id )",nativeQuery=true)
	  Long getCount();

	


	

	 

}
