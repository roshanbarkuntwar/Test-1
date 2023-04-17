/**
 * 
 */
package com.lhs.taxcpcAdmin.dao.generic;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.model.entity.AppDetails;
import com.lhs.taxcpcAdmin.model.entity.ClientAppDetails;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.DocTran;
import com.lhs.taxcpcAdmin.model.entity.EntityMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbDetails;
import com.lhs.taxcpcAdmin.model.entity.LhssysDbObjects;
import com.lhs.taxcpcAdmin.model.entity.LhssysDefaultEntityClient;
import com.lhs.taxcpcAdmin.model.entity.LhssysMainTables;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcBankAuditMast;
import com.lhs.taxcpcAdmin.model.entity.LhssysTaxcpcDeploymentTran;
import com.lhs.taxcpcAdmin.model.entity.ProjectMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectMenuMast;
import com.lhs.taxcpcAdmin.model.entity.ProjectModuleMast;
import com.lhs.taxcpcAdmin.model.entity.ReqTran;
import com.lhs.taxcpcAdmin.model.entity.ServerDetailModel;
import com.lhs.taxcpcAdmin.model.entity.TaxcpcWishlistPendingWork;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;
import com.lhs.taxcpcAdmin.model.entity.UserMenuMast;

/**
 * @author ayushi.jain
 *
 */
@Component
public class GlobalDoWorkExecuter extends HibernateConfiguration {

	@Autowired
	private LhsStringUtility strUtil;

	@PersistenceContext
	private EntityManager entityManager;



	
	int count = 0;
	public int getRowCount(String query) 
	{
		Session ssn = getSession();
		try 
		{
			Work work = new Work() 
			{
				@Override
				public void execute(Connection connection) throws SQLException 
				{
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try 
					{
						pstmt = connection.prepareStatement(query);
						rs = pstmt.executeQuery();
						while (rs != null && rs.next()) {
							count = rs.getInt(1);
						}

					} catch (Exception e)
					{
						e.printStackTrace();
					} finally
					{
						try 
						{
							if (pstmt != null) 
							{
								pstmt.close();
							}
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

			};
			// ssn.beginTransaction();
			ssn.doWork(work);
			// ssn.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return count;
	}//End Method
	
	
	public <T> ArrayList<T> getGenericList(final T object, final String l_query) {
		final ArrayList<T> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@SuppressWarnings("unchecked")
				@Override
				public void execute(Connection cnctn) throws SQLException {
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(l_query);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							boolean record_fetched = true;
							T result_data = null;
							try {
								result_data = (T) object.getClass().newInstance();
							} catch (InstantiationException ex) {
								Logger.getLogger(GlobalDoWorkExecuter.class.getName()).log(Level.SEVERE, null, ex);
							} catch (IllegalAccessException ex) {
								Logger.getLogger(GlobalDoWorkExecuter.class.getName()).log(Level.SEVERE, null, ex);
							}
							try {
								Field[] tim_tbl_flds = result_data.getClass().getDeclaredFields();
								for (Field fld : tim_tbl_flds) {
									String fld_name = fld.getName();
									String fld_value = rs.getString(fld_name);
									fld_value = strUtil.isNull(fld_value) ? "" : fld_value;
									fld.set(result_data, fld_value);
								} // end for
							} catch (SecurityException e) {
								record_fetched = false;
								e.printStackTrace();
							} catch (SQLException e) {
								record_fetched = false;
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								record_fetched = false;
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								record_fetched = false;
								e.printStackTrace();
							}
							if (record_fetched) {
								list.add(result_data);
							}
						} // end while
					} catch (SQLException ex) {
					} finally {
						if (pstmt != null) {
							pstmt.close();
						}
						if (rs != null) {
							rs.close();
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}// End Method

	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public <T> T getGenericWorkInterfaceExecuter(final T entity, final String para_query) {
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					try {
						pstmt = cnctn.prepareStatement(para_query);
						rs = pstmt.executeQuery(para_query);
						while (rs.next()) {
							Field[] all_dtls = entity.getClass().getDeclaredFields();
							for (Field dtl : all_dtls) {
								try {
									String detail_name = dtl.getName();
									String detail_value = rs.getString(detail_name);
									detail_value = strUtil.isNull(detail_value) ? "" : detail_value;
									dtl.set(entity, detail_value);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} // end for
						}
					} catch (SQLException ex) {
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) entity;
	}// End Method

	List<Object[]> list = null;

	public List<Object[]> executeSQLQueryAsListWithBeginEnd(String query, String callableString) {
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection con) throws SQLException {
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						con.prepareCall(callableString);
						pstmt = con.prepareStatement(query);

						rs = pstmt.executeQuery();
						ResultSetMetaData rsmd = rs.getMetaData();
						int column_count = rsmd.getColumnCount();
						while (rs.next()) {
							Object[] objArr = new Object[column_count];
							for (int i = 0; i < column_count; i++) {
								objArr[i] = rs.getString(i + 1);
							}
							list.add(objArr);
						}
					} catch (SQLException ex) {
						listOfListFunctionOutput = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};

			list = new ArrayList<Object[]>();
			ssn.doWork(work);
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		}
		return list;
	}// End Method

	ArrayList<ArrayList<String>> listOfListFunctionOutput = null;

	public ArrayList<ArrayList<String>> executeSQLQueryAsListOfList(final String para_query) {
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(para_query);
						rs = pstmt.executeQuery(para_query);
						ResultSetMetaData rsmd = rs.getMetaData();
						int column_count = rsmd.getColumnCount();
						while (rs.next()) {
							ArrayList<String> arr = new ArrayList<String>();
							for (int i = 0; i < column_count; i++) {
								String col_value = rs.getString(i + 1);
								col_value = (col_value == null) ? "" : col_value;
								arr.add(col_value);
							}
							listOfListFunctionOutput.add(arr);
						}
					} catch (SQLException ex) {
						listOfListFunctionOutput = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};

			listOfListFunctionOutput = new ArrayList<>();
			ssn.doWork(work);
		} catch (Exception e) {
			listOfListFunctionOutput = null;
			e.printStackTrace();
		}
		return listOfListFunctionOutput;
	}// End Method

	public Object getSingleValueFromSQLQuery(String query) {
		try {
			return entityManager.createNativeQuery(query).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}// End Method

	public Object getSingleValueFromQuery(String query) {
		try {
			return entityManager.createQuery(query).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}// End Method

	@Transactional
	public int updateSQLQuery(String sqlQuery) {
		try {
			return entityManager.createNativeQuery(sqlQuery).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}// End Method

	@Transactional
	public int updateQuery(String hqlQuery) {
		try {
			return entityManager.createQuery(hqlQuery).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}// End Method

	@SuppressWarnings("unchecked")
	public List<Object[]> executeSQLQueryAsList(String sqlQuery) {
		try {
			return (List<Object[]>) entityManager.createNativeQuery(sqlQuery).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}// End Method



	public List<ReqTran> executeSQLQuery(String para_query) {
		List<ReqTran> reqList1 = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<ReqTran> reqList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(para_query);
						rs = pstmt.executeQuery(para_query);
						while (rs.next()) {
							ReqTran reqtran = new ReqTran();
							reqtran.setReq_code(rs.getString("req_code"));
							reqtran.setReported_by(rs.getString("reported_by"));
							reqtran.setProject_name(rs.getString("project_name"));
							reqtran.setModule_name(rs.getString("module_name"));
							reqtran.setMenu_name(rs.getString("menu_name"));
							reqtran.setReported_date(rs.getDate("reported_date"));
							reqtran.setIssue_type(rs.getString("issue_type"));
							reqtran.setReq_error_group_str(rs.getString("req_error_group_str"));
							reqtran.setSample_data_filter_str(rs.getString("sample_data_filter_str"));
							reqtran.setIssue_description(rs.getString("issue_description"));
							reqtran.setReq_priority(rs.getString("req_priority"));
							reqtran.setCurrent_req_status(rs.getString("current_req_status"));
							reqtran.setAssigned_to_dev(rs.getString("assigned_to_dev"));
							reqtran.setAssigned_dev_date(rs.getDate("assigned_dev_date"));
							reqtran.setApproved_by(rs.getString("approved_by"));
							reqtran.setApproved_by_status(rs.getString("approved_by_status"));
							reqtran.setDev_status(rs.getString("dev_status"));
							reqtran.setReopened_status(rs.getString("reopened_status"));
							reqtran.setReq_type(rs.getString("req_type"));
							reqtran.setFinal_status(rs.getString("final_status"));
							reqtran.setClient_final_status(rs.getString("client_final_status"));
							reqtran.setReinitiate_pm_work_flag(rs.getString("reinitiate_pm_work_flag"));
							reqtran.setReinitiate_pm_work_remark(rs.getString("reinitiate_pm_work_remark"));
							reqtran.setReinitiate_work_assigned_to(rs.getString("reinitiate_work_assigned_to"));
							reqtran.setDeploy_fail_req_assigned_to(rs.getString("deploy_fail_req_assigned_to"));
							reqtran.setReq_entered_mode(rs.getString("req_entered_mode"));
							reqtran.setDeploy_delivered_date(rs.getDate("deploy_delivered_date"));
							reqtran.setReported_date(rs.getDate("reported_date"));
							

							reqList.add(reqtran);
						}
						reqList1.addAll(reqList);
						rs.close();

					} catch (SQLException ex) {
						reqList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqList1;
	}// End Method

	
	public List<LhssysTaxcpcDeploymentTran> executeSQLQuery1(String para_query) {
		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<LhssysTaxcpcDeploymentTran> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(para_query);
						rs = pstmt.executeQuery(para_query);
						while (rs.next()) {
							LhssysTaxcpcDeploymentTran deploymentTran = new LhssysTaxcpcDeploymentTran();

							deploymentTran.setSeq_id(rs.getLong("seq_id"));
							deploymentTran.setProject_code(rs.getString("project_code"));
							deploymentTran.setWar_filename(rs.getString("war_filename"));
							deploymentTran.setSample_data_filter_str(rs.getString("sample_data_filter_str"));
							deploymentTran.setRemark(rs.getString("remark"));
							deploymentTran.setServer_url(rs.getString("server_url"));
							deploymentTran.setLastupdate(rs.getDate("lastupdate"));
							deploymentTran.setFlag(rs.getString("flag"));
							deploymentTran.setDepl_code(rs.getString("depl_code"));

							list1.add(deploymentTran);
						}
						list.addAll(list1);
						rs.close();

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}// End Method

	public List<TaxcpcWishlistPendingWork> executeSQLQuery2(String query) {
		List<TaxcpcWishlistPendingWork> entitylist = new ArrayList<>();

		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<TaxcpcWishlistPendingWork> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							TaxcpcWishlistPendingWork taxcpcwish = new TaxcpcWishlistPendingWork();
							taxcpcwish.setWork_code(rs.getLong("Work_code"));
							taxcpcwish.setWork_type(rs.getString("Work_type"));
							taxcpcwish.setWork_nature(rs.getString("Work_nature"));
							taxcpcwish.setWork_priority(rs.getString("work_priority"));
							taxcpcwish.setProject_code(rs.getString("project_code"));
							taxcpcwish.setCompletion_date(rs.getString("completion_date"));
							taxcpcwish.setUser_code(rs.getString("user_code"));
							taxcpcwish.setWork_description(rs.getString("work_description"));
							taxcpcwish.setStatus(rs.getString("status"));
							taxcpcwish.setExternal_link(rs.getString("external_link"));
							taxcpcwish.setRemark(rs.getString("remark"));
							taxcpcwish.setLastupdate(rs.getDate("lastupdate"));
							taxcpcwish.setFlag(rs.getString("flag"));
							list1.add(taxcpcwish);
						}
						entitylist.addAll(list1);
						rs.close();

						

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (List<TaxcpcWishlistPendingWork>) entitylist;
	}

	public List<ProjectModuleMast> executeSQLQuery3(String query) {
		List<ProjectModuleMast> projModList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<ProjectModuleMast> projList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							ProjectModuleMast projectModuleMast = new ProjectModuleMast();

							projectModuleMast.setModule_code(rs.getString("module_code"));
							projectModuleMast.setModule_name(rs.getString("module_name"));
							projectModuleMast.setRemark(rs.getString("remark"));
							projectModuleMast.setProject_code(rs.getString("project_code"));
							projectModuleMast.setLastupdate(rs.getDate("lastupdate"));
							projectModuleMast.setFlag(rs.getString("flag"));
							projectModuleMast.setModule_status(rs.getString("module_status"));
							projList.add(projectModuleMast);
						}
						projModList.addAll(projList);
						rs.close();

					} catch (SQLException ex) {
						projList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projModList;
	}// End Method

	public List<ProjectMast> executeSQLQuery4(String query) {
		List<ProjectMast> projectmastList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<ProjectMast> projList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							ProjectMast projectMast = new ProjectMast();

							projectMast.setProject_code(rs.getString("project_code"));
							projectMast.setProject_name(rs.getString("project_name"));
							projectMast.setProject_info(rs.getString("project_info"));
							projectMast.setProject_type(rs.getString("project_type"));
							projectMast.setApplication_type(rs.getString("application_type"));
							projectMast.setArchitecture_type_code(rs.getString("architecture_type_code"));
							projectMast.setFrontend_type_code(rs.getString("frontend_type_code"));
							projectMast.setBackend_type_code(rs.getString("backend_type_code"));
							projectMast.setDatabase_type_code(rs.getString("database_type_code"));
							projectMast.setFramework_type_code(rs.getString("framework_type_code"));
							projectMast.setSvn_link(rs.getString("svn_link"));
							projectMast.setWar_filename(rs.getString("war_filename"));
							projectMast.setOther_war_filename(rs.getString("other_war_filename"));
							projectMast.setDomain_code(rs.getString("domain_code"));
							projectMast.setParent_code(rs.getString("parent_code"));
							projectMast.setProject_status(rs.getString("project_status"));
							projectMast.setRemark(rs.getString("remark"));
							projectMast.setLastupdate(rs.getDate("lastupdate"));
							projectMast.setFlag(rs.getString("flag"));
							projectMast.setConnected_db_user(rs.getString("connected_db_user"));
							projList.add(projectMast);
						}
						projectmastList.addAll(projList);
						rs.close();

					} catch (SQLException ex) {
						projList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectmastList;
	}// End Method

	public List<ProjectMenuMast> executeSQLQuery5(String query) {
		List<ProjectMenuMast> menuList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<ProjectMenuMast> menumastList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							ProjectMenuMast projectMenuMast = new ProjectMenuMast();

							projectMenuMast.setMenu_code(rs.getString("menu_code"));
							projectMenuMast.setMenu_name(rs.getString("menu_name"));
							projectMenuMast.setMenu_description(rs.getString("menu_description"));
							projectMenuMast.setModule_code(rs.getString("module_code"));
							projectMenuMast.setProject_code(rs.getString("project_code"));
							projectMenuMast.setParent_menu_code(rs.getString("parent_menu_code"));
							projectMenuMast.setSub_menu_type(rs.getString("sub_menu_type"));
							projectMenuMast.setMenu_type(rs.getString("menu_type"));
							projectMenuMast.setMenu_status(rs.getString("menu_status"));
							projectMenuMast.setLastupdate(rs.getDate("lastupdate"));
							projectMenuMast.setFlag(rs.getString("flag"));
							menumastList.add(projectMenuMast);
						}
						menuList.addAll(menumastList);
						rs.close();

					} catch (SQLException ex) {
						menumastList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuList;
	}// End Method

	public List<ServerDetailModel> executeSQLQuery6(String para_query) {
		List<ServerDetailModel> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				/**
				 *
				 */
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<ServerDetailModel> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(para_query);
						rs = pstmt.executeQuery(para_query);
						while (rs.next()) {
							ServerDetailModel serverDetailModel = new ServerDetailModel();
							serverDetailModel.setConfiguration_type(rs.getString("configuration_type"));
							serverDetailModel.setApp_server_name(rs.getString("app_server_name"));
							serverDetailModel.setTag_name(rs.getString("tag_name"));
							serverDetailModel.setServer_ip(rs.getString("server_ip"));
							serverDetailModel.setPublic_ip(rs.getString("public_ip"));
							serverDetailModel.setHost_name(rs.getString("host_name"));
							serverDetailModel.setApp_server_port(rs.getString("app_server_port"));
							serverDetailModel.setApp_server_tag_name(rs.getString("app_server_tag_name"));
							serverDetailModel.setInstalled_db(rs.getString("installed_db"));
							serverDetailModel.setInstalled_db_tool(rs.getString("installed_db_tool"));
							serverDetailModel.setServer_id(rs.getString("server_id"));
							serverDetailModel.setEntity_code(rs.getString("entity_code"));
							serverDetailModel.setServer_owner_name(rs.getString("server_owner_name"));
							serverDetailModel.setApp_server_ip(rs.getString("app_server_ip"));
							
							
							list1.add(serverDetailModel);
//							System.out.println("list1="+list1);
						}
						list.addAll(list1);
						rs.close();

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}// End Method

	private ArrayList<String> resultSetColumnHeadings = null;

	public ArrayList<String> getResultSetColumnHeadings(final String nativeQuery) {
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(nativeQuery);
						rs = pstmt.executeQuery(nativeQuery);
						ResultSetMetaData resultSetMetaData = rs.getMetaData();
						int columns = resultSetMetaData.getColumnCount();
						for (int i = 1; i <= columns; i++) {
							resultSetColumnHeadings.add(resultSetMetaData.getColumnName(i));
						}
					} catch (SQLException ex) {
						resultSetColumnHeadings = null;
					} finally {
						if (pstmt != null) {
							pstmt.close();
						}
						if (rs != null) {
							rs.close();
						}
					}
				}
			};
			resultSetColumnHeadings = new ArrayList<String>();
			ssn.doWork(work);
		} catch (Exception e) {
			resultSetColumnHeadings = null;
			System.out.println(e.getMessage());
		}
		return resultSetColumnHeadings;
	}// end method

	public Map<String, String> sortTheList(Map<String, String> map) {
		Map<String, String> sortedMap = map.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));

		return sortedMap;
	}

	public Map<String, String> sortList(Map<String, String> map) {
		Map<String, String> sortedMap = map.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));

		return sortedMap;
	}



	
	
	public List<EntityMast> executeSQLQueryEntity(String query) {
		
		List<EntityMast> List = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<EntityMast> entityList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							EntityMast entity = new EntityMast();
							entity.setEntity_code(rs.getString("entity_code"));
							entity.setEntity_name(rs.getString("entity_name"));	
							entity.setDivision(rs.getString("division"));
							entity.setCity(rs.getString("city"));
							entity.setPin(rs.getString("pin"));
							entity.setPhone(rs.getString("phone"));
							entity.setEmail(rs.getString("email"));
							entity.setWebsite(rs.getString("website"));
							entity.setAdd3(rs.getString("add3"));
							entity.setDb_user(rs.getString("db_user"));
							
							entityList.add(entity);
						}
						List.addAll(entityList);
						rs.close();

					} catch (SQLException ex) {
						entityList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return List;
	}// End Method
		

	
public List<UserMenuMast> executeSQLQueryMenuList(String query) {
		
		List<UserMenuMast> List = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<UserMenuMast> entityList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							UserMenuMast entity = new UserMenuMast();
							entity.setMenu_name(rs.getString("menu_name"));
							entity.setMenu_code(rs.getString("menu_code"));

							entityList.add(entity);
						}
						List.addAll(entityList);
						rs.close();

					} catch (SQLException ex) {
						entityList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return List;
	}// End Method
		

	
	public List<ClientDetails> executeSQLQueryClientDeatils(String query) {
		List<ClientDetails> clientList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<ClientDetails> clientDetailList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							ClientDetails clientDetailMast = new ClientDetails();
							
							clientDetailMast.setEntity_code(rs.getString("entity_code"));
							clientDetailMast.setEntity_name(rs.getString("entity_name"));
							clientDetailMast.setTeam_name(rs.getString("team_name"));
							clientDetailMast.setResp_person_name(rs.getString("resp_person_name"));
							clientDetailMast.setResp_person_email_id(rs.getString("resp_person_email_id"));
							clientDetailMast.setResp_person_mobileno(rs.getString("resp_person_mobileno"));
							clientDetailMast.setBranch_city(rs.getString("branch_city"));
							clientDetailList.add(clientDetailMast);
						}
						clientList.addAll(clientDetailList);
						rs.close();

					} catch (SQLException ex) {
						clientDetailList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientList;
	}// End Method
	
	
	public List<ClientAppDetails> executeSQLQueryClientAppDetils(String query) {
		List<ClientAppDetails> clientAppList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<ClientAppDetails> clientAppDetailList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							ClientAppDetails clientAppDetailMast = new ClientAppDetails();
							
							clientAppDetailMast.setClient_code(rs.getString("client_code"));
							clientAppDetailMast.setApplication_type(rs.getString("application_type"));
							clientAppDetailMast.setApp_name(rs.getString("app_name"));
							clientAppDetailMast.setApp_url(rs.getString("app_url"));
							clientAppDetailMast.setConnected_db_username(rs.getString("connected_db_username"));
							clientAppDetailMast.setConnected_db_pwd(rs.getString("connected_db_pwd"));
							clientAppDetailMast.setConnected_db_sid(rs.getString("connected_db_sid"));
							clientAppDetailMast.setConnected_port(rs.getString("connected_port"));
							clientAppDetailMast.setConnected_db_remark(rs.getString("connected_db_remark"));
							clientAppDetailMast.setLastupdate(rs.getDate("lastupdate"));
							clientAppDetailMast.setRemark(rs.getString("remark"));
							
							clientAppDetailList.add(clientAppDetailMast);
						}
						clientAppList.addAll(clientAppDetailList);
						rs.close();

					} catch (SQLException ex) {
						clientAppDetailList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientAppList;
	}// End Method
	
	
	public List<UserMast> executeSQLQueryUserMastDeatils(String query) {
		List<UserMast> userList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<UserMast> userDetailList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							UserMast user = new UserMast();
//							ClientDetails clientDetailMast = new ClientDetails();
							
						     user.setLoginId(rs.getString("login_id"));
							user.setUser_code(rs.getString("user_code"));
							user.setUser_name(rs.getString("user_name"));
							user.setCreated_date(rs.getDate("created_date"));
							user.setRole_code(rs.getString("role_code"));
							userList.add(user);
							userDetailList.add(user);
						}
						userList.addAll(userDetailList);
						rs.close();

					} catch (SQLException ex) {
						userDetailList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}// End Method


	public int updateSQLQuery1(String Query) {
		try {
			return entityManager.createNativeQuery(Query).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}// End Method		return 0;

	public List<LhssysTaxcpcDeploymentTran> executeSQLQuery7(String query) {
		List<LhssysTaxcpcDeploymentTran> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<LhssysTaxcpcDeploymentTran> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							LhssysTaxcpcDeploymentTran entity = new LhssysTaxcpcDeploymentTran();
							
							entity.setSeq_id(rs.getLong("seq_id"));
							entity.setProject_code(rs.getString("project_code"));
							entity.setDepl_code(rs.getString("depl_code"));
							entity.setWar_filename(rs.getString("war_filename"));
							entity.setLastupdate(rs.getDate("lastupdate"));
							
							list1.add(entity);
//							System.out.println("list1="+list1);
						}
						list.addAll(list1);
						rs.close();

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<LhssysDbObjects> getLhssysDbObjectList(String query) {
		List<LhssysDbObjects> databaseList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<LhssysDbObjects> databaseDetailList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							LhssysDbObjects LhssysDbObjectsDetailMast = new LhssysDbObjects();
							
							LhssysDbObjectsDetailMast.setDb_object_name(rs.getString("db_object_name"));
							LhssysDbObjectsDetailMast.setDb_password(rs.getString("db_password"));
							LhssysDbObjectsDetailMast.setType_of_database(rs.getString("type_of_database"));
							LhssysDbObjectsDetailMast.setDb_objects_use(rs.getString("db_objects_use"));
							LhssysDbObjectsDetailMast.setObject_type(rs.getString("object_type"));
							LhssysDbObjectsDetailMast.setRemark(rs.getString("remark"));
							LhssysDbObjectsDetailMast.setStatus(rs.getString("status"));
							LhssysDbObjectsDetailMast.setLast_ddl_time(rs.getDate("last_ddl_time"));
							databaseDetailList.add(LhssysDbObjectsDetailMast);
						}
						databaseList.addAll(databaseDetailList);
						rs.close();

					} catch (SQLException ex) {
						databaseDetailList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return databaseList;
	}
	
	public List<LhssysDbDetails> getLhssysDetailList(String query) {
		List<LhssysDbDetails> databaseList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<LhssysDbDetails> databaseDetailList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							LhssysDbDetails LhssysDbDetailMast = new LhssysDbDetails();
							
							LhssysDbDetailMast.setType_of_db(rs.getString("type_of_db"));
							LhssysDbDetailMast.setDb_port(rs.getString("db_port"));
							LhssysDbDetailMast.setDb_sid(rs.getString("db_sid"));
							LhssysDbDetailMast.setDb_ip(rs.getString("db_ip"));
							LhssysDbDetailMast.setRemark(rs.getString("remark"));
							LhssysDbDetailMast.setDb_code(rs.getString("db_code"));
							LhssysDbDetailMast.setLastupdate(rs.getDate("lastupdate"));
							databaseDetailList.add(LhssysDbDetailMast);
						}
						databaseList.addAll(databaseDetailList);
						rs.close();

					} catch (SQLException ex) {
						databaseDetailList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return databaseList;
	}

	public List<LhssysMainTables> getLhssysMainTableList(String query) {
		List<LhssysMainTables> databaseMainList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<LhssysMainTables> databaseMainTableList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							LhssysMainTables LhssysMainDetailMast = new LhssysMainTables();
							
							LhssysMainDetailMast.setTable_or_view_name(rs.getString("table_or_view_name"));
							LhssysMainDetailMast.setObject_type(rs.getString("object_type"));
							LhssysMainDetailMast.setProject_code(rs.getString("project_code"));
							LhssysMainDetailMast.setRemark(rs.getString("remark"));
							LhssysMainDetailMast.setObject_code(rs.getString("object_code"));
							LhssysMainDetailMast.setLastupdate(rs.getDate("lastupdate"));
							databaseMainTableList.add(LhssysMainDetailMast);
						}
						databaseMainList.addAll(databaseMainTableList);
						rs.close();

					} catch (SQLException ex) {
						databaseMainTableList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return databaseMainList;
	}

	public List<UserLoginTran> getUserLoginDetailList(String query) {
		List<UserLoginTran> UserLoginTranList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<UserLoginTran> UserLoginTranTableList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							UserLoginTran UserLoginTranMast = new UserLoginTran();
							
//							UserLoginTranMast.setSession_seqno(rs.getLong("session_seqno"));
							UserLoginTranMast.setLogin_id(rs.getString("login_id"));
							UserLoginTranMast.setUser_name(rs.getString("user_name"));

							UserLoginTranMast.setLogin_time(rs.getTimestamp("last_login_time"));
				//		    UserLoginTranMast.setLogin_time(rs.getTimestamp("last_login"));
//							UserLoginTranMast.setLogout_time(rs.getTimestamp("login_time"));
//							UserLoginTranMast.setLogout_method(rs.getString("logout_method"));
//							UserLoginTranMast.setMachine_name(rs.getString("machine_name"));
//							UserLoginTranMast.setMachine_ip(rs.getString("machine_ip"));
//							UserLoginTranMast.setMachine_browser(rs.getString("machine_browser"));
//							UserLoginTranMast.setMachine_os_name(rs.getString("machine_os_name"));
//							UserLoginTranMast.setMachine_other_info(rs.getString("machine_other_info"));
							UserLoginTranMast.setUser_code(rs.getString("user_code"));
//							UserLoginTranMast.setLastupdate(rs.getDate("lastupdate"));
							UserLoginTranMast.setLogin_count(rs.getString("no_of_login_count"));
//							UserLoginTranMast.setFlag(rs.getString("flag"));
							UserLoginTranTableList.add(UserLoginTranMast);
						}
						UserLoginTranList.addAll(UserLoginTranTableList);
						rs.close();

					} catch (SQLException ex) {
						UserLoginTranTableList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return UserLoginTranList;
	}
	
	public List<UserLoginTran> getUserLoginDetailNewList(String query) {
		List<UserLoginTran> UserLoginTranList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<UserLoginTran> UserLoginTranTableList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							UserLoginTran UserLoginTranMast = new UserLoginTran();
							
//							UserLoginTranMast.setSession_seqno(rs.getLong("session_seqno"));
							UserLoginTranMast.setLogin_id(rs.getString("login_id"));
							UserLoginTranMast.setUser_name(rs.getString("user_name"));
						UserLoginTranMast.setLogin_time(rs.getTimestamp("login_time"));
//						UserLoginTranMast.setLogin_time(rs.getTimestamp("last_login_time"));
//							UserLoginTranMast.setLogout_time(rs.getTimestamp("login_time"));
//							UserLoginTranMast.setLogout_method(rs.getString("logout_method"));
//							UserLoginTranMast.setMachine_name(rs.getString("machine_name"));
//							UserLoginTranMast.setMachine_ip(rs.getString("machine_ip"));
//							UserLoginTranMast.setMachine_browser(rs.getString("machine_browser"));
//							UserLoginTranMast.setMachine_os_name(rs.getString("machine_os_name"));
//							UserLoginTranMast.setMachine_other_info(rs.getString("machine_other_info"));
							UserLoginTranMast.setUser_code(rs.getString("user_code"));
//							UserLoginTranMast.setLastupdate(rs.getDate("lastupdate"));
							UserLoginTranMast.setLogin_count(rs.getString("no_of_login_count"));
//							UserLoginTranMast.setFlag(rs.getString("flag"));
							UserLoginTranTableList.add(UserLoginTranMast);
						}
						UserLoginTranList.addAll(UserLoginTranTableList);
						rs.close();

					} catch (SQLException ex) {
						UserLoginTranTableList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return UserLoginTranList;
	}

	public List<LhssysTaxcpcBankAuditMast> executeSQLQuery8(String query) {
		List<LhssysTaxcpcBankAuditMast> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<LhssysTaxcpcBankAuditMast> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							LhssysTaxcpcBankAuditMast entity = new LhssysTaxcpcBankAuditMast();

							entity.setSeq_id(rs.getInt("seq_id"));
							entity.setEntity_code(rs.getString("entity_code"));
							entity.setAudit_type(rs.getString("audit_type"));
							entity.setAudit_name(rs.getString("Audit_name"));
							entity.setAudit_description(rs.getString("audit_description"));
							entity.setAudit_ext_link(rs.getString("audit_ext_link"));
							entity.setAudit_applied_server_ip(rs.getString("audit_applied_server_ip"));
							entity.setAudit_applied_region(rs.getString("audit_applied_region"));
							entity.setAudit_applied_app_name(rs.getString("audit_applied_app_name"));
							entity.setOther_info(rs.getString("other_info"));
							entity.setOther_info1(rs.getString("other_info1"));
							entity.setOther_info2(rs.getString("other_info2"));
							entity.setAudit_resolve_status(rs.getString("audit_resolve_status"));
							entity.setAudit_resolve_by(rs.getString("audit_resolve_by"));
							entity.setAudit_resolve_remark(rs.getString("audit_resolve_remark"));
							entity.setAudit_resolve_reference(rs.getString("audit_resolve_reference"));
							entity.setUser_code(rs.getString("user_code"));
							entity.setLastupdate(rs.getDate("lastupdate"));
							
							
							list1.add(entity);
//							System.out.println("list1="+list1);
						}
						list.addAll(list1);
						rs.close();

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public List<DocTran> executeSQLQueryDocTran(String query) {
		List<DocTran> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<DocTran> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							DocTran entity = new DocTran();

							entity.setDoc_type_code(rs.getString("doc_type_code"));
							entity.setDoc_name(rs.getString("doc_name"));
							entity.setProject_code(rs.getString("project_code"));
							entity.setUser_code(rs.getString("user_code"));
							entity.setLastupdate(rs.getDate("lastupdate"));
							
							
							list1.add(entity);
//							System.out.println("list1="+list1);
						}
						list.addAll(list1);
						rs.close();

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	
	
	
	public List<UserLoginTran> getUserLoginDetailListx(String query) {
		List<UserLoginTran> UserLoginTranList = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<UserLoginTran> UserLoginTranTableList = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							UserLoginTran UserLoginTranMast = new UserLoginTran();
							
//							UserLoginTranMast.setSession_seqno(rs.getLong("session_seqno"));
							UserLoginTranMast.setLogin_id(rs.getString("login_id"));
							UserLoginTranMast.setUser_name(rs.getString("user_name"));

						//	UserLoginTranMast.setLogin_time(rs.getTimestamp("last_login_time"));
						    UserLoginTranMast.setLogin_time(rs.getTimestamp("login_time"));
//							UserLoginTranMast.setLogout_time(rs.getTimestamp("logout_time"));
//							UserLoginTranMast.setLogout_method(rs.getString("logout_method"));
							UserLoginTranMast.setMachine_name(rs.getString("machine_name"));
							UserLoginTranMast.setMachine_ip(rs.getString("machine_ip"));
							UserLoginTranMast.setMachine_browser(rs.getString("machine_browser"));
							UserLoginTranMast.setMachine_os_name(rs.getString("machine_os_name"));
//							UserLoginTranMast.setMachine_other_info(rs.getString("machine_other_info"));
							UserLoginTranMast.setUser_code(rs.getString("user_code"));
//							UserLoginTranMast.setLastupdate(rs.getDate("lastupdate"));
							//UserLoginTranMast.setLogin_count(rs.getString("no_of_login_count"));
//							UserLoginTranMast.setFlag(rs.getString("flag"));
							UserLoginTranTableList.add(UserLoginTranMast);
						}
						UserLoginTranList.addAll(UserLoginTranTableList);
						rs.close();

					} catch (SQLException ex) {
						UserLoginTranTableList = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return UserLoginTranList;
	}


	public List<LhssysDefaultEntityClient> executeSQLQueryDefaultEntity(String query) {
		List<LhssysDefaultEntityClient> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<LhssysDefaultEntityClient> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							LhssysDefaultEntityClient entity = new LhssysDefaultEntityClient();

							entity.setEntity_code(rs.getString("entity_code"));
							entity.setClient_code(rs.getString("client_code"));
							entity.setClient_name(rs.getString("client_name"));
							entity.setNo_of_transaction(rs.getString("no_of_transaction"));
							entity.setInitiate_date(rs.getDate("initiate_date"));
							entity.setParent_code(rs.getString("parent_code"));
							entity.setClosed_date(rs.getDate("closed_date"));
							entity.setClosed_remark(rs.getString("closed_remark"));
							entity.setLastupdate_from_fgs(rs.getDate("lastupdate_from_fgs"));
							entity.setLastupdate(rs.getDate("lastupdate"));
							
							
							list1.add(entity);
//							System.out.println("list1="+list1);
						}
						list.addAll(list1);
						rs.close();

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public List<AppDetails> executeSQLQueryAppDetail(String query) {
		List<AppDetails> list = new ArrayList<>();
		Session ssn = getSession();
		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					List<AppDetails> list1 = new ArrayList<>();
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = cnctn.prepareStatement(query);
						rs = pstmt.executeQuery(query);
						while (rs.next()) {
							AppDetails entity = new AppDetails();

							entity.setEntity_code(rs.getString("entity_code"));
							entity.setApplication_type("application_type");
							entity.setProject_code(rs.getString("project_code"));
							entity.setServer_ip(rs.getString("server_ip"));
							entity.setApp_server_ip(rs.getString("app_server_ip"));
							entity.setApp_server_name(rs.getString("app_server_name"));
							entity.setJar_name(rs.getString("jar_name"));
							entity.setLastupdate(rs.getDate("lastupdate"));
							
							
							list1.add(entity);
//							System.out.println("list1="+list1);
						}
						list.addAll(list1);
						rs.close();

					} catch (SQLException ex) {
						list1 = null;
						ex.printStackTrace();
					} finally {
						try {
							if (pstmt != null) {
								pstmt.close();
							}
						} catch (Exception e) {
						}
						try {
							if (rs != null) {
								rs.close();
							}
						} catch (Exception e) {
						}
					}
				}
			};
			ssn.doWork(work);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	};
	
	
}


	

		
			
// End Class
