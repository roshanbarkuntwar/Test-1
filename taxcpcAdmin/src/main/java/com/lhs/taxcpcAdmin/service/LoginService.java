/**
 * 
 */
package com.lhs.taxcpcAdmin.service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

/**
 * @author gaurav.khanzode
 *
 */
public interface LoginService {

	public UserMast getUserMastByLoginId(String loginid);

	public String getAppLastBuildTimestamp(Instant instant);

	boolean checkIfLicenseKeyExpired(Path parentPath);

	public List<UserLoginTran>  getUserLoginLastDetail(String usercode);

}
