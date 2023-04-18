package com.lhs.taxcpcAdmin.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.BankAuditAttach;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;

public interface LhssysTaxcpcBankAuditService {

	/// amam

	Map<String, String> getEntityList();

	long getCount(FilterDTO filter);

	List<LhssysTaxcpcBankAuditMast> getAppBroseList(HttpSession session, FilterDTO filter, DataGridDTO dataGridDTO,
			String recPerPage);

	String viewAppAuditDashboard(int seq_id);

	////

	LhssysTaxcpcBankAuditMast getAuditbyCode();

	LhssysTaxcpcBankAuditMast saveTaxcpcBankAuditMast(LhssysTaxcpcBankAuditMast entity);

	void savelogo(BankAuditAttach bankattach);

	List<LhssysTaxcpcBankAuditMast> getBankAuditMastList();

	LhssysTaxcpcBankAuditMast getEditList(Integer seq_id);

	String getfileName(Integer seq_id);

	String UpdateForm(LhssysTaxcpcBankAuditMast entity1, Integer seq_id, LhssysTaxcpcBankAuditMast entity,
			BankAuditAttach bankentity);

	LhssysTaxcpcBankAuditMast saveTaxcpcBankAuditMastOther(LhssysTaxcpcBankAuditMast entity);

	BankAuditAttach getentitylistbycode(Integer seq_id);

	LhssysTaxcpcBankAuditMast getAuditDetails(Integer seq_id);

	BankAuditAttach savedFile(BankAuditAttach entityFile);

	List<LhssysTaxcpcBankAuditMast> getAppAuditList();

	//// sneha

	String updateReviewForm(String userCode, int seq_id, LhssysTaxcpcBankAuditMast entity,BankAuditAttach bankentity);

	BankAuditAttach getReviewEntitylistbycode(Integer seq_id);

	LhssysTaxcpcBankAuditMast saveSRCAuditDetail(LhssysTaxcpcBankAuditMast entity);

	BankAuditAttach saveattachDetail(BankAuditAttach bankattachsrc);

	LhssysTaxcpcBankAuditMast getEditSourceCodeList(Integer seq_id);

	String getfile(Integer seq_id);

	Map<String, String> getProjectMast();

	//int getCountTable(String entity_code, String project_name, String audit_type);

	//List<LhssysTaxcpcBankAuditMast> getApplicationListFilter(String entity_code, String project_name,
	//		String audit_type);

}
