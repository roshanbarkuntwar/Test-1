function myFunction(){
	var action = document.getElementById("action").value;
	if(action == 'save'){
	document.getElementById('description').style.display='none';
	}
}

function showFieldsOnType() {
	document.getElementById('description').style.display='none';
}

function saveDictionary() {

	var action = document.getElementById('action').value;
	var description = $(".Editor-editor").html(); 
	$("#description").val(description);
	if (validateForm()) {

		ajaxSubmitPostData("saveProjectDictionary", "projectDictionaryForm",
				saveDictionaryResponse);

	}
}

function saveDictionaryResponse(response) {
    if(response === "success") {
    	var action = $('#action').val();
    	//alert("action"+action);
    	if (action == "save") {
    		openSuccessModal("Data saved successfully !" ,"window.location.href='./projectDictionary';");
		}else if (action == "edit") {
			openSuccessModal("Data updated successfully !" ,"window.location.href='./projectDictionary';");
		}  
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}

function validateForm(){
	var status;
    var keyword = $('#keyword_title_question').val();
    var project_code = $('#project_code').val();
    var description = '';

    	description = $(".Editor-editor").html();
    var link = $('#link').val();
    
	if(lhsIsNull(project_code)) {
		openErrorModal("Please select Project Name.");
	    status = false;
	} else if(lhsIsNull(keyword)) {
    	openErrorModal("Please enter Project Keyword / Dictionary Title.");
        status = false;
    } 
	else if(keyword.length > 100) {
    	openErrorModal("Project Keyword / Dictionary Title Exceeds Maximum Limit Of Characters.");
        status = false;
    }
	else if (lhsIsNull(description)) {
		openErrorModal("Please enter Description.");
		 status = false;
	}
	else if (description.length >= 4000) {
		openErrorModal("Description Exceeds Maximum Limit Of Characters.");
		 status = false;
	} 
	else if (link.length >= 2000) {
		openErrorModal("Link Exceeds Maximum Limit Of Characters..");
		 status = false;
	}else {	
        status = true;
    }
    
    return status;
}//End function
 
function editDictionary() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);
		editProjectDictionary(checkedId);
	}

}
function editProjectDictionary(id) {
	
	var split_id = id.split("-");
	var action = split_id[0];
	var sr_no = split_id[1];
	var seq_id = split_id[2];
	if (!lhsIsNull(seq_id)) {
		var doc_file = $('#doc_file').val();
		
	   var url = "./projectDictionaryEntry?seq=" + seq_id;
		window.location =url;
		
	}	
}

function deleteDictionary() {

	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..." + checkedId);
		deleteProjectDictionary(checkedId);
	}
}
function deleteProjectDictionary(id){
	var split_id = id.split("-");
	var action = split_id[0];
	var sr_no = split_id[1];
	var seq_id = split_id[2];
	openConfirmModal("Do you want to delete ?","deleteSelectedDictionary('" + seq_id + "')");
}

function deleteSelectedDictionary(id) {
	closeConfirmModal();
	var cp = document.getElementById("globalcontextpath").value;
	var url = cp+"deleteDictionary?seq=" + id;
	
	ajaxPostUrl(url, deleteSelectedDictionaryResponse);
}

function deleteSelectedDictionaryResponse(response) {
    if(response === "success") {
        openSuccessModal(globalDeleteMessage ,"window.location.reload();");
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function


function viewDictionary() {

	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split("-");
		var action = split_id[0];
		var sr_no = split_id[1];
		var seq_id = split_id[2];
		 var url = "./viewDictionaryDetail?seq=" +seq_id;
       ajaxPostUrl(url, viewDictionaryResponse);
	}
}

function viewDictionaryResponse(response) {
	if(!lhsIsNull(response)) {
	$("#workModelTableBody").html("");
	$("#workModelTableBody").html(response);
	$("#detail-modal").modal({show: true});
	
    } else {
    	$("#detail-modal").modal({show: true});
    }
}

function onEditAction(){
	var action = $('#action').val();
	if (action == "edit") {
		var description = $("#description").text();
		$(".Editor-editor").html(description);
	}
}


function OpenLink(id){
	var split_id = id.split('~');
	var link = split_id[1];
	var url;
	if( link.indexOf("http") == 0 ) {
	    url=link;
	} else {
		url='https://'+link;
		
	}
	if(!lhsIsNull(url)){
	window.open(url);
	}      
	}


function downloadSingleFile(id){
	var split_id = id.split('~');
	var seq_id = split_id[1];
	var cp = document.getElementById("globalcontextpath").value;
	//alert("seq_id.."+seq_id);
	var file_name = split_id[2];
	if(file_name === 'null'){
		document.getElementById('notificationMsg').innerHTML='File Does Not Exist';
		document.getElementById('notification').style.display = 'block';
	}else{
	var url = cp+"getdownloadSingleFile?seq_id="+seq_id;
	window.location = url;
   }
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

/*function CheckAndDisplayButton()
{
var download =document.getElementById("download").value;
var link = 	document.getElementById("link").value;
	alert("download........"+download);
	alert("link......."+link);
}*/

function searchProjectDictionary() {   
	var formdata = $('#docDetails').serializeArray();
	var fdata = JSON.stringify(formdata);
	
	var project_code =  document.getElementById("project_code").value;
	
	var dict_name =  document.getElementById("dict_name").value;
	
	//var login_count =  document.getElementById("login_count").value;
	//var user_code =  document.getElementById("user_code").value;
	
		if (lhsIsNull(project_code) && lhsIsNull(dict_name)) {
	        openErrorModal("Please select filter to see the records !");
		}else{
			var actionUrl = "./searchProjectDictionary?project_code="+project_code+"&dict_name=" +dict_name;
		 //  var actionUrl = "./searchLoginDetail?login_id="+login_id+"&user_name=" +user_name+"&user_code="+user_code;
			ajaxPostDataArray(actionUrl, fdata, searchFilterResponse);
		}
		console.log("Filter:"+fdata);
 }

function searchFilterResponse(response) {
	 console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function


function refreshProjectDictionaryFilter(){
	$("#docDetails").find("input[type=text], textarea, select").val("");
	refreshProjectDetail();
	window.location.href='./projectDictionary';
}

function refreshProjectDetail() {
	var url = "./projectDictionary";
	window.location = url;
}