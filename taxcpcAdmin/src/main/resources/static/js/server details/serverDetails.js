/**
 * @aman.dahat
 */



function saveServerEntry() {
	var config_type = document.getElementById('configuration_type').value;

	if (config_type == 'PSC') {
		if (validateServerForm()) {
			ajaxSubmitPostData("physicalServerEntry", "serverEntryForm", serverEntryResponse);
		}
	} else if (config_type == 'ASC' || config_type == 'DBC' || config_type == 'WEBC') {
		if (validateServerForm()) {
			ajaxSubmitPostData("appServerDetailEntry", "serverEntryForm", serverEntryResponse);
		}
	}
}

function serverEntryResponse(response) {
	var config_type = document.getElementById('configuration_type').value;
	var action = document.getElementById('action').value;

	if (response === "success" && action == "save") {
		openSuccessModal("Data Saved Successfully!", "resetServerEntry()");
	} else if (response === "success" && action == "edit") {
		openSuccessModal("Data Updated Successfully!", "resetServerEntry()");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function resetServerEntry() {
	var cp = document.getElementById("globalcontextpath").value;
	window.location.href = cp + 'serverDetailDashboard?server_type=PSC';
}

function validateServerForm() {
	var status;
	var config_type = $("#configuration_type").val();
	var server_owner_name = $("#server_owner_name").val();
	var server_ip = $("#server_ip").val();
	var entity_code = $("#entity_code").val();
	var host_name = $("#host_name").val();
	var server_os = $("#server_os").val();
	var remote_username = $("#remote_username").val();
	var remote_pwd = $("#remote_pwd").val();
	var mapped_drive = $("#mapped_drive").val();
	var tag_name = $("#tag_name").val();

	var app_server_name = $("#app_server_name").val();
	var app_server_ip = $("#app_server_ip").val();
	var app_server_ip_1 = $("#app_server_ip_1").val();
	var app_server_port = $("#app_server_port").val();
	var app_server_console_add = $("#app_server_console_add").val();
	var app_server_log_path = $("#app_server_log_path").val();
	var app_server_username = $("#app_server_username").val();
	var app_server_pwd = $("#app_server_pwd").val();

	var web_server_name = $("#web_server_name").val();
	var web_server_ip = $("#web_server_ip").val();
	var web_server_ip_1 = $("#web_server_ip_1").val();
	var web_server_port = $("#web_server_port").val();
	var web_server_console_add = $("#web_server_console_add").val();
	var web_server_log_path = $("#web_server_log_path").val();
	var web_server_username = $("#web_server_username").val();
	var web_server_pwd = $("#web_server_pwd").val();

	var db_server_ip = $("#db_server_ip").val();
	var installed_db = $("#installed_db").val();
	var installed_db_tool = $("#installed_db_tool").val();

	//	if (lhsIsNull(config_type)) {
	//		openErrorModal("Please Select Configuration Type.");
	//		status = false;
	//	} else
	if (config_type == 'PSC') {//physical
		if (lhsIsNull(server_owner_name)) {
			openErrorModal("Please Select Server Owner Name.");
			status = false;
		} else if (!lhsIsNull(server_owner_name) && server_owner_name == 'BANK' && entity_code == '') {
			//	if (entity_code == '') {
			openErrorModal("Please Select Entity Name.");
			status = false;
			//}
		} else if (lhsIsNull(server_ip)) {
			openErrorModal("Please Enter Server IP.");
			status = false;
		} else if (lhsIsNull(host_name)) {
			openErrorModal("Please Enter Host Name.");
			status = false;
		} else if (lhsIsNull(server_os)) {
			openErrorModal("Please Select Server OS.");
			status = false;
		} else if (lhsIsNull(remote_username)) {
			openErrorModal("Please Enter Remote Username.");
			status = false;
		} else if (lhsIsNull(remote_pwd)) {
			openErrorModal("Please Enter Remote Password.");
			status = false;
		} else if (mapped_drive == "") {
			openErrorModal("Please Select Drive Name.");
			status = false;
		} else if (lhsIsNull(tag_name)) {
			openErrorModal("Please Enter Tag Name.");
			status = false;
		} else if ($("#server_remark").val().length > 240) {
			openErrorModal("Remark Exceed Maximum Limit Of Characters.");
			status = false;
		} else {
			status = true;
		}
	} else if (config_type == 'ASC') { //Application
		if (lhsIsNull(app_server_ip)) {
			openErrorModal("Please Select Server IP.");
			status = false;
		} else if (lhsIsNull(app_server_name)) {
			openErrorModal("Please Select App Server Name.");
			status = false;
		} else if (lhsIsNull(app_server_ip_1)) {
			openErrorModal("Please Enter App Server IP.");
			status = false;
		} else if (lhsIsNull(app_server_port)) {
			openErrorModal("Please Enter App Server Port.");
			status = false;
		} else if (lhsIsNull(app_server_console_add)) {
			openErrorModal("Please Enter App Server Console Address.");
			status = false;
		} else if (lhsIsNull(app_server_log_path)) {
			openErrorModal("Please Enter App Server Log Path.");
			status = false;
		} else if (lhsIsNull(app_server_username)) {
			openErrorModal("Please Enter App Server Username.");
			status = false;
		} else if (lhsIsNull(app_server_pwd)) {
			openErrorModal("Please Enter App Server Password.");
			status = false;
		} else if ($("#app_server_remark").val().length > 240) {
			openErrorModal("Remark Exceed Maximum Limit Of Characters.");
			status = false;
		} else {
			status = true;
		}
	} else if (config_type === 'WEBC') {
		if (lhsIsNull(web_server_ip)) {
			openErrorModal("Please Select Server IP.");
			status = false;
		} else if (lhsIsNull(web_server_name)) {
			openErrorModal("Please Select Web Server Name.");
			status = false;
		} else if (lhsIsNull(web_server_ip_1)) {
			openErrorModal("Please Enter Web Server IP.");
			status = false;
		} else if (lhsIsNull(web_server_port)) {
			openErrorModal("Please Enter Web Server Port.");
			status = false;
		} else if (lhsIsNull(web_server_console_add)) {
			openErrorModal("Please Enter Web Server Console Address.");
			status = false;
		} else if (lhsIsNull(web_server_log_path)) {
			openErrorModal("Please Enter Web Server Log Path.");
			status = false;
		} else if (lhsIsNull(web_server_username)) {
			openErrorModal("Please Enter Web Server Username.");
			status = false;
		} else if (lhsIsNull(web_server_pwd)) {
			openErrorModal("Please Enter Web Server Password.");
			status = false;
		} else if ($("#app_server_remark").val().length > 240) {
			openErrorModal("Remark Exceed Maximum Limit Of Characters.");
			status = false;
		} else {
			status = true;
		}

	} else if (config_type === 'DBC') {
		if (lhsIsNull(db_server_ip)) {
			openErrorModal("Please Select Server IP.");
			status = false;
		} else if (lhsIsNull(installed_db)) {
			openErrorModal("Please Enter Installed Database Name.");
			status = false;
		} else if (lhsIsNull(installed_db_tool)) {
			openErrorModal("Please Enter Installed Database Tool Name.");
			status = false;
		} else if ($("#server_remark").val().length > 240) {
			openErrorModal("Remark Exceed Maximum Limit Of Characters.");
			status = false;
		} else {
			status = true;
		}
	} else {
		openErrorModal("Some Error Occured..! ");
		status = false;
	}
	return status;
}


function copyContentApp() {
	var a = document.getElementById('app_server_ip_1').value;

	document.getElementById('app_server_console_add').value = a;

	var b = document.getElementById('app_server_port').value;
	document.getElementById('port').value = b;

}
function copyContentWeb() {
	var a = document.getElementById('web_server_ip').value;

	document.getElementById('web_server_console_add').value = a;

	var b = document.getElementById('web_server_port').value;
	document.getElementById('port1').value = b;

}

function validateIPaddress(inputElement) {

	var ipaddress = inputElement.value;
	var server_ip = $("#server_ip").val();
	var public_ip = $("#public_ip").val();

	if (server_ip != '') {
		if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
			.test(ipaddress)) {
			return (true)
		}
		else {
			openErrorModal('You have entered an invalid IP address!', '')
			$('#' + inputElement.id).focus();
			inputElement.value = '';
			$('#host_name').val('');
			return (false)
		}
	}
	else if (public_ip != '') {
		if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
			.test(ipaddress)) {
			return (true)
		}
		else {
			openErrorModal('You have entered an invalid IP address!', '')
			$('#' + inputElement.id).focus();
			inputElement.value = '';
			$('#host_name').val('');
			return (false)
		}

	}
}

function validateIPaddress1(inputElement) {

	var ipaddress = inputElement.value;
	var server_ip = $("#app_server_ip").val();


	if (server_ip != '') {
		if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
			.test(ipaddress)) {
			return (true)
		}
		else {
			openErrorModal('You have entered an invalid IP address!', '')
			$('#' + inputElement.id).focus();
			inputElement.value = '';
			$('#host_name').val('');
			$('#app_server_console_add').val('');
			return (false)
		}
	}

}

function validateIPaddress2(inputElement) {

	var ipaddress = inputElement.value;
	var server_ip = $("#web_server_ip").val();


	if (server_ip != '') {
		if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/
			.test(ipaddress)) {
			return (true)
		}
		else {
			openErrorModal('You have entered an invalid IP address!', '')
			$('#' + inputElement.id).focus();
			inputElement.value = '';
			$('#host_name').val('');
			$('#web_server_console_add').val('');
			return (false)
		}
	}

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
	$('#port').val('');
	return (false)
}

function validatePortNumber1(inputElement) {
	var ipaddress = inputElement.value;
	if (/^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/
		.test(ipaddress)) {
		return (true)
		copyContentWeb();
	}
	openErrorModal('You have entered an invalid Port Number!', '')
	$('#' + inputElement.id).focus();
	inputElement.value = '';
	$('#port1').val('');
	return (false)
}

function onLoadRadioButton() {
	document.getElementById('bulk_download_btn').style.display = 'none';
}

function getServerData(server_id) {
	// alert("id= "+server_id);
	id_count = server_id.split("-");
	var server_id1 = document.getElementById("parent_server11-" + id_count[1]).value;

	// var server_id1 = document.getElementById("parent_server11").value;

	// alert("server_id = "+server_id1)
	var server_type_code = document.getElementById("server_type_code-" + id_count[1]).value;
	// alert("server_type_code = "+server_type_code)
	// var x = document.getElementById("displayAppServerDiv");
	var x = document.getElementById("displayAppServerDiv_" + id_count[1]);
	var cp = document.getElementById("globalcontextpath").value;
	if (x.style.display === "none") {
		x.style.display = "block";
		var data = 'server_id=' + encodeURIComponent(server_id1)
			+ '&server_type_code=' + encodeURIComponent(server_type_code);
		$
			.ajax({
				type: "GET",
				contentType: "application/json",
				url: cp + "getAppServerListWithStatus",
				data: data,
				cache: false,
				timeout: 600000,
				success: function(response) {
					// document.getElementById("tableContent").innerHTML =
					// response
					document.getElementById("tableContent_" + id_count[1]).innerHTML = response
					const serverIPPort = [];
					$('#appServerTable_' + id_count).find('tr').each(
						function() {
							if (!this.rowIndex)
								return; // skip first row
							var serverIp = this.cells[2].innerHTML;
							var serverPort = this.cells[3].innerHTML;
							serverIPPort.push(serverIp + "#"
								+ serverPort);
						});
				},
				error: function(e) {
					openErrorModal('Please Add Application Server, Not Single One Configured.');
				}
			});
	} else {
		x.style.display = "none";
	}
}

let server_id_new = "";
let server_type_code_new = "";

function deleteServerDetails(id) {
	var id1 = id.split("-");
	var server_id = id1[0];
	var server_type_code = id1[1];

	server_id_new = server_id;
	server_type_code_new = server_type_code;
	var url = "./deleteServerTemp?server_id=" + server_id + "&server_type_code=" + server_type_code;
	ajaxPostUrl(url, deleteServerDetailsResponse);
}

function deleteServerDetailsResponse(response) {

	if (response == 0) {
		openConfirmModal("Do You Want To Delete ?", "deleteSelectedServer('" + server_id_new + "')");
	} else {
		openConfirmModal("Delete Application Server First", "closeConfirmModal();");
	}
}

function deleteSelectedServer(server_id) {
	closeConfirmModal();
	var server_type_code = server_type_code_new;
	var url = "./deleteServer?server_id=" + server_id + "&server_type_code=" + server_type_code_new;
	ajaxPostUrl(url, deleteServerResponse);
}

function deleteServerResponse(response) {
	// alert("response..."+response);
	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "window.location.reload();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function
//app
function deleteAppServerDetails(server_id) {

	//	server_id_new = server_id;
	//	var url = "./deleteAppServerTemp?server_id=" + server_id;
	//	ajaxPostUrl(url, deleteAppServerDetails1);
	openConfirmModal("Do You Want To Delete ?", "deleteAppServer('" + server_id + "')");

}

function deleteAppServerDetails1(response) {
	if (response == 0) {
		openConfirmModal("Do You Want To Delete ?", "deleteAppServer('" + server_id + "')");
	} else {
		openConfirmModal("This Contains Application Name , Delete Application Name First", "closeConfirmModal();");
	}
}

function deleteAppServer(server_id) {
	closeConfirmModal();
	var server_type_code = server_type_code_new;
	var url = "./deleteAppServer?server_id=" + server_id + "&server_type_code=" + server_type_code_new;
	ajaxPostUrl(url, deleteAppServerResponse);
}

function deleteAppServerResponse(response) {
	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "window.location.reload();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function editServerDetails(id) {

	if (!lhsIsNull(id)) {
		var id1 = id.split("-");
		var server_id = id1[0];
		var server_type_code = id1[1];

		var cp = document.getElementById("globalcontextpath").value;

		window.location = cp + "serverDetailEntry?server_id=" + server_id
			+ "&server_type_code=" + server_type_code;
	}

}

function viewServerDetails(id) {

	if (!lhsIsNull(id)) {
		var id1 = id.split("-");
		var server_id = id1[0];
		var server_type_code = id1[1];

		var cp = document.getElementById("globalcontextpath").value;
		// var server_type_code =
		// document.getElementById("server_type_code_edit").value;

		// window.location = cp + "serverDetailEntry?server_id=" + server_id;
		var url = "viewServerDetails?server_id=" + server_id;
		// + "&server_type_code=" + server_type_code;

		ajaxPostUrl(url, viewServerDetailsResponse);
	}

}

function viewServerDetailsResponse(response) {
	// alert(response)
	if (!lhsIsNull(response)) {
		// alert(response);
		$("#serverModelTableBody").html("");
		$("#serverModelTableBody").html(response);
		$("#myModal").modal({
			show: true
		});

	} else {
		$("#myModal").modal({
			show: true
		});
	}
}

function editAppServerDetails(server_id) {
	var cp = document.getElementById("globalcontextpath").value;
	var server_type_code = document.getElementById("type_code").value;

	window.location = cp + "serverDetailEntry?server_id=" + server_id
		+ "&server_type_code=" + server_type_code;

}

var count1;

function GetDynamicTextBox(num) {
	var size = document.getElementById('size').value;
	var app_name = "";
	var local_app_url = "";
	var public_app_url = "";
	var app_code = "";
	if (lhsIsNull(count1)) {
		count1 = num;

	}
	if (num <= size) {
		app_name = document.getElementById('app_name-' + num).value;
		local_app_url = document.getElementById('local_app_url-' + num).value;
		public_app_url = document.getElementById('public_app_url-' + num).value;
		app_code = document.getElementById('app_code-' + num).value;
	}

	document.getElementById('boxCount').value = count1;
	return ('<div class="input-group mb-1 mr-2" style="padding:10px ; width:700px"><input type="hidden" id="app_code~'
		+ count1
		+ '~0" value=\"'
		+ app_code
		+ '\"/><input class="form-control form-control-sm" id="DynamicTextBox~'
		+ count1
		+ '~1" name="DynamicTextBox1" type="text" value=\"'
		+ app_name
		+ '\"  placeholder="Enter App Name"/><input class="form-control form-control-sm" id="DynamicTextBox~'
		+ count1
		+ '~2" name="DynamicTextBox2" type="text" value=\"'
		+ local_app_url
		+ '\" placeholder="Enter Local App URL"/><input class="form-control form-control-sm" id="DynamicTextBox~'
		+ count1
		+ '~3" name="DynamicTextBox3" type="text" value=\"'
		+ public_app_url + '\" placeholder="Enter Public App URL"/><div class="input-group-append mr-2"><button class="btn btn-primary btn-sm rounded-0" type="button" onclick="RemoveTextBox(this)" data-toggle="tooltip" data-placement="top" title="Delete"><i class="fa fa-trash"></i></button></div></div>'

	);

}

function AddTextBox() {
	var boxcount = document.getElementById('boxCount').value;

	var a = document.getElementById('app_name').value;
	document.getElementById("Div").style.display = "block";
	count1 = boxcount;
	count1++;
	var div = document.createElement("DIV");
	div.innerHTML = GetDynamicTextBox(count1);
	document.getElementById("TextBoxContainer").appendChild(div);

}

function AddTextBox1() {
	var size = document.getElementById('size').value;

	if (size > 0) {
		for (let i = 1; i <= size; i++) {
			var div = document.createElement("DIV");
			div.innerHTML = GetDynamicTextBox(i);
			document.getElementById("TextBoxContainer").appendChild(div);
			count1++;
		}
	}

	count1 = size;
}

function RemoveTextBox(div) {
	document.getElementById("TextBoxContainer").removeChild(
		div.parentNode.parentNode.parentNode);

	count1--;
	document.getElementById('boxCount').value = count1;
}

function changeBtnText() {
	var app_server_name = document.getElementById("app_server_name").value;
	var app_server_ip = document.getElementById("app_server_ip").value;

	if (!lhsIsNull(app_server_name) && !lhsIsNull(app_server_ip)) {
		document.getElementById("btn-1").textContent = "Update & Next";
	} else {
		document.getElementById("btn-1").textContent = "Save & Next";
	}

}

function serachByServerType(server_type) {
	var formdata = $('#serverForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	
	document.getElementById('server_owner_name').value ='';
	document.getElementById('server_ip').value ='';
	document.getElementById('tag_name').value ='';
	document.getElementById('entity_code').value ='';
	
	var url = "./serverDetailDashboard?server_type=" + server_type;
	ajaxPostDataArray(url, fdata, SearchResponse);

}

function SearchResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();
} // function

function selectEntity() {
	var server_owner = document.getElementById('server_owner_name').value;
	if (server_owner == 'BANK') {
		document.getElementById('span_id').innerHTML = '*';

	} else {
		document.getElementById('span_id').innerHTML = '';
	}
	var url = "getEntityList?server_owner=" + server_owner;
	ajaxPostUrl(url, selectEntityResponse);

}

function selectEntityResponse(response) {
	var entity_code = $('#entity_code');
	entity_code.empty();
	if (!lhsIsNull(response)) {

		$.each(response, function(k, v) {
			entity_code.append('<option value=' + k + '>' + v + '</option>');
			console.log("key " + k + "value " + v);
		});

		$('#entity_code').multiselect({
			buttonWidth: '160px',
			includeSelectAllOption: true,
			nonSelectedText: '--Select Entity Name--'
		});
	} else {
		entity_code.append('<option value="">--Select--</option>');
	}
}

function selectConfigType() {
	var type = document.getElementById('configuration_type').value;
	if (type == 'PSC') {

		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "block";
		document.getElementById("app_server_entry").style.display = "none";
		document.getElementById("web_server_entry").style.display = "none";
		document.getElementById("db_server_entry").style.display = "none";

		//	document.getElementById('serverTypeDiv').style.display = 'flex';
		document.getElementById('serverNameDiv').style.display = 'flex';
		document.getElementById('entityNameDiv').style.display = 'flex';
		document.getElementById('serverIpDiv').style.display = 'flex';
		document.getElementById('publicIpDiv').style.display = 'flex';
		document.getElementById('hostNameDiv').style.display = 'flex';
		document.getElementById('serverOsDiv').style.display = 'flex';
		document.getElementById('remoteUserNameDiv').style.display = 'flex';
		document.getElementById('remotePwdDiv').style.display = 'flex';
		document.getElementById('driveDiv').style.display = 'flex';
		document.getElementById('tagNameDiv').style.display = 'flex';
		document.getElementById('remarkDiv').style.display = 'flex';
		document.getElementById('btns_1').style.display = 'flex';

		//	document.getElementById('serverIPDiv').style.display = 'none';
		document.getElementById('appServerIPDiv').style.display = 'none';
		document.getElementById('appServerIPDiv1').style.display = 'none';
		document.getElementById('appServerDiv').style.display = 'none';
		document.getElementById('appServerPortDiv').style.display = 'none';
		document.getElementById('appServerConsoleDiv').style.display = 'none';
		document.getElementById('appServerLogPathDiv').style.display = 'none';
		document.getElementById('AppUsernameDiv').style.display = 'none';
		document.getElementById('appPasswordDiv').style.display = 'none';
		document.getElementById('appTagNameDiv').style.display = 'none';
		document.getElementById('appRemarkDiv').style.display = 'none';

		document.getElementById('webServerIPDiv').style.display = 'none';
		document.getElementById('webServerIPDiv1').style.display = 'none';
		document.getElementById('webServerDiv').style.display = 'none';
		document.getElementById('webServerPortDiv').style.display = 'none';
		document.getElementById('webServerConsoleDiv').style.display = 'none';
		document.getElementById('webServerLogPathDiv').style.display = 'none';
		document.getElementById('webUsernameDiv').style.display = 'none';
		document.getElementById('webPasswordDiv').style.display = 'none';
		document.getElementById('webTagNameDiv').style.display = 'none';
		document.getElementById('webRemarkDiv').style.display = 'none';
		document.getElementById('dbServerIPDiv').style.display = 'none';
		document.getElementById('dbServerIpDiv1').style.display = 'none';
		document.getElementById('databaseDiv').style.display = 'none';
		document.getElementById('databaseToolDiv').style.display = 'none';
	} else if (type == 'ASC') {

		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "none";
		document.getElementById("app_server_entry").style.display = "block";
		document.getElementById("web_server_entry").style.display = "none";
		document.getElementById("db_server_entry").style.display = "none";

		//	document.getElementById('serverTypeDiv').style.display = 'none';
		document.getElementById('serverNameDiv').style.display = 'none';
		document.getElementById('entityNameDiv').style.display = 'none';
		document.getElementById('serverIpDiv').style.display = 'none';
		document.getElementById('publicIpDiv').style.display = 'none';
		document.getElementById('hostNameDiv').style.display = 'none';
		document.getElementById('serverOsDiv').style.display = 'none';
		document.getElementById('remoteUserNameDiv').style.display = 'none';
		document.getElementById('remotePwdDiv').style.display = 'none';
		document.getElementById('driveDiv').style.display = 'none';
		document.getElementById('databaseDiv').style.display = 'none';
		document.getElementById('databaseToolDiv').style.display = 'none';
		document.getElementById('tagNameDiv').style.display = 'none';
		document.getElementById('remarkDiv').style.display = 'none';
		document.getElementById('btns_1').style.display = 'flex';

		document.getElementById('webServerIPDiv').style.display = 'none';
		document.getElementById('webServerDiv').style.display = 'none';
		document.getElementById('webServerPortDiv').style.display = 'none';
		document.getElementById('webServerConsoleDiv').style.display = 'none';
		document.getElementById('webServerLogPathDiv').style.display = 'none';
		document.getElementById('webUsernameDiv').style.display = 'none';
		document.getElementById('webPasswordDiv').style.display = 'none';
		document.getElementById('webTagNameDiv').style.display = 'none';
		document.getElementById('webRemarkDiv').style.display = 'none';
		document.getElementById('dbServerIPDiv').style.display = 'none';
		document.getElementById('dbServerIpDiv1').style.display = 'none';

		document.getElementById('appServerIPDiv1').style.display = 'flex';
		document.getElementById('appServerIPDiv').style.display = 'flex';
		document.getElementById('appServerDiv').style.display = 'flex';
		document.getElementById('appServerPortDiv').style.display = 'flex';
		document.getElementById('appServerConsoleDiv').style.display = 'flex';
		document.getElementById('appServerLogPathDiv').style.display = 'flex';
		document.getElementById('AppUsernameDiv').style.display = 'flex';
		document.getElementById('appPasswordDiv').style.display = 'flex';
		document.getElementById('appTagNameDiv').style.display = 'flex';
		document.getElementById('appRemarkDiv').style.display = 'flex';

		var btnName = document.getElementById("btn-1").textContent;
		var y = btnName.innerHTML = "Save & Exit";
		document.getElementById("btn-1").innerText = y;
	} else if (type == 'WEBC') {

		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "none";
		document.getElementById("app_server_entry").style.display = "none";
		document.getElementById("web_server_entry").style.display = "block";
		document.getElementById("db_server_entry").style.display = "none";



		//document.getElementById('serverTypeDiv').style.display = 'none';
		document.getElementById('serverNameDiv').style.display = 'none';
		document.getElementById('entityNameDiv').style.display = 'none';
		document.getElementById('serverIpDiv').style.display = 'none';
		document.getElementById('publicIpDiv').style.display = 'none';
		document.getElementById('hostNameDiv').style.display = 'none';
		document.getElementById('serverOsDiv').style.display = 'none';
		document.getElementById('remoteUserNameDiv').style.display = 'none';
		document.getElementById('remotePwdDiv').style.display = 'none';
		document.getElementById('driveDiv').style.display = 'none';
		document.getElementById('databaseDiv').style.display = 'none';
		document.getElementById('databaseToolDiv').style.display = 'none';
		document.getElementById('tagNameDiv').style.display = 'none';
		document.getElementById('remarkDiv').style.display = 'none';
		document.getElementById('btns_1').style.display = 'flex';
		document.getElementById('dbServerIPDiv').style.display = 'none';
		document.getElementById('dbServerIpDiv1').style.display = 'none';

		document.getElementById('appServerIPDiv').style.display = 'none';
		document.getElementById('appServerIPDiv1').style.display = 'none';
		document.getElementById('appServerDiv').style.display = 'none';
		document.getElementById('appServerPortDiv').style.display = 'none';
		document.getElementById('appServerConsoleDiv').style.display = 'none';
		document.getElementById('appServerLogPathDiv').style.display = 'none';
		document.getElementById('AppUsernameDiv').style.display = 'none';
		document.getElementById('appPasswordDiv').style.display = 'none';
		document.getElementById('appTagNameDiv').style.display = 'none';
		document.getElementById('appRemarkDiv').style.display = 'none';

		document.getElementById('webServerIPDiv').style.display = 'flex';
		document.getElementById('webServerIPDiv1').style.display = 'flex';
		document.getElementById('webServerDiv').style.display = 'flex';
		document.getElementById('webServerPortDiv').style.display = 'flex';
		document.getElementById('webServerConsoleDiv').style.display = 'flex';
		document.getElementById('webServerLogPathDiv').style.display = 'flex';
		document.getElementById('webUsernameDiv').style.display = 'flex';
		document.getElementById('webPasswordDiv').style.display = 'flex';
		document.getElementById('webTagNameDiv').style.display = 'flex';
		document.getElementById('webRemarkDiv').style.display = 'flex';

		var btnName = document.getElementById("btn-1").textContent;
		var y = btnName.innerHTML = "Save & Exit";
		document.getElementById("btn-1").innerText = y;
	} else if (type == 'DBC') {

		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "none";
		document.getElementById("app_server_entry").style.display = "none";
		document.getElementById("web_server_entry").style.display = "none";
		document.getElementById("db_server_entry").style.display = "block";

		//document.getElementById('serverIPDiv').style.display = 'flex';
		document.getElementById('databaseDiv').style.display = 'flex';
		document.getElementById('databaseToolDiv').style.display = 'flex';

		document.getElementById('serverNameDiv').style.display = 'none';
		document.getElementById('entityNameDiv').style.display = 'none';
		document.getElementById('serverIpDiv').style.display = 'none';
		document.getElementById('publicIpDiv').style.display = 'none';

		document.getElementById('hostNameDiv').style.display = 'none';
		document.getElementById('serverOsDiv').style.display = 'none';
		document.getElementById('remoteUserNameDiv').style.display = 'none';
		document.getElementById('remotePwdDiv').style.display = 'none';
		document.getElementById('driveDiv').style.display = 'none';
		document.getElementById('tagNameDiv').style.display = 'none';
		document.getElementById('remarkDiv').style.display = 'flex';
		document.getElementById('btns_1').style.display = 'flex';
		document.getElementById('dbServerIPDiv').style.display = 'flex';
		document.getElementById('dbServerIpDiv1').style.display = 'flex';

		document.getElementById('appServerIPDiv').style.display = 'none';
		document.getElementById('appServerIPDiv1').style.display = 'none';
		document.getElementById('appServerDiv').style.display = 'none';
		document.getElementById('appServerPortDiv').style.display = 'none';
		document.getElementById('appServerConsoleDiv').style.display = 'none';
		document.getElementById('appServerLogPathDiv').style.display = 'none';
		document.getElementById('AppUsernameDiv').style.display = 'none';
		document.getElementById('appPasswordDiv').style.display = 'none';
		document.getElementById('appTagNameDiv').style.display = 'none';
		document.getElementById('appRemarkDiv').style.display = 'none';

		document.getElementById('webServerIPDiv').style.display = 'none';
		document.getElementById('webServerIPDiv1').style.display = 'none';
		document.getElementById('webServerDiv').style.display = 'none';
		document.getElementById('webServerPortDiv').style.display = 'none';
		document.getElementById('webServerConsoleDiv').style.display = 'none';
		document.getElementById('webServerLogPathDiv').style.display = 'none';
		document.getElementById('webUsernameDiv').style.display = 'none';
		document.getElementById('webPasswordDiv').style.display = 'none';
		document.getElementById('webTagNameDiv').style.display = 'none';
		document.getElementById('webRemarkDiv').style.display = 'none';
	} else {
		openErrorModal("Please Select Configuration Type!");
	}
}

function validateServerName() {
	var configuration_type = document.getElementById('configuration_type').value;
	var app_server_name = document.getElementById('app_server_name').value;
	var app_server_ip = document.getElementById('app_server_ip').value;
	var url = "validateServerName?configuration_type=" + configuration_type + "&app_server_ip=" + app_server_ip
		+ "&app_server_name=" + app_server_name;
	ajaxPostUrl(url, validateServerNameResponse);

}

function validateServerNameResponse(response) {
	var status;
	if (!lhsIsNull(response)) {
		openErrorModal("Selected App Server Already Exists!");
		status = false;
	} else {
		status = true;
	}
}

function setPublicIpApp() {
	var app_server_ip = document.getElementById('app_server_ip').value;
	if (!lhsIsNull(app_server_ip)) {
		var url = "setPublicIp?app_server_ip=" + app_server_ip;
		ajaxPostUrl(url, setPublicIpAppResponse);
	}

}

function setPublicIpAppResponse(response) {
	if (!lhsIsNull(response)) {
		document.getElementById('app_public_ip').value = response;
	} else {
		document.getElementById('app_public_ip').value = "";
	}
}

function setPublicIpWeb() {
	var app_server_ip = document.getElementById('web_server_ip').value;
	if (!lhsIsNull(app_server_ip)) {
		var url = "setPublicIp?app_server_ip=" + app_server_ip;
		ajaxPostUrl(url, setPublicIpWebResponse);
	}

}

function setPublicIpWebResponse(response) {
	if (!lhsIsNull(response)) {
		document.getElementById('web_public_ip').value = response;
	} else {
		document.getElementById('web_public_ip').value = "";
	}
}

function setPublicIpDb() {
	var app_server_ip = document.getElementById('db_server_ip').value;
	if (!lhsIsNull(app_server_ip)) {
		var url = "setPublicIp?app_server_ip=" + app_server_ip;
		ajaxPostUrl(url, setPublicIpDbResponse);
	}

}

function setPublicIpDbResponse(response) {
	if (!lhsIsNull(response)) {
		document.getElementById('db_public_ip').value = response;
	} else {
		document.getElementById('db_public_ip').value = "";
	}

	document.getElementById('db_server_ip1').value = document.getElementById('db_server_ip').value;
}

function cancelAppServerEntry() {
	window.location = "serverDetailDashboard?server_type=PSC";
}

function checkServerIp(inputElement) {
	var ipaddress = inputElement.value;

	var url = "checkServerIp?server_id=" + ipaddress;
	ajaxPostUrl(url, checkServerIpResponse);
}

function checkServerIpResponse(response) {
	if (!lhsIsNull(response)) {
		openErrorModal(" Server IP Is Already Exists!");
		document.getElementById('server_ip').value = '';
	} else {

	}
}


function getServerIP() {
	var config_type = $('#configuration_type').val();

	if (!lhsIsNull(config_type)) {
		var url = "./getServerIp?config_type=" + config_type;
		ajaxPostUrl(url, getServerIPResponse);
	} else {
	}
}

function getServerIPResponse(response) {
	var app_server_ip = $('#app_server_ip1');
	app_server_ip.empty();
	if (!lhsIsNull(response)) {

		app_server_ip.append('<option value="">--Select--</option>');
		$.each(response, function(k, v) {
			app_server_ip.append('<option value=' + k + '>' + v + '</option>');
			// console.log("key " + k + "value " + v);
		});
	} else {
		app_server_ip.append('<option value="">--Select--</option>');
	}
}

function resetEntryForm() {


	//document.getElementById('entity_code').selectedIndex = -1;
	//$('#entity_code').multiselect('disable');

	$("#entity_code").multiselect("clearSelection");
	$("#mapped_drive").multiselect("clearSelection");
	document.getElementById('server_owner_name').value = '';
	document.getElementById('server_ip').value = '';
	document.getElementById('public_ip').value = '';
	document.getElementById('host_name').value = '';
	document.getElementById('server_os').value = '';
	document.getElementById('remote_username').value = '';
	document.getElementById('remote_pwd').value = '';
	document.getElementById('tag_name').value = '';
	document.getElementById('server_remark').value = '';

	document.getElementById('app_server_ip').value = ' ';
	document.getElementById('app_public_ip').value = '';
	document.getElementById('app_server_name').value = ' ';
	document.getElementById('app_server_ip_1').value = '';
	document.getElementById('app_server_port').value = '';
	document.getElementById('app_server_console_add').value = '';
	document.getElementById('app_Controller').value = '';
	document.getElementById('app_server_log_path').value = '';
	document.getElementById('app_server_username').value = '';
	document.getElementById('app_server_pwd').value = '';
	document.getElementById('app_server_tag_name').value = '';
	document.getElementById('app_server_remark').value = '';

	document.getElementById('web_server_ip').value = ' ';
	document.getElementById('web_public_ip').value = '';
	document.getElementById('web_server_name').value = ' ';
	document.getElementById('web_server_ip_1').value = '';
	document.getElementById('web_server_port').value = '';
	document.getElementById('web_server_console_add').value = '';
	document.getElementById('web_Controller').value = '';
	document.getElementById('web_server_log_path').value = '';
	document.getElementById('web_server_username').value = '';
	document.getElementById('web_server_pwd').value = '';
	document.getElementById('web_server_tag_name').value = '';
	document.getElementById('web_server_remark').value = '';

	document.getElementById('db_server_ip').value = ' ';
	document.getElementById('db_public_ip').value = '';
	document.getElementById('db_server_ip1').value = '';
	document.getElementById('installed_db').value = '';
	document.getElementById('installed_db_tool').value = '';
}


function onLoadConfigType() {
	var type = document.getElementById('configuration_type').value;
	if (type == 'PSC') {

		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "block";
		document.getElementById("app_server_entry").style.display = "none";
		document.getElementById("web_server_entry").style.display = "none";
		document.getElementById("db_server_entry").style.display = "none";

		document.getElementById('serverNameDiv').style.display = 'flex';
		document.getElementById('entityNameDiv').style.display = 'flex';
		document.getElementById('serverIpDiv').style.display = 'flex';
		document.getElementById('publicIpDiv').style.display = 'flex';
		document.getElementById('hostNameDiv').style.display = 'flex';
		document.getElementById('serverOsDiv').style.display = 'flex';
		document.getElementById('remoteUserNameDiv').style.display = 'flex';
		document.getElementById('remotePwdDiv').style.display = 'flex';
		document.getElementById('driveDiv').style.display = 'flex';
		document.getElementById('tagNameDiv').style.display = 'flex';
		document.getElementById('remarkDiv').style.display = 'flex';
		document.getElementById('btns_1').style.display = 'flex';

		document.getElementById('appServerIPDiv').style.display = 'none';
		document.getElementById('appServerIPDiv1').style.display = 'none';
		document.getElementById('appServerDiv').style.display = 'none';
		document.getElementById('appServerPortDiv').style.display = 'none';
		document.getElementById('appServerConsoleDiv').style.display = 'none';
		document.getElementById('appServerLogPathDiv').style.display = 'none';
		document.getElementById('AppUsernameDiv').style.display = 'none';
		document.getElementById('appPasswordDiv').style.display = 'none';
		document.getElementById('appTagNameDiv').style.display = 'none';
		document.getElementById('appRemarkDiv').style.display = 'none';

		document.getElementById('webServerIPDiv').style.display = 'none';
		document.getElementById('webServerIPDiv1').style.display = 'none';
		document.getElementById('webServerDiv').style.display = 'none';
		document.getElementById('webServerPortDiv').style.display = 'none';
		document.getElementById('webServerConsoleDiv').style.display = 'none';
		document.getElementById('webServerLogPathDiv').style.display = 'none';
		document.getElementById('webUsernameDiv').style.display = 'none';
		document.getElementById('webPasswordDiv').style.display = 'none';
		document.getElementById('webTagNameDiv').style.display = 'none';
		document.getElementById('webRemarkDiv').style.display = 'none';
		document.getElementById('dbServerIPDiv').style.display = 'none';
		document.getElementById('dbServerIpDiv1').style.display = 'none';
		document.getElementById('databaseDiv').style.display = 'none';
		document.getElementById('databaseToolDiv').style.display = 'none';
	} else if (type == 'ASC') {

		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "none";
		document.getElementById("app_server_entry").style.display = "block";
		document.getElementById("web_server_entry").style.display = "none";
		document.getElementById("db_server_entry").style.display = "none";

		document.getElementById('serverNameDiv').style.display = 'none';
		document.getElementById('entityNameDiv').style.display = 'none';
		document.getElementById('serverIpDiv').style.display = 'none';
		document.getElementById('publicIpDiv').style.display = 'none';
		document.getElementById('hostNameDiv').style.display = 'none';
		document.getElementById('serverOsDiv').style.display = 'none';
		document.getElementById('remoteUserNameDiv').style.display = 'none';
		document.getElementById('remotePwdDiv').style.display = 'none';
		document.getElementById('driveDiv').style.display = 'none';
		document.getElementById('databaseDiv').style.display = 'none';
		document.getElementById('databaseToolDiv').style.display = 'none';
		document.getElementById('tagNameDiv').style.display = 'none';
		document.getElementById('remarkDiv').style.display = 'none';
		document.getElementById('btns_1').style.display = 'flex';

		document.getElementById('webServerIPDiv').style.display = 'none';
		document.getElementById('webServerDiv').style.display = 'none';
		document.getElementById('webServerPortDiv').style.display = 'none';
		document.getElementById('webServerConsoleDiv').style.display = 'none';
		document.getElementById('webServerLogPathDiv').style.display = 'none';
		document.getElementById('webUsernameDiv').style.display = 'none';
		document.getElementById('webPasswordDiv').style.display = 'none';
		document.getElementById('webTagNameDiv').style.display = 'none';
		document.getElementById('webRemarkDiv').style.display = 'none';
		document.getElementById('dbServerIPDiv').style.display = 'none';
		document.getElementById('dbServerIpDiv1').style.display = 'none';

		document.getElementById('appServerIPDiv1').style.display = 'flex';
		document.getElementById('appServerIPDiv').style.display = 'flex';
		document.getElementById('appServerDiv').style.display = 'flex';
		document.getElementById('appServerPortDiv').style.display = 'flex';
		document.getElementById('appServerConsoleDiv').style.display = 'flex';
		document.getElementById('appServerLogPathDiv').style.display = 'flex';
		document.getElementById('AppUsernameDiv').style.display = 'flex';
		document.getElementById('appPasswordDiv').style.display = 'flex';
		document.getElementById('appTagNameDiv').style.display = 'flex';
		document.getElementById('appRemarkDiv').style.display = 'flex';

		//	document.getElementById('web_server_ip').value = '';
		document.getElementById('web_public_ip').value = '';
		document.getElementById('web_server_name').value = '';
		document.getElementById('web_server_ip_1').value = '';
		document.getElementById('web_server_port').value = '';
		document.getElementById('web_server_console_add').value = '';
		document.getElementById('web_server_log_path').value = '';
		document.getElementById('web_server_username').value = '';
		document.getElementById('web_server_pwd').value = '';
		document.getElementById('web_server_tag_name').value = '';
		document.getElementById('web_server_remark').value = '';

	} else if (type == 'WEBC') {

		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "none";
		document.getElementById("app_server_entry").style.display = "none";
		document.getElementById("web_server_entry").style.display = "block";
		document.getElementById("db_server_entry").style.display = "none";

		document.getElementById('serverNameDiv').style.display = 'none';
		document.getElementById('entityNameDiv').style.display = 'none';
		document.getElementById('serverIpDiv').style.display = 'none';
		document.getElementById('publicIpDiv').style.display = 'none';
		document.getElementById('hostNameDiv').style.display = 'none';
		document.getElementById('serverOsDiv').style.display = 'none';
		document.getElementById('remoteUserNameDiv').style.display = 'none';
		document.getElementById('remotePwdDiv').style.display = 'none';
		document.getElementById('driveDiv').style.display = 'none';
		document.getElementById('databaseDiv').style.display = 'none';
		document.getElementById('databaseToolDiv').style.display = 'none';
		document.getElementById('tagNameDiv').style.display = 'none';
		document.getElementById('remarkDiv').style.display = 'none';
		document.getElementById('btns_1').style.display = 'flex';
		document.getElementById('dbServerIPDiv').style.display = 'none';
		document.getElementById('dbServerIpDiv1').style.display = 'none';

		document.getElementById('appServerIPDiv').style.display = 'none';
		document.getElementById('appServerIPDiv1').style.display = 'none';
		document.getElementById('appServerDiv').style.display = 'none';
		document.getElementById('appServerPortDiv').style.display = 'none';
		document.getElementById('appServerConsoleDiv').style.display = 'none';
		document.getElementById('appServerLogPathDiv').style.display = 'none';
		document.getElementById('AppUsernameDiv').style.display = 'none';
		document.getElementById('appPasswordDiv').style.display = 'none';
		document.getElementById('appTagNameDiv').style.display = 'none';
		document.getElementById('appRemarkDiv').style.display = 'none';

		document.getElementById('webServerIPDiv').style.display = 'flex';
		document.getElementById('webServerIPDiv1').style.display = 'flex';
		document.getElementById('webServerDiv').style.display = 'flex';
		document.getElementById('webServerPortDiv').style.display = 'flex';
		document.getElementById('webServerConsoleDiv').style.display = 'flex';
		document.getElementById('webServerLogPathDiv').style.display = 'flex';
		document.getElementById('webUsernameDiv').style.display = 'flex';
		document.getElementById('webPasswordDiv').style.display = 'flex';
		document.getElementById('webTagNameDiv').style.display = 'flex';
		document.getElementById('webRemarkDiv').style.display = 'flex';

		//document.getElementById('app_server_ip').value = '';
		document.getElementById('app_public_ip').value = '';
		document.getElementById('app_server_name').value = '';
		document.getElementById('app_server_ip_1').value = '';
		document.getElementById('app_server_port').value = '';
		document.getElementById('app_server_console_add').value = '';
		document.getElementById('app_server_log_path').value = '';
		document.getElementById('app_server_username').value = '';
		document.getElementById('app_server_pwd').value = '';
		document.getElementById('app_server_tag_name').value = '';
		document.getElementById('app_server_remark').value = '';

	} else if (type == 'DBC') {


		document.getElementById("server_entry").style.display = "none";
		document.getElementById("phy_server_entry").style.display = "none";
		document.getElementById("app_server_entry").style.display = "none";
		document.getElementById("web_server_entry").style.display = "none";
		document.getElementById("db_server_entry").style.display = "block";

		document.getElementById('dbServerIPDiv').style.display = 'flex';
		document.getElementById('dbServerIpDiv1').style.display = 'flex';
		document.getElementById('databaseDiv').style.display = 'flex';
		document.getElementById('databaseToolDiv').style.display = 'flex';

		document.getElementById('serverNameDiv').style.display = 'none';
		document.getElementById('entityNameDiv').style.display = 'none';
		document.getElementById('serverIpDiv').style.display = 'none';
		document.getElementById('publicIpDiv').style.display = 'none';

		document.getElementById('hostNameDiv').style.display = 'none';
		document.getElementById('serverOsDiv').style.display = 'none';
		document.getElementById('remoteUserNameDiv').style.display = 'none';
		document.getElementById('remotePwdDiv').style.display = 'none';
		document.getElementById('driveDiv').style.display = 'none';
		document.getElementById('tagNameDiv').style.display = 'none';
		document.getElementById('remarkDiv').style.display = 'flex';
		document.getElementById('btns_1').style.display = 'flex';

		document.getElementById('appServerIPDiv').style.display = 'none';
		document.getElementById('appServerIPDiv1').style.display = 'none';
		document.getElementById('appServerDiv').style.display = 'none';
		document.getElementById('appServerPortDiv').style.display = 'none';
		document.getElementById('appServerConsoleDiv').style.display = 'none';
		document.getElementById('appServerLogPathDiv').style.display = 'none';
		document.getElementById('AppUsernameDiv').style.display = 'none';
		document.getElementById('appPasswordDiv').style.display = 'none';
		document.getElementById('appTagNameDiv').style.display = 'none';
		document.getElementById('appRemarkDiv').style.display = 'none';

		document.getElementById('webServerIPDiv').style.display = 'none';
		document.getElementById('webServerIPDiv1').style.display = 'none';
		document.getElementById('webServerDiv').style.display = 'none';
		document.getElementById('webServerPortDiv').style.display = 'none';
		document.getElementById('webServerConsoleDiv').style.display = 'none';
		document.getElementById('webServerLogPathDiv').style.display = 'none';
		document.getElementById('webUsernameDiv').style.display = 'none';
		document.getElementById('webPasswordDiv').style.display = 'none';
		document.getElementById('webTagNameDiv').style.display = 'none';
		document.getElementById('webRemarkDiv').style.display = 'none';


	}



	var action = $('#action').val();
	if (action == 'edit') {
		document.getElementById('configuration_type').disabled = true;
		document.getElementById('btn-con').disabled = true;

		var btnName = document.getElementById("btn-1").textContent;
		var y = btnName.innerHTML = "Update & Exit";
		document.getElementById("btn-1").innerText = y;
	}


}



function serverDetailFilter(){
	 var config_type = '';
	 
	 if(document.getElementById("PSC").checked){
		 config_type="PSC";
	 }else if(document.getElementById("ASC").checked){
		 config_type="ASC";
	 }else if(document.getElementById("DBC").checked){
		 config_type="DBC";
	 }else if(document.getElementById("WEBC").checked){
		 config_type="WEBC";
	 }
	var formdata = $('#serverForm').serializeArray();
	var fdata = JSON.stringify(formdata);


	
	var server_owner_name = document.getElementById("server_owner_name").value;
	var server_ip = document.getElementById("server_ip").value;
	var tag_name = document.getElementById("tag_name").value;
	var entity_code = document.getElementById("entity_code").value;


	if (lhsIsNull(entity_code) && lhsIsNull(server_owner_name) && lhsIsNull(server_ip) && lhsIsNull(tag_name)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
			var actionUrl = "./serverDetailFilter?configuration_type="+config_type+"&server_owner_name=" + server_owner_name + "&server_ip=" + server_ip + "&tag_name=" + tag_name + "&entity_code=" + entity_code ;
			ajaxPostDataArray(actionUrl, fdata, serverDetailFilterResponse);
	}
}
function serverDetailFilterResponse(response){
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();
}


//////////////////// sushma////////////////////////

function openActionDiv(id) {
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


function editAppRecord(id) {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var app_code = split_id[2];
		var View = split_id[3];
		var url = "./appDetailEntry?app_code=" + app_code + "&View=" + View;
		window.location = url;
	}

}

function editAppList(id) {
	var app_code = id;
	var View = document.getElementById('viewCard').value;
	var url = "./appDetailEntry?app_code=" + app_code + "&View=" + View;
	window.location = url;

}


function viewAppDetail() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var app_code = split_id[2];
		var url = "./AppViewDetails?app_code=" + app_code;
		ajaxPostUrl(url, viewEntityTable);
	}
}



function viewAppList(id) {
	var app_code = id;
	var url = "./AppViewDetails?app_code=" + app_code;
	ajaxPostUrl(url, viewEntityTable);

}
function viewEntityTable(response) {
	if (!lhsIsNull(response)) {
		$("#ModelTableBody").html("");
		$("#ModelTableBody").html(response);
		$("#myModal").modal({
			show: true
		});

	} else {
		$("#myModal").modal({
			show: true
		});
	}
}




function getApp_serverIP() {
	var serverip = document.getElementById('server_ip').value;
	document.getElementById("app_server_ip").disabled = false;

	var split_id = serverip.split("-");
	var server_ip = split_id[0]

	if (!lhsIsNull(server_ip)) {

		$('#app_server_ip').val("");
		$('#app_server_ip').append('<option value="">--Select--</option>');
		var url = "setAppServerIP?server_ip=" + server_ip;
		ajaxPostUrl(url, appServerResponse);

	}

}

function appServerResponse(response) {
	var app_serverip = $('#app_server_ip');
	app_serverip.empty();
	if (!lhsIsNull(response)) {
		app_serverip.append('<option value="">--Select--</option>');
		$.each(response, function(k, v) {
			app_serverip.append('<option value=' + k + '>' + v + '</option>');
			
		});
	} else {
		app_serverip.append('<option value="">--Select--</option>');
	}
}

function getApplicationType() {
	var application_type = document.getElementById('application_type').value;



	if (application_type === 'apptype_02') {
		document.getElementById('jar_name').style.display = 'flex';
		document.getElementById('jar_parameter').style.display = 'flex';
		document.getElementById('local_url').style.display = 'none';
		document.getElementById('app_url').style.display = 'none';
		document.getElementById('app_name1').style.display = 'none';

	} else if (application_type === 'apptype_01') {
		document.getElementById('jar_name').style.display = 'none';
		document.getElementById('jar_parameter').style.display = 'none';
		document.getElementById('local_url').style.display = 'flex';
		document.getElementById('app_url').style.display = 'flex';
		document.getElementById('app_name1').style.display = 'flex';

	} else if (application_type === 'apptype_03') {
		document.getElementById('jar_name').style.display = 'none';
		document.getElementById('jar_parameter').style.display = 'none';
		document.getElementById('app_name1').style.display = 'flex';
		document.getElementById('local_url').style.display = 'flex';
		document.getElementById('app_url').style.display = 'flex';
	}

}

function getApplicationServerName() {
	var app_servername = document.getElementById('app_server_ip').value;
	document.getElementById("app_server_name").disabled = false;
//	if(app_name.includes('#')){
//	var split_id =app_name.split("#");
//	var app_servername=split_id[0]
//	var app_ip=split_id[1]
	var Port = document.getElementById('port1').value;
	document.getElementById("ip1").value = app_servername+":"+Port+"/";
	
	if (!lhsIsNull(app_servername)) {

		$('#app_server_name').val("");
		$('#app_server_name').append('<option value="">--Select--</option>');
		var url = "setAppName?app_servername=" + app_servername;
		ajaxPostUrl(url, appResponse);

	//}
	}
}

function appResponse(response) {
	var menuName = $('#app_server_name');
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



function onLoadAppType(){
	
	var action =$("#action").val();
	
	var app_type =$("#application_type").val();

     if(action === 'save'){
    	    document.getElementById('jar_name').style.display = 'none';
    		document.getElementById('jar_parameter').style.display = 'none';
    		document.getElementById('app_name1').style.display = 'none';
    		document.getElementById('local_url').style.display = 'none';
    		document.getElementById('app_url').style.display = 'none';
    		document.getElementById("app_server_name").disabled = true;	
    		document.getElementById("app_server_ip").disabled = true;

     } else if(action === 'edit' && app_type === 'apptype_02'){
    	
    	document.getElementById('jar_name').style.display = 'flex';
 		document.getElementById('jar_parameter').style.display = 'flex';
 		document.getElementById("app_server_name").disabled = false;	 
		document.getElementById("app_server_ip").disabled = false;
		document.getElementById('app_name1').style.display = 'none';
		document.getElementById('local_url').style.display = 'none';
		document.getElementById('app_url').style.display = 'none';

     }else if(action === 'edit' && app_type === 'apptype_03'){
    	 
    	document.getElementById('jar_name').style.display = 'none';
  		document.getElementById('jar_parameter').style.display = 'none';
  		document.getElementById("app_server_name").disabled = false;	 
 		document.getElementById("app_server_ip").disabled = false;
 		document.getElementById('app_name1').style.display = 'flex';
 		document.getElementById('local_url').style.display = 'flex';
 		document.getElementById('app_url').style.display = 'flex';
 		
     }else if(action === 'edit' && app_type === 'apptype_01'){
    	 
    	document.getElementById('jar_name').style.display = 'none';
   		document.getElementById('jar_parameter').style.display = 'none';
   		document.getElementById("app_server_name").disabled = false;	 
  		document.getElementById("app_server_ip").disabled = false;
  		document.getElementById('app_name1').style.display = 'flex';
  		document.getElementById('local_url').style.display = 'flex';
  		document.getElementById('app_url').style.display = 'flex';	
    	 
     }
	
  
}


function isValidUrl(field) {
	// var regexp =
	// /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
	var pattern = new RegExp('^(\\/\\/)?' + // protocol
		'((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
		'((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
		'(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
		'(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
		'(\\#[-a-z\\d_]*)?$', 'i'); //
	if (pattern.test(field.value)) {
		return true;
	} else {
		openErrorModal("Invalid Application Url");
		field.value = "";
		return false;
	}
}

function validateAppEntryForm() {
	var status;

	var entity_code = $("#entity_code").val();
	var application_type = $("#application_type").val();
	var server_ip = $("#server_ip").val();
	var app_server_ip = $("#app_server_ip").val();
	var app_server_name = $("#app_server_name").val();
	var remark1 = $("#remark1").val();
	var remark2 = $("#remark2").val();
	var local_app_url = $("#local_app_url").val();
	var public_app_url = $("#public_app_url").val();
	var project_code = $("#project_code").val();
	
	
 if (lhsIsNull(entity_code)) {
		openErrorModal("Please select Entity Name.");
		status = false;
	}  else if (lhsIsNull(project_code)) {
		openErrorModal("Please Select Project Name");
		status = false;
	}
 else if (lhsIsNull(application_type)) {
		openErrorModal("Please Select Application Type");
		status = false;
	} else if (lhsIsNull(server_ip)) {
		openErrorModal("Please Select Server  IP");
		status = false;
		
		} else if (lhsIsNull(remark1)) {
		openErrorModal("Please Enter Remark1");
		status = false;
	} else if ($("#remark1").val().length > 4000) {
		openErrorModal("Remark Exceed Maximum Limit Of Characters.");
		status = false;
	} 
//	else if (lhsIsNull(remark2)) {
//		openErrorModal("Please Enter Remark2");
//		status = false;
//	} else if ($("#remark2").val().length > 4000) {
//		openErrorModal("Remark Exceed Maximum Limit Of Characters.");
//		status = false;
//	} 
		else {
		status = true;
	}
	return status;
}


function saveAppDetailEntry() {

	if (validateAppEntryForm()) {

		ajaxSubmitPostData("./saveAppDetail", "appEntryForm",
			saveAppResponse);
	}

}

function UpdateAppDetailEntry(){
	 if (validateAppEntryForm()) {
		 ajaxSubmitPostData("./UpdateAppDetail", "appEntryForm", savelist);
		      }
}


function savelist(response) {
	var mode = document.getElementById('mode').value;

	if (response === "success") {
		var action = $('#action').val();

		if (action == "edit") {
			openSuccessModal("Data Updated successfully!",
				"OpenWithView_Mode('" + mode + "');");
		}

	}
}






function saveAppResponse(response) {
	var mode = document.getElementById('mode').value;


	if (response === "success") {
		var action = $('#action').val();

		if (action == "save") {
			openSuccessModal("Data saved successfully!",
				"window.location.href='./appDetailDashboard';");
		}

	}
}



function OpenWithView_Mode(mode) {

	var url = "./appDetailDashboard?mode=" + mode;
	window.location = url;

}

function applicationMode(mode) {

	document.getElementById('mode').value = mode;

	if (mode == 'Card') {
		document.getElementById('bulk_download_btn').style.display = 'none';

	} else if (mode == 'Text') {
		document.getElementById('bulk_download_btn').style.display = 'block';
	}
	var formdata = $('#appDetails').serializeArray();
	var fdata = JSON.stringify(formdata);
	var actionUrl = "./appDetailDashboard?mode=" + mode;
	ajaxPostDataArray(actionUrl, fdata, ModeResponse);

}// end function

function ModeResponse(response) {
	// console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);

}//


function loadData() {
	var mode = document.getElementById("mode").value;
	if (mode === 'Card') {
		document.getElementById("Card").checked = true;
		document.getElementById("Text").checked = false;

		document.getElementById('bulk_download_btn').style.display = 'none';

	} else if (mode === 'Text') {
		document.getElementById("Text").checked = true;
		document.getElementById("Card").checked = false;

		document.getElementById('bulk_download_btn').style.display = 'none';
	}

}


function searchAppFilter() {
	var mode = document.getElementById("mode").value;
	var formdata = $('#appDetails').serializeArray();
	var fdata = JSON.stringify(formdata);


	var entity_code = document.getElementById("entity_code").value;
	var application_type = document.getElementById("application_type").value;
	var project_code = document.getElementById("project_code").value;
	var search_entity = document.getElementById("search_entity").value;


	if (lhsIsNull(entity_code) && lhsIsNull(application_type) && lhsIsNull(project_code) && lhsIsNull(search_entity)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		if (mode == 'Text') {
			var actionUrl = "./appTextFilter?entity_code=" + entity_code + "&application_type=" + application_type + "&project_code=" + project_code + "&search_entity=" + search_entity + "&mode=" + mode;
			ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
		} else {
			var actionUrl = "./appCardFilter?entity_code=" + entity_code + "&application_type=" + application_type + "&project_code=" + project_code + "&search_entity=" + search_entity + "&mode=" + mode;
			ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
		}

	}
}

function searchFilterWorkResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);// ("response");
	loadDataUsingPaginatorGrid();

}// end f



function refreshApp() {
	var mode = document.getElementById("mode").value;
	window.location.href = "./appDetailDashboard?mode=" + mode;
}

function cancelAppdetail() {
	var mode = document.getElementById("mode").value;
	window.location.href = "./appDetailDashboard?mode=" + mode;


}

function deleteRecord(funName) {
	if (!lhsIsNull(checkedId)) {
		window[funName](checkedId);
	}
}

function deleteAppList(id) {
	openConfirmModal("Do you want to delete ?", "deleteSelected('" + id
		+ "')");
}


function deleteDocdetails(id) {

	openConfirmModal("Do you want to delete ?", "deleteSelectedRow('" + id
		+ "')");
}

function deleteSelected(id) {
	var app_code = id;
	var url = "./deleteAppDocument?app_code=" + app_code;
	ajaxPostUrl(url, deleteUserResponse);
}

function deleteSelectedRow(id) {

	closeConfirmModal();
	var split_id = id.split("~");
	var app_code = split_id[2];

	var url = "./deleteAppDocument?app_code=" + app_code;
	ajaxPostUrl(url, deleteUserResponse);
	// }
}

function deleteUserResponse(response) {

	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "refreshApp()");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function getappname(){
	var ip1 = $('#ip1').val();
	var app_name = $('#app_name').val();
	var action = $('#action').val();
	var split_id =ip1.split("/");
	var ip=split_id[0]
	 
if(action === 'save'){

document.getElementById("ip1").value =ip1+app_name ;
document.getElementById("public_app_url").value =app_name ;
}else if(action ==='edit'){
	document.getElementById("ip1").value =ip+"/"+app_name ;
}
}