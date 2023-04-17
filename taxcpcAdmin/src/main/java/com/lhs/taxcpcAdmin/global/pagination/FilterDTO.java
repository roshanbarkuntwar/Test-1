package com.lhs.taxcpcAdmin.global.pagination;





import java.util.Date;

import lombok.Data;
import oracle.sql.DATE;

@Data
public class FilterDTO {
    
	private String user_code;
	private String user_name;
	private String user_type;	
	private String user_mode;
	private String dept_str;
	private String status;
	private String doc_type_code;
	private String doc_name;
	private String role_code;
	private String role_name;
	private String work_type;
	private int filterFlag;
	private String work_priority;
	private String work_nature;
	private String project_code;
	private String completion_date;
	
	private String req_code;
	private Date reported_date;
	private String project_name;
	private String module_name;
	private String menu_name;
	private String issue_type;
	private String current_req_status;
	private String req_priority;
	private String req_type;
	private String dev_status;
	private String reported_by;
	
	private String login_time;	

	private String project_info;
	private String project_type;
	private String architecture;	
	private String frontend;
	private String backend;
	private String database;
	private String framework;
	private String svn_link;
	private String war_filename;
	private String other_war_filename;
	private String domain_code;
	private String parent_code;
	private String project_status;
	private String module_code;
	private String remark;
	private String lastupdate;
	private String flag;
	private String module_status;
	private String menu_description;
	private String sub_menu_type;
	private String menu_type;
	
	public String team_name;
	public String resp_person_name;
	public String resp_person_mobileno;
	public String branch_add1;
	public String filter_name ;
	private String depl_code;
	public String login_id ;
	public String login_count ;
	public String entity_name;
	public String entity_code;
	public String division;
	public String city;
	public String pin;
	public String district;
	public String website;
	public String page_mode;
	public String search_entity;
	
	public String dict_name;
	
	private String audit_type;
	private String search_value;
	
	private String from_date1;
	private String to_date1;
	private String doc_mode;
	private String initiate_date1;
	private String closed_date1;
	
	
	private String deploy_delivered_date;
	private String req_code1;
	private String from_date2;
	private String to_date2;
	private String table_or_view_name;
	private String object_type;
	private String type_of_db;
	private String db_ip;
	private String db_object_name;
	private String type_of_database;
	
	private String application_type;
	private String application_name;
	
	private String configuration_type;
	private String server_owner_name;
	private String server_ip;
	private String tag_name;

}

