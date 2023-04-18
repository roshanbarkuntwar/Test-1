function saveWishlistWork() {
	if (validateForm()) {
		ajaxSubmitPostData("saveWishlistPendingWork", "wishlistPendingForm",
				saveWishlistWorkResponse);
	}
}

function saveWishlistWorkResponse(response) {
	if (response === "success") {
		var action = $('#action').val();
		var work_type = $('input[name="workTypeBtn"]:checked').val();

		if (action == "save") {
			openSuccessModal("Data saved successfully !", "openWorkForm('"+ work_type + "');");
		} else if (action == "edit") {
			openSuccessModal("Data updated successfully !", "openWorkForm('"
					+ work_type + "');");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function validateForm() {

	var status1;
	var work_type = $('input[name="workTypeBtn"]:checked').val();
	var work_nature = $('input[name="workNatureBtn"]:checked').val();
	var project_code = $("#project_code").val();
	var completion_date = $("#completion_date").val();
	var work_description = $("#work_description").val();
	var action_name = $('#action').val();

//	if (lhsIsNull(work_type)) {
//		openErrorModal("Please select Work Type.");
//		status1 = false;
//	} else if (lhsIsNull(work_nature)) {
//		openErrorModal("Please select Work Nature.");
//		status1 = false;
//	} else if (lhsIsNull(project_code) && !(work_type == 'W')
//			&& !action_name === 'edit') {
//		openErrorModal("Please select On which Application");
//		status1 = false;
//	}
//
//	else if (lhsIsNull(completion_date) && !(work_type == 'W')) {
//		openErrorModal("Please select completion_date");
//		status1 = false;
	//} else 
		if (lhsIsNull(work_description)) {
		openErrorModal("Please enter Detailed Work Description.");
		status1 = false;
	} else if(work_description.length >= 4000) {
		openErrorModal("Detailed Work Description Value too  Large");
		status = false;
     }else {

		$('#work_type').val(work_type)
		$('#work_nature').val(work_nature)
		status1 = true;
	}

	return status1;
}// End function

var checkedId = '';
function openActionDiv(id) {
	var split_id = id.split('~');
	var status = split_id[4];
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

	if (status === 'N') {
		 document.getElementById('Btnmark').style.display='inline-flex';

	} else if (status === 'D') {
		 document.getElementById('Btnmark').style.display='none';


	}
}



function editWork() {

	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var work_code = split_id[2];
		var url = "workEntry?workCode=" + work_code;
		window.location = url;
	}

}

function deleteWork() {
	openConfirmModal("Do you want to delete ?", "deleteSelectedWork()");
}

function deleteSelectedWork() {
	closeConfirmModal();
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var work_code = split_id[2];

		var url = "./deleteWork?workCode=" + work_code;
		ajaxPostUrl(url, deleteWorkResponse);
	}

}

function deleteWorkResponse(response) {
	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "window.location.reload();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function viewWork() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var work_code = split_id[2];
		var Action = split_id[3];
		var status= split_id[4]
		var url = "workEntry?workCode=" + work_code + "&Action=" + Action+"&status="+status;
		window.location = url;
	}
}

function markdone() {

	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var work_code = split_id[2];
		var mark = split_id[5];
		var url = "workEntry?workCode=" + work_code + "&mark=" + mark;
		window.location = url;
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

function openWorkForm(workType) {
//	$("#worktypeget").val()=workType;
	var url = "./workDetail?workType=" + workType;
	window.location = url;
	//searchwishlistFilter();
}

function searchwishlistFilter() {
	var worktype = $('#worktypeget').val(); 
	var work_nature = $("#work_nature").val();
	var work_priority = $("#work_priority").val();
	var project_code = $("#project_code").val();
	var user_code = $("#user_code").val();
	var status = $("#status").val();
	var completion_date = $("#completion_date").val();
	var formdata = $('#workDetails').serializeArray();
	var fdata = JSON.stringify(formdata);

	if (lhsIsNull(work_nature) && lhsIsNull(work_priority)
			&& lhsIsNull(project_code) && lhsIsNull(user_code)
			&& lhsIsNull(completion_date) && lhsIsNull(status) && lhsIsNull(worktype) ) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		var actionUrl = "./workDetailfilter?work_type="+worktype+"&work_nature="+work_nature+"&work_priority="+work_priority+"&project_code="+project_code+"&status="+status;
		ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
	}

	console.log("Filter:" + fdata);

}

function searchFilterWorkResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function

function resetwishFilter() {
	$("#workDetails").find("input[type=text], textarea, select").val("");
	refreshWish();

}

function refreshWish() {
	var worktype = $('#worktypeget').val();
	var url = "./workDetail?workType=" + worktype;
	window.location = url;
}

function Back() {
	var work_type = $('input[name="workTypeBtn"]:checked').val();
		var url = "./workDetail?workType="+work_type;
		window.location = url;

}

function onload() {
	
	var Action = $('#Action').val();
	var action = $('#action').val();
	var mark = $('#mark').val();
	var status = $('#status').val();

	if (Action === 'view') {
		document.getElementById('user_code').disabled = true;
		document.getElementById('work_description').readOnly = true;
		document.getElementById('completion_date').readOnly = true;
		document.getElementById('project_code').disabled = true;
		document.getElementById('work_priority').disabled = true;
		document.getElementById('workTypeBtn1').disabled = true;
		document.getElementById('workTypeBtn2').disabled = true;
		document.getElementById('workNatureBtn1').disabled = true;
		document.getElementById('workNatureBtn2').disabled = true;
		document.getElementById('remark').readOnly = true;
		document.getElementById('btn_save').style.display = 'none';
		document.getElementById('btn_done').style.display = 'none';
		document.getElementById('details').style.display = 'none';
		document.getElementById('external_link').disabled = true;;
            
		if(status === 'N'){
		document.getElementById('remark-done').style.display = 'none';	
	
		}
              
	} else if (action === 'save') {
		document.getElementById('btn_done').style.display = 'none';
		document.getElementById('details').style.display = 'none';
		document.getElementById('remark-done').style.display = 'none';


	} else if (mark === 'mark') {
		document.getElementById('btn_done').style.display = 'inline-flex';
		document.getElementById('btn_save').style.display = 'none';
		document.getElementById('user_code').disabled = true;
		document.getElementById('work').readOnly = true;
		document.getElementById('completion_date').readOnly = true;
		document.getElementById('project_code').disabled = true;
		document.getElementById('work_priority').disabled = true;
		document.getElementById('workTypeBtn1').disabled = true;
		document.getElementById('workTypeBtn2').disabled = true;
		document.getElementById('workNatureBtn1').disabled = true;
		document.getElementById('workNatureBtn2').disabled = true;
		document.getElementById('details1').style.display = 'none';
		document.getElementById('link').style.display = 'none';


	} else if (action === 'edit') {
			document.getElementById('btn_done').style.display = 'none';
			document.getElementById('remark-done').style.display = 'none';
			document.getElementById('details').style.display = 'none';

		

	}else if(status === 'D'){
		document.getElementById('remark-done').style.display = 'inline-flex';

	}else if(status === 'N'){
		document.getElementById('remark-done').style.display = 'none';	
	}

}

function downloadbutton() {
	document.getElementById('bulk_download_btn').style.display = 'none';
}

function validate() {
	var r = document.workDetail;
	var status;
	var remark = $("#remark").val();
	var work_code = $("#work_code").val();

	if (remark == '') {
		openErrorModal("Please enter Remark");
		return false;
	} else {

		status = true;

	}

	return status;
}// End function

function getDone() {
	var remark = $("#remark").val(); 
	var work_code = $("#work_code").val();

	if (lhsIsNull(remark)) {
		openErrorModal("Please enter Remark.");

	} else if (work_code != null) {
		if (!lhsIsNull(remark)) {
			openConfirmModal("Do you want to Mark Status As Done?",
					"confirmSave();");
		} else {

		}

	}

}

function confirmSave() {
	var remark = $("#remark").val();
	var work_code = $("#work_code").val();
	var url = "SaveViewForm?workCode=" + work_code + "&remark=" + remark;
	ajaxPostUrl(url, saveWishlist);
}

function saveWishlist(response) {
	var work_type = $('input[name="workTypeBtn"]:checked').val();
	if (response === "success") {
		openSuccessModal("Data saved successfully !", "openWorkForm('"+ work_type + "');");
	} else {
		openErrorModal("Some Error Occured..!", "openWorkForm('" + work_type
				+ "');");
	}
}// End function

function myFunction() {

	var work_description = $("#work_description").val();
	var work_code = $("#work_code").val();
	var url = "/workEntry?workCode=" + work_code + "&work_description="
			+ work_description;
	// ajaxPostUrl(url, viewWorkResponse);
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



function OpenExternalLink(id) {
	var split_id = id.split('~');
	var golbal_path = document.getElementById("url~" + split_id[1]).value; 
	var url;
	if( golbal_path.indexOf("http") == 0 ) {
	    url=golbal_path;
	} else {
		 url='https://'+golbal_path;
	}
	

	if(!lhsIsNull(url)){
	window.open(url);
	}
}


function getclearvalue(){
	// $('#user_code').multiselect();
	 $("#user_code").multiselect("clearSelection");
	 //$("#user_code").multiselect( 'refresh' );
	//includeSelectAllOption : true,	
}
