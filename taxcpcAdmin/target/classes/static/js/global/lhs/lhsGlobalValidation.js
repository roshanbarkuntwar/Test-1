/**
 * 
 */

function lhsIsNull(value) {
	try {
		value = value.trim();
	} catch (err) {
		value = value;
	}
	if (value !== "" && value !== "null" && value !== null
			&& value !== undefined && value !== 'undefined')
		return false;
	else
		return true;
}// end lhs_isNull function
//
function lhsParseInt(value) {
    try {
	value = value.trim();
    } catch (err) {
	value = value;
    }
    return !lhsIsNull(value)?parseInt(value):0.0;
}// end lhs_isNull function

function lhsParseFloat(value) {
    try {
	value = value.trim();
    } catch (err) {
	value = value;
    }
    return (!lhsIsNull(value)?parseFloat(value):0.0);
}// end lhs_isNull function

function lhsParseFloatFixed(value, fixedNo) {
    try {
	value = value.trim();
    } catch (err) {
	value = value;
    }
    var fixedValue=0.0;
    var doubleValue= lhsParseFloat(value);
	    
    if(!lhsIsNull(fixedNo)){
	fixedValue=doubleValue.toFixed(fixedNo);
    }
    return fixedValue;
}// end lhs_isNull function

function lhsValidateAlphanumeric(event) {
	var inputValue = event.which;
	if (!(inputValue >= 65 && inputValue <= 90)
			&& !(inputValue >= 97 && inputValue <= 122)
			&& (inputValue != 32 && inputValue != 0)
			&& !(inputValue >= 48 && inputValue <= 57)
			&& (inputValue != 47 && inputValue != 45)) {

		event.preventDefault();
	}
}// end lhs_validateAlphanumeric method

function lhsValidateAlphaNumericWithoutSpclChars(event) {
	var browser = checkUserBrowser();
	var inputValue = event.which;
	var keyCode = event.keyCode;
	console.log("inputValue.." + inputValue);
	console.log("keyCode.." + keyCode);
	// allow letters, numbers and whitespaces,'/','-' only.
	if (!(inputValue >= 65 && inputValue <= 90)
			&& !(inputValue >= 97 && inputValue <= 122)
			&& (inputValue != 32 && inputValue != 0)
			&& !(inputValue >= 48 && inputValue <= 57)) {
		if (browser === 'mozilla') {
			var keyCode = event.keyCode;
			if (inputValue === 46 || (keyCode >= 37 && keyCode <= 40)
					|| inputValue === 8) {

			} else {
				event.preventDefault();
			}
		} else {
			// if (charCode === 46 || (charCode >= 37 && charCode <= 40)) {
			if (inputValue === 46 || inputValue === 8) {

			} else {
				event.preventDefault();
			}
		}

	}
}// end lhs_validateAlphaNumericWithoutSpclChars method

function lhsValidateAlphabet(event) {
	var inputValue = event.which;
	if ((inputValue > 64 && inputValue < 91)
			|| (inputValue > 96 && inputValue < 123) || inputValue == 8
			|| inputValue == 32) {
	} else {
		var browser = checkUserBrowser();
		if (browser === 'mozilla') {
			var keyCode = event.keyCode;
			if (inputValue === 46 || (keyCode >= 37 && keyCode <= 40)
					|| inputValue === 8) {
			} else {
				event.preventDefault();
			}
		} else {
			event.preventDefault();
		}
	}
}// end lhs_validateAlphaNumericWithoutSpclChars method

function lhsIsNumericValue(value) {
    return !isNaN(value);
}// end lhsIsNumericValue

function lhsIsNumber(evt) {
	var result = false;
	var browser = checkUserBrowser();
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	var target = evt.target || evt.srcElement;
	if (browser === 'mozilla') {
		var keyCode = evt.keyCode;

		var num = (charCode >= 48 && charCode <= 57);
		var other = ((keyCode >= 37 && keyCode <= 40) || (keyCode === 8 || keyCode === 46));
		if (num) {
			result = true;

		} else if (other) {
			result = true;

		} else {
			result = false;
		}
	} else {
		if (charCode > 31 && (charCode < 48 || charCode > 57)) {
			result = false;
		} else {
			result = true;
		}
	}
	return result;

}// end lhs_isNumber method

function isNumberWithDecimal(evt) {
	var browser = checkUserBrowser();
	evt = (evt) ? evt : window.event;

	var charCode = (evt.which) ? evt.which : evt.keyCode;
	// var target = evt.target || evt.srcElement;

	// alert("which--" + evt.which + "\evt.keyCode--" + evt.keyCode);
	if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		if (browser === 'mozilla') {
			var keyCode = evt.keyCode;
			if (charCode === 46 || (keyCode >= 37 && keyCode <= 40)) {
				return true;
			} else {
				return false;
			}
		} else {
			// if (charCode === 46 || (charCode >= 37 && charCode <= 40)) {
			if (charCode === 46) {
				return true;
			} else {
				return false;
			}
		}
	} else {
		return true;
	}
}// end lhs_isNumber method

function checkUserBrowser() {
	// Opera 8.0+
	var isOpera = (!!window.opr && !!opr.addons) || !!window.opera
			|| navigator.userAgent.indexOf(' OPR/') >= 0;

	// Firefox 1.0+
	var isFirefox = typeof InstallTrigger !== 'undefined';

	// Safari 3.0+ "[object HTMLElementConstructor]"
	var isSafari = /constructor/i.test(window.HTMLElement) || (function(p) {
		return p.toString() === "[object SafariRemoteNotification]";
	})(!window['safari'] || safari.pushNotification);

	// Internet Explorer 6-11
	var isIE = /* @cc_on!@ */false || !!document.documentMode;

	// Edge 20+
	var isEdge = !isIE && !!window.StyleMedia;

	// Chrome 1+
	var isChrome = !!window.chrome && !!window.chrome.webstore;

	// Blink engine detection
	var isBlink = (isChrome || isOpera) && !!window.CSS;

	var type = "";

	if (isOpera) {
		type = "opera";
	} else if (isFirefox) {
		type = "mozilla";
	} else if (isSafari) {
		type = "safari";
	} else if (isIE) {
		type = "ie";
	} else if (isEdge) {
		type = "edge";
	} else if (isChrome) {
		type = "crome";
	} else if (isBlink) {
		type = "blink";
	}
	return type;

}
/**
 * onkeypress="return lhsIsPercentWithDecimal(event, this.id);"
 * 
 * @param evt
 * @param id
 * @returns
 */
function lhsIsPercentWithDecimal(evt, id) {
	var status = true;
	var idValue = document.getElementById(id).value; 
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	var target = evt.target || evt.srcElement;
	if (charCode == 46) {
		// Check if the text already contains the . character
		if (idValue.indexOf('.') === -1) {
			status = true;
		} else {
			status = false;
		}
	}

	if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode != 46) {
		status = false;
	}

	if (status == true) {		
		var idValue_1 = idValue + String.fromCharCode(evt.which);
		if (idValue_1 > 100) {
			status = false;
		}
	}
	return status;

}// end Function

function AvoidSpace(event) {
    var k = event ? event.which : window.event.keyCode;
    if (k == 32)
        return false;
}