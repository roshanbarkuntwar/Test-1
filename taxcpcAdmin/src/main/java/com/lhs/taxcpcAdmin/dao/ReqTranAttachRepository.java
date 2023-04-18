package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.ReqTranAttach;

@Repository
public interface ReqTranAttachRepository extends JpaRepository<ReqTranAttach, String>, GenericCustomRepository<String,ReqTranAttach>{

	@Query(value="select * from req_tran_attach  where req_code =:reqCode and slno=1", nativeQuery = true)
	ReqTranAttach findByReqCode(String reqCode);

	@Query(value="select req_code, slno, req_attach_name from req_tran_attach where req_code='10166' and slno='1'", nativeQuery = true)
	List<String> getFileName();

	@Query("select slno from ReqTranAttach where req_code=:reqCode")
	List<Integer> getListOfslno(String reqCode);

	@Query("select req_attach_name from ReqTranAttach where req_code=:reqCode order by slno asc")
	List<String> getListOfFileName(String reqCode);
}
