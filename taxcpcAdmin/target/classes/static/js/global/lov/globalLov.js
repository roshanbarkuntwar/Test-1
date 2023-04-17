/**
 * 
 */

// Functions--------------------------------------

// function globalLovKeyPressFunctionality(){
// opneGlobalLovModalModal();
// }// end function

function open_single_selection_lov(lov_id, replacement_string, postbackcolstring) {
        try {
                clearLovFields();
                document.getElementById("lov_lovid").value = lov_id;
                document.getElementById("lov_post_back_cols").value = postbackcolstring;
                document.getElementById("lov_replacementString").value = replacement_string;

                var url = "./lovConfiguration?lovid=" + encodeURIComponent(lov_id) + "&replacementString=" + encodeURIComponent(replacement_string) + "&postBackValue=" + encodeURIComponent(postbackcolstring);
                ajax_call_enabled = true;
                showProcessIndicator();
                $.ajax({
                        type : 'POST',
                        url : url,
                        // data : new FormData($(formElement)[0]),
                        async : false,
                        cache : false,
                        contentType : false,
                        processData : false,
                        // or your custom data either as object {foo: "bar",
			// ...} or
                        // foo=bar&...
                        success : function(response) {
                                hideProcessIndicator();
                                // alert("response--28---->\n"+response.instantSearch);
                                showAndHideSearchBtn(lov_id);
                                $("#lov_header").html(response.header);
                                $("#instant_search").val(response.instantSearch);
                                $("#globalLovModalBody").html(response.dataList);
                                $("#globalLovModal").modal('show');
                                ajax_call_enabled = false;

                        },
                        error : function(jqXHR, textStatus, errorThrown) {
                                console.log("errorThrown : " + errorThrown);
                                ajax_call_enabled = false; // if fails
                        }
                });
        } catch (error) {
                alert(error);
                // console.log(error);
                window.alert("All lOV parameters are not available to open LOV");
                console.log(error);
        }
} // end function
var callBackFunction;
function open_single_selection_lov_withCallBackFun(lov_id, replacement_string, postbackcolstring, callBackFun) {
        callBackFunction = callBackFun;
        try {
                clearLovFields();
                document.getElementById("lov_lovid").value = lov_id;
                document.getElementById("lov_post_back_cols").value = postbackcolstring;
                document.getElementById("lov_replacementString").value = replacement_string;

                var url = "./lovConfiguration?lovid=" + encodeURIComponent(lov_id) + "&replacementString=" + encodeURIComponent(replacement_string) + "&postBackValue=" + encodeURIComponent(postbackcolstring);
                ajax_call_enabled = true;
                //showProcessIndicator();
                $.ajax({
                        type : 'POST',
                        url : url,
                        // data : new FormData($(formElement)[0]),
                        async : false,
                        cache : false,
                        contentType : false,
                        processData : false,
                        // or your custom data either as object {foo: "bar",
			// ...} or
                        // foo=bar&...
                        success : function(response) {
                                //hideProcessIndicator();
                                // alert("response---72--->\n"+response.instantSearch);
                                showAndHideSearchBtn(lov_id);
                                $("#lov_header").html(response.header);
                                $("#instant_search").val(response.instantSearch);
                                $("#globalLovModalBody").html(response.dataList);
                                opneGlobalLovModalModal();
                                $("#globalLovModal").modal('show');
                                ajax_call_enabled = false;
                        },
                        error : function(jqXHR, textStatus, errorThrown) {
                                console.log("errorThrown : " + errorThrown);
                                ajax_call_enabled = false; // if fails
                        }
                });
        } catch (error) {
                alert(error);
                window.alert("All lOV parameters are not available to open LOV");
                console.log(error);
        }
} // end function

function reset_lov_data() {
        document.getElementById("lov_search_field").value = "";
        get_lov_data();
}

function get_lov_data() {
        try {

                var lov_id = document.getElementById("lov_lovid").value;
                alert(lov_id);
                var postbackcolstring = document.getElementById("lov_post_back_cols").value;
                var replacement_string = document.getElementById("lov_replacementString").value;
                var lov_search_field = document.getElementById("lov_search_field").value;

                var url = "./lovConfiguration?lovid=" + encodeURIComponent(lov_id) + "&replacementString=" + encodeURIComponent(replacement_string) + "&postBackValue=" + encodeURIComponent(postbackcolstring) + "&searchValue=" + encodeURIComponent(lov_search_field);
                url += "&searchOnBtn=T";
                // alert(url);
                ajax_call_enabled = true;
                $.ajax({
                        type : 'POST',
                        url : url,
                        // data : new FormData($(formElement)[0]),
                        async : false,
                        cache : false,
                        contentType : false,
                        processData : false,
                        // or your custom data either as object {foo: "bar",
			// ...} or
                        // foo=bar&...
                        success : function(response) {


                                // $("#globalLovModal").modal('show');

                                $("#globalLovModalBody").html(response.dataList);
                                on_lov_search_field_keyDown();
                                ajax_call_enabled = false;


                        },
                        error : function(jqXHR, textStatus, errorThrown) {
                                console.log("errorThrown : " + errorThrown);
                                ajax_call_enabled = false; // if fails
                        }
                });
        } catch (error) {
                alert(error);
                window.alert("All lOV parameters are not available to open LOV");
                console.log(error);
        }
} // end function

function post_back_data(row) {
	
        var postbackcols = document.getElementById("lov_post_back_cols").value;
alert(postbackcols);
        try {
                var all_elems_2 = postbackcols.split("#");
                for (var i = 0; all_elems_2.length; i++) {
                        // window.parent.document.getElementById(all_elems_2[i]).value
			// =
                        // row.cells[i].innerHTML;
                        var anchor = $(row.cells[i]).find("a")[0];
                        document.getElementById(all_elems_2[i]).value = $(anchor).html().replace(/&amp;/, "&");
                }
        } catch (error) {
                console.log(error);
        }
        $("#globalLovModal").modal('hide');
        setTimeout(() => {
                clearLovFields();
                try {
                        // callBackFunction();
                        var fun = new Function(callBackFunction);
                        fun();
                } catch (error) {
                        console.log(error);
                }
        }, 1000);
} // end function

function clearLovFields() {
        document.getElementById("lov_lovid").value = "";
        document.getElementById("lov_post_back_cols").value = "";
        document.getElementById("lov_replacementString").value = "";
        document.getElementById("lov_search_field").value = "";
}

var selected_data_searech = "";
function setSelectedCodes(elem) {
        try {
                var id = elem.id.split("~");
                var value = document.getElementById("lovrowselectable~" + id[1]).cells[1].innerHTML;
                if (elem.checked) {
                        selected_data_searech += value + "~~";
                } else {
                        var newsrchval = "";
                        var ttlrcd = document.getElementById("lovtotalrecord").value;
                        for (var i = 1; i <= parseInt(ttlrcd); i++) {
                                if (document.getElementById("lovrowselector~" + i).checked) {
                                        var row = document.getElementById("lovrowselectable~" + i);

                                        newsrchval += row.cells[1].innerHTML + "~~";
                                } // if
                        } // for
                        if (newsrchval.length > 2) {
                                newsrchval = newsrchval.substr(0, newsrchval.length - 2);
                        }
                        selected_data_searech = newsrchval;
                } // else
        } catch (error) {
                console.log(error);
        }
        return selected_data_searech;
}

function lovcheckall(elem) {
        var ttlrcd = document.getElementById("lovtotalrecord").value;
        for (var i = 1; i <= parseInt(ttlrcd); i++) {
                document.getElementById("lovrowselector~" + i).checked = elem.checked;
        }
}

function postDataToForm() {
        var selected_data = "";
        var selected_name = "";
        var ttlrcd = document.getElementById("lovtotalrecord").value;
        var nororselected = true;
        for (var i = 1; i <= parseInt(ttlrcd); i++) {
                if (document.getElementById("lovrowselector~" + i).checked) {
                        var row = document.getElementById("lovrowselectable~" + i);

                        // selected_data += row.cells[1].innerHTML + "#";
                        // selected_name += row.cells[2].innerHTML + ", ";
                        //	    
                        var anchorCell1 = $(row.cells[1]).find('a')[0];
                        var anchorCell2 = $(row.cells[2]).find('a')[0];

                        selected_data += $(anchorCell1).html() + "#";
                        selected_name += $(anchorCell2).html() + ", ";

                        nororselected = false;
                }
        }
        selected_data = selected_data.substr(0, selected_data.length - 1);
        selected_name = selected_name.substr(0, selected_name.length - 1);
        if (!nororselected) {
                var postbackcols = document.getElementById("lov_post_back_cols").value;
                if (postbackcols !== null && postbackcols.length > 0) {
                        var all_elems_2 = postbackcols.split("#");
                        try {
                                window.parent.document.getElementById(all_elems_2[0]).value = selected_data.trim();
                                window.parent.document.getElementById(all_elems_2[1]).value = selected_name.trim();
                        } catch (error) {
                                console.log(error);
                        }
                }
                try {
                        window.parent.document.getElementById(all_elems_2[1]).style.overflow = "auto";
                } catch (error) {
                        console.log(error);
                }
                try {
                        callBackFunction();
                } catch (error) {
                        console.log(error);
                }
                clearLovFields();
                $("#globalLovModal").modal('hide');
        } else {
                // window.alert("No record selected");
                // document.getElementById("dialog-message_noRecrdSelctd").style.display
		// =
                // "block";
                // $(function() {
                // $("#dialog-message_noRecrdSelctd").dialog({
                // modal : true,
                // buttons : {
                // Ok : function() {
                // $(this).dialog("close");
                // }
                // }
                // });
                // });
                openGlobalMsgModal('error', "Please select records ");
        }

}

function get_lov_data_instant_search(event) {
        var is = document.getElementById("instant_search").value;
        if (is.match("T")) {
                var callable = check_key_pressed(event);
                if (callable) {
                        get_lov_data();
                }
        }
}

function check_key_pressed(nkey) {
        var keyval;
        try {
                if (navigator.appName === "Microsoft Internet Explorer") {
                        keyval = window.event.keyCode;
                } else if (navigator.appName === 'Netscape') {
                        nkeycode = nkey.which;
                        keyval = nkeycode;
                }
                if (keyval === 37 || keyval === 38 || keyval === 39 || keyval === 40 || keyval === 16 || keyval === 17 || keyval === 18 || keyval === 19
                        || keyval === 20 || keyval === 33 || keyval === 34 || keyval === 35 || keyval === 36 || keyval === 44 || keyval === 45) {
                        return false;
                } else if (keyval === 27) {
                        window.parent.close_light_box();
                } else {
                        return true;
                }
        } catch (error) {
                return true;
                console.log(error);
        }
} // End of the function

function opneGlobalLovModalModal() {
        $('#globalLovModal').on('shown.bs.modal', function(event) {

                $('#lov_search_field').focus();
                on_lov_search_field_keyDown();
        });
} // ef

function on_lov_search_field_keyDown() {
        $('#lov_search_field').on('keydown', function(event) {
                if (event.keyCode === 40) { // down-arrow
                        $('#globalLovTableDataGrid tbody').find('a,input[type=checkbox]')[0].focus();
                } else {
                        return true;
                }
        });
        lhsArrowOnTable("globalLovTableDataGrid");

} // ef



function showAndHideSearchBtn(lovId) {
        // This method Logic Is only 20-6-2018 as per sanjeev sir to hide search
	// and clear button the batch or item stock lov on salebillEntry form;

        $('#globalLovSearchBtnSpan').css('display', 'block');

        if (lovId === 'getStockList') {
                $('#globalLovSearchBtnSpan').css('display', 'none');
        }
}
// end function
