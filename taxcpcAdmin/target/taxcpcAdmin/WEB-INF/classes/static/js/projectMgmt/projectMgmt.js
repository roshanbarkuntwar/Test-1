
function getProjectModules() {
	var projectCode = $('#project_code').val();
	document.getElementById("module_code").disabled = false;

	if (!lhsIsNull(projectCode)) {
		
		$('#module_code').val("");
		//$('#parent_menu_code').val("");
		
	//	$('#menu_name').empty();
	//	document.getElementById("menu_name").disabled = true;
	//	$('#menu_name').append('<option value="">--Select--</option>');
		// $('#module_name').append('<option value="">--Select--</option>');
		var url = "./getProjectModuleFromProjectName?projectCode=" + projectCode;
		ajaxPostUrl(url, getProjectModulesResponse);
	} else {
		// getProjectModulesResponse();
	}
}

function getProjectModulesResponse(response) {
	var moduleName = $('#module_code');
	moduleName.empty();
	
	if (!lhsIsNull(response)) {
		
		moduleName.append('<option value="">--Select--</option>');
		$.each(response, function(k, v) {
			moduleName.append('<option value=' + k + '>' + v + '</option>');
			// console.log("key " + k + "value " + v);
		});
	} else {
		moduleName.append('<option value="">--Select--</option>');
	}
	

	
}


function getApplicationTypeName(){
	var application_type = $('#application_type').val();
	document.getElementById("architecture_type_code").disabled = false;
	if(!lhsIsNull(application_type) && application_type ==='apptype_01' ){
		$('#architecture_type_code').val("");
		 $('#frontend_type_code').multiselect('enable');

		var url = "./getArchitecture_type?application_type=" + application_type;
	
	}else if(!lhsIsNull(application_type) && application_type ==='apptype_02'){
		$('#architecture_type_code').val("");
		 $('#frontend_type_code').multiselect('disable');
		var url = "./getArchitecture_type?application_type=" + application_type;

	}else if(!lhsIsNull(application_type) && application_type ==='apptype_03'){
		$('#architecture_type_code').val("");
		 $('#frontend_type_code').multiselect('enable');

		var url = "./getArchitecture_type?application_type=" + application_type;
	
	}
	ajaxPostUrl(url, getArchitectureResponse);

}

function getArchitectureResponse(response) {
	var projectArchitectureList= $('#architecture_type_code');
	projectArchitectureList.empty();
	
	if (!lhsIsNull(response)) {
		
		projectArchitectureList.append('<option value="">--Select Architecture --</option>');
		$.each(response, function(k, v) {
			projectArchitectureList.append('<option value=' + k + '>' + v + '</option>');
			// console.log("key " + k + "value " + v);
		});
	} else {
		projectArchitectureList.append('<option value="">--Select Architecture --</option>');
	}
	

	
}


function viewMenuButton() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var menu = split_id[2]; 
		
		 var url = "./viewprojectMenuDetails?menu=" + menu;
		 ajaxPostUrl(url, viewMenuButtonResponse);
	}
}

function viewMenuButtonResponse(response) {
	if(!lhsIsNull(response)) {
		$("#workModelTableBody").html("");
		$("#workModelTableBody").html(response);
		$("#detail-modal").modal({show: true});
    } else {
    	$("#detail-modal").modal({show: true});
    }
}


function viewModuleButton() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('~');
		var module = split_id[2]; 
		
		 var url = "./viewprojectModuleDetails?module=" +module;
		 ajaxPostUrl(url, viewModuleButtonResponse);
	}
}

function viewModuleButtonResponse(response) {
	if(!lhsIsNull(response)) {
		$("#workModelTableBody").html("");
		$("#workModelTableBody").html(response);
		$("#detail-modal").modal({show: true});
    } else {
    	$("#detail-modal").modal({show: true});
    }
}

function viewButton() {
	if (!lhsIsNull(checkedId)) {
		var split_id = checkedId.split('-');
		var proj = split_id[2]; 
		 var url = "./viewprojectDetail?proj=" + proj;
		 ajaxPostUrl(url, viewButtonResponse);
	}
}

function viewButtonResponse(response) {
	if(!lhsIsNull(response)) {
		$("#workModelTableBody").html("");
		$("#workModelTableBody").html(response);
		$("#detail-modal").modal({show: true});
    } else {
    	$("#detail-modal").modal({show: true});
    }
}

function resetProjectFilter(){
	document.getElementById("module_name").value = "";
	window.location.href='./ModuleEntryForm';
}


function getProjectMenu() {
	var projectCode = $('#project_name').val();
	var moduleCode = $('#module_name').val();
	if (!lhsIsNull(projectCode)){
		$('#menu_name').append('<option value="">--Select--</option>');
		var url = "./getprojectModuleDetailsEntry?projectCode=" + projectCode+"&moduleCode="+moduleCode;
		ajaxPostUrl(url, getProjectMenuResponse);
	}else{
//		getProjectMenuResponse();
	}
}

function getProjectMenuResponse(response) {
	var menuName = $('#menu_name');
	menuName.empty();
	if (!lhsIsNull(response)){
		menuName.append('<option value="">--Select--</option>');
        $.each(response, function(k, v) {
        	menuName.append('<option value=' + k + '>'+ v +'</option>');
        });
   }else{
	   menuName.append('<option value="">--Select--</option>');
   }
}

function saveProjectDetail(){
	var project_code = document.getElementById('project_code').value

	if (validateProjectEntryForm()) {
		ajaxSubmitPostData("saveProjectDetail","projectEntryForm", saveProjectDetailResponse);
} 	 
}

function saveProjectDetailResponse(response) {
	var action = document.getElementById('action').value
	//alert("action......."+action);
    if(response === "success") {
    	if(action === "save"){
        openSuccessModal("Data saved successfully !" ,"backToProjectDashboard();");
    	}else{
    		openSuccessModal("Data Updated successfully !" ,"backToProjectDashboard();");
    	}
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function

function saveProjectModuleDetail(){
	var module_code = document.getElementById('module_code').value
	if (validateModuleEntryForm()) {
		ajaxSubmitPostData("saveProjectModuleDetail","moduleEntryForm", saveProjectModuleResponse);
	} 	 
}

function saveProjectModuleResponse(response) {
	 var action_name = $('#action').val();
	    //alert("action_name"+action_name);
    if(response === "success") {
    	if(action_name === "save"){
    		 openSuccessModal("Data Saved Successfully !" ,"backToProjectModuleDashboard();");
        	}else{
        		 openSuccessModal("Data Updated Successfully !" ,"backToProjectModuleDashboard();");    
        		 }
       
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function

function saveProjectMenu(){
	var menu_code = document.getElementById('menu_code').value
	document.getElementById("module_code").disabled = false;
	//alert("menu_code"+menu_code);
	if (validateMenuEntryForm()) {
	ajaxSubmitPostData("saveProjectMenu", "reqMenuEntryForm", saveProjectMenuResponse);
}
}

function saveProjectMenuResponse(response) {
	var action = $('#action').val();

    if(response === "success") {
    	if(action === "save"){
        openSuccessModal("Data Saved Successfully !" ,"OpenProjectMenu();");
    	}else{
            openSuccessModal("Data Update Successfully !" ,"OpenProjectMenu();");
                	}
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function



function validateProjectEntryForm(){
	
	var status;
    var project_name = $('#project_name').val();
    var project_info = $('#project_info').val();
    var app_type = $('#application_type').val();
   
    var assigned_app_type_menu = app_type.toString().replaceAll(',', '#');
   
    var arch = $('#architecture_type_code').val();
    
    var assigned_architecture_type_code = arch.toString().replaceAll(',', '#');
    
    var frontend = $('#frontend_type_code').val();
    
    var assigned_frontend_type_code = frontend.toString().replaceAll(',', '#');
    
    var backend = $('#backend_type_code').val();
    
    var assigned_backend_type_code = backend.toString().replaceAll(',', '#');
    
    var database = $('#database_type_code').val();
    
    var assigned_database_type_code = database.toString().replaceAll(',', '#');
    
    var framework = $('#framework_type_code').val();
    
    var assigned_framework_type_code = framework.toString().replaceAll(',', '#');
    
    var svn_link = $('#svn_link').val();
    
    var war_filename = $('#war_filename').val();
    
    var other_war_filename = $('#other_war_filename').val();
    
    var remark = $('#remark').val();
    
    var project_status = $('input[name="project_status"]:checked').val();
    
    var action_name = $('#action').val();
  
    if(lhsIsNull(project_name)) {
    	openErrorModal("Please Enter Project Name.");
        status = false;
    } else if(project_name.length > 50) {
    	openErrorModal("Project Name Exceeds Maximum Limit Of Characters.");
        status = false;
    }else if(lhsIsNull(project_info)) {
    	openErrorModal("Please Enter Project Information.");
        status = false;
    } else if(project_info.length > 500) {
    	openErrorModal("Project Information Exceeds Maximum Limit Of Characters.");
        status = false;
    } else if (lhsIsNull(assigned_app_type_menu)) {
		openErrorModal("Please Select Application Type.");
		status = false;
	} else if (lhsIsNull(assigned_architecture_type_code)) {
		openErrorModal("Please Select Architecture.");
		status = false;
	} else if (lhsIsNull(assigned_frontend_type_code)) {
		openErrorModal("Please Select Frontend.");
		status = false;
	} else if (lhsIsNull(assigned_backend_type_code)) {
		openErrorModal("Please Select Backend.");
		status = false;
	} else if (lhsIsNull(assigned_database_type_code)) {
		openErrorModal("Please Select Database.");
		status = false;
	} else if (lhsIsNull(assigned_framework_type_code)) {
		openErrorModal("Please Select Framework.");
		status = false;
	} else if (lhsIsNull(svn_link)) {
		openErrorModal("Please Select SVN_link.");
		status = false;
	} else if (svn_link.length > 500) {
		openErrorModal("SVN_link Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (war_filename.length > 500) {
		openErrorModal("War Filename Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (other_war_filename.length > 500) {
		openErrorModal("Other War Filename Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(remark)) {
		openErrorModal("Please Enter Remark.");
		status = false;
	}
	else if (remark.length > 2000) {
		openErrorModal("Remark Exceeds Maximum Limit Of Characters.");
		status = false;
	}else if (lhsIsNull(project_status)) {
		openErrorModal("Please Select Project Status.");
		status = false;
	} 
    else {	
        status = true;
    }
    return status;
}//End function

function validateModuleEntryForm(){
	var status;
    var module_name = $('#module_name').val();
    var remark = $('#remark').val();
    var project_name = $('#project_code').val();
    var module_status = $('input[name="module_status"]:checked').val();
    var action_name = $('#action').val();
   // alert("action_name"+action_name);
    
    if(lhsIsNull(module_name)) {
    	openErrorModal("Please Enter Module Name.");
        status = false;
    } else if(module_name.length > 100) {
    	openErrorModal("Module Name Exceeds Maximum Limit Of Characters.");
        status = false;
    }  else if(lhsIsNull(project_name)) {
    	openErrorModal("Please Enter Project Name.");
        status = false;
    }  else if (remark.length > 2000) {
		openErrorModal("Remark Exceeds Maximum Limit Of Characters.");
		status = false;
	}  else if (lhsIsNull(module_status)) {
		openErrorModal("Please Select Module Status.");
		status = false;
	} 
    else {	
        status = true;
    }
    return status;
}//End function

function validateMenuEntryForm(){
	var status;
    var menu_name = $('#menu_name').val();
    var modulename = $('#module_code').val();
    var project_name = $('#project_code').val();
    var menu_description = $('#menu_description').val();
     var menu_type = $('#menu_type').val();
    var menu_status = $('input[name="menu_status"]:checked').val();
   var action_name = $('#action').val();
   //alert("action_name"+action_name);
   
   if (lhsIsNull(project_name)) {
		openErrorModal("Please Select Project Name.");
		status = false;
	} else if(lhsIsNull(modulename)) {
    	openErrorModal("Please Enter Module Name.");
        status = false;
    } else if(lhsIsNull(menu_name)) {
    	openErrorModal("Please Enter Menu Name.");
        status = false;
    } else if(menu_name.length > 100) {
    	openErrorModal("Menu Name Exceeds Maximum Limit Of Characters.");
        status = false;
    }else if(menu_description.length > 500) {
    	openErrorModal("Menu Description Exceeds Maximum Limit Of Characters.");
        status = false;
    }else if(menu_type.length > 200) {
    	openErrorModal("Menu Type Exceeds Maximum Limit Of Characters.");
        status = false;
    }else if (lhsIsNull(menu_status)) {
		openErrorModal("Please Select Menu Status.");
		status = false;
	} 
    else {	
        status = true;
    }
    
    return status;
}//End function

function resetProjectEntryForm() {
	window.location.href='./projectEntry';
}

function backToProjectDashboard() {
	window.location.href='./projectDetail';
}

function backToProjectModuleDashboard() {
	window.location.href='./ModuleEntryForm';
}

function OpenProjectMenu(){
	window.location.href='./projectMenuDetails';
}


function resetModuleEntryForm() {
	window.location.href='./projectModuleEntry';
}

function resetMenuEntryForm() {
	window.location.href='./projectMenuDetailsEntry';
}

function ModuleEntryForm(id) {
	//alert('id....'+id);
	var module_code = document.getElementById("modulename").value;
	//alert("module_name......" +modname);
	//window.location.href='./ModuleEntryForm';
	var url = "./ModuleEntryForm?modname=" + modname;
	//alert("url.."+url);
	window.location =url;
}

function backToModuleDashboard() {
	window.location.href='./projectModuleDetail';
}

//function saveProjectModule() {
////	if (validateModuleEntryForm()) {
//	ajaxSubmitPostData("saveProjectModule",
//			"saveProjectModule", saveProjectModuleResponse);
////}
//}

function editProjectDetails(id){
	//alert("id.."+id);
	var project_code = id.split("-");
	var url = "./editprojectEntry?project_code=" + project_code[2];
	//alert("url.."+url);
	window.location =url;
	
	//window.location.href='./editprojectEntry';
}

function deleteProject(id){
	openConfirmModal("Do you want to delete ?","deleteProjectDetails('" + id + "')");
}
function deleteProjectDetails(id){
	var project_code = id.split("-");
	//var url = "/deleteprojectEntry?project_code=" + project_code[2];
//window.location =url;
	ajaxSubmitPostData("deleteprojectEntry?project_code="+ project_code[2],
			"saveProjectModule", deleteProjectResponse);
}

function deleteProjectResponse(response) {
	//alert("response.."+response);
    if(response === "success") {
        openSuccessModal("Data deleted successfully !" ,"backToProjectDashboard();");
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}//End function

function editProjectModule(id){
	//alert("id.."+id);
	var module_code = id.split("~");
	//alert("module_code.."+module_code);
	var url = "./editProjectModule?module_code=" + module_code[2];
	//alert("url.."+url);
	window.location =url;
}

function editProjectMenuDetails(id){
	// alert("id.."+id);
	var menu_code = id.split("~");
	var url = "./editProjectMenu?menu_code=" + menu_code[2];
	window.location =url;

}

function deleteModel(id){
	openConfirmModal("Do you want to delete ?","deleteProjectModule('" + id + "')");
}

function deleteProjectModule(id){
	//alert("id.."+id);
	var module_code = id.split("~");
	
	ajaxSubmitPostData("deleteprojectModuleEntry?module_code="+ module_code[2],
			"moduleEntryForm",deleteProjectModuleResponse);
}

function deleteProjectModuleResponse(response){
//	alert("response.."+response);
    if(response === "success") {
        openSuccessModal("Data deleted successfully !" ,"backToProjectModuleDashboard();");
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}

 function deleteMenu(id){
	openConfirmModal("Do you want to delete ?","deleteProjectMenuDetails('" + id + "')");
}

function deleteProjectMenuDetails(id){
	//alert("id.."+id);
	var menu_code = id.split("~");
	ajaxSubmitPostData("deleteprojectMenuEntry?menu_code="+ menu_code[2], 
			"reqMenuEntryForm",deleteProjectMenuResponse);
	
}

function deleteProjectMenuResponse(response){
	//alert("response.."+response);
    if(response === "success") {
        openSuccessModal("Data deleted successfully !" ,"OpenProjectMenu();");
    } else {
        openErrorModal("Some Error Occured..!","");
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

function editWork() {
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		editProjectDetails(checkedId);
	}
}

function deleteWork(){
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		deleteProject(checkedId);
	}
}

function editModuleButton(){
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		editProjectModule(checkedId);
	}
	
}

function deleteModuleButon(){
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		deleteModel(checkedId);
	}
}


function editMenuButton(){
	
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		editProjectMenuDetails(checkedId);
	}
	
}

function deleteMenuButton(){
	if (!lhsIsNull(checkedId)) {
		console.log("checkedId..."+checkedId);
		
		deleteMenu(checkedId);
	}
	
}

function searchProjectFilter(){
	
	var formdata = $('#moduleForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	var modname = document.getElementById("module_name").value;
	//alert("module_name......" +modname);
	var projcode = document.getElementById("project_code").value;
	//var stat = document.getElementById()
	//alert("projcode......" +projcode);
	var projstat = document.getElementById("module_status").value;
	//alert("modulestatus......" +projstat);
	//var url = "./getSearch?modname="+modname+ "&projcode=" +projcode+ "&projstat=" +projstat;
	
	
	if (lhsIsNull(modname) && lhsIsNull(projcode) && lhsIsNull(projstat)) {
        openErrorModal("Please select any filter to see the records !");
	}else{
		var url = "./getSearch?modname=" + modname+"&projcode="+projcode+"&projstat="+projstat;
		
		ajaxPostDataArray(url, fdata, searchFilterModResponse);
	}
   }

function searchFilterModResponse(response) {
	 console.log(response);
	$('#modtable').html('');
	$('#modtable').html(response);

}// end function

/*function searchProjectDetailFilter()
{
	var formdata = $('#projectForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	
	var projname = document.getElementById("project_name").value;
	//alert("project_name" +projname);
	var projtype = document.getElementById("application_type").value;
	//alert("project_type" +projtype);
	var framework = document.getElementById("project_framework").value;
	//alert("project_framework" +framework);
	var database = document.getElementById("project_database").value;
	//alert("project_database" +database);
	var status = document.getElementById("project_status").value;
	//alert("project_status" +status);
	
	if (lhsIsNull(projname) && lhsIsNull(projtype) && lhsIsNull(framework) && lhsIsNull(database) && lhsIsNull(status) ) {
        openErrorModal("Please select any filter to see the records !");
	}else{

	var actionUrl = "./getSearchProjectDetail?projname=" +projname+ "&projtype=" +projtype+ "&framework=" +framework+ "&database=" +database+ "&status=" +status;
	
	ajaxPostDataArray(actionUrl, fdata, searchFilterDocResponse);
	}
}// end function
*/

function searchProjectDetailFilter()
{
	var formdata = $('#projectForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	// console.log("Filter:"+fdata);
	
	var projname = document.getElementById("project_name").value;

	var status = document.getElementById("project_status").value;
	//alert("project_status" +status);
	
	if (lhsIsNull(projname) && lhsIsNull(status) ) {
        openErrorModal("Please select any filter to see the records !");
	}else{

	var actionUrl = "./getSearchProjectDetail?projname=" +projname+ "&status=" +status;
	
	ajaxPostDataArray(actionUrl, fdata, searchFilterDocResponse);
	}
}// end function


function searchFilterDocResponse(response) {
	 console.log(response);
	$('#projtable').html('');
	$('#projtable').html(response);

}// end function


function searchProjectMenuDetailFilter(){
	
	var formdata = $('#menuForm').serializeArray();
	var fdata = JSON.stringify(formdata);
	
	 var project_name = document.getElementById("project_name").value;

	var menuname = document.getElementById("menuName").value;
	
	// var menutype = document.getElementById("menu_type").value;
	
	var module_Name = document.getElementById("module_Name").value;
	
	var menuStatus = document.getElementById("menu_status").value;
	//alert("menuStatus" +menuStatus);
	
	if (lhsIsNull(menuname)&& lhsIsNull(menuStatus) && lhsIsNull(project_name) && lhsIsNull(module_Name) ) {
        openErrorModal("Please select any filter to see the records !");
	}else{
		
		var url = "./getSearchProjectMenuDetail?menuname=" +menuname+"&menuStatus=" +menuStatus+"&project_name="+project_name+"&module_Name="+module_Name;
		//alert("url" +url);
		
		//window.location =url;
		ajaxPostDataArray(url, fdata, searchFilterMenuResponse);
	}
	
   }

function searchFilterMenuResponse(response) {
	 console.log(response);
	$('#table').html('');
	$('#table').html(response);

}// end function


function resetProjectMenuDetailFilter(){
	$("#projectMenuDetails").find("input[type=text], textarea, select").val("");
	refreshProjectMenuDetail();
	window.location.href='./projectMenuDetails';
}

function refreshProjectMenuDetail() {
	var url = "./projectMenuDetails";
	window.location = url;
}

function resetProjectDetailFilter(){
	$("#projectDetail").find("input[type=text], textarea, select").val("");
	refreshProjectDetail();
	window.location.href='./projectDetail';
}

function refreshProjectDetail() {
	var url = "./projectDetail";
	window.location = url;
}

function onload(){
	document.getElementById("architecture_type_code").disabled = true;
	
}

