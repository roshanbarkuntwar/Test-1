package com.lhs.taxcpcAdmin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.model.entity.LhssysParameters;

@Repository
public interface LhssysParametersRepository extends JpaRepository<LhssysParameters, String> {

	@Query(value = "SELECT ROWNUM SRNO, T.PARAMETER_NAME,\r\n" +
			"       T.PARAMETER_VALUE,\r\n" +
			"       T.ENTITY_CODE,\r\n" +
			"       T.SESSION_FLAG,\r\n" +
			"       T.REMARK\r\n" +
			"  FROM (SELECT S.PARAMETER_NAME,\r\n" +
			"               S.PARAMETER_VALUE,\r\n" +
			"               S.ENTITY_CODE,\r\n" +
			"               S.SESSION_FLAG,\r\n" +
			"               S.REMARK\r\n" +
			"          FROM LHSSYS_PARAMETERS S\r\n" +
			"         ORDER BY S.PARAMETER_NAME) T", nativeQuery = true)
	public List<LhssysParameters> getAllParameters();

}
