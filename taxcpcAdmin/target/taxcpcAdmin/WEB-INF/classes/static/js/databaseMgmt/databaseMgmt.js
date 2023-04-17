function saveLhssysDbDetail(){
	//var project_code = document.getElementById('project_code').value
		
// alert("project_code.."+project_code);
	if (validateLhssysDbDetailEntryForm()) {
		ajaxSubmitPostData("saveDatabaseDetail","databaseEntryForm", saveLhssysDbDetailResponse);
} 	 
}

function saveLhssysDbDetailResponse(response) {
    if(response === "success") {
        openSuccessModal("Data saved successfully !" ,"backToDatabaseBrowser();");
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function

function backToDatabaseBrowser() {
	window.location.href='./databaseDetails';
}



function validateLhssysDbDetailEntryForm(){
	
	var status;
    var type_of_db = $('#type_of_db').val();
   var db_ip = $('#db_ip').val();
    var tag_name = $('#tag_name').val();
    var db_port = $('#db_port').val();
    var db_sid = $('#db_sid').val();
   
   // alert("action_name"+action_name);
    
    if(lhsIsNull(type_of_db)) {
    	openErrorModal("Please Select Type of Database.");
        status = false;
    } else if(lhsIsNull(db_ip)) {
    	openErrorModal("Please Enter Valid IP Address.");
        status = false;
    }  
    else if(lhsIsNull(tag_name)) {
    	openErrorModal("Please Enter Tag Name.");
        status = false;
    } else if (lhsIsNull(db_port)) {
		openErrorModal("Please Enter Database Port.");
		status = false;
	} else if (lhsIsNull(db_sid)) {
		openErrorModal("Please Enter Database SID.");
		status = false;
	} 
    else {	
        status = true;
    }
    
    return status;
}//End function

function editDatabaseDetails() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		editLhssysDbDetails(checkedId);
	}
} 
function editLhssysDbDetails(id){
	//alert("id.."+id);
	var db_code = id.split("-");
	var url = "./editdatabaseEntry?db_code=" + db_code[2];
	//alert("url.."+url);
	window.location =url;
	
	//window.location.href='./editprojectEntry';
}


var checkedId = '';
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
    }else{
    	checkedId = '';
     	$("#actiondiv").css('display', 'none');
    }
}

function editDatabaseTable() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		editdatabaseTableEntry(checkedId);
	}
} 
function editdatabaseTableEntry(id){
	//alert("id.."+id);
	var object_code = id.split("-");
	var url = "./editdatabaseTableEntry?object_code=" +object_code[2];
	//alert("url.."+url);
	window.location =url;
	
	//window.location.href='./editprojectEntry';
}

function saveDatabaseTableBrowser(){
	//var project_code = document.getElementById('project_code').value
		
// alert("project_code.."+project_code);
	if (validateLhssysMainTableEntryForm()) {
		ajaxSubmitPostData("saveDatabaseMainTableDetail","databaseTableEntryForm", saveLhssysMainTableResponse);
} 	 
}

function saveLhssysMainTableResponse(response) {
    if(response === "success") {
        openSuccessModal("Data saved successfully !" ,"backToDatabaseTableBrowser();");
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function

function backToDatabaseTableBrowser() {
	window.location.href='./databaseMainTable';
}

function validateLhssysMainTableEntryForm(){
	
	var status;
    var object_type = $('#object_type').val();
    var table_or_view_name = $('#table_or_view_name').val();

   
   // alert("action_name"+action_name);
    
    if(lhsIsNull(object_type)) {
    	openErrorModal("Please Select Object Type.");
        status = false;
    } else if(lhsIsNull(table_or_view_name)) {
    	openErrorModal("Please Enter Table or View Name.");
        status = false;
    }   
    else {	
        status = true;
    }
    
    return status;
}//End function

function searchDatabaseFilter()
{
	var formdata = $('#databaseDetails').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	
	var type_of_db = document.getElementById("type_of_db").value;

	var db_ip = document.getElementById("db_ip").value;
	//alert("project_status" +status);
	
	if (lhsIsNull(type_of_db) && lhsIsNull(db_ip) ) {
        openErrorModal("Please select any filter to see the records !");
	}else{

	var actionUrl = "./getSearchDatabaseDetail?type_of_db=" +type_of_db+ "&db_ip=" +db_ip;
	
	ajaxPostDataArray(actionUrl, fdata, searchFilterDocResponse);
	}
}// end function


function searchFilterDocResponse(response) {
	 console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function

function resetDatabaseFilter(){
	$("#databaseDetails").find("input[type=text], textarea, select").val("");
	refreshDatabaseDetail();
	window.location.href='./databaseDetails';
}

function refreshDatabaseDetail() {
	var url = "./databaseDetails";
	window.location = url;
}

function searchDatabaseTableFilter()
{
	var formdata = $('#databaseMainTable').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	
	var table_or_view_name = document.getElementById("table_or_view_name").value;
	var project_code = document.getElementById("project_code").value;
	var object_type = document.getElementById("object_type").value;
	//alert("project_status" +status);
	
	if (lhsIsNull(table_or_view_name) && lhsIsNull(object_type)  && lhsIsNull(project_code)) {
        openErrorModal("Please select any filter to see the records !");
	}else{

	var actionUrl = "./getSearchDatabaseTableFilter?table_or_view_name=" +table_or_view_name+ "&object_type=" +object_type+ "&project_code=" +project_code;
	
	ajaxPostDataArray(actionUrl, fdata, searchDatabaseMainResponse);
	}
}// end function


function searchDatabaseMainResponse(response) {
	 //console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
    loadDataUsingPaginatorGrid();
	

}// end function

function resetDatabaseTableFilter(){
	$("#databaseMainTable").find("input[type=text], textarea, select").val("");
	refreshDatabaseMainTable();
	window.location.href='./databaseMainTable';
}

function refreshDatabaseMainTable() {
	var url = "./databaseMainTable";
	window.location = url;
}


function searchDatabaseUserFilter()
{
	var formdata = $('#datbaseUser').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	
	var db_object_name = document.getElementById("db_object_name").value;

	var type_of_database = document.getElementById("type_of_database").value;
	var status = document.getElementById("status").value;
	//alert("project_status" +status);
	
	if (lhsIsNull(db_object_name) && lhsIsNull(type_of_database) && lhsIsNull(status)  ) {
        openErrorModal("Please select any filter to see the records !");
	}else{

	var actionUrl = "./getSearchDatabaseUserFilter?db_object_name=" +db_object_name+ "&type_of_database=" +type_of_database+ "&status=" +status;
	
	ajaxPostDataArray(actionUrl, fdata, searchDatabaseUserResponse);
	}
}// end function


function searchDatabaseUserResponse(response) {
	 console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
    loadDataUsingPaginatorGrid();

}// end function

function resetDatabaseUserFilter(){
	$("#databaseObjects").find("input[type=text], textarea, select").val("");
	refreshDatabaseUser();
	window.location.href='./databaseObjects';
}

function refreshDatabaseUser() {
	var url = "./databaseObjects";
	window.location = url;
}


function searchDatabasePackageFilter()
{
	var formdata = $('#databasePackage').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	
	var db_object_name = document.getElementById("db_object_name").value;

	//alert("db_object_name."+db_object_name);
	var status = document.getElementById("status").value;
	//alert("project_status" +status);
	
	if (lhsIsNull(db_object_name)  && lhsIsNull(status)  ) {
        openErrorModal("Please select any filter to see the records !");
	}else{

	var actionUrl = "./getSearchDatabasePackageFilter?db_object_name=" +db_object_name+ "&status=" +status;
	
	ajaxPostDataArray(actionUrl, fdata, searchDatabasePackageResponse);
	}
}// end function


function searchDatabasePackageResponse(response) {
	 console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
    loadDataUsingPaginatorGrid();


}// end function

function resetDatabasePackageFilter(){
	$("#databasePackage").find("input[type=text], textarea, select").val("");
	refreshDatabasePackage();
	window.location.href='./databasePackage';
}

function refreshDatabasePackage() {
	var url = "./databasePackage";
	window.location = url;
}


resetDatabaseSynonymFilter

function searchDatabaseSynonymFilter()
{
	var formdata = $('#databaseSynonym').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	
	var db_object_name = document.getElementById("db_object_name").value;
	var type_of_database = document.getElementById("type_of_database").value;
	var status = document.getElementById("status").value;
	//alert("project_status" +status);
	
	if (lhsIsNull(db_object_name)  && lhsIsNull(status)  ) {
        openErrorModal("Please select any filter to see the records !");
	}else{

	var actionUrl = "./getSearchDatabaseSynonymFilter?db_object_name=" +db_object_name+ "&type_of_database=" +type_of_database+ "&status=" +status;
	
	ajaxPostDataArray(actionUrl, fdata, searchDatabaseSynonymResponse);
	}
}// end function


function searchDatabaseSynonymResponse(response) {
	 console.log(response);
	$('#synonymtable').html('');
	$('#synonymtable').html(response);

}// end function

function resetDatabaseSynonymFilter(){
	$("#databaseSynonym").find("input[type=text], textarea, select").val("");
	refreshDatabaseSynonym();
	window.location.href='./databaseSynonym';
}

function refreshDatabaseSynonym() {
	var url = "./databaseSynonym";
	window.location = url;
}



/*
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
*/


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

//function searchLoginFilter() {   
//	var formdata = $('#userForm').serializeArray();
//	var fdata = JSON.stringify(formdata);
//	
//	var login_id =  document.getElementById("login_id").value;
//	
//	var user_name =  document.getElementById("user_name").value;
//	
//	//var login_count =  document.getElementById("login_count").value;
//	var user_code =  document.getElementById("user_code").value;
//	
//		if (lhsIsNull(login_id) && lhsIsNull(user_name) && lhsIsNull(user_code)) {
//	        openErrorModal("Please select filter to see the records !");
//		}else{
//			// var actionUrl = "./userLoginDetail?login_id="+login_id+"&user_name=" +user_name+"&user_code="+user_code;
//		 var actionUrl = "./searchLoginDetail?login_id="+login_id+"&user_name=" +user_name+"&user_code="+user_code;
//			ajaxPostDataArray(actionUrl, fdata, searchFilterDocResponse);
//		}
//		console.log("Filter:"+fdata);
// }
//
//function searchFilterDocResponse(response) {
//	 console.log(response);
//	$('#dataSpan').html('');
//	$('#dataSpan').html(response);
//	loadDataUsingPaginatorGrid();
//
//}// end function

