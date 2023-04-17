package com.lhs.taxcpcAdmin.model;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Assessment {
	
	private String accYear;
    private String tdsTypeCode;
    private Integer quarterNo;
    private String month;
    private Date quarterFromDate;
    private Date quarterToDate;
    private Date yearBegDate;
    private Date yearEndDate;
}//End
