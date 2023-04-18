function getWarFileName() {
	var projectCode = $('#project_code').val();
	if (!lhsIsNull(projectCode)) {
		$('#war_filename').val("");
		var url = "./getWarFileName?projectCode=" + projectCode;
		ajaxPostUrl(url, getWarFileNameResponse);
	}
}

function getWarFileNameResponse(response) {
	if (!lhsIsNull(response)) {
		$('#war_filename').val(response);
	} else {
		$('#war_filename').val("");
	}
}

function saveDeployment() {
	if (validateForm()) {
		ajaxSubmitPostData("saveDeployment", "deploymentForm",
				saveDeploymentResponse);
	}
}

function saveDeploymentResponse(response) {
	if (response === "success") {
		// var action = $('#action').val();
		// if (action == "save") {
		openSuccessModal("Data saved successfully !",
				"window.location.href='./deploymentDetail';");
		// }else if (action == "edit") {
		// openSuccessModal("Data updated successfully !"
		// ,"window.location.href='./projectDictionary';");
		// }
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function validateForm() {
	var status;
	var project_code = $('#project_code').val();
	var war_filename = $('#war_filename').val();
	var depl_code = $('#depl_code').val();
	var server_ip = $('#server_ip').val();
	var server_port = $('#server_port').val();
	var remark = $('#remark').val();
	
	var temp = document.getElementById("depl_code").value;

	if (lhsIsNull(project_code)) {
		openErrorModal("Please select Application Name.");
		status = false;
	} else if (lhsIsNull(depl_code)) {
		openErrorModal("Please select  Deployment Type.");
		status = false;
	} else if(remark.length > 500 ){

		openErrorModal("Remark Exceed Maximum Limit Of Characters.");
		status = false;
	} else if (depl_code === 'APP_WAR_FILE') {
		if (lhsIsNull(war_filename)) {
			openErrorModal("Please enter  WAR file Name.");
			status = false;
		} else if (lhsIsNull(server_ip)) {
			openErrorModal("Please enter server URL.");
		} else if (lhsIsNull(server_port)) {
			openErrorModal("Please enter server port.");
		} else {
			var server_url = "http://" + server_ip + ":" + server_port;
			$('#server_url').val(server_url);
			status = true;
		}
	} else if (depl_code != 'APP_WAR_FILE') {
		if (lhsIsNull(server_ip)) {
			openErrorModal("Please enter server IP.");
			status = false;
		} else {
			$('#server_url').val(server_ip);
			status = true;
		}
	}
	else {
		status = true;
	}

	return status;
}// End function

function getSampledata() {
	$("#sample_data").modal({
		show : true
	});
}

function clearUncheckedSampleValue(id) {
	var checkbox = document.getElementById(id).checked;
	if (!checkbox) {
		var splitId = id.split("~");
		document.getElementById("sample_code_txt~" + splitId[1]).value = "";
	}
}

function chkSampleCkBox(fld) {
	if (!lhsIsNull(fld.value)) {
		document.getElementById("sample_code~" + fld.id.split("~")[1]).checked = true;
	} else {
		document.getElementById("sample_code~" + fld.id.split("~")[1]).checked = false;
	}
}
var error_code = "";
function createString() {
	var code = document.getElementsByName("sample_code");
	error_code = "";
	for (var i = 0; i < code.length; i++) {
		if (code[i].checked) {

			var sample_code = code[i].value;
			var sample_id = code[i].id;
			var sam = sample_id.split("~");
			var value_txt = document
					.getElementById("sample_code_txt~" + sam[1]).value;
			error_code += sample_code + ":" + value_txt + "||";
		}
	}
	error_code = error_code.substring(0, error_code.length - 2);
	document.getElementById("sample_data_filter_str").value = error_code;
	$("#sample_data").modal('hide');
	// getErrorSubClassification(error_code);
}

function selectServerIp() {
	// var depl_code = document.getElementById("depl_code");
	var depl_code = $('#depl_code').val();

	if (depl_code === 'OTHER_CONFIG' || depl_code === 'DB_PACKAGE'
			|| depl_code === 'DB_OBJECT') {
		document.getElementById("serverUrl").innerHTML = "Database Server IP<span class='text-danger font-weight-bold ml-1'>*</span>";
		document.getElementById("ipPrefix").style.display = "none";
		document.getElementById("portPrefix").style.display = "none";
		document.getElementById("server_port").style.display = "none";
		document.getElementById("war_filename_div").style.display = "none";
	} else {
		document.getElementById("serverUrl").innerHTML = "Server URL<span class='text-danger font-weight-bold ml-1'>*</span>";
		document.getElementById("ipPrefix").style.display = "flex";
		document.getElementById("portPrefix").style.display = "flex";
		document.getElementById("server_port").style.display = "flex";
		document.getElementById("war_filename_div").style.display = "flex";
	}

}



function deploymentSearch() {

	var formdata = $('#deploymentDetail').serializeArray();
	var fdata = JSON.stringify(formdata);

	var project_name = document.getElementById("project_name").value;
	var war_file = document.getElementById("war_file").value;
	var depl_code = document.getElementById("depl_code").value;
	// alert("project name = "+project_name);
	// alert("war_file name = "+war_file);

	if (lhsIsNull(project_name) && lhsIsNull(war_file) && lhsIsNull(depl_code)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		
		var url = "./deploymentDetail?project_name=" + project_name
		+ "&depl_code=" + depl_code + "&war_file=" + war_file;
	
//		var url = "./getDelSearchData?project_name=" + project_name
//				+ "&depl_code=" + depl_code + "&war_file=" + war_file;
	}
	ajaxPostDataArray(url, fdata, deploymentSearchResponse);
}
function deploymentSearchResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();
} // function

function deploymentReset() {
	$("#deploymentDetail").find("input[type=text], textarea, select").val("");
	refreshPage();
}
function refreshPage() {
	var cp = document.getElementById("globalcontextpath").value;
	var url = cp + "./deploymentDetail";
	window.location = url;
}

function deleteRecord1(funName) {
	if (!lhsIsNull(checkedId)) {
		// funName(checkedId);
		window[funName](checkedId);
	}
}
function deleteDocdetails(id) {
	// alert(id);
	openConfirmModal("Do you want to delete ?", "deleteSelectedRow('" + id
			+ "')");
}

function deleteSelectedRow(id) {
	closeConfirmModal();
	var split_id = id.split('~');
	var seq_id = split_id[2];
	var url = "./deleteDocuments?seq_id=" + seq_id;

	ajaxPostUrl(url, deleteUserResponse);
}

function deleteUserResponse(response) {
	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "refresh()");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function
function refresh() {
	var url = "./deploymentDetail";
	window.location = url;
}

function viewRecord(funName) {
	if (!lhsIsNull(checkedId)) {
		// funName(checkedId);
		window[funName](checkedId);
	}
}
function viewDoc(id) {
//	if (!lhsIsNull(checkedId)) {
//	alert(id);
	var split_id = id.split('~');
	var seq_id = split_id[2];
//	 alert("seq_id <>>> "+seq_id);
	var url = "./deploymentEntryDetails?seq_id=" + seq_id;
	// window.location = url;

	ajaxPostUrl(url, viewDeploymentDetailsResponse);
//}
}

function viewDeploymentDetailsResponse(response) {
	// alert(""+response);
	if (!lhsIsNull(response)) {
		// alert("inside model fn");
//		 alert(response);

		$("#ModelTableBody").html("");
		$("#ModelTableBody").html(response);

		$("#myModal").modal({
			show : true
		});

	} else {
		$("#myModal").modal({
			show : true
		});
	}
}


////var checkedId = '';
//function openActionDiv(id) {
//	 var checkedId = '';
//	 alert(id);
//	var checkbox = document.getElementById(id).checked;
//	if (checkbox) {
//		var cbs = document.getElementsByName("actionCheckbox");
//		for (var i = 0; i < cbs.length; i++) {
//			cbs[i].checked = false;
//		}
//		document.getElementById(id).checked = true;
//		checkedId = id;
//		$("#actiondiv").css('display', 'block');
//	} else {
//		checkedId = '';
//		$("#actiondiv").css('display', 'none');
//	}
//}
function validateIPaddress(inputElement) {
	
	var ipaddress = inputElement.value;
	if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
			.test(ipaddress)) {
		return (true)
	}
	openErrorModal('You have entered an invalid IP address!', '')
	$('#' + inputElement.id).focus();
	inputElement.value = '';
	$('#host_name').val('');
	return (false)
}
function validatePortNumber(inputElement) {
	var ipaddress = inputElement.value;
	if (/^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/
			.test(ipaddress)) {
		return (true)
	}
	openErrorModal('You have entered an invalid Port Number!', '')
	$('#' + inputElement.id).focus();
	inputElement.value = '';
	return (false)
}

function refreshDeplDetails() {

	document.getElementById('sample_data_filter_str').value = '';

	document.getElementById('sample_code~1').checked = false;
	document.getElementById('sample_code~2').checked = false;
	document.getElementById('sample_code~3').checked = false;

	document.getElementById('sample_code_txt~1').value = '';
	document.getElementById('sample_code_txt~2').value = '';
	document.getElementById('sample_code_txt~3').value = '';
}
function closeDeplDetails() {
	if (error_code == "") {
		document.getElementById('sample_data_filter_str').value = '';

		document.getElementById('sample_code~1').checked = false;
		document.getElementById('sample_code~2').checked = false;
		document.getElementById('sample_code~3').checked = false;

		document.getElementById('sample_code_txt~1').value = '';
		document.getElementById('sample_code_txt~2').value = '';
		document.getElementById('sample_code_txt~3').value = '';
	} else {

	}

}
