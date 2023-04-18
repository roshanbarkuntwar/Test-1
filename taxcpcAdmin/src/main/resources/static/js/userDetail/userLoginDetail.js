
function userLoginDetail() {
	window.location.href='./useLoginDetail';
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
 
function searchLoginFilter() {   
	var formdata = $('#userForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	
	var login_id =  document.getElementById("login_id").value;
	
	var user_name =  document.getElementById("user_name").value;
	
	//var login_count =  document.getElementById("login_count").value;
	var user_code =  document.getElementById("user_code").value;
	
		if (lhsIsNull(login_id) && lhsIsNull(user_name) && lhsIsNull(user_code)) {
	        openErrorModal("Please select filter to see the records !");
		}else{
			// var actionUrl = "./userLoginDetail?login_id="+login_id+"&user_name=" +user_name+"&user_code="+user_code;
		 var actionUrl = "./searchLoginDetail?login_id="+login_id+"&user_name=" +user_name+"&user_code="+user_code;
			ajaxPostDataArray(actionUrl, fdata, searchFilterDocResponse);
		}
		console.log("Filter:"+fdata);
 }

function searchFilterDocResponse(response) {
	 console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();

}// end function


function resetLoginFilter(){
	$("#userForm").find("input[type=text], textarea, select").val("");
	refreshUser();
	window.location.href='./userLoginDetail';
}

function refreshUser() {
	var url = "./userLoginDetail";
	window.location = url;
}

function searchFilterBrowseResponse(response) {
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();
}// end function

function viewUserDetails(userCode) {
 var url =  "./userLoginListCase?userCode="+userCode;

 window.location = url;
}

function backtoUserDashboard()
{
	var url = "./userLoginDetail";
	window.location = url;
	}


function openBrowseCalendar(id, btnid, left, right) {
    var myCalendar;
    myCalendar = new dhtmlXCalendarObject({input: id, button: btnid});
    myCalendar.setDateFormat("%d-%m-%Y");
    myCalendar.hideTime();

    if (window.dhx4.isIE11) {
        let settings = {
            date_field_id: id,
            calendar: myCalendar,
            left: left,
            right: right
        };
        openGlobalCalendarForIE(settings);
    }
}


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



function searchViewLoginFilter() {   
	var formdata = $('#userViewForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	
	var login_time =  document.getElementById("login_time").value;
	var user_code =  document.getElementById("user_code").value;
	
		if (lhsIsNull(login_time)) {
	        openErrorModal("Please select filter to see the records !");
		}else{
			// var actionUrl = "./userLoginListCase?login_time="+login_time+"&user_code="+user_code;
			 var actionUrl = "./searchLoginListCase?login_time="+login_time+"&user_code="+user_code;
			ajaxPostDataArray(actionUrl, fdata, searchViewFilterDocResponse);
		}
		console.log("Filter:"+fdata);
 }

function searchViewFilterDocResponse(response) {
	 console.log(response);
	$('#dataSpan').html('');
	$('#dataSpan').html(response);
	loadDataUsingPaginatorGrid();
}// end function



function refreshUserView() {
	document.getElementById("login_time").value = '';
	var user_code =  document.getElementById("user_code").value;
	var url = "./userLoginListCase?user_code="+user_code;
	window.location = url;
}