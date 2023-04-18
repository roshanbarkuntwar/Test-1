/**
 * 
 */

var ajax_call_enabled = false;

/**
 * Posts or submits form data
 * @param {type} url : url to submit without context path ex. /indentEntry?action=save
 * @param {type} formId : Id of form element to submit/post
 * @param {type} callBackFunction : function to call after success or error. Send null if not required
 * @returns {undefined} void
 */
function ajaxSubmitPostData(url, formId, callBackFunction) {
//    alert("submit--"+url);
//    showProcessIndicator();
    if (!ajax_call_enabled) {
        var cp = document.getElementById("globalcontextpath").value;
        var formElement = document.getElementById(formId);
        ajax_call_enabled = true;
        $.ajax({
            type: 'POST',
            url: cp + url,
            data: new FormData($(formElement)[0]),
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            //or your custom data either as object {foo: "bar", ...} or foo=bar&...
            success: function (response) {
                ajax_call_enabled = false;
                /*hideProcessIndicator();*/
                callBackFunction(response);
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                callBackFunction(null);
                /*hideProcessIndicator();*/
                console.log("errorThrown : " + errorThrown);
                ajax_call_enabled = false; //if fails    
            }
        });
    } else {
        window.alert("Please wait..\nYour previous request is in progress");
    }
}//end method
//================================================================================================================
/**
 * Posts or submits form data
 * @param {type} url : url to post without context path ex. /indentEntry?action=save
 * @param {type} callBackFunction : function to call after success or error. Send null if not required
 * @returns {undefined} void
 */
function ajaxPostUrl(url, callBackFunction) {
//    alert("Post--"+url);
    if (!ajax_call_enabled) {
//    	var cp = document.getElementById("globalcontextpath").value;
        ajax_call_enabled = true;
        $.ajax({
//            url: cp + url,
        	url: url,
            type: "POST",
            async : false,
            success: function (response, textStatus, jqXHR)
            {
                ajax_call_enabled = false;
                callBackFunction(response);
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                callBackFunction(null);
                console.log("errorThrown : " + errorThrown);
                ajax_call_enabled = false; //if fails     
            }
        });
    } else {
        window.alert("Please wait..\nYour previous request is in progress");
    }
}//end method
//
//================================================================================================================
/**
 * Posts or submits form data
 * @param {type} url : url to post without context path ex. /indentEntry?action=save
 * @param {type} callBackFunction : function to call after success or error. Send null if not required
 * @param {type} callBackParameter : function to call after success or error. Send null if not required
 * @returns {undefined} void
 */
function ajaxPostUrlWithParam(url, callBackFunction, callBackParameter) {
//    alert("Post--"+url);
    if (!ajax_call_enabled) {
//        var cp = document.getElementById("globalcontextpath").value;
        ajax_call_enabled = true;
        $.ajax({
//            url: cp + url,
            url: url,
            type: "POST",
            success: function (response, textStatus, jqXHR)
            {
                ajax_call_enabled = false;
                callBackFunction(response, callBackParameter);
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                callBackFunction(null, callBackParameter);
                console.log("errorThrown : " + errorThrown);
                ajax_call_enabled = false; //if fails     
            }
        });
    } else {
        window.alert("Please wait..\nYour previous request is in progress");
    }
}//end method
function ajaxPostUrlWithParamWithoutAjaxSync(url, callBackFunction, callBackParameter) {
//    alert("Post--"+url);

//    var cp = document.getElementById("globalcontextpath").value;

    $.ajax({
//        url: cp + url,
        url: url,
        type: "POST",
        success: function (response, textStatus, jqXHR)
        {
            callBackFunction(response, callBackParameter);
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            callBackFunction(null, callBackParameter);
            console.log("errorThrown : " + errorThrown);
        }
    });

}//end method
function ajaxPostUrlWithoutAjaxSync(url, callBackFunction) {
//    alert("Post--"+url);


//    var cp = document.getElementById("globalcontextpath").value;
    $.ajax({
//        url: cp + url,
        url: url,
        type: "POST",
        success: function (response, textStatus, jqXHR)
        {
            callBackFunction(response);
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            callBackFunction(null);
            console.log("errorThrown : " + errorThrown);
        }
    });

}//end method
function ajaxSubmitPostDataWithoutAjaxSync(url, formId, callBackFunction) {
//    alert("Post--"+url);
//    var cp = document.getElementById("globalcontextpath").value;
    var formElement = document.getElementById(formId);
    $.ajax({
//        url: cp + url,
        url: url,
        type: "POST",
        data: new FormData($(formElement)[0]),
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response, textStatus, jqXHR)
        {
            callBackFunction(response);
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            callBackFunction(null);
            console.log("errorThrown : " + errorThrown);
        }
    });

}//end method
//================================================================================================================
/**
 * Posts or submits form data
 * @param {type} url : url to submit without context path ex. /indentEntry?action=save
 * @param {type} dataArray : array of data to submit/post
 * @param {type} callBackFunction : function to call after success or error. Send null if not required
 * @returns {undefined} void
 */
function ajaxPostDataArray(url, dataArray, callBackFunction) {
//    alert("Post ary--"+url);
    if (!ajax_call_enabled) {
//        var cp = document.getElementById("globalcontextpath").value;
        ajax_call_enabled = true;
        $.ajax({
//            url: cp + url,
            url: url,
            type: "POST",
            data: dataArray,
            success: function (response, textStatus, jqXHR)
            {
                ajax_call_enabled = false;
                callBackFunction(response);
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                callBackFunction(null);
                console.log("errorThrown : " + errorThrown);
                ajax_call_enabled = false; //if fails      
            }
        });
    } else {
        window.alert("Please wait..\nYour previous request is in progress");
    }
}//end method
//================================================================================================================

function ajaxSerializedSubmitPostData(url, formId, callBackFunction) {
//    alert("Serialise--"+url);
    if (!ajax_call_enabled) {
//        var cp = document.getElementById("globalcontextpath").value;
        var formElement = document.getElementById(formId);
        ajax_call_enabled = true;
        $.ajax({
            type: 'POST',
//            url: cp + url,
            url: url,
            data: $(formElement).serialize(),
            //or your custom data either as object {foo: "bar", ...} or foo=bar&...
            success: function (response) {
                ajax_call_enabled = false;
                callBackFunction(response);
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                callBackFunction(null);
                console.log("errorThrown : " + errorThrown);
                ajax_call_enabled = false; //if fails    
            }
        });
    } else {
        window.alert("Please wait..\nYour previous request is in progress");
    }
}//end method

