/**
 * 
 */
package com.lhs.taxcpcAdmin.global.pagination;

import lombok.Data;

/**
 * @author ayushi.jain
 */
@Data
public class PaginatorDTO {
	
	private String recordsPerPage;
	private long pageNumber;
	private long totalRecords;
	private long totalPages;
	private String dataGridAction;
	private String client_mode;
	
}
