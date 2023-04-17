package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "req_tran")
public class ReqTran implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Embedded
//	private ReqTranAttachId reqAttachTran;
	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "REQ_TRAN_SEQ"))
	@GeneratedValue(generator = "generator")
	@Column(name = "req_code", length = 20, nullable = false)
	private String req_code;
	@Column(name = "reported_by", length = 20)
	private String reported_by;
	@Column(name = "reported_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date reported_date;
	@Column(name = "project_name", length = 50)
	private String project_name;
	@Column(name = "module_name", length = 50)
	private String module_name;
	@Column(name = "menu_name", length = 50)
	private String menu_name;
	@Column(name = "req_type", length = 20)
	private String req_type;
	@Column(name = "issue_type", length = 20)
	private String issue_type;
	@Column(name = "issue_description", length = 500)
	private String issue_description;
	@Column(name = "req_priority", length = 1)
	private String req_priority;
	@Column(name = "parent_req_code", length = 20)
	private String parent_req_code;
	@Column(name = "req_error_group_str", length = 500)
	private String req_error_group_str;
	@Column(name = "sample_data_filter_str", length = 500)
	private String sample_data_filter_str;
	@Column(name = "reinitiate_pm_work_flag", length = 1)
	private String reinitiate_pm_work_flag;
	@Column(name = "reinitiate_pm_work_remark", length = 100)
	private String reinitiate_pm_work_remark;
	@Column(name = "approved_by", length = 20)
	private String approved_by;
	@Column(name = "approved_by_status", length = 1)
	private String approved_by_status;
	@Column(name = "approved_by_remark", length = 500)
	private String approved_by_remark;
	@Column(name = "assigned_to_dev", length = 20)
	private String assigned_to_dev;
	@Column(name = "assigned_dev_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date assigned_dev_date;
	@Column(name = "dev_status", length = 1)
	private String dev_status;
	@Column(name = "dev_remark", length = 500)
	private String dev_remark;
	@Column(name = "dev_close_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dev_close_date;
	@Column(name = "assigned_to_qc", length = 20)
	private String assigned_to_qc;
	@Column(name = "assigned_qc_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date assigned_qc_date;
	@Column(name = "testing_status", length = 1)
	private String testing_status;
	@Column(name = "testing_remark", length = 500)
	private String testing_remark;
	@Column(name = "testing_closed_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date testing_closed_date;
	@Column(name = "deploy_delivered_by", length = 20)
	private String deploy_delivered_by;
	@DateTimeFormat(pattern="dd-MM-yyyy")
	@Column(name = "deploy_delivered_date")
	private Date deploy_delivered_date;
	@Column(name = "closure_remark", length = 500)
	private String closure_remark;
	@Column(name = "closure_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date closure_date;
	@Column(name = "req_closed_by", length = 20)
	private String req_closed_by;
	@Column(name = "lastupdate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag", length = 1)
	private String flag;
	@Column(name = "current_req_status", length = 500)
	private String current_req_status;
//	@Column(name = "attachment_file")
//	private byte[] attachment_file;
	@Column(name = "reinitiate_work_assigned_to", length = 20)
	private String reinitiate_work_assigned_to;
	
	@Column(name = "deploy_fail_req_assigned_to", length = 20)
	private String deploy_fail_req_assigned_to;
	@Column(name = "deploy_type", length = 5)
	private String deploy_type;
	@Column(name = "assigned_to_func" , length = 20)
	private String assigned_to_func;
	@Column(name = "assigned_func_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date assigned_func_date;
	@Column(name = "final_status", length = 1)
	private String final_status;
	@Column(name = "reopened_status", length = 1)
	private String reopened_status;
	@Column(name = "deploy_status", length = 1)
	private String deploy_status;
	@Column(name = "client_final_status", length = 1)
	private String client_final_status;
	@Column(name = "func_remark", length = 500)
	private String func_remark;
	@Column(name = "func_status", length = 1)
	private String func_status;
	@Column(name = "req_entered_mode" , length = 1)
	private String req_entered_mode;
	@Column(name = "reopened_remark", length = 500)
	private String reopened_remark;
	@Column(name = "reopened_dev_name", length = 500)
	private String reopened_dev_name;
	
	
	@Transient
	transient private MultipartFile onsite_req_att;


	@Transient
	private MultipartFile doc_file;
	
	@Transient
	private List<MultipartFile> doc_file1;

}
