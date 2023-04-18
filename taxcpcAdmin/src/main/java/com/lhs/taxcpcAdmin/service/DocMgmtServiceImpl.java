package com.lhs.taxcpcAdmin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhs.javautilities.LhsDateUtility;
import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.DocTranAttachRepository;
import com.lhs.taxcpcAdmin.dao.DocTranRepository;
import com.lhs.taxcpcAdmin.dao.DocTranRepositorySupport;
import com.lhs.taxcpcAdmin.dao.LhssysTaxcpcLinkMastRepository;
import com.lhs.taxcpcAdmin.dao.ProjectMastRepository;
import com.lhs.taxcpcAdmin.dao.UserMastRepository;
import com.lhs.taxcpcAdmin.dao.ViewDocCodeRepository;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.DocTranAttach;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcLinkMast;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;

@Service
@Transactional
public class DocMgmtServiceImpl implements DocMgmtService {

	@Autowired
	DocTranRepository docRepo;

	@Autowired
	DocTranAttachRepository docAttachRepo;

	@Autowired
	LhssysTaxcpcLinkMastRepository linkMastRepo;

	@Autowired
	GlobalDoWorkExecuter executer;

	@Autowired
	DocTranRepositorySupport docTranSupp;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	private LhsDateUtility lhsDateUtility;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	ProjectMastRepository projectMastRepository;

	@Autowired
	UserMastRepository UserMastRepo;

	@Autowired
	ViewDocCodeRepository viewRepo;

	@Override
	public DocTran saveDocDetail(DocTran entity) {
		String response = "error";
		DocTran docEntity = new DocTran();

		try {

			docEntity = docRepo.save(entity);
			response = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docEntity;
	}

	@Override
	public Map<String, String> getDocTypeList() {
		// TODO Auto-generated method stub
		Map<String, String> docTypelist = new HashMap<>();
		try {
			String queryStr = "select doc_type_code, doc_type_name from view_doc_type";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object[] obj : result) {
				docTypelist.put((String) obj[0], (String) obj[1]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (docTypelist != null && docTypelist.size() > 0) ? docTypelist : null;
	}

	@Override
	public List<DocTran> getQuickDocList(String userCode) {
		// TODO Auto-generated method stub
		List<DocTran> quickDocList = new ArrayList<DocTran>();
		try {
			quickDocList = docRepo.getQuickDocList(userCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (quickDocList != null && quickDocList.size() > 0) ? quickDocList : null;
	}

	@Override
	public void saveDocAttachment(DocTranAttach docAttachEntity) {
		String response = "error";
		try {
			docAttachRepo.save(docAttachEntity);

			response = "success";

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public String deleteDocument(String doc_code) {
		String response = "error";
		try {

			String query = "delete from doc_tran where doc_code=:d_code";
			Query nativeQuery = entityManager.createNativeQuery(query, DocTran.class);
			nativeQuery.setParameter("d_code", doc_code);
			int i = nativeQuery.executeUpdate();
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<String> getImportantLinkTypeList(String user_type) {
		// TODO Auto-generated method stub
		List<String> linkTypelist = new ArrayList<>();
		try {
			String queryStr = "select link_type from view_taxcpc_link_type where user_type = '" + user_type
					+ "' or user_type is null";
			List<Object[]> result = executer.executeSQLQueryAsList(queryStr);
			for (Object obj : result) {
				linkTypelist.add((String) obj);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (linkTypelist != null && linkTypelist.size() > 0) ? linkTypelist : null;
	}

	@Override
	public String saveImpUrlDetail(LhssysTaxcpcLinkMast entity) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			entity.setLastupdate(new Date());
			linkMastRepo.save(entity);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public List<LhssysTaxcpcLinkMast> getLinkDetailByType(String link_type) {
		// TODO Auto-generated method stub
		List<LhssysTaxcpcLinkMast> impLinkList = new ArrayList<>();
		try {
			impLinkList = linkMastRepo.getDetailByLinkType(link_type);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return impLinkList;
	}

	@Override
	public LhssysTaxcpcLinkMast getLinkDetailByCode(String linkCode) {
		// TODO Auto-generated method stub
		LhssysTaxcpcLinkMast entity = new LhssysTaxcpcLinkMast();
		try {
			entity = linkMastRepo.findById(linkCode).get();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String deleteImpUrl(String linkCode) {
		// TODO Auto-generated method stub
		String response = "error";
		try {
			linkMastRepo.deleteById(linkCode);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String viewLinkDetail(String linkCode) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		try {
			LhssysTaxcpcLinkMast entity = getLinkDetailByCode(linkCode);
			if (entity != null) {
				sb.append("<tr><td class=\"text-bold\">Link Type: </td><td class=\"pre-wrap\">" + entity.getLink_type()
						+ "</td></tr>");
				sb.append("<tr><td class=\"text-bold\">Link : </td><td class=\"pre-wrap\">" + entity.getLink()
						+ "</td></tr>");
				sb.append("<tr><td class=\"text-bold\">Description : </td><td class=\"pre-wrap\">"
						+ entity.getLink_description() + "</td></tr>");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public List<DocTran> getDocDetails(String user_code) {
		// TODO Auto-generated method stub

		List<DocTran> docList = new ArrayList<DocTran>();
		try {
			docList = docRepo.getDocList(user_code);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (docList != null && docList.size() > 0) ? docList : null;
	}

	@Override
	public Long getDocDetailsCount(FilterDTO filter) {
		// TODO Auto-generated method stub
		return docTranSupp.getDocTranBrowseCount(filter);
	}

	@Override
	public List<DocTran> getDocDetailsList(FilterDTO filter, DataGridDTO dataGridDTO, Map<String, String> docTypeList,
			Map<String, String> projectList, Map<String, String> userList, String recPerPage, long total) {

		List<DocTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();

		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {

				long minVal = paginatorEntity.getMinVal();

				long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage())
						&& !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL"))
								? Long.parseLong(paginatorEntity.getRecordsPerPage())
								: paginatorEntity.getTotal();

				list = docTranSupp.getDocTranBrowseList(filter, minVal, maxVal, docTypeList, projectList, userList);

			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public DocTran getDocDetailbydocCode(String doc_code) {
		// TODO Auto-generated method stub
		DocTran docEntity = new DocTran();
		String response = "error";
		try {
			docEntity = docRepo.getDocDetailbydocCode(doc_code);
			response = "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return docEntity;
	}

	public String getProjectName(String projectCode) {
		String Project_code = "";
		try {
			Project_code = projectMastRepository.getProjectName(projectCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Project_code;
	}

	public String getUserName(String userCode) {
		String User_code = "";
		try {
			User_code = UserMastRepo.getUserName(userCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return User_code;
	}

	public String getDocName(String Code) {
		String code = "";
		try {
			code = viewRepo.getCode(Code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	@Override
	public String viewDocTran(String docCode) {
		StringBuilder sb = new StringBuilder();
		String Project_code = "";
		String User_code = "";
		String doctype = "";
		try {
			DocTran entity = getDocDetailbydocCode(docCode);
			Project_code = getProjectName(entity.getProject_code());
			User_code = getUserName(entity.getUser_code());
			doctype = getDocName(entity.getDoc_type_code());
			if (entity != null) {
				String remark = entity.getRemark() != null ? entity.getRemark() : "";
				if (doctype != null) {
					sb.append("<tr><td class=\"text-bold\">Document Type </td><td>" + doctype + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Document Type  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getDoc_name() != null) {
					sb.append("<tr><td class=\"text-bold\">Document Name  </td><td class=\"pre-wrap\">"
							+ entity.getDoc_name() + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Document Name  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (Project_code != null) {
					sb.append("<tr><td class=\"text-bold\">Application Name  </td><td class=\"pre-wrap\">"
							+ Project_code + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Application Name  </td><td class=\"pre-wrap\"></td></tr>");
				}

				if (User_code != null) {
					sb.append("<tr><td class=\"text-bold\">Prepared By </td><td class=\"pre-wrap\">" + User_code
							+ "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Prepared By </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getRefer_link() != null) {
					sb.append("<tr><td class=\"text-bold\">Refer Link  </td><td class=\"pre-wrap\">"
							+ entity.getRefer_link() + "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Refer Link </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getRemark() != null) {
					sb.append("<tr><td class=\"text-bold\">Remark  </td><td class=\"pre-wrap\">" + entity.getRemark()
							+ "</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Remark  </td><td class=\"pre-wrap\"></td></tr>");
				}
				if (entity.getLastupdate() != null) {
					sb.append("<tr><td class=\"text-bold\">Last Update  </td><td class=\"pre-wrap\">")
							.append(lhsDateUtility.getFormattedDate(entity.getLastupdate(), "dd-MM-yyyy HH:mm:ss.SS"))
							.append("</td></tr>");
				} else {
					sb.append("<tr><td class=\"text-bold\">Last Update</td><td class=\"pre-wrap\"></td></tr>");
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public String getFilePath(String doc_code) {

		String filePathList = "";
		try {
			filePathList = docRepo.getFilePathList(doc_code);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return filePathList;
	}

	@Override
	public List<DocTran> getDocDetailEntity() {

		List<DocTran> list = new ArrayList<>();
		try {
			list = docRepo.findAll();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public List<DocTranAttach> getDocDetailAttachList() {
		List<DocTranAttach> list = new ArrayList<>();

		try {
			list = docAttachRepo.findAll();
		}

		catch (Exception e) {
			e.printStackTrace();

		}
		return list;
	}

	@Override
	public String getDocAttachfile(String doc_code) {
		String filePathList = "";
		try {

			filePathList = docAttachRepo.getFileName(doc_code);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return filePathList;
	}

	@Override
	public DocTranAttach getdocCode(String doc_code) {
		DocTranAttach docEntity = new DocTranAttach();
		try {
			docEntity = docAttachRepo.getdocCode(doc_code);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return docEntity;
	}

	@Override
	public byte[] getfileData(String doc_code) {
		byte[] file = null;

		try {

			file = docAttachRepo.getFile(doc_code);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return file;
	}

	@Override
	public int getcountTable(String doc_code_type, String doc_name, String user_code, String project_code) {
		int count = 0;
		try {

			String Query = "select count(*) from doc_tran where doc_mode='O'";
			if (!doc_code_type.isEmpty()) {
				Query = Query + " and  lower(doc_type_code)like lower('%" + doc_code_type + "%')";
			}
			if (!doc_name.isEmpty()) {
				Query = Query + " and  lower(doc_name)like lower('%" + doc_name + "%')";
			}
			if (!project_code.isEmpty()) {
				Query = Query + " and  lower(project_code)like lower('%" + project_code + "%')";
			}
			if (!user_code.isEmpty()) {
				Query = Query + " and  lower(user_code)like lower('%" + user_code + "%')";
			}
			count = executer.getRowCount(Query);

		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public List<DocTran> getEntityListFilter(String doc_code_type, String doc_name, String user_code,
			String project_code) {
		List<DocTran> list = new ArrayList<>();
		String Query = "";
		try {
			Query = "select * from doc_tran where doc_mode='O'";
			if (!doc_code_type.isEmpty()) {
				Query = Query + " and  lower(doc_type_code)like lower('%" + doc_code_type + "%')";
			}
			if (!doc_name.isEmpty()) {
				Query = Query + " and  lower(doc_name)like lower('%" + doc_name + "%')";
			}
			if (!project_code.isEmpty()) {
				Query = Query + " and  lower(project_code)like lower('%" + project_code + "%')";
			}
			if (!user_code.isEmpty()) {
				Query = Query + " and  lower(user_code)like lower('%" + user_code + "%')";
			}
			list = executer.executeSQLQueryDocTran(Query);
			
			System.out.println("Query in doc..."+Query);

		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return list;
	}

	@Override
	public long getquickDocDetailsCount(FilterDTO filter, String userCode) {

		return docTranSupp.getQuickDocTranBrowseCount(filter, userCode);

	}

	@Override
	public List<DocTran> getQuickDocDetailsList(FilterDTO filter, DataGridDTO dataGridDTO,
			Map<String, String> docTypeList, Map<String, String> menuList, String recPerPage, long total,
			String userCode) {
		List<DocTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();

		try {
			PaginatorEntity paginatorEntity = manipulation.getCalculatedValueDoc(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {

				long minVal = paginatorEntity.getMinVal();

				
				  long maxVal = (!strUtl.isNull(paginatorEntity.getRecordsPerPage()) &&
				  !paginatorEntity.getRecordsPerPage().equalsIgnoreCase("ALL")) ?
				  Long.parseLong(paginatorEntity.getRecordsPerPage()) :
				  paginatorEntity.getTotal();
			
				// long maxVal = 8;

				list = docTranSupp.getQuickDocTranBrowseList(filter, minVal, maxVal, docTypeList, menuList, userCode);

			} else {
				System.out.println("Object is null..");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	@Override
	public int getcountQuickTable(String doc_name) {
		int count = 0;
		try {

			String Query = "select count(*) from doc_tran where doc_mode='Q'";
			
			if (!doc_name.isEmpty()) {
				Query = Query + " and  lower(doc_name)like lower('%" + doc_name + "%')";
			}
			
			System.out.println("Query======="+Query);

			count = executer.getRowCount(Query);

		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
		// TODO Auto-generated method stub
		return count;
	}

	public List<DocTran> getEntityListQuickFilter(String doc_name) {
			List<DocTran> list = new ArrayList<>();
			String Query = "";
			try {
				Query = "select * from doc_tran where doc_mode='Q'";
				
				if (!doc_name.isEmpty()) {
					Query = Query + " and  lower(doc_name)like lower('%" + doc_name + "%')";
					
					System.out.println("Query======="+Query);
				}
				
				
				list = executer.executeSQLQueryDocTran(Query);

			} catch (Exception e) {
				// TODO: handle exception
			}
			// TODO Auto-generated method stub
			return list;
	}


}