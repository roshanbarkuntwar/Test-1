package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.BankAuditAttach;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;

	

@Repository
public interface BankAuditAttachRepository extends JpaRepository<BankAuditAttach, Integer>,GenericCustomRepository<Integer, BankAuditAttach>{

	@Query(value = "select doc_attach_name from bank_audit_attach where audit_mast_seq_id=:audit_mast_seq_id", nativeQuery = true)
	String getFileName(Integer audit_mast_seq_id);


	@Query("from BankAuditAttach t where t.audit_mast_seq_id =:audit_mast_seq_id")
	BankAuditAttach getlistbyCode(Integer audit_mast_seq_id);

	@Query(value = "select doc_resolve_attach_name from bank_audit_attach where audit_mast_seq_id=:audit_mast_seq_id", nativeQuery = true)
	String getFile(Integer audit_mast_seq_id);



}
