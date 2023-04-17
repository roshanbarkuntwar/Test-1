/**
 * 
 */
package com.lhs.taxcpcAdmin.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsEncryptionDecryptionUtility;
import com.lhs.taxcpcAdmin.dao.UserLoginTranRepositorySupport;
import com.lhs.taxcpcAdmin.model.entity.UserLoginTran;
import com.lhs.taxcpcAdmin.model.entity.UserMast;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LhsEncryptionDecryptionUtility lhsEncDecUtl;
	
	@Autowired
	private UserLoginTranRepositorySupport userRepoSupport;

	public UserMast getUserMastByLoginId(String loginid) {
		UserMast userMast = null;

		return userMast;
	}

	@Override
	public String getAppLastBuildTimestamp(Instant instant) {
		String appLastBuildTimestamp = "";
		try {
			DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a").withZone(ZoneId.systemDefault());
			appLastBuildTimestamp = DATE_TIME_FORMATTER.format(instant);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appLastBuildTimestamp;
	}// end method

	@Override
	public boolean checkIfLicenseKeyExpired(Path parentPath) {

		try {
			log.warn("Checking whether the License key is expired.");

			String keyFileLoc = parentPath + File.separator + "TDI_PRE_LOGIN_CONFIG" + File.separator + "TAXCPC_KEY_GENERATION"
					+ File.separator + "License.key";

			Path keyFilePath = Paths.get(keyFileLoc);
			if (Files.exists(keyFilePath)) {
				List<String> lines = Files.readAllLines(keyFilePath, StandardCharsets.UTF_8);
				if (lines != null && !lines.isEmpty()) {
					final String secretKey = "fgs@taxcpc#hsag##87@lhs";
					final String salt = "dcbqpodcbyxwbazuts";
					final String matchingKeyword = "taxcpc@lhs87#fgs2020";

					String decryptedKeyStr = lhsEncDecUtl.decrypt(lines.get(0), secretKey, salt);
					StringTokenizer keyTokens = new StringTokenizer(decryptedKeyStr, "|*|");

					if (keyTokens != null) {
						String expiryDateStr = keyTokens.nextToken();
						String keywordStr = keyTokens.nextToken();

						if (keywordStr.equals(matchingKeyword)) {
							LocalDate expiryDate = LocalDate.parse(expiryDateStr, DateTimeFormatter.ofPattern("ddMMyyyy"));
							LocalDate currentDate = LocalDate.now();

							log.info("Current date= {}, Expiry Date= {}", currentDate, expiryDate);

							if (currentDate.isBefore(expiryDate) || expiryDate.equals(currentDate)) {
								log.info("License key is not expired.");
								log.info("Expiry date [{}] is before or equal to the current date [{}].", expiryDate, currentDate);
								log.info("You will be redirected to the login page...");
							} else {
								log.error("License key is got expired.");
								log.error("Expiry date [{}] is not before or equal to the current date [{}].", expiryDate, currentDate);
								log.info("You are not authorized to get logging in... please contact Taxcpc team.");
								return true;
							}
						}
					}
				} else {
					log.error("License key is invalid or currupt.");
					log.info("You are not authorized to get logging in... please contact Taxcpc team.");
				}
			} else {
				log.error("License key file does not exists= {}", keyFilePath);
				log.info("You are not authorized to get logging in... please contact Taxcpc team.");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}// end method
	
	
	@Override
	public List<UserLoginTran> getUserLoginLastDetail(String user_code) {


		// System.out.println("usercode................."+user_code);

		List<UserLoginTran> list = new ArrayList<>();

		list = userRepoSupport.getUserLastdetail(user_code);

		// System.out.println("List Details............" +list);
		return list;

	}
}
