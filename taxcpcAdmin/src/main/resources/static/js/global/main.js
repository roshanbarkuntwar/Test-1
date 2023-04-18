/**
 * Alert callback: An assessment change confirm alert modal.
 */
function changeAssessmentDialog() {
	document.getElementById("dialog-confirm_change_assessment").style.display = "block";
	$(function() {
		$("#dialog-confirm_change_assessment").dialog({
			resizable : false,
			font : 10,
			modal : true,
			buttons : {
				"Yes" : function() {
					$("#dialog-confirm_change_assessment").dialog("close");
					changeAssessment();
				},
				"No" : function() {
					$("#dialog-confirm_change_assessment").dialog("close");
				}
			}
		});
	});
}// end

/**
 * Changing the assessment settings accordingly.
 * 
 * @returns
 */
function changeAssessment() {

	let asmtYear = $('#accYear').val();
	let asmtQuarter = $('#quarter').val();
	let asmtTdsType = $('#tdsType').val();
	let asmtMonth = $('#month').val();

	let actionUrl = './changeAssessment';
	let reqBody = {
		accYear : asmtYear,
		quarterNo : asmtQuarter,
		tdsTypeCode : asmtTdsType,
		month : asmtMonth
	};

	$.ajax({
		url : actionUrl,
		type : 'POST',
		data : JSON.stringify(reqBody),
		dataType : 'text',
		contentType : 'application/json',
		success : function(response, textStatus, jqXHR) {
			if (textStatus === 'success') {
				window.location.reload();
			}
		},
		error : function(error, status, jqXHR) {
			window.location.reload();
		}
	});
}// end

/**
 * This function is executed once upon login success. This call actually sets up
 * the global parameters that can be used throughout the project.
 * 
 * @returns
 */
function doProcessGlobalParameters() {
	let actionUrl = './processGlobalParams';
	$.ajax({
		url : actionUrl,
		type : 'GET',
		success : function(response, textStatus, jqXHR) {
			if (textStatus === 'success' && response === 'SUCCESS') {
				window.location.reload();
			} else if (textStatus === 'error') {
				console.log('Error: While setting up application global parameters!');
			}
		}
	});
}// end

/**
 * A global function declaration for calling the DB procedures using ajax call.
 * 
 * @param {type}
 *            procBody
 * @returns {undefined}
 */
function callDedicatedProcedure(procBody) {

	try {
		$.ajax({
			url : './executeCommonProcedureCallback',
			type : 'POST',
			data : JSON.stringify(procBody),
			dataType : 'text',
			contentType : 'application/json',
			async : true
		});
	} catch (e) {
		console.log('%cProcedure calling ajax error', 'color: red;');
		console.log(e);
	}
	console.log('%cCalling ' + procBody.callingProcName + ' procedure ajax completed...' + procBody.tokenNo,
			'font-style: italic;');
}// end function

/**
 * A global function for generating a new token for a provided process
 * name/type.
 * 
 * This function every time returns a unique token when called.
 * 
 * @param paramsObj
 * @returns
 */
function globalTokenGenerate(paramsObj, callbackFunction) {
	$.ajax({
		url : './executeTokenProcedureCallback',
		type : 'POST',
		data : JSON.stringify(paramsObj),
		dataType : 'text',
		contentType : 'application/json',
		cache : false,
		success : function(response, textStatus, jqXHR) {

			if (textStatus === 'success' && !lhsIsNull(response)) {
				let parsedToken = -1;
				try {
					parsedToken = parseInt(response.trim());
					callbackFunction(parsedToken);
				} catch (e) {
					parsedToken = -1;
				}

				return parsedToken;

			} else if (textStatus === 'success' && !lhsIsNull(callbackFunction)) {
				callbackFunction(-1);
			} else {
				return -1;
			}
		},
		error : function(jqXHR, status, error) {
			jsAjaxErrorLogger(jqXHR, status, error);

			return -1;
		}
	});
}// end

function jsAjaxErrorLogger(jqXHR, status, error) {
	console.log('%cError in ajax request:', 'color: red; font-weight: bold;');
	console.log('%c' + JSON.stringify(jqXHR), 'color: red;');
	console.log('%c' + status, 'color: red;');
	console.log('%c' + JSON.stringify(error), 'color: red;');
}// end

function downloadStaticExcel() {
	var table = getSimpleTable($('#table'));
	$('div.page-section').append("<div class=\"d-none\" id=\"staticTableContent\"></div>");
	$('#staticTableContent').html(table);

	// call for table2excel plugin to export HTML data into EXCEL
	$("#staticTable").table2excel({// specifying excel file properties
		// filename: fileName,
		fileHeading : "Import Process Details",
		preserveColors : false
	});
	$('#staticTableContent').remove();
}

function getSimpleTable(tableElement) {
	var temptable = getTemporaryTable(tableElement);
	try {
		var table = "<table id=\"staticTable\"> <thead> <tr>";
		var tableHeader = $(temptable).find("thead tr:first").html();
		tableHeader = tableHeader.replace(/<div[^>]*>|<\/div>/gi, "");
		tableHeader = tableHeader.replace(/<i[^>]*>|<\/i>/gi, "");
		tableHeader = tableHeader.replace(/<hr>/gi, "</th><th>");
		tableHeader = tableHeader.replace(/id="[^"]*"/gi, "");
		tableHeader = tableHeader.replace(/title="[^"]*"/gi, "");
		table += tableHeader + "</tr> </thead> \n\n ";
	} catch (e) {
	}
	try {
		table += "<tbody>";
		var tableBody = $(temptable).find("tbody").html();
		tableBody = tableBody.replace(/<form[^>]*>|<\/form>/gi, "");
		tableBody = tableBody.replace(/<div[^>]*>|<\/div>/gi, "");
		tableBody = tableBody.replace(/<i[^>]*>|<\/i>/gi, "");
		tableBody = tableBody.replace(/<a[^>]*>|<\/a>/gi, "");
		tableBody = tableBody.replace(/<hr>/gi, "</td><td>");
		tableBody = tableBody.replace(/id="[^"]*"/gi, "");
		tableBody = tableBody.replace(/title="[^"]*"/gi, "");
		table += tableBody + "</tbody>";
	} catch (e) {
	}
	return table;
}// end function

function getTemporaryTable(tableElement) {
	var temptable = $(tableElement).clone();
	$.each($(temptable).find("tbody tr"), function() {
		var _row = $(this);
		$.each($(_row).find("td"), function(i) {
			try {
				if ($(this).hasClass("d-none")) {
					$(this).remove();
				} else if ($(this).has('input[type=text]')) {
					var inputvalue = this.childNodes[0].value;
					console.log(inputvalue);
					$(this).html(inputvalue);
				}
			} catch (e) {
				console.log(i);
				console.log(e);
			}
		});

		// getting each dropdown field value
		$.each($(_row).find("td select"), function(i) {
			try {
				var text = $(this).find(":selected").html();
				if (!lhsIsNull(text) && !text.toLowerCase().includes("-select-")) {
					$(this).parent().html(text);
				} else if (!lhsIsNull(text) && text.toLowerCase().includes("-select-")) {
					$(this).parent().html('');
				} else if (lhsIsNull(text)) {
					$(this).parent().html('');
				}
			} catch (e) {
				console.log('select ' + i);
				console.log(e);
			}
		});
	});
	return temptable;
}// end function
