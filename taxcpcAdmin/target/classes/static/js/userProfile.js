/**
 * 
 */
var counting = 0;
//var newchecked = $('input[type=checkbox]:checked').length;

function updateUserProfile() {
	if (validateUserProfileForm()) {
		ajaxSubmitPostData("userProfileUpdate",
				"userProfileForm", updateUserProfileResponse);
	}
}// End function

function updateUserProfileResponse(response) {
    if(response === "success") {
        openSuccessModal(globalUpdateMessage ,"refreshUser()");
    } else {
        openErrorModal("Some Error Occured..!","");
    }
}// End function

function refreshUser() {
	 var url = "logout";
	 window.location =url;
}

function validateUserProfileForm() {
    var status;
    var loginId = document.getElementById("loginId").value;
    var userName = document.getElementById("userName").value;
    var role_code = $('#role_code').val();
    var newPassword = document.getElementById("newPassword").value;
    var confirmNewPassword = document.getElementById("confirmNewPassword").value;
    
    if(lhsIsNull(role_code)) {
        addActionError("message","Role cannot be blank");
        status = false;
    } else if(lhsIsNull(loginId)) {
        addActionError("message","Login Id cannot be blank");
        status = false;
    } else if(lhsIsNull(userName)) {
        addActionError("message","Name cannot be blank");
        status = false;
    }  else  {	
        if(newPassword !== confirmNewPassword) {
            addActionError("message","New Password and Confirm New Password must be same");
            status = false;
            if(lhsIsNull(confirmNewPassword)){
        		addActionError("message","Please provide Confirm New Password also! ");
                status = false;
        	}
        } else {
            status = true;
        }
    }
    
    return status;
}// End function


var Count;
function Validation(id){  
	var checked = $('input[type=checkbox]:checked').length;
	var code = document.getElementsByName("menu_code");
	var sample_code;
	var Menu_code;
	
	if(checked <=5){
		for (var i = 0; i < code.length; i++) {
			if (code[i].checked) {
				 sample_code = document.getElementById("action~" + (i + 1)).innerHTML;
				 sample_code = code[i].value;
				 Menu_code +=sample_code+"#";
			     
          	   var updatedcode1=document.getElementById("code").value;
               var updatedcode=document.getElementById("newcode").value;

                   if(updatedcode !=null){
  				    Menu_code+=updatedcode; 
                   }else
                	   if(updatedcode1.length<=45){
                	   let inputs = document.getElementById(id);
     				   inputs.checked = false;   
                       openErrorModal("Add 5 Menus As favorites !","");  
                   }
                   
			      if(Menu_code.length<=79){
			    	    
			      }else{
			    	  let inputs = document.getElementById(id);
					  inputs.checked = false;
					openErrorModal("Add 5 Menus As favorites !","");  
			      }
				 localStorage.setItem('Menu_code', JSON.stringify(Menu_code));
			}
		}
	    }
	  else{
		let inputs = document.getElementById(id);
	    inputs.checked = false;
		openErrorModal("Add 5 Menus As favorites !","");	
     }
}


 

function SaveList(){ 

	var code = document.getElementsByName("menu_code");
	
    var sample_code=""
     var Menu_code = "";
    var newmenu="";
	for (var i = 0; i < code.length; i++) {
		if (code[i].checked) {
			 sample_code = document.getElementById("action~" + (i + 1)).innerHTML;
			 sample_code = code[i].value;
			 Menu_code +=sample_code+"#";
			
		}
	} 
	menulist=sample_code+sample_code;
	var updatedcode=document.getElementById("newcode").value;
	 
    Menu_code=Menu_code+updatedcode; 
	
      if(Menu_code.length<=45){
    	  var url = "dash-admin?Menu_code="+encodeURIComponent(Menu_code);
    	    window.location.href = url;	  
      }else{
		openErrorModal("Add 5 Menus As favorites !","");  
      }
	
   
		
	
}




function FindMenuList(){
	var formdata = $('#addmenuListForm').serializeArray();
	var searchvalue = document.getElementById("search_entity").value;
	var code1 = JSON.parse(localStorage.getItem('Menu_code'));
	var code2 = document.getElementById("code").value;

	if (lhsIsNull(searchvalue)) {
		openErrorModal("Please select filter to see the records !");
	} else {
		 $.ajax({
	           url: './SearchMenuList',
	           type: 'POST',
	           data: {
	        	   "searchvalue": searchvalue,
	        	   "code1": code1,
	        	   "code2": code2,
	        	  
	           },
	           success: function (response) {
	        	   entitySearchResponse(response);
	           }
	       });
		/*var url = "SearchMenuList?searchvalue="+searchvalue+"&code1="+code1;
		ajaxPostDataArray(url, formdata, entitySearchResponse);*/
	}

}	

	function entitySearchResponse(response) {
		$('#dataspan').html('');
		$('#dataspan').html(response);
	} //function end


	function getLoadbyUrl(id){
	var url =id;
	window.location.href = url;
	}
	
	
	
	
	function OpenLink(id) {
		var url=id;
		if(!lhsIsNull(url)){
		window.open(url);
		}
	}
	
	
	function getOpenMenuList(){
		 var url = "addFavMenu";
		 window.location =url;	
	}
	

	function  lastLoginDetail()
	{
		window.location.href='./lastLoginDetail';
		}

     	 
	
	


// function togglePasswordFields(fieldId, iconId) {
// var pwdField = document.getElementById(fieldId);
// var icon = document.getElementById(iconId);
//
// try {
// if (pwdField.type === "password") {
// pwdField.type = "text";
// icon.className = "fa fa-eye-slash";
// icon.title = "Hide Password";
// } else {
// pwdField.type = "password";
// icon.className = "fa fa-eye";
// icon.title = "Show Password";
// }
// } catch (err) {
// console.log(err);
// }
// }

	
