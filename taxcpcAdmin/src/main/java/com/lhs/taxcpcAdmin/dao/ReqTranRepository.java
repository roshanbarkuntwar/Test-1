package com.lhs.taxcpcAdmin.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lhs.taxcpcAdmin.dao.generic.GenericCustomRepository;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;

@Repository
public interface ReqTranRepository extends JpaRepository<ReqTran, String>, GenericCustomRepository<String,ReqTran>{

	@Query("from ReqTran t where t.reported_by = :user_code order by t.reported_date desc")
	List<ReqTran> findByReportedUser(String user_code);
	
	@Query("from ReqTran t where t.issue_type = :dept_str and t.approved_by_status is null order by t.reported_date desc")
	List<ReqTran> findByIssueType(String dept_str);
	
	@Query("from ReqTran t where t.approved_by_status is null order by t.reported_date desc")
	List<ReqTran> findAllRequisition();

	@Query("from ReqTran t where t.assigned_to_dev = :user_code order by t.assigned_dev_date desc")
	List<ReqTran> findByAssignedToDev(String user_code);

	@Query("from ReqTran t where t.assigned_to_qc = :user_code order by t.assigned_qc_date desc")
	List<ReqTran> findByAssignedToQc(String user_code);
	
	@Query("from ReqTran t where t.req_code = :reqCode ")
	ReqTran findByReqCode(String reqCode);

	@Query(value="select max(to_number(req_code)) from req_tran", nativeQuery = true)
	String getMaxCount();
	
	@Query(value="select remark_count from req_tran where req_code=?1 ", nativeQuery = true)
	int getRemarkCount(String req_code);
	
	@Query(value="select reopened_remark from req_tran where req_code=?1 ", nativeQuery = true)
	String getReopenRemark(String req_code);
	
	@Query(value="select dev_remark from req_tran where req_code=?1 ", nativeQuery = true)
	String getDevRemark(String req_code);
	
	@Query(value="select reinitiate_pm_work_remark from req_tran where req_code=?1 ", nativeQuery = true)
	String getPmWorkRemark(String req_code);

	@Query(value="select issue_description from req_tran  where req_code=:parentReqCode", nativeQuery = true)
	String getParentReqDescription(String parentReqCode);
	
	@Query(value="select client_final_status from req_tran where req_code=?1 ", nativeQuery = true)
	String getClientFinalStatus(String req_code);

	@Query(value = "select t.lastupdate from req_tran t where t.req_code=:req_code" , nativeQuery = true)
	Date getlastUpdateDate(String req_code);

	@Query(value = "select status_name from view_current_req_status  where status_code=:status_code", nativeQuery = true)
	String getCurrentStatus(String status_code);
	
	@Query(value = "select * from req_tran where req_entered_mode='E' and req_entered_mode!='O'  and req_entered_mode!='I'", nativeQuery = true)
	List<ReqTran> getClientReq();
	
	@Query(value="select * from req_tran where (req_entered_mode='E' Or req_entered_mode='I') and req_entered_mode!='O'", nativeQuery = true)
	List<ReqTran> getListOfEntries();


}
