package com.lhs.taxcpcAdmin.dao;

import java.util.ArrayList;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;



@Service
@Transactional
public class UserLoginTranRepositorySupport<T> {
	
	@Autowired
	private UserLoginTranRepository repository;
	
	@Autowired
	private UserMastRepository userrepository;

	@Autowired
	GlobalDoWorkExecuter executer;


	public int getUserTranBrowseCount(FilterDTO filter){
		int count = 0 ;
	
		StringBuilder sd= new StringBuilder();
		try {
		
			String queryString = "select count(*) from (select row_number() over (order by 1) SLNO,\r\n" + 
					"                              a.login_id login_id, \r\n" + 
					"                             a.user_code user_code, \r\n" + 
					"                             a.user_name user_name,\r\n" + 
					"                             max(a.login_time) last_login_time,\r\n" + 
					"                             count(1) no_of_login_count\r\n" + 
					"                        from (select v.user_code, \r\n" + 
					"                                     b.user_name, \r\n" + 
					"                                     b.login_id,\r\n" + 
					"                                     v.login_time,\r\n" + 
					"                                    count(v.login_time) over(partition by v.login_time) as a_login_time\r\n" + 
					"                               from user_login_tran v,user_mast b WHERE b.user_code = v.user_code\r\n" + 
					"                              group by v.user_code, v.login_time,b.login_id,b.user_name) a\r\n" + 
					"                               group by a.user_code,a.user_name,a.login_id ) " ;
			
			if (filter != null) {
				if(filter.getLogin_id() != null && !filter.getLogin_id().equalsIgnoreCase("") && filter.getUser_name() != null && !filter.getUser_name().equalsIgnoreCase("") && filter.getUser_code() != null && !filter.getUser_code().equalsIgnoreCase("")) {
					queryString = queryString.concat(" WHERE lower(login_id) like lower('%"+filter.getLogin_id()+"%') AND lower(user_name) like lower('%"+filter.getUser_name()+"%') AND user_code='"+filter.getUser_code()+"'");
				}
				else if(filter.getLogin_id() != null && !filter.getLogin_id().equalsIgnoreCase("") && filter.getUser_name() != null && !filter.getUser_name().equalsIgnoreCase("")) {
					queryString = queryString.concat(" WHERE lower(login_id) like lower('%"+filter.getLogin_id()+"%') AND lower(user_name) like lower('%"+filter.getUser_name()+"%')");					
				}
				else if(filter.getUser_name() != null && !filter.getUser_name().equalsIgnoreCase("") && filter.getUser_code() != null && !filter.getUser_code().equalsIgnoreCase("")) {
					queryString = queryString.concat(" WHERE lower(login_id) like lower('%"+filter.getLogin_id()+"%') AND user_code='"+filter.getUser_code()+"'");					
				}
				
				else if(filter.getLogin_id() != null && !filter.getLogin_id().equalsIgnoreCase("") && filter.getUser_code() != null && !filter.getUser_code().equalsIgnoreCase("")) {
					queryString = queryString.concat(" WHERE lower(login_id) like lower('%"+filter.getLogin_id()+"%') AND user_code='"+filter.getUser_code()+"'");
				}
				else if(filter.getLogin_id() != null && !filter.getLogin_id().equalsIgnoreCase("")) {
					queryString = queryString.concat(" WHERE lower(login_id) like lower('%"+filter.getLogin_id()+"%')");					
				}
				else if(filter.getUser_name() != null && !filter.getUser_name().equalsIgnoreCase("")) {
					queryString = queryString.concat(" WHERE lower(user_name) like lower('%"+filter.getUser_name()+"%')");					
				}
				
				else if(filter.getUser_code() != null && !filter.getUser_code().equalsIgnoreCase("")) {
					queryString = queryString.concat(" WHERE user_code='"+filter.getUser_code()+"'");
				}
               
		     }	
			System.out.println("Count Query== "+queryString);
			
		count =	executer.getRowCount(queryString);
		System.out.println("count.."+count);
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	
	
	public List<UserLoginTran> getUserLoginBrowseList(FilterDTO filter, long minVal, long maxVal) {
	
		List<UserLoginTran> list = new ArrayList<>();
      
		String queryString = "";
		try {
			System.out.println("filter 1====="+filter);
	
			
	
				queryString ="SELECT b.* FROM (select row_number() over (order by 1) SLNO,\r\n" + 
						"                      a.login_id login_id,\r\n" + 
						"                      a.user_code user_code,\r\n" + 
						"                      a.user_name user_name,\r\n" + 
						"                      max(a.login_time) last_login_time,\r\n" + 
						"                      count(*) no_of_login_count\r\n" + 
						"                 from (select v.user_code,\r\n" + 
						"                              b.user_name,\r\n" + 
						"                              b.login_id,\r\n" + 
						"                              v.login_time,\r\n" + 
						"                             count(v.login_time) over(partition by v.login_time) as a_login_time\r\n" + 
						"                        from user_login_tran v,user_mast b WHERE b.user_code = v.user_code\r\n" ; 
//						"                        group by v.user_code, v.login_time,b.login_id,b.user_name) a  \r\n"+
//						"group by a.user_code,a.user_name,a.login_id   ) b where 1=1 ";
//			
//				queryString = queryString.concat("group by a.user_code,a.user_name,a.login_id   ) b\r\n" +
//						"where 1=1  AND slno BETWEEN "+minVal+" AND "+maxVal+""); 
				if (filter != null) {
					
					//System.out.println("Inside If .........");
					if(filter.getLogin_id() != null && !filter.getLogin_id().equalsIgnoreCase("")) {
						queryString = queryString.concat(" and lower(login_id) like lower('%"+filter.getLogin_id()+"%')");
					}
					if(filter.getUser_name() != null && !filter.getUser_name().equalsIgnoreCase("")) {
						queryString = queryString.concat(" and lower(user_name) like lower('%"+filter.getUser_name()+"%')");					
					}
					
					if(filter.getUser_code() != null && !filter.getUser_code().equalsIgnoreCase("")) {
						queryString = queryString.concat(" and b.user_code='"+filter.getUser_code()+"'");
					}

	                
			     }	
				
				queryString = queryString.concat("group by v.user_code, v.login_time, b.login_id, b.user_name) a  \r\n" + 
						"        group by a.user_code, a.user_name, a.login_id) b\r\n" + 
						" where 1=1 AND slno BETWEEN "+minVal+" AND "+maxVal+"");
				
			 System.out.println("Query........"+queryString);
				list = executer.getUserLoginDetailList(queryString);
			
		// System.out.println("List>>>>>............"+list);

		}catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("list1 size.."+list.size());
		return list;
	}

	
	public int getUserTranDeatilBrowserCount(FilterDTO filter, String user_code){
		int count = 0 ;

		try {

			String queryString = "SELECT count(*) FROM (select row_number() over (order by 1) SLNO,\r\n" + 
					"                      a.login_id,\r\n" + 
					"                      a.user_code,\r\n" + 
					"                      a.user_name,\r\n" + 
					"                      a.login_time,\r\n" + 
					"                      count(*) no_of_login_count\r\n" + 
					"                 from (select v.user_code,\r\n" + 
					"                               b.user_name,\r\n" + 
					"                               b.login_id,\r\n" + 
					"                              v.login_time,\r\n" + 
					"                             count(v.login_time) over(partition by v.login_time) as a_login_time\r\n" + 
					"                        from user_login_tran v,user_mast b WHERE b.user_code = v.user_code AND v.user_code ='" + user_code +"'\r\n" + 
					"                        group by v.user_code, v.login_time,b.login_id,b.user_name) a  \r\n" + 
					"                        group by a.user_code,a.user_name,a.login_id,a.login_time)\r\n";
					
			//queryString = queryString + "where user_code ='" + user_code + "'";
			
			if (filter != null) {
				  if (filter.getLogin_time() != null && !filter.getLogin_time().equalsIgnoreCase("")) {
					
					  queryString = queryString.concat(" where  to_date(login_time) = to_date('"+filter.getLogin_time()+"','DD-MM-RRRR')");
			        }
			}
			System.out.println("queryString..."+queryString);
		count =	executer.getRowCount(queryString);
		System.out.println("count.."+count);
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		return count;
	}
	
	

	public List<UserLoginTran> getUserLoginNewBrowserList(FilterDTO filter, long minVal,long maxVal, String user_code) 
	{
		System.out.println("User_code....."+user_code);
		System.out.println("minVal....."+minVal);
		System.out.println("maxVal....."+maxVal);
		List<UserLoginTran> list = new ArrayList<>();
	
		String Query = "";
		try {
			

			Query = "SELECT b.* FROM (select row_number() over (order by 1) SLNO,\r\n"
					+ "                      a.login_id,\r\n" + " a.user_code,\r\n"
					+ "                      a.user_name,\r\n" + "a.login_time,\r\n"
					+ "                      count(*) no_of_login_count\r\n"
					+ "                 from (select v.user_code,\r\n"
					+ "                               b.user_name,\r\n"
					+ "                               b.login_id,\r\n"
					+ "                              v.login_time,\r\n"
					+ "                             count(v.login_time) over(partition by v.login_time) as a_login_time\r\n"
					+ "                        from user_login_tran v,user_mast b WHERE b.user_code = v.user_code AND v.user_code ='" + user_code + "'";
//				    + "                        group by v.user_code, v.login_time,b.login_id,b.user_name) a\r\n"
//					+ "                        group by a.user_code,a.user_name,a.login_id,a.login_time)b ";
			

			
			if (filter != null) {
				
			  if (filter.getLogin_time() != null && !filter.getLogin_time().equalsIgnoreCase("")) {
				  Query = Query.concat(" AND to_date(login_time) = to_date('"+filter.getLogin_time()+"','DD-MM-RRRR')");
		        }

			}
			
			Query = Query.concat("group by v.user_code, v.login_time, b.login_id, b.user_name) a  \r\n" + 
					"        group by a.user_code, a.user_name, a.login_id,a.login_time) b\r\n" + 
					" where 1=1 AND slno BETWEEN "+minVal+" AND "+maxVal+"");
			

			
		    System.out.println("Query ........."+Query);
			list = executer.getUserLoginDetailNewList(Query);
				
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

	public List<UserLoginTran> getUserLastdetail(String user_code) {
		// System.out.println("User Code Printing..........." + user_code);
//		System.out.println("User Code Printing..........."+user_name);

		List<UserLoginTran> list = new ArrayList<>();

		String Query = "";
		try {
			Query = "SELECT V.*\r\n" + 
					"  FROM (SELECT ROW_NUMBER() OVER(ORDER BY 1) AS SLNO,\r\n" + 
					"               V.USER_CODE, \r\n" + 
					"               B.USER_NAME, \r\n" + 
					"               B.LOGIN_ID, \r\n" + 
					"      V.MACHINE_NAME , V.MACHINE_IP,V.MACHINE_BROWSER,V.MACHINE_OS_NAME,V.LOGIN_TIME\r\n" + 
					"          FROM USER_LOGIN_TRAN V, USER_MAST B\r\n" + 
					"         WHERE B.USER_CODE = V.USER_CODE\r\n" + 
					"           AND V.USER_CODE = '"+user_code+"'\r\n" + 
					"         ORDER BY V.LOGIN_TIME DESC)V \r\n" + 
					" WHERE 1 = 1\r\n" + 
					"   AND SLNO BETWEEN 1 AND 10";

			 System.out.println("Query ........."+Query);
			list = executer.getUserLoginDetailListx(Query);
			// System.out.println("LIst>>>>>............"+list);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;

	}

	
}
