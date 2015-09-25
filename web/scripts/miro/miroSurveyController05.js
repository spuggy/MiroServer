var SEP = '#';
var MIRO_OK = "1";
var MIRO_EXCEPTION = "2";
var MIRO_DUPE = "3";
var MIRO_INVALID = "4";


function errorHandler(errorString, exception) {

  //assume session has timed out
  if (errorString == "null") {
    alert("Your session has expired, click ok to login");
    location = "mainmenu.html";
    return;
  }

  alert("An unknown error has occurred, please report this to the Systems administrator: " + errorString);

}


var MiroSurveyController = function(sid, firstQid) {

  this.sid = sid;
  this.firstQid = firstQid;

  DWREngine.setErrorHandler(errorHandler);

}

//defining the rest of the class implmentation
MiroSurveyController.prototype = {


  startButton: function () {
    Element.hide("miroInvalid");
    Element.hide("intro");
    Element.show("loading");
    this.getQuestions();


  },
  finishButton: function () {


    if (!this.isValid()) {
      this.showError("Please select a value");
      return;
    }


    this.currentQuestionDTO.secondAnswer = this.getAnswer();

    var questionTrail = new Array(this.questionCount);
    var firstTrail = new Array(this.questionCount);
    var secondTrail = new Array(this.questionCount);

    var i = 0;

    var q = this.firstQid;

    while (q != 0) {

      dto = this.questionDTOs[q];
      questionTrail[i] = dto.QForm.id;
      firstTrail[i] = dto.firstAnswer;
      secondTrail[i] = dto.secondAnswer;
      q = dto.QForm.JQuestion_id
      i++;
    }

    Element.show("loading");
    Element.hide("miroForm");

    var cSubmitMiroResponseCallBack = this.submitMiroResponseCallBack.bind(this);
    ajaxSurveyEditManager.submitMiroResponse(this.sid, questionTrail, firstTrail, secondTrail, cSubmitMiroResponseCallBack);


  },

  submitMiroResponseCallBack: function (rValue) {


    if (rValue == MIRO_OK) {
      Element.hide("loading");
      Element.hide("miroError");
      Element.show("miroThanks");
      testInProgress = false;
      return;
    }

    if (rValue == MIRO_EXCEPTION) {
      Element.hide("loading");
      Element.show("miroForm");
      this.showError("An error happened on the server, if this keeps happening please contact your support representitive");
      return;
    }

    if (rValue == MIRO_DUPE) {
      Element.hide("busy");
      this.showError("We already have a reponse from you!");
      Element.show("miroThanks");
      return;
    }

    if (rValue == MIRO_INVALID) {

      Element.hide("loading");
      Element.hide("miroThanks");
      Element.show("miroInvalid");
      return;
    }


  },

  nextButton: function () {

    testInProgress = true;

    var qForm = this.currentQuestionDTO.QForm;

    if (!this.isValid()) {
      this.showError("Please select a value");
      return;
    }

    if (qForm.QMeta == "Q11Inst" || qForm.QMeta == "Q11") {
      this.currentQuestionDTO.secondAnswer = this.getAnswer();
      this.setCurrentQuestion(this.currentQuestionDTO.QForm.JQuestion_id);
      this.qNo++;
    } else {

      if (this.isFirstPage()) {
        this.setFirstPage(false);
        this.currentQuestionDTO.firstAnswer = this.getAnswer();

      } else {
        var prevQid = this.currentQuestionDTO.QForm.id;
        this.currentQuestionDTO.secondAnswer = this.getAnswer();
        this.setCurrentQuestion(this.currentQuestionDTO.QForm.JQuestion_id);
        this.setFirstPage(true);
        this.currentQuestionDTO.prevQid = prevQid;
        this.qNo++;
      }
    }

    this.showCurrentQuestion();

  },

  prevButton: function () {


    if (this.isFirstPage()) {
      this.setCurrentQuestion(this.currentQuestionDTO.prevQid);
      this.setFirstPage(false);
      this.qNo--;
    } else {
      this.setFirstPage(true);
    }


    this.showCurrentQuestion();

  },


  getAnswer:function() {


    var radioButtons = Form.getInputs('miroForm','radio', 'qOptions')
    var answer= "" ;
    radioButtons.each( function(radioButton){

      if(radioButton.checked) {
        answer =  radioButton.value;
      }

    });

    return answer;
  },

  showError: function (eText) {

    Element.update("miroError", eText );
    Element.show("miroError");
  },


  isFirstPage: function () {
    return this.currentQuestionDTO.page;
  },
  isValid: function () {

    var answer = this.getAnswer();

    if (answer == "" || typeof answer == 'undefined') {
      return false;
    } else {
      return true;
    }


  },
  setCurrentQuestion: function (qid) {

    this.currentQuestionDTO = this.questionDTOs[qid];
  },
  setFirstPage: function (state) {

    this.currentQuestionDTO.page = state;

  },
  showFirstQuestion: function () {

    this.qNo = 1;

    this.setCurrentQuestion(this.firstQid);
    this.currentQuestionDTO.prevQid = 0;
    this.setFirstPage(true);

    this.showCurrentQuestion();


  }, showCurrentQuestion: function () {

    var qForm = this.currentQuestionDTO.QForm;

    switch (qForm.QMeta) {

      case "tie" :
      case "Q" :
        return this.showCurrentMiro10Question();
      case "Q11Inst" :
      case "Q11":
        return this.showCurrentMiro11Question();
    }


  },
  showCurrentMiro11Question: function () {
    var qForm = this.currentQuestionDTO.QForm;
    var self = this;


    Element.update("qNum","Question " + this.qNo + " of " + this.questionCount) ;
    Element.update("miro11Prompt",qForm.QTxt);
    Element.show("miro11Prompt");

    Element.hide("leastPrompt");
    Element.hide("mostPrompt");

    switch (qForm.QMeta) {

      case "Q11Inst" :
        Element.hide("miroQuestionRadio");
        Element.show("miro11Prompt");
        Element.show("next");
        Element.hide("prev");
        Element.hide("finish");
        break;
      case "Q11":
        var radioHtml = self.getRadioButtonHTML();
        Element.update("miroQuestionRadio", radioHtml);
        Element.show("miroQuestionRadio");
        Element.show("miro11Prompt");
        Element.show("next");
        if (this.isLastQuestion()) {
          Element.show("finish");
          Element.hide("next");
        } else {
          Element.hide("finish");
          Element.show("next");
        }
        break;
    }

  },
  showCurrentMiro10Question: function () {

    var self = this;

    Element.hide("miro11Prompt");

    if (this.isFirstPage()) {
      Element.show("mostPrompt");
      Element.hide("leastPrompt");
    } else {
      Element.show("leastPrompt");
      Element.hide("mostPrompt");
    }

    var radioHtml =  self.getRadioButtonHTML();

    Element.update("miroQuestionRadio", radioHtml);
    Element.update("qNum","Question " + this.qNo + " of " + this.questionCount);

    Element.hide("loading");
    Element.show("miroForm");
    Element.hide("miroError");

    //show the correct Buttons
    if (this.isLastQuestion() && !this.isFirstPage()) {
      Element.hide("next");
      Element.show("prev");
      Element.show("finish");
    } else if (this.isFirstQuestion() && this.isFirstPage()) {
      Element.show("next");
      Element.hide("prev");
      Element.hide("finish");
    } else if (this.isFirstPage()) {
      Element.show("next");
      Element.hide("prev");
      Element.hide("finish");
    } else {
      Element.show("next");
      Element.show("prev");
      Element.hide("finish");
    }

  },
  isLastQuestion: function () {

    if (this.currentQuestionDTO.QForm.JQuestion_id == 0) {
      return true;
    } else {
      return false;
    }

  }, isFirstQuestion: function () {

    if (this.currentQuestionDTO.prevQid == 0) {
      return true;
    } else {
      return false;
    }

  },
  getQuestions: function () {

    //the bind stuff is all beacuse javascript can do some weird stuff with this.function) calls
    //you need to bind the function before calling it
    var cShowAllQuestionsCallBack = this.showAllQuestionsCallBack.bind(this);
    ajaxSurveyEditManager.getQuestions(this.sid, cShowAllQuestionsCallBack);

  },
  showAllQuestionsCallBack: function (questionDTOs) {

    if (questionDTOs == null) {
      this.handleAjaxErrors(questionDTOs);
      return;
    }


    this.questions = new Object();
    var questionDTO;
    this.questionCount = 0;
    for (var q in questionDTOs) {
      questionDTO = questionDTOs[q];
      this.questions[q] = questionDTO.QForm;
      this.questionCount++;
    }

    this.questionDTOs = questionDTOs;

    Element.hide("loading");

    this.showFirstQuestion();

  },

  getRadioButtonHTML: function () {

    //build the html string to shove ;
    var htmlStr = "";
    var bits = "";


    var OForms = this.currentQuestionDTO.OForms;
    var isFirstPage = this.isFirstPage();
    var answer = "";
    var firstAnswer = this.currentQuestionDTO.firstAnswer;

    if (isFirstPage) {
      answer = this.currentQuestionDTO.firstAnswer;
    } else {
      answer = this.currentQuestionDTO.secondAnswer;
    }


    var checked = "";

    var i = 0 ;
    for( i = 0 ; i< OForms.length;i++)  {

      var OForm = OForms[i];

      if (!isFirstPage && OForm.OText == firstAnswer) {
        //do not add tos list
      }  else {

        if (OForm.OText == answer) {
          checked = "checked";
        } else {
          checked = "";
        }
        bits = OForm.OText.split(SEP);
        htmlStr = htmlStr + '<input type="radio" name="qOptions" value="' + OForm.OText + '"  ' + checked + '/>' + bits[0] + '<br/>';
      }



    };
    return htmlStr;

  },
  handleAjaxErrors: function (questionDTO) {

    //if it is null then assume we have timed out and redirect to login.
    if (questionDTO == null) {
      alert("Session timed out ...");
      location = "mainmenu.html";
      return;
    }

    alert("oooo an error happened talking to the server!!")

    location = "http://www.miro-assessment.com";

    return;

  },
  dummy: function () {
    //dummy at end so we don't worry about the comma
  }
};	