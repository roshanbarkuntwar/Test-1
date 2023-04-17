package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.DocTran;

@Repository
public interface DocTranRepository extends JpaRepository<DocTran, String>,GenericCustomRepository<String,DocTran> {

	@Query("from DocTran t where t.doc_mode = 'Q' and  user_code=:userCode order by t.lastupdate desc")
	List<DocTran> getQuickDocList(String userCode);
	
	@Query("from DocTran t where t.user_code = :userCode order by t.lastupdate desc")
	List<DocTran> getDocList(String userCode);
	
	@Query("from DocTran t where t.doc_code = :doc_code ")
	DocTran getDocDetailbydocCode(String doc_code);
 
	
	@Query("delete from DocTran where doc_code=:doc_code and doc_type_code=:doc_t_code")
	public void deleteRowFromDocTran(String doc_code, String doc_t_code);

	@Query(value="select local_drive_path from doc_tran where doc_code=:doc_code" ,nativeQuery=true)
	String getFilePathList(String doc_code);
	
	@Query(value="SELECT(*) FROM doc_tran",nativeQuery=true)
	List<DocTran> findList1();

	@Query("select user_code from DocTran where doc_mode='O' and doc_code=:doc_code")
	List<DocTran> getQuickDocList1(String doc_code);



	
	
	

	
	

}
