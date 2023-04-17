package com.lhs.taxcpcAdmin.service.lov;

import java.util.ArrayList;

import com.lhs.taxcpcAdmin.global.lov.LovDataObject;

public class ReqTranLov {
	
	
	public LovDataObject getParentReq() {
		LovDataObject object = new LovDataObject();
		
		object.setHeader("Select Requisition");
		object.setQuery("select req_code, (select i.user_name from user_mast i where v.reported_by =  i.user_code)reported_by, reported_date, req_priority, req_type, issue_type, (select j.project_name  from project_mast j where j.project_code = v.project_name) project_name, issue_description from req_tran v order by reported_date desc");
		
		ArrayList<String> searchAry = new ArrayList<String>();
		searchAry.add("req_code");	
		searchAry.add("reported_by");
		searchAry.add("reported_date");
		searchAry.add("req_priority");
		searchAry.add("req_type");
		searchAry.add("issue_type");
		searchAry.add("project_name)");
		searchAry.add("issue_description");

		object.setSearch_field_cols(searchAry);
		
		ArrayList<String> displayAry = new ArrayList<String>();
		displayAry.add("Sr No~D");
		displayAry.add("Parent Req Code~D");
		displayAry.add("Reported By~D");
		displayAry.add("Reported Date~D");
		displayAry.add("Priority~D");
		displayAry.add("Req Type~D");
		displayAry.add("Issue Type~D");
		displayAry.add("Application Name~D");
		displayAry.add("Parent Req Description~D");

		object.setDisplay_cols(displayAry);
		
		object.setValidateLovCode("user_code");
		object.setPost_back_cols_f8("null#issue_description#req_code");
		object.setPost_back_cols_f9("null#req_code#issue_description");
		object.setSearch_on_keypress("T");
		object.setMultiselect("F");
		object.setPaginationRequired("T");
		
		return object;
	}// End Method
	
}
