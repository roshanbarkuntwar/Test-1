/**
 * @roshan
 */

/**
 * 
 * 
 * KeyCode Value
 * 
 * enter 13 left-arrow 37 up-arrow 38 right-arrow 39 down-arrow 40 backspace 8
 * tab 9 ctrl 17 alt 18 shift 16 escape 27 space 32 page-up 33 page-down 34
 * delete 46
 * 
 * 
 * @returns
 */

function lhsTabToEnter() {
	$('body').on('keydown', 'input, a,select,button, textarea', function(event) {
		var index = 1;
		console.log("lhsTabToEnterTrigger...");
		if (event.shiftKey) {
			index = -1;
			console.log("shift key Trigger...");

		}

		// defining shortcut keys

		if (event.ctrlKey && (event.keyCode === 65)) {
			$('#address').focus();
		}

		if (checkGlobalLovModalIsVisible()) {
//			console.log("check---");
			if (event.ctrlKey && event.shiftKey) {
				console.log("lov_search_field Trigger...");
				$('#lov_search_field').focus();
				return;
			}
		}

		/*
		 * if (event.keyCode === 13) {
		 * 
		 * lhs_eventPropagation(event); var self = $(this), form =
		 * self.parents('body'), focusable, next;
		 * 
		 * focusable =
		 * form.find('input:not([readonly="readonly"]),a:not([href="javascript:void(0);"]),select,button,textarea').filter(':visible');
		 * next = focusable.eq(focusable.index(this) + index); if (next.length) {
		 * next.focus(); } else { // form.submit(); } return false; }
		 * 
		 * if (event.keyCode === 32) { var tagName = $(this).prop('tagName');
		 * 
		 * if (tagName === 'A') { var self = $(this), table =
		 * self.parents('table'); if (table != null || table !== 'undefined') {
		 * var id = table.prop("id"); if (id === 'globalLovTableDataGrid') { var
		 * tr = self.parents('tr'); tr.trigger('click'); } } } }
		 */
	});

}// End Function

function checkGlobalLovModalIsVisible() {
	var flag = $('#globalLovModal').css('display') === 'block' ? true : false;
	return flag;
}// End Function

function lhs_eventPropagation(event) {
	event.preventDefault();
	event.stopPropagation();
	event.stopImmediatePropagation();
}// End Function

function lhsArrowOnTable(table) {
	$('#' + table).on('keydown', 'input, select,a,button, textarea', function(event) {
//		console.log("ss---" + event.keyCode);

		if (checkGlobalLovModalIsVisible()) {

			if (event.ctrlKey && event.shiftKey && event.keyCode === 65) {
				console.log("Done Trigger...");
				$('#postLovDataToForm').focus();
				return;
			}

			if (event.ctrlKey && event.shiftKey) {
//				console.log("lov_search_field Trigger...");

				$('#lov_search_field').focus();
				return;
			}
		}

		if (event.keyCode === 37) {// left-arrow
			lhs_eventPropagation(event)
			lhs_leftArrow($(this));
		} else if (event.keyCode === 38) {// up-arrow
			lhs_eventPropagation(event);
			lhs_upArrow($(this));
		} else if (event.keyCode === 39) {// right-arrow
			lhs_eventPropagation(event);
			lhs_rightArrow($(this));
		} else if (event.keyCode === 40) {// down-arrow
			lhs_eventPropagation(event);
			lhs_downArrow($(this));
		} else {
			return true;
		}

	});
}// End Function

function lhs_leftArrow(field) {
	try {
		console.log("left-arrow");
		var self = $(field), form = self.parents('table'), focusable, next;
		focusable = form.find('input:not([readonly="readonly"]),a:not([href="javascript:void(0);"]),select,button,textarea').filter(':visible');
		next = focusable.eq(focusable.index(field) - 1);
		if (next.length) {
			next.focus();
//			console.log("left-arrow--focus");
			checkSelfParentIsGlobalLovOrNot(next);
		}
	} catch (err) {
		console.log("left err.." + err);
	}

}// End Function

function lhs_rightArrow(field) {
	try {
		console.log("right-arrow");
		var self = $(field), form = self.parents('table'), focusable, next;
		focusable = form.find('input:not([readonly="readonly"]),a:not([href="javascript:void(0);"]),select,button,textarea').filter(':visible');
		next = focusable.eq(focusable.index(field) + 1);
		if (next.length) {
			console.log("rigth focuse tage name..." + next.prop('id'));
			next.focus();
			console.log("right-arrow--focus");
			checkSelfParentIsGlobalLovOrNot(next);
		}
	} catch (err) {
		console.log("right err.." + err);
	}
}// End Function

function lhs_downArrow(field) {
	try {
		var self = $(field);
		var form = self.parents('tr');
		var formSib = form.next();
		var focusable;
		var next;
		var focusableSib;
		focusable = form.find('input:not([readonly="readonly"]),a:not([href="javascript:void(0);"]),select,button,textarea').filter(':visible');
		focusableSib = formSib.find('input:not([readonly="readonly"]),a:not([href="javascript:void(0);"]),select,button,textarea').filter(':visible');
		next = focusableSib.eq(focusable.index(field));
		if (next.length) {
			next.focus();
			console.log("Global....down...")
			checkSelfParentIsGlobalLovOrNot(next);
		}
	} catch (err) {

	}

}// End Function

function checkSelfParentIsGlobalLovOrNot(field) {
	var self = $(field);
	var table = self.parents('table');
	if (table != null || table !== 'undefined') {
		var id = table.prop("id");
		if (id === 'globalLovBodyTableDataGrid') {
			var tr = self.parents('tr');
			var background = $(tr).css("background-color");
			$(tr).css("background-color", "#c1c1c1");
			$(tr).focusout(function() {
				$(this).css("background-color", background);
			})
		} else if (id === "ledgerDataGridTable") {
			console.log("self---" + self);
			var tr = self.parents('tr');
			var background = $(tr).css("background-color");
			$(tr).css("background-color", "lightcoral");
			$(tr).focusout(function() {
				$(this).css("background-color", background);
			})
		}
	}
}// End Function

function lhs_upArrow(field) {
	try {
		var self = $(field);
		var form = self.parents('tr');
		var formSib = form.prev();
		var focusable;
		var next;
		var focusableSib;
		focusable = form.find('input:not([readonly="readonly"]),a:not([href="javascript:void(0);"]),select,button,textarea').filter(':visible');
		focusableSib = formSib.find('input:not([readonly="readonly"]),a:not([href="javascript:void(0);"]),select,button,textarea').filter(':visible');
		next = focusableSib.eq(focusable.index(field));
		if (next.length) {
			next.focus();
			checkSelfParentIsGlobalLovOrNot(next);
		}
	} catch (err) {

	}

}// End Function

//--------------------------------------------------------
//------------------------key shortcuts--------------------------------
//--------------------------------------------------------

//----------------------save btn CTRL+S -----------------------------------
$(document).ready(function() {
	$('body').on("keydown", function(e) {
		if ((e.keyCode == 83 && e.shiftKey)) { // && e.shiftKey+S
			$('#saveBtn').focus();
		}
	});
});
//--------------------------------------------------------
//------------------------key shortcuts--------------------------------
//--------------------------------------------------------