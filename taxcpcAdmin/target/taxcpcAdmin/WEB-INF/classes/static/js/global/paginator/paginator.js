/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var xmlHttp = "";

var ajax_call_enabled = false;

var paginatorFormId = "";
var callBackFun;

function make_ajax_call(url, resuting_call) {
	// alert("url-->"+url)
	if (!ajax_call_enabled) {
		if (typeof XMLHttpRequest !== "undefined") {
			xmlHttp = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (xmlHttp === null) {
			openErrorModal('Browser not support', '');
			return;
		}
		ajax_call_enabled = true;
		xmlHttp.onreadystatechange = resuting_call;
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
	} else {
		openErrorModal('Please wait..\nYour previous request is in progress',
				'');
	}
};// End Function

function getCurrentPageData1(pageNum) {
	var pageNumber = pageNum;
	currentPageNo = pageNum;
	var recordsPerPage = document.getElementById("recordsPerPageSelect").value;
	var totalRecords = document.getElementById("totalRecordsSpan").innerHTML
			.trim();
	var dataGridAction = document.getElementById("dataGridAction").value;
	var FilterIds = document.getElementById("FilterIds").value;

	var url = dataGridAction + "?";
	url += "search=true";
	url += "&pageNumber=" + encodeURIComponent(pageNumber);
	url += "&recordsPerPage=" + encodeURIComponent(recordsPerPage);
	url += "&totalRecords=" + encodeURIComponent(totalRecords);
	if (FilterIds !== null && FilterIds !== "null" && FilterIds !== "") {
		try {
			var FilterIdsSplit = FilterIds.split("#");
			for (var i = 0; i < FilterIdsSplit.length; i++) {
				if (FilterIdsSplit[i] !== "" && FilterIdsSplit[i] !== "null"
						&& FilterIdsSplit[i] !== null) {
					var splitData = FilterIdsSplit[i].split("~");
					url += "&"
							+ splitData[0]
							+ "="
							+ encodeURIComponent(document
									.getElementById(splitData[1]).value);
				}
			}
		} catch (err) {
		}
	}
	make_ajax_call(url, setCurrentPage);
}// End Function

function setCurrentPage() {
	if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
		var q = xmlHttp.responseText;
		try {
			q = q.trim();
			// alert(q);
			document.getElementById("dataSpan").innerHTML = q;
			try {
				document.getElementById("pageNumberSpan").innerHTML = currentPageNo;
			} catch (e) {
				ajax_call_enabled = false;
			}
			ajax_call_enabled = false;
		} catch (err) {
			ajax_call_enabled = false;
		}
	}
}// End Function

function getPreviousPage() {
	if (!ajax_call_enabled) {
		document.getElementById("pagination_number").value = "";

		var page_count = document.getElementById("pageNumberSpan").innerHTML;
		page_count = parseInt(page_count);

		if (page_count <= 1) {
			openErrorModal('Can not move to previous page', '');
		} else {
			var count = page_count - 1;
			getCurrentPageData(count);
		}
	} else if (ajax_call_enabled) {
		openErrorModal('Please wait..\nYour previous request is in progress',
				'');
	}
}// End Function

function getNextPage() {

	if (!ajax_call_enabled) {
		document.getElementById("pagination_number").value = "";
		var page_count = document.getElementById("pageNumberSpan").innerHTML;
		var total_count = document.getElementById("totalPagesSpan").innerHTML;

		page_count = parseInt(page_count);
		total_count = parseInt(total_count);

		if (page_count >= total_count) {
			openErrorModal('Can not move to next page', '');
		} else {
			var count = page_count + 1;
			getCurrentPageData(count);
		}
	} else if (ajax_call_enabled) {
		openErrorModal('Please wait..\nYour previous request is in progress',
				'');
	}
	// End Function
}

function jumpToPage(id) {
	if (!ajax_call_enabled) {
		var page_count = 0;
		if (id === 'topBtn') {
			page_count = document.getElementById("pagination_number").value;
			document.getElementById("pagination_number").value = "";
		}
		try {
			page_count = parseInt(page_count);
		} catch (error) {
			page_count = 0;
		}
		if (!isNaN(page_count)) {
			var total_count = document.getElementById("totalPagesSpan").innerHTML;
			total_count = parseInt(total_count);
			if (page_count > total_count || page_count === 0) {
				openErrorModal('Invalid page number', '');
			} else {
				var count = page_count;
				getCurrentPageData(count);
			}
		} else {
			openErrorModal('Invalid page number', '');
		}
	} else if (ajax_call_enabled) {
		openErrorModal('Please wait..\nYour previous request is in progress',
				'');
	}
}// End Function

function defaultValues() {
	var recordsPerPage = document.getElementById("recordsPerPage").value;
	if (recordsPerPage === '10') {
		document.getElementById("recordsPerPageSelect10").selected = true;
		document.getElementById("recordsPerPageSelect20").selected = false;
		document.getElementById("recordsPerPageSelect50").selected = false;
		document.getElementById("recordsPerPageSelect100").selected = false;
		document.getElementById("recordsPerPageSelectAll").selected = false;
	} else if (recordsPerPage === '20') {
		document.getElementById("recordsPerPageSelect10").selected = false;
		document.getElementById("recordsPerPageSelect20").selected = true;
		document.getElementById("recordsPerPageSelect50").selected = false;
		document.getElementById("recordsPerPageSelect100").selected = false;
		document.getElementById("recordsPerPageSelectAll").selected = false;

	} else if (recordsPerPage === '50') {
		document.getElementById("recordsPerPageSelect10").selected = false;
		document.getElementById("recordsPerPageSelect20").selected = false;
		document.getElementById("recordsPerPageSelect50").selected = true;
		document.getElementById("recordsPerPageSelect100").selected = false;
		document.getElementById("recordsPerPageSelectAll").selected = false;

	} else if (recordsPerPage === '100') {
		document.getElementById("recordsPerPageSelect10").selected = false;
		document.getElementById("recordsPerPageSelect20").selected = false;
		document.getElementById("recordsPerPageSelect50").selected = false;
		document.getElementById("recordsPerPageSelect100").selected = true;
		document.getElementById("recordsPerPageSelectAll").selected = false;
	} else if (recordsPerPage === 'ALL') {
		document.getElementById("recordsPerPageSelect10").selected = false;
		document.getElementById("recordsPerPageSelect20").selected = false;
		document.getElementById("recordsPerPageSelect50").selected = false;
		document.getElementById("recordsPerPageSelect100").selected = false;
		document.getElementById("recordsPerPageSelectAll").selected = true;
	}

}// End Function


function getCurrentPageOnChange() {
	
	document.getElementById("recordsPerPage").value = document.getElementById("recordsPerPageSelect").value;
	
//	alert("Records per page : " + document.getElementById("recordsPerPage").value);
//	alert("paginatorFormId.."+paginatorFormId);

	document.getElementById(paginatorFormId).submit();
}// End Function

// ************new Function************
function loadDataUsingPaginator(formId, callBackFunction) {
	// alert("formId.."+formId);
	try {
		paginatorFormId = formId;
		try {
			defaultValues();
		} catch (e) {
		}

		if (callBackFunction != null && callBackFunction != undefined) {
			callBackFun = callBackFunction;
		}

		var dataGridAction = document.getElementById("dataGridAction").value;
		if (dataGridAction !== undefined && dataGridAction !== ""
				&& dataGridAction !== "null" && dataGridAction !== null) {
			var pageNumber = document.getElementById("pageNumber").value;
			var totalRecords = document.getElementById("totalRecords").value;
			var totalPages = document.getElementById("totalPages").value;
			var recordsPerPage = document
					.getElementById("recordsPerPageSelect").value;
			document.getElementById("totalRecordsSpan").innerHTML = totalRecords;
			document.getElementById("totalPagesSpan").innerHTML = totalPages;
			document.getElementById("pageNumberSpan").innerHTML = pageNumber;
			document.getElementById("recordsPerPage").value = recordsPerPage;
			getCurrentPageData(pageNumber);

		}
	} catch (err) {

	}
}// End Function

function loadDataUsingPaginatorGrid() {

	var pageNumber = document.getElementById("pageNumbergrid").value;
	var totalRecords = document.getElementById("totalrecordsgrid").value;
	var totalPages = document.getElementById("totalPagesgrid").value;
	var recordsPerPage = document.getElementById("recordsPerPageSelectgrid").value;
	document.getElementById("totalRecordsSpan").innerHTML = totalRecords;
	document.getElementById("totalPagesSpan").innerHTML = totalPages;
	document.getElementById("pageNumberSpan").innerHTML = pageNumber;
	document.getElementById("recordsPerPage").value = recordsPerPage;
	
	getCurrentPageData(pageNumber);
}

function getCurrentPageData(pageNum) {

	// document.getElementById("global-process-indicator").style.display =
	// "block";
	var pageNumber = pageNum;
	currentPageNo = pageNum;
	var recordsPerPage = document.getElementById("recordsPerPageSelect").value;
	var totalRecords = document.getElementById("totalRecordsSpan").innerHTML;
	var dataGridAction = document.getElementById("dataGridAction").value;
	document.getElementById("pageNumber").value = pageNumber;
	document.getElementById("recordsPerPage").value = recordsPerPage;
	// alert("recordsPerPage.."+recordsPerPage)

	// var url = dataGridAction + "?";
	// url += "search=true";
	// url += "&pageNumber=" + encodeURIComponent(pageNumber);
	// url += "&recordsPerPage=" + encodeURIComponent(recordsPerPage);
	// url += "&totalRecords=" + encodeURIComponent(totalRecords);

	var url = dataGridAction;
	ajaxPaginatorSubmitPostData(url);
}// End Function

function ajaxPaginatorSubmitPostData(urlvar) {
	// alert("submit--"+urlvar);
	showProcessIndicator();
	if (!ajax_call_enabled) {
		var dataGridAction = document.getElementById("dataGridAction").value;
		var formElement = document.getElementById(paginatorFormId);
		ajax_call_enabled = true;
		$
				.ajax({
					type : 'POST',
					url : urlvar,
					data : new FormData($(formElement)[0]),
					async : false,
					cache : false,
					contentType : false,
					processData : false,
					success : function(response) {
						hideProcessIndicator();
						try {
							// console.log(response);
							document.getElementById("dataSpan").innerHTML = response;
						} catch (err) {
						}
						// ------- For Item Rate Master Form & User Roles
						// Dashboard --------
						var id = $("#firstRecordId").val();
						// console.log("firstRecordId --- "+id);
						if (!lhsIsNull(id)) {
							setRowValue(id);
						}
						// ----------------------END--------------------
						try {
							document.getElementById("pageNumberSpan").innerHTML = currentPageNo;
						} catch (e) {
							ajax_call_enabled = false;
						}
						ajax_call_enabled = false;
						try {
							eval(callBackFun);
						} catch (e) {
							console.log(e);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						hideProcessIndicator();
						console.log("errorThrown : " + errorThrown);
						ajax_call_enabled = false; // if fails
					}
				});
	} else {
		// window.alert("Please wait..\nYour previous request is in progress");
		openErrorModal('Please wait..\nYour previous request is in progress',
				'');
	}
}// End Function

function fnExcelReport() {
	var tab_text = "<table><tr>";
	var textRange;
	var j = 0;
	tab = document.getElementById('dataSpanTable'); // id of table

	for (j = 0; j < tab.rows.length; j++) {
		tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
		// tab_text=tab_text+"</tr>";
	}

	tab_text = tab_text + "</table>";
	tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, "");// remove if u want
														// links in your table
	tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want
														// images in your table
	tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves
																	// input
																	// params

	var ua = window.navigator.userAgent;
	var msie = ua.indexOf("MSIE ");

	if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) // If
																		// Internet
																		// Explorer
	{
		txtArea1.document.open("txt/html", "replace");
		txtArea1.document.write(tab_text);
		txtArea1.document.close();
		txtArea1.focus();
		sa = txtArea1.document.execCommand("SaveAs", true, "Download Data.xls");
	} else
		// other browser not tested on IE 11
		sa = window.open('data:application/vnd.ms-excel,'
				+ encodeURIComponent(tab_text));

	return (sa);
}

function exportToExcel() {
	var htmls = "";
	var uri = 'data:application/vnd.ms-excel;base64,';
	var template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>';
	var base64 = function(s) {
		return window.btoa(unescape(encodeURIComponent(s)))
	};

	var format = function(s, c) {
		return s.replace(/{(\w+)}/g, function(m, p) {
			return c[p];
		})
	};

	htmls = document.getElementById('dataSpan').innerHTML;

	var ctx = {
		worksheet : 'Worksheet',
		table : htmls
	}

	var link = document.createElement("a");
	link.download = "Download Data.xls";
	link.href = uri + base64(format(template, ctx));
	link.click();
}
