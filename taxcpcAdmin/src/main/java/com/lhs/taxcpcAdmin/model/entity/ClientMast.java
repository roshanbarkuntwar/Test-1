package com.lhs.taxcpcAdmin.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_mast")
public class ClientMast {

	@Column(name = "entity_code", length = 2)
	private String entity_code;
	
	@Id
	@Column(name = "client_code", length = 15, nullable = false)
	private String client_code;
	@Column(name = "client_name", length = 100)
	private String client_name;
	@Column(name = "parent_code", length = 15)
	private String parent_code;
	@Column(name = "remark", length = 240)
	private String remark;
	@Column(name = "initiate_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date initiate_date;
	@Column(name = "client_type_code", length = 5)
	private String client_type_code;
	@Column(name = "client_status", length = 1)
	private String client_status;
	@Column(name = "closed_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date closed_date;
	@Column(name = "geo_org_code", length = 500)
	private String geo_org_code;
	@Column(name = "approvedby", length = 8)
	private String approvedby;
	@Column(name = "approveddate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date approveddate;
	@Column(name = "owner_name", length = 50)
	private String owner_name;
	@Column(name = "deductor_name", length = 100)
	private String deductor_name;
	@Column(name = "deductor_desig", length = 100)
	private String deductor_desig;
	@Column(name = "deductor_phoneno", length = 100)
	private String deductor_phoneno;
	@Column(name = "deductor_mobileno", length = 100)
	private String deductor_mobileno;
	@Column(name = "deductor_email_id", length = 250)
	private String deductor_email_id;
	@Column(name = "reference_remark", length = 240)
	private String reference_remark;
	@Column(name = "website", length = 25)
	private String website;
	@Column(name = "add1", length = 100)
	private String add1;
	@Column(name = "add2", length = 100)
	private String add2;
	@Column(name = "add3", length = 100)
	private String add3;
	@Column(name = "add4", length = 100)
	private String add4;
	@Column(name = "city_code", length = 100)
	private String city_code;
	@Column(name = "pin", length = 6)
	private String pin;
	@Column(name = "stdcode", length = 6)
	private String stdcode;
	@Column(name = "phoneno", length = 100)
	private String phoneno;
	@Column(name = "mobileno", length = 100)
	private String mobileno;
	@Column(name = "email_id", length = 250)
	private String email_id;
	@Column(name = "estd_year", length = 4)
	private String estd_year;
	@Column(name = "panno", length = 10)
	private String panno;
	@Column(name = "tanno", length = 12)
	private String tanno;
	@Column(name = "login_id", length = 100)
	private String loginId;
	@Column(name = "login_pwd", length = 100)
	private String loginPwd;
	@Column(name = "web1_login_id", length = 100)
	private String web1_login_id;
	@Column(name = "web1_login_pwd", length = 100)
	private String web1_login_pwd;
	@Column(name = "web2_login_id", length = 100)
	private String web2_login_id;
	@Column(name = "web2_login_pwd", length = 100)
	private String web2_login_pwd;
	@Column(name = "web3_login_id", length = 100)
	private String web3_login_id;
	@Column(name = "web3_login_pwd", length = 100)
	private String web3_login_pwd;
	@Column(name = "lastupdate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag", length = 1)
	private String flag;
	@Column(name = "state_code", length = 2)
	private String state_code;
	@Column(name = "branch_division", length = 65)
	private String branch_division;
	@Column(name = "deductor_add1", length = 100)
	private String deductor_add1;
	@Column(name = "deductor_add2", length = 100)
	private String deductor_add2;
	@Column(name = "deductor_add3", length = 100)
	private String deductor_add3;
	@Column(name = "deductor_add4", length = 100)
	private String deductor_add4;
	@Column(name = "deductor_city_code", length = 100)
	private String deductor_city_code;
	@Column(name = "deductor_pin", length = 6)
	private String deductor_pin;
	@Column(name = "deductor_stdcode", length = 6)
	private String deductor_stdcode;
	@Column(name = "deductor_state_code", length = 2)
	private String deductor_state_code;
	@Column(name = "alternate_phoneno", length = 100)
	private String alternate_phoneno;
	@Column(name = "alternate_mobileno", length = 100)
	private String alternate_mobileno;
	@Column(name = "alternate_email_id", length = 250)
	private String alternate_email_id;
	@Column(name = "country_code", length = 3)
	private String country_code;
	@Column(name = "deductor_country_code", length = 3)
	private String deductor_country_code;
	@Column(name = "ministry_code", length = 5)
	private String ministry_code;
	@Column(name = "sub_ministry_code", length = 5)
	private String sub_ministry_code;
	@Column(name = "alternate_stdcode", length = 7)
	private String alternate_stdcode;
	@Column(name = "deductor_alternate_stdcode", length = 6)
	private String deductor_alternate_stdcode;
	@Column(name = "deductor_alternate_phoneno", length = 100)
	private String deductor_alternate_phoneno;
	@Column(name = "deductor_alternate_email_id", length = 250)
	private String deductor_alternate_email_id;
	@Column(name = "ain_no", length = 50)
	private String ain_no;
	@Column(name = "client_catg_code", length = 5)
	private String client_catg_code;
	@Column(name = "pao_code", length = 20)
	private String pao_code;
	@Column(name = "pao_registration_no", length = 10)
	private String pao_registration_no;
	@Column(name = "ddo_code", length = 20)
	private String ddo_code;
	@Column(name = "ddo_registration_no", length = 10)
	private String ddo_registration_no;
	@Column(name = "temp_zen_deductor_id")
	private Long temp_zen_deductor_id;
	@Column(name = "deductor_panno", length = 10)
	private String deductor_panno;
	@Column(name = "deductor_add_change", length = 1)
	private String deductor_add_change;
	@Column(name = "deductor_add_change_on")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date deductor_add_change_on;
	@Column(name = "ministry_name_other", length = 100)
	private String ministry_name_other;
	@Column(name = "login_pwd_expiry_days", length = 4)
	private String login_pwd_expiry_days;
	@Column(name = "closed_remark", length = 4000)
	private String closed_remark;
	@Column(name = "ministry_state_code", length = 2)
	private String ministry_state_code;
	@Column(name = "traces_id", length = 50)
	private String traces_id;
	@Column(name = "traces_pwd", length = 50)
	private String traces_pwd;
	@Column(name = "add_change", length = 1)
	private String add_change;
	@Column(name = "add_change_on")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date add_change_on;
	@Column(name = "last_deduction_ref_no_e_catg")
	private Long last_deduction_ref_no_e_catg;
	@Column(name = "last_deduction_ref_no_o_catg")
	private Long last_deduction_ref_no_o_catg;
	@Column(name = "bulk_pan_verification_username", length = 50)
	private String bulk_pan_verification_username;
	@Column(name = "bulk_pan_verification_password", length = 50)
	private String bulk_pan_verification_password;
	@Column(name = "code_level", nullable = false)
	private Long code_level;
	@Column(name = "bank_branch_code", length = 15)
	private String bank_branch_code;
	@Column(name = "g_parent_code", length = 15)
	private String g_parent_code;
	@Column(name = "sg_parent_code", length = 15)
	private String sg_parent_code;
	@Column(name = "ssg_parent_code", length = 15)
	private String ssg_parent_code;
	@Column(name = "sssg_parent_code", length = 15)
	private String sssg_parent_code;
	@Column(name = "appr_random_no", length = 15)
	private String appr_random_no;
	@Column(name = "appr_verify_code", length = 15)
	private String appr_verify_code;
	@Column(name = "pan_verification_rn", length = 15)
	private String pan_verification_rn;
	@Column(name = "efiling_rn", length = 15)
	private String efiling_rn;
	@Column(name = "bulk_15gh_xml_rn", length = 15)
	private String bulk_15gh_xml_rn;
	@Column(name = "bflag_generation_level", length = 30)
	private String bflag_generation_level;
	@Column(name = "client_level_type", nullable = false)
	private Long client_level_type;
	@Column(name = "bflag_generation_level_value", length = 30)
	private String bflag_generation_level_value;
	@Column(name = "deduction_ref_no_e_catg_15gh")
	private Long deduction_ref_no_e_catg_15gh;
	@Column(name = "deduction_ref_no_o_catg_15gh")
	private Long deduction_ref_no_o_catg_15gh;
	@Column(name = "add_right")
	private Long add_right;
	@Column(name = "edit_right")
	private Long edit_right;
	@Column(name = "delete_right")
	private Long delete_right;
	@Column(name = "print_right")
	private Long print_right;
	@Column(name = "query_right")
	private Long query_right;
	@Column(name = "special_right")
	private Long special_right;
	@Column(name = "approved_right")
	private Long approved_right;
	@Column(name = "valuation_right")
	private Long valuation_right;
	@Column(name = "gstn_no", length = 15)
	private String gstn_no;
	@Column(name = "process_seqno")
	private Long process_seqno;

}
