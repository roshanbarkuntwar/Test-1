
package com.lhs.taxcpcAdmin.model.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import oracle.sql.NUMBER;

/**
 *
 * @author sakshi.bandhate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_mast")
public class UserMast implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7925328400089676957L;

	@Id
	@GenericGenerator(name = "generator", strategy = "sequence-identity", parameters = @Parameter(name = "sequence", value = "user_code_seq"))
	@GeneratedValue(generator = "generator")
	@Column(name = "user_code" ,length=15,nullable = false)
	private String user_code;
	@Column(name = "user_name" ,length=100)
	private String user_name;
	@Column(name = "user_type" ,length=5)
	private String user_type;
	@Column(name = "login_id" ,length=100,nullable = false)
	private String loginId;
	@Column(name = "login_pwd" ,length=100,nullable = false)
	private String loginPwd;
	@Column(name = "role_code" ,length=20,nullable = false)
	private String role_code;
	@Column(name = "dept_str" ,length=50)
	private String dept_str;
	@Column(name = "user_status" ,length=1)
	private String user_status;
	@Column(name = "user_mode" ,length=1)
	private String user_mode;
	@Column(name = "email" ,length=100)
	private String email;
	@Column(name = "phone_no" ,length=10)
	private String phone_no;
	@Column(name = "created_by" ,length=15)
	private String created_by;
	@Column(name = "created_date" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date created_date;
	@Column(name = "lastupdate" )
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date lastupdate;
	@Column(name = "flag" ,length=1)
	private String flag;
	@Column(name = "edit_right")
	private String edit_right;
	@Column(name = "add_right")
	private String add_right;
	@Column(name = "delete_right")
	private String delete_right;
	@Column(name = "favourite_menu",length=100)
	private String favourite_menu;
	
	@Column(name = "entity_code",length=2)
	private String entity_code;
	
//	@Column(name = "login_count", length=100)
//	private String login_count;
//	
//	@Column(name = "last_login")
//	private Timestamp last_login;
	
	@Transient
	private String login_count;
	
	@Transient
	private Timestamp last_login;
	
	

	
}// End Class
