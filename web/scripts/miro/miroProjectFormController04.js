//fix for IE8 and less missing bind function on a function
if (!Function.prototype.bind) {
	Function.prototype.bind = function(oThis) {
		if (typeof this !== 'function') {
			// closest thing possible to the ECMAScript 5
			// internal IsCallable function
			throw new TypeError('Function.prototype.bind - what is trying to be bound is not callable');
		}

		var aArgs   = Array.prototype.slice.call(arguments, 1),
				fToBind = this,
				fNOP    = function() {},
				fBound  = function() {
					return fToBind.apply(this instanceof fNOP
									? this
									: oThis,
							aArgs.concat(Array.prototype.slice.call(arguments)));
				};

		if (this.prototype) {
			// native functions don't have a prototype
			fNOP.prototype = this.prototype;
		}
		fBound.prototype = new fNOP();

		return fBound;
	};
}

MIROV10  = "4"
MIROV11  = "5"




String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}


function echeck(str) {

		var at="@"
		var dot="."
		var lat=str.indexOf(at)
		var lstr=str.length
		var ldot=str.indexOf(dot)
		if (str.indexOf(at)==-1){
		   return false
		}

		if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
		   return false
		}

		if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
		    return false
		}

		 if (str.indexOf(at,(lat+1))!=-1){
		    return false
		 }

		 if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
		    return false
		 }

		 if (str.indexOf(dot,(lat+2))==-1){
		    return false
		 }
		
		 if (str.indexOf(" ")!=-1){
		    return false
		 }

 		 return true					
	}




function errorHandler(errorString, exception) {
	
	if(errorString=="Timeout") {
		alert("Hmmm this is taking a bit longer than we expected, lets refresh the page");
		window.location.reload();
		return;	
	}

	alert("An unknown error has occurred, please report this to the Systems administrator: " + errorString);	
	
}

function sessionExpireHandler() {

	
	alert("Your session has expired, click ok to login");
	window.location.reload();
		
}
 
function timeOutHandler() {

	
	alert("Hmmm, your request is taking a long time.  Click OK to reload the page");
	window.location.reload();
		
}

 

var MiroProjectFormController = function(pid) {

  this.pid = pid;

  DWREngine.setErrorHandler(errorHandler);
  DWREngine.setTextHtmlHandler(sessionExpireHandler);
  DWREngine.setWarningHandler(timeOutHandler);
  DWREngine.setTimeout(30000); //30 secs;

}

//defining the rest of the class implmentation
MiroProjectFormController.prototype = {

  showReportDownloadDialog:function(id,survey_id) {

    var url = "miroReportShow.html"
    var v10url = url+"?version=v10&id="
    var v11url = url+"?version=v11&id="

    //this is a v10 response so you can only load the v10 survey so go do it and don't show dialog
    if(survey_id == MIROV10) {
       window.location = v10url + id;
       return  ;
    }

    $( "#miro11download" ).click(function() {
      window.location = v11url + id;
      $('#downloadreport').dialog( "close" );
    });

    $( "#miro10download" ).click(function() {
      window.location = v10url + id;
      $('#downloadreport').dialog( "close" );
    });


    $('#downloadreport').dialog({
      modal: true
    });


   },

   
    buyReportButtonClicked:function(id) {
   		
   	
   		
    	if(!confirm("By clicking \"OK\" you are agreeing to buy this report. Do you wish to continue?")) {
    		return;
   		}
   		
   		
   		
   		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 
    	var cBuyReportButtonCallBack = this.buyReportButtonCallBack.bind(this);	
  		ajaxMiroProjectManager.buyReport(id,cBuyReportButtonCallBack); 
   		
   	
   },
   
   buyReportButtonCallBack:function(data) {
   	
   	if(data.status=="5") {
   		alert("invalid report please contact your support representative") ; 
   		return;
   	}
   	
   	if(data.status=="4") {
   		if(confirm("You balance is zero! Would you like to purchase more credits?")){
   					location="addCredits.html" ;
   		}
   		return;
   	}
   	
   
   	
   	//  its ok so update balance etc.
   	//	Element.update("creditBal",data.creditBalance);
   		this.loadCandidates();
   
   	
   },
   
   addToCartButtonClicked:function(id) {
   		
   		
   		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 
    	var cAddToCartButtonCallBack = this.addToCartButtonCallBack.bind(this);	
  		ajaxMiroProjectManager.addToCart(id,cAddToCartButtonCallBack); 
   		
   	
   },
   
   addToCartButtonCallBack:function(dto) {
   	
   	
	if(dto.status != 0 ){
   			alert(dto.errorMessage)
   			return;
   	}    	
   	this.getShoppingCartItems();	
    
   	
   },
   
   getShoppingCartItems:function(){
   	
   	
   	
   		var cGetShoppingCartItemsCallBack = this.getShoppingCartItemsCallBack.bind(this);	
  		ajaxMiroProjectManager.getShoppingCartItems(cGetShoppingCartItemsCallBack); 
   	
   },
   
   getShoppingCartItemsCallBack:function(miroProjectDTOs){
   	
   		scRowCount=0;
   	
  	 	if(miroProjectDTOs==null)  {
		  	$("#shoppingCartUI").hide();
	   	   	return ; 
	   	}
   	
   		
   		if(miroProjectDTOs.length ==0)  {
		  	$("#shoppingCartUI").hide();
	   	   	return ; 
	   	}
   		
   	
   		$("#shoppingCartUI").show();
	   	dwr.util.setEscapeHtml(false);
	   	DWRUtil.removeAllRows("shoppingCartTableBody");
   		DWRUtil.addRows( "shoppingCartTableBody",miroProjectDTOs , shoppingCartCellFuncs,shoppingCartTRCRCreators);
	   
   },
   
   
   selectAllSendEmail:function(){
   		var check ="";
   		if($("#cSelectAllSendEmail").is(':checked')) {
   			check= true;
   		} else {
   			check=false;
   		}
   		
   		var checkBox="";
   		for(i=0;i<this.candidates.length;i++){
   			try {
   			checkBox = $("#cSendEmail_"+this.candidates[i].id);
        checkBox.prop( "checked", check );
   			} catch(e) {
   				
   			}
   		}
   	
   	
   },
   
      
    sendEmails:function(){
    	
    	var emailsToSend =  new Array() ;
    	
   		var checkBox="";
   		var c = 0 ;
   		
   		for(i=0;i<this.candidates.length;i++){
   			try{
   			checkBox = $("#cSendEmail_"+this.candidates[i].id);

        if(checkBox.is(':checked')) {
          emailsToSend[c++] = this.candidates[i].id+"";
        }
      } catch(e) {
   				//do nowt
   			}
   		}

		if(emailsToSend.length==0) {
			alert("Please select at least one candidate");
			return;
		}   	
   	
   		$("#sendEmailReportUI").show();
   		$("#sendEmailReportUISpinner").show();
   	
   	
   		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 
    	var cSendInviteEmailsCallBack = this.sendInviteEmailsCallBack.bind(this);	
  		ajaxMiroProjectManager.sendInviteEmails(emailsToSend,cSendInviteEmailsCallBack); 
   	
   },
   
      
   sendInviteEmailsCallBack:function(report) {
   	
   		$("#sendEmailReportUI").show();
   		$("#sendEmailReportUISpinner").hide();
   		$("#sendEmailReportUITable").show();
   		  
   		  
	   	dwr.util.setEscapeHtml(false);
	   	DWRUtil.removeAllRows("sendEmailReportTableBody");
   		DWRUtil.addRows( "sendEmailReportTableBody",report , sendMailCellFuncs);
      $("#cSelectAllSendEmail").prop( "checked", false);

   		this.selectAllSendEmail();
   	
   		this.loadCandidates();   	
   	
   },
   
   clearSendMailButtonClicked:function() {
   		$("#sendEmailReportUI").hide();
   },
   
   addNewClicked:function() {
   	
   	 $("#addNewPanel").show();
      $("#iFirstName_new").focus();
   
    },
    delCand:function(id) {
  		
  		
									
	var firstName = $("#iFirstName_"+id);
	var lastName = $("#iLastName_"+id);
	var email =  $("#iEmail_"+id);
		
	
	
	if (!confirm("Are you sure you want to delete " + firstName.val() + " " + lastName.val() +"?")){
		return false
	}
	
   		
   	$("#iSavePanel_"+id).hide();
    $("#formSpinner_"+id).show();
      	
      	
      	
   		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 
    	var cDeleteCandidateCallBack = this.deleteCandidateCallBack.bind(this);	
  		ajaxMiroProjectManager.deleteCandidate(id,cDeleteCandidateCallBack); 
    },
    
    
    deleteCandidateCallBack:function(dto) {
   	
   		$("#iSavePanel_new").show();
      	$("#formSpinner_new").hide();
   		
   	
   		if(dto.status != 0 ){
   			alert(dto.errorMessage)
   			return;
   		}    	
   	
   	
      	frm =  $("#addNewForm");
   	  	frm[0].reset();
   	  	
   	  	this.loadCandidates();
   	
   	
    },
    
    saveCandEmail:function(id) {
    	 this.saveCand(id,true); 
    	
    },
    
    saveCandOnly:function(id){
    	
    	  this.saveCand(id,false); 
    	
    },
    
    saveCand:function(id,emailme) {
  		
  		
									
	var firstName = $("#iFirstName_"+id)  ;
	var lastName = $("#iLastName_"+id) ;
	var email =  $("#iEmail_"+id);
		
	
	
	if ((firstName.val()==null)||(firstName.val().trim()=="")){
		alert("First Name is required") ; 
		firstName.focus();
		return false
	}
	
	
	
	if ((lastName.val()==null)||(lastName.val().trim()=="")){
		alert("Last Name is required") ; 
		lastName.focus();
		return false
	}
	
	if ((email.val()==null)||(email.val().trim()=="")){
		alert("Email is Required");
		email.focus();
		return false
	}
		
	if (echeck(email.val().trim())==false){
		email.focus();
		alert("Please enter a valid email address");
		return false
	}

  		
  			      
   		var dto = new Object() ;
   		frm =  $("#addNewForm");
   		dto.id=id;
   		dto.firstName = firstName.val().trim();
   		dto.lastName =   lastName.val().trim();
   		dto.emailAddress =  email.val().trim();
   		dto.project_id = this.pid;
   		
   		$("#iSavePanel_"+id).hide();
      $("#formSpinner_"+id).show();
      	
      	
      	
   		if(emailme==true){
   			var cSaveCandidateCallBack = this.saveCandidateCallBack.bind(this);	
  			ajaxMiroProjectManager.saveCandidateAndEmail(dto,cSaveCandidateCallBack);
   			
   		} else {
    		var cSaveCandidateCallBack = this.saveCandidateCallBack.bind(this);	
  			ajaxMiroProjectManager.saveCandidate(dto,cSaveCandidateCallBack);
   		} 
    },
    
    saveCandidateCallBack:function(dto) {
   	
   		$("#iSavePanel_new").show();
      $("#formSpinner_new").hide();
   		
   	
   		if(dto.status != 0 ){
   			alert(dto.errorMessage);
   		}    	
   	
   	
      	frm =  $("#addNewForm");
   	  	frm[0].reset();
   	  	
   	  	this.loadCandidates();
   	
   	
    },
    
    
    
    cancelAddNewClicked:function() {
   			
   	  frm =  $("#addNewForm");
   	  frm[0].reset();
 
      $("#addNewPanel").hide();
   
   	
  
    },
    
    
    

    
    
  
     
   loadCandidates:function() {
   	
   	
   		$("#formSpinner_cand").show();
   
   			
   
      //the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
	  //you need to bind the function before calling it 
      var cgetCandidatesCallBack = this.getCandidatesCallBack.bind(this);	
  	  ajaxMiroProjectManager.getCandidates(this.pid,cgetCandidatesCallBack);    
      
   },
   delFromCartButtonClicked:function(id) {
   	 var cDelFromCartButtonClickedCallBack = this.delFromCartButtonClickedCallBack.bind(this);	
  	  ajaxMiroProjectManager.delFromCart(id,cDelFromCartButtonClickedCallBack);    
     
   },
   
   delFromCartButtonClickedCallBack:function(dto) {
   		
   		if(dto.status != 0 ){
   			alert(dto.errorMessage)
   			return;
   		}    	
   		
   		
   		 
   		this.getShoppingCartItems();
   },
   
    getCandidatesCallBack: function(miroProjectDTOs) {
   		
   		rowCount = 0 ;
   	
   		$("#formSpinner_cand").hide();
   
   	
   	
   		if(miroProjectDTOs==null)  {
		  	this.handleAjaxErrors(miroProjectDTOs) ; 
	   	   	return ; 
	   	}
	   	
	   	if(miroProjectDTOs.length ==0)  {
		  	$("#candidatesListUI").hide();
		  	$("#welcommess").show();
	   	   	return ; 
	   	}
	   	
	   	$("#welcommess").hide();
	   	
	   	this.candidates = new Array();
	   	
	   	var candidateDTO = "";
	   	
   		this.candidates = miroProjectDTOs; 
   		
   		
   		
	    $("#candidatesListUI").show();
	   	dwr.util.setEscapeHtml(false);
	   	DWRUtil.removeAllRows("candidatesTableBody");
   		DWRUtil.addRows( "candidatesTableBody",miroProjectDTOs , candidateCellFuncs,candidateTRCRCreators);
	   	
   		
   		
   		
   		
	 	
   },
   
   handleAjaxErrors:function(miroProjectDTOs){
   	
   			//if it is null then assume we have timed out and redirect to login.
   			if(miroProjectDTOs ==null) {
   				alert("Session timed out ...") ;
   				location="mainmenu.html";
   				return ; 
   			}
   	
   			alert("oooo an error happened talking to the server!!")
   			
   			location="http://www.miro-assessment.com";
   			
   			return ; 
		
   },
   
   editCand:function(id) {
  
  	    this.toggleCandEdit(id,true);
  	     
  
   }, 
    cancelCand:function(id) {
  
  	    this.toggleCandEdit(id,false);
  	     
  
   }, 
   
   toggleCandEdit:function(id,showEdits) {
   	
   		if(showEdits) {
   		$("#cFirstName_"+id).hide();
  	    $("#cLastName_"+id).hide();
  	    $("#cEmail_"+id).hide();
  	    $("#cSendEmail_"+id).hide();
  	    $("#cEditPanel_"+id).hide();
  	     
  	    $("#iFirstName_"+id).show();
  	    $("#iLastName_"+id).show();
  	    $("#iEmail_"+id).show();
  	    $("#iSavePanel_"+id).show();
  
  	    $("iFirstName_"+id).focus()
   		} else {
   			$("#cFirstName_"+id).show();
  	    $("#cLastName_"+id).show();
  	    $("#cEmail_"+id).show();
  	    $("#cSendEmail_"+id).show();
  	    $("#cEditPanel_"+id).show();
  	     
  	    $("#iFirstName_"+id).hide();
  	    $("#iLastName_"+id).hide();
  	    $("#iEmail_"+id).hide();
  	    $("#iSavePanel_"+id).hide();
   		} 
   	
   },
   
   
   
   dummy: function() {
   	//dummy at end so we don't worry about the comma
   }
};	

var sendMailCellFuncs = [ function(reportLine) { return   reportLine ;  }];



var candidateCellFuncs = [
     
   function(cForm) { return   rowCount ;  },
    function(cForm) { if(cForm.reportStatus>"20") {
    			return "";
    } else {
    	return   "<input type=\"checkbox\" id=\"cSendEmail_" + cForm.id + "\"  />" ;  
    }
    },
    	
    	
   function(cForm) { return  getTextDiv("cFirstName_",cForm,cForm.firstName)+getInputField("iFirstName_",cForm,cForm.firstName); },
    function(cForm) { return getTextDiv("cLastName_",cForm,cForm.lastName) +getInputField("iLastName_",cForm,cForm.lastName) ;  },
   function(cForm) { return   getTextDiv("cEmail_", cForm,  cForm.emailAddress) +  getInputField("iEmail_", cForm,  cForm.emailAddress)},
   
   function(cForm) { 
   	
   	 var prefix =    "<div   id=\"cControls_" + cForm.id + "\" >" ;
   	  var suffix = "</div>" ; 
      var controls = "waiting";
      if(cForm.testComplete==true) {
      	 prefix =  "<div   id=\"cControls_" + cForm.id + "\" >" ;
      	
      } 

     //TODO add function that displays the POPUP SELECTION
   	
   	  switch (cForm.reportStatus) {
		case "0": controls = "<span class=\"status0\">email not sent</span>"; break;
		case "10": controls = "<span class=\"status10\">email sent</span>"; break;
		case "20": controls = "<span class=\"status10\">email sent</span>"; break; //make it the same as the batch will get there soon.
		case "30":  controls = "<a href=\"#\" onclick=\"mpfc.buyReportButtonClicked(" + cForm.id + ")\" /><span class=\"status30\">Buy Report</span></a>" ; break
		case "40":  controls = "<a href=\"#\" onclick=\"mpfc.showReportDownloadDialog(" + cForm.id + ","+cForm.survey_id +")\" /><span class=\"status40\">Download <img src=\"images/pdf.gif\" /></span></a>" ; break
		
		default: controls = "unknown status!!"; break; 
		;
	}
   	
   	  
      return prefix + controls + suffix;
     
    },
    function(cForm) {
    	
    	if(cForm.reportStatus>"20") {
    		return "";
    	} else {
    	return "<div  id=\"cEditPanel_" + cForm.id + "\" ><a href=\"#\" onclick=\"mpfc.editCand('" + cForm.id + "')\" >edit</a></div>" +
    			"<div  id=\"iSavePanel_" + cForm.id + "\" style=\"display:none\">" +
    			"<div><a href=\"#\" onclick=\"mpfc.saveCand('" + cForm.id + "')\" >save</a></div> " +
    			"<div><a href=\"#\" onclick=\"mpfc.delCand('" + cForm.id + "')\" >delete</a></div> " +
    			"<div><a href=\"#\" onclick=\"mpfc.cancelCand('" + cForm.id + "')\" >cancel</a></div>"+
    			"<div	id=\"formSpinner_" + cForm.id + "\" style=\"display:none\"> <img class=\"chutney\" src=\"styles/images/ajax-ball.gif\" border=\"0\" /> </div>";
    	
    	} 
    }
   

];



function getTextDiv(id,cForm,txt) {
	return "<div  id=\""+ id  + cForm.id + "\" >" +  txt + "</div>";
} 

function getInputField(id,cForm,txt) {
	return "<input id=\""+ id  + cForm.id + "\" value=\"" + txt +"\" style=\"display:none\"/>";
}


var candidateTRCRCreators = {
	rowCreator:function(candidates) {
    var row = document.createElement("tr");
    row.id = "cRow_" + candidates.rowData.id ;
    var rtype = rowCount++ % 2;
    if(rtype==0) {
    	row.className="even";
    } else {
    	row.className="odd";
    }
    return row;
  },
  cellCreator:function(options) {
    var td = document.createElement("td");   
    td.verticalAlign = "text-top";

  	
    
    td.noWrap = true;
    
    
  
    return td;
  }
}

var shoppingCartTRCRCreators = {
	rowCreator:function(candidates) {
    var row = document.createElement("tr");
    row.id = "cRow_" + candidates.rowData.id ;
    var rtype = scRowCount++ % 2;
    if(rtype==0) {
    	row.className="even";
    } else {
    	row.className="odd";
    }
    return row;
  },
  cellCreator:function(options) {
    var td = document.createElement("td");   
    td.verticalAlign = "text-top";

  	 
    td.noWrap = true;
    
    
    return td;
  }
}

var shoppingCartCellFuncs = [
     
   function(cForm) { return cForm.firstName + " "+ cForm.lastName ;  },
   
   function(cForm) { 
   	
   	 return "<a href=\"#\" onclick=\"mpfc.delFromCartButtonClicked(" + cForm.id + ")\" />del</a>" ; 
		
	
    }
   

];


 