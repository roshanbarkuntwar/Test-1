/**
 * 
 */
package com.lhs.taxcpcAdmin.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lhs.taxcpcAdmin.global.pagination.DataGridDTO;
import com.lhs.taxcpcAdmin.global.pagination.FilterDTO;
import com.lhs.taxcpcAdmin.model.entity.ClientDetails;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

/**
 * @author sakshi.bandhate
 *
 */
public interface UserLoginTranService {

	public Long save(UserMast userMast, HttpServletRequest request);

	public UserLoginTran fetchByClientSessionSeq(Long clientSessionId) throws Exception;

	public void update(UserLoginTran updateTranObj) throws Exception;


	List<UserLoginTran> getLoginListMinToMax();

	public int getLoginDetailsCount(FilterDTO filter);
	
	public int getLoginDetailsListCount(FilterDTO filter, String user_code);

	List<UserLoginTran> getUserLoginDetailBrowserList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage,String user_code);

	public List<UserLoginTran> getUserLoginBrowseList(FilterDTO filter, DataGridDTO dataGridDTO, String recPerPage);

    public int getcount(String login_id, String user_name, String user_code);


	public List<UserLoginTran> getSearchUserLoginDetail(String login_id, String user_name, String user_code);

	public int getcountUserList(String login_time, String user_code);

	public List<UserLoginTran> getSearchUserList(String login_time, String user_code);


}





