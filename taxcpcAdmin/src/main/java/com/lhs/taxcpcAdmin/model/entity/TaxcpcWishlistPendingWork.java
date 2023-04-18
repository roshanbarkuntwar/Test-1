package com.lhs.taxcpcAdmin.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "taxcpc_wishlist_pending_work")
public class TaxcpcWishlistPendingWork implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "work_code", nullable = false)
	private Long work_code;
	@Column(name = "work_type", length = 1)
	private String work_type;
	@Column(name = "work_nature", length = 1)
	private String work_nature;
	@Column(name = "work_priority", length = 5)
	private String work_priority;
	@Column(name = "project_code", length = 20)
	private String project_code;
	@Column(name = "completion_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date completion_date;
	@Column(name = "user_code", length = 20)
	private String user_code;
	@Column(name = "work_description", length = 4000)
	private String work_description;
	@Column(name = "remark", length = 200)
	private String remark;
	@Column(name = "lastupdate")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag", length = 1)
	private String flag;
	@Column(name = "status", length = 1)
	private String status;
	
	@Column(name = "external_link", length = 1)
	private String external_link;
	
	@Transient
	transient private MultipartFile doc_file;
	
	public Date getCompletion_date() {
		return this.completion_date;
	}

	public void setCompletion_date(String completion_date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date d = sdf.parse(completion_date);
			this.completion_date = d;
		} catch (Exception e) {
			this.completion_date = null;
		}
	}

}
