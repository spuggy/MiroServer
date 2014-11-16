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

 

var MiroProjectFormController = Class.create();

//defining the rest of the class implmentation
MiroProjectFormController.prototype = {

   initialize:function(pid) {
		
		this.pid = pid;
		 
		DWREngine.setErrorHandler(errorHandler);
		DWREngine.setTextHtmlHandler(sessionExpireHandler);
		DWREngine.setWarningHandler(timeOutHandler);
		DWREngine.setTimeout(30000); //30 secs;
		
   }, 
   
    buyReportButtonClicked:function(id) {
   		
   	
   		
    	if(!confirm("By clicking \"OK\" you agree to purchase this assessment report and will be charged accordingly. Do you wish to continue?")) {
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
		  	Element.hide("shoppingCartUI")  
	   	   	return ; 
	   	}
   	
   		
   		if(miroProjectDTOs.length ==0)  {
		  	Element.hide("shoppingCartUI") 
	   	   	return ; 
	   	}
   		
   	
   		Element.show("shoppingCartUI");   
	   	dwr.util.setEscapeHtml(false);
	   	DWRUtil.removeAllRows("shoppingCartTableBody");
   		DWRUtil.addRows( "shoppingCartTableBody",miroProjectDTOs , shoppingCartCellFuncs,shoppingCartTRCRCreators);
	   
   },
   
   
   selectAllSendEmail:function(){
   		var check ="";
   		if($("cSelectAllSendEmail").checked) {
   			check= true;
   		} else {
   			check=false;
   		}
   		
   		var checkBox="";
   		for(i=0;i<this.candidates.length;i++){
   			try {
   			checkBox = $("cSendEmail_"+this.candidates[i].id);
   			checkBox.checked = check; 
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
   			checkBox = $("cSendEmail_"+this.candidates[i].id);
  
   			if(checkBox.checked==true) {
   				
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
   	
   		Element.show("sendEmailReportUI");
   		Element.show("sendEmailReportUISpinner");   
   	
   	
   		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 
    	var cSendInviteEmailsCallBack = this.sendInviteEmailsCallBack.bind(this);	
  		ajaxMiroProjectManager.sendInviteEmails(emailsToSend,cSendInviteEmailsCallBack); 
   	
   },
   
      
   sendInviteEmailsCallBack:function(report) {
   	
   		Element.show("sendEmailReportUI");
   		Element.hide("sendEmailReportUISpinner");   
   		Element.show("sendEmailReportUITable");   
   		  
   		  
	   	dwr.util.setEscapeHtml(false);
	   	DWRUtil.removeAllRows("sendEmailReportTableBody");
   		DWRUtil.addRows( "sendEmailReportTableBody",report , sendMailCellFuncs);
	   	
   		$("cSelectAllSendEmail").checked = false;
   		this.selectAllSendEmail();
   	
   		this.loadCandidates();   	
   	
   },
   
   clearSendMailButtonClicked:function() {
   		Element.hide("sendEmailReportUI");      	
   },
   
   addNewClicked:function() {
   	
   	 Element.show("addNewPanel")
      $("iFirstName_new").focus();
   
    },
    delCand:function(id) {
  		
  		
									
	var firstName = $("iFirstName_"+id);
	var lastName = $("iLastName_"+id);
	var email =  $("iEmail_"+id);			
		
	
	
	if (!confirm("Are you sure you want to delete " + firstName.value + " " + lastName.value +"?")){
		return false
	}
	
   		
   	Element.hide("iSavePanel_"+id);   
    Element.show("formSpinner_"+id);
      	
      	
      	
   		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 
    	var cDeleteCandidateCallBack = this.deleteCandidateCallBack.bind(this);	
  		ajaxMiroProjectManager.deleteCandidate(id,cDeleteCandidateCallBack); 
    },
    
    
    deleteCandidateCallBack:function(dto) {
   	
   		Element.show("iSavePanel_new");   
      	Element.hide("formSpinner_new");
   		
   	
   		if(dto.status != 0 ){
   			alert(dto.errorMessage)
   			return;
   		}    	
   	
   	
      	 frm =  $("addNewForm");
   	  	frm.reset();
   	  	
   	  	this.loadCandidates();
   	
   	
    },
    
    saveCandEmail:function(id) {
    	 this.saveCand(id,true); 
    	
    },
    
    saveCandOnly:function(id){
    	
    	  this.saveCand(id,false); 
    	
    },
    
    saveCand:function(id,emailme) {
  		
  		
									
	var firstName = $("iFirstName_"+id);
	var lastName = $("iLastName_"+id);
	var email =  $("iEmail_"+id);			
		
	
	
	if ((firstName.value==null)||(firstName.value.trim()=="")){
		alert("First Name is required") ; 
		firstName.focus();
		return false
	}
	
	
	
	if ((lastName.value==null)||(lastName.value.trim()=="")){
		alert("Last Name is required") ; 
		lastName.focus();
		return false
	}
	
	if ((email.value==null)||(email.value.trim()=="")){
		alert("Email is Required");
		email.focus();
		return false
	}
		
	if (echeck(email.value)==false){
		email.focus();
		alert("Please enter a valid email address");
		return false
	}

  		
  			      
   		var dto = new Object() ;
   		frm =  $("addNewForm");
   		dto.id=id;
   		dto.firstName = firstName.value.trim();
   		dto.lastName =   lastName.value.trim();
   		dto.emailAddress =  email.value.trim();
   		dto.project_id = this.pid;
   		
   		Element.hide("iSavePanel_"+id);   
      	Element.show("formSpinner_"+id);
      	
      	
      	
   		if(emailme==true){
   			var cSaveCandidateCallBack = this.saveCandidateCallBack.bind(this);	
  			ajaxMiroProjectManager.saveCandidateAndEmail(dto,cSaveCandidateCallBack);
   			
   		} else {
    		var cSaveCandidateCallBack = this.saveCandidateCallBack.bind(this);	
  			ajaxMiroProjectManager.saveCandidate(dto,cSaveCandidateCallBack);
   		} 
    },
    
    saveCandidateCallBack:function(dto) {
   	
   		Element.show("iSavePanel_new");   
      	Element.hide("formSpinner_new");
   		
   	
   		if(dto.status != 0 ){
   			alert(dto.errorMessage);
   		}    	
   	
   	
      	frm =  $("addNewForm");
   	  	frm.reset();
   	  	
   	  	this.loadCandidates();
   	
   	
    },
    
    
    
    cancelAddNewClicked:function() {
   			
   	  frm =  $("addNewForm");
   	  frm.reset();
 
      Element.hide("addNewPanel");
   
   	
  
    },
    
    
    

    
    
  
     
   loadCandidates:function() {
   	
   	
   		Element.show("formSpinner_cand");
   
   			
   
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
   	
   		Element.hide("formSpinner_cand");
   
   	
   	
   		if(miroProjectDTOs==null)  {
		  	this.handleAjaxErrors(miroProjectDTOs) ; 
	   	   	return ; 
	   	}
	   	
	   	if(miroProjectDTOs.length ==0)  {
		  	Element.hide("candidatesListUI") 
		  	Element.show("welcommess");
	   	   	return ; 
	   	}
	   	
	   	Element.hide("welcommess");
	   	
	   	this.candidates = new Array();
	   	
	   	var candidateDTO = "";
	   	
   		this.candidates = miroProjectDTOs; 
   		
   		
   		
	    Element.show("candidatesListUI");   
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
   		Element.hide("cFirstName_"+id); 
  	    Element.hide("cLastName_"+id); 
  	    Element.hide("cEmail_"+id);   
  	    Element.hide("cSendEmail_"+id);   
  	    Element.hide("cEditPanel_"+id); 
  	     
  	    Element.show("iFirstName_"+id); 
  	    Element.show("iLastName_"+id); 
  	    Element.show("iEmail_"+id);  
  	    Element.show("iSavePanel_"+id);
  
  	    $("iFirstName_"+id).focus()
   		} else {
   			Element.show("cFirstName_"+id); 
  	    Element.show("cLastName_"+id); 
  	    Element.show("cEmail_"+id);   
  	    Element.show("cSendEmail_"+id);   
  	    Element.show("cEditPanel_"+id); 
  	     
  	    Element.hide("iFirstName_"+id); 
  	    Element.hide("iLastName_"+id); 
  	    Element.hide("iEmail_"+id);  
  	    Element.hide("iSavePanel_"+id);
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
		case "40":  controls = "<a href=\"miroReportShow.html?id=" + cForm.id + "\" /><span class=\"status40\">download <img src=\"images/pdf.gif\" /></span></a>"; break; 
		
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


 