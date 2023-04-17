package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.model.entity.DocTranAttach;

@Repository
public interface DocTranAttachRepository extends JpaRepository<DocTranAttach, String> {

	@Query("from DocTranAttach t where t.doc_code =:doc_code1")
	List<DocTranAttach> getattachdocfile(String doc_code1);

	@Query("from DocTranAttach t where t.doc_code =:doc_code1")
	List<DocTranAttach> findNameById(String doc_code1);

	@Query(value = "select doc_attach_name from doc_tran_attach where doc_code=:doc_code", nativeQuery = true)
	String getFileName(String doc_code);

	@Query("from DocTranAttach t where t.doc_code =:doc_code")
	DocTranAttach getdocCode(String doc_code);

	@Query(value = "select doc_attach from doc_tran_attach where doc_code=:doc_code", nativeQuery = true)
	byte[] getFile(String doc_code);
	
	

}
