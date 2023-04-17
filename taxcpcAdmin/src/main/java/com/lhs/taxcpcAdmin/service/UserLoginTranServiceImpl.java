package com.lhs.taxcpcAdmin.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.UserLoginTranRepository;
import com.lhs.taxcpcAdmin.dao.UserLoginTranRepositorySupport;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;
import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorEntity;
import com.lhs.taxcpcAdmin.global.pagination.PaginatorManipulation;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;

@Service
@Transactional
public class UserLoginTranServiceImpl implements UserLoginTranService {

	@Autowired
	private UserLoginTranRepository clientLoginRepo;

	@Autowired
	private UserLoginTranRepositorySupport userRepoSupport;

	@Autowired
	private LhsStringUtility strUtl;

	@Autowired
	GlobalDoWorkExecuter executer;

	public Long save(UserMast userMast, HttpServletRequest request) {
		Long generatedClientSeq = null;
		try {
			String ipAddress = request.getRemoteAddr();
			InetAddress addr = InetAddress.getByName(ipAddress);
			String host = addr.getHostName();
			Timestamp timestamp = new Timestamp(new Date().getTime());
			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
			Browser browser = userAgent.getBrowser();
			String browserNameString = browser.getGroup().getName() + " " + userAgent.getBrowserVersion();
			String osName = userAgent.getOperatingSystem().getName();

			UserLoginTran clientLoginTran = new UserLoginTran();
// clientLoginTran.setEntity_code(clientMast.getEntity_code());
// clientLoginTran.setClient_code(clientMast.getClient_code());
			clientLoginTran.setLogin_time(timestamp);
			clientLoginTran.setMachine_name(host);
			clientLoginTran.setMachine_ip(ipAddress);
			clientLoginTran.setMachine_browser(browserNameString);
			clientLoginTran.setMachine_os_name(osName);
			clientLoginTran.setUser_code(userMast.getUser_code());
			clientLoginTran.setFlag("D");

			UserLoginTran savedTranObj = clientLoginRepo.save(clientLoginTran);
			generatedClientSeq = savedTranObj.getSession_seqno();

		} catch (UnknownHostException e) {
			generatedClientSeq = null;
			e.printStackTrace();
		}
		return generatedClientSeq;
	}

	@Override
	public UserLoginTran fetchByClientSessionSeq(Long clientSessionId) throws Exception {

		return clientLoginRepo.findById(clientSessionId).get();
	}

	@Override
	public void update(UserLoginTran updateTranObj) throws Exception {
		clientLoginRepo.save(updateTranObj);
	}

	@Override
	public List<UserLoginTran> getLoginListMinToMax() {
		List<UserLoginTran> list = new ArrayList<UserLoginTran>();
		try {
			list = clientLoginRepo.getListBetweenMinToMaxValue();
		} catch (Exception e) {
// TODO: handle exception
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<UserLoginTran> getUserLoginBrowseList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage) {

		List<UserLoginTran> list = new ArrayList<>();

		String datagrid = "";
		PaginatorManipulation manipulation = new PaginatorManipulation();
// System.out.println("dataGridDTO.getPaginator()..." +
// dataGridDTO.getPaginator());

		try {

			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {
				long minVal = paginatorEntity.getMinVal() + 1L;
				System.out.println(
						"dataGridDTO.getPaginator().getPageNumber().." + dataGridDTO.getPaginator().getPageNumber());
				long maxVal = Long.parseLong(recPerPage) * (dataGridDTO.getPaginator().getPageNumber());

				System.out.println("mxvl........" + maxVal);
				System.out.println("minVal.." + minVal);
				System.out.println("maxVal.." + maxVal);

				list = userRepoSupport.getUserLoginBrowseList(filter, minVal, maxVal);

			} else {
// System.out.println("Object is null..");
			}

		} catch (Exception e) {
// TODO: handle exception
		}
		recPerPage = "";

		return list;
	}

	@Override
	public int getLoginDetailsCount(FilterDTO filter) {
// TODO Auto-generated method stub
		return userRepoSupport.getUserTranBrowseCount(filter);
	}

	@Override
	public List<UserLoginTran> getUserLoginDetailBrowserList(FilterDTO filter, DataGridDTO dataGridDTO,
			String recPerPage, String user_code) {
// TODO Auto-generated method stub

		List<UserLoginTran> list = new ArrayList<>();
		PaginatorManipulation manipulation = new PaginatorManipulation();
// System.out.println("dataGridDTO.getPaginator()..." +
// dataGridDTO.getPaginator());
		String datagrid = "";
		try {

			PaginatorEntity paginatorEntity = manipulation.getCalculatedValue(dataGridDTO.getPaginator());
			if (paginatorEntity != null) {

				long minVal = paginatorEntity.getMinVal() + 1;

				long maxVal = Long.parseLong(recPerPage) * dataGridDTO.getPaginator().getPageNumber();

				System.out.println("minVal.." + minVal);
				System.out.println("maxVal.." + maxVal);

				list = userRepoSupport.getUserLoginNewBrowserList(filter, minVal, maxVal, user_code);

			} else {
// System.out.println("Object is null..");
			}

		} catch (Exception e) {
// TODO: handle exception
		}
		recPerPage = "";
		return list;
	}

	@Override
	public int getLoginDetailsListCount(FilterDTO filter, String user_code) {
// TODO Auto-generated method stub
		return userRepoSupport.getUserTranDeatilBrowserCount(filter, user_code);
	}

	@Override
	public int getcount(String login_id, String user_name, String user_code) {

		int count = 0;

		try {
              String Query = "select count(*) from (select row_number() over (order by 1) SLNO,\r\n" + 
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
              		"                               group by a.user_code,a.user_name,a.login_id ) where 1=1 ";

			if (!user_name.isEmpty()) {
				Query = Query + "and  lower(user_name)like lower('%" + user_name + "%')";
			}
			if (!login_id.isEmpty()) {
				Query = Query + "and  lower(login_id)like lower('%" + login_id + "%')";
			}
			if (!user_code.isEmpty()) {
				Query = Query + "and  user_code = '" + user_code + "'";
			}

			System.out.println("Query..." + Query);

			count = executer.getRowCount(Query);

		} catch (Exception e) {
// TODO: handle exception
		}
		return count;
	}

	@Override
	public List<UserLoginTran> getSearchUserLoginDetail(String login_id, String user_name, String user_code) {

		List<UserLoginTran> list = new ArrayList<UserLoginTran>();

		String Query = "";

		try {

			Query = "SELECT b.*\r\n" + 
					"  FROM (select row_number() over(order by 1) SLNO,\r\n" + 
					"               a.login_id login_id,\r\n" + 
					"               a.user_code user_code,\r\n" + 
					"               a.user_name user_name,\r\n" + 
					"               max(a.login_time) last_login_time,\r\n" + 
					"               count(*) no_of_login_count\r\n" + 
					"          from (select v.user_code,\r\n" + 
					"                       b.user_name,\r\n" + 
					"                       b.login_id,\r\n" + 
					"                       v.login_time,\r\n" + 
					"                       count(v.login_time) over(partition by v.login_time) as a_login_time\r\n" + 
					"                  from user_login_tran v, user_mast b\r\n" + 
					"                 WHERE b.user_code = v.user_code\r\n" ;
//					"                 group by v.user_code, v.login_time, b.login_id, b.user_name) a\r\n" + 
//					"         group by a.user_code, a.user_name, a.login_id) b\r\n" + 
//					" where 1=1 ";

			if (!user_name.isEmpty()) {
				Query = Query + " and  lower(user_name)like lower('%" + user_name + "%')";
			}
			if (!login_id.isEmpty()) {
				Query = Query + " and  lower(login_id)like lower('%" + login_id + "%')";
			}
			if (!user_code.isEmpty()) {
				Query = Query + " and  b.user_code = '" + user_code + "'";
			}
Query = Query +"group by v.user_code, v.login_time, b.login_id, b.user_name) a  \r\n" + 
		"        group by a.user_code, a.user_name, a.login_id) b\r\n" + 
		" where 1=1   AND slno BETWEEN  1 AND 10";
			System.out.println("Query..." + Query);
			
			list = executer.getUserLoginDetailList(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int getcountUserList(String login_time, String user_code) {

		int count = 0;

		try {
              String Query = "SELECT count(*) FROM (select row_number() over (order by 1) SLNO,\r\n" + 
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
              		"                        from user_login_tran v,user_mast b WHERE b.user_code = v.user_code AND v.user_code ='" +user_code+"'\r\n" + 
              		"                        group by v.user_code, v.login_time,b.login_id,b.user_name) a  \r\n" + 
              		"                        group by a.user_code,a.user_name,a.login_id,a.login_time)\r\n";

			if (!login_time.isEmpty()) {
				Query = Query + "where  to_date(login_time) = to_date('" + login_time + "','DD-MM-RRRR')";
			}
//			if (!login_id.isEmpty()) {
//				Query = Query + "and  lower(login_id)like lower('%" + login_id + "%')";
//			}
//			if (!user_code.isEmpty()) {
//				Query = Query + "and  user_code = '" + user_code + "'";
//			}

			System.out.println("Query..." + Query);

			count = executer.getRowCount(Query);

		} catch (Exception e) {
// TODO: handle exception
		}
		return count;
	}

	@Override
	public List<UserLoginTran> getSearchUserList(String login_time, String user_code) {

		List<UserLoginTran> list = new ArrayList<UserLoginTran>();

		String Query = "";

		try {

			Query = "SELECT b.* FROM (select row_number() over (order by 1) SLNO,\r\n" + 
					"                      a.login_id,\r\n" + 
					" a.user_code,\r\n" + 
					"                      a.user_name,\r\n" + 
					"a.login_time,\r\n" + 
					"                      count(*) no_of_login_count\r\n" + 
					"                 from (select v.user_code,\r\n" + 
					"                               b.user_name,\r\n" + 
					"                               b.login_id,\r\n" + 
					"                              v.login_time,\r\n" + 
					"                             count(v.login_time) over(partition by v.login_time) as a_login_time\r\n" + 
					"                        from user_login_tran v,user_mast b WHERE b.user_code = v.user_code AND v.user_code ='" + user_code + "'\r\n"; 
//					"                        group by v.user_code, v.login_time,b.login_id,b.user_name) a\r\n" + 
//					"                        group by a.user_code,a.user_name,a.login_id,a.login_time)b";

			if (!login_time.isEmpty()) {
				Query = Query + "AND to_date(login_time) = to_date('" + login_time + "','DD-MM-RRRR')";
			}

			Query = Query +"group by v.user_code, v.login_time, b.login_id, b.user_name) a  \r\n" + 
					"        group by a.user_code, a.user_name, a.login_id,a.login_time) b\r\n" + 
					" where 1=1   AND slno BETWEEN  1 AND 10";
						System.out.println("Query..." + Query);
						
			System.out.println("Query..." + Query);
			
			list = executer.getUserLoginDetailNewList(Query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}