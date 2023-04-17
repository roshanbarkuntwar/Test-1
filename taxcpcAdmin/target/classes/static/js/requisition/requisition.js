//function getProjectList() {
//	$('#project_name').autocomplete({
//        source: function (request, response) {
//           $.ajax({
//               url: './getProjectList',
//               type: 'POST',
//               dataType: 'json',
//               data: request,
//               success: function (data) {
//                   response($.map(data, function (value, key) { 
//                        return {
//                            label: value,
//                            value: key
//                        };
//                   }));
//               }
//           });
//        },
//        select: function( event, ui ) {
//        	$( "#project_name" ).val( ui.item.label);
//            $( "#project_code" ).val( ui.item.value);
//            return false;
//        },
//        minLength: 1
//    });
//}

function showFieldsOnType() {
	var req_type = $("#req_type").val();
	$("#reqEntryForm")
			.find(
					"input[type=text], input[type=file], textarea, select:not([id='req_type'])")
			.val("");
	if (req_type == 'Application') {
		$(".applicationDiv").css('display', 'flex');
		getSampleDataFilter('A');
	} else if (req_type == 'Deployment') {
		$(".applicationDiv").css('display', 'none');
		getSampleDataFilter('D');
	}
}

function getSampleDataFilter(reqType) {
	if (!lhsIsNull(reqType)) {
		var url = "./getSampleDataFilter?reqType=" + reqType;
		ajaxPostUrl(url, getSampleDataFilterResponse);
	}
}

function getSampleDataFilterResponse(response) {
	var sampleFilterDiv = $('#sampleReqDiv1');
	// alert("response..."+response);
	sampleFilterDiv.empty();
	if (!lhsIsNull(response)) {
		sampleFilterDiv.append('');
		var html = '';
		for (var i = 0; i < response.length; i++) {
			html += '<div class="row">';
			html += '<div class="col-md-4" style="border-right: 1px;">';
			html += '<p>';
			html += '<input type="checkbox" id="sample_code~' + i
					+ '" name="sample_code"';
			html += 'value="' + response[i] + '"';
			html += 'onclick="clearUncheckedSampleValue(this.id);"> <label';
			html += 'for="sample_code~' + i
					+ '" class="sample-code" id="sample_code_lbl~' + i + '">'
					+ response[i] + '</label>';
			html += '</p>';
			html += '</div>';
			html += '<div class="col-md-8">';
			html += '<p>';
			html += '<input type="text" class="form-control" id="sample_code_txt~'
					+ i + '"';
			html += '	onkeyup="chkSampleCkBox(this);" onblur="chkSampleCkBox(this);"';
			html += 'tabindex="21">';
			html += '</p>';
			html += '</div>';
			html += '</div>';
		}
		sampleFilterDiv.append(html);
	}
}

function openParentReqLov(event) {
	var project_name = $("#project_name").val();
	var module_name = $("#module_name").val();

	var getStrings = {
		strProjectCode : project_name,
		strModuleCode : module_name
	};

	openLOV(event, 'getParentReq', getStrings, '',
			'null#parent_req_code#parent_req_desc', '');
}

function getErrorClassification() {
	$("#error_data").modal({
		show : true
	});
}

function getSampledata1() {
	$("#sample_data").modal({
		show : true
	});
}

function clearUncheckedValue(id) {
	var checkbox = document.getElementById(id).checked;
	if (!checkbox) {
		var splitId = id.split("~");
		document.getElementById("error_code_txt~" + splitId[1]).value = "";
	}
}

function clearUncheckedSampleValue(id) {
	var checkbox = document.getElementById(id).checked;
	if (!checkbox) {
		var splitId = id.split("~");
		document.getElementById("sample_code_txt~" + splitId[1]).value = "";
	}
}

function chkCkBox(fld) {
	if (!lhsIsNull(fld.value)) {
		document.getElementById("error_code~" + fld.id.split("~")[1]).checked = true;
	} else {
		document.getElementById("error_code~" + fld.id.split("~")[1]).checked = false;
	}
}

function chkSampleCkBox(fld) {
	if (!lhsIsNull(fld.value)) {
		document.getElementById("sample_code~" + fld.id.split("~")[1]).checked = true;
	} else {
		document.getElementById("sample_code~" + fld.id.split("~")[1]).checked = false;
	}
}

function createString() {
	var code = document.getElementsByName("sample_code");
	var error_code = "";
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
var error_code = "";
function createErrorString() {
	error_code = "";
	// alert("inside createErrorString..");
	var code = document.getElementsByName("error_code");
	// alert("code.."+code);
	// var code1 = document.getElementById("error_name~");

	for (var i = 0; i < code.length; i++) {
		if (code[i].checked) {
			var sample_code = document.getElementById("error_name~" + (i + 1)).innerHTML;
			// var sample_code = code[i].value;
			// alert("sample_code.."+sample_code);
			// code[i].value;
			var sample_id = code[i].id;
			var sam = sample_id.split("~");
			var value_txt = document.getElementById("error_code_txt~" + sam[1]).value;
			error_code += sample_code + ":" + value_txt + "||";
			// alert("value_txt.."+value_txt);
		}
	}
	error_code = error_code.substring(0, error_code.length - 2);
	document.getElementById("req_error_group_str").value = error_code;
	// alert("error_code.."+error_code);
	$("#error_data").modal('hide');
	// setTotalCheckData();
	// getErrorSubClassification(error_code);
}

function addRequisition() {
	if (validateReqEntryForm()) {
		ajaxSubmitPostData("addNewRequisition", "reqEntryForm", addReqResponse);
		// $.ajax("/addNewRequisition");
	}
}

function addReqResponse(response) {
	const myArray = response.split(",");
	let response1 = myArray[0];
	let req_code1 = myArray[1];
	let req_code = req_code1;
	var role_code = document.getElementById("reported_rolecode").value;
	var roleCode = role_code.split("-");
	// alert("response1.."+response1);
	if (response1 === "success") {
		if (roleCode[0] == "CLI") {
			openSuccessModal(
					"Requisition added successfully! \n\t Requisition No. : "
							+ req_code + "", "resetReqEntryClient();");
		} else {
			openSuccessModal(
					"Requisition added successfully! \n\t Requisition No. : "
							+ req_code + "", "resetReqEntry();");
		}

	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function resetReqEntry() {
	document.getElementById("reqDoc").value = null;
	window.location.href = './reqEntryDetail';

}

function resetReqEntryClient() {
	document.getElementById("reqDoc").value = null;
	window.location.href = './clientReqEntryDetail';

}

function CloseErrorData() {

	if (error_code == "") {
		document.getElementById('req_error_group_str').value = '';

		document.getElementById('error_code~1').checked = false;
		document.getElementById('error_code~2').checked = false;
		document.getElementById('error_code~3').checked = false;
		document.getElementById('error_code~4').checked = false;
		document.getElementById('error_code~5').checked = false;
		document.getElementById('error_code~6').checked = false;
		document.getElementById('error_code~7').checked = false;

		document.getElementById('error_code_txt~1').value = '';
		document.getElementById('error_code_txt~2').value = '';
		document.getElementById('error_code_txt~3').value = '';
		document.getElementById('error_code_txt~4').value = '';
		document.getElementById('error_code_txt~5').value = '';
		document.getElementById('error_code_txt~6').value = '';
		document.getElementById('error_code_txt~7').value = '';
	} else {

	}

}
function refreshErrorData() {

	document.getElementById('req_error_group_str').value = '';

	document.getElementById('error_code~1').checked = false;
	document.getElementById('error_code~2').checked = false;
	document.getElementById('error_code~3').checked = false;
	document.getElementById('error_code~4').checked = false;
	document.getElementById('error_code~5').checked = false;
	document.getElementById('error_code~6').checked = false;
	document.getElementById('error_code~7').checked = false;

	document.getElementById('error_code_txt~1').value = '';
	document.getElementById('error_code_txt~2').value = '';
	document.getElementById('error_code_txt~3').value = '';
	document.getElementById('error_code_txt~4').value = '';
	document.getElementById('error_code_txt~5').value = '';
	document.getElementById('error_code_txt~6').value = '';
	document.getElementById('error_code_txt~7').value = '';
}

function refreshSampleData() {

	document.getElementById('sample_data_filter_str').value = '';

	var req_type = document.getElementById('req_type').value;

	if (req_type === 'Application') {
		document.getElementById('sample_code~0').checked = false;
		document.getElementById('sample_code~1').checked = false;
		document.getElementById('sample_code~2').checked = false;
		document.getElementById('sample_code~3').checked = false;
		document.getElementById('sample_code~4').checked = false;
		document.getElementById('sample_code~5').checked = false;
		document.getElementById('sample_code~6').checked = false;

		document.getElementById('sample_code_txt~0').value = '';
		document.getElementById('sample_code_txt~1').value = '';
		document.getElementById('sample_code_txt~2').value = '';
		document.getElementById('sample_code_txt~3').value = '';
		document.getElementById('sample_code_txt~4').value = '';
		document.getElementById('sample_code_txt~5').value = '';
		document.getElementById('sample_code_txt~6').value = '';
	} else {
		document.getElementById('sample_code~0').checked = false;
		document.getElementById('sample_code~1').checked = false;
		document.getElementById('sample_code~2').checked = false;
		document.getElementById('sample_code~3').checked = false;

		document.getElementById('sample_code_txt~0').value = '';
		document.getElementById('sample_code_txt~1').value = '';
		document.getElementById('sample_code_txt~2').value = '';
		document.getElementById('sample_code_txt~3').value = '';

	}
}
function CloseSampleData() {

	if (error_code == "") {

		document.getElementById('sample_data_filter_str').value = '';
		var req_type = document.getElementById('req_type').value;

		if (req_type === 'Application') {
			document.getElementById('sample_code~0').checked = false;
			document.getElementById('sample_code~1').checked = false;
			document.getElementById('sample_code~2').checked = false;
			document.getElementById('sample_code~3').checked = false;
			document.getElementById('sample_code~4').checked = false;
			document.getElementById('sample_code~5').checked = false;
			document.getElementById('sample_code~6').checked = false;

			document.getElementById('sample_code_txt~0').value = '';
			document.getElementById('sample_code_txt~1').value = '';
			document.getElementById('sample_code_txt~2').value = '';
			document.getElementById('sample_code_txt~3').value = '';
			document.getElementById('sample_code_txt~4').value = '';
			document.getElementById('sample_code_txt~5').value = '';
			document.getElementById('sample_code_txt~6').value = '';
		} else {
			document.getElementById('sample_code~0').checked = false;
			document.getElementById('sample_code~1').checked = false;
			document.getElementById('sample_code~2').checked = false;
			document.getElementById('sample_code~3').checked = false;

			document.getElementById('sample_code_txt~0').value = '';
			document.getElementById('sample_code_txt~1').value = '';
			document.getElementById('sample_code_txt~2').value = '';
			document.getElementById('sample_code_txt~3').value = '';

		}
	} else {

	}

}

function resetReqDash() {

	window.location.href = './reqEntry';
}

function validateReqEntryForm() {
	var status;

	var req_type = $("#req_type").val();
	var project_name = $('#project_name').val();
	var module_name = $('#module_name').val();
	var menu_name = $('#menu_name').val();
	var sample_data_filter_str = $('#sample_data_filter_str').val();
	var issue_type = $('#issue_type').val();
	var doc_file = $('#reqDoc').val();
	var req_priority = $('#req_priority').val();
	var issue_description = $('#issue_description').val();

	if (lhsIsNull(req_type)) {
		openErrorModal("Please select Requisition Type.");
		status = false;
	} else if (lhsIsNull(project_name)) {
		openErrorModal("Please select Application Name.");
		status = false;
	} else if (lhsIsNull(module_name) && req_type === 'Application') {
		openErrorModal("Please select Module Name.");
		status = false;
	} else if (lhsIsNull(menu_name) && req_type === 'Application') {
		openErrorModal("Please select Menu Name.");
		status = false;
	} else if (lhsIsNull(issue_type)) {
		openErrorModal("Please select Issue Type.");
		status = false;
	} else if (lhsIsNull(sample_data_filter_str)) {
		openErrorModal("Please select Sample Data Filter.");
		status = false;
	} else if (lhsIsNull(doc_file) && req_type === 'Application') {
		openErrorModal("Please select Attachment file.");
		status = false;
	} else if (lhsIsNull(req_priority)) {
		openErrorModal("Please select Priority.");
		status = false;
	} else if (lhsIsNull(issue_description)) {
		openErrorModal("Please enter the Issue Description.");
		status = false;
	} else if(issue_description.length >500){
		openErrorModal("Issue Discription Exceed Maximum Limit Of Characters.");
		status = false;
	}else if (document.getElementById("reqDoc").files.length > 5) {
	
		openErrorModal("Maximum 5 files can be Uploaded.");
		status = false;
	} else {
		status = true;
	}

	return status;
}// End function

function getProjectModules() {
	var projectCode = $('#project_name').val();
	document.getElementById("module_name").disabled = false;

	if (!lhsIsNull(projectCode)) {

		$('#module_name').val("");
		$('#menu_name').empty();
		document.getElementById("menu_name").disabled = true;
		$('#menu_name').append('<option value="">--Select--</option>');
		// $('#module_name').append('<option value="">--Select--</option>');
		var url = "./getProjectModule1?projectCode=" + projectCode;
		ajaxPostUrl(url, getProjectModulesResponse);
	} else {
		// getProjectModulesResponse();
	}
}

function getProjectModulesResponse(response) {
	var moduleName = $('#module_name');
	moduleName.empty();
	if (!lhsIsNull(response)) {

		moduleName.append('<option value="">--Select--</option>');
		$.each(response, function(k, v) {
			moduleName.append('<option value=' + k + '>' + v + '</option>');
			// console.log("key " + k + "value " + v);
		});
	} else {
		moduleName.append('<option value="">--Select--</option>');
	}
}

function getProjectMenu() {
	var projectCode = $('#project_name').val();
	var moduleName = $('#module_name').val();
	document.getElementById("menu_name").disabled = false;
	// console.log("project_name "+projectCode);
	// console.log("moduleName "+moduleName);
	if (!lhsIsNull(projectCode)) {
		var url = "./getProjectMenu1?projectCode=" + projectCode
				+ "&moduleCode=" + moduleName;
		ajaxPostUrl(url, getProjectMenuResponse);
	} else {
		// getProjectMenuResponse();
	}
}

function getProjectMenuResponse(response) {
	var menuName = $('#menu_name');
	menuName.empty();
	if (!lhsIsNull(response)) {
		menuName.append('<option value="">--Select--</option>');
		$.each(response, function(k, v) {
			menuName.append('<option value=' + k + '>' + v + '</option>');
		});
	} else {
		menuName.append('<option value="">--Select--</option>');
	}
}

function approveReq(id) {
	// alert("Idjkjjkjk.."+id);
	var splitId = id.split("~");
	var project_name = document.getElementById("project_name~" + splitId[1]).innerHTML;
	// var module_name = document.getElementById("module_name~" +
	// splitId[1]).innerHTML;
	$("#assignedReq").val(splitId[2]);
	$("#errorProject").val(project_name);
	// $("#errorModule").val(module_name);
	$("#approveModel").modal({
		show : true
	});
}

function rejectReq(id) {
	var splitId = id.split("~");

	var project_name = document.getElementById("project_name~" + splitId[1]).innerHTML;
	// var module_name = document.getElementById("module_name~" +
	// splitId[1]).innerHTML;
	var approved_by = document.getElementById("approved_by~" + splitId[1]).innerHTML;

	$("#assignedReq1").val(splitId[2]);
	$("#errorProject1").val(project_name);
	// $("#errorModule1").val(module_name);
	$("#approved_by1").val(approved_by);

	$("#rejectModel").modal({
		show : true
	});

	// var url= "./reqRejectById?req_code="+req_code;
	//
	// window.location = url;
	// ajaxPostUrl(url,getReqRejectResponse);
}

function rejectRequisition() {

	var reqCode = $('#assignedReq1').val();
	var remark = $('#rejectRemark').val();
	var approved_by = $('#approved_by1').val();
	// alert("reqCode>>>>>>>>>>>>>>>>>"+reqCode);
	// var assigned_to = $('#assigned_to').val();
	// var cp = document.getElementById("globalcontextpath").value;
	if (!lhsIsNull(remark)) {
		if (!lhsIsNull(reqCode)) {
			var url = "./reqRejectById?reqCode=" + reqCode;
			ajaxPostUrl(url, getReqRejectResponse);
		}
	} else {
		openErrorModal("Please Enter Remark !", "");
	}

}

function getReqRejectResponse(response) {
	// alert("getReqRejectResponse"+response);
	if (!lhsIsNull(response) && response == 'success') {
		$("#rejectModel").modal({
			hide : true
		});
		openSuccessModal("Requisition Rejected !",
				"window.location.href='./reqApprovalDashBoard';");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function assignedRequisition() {

	var reqCode = $('#assignedReq').val();
	var assigned_to = $('#assigned_to').val();
	var approved_by_remark = $('#approved_by_remark').val();

	if (assigned_to == "") {
		openErrorModal("Please select Assigned to", "");
	} else if (!lhsIsNull(reqCode) && !lhsIsNull(assigned_to)) {
		var url = "./reqApprovalByAuth?reqCode=" + reqCode + "&assignedTo="
				+ assigned_to + "&approved_by_remark=" + approved_by_remark;
		ajaxPostUrl(url, getAssignedReqResponse);
	}
}

function getAssignedReqResponse(response) {
	// alert("getAssignedReqResponse");
	if (!lhsIsNull(response) && response == 'success') {
		$("#approveModel").modal({
			hide : true
		});
		openSuccessModal("Requisition Approved Successfully !",
				"window.location.href='./reqApprovalDashBoard';");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function startReq(id) {

	openConfirmModal("Do you want to Start Assigned Requisition?",
			"startReqAssigned('" + id + "')");
}

function startReqAssigned(id) {
	closeConfirmModal();
	var split_id = id.split('~');
	var req_code = split_id[2];
	var reopened = "NEW";
	var url = "./startAssignedReq?req_code=" + req_code + "&reopened="
			+ reopened;

	ajaxPostUrl(url, startReqResponse);
}

function startReqResponse(response) {
	if (!lhsIsNull(response) && response == 'success') {

		// var delayInMilliseconds = "180000";
		//		
		
//		 setTimeout(setBtnActive(), 45000);
		document.getElementById('btn_status').value="S";
		openSuccessModal("Requisition Started Successfully!", "window.location.href='./reqAssignedDetail';");
		

	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}
function setBtnActive() {
	var v = document.getElementById('btn_status').value;
//	alert(v)
	if(v == "S"){
//		alert("hiiii")
	}else{
//		alert("hello")
	}
	

}

function startFailReq(id) {
	openConfirmModal("Do you want to Start Assigned Requisition?",
			"startFailReqAssigned('" + id + "')");
}

function startFailReqAssigned(id) {
	closeConfirmModal();
	var split_id = id.split('~');
	var req_code = split_id[2];
	var reopened ="";
	var reinitiate = document.getElementById('reinitiate_status-'+split_id[1]).value;
	var reopen_status = document.getElementById('reopen_status-'+split_id[1]).value;
	
	if(reopen_status=="F"){
		reopened = "DF" ;// DF=DeployFail
	}
	
	if(reinitiate=="F"){
		reopened = "RI"; //REINITIATE
	}
	 
	var url = "./startAssignedReq?req_code=" + req_code + "&reopened="
			+ reopened;

	ajaxPostUrl(url, startReqResponse);
}

function showReopenReq(id) {

	openConfirmModal("Do you want to Start Reopened Requisition?",
			"startReopenReqAssigned('" + id + "')");

}

function startReopenReqAssigned(id) {
	closeConfirmModal();
	var split_id = id.split('~');
	var req_code = split_id[2];
	var reopened = "RE"
	var url = "./startAssignedReq?req_code=" + req_code + "&reopened="
			+ reopened;

	ajaxPostUrl(url, startReopenReqResponse);
}

function startReopenReqResponse(response) {
	if (!lhsIsNull(response) && response == 'success') {
		openSuccessModal("Reopened Requisition Started Successfully!",
				"window.location.href='./reqAssignedDetail';");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function showCloseReqModel(id) {
	var splitId = id.split("~");
	var status = splitId[3];

	var reopen_status = $('#reopen_status-' + splitId[1]).val();
	// alert("reopen_status.."+reopen_status);

	var roleCode = $('#user_role_code').val();
	// alert("user_role_code.."+roleCode);
	var user_role_code = roleCode.split("-");

	if (user_role_code[0] == "CON") {

		$("#assignedReq").val(splitId[2]);
		$("#assignedModel").modal({
			show : true
		});

	} else if (user_role_code[0] == "DEV") {
		if (reopen_status == "S") {
			// reopenassignedModel
			$("#reassignedReq").val(splitId[2]);
			$("#reopenassignedModel").modal({
				show : true
			});
			document.getElementById('reopenReq').value = "Close Requisition";
		//	 $('#closeButton').val()= "Close Requisition";
			
		} else {
			$("#assignedReq").val(splitId[2]);
			$("#assignedModel").modal({
				show : true
			});
		}
	} else if (user_role_code[0] == "TES") {

		$("#assignedReq").val(splitId[2]);
		$("#assignedModel").modal({
			show : true
		});
	} else if (user_role_code[0] == "MAN") {
		
		$("#reassignedReq").val(splitId[2]);
		var reinitiate = document.getElementById('reinitiate_status-'+splitId[1]).value;
		var reopen_status = document.getElementById('reopen_status-'+splitId[1]).value;
		var deploy_status = document.getElementById('deploy_status-'+splitId[1]).value;
//		alert("reinitiate.."+reinitiate);
//		alert("reopen_status.."+reopen_status);
//		alert("deploy_status.."+deploy_status);
		
		if((reopen_status!=''|| reopen_status!='null')&& (reopen_status == "T"|| deploy_status == "R")){
			$("#reopenassignedModel").modal({
				show : true
			});
		}
		if((reinitiate!=''|| reinitiate!='null')&& reinitiate == "F"){
			$("#reinitiateModel").modal({
				show : true
			});
		}
	}

}

function setTestByQc() {
	var test_type = $('#test_type').val();
	var testByDiv = $('#assigned_to_qc');
	testByDiv.empty();
	if (test_type === 'self') {
		var username = $('#login_username').val();
		var usercode = $('#login_usercode').val();
		testByDiv.append("<option value='" + usercode + "'>" + username
				+ "</option>");
	} else {
		var role_code = "TSTR";
		var url = "./getUserOfDeptQc?role_code=" + role_code;
		ajaxPostUrl(url, setTestByQcResponse);
	}
}

function setTestByQcResponse(response) {
	var testByDiv = $('#assigned_to_qc');
	testByDiv.empty();
	if (!lhsIsNull(response)) {
		$.each(response, function(k, v) {
			testByDiv.append('<option value=' + k + '>' + v + '</option>');
		});
	} else {
		testByDiv.append('<option value="">--Select--</option>');
	}
}

function setDeployByQc() {

	var test_type = $('#deploy_type').val();
	var testByDiv = $('#assigned_to_functional');
	testByDiv.empty();
	if (test_type === 'self') {
		var username = $('#login_username').val();
		var usercode = $('#login_usercode').val();
		testByDiv.append("<option value='" + usercode + "'>" + username
				+ "</option>");
	} else {
		var role_code = "FUNC";
		var url = "./getUserOfDeptQc?role_code=" + role_code;
		ajaxPostUrl(url, setDeployByQcResponse);
	}
}

function setDeployByQcResponse(response) {
	var testByDiv = $('#assigned_to_functional');
	testByDiv.empty();
	if (!lhsIsNull(response)) {
		testByDiv.append('<option value="">--Select--</option>');
		$.each(response, function(k, v) {

			testByDiv.append('<option value=' + k + '>' + v + '</option>');
		});
	} else {
		testByDiv.append('<option value="">--Select--</option>');
	}
}

function closeReopenReq() {
	var remark = $('#reopen_remark').val();
	//alert("remark.."+remark);
	var req_code = $("#reassignedReq").val();
//	var req_type =document.getElementById('reopenReq').value; 
	var url = "./closeReAssignedRequisition?" + "req_code=" + req_code
			+ "&remark=" + remark ;
//			+"&req_type=" +req_type;

	ajaxPostUrl(url, closeReReqResponse);
}

function closeReReqResponse(response) {
	if (!lhsIsNull(response) && response == "success") {
		$("#reopenassignedModel").modal({
			hide : true
		});
		openSuccessModal("Requisition Closed Successfully!",
				"window.location.href='./reqAssignedDetail';");
		// window.location.href = './reqAssignedDetail';
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function closeReq() {

	var contactvalue = "";
	var qcType = "";
	var testBy = "";
	var remark = "";
	var req_code = "";
	var deployType = "";
	var deployTo = "";
	var assignDev = "";
	var reopened = "";
	var status = "";

	var role_code = document.getElementById('role_code').value;
	var role_code_split = role_code.split("-");

	if (role_code_split[0] == "TES") {
		if (document.getElementById('closedReq').checked == true) {
			contactvalue = document.getElementById('closedReq').value;
		} else {
			contactvalue = document.getElementById('reopenReq').value;
		}
		// alert("contactvalue"+contactvalue);

		if (contactvalue == "C") {
			// qcType = $('#test_type').val();
			// testBy = $('#assigned_to_qc').val();
			remark = $('#dev_remark').val();
			req_code = $("#assignedReq").val();
			deployType = $('#deploy_type').val();
			deployTo = $('#assigned_to_functional').val();
			reopened = contactvalue;
			// alert("qcType."+qcType);
			// if(qcType =="self"){
			// deployTo = $('#assigned_to_functional').val();
			// alert("deployTo..."+deployTo);
			// }
			// if (lhsIsNull(test_type)) {
			// openErrorModal("Please select Test Type.");
			// } else if (lhsIsNull(testBy)) {
			// openErrorModal("Please select Test By.");
			// } else {
			// alert("closeReq");
			var url = "./closeAssignedRequisition?" + "req_code=" + req_code
					+ "&qcType=" + qcType + "&assignedToQc=" + testBy
					+ "&devRemark=" + remark + "&deployType=" + deployType
					+ "&deployTo=" + deployTo + "&assignDev=" + assignDev
					+ "&reopened=" + reopened;

			if (validateCloseReq()) {
				ajaxPostUrl(url, closeReqResponse);
			}

			// }
		} else {
			assignDev = $('#selectDeveloper').val();
			reopened = contactvalue;
			remark = $('#dev_remark').val();
			req_code = $("#assignedReq").val();

			var url = "./closeAssignedRequisition?" + "req_code=" + req_code
					+ "&qcType=" + qcType + "&assignedToQc=" + testBy
					+ "&devRemark=" + remark + "&deployType=" + deployType
					+ "&deployTo=" + deployTo + "&assignDev=" + assignDev
					+ "&reopened=" + reopened;

			if (validateCloseReq()) {
				ajaxPostUrl(url, closeReqRepoenResponse);
			}
		}
	} else if (role_code_split[0] == "DEV") {

		qcType = $('#test_type').val();
		testBy = $('#assigned_to_qc').val();
		remark = $('#dev_remark').val();
		req_code = $("#assignedReq").val();
		deployType = $('#deploy_type').val();

		// alert("qcType." + qcType);
		if (qcType == "self") {
			deployTo = $('#assigned_to_functional').val();
			if (lhsIsNull(qcType)) {
				openErrorModal("Please select Test Type.");
			} else if (lhsIsNull(testBy)) {
				openErrorModal("Please select Test By.");
			} else if (lhsIsNull(deployType)) {
				openErrorModal("Please select Deployment Type");
			} else if (lhsIsNull(deployTo)) {
				openErrorModal("Please select Deploy To");
			} else {
				status = "OK";
			}

		} else {

			if (lhsIsNull(qcType)) {
				openErrorModal("Please select Test Type.");
			} else if (lhsIsNull(testBy)) {
				openErrorModal("Please select Test By.");
			} else {
				status = "OK";
			}
		}

		// alert("closeReq");
		if (status == "OK") {
			var url = "./closeAssignedRequisition?" + "req_code=" + req_code
					+ "&qcType=" + qcType + "&assignedToQc=" + testBy
					+ "&devRemark=" + remark + "&deployType=" + deployType
					+ "&deployTo=" + deployTo + "&assignDev=" + assignDev
					+ "&reopened=" + reopened;

			ajaxPostUrl(url, closeReqResponse);
		}

	} else if (role_code_split[0] == "CON") {

		if (document.getElementById('deployStatusT').checked == true) {
			contactvalue = document.getElementById('deployStatusT').value;
		} else {
			contactvalue = document.getElementById('deployStatusF').value;
		}

		deployType = contactvalue;
		remark = $('#dev_remark').val();
		req_code = $("#assignedReq").val();

		var url = "./closeAssignedRequisition?" + "req_code=" + req_code
				+ "&qcType=" + qcType + "&assignedToQc=" + testBy
				+ "&devRemark=" + remark + "&deployType=" + deployType
				+ "&deployTo=" + deployTo + "&assignDev=" + assignDev
				+ "&reopened=" + reopened;

		ajaxPostUrl(url, closeReqResponse);

	}

}

function validateCloseReq() {

	var contactvalue = "";
	var deployType = "";
	var deployTo = "";
	var assignDev = "";
	var status;
	var role_code = document.getElementById('role_code').value;
	var role_code_split = role_code.split("-");

	if (role_code_split[0] == "TES") {
		if (document.getElementById('closedReq').checked == true) {
			contactvalue = document.getElementById('closedReq').value;
		} else {
			contactvalue = document.getElementById('reopenReq').value;
		}
		if (contactvalue == "C") {
			deployType = $('#deploy_type').val();
			deployTo = $('#assigned_to_functional').val();
			if (lhsIsNull(deployType)) {
				openErrorModal("Please select Deployment.");
				status = false;
			} else if (lhsIsNull(deployTo)) {
				openErrorModal("Please select Deployed By.");
				status = false;
			} else {
				status = true;
			}
		} else {
			assignDev = $('#selectDeveloper').val();
			if (lhsIsNull(assignDev)) {
				openErrorModal("Please select Assigned to Developer.");
				status = false;
			} else {
				status = true;
			}
		}
	}
	return status;
}

//function closeReqResponse(response) {
//	if (!lhsIsNull(response) && response == "success") {
//		$("#assignedModel").modal({
//			hide : true
//		});
//		openSuccessModal("Requisition Closed Successfully!",
//				"window.location.href='./reqAssignedDetail';");
//		// window.location.href = './reqAssignedDetail';
//	} else {
//		openErrorModal("Some Error Occured..!", "");
//	}
//}


function closeReqResponse(response) {
	//alert(response)
	if (!lhsIsNull(response) && response === "delay") {
		openErrorModal("Please Wait for 10 mins to Close the Requisition ","");
	}else{
	if (!lhsIsNull(response) && response == "success") {
		$("#assignedModel").modal({
			hide: true
		});
		openSuccessModal("Requisition Closed Successfully!",
			"window.location.href='./reqAssignedDetail';");
		// window.location.href = './reqAssignedDetail';
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
	}
}


function closeReqRepoenResponse(response) {
	if (!lhsIsNull(response) && response == "success") {
		$("#assignedModel").modal({
			hide : true
		});
		openSuccessModal("Requisition Reopened Successfully.",
				"window.location.href='./reqAssignedDetail';");
		// window.location.href = './reqAssignedDetail';
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}



function openViewBox(id) {
	// $("#detail-modal").modal({show: true});
	if (!lhsIsNull(id)) {
		var split_id = id.split('-');
		var req_code = split_id[2];

		var url = "./viewReqDashboard?reqCode=" + req_code;
		ajaxPostUrl(url, viewReqdashboard);
	}
	// $('#myModal').modal('show')
}

function viewReqdashboard(response) {
	if (!lhsIsNull(response)) {
		// alert(response);
		$("#workModelTableBody").html("");
		$("#workModelTableBody").html(response);
		$("#myModal").modal({
			show : true
		});

		var req_type = document.getElementById("req_type_sb").value;
		if (req_type == "Application") {
			document.getElementById("download_btn").style.display = "block";
		} else {
			document.getElementById("download_btn").style.display = "none";
		}
	} else {
		$("#myModal").modal({
			show : true
		});
	}
}

function getReqEntrySearchFilter() {
	// alert("inside getSearchFilter...");
	
	var formdata = $('#reqEntryForm').serializeArray();
	var fdata = JSON.stringify(formdata);

	var req_code = document.getElementById("req_code").value;
	var from_date = document.getElementById("from_date").value;
	var to_date = document.getElementById("to_date").value;
	var project_name = document.getElementById("project_name").value;
	var issue_type = document.getElementById("issue_type").value;
	var current_req_status = document.getElementById("current_req_status").value;
	var req_type = document.getElementById("req_type").value;
	var req_priority = document.getElementById("req_priority").value;
	
	
	document.getElementById('from_date1').value=from_date;
	document.getElementById('to_date1').value=to_date;
	// alert("project_name 0.."+project_name);

	if (lhsIsNull(req_code) && lhsIsNull(from_date) && lhsIsNull(to_date)
			&& lhsIsNull(project_name) && lhsIsNull(issue_type)
			&& lhsIsNull(current_req_status) && lhsIsNull(req_type)
			&& lhsIsNull(req_priority)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		var url = "./getSearchData?req_code=" + req_code + "&from_date="
				+ from_date + "&to_date=" + to_date + "&project_name="
				+ project_name + "&issue_type=" + issue_type
				+ "&current_req_status=" + current_req_status + "&req_type="
				+ req_type + "&req_priority=" + req_priority;
	}
	// window.location = url;
	// var actionUrl = "./getSearchData";
	ajaxPostDataArray(url, fdata, getReqEntrySearchFilterResponse);

}

function getReqEntrySearchFilterResponse(response) {
	// alert(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	
	loadDataUsingPaginatorGrid();
	

//	loadDataUsingPaginator('reqEntryForm');

}// end function

function getClientReqEntrySearchFilter() {
	// alert("inside getSearchFilter...");

	var formdata = $('#reqEntryForm').serializeArray();
	var fdata = JSON.stringify(formdata);

	var req_code = document.getElementById("req_code").value;
	var from_date = document.getElementById("from_date").value;
	var to_date = document.getElementById("to_date").value;
	var project_name = document.getElementById("project_name").value;
	var issue_type = document.getElementById("issue_type").value;
	var current_req_status = document.getElementById("current_req_status").value;
	var req_type = document.getElementById("req_type").value;
	var req_priority = document.getElementById("req_priority").value;
	// alert("project_name 0.."+project_name);
	document.getElementById('from_date1').value=from_date;
	document.getElementById('to_date1').value=to_date;

	if (lhsIsNull(req_code) && lhsIsNull(from_date) && lhsIsNull(to_date)
			&& lhsIsNull(project_name) && lhsIsNull(issue_type)
			&& lhsIsNull(current_req_status) && lhsIsNull(req_type)
			&& lhsIsNull(req_priority)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		var url = "./getSearchDataForClient?req_code=" + req_code
				+ "&from_date=" + from_date + "&to_date=" + to_date
				+ "&project_name=" + project_name + "&issue_type=" + issue_type
				+ "&current_req_status=" + current_req_status + "&req_type="
				+ req_type + "&req_priority=" + req_priority;
	}
	// window.location = url;
	// var actionUrl = "./getSearchData";
	ajaxPostDataArray(url, fdata, getclientReqEntrySearchFilterResponse);

}

function getclientReqEntrySearchFilterResponse(response) {
	// alert(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function

function resetReqEntrySearchFilter() {
	$("#reqEntryForm").find("input[type=text], textarea, select").val("");
	refreshUser();
}

function refreshUser() {
	var url = "./reqEntryDetail";
	window.location = url;
}

function resetClientReqEntrySearchFilter() {
	$("#reqEntryForm").find("input[type=text], textarea, select").val("");
	refreshClientUser();
}

function refreshClientUser() {
	var url = "./clientReqEntryDetail";
	window.location = url;
}

function getReqApprovalFilter() {
	// alert("inside js..");

	var formdata = $('#reqAprovalForm').serializeArray();
	var fdata = JSON.stringify(formdata);

	var req_code = document.getElementById("req_code").value;
	var from_date = document.getElementById("from_date").value;
	var to_date = document.getElementById("to_date").value;
	var project_name = document.getElementById("project_name").value;
	var issue_type = document.getElementById("issue_type").value;
	var req_type = document.getElementById("req_type").value;
	var req_priority = document.getElementById("req_priority").value;
	
	document.getElementById('from_date1').value=from_date;
	document.getElementById('to_date1').value=to_date;

	if (lhsIsNull(req_code) && lhsIsNull(from_date) && lhsIsNull(to_date)
			&& lhsIsNull(project_name) && lhsIsNull(issue_type)
			&& lhsIsNull(req_type) && lhsIsNull(req_priority)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		var url = "./getReqApprovalDataFilter?req_code=" + req_code
				+ "&from_date=" + from_date + "&to_date=" + to_date
				+ "&project_name=" + project_name + "&issue_type=" + issue_type
				+ "&req_type=" + req_type + "&req_priority=" + req_priority;

		ajaxPostDataArray(url, fdata, getReqApprovalDataFilterResponse);
	}
}

function getReqApprovalDataFilterResponse(response) {
	// console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function

function resetReqAprovalFilter() {
	$("#reqAprovalForm").find("input[type=text], textarea, select").val("");
	refreshReqApproval();
}

function refreshReqApproval() {
	var url = "./reqApprovalDashBoard";
	window.location = url;
}

function getReqAssignedFilter() {
	var formdata = $('#reqAssignedForm').serializeArray();
	var fdata = JSON.stringify(formdata);

	var req_code = document.getElementById("req_code").value;
	var project_name = document.getElementById("project_name").value;
	var req_type = document.getElementById("req_type").value;
	var req_priority = document.getElementById("req_priority").value;
	var menu_name = document.getElementById("menu_name").value;
	var from_date = document.getElementById("from_date").value;
	var to_date = document.getElementById("to_date").value;
	var dev_status = document.getElementById("dev_status").value;
	
	document.getElementById('from_date1').value=from_date;
	document.getElementById('to_date1').value=to_date;

	if (lhsIsNull(req_code) && lhsIsNull(project_name) && lhsIsNull(req_type)
			&& lhsIsNull(req_priority) && lhsIsNull(menu_name)
			&& lhsIsNull(from_date) && lhsIsNull(to_date)
			&& lhsIsNull(dev_status)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		var url = "./getReqAssignedDataFilter?req_code=" + req_code
				+ "&project_name=" + project_name + "&req_type=" + req_type
				+ "&req_priority=" + req_priority + "&menu_name=" + menu_name
				+ "&from_date=" + from_date + "&to_date=" + to_date
				+ "&dev_status=" + dev_status;
		// window.location = url;
		ajaxPostDataArray(url, fdata, getReqAssignedFilterResponse);
	}
}

function getReqAssignedFilterResponse(response) {
	// console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();


}// end function

function resetReqAprovedFilter() {
	$("#reqAssignedForm").find("input[type=text], textarea, select").val("");
	refreshReqApprovedFilter();
}

function refreshReqApprovedFilter() {
	var url = "./reqAssignedDetail";
	window.location = url;
}

function opendeploymentmodule(value) {
	// alert("value...."+value);
	// const box = document.getElementById('reqsitionblock');
	// if(value == "self"){
	// //alert("box..");
	// box.style.display = 'block';
	// }else{
	// box.style.display = 'none';
	// }

}

function showdeploymodule(value) {
	// alert("value.."+value);
	const box = document.getElementById('deployDiv');
	if (value == "self") {
//		 alert("box.."+value);
		document.getElementById("self").selected = true;
		document.getElementById("deploy_type").disabled = true;
		document.getElementById("deploy_by").style.display = "flex";

		setDeployByQc();
	} else {
		document.getElementById("another").selected = true;
		document.getElementById("deploy_by").style.display = "none";
		document.getElementById("deploy_type").disabled = true;
	}

}

function OpentestingDiv(id) {

	var openDivValue = document.getElementById(id).value;
	// alert("openDivValue..."+openDivValue);
	if (openDivValue == "C") {
		document.getElementById("Deployment").style.display = "flex";
		document.getElementById("deploy_by").style.display = "flex";
		document.getElementById("AssignTodev").style.display = "none";
		document.getElementById("closeButton").innerHTML = "Close Requisition";
	} else {
		// alert("openDivValue1..."+openDivValue);
		document.getElementById("Deployment").style.display = "none";
		document.getElementById("deploy_by").style.display = "none";
		document.getElementById("AssignTodev").style.display = "flex";
		document.getElementById("closeButton").innerHTML = "Reopen Requisition";
		setDevelopers();
	}

}

function setDevelopers() {

	var testByDiv = $('#selectDeveloper');
	testByDiv.empty();

	var role_code = "DLPR";
	var url = "./getUserOfDeptQc?role_code=" + role_code;
	ajaxPostUrl(url, setDevelopersResponse);

}

function setDevelopersResponse(response) {
	var testByDiv = $('#selectDeveloper');
	testByDiv.empty();
	if (!lhsIsNull(response)) {
		testByDiv.append('<option value="">--Select--</option>');
		$.each(response, function(k, v) {

			testByDiv.append('<option value=' + k + '>' + v + '</option>');
		});
	} else {
		testByDiv.append('<option value="">--Select--</option>');
	}
}

function downloadSingleFile(id) {
	// alert(id);
	var count = id.split("~");
	var req_code = count[0];
	// alert("req_code=" + req_code);
	var slno = count[1];
	// alert("slno=" + slno);
	// var req_code = document.getElementById("reqCode_sb").value;
	var cp = document.getElementById("globalcontextpath").value;
	// alert("seq_id.."+seq_id);
	// var file_name = document.getElementById("file_name").value;
	var url = cp + "downloadFile?req_code=" + req_code + "&slno=" + slno;
	window.location = url;
}

function showCloseReq(id) {
	// alert("id.."+id);
	var splitId = id.split("~");
	var status = splitId[3];

	$("#Reqcode").val(splitId[2]);
	$("#closeReqModel").modal({
		show : true
	});
}

function ReqClosedByClient() {

	var remark = $('#client_closed_remark').val();
	var req_code = $("#Reqcode").val();
	var ReqClosed = "";
	if (document.getElementById('ReqStatusT').checked == true) {
		ReqClosed = document.getElementById('ReqStatusT').value;
	} else {
		ReqClosed = document.getElementById('ReqStatusF').value;
	}
	// alert("ReqClosed.."+ReqClosed);
	var url = "./closedRequisitionByClient?" + "req_code=" + req_code
			+ "&remark=" + remark + "&ReqClosed=" + ReqClosed;

	ajaxPostUrl(url, clientCloseReqResponse);

}

function clientCloseReqResponse(response) {
	if (!lhsIsNull(response) && response == "success") {
		$("#closeReqModel").modal({
			hide : true
		});
		openSuccessModal("Requisition Closed Successfully!",
				"window.location.href='./clientReqEntryDetail';");
		// window.location.href = './reqAssignedDetail';
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function setFromDate() {
	var fromDate = document.getElementById('from_date').value;
	alert(fromDate)
	document.getElementById('from_date1').value=fromDate
	
}

function viewDiscription(count) {
	
	
	var discription = document.getElementById("discrption_id~"+count).value;

	var url = "./viewLovDescription?discription=" + discription;
	ajaxPostUrl(url, viewLovDiscriptionResponse);
	
	
}

function viewLovDiscriptionResponse(response) {
	

if (!lhsIsNull(response)) {
	// alert(response);
	$("#lovModelTableBody").html("");
	$("#lovModelTableBody").html(response);
	$("#myModalLov").modal({
		show : true
	});

}
}

function getPieChartData(){
	var waiting_for_approval_list = document.getElementById('waiting_for_approval_list').value;
	var work_in_progress_list = document.getElementById('work_in_progress_list').value;
	var closed_by_lhs_list = document.getElementById('closed_by_lhs_list').value;
	var closed_by_fgs_list = document.getElementById('closed_by_fgs_list').value;
	var reinitiated_list = document.getElementById('reinitiated_list').value;
	var rejected_list = document.getElementById('rejected_list').value;
var xValues = [ "Waiting For Approval", "Work in Progress",  "Closed By LHS Team" ,"Closed By FGS Team", "Re-initiated", "Rejected",];
var yValues = [ waiting_for_approval_list, work_in_progress_list, closed_by_lhs_list, closed_by_fgs_list, reinitiated_list ,rejected_list ];
var barColors = [ "#2b5787", "#00aba9", "#a9de89", "#5d7c3b", "#fd7e14" ,"#b91d47 "  ];

new Chart("myPieChart", {
	type : "pie",
	data : {
		labels : xValues,
		datasets : [ {
			backgroundColor : barColors,
			data : yValues
		} ]
	},
	options : {
		title : {
			display : true,
//			text : "Statistical Requistion"
		}
	}
});
}

function getBarChartData(){
	
	var waiting_for_approval_list = document.getElementById('waiting_for_approval_list').value;
	var work_in_progress_list = document.getElementById('work_in_progress_list').value;
	var closed_by_lhs_list = document.getElementById('closed_by_lhs_list').value;
	var closed_by_fgs_list = document.getElementById('closed_by_fgs_list').value;
	var reinitiated_list = document.getElementById('reinitiated_list').value;
	var rejected_list = document.getElementById('rejected_list').value;
	
	var xValues = [ "Waiting For Approval", "Work in Progress",  "Closed By LHS Team" ,"Closed By FGS Team", "Re-initiated", "Rejected",];
	var yValues = [ waiting_for_approval_list, work_in_progress_list, closed_by_lhs_list, closed_by_fgs_list, reinitiated_list ,rejected_list ];
	var barColors = [ "#2b5787", "#00aba9", "#a9de89", "#5d7c3b", "#fd7e14" ,"#b91d47 "  ];

	new Chart("myBarChart", {
	  type: "bar",
	  data: {
	    labels: xValues,
	    datasets: [{
	      backgroundColor: barColors,
	      data: yValues
	    }]
	  },
	  options: {
	    legend: {display: false},
	    title: {
	      display: true,
//	      text: "World Wine Production 2018"
	    }
	  }
	});
}

function filterSelection(c) {

	getPieChartData();
	getBarChartData();
	var x, i;
	x = document.getElementsByClassName("column");
	if (c == "all")
		c = "";
		
	for (i = 0; i < x.length; i++) {
		RemoveClass(x[i], "show1");
		if (x[i].className.indexOf(c) > -1)
			AddClass(x[i], "show1");
	}
}

function AddClass(element, name) {
	var i, arr1, arr2;
	arr1 = element.className.split(" ");
	arr2 = name.split(" ");
	for (i = 0; i < arr2.length; i++) {
		if (arr1.indexOf(arr2[i]) == -1) {
			element.className += " " + arr2[i];
		}
	}
}

function RemoveClass(element, name) {
	var i, arr1, arr2;
	arr1 = element.className.split(" ");
	arr2 = name.split(" ");
	for (i = 0; i < arr2.length; i++) {
		while (arr1.indexOf(arr2[i]) > -1) {
			arr1.splice(arr1.indexOf(arr2[i]), 1);
		}
	}
	element.className = arr1.join(" ");
}


function saveOnsiteReqEntry(){
	//var req_code = document.getElementById('req_code').value;
	 
	if (validateOnsiteReqEntryForm()) {
		ajaxSubmitPostData("saveOnsiteReqDetail","onsiteReqEntryForm", saveOnsiteReqEntryResponse);
	 
}
}

function saveOnsiteReqEntryResponse(response) {
	const myArray = response.split(",");
	let response1 = myArray[0];
	let req_code1 = myArray[1];
	let req_code = req_code1;
	var action = document.getElementById('action').value
	//alert("action......."+action);
	// var req_code = document.getElementById('req_code').value;
    if(response1 === "success") {
    
        openSuccessModal("Requisition added successfully! \n\t Requisition No. : "
						+ req_code + "" ,"backToOnsiteReqDashboard();");
       /* openSuccessModal(
				"Requisition added successfully! \n\t Requisition No. : "
						+ req_code + "", "resetReqEntryClient();");*/
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function

function backToOnsiteReqDashboard() {
window.location.href='./onsiteReqDash';
}

function validateOnsiteReqEntryForm(){
	var status;
    var req_type = $('#req_type').val();
    var project_name = $('#project_name').val();
    var issue_description = $('#issue_description').val();
    var deploy_delivered_date = $('#deploy_delivered_date').val();
    var sample_data_filter_str = $('#sample_data_filter_str').val();
    var onsite_req_att = $('#onsite_req_att').val();
    var action_name = $('#action').val();
 
    
    if(lhsIsNull(req_type)) {
    	openErrorModal("Please Enter Type of Issue.");
        status = false;
    }   else if(lhsIsNull(project_name)) {
    	openErrorModal("Please Enter Application/Jar/Other Name.");
        status = false;
    } else if(project_name.length > 50) {
    	openErrorModal("Application/Jar/Other Name Exceeds Maximum Limit Of Characters.");
        status = false;
    } else if(lhsIsNull(issue_description)) {
    	openErrorModal("Please Enter Issue Description.");
        status = false;
    }else if (issue_description.length > 500) {
		openErrorModal("Issue Description Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if(lhsIsNull(sample_data_filter_str)) {
    	openErrorModal("Please Enter Information About Issue.");
        status = false;
    }else if (sample_data_filter_str.length > 500) {
		openErrorModal("Information About Issue Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if(lhsIsNull(onsite_req_att)) {
    	openErrorModal("Please Select Attachment.");
        status = false;
    }  else if (lhsIsNull(deploy_delivered_date)) {
		openErrorModal("Please Select Target Date.");
		status = false;
	} 
    else {	
        status = true;
    }
    return status;
}//End function


function showApplicationTab()
{
var issue_tar = document.getElementById('req_type').value;
if(issue_tar === 'isstype_01')
	{
	document.getElementById('app_name').style.display = 'flex';
	document.getElementById('req_id_onsite').innerHTML = 'Application Name *';

	
	//document.getElementById('other_name').style.display = 'none';
	}
else if(issue_tar === 'isstype_02')
	{
	// document.getElementById('app_name').style.display = 'none';
	document.getElementById('app_name').style.display = 'flex';
	// document.getElementById('other_name').style.display = 'none';
	document.getElementById('req_id_onsite').innerHTML = 'Jar Name *';
	}
else{
//	document.getElementById('app_name').style.display = 'none';
//	document.getElementById('jar_name').style.display = 'none';
	document.getElementById('app_name').style.display = 'flex';
	document.getElementById('req_id_onsite').innerHTML = 'Others *';
}
}


function openCalendarTarget(id, btnid, left, top) {
    var myCalendar;
    myCalendar = new dhtmlXCalendarObject({input: id, button: btnid});
    myCalendar.setDateFormat("%d-%m-%Y");
    myCalendar.hideTime();

    if (window.dhx4.isIE11) {
        let settings = {
            date_field_id: id,
            calendar: myCalendar,
            left: left,
            right: top
        };
        openGlobalCalendarForIE(settings);
    }
}//end function

function openGlobalCalendarForIE(settings) {
    let calDivs = document.getElementsByClassName("dhtmlxcalendar_dhx_terrace");
    let calDiv = calDivs[calDivs.length - 1];


    $(calDiv).css('left', settings.left);
    $(calDiv).css('top', settings.right);
    $(calDiv).css('display', 'block');

    settings.calendar.attachEvent("onClick", function (e) {

        document.getElementById(settings.date_field_id).value = getDisplayDate(settings.calendar.getDate());

        $('.dhtmlxcalendar_dhx_terrace').remove();

        return;
    });
}


function openActionDiv(id){
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

//onsite
function viewOnsite(id){
	
	if (!lhsIsNull(id)) {
		var split_id = id.split('~');
		var Req_code = split_id[1]; 
		 var url = "./viewOnsiteDetail?Req_code=" + Req_code;
		 ajaxPostUrl(url, viewButtonResponse);
	}
}

function viewButtonResponse(response) {
	if(!lhsIsNull(response)) {
		$("#workModelTableBody").html("");
		$("#workModelTableBody").html(response);
		$("#detail-modal").modal({show: true});
    } else {
    	$("#detail-modal").modal({show: true});
    }
}
	

function StartOnsite(id) {
	openConfirmModal("Are You Want to Start Work?", "StartOnsiteReq('" + id+ "')");
}


function StartOnsiteReq(id){
	if (!lhsIsNull(id)) {
		var split_id = id.split('-');
		var Req_code = split_id[1];
		var url = "./StartWork?req_code="+Req_code
		 ajaxPostUrl(url, viewButtonResponse2);

		}
	}

function viewButtonResponse2(response) {
	if(!lhsIsNull(response)) {
		if (response === "success") {
			openSuccessModal("Work Start Successfully", "window.location.href='./onsiteReqDash';");
		} else {
			openErrorModal("Some Error Occured..!", "");
		}
}
}
	

//function markDoneOnsite(id){
//openConfirmModal("Do you want to Mark Status As Done ?", "MarkDoneOnsiteReq('" + id+ "')");
//}
//
//
//function MarkDoneOnsiteReq(id){
//	$("#description_data").modal({
//		show : true
//	});
//	//closeConfirmModal();
//	if (!lhsIsNull(id)) {
//		var split_id = checkedId.split('-');
//		var Req_code = split_id[1]; 
//		document.getElementById("req_code").value=Req_code;
//}
//}



function markDoneOnsite(id){
	$("#description_data").modal({
		show : true
	});
	if (!lhsIsNull(id)) {
	var split_id = id.split('-');
	var Req_code = split_id[1];
	document.getElementById("req_code").value=Req_code;
	}
}

function getCodeMark(id){
	
	if (!lhsIsNull(id)) {
		var split_id = id.split('-');
		var Req_code = split_id[2]; 
		
		var url = "./onsiteReqDash?req_code="+Req_code
		window.location = url; 
	}	
}

function submitDescription(){
	var remark = $('#closure_remark').val();
	var req_code = $('#req_code').val();
	 var url = "./closedOnsiteRemark?remark="+remark+"&req_code="+req_code;
	 ajaxPostUrl(url, viewButtonResponse1);
 
}

function viewButtonResponse1(response) {
	if(!lhsIsNull(response)) {
		if (response === "success") {
			openSuccessModal("Remark Save Successfully", "window.location.href='./onsiteReqDash';");
		} else {
			openErrorModal("Some Error Occured..!", "");
		}
}
}


function searchOnsiteDetailFilter(){
	var req_code = $('#req_code1').val();
	var req_type = $('#req_type').val();
	//var deploy_delivered_date = $('#deploy_delivered_date').val();
	var from_date = document.getElementById("from_date").value;
	var to_date = document.getElementById("to_date").value;
	document.getElementById('from_date1').value=from_date;
	document.getElementById('to_date1').value=to_date;
	
	var from_date_new = document.getElementById("from_date_new").value;
	var to_date_new = document.getElementById("to_date_new").value;
	document.getElementById('from_date2').value=from_date_new;
	document.getElementById('to_date2').value=to_date_new;
	//var reported_date = $('#reported_date').val();
	var current_req_status = $('#current_req_status').val();
	var formdata = $('#onsiteReqForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	if (lhsIsNull(req_type) && lhsIsNull(req_code) && lhsIsNull(from_date) && lhsIsNull(to_date) && lhsIsNull(from_date_new) && lhsIsNull(to_date_new) && lhsIsNull(current_req_status)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		 $.ajax({
	           url: './Onsitefilter',
	           type: 'POST',
	           data: {
	        	   "req_type": req_type,
	        	   "req_code": req_code,
	        	   "from_date":from_date,
	        	   "to_date":to_date,
	        	   "from_date_new":from_date_new,
	        	   "to_date_new":to_date_new,
	        	   "current_req_status":current_req_status,

	           },
	           success: function (response) {
	        	   searchFilterWorkResponse(response);
	           }
	       });
		//var actionUrl = "./Onsitefilter?req_type="+req_type+"&deploy_delivered_date="+deploy_delivered_date;
		//ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
	}
}


function searchFilterWorkResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// e


function resetOnsiteDetailFilter(){
	$("#onsiteReqForm").find("input[type=text], textarea, select").val("");
	refreshProjectDetail();
	window.location.href='./onsiteReqDash';
}

function refreshProjectDetail() {
	var url = "./onsiteReqDash";
	window.location = url;
}


function downloadOnsiteFile(id){
	//var split_id = id.split('-');
	var req_code =id;
	var cp = document.getElementById("globalcontextpath").value;
	//alert("seq_id.."+seq_id);
	var url = cp+"downloadOnsiteFile?req_code="+req_code;
	window.location = url;
   
}
