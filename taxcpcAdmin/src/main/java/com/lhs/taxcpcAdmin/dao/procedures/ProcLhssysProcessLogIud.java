/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lhs.taxcpcAdmin.dao.procedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import javax.transaction.Transactional;

import org.hibernate.JDBCException;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lhs.taxcpcAdmin.dao.generic.GlobalDoWorkExecuter;

@Component
@Transactional
public class ProcLhssysProcessLogIud {

	@Autowired
	private GlobalDoWorkExecuter doWorkExecuter;

	public void executeProcedure(final Long a_process_seqno, final String a_entity_code,
			final String a_client_code,
			final String a_acc_year,
			final int a_quarter_no,
			final Date a_from_date,
			final Date a_to_date,
			final String a_tds_type_code,
			final Long a_client_login_session_seqno,
			final String a_month,
			final String a_process_status,
			final String a_process_remark,
			final String a_file_name,
			final String a_file_no_of_lines,
			final String a_file_no_of_record,
			final Long a_process_sid,
			final Long a_process_serial,
			final String a_process_type,
			final String a_user_code,
			final String a_iud_type,
			final String templateCode,
			final String a_record_insert_flag,
			final String a_report_name,
			final String a_proc_string) {

		try {
			Work work = new Work() {
				@Override
				public void execute(Connection cnctn) throws SQLException {
					try {
						System.out.println("*** Token Generation Parameter ***");
						System.out.println("Process Seqno: " + a_process_seqno);
						System.out.println("Entity Code: " + a_entity_code);
						System.out.println("Client Code: " + a_client_code);
						System.out.println("Acc Year: " + a_acc_year);
						System.out.println("Quarter No: " + a_quarter_no);
						System.out.println("From Date: " + a_from_date);
						System.out.println("To Date: " + a_to_date);
						System.out.println("TDS Type Code: " + a_tds_type_code);
						System.out.println("Client Login Sequence: " + a_client_login_session_seqno);
						System.out.println("Month: " + a_month);
						System.out.println("Process Status: " + a_process_status);
						System.out.println("Process Remark: " + a_process_remark);
						System.out.println("File Name: " + a_file_name);
						System.out.println("File No Of Lines: " + a_file_no_of_lines);
						System.out.println("File No Of Records: " + a_file_no_of_record);
						System.out.println("Process SID: " + a_process_sid);
						System.out.println("Process Serial: " + a_process_serial);
						System.out.println("Process Type: " + a_process_type);
						System.out.println("User Code: " + a_user_code);
						System.out.println("IUD Type: " + a_iud_type);
						System.out.println("Template Code: " + templateCode);
						System.out.println("Record Insert Flag: " + a_record_insert_flag);
						System.out.println("Report Name: " + a_report_name);
						System.out.println("Proc String: " + a_proc_string);

						java.sql.Date sql_a_from_date = new java.sql.Date(a_from_date.getTime());
						java.sql.Date sql_a_to_date = new java.sql.Date(a_to_date.getTime());

						CallableStatement cs;
						String proc = "{call lhs_utility.proc_lhssys_process_log_iud(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
						cs = cnctn.prepareCall(proc);

						if (a_process_seqno != null) {
							cs.setLong(1, a_process_seqno);
						} else {
							cs.setNull(1, java.sql.Types.INTEGER);

						}

						cs.setString(2, a_entity_code);// entity code
						cs.setString(3, a_client_code);// client code
						cs.setString(4, a_acc_year);// acc_year
						cs.setInt(5, a_quarter_no);// quarter no.
						cs.setDate(6, sql_a_from_date);// from date
						cs.setDate(7, sql_a_to_date);// to date
						cs.setString(8, a_tds_type_code);
						cs.setLong(9, a_client_login_session_seqno);
						cs.setString(10, a_month);
						cs.setDate(11, null);
						cs.setDate(12, null);
						cs.setString(13, a_process_status);
						cs.setString(14, a_process_remark);
						cs.setString(15, a_file_name);
						cs.setString(16, a_file_no_of_lines);
						cs.setString(17, a_file_no_of_record);

						if (a_process_sid != null) {
							cs.setLong(18, a_process_sid);
						} else {
							cs.setLong(18, java.sql.Types.INTEGER);

						}
						if (a_process_serial != null) {

							cs.setLong(19, a_process_serial);
						} else {
							cs.setLong(19, java.sql.Types.INTEGER);

						}
						cs.setString(20, a_process_type);
						cs.setString(21, a_user_code);
						cs.setString(22, a_iud_type);// use for proccess insert in lhssys_process_log indicate process is start
						cs.setString(23, templateCode);
						cs.setString(24, a_record_insert_flag);
						cs.registerOutParameter(25, Types.VARCHAR);// out parameter
						cs.setString(26, null);
						cs.setString(27, null);
						cs.setString(28, null);
						cs.setString(29, a_report_name);

						cs.executeUpdate();
						System.out.println("procedure of lhssys_process_log_iud..."+proc);
						System.out.println("*** ProcLhssysProcessLogIud Procedure Execution Completed! 1***");
						

						try {
							cs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}

				}
			};

			doWorkExecuter.getSession().doWork(work);
		} catch (JDBCException ex) {
			ex.printStackTrace();
		}
	}
}
