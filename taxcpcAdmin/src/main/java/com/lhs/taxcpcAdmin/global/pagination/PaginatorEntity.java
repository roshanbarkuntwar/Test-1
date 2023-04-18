/**
 * 
 */
package com.lhs.taxcpcAdmin.global.pagination;

import lombok.Data;

/**
 * @author ayushi.jain
 */
@Data
public class PaginatorEntity {
	
	private String recordsPerPage;
	private long minVal;
	private long total;
	
}
