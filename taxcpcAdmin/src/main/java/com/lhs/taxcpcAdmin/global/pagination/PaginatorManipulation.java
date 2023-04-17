/**
 * 
 */
package com.lhs.taxcpcAdmin.global.pagination;

import com.lhs.javautilities.LhsStringUtility;

/**
 * @author ayushi.jain
 */
public class PaginatorManipulation {
	
	LhsStringUtility stringUtility;

	public PaginatorManipulation() {
		stringUtility = new LhsStringUtility();
	}// End Constructor

	public PaginatorDTO getPaginatorCount(String dataGrid, long total, String recNumber) {
		
		
		PaginatorDTO paginator = new PaginatorDTO();
		long pages = 0l;
		long pageNo = 0l;
		String recordsPerPage = null;
		try {
			if (total > 0) {
				recNumber = stringUtility.isNull(recNumber) ? "10" : recNumber;

				if (!recNumber.equalsIgnoreCase("ALL")) {
					long recsPerPage = 0l;
					int recVal = Integer.parseInt(recNumber);

					recsPerPage = recVal;
					long mod = total % recVal;
					long rem = 0;
					if (mod > 0) {
						rem = 1;
					}
					pages = (long) (Math.ceil(total / recVal) + rem);
					System.out.println("pages.."+pages);

					recordsPerPage = String.valueOf(recsPerPage);

				} else {
					pages = 1;
					recordsPerPage = "ALL";
				}
				if (pageNo > pages || pageNo == 0) {
					pageNo = 1;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in setting pagination values : " + e.getMessage());
		}
//		System.out.println("total101010..."+total);
		paginator.setDataGridAction(dataGrid);
		paginator.setTotalRecords(total);
		paginator.setTotalPages(pages);
		paginator.setRecordsPerPage(recordsPerPage);
		paginator.setPageNumber(pageNo);

		return paginator;
	}// End Method

	public PaginatorEntity getCalculatedValue(PaginatorDTO paginatorDTO) {
		PaginatorEntity entity = null;
		try {
			if (paginatorDTO != null) {
				long totalRecord = paginatorDTO.getTotalRecords();
				String recordsPerPage = paginatorDTO.getRecordsPerPage();
				


				long recsPerPage = (stringUtility.isNull(recordsPerPage) || recordsPerPage.equalsIgnoreCase("ALL")) ? totalRecord : Long.parseLong(recordsPerPage);
				long pagenumber = paginatorDTO.getPageNumber();

				if (totalRecord > 0 && recsPerPage > 0 && pagenumber > 0) {
					entity = new PaginatorEntity();
					long maxVal = totalRecord;
					long minVal = 1l;
					if (totalRecord > recsPerPage) {
						maxVal = (pagenumber * recsPerPage);
						
						long temp = recsPerPage;
						minVal = (maxVal - temp) + 1l;
						if (maxVal > totalRecord) {
							maxVal = totalRecord;
						}
					}
					entity.setMinVal(minVal - 1);
					entity.setTotal(totalRecord);
					entity.setRecordsPerPage(recordsPerPage);
				}
			}
		} catch (Exception e) {
		}
		
		return entity;
	}// End Method
	
public PaginatorDTO getPaginatorCountDoc(String dataGrid, long total, String recNumber) {
		
		
		PaginatorDTO paginator = new PaginatorDTO();
		long pages = 0l;
		long pageNo = 0l;
		String recordsPerPage = null;
//		System.out.println("recNumber.."+recNumber);
		try {
			if (total > 0) {
				recNumber = stringUtility.isNull(recNumber) ? "10" : recNumber;

				if (!recNumber.equalsIgnoreCase("ALL")) {
					long recsPerPage = 0l;
					int recVal = Integer.parseInt(recNumber);

					recsPerPage = recVal;
					long mod = total % recVal;
					long rem = 0;
					if (mod > 0) {
						rem = 1;
					}
					pages = (long) (Math.ceil(total / recVal) + rem);

					recordsPerPage = String.valueOf(recsPerPage);

				} else {
					pages = 1;
					recordsPerPage = "ALL";
				}
				if (pageNo > pages || pageNo == 0) {
					pageNo = 1;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in setting pagination values : " + e.getMessage());
		}
//		System.out.println("total101010..."+total);
		paginator.setDataGridAction(dataGrid);
		paginator.setTotalRecords(total);
		paginator.setTotalPages(pages);
		paginator.setRecordsPerPage(recordsPerPage);
		paginator.setPageNumber(pageNo);

		return paginator;
	}// End Method

	public PaginatorEntity getCalculatedValueDoc(PaginatorDTO paginatorDTO) {
		PaginatorEntity entity = null;
		try {
			if (paginatorDTO != null) {
				long totalRecord = paginatorDTO.getTotalRecords();
				//String recordsPerPage = paginatorDTO.getRecordsPerPage();
				
				String recordsPerPage = "8";


				long recsPerPage = (stringUtility.isNull(recordsPerPage) || recordsPerPage.equalsIgnoreCase("ALL")) ? totalRecord : Long.parseLong(recordsPerPage);
				long pagenumber = paginatorDTO.getPageNumber();

				if (totalRecord > 0 && recsPerPage > 0 && pagenumber > 0) {
					entity = new PaginatorEntity();
					long maxVal = totalRecord;
					long minVal = 1l;
					if (totalRecord > recsPerPage) {
						maxVal = (pagenumber * recsPerPage);
						
						long temp = recsPerPage;
						minVal = (maxVal - temp) + 1l;
						if (maxVal > totalRecord) {
							maxVal = totalRecord;
						}
					}
					entity.setMinVal(minVal - 1);
					entity.setTotal(totalRecord);
					entity.setRecordsPerPage(recordsPerPage);
				}
			}
		} catch (Exception e) {
		}
		
		return entity;
	}// End Method
	
}// End Class