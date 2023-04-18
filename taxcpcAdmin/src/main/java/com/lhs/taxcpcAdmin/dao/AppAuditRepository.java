package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.AppDetails;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;

@Repository
public interface AppAuditRepository extends JpaRepository<LhssysTaxcpcBankAuditMast, Integer> , GenericCustomRepository<Integer,LhssysTaxcpcBankAuditMast>{

	@Query("from LhssysTaxcpcBankAuditMast t where t.seq_id=:seqId")
	LhssysTaxcpcBankAuditMast findBySeqId1(int seqId);

}
