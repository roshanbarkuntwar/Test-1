///Aman

function onLoadFunction() {
	document.getElementById('bulk_download_btn').style.display = "none";
//	document.getElementById('editAuditBtn').style.display = "none";
}

function searchFilter(audit_type) {
	if (lhsIsNull(audit_type)) {
		audit_type = document.getElementById('hidden_audit_type').value;

	} else {
		document.getElementById('hidden_audit_type').value = audit_type;
	}

	var formdata = $('#AuditDashboard').serializeArray();
	var fdata = JSON.stringify(formdata);

	var entity_code = document.getElementById("entity_code").value;
//	var search_value = document.getElementById("search_value").value;
	var project_name = document.getElementById("project_name").value;

	// if (lhsIsNull(entity_code) && lhsIsNull(search_value) ) {
	// openErrorModal("Please Select Any Filter To See The Records !");
	// } else {

	var url = "./appAuditDashboard?entity_code=" + entity_code
			+ "&project_name=" + project_name + "&audit_type=" + audit_type;

	ajaxPostDataArray(url, fdata, searchAppAuditFilterResponse);
	// }

}

function searchAppAuditFilterResponse(response) {
	// alert(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function

function resetAppAuditFilter() {
	$("#AuditDashboard").find("input[type=text], textarea, select").val("");
	refreshFilter();
}

function refreshFilter() {
	var url = "./appAuditDashboard";
	window.location = url;
}

function viewAuditRecord(funName) {
	if (!lhsIsNull(checkedId)) {
		// funName(checkedId);
		window[funName](checkedId);
	}
}

function viewAppAuditData(id) {
	
	if (!lhsIsNull(id)) {
		var split_id = id.split('~');
		var seq_id = split_id[2];
		var url = "./viewAppAuditDashboard?seq_id=" + seq_id;
		ajaxPostUrl(url, viewAppAuditDashboard);
	}
}

function viewAppAuditDashboard(response) {
	if (!lhsIsNull(response)) {
		// alert(response);
		$("#appAuditView").html("");
		$("#appAuditView").html(response);
		$("#myModalAudit").modal({
			show : true
		});
	}
}
// /////

// sushma

function openUrlByType() {
	var url = "./AppSecEntry";
	window.location = url;
	// indow.location.href='./AppSecEntry';
}

function openUrlByOther() {
	var url = "./OtherEntry";
	window.location = url;
}



function saveAuditWork() {

	if (validateEntryForm()) {
		ajaxSubmitPostData("saveAuditForm", "AppSecEntryForm", saveResponse);

	}

}

function saveResponse(response) {
	if (response === "success") {
		var action = $('#action').val();
		if (action == "save") {
			openSuccessModal("Data saved successfully!",
					"window.location.href='./appAuditDashboard';");
		} else {
			openSuccessModal("Data Updated successfully!",
					"window.location.href='./appAuditDashboard';");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function validateEntryForm() {
	var status;
	// alert("fegjLZxlak");
	var entity_code = $('#entity_code').val();
	var audit_name = $('#audit_name').val();
	var audit_description = $('#audit_description').val();
	var action_name = $('#action').val();

	if (lhsIsNull(entity_code)) {
		openErrorModal("Please select Entity /Bank Type.");
		status = false;
	} else if (lhsIsNull(audit_name)) {
		openErrorModal("Please Enter AppSec Header.");
		status = false;
	} else if (lhsIsNull(audit_description)) {
		openErrorModal("Please Enter AppSec Description .");
		status = false;
	} else if (lhsIsNull(audit_name.length >= 100)) {
		openErrorModal("AppSec Header Exceed Maximum  length Of Size");
		status = false;
	} else if (lhsIsNull(audit_description.length >= 4000)) {
		openErrorModal("AppSec Description Exceed Maxumum length Of Size");
		status = false;
	} else if (audit_description.length > 4000) {
		openErrorModal("Audit Description Exceeds Maximum Length Of Characters");
		status = false;
	}

	else {
		status = true;
	}

	return status;
}// End function

function validateForm() {
	var status;

	var audit_resolve_remark = $("#audit_resolve_remark").val();
	if (lhsIsNull(audit_resolve_remark)) {
		openErrorModal("Please Enter Resolve Remark");
		status = false;
	}
	if (lhsIsNull(audit_resolve_remark.length >= 4000)) {
		openErrorModal(" Resolve Remark  Exceed Maxumum length Of Size");
		status = false;
	} else {
		status = true;
	}

	return status;
}// End function

function saveOtherWork() {

	if (validateEntryForm()) {
		ajaxSubmitPostData("saveOtherForm", "OtherEntryForm", saveResponseOther);

	}
}

function saveResponseOther(response) {
	if (response === "success") {
		var action = $('#action').val();
		if (action == "save") {
			openSuccessModal("Data saved successfully!",
					"window.location.href='./appAuditDashboard';");
		} else {
			openSuccessModal("Data Updated successfully!",
					"window.location.href='./appAuditDashboard';");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function onLoadData() {
	var action_name = $('#action').val();
	if (action_name == 'save') {
		document.getElementById('resolve').style.display = 'none';

	} else if (action_name == 'edit') {
		document.getElementById('resolve').style.display = 'block';
		document.getElementById('audit_applied_app_name').disabled = true;
		document.getElementById('entity_code').disabled = true;
		document.getElementById('audit_name').readOnly = true;
		document.getElementById('no_of_occurences').readOnly = true;
		document.getElementById('audit_description').readOnly = true;
		document.getElementById('audit_ext_link').readOnly = true;
		document.getElementById('doc_file').disabled = true;
		document.getElementById('audit_applied_region').readOnly = true;
		document.getElementById('audit_applied_server_ip').readOnly = true;
		document.getElementById('audit_applied_app_name').readOnly = true;
		document.getElementById('other_info').readOnly = true;
		document.getElementById('other_info1').readOnly = true;
		document.getElementById('other_info2').readOnly = true;
		document.getElementById('no_of_occurences').readOnly = true;
		document.getElementById('audit_description_view').readOnly = true;
		document.getElementById('other_categaory').readOnly = true;


	}
}

function backToDashboard() {
	var action_name = $('#action').val();
	if (action_name == 'save') {
		window.location.href = './AppAuditEntry';

	} else {
		window.location.href = './appAuditDashboard';
	}
}

function editAuditEntry(funName) {
	if (!lhsIsNull(checkedId)) {
		// funName(checkedId);
		window[funName](checkedId);
	}
}

function editAppSecForm(id) {
	if (!lhsIsNull(id)) {
		var split_id = id.split('~');
		var seq_id = split_id[2];
		var audit_type = split_id[3];
		if (audit_type == 'APS') {
			var url = "AppSecEntry?seq_id=" + seq_id;
			window.location = url;
		} else if (audit_type == 'VA') {
			var url = "./vulnerableEntry?seq_id=" + seq_id;
			window.location = url;
		} else if (audit_type == 'OT') {
			var url = "OtherEntry?seq_id=" + seq_id;
			window.location = url;
		} else if (audit_type == 'BP') {
			var url = "./bestPracticeEntry?seq_id=" + seq_id;
			window.location = url;
		} else if (audit_type == 'SC') {
			var url = "sourceCodeReview?seq_id=" + seq_id;
			window.location = url;
		}
	}
}

 function UpdateAuditWork() {

 if (validateForm()) {
 ajaxSubmitPostData("UpdateAppSecForm", "AppSecEntryForm", savelist);
      }
 }

 function saveList(response) { alert(response);
	 var action = $('#action').val();
		
	if (response === "success") {
		if (action == "edit") {
		openSuccessModal("Update saved successfully !",
				"window.location.href='./appAuditDashboard';");
	}
		} else {
		openErrorModal("Some Error Occured..!",
				"window.location.href='./appAuditDashboard';");
	}
}

function UpdateotherWork() {
	if (validateForm()) {
		ajaxSubmitPostData("UpdateAppSecForm", "OtherEntryForm", savelist);
    }

}

function vulnerableEntry() {
	var url = "./vulnerableEntry"

	window.location.href = url;
}

function bestPracticeEntry() {
	var url = "./bestPracticeEntry"

	window.location.href = url;
}

function saveVAudit() {

	var action = document.getElementById('action').value;

	if (validateVulnerabalEntryForm()) {

		ajaxSubmitPostData("saveVADetail", "vulnerabilityEntryForm",
				saveVADetailResponse);

	}

	
}

function saveVADetailResponse(response) {
	var action = document.getElementById('action').value
	// alert("action......."+action);
	if (response === "success") {
		if (action === "save") {
			openSuccessModal("Data saved successfully !",
					"backToBankAuditEntry();");
		} else {
			openSuccessModal("Data Updated successfully !",
					"backToBankAuditEntry();");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function backToBankAuditEntry1() {
	var action = $('#action').val();
	// alert(action);
	if (action == "save") {
		window.location.href = "./AppAuditEntry";
	} else {
		window.location.href = "./appAuditDashboard"
	}
}



function validateVulnerabalEntryForm() {

	var status;
	var app_name = $('#audit_applied_app_name').val();
	var entity_code = $('#entity_code').val();
	var audit_name = $('#audit_name').val();
	var audit_description = $('#audit_description').val();
	

	var action_name = $('#action').val();

	if (lhsIsNull(app_name)) {
		openErrorModal("Please Select Application Name.");
		status = false;
	}else if (lhsIsNull(entity_code)) {
		openErrorModal("Please Select Entity / Bank Type.");
		status = false;
	} else if (lhsIsNull(audit_name)) {
		openErrorModal("Please Enter Section Header.");
		status = false;
	} else if (lhsIsNull(audit_description)) {
		openErrorModal("Please Enter Section Description.");
		status = false;

	}

	

	else {
		status = true;
	}
	return status;
}// End function

function validateVulnerabalEditForm() {
	var status;
	var audit_resolve_remark = $('#audit_resolve_remark').val();
	

	var action_name = $('#action').val();

	if (lhsIsNull(audit_resolve_remark)) {
		openErrorModal("Please Enter Resolve Remark.");
		status = false;

	}

	else {

		status = true;
	}
	return status;
}

function ValidateIP(field) {
	var ipaddress = field.value;
	var regexp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;

	if (regexp.test(ipaddress)) {
		return true;
	} else {
		openErrorModal('You have entered an invalid IP address!', '');
		field.value = "";
		return false;
	}
}




function UpdateotherWork() {
	if (validateForm()) {
		ajaxSubmitPostData("UpdateAppSecForm", "OtherEntryForm", savelist);

	}

}



function savelist(response) {
	if (response === "success") {
		openSuccessModal("Data saved successfully !",
				"window.location.href='./appAuditDashboard';");
	} else {
		openErrorModal("Some Error Occured..!",
				"window.location.href='./appAuditDashboard';");
	}
}

function editVA() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);

		editVulnerableDetails(checkedId);
	}
}

function editVulnerableDetails(id) {

	var seq_id = id;

	var url = "./vulnerableEntry?seq_id=" + seq_id;

	window.location = url;

}

function updateVulnerableDetails() {
	if (validateVulnerabalEditForm()) {
		ajaxSubmitPostData("UpdateAppSecForm", "vulnerabilityEntryForm",
				updateVADetailResponse);

	}

}

function updateVADetailResponse(response) {
	if (response === "success") {
		openSuccessModal("Data Updated Successfully !",
				"window.location.href='./appAuditDashboard';");
	} else {
		openErrorModal("Some Error Occured..!",
				"window.location.href='./appAuditDashboard';");
	}
}

function onloadDisplayFunction() {
	var action_name = $('#action').val();

	if (action_name == 'save') {
		document.getElementById('vulnerabilityEditForm').style.display = 'none';
	} else if (action_name == 'edit') {
		document.getElementById('vulnerabilityEditForm').style.display = 'block';
		document.getElementById('button_desc').disabled = true;
		document.getElementById('audit_applied_app_name').disabled = true;
		document.getElementById('entity_code').disabled = true;
		document.getElementById('audit_name').readOnly = true;
		document.getElementById('audit_description').readOnly = true;
		document.getElementById('audit_ext_link').readOnly = true;
		document.getElementById('no_of_occurences').readOnly = true;
		document.getElementById('doc_file').disabled = true;
		document.getElementById('audit_applied_region').readOnly = true;
		document.getElementById('audit_applied_server_ip').readOnly = true;
		document.getElementById('audit_applied_app_name').readOnly = true;
		document.getElementById('other_info').readOnly = true;
		document.getElementById('other_info1').readOnly = true;
		document.getElementById('other_info2').readOnly = true;


	}
}

function saveBestPracticeAudit() {

	var action = document.getElementById('action').value;

	if (validateBestPracticeEntryForm()) {
		ajaxSubmitPostData("saveBestPracticeDetail", "BestPracticeEntryForm",
				saveBestPracticeResponse);
	}

}

function saveBestPracticeResponse(response) {
	var action = document.getElementById('action').value
	// alert("action......."+action);
	if (response === "success") {

		openSuccessModal("Data saved successfully !", "backToBankAuditEntry();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function backToBankAuditEntry() {
	window.location.href = './AppAuditEntry';
}

function validateBestPracticeEntryForm() {

	var status;

	var audit_name = $('#audit_name').val();
	var audit_description = $('#audit_description').val();
	
	var action_name = $('#action').val();

	if (lhsIsNull(audit_name)) {
		openErrorModal("Please Enter Section Header.");
		status = false;
	} else if (lhsIsNull(audit_description)) {
		openErrorModal("Please Enter Section Description.");
		status = false;
	}

	

	else {
		status = true;
	}
	return status;
}// End function

function validateBestPracticeEditForm() {
	var status;
	var audit_resolve_remark = $('#audit_resolve_remark').val();
	

	var action_name = $('#action').val();

	if (lhsIsNull(audit_resolve_remark)) {
		openErrorModal("Please Enter Resolve Remark.");
		status = false;
	}
	
	else {
		status = true;
	}
	return status;
}

function editBestPractice() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);

		editBestPracticeDetails(checkedId);
	}
}

function editBestPracticeDetails(id) {

	var seq_id = id;

	var url = "./bestPracticeEntry?seq_id=" + seq_id;
	window.location = url;

}

function updateBestPracticeDetails() {
	if (validateBestPracticeEditForm()) {
		ajaxSubmitPostData("UpdateAppSecForm", "BestPracticeEntryForm",
				updateBestPracticeDetailResponse);

	}

}

function updateBestPracticeDetailResponse(response) {
	if (response === "success") {
		openSuccessModal("Data Updated Successfully !",
				"window.location.href='./appAuditDashboard';");
	} else {
		openErrorModal("Some Error Occured..!",
				"window.location.href='./appAuditDashboard';");
	}
}

function onloadBestPractice() {
	var action_name = $('#action').val();

	if (action_name == 'save') {
		document.getElementById('BestPracticeEditForm').style.display = 'none';
	} else if (action_name == 'edit') {
		document.getElementById('BestPracticeEditForm').style.display = 'block';

		document.getElementById('audit_name').readOnly = true;
		document.getElementById('audit_description').readOnly = true;
		document.getElementById('audit_ext_link').readOnly = true;
		document.getElementById('no_of_occurences').readOnly = true;
		document.getElementById('doc_file').disabled = true;
		document.getElementById('audit_applied_region').readOnly = true;
		document.getElementById('audit_applied_server_ip').readOnly = true;
		document.getElementById('audit_applied_app_name').readOnly = true;
		document.getElementById('other_info').readOnly = true;

	}
}

function validateIPaddresdatabase(inputElement) {

	var ipaddress = inputElement.value;

	if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
			.test(ipaddress)) {
		return (true)
	} else {

		openErrorModal('You have entered an invalid IP address!', '')
		$('#' + inputElement.id).focus();
		inputElement.value = '';
		$('#db_ip').val('');
		return (false)
	}

}

function ValidateIP(field) {
	var regexp = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;

	if (regexp.test(field.value)) {
		return true;
	} else {
		openErrorModal('You have entered an invalid IP address!', '');
		field.value = "";
		return false;
	}
}

function isValidUrl(field) {
	var regexp = /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
	if (regexp.test(field.value)) {
		return true;
	} else {
		openErrorModal("You have entered an invalid IP address!");
		field.value = "";
		return false;
	}
}


// /sneha

function srcCodeReview() {
	var url = "./sourceCodeReview";
	window.location.href = url;
}

function backToDashBoard() {

	var url = "./appAuditDashboard";
	window.location.href = url;
}

function saveAuditSrcReviews() {
	// alert("call");
	if (validateSrcReview()) {

		ajaxSubmitPostData("saveReviewDetails", "srcReviewform",
				saveResponseReview);
	}

}

function updateSrcReviewattach() {
	if (validateSRCReviewEdit()) {
		ajaxSubmitPostData("updateReviewForm", "srcReviewform", saveAttachments);
	}

}

function saveAttachments(response) {
	if (response === "success") {
		openSuccessModal("Data Updated successfully !",
				"window.location.href='./appAuditDashboard';");
	} else {
		openErrorModal("Some Error Occured..!",
				"window.location.href='./appAuditDashboard';");
	}
}

function editsrcReviewForm(id) {
	var seq_id = id;
	var url = "sourceCodeReview?seq_id=" + seq_id;
	window.location = url;
}

function saveResponseReview(response) {
	if (response == "success") {
		var action = $('#action').val();

		if (action == "save") {
			openSuccessModal("Data Saved Successfully!",
					"window.location.href='./appAuditDashboard';");
		} else {
			openSuccessModal("Data Updated successfully!",
					"window.location.href='./appAuditDashboard';");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function validateSRCReviewEdit() {

	var status;
	var audit_resolve_remark = $('#audit_resolve_remark').val();
	// var audit_resolve_reference = $('#audit_resolve_reference').val();
	// var audit_doc_resolve = $('#file_attach').val();

	var action_name = $('#action').val();

	if (lhsIsNull(audit_resolve_remark)) {
		openErrorModal("Please Enter Resolve Remark.");
		status = false;
	} /*
		 * else if (lhsIsNull(audit_resolve_reference)) { openErrorModal("Please
		 * Enter Resolve Reference."); status = false; }
		 *//*
		 * else if (lhsIsNull(audit_doc_resolve)) { // openErrorModal("Please
		 * Select Resolve Attachment."); status = false; }
		 */else {
		status = true;
	}
	return status;
}

function validateSrcReview() {
	var status;
	// alert("fegjLZxlak");
	var entity_code = $('#entity_code').val();
	var audit_name = $('#audit_name').val();
	var audit_description = $('#audit_description').val();
	var action_name = $('#action').val();

	if (lhsIsNull(entity_code)) {
		openErrorModal("Please select Entity /Bank Type.");
		status = false;
	} else if (lhsIsNull(audit_name)) {
		openErrorModal("Please Enter AppSec Header.");
		status = false;
	} else if (lhsIsNull(audit_description)) {
		openErrorModal("Please Enter AppSec Description .");
		status = false;
	} else if (lhsIsNull(audit_name.length >= 100)) {
		openErrorModal("AppSec Header Exceed Maximum  length Of Size");
		status = false;
	} else if (lhsIsNull(audit_description.length >= 4000)) {
		openErrorModal("AppSec Description Exceed Maxumum length Of Size");
		status = false;
	} else if (audit_description.length > 4000) {
		openErrorModal("Audit Description Exceeds Maximum Length Of Characters");
		status = false;
	}

	else {
		status = true;
	}

	return status;
}



function saveDetailResponse(response) {
	var action = document.getElementById('action').value
	// alert("action......."+action);
	if (response === "success") {
		if (action === "save") {
			openSuccessModal("Data saved successfully !",
					"window.location.href='./appAuditDashboard';");
		} else if (action === "edit") {
			openSuccessModal("Data Updated successfully !",
					"window.location.href='./appAuditDashboard';");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function onLoadToHideData() {
	var action_name = $('#action').val();
	if (action_name == 'save') 
	{
		document.getElementById('resolve').style.display = 'none';

	} else if (action_name == 'edit') {
		document.getElementById('resolve').style.display = 'block';
		document.getElementById('audit_applied_app_name').disabled = true;
		document.getElementById('entity_code').disabled = true;
		document.getElementById('audit_name').readOnly = true;
		document.getElementById('no_of_occurences').readOnly = true;
		document.getElementById('audit_description').readOnly = true;
		document.getElementById('audit_description_view').readOnly = true;
		document.getElementById('audit_ext_link').readOnly = true;
		document.getElementById('doc_file').disabled = true;
		document.getElementById('audit_applied_region').readOnly = true;
		document.getElementById('audit_applied_server_ip').readOnly = true;
		document.getElementById('audit_applied_app_name').readOnly = true;
		document.getElementById('other_info').readOnly = true;
		document.getElementById('other_info1').readOnly = true;
		document.getElementById('other_info2').readOnly = true;
		document.getElementById('no_of_occurences').readOnly = true;
		
	}
}

function viewDescription() {
	$("#description_data").modal({
		show : true
	});
}


function submitDescription() {
	
	var a = document.getElementById('audit_description_view').value;
	
	document.getElementById("audit_description").value = a;

	$("#description_data").modal('hide');
	
}



function resetDescription() {
	
	document.getElementById('audit_description_view').value = '';
	
}


var checkedId = '';
function openActionDiv(id) {
	var a= id.split('~');
	var status =a[4];
	
	if(status === 'R'){
		document.getElementById('editAuditBtn').style.display='none';
	}else{
		document.getElementById('editAuditBtn').style.display='inline-block';
	}
	checkedId = '';
	var checkbox = document.getElementById(id).checked;
	if (checkbox) {
		var cbs = document.getElementsByName("actionCheckbox");
		for (var i = 0; i < cbs.length; i++) {
			cbs[i].checked = false;
		}
		document.getElementById(id).checked = true;
		checkedId = id;
		$("#actiondiv").css('display', 'block');
	} else {
		checkedId = '';
		$("#actiondiv").css('display', 'none');
	}
}


function downloadResolveAttach(id) {
	// alert(id);
	var count = id.split("-");
	var seq_id = count[1];
	
	var cp = document.getElementById("globalcontextpath").value;
	
	var url = cp + "downloadResolveAttach?seq_id=" + seq_id ;
	window.location = url;
}


function downloadUnresolveAttach(id) {
	// alert(id);
	var count = id.split("-");
	var seq_id = count[1];
	
	var cp = document.getElementById("globalcontextpath").value;
	
	var url = cp + "downloadUnresolveAttach?seq_id=" + seq_id ;
	window.location = url;
}

function CloseErrorData() {

		document.getElementById('audit_description_view').value = '';
		
}

var descri ="";
function closeErrorDataSrc() {
    var decs = document.getElementById('audit_description_view').value; 
    
    var action_name = $('#action').val();
	if (action_name == 'save') 
	{
		 if(decs != null)
	    	{
	    	document.getElementById('audit_description').value = '';
	        document.getElementById('audit_description_view').value = '';
	        }
		 $("#srcDescriptionData").modal('hide');
	}
	else
		{
		$("#srcDescriptionData").modal('hide');
		}
 
 }


function submitDescriptionSrc()
{

	var description = document.getElementById('audit_description_view').value;
	
	document.getElementById("audit_description").value = description;

	$("#srcDescriptionData").modal('hide');
}

function descriptionView() {
	var actionnew = $('#action').val();
	var demo = document.getElementById('audit_description').value;

	if (actionnew == 'save') {
		$("#srcDescriptionData").modal({
			show : true
		});
	} else {
		$("#srcDescriptionData").modal({
			show : true
		});
		if (demo != null) {
			document.getElementById('audit_description_view').value = demo;
			document.getElementById('audit_description_view').style.display = 'inline-block';
		}
		document.getElementById('submitbtn').disabled = true;
		document.getElementById('resetbtn').disabled = true;
	}

}

