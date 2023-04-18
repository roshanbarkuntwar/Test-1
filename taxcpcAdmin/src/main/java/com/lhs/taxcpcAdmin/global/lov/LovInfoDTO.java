/**
 * 
 */
package com.lhs.taxcpcAdmin.global.lov;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LovInfoDTO {

	private String lovId;
    private String header;
    private String instantSearch;
    private String displayDataOnLoad;
    private String dataList;
    private String postBackCols;
    private String paginationEnabled;

}// End Class
