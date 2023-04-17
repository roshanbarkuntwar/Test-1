function saveImpUrlData() {
	if (validateForm()) {
		ajaxSubmitPostData("./saveImpUrlDetail",
				"impUrlEntryForm", saveImpUrlDataResponse);
	} 
}

function saveImpUrlDataResponse(response) {
    if(response === "success") {
    	var action = $('#action').val();
    	if (action == "save") {
    		openSuccessModal("Data saved successfully !" ,"window.location.href='./importantUrl';");
		}else if (action == "edit") {
			openSuccessModal("Data updated successfully !" ,"backToDetailLink()");
		}       
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function



function validateForm(){
	var status;
    var link_type = $('#link_type').val();
    var link = $("#link").val();
  
     if(lhsIsNull(link_type)) {
    	openErrorModal("Please select Link Type.");
        status = false;
    }  else if (lhsIsNull(link)) {
		openErrorModal("Please enter Link.");
	} else {	
        status = true;
    }
    
    return status;
}//End function

function openFormOnUrlType(urlType) { 
	window.location.href='./impUrlDetailByType?link_type='+urlType;
}

function editLinkDetail(linkCode) {
	if (!lhsIsNull(linkCode)) {
		var url = "./importantUrlEntry?linkCode=" + linkCode;
		 window.location =url;
	}	
}

function backToDetailLink() {
	var link_type = $('#link_type').val();
	if (!lhsIsNull(link_type)) {
		window.location.href='./impUrlDetailByType?link_type='+link_type;
	}	
}

function deleteLinkDetail(linkCode){
	openConfirmModal("Do you want to delete ?","deleteSelectedLinkDetail('" + linkCode + "')");
}

function deleteSelectedLinkDetail(linkCode) {
	closeConfirmModal();
	var url = "./deleteLink?linkCode=" + linkCode;
	
	ajaxPostUrl(url, deleteLinkResponse);
}

function deleteLinkResponse(response) {
    if(response === "success") {
        openSuccessModal(globalDeleteMessage ,"window.location.reload();");
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function

function viewLinkDetail(linkCode) {
	if (!lhsIsNull(linkCode)) {
		var url = "./viewLinkDetail?linkCode=" + linkCode;		
		ajaxPostUrl(url, viewLinkDetailResponse);
	} 
}

function viewLinkDetailResponse(response) {
	if(!lhsIsNull(response)) {
		$("#viewModelBody").html("");
		$("#viewModelBody").html(response);
		$("#view-details").modal({show: true});
    } else {
    	$("#view-details").modal({show: true});
    }
}

function resetImpUrlform() {
	$("#impUrlEntryForm").find("input[type=text], textarea, select").val("");
	
}


function OpenLink(link){
	//alert("link=="+link);
	window.open(link);	
}

function onloadTOHideclient(){
	var user_code = $('#user_code').val();
	if(user_code === '11005' || user_code === '10144'){
 document.getElementById('svn').style.display='none';
	
	}
}