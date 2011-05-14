 //*****************************************************
// this library contains the classes used to updated
// the UI elements on the surveys form - RGS Bluetrail 15/05/06
// depends on DWR and prototype.js 
//*****************************************************

//global variables
var gCount = 1; //the question number for the first column in questions table

var LINKME_FLASH = "<span style=\"color:red ; text-decoration: blink\">Click a Question</span>" ;
var LINKME = "link-me" ;
var LINKED = "<b>unlink-me</b>"


//a utilty class to store a cut down options so we can track changes.
var ShadowOption = Class.create();

ShadowOption.prototype = {

   initialize: function(opt) {
		this.id = opt.id ; 
		
		this.deleted = opt.deleted ;
		
		this.OText =  opt.OText ;	
		this.JQuestion_id = opt.JQuestion_id ;
				
   },
   dummy: function() {
   	//dummy at end so we don't worry about the comma
   }
}



//*****************************************************
// a UI view object to abstract updating survey UI elements
//*****************************************************
var SurveyUI = Class.create();

//defining the rest of the class implmentation
SurveyUI.prototype = {

   initialize: function() {

		
		
		this.setHides();
   		
				
				
   },
   
   showUpdatedSurvey:function() {
   	
   		alert("Saved survey");
   	
   },
   
   setHides:function() {
   	
   		var visibilityType = $F("visibility") ;
   		
   		
   		switch (visibilityType) {
  		 
 		 	case "1" :   		 	
			   Element.hide("surveyUsers");
  		 	   break;
 		 	case "2" : 
 		 	  Element.show("surveyUsers");
 		 	  break ;
 		 	case "0" :   		 	
			   Element.hide("surveyUsers");  		 	  
 		 	   break;
 		
   		}
   	
   },
   
   updateSurvey:function(questionDTO){
   	
   		BTLAssert(questionDTO.SForm, "Survey form is null!!");
   		//copys over the survey version so we are up to date with changes.
   		//this ensures that is some one changes somethign on the server we can catch it 
   		//before the ui goes potty.
  		$("sversion").value = questionDTO.SForm.version;
  		$("firstQuestion_id").value = questionDTO.SForm.firstQuestion_id;
  		
   	
   	
   },

   dummy: function() {
   	//dummy at end so we don't worry about the comma
   }
};	

//***********************************************************
// a UI view object to abstract updating the question list UI
//************************************************************
var QuestionsUI = Class.create();

//defining the rest of the class implmentation
QuestionsUI.prototype = {

   initialize: function() {
		
		//do that little red box thing in the corner when ajax running
		DWRUtil.useLoadingMessage();
			
		this.lastQuestion = null ;
		
   },
   
   
   highLightQuestion:function(questionDiv) {
   	     //does en select effect on the dropped on question   	     
   	     new Effect.Highlight(questionDiv.id, {startcolor:'#EA8E75', endcolor:'#999999'}) ; 
   },


   	getQuestionRowHtml:function(qForm){
   		
   		var htmlStr = "<tr id=\"qRow_" + qForm.id + "\">"  	    
   		htmlStr = htmlStr + this.getQuestionRowCellsHtml(qForm) + "</tr>";
   	
   		return htmlStr ;
   		
   	},
   	
	getQuestionRowCellsHtml:function(qForm){
   		
   		var htmlStr = ""  	    
   	    questionCellFuncs.each( function(cellFunc){
		   	    htmlStr = htmlStr + "<td>" + cellFunc(qForm) + "</td>" ; 
		});

   	
   		return htmlStr ;
   		
   	},
   	
   	
   	updateQuestionRow:function(qForm) {
		//this function is required as the inner html of TR in IE
		// is read only so rather than do one big string 
		//we have to do each cell.

		var colNames = new Array("qTxt" , "qshortname" , "qQtype", "qrequired", "qsticky" ) ;
		
		var colIdx = 0 ; 
  	    
   	    questionCellFuncs.each( function(cellFunc){

   	    	    if(colIdx < colNames.length  ) {
		   	    	Element.update(colNames[colIdx] +"_" + qForm.id ,  cellFunc(qForm)); 
   	    	    }
   	    	    colIdx++;
		});


	
   	 
   	
   		


   		
   	},
   	

   	showAjaxErrors:function(questionDTO) {
   		    var bigErrorMess = ""
			questionDTO.errorMessages.each( function(eMess){bigErrorMess = bigErrorMess + eMess + "\n" ; 	});
			alert(bigErrorMess);
   	},
   	
   	showUpdatedQuestion:function(questionDTO) {
   		
   		questionEditorUI.hide();			
   		this.updateQuestionRow(questionDTO.QForm) ;
   	    
   	  	
   	},
   	
   	showInsertedQuestion:function(questionDTO) {

   		questionEditorUI.hide();	
   	
   		

   		//build the html string to shove ; 
   	  	htmlStr = this.getQuestionRowHtml(questionDTO.QForm)
   	  	
   	  	var insertionPoint = "qRow_" + questionDTO.targetId ;  
		
   	  	
   		//shove em and add a draggable ; 
   		new Insertion.Before(insertionPoint,  htmlStr);   		
		//this.highLightQuestion($("qRow_"+ questionDTO.QForm.id));
						
   	},
   	
   	
   	showSavedQuestion:function(questionDTO) {
   		
   		questionEditorUI.hide();			
   		//build the html string to shove ; 
   	  	htmlStr = this.getQuestionRowHtml(questionDTO.QForm)
   	  	
   		//shove em and add a draggable ; 
   		new Insertion.Bottom("questionsTableBody",  htmlStr);
   		this.lastQuestion = questionDTO.QForm ;
	//	questionsUI.highLightQuestion(questionDiv);	
		
   			
   	},
   	
   	getLastQuestion:function(){
   		return 	this.lastQuestion ;
   	
   	},
   	
   	setLastQuestion:function(qForm){
   		this.lastQuestion = qForm ;
   	
   	},

   showAllQuestions:function(questions) {
   	
   	   BTLAssert(questions != null,"surveyFormUI.showAllQuestions");

	   if(questions == null) {
	   	  return null ;
	   }

				
		this.lastQuestion = null ;
   	
   		
		
		this.questionsInOrder = new Object();
   				
  		var nextQid = $F('firstQuestion_id') ;
		
		var question ; 

		question = questions[nextQid] ;		
   		while(question){   			 
   			if(question.deleted ==false) {
	   			this.lastQuestion = question ;
	   			this.questionsInOrder[nextQid] = question ;
   			}
	   		nextQid = question.JQuestion_id ;
			question = questions[nextQid] ;		
   		}
   		

		DWRUtil.removeAllRows("questionsTableBody");
   		DWRUtil.addRows( "questionsTableBody",this.questionsInOrder , questionCellFuncs,questionTRCRCreators);

		
		//add droppables for all the rows
		for(var rowIndex in this.questionsInOrder) { 
			 var x = this.questionsInOrder[rowIndex] ; 
			 this.addDroppable(x.id) ;				
		}

		
		
		
   },
   
   removeDeletedQuestion:function(questionDTO) {
   			
   	
   		Element.hide("qRow_" + questionDTO.targetId); 
   			
   }, 	
   
   addDroppable:function(qid) {

			//sets a droppable element to memory    	
   	
   	/*
   	  		 Droppables.add("qTxt_" + qid , 
			 {	
			  		   			   
			   onDrop: function(dragElement,dropElement) { sc.jumpLinkDroppedCallBack(dragElement,dropElement) ;}

		   	});
   	  */
   },
   


   dummy: function() {
   	//dummy at end so we don't worry about the comma
   }
};	




//***********************************************************
// a UI view object to abstract the question edit popup UI
//************************************************************
var QuestionEditorUI = Class.create();

//defining the rest of the class implmentation
QuestionEditorUI.prototype = {

   initialize:function() {
		this.optionCount = 0 ; 
		this.lastOption = null ;
		//keeps a map of the objects we are working on
		this.optionMap = null  ; //object to keep a list of the options created.	
		
		
				
   },

  linkMeConnect:function(qid,qTxt){
  	//connect an open link up
  		
  	//opens up a link to be linked if you know what I mean
  	if(this.linkType) {
		//we have an open link so carry on eugene
    } else {
  	  //nothing to link to so ignore
  	  return ; 
  	}
		  		
	 	
  	if(this.linkType =="opt") {
  		//make an option link
  		 $("oJQuestion_id_" + this.linkId).value = qid ;  		
  		 var oLinkMe = $("oLinkMe_" + this.linkId)
   		 oTxt = $("oTxt_" + this.linkId).value ; 
   		 this.setLinked(oLinkMe,oTxt) ;
  		 alert("'"+ oTxt + "' conected to '" + qTxt +"'");
  	} else {
  		 $("branch_jquestion_id").value = qid ;
   		 qTxt = $("QTxt").value ; 
   		 var qLinkMe  = $("qLinkMe") ;
		 this.setLinked(qLinkMe,qTxt);
		 
			
		 alert("'"+ qTxt + "' conected to '" + sc.questions[qid].QTxt +"'");
  	}
  	
  	//clear out the link vcariables as we have made the link
	this.linkType = null ;
	this.linkId = null  ;
	

  }, 
  setUnLinked:function(element) {
	
		 Element.update(element , LINKME)  ;
  	
  },
  
  setLinked:function(element,titletxt) {
  	 

		 Element.update(element , LINKED)  ;
   	 	 element.title = titletxt ;  	 
  	
  	
  	
  },

  linkMeDelete:function(type ,id){
  	
  	 	
  	if(type =="opt") {
  		//make an option link
  		 $("oJQuestion_id_" + id).value = "0" ; 
  		 var oLinkMe = $("oLinkMe_" + id)
   		 this.setUnLinked(oLinkMe) ;

  	} else {
  		 $("branch_jquestion_id").value = "0"
   		 var qLinkMe  = $("qLinkMe") ;
		 this.setUnLinked(qLinkMe);

  	}
  	
 	//clear out the link vcariables as we have made the link
	this.linkType = null ;
	this.linkId = null  ;
  	
  	
  	
  	
  },
  
  
   
  linkMeOpen:function(type ,id){
  	
  	
  	//opens up a link to be linked if you know what I mean
  	if(this.linkType) {
	 //clear old link 
	 if(type=="opt") {
  		//make an option link
  		 Element.update($("oLinkMe_" + id), LINKME ) ;
  	} else {
  		 Element.update($("qLinkMe") , LINKME)  ;
  	}
		  		

  	}
  	
  	if(type=="opt") {
  		//make an option link
  		 Element.update($("oLinkMe_" + id), LINKME_FLASH )  ;
  	} else {
  		 Element.update($("qLinkMe"), LINKME_FLASH) ;
  	}
  	
  	//rememebr the links
	this.linkType = type ;
	this.linkId = id  ;

  },
  
   reeditQuestion:function(questionDTO) {
	   
		this.editQuestion(questionDTO);
	
   },
  
   
   editQuestion:function(questionDTO) {
		//sets up the edit window to edit a question.
		
		this.questionDTO = questionDTO ; 
		
		//reset the form
		Form.reset($("qEditPaneForm"));
		fullFormReset($("qEditPaneForm"));
		$("QTxt").value = "";
	
  		
	
		//reset the link variables
		this.linkType = null ;
		this.linkId = null ;
			
		
		//reset counts each time a question is uploaded;	
		this.optionCount = 0 ; 
		this.lastOption = null ;
		//keeps a map of the objects we are working on
		this.optionMap = null  ; //object to keep a list of the options created.	
		this.originalOptionMap =null  ; // a shadow of the original options so we can track changes ;
		
		this.targetId = questionDTO.targetId ;  
		
		

		//copy over stuff to constraints		
		DWRUtil.setValues(questionDTO.CForm);
		//frig a couple of values in the constraint so all the fields have unique values
		if(questionDTO.CForm==null) {
			$("cid").value = "" ;
			$("cVersion").value = "" ; 
		} else {
			DWRUtil.setValues(questionDTO.CForm);
			$("cid").value = questionDTO.CForm.id ; 
			$("cVersion").value = questionDTO.CForm.version ; 
		}



  		

		//this is a DWR util that copies values from an abject to associated form elements on a document
		//see dwr doc for more info
		//copy over stuff to question - this is down last so we over right any ids that match
		DWRUtil.setValues(questionDTO.QForm);
	
		//quick patch to set the jump to 0 
		var qLinkMe  = $("qLinkMe") ;
		if($("JQuestion_id").value =="") {
	
			$("JQuestion_id").value ="0"
			$("branch_jquestion_id").value = "0" ;
	
			this.setUnLinked(qLinkMe);
		} else {
			
	
			if($("branch_jquestion_id").value != "0"){
	
				var qTxt = sc.questions[$("branch_jquestion_id").value].QTxt ;
				this.setLinked(qLinkMe,qTxt);
			} else {
	
				this.setUnLinked(qLinkMe);
			}
		}
		
		
		
		//fixes for IE
		if(questionDTO.QForm){
			$("qid").value = questionDTO.QForm.id  ; 
 			$("qversion").value = questionDTO.QForm.version  ; 
		}
		

  		

		
		//create option tables
		DWRUtil.removeAllRows("optionsTableBody"); //zap options from previous question if any.
		DWRUtil.addRows( "optionsTableBody",questionDTO.OForms , optionCellFuncs,optionTRCRCreators);


		if(questionDTO.OForms != null) {
			
			this.optionMap = new Object(); 
			this.shadowOptionMap = new Object() ; 
						
			var opt = null;
			for(var i=0;i < questionDTO.OForms.length ; i++) {
				opt = questionDTO.OForms[i] ; 
				this.optionMap[opt.id] = opt ;
				this.shadowOptionMap[opt.id] = new ShadowOption(opt) ;
				this.lastOption = opt ; //so we know where to add new options!
				this.optionCount++ ; 

				
  			}
		
			
		} 
		
		
		
		
		//from the settings set the display settings for the constraint and options
		this.setConstraintHides();
		this.setOptionHides() ;

		
		//set the display for the buttons
		this.hideButtons() ;	
		Element.show("qEditPaneDiv");
   	   
		
  		this.focus();
		
				
   },
   focus:function() {
   
   	    Field.focus("QTxt");
   
},
hideButtons:function() {
	
	
		Element.hide("saveInsertButton");   	   
  		Element.hide("updateButton");   	   
  		Element.hide("saveButton"); 
 		Element.hide("saveAndNewButton"); 
	
},

hide:function() {

		this.hideButtons() ;
       	Element.hide("qEditPaneDiv");	   		
       	
},
 
   addDraggable:function(optionForm) { 
   			//new Draggable("optDrag_" + optionForm.id ,{revert:true});
   },
   
    cancel:function()  {
    	this.hide();

    },
   
   setHides:function() {
   		this.setOptionHides() ;
   		this.setConstraintHides();
	
   },
   
   
   setConstraintHides:function(){
   		//hides and shows the controls for a constraint.
   		var optionType = $F("QType") ;
   		var constraintType = $F("CType") ;
   		
		
		var constraintDateFields = "constraintDateValues" ; 
		var constraintIntegerFields = "constraintIntegerValues" ; 
		
		
		var optionFields = "optionFieldSet"		 
   	   	var optionMetaType = "none" ; 
   	   	
   	   	switch (optionType) {
  		   		 	  		 
  		 	case "2" : 
  		 	   //date
  		 	   Element.hide(constraintIntegerFields);  		
  		 	   Element.show(constraintDateFields);
  		 	    
  		 	   optionMetaType = "date" ;
  		 	   break;
  		 	case "7":
  		 	  //decimal
  			//there are no decmial fields at the mo only integers
   		 	   Element.hide(constraintIntegerFields)
  		 	   Element.hide(constraintDateFields);
  		 	    
  		 	   optionMetaType = "number"; 
  		 	   break;
  		 	case "4":
	 		case "9":
  		 	   //number
  		 	   Element.show(constraintIntegerFields);
  		 	   Element.hide(constraintDateFields);  		 	   
  		 	   optionMetaType = "number";  
  		 	   break;
  		 	   
  		 	case "0":
  		 	case "1":
  		 	case "3":
  		 	case "5":
  		 	case "6":  	
	 		case "8":
	 		case "10":
	 		case "11":
	 		 	
	 		
  		 	   //show nothing
  		 	   Element.hide(constraintIntegerFields);
  		 	   Element.hide(constraintDateFields);  		 	 
  		 	   break;
  		 	default: 
   				BTLAssert(false, "Should not have got " + optionType);   				  		 	
   	   	}
   	   	
   	   	//set the constraint type fields for numbers
   	   	if(optionMetaType == "number"){
   	   		Element.show("constraintIntegerValues") ; 
   	   	} else {
   	   		Element.hide("constraintIntegerValues") ; 
   	   	}
   	   	
   	   	
   	   	
   	   	
   	   	
   	   	if(optionMetaType == "number" && constraintType == "7") {
   	   		Element.show("constraintIntegerValuesBetween") ; 
   	   		 	   		
   	   	} else {
   	   		Element.hide("constraintIntegerValuesBetween") ; 
   	   	}
   	   	
   	   	
   	   	//set the constraint fields for dates
   	   	if(optionMetaType == "date"){
   	   		Element.show("constraintDateValues") ; 
   	   	} else {
   	   		Element.hide("constraintDateValues") ; 
   	   	}
   	   	
   	   	if(optionMetaType == "date" && constraintType == "7") {
   	   		Element.show("constraintDateValuesBetween") ; 
   	   		 	   		
   	   	} else {
   	   		Element.hide("constraintDateValuesBetween") ; 
   	   	}
   	   	
   	   	
   	   	//finally if is not selected then hide all
   	   	if(constraintType == "0") {
   	   		Element.hide(constraintIntegerFields);
  		    Element.hide(constraintDateFields); 

  		    
   	   	}
   	   	
   	   	
   	   	
   },
   
   setOptionHides:function(){
		//hides and shows controls for a vlue of question type
		
		var optionType = $F("QType") ;
		
		var constraintFields = "constraintFieldSet" ; 
		var optionFields = "optionFieldSet"
		var metaFields = "qMetaFieldSet"
		
		switch (optionType) {
  		 
  		 	case "1" : 
  		 	   //checkbox
  		 	   Element.hide(constraintFields);
  		 	   Element.show(optionFields); 
  		 	   Element.hide(metaFields);
  		 	   break;
   			case "2" : 
   			   //date
   			   Element.show(constraintFields);
  		 	   Element.hide(optionFields);
  		 	   Element.hide(metaFields); 
   			     break;
   			case "3" :
   			case "10" :
   			     //message 
				 Element.hide(constraintFields);
  		 	   Element.hide(optionFields);
  		 	   Element.hide(metaFields); 
				 
				  break;
   			case "4" : 
   			     //number
   			     Element.show(constraintFields);
  		 	     Element.hide(optionFields);
  		 	     Element.hide(metaFields); 
				 break;
			case "9" : 
   			     //score
   			     Element.show(constraintFields);
  		 	     Element.show(optionFields);
  		 	     Element.hide(metaFields); 
				 break;
   			case "5" : 
   			     //radio
				 Element.hide(constraintFields);
  		 	   Element.show(optionFields);
  		 	   Element.hide(metaFields); 
  		 	   break;
   			case "6" : 
   			     //text
				  Element.hide(constraintFields);
  		 	   Element.hide(optionFields);
  		 	   Element.hide(metaFields); 
				 break;
   			case "7" : 
                 //decimal   			     
				     Element.show(constraintFields);
  		 	     Element.hide(optionFields);
  		 	     Element.hide(metaFields); 
			 break;
			case "8" : 
                 //percent   			     
				 Element.hide(constraintFields);
  		 	     Element.show(optionFields);
  		 	     Element.hide(metaFields); 
			 break;
			 
			case "11" : 
                 //lookup   			     
				 Element.hide(constraintFields);
  		 	     Element.hide(optionFields);
  		 	     Element.show(metaFields); 
			 break;
			 
			 
   			default: 
   				BTLAssert(false, "Should not have got " + optionType);
		}
  
   },
   
   setOptionJump:function(dragElement,questionDiv,questions) {
   	
   	    var tmp = dragElement.id.split("_") ;
   	    var optionJump = $("oJQuestion_id_"+ tmp[1]); //creates a pointer to the jump id input box 
   	    tmp = questionDiv.id.split("_") ;
   	    var qid = tmp[1];
   		optionJump.value = qid; //chop out the question no from the div id and slap it in man.
	    Element.update(dragElement ,"<a href=\"#\" class=\"morelink\" title=\"" + questions[qid].QTxt + "\">Linked</a>");   		
   },
   
   setQBranchJump:function(dragElement,questionDiv,questions) {

   	    var qBranchJump = $("branch_jquestion_id"); //creates a pointer to the jump id input box in question
   	    tmp = questionDiv.id.split("_") ;
   	    var qid = tmp[1]; 
   		qBranchJump.value = qid; //chop out the question no from the div id and slap it in man.
        Element.update(dragElement ,"<a href=\"#\" class=\"morelink\" title=\"" +  questions[qid].QTxt  + "\">Linked</a>");   		   		
   },
   
  
   	
   	
   
   
   
   addOption: function() {
   		
   		//appends a new empty option to the bottom of 
   		//the options table;
   	
   		//work ot where to shove it
   		var insertPointId ;
   		insertPointId = "optionsTableBody" ; 
   		
   		//build an empty option to shove
   		var newOptionForm = new Object() ; 
   		
   		newOptionForm.id = "new" + this.optionCount ; 
   		newOptionForm.OText = "" ; 
   		newOptionForm.JQuestion_id = "" ; 
   	
   	  	//build the html string to shove ; 
   	  	var htmlStr = "<tr id=\"oRow_" + newOptionForm.id + "\">"  	    
   	    optionCellFuncs.each( function(cellFunc){
		   	    htmlStr = htmlStr + "<td>" + cellFunc(newOptionForm) + "</td>" ; 
		});
   		htmlStr = htmlStr + "</tr>";
   	
   		//shove em and add a draggable ; 
   		new Insertion.Bottom(insertPointId,  htmlStr);
   		this.addDraggable(newOptionForm);
   		if(this.optionMap == null){
   			this.optionMap = new Object();
   		}
   		this.optionMap[newOptionForm.id] = newOptionForm ;
		this.optionCount++ ; 
   		this.lastOption = newOptionForm;
   		
   		
   		$("oTxt_" + newOptionForm.id ).focus();
   		 
   },
   
   	   	
   
     
   delOption:function(oid) {
   	
   		

   		var theOptionDeleted = $("oDeleted_"+oid) ; 
   		theOptionDeleted.value = "true" ; 
   		var theOptionRow = $("oRow_"+oid) ; 
   		this.optionCount--;

		

   	
   		//zap the whole row if it is new 
   		if(oid.indexOf("new") != -1) {   			
   			delete this.optionMap[oid] ; 
	   		Element.remove(theOptionRow);  			
  		} else {
	   		Element.hide(theOptionRow);  			
  		}
   	
   		

   		
   		   	
   },

   dummy: function() {
   	//dummy at end so we don't worry about the comma
   }
};	

//********************************************************************
// utility variables called by DWR functions to populate question table
// see http://getahead.ltd.uk/dwr/browser/tables for documentation
//********************************************************************
var questionCellFuncs = [
   
   //this routine builds the question block and shoves the options underneath.
   function(qForm) { 
   	
   	var str1 =  "<div  id=\"qTxt_" + qForm.id + "\" onclick=sc.questionLinkBackClicked('" + qForm.id + "')><div class=\"qTxtLabel\">" +  qForm.QTxt + "</div>"  ; 

	//callback tot he sc contraoller to get at the list of questionDTOs
	//where we can get the list of options if appropriate
	//but only for checklists and radios
	
	
	var questionBranchAdded = false ;
	//now check the branching 
	if(qForm.branch_jquestion_id != "0") {
	
		branchQuestion = sc.questions[qForm.branch_jquestion_id ] ;
					
		if (typeof(branchQuestion) != "undefined") {
			if(branchQuestion.deleted == false) {
				str1 = str1 + "<div class=\"optionDetail\">- Branch";
		          str1 = str1 +  " > "+  branchQuestion.shortname + "</div>"; 
	          	questionBranchAdded = true ;
			}
		}
			
	
	}
	
	
	
	//for non-options types just quit with the question details
	if(qForm.QType != "9" && qForm.QType != "8" && qForm.QType != "5" && qForm.QType != "1"){
		return str1 ;
    }	



	var options = sc.getOptionsForQuestion(qForm.id) ;

	if(!options) {
		return str1 ;
	}


	//add a separator if we have a branch
    if(questionBranchAdded ) {
    	str1 = str1 + "<div class=\"optionDetail\">-------------------</div>";
    }


    var branchQuestion ; 
    //spin through the options and append underneath 
    //if they have a branch question then add a pointer to that but only if it is exists.
	for(var i=0 ; i < options.length; i++){
		str1 = str1 + "<div class=\"optionDetail\">-" + options[i].OText 

		if(qForm.QType == "5" && options[i].JQuestion_id !="" && options[i].JQuestion_id !=null ) {
		
			branchQuestion = sc.questions[options[i].JQuestion_id] ;
			
			if (typeof(branchQuestion) != "undefined") {
				if(branchQuestion.deleted == false){
		          str1 = str1 +  " > " + branchQuestion.shortname ; 
				}
			}
			
			
		}
		str1 = str1 + "</div>"; 
	}
	
		
    //add on the closing div for the qTxt div
	return str1 + "</div>"; 
	
   	
   
   },  
     function(qForm) { 	return   "<div  id=\"qshortname_" + qForm.id + "\" >" +  qForm.shortname + "</div>" ;  },
   function(qForm) { return   "<div  id=\"qQtype_" + qForm.id + "\" >" +  sc.getMessage(qForm.QType) + "</div>" ;  },
   function(qForm) { return  "<div  id=\"qrequired_" + qForm.id + "\" >" +  sc.getMessage(qForm.required) + "</div>" ;  },
   function(qForm) { return  "<div  id=\"qsticky_" + qForm.id + "\" >" +  sc.getMessage(qForm.sticky)  + "</div>" ;  },
  function(qForm) {
    htmlStr1 = "<input class=\"qrowbutton\" type='button' value='e' onclick='sc.editButton(\"" + qForm.id + "\");'/>"; 
    htmlStr2 = "<input  class=\"qrowbutton\" type='button' value='-' onclick='sc.delButton(\"" + qForm.id + "\");'/>";
    htmlStr3 = "<input class=\"qrowbutton\" type='button' value='+' onclick='sc.insertButton(\"" + qForm.id + "\");'/>";
    return "<div style=\"display: block\">" + htmlStr1 + htmlStr2 + htmlStr3 +"</div>" ;
    
  }
];


var questionTRCRCreators = {
	rowCreator:function(options) {
    var row = document.createElement("tr");
    row.id = "qRow_" + options.rowData.id ;
    return row;
  },
  cellCreator:function(options) {
    var td = document.createElement("td");   
    td.verticalAlign = "text-top";

  	
   
   	if(options.cellNum == 0) {    	
    	td.width="500px";
    }
   
    if(options.cellNum == 5) {
    	
    	td.noWrap = true;
    }

    
  
    return td;
  }
}


//********************************************************************
// utility variables called by DWR functions to populate option table
// see http://getahead.ltd.uk/dwr/browser/tables for documentation
//THE trc FUNCS HAVE BEEN DUPLICATED IN THE ADDOPTION FUNCTIONS!!!
//********************************************************************
var optionCellFuncs = [
  
  function(oForm) { 
  			  	
		  	var htmlStr1 = "<input type=\"text\" id=\"oTxt_" + oForm.id + "\" value=\"" +oForm.OText +"\"/>";   
		  	var htmlStr2 = "<input type=\"hidden\" id=\"oJQuestion_id_" + oForm.id + "\" value=\"" +oForm.JQuestion_id +"\"/>";   
		  	var htmlStr3 = "<input type=\"hidden\" id=\"oDeleted_" + oForm.id + "\" value=\"" + oForm.deleted +"\"/>";   
		 	var htmlStr4 = "<input type=\"hidden\" id=\"oid_" + oForm.id + "\" value=\"" +   oForm.id + "\"/>";   
		 	var htmlStr5 = "<div><input type=\"hidden\" id=\"oVersion_" + oForm.id + "\" value=\"" + oForm.version+"\"/></div>";   
			



		  	return htmlStr1 + htmlStr2 + htmlStr3 + htmlStr4 + htmlStr5 ;
  },
  function(oForm) {
    
    var htmlStr2 = "<a href=\"#\" onclick='sc.delOptionButton(\"" + oForm.id + "\"); return false;'>del</a>";

	var linkField = oForm.JQuestion_id ;     
	var qTxt = "" 
	var linkText = LINKME; 
	 	 
	
	if(!linkField || linkField.value == "0" ||
  		linkField.value == "null" || linkField.value == "" ){			
			
  			
  	} else {

  		var key = eval("\"" +  oForm.JQuestion_id + "\"");
  		
  		if(sc.questions[key]) {
	  		qTxt = sc.questions[key].QTxt ;  			
	  		linkText = LINKED ; 
	  		
  		}  else {
  			//its a bad link !! so zero it and leave as a link
  			oForm.JQuestion_id = "0" ;
  			
  		}

  	}
	
		

	
    
    var htmlStr3 = "<a title=\"" + qTxt + "\" id=\"oLinkMe_"+ oForm.id + "\" href=\"#\" onclick='sc.linkMeClicked(\"opt\",\"" + oForm.id + "\"); return false;'>" + linkText + "</a>";
    
    
	//var htmlStr3 = "<span class=\"jumpDrag\" id=\"optDrag_"+ oForm.id + "\">Drag-me</span>";
    //var htmlStr3 = "<select style=\"\" id=\"oJQuestion_id_" + oForm.id + "\"> <OPTION VALUE=\"7\" />gejejej</select>"
    
    var qType = $("QType")
	if(qType.selectedIndex == 2 ){
		return htmlStr2 + " | " + htmlStr3;	
	} else {
		return htmlStr2 ;
	}
    
    
  }
];


var optionTRCRCreators = {
	rowCreator:function(options) {
    var row = document.createElement("tr");
    row.id = "oRow_" + options.rowData.id ;
    return row;
  },
  cellCreator:function(options) {
    var td = document.createElement("td");   
    td.verticalAlign = "text-top";
    if(options.cellNum == 2) {
    	td.className="right";
    }
    return td;
  }
}


//util function to loop thru form ans zap stuff.  form reset does not do hiddens as far as I can see.
function fullFormReset(frm){
	
	 var inputs = Form.getInputs(frm,"hidden") ;
	 for(var i =0 ; i < inputs.length ; i++) {
	 	inputs[i].value = "" ;
	 	
	 }
	
}

