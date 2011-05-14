/**
 * a controller class for the surveyrome.jsp
 * 
 * it knows about the back end ajax js and the front end UI classes 
 */
 
function errorHandler(errorString, exception) {

	//assume session has timed out
	if(errorString=="null") {
		alert("Your session has expired, click ok to login");
		location="surveys.html" ;
		return ; 
	}

	alert("An unknown error has occurred, please report this to the Systems administrator: " + errorString);	
	
}
 
 
var questionsUI ; 
var questionEditorUI ;
var surveyUI ;

var SurveyEditController = Class.create();

//defining the rest of the class implmentation
SurveyEditController.prototype = {

   initialize: function(surveyFrm) {
		
		DWREngine.setErrorHandler(errorHandler);
		 dwr.util.setEscapeHtml(false);
		
		
		//these were going to be instance variables if sc but javascript started doing weird stuff
		//so I made them global.  Not a big deal as therir is only going to be one controller.
		surveyUI = new SurveyUI() ; 
		questionsUI = new QuestionsUI() ; 
		questionEditorUI = new QuestionEditorUI() ;
				
		this.survey = this.getCurrentSurvey() ;
		this.questions = null;
		
		this.messages = new Object() ;  //messages int he right local passed down from jsp
		
		this.uiLocked = false ;
		
		this.buttonsDisabled = true;
	
		this.isEditorOpen = false;
				
		BTLAssert(this.survey != null) ;	
	
		
		
   }, disableButtons:function(){
   		
   	this.buttonsDisabled = true;
   		
   }, enableButtons:function() {
   	
   	 this.buttonsDisabled = false;
   	
   }, isButtonsDisabled:function() {
   	
   	 return this.buttonsDisabled;
   	 
   }, 
   
   visibilityChange:function() {
   	
   		
	   			
   				surveyUI.setHides() ;
  		
   		
   	   
   	
   },   
   setUiLocked:function(uiLocked) {
   	
   		this.uiLocked = uiLocked ;
   	   
   	
   },

   isUiLocked:function() {
   	
   		return this.uiLocked ;   	   
   	  
   },
   
   
   linkMeClicked:function(type,id) {
		
		
		 var linkField ; 
    
    	 if(type=="opt") {
			linkField = $("oJQuestion_id_" + id) ;  		  			

	  	} else {
		    linkField = $("branch_jquestion_id") ;
  		}		  
  		

		//if we hav an exisintg link then zap it  		
  		if(!linkField || linkField.value == "0" ||
  		    linkField.value == "null" || linkField.value == "" ){			
				questionEditorUI.linkMeOpen(type ,id);  				
  			
  		} else {
	   		questionEditorUI.linkMeDelete(type ,id);
  		}


   	
   		
   },

      
   questionLinkBackClicked:function(qid) {
   		//connects a question to a link
   	    questionEditorUI.linkMeConnect(qid,this.questions[qid].QTxt);
   },
   
   
   addMessage:function(key,message) { 
   		//function to populate message table ;
   		this.messages[key] = message ; 
   	
   },
   
   getMessage:function(key){
   	  
   	   var mess = this.messages[key] ;
   	   
   	   // you can't find the message return the key;
   	   if(mess) {
   	   	   return mess
   	   } else {
   	   	  return "$$"+key+"$$" ; 
   	   }
   	  
   },
   
   addOptionButton:function() {
   	
   		questionEditorUI.addOption() ;
   	
   },
   
	delOptionButton:function(oid){
		
		if(confirm(this.getMessage("optionForm.deletePrompt"))) {
			questionEditorUI.delOption(oid) ;				
		}

		
		
	},
	
	insertButton:function(qid) {
		
		if(this.isButtonsDisabled()) {
   			return;
   		}
		
		if(this.isEditorOpen){
   			questionEditorUI.focus();
   			return;
   		}
		
	//finally set the last question to the jump question
  		//so the server can repoint
  		var questionDTO = this.getEmptyQuestionDTO() ;

		//if the last question is null we must be appending the first question.
		questionDTO.targetId = qid ; 
  		
  		
  		  	   

  		
  		this.editType="insert" ; 
  		this.setEditPanePosition(true);		
   		questionEditorUI.editQuestion(questionDTO);					
		this.setEditButtons(this.editType); 

		this.isEditorOpen = true;
  		
   	
   },
   
   setEditButtons:function(editType){
   	
   		switch (editType) {
  		 
 		 	case "edit" : 
  		 	
			   	Element.show("updateButton");  
	 	  		Element.show("cancelButton");  	  
  		 	   break;
 		 	case "insert" : 
 		 	  Element.show("saveInsertButton");   
  	 	  		Element.show("cancelButton");  	  	
 		 	  break ;
 		 	case "append" :   		 	
		   		Element.show("saveButton"); 
		 		Element.show("saveAndNewButton"); 
	 	  		Element.show("cancelButton");  	  
	 	  		break ;
	 	  	case "first" :
	 	  		Element.show("saveButton"); 
		 		Element.show("saveAndNewButton"); 
	 	  		Element.hide("cancelButton");  	  

 		 	   break;
 		
   		}
   	
   	
   },
   
   
   qTypeChange:function() {
   		//called when the qType changes in the question editor;
   		
   		var questionDTO = this.buildQuestionDTO(true) ;
   		
   		questionEditorUI.reeditQuestion(questionDTO)	;
   		this.setEditButtons(this.editType);
   		
   			
   },
   
   cTypeChange:function() {
   		//called when the cType changes in the question editor;
   	    questionEditorUI.setHides();
   },
   
   jumpLinkDroppedCallBack:function(dragElement,questionDiv) {

	    	 
   		 if(dragElement.id=="qBranchDrag") {
	     	questionEditorUI.setQBranchJump(dragElement,questionDiv,this.questions);
	     } else {
   			questionEditorUI.setOptionJump(dragElement,questionDiv,this.questions);		
	     }
	     questionsUI.highLightQuestion(questionDiv);
   },
   
   cancelButton:function() {
   		if(this.isButtonsDisabled()) {
   			return;
   		}
   		
   		questionEditorUI.cancel();	
   		this.isEditorOpen = false;
   	 	
   },
   
   appendButton:function(){
   		
   		if(this.isButtonsDisabled()) {
   			return;
   		}
   		
   		if(this.isEditorOpen){
   			questionEditorUI.focus();
   			return;
   		}
   		
   		
   		
   		
   		//finally set the last question to the jump question
  		//so the server can repoint
  		var questionDTO = this.getEmptyQuestionDTO() ;
  		
  		  		  			
  		
  		//if the last question is null we must be appending the first question.
		if(questionsUI.getLastQuestion()==null){
			questionDTO.targetId = ""
		} else {
			questionDTO.targetId = questionsUI.lastQuestion.id ;	
			BTLAssert(questionDTO.targetId,"TargetId question is null!") ;
   		}
  		
  		
  		this.editType="append" ; 
  		this.setEditPanePosition(true);
   		questionEditorUI.editQuestion(questionDTO);					
		this.setEditButtons(this.editType); 
		
      	


   	
   },
   
   firstQuestion:function(){
   		
   		//finally set the last question to the jump question
  		//so the server can repoint
  		var questionDTO = this.getEmptyQuestionDTO() ;
  		
  		  		  			
  		
  		//if the last question is null we must be appending the first question.
		if(questionsUI.getLastQuestion()==null){
			questionDTO.targetId = ""
		} else {
			questionDTO.targetId = questionsUI.lastQuestion.id ;	
			BTLAssert(questionDTO.targetId,"TargetId question is null!") ;
   		}
  		
  		
  		this.editType="first" ; 
   		questionEditorUI.editQuestion(questionDTO);					
		this.setEditButtons(this.editType); 


   	
   },
   
   delButton:function(qid){
   		
   		if(this.isButtonsDisabled()) {
   			return;
   		}
   		
   		if(this.isEditorOpen){
   			questionEditorUI.focus();
   			return;
   		}

		if(!confirm(this.getMessage("questionForm.deletePrompt"))){
			return;
		}
		
		var questionDTO = this.getEmptyQuestionDTO() ;
  		questionDTO.QForm = new Object()
  		questionDTO.QForm.id = qid ;
  		

		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 						
		var cDelButtonCallBack = this.delButtonCallBack.bind(this);
		ajaxSurveyEditManager.deleteQuestion(questionDTO,cDelButtonCallBack);   
		this.disableButtons();			
		this.isEditorOpen = false;
   	
   },
   
   delButtonCallBack:function(questionDTO) {

		if(questionDTO==null )  {
		
   	   		this.handleAjaxErrors(questionDTO) ; 
   	   		return ; 
   	    }
   	    
   	    $('firstQuestion_id').value = questionDTO; 
		
		
		
		this.showAllQuestions();
		return;

		

		if(questionDTO==null || questionDTO.status != 1)  {
		
   	   		this.handleAjaxErrors(questionDTO) ; 
   	   		return ; 
   	    }

		
			
		surveyUI.updateSurvey(questionDTO); //updates the surveys version - important to keep track of any mods on the server
		
		
		//in this case the QForm that comes back is the lastform 
		//the id passed back is the id of the question we just deleted.
		questionsUI.lastQuestion = questionDTO.QForm;		
		
		
		//we used to delete a question but since we are displaying the links then we have to redraw the questions.
		//questionsUI.removeDeletedQuestion(questionDTO) ; 		
		
		this.deleteQuestionsInMemory(questionDTO) ;
		
		questionsUI.showAllQuestions(this.questions);	
		this.displayQuestionTable();
		this.enableButtons();
   	
   },
   
   
   buildQuestionDTO:function(copyAll){
   		
   		//the copy all variable forces this function to return oll the option
   		//if this is not specied then only changes are copied
   	
   	
   		// builds the questionDRO form the uiEditor
   		
   		var questionForm = this.getQuestionFormTemplate() ;
  		var constraintForm = this.getConstraintFormTemplate() ; 
  		var surveyForm = this.getSurveyFormTemplate() ;
  		
  		
  		
  		
  		//copy over the values from the forms	
  		DWRUtil.getValues(questionForm);
 		DWRUtil.getValues(constraintForm) ;
 		DWRUtil.getValues(surveyForm) ;
		this.timeStamp(questionForm);
		this.timeStamp(constraintForm);
		this.timeStamp(surveyForm);
  	
  		//copy over survey details,  these are named differntly on the form so we can use the 
		surveyForm.id =  $F("survey_id");
  		surveyForm.version =  $F("sversion");  		
		surveyForm.userNames = this.getUserNames() ;


		//copy over question details
  		questionForm.version = $F("qversion"); 		
  		questionForm.id = $F("qid");   		


		//copy over constraint details
  		constraintForm.id = $F("cid");
  		constraintForm.version = $F("cVersion");


  		
  		//copy to a DTO object
  		var questionDTO = new Object() ; 
  		questionDTO.QForm = questionForm ;
  		questionDTO.CForm = constraintForm;
  		questionDTO.SForm = surveyForm;
  		questionDTO.OForms = null;
  		

  		var oMap = questionEditorUI.optionMap ;
  		var oShadowMap = questionEditorUI.shadowOptionMap ;
  		
  		//build the array of options and add to the DTO
  		var optionIndex = 0 ;	
  		var liveOptionCount = 0 ; 
  		
  		var cccc = 1  ; 
  		
  		for(tmpOptionId in questionEditorUI.optionMap){
  		
  			
  		
  			if(questionDTO.OForms  ==null) {
  				questionDTO.OForms = new Array(questionEditorUI.optionCount) ; 
  			}
  			  	
  			var option = new Object() ; 
  			option.OText = $F("oTxt_" + tmpOptionId);  			
  			option.id = $F("oid_" + tmpOptionId);
  			
  			
  			//if the option id has new in the string then it is a new option
  			//and we need to wip the id out so it is save on the server as a new option
  			if(copyAll==true ) {
				
				//do nothing 
				
			} else {
				if(option.id.indexOf("new") != -1) {
  					option.id="";
  				}
  			}
			
  			
  			option.JQuestion_id = $F("oJQuestion_id_" + tmpOptionId);
  			option.question_id = questionForm.id
  			option.deleted = $F("oDeleted_" + tmpOptionId) ;
  			option.version = $F("oVersion_" + tmpOptionId) ;
  			this.timeStamp(option);

			//mark as deleted if the question is not a choice questions			  			
			if(copyAll==true ) {
				
				//do nothing 
				
			} else {
				
				if(!this.isChoiceTypeQuestion(questionDTO.QForm)) {		
					
					option.deleted = true ;
				}
				
			}

			  			
  			//keep a count of the options on the form 
  			//so we can check to see  if there is at least one option on the 
  			//server!
  			if ((option.deleted +"") != "true" && option.oText != "") {
	  					liveOptionCount ++;
	  		} 



  			
  			//only process the option if it new or changed
  			var cont = optionChanged(option,oShadowMap) ;
  			if(cont==true || copyAll==true) {
  				//after all theat only add it if they have filled something in
  				if(option.oText != "") {
  					
	  				questionDTO.OForms[optionIndex++] = option;	  				
  				} 
  			} 
  			
  		}
  		//remove any nulls
  		if(questionDTO.OForms) {
	  		questionDTO.OForms = questionDTO.OForms.compact();
  		}
  		
  		//add the number of options on the question just so we can trap 
  		//if they have entered no options
  		questionDTO.liveOptionCount = liveOptionCount;
  		
  		//copy of the form that points to this form in the linked list of questions
  		questionDTO.targetId = questionEditorUI.targetId
  		

  		
   		return questionDTO ; 
   	
   	
   }, 
   
   isChoiceTypeQuestion:function(qForm) {
   	
   		t = qForm.QType; 
   	
   		if(t == "1" || t == "5" || t == "8" || t== "9") {
   			return true ;
   		} else {
   			return false ;
   		}
   	
   },
   
   getUserNames:function() {
   	
		   	var users = $("userNames") ;
    		
    		
    		var userCount =0 ; 
    	
    		//count the users 	
    		for (var i=0; i<users.options.length; i++) {
   				 if (users.options[i].selected) {
   		
			          	userCount++;
		    	}
  			}
  			
  			
   			var userNames = new Array(userCount) ;
            
            
            userCount=0 ; 
   			//add them to the array the users 	
    		for (var v=0; v <users.options.length; v++) {
   				 if (users.options[v].selected) {

					userNames[userCount++] = users.options[v].value ; 			          	
		    	}
  			}
   		   
   		    
   		   
   			return userNames ;
   	
   },
   
   
   timeStamp:function(obj) {
   	
   	obj.created_on = $F("created_on");
	obj.lastUpdateBy_id = $F("lastUpdateBy_id");
	obj.updated_at  = $F("updated_at");
	obj.createdBy_id = $F("createdBy_id");
   
   	
   },
   
   updateButton:function() {
   	
   	if(this.isButtonsDisabled()) {
   			return;
   		}
   	
   		var questionDTO = this.buildQuestionDTO() ;
   	
   	
   		
   	
  		//post up with ajax call;
		var cUpdateButtonCallBack = this.updateButtonCallBack.bind(this);
		ajaxSurveyEditManager.updateQuestion(questionDTO,cUpdateButtonCallBack);   		
   	    
   	    this.disableButtons();	
   	
   }, 
   
   updateButtonCallBack:function(questionDTO) {

		
				
		if(questionDTO==null || questionDTO.status != 1)  {
		
			this.handleAjaxErrors(questionDTO) ; 
	   	   	return ; 
	   	}

		

		surveyUI.updateSurvey(questionDTO); //updates the surveys version - important to keep track of any mods on the server
	
		
		
		this.updateQuestionsInMemory(questionDTO)  ;
		
		
		questionsUI.showUpdatedQuestion(questionDTO) ; 								
		
   		this.isEditorOpen = false;
   	    this.enableButtons();	
   	
   },
   
   updateQuestionsInMemory:function(questionDTO) {
   	
   		
		this.questions[questionDTO.QForm.id] = questionDTO.QForm ;
   		this.questionDTOs[questionDTO.QForm.id] = questionDTO ;
	
   	
   },
   
   insertQuestionsInMemory:function(questionDTO) {
   
   
   		prevQuestion =  this.questions[questionDTO.targetId] ; 
   		
   		prevQuestion.JQuestion_id =  questionDTO.QForm.id ; 
   				
   			
   		this.questions[questionDTO.QForm.id] = questionDTO.QForm ;
   		this.questionDTOs[questionDTO.QForm.id] = questionDTO ;
	
   	
   },
   
   deleteQuestionsInMemory:function(questionDTO) {
   	
   		this.questions[questionDTO.targetId].deleted = true ; 			
   	
   },
   
   appendQuestionsInMemory:function(questionDTO) {
   	
   		lastQ = questionsUI.getLastQuestion() ; 
   		
   		//point the previous last question to the new one if it exisits
   		if(lastQ) {
   			lastQ.JQuestion_id = questionDTO.QForm.id ;
   		}
   		   			   		
   		this.questions[questionDTO.QForm.id] = questionDTO.QForm ;
   		this.questionDTOs[questionDTO.QForm.id] = questionDTO ;
		questionsUI.setLastQuestion(questionDTO.QForm) ; 
   	
   },
   
	
   publishSurveyButton:function() {
   	
   	  $("visibility").value = "1" ; //make the visibility all - this is a publish for version 2.0 
   	
   	  var questionDTO = this.buildQuestionDTO() ;
   		
   	  //post up with ajax call;
      var cPublishSurveyButtonCallBack = this.publishSurveyButtonCallBack.bind(this);
	  ajaxSurveyEditManager.publishSurvey(questionDTO,cPublishSurveyButtonCallBack);   		
   	
	
   	
   }, 
   
   publishSurveyButtonCallBack:function(questionDTO){
   	
   	    if(questionDTO==null || questionDTO.status != 1)  {
		
	   	   	this.handleAjaxErrors(questionDTO) ; 
	   	   	return ; 
	   	}

		alert("Survey Published") ; 
		
		location="surveys.html";
   	
   },
   
	


   
      
   updateSurveyButton:function() {
   	
   	  if(this.isButtonsDisabled()) {
   			return;
   		}
   		
   		if(this.isEditorOpen){
   			questionEditorUI.focus();
   			return;
   		}
   	
   	  var questionDTO = this.buildQuestionDTO() ;
   		
   	  //post up with ajax call;
      var cUpdateSurveyButtonCallBack = this.updateSurveyButtonCallBack.bind(this);
	  ajaxSurveyEditManager.updateSurvey(questionDTO,cUpdateSurveyButtonCallBack);   		
   	
		this.disableButtons();	
 
   	
   }, 
   
   updateSurveyButtonCallBack:function(questionDTO){
   	
   	    if(questionDTO==null || questionDTO.status != 1)  {
		
	   	   	this.handleAjaxErrors(questionDTO) ; 
	   	   	return ; 
	   	}

		
		surveyUI.updateSurvey(questionDTO); //updates the surveys version - important to keep track of any mods on the server
		surveyUI.showUpdatedSurvey(questionDTO) ; 								
   	
   		this.isEditorOpen = false;
   	    this.enableButtons();	
   },
   
   
   
   
   saveInsertButton:function() {
   	
   	    if(this.isButtonsDisabled()) {
   			return;
   		}
   	
   		var questionDTO = this.buildQuestionDTO() ;
   		
  		//post up with ajax call;
		var cSaveInsertButtonCallBack = this.saveInsertButtonCallBack.bind(this);
		ajaxSurveyEditManager.saveInsertQuestion(questionDTO,cSaveInsertButtonCallBack);   		
   	    this.disableButtons();	
   },
   
   saveInsertButtonCallBack:function(questionDTO) {

		
		if(questionDTO==null || questionDTO.status != 1)  {
		
	   	   	this.handleAjaxErrors(questionDTO) ; 
	   	   	return ; 
	   	}

		
		surveyUI.updateSurvey(questionDTO); //updates the surveys version - important to keep track of any mods on the server
	
		
		this.insertQuestionsInMemory(questionDTO)  ;
		this.displayQuestionTable();
		
		questionsUI.showInsertedQuestion(questionDTO) ; 
						
		this.enableButtons();	
		this.isEditorOpen = false;
   	
   },
    
    
   saveNewButton:function(again) {
   	
   	    if(this.isButtonsDisabled()) {
   			return;
   		}
   	
   		var questionDTO = this.buildQuestionDTO() ;
   		
   		//if this is true then we will display the window again
   		this.again = again ;
   		
   		
   		
  		//post up with ajax call;
		var cSaveNewButtonCallBack = this.saveNewButtonCallBack.bind(this);
		ajaxSurveyEditManager.saveNewQuestion(questionDTO,cSaveNewButtonCallBack);   		
   	    this.disableButtons();	
   	    
   	
   }, 
   
   
   saveNewButtonCallBack:function(questionDTO) {


		if(questionDTO==null || questionDTO.status != 1)  {
		
	   	   	this.handleAjaxErrors(questionDTO) ; 
	   	   	return ; 
	   	}

	
		
		surveyUI.updateSurvey(questionDTO); //updates the surveys version - important to keep track of any mods on the server
	
		this.appendQuestionsInMemory(questionDTO)  ;
		this.displayQuestionTable();
		questionsUI.showSavedQuestion(questionDTO) ; 								
		
		if(this.again) {
			this.again = true;
			this.appendButton() ;
		}
	    this.isEditorOpen = false;
	    this.enableButtons();	
   	
   },
   
      
   getEmptyQuestionDTO: function() {
 		//returns and empty DTO object to use to populate new questions  	

  		var emptyQuestionForm = this.getQuestionFormTemplate() ;
  		var emptyConstraintForm = this.getConstraintFormTemplate()
  		var options = null ; 
  		
  		var emptyDTO = new Object() ;
  		emptyDTO.qForm = emptyQuestionForm ; 
  		emptyDTO.qForm.QTxt = "" ; 
  		
  	
  		emptyDTO.cForm = null ; 
  		emptyDTO.optionForms = null ;
  		
  		
		return emptyDTO ;   	
   },
   
   getConstraintFormTemplate:function() {
   	 	var emptyConstraint = {i1:"",i2:"",d1:"",d2:"",CType:"0",cVersion:"0",cid:""  };
   		return emptyConstraint ;
   },
   
   getSurveyFormTemplate:function() {
   	 	var emptySurvey = {common:"",firstQuestion_id:"",survey_id:"",title:"",visibility:"0" ,created_on:"",created_by_id:"",lastUpdateBy_id:"",update_at:"",sversion:"",photoFile:""  };
   	 	
   	 	
   		return emptySurvey ;
   },
   
   
    getQuestionFormTemplate:function() {
   	
   		var emptyQForm = {QTxt:"",QType:"",id:"",survey_id:"",required:"", sticky:"", shortname:"",JQuestion_id:"0",qversion:"",created_on:"",lastUpdateBy_id:"",updated_at:"",createdBy_id:"",QMeta: "0" ,branch_jquestion_id:"0"};
		return emptyQForm;
   	
   },
   
 
   
   editButton: function(qid) {
		
		if(this.isButtonsDisabled()) {
   			return;
   		}
		
		if(this.isEditorOpen){
   			questionEditorUI.focus();
   			return;
   		}
		
		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 						
		var cEditButtonCallBack = this.editButtonCallBack.bind(this);
		ajaxSurveyEditManager.getQuestion(qid,cEditButtonCallBack);   
		this.disableButtons();				
   },
   
   handleAjaxErrors:function(questionDTO){
   			
   			this.enableButtons();
   	
   			//if it is null then assume we have timed out and redirect to login.
   			if(questionDTO ==null) {
   				alert("Session timed out ...") ;
   				location="surveys.html";
   				return ; 
   			}
   	
   			if(questionDTO.status != 1)  {
		
   			questionsUI.showAjaxErrors(questionDTO) ; 								
   			}
			return ; 
		
		    
		 
   },
   
   editButtonCallBack:function(questionDTO) {
   	    
   	    
   	    
   	    
   	    if(questionDTO==null || questionDTO.status != 1)  {
		
	   	   	this.handleAjaxErrors(questionDTO) ; 
	   	   	return ; 
	   	}

				
			   
  		
   	    this.editType="edit" ; 	   
   	    this.setEditPanePosition(true);		
   		questionEditorUI.editQuestion(questionDTO);					
		this.setEditButtons(this.editType); 
		
   		this.enableButtons();	
   		this.isEditorOpen = true;
  		 		
   },
   
   
   
   
   
   showAllQuestions: function() {
		
				
		//its a new survey so do nowt.
		if(this.survey.survey_id == '') {
			return ; 
		}
				
		//the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
		//you need to bind the function before calling it 
    	var cShowAllQuestionsCallBack = this.showAllQuestionsCallBack.bind(this);	
  		ajaxSurveyEditManager.getQuestions(this.survey.id,cShowAllQuestionsCallBack);    
		
		this.disableButtons();	
		
   },
   
   
   
   showAllQuestionsCallBack: function(questionDTOs) {
   	
   		if(questionDTOs==null)  {
		  	this.handleAjaxErrors(questionDTOs) ; 
	   	   	return ; 
	   	}
   		
   		//previously we used to just get a hash of questions returned
   		//now we are retuning a hash of the questionDTOs so will build old data structure
   		//to support old code.
   		
   		this.questions = new Object() ; 
   		var questionDTO ; 
   		var questionCount = 0 ; 
   		for(var q in questionDTOs) {
   			questionDTO = questionDTOs[q] ; 
   			this.questions[q] = questionDTO.QForm ; 
   			questionCount++;
   		}
   	   	
   	   	this.questionDTOs = questionDTOs;
		
		if(questionCount>0) {
			questionsUI.showAllQuestions(this.questions);	
			this.displayQuestionTable();
		} else {
			this.firstQuestion();
		}
					
		this.enableButtons();	
   },
   
   displayQuestionTable:function(){
   	
	   	
	   	Element.show("questionsEditUI");
		this.setEditPanePosition(true);
			
   	
   },
   
   setEditPanePosition:function(absolute) {

		var editDiv = $("qEditPaneDiv") ; 

		if(!absolute) {
			editDiv.style.position="relative" ; 			
			editDiv.style.top = 0 ; 
			editDiv.style.left = 0 ; 
			return ; 
		}

		editDiv.style.position="absolute" ; 			
		editDiv.style.left = "650px" ; 
   	
	   	var topOfEdit = 200 ; 
		var scrollAmount=getScrollTop() ; 
		
		
		
		//reposition config table
		//the 200 refers to the amount it should be from the top of the page



		//setup the css positioning
  		if(scrollAmount> topOfEdit) {
			editDiv.style.top = (scrollAmount)+"px";
  		 } else {
			editDiv.style.top = (topOfEdit)+"px";
  		}
		
	   	
   	
   },
   
   
   getCurrentSurvey:function() {
   		//returns current survey form
   			
   	   	var sForm = {survey_id:"",title:"",common:"",users:""};
   	   	
    	DWRUtil.getValues(sForm);
    	sForm.id = sForm.survey_id ; 
   	     	  
   	    
   	    return sForm ;   
   	
   },
   
   getOptionsForQuestion:function(id) {
   	
   	  return this.questionDTOs[id].OForms ;
   	
   },
   
   
   
   dummy: function() {
   	//dummy at end so we don't worry about the comma
   }
};	


/*
 *  Quick assert function to add a bit of defensive stuff to code 
 *  and aid debugging
 */

function BTLAssert(isTrue,mess) { 
	
	if(isTrue) {
		//do nowt cos its true ; 
	} else  {
	  alert("AssertFailed: "  + mess) ; 
	}
	
}
//quick function to see is an option has changed
function optionChanged(opt,map){
   //checks a map for old options and sees if they have changed;
   		
   		if(map) {
   			//carry on we have a map 
   		} else {
   			//we have no old options so must be new ; 
   			return true ;
   		}
   		
   		var oldOpt= map[opt.id] ; 
   		
   		if(oldOpt)  {
   			//we have found it keep checking
   		} else {
   			//must be a new option; 
   			return true ; 
   		}
   		
   		if(oldOpt.OText != opt.OText ) {
   			return true ; 
   		}

		if(oldOpt.deleted+"" != opt.deleted+"" ) {
   			return true ; 
   		}
		   		
   		if(oldOpt.JQuestion_id+"" != opt.JQuestion_id+"") {
   			return true ; 
   		}
   		
   		//nowts changed 
   		return false ;
   
   }
   
   
   //utiltiy function to get at the top of the scroll numbr
function getScrollTop() {

var ScrollTop = document.body.scrollTop;
 

if (ScrollTop == 0)

{

    if (window.pageYOffset)

        ScrollTop = window.pageYOffset;

    else

        ScrollTop = (document.body.parentElement) ? document.body.parentElement.scrollTop : 0;

}

return ScrollTop ;

}
   