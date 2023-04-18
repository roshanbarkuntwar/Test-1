function onloadApplication() {
	var action = document.getElementById('action').value;
	var app_type = document.getElementById('app_type').value;
	var client_code = document.getElementById('client_code').value;

	// alert("client_code......."+client_code);
	// alert("action......."+action);
	if ( action == 'view'){
		document.getElementById('application_type').disabled = true;
	 //   document.getElementById('actiondiv_view').style.display = 'block';


	}
	
	if (action == 'edit' || action == 'view') {
		if (app_type == 'API') {
			document.getElementById('application_type').selectedIndex = '1';
			// alert("application_type....."+application_type);
		}
		if (app_type == 'JAR') {
			document.getElementById('application_type').selectedIndex = '2';
			// alert("application_type....."+application_type);
		}
		if (app_type == 'Web') {
			document.getElementById('application_type').selectedIndex = '3';
			// alert("application_type....."+application_type);
		}
	}

	var formdata = $('#view_mast').serializeArray();
	var fdata = JSON.stringify(formdata);
	// var app_type = document.getElementById("application_type").value;

	var x = document.getElementById("myDIV");

	/*if (lhsIsNull(app_type)) {
		openErrorModal("Please select Application Type to see the records !");
	} else {
*/
		var actionUrl = "./getSearchApplicationDetail?app_type=" + app_type
				+ "&action=" + action+ "&client_code=" + client_code;

		ajaxPostDataArray(actionUrl, fdata, EditFilterDocResponse);
	// }
}

function EditFilterDocResponse(response) {
	
	console.log(response);
	$('#app_table').html('');
	$('#app_table').html(response);
	testcase();
	loadDataUsingPaginatorGrid();
	
}// end function

function differentMode(mode) {
	document.getElementById('filter_name').value = '';
	document.getElementById('client_mode').value = mode;
	if (mode == 'Card') {
		document.getElementById('bulk_download_btn').style.display = 'none';
		document.getElementById('actiondiv').style.display = 'none';
		document.getElementById('client_mode').value = 'Card';
  
	} else if (mode == 'Text') {
		document.getElementById('bulk_download_btn').style.display = 'block';
		document.getElementById('client_mode').value = 'Text';
	}
	var formdata = $('#clientDashboard').serializeArray();
	var fdata = JSON.stringify(formdata);
	var actionUrl = "./clientDetailDashboardTable?client_mode=" + mode;
	ajaxPostDataArray(actionUrl, fdata, ModeResponse);

}// end function

function ModeResponse(response) {
	//console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);

}//

function searchApplicationFilter() {
	var action = document.getElementById("action").value;
	var formdata = $('#view_mast').serializeArray();
	var fdata = JSON.stringify(formdata);
	var app_type = document.getElementById("application_type").value;
	var client_code = document.getElementById("client_code").value;
	
	

	var x = document.getElementById("myDIV");

	if (lhsIsNull(app_type)) {
		openErrorModal("Please select Application Type to see the records !");
	} else {

		var actionUrl = "./getSearchApplicationDetail?app_type=" + app_type
				+ "&action=" + action +"&client_code="+client_code;

		ajaxPostDataArray(actionUrl, fdata, searchFilterDocResponse);
	}
}// end function

function searchFilterDocResponse(response) {

	console.log(response);
	$('#app_table').html('');
	$('#app_table').html(response);
	loadDataUsingPaginatorGrid();
}// end function

function saveClientWork(id) {
	var client_mode = id;
	var action = document.getElementById("action").value;
	var client_code = document.getElementById("client_code").value;
	var entity_code = document.getElementById("entity_code").value;

	if (validateClientEditForm()) {
		var url = "./SaveclientDetail?client_mode=" + client_mode;
		ajaxSubmitPostData(url, "client_Form", saveClientResponse);
	}
}

function saveEditClientWork(id) {
	var client_mode = id;
	var action = document.getElementById("action").value;
	var client_code = document.getElementById("client_code").value;
	var cp = document.getElementById("globalcontextpath").value;

	if (validateClientEditForm()) {
		var url = "./SaveclientDetail?client_mode=" + client_mode;
		ajaxSubmitPostData(url, "client_Form", saveClientResponse);
	}
}

function saveClientResponse(response) {
	if (response === "success") {
		var action = $('#action').val();
		var client_code = document.getElementById("client_code").value;
		var entity_code = document.getElementById("entity_code").value;
// alert("client_code.."+client_code);
		var client_mode = $('#mode').val();

		if (action == "save") {
			openSuccessModal("Data Saved Successfully !",
					"backToEditApplication('" + client_code + "','" + entity_code + "','" + action
							+ "','" + client_mode + "');");
		} else if (action == "edit") {
			openSuccessModal("Data Updated Successfully !",
					"backToEditApplication('" + client_code + "','" + entity_code + "','" + action
							+ "','" + client_mode + "');");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}


function saveBackdashborad(id) {
	var client_mode = id;
	var action = document.getElementById("action").value;
	var client_code = document.getElementById("client_code").value;

	if (validateClientEditForm()) {
		var url = "./SaveclientDetail?client_mode=" + client_mode;
		ajaxSubmitPostData(url, "client_Form", saveResponse);
	}
}


function saveResponse(response) {
	if (response === "success") {
		var action = $('#action').val();
		var client_code = document.getElementById("client_code").value;
// alert("client_code.."+client_code);
		var client_mode = $('#mode').val();

		if (action == "save") {
			openSuccessModal("Data Saved Successfully !",
					"backtodashborad('" + client_mode + "');");
		} else if (action == "edit") {
			openSuccessModal("Data Updated Successfully !",
					"backtodashborad('" + client_mode + "');");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}

}

function backToEditApplication(client_code, entity_code, action, client_mode) {
    var cp = document.getElementById("globalcontextpath").value;
	var url =cp+"applicationDetails?client_code=" + client_code + "&entity_code=" + entity_code +"&action="
			+ action + "&client_mode=" + client_mode;
	window.location = url;
}


function viewClientDetails(id) {
	var client_mode = id;
	var action = document.getElementById("action").value;
	var client_code = document.getElementById("client_code").value;
	var entity_code = document.getElementById("entity_code").value;

	if (validateClientEditForm()) {
		var url = "./viewclientDetailForm?client_mode=" + client_mode;
		ajaxSubmitPostData(url, "client_Form", viewClientResponse);
	}
}

function viewClientResponse(response) {
	if (response === "success") {
	//	var action = $('#action').val();
	//	var client_code = document.getElementById("client_code").value;
// alert("client_code.."+client_code);
		var client_mode = $('#mode').val();
		
		var action = document.getElementById("action").value;
		var client_code = document.getElementById("client_code").value;
		var entity_code = document.getElementById("entity_code").value;
		// var client_mode = document.getElementById("client_mode").value;
		
		var cp = document.getElementById("globalcontextpath").value;
		var url =cp+"applicationDetails?client_code=" + client_code + "&entity_code=" + entity_code +"&action="
				+ action + "&client_mode=" + client_mode;
		window.location = url;
		
	} 
}


function saveCardWork() {
	if (validateClientEditForm()) {
		ajaxSubmitPostData("saveClientForm", "client_Form",
				saveClientCardResponse);
	}
}

function saveClientCardResponse(response) {
	if (response === "success") {
		var action = $('#action').val();

		if (action == "save") {
			openSuccessModal("Data saved successfully !",
					"window.location.href='./clientDetailDashboardTable';");
		} else if (action == "edit") {
			openSuccessModal(" Data Updated successfully !",
					"window.location.href='./clientDetailDashboardTable';");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function validateClientEditForm() {
	var status;
	var entity_name = $('#entity_name').val();
	var team_name = $('#team_name').val();
	var resp_person_name = $('#resp_person_name').val();
	var resp_person_desig = $('#resp_person_desig').val();
	var resp_person_mobileno = $('#resp_person_mobileno').val();
	var resp_person_email_id = $('#resp_person_email_id').val();
	var resp_person_add1 = $('#resp_person_address').val();
	var branch_add1 = $('#branch_address').val();
	var branch_pin = $('#branch_pin').val();
	var branch_city = $('#branch_city').val();
	var branch_state_code = $('#branch_state_code').val();
	var action_name = $('#action').val();

	// var pincode=/^\d{6}$/

	if (lhsIsNull(entity_name)) {
		openErrorModal("Please Enter Entity Name.");
		status = false;
	} else if (lhsIsNull(team_name)) {
		openErrorModal("Please Enter Team Name.");
		status = false;
	} else if (lhsIsNull(resp_person_name)) {
		openErrorModal("Please Enter Responsible Person Name.");
		status = false;
	} else if (resp_person_name.length > 100) {
		openErrorModal("Responsible Person Name Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(resp_person_desig)) {
		openErrorModal("Please Enter Role/Designation.");
		status = false;
	}else if (resp_person_desig.length > 100) {
		openErrorModal("Role/Designation Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(resp_person_mobileno)) {
		openErrorModal("Please Enter Valid Mobile NO.");
		status = false;
	} else if (resp_person_mobileno.length < 10 ) {
		openErrorModal("Please Enter Valid 10 digit Mobile NO.");
		status = false;
	}else if (resp_person_mobileno.length > 15) {
		openErrorModal("Mobile NO. Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(resp_person_email_id)) {
		openErrorModal("Please Select Email Id.");
		status = false;
	} else if (resp_person_email_id.length > 250) {
		openErrorModal("Email Id Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(resp_person_add1)) {
		openErrorModal("Please Enter Address.");
		status = false;
	}else if (resp_person_add1.length > 4000) {
		openErrorModal("Address Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(branch_add1)) {
		openErrorModal("Please Enter Branch Address.");
		status = false;
	} else if (branch_add1.length > 4000) {
		openErrorModal("Branch Address Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if ((branch_pin.length != 6)) {
		openErrorModal("Please Enter Branch PinCode.");
		status = false;
	} else if (lhsIsNull(branch_city)) {
		openErrorModal("Please Enter Branch City.");
		status = false;
	} else if (branch_city.length > 1000) {
		openErrorModal("Branch City Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(branch_state_code)) {
		openErrorModal("Please Enter Branch State Code.");
		status = false;
	} else {
		status = true;
	}
	return status;
}//End function




function viewDashboardTable() {
	document.getElementById('action').value = 'view'; 
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);
		viewClientDetailDashboardTable(checkedId);
	}
}


function viewClientDetailDashboardTable(id) {
	var split_id = id.split("-");
	var entity_code = split_id[2];
	var client_code = split_id[3];
	var client_mode = split_id[4];
	var url = "viewClientDetails?entity_code=" + entity_code
			+ "&client_code=" + client_code + "&client_mode=" + client_mode;
 window.location = url;
	//ajaxPostDataArray(url, viewTableResponse);
	
}

function viewTableResponse(){
	var action = document.getElementById('action').value; 
	if(action == "view")
		{
	document.getElementById('entity_name').disabled = true;
	document.getElementById('team_name').disabled = true;
	document.getElementById('resp_person_name').readOnly = true;
	document.getElementById('resp_person_desig').readOnly = true;
	document.getElementById('resp_person_mobileno').readOnly = true;
	document.getElementById('resp_person_email_id').readOnly = true;
	document.getElementById('resp_person_address').readOnly = true;
	document.getElementById('branch_address').readOnly = true;
	document.getElementById('branch_pin').readOnly = true;
	document.getElementById('branch_city').readOnly = true;
	document.getElementById('branch_state_code').disabled = true;
}
}

function editDashboardTable() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);
		editClientDetailDashboardTable(checkedId);
	}
}


function editClientDetailDashboardTable(id) {
	var split_id = id.split("-");
	var entity_code = split_id[2];
	var client_code = split_id[3];
	var client_mode = split_id[4];
	var url = "./editClientDetails?entity_code=" + entity_code
			+ "&client_code=" + client_code + "&client_mode=" + client_mode;
	window.location = url;
}

function deleteDashboardTable() {

	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);
		deleteClientDetailDashboardTable(checkedId);
	}
}

function deleteClientDetailDashboardTable(id) {
	openConfirmModal("Do you want to delete ?",
			"deleteClientDetailDashboardTableDetails('" + id + "');");
}
function deleteClientDetailDashboardTableDetails(id) {
	var split_id = id.split("-");
	var entity_code = split_id[2];
	var client_code = split_id[3];
	
	ajaxPostUrl("deleteClientEntity?entity_code=" + entity_code
			+ "&client_code=" + client_code,
			deleteClientDetailDashboardTableResponse);
}

function deleteClientDetailDashboardTableResponse(response) {
	 if (response === "success") {
		openSuccessModal("Data deleted successfully !",
				"backToClientDashboard();");
	}
}//End function

function backToClientDashboard() {
	window.location.href = './clientDetailDashboardTable';
}

var checkedId = '';
function openActionDivTable(id) {
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



function resetClientDashboardForm() {
	window.location.href = './clientDetailDashboard';
}

function resetClientDashboardTableForm() {
	var client_mode = document.getElementById("client_mode").value;
	if (client_mode === "Card") {
		
		document.getElementById("Text").checked = true;
		document.getElementById("Card").checked = false;
	}else{
		
		document.getElementById("Card").checked = true;
		document.getElementById("Text").checked = false;
	}
	var url = "./clientDetailDashboardTable?client_mode=" + client_mode;
	window.location = url;
}

function onloadFunction() {
	var mode = document.getElementById("client_mode").value;
	//alert("mode..." + mode);
	 if (mode == "Card") {
		document.getElementById("Card").checked = true;
		document.getElementById("Text").checked = false;
		document.getElementById('client_mode').value = "Card";
		document.getElementById('bulk_download_btn').style.display = 'none';
	} else {
		document.getElementById("Card").checked = false;
		document.getElementById("Text").checked = true;
		document.getElementById('client_mode').value = "Text";
		document.getElementById('bulk_download_btn').style.display = 'none';
	}
}

function searchClientDashboardFilter() {
	var mode = document.getElementById("client_mode").value;
	
	var formdata = $('#clientDashboard').serializeArray();
	var fdata = JSON.stringify(formdata);
	var clientName = document.getElementById("filter_name").value;
	
		
	if (lhsIsNull(clientName)) {
		openErrorModal("Please select filter to see the records !");
	} else {
		if (mode == 'Text') {
			mode == "Text";
			//var actionUrl = "./clientDetailDashboardTable";
			var actionUrl = "./searchClientDashboardFilter?clientName="
				+ clientName;
			//alert("searchClientDashboardFilter");
		} else {
			mode == "Card";
			var actionUrl = "./searchClientDashboardCardFilter?clientName="
					+ clientName;
		}
		ajaxPostDataArray(actionUrl, fdata, searchClientDashboardFilterResponse);
	}

	console.log("Filter:" + fdata);
}// end function

function searchClientDashboardFilterResponse(response) {
	console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();
}// end function


function searchClientDashboardCardFilter() {
	var formdata = $('#clientDashboardCard').serializeArray();
	var fdata = JSON.stringify(formdata);
	var clientName = document.getElementById("filter_name").value;
	if (lhsIsNull(clientName)) {
		openErrorModal("Please select filter to see the records !");
	} else {
		var actionUrl = "./searchClientDashboardCardFilter?clientName="
				+ clientName;
		ajaxPostDataArray(actionUrl, fdata,
				searchClientDashboardCardFilterResponse);
	}
}// end function

function searchClientDashboardCardFilterResponse(response) {
	console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();
}// end function

function editDetail(id) {
	// alert("id......"+id);
	var client_mode = document.getElementById("client_mode2").value;
	// alert("mode."+mode);

	var client_code = id;
	var url = "./editClientDetails?client_code=" + client_code + "&client_mode="
			+ client_mode;
	window.location = url;
}


function deleteDetail(id) {
	var client_mode = document.getElementById("client_mode2").value;
//	openSuccessModal("Do you want to delete ?", "deleteClientEntity('" + id
//			+ "')");
	openConfirmModal("Do you want to delete ?","deleteClientEntity('" + id + "')");
}

function deleteClientEntity(id) {
	var client_code = id;
	var url = "./deleteClientEntity?client_code=" + client_code;
	ajaxPostUrl(url, deleteEntityResponse);
}

function deleteEntityResponse(response) {
	
	if (response === "success") 
	{
		
		openSuccessModal("Data deleted successfully !","resetClientDashboardTableForm();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}//End function

function resetClientDashboardTableForm() {
	var client_mode = document.getElementById("client_mode").value;
	if (client_mode === "Card") {
		
		document.getElementById("Text").checked = true;
		document.getElementById("Card").checked = false;
	}else{
		
		document.getElementById("Card").checked = true;
		document.getElementById("Text").checked = false;
	}
	var url = "./clientDetailDashboardTable?client_mode=" + client_mode;
	window.location = url;
}

//function backtocarddash() {
//	window.location.href = './clientDetailDashboard';
//}

function viewEntity(id) {
	var split_id = id.split('-');
	
	var client_code = split_id[0];
	var entity_code = split_id[1];
	var client_mode = document.getElementById('client_mode').value;
	var url = "viewClientDetails?entity_code=" + entity_code
			+ "&client_code=" + client_code + "&client_mode=" + client_mode;
 window.location = url;
//	var url = "./viewClientDetails?client_code=" + client_code;
//	ajaxPostUrl(url, viewClientButtonResponse);
}

function viewClientButtonResponse(response) {
	if (!lhsIsNull(response)) {
		$("#workModelTableBody").html("");
		$("#workModelTableBody").html(response);
		$("#detail-modal").modal({
			show : true
		});
	} else {
		$("#detail-modal").modal({
			show : true
		});
	}
}

function showClientForm() {
	var a = document.getElementById('entity_code1').value;
	if (a.length != 0) {
		document.getElementById('clientForm').style.display = 'block';

		var getIndex = document.getElementById('entity_code1').selectedIndex;
		document.getElementById('entity_code2').selectedIndex = getIndex;

	} else {
		alert("Please Select Entity name");
		document.getElementById('clientForm').style.display = 'none';
	}
}

function showApplicationDetailForm(event) {
	var app_type = document.getElementById('application_type').value;
	var url = "./viewProjectMast?app_type=" + app_type;
	ajaxPostUrl(url, savedResponse1);
	if (app_type.length != 0) {
		document.getElementById('AppDetailTable').style.display = 'block';
	} else {
		openErrorModal("Please Select Application Type");
		document.getElementById('AppDetailTable').style.display = 'none';
	}
}

function savedResponse1(response) {
	var split_id = response.split('#');
	var app_split = split_id[1];
	var response = split_id[0];
	if (response == "success") {
		var url = "./applicationDetails?app_split=" + app_split;
		window.location.href = url;
	} else {
		openErrorModal("Some Error Occured..!", "");
	}

}

var checkedId = '';
function openActionDiv(id) {
	checkedId = '';
	var count = $("[type='checkbox']:checked").length;
	var action = document.getElementById("action").value;
	var checkbox = document.getElementById(id).checked;
	
	var split_id = id.split('-');
	var index = split_id[1];
	var project_code = split_id[2];
	
	if (checkbox) {
	
		if(count <= 1){
		
			document.getElementById("indexId").value = index;
		}else{
			var test = document.getElementById("indexId").value;
			index = test+','+index;
		}
		
		document.getElementById("indexId").value = index;
		document.getElementById(id).checked = true;
		checkedId = id;
		$("#actiondiv").css('display', 'block');
	} else {
		checkedId = '';
		$("#actiondiv").css('display', 'none');
	}
	
	document.getElementById("uniqueId").value = index;
	
}

function callback(response) {
	if (response === "success") {
		openSuccessModal("Data Saved successfully !", "");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function validateClientDetails() {
	var status;
	var entity_code1 = $('#entity_code1').val();
	var team_name = $('#team_name').val();
	var resp_person_name = $('#resp_person_name').val();
	var resp_person_desig = $('#resp_person_desig').val();
	var resp_person_mobileno = $('#resp_person_mobileno').val();
	var resp_person_email_id = $('#resp_person_email_id').val();
	var resp_person_add1 = $('#resp_person_address').val();
	var branch_add1 = $('#branch_address').val();
	var branch_pin = $('#branch_pin').val();
	var branch_city = $('#branch_city').val();
	var branch_state_code = $('#branch_state_code').val();
	var action_name = $('#action').val();

	var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	// var pincode=/^\d{6}$/

	if (lhsIsNull(entity_code1)) {
		openErrorModal("Please Enter Entity Name.");
		status = false;
	} else if (lhsIsNull(team_name)) {
		openErrorModal("Please Enter Team Name.");
		status = false;
	} else if (lhsIsNull(resp_person_name)) {
		openErrorModal("Please Enter Responsible Person Name.");
		status = false;
	} else if (resp_person_name.length > 100) {
		openErrorModal("Responsible Person Name Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(resp_person_desig)) {
		openErrorModal("Please Enter Role/Designation.");
		status = false;
	}else if (resp_person_desig.length > 100) {
		openErrorModal("Role/Designation Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(resp_person_mobileno)) {
		openErrorModal("Please Enter Valid Mobile NO.");
		status = false;
	} else if (resp_person_mobileno.length < 10 ) {
		openErrorModal("Please Enter Valid 10 digit Mobile NO.");
		status = false;
	}else if (resp_person_mobileno.length > 15) {
		openErrorModal("Mobile NO. Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(resp_person_email_id)) {
		openErrorModal("Please Select Email Id.");
		status = false;
	}else if (!resp_person_email_id.match(mailformat)) {
		openErrorModal("Please Enter Valid Email Address!");
		status = false;
	} else if (resp_person_email_id.length > 250) {
		openErrorModal("Email Id Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(resp_person_add1)) {
		openErrorModal("Please Enter Address.");
		status = false;
	}else if (resp_person_add1.length > 4000) {
		openErrorModal("Address Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(branch_add1)) {
		openErrorModal("Please Enter Branch Address.");
		status = false;
	} else if (branch_add1.length > 4000) {
		openErrorModal("Branch Address Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if ((branch_pin.length != 6)) {
		openErrorModal("Please Enter Branch PinCode.");
		status = false;
	} else if (lhsIsNull(branch_city)) {
		openErrorModal("Please Enter Branch City.");
		status = false;
	} else if (branch_city.length > 1000) {
		openErrorModal("Branch City Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(branch_state_code)) {
		openErrorModal("Please Enter Branch State Code.");
		status = false;
	} else {
		status = true;
	}
	return status;
}


function validateConnectedDatabase() {
	var r = document.connected_database;
	var status;

	if (r.connected_db_username.value == '') {
		openErrorModal("Please Enter Database User Name.");
		return false;
	}
	if (r.connected_db_username.value.length > 50) {
		openErrorModal("Database User Name Exceeds Maximum Limit Of Characters.");
		return false;
	}
	if (r.connected_db_pwd.value == '') {
		openErrorModal("Please Enter Database User Password.");
		return false;
	}
	if (r.connected_db_pwd.value.length > 50) {
		openErrorModal("Password Exceeds Maximum Limit Of Characters.");
		return false;
	}
	if (r.connected_db_sid.value == '') {
		openErrorModal("Please Enter Sid.");
		return false;
	}
	if (r.connected_db_sid.value.length > 50) {
		openErrorModal("Sid Exceeds Maximum Limit Of Characters.");
		return false;
	}
	if (r.connected_port.value.length != 4) {
		openErrorModal("Please Enter 4 Digit Port.");
		return false;
	}
	if (r.connected_db_remark.value.length > 50) {
		openErrorModal("Remark Exceeds Maximum Limit Of Characters.");
		return false;
	} else {
		status = true;
	}
	return status;
}

function savedBox() {
	if (validateClientDetails()) {
		ajaxSubmitPostData("SaveclientDetail", "client_Form", savedResponse);
	}
}

function saveAfterPrevious() {
	if (validateClientEditForm()) {
		ajaxSubmitPostData("SaveclientDetail", "client_Form", savedResponse);
	}
}

function savedResponse(response) {
	if (response === "success") {
		openSuccessModal("Data Saved Successfully !", "resetEntry();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function resetEntry() {
		window.location.href = './applicationDetails';
}

function saveApplicationDetails() {
	
	var id = document.getElementById("uniqueId").value;
	var len = id.length;
	if(len === 1)
		{
		var index = id;
		
		var project_name = document.getElementById("app_name~"+index).value;
		var application_type = document.getElementById("application_type~"+index).value;
	    var protocol = document.getElementById("protocol~"+index).value;
		var url = document.getElementById("url~"+index).value;
		var connected_db = document.getElementById("connected_db~"+index).value;
		var remark = document.getElementById("remark~"+index).value;	
		var action = document.getElementById("action").value;
		
		if (validateApplicationDatabase(index)) {
			
			 $.ajax({
	           url: './SaveApplicationDetails',
	           type: 'POST',
	           data: {
	        	   "project_name": project_name,
	        	   "application_type": application_type,
	        	   "protocol": protocol,
	        	   "url": url,
	        	   "connected_db": connected_db,
	        	   "remark": remark,
	        	   "action": action
	           },
	           success: function (response) {
	               savedResponseApplication(response);
	           }
	       });

		}
			}
	else{
	for(var i=0; i<len-1; i++)
		{
	var split_id = id.split(',');
	
	var index = split_id[i];
	
	var project_name = document.getElementById("app_name~"+index).value;
	var application_type = document.getElementById("application_type~"+index).value;
    var protocol = document.getElementById("protocol~"+index).value;
	var url = document.getElementById("url~"+index).value;
	var connected_db = document.getElementById("connected_db~"+index).value;
	var remark = document.getElementById("remark~"+index).value;	
	var action = document.getElementById("action").value;
	
	if (validateApplicationDatabase(index)) {
		
		 $.ajax({
           url: './SaveApplicationDetails',
           type: 'POST',
           data: {
        	   "project_name": project_name,
        	   "application_type": application_type,
        	   "protocol": protocol,
        	   "url": url,
        	   "connected_db": connected_db,
        	   "remark": remark,
        	   "action": action
           },
           success: function (response) {
               savedResponseApplication(response);
           }
       });
		
//		ajaxSubmitPostData("SaveApplicationDetails", "application_detail",
//				savedResponseApplication);
	}
		}
	}
}

function savedResponseApplication(response) {
	var action = document.getElementById("action").value;
	if (response === "success") {
		if (action === 'save') {
		openSuccessModal("Data Saved Successfully !", "resetEntryApplication();");
		}else{
		openSuccessModal("Data Updated Successfully !", "resetEntryApplication();");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function validateApplicationDatabase(index) {
	var status;
	var app = document.getElementsByName("app_url");

	var url = document.getElementById("url~"+index).value;
	var conn_db = document.getElementById("connected_db~"+index).value;
	var remark = document.getElementById("remark~"+index).value;

	var pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
	'((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
	'((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
	'(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
	'(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
	'(\\#[-a-z\\d_]*)?$', 'i');

	 var  regexp =  /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;

	if (lhsIsNull(app)) {
		openErrorModal("Please Select Proper Url.");
		status = false;
	} 
	else if (url.length > 100) {
		openErrorModal("Application URL Exceeds Maximum Limit Of Characters");
		status = false;
	}
	
	else if (lhsIsNull(conn_db)) {
		openErrorModal("Please Select Connected Database.");
		status = false;
	}
	
	 else if (remark.length > 250 ) {
		openErrorModal("Remark Exceeds Maximum Limit Of Characters.");
		status = false;
	}
	else {
		status = true;
	}
	return status;

}

function resetEntryApplication() {
	var action = document.getElementById("action").value;
	var client_mode = document.getElementById("client_mode").value;

	if (action === 'save') {
		window.location.href = "./clientDetailDashboardTable";
	} else {
		var url = "./clientDetailDashboardTable?client_mode=" + client_mode;
		//ajaxPostUrl(url,checkedcase);
		window.location = url;
	}
}
//
//function checkedcase(){
///*	document.getElementById(page_mode2).value='checked';*/
//}

function saveConnectedDatabase() {
	if (validateConnectedDatabase()) {
		ajaxSubmitPostData("SaveConnectedDatabase", "connected_database",
				savedResponsedatabase);
	}
}

function savedResponsedatabase(response) {
	var action = document.getElementById("action").value;
	if (response === "success") {
		if(action === "save")
			{
		openSuccessModal("Data Saved Successfully !", "backToApplication();");
			}else{
		openSuccessModal("Data Updated Successfully !", "editTest();");
			}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}

}

function backToApplication() {
	
	var id = document.getElementById("testForm").value;
//	var split_id = id.split('-');
//	var index = split_id[1];
//	var project_code = split_id[2];
	var a = document.getElementById('connected_db_username').value;
	var b = document.getElementById('connected_db_pwd').value;
	var c = document.getElementById('connected_db_sid').value;
	var d = document.getElementById('connected_port').value;
	var e = document.getElementById('connected_db_remark').value;
	document.getElementById("connected_db~"+id).value = a + "," + b + "," + c
			+ "," + d + "," + e;

	$("#myModal").modal('hide');
}

function editTest(id) {
	
	var id = document.getElementById("testForm").value;
//	var split_id = id.split('~');
//	var index = split_id[1];
//	var project_code = split_id[2];
	var a = document.getElementById('connected_db_username').value;
	var b = document.getElementById('connected_db_pwd').value;
	var c = document.getElementById('connected_db_sid').value;
	var d = document.getElementById('connected_port').value;
	var e = document.getElementById('connected_db_remark').value;
	document.getElementById("connected_db~"+id).value = a + "," + b + "," + c
			+ "," + d + "," + e;

	$("#myModal").modal('hide');
}

function databaseconnect(id)
{
  var split_id = id.split("~");
  document.getElementById("testForm").value = split_id[1];
  refreshErrorData();
	$("#myModal").modal({
		show : true
	});
}

function getErrorClassification(id) {
 
	var split_id = id.split('~');
	var index = split_id[1];
document.getElementById('testForm').value = index;
 var dbvalue = document.getElementById(id).value;

var split_id = dbvalue.split(",");
var db_username = split_id[0];
var db_pwd = split_id[1];
var db_sid = split_id[2];
var port = split_id[3];
var db_remark = split_id[4];


document.getElementById('connected_db_username').value = db_username;
document.getElementById('connected_db_pwd').value = db_pwd;
document.getElementById('connected_db_sid').value = db_sid;
document.getElementById('connected_port').value = port;
document.getElementById('connected_db_remark').value = db_remark;

$("#myModal").modal({
	show : true
});
//		}
}

function refreshErrorData() {
	
	var id = document.getElementById("testForm").value;
	/*var split_id = id.split('-');
	var index = split_id[1];
	var project_code = split_id[2];*/
	
	document.getElementById('connected_db_username').value = '';
	document.getElementById('connected_db_pwd').value = '';
	document.getElementById('connected_db_sid').value = '';
	document.getElementById('connected_port').value = '';
	document.getElementById('connected_db_remark').value = '';
	document.getElementById('connected_db~'+id).value = '';
}

function backtodashborad(id) {
	var client_mode = id;

	var url = "./clientDetailDashboardTable?client_mode=" + client_mode;
	//ajaxPostUrl(url,checkedcase);
	window.location = url;
}

function testcase()
{ 
	var action = document.getElementById('action').value
	if(action === 'edit'){
	document.getElementById('application_type').disabled = true;
	}
}	



function previousClientDetails() {
		
		var id = document.getElementById("uniqueId").value;
		var len = id.length;
		if(len === 1)
			{
			var index = id;
			
			var project_name = document.getElementById("app_name~"+index).value;
			var application_type = document.getElementById("application_type~"+index).value;
		    var protocol = document.getElementById("protocol~"+index).value;
			var url = document.getElementById("url~"+index).value;
			var connected_db = document.getElementById("connected_db~"+index).value;
			var remark = document.getElementById("remark~"+index).value;	
			var action = document.getElementById("action").value;
			
			if (validateApplicationDatabase(index)) {
				
				 $.ajax({
		           url: './SaveApplicationDetails',
		           type: 'POST',
		           data: {
		        	   "project_name": project_name,
		        	   "application_type": application_type,
		        	   "protocol": protocol,
		        	   "url": url,
		        	   "connected_db": connected_db,
		        	   "remark": remark,
		        	   "action": action
		           },
		           success: function (response) {
		        	   previousApplication(response);
		           }
		       });

			}
				}
		else{
		for(var i=0; i<len-1; i++)
			{
		var split_id = id.split(',');
		
		var index = split_id[i];
		
		var project_name = document.getElementById("app_name~"+index).value;
		var application_type = document.getElementById("application_type~"+index).value;
	    var protocol = document.getElementById("protocol~"+index).value;
		var url = document.getElementById("url~"+index).value;
		var connected_db = document.getElementById("connected_db~"+index).value;
		var remark = document.getElementById("remark~"+index).value;	
		var action = document.getElementById("action").value;
		
		if (validateApplicationDatabase(index)) {
			
			 $.ajax({
	           url: './SaveApplicationDetails',
	           type: 'POST',
	           data: {
	        	   "project_name": project_name,
	        	   "application_type": application_type,
	        	   "protocol": protocol,
	        	   "url": url,
	        	   "connected_db": connected_db,
	        	   "remark": remark,
	        	   "action": action
	           },
	           success: function (response) {
	        	   previousApplication(response);
	           }
	       });
			
//			ajaxSubmitPostData("SaveApplicationDetails", "application_detail",
//					savedResponseApplication);
		}
			}
		}
	}


function previousApplication(response) {
	var action = document.getElementById("action").value;
	if (response === "success") {
		if (action === 'save') {
		openSuccessModal("Data Saved Successfully !", "ClientDetailsPreviousEdit();");
		}else{
		openSuccessModal("Redirect To Client Detail Page !", "ClientDetailsPreviousEdit();");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}



function previousbutton(){
	
	var entity_code = document.getElementById('entity_code').value;
	var client_code = document.getElementById('client_code').value;
	var client_mode = document.getElementById('client_mode').value;
	var url = "viewClientDetails?entity_code=" + entity_code
			+ "&client_code=" + client_code + "&client_mode=" + client_mode;
 window.location = url;
	
}

function ClientDetailsPreviousEdit() {
	
	var entity_code = document.getElementById('entity_code').value;
	var client_code = document.getElementById('client_code').value;
	var client_mode = document.getElementById('client_mode').value;
	var url = "./editClientDetails?entity_code=" + entity_code
			+ "&client_code=" + client_code + "&client_mode=" + client_mode;
	window.location = url;
}

function SavePreviousButton() {
	
	var entity_code = document.getElementById('entity_code1').value;
	var client_code = document.getElementById('clientname').value;
	var client_mode = document.getElementById('Client_mode1').value;
	var pre="previous";
	var url = "./editClientDetails?entity_code=" + entity_code
			+ "&client_code=" + client_code + "&client_mode=" + client_mode + "&pre=" + pre;
	window.location = url;
}
