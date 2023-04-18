package com.lhs.taxcpcAdmin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;

@Repository
public interface LhssysTaxcpcDeploymentTranRepository extends JpaRepository<LhssysTaxcpcDeploymentTran, Long>, GenericCustomRepository<Long,LhssysTaxcpcDeploymentTran>{
	
	@Query(value="delete from LhssysTaxcpcDeploymentTran t where t.seq_id =:seq_id", nativeQuery = true)
	void deleteById1(long seq_id);

	@Query("select t from LhssysTaxcpcDeploymentTran t where t.seq_id =:seq_id1")
	LhssysTaxcpcDeploymentTran findBySeqId(long seq_id1);

	

	
}
 