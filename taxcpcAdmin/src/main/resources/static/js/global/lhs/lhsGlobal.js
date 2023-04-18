
var globalUpdateMessage = "Record(s) updated successfully !";
var globalDeleteMessage = "Record deleted successfully !";

$(function(){
	
	  $('#table').tablesorter({
	
	   // options here
	
	  });
	
	});

function showProcessIndicator() {
    document.getElementById("global-simple-process-indicator").style.display = "block";
}// End Function

function hideProcessIndicator() {
    document.getElementById("global-simple-process-indicator").style.display = "none";
}// End Function

function showBlackProcessIndicator() {
    document.getElementById("global-black-process-indicator").style.display = "block";
}// End Function

function hideBlackProcessIndicator() {
    document.getElementById("global-black-process-indicator").style.display = "none";
}// End Function

function openErrorModal(errorMessage, callBackFunction) {
    document.getElementById("errorAlertMsg").innerHTML = errorMessage;
    $("#dangerAlert").modal({show: true});
    if (!lhsIsNull(callBackFunction)) {
        $('#errorBtn').attr('onClick', callBackFunction);
    } else {
        $('#errorBtn').attr('onClick', "");
    }
}//end method

function openSuccessModal(errorMessage, callBackFunction) {
    document.getElementById("successAlertMsg").innerHTML = errorMessage;
    $("#successAlert").modal({show: true});
    if (!lhsIsNull(callBackFunction)) {
        $('#successBtn').attr('onClick', callBackFunction);
    } else {
        $('#successBtn').attr('onClick', "");
    }
}//end method

function openConfirmModal(errorMessage, callBackFunction) {
	// alert("global");
    document.getElementById("globalConfirmMsg").innerHTML = errorMessage;
    $("#confirmAlert").modal({show: true});
    if (!lhsIsNull(callBackFunction)) {
        $('#confirmOkBtn').attr('onClick', callBackFunction);
    } else {
        $('#confirmOkBtn').attr('onClick', "");
    }
}//end method

function closeConfirmModal() {
	$("#confirmAlert").modal('hide');
}// End Function

function clearFields(idStr) {
	try {
		var arr = idStr.split("#");
		for (var i = 0; i < arr.length; i++) {
			$('#' + arr[i]).val('');
		}
	} catch (err) {
		console.log(err);
	}
}// End Function

function setFocusOnField(id) {
	setTimeout(() => {
		$('#'+id).focus();
	}, 1000);
}// End Function

function callmyRepoerEngine(url) {
	//alert(url);
	setTimeout(function() {
			window.open(url, "_blank");
		}, 500);
}// End Function

function togglePasswordFields(fieldId, iconId) {
	var pwdField = document.getElementById(fieldId);
	var icon = document.getElementById(iconId);

	try {
		if (pwdField.type === "password") {
			pwdField.type = "text";
			icon.className = "fa fa-eye-slash";
			icon.title = "Hide Password";
		} else {
			pwdField.type = "password";
			icon.className = "fa fa-eye";
			icon.title = "Show Password";
		}
	} catch (err) {
		console.log(err);
	}
}// End Function

function addActionError(type, msg) {
    if (!lhsIsNull(type) && !lhsIsNull(msg)) {
        if (type === "success") {
            document.getElementById("successMsgDiv").style.display = "block";
            $("#successMsg").html(msg);
            try {
                document.getElementById("errorMsgDiv").style.display = "none";
                document.getElementById("notificationMsgDiv").style.display = "none";
            } catch (err) {
                console.log("errr...");
            }
        } else if (type === "error") {
            document.getElementById("errorMsgDiv").style.display = "block";
            $("#errorMsg").html(msg);
            try {
                document.getElementById("successMsgDiv").style.display = "none";
                document.getElementById("notificationMsgDiv").style.display = "none";
            } catch (err) {
                console.log("errr...");
            }
        } else if (type === "message") {
            document.getElementById("notificationMsgDiv").style.display = "block";
            $("#notificationMsg").html(msg);
            try {
                document.getElementById("successMsgDiv").style.display = "none";
                document.getElementById("errorMsgDiv").style.display = "none";
            } catch (err) {
                console.log("errr...");
            }
        }
    }
}//End of Function

function openCalendar(id, btnid, left, top) {
    var myCalendar;
    myCalendar = new dhtmlXCalendarObject({input: id, button: btnid});
    myCalendar.setDateFormat("%d-%m-%Y");
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

function openGlobalCalendarForIE(settings) {
    let calDivs = document.getElementsByClassName("dhtmlxcalendar_dhx_terrace");
    let calDiv = calDivs[calDivs.length - 1];


    $(calDiv).css('left', settings.left);
    $(calDiv).css('top', settings.right);
    $(calDiv).css('display', 'block');

    settings.calendar.attachEvent("onClick", function (e) {

        document.getElementById(settings.date_field_id).value = getDisplayDate(settings.calendar.getDate());

        $('.dhtmlxcalendar_dhx_terrace').remove();

        return;
    });
}

function openTimePicker(id) {
	$('#'+id).chungTimePicker();
	/*$('#'+id).chungTimePicker({
		viewType: 1
	});*/
}
