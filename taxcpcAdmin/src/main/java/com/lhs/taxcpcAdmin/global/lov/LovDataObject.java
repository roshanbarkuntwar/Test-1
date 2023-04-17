/**
 * 
 */
package com.lhs.taxcpcAdmin.global.lov;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sakshi.bandhate
 *
 */
@Data
@NoArgsConstructor
public class LovDataObject {

	private String header;
	private String query;
	private String validateLovCode;
	private ArrayList<String> display_cols;
	private ArrayList<String> search_field_cols;
	private String post_back_cols_f8;
	private String post_back_cols_f9;
	private String search_on_keypress;
	private String multiselect;
	private String callableStatement;  // begin end to be called before query
	private String paginationRequired;

}// End Class
