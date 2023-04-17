/**
 * 
 */
package com.lhs.taxcpcAdmin.utilities;

import org.springframework.stereotype.Component;

import com.lhs.taxcpcAdmin.model.Assessment;

@Component
public class TaxcpcUtil {

	public String getBaseName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			return fileName;
		} else {
			return fileName.substring(0, index);
		}
	}

	public String changeAccYearToAssessmentAccYear(String Acc_year) {
		String assesment_acc_year = "";
		try {
			String[] acc_year_split = Acc_year.split("-");
			String acc_year_p1 = acc_year_split[0];
			int newaccyear_p1 = Integer.valueOf(acc_year_p1) + 1;
			String acc_year_p2 = acc_year_split[1];
			int newaccyear_p2 = Integer.valueOf(acc_year_p2) + 1;
			assesment_acc_year = newaccyear_p1 + "-" + newaccyear_p2;
		} catch (Exception e) {
			assesment_acc_year = "";
		}
		return assesment_acc_year;
	}

	public String getLogFileName(Assessment asmt, String entityCode, String processType, long tokenNo) {
		String returnFileName = "";
		try {
			/**
			 * Returns a log file name. e.g. B4PNBHO19326QIMP1234
			 */

			String logAccYear = asmt.getAccYear().replace("-", "_");
			returnFileName = entityCode + "_"
					+ logAccYear + "_"
					+ asmt.getQuarterNo() + "_"
					+ asmt.getTdsTypeCode() + "_" + processType + "_" + tokenNo;

			returnFileName = returnFileName.toLowerCase() + ".log";
		} catch (Exception e) {
		}
		return returnFileName;
	}// end method

}
