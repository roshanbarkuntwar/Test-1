package com.lhs.taxcpcAdmin.global.lov;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhs.javautilities.LhsStringUtility;

/**
 * 
 */

/**
 * @author sakshi.bandhate
 *
 */

@Controller
public class LovController {

	@Autowired
	private LovService lovService;

	@Autowired
	private LhsStringUtility lhsStringUtility;

	@RequestMapping("/lovConfiguration")
	public @ResponseBody LovInfoDTO getLovService(HttpServletRequest request, @RequestParam(value = "lovid", required = true) String lovId, @RequestParam(value = "rowNum", required = false, defaultValue = "1") String rowNum, @RequestParam(value = "recPerPage", required = false, defaultValue = "10") String recPerPage, @RequestParam(value = "searchValue", required = false) String searchValue, @RequestParam(value = "keyPressButton", required = false) String keyPressButton, @RequestParam(value = "replaceMap", required = false) String replaceMap, @RequestParam(value = "postBackCols", required = false) String postBackCols, @RequestParam(value = "currentCheckedValues", required = false) String currentCheckedValues) {
		String message = "";
		LovInfoDTO lovInfoDTO = null;
		try {
//			String searchValue = request.getParameter("searchValue");
//			String keyPressButton = request.getParameter("keyPressButton");
//			String replaceMap = request.getParameter("replaceMap");
//			String postBackCols = request.getParameter("postBackCols");
//			String currentCheckedValues = request.getParameter("currentCheckedValues"); 

//			System.out.println("postBackCols : "+postBackCols);

			Map<String, Object> returnReplaceMap = new HashMap<String, Object>();
			if (!lhsStringUtility.isNull(replaceMap)) {
				returnReplaceMap = toMap(replaceMap);
			}

			lovInfoDTO = lovService.getLovDataByConfiguration(lovId, searchValue, returnReplaceMap, postBackCols, "T", keyPressButton, rowNum, recPerPage, currentCheckedValues);
//			System.out.println("lovInfoDTO..."+lovInfoDTO);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lovInfoDTO;
	}// End Method

	@RequestMapping(value = "/validateFromLOV", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> validateFromLOV(HttpServletRequest request, @RequestParam(name = "lovId", required = true) String lovId, @RequestParam(name = "valueToValidate", required = true) String valueToValidate, @RequestParam(name = "queryReplaceValues", required = true) String queryReplaceValues, @RequestParam(name = "postBackCols", required = true) String postBackCols) {
		Map<String, String> lov_data_values = null;

		try {
			Map<String, Object> replaceValueMap = new HashMap<String, Object>();
			if (!lhsStringUtility.isNull(queryReplaceValues)) {
				replaceValueMap = toMap(queryReplaceValues);
			}

			lov_data_values = lovService.getValidateLOVDataValues(lovId, valueToValidate, replaceValueMap, postBackCols);

		} catch (JSONException ex) {
			lov_data_values = null;
			ex.printStackTrace();
		}

		return lov_data_values;
	}// End Method

	@SuppressWarnings("unchecked")
	private Map<String, Object> toMap(String jsonString) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject object = new JSONObject(jsonString);
			Iterator<String> keysItr = object.keys();
			while (keysItr.hasNext()) {
				String key = keysItr.next();
				Object value = object.get(key);
				if (value instanceof JSONObject) {
					String valueString = value.toString();
					value = toMap(valueString);
				}
				map.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}// End Method

}// End Class
