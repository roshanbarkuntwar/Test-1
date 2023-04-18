 var docMode = '';
function saveDocument() {
	checkboxtrueOrfalse();	

    var username = $('#code').val(); 
	docMode = $('#doc_mode').val();
	if (!lhsIsNull(docMode) && docMode === 'Q') {
		var doc_data = $(".Editor-editor").html(); 
		$("#doc_data").val(doc_data);
	}

	if (validateDocEntryForm(docMode)) {
		ajaxSubmitPostData("./saveDocDetail", "docEntryForm",
				saveDocumnetResponse);
	}
}




function saveDocumentQ(){
	 var username = $('#code').val(); 
		docMode = $('#doc_mode').val();
		if (!lhsIsNull(docMode) && docMode === 'Q') {
			var doc_data = $(".Editor-editor").html(); 
			$("#doc_data").val(doc_data);
		}

		if (validateDocEntryForm(docMode)) {
			ajaxSubmitPostData("./saveDocDetail", "docEntryForm",
					saveDocumnetResponse);
		}
	}	




function saveDocumnetResponse(response) {
	if (response === "success"){
		var redirectUrl = '';
		if (!lhsIsNull(docMode) && docMode === 'Q') {
			openSuccessModal(" Saved Successfully !", "window.location.href='./quickDocDetail';");

		} else {
			redirectUrl = "window.location.href='./docEntry';";
		}
		var action = $('#action').val();
		 
		if (action === "save") {
			openSuccessModal(" Saved Successfully !", "window.location.href='./docDetail';");
		} else if (action === "edit"){ 
			openSuccessModal("Updated Successfully !", "window.location.href='./docDetail';");
		}

	} else {
		openErrorModal("Some Error Occured.!", "");
	}
}// End function


function validateDocEntryForm(docMode) {
	var r=  document.docEntryForm;
	var status;
	var doc_type = $('#doc_type_code').val();
	var doc_name = $('#doc_name').val();
	var doc_data = '';
	var doc_file = $('#doc_file').val();
	var remark = $('#remark').val();
	var action_name = $('#action').val();
	var doc_location1 = $('#doc_location1').val();
	var doc_location2 = $('#doc_location2').val();
	var doc_location3 = $('#doc_location3').val();
	if (!lhsIsNull(docMode) && docMode === 'Q') {
		doc_data = $(".Editor-editor").html();
	} else {
		/*
		 * doc_file =$('#doc_file').val(); 
		 */

	}

	
	if (lhsIsNull(doc_type)) {
		openErrorModal("Please select Document Type.");
		status = false;
	} else if (lhsIsNull(doc_name)) {
		openErrorModal("Please Enter Document Name.");
		status = false;
	} else if (!lhsIsNull(docMode) && docMode === 'O' && lhsIsNull(doc_file) && action_name==='save') {
		openErrorModal("Please select Document.");
		status = false;
    }else if((doc_location1 =='F' && doc_location3 =='F' && doc_location2 =='F') && action_name==='save') {
		openErrorModal("Please select Upload To Location ");
        status = false; 
    }else if( doc_location2 =='T') {
		openErrorModal("It Is Under Implementation, Please Select Other Option");
		status = false;
    }
    else if (!lhsIsNull(docMode) && docMode === 'Q' && lhsIsNull(doc_data)) {
		openErrorModal("Please Enter Document Data.");
		status = false;
	}else if(doc_data.length >= 4000) {
		openErrorModal("Document Data Value too  Large");
		status = false;
     }else if( docMode === 'O' &&  remark.length >= 4000 ) {
    			openErrorModal("Remark Value to Large");
    			status = false;
    	     }else {
		status = true;
	}

	return status;
}// End function


function deleteDoc(id) {
	openConfirmModal("Do you want to delete ?", "deleteSelectedRow('" + id
			+ "')");
}


function deleteSelectedRow(id) {
	// closeConfirmModal();
	var split_id = id.split('~');
	var doc_code = split_id[2];
	var doc_mode = split_id[3];
	var url = "./deleteDocument?doc_code=" + doc_code;
	ajaxPostUrlWithParam(url, deleteDocResponse, doc_mode);
}

function deleteDocResponse(response, doc_mode) {
	if (response === "success") {
		var redirectUrl = '';
		if (!lhsIsNull(doc_mode) && doc_mode === 'Q') {
			redirectUrl = "window.location.href='./quickDocDetail';";
		} else {
			redirectUrl = "window.location.href='./docDetail';";
		}
		openSuccessModal(globalDeleteMessage, redirectUrl);
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function



 function downloadQuickDoc(id){
	var split_id = id.split('~'); 
	var doc_data=split_id[2];
	const strippedString = doc_data.replace(/(<([^>]+)>)/gi, "");
	var op = strippedString.replace(/&nbsp;/g, '');
	//var doc_data = document.getElementById("'doc_data~" + split_id[1]).innerHTML; 
	var doc_name = document.getElementById("doc_name~" + split_id[1]).innerHTML; 
	var element = document.createElement('a');
	element.setAttribute('href', 'data:text/plain;charset=utf-8,'
			+ encodeURIComponent(op));
	element.setAttribute('download', doc_name + '.txt');
	element.style.display = 'none';
	document.body.appendChild(element);
	element.click();
	document.body.removeChild(element);
}// End function


 
function  searchQuickFilter(){
	
var formdata = $('#QuickdocDetails').serializeArray();
var fdata = JSON.stringify(formdata);
var doc_name = $('#doc_name').val();

//if (lhsIsNull(doc_name)) {
//	openErrorModal("Please select any filter to see the records !");
//} else {
	var actionUrl = "quickDocDetail";
	ajaxPostDataArray(actionUrl, fdata, searchFilterQuickResponse);

}
 
 

function searchFilterQuickResponse(response) { 
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function

 
function searchDocFilter() {
	var formdata = $('#docDetails').serializeArray();
	var fdata = JSON.stringify(formdata);
	var doc_code_type = $('#doc_code_type').val();
	var doc_name = $('#doc_name').val();
	var project_code = $('#project_code').val();
	var user_code = $('#user_code').val();
	  document.getElementById("table").style.display = "none";

	if (lhsIsNull(doc_code_type) && lhsIsNull(doc_name)
			&& lhsIsNull(project_code) && lhsIsNull(user_code)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		var actionUrl = "docDetailFilter?doc_code_type="+doc_code_type+"&doc_name="+doc_name+"&project_code="+project_code+"&user_code="+user_code;
		ajaxPostDataArray(actionUrl, fdata, searchFilterDocResponse);

	}

}

function searchFilterDocResponse(response) { 
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function

function Back(id) {
	var doc_code = id;
	var url = "./docDetail?doc_code=" + doc_code;
	window.location.href = url;

}

function openActionDiv(id) {
	var split_id = id.split('~');
	var User_code = split_id[3];
	var u_code = $('#u_code').val();
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
	
	if(User_code !== u_code ){
    	$("#editBtn").css('display', 'none');
	}else{
		$("#editBtn").css('display', 'inline-flex');	
	}	
	
	if(User_code !== u_code ){
    	$("#deleteBtn").css('display', 'none');
	}else{
		$("#deleteBtn").css('display', 'inline-flex');	
	}	
}


function editRecord(funName) {
	if (!lhsIsNull(checkedId)) {
		window[funName](checkedId);
	}
}

function editDocCode(id) {
	
	var split_id = id.split('~');
	var doc_code = split_id[2];
	var User_code = split_id[3];
	var golbal_path = document.getElementById("golbal_path-" + split_id[1]).value;
	var local_path = document.getElementById("local_path-" + split_id[1]).value;
    var database_path = document.getElementById("database_path-" + split_id[1]).value;
//    var u_code = $('#u_code').val();
//    if(User_code !== u_code ){
//    	$("#editBtn").css('display', 'none');
//		//openErrorModal("You Have No Rights to Edit records !");
//    }else{
    	var url = "docEntry?doc_code="+doc_code+"&local_path="+local_path+"&database_path="+database_path;
    	window.location = url; 
   // }
	
}


function deleteRecord(funName){
	if (!lhsIsNull(checkedId)) {
		window[funName](checkedId);
	}
}


function deleteDocdetails(id) {

	openConfirmModal("Do you want to delete ?", "deleteSelectedRow('" + id
			+ "')");
}

function deleteSelectedRow(id){

	closeConfirmModal();
	var split_id = id.split("~");
	var doc_code = split_id[2];
	var user_code = split_id[3];
//	 var u_code = $('#u_code').val();
//	    if(user_code !== u_code ){
//			openErrorModal("You Have No Rights to Edit records !");
//	    }else{
	  var url = "./deleteDocument?doc_code=" + doc_code;
	  ajaxPostUrl(url, deleteUserResponse);
   //}
}

function deleteUserResponse(response){

	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "window.location.reload();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function viewDocTran() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var doc_code = split_id[2];
		var url = "./viewDocTran?docCode=" + doc_code;
		ajaxPostUrl(url, viewDocTranResponse);
	}
}

function viewDocTranResponse(response) {
	if (!lhsIsNull(response)) {
		$("#doctranModelTableBody").html("");
		$("#doctranModelTableBody").html(response);
		$("#detailss-modal").modal({
			show : true
		});
	} else {
		$("#detailss-modal").modal({
			show : true
		});
	}
}

function download(id) {
	var count1 = id.split("-");
	var count=count1[0];
	var code =count1[1];

	var golbal_path = document.getElementById("golbal_path-" + count[0]).value;
	var local_path = document.getElementById("local_path-" + count[0]).value;
	var database_path = document.getElementById("database_path-"+ count[0]).value;
	var localfile = document.getElementById("localfile-"+ count[0]).value;

		
	if (local_path === "T") {
		localfileDownload(localfile);
	} 
	else if (database_path === "T" ) {
		databasefileDownload(code);
		
	}
	else if (database_path === "T" && local_path==="T"){
		localfileDownload(localfile);
		databasefileDownload(code);

		//localfileDownload(code);
	}
}

function localfileDownload(localfile) {
var localpath=localfile;
	if (!lhsIsNull(localpath)) {
		var folderpath = localpath.substr(0, localpath.lastIndexOf('\\'));
		var path = localpath.replace(/\\$/, '').split('\\').pop();
		var filename = path;
		url = 'downloadSingleFile?folderpath='
				+ encodeURIComponent(folderpath) + '&filename='
				+ encodeURIComponent(filename);
		window.location.href = url;
	}

}

function databasefileDownload(code) {
	var doc_code=code;
	var doc_attach_name = document.getElementById("doc_attach_name").value;
	var cp = document.getElementById("globalcontextpath").value;
	if (!lhsIsNull(doc_attach_name)) {
		
		var url =cp+"downloadDatabaseFile?doc_code="+ doc_code;
				window.location = url;
	}
}


function OpenLink(id) {
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



function refreshQDoc(){
	$("#QuickdocDetails").find("input[type=text], textarea, select").val("");
	refreshQuickDocument();	
}

function refreshQuickDocument() {
	var url = "./quickDocDetail";
	window.location = url;
}

function refreshDoc(){
	$("#docDetails").find("input[type=text], textarea, select").val("");
	refreshDocument();
}

function refreshDocument() {
	var url = "./docDetail";
	window.location = url;
}


function checkboxtrueOrfalse(){
var chkPassport = document.getElementById("doc_location1");
if (chkPassport.checked) {
	document.getElementById("doc_location1").value='T';
} else {
	document.getElementById("doc_location1").value='F';

}


var chkPassport = document.getElementById("doc_location3");
if (chkPassport.checked) {
	document.getElementById("doc_location3").value='T';
} else {
	document.getElementById("doc_location3").value='F';

}


}


function onload(){
document.getElementById('bulk_download_btn').style.display = 'none';
document.getElementById('liTop_1').style.display = 'none';
document.getElementById('page').style.display = 'none';
document.getElementById('first').style.display = 'none';
document.getElementById('last').style.display = 'none';
document.getElementById('previous').style.display = 'none';
document.getElementById('next').style.display = 'none';


	
}


function getHideDownload(){
	document.getElementById('bulk_download_btn').style.display = 'none';
	
}
