/**
 * 
 */
package com.lhs.taxcpcAdmin.global.lov;

import java.util.Map;

/**
 * @author sakshi.bandhate
 *
 */
public interface LovService {
	
	public LovInfoDTO getLovDataByConfiguration(String lovId, String searchValue, Map<String, Object> returnReplaceMap, String postbackCols, String searchOnBtn, String keyPressButton, String rowNum, String recPerPage, String currentCheckedValues);

	public Map<String, String> getValidateLOVDataValues(String lovId, String codeValue, Map<String, Object> replaceValueMap, String postbackCols);
	
}// End Interface
