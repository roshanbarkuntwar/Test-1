package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcDictionaryDevCodeHelp;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

@Repository
public interface TaxcpcDictionaryDevCodeHelpRepository extends JpaRepository<TaxcpcDictionaryDevCodeHelp, Integer>,GenericCustomRepository<TaxcpcDictionaryDevCodeHelp, Integer>{

	@Query("from TaxcpcDictionaryDevCodeHelp t where t.seq_id= :seq_id")
	List<TaxcpcDictionaryDevCodeHelp> findBySeq_id(Long seq_id);

	@Query("from TaxcpcDictionaryDevCodeHelp t where t.entry_type= :entry_type")
	List<TaxcpcDictionaryDevCodeHelp> findByEntrytype(String entry_type);

	@Query(value = "select attachment_name from taxcpc_dictionary_dev_codehelp where seq_id=:seq_id", nativeQuery = true)
	String getFileName(Integer seq_id);
}
