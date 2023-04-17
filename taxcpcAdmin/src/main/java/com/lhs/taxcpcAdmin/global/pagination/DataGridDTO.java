package com.lhs.taxcpcAdmin.global.pagination;

import lombok.Data;

/**
 * @author ayushi.jain
 */
@Data
public class DataGridDTO {

	private PaginatorDTO paginator;
	private FilterDTO filter;
	private String currentPageNo;

}// end function
