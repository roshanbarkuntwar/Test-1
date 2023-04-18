
function saveUser() {
	var edit_right = document.getElementById("edit_right").value;
	var delete_right = document.getElementById("delete_right").value;
	var add_right = document.getElementById("add_right").value;
	checkboxtrueOrfalse();
	if (validateUserEntryForm()) {
		ajaxSubmitPostData("saveOrUpdateUser", "userConfigForm",
				saveUserResponse);
	}
}

// var cp = document.getElementById("globalcontextpath").value;

function saveUserResponse(response) {
	if (response === "success") {
		var action = $('#action').val();
		var msg = "";
		if (action == "save") {
			msg = "User saved successfully !";
		} else if (action == "edit") {
			msg = "User updated successfully !";
		}
		openSuccessModal(msg, "refreshUser()");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function validateUserEntryForm() {
	
	var status;
	var user_name = $('#user_name').val();
	var user_type = $('#usertype').val();
	var entity_code = $('#entity_code').val();
	var loginId = $('#loginId').val();
	var loginPwd = $('#loginPwd').val();
	var role_code = $('#role_code').val();
	var dept_code = $('#dept_str').val();
	var email = $('#email').val();
	var phone_no = $('#phone_no').val();
	var user_status = $("input[type='radio'][name='user_status']:checked").val();
	
	
	if (lhsIsNull(user_name)) {
		openErrorModal("Please enter the User Name.");
		status = false;
	}else if (user_name.length > 100 ) {
		openErrorModal("User Name Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(user_type)) {
		openErrorModal("Please select the User Type.");
		status = false;
	} else if(lhsIsNull(entity_code) && user_type === 'ONSITE'){
		openErrorModal("Please select Entity Name");
		status = false;	
	}else if (lhsIsNull(loginId)) {
		openErrorModal("Please enter the Login Id.");
		status = false;
	}else if (loginId.length > 100 ) {
		openErrorModal("Login Id Exceeds Maximum Limit Of Characters.");
		status = false;
	} else if (lhsIsNull(loginPwd)) {
		openErrorModal("Please enter the Password.");
		status = false;
	} else if (loginPwd.length > 100) {
		openErrorModal("Password Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(role_code)) {
		openErrorModal("Please select Role.");
		status = false;
	} else if (lhsIsNull(dept_code)) {
		openErrorModal("Please select Department.");
		status = false;
	} else if (email.length > 100) {
		openErrorModal("Email Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (phone_no.length > 20) {
		openErrorModal("Phone No. Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(user_status)) {
		openErrorModal("Please select Status.");
		status = false;
	}else{

		status = true;
	}

	return status;
}// End function

function editUser() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);
		editUserConfig(checkedId);

	}
}

function checkboxtrueOrfalse(){
	var chkPassport = document.getElementById("edit_right");
	if (chkPassport.checked) {
		document.getElementById("edit_right").value='1';
	} else {
		document.getElementById("edit_right").value='0';

	}

	var chkPassport = document.getElementById("delete_right");
	if (chkPassport.checked) {
		document.getElementById("delete_right").value='1';
	} else {
		document.getElementById("delete_right").value='0';

	}
	
	var chkPassport = document.getElementById("add_right");
	if (chkPassport.checked) {
		document.getElementById("add_right").value='1';
	} else {
		document.getElementById("add_right").value='0';

	}
	var chkPassport = document.getElementById("user_status");
	if (chkPassport.checked) {
		document.getElementById("user_status").value='A';
	} else {
		document.getElementById("user_status").value='I';

	}

	}


function editUserConfig(id) {
	// alert("id.."+id);
	var split_id = id.split('~');
	var user_code = split_id[2];
	var cp = document.getElementById("globalcontextpath").value;
	var url = cp + "addNewUser?user_code=" + user_code;
	window.location = url;
}

function deleteUser(id) {
	openConfirmModal("Do you want to delete ?", "deleteSelectedRow('" + id
			+ "')");
}

function deleteSelectedRow(id) {
	closeConfirmModal();
	var split_id = id.split('~');
	var user_code = split_id[2];
	var cp = document.getElementById("globalcontextpath").value;
	var url = cp + "./deleteUser?user_code=" + user_code;

	ajaxPostUrl(url, deleteUserResponse);
}

function deleteUserResponse(response) {
	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "refreshUser()");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function searchUserFilter() {
	var user_name = document.getElementById("user_name").value;
	var user_type = document.getElementById("user_type").value;
	var user_mode = document.getElementById("user_mode").value;
	var role_code = document.getElementById("role_code").value;
	var user_status = document.getElementById("user_status").value;
	
	var formdata = $('#userForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	// var cp = document.getElementById("globalcontextpath").value;
	//var actionUrl = "./userDetails";
	var actionUrl = "./searchUserConfigDetail?user_name=" + user_name + "&user_type="
			+ user_type+ "&user_mode="+ user_mode+ "&role_code="+ role_code+ "&user_status="
			+ user_status;
	ajaxPostDataArray(actionUrl, fdata, searchFilterBrowseResponse);
}

function searchRoleFilter() {

	var formdata = $('#userRoleForm').serializeArray();
	var fdata = JSON.stringify(formdata);

	var role_name = $('#role_name').val();
	var role_status = $('#role_status').val();

	if (lhsIsNull(role_name) && lhsIsNull(role_status)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		var actionUrl = "roleDetails";
		ajaxPostDataArray(actionUrl, fdata, searchFilterBrowseResponse);

	}
}

function searchFilterBrowseResponse(response) {
	// console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);

	loadDataUsingPaginatorGrid();

}// end function

function resetUserFilter() {
	$("#userForm").find("input[type=text], textarea, select").val("");
	refreshUser();
}

function resetRoleFilter() {
	$("#userRoleForm").find("input[type=text], textarea, select").val("");
	refreshRole();
}

function refreshUser() {
	var url = "./userDetails";
	window.location = url;
}

function refreshRole() {
	var url = "./roleDetails";
	window.location = url;
}

function editRole(id) {
	var split_id = id.split('~');
	var role_code = split_id[2];

	var url = "addNewRole?role_code=" + role_code;
	window.location = url;
}

function saveRole() {
	if (validateRoleEntryForm()) {
		ajaxSubmitPostData("./saveOrUpdateRole", "userRoleForm",
				saveRoleResponse);
	}
}

function saveRoleResponse(response) {
	// alert("response="+response);
	if (response === "success") {
		var action = $('#action').val();
		var msg = "";
		if (action == "save") {
			msg = "Role saved successfully !";
		} else if (action == "edit") {
			msg = "Role updated successfully !";
		}
		openSuccessModal(msg, "refreshRole()");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function validateRoleEntryForm() {
	var selectedVal = $("#multiselect").val();
	var status;
	var role_type_code = $('#role_type_code').val();
	var role_name = $('#role_name').val();
	var role_status = $('#role_status').val();
	var assigned_dashboard = $('#assigned_dashboard').val();

	var checkboxes = document.getElementsByName('menu');
	var assigned_menu = selectedVal.toString().replaceAll(',', '#');
	// for (var i=0, n=checkboxes.length;i<n;i++)
	// {
	// if (checkboxes[i].checked)
	// {
	// assigned_menu += checkboxes[i].value + "#";
	// }
	// }

	if (lhsIsNull(role_type_code)) {
		openErrorModal("Please enter the Role Type Code.");
		status = false;
	} else if (lhsIsNull(role_name)) {
		openErrorModal("Please enter the Role Name.");
		status = false;
	} else if (lhsIsNull(role_status)) {
		openErrorModal("Please select Status.");
		status = false;
	} else if (lhsIsNull(assigned_menu)) {
		openErrorModal("Please select Assigned Menu.");
		status = false;
	} else if (lhsIsNull(assigned_dashboard)) {
		openErrorModal("Please select Assigned dashboard.");
		status = false;
	} else {
		$("#assigned_menu").val(assigned_menu);
		$("#role_name").val(role_name.trim().toUpperCase());
		status = true;
	}

	return status;
}// End function

var checkedId = '';
function openActionDiv(id) {
	checkedId = '';
	// alert(id);
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

function editRecord(funName) {
	if (!lhsIsNull(checkedId)) {
		// funName(checkedId);
		window[funName](checkedId);
	}
}

function deleteRecord1(funName) {
	if (!lhsIsNull(checkedId)) {
		// funName(checkedId);
		window[funName](checkedId);
	}
}
function deleteDocdetails(id) {
	// alert(id);
	openConfirmModal("Do you want to delete ?", "deleteSelectedRow('" + id + "')");
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





function viewUser() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var role_code = split_id[2];
		var url = "./viewUserDetail?role_code=" + role_code;
		ajaxPostUrl(url, viewWorkResponse);
	}
}


function viewWorkResponse(response) {
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


function hideDownloadFunction() {
	
		document.getElementById('bulk_download_btn').style.display = 'none';
	
}

function LoadData(){
	document.getElementById('bulk_download_btn').style.display = 'none';

}

function showFieldsOnUserType(){
var user_type = $("#user_type").val();
document.getElementById("usertype").value=user_type;
if(user_type === 'ONSITE'){
$(".userDiv").css('display', 'flex');	
}else if(user_type === 'LHS'){
$(".userDiv").css('display', 'none');	
}else if(user_type === 'FGS'){
$(".userDiv").css('display', 'none');	
}
}


function onLoadUserType(){
var usertype = $("#user_type").val();
var action = $("#action").val();
if(action === 'edit' && usertype === 'LHS'){
	document.getElementById('user_Div').style.display = 'none';
}else if(action === 'edit' && usertype === 'FGS'){
	document.getElementById('user_Div').style.display = 'none';
}else if(action === 'edit' && usertype === 'ONSITE'){
	document.getElementById('user_Div').style.display = 'flex';
}else if(action === 'save'){
	document.getElementById('user_Div').style.display = 'none';
}else if(action === 'save' && usertype === 'ONSITE'){
	document.getElementById('user_Div').style.display = 'flex';
}
}


function getentityName(){
	var entity_code = $("#entity_code").val();
	
	if(!lhsIsNull(entity_code)){

		var url = "getEntityName?entity_code=" + entity_code;
		ajaxPostUrl(url, getEntityResponse);
	}
}

function getEntityResponse(response) { 
	//var entity_name = $('#entity_name');

if (!lhsIsNull(response)) {
	
	document.getElementById('entity_name').innerHTML = response;	
} 
	
}

