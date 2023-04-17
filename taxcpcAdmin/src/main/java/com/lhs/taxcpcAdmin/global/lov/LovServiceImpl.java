/**
 * 
 */
package com.lhs.taxcpcAdmin.global.lov;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.javautilities.LhsStringUtility;
import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;

/**
 * @author sakshi.bandhate
 *
 */
@Service
public class LovServiceImpl implements LovService {

	@Autowired
	private LhsStringUtility stringUtility;

	@Autowired
	private GlobalDoWorkExecuter globalDoWorkExecuter;

	private BigDecimal count = new BigDecimal(0);

	@Override
	public LovInfoDTO getLovDataByConfiguration(String lovId, String searchValue, Map<String, Object> returnReplaceMap,
			String postbackCols, String searchOnBtn, String keyPressButton, String rowNum, String recPerPage,
			String currentCheckedValues) {
		String data = "";
		LovInfoDTO object = new LovInfoDTO();
		String header = "";
		String instantSearch = "";
		String displayDataOnLoad = "";
		String postBackCols = "";

		String keyButtonPressed = "F9";
		String paginationRequired = "";
		currentCheckedValues = !stringUtility.isNull(currentCheckedValues) ? currentCheckedValues : "";

//		System.out.println("returnReplaceMap---"+returnReplaceMap);
		try {

			if (!stringUtility.isNull(lovId)) {
				keyButtonPressed = !stringUtility.isNull(keyPressButton) ? keyPressButton : "F9";

				LovConfiguration configuration = new LovConfiguration();
				LovDataObject lovObject = configuration.getLovDataObjectById(lovId);

				if (lovObject != null) {
					header = !stringUtility.isNull(lovObject.getHeader()) ? lovObject.getHeader() : "";
					instantSearch = !stringUtility.isNull(lovObject.getSearch_on_keypress())
							? lovObject.getSearch_on_keypress()
							: "F";

					paginationRequired = !stringUtility.isNull(lovObject.getPaginationRequired())
							? lovObject.getPaginationRequired()
							: "F";

					if (displayDataOnLoad.equalsIgnoreCase("T")) {
						searchOnBtn = "T";
					}

					if (!stringUtility.isNull(keyButtonPressed)) {
						postBackCols = !stringUtility.isNull(postbackCols) ? (postbackCols)
								: (keyButtonPressed.equalsIgnoreCase("F8") ? lovObject.getPost_back_cols_f8()
										: lovObject.getPost_back_cols_f9());
					}

//					System.out.println("postBackCols---"+postBackCols);

					List<Object[]> datalist = null;
					if (searchOnBtn.equalsIgnoreCase("T")) {
						String query = lovObject.getQuery();
						String whereClauseString = "";
						String countQuery = "";

						query = proc_external_values_replacement(query, returnReplaceMap);

						if (!stringUtility.isNull(searchValue) && lovObject.getSearch_field_cols() != null
								&& lovObject.getSearch_field_cols().size() > 0) {
							whereClauseString = " and (";
							for (String clause : lovObject.getSearch_field_cols()) {
								if (!stringUtility.isNull(clause)) {
									whereClauseString = whereClauseString + "upper(" + clause + ") like upper('%"
											+ searchValue + "%') or ";
								}
							}
							if (!stringUtility.isNull(whereClauseString)) {
								whereClauseString = whereClauseString.substring(0, whereClauseString.length() - 3);
							}
							whereClauseString = whereClauseString + ")";
						}

						countQuery = "select count(*) from (" + query + " ) where 1 = 1 " + whereClauseString;
						if (!stringUtility.isNull(paginationRequired) && paginationRequired.equalsIgnoreCase("T")) {
							query = "select * from(select rownum slno, t.* from (" + query + ") t where 1 = 1 "
									+ whereClauseString + " ) " + "w where w.slno between " + rowNum + " and "
									+ (Integer.parseInt(rowNum) + (Integer.parseInt(recPerPage) - 1));
						} else {
							query = "select * from(select rownum slno, t.* from (" + query + ") t where 1 = 1 "
									+ whereClauseString + " ) ";
						}

//						System.out.println("lovObject.getQuery()...." + query);

						if (!stringUtility.isNull(lovObject.getCallableStatement())) {
//							System.out.println("--if--");

							/**
							 * Do not delete the below line... to be changed......
							 * 
							 **/
//							datalist = globalDoWorkExecuter.executeQueryAsListOfListWithBeginEnd(query, lovObject.getCallableStatement());
						} else {
//							System.out.println("--else--");
							count = (BigDecimal) globalDoWorkExecuter.getSingleValueFromSQLQuery(countQuery);
							datalist = globalDoWorkExecuter.executeSQLQueryAsList(query);
//							if (!stringUtility.isNull(paginationRequired) && paginationRequired.equalsIgnoreCase("T")) {
//								datalist = globalDoWorkExecuter.executeQueryAsListOfList(query);
//							} else {
//								datalist = globalDoWorkExecuter.executeQueryAsListOfList(query);
//							}
						}
					}
					ArrayList<String> display_cols = lovObject.getDisplay_cols();

					String multiSelect = !stringUtility.isNull(lovObject.getMultiselect()) ? lovObject.getMultiselect()
							: "F";

					data = tableDataGrid(display_cols, datalist, multiSelect, keyButtonPressed, paginationRequired,
							currentCheckedValues);

				} else {
					data = "Lov Is Not Configure";
				}

			}
		} catch (Exception e) {
			data = "Lov Is Not Configure";
			e.printStackTrace();
		}
		// System.out.println("data..." + data);
		object.setLovId(lovId);
		object.setPostBackCols(postBackCols);
		object.setHeader(header);
		object.setInstantSearch(instantSearch);
		object.setDataList(data);
		object.setPaginationEnabled(paginationRequired);

		return object;
	}// End Method

	public String tableDataGrid(ArrayList<String> headerList, List<Object[]> dataList, String multiselect,
			String keyButtonPressed, String paginationRequired, String currentCheckedValues) {

		StringBuilder sb = new StringBuilder();
		try {
			if (multiselect.equalsIgnoreCase("T")) {
				String[] split = currentCheckedValues.split("#");

				sb.append("<div class=\"lov-table-wrapper\">");
				sb.append("<div class=\"table-fix-header\" id=\"TableHead\">");
				sb.append("<table class=\"table table-sm table-bordered mb-0\" id=\"globalLovTableDataGrid\">");
				sb.append("<thead>");
				sb.append("<tr class=\"text-white\">");
				int colspan = 1;
				if (headerList != null && headerList.size() > 0) {
					colspan = headerList.size();
					sb.append("<th>");
					sb.append("<input type=\"checkbox\" onclick=\"lovcheckall(this);\">");
					sb.append("</th>");
					String headerString = getHeaderList(headerList, keyButtonPressed);
					sb.append(headerString);
				}
				sb.append("</tr>");
				sb.append("</thead>");
				sb.append("</table>");
				sb.append("</div>");
				sb.append(
						"<div class=\"table-fix-body\" style=\"max-height:150px;overflow-y:scroll;\" onscroll=\"global_HorizontalTableScroller('TableBody', 'TableHead');\" id=\"TableBody\">");
				sb.append("<table class=\"table table-sm table-bordered table-striped table-hover mb-0 \" >");
				sb.append("<tbody>");
				long i = 0l;
				if (dataList != null && dataList.size() > 0) {
					for (Object[] dataObjects : dataList) {
						i++;
						sb.append("<tr  id=\"lovrowselectable~" + i + "\" >");
						sb.append("<td>");

						if (!stringUtility.isNull(keyButtonPressed) && keyButtonPressed.equalsIgnoreCase("F8")) {
							int size = headerList.size();
							if (size > 9) {
//								Object dataPostionOne = dataObjects[1];
//								Object dataPostionTwo = dataObjects[2];
//								dataObjects[1] = dataPostionTwo;
//								dataObjects[2] = dataPostionOne;
								Object dataPostionOne = dataObjects[1];
								Object dataPostionTwo = dataObjects[2];
								Object dataPostionThree = dataObjects[3];
								Object dataPostionFour = dataObjects[4];
								Object dataPostionFive = dataObjects[5];
								Object dataPostionSix = dataObjects[6];
								Object dataPostionSeven = dataObjects[7];
								Object dataPostionEight = dataObjects[8];
								Object dataPostionNine = dataObjects[9];
								
								dataObjects[1] = dataPostionNine;
								dataObjects[2]= dataPostionEight;
								dataObjects[3] = dataPostionSeven;
								dataObjects[4] = dataPostionSix;
								dataObjects[5] = dataPostionFive;
								dataObjects[6] = dataPostionFour;
								dataObjects[7] = dataPostionThree;
								dataObjects[8] = dataPostionTwo;
								dataObjects[9] = dataPostionOne;
							}
						}

						int count = 0;
						for (String string : split) {
							if (string.equals(dataObjects[1])) {
								count++;
							}
						}

						if (count >= 1) {
							sb.append("<input type=\"checkbox\" class=\"cursor-pointer\" id=\"lovrowselector~" + i
									+ "\" onclick=\"setSelectedCodes(this);\" checked>");
						} else {
							sb.append("<input type=\"checkbox\" class=\"cursor-pointer\" id=\"lovrowselector~" + i
									+ "\" onclick=\"setSelectedCodes(this);\">");
						}

						sb.append("</td>");
						String dataValueString = getDataList(headerList, dataObjects, keyButtonPressed, i);
						sb.append(dataValueString);
						sb.append("</tr>");
					}
					sb.append("<tr>");
				} else {
					sb.append("<tr>");
					sb.append("<td colspan=\"" + colspan + "\">No Records Found</td>");
					sb.append("</tr>");
				}
				sb.append("</tbody>");
				sb.append("</table>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<div class=\"text-center my-1\">");
				sb.append(
						"<input type=\"button\" class=\"btn btn-sm btn-default global-btn-secondary mr-4\"  value=\"Done\" id=\"postLovDataToForm\" onclick=\"postDataToForm()\">");
				sb.append("</div>");
//				System.out.println("count.intValue()---"+count.intValue());
				if (!stringUtility.isNull(paginationRequired) && paginationRequired.equalsIgnoreCase("T")) {
					sb.append("<div class=\"lov-table-wrapper text-right mt-1\">");
					sb.append(
							"<label class=\"float-left\">Page No. <span for=\"pageNo\" id=\"pageNo\">1</span></label>");
					if (count.intValue() > 0) {
						sb.append("<div>");
						sb.append(
								"<button type=\"button\" onclick=prevRecords();  class=\"btn btn-primary btn-sm px-2 py-1\"><i class=\"fa fa-chevron-left\"></i></button>");
						sb.append(count.intValue()
								+ " Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\""
								+ count.intValue() + "\">");
						sb.append(
								"<button type=\"button\" class=\"btn btn-primary btn-sm px-2 py-1\" onclick=nextRecords();><i class=\"fa fa-chevron-right\"></i></button>");
						sb.append("</div>");
					} else {
						sb.append("0 Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\"0\">");
					}
					sb.append("</div>");
				} else {
					sb.append("<div class=\"lov-table-wrapper text-right mt-1\">");
					if (count.intValue() > 0) {
						sb.append(count.intValue()
								+ " Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\""+ count.intValue() + "\">");
					} else {
						sb.append("0 Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\"0\">");
					}
					sb.append("</div>");
				}
			} else {
				sb.append("<div class=\"lov-table-wrapper\">");
				sb.append("<div class=\"table-fix-header iyy\" id=\"lovThead\">");
				sb.append("<table class=\"table table-sm table-bordered mb-0\" id=\"globalLovTableDataGrid\">");
				sb.append("<thead  class=\"text-center my-1 \">");
				sb.append("<tr class=\"text-white\">");
				int colspan = 1;
				if (headerList != null && headerList.size() > 0) {
					colspan = headerList.size();
					String headerString = getHeaderList(headerList, keyButtonPressed);
					sb.append(headerString);
				}
				sb.append("</tr>");
				sb.append("</thead>");
				sb.append("</table>");
				sb.append("</div>");
				sb.append(
						"<div class=\"table-fix-body\" onscroll=\"global_HorizontalTableScroller('lovTbody', 'lovThead');\" id=\"lovTbody\" style=\"max-height:350px;\">");
				sb.append("<table class=\"table table-sm table-bordered table-striped table-hover mb-0 \" >");
				sb.append("<tbody>");
				long i = 0l;
				if (dataList != null && dataList.size() > 0) {
					for (Object[] dataObjects : dataList) {
						i++;
						sb.append("<tr ondblclick=\"post_back_data(this,"+i+");\" id=\"lovrowselectable~" + i + "\" >");
						String dataValueString = getDataList(headerList, dataObjects, keyButtonPressed, i);
						sb.append(dataValueString);
						sb.append("</tr>");
					}
				} else {
					sb.append("<tr>");
					sb.append("<td colspan=\"" + colspan + "\">No Records Found</td>");
					sb.append("</tr>");
				}
				sb.append("</tbody>");
				sb.append("</table>");
				sb.append("</div>");
				sb.append("</div>");
			

				if (!stringUtility.isNull(paginationRequired) && paginationRequired.equalsIgnoreCase("T")) {
					sb.append("<div class=\"lov-table-wrapper text-right mt-1\">");
					sb.append(
							" <label class=\"float-left\">Page No. <span for=\"pageNo\" id=\"pageNo\">1</span></label>");
					if (count.intValue() > 0) {
						sb.append("<div class='float-right' style='border:1px solid #4e8dff'>");
						sb.append(
								"<button type=\"button\" onclick=prevRecords();  class=\"btn btn-primary btn-sm px-2 py-1\" style=\"border-radius:0\"><i class=\"fa fa-chevron-left\"></i></button>");
						sb.append("&nbsp;&nbsp;"+ count.intValue()
								+ " Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\""
								+ count.intValue() + "\"> &nbsp;&nbsp;");
						sb.append(
								"<button type=\"button\" class=\"btn btn-primary btn-sm px-2 py-1\" style=\"border-radius:0\" onclick=nextRecords();><i class=\"fa fa-chevron-right\"></i></button>");
						sb.append("</div>");
					} else {
						sb.append("0 Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\"0\">");
					}
					sb.append("</div>");
				} else {
					sb.append("<div class=\"lov-table-wrapper text-right mt-1\">");
					if (count.intValue() > 0) {
						sb.append(count.intValue()
								+ " Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\""
								+ count.intValue() + "\">");
					} else {
						sb.append("0 Records Found <input type=\"hidden\" id=\"lovtotalrecord\" value=\"0\">");
					}
					sb.append("</div>");
				}
			}
		} catch (Exception e) {
			sb = new StringBuilder();
			sb.append("<div>Lov Not Configure Properly</div>");
			e.printStackTrace();
		}

		return sb.toString();

	}// end function

	public String getHeaderList(ArrayList<String> headerList, String keyButtonPressed) {
		StringBuilder sb = new StringBuilder();
		if (!stringUtility.isNull(keyButtonPressed) && keyButtonPressed.equalsIgnoreCase("F8")) {
			int size = headerList.size();
			if (size > 2) {
				String postionOne = headerList.get(1);
				String postionTwo = headerList.get(2);
				headerList.set(1, postionTwo);
				headerList.set(2, postionOne);
			}
		}

		int count = 0;
		for (String header : headerList) {
			String[] head = header.split("~");

			String style = "";
			if (head[1].equalsIgnoreCase("H")) {
				style += "display:none;";
			}
			
			
			if (count > 1) {
//				style += "word-break:break-all;min-width 150px;max-width:150px;width:140px ";
				style += "word-break:break-all;min-width:120px;max-width:120px;width:120px";
			}
			if (count == 1) {
				style += "word-break:break-all;min-width:90px;max-width:90px;width:90px ";
//				style += "min-width:45px;max-width:100px;width:120px";
			}
			else  if(count == 0){
				style += "min-width:40px;max-width:40px;width:40px";
			}
			String className = "";
			if (head.length > 9 && head[9].equalsIgnoreCase("R")) {
				className = "class=\"text-right\" ";
			}
			count++;
			sb.append("<th " + className + " style=\"" + style + "\" >");
			sb.append(head[0]);
			sb.append("</th>");
		}

		return sb.toString();
	}// end function

	public String getDataList(ArrayList<String> headerList, Object[] dataObjects, String keyButtonPressed,
			long rownum) {

		StringBuilder sb = new StringBuilder();
		

		if (!stringUtility.isNull(keyButtonPressed) && keyButtonPressed.equalsIgnoreCase("F8")) {
			int size = headerList.size();
			if (size > 10) {
				Object dataPostionOne = dataObjects[1];
				Object dataPostionTwo = dataObjects[2];
				Object dataPostionThree = dataObjects[3];
				Object dataPostionFour = dataObjects[4];
				Object dataPostionFive = dataObjects[5];
				Object dataPostionSix = dataObjects[6];
				Object dataPostionSeven = dataObjects[7];
				Object dataPostionEight = dataObjects[8];
				Object dataPostionNine = dataObjects[9];
				
				dataObjects[1] = dataPostionNine;
				dataObjects[2]= dataPostionEight;
				dataObjects[3] = dataPostionSeven;
				dataObjects[4] = dataPostionSix;
				dataObjects[5] = dataPostionFive;
				dataObjects[6] = dataPostionFour;
				dataObjects[7] = dataPostionThree;
				dataObjects[8] = dataPostionTwo;
				dataObjects[9] = dataPostionOne;
			}
		}

		int count = 0;
		
		for (Object data : dataObjects) {
		
			String header = headerList.get(count);
			String[] head = header.split("~");

			String style = "";
			if (head[1].equalsIgnoreCase("H")) {
				style += "display:none;";
			}
			
			if (count > 1) {
//				style += "word-break:break-all;min-width:150px;max-width:150px;width:140px";
				style += "word-break:break-all;min-width:120px;max-width:120px;width:120px";
			}
			if (count == 1) {
//				style += "min-width:45px;max-width:100px;width:120px";
				style += "word-break:break-all;min-width:90px;max-width:90px;width:90px";
			}
			else if(count == 0) {
//				style += "min-width:75px;max-width:75px;width:75px";
				style += "min-width:40px;max-width:40px;width:40px";
			}
			String className = "";
			if (head.length > 9 && head[9].equalsIgnoreCase("R")) {
				className = "class=\"text-right \" ";
			} else {
				className = "class=\"text-center pre-wrap\" ";
			}
			
			count++;
			sb.append("<td " + className + " style=\"" + style + "\" >");
			sb.append("<label style=\"pre-wrap;\" for=\"lovrowselector~" + rownum + "\">");
			// sb.append("<a href=\"#\">");
			try {
				if(count == 9) {
					sb.append("<input type=\"hidden\" id=\"dataId~"+ rownum + "\" value=\""+ data + "\">");
					sb.append("<script> $(function () {  $('[data-toggle=\"popover\"]').popover() }) </script>" );
					sb.append("<script>\r\n" + 
							"$('body').on('click', function (e) {\r\n" + 
							"    $('[data-toggle=popover]').each(function () {\r\n" + 
							"        // hide any open popovers when the anywhere else in the body is clicked\r\n" + 
							"        if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {\r\n" + 
							"            $(this).popover('hide');\r\n" + 
							"        }\r\n" + 
							"    });\r\n" + 
							"});\r\n" + 
							"</script>");
					sb.append("<a  href=\"#\" data-toggle=\"popover\"  data-content=\""+data.toString()+"\"> <i class=\"fa details text-primary cursor-pointer mr-1\"></i></a>");
				}else {
				data = (!stringUtility.isNull(data.toString()) ? data : "");
				sb.append(data);
				}
			} catch (Exception e) {
				data = "";
			}
//			System.out.println("lov dataaaaa-->"+data);
			
			// sb.append("</a>");
			sb.append("</label>");
			sb.append("</td>");
		}

		return sb.toString();

	}// end function

	private String proc_external_values_replacement(String a_query, Map<String, Object> replacement_data) {
		String l_query = a_query;
		String l_query_temp = a_query;
		boolean rep_required = true;
		if (rep_required) {
			try {
				Set<String> keys = replacement_data.keySet();
				Object[] key_array = keys.toArray();
				for (Object obj : key_array) {
					String rep_head = (String) obj;
					String rep_value = "";
					try {
						rep_value = (String) replacement_data.get(rep_head);
					} catch (Exception ex) {
						try {
							Integer rep_value_i = (Integer) replacement_data.get(rep_head);
							rep_value = rep_value_i != null ? rep_value_i.toString() : "";
						} catch (Exception e) {
							try {
								Long rep_value_i = (Long) replacement_data.get(rep_head);
								rep_value = rep_value_i != null ? rep_value_i.toString() : "";
							} catch (Exception el) {
								try {
									Double rep_value_i = (Double) replacement_data.get(rep_head);
									rep_value = rep_value_i != null ? rep_value_i.toString() : "";
								} catch (Exception ed) {
								}
							}
						}
					}
					a_query = a_query.replaceAll(rep_head, rep_value);
				} // end for
				l_query = a_query;
			} catch (Exception e) {
				l_query = l_query_temp;
				// System.out.println("Error in external value replacement, no value replaced");
				e.printStackTrace();
			}
		} // end if
		return l_query;
	}// end function

	@Override
	public Map<String, String> getValidateLOVDataValues(String lovId, String codeValue,
			Map<String, Object> replaceValueMap, String postBackCols) {
//		public HashMap<String, Object> getValidateLOVDataValues(String elementId, String lovId, String codeValue, Map<String, Object> replaceValueMap) {
		Map<String, String> lov_key_values = null;

		if (!stringUtility.isNull(lovId)) {
			LovConfiguration lov_config = new LovConfiguration();
			LovDataObject lov_object = lov_config.getLovDataObjectById(lovId);

			if (lov_object != null) {
				String lov_query_string = " SELECT * FROM ( " + lov_object.getQuery() + " ) ";

				lov_query_string = proc_external_values_replacement(lov_query_string, replaceValueMap);

				ArrayList<String> headerList = (ArrayList<String>) getHeaders(
						!stringUtility.isNull(postBackCols) ? postBackCols : lov_object.getPost_back_cols_f9());
//				ArrayList<String> headerList = (ArrayList<String>) Arrays.asList(lov_object.getPost_back_cols_f9().split("#"));

				lov_query_string += " WHERE UPPER(" + lov_object.getValidateLovCode() + ") = UPPER('" + codeValue
						+ "') ";

				ArrayList<ArrayList<String>> data_list = null;
				try {
//					System.out.println("Validate LOV Query--->\n" + lov_query_string);
					data_list = globalDoWorkExecuter.executeSQLQueryAsListOfList(lov_query_string);

					if (data_list != null && data_list.size() > 0) {
						lov_key_values = new LinkedHashMap<String, String>();
						ArrayList<String> data_values = data_list.get(0);

						// Collecting geo details as key value pair in map
						lov_key_values = (HashMap<String, String>) IntStream
								.range(0, Math.min(headerList.size(), data_values.size())).boxed()
								.collect(Collectors.toMap(i -> headerList.get(i), i -> data_values.get(i)));
					}
				} catch (Exception e) {
					data_list = null;
					lov_key_values = null;
					e.printStackTrace();
				}
			} else {
				lov_key_values = null;
			}
		} else {
			lov_key_values = null;
		}

//		System.out.println("lov_key_values---" + lov_key_values);
		return lov_key_values;
	}// End Method

	private List<String> getHeaders(String postBackCols) {
		List<String> headerList = new ArrayList<String>();
		try {
			String arr[] = postBackCols.split("#");
			for (String s : arr) {
				if (!stringUtility.isNull(s)) {
					headerList.add(s);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return headerList;
	}// End Method

}// End Class