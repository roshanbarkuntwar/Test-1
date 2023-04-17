
	function OpenResetpassword(){
	//    $("#detail-modal").modal('show');
	    window.location.href ='./forgotPassword';
	}
	
	function OpenResetpassword(){
		//    $("#detail-modal").modal('show');
		    window.location.href ='./forgotPassword';
		}

	function checkEmailId(){
	
		var email = document.getElementById("email").value;
	
		var url = "./forgetPassword?email=" + email;
		ajaxPostUrl(url, checkButtonResponse);	
		
	} 	 
		
	function checkButtonResponse(response) {
		
		if(response === "success") {
			window.location.href ='./changePassword';
			//openSuccessModal1("Please Change Password !" ,"backToChangePassword();");
    	}else{
    		//openErrorModal("Email Id Does Not Exist !" ,"backToLoginPage();");
    		document.getElementById('notificationMsg').innerHTML='Please Enter Registered Email Id!';
			document.getElementById('notification').style.display = 'block';
    	}
		}
	/*
	function backToChangePassword()
	{ 
	    document.getElementById('submit_button').style.display ='none';
		document.getElementById('passwordForm').style.display ='flex';
		
	}*/
	
	function confirmPassword()
	{
		var new_password = document.getElementById("password_new").value;
	
		var confirm_password = document.getElementById("password_confirm_new").value;
		if(new_password === ""){
			document.getElementById('notificationMsg').innerHTML='Please Enter New Password!';
			document.getElementById('notification').style.display = 'block';
		}
		else if(confirm_password === ""){
			document.getElementById('notificationMsg').innerHTML='Please Enter Confirm New Password!';
			document.getElementById('notification').style.display = 'block';
		}
		else if(new_password !== confirm_password){
			
			document.getElementById('notificationMsg').innerHTML='Password Does Not Match!';
			document.getElementById('notification').style.display = 'block';
			responseClear();
		}else{
			var url = "./checkPassword?new_password=" +new_password+"&confirm_password="+confirm_password;
			ajaxPostUrl(url, passwordresponse);	
		}
		
	}
	
	function responseClear()
	{
		document.getElementById("password_new").value = "";
		document.getElementById("password_confirm_new").value = "";
		
		//window.location.href = "./changePassword"
	}
	
	function passwordresponse(response)
	{
		if(response === "success") {
			openSuccessModal1("Password changed Successfully !" ,"backToLoginPage();");
    	}else{
    		openErrorModal("Password Not Changed!" ,"backToLoginPage();");
    	}
	}
	
	function backToLoginPage()
	{
	window.location.href= "./login";
	}
	
	function openSuccessModal1(errorMessage, callBackFunction) {
	    document.getElementById("successAlertMsg1").innerHTML = errorMessage;
	    $("#successAlertForgotPassword").modal({show: true});
	    if (!lhsIsNull(callBackFunction)) {
	        $('#successBtn').attr('onClick', callBackFunction);
	    } else {
	        $('#successBtn').attr('onClick', "");
	    }
	}//end method
	
	
	function validateEmailId(email){

	var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	var status;
	if(mailformat.test(email.value))
		{
		return true;
		}else{
			document.getElementById('notificationMsg').innerHTML='Please Enter Valid Email Id.';
			document.getElementById('notification').style.display = 'block';
			//openErrorModal("Please Enter Valid Email Id.");
			email.value="";
			return false;
		}
	document.getElementById('notification').style.display = 'none';
	}
	

function validatePassword(password_new) { 
	
		//  var pw = document.getElementById("new_password").value; 
		 
		  if(password_new == "") {  
			  openErrorModal("Please Fill the password !."); 
		     status = false; 
		  }  
		   
		 //minimum password length validation  
		  else if(password_new.length < 3) {  
			  openErrorModal("Password length must be atleast 3 characters."); 
		     status = false; 
		  }  
		  
		//maximum length of password validation  
		  else if(password_new.length > 5) {  
			  openErrorModal("Password length must not exceed 5 characters.");
		     status = false;  
		  } else {
				status = true;
			}
			return status;
		  
		}  	

function validatePassword(password_confirm_new) { 
	
	//  var pw = document.getElementById("new_password").value; 
	 
	  if(password_confirm_new == "") {  
		  openErrorModal("Please Fill the password !."); 
	     status = false; 
	  }  
	   
	 //minimum password length validation  
	  else if(password_confirm_new.length < 3) {  
		  openErrorModal("Password length must be atleast 3 characters."); 
	     status = false; 
	  }  
	  
	//maximum length of password validation  
	  else if(password_confirm_new.length > 5) {  
		  openErrorModal("Password length must not exceed 5 characters.");
	     status = false;  
	  } else {
			status = true;
		}
		return status;
	  
	} 
	
function displayNone()
{
	document.getElementById('notification').style.display = 'none';
	}