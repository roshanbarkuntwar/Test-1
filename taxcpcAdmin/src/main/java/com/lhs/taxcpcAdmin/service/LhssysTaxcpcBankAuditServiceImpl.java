package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.AppAuditRepository;
import com.lhs.taxcpcAdmin.dao.AppAuditRepositorySupport;
import com.lhs.taxcpcAdmin.dao.BankAuditAttachRepository;
import com.lhs.taxcpcAdmin.dao.EntityMastRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.BankAuditAttach;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;

@Service
@Transactional
public class LhssysTaxcpcBankAuditServiceImpl implements LhssysTaxcpcBankAuditService {

	@Autowired
	private GlobalDoWorkExecuter executer;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	private AppAuditRepositorySupport BankAuditRepoSupport;

	@Autowired
	private AppAuditRepository appAuditRepository;

	@Autowired
	private EntityMastRepository entityMastRepository;

	@Autowired
	private BankAuditAttachRepository bankRepo;
	
	@Autowired
	private RequisitionServiceImpl requisitionServiceImpl;

	@Override
	public long getCount(FilterDTO filter) {

		return BankAuditRepoSupport.getCountOfBankAudit(filter);
	}

	@Override
	public Map<String, String> getEntityList() {
		Map<String, String> List = new HashMap<>();
		try {
			String queryStr = "select entity_code, entity_name  from  entity_mast ";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				List.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (List != null && List.size() > 0) ? List : null;
	}

	@Override
	public Map<String, String> getProjectMast() {
		Map<String, String> List = new HashMap<>();
		try {
			String queryStr = "select project_code, project_name  from  project_mast ";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				List.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (List != null && List.size() > 0) ? List : null;
	}
	@Override
	public List<LhssysTaxcpcBankAuditMast> getAppBroseList(HttpSession session, FilterDTO filter,
			DataGridDTO dataGridDTO, String recPerPage) {
		System.out.println("recPerPage=====" + recPerPage);
		List<LhssysTaxcpcBankAuditMast> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
		System.out.println("dataGridDTO.getPaginator()......................" + dataGridDTO.getPaginator());
		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
//			System.out.println("paginatorEntity.getRecordsPerPage()).." + paginatorEntity.getRecordsPerPage());
			if (paginatorEntity != null) {
				System.out.println("here");
				long minVal = paginatorEntity.getMinVal();
				System.out.println("minVal.." + minVal);
//				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())  ? Long.parseLong(paginatorEntity.getRecordsPerPage()) : paginatorEntity.getTotal());
				long maxVal = Long.parseLong(paginatorEntity.getRecordsPerPage());

				System.out.println("minVal.." + minVal);
				System.out.println("maxVal.." + maxVal);

//				UserMast userMast = (UserMast) session.getAttribute("LOGIN_USER");
				list = BankAuditRepoSupport.getAppBroseList(filter, minVal, maxVal);
//				System.out.println("list==" + list);
			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public String viewAppAuditDashboard(int seq_id) {

		System.out.println("seq_id>>" + seq_id);
		StringBuilder sb = new StringBuilder();
		String audit_type = "";
		String audit_status = "";
		String entity_name = "";
		String projectName = "";
		String resolveBy ="";
		LhssysTaxcpcBankAuditMast entity = new LhssysTaxcpcBankAuditMast();

		entity = appAuditRepository.findBySeqId1(seq_id);
		entity_name = entityMastRepository.getEntityName(entity.getEntity_code());
		projectName = requisitionServiceImpl.getProjectName(entity.getAudit_applied_app_name());
		resolveBy = requisitionServiceImpl.getUserName(entity.getAudit_resolve_by());
		sb.append("<div>");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-12\">");
		
		sb.append("<table  class=\"table details-table\">");
		sb.append("<tbody>");

		if (entity != null) {

			if (entity_name != null) {
				sb.append("<tr><td class=\"text-bold pre-wrap\">Entity Name  </td><td>" + entity_name + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold pre-wrap\">Entity Name  </td><td></td></tr>");
			}
			if (entity.getAudit_type() != null) {
				if (entity.getAudit_type().equalsIgnoreCase("VA")) {
					audit_type = "Vulnerability Assessment";
				}
				if (entity.getAudit_type().equalsIgnoreCase("APS")) {
					audit_type = "AppSec";
				}
				if (entity.getAudit_type().equalsIgnoreCase("SC")) {
					audit_type = "Source Code";
				}
				if (entity.getAudit_type().equalsIgnoreCase("OT")) {
					audit_type = "Other";
				}
				if (entity.getAudit_type().equalsIgnoreCase("BP")) {
					audit_type = "Best Practice";
				}
				sb.append("<tr><td class=\"text-bold\">Audit Type  </td><td class=\"pre-wrap\">" + audit_type + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Type  </td><td></td></tr>");
			}
			if (entity.getAudit_name() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Name  </td><td  class=\"pre-wrap\">" + entity.getAudit_name() + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Name  </td><td></td></tr>");
			}
			if (entity.getAudit_description() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Discription  </td><td  class=\"pre-wrap\" style=\"white-space: pre-wrap;\" >" + entity.getAudit_description()
						+ "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Discription  </td><td></td></tr>");
			}
			if (entity.getAudit_ext_link() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit External Link  </td><td class=\"pre-wrap\" style=\"white-space: pre-wrap;\">" + entity.getAudit_ext_link()
						+ "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Enternal Link </td><td></td></tr>");
			}
			if (entity.getAudit_applied_server_ip() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Applied Server IP </td><td>"
						+ entity.getAudit_applied_server_ip() + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Applied Server IP  </td><td></td></tr>");
			}
			if (entity.getAudit_applied_region() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Applied Region  </td><td>"
						+ entity.getAudit_applied_region() + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Applied Region  </td><td></td></tr>");
			}
			if (projectName != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Applied App Name  </td><td>"
						+ projectName + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Applied App Name  </td><td></td></tr>");
			}

			if (entity.getOther_info1() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Other Info1  </td><td class=\"pre-wrap\" style=\"white-space: pre-wrap;\">" + entity.getOther_info1()+ "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Other Info1 </td><td></td></tr>");
			}
			if (entity.getOther_info2() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Other Info2  </td><td class=\"pre-wrap\" style=\"white-space: pre-wrap;\">" + entity.getOther_info2()
						+ "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Other Info2 </td><td></td></tr>");
			}
			if (entity.getAudit_resolve_status() != null) {
				if (entity.getAudit_resolve_status().equalsIgnoreCase("U")) {

					audit_status = "Unresolved";
				} else if (entity.getAudit_resolve_status().equalsIgnoreCase("R")) {

					audit_status = "Resolved";
				}
				sb.append("<tr><td class=\"text-bold\">Audit Resolve Status </td><td>" + audit_status + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve Status  </td><td></td></tr>");
			}
//			if(entity.getAudit_resolve_status().equalsIgnoreCase("R")) {

			if (entity.getAudit_resolve_by() != null && entity.getAudit_resolve_status().equalsIgnoreCase("R"))  {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve By  </td><td>" + entity.getAudit_resolve_by()
						+ "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve By  </td><td></td></tr>");
			}
			if (entity.getAudit_resolve_remark() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve Remark  </td><td class=\"pre-wrap\" style=\"white-space: pre-wrap;\">"
						+ entity.getAudit_resolve_remark() + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve Remark  </td><td></td></tr>");
			}
			if (entity.getAudit_resolve_reference() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve Reference  </td><td class=\"pre-wrap\" style=\"white-space: pre-wrap;\">"
						+ entity.getAudit_resolve_reference() + "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve Reference  </td><td></td></tr>");
			}
//			}
			if (entity.getLastupdate() != null) {
				sb.append("<tr><td class=\"text-bold\">Audit Last Update  </td><td>" + entity.getLastupdate()
						+ "</td></tr>");
			} else {
				sb.append("<tr><td class=\"text-bold\">Audit Resolve Last Update  </td><td></td></tr>");
			}
		}

		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();
	}

	////////////////

	@Override
	public LhssysTaxcpcBankAuditMast getAuditbyCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LhssysTaxcpcBankAuditMast saveTaxcpcBankAuditMast(LhssysTaxcpcBankAuditMast entity) {
		String response = "error";
		LhssysTaxcpcBankAuditMast Entity = new LhssysTaxcpcBankAuditMast();
		try {

			Entity = appAuditRepository.save(entity);
			System.out.println("Entity====..." + Entity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Entity;
	}

	public List<LhssysTaxcpcBankAuditMast> getBankAuditMastList() {

		List<LhssysTaxcpcBankAuditMast> list = new ArrayList<LhssysTaxcpcBankAuditMast>();

		try {

			list = appAuditRepository.findAll();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<LhssysTaxcpcBankAuditMast> getAppAuditList() {
		List<LhssysTaxcpcBankAuditMast> list = new ArrayList<>();

		try {
			list = appAuditRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("list==>>" + list);
		return list;
	}

	@Override
	public LhssysTaxcpcBankAuditMast getAuditDetails(Integer seq_id) {

		System.out.println("seq_id........" + seq_id);

		LhssysTaxcpcBankAuditMast list = new LhssysTaxcpcBankAuditMast();
		try {
			list = appAuditRepository.findById(seq_id).get();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public BankAuditAttach savedFile(BankAuditAttach entityFile) {
		String response = "error";
		BankAuditAttach bankAttachEntity = new BankAuditAttach();
		try {
			bankRepo.save(entityFile);

			response = "success";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bankAttachEntity;

	}

	public LhssysTaxcpcBankAuditMast getEditList(Integer seq_id) {

		LhssysTaxcpcBankAuditMast entity = new LhssysTaxcpcBankAuditMast();
		try {
			entity = appAuditRepository.findById(seq_id).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override

	public String getfileName(Integer seq_id) {
		Integer audit_mast_seq_id = seq_id;

		String filePathList = "";
		try {

			filePathList = bankRepo.getFileName(audit_mast_seq_id);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return filePathList;
	}

	@Override

	public String UpdateForm(LhssysTaxcpcBankAuditMast entity1, Integer seq_id, LhssysTaxcpcBankAuditMast entity,
			BankAuditAttach bankentity) {

		String response = "error";
		String flag1="";
		try {
			System.out.println("Entity." + entity);
			String resolve_remark = entity.getAudit_resolve_remark();
			String resolve_reference = entity.getAudit_resolve_remark();
			byte[] bFile = entity.getFile_attach().getBytes();
			String Filename = entity.getFile_attach().getOriginalFilename();
			String file = bankentity.getDoc_attach_name();
			byte[] bfile = bankentity.getAudit_doc_attach();

			Optional<LhssysTaxcpcBankAuditMast> entitylist = this.appAuditRepository.findById(seq_id);
			BankAuditAttach bankentityist = new BankAuditAttach();
			entitylist.get().setAudit_resolve_remark(resolve_remark);
			entitylist.get().setAudit_resolve_reference(resolve_reference);
			entitylist.get().setAudit_resolve_status("R");
			entitylist.get().setLastupdate(new Date());
			
			if(!file.isEmpty())
			{
				flag1 = entitylist.get().getFlag();
				if(! flag1.isEmpty()) {
					flag1=flag1+"#R";
					entitylist.get().setFlag(flag1);
				}else {
					
					entitylist.get().setFlag(flag1);
				}
			}else {
				
			  }
			
			entity1 = appAuditRepository.saveAndFlush(entitylist.get());
			bankentityist.setAudit_mast_seq_id(entitylist.get().getSeq_id());
			bankentityist.setDoc_attach_name(file);
			bankentityist.setAudit_doc_attach(bfile);
			bankentityist.setDoc_resolve_attach_name(Filename);
			bankentityist.setAudit_doc_resolve_attach(bFile);
			bankRepo.saveAndFlush(bankentityist);

			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	@Override
	public void savelogo(BankAuditAttach bankattach) {
		String response = "error";

		try {
			bankRepo.save(bankattach);

			response = "success";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	
	@Override
	public LhssysTaxcpcBankAuditMast saveTaxcpcBankAuditMastOther(LhssysTaxcpcBankAuditMast entity) {
		String response = "error";
		LhssysTaxcpcBankAuditMast Entity = new LhssysTaxcpcBankAuditMast();
		try {

			Entity = appAuditRepository.save(entity);
			System.out.println("Entity====" + Entity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Entity;
	}

	@Override

	public BankAuditAttach getentitylistbycode(Integer seq_id) {

		BankAuditAttach docEntity = new BankAuditAttach();

		Integer audit_mast_seq_id = seq_id;

		try {
			docEntity = bankRepo.getlistbyCode(audit_mast_seq_id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return docEntity;

	}

	///// sneha
	// sneh
	@Override
	public BankAuditAttach saveattachDetail(BankAuditAttach bankattachsrc) {

		String response = "error";
		BankAuditAttach bankattachsrcreview = new BankAuditAttach();
		try {
			bankRepo.save(bankattachsrc);

			response = "success";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return bankattachsrcreview;

	}


	@Override
	public LhssysTaxcpcBankAuditMast saveSRCAuditDetail(LhssysTaxcpcBankAuditMast entity) {
		// TODO Auto-generated method stub
		String response = "error";

		LhssysTaxcpcBankAuditMast listentity = new LhssysTaxcpcBankAuditMast();
		try {
			System.out.println("to be save");

			entity.setLastupdate(new Date());
			entity.setAudit_type("SC");
			entity.setAudit_resolve_status("U");
			// entity.setSeq_id(10L);

			System.out.println("before save");
			listentity = appAuditRepository.save(entity);
			// System.out.println("List Of data ===="+listentity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return listentity;
	}

	@Override
	public String updateReviewForm(String UserCode, int seq_id, LhssysTaxcpcBankAuditMast entity,
			BankAuditAttach bankentity) {
		String response = "error";
		String flag1="";
		
		try {
			System.out.println("UpdateReviewForm...");
			String resolve_remark = entity.getAudit_resolve_remark();
			String resolve_reference = entity.getAudit_resolve_reference();
			byte[] bFile = entity.getFile_attach().getBytes();
			String Filename = entity.getFile_attach().getOriginalFilename();
			String file = bankentity.getDoc_attach_name();
			byte[] bfile = bankentity.getAudit_doc_attach();

			Optional<LhssysTaxcpcBankAuditMast> entitylist = this.appAuditRepository.findById(seq_id);
			BankAuditAttach bankentitylist = new BankAuditAttach();

			entitylist.get().setAudit_resolve_remark(resolve_remark);
			entitylist.get().setAudit_resolve_reference(resolve_reference);
			entitylist.get().setAudit_resolve_by(UserCode);
			entitylist.get().setAudit_resolve_status("R");
			entitylist.get().setLastupdate(new Date());

			appAuditRepository.saveAndFlush(entitylist.get());

            
			if(! file.isEmpty()) {
				flag1 = entitylist.get().getFlag();
				if(! flag1.isEmpty()) {
					flag1=flag1+"#R";
					entitylist.get().setFlag(flag1);
				}else {
					
					entitylist.get().setFlag(flag1);
				}
			}else {
				
			}

			
			appAuditRepository.saveAndFlush(entitylist.get());

			bankentitylist.setAudit_mast_seq_id(entitylist.get().getSeq_id());
			bankentitylist.setDoc_attach_name(file);
			bankentitylist.setAudit_doc_attach(bfile);
			bankentitylist.setDoc_resolve_attach_name(Filename);
			bankentitylist.setAudit_doc_resolve_attach(bFile);
			bankentitylist.setLastupdate(new Date());
			bankRepo.saveAndFlush(bankentitylist);

	
			response = "success";
			} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	@Override
	public BankAuditAttach getReviewEntitylistbycode(Integer seq_id) {
		BankAuditAttach docEntity = new BankAuditAttach();

		try {
			docEntity = bankRepo.getlistbyCode(seq_id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return docEntity;

	}

	@Override
    public LhssysTaxcpcBankAuditMast getEditSourceCodeList(Integer seq_id) {
            
            LhssysTaxcpcBankAuditMast list = new LhssysTaxcpcBankAuditMast();
            try {
                    list = appAuditRepository.findById(seq_id).get();
                    
            } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
            }
            return list;
    }

	@Override
	public String getfile(Integer seq_id) {
		Integer audit_mast_seq_id = seq_id;

		String filePathList = "";
		try {

			filePathList = bankRepo.getFile(audit_mast_seq_id);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return filePathList;
	}

//	@Override
//	public int getCountTable(String entity_code, String project_name, String audit_type) {
//		int count=0;
//		try {
//			
//
//				String Query = "select count(*) from lhssys_taxcpc_bank_audit_mast t where ";
//				if (!entity_code.isEmpty()) {
//					Query = Query + "entity_code='"+entity_code+"'";
//				}
//				if (!project_name.isEmpty()) {
//					Query = Query + " audit_applied_app_name='"+ project_name +"'";
//				}
//				if (!audit_type.isEmpty()) {
//					Query = Query + " audit_type ='"+ audit_type +"'";
//				}
//				System.out.println("count in filter1======="+Query);
//
//				count = executer.getRowCount(Query);
//             System.out.println("count in filter======="+count);
//			
//		} catch (Exception e) {
//		e.printStackTrace();	// TODO: handle exception
//		}// TODO Auto-generated method stub
//		return count;
//	}
//
//	@Override
//	public List<LhssysTaxcpcBankAuditMast> getApplicationListFilter(String entity_code, String project_name,
//			String audit_type) {
//		
//			List<LhssysTaxcpcBankAuditMast> list = new ArrayList<>();
//			String Query = "";
//			String queryString="";
//			try {
//				
//				if (!entity_code.isEmpty()) {
//					queryString = queryString + "entity_code='"+entity_code+"'";
//				}
//				if (!project_name.isEmpty()) {
//					queryString = queryString + " audit_applied_app_name='"+ project_name +"'";
//				}
//				if (!audit_type.isEmpty()) {
//					queryString = queryString + " and  audit_type ='"+ audit_type +"'";
//				}
//			      queryString = "select  t.seq_id,\r\n" + 
//						"        (select a.entity_name from entity_mast a where a.entity_code=t.entity_code)entity_code,\r\n" + 
//						"        t.audit_type,\r\n" + 
//						"        t.audit_name,\r\n" + 
//						"        t.audit_description,\r\n" + 
//						"        t.audit_ext_link,\r\n" + 
//						"        t.audit_applied_server_ip,\r\n" + 
//						"        t.audit_applied_region,\r\n" + 
//						"        (select a.project_name from project_mast a where a.project_code=t.audit_applied_app_name)audit_applied_app_name,\r\n" + 
//						"        t.other_info,\r\n" +
//						"        t.other_info1,\r\n" +
//						"        t.other_info2,\r\n" +
//						"        t.audit_resolve_status,\r\n" + 
//						"        t.audit_resolve_by,\r\n" + 
//						"        t.audit_resolve_remark,\r\n" + 
//						"        t.audit_resolve_reference,\r\n" + 
//						"        t.user_code,\r\n" + 
//						"        t.no_of_occurences,\r\n" +
//						"        t.other_categaory,\r\n" +
//						"        t.lastupdate,\r\n" + 
//						"        t.flag from lhssys_taxcpc_bank_audit_mast t where 1=1 "+sb+" order by t.lastupdate desc ";
//				
//				
//				System.out.println("count in filter111======="+Query);
//	
//				list = executer.executeSQLQuery8(Query);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		
//		
//		
//		// TODO Auto-generated method stub
//		return list;
//	}


}
