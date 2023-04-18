function saveEntityWork() {
	var filename = document.getElementById("file_logo").value;
	if (validateEntryForm()) {
		ajaxSubmitPostData("saveEntityForm", "entityEntryForm",
				saveEntityResponse);

	}
}

function saveEntityResponse(response) {

	if (response === "success") {
		var action = $('#action').val();

		var mode = document.getElementById("mode").value;
		if (action == "save") {
			openSuccessModal("Data saved successfully!",
					"window.location.href='./entityDetail';");
		} else {
			openSuccessModal("Data Updated successfully!",
					"OpenWithView_Mode('" + mode + "');");
		}
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}

function OpenWithView_Mode(mode) {
	var url = "./entityDetail?mode=" + mode;
	window.location = url;
}

function validateEntryForm() {
	var entity_code = $('#entity_code').val();
	var entity_name = $('#entity_name').val();
	var parent_code = $('#parent_code').val();
	var division = $('#division').val();
	var add1 = $('#add1').val();
	var add3 = $('#add3').val();
	var city = $('#city').val();
	var Pin = $('#Pin').val();
	var phoneno = $('#phone').val();
	var email = $('#email').val();
	var print_name = $('#print_name').val();
	var website = $('#website').val();

	var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

	
	var pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
	'((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
	'((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
	'(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
	'(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
	'(\\#[-a-z\\d_]*)?$', 'i');

	
	var phoneno1 = /^\d{10}$/;
	var pincode = /^\d{6}$/
	var dbuser = $('#db_user').val();
	var dbuser_pwd = $('#db_user_pwd').val();
	var action_name = $('#action').val();
	var status;

	if (lhsIsNull(entity_code)) {
		openErrorModal("Please Enter Entity Code.");
		status = false;
	} else if (lhsIsNull(entity_name)) {
		openErrorModal("Please Enter Entity Name.");
		status = false;
	} else if (lhsIsNull(division)) {
		openErrorModal("Please Enter Division.");
		status = false;
	} else if (lhsIsNull(add1)) {
		openErrorModal("Please Enter Address.");
		status = false;
	} else if (lhsIsNull(city)) {
		openErrorModal("Please Enter City Name.");
		status = false;
	} else if (lhsIsNull(Pin)) {
		openErrorModal("Please Enter PIN Code.");
		status = false;
	} else if (lhsIsNull(phoneno)) {
		openErrorModal(" Please Enter Phone Number.");
		status = false;
	} else if (phoneno.length < 10 ) {
			openErrorModal(" Please Enter  Valid 10 Digit Phone Number.");
			status = false;
	} else if (phoneno.length > 15 ) {
		openErrorModal(" Mobile Number Excced  Maximum length of size");
		status = false;
       }
	else if (!email.match(mailformat)) {
		openErrorModal("Please Enter Email Address!");
		status = false;
	} else if (lhsIsNull(print_name)) {
		openErrorModal("Please Enter Print Name.");
		status = false;
	} else if (lhsIsNull(website)) {
		openErrorModal("Please Enter  Website .");
		status = false;
	} else if (lhsIsNull(add3)) {
		openErrorModal("Please Enter War File.");
		status = false;
	} else if (lhsIsNull(dbuser)) {
		openErrorModal("Please Enter DB User.");
		status = false;
	} else if (lhsIsNull(dbuser_pwd)) {
		openErrorModal("Please Enter DB UserPassword.");
		status = false;
	} else if (add1.length >= 4000) {
		openErrorModal("Address exceed Maximum limit of character");
		status = false;
	}else{
		status = true;
	}

	return status;
}

function isValidUrl(field) {
	//var regexp = /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
	 var pattern = new RegExp('^(https?:\\/\\/)?'+ // protocol
			    '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
			    '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
			    '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
			    '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
			    '(\\#[-a-z\\d_]*)?$','i'); //
	if (pattern.test(field.value)) {
		return true;
	} else {
		openErrorModal("Invalid Website");
		field.value = "";
		return false;
	}
}

function pinCode(field) {
	var validPin = /(^\d{6}$)/;
	if (validPin.test(field.value)) {
		return true;

	} else {
		openErrorModal("Invalid  Please Enter 6 Digit PIN Code");
		field.value = "";
		return false;
	}
}// end function

function mobileNO(field) {
	//var validPin = /(^\+(?=.{10,15})\d{10,15}_{0,5}$)/;
	if (field.value.length >10 ){
		return true;

	} else {
		openErrorModal("Invalid Number");
		field.value = "";
		return false;
	}
}// end function

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

function viewRecord(funName) {
	if (!lhsIsNull(checkedId)) {
		window[funName](checkedId);
	}
}

function viewEntity(id) {
	var entity_code = id;
	var url = "./entityEntryDetails?entity_code=" + entity_code;
	ajaxPostUrl(url, viewEntitytDetails);
}

function viewEntitytDetails(response) {

	if (!lhsIsNull(response)) {
		$("#ModelTableBody").html("");
		$("#ModelTableBody").html(response);
		$("#myModal").modal({
			show : true
		});

	} else {
		$("#myModal").modal({
			show : true
		});
	}
}

function viewEntityText() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var entity_code = split_id[2];
		var url = "./entityEntryDetails?entity_code=" + entity_code;
		ajaxPostUrl(url, viewEntityTable);
	}
}

function viewEntityTable(response) {
	if (!lhsIsNull(response)) {
		$("#ModelTableBody").html("");
		$("#ModelTableBody").html(response);
		$("#myModal").modal({
			show : true
		});

	} else {
		$("#myModal").modal({
			show : true
		});
	}
}

function viewDefaultEntityText() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var client_code = split_id[2];
		var url = "./DefaultentityEntryDetails?client_code=" + client_code;
		ajaxPostUrl(url, viewEntityTable);
	}
}

function viewEntityTable(response) {
	if (!lhsIsNull(response)) {
		$("#ModelTableBody").html("");
		$("#ModelTableBody").html(response);
		$("#myModal").modal({
			show : true
		});

	} else {
		$("#myModal").modal({
			show : true
		});
	}
}

function openviewForm(mode) {
	var url = "/entityDetail?mode=" + mode;
	window.location = url;
}

function deleteEntity(id) {
	openConfirmModal("Do you want to delete ?", "deleteSelectedWork('" + id
			+ "')");
}

function deleteSelectedWork(id) {

	var entity_code = id;
	var url = "./deleteEntity?entity_code=" + entity_code;
	ajaxPostUrl(url, deleteWorkResponse);

}

function deleteWorkResponse(response) {
	if (response === "success") {
		openSuccessModal(globalDeleteMessage, "window.location.reload();");
	} else {
		openErrorModal("Some Error Occured..!", "");
	}
}// End function

function editEntity(id) {

	var entity_code = id
	var mode = document.getElementById("viewCard").value;
	var url = "entityEntry?entity_code=" + entity_code + "&mode=" + mode;
	window.location = url;
}

function editEntityText() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var entity_code = split_id[2];
		var View = split_id[3];
		var url = "entityEntry?entity_code=" + entity_code + "&View=" + View;
		window.location = url;
	}

}

function EntitySearch() {

	var mode = document.getElementById("mode").value;
	var formdata = $('#entityDetail').serializeArray();
	var fdata = JSON.stringify(formdata);
	

	var searchvalue = document.getElementById("search_entity").value;

	if (lhsIsNull(searchvalue)) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		if (mode == 'Text') {
			var actionUrl = "./entityDetailFilter?searchvalue="+searchvalue;
			ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
		} else {
			var actionUrl = "./entitySearchFilter?searchvalue="+searchvalue;
			ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
		}

	}
}

function searchFilterWorkResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);// ("response");
	loadDataUsingPaginatorGrid();

}// end function

function loadData() {
	var mode = document.getElementById("mode").value;
	if (mode === 'Card') {
		document.getElementById("Card").checked = true;
		document.getElementById("Text").checked = false;
		//document.getElementById('search_entity').style.display = 'inline-flex';
		//document.getElementById('division').style.display = 'none';
//		document.getElementById('to_date11').style.display = 'none';
//		document.getElementById('to_date12').style.display = 'none';
		document.getElementById('bulk_download_btn').style.display = 'none';

	} else if (mode === 'Text') {
		document.getElementById("Text").checked = true;
		document.getElementById("Card").checked = false;
		//document.getElementById('search_entity').style.display = 'none';
		//document.getElementById('division').style.display = 'inline-flex';
//		document.getElementById('to_date11').style.display = 'inline-flex';
//		document.getElementById('to_date12').style.display = 'inline-flex';
		document.getElementById('bulk_download_btn').style.display = 'none';
	}

}

function entityRefresh() {
	var mode = document.getElementById("mode").value;
	window.location.href = "./entityDetail?mode=" + mode;
}

function getLoadView(mode) {

	//document.getElementById('entity_name').value = '';
	//document.getElementById('division').value = '';
	document.getElementById('search_entity').value = '';

	document.getElementById('mode').value = mode;

	if (mode == 'Card') {
		document.getElementById('bulk_download_btn').style.display = 'none';
		//document.getElementById('search_entity').style.display = 'inline-flex';
		//document.getElementById('division').style.display = 'none';
		//document.getElementById('entity_name').style.display = 'none';
//		document.getElementById('to_date11').style.display = 'none';
//		document.getElementById('to_date12').style.display = 'none';
		

	} else if (mode == 'Text') {
		document.getElementById('bulk_download_btn').style.display = 'none';
		//document.getElementById('search_entity').style.display = 'none';
		//document.getElementById('division').style.display = 'inline-flex';
//		document.getElementById('to_date11').style.display = 'inline-flex';
//		document.getElementById('to_date12').style.display = 'inline-flex';

	}
	var formdata = $('#entityDetail').serializeArray();
	var fdata = JSON.stringify(formdata);
	var actionUrl = "./entityDetail?mode=" + mode;
	ajaxPostDataArray(actionUrl, fdata, SaveResponse);

}// end function

function SaveResponse(response) {
	console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);

}//

function BackToDashboard(id) {
	var mode = id;
	var url = "./entityDetail?mode=" + mode;
	window.location = url;

}

function saveDefaultEntryDetail(){
	var client_code = document.getElementById('client_code').value

	if (validateEntityEntryBO()) {
		ajaxSubmitPostData("saveDefaultEntryDetail","defaultEntityEntry", saveDefaultResponse);
} 	 
}

function saveDefaultResponse(response) {
	var action = document.getElementById('action').value
	//alert("action......."+action);
    if(response === "success") {
    	if(action === "save"){
        openSuccessModal("Data saved successfully !" ,"backToDefaultDashboard();");
    	}else{
    		openSuccessModal("Data Updated successfully !" ,"backToDefaultDashboard();");
    	}
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function


function backToDefaultDashboard() {
	window.location.href='./defaultentityDetail';
}


function backToDefaultEntryDashboard()
{
	window.location.href='./defaultentityDetail';
}


function saveDefaultEntry(){
	var client_code = document.getElementById('client_code').value

	if (validateEntityEntryBO()) {
		ajaxSubmitPostData("saveDefaultEntryDetail","defaultEntityEntry", saveDefaultEntryResponse);
} 
}

function saveDefaultEntryResponse(response) {
	var action = document.getElementById('action').value
	//alert("action......."+action);
    if(response === "success") {
    	if(action === "save"){
        openSuccessModal("Data saved successfully !" ,"backToEntityForm();");
    	}else{
    		openSuccessModal("Data Updated successfully !" ,"backToUpdateEntityForm();");
    	}
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function


function backToEntityForm() {
	window.location.href='./defaultEntityEntry';
}

function backToUpdateEntityForm(){
	 var client_code = document.getElementById('client_code').value;
var url = "./editDefaultEntry?client_code=" + client_code;
//alert("url.."+url);
window.location =url;
}

function validateEntityEntryBO(){
	var status;
	
	var action =  document.getElementById('action').value;
	
	
    var client_name = $('#client_name').val();
    var client_code = document.getElementById('client_code').value;
    var no_of_client = $('#no_of_transaction').val();
    var parent_code = document.getElementById('parent_code').value;
    var initiate_date = $('#initiate_date').val();
  
    
    if(lhsIsNull(client_code) && lhsIsNull(client_name) && lhsIsNull(parent_code) && lhsIsNull(no_of_client) && lhsIsNull(initiate_date)) {
    	openErrorModal("Please Fill Form.Null Values Not Accepted");
        status = false;
    }else if(lhsIsNull(client_code)) {
    	openErrorModal("Please Enter Client Code.");
        status = false;
    } else if(client_code.length > 8) {
		openErrorModal("Maximum Limit is 8 Characters.");
		status = false;
	}
    else if(lhsIsNull(client_name)) {
    	openErrorModal("Please Enter Client Name.");
        status = false;
    }
    
    /*else if(lhsIsNull(parent_code)) {
    	openErrorModal("Please Enter Parent Code.");
        status = false;
    }else if(parent_code.length > 8) {
		openErrorModal("Maximum Limit is 8 Characters.");
		status = false;
	}*/
    
    else if(lhsIsNull(no_of_client)) {
    	openErrorModal("Please Enter No. of Transaction.");
        status = false;
    }   else if (lhsIsNull(initiate_date)) {
		openErrorModal("Please Enter Initiate Date.");
		status = false;
	} 
    else {	
        status = true;
    }
    
    if(action == "edit") {
    	  var closed_date = $('#closed_date').val();
    	  var closed_remark = document.getElementById('closed_remark').value;
    	 
    	  if(lhsIsNull(client_code)) {
    	    	openErrorModal("Please Enter Client Code.");
    	        status = false;
    	    }
    	    else if(client_code.length > 8) {
    			openErrorModal("Maximum Limit is 8 Characters.");
    			status = false;
    		} 
    	    else if(lhsIsNull(client_name)) {
    	    	openErrorModal("Please Enter Client Name.");
    	        status = false;
    	    }
    	    
    	  /*  else if(lhsIsNull(parent_code)) {
    	    	openErrorModal("Please Enter Parent Code.");
    	        status = false;
    	    }else if(parent_code.length > 8) {
    			openErrorModal("Maximum Limit is 8 Characters.");
    			status = false;
    		}*/
    	    else if(lhsIsNull(no_of_client)) {
    	    	openErrorModal("Please Enter No. of Transaction.");
    	        status = false;
    	    }   else if (lhsIsNull(initiate_date)) {
    			openErrorModal("Please Enter Initiate Date.");
    			status = false;
    		} else if(lhsIsNull(closed_date)) {
    	    	openErrorModal("Please Enter Closed Date.");
    	        status = false;
    	    }   else if (lhsIsNull(closed_remark)) {
    			openErrorModal("Please Enter closed Remark.");
    			status = false;
    		} 
    	    else {	
    	        status = true;
    	    }
    }
    return status;
}



function DefaultViewEntity(id) {
	var client_code = id;
	var url = "./DefaultentityEntryDetails?client_code=" + client_code;
	ajaxPostUrl(url, viewEntitytDetails);
}

function viewEntitytDetails(response) {

	if (!lhsIsNull(response)) {
		$("#ModelTableBody").html("");
		$("#ModelTableBody").html(response);
		$("#myModal").modal({
			show : true
		});

	} else {
		$("#myModal").modal({
			show : true
		});
	}
}

function DefaultEntitySearch(){
	var mode = document.getElementById("mode").value;
	var formdata = $('#defaultEntityDetail').serializeArray();
	var fdata = JSON.stringify(formdata);

	var from_date = document.getElementById("from_date").value;
	var to_date = document.getElementById("to_date").value;
	var searchvalue = document.getElementById("search_entity").value;
//	var initiate_date = document.getElementById("initiate_date").value;
//	var closed_date = document.getElementById("closed_date").value;
//	document.getElementById('initiate_date1').value=initiate_date;
//	document.getElementById('closed_date1').value=closed_date;
	document.getElementById('from_date1').value=from_date;
	document.getElementById('to_date1').value=to_date;
	if (lhsIsNull(searchvalue) && lhsIsNull(from_date) && lhsIsNull(to_date) ) {
		openErrorModal("Please select any filter to see the records !");
	} else {
		if (mode == 'Text') {
			var actionUrl = "./entityDefaultDetailFilter?searchvalue="+searchvalue+"&from_date="+from_date+"&to_date="+to_date;
			ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
		} else {
			var actionUrl = "./entityDefaultDetailFilter?searchvalue="+searchvalue+"&from_date="+from_date+"&to_date="+to_date;
			ajaxPostDataArray(actionUrl, fdata, searchFilterWorkResponse);
		}

	}
}


function searchFilterWorkResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);// ("response");
	loadDataUsingPaginatorGrid();

}// end function

function entityDefaultRefresh(){
	var mode = document.getElementById("mode").value;
	window.location.href = "./defaultentityDetail?mode=" + mode;	
}


function getLoadViewDefault(mode){
	
	document.getElementById('search_entity').value = '';

	document.getElementById('mode').value = mode;

	if (mode == 'Card') {
		document.getElementById('bulk_download_btn').style.display = 'none';
		document.getElementById('to_date11').style.display = 'none';
		document.getElementById('to_date12').style.display = 'none';

	} else if (mode == 'Text') {
		document.getElementById('bulk_download_btn').style.display = 'none';
		document.getElementById('to_date11').style.display = 'inline-flex';
		document.getElementById('to_date12').style.display = 'inline-flex';
	}
	var formdata = $('#defaultEntityDetail').serializeArray();
	var fdata = JSON.stringify(formdata);
	var actionUrl = "./defaultentityDetail?mode=" + mode;
	ajaxPostDataArray(actionUrl, fdata, SaveResponse);

}// end function

function SaveResponse(response) {
	
	$('#dataSpan').html('');
	$('#dataSpan').html(response);

}

function validateDateOnBlur(field) {
    try {
        var value = field.value;
        var checkTodayDate = new Date();

        if (value !== "" && value !== "null" && value !== null) {
            var dataSplit = value.split("-")[2];
            var yearSplit = dataSplit.split('');
            var firstTwo = parseInt(yearSplit[0] + yearSplit[1]);
            var lastTwo = yearSplit[2] + yearSplit[3];
            if (firstTwo != 19) {
                if (firstTwo != 20) {
                    firstTwo = 20;
                    for (var i = 2; i < lastTwo.length; i++) {//--- if year has less than four chars
                        lastTwo = "0" + lastTwo;
                    }
                }
            }
            var dateFinal = value.split("-")[0] + "-" + value.split("-")[1] + "-" + firstTwo + lastTwo;
            var checkDate = value.split("-")[1] + "/" + value.split("-")[0] + "/" + firstTwo + lastTwo;
            var convertToDateFormat = new Date(checkDate);

            if (convertToDateFormat > checkTodayDate) {
                field.value = "";
                openErrorModal("Future date is not allowed");
            } else {
                field.value = dateFinal;
            }
        }

    } catch (e) {
    }
}//end method


function validateDateOnKeyDown(field, evt) {
    var dateVal = field.value;
    var dateValLen = dateVal.length;
    var theEvent = evt || window.event;
    var key = theEvent.keyCode || theEvent.which;
    key = String.fromCharCode(key);
    var regex = /[0-9]|\-/;

    if (!regex.test(key)) {//if other than no or hyphen
        theEvent.returnValue = false;
        if (theEvent.preventDefault)
            theEvent.preventDefault();
    } else {//
        var regex1 = /-/;
        var regex2 = /[0-9]/;
        if (key.match(regex1)) {//for hyphen
            if (dateValLen != 2 && dateValLen != 5) {
                theEvent.returnValue = false;
                if (theEvent.preventDefault)
                    theEvent.preventDefault();
            }
        } else {//for numbers
            if (key.match(regex2)) {
                if (dateValLen == 2 || dateValLen == 5) {
                    theEvent.returnValue = false;
                    if (theEvent.preventDefault)
                        theEvent.preventDefault();
                }
            }
        }
    }
}//end method



function editDefaultEntityText() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		editDefaultEntityTextDetails(checkedId);
	}
}

function editDefaultEntityTextDetails(id){
	//alert("id.."+id);
	var client_code = id.split("~");
	var url = "./editDefaultEntry?client_code=" + client_code[2];
	//alert("url.."+url);
	window.location =url;
	
	//window.location.href='./editprojectEntry';
}


function DefaultEditEntity(id){

	var client_code = id;
	var mode = document.getElementById("viewCard").value;
	var url = "/editDefaultEntry?client_code=" + client_code + "&mode=" + mode;
	window.location = url;
}


function deleteDefaultText() {
	openConfirmModal("Do you want to delete ?", "deleteSelectedWork()");
}


function deleteSelectedWork() {
	closeConfirmModal();
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var client_code = split_id[2];
		var url = "./deleteDefaultEntry?client_code="+client_code;
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



function openCalendarDefault(id, btnid, left, top) {
    var myCalendar;
    myCalendar = new dhtmlXCalendarObject({input: id, button: btnid});
    myCalendar.setDateFormat("%d/%m/%Y");
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


function openCalendarOnLoad(id, btnid, left, top) {
    var myCalendar;
    myCalendar = new dhtmlXCalendarObject({input: id, button: btnid});
    myCalendar.setDateFormat("%d/%m/%Y");
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

function onLoadFunctionDefault(){ 
	document.getElementById("entity_code").readOnly = true;
	}