var LOV_ID;
var REPLACEMENT_FUNC;
var POST_BACK_COLS;
var CALL_BACK_FUNC;
var KEY_PRESSED_BTN;
var FOCUS_ON_RETURN_ID;
var ROW_NUM_COUNT = 1;
var ROW_NUM_COUNT_SEARCH = 1;
var DEFAULT_RECORD_VAL = 10;
var PAGE_COUNT = 1;
var PAGE_COUNT_SEARCH = 1;

function F8_BtnPressed(event) {
	var key = event.keyCode;
	if (key === 119)
		return true;
	else
		return false;
}// End Function

function F9_BtnPressed(event) {
	var key = event.keyCode;
	if (key === 120)
		return true;
	else
		return false;
}// End Function

function validateFromLOV(event, lovID, replacementFunction, callBackFunction, postBackCols, focusOnReturnID, fieldID) {
	if (!lhsIsNull(lovID)) {
		var value_to_validate = $("#"+fieldID).val();
		
		var replacementObj;
    	if(!lhsIsNull(replacementFunction)) {
    		replacementObj = eval(replacementFunction);
    	}
    	
    	var value = '';
        if (replacementObj != null) {
        	value = JSON.stringify(replacementObj);
        }
    	
		if(!lhsIsNull(value_to_validate)) {
			var action_url = "./validateFromLOV";
			
			if (!ajax_call_enabled) {
				ajax_call_enabled = true;
				
				$.ajax({
					url : action_url,
					type : 'GET',
					data : {
						lovId : lovID,
						valueToValidate : value_to_validate,
						queryReplaceValues : value,
						postBackCols : postBackCols
					},
					contentType : 'application/json',
					success : function(response, statusText, jqXhr) {
						ajax_call_enabled = false;
						if (!lhsIsNull(response)) {
							$.parseJSON(JSON.stringify(response), function(key, val) {
								if (!lhsIsNull(key) && !lhsIsNull(val)) {
									document.getElementById(key.toLowerCase()).value = val;
								}
							});							
							try {
					        	if (!lhsIsNull(CALL_BACK_FUNC)) {
					            	eval(CALL_BACK_FUNC);
					            }
					        } catch (error) {
					            console.log(error);
					        }
						} else {
							openLOV(event, lovID, replacementFunction, callBackFunction, postBackCols, focusOnReturnID, "T");  // "T" for bypassing eventcheck
						}
					},
					error : function(jqXhr, textStatus, errorThrown) {
						ajax_call_enabled = false;
						console.log('errorThrown: ' + errorThrown);
						openLOV(event, lovID, replacementFunction, callBackFunction, postBackCols, focusOnReturnID, "T");  // "T" for bypassing eventcheck
					}
				});
			}
		}
	}
}// End Function

function openLOV(event, lovID, replacementFunction, callBackFunction, postBackCols, focusOnReturnID, flag, defPar = "default") {
	var hiddenSelectedCellTwo = document.getElementById("hiddenSelectedCellTwo").value;
	
	if(!lhsIsNull(event)) {		
		if (F8_BtnPressed(event)) {
			KEY_PRESSED_BTN = "F8";
		} else {
			KEY_PRESSED_BTN = "F9"
		}
	}
	
	try {
		LOV_ID = lovID;
	} catch(err) {}
	
	try { 
		REPLACEMENT_FUNC = replacementFunction;
	} catch(err) {}
	
	try { 
		CALL_BACK_FUNC = callBackFunction;
	} catch(err) {}

	try {
		POST_BACK_COLS = postBackCols;
	} catch(err) {}
	
	try { 
		FOCUS_ON_RETURN_ID = focusOnReturnID;
	} catch(err) {}
	
	if ((!lhsIsNull(event) && (F8_BtnPressed(event) || F9_BtnPressed(event) || event.type.match("dblclick") || event.type.match("click"))) || flag === "T") {
		lovCallBackFunction = '';
        replaceKeyValueObj = null;
        if(defPar != "Pagination") {
        	ROW_NUM_COUNT = 1;
        	ROW_NUM_COUNT_SEARCH = 1;
        	PAGE_COUNT = 1;
        	PAGE_COUNT_SEARCH = 1; 
        }
        
    	var replacementObj;
    	if(!lhsIsNull(REPLACEMENT_FUNC)) {
    		replacementObj = eval(REPLACEMENT_FUNC);
    	}
    	
// 		console.log(JSON.stringify(replacementObj));
    	
    	if(!lhsIsNull(POST_BACK_COLS) && KEY_PRESSED_BTN === "F8") {
    		POST_BACK_COLS = getPostBackColsF8(POST_BACK_COLS);
    	}    	
    	
        try {
        	clearLovFields();
        	
            var value = '';
            if (replacementObj != null) {
            	value = JSON.stringify(replacementObj);
            }
                        
 			showProcessIndicator();
            var url = "./lovConfiguration?lovid=" + encodeURIComponent(LOV_ID) 
            		+ "&replaceMap=" + encodeURIComponent(value) 
            		+ "&postBackCols=" + encodeURIComponent(POST_BACK_COLS) 
            		+ "&keyPressButton=" + encodeURIComponent(KEY_PRESSED_BTN)
            		+ "&currentCheckedValues="+encodeURIComponent(hiddenSelectedCellTwo)
            		+ "&rowNum="+ROW_NUM_COUNT
            		+ "&recPerPage="+DEFAULT_RECORD_VAL;
// 			console.log("url--"+url);
                        
            ajax_call_enabled = true;
            $.ajax({
                type : 'POST',
                url : url,
                data : {},
                async : false,
                cache : false,
                contentType : false,
                processData : false,
                success : function(response) {
                	ajax_call_enabled = false;
 					setTimeout(() => {
 						hideProcessIndicator();
 					}, 100);
                	
                	$("#lov_lovid").val(response.lovId);
           	  		$("#lov_post_back_cols").val(response.postBackCols);
                    $("#lov_header").html(response.header);
                    $("#instant_search").val(response.instantSearch);
                    $("#globalLovModalBody").html(response.dataList);
                  
                    on_lov_search_field_keyDown();
                    $("#globalLovModal").modal('show');
                    
                    // Lov Pagination
                    var totalRecords = $("#lovtotalrecord").val();
                	var noOfPage = totalRecords / DEFAULT_RECORD_VAL;
                	PAGE_COUNT_SEARCH = Math.ceil(noOfPage);
            		$("#pageNo").text(1);
                },
                error : function(jqXHR, textStatus, errorThrown) {
 					setTimeout(() => {
 						hideProcessIndicator();
 					}, 100);
 					console.log("errorThrown : " + errorThrown);
 					ajax_call_enabled = false; // if fails
                }
            });
        } catch (error) {
            window.alert("All lOV parameters are not available to open LOV");
            console.log(error);
            hideProcessIndicator();
        }
	}
}// End Function

function get_lov_data(defVal = "default") {
    try {
    	
    	var replacementObj;
    	if(!lhsIsNull(REPLACEMENT_FUNC)) {
    		replacementObj = eval(REPLACEMENT_FUNC);
    	}
    	
    	var value = '';
        if (replacementObj != null) {
        	var value = JSON.stringify(replacementObj);
        }

        var lov_id = document.getElementById("lov_lovid").value;
        var postbackcolstring = document.getElementById("lov_post_back_cols").value;
        var lov_search_field = document.getElementById("lov_search_field").value;
        var hiddenSelectedCellTwo = document.getElementById("hiddenSelectedCellTwo").value;
        
        if(defVal != "Pagination") {
        	ROW_NUM_COUNT = 1;
        	ROW_NUM_COUNT_SEARCH = 1;
    		$("#pageNo").text(1);
        }
        
        if(lov_search_field.length > 0 && lov_search_field != "") {
 			showProcessIndicator();
            var url = "./lovConfiguration?lovid=" + encodeURIComponent(LOV_ID)
            		+ "&searchValue=" + encodeURIComponent(lov_search_field)
		    		+ "&replaceMap=" + encodeURIComponent(value)
		    		+ "&postBackCols=" + encodeURIComponent(POST_BACK_COLS) 
		    		+ "&keyPressButton=" + encodeURIComponent(KEY_PRESSED_BTN)
		    		+ "&currentCheckedValues="+encodeURIComponent(hiddenSelectedCellTwo)
		    		+ "&rowNum="+ROW_NUM_COUNT_SEARCH
		    		+ "&recPerPage="+DEFAULT_RECORD_VAL;
            
            ajax_call_enabled = true;
            $.ajax({
                type : 'POST',
                url : url,
                async : false,
                cache : false,
                contentType : false,
                processData : false,
                success : function(response) {
                	ajax_call_enabled = false;
                	setTimeout(() => {
                		hideProcessIndicator();
                	}, 100);
                	
                    $("#globalLovModal").modal('show');
                    $("#globalLovModalBody").html(response.dataList);
                    on_lov_search_field_keyDown();
                    
                    var totalRecords = $("#lovtotalrecord").val();
                	var noOfPage = totalRecords / DEFAULT_RECORD_VAL;
                	PAGE_COUNT_SEARCH = Math.ceil(noOfPage);
                	if(ROW_NUM_COUNT_SEARCH === 1) {                		
                		$("#pageNo").text(1);
                	}
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    console.log("errorThrown : " + errorThrown);
                    ajax_call_enabled = false; // if fails
 					setTimeout(() => {
 						hideProcessIndicator();
 					}, 100);
                }
            });
        } else {
        	openLOV(null, LOV_ID, REPLACEMENT_FUNC, CALL_BACK_FUNC, POST_BACK_COLS, FOCUS_ON_RETURN_ID, "T");
        }
    } catch (error) {
        alert(error);
        window.alert("All lOV parameters are not available to open LOV");
        console.log(error);
    }
}// End Function

/**
 * *******************Lov Functionality For Next/Previous Page**************************
 */
function nextRecords() {
	var lov_search_field = document.getElementById("lov_search_field").value;
	if(lov_search_field.length > 0 && lov_search_field != "") {
		var count = Math.floor((ROW_NUM_COUNT_SEARCH + DEFAULT_RECORD_VAL) / 10);
		if(count < PAGE_COUNT_SEARCH){
			ROW_NUM_COUNT_SEARCH = ROW_NUM_COUNT_SEARCH + DEFAULT_RECORD_VAL;
			get_lov_data("Pagination");
			$("#pageNo").text(count+1);
		}
	} else {
		var count = Math.floor((ROW_NUM_COUNT + DEFAULT_RECORD_VAL) / 10);
		if(count < PAGE_COUNT_SEARCH){
			ROW_NUM_COUNT = ROW_NUM_COUNT + DEFAULT_RECORD_VAL;
			openLOV(null, LOV_ID, REPLACEMENT_FUNC, CALL_BACK_FUNC, POST_BACK_COLS, FOCUS_ON_RETURN_ID, "T", "Pagination");
			$("#pageNo").text(count+1);
		}
	}
}// End Function

function prevRecords() {
	var lov_search_field = document.getElementById("lov_search_field").value;
	if(lov_search_field.length > 0 && lov_search_field != "") {
		if(ROW_NUM_COUNT_SEARCH - 10 > 0) {
			var count = Math.floor((ROW_NUM_COUNT_SEARCH - DEFAULT_RECORD_VAL) / 10);
			if(count < PAGE_COUNT_SEARCH) {
				ROW_NUM_COUNT_SEARCH = ROW_NUM_COUNT_SEARCH - DEFAULT_RECORD_VAL;
				get_lov_data("Pagination");
				$("#pageNo").text(count + 1);
			}
		}
	} else {
		if(ROW_NUM_COUNT - 10 > 0) {
			var count = Math.floor((ROW_NUM_COUNT - DEFAULT_RECORD_VAL) / 10);
			ROW_NUM_COUNT = ROW_NUM_COUNT - DEFAULT_RECORD_VAL;
			openLOV(null, LOV_ID, REPLACEMENT_FUNC, CALL_BACK_FUNC, POST_BACK_COLS, FOCUS_ON_RETURN_ID, "T", "Pagination");
			$("#pageNo").text(count + 1);
		}
	}
}// End Function

function reset_lov_data() {
	document.getElementById("lov_search_field").value = "";

	rowNumCount = 1;
    rowNumCountSearch = 1;
    pageCount = 1;
    pageCountSearch = 1;
    
    get_lov_data();
	$("#pageNo").text(1);
	
	$("#hiddenSelectedCellTwo").val("");
    $("#hiddenSelectedCellThree").val("");
    get_lov_data();
}// End Function

function post_back_data(row,count) {
	

    var postbackcols = document.getElementById("lov_post_back_cols").value;
//    alert(postbackcols);
    var data = document.getElementById('dataId~'+count).value;

    try {
	    var all_elems_2 = postbackcols.split("#");
	    for (var i = 0; i < all_elems_2.length; i++) {
	        var lbl = $(row.cells[i]).find("label")[0];
	        try {
// 				console.log("all_elems_2["+i+"]..."+all_elems_2[i]);
// 				console.log("$(anchor).html()-->"+$(anchor).html());
	            if(!lhsIsNull(all_elems_2[i])) {
	            	if(i == 1){
	            	document.getElementById(all_elems_2[i]).value = $(lbl).html().replace(/&amp;/, "&");
	            	}else{
	            	document.getElementById(all_elems_2[i]).value = data;
	            	}
	            }
	        } catch (e) {
	            console.log(e.message);
	        }
	    }
    } catch (error) {
            console.log(error);
    }
    
    $("#globalLovModal").modal('hide');
    
    setTimeout(() => {
        try {
            if (!lhsIsNull(FOCUS_ON_RETURN_ID)) {
                document.getElementById(FOCUS_ON_RETURN_ID).focus();
            }
        } catch (err) {}
        
        try {
            if (!lhsIsNull(CALL_BACK_FUNC)) {
            	eval(CALL_BACK_FUNC);
            }
        } catch (error) {
        	console.log(error);
        }
        
        clearLovFields();
    }, 300);
}// End Function

function clearLovFields() {
    document.getElementById("lov_lovid").value = "";
    document.getElementById("lov_post_back_cols").value = "";
    document.getElementById("lov_search_field").value = "";
}// End Function

var selected_data_search = "";
function setSelectedCodes(elem) {
    try {
	    var id = elem.id.split("~");
	    var value = document.getElementById("lovrowselectable~" + id[1]).cells[1].innerHTML;
	    if (elem.checked) {
	    	selected_data_search += value + "~~";
//	    	add cell 2 and cell 3 value to the hidden Fields
	    	try {
	    		var value_cell_2 = document.getElementById("lovrowselectable~" + id[1]).cells[2].innerText;
	    		var value_cell_3 = document.getElementById("lovrowselectable~" + id[1]).cells[3].innerText;
	    		
	    		$("#hiddenSelectedCellTwo").val(($("#hiddenSelectedCellTwo").val()) + value_cell_2 + "#");
	    		$("#hiddenSelectedCellThree").val(($("#hiddenSelectedCellThree").val()) + value_cell_3 + "#");
			} catch (e) {
	           console.log("Error : " + e);
			}	    	
	    } else {
	        var newsrchval = "";
	        var ttlrcd = document.getElementById("lovtotalrecord").value;
	        for (var i = 1; i <= parseInt(ttlrcd); i++) {
                if (document.getElementById("lovrowselector~" + i).checked) {
                    var row = document.getElementById("lovrowselectable~" + i);
                    newsrchval += row.cells[1].innerHTML + "~~";
                }
                else{
                	var row = document.getElementById("lovrowselectable~" + i);
                	
                	var cell2Data = row.cells[2].innerText;
                	var cell3Data= row.cells[3].innerText;
                	var hiddenSelectedCellTwoArr = $("#hiddenSelectedCellTwo").val().split("#");
                    var hiddenSelectedCellThreeArr = $("#hiddenSelectedCellThree").val().split("#");
                    if(hiddenSelectedCellTwoArr.includes(cell2Data) && hiddenSelectedCellThreeArr.includes(cell3Data)) {
                    	hiddenSelectedCellTwoArr = hiddenSelectedCellTwoArr.filter(e => e !== cell2Data);
                    	hiddenSelectedCellThreeArr = hiddenSelectedCellThreeArr.filter(e => e !== cell3Data);
//                    	console.log("hiddenSelectedCellTwoArr to String"+ hiddenSelectedCellTwoArr.toString());
                    	$("#hiddenSelectedCellTwo").val(hiddenSelectedCellTwoArr.toString().replace(/,/g, '#'));
                    	$("#hiddenSelectedCellThree").val(hiddenSelectedCellThreeArr.toString().replace(/,/g, '#'));
                    }
                }
	        }
	        if (newsrchval.length > 2) {
                newsrchval = newsrchval.substr(0, newsrchval.length - 2);
	        }
	        selected_data_search = newsrchval;
	    }
    } catch (error) {
        console.log(error);
    }
    
    try {
		checkAllCheckboxCheckedOrNot();
	} catch (error) {
		console.log(error);
	}
    
    return selected_data_search;
}// End Function

function lovcheckall(elem) {
	$("#hiddenSelectedCellTwo").val("");
	$("#hiddenSelectedCellThree").val("");
	var status= elem.checked;
    var ttlrcd = document.getElementById("lovtotalrecord").value;
    for (var i = 1; i <= parseInt(ttlrcd); i++) {
        document.getElementById("lovrowselector~" + i).checked = status;
        try {
        	setSelectedCodes(document.getElementById("lovrowselector~" + i));	
		} catch (e) {
			console.log("Error : " + e);
		}
    }
}// End Function

function checkAllCheckboxCheckedOrNot() {
	var lovtotalrecord = parseInt($("#lovtotalrecord").val());
	var count = 0;
	
	for (var i = 1; i <= lovtotalrecord; i++) {
		if(document.getElementById("lovrowselector~" + i).checked) {
			count++;
		}	
	}
	
	if(count == lovtotalrecord) {
		$('#globalLovTableDataGrid thead tr th input:checkbox:first').prop("checked", true);
	} else {
		$('#globalLovTableDataGrid thead tr th input:checkbox:first').prop("checked", false);
	}
}// End Function

function postDataToForm() {
	var hiddenSelectedCellTwo =  $("#hiddenSelectedCellTwo").val();
	var hiddenSelectedCellThree =  $("#hiddenSelectedCellThree").val();
	
	try {
		if(hiddenSelectedCellTwo.charAt(hiddenSelectedCellTwo.length-1) == "#"){
			hiddenSelectedCellTwo = hiddenSelectedCellTwo.slice(0, -1);
		}
		if(hiddenSelectedCellThree.charAt(hiddenSelectedCellThree.length-1) == "#"){
			hiddenSelectedCellThree = hiddenSelectedCellThree.slice(0, -1); 
		}
	} catch (e) {
		console.log("Error : " + e);
	}  
	
	if(KEY_PRESSED_BTN === "F8") {
		hiddenSelectedCellTwo = hiddenSelectedCellTwo.replace(/#/g, ',');
	} else {
		hiddenSelectedCellThree = hiddenSelectedCellThree.replace(/#/g, ',');
	}
	
	$("#hiddenSelectedCellTwo").val("");
	$("#hiddenSelectedCellThree").val("");

    var postbackcols = document.getElementById("lov_post_back_cols").value;
    if (postbackcols !== null && postbackcols.length > 0) {
        var all_elems_2 = postbackcols.split("#");
        try {
        	document.getElementById(all_elems_2[1]).value = hiddenSelectedCellTwo.trim();
            document.getElementById(all_elems_2[2]).value = hiddenSelectedCellThree.trim();
        } catch (error) {
            console.log(error);
        }
    }
    
    try {
    	document.getElementById(all_elems_2[1]).style.overflow = "auto";
    } catch (error) {
    	console.log(error);
    }
    
    try {
    	if (!lhsIsNull(CALL_BACK_FUNC)) {
        	eval(CALL_BACK_FUNC);
        }
    } catch (error) {
        console.log(error);
    }
    
    clearLovFields();
    $("#globalLovModal").modal('hide');
}// End Function

function on_lov_search_field_keyDown() {
    $('#lov_search_field').on('keydown', function(event) {
        if (event.keyCode === 40) { // down-arrow
            var ele = $('#globalLovTableDataGrid tbody').find('a,input[type=checkbox]')[0];
            ele.focus();
            var tr = $(ele).parents('tr');
            var background = $(tr).css("background-color");
            $(tr).css("background-color", "#c1c1c1");
            $(tr).focusout(function() {
            	$(this).css("background-color", background);
            });
        } else {
        	return true;
        }
    });
    lhsArrowOnTable("globalLovTableDataGrid");
    setTimeout(() => {
        $('#lov_search_field').focus();
    }, 1500);
}// End Function

function get_lov_data_instant_search(event) {
    var is = document.getElementById("instant_search").value;
    if (is.match("T")) {
        var callable = check_key_pressed(event);
        if (callable) {
            get_lov_data();
        }
    }
}// End Function

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
}// End Function

$("#globalLovModal").on("hidden.bs.modal", function () {
	closeGlobalLovModal();
});// End Function

function closeGlobalLovModal() {
	$("#hiddenSelectedCellTwo").val("");
    $("#hiddenSelectedCellThree").val("");
    setTimeout(() => {
	    try {
	        if (!lhsIsNull(FOCUS_ON_RETURN_ID)) {
	        	document.getElementById(FOCUS_ON_RETURN_ID).focus();
	        }
	    } catch (err) {}
    }, 300);
}// End Function

function getPostBackColsF8(post_back_cols) {
//	alert(post_back_cols)
	if(!lhsIsNull(post_back_cols)) {
		var colArr = post_back_cols.split("#");
		colArr = colArr.swap(1, 2);
//		console.log("colArr : "+colArr);		
		return colArr.join('#');
	} else {
		return post_back_cols;
	}
}// End Function