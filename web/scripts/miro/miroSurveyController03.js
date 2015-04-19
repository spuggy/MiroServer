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
    $("#miroInvalid").hide();
    $("#intro").hide();
    $("#loading").show();
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

    $("#loading").show();
    $("#miroForm").hide();

    var cSubmitMiroResponseCallBack = this.submitMiroResponseCallBack.bind(this);
    ajaxSurveyEditManager.submitMiroResponse(this.sid, questionTrail, firstTrail, secondTrail, cSubmitMiroResponseCallBack);


  },

  submitMiroResponseCallBack: function (rValue) {


    if (rValue == MIRO_OK) {
      $("#loading").hide();
      $("#miroError").hide();
      $("#miroThanks").show();
      testInProgress = false;
      return;
    }

    if (rValue == MIRO_EXCEPTION) {
      $("#loading").hide();
      $("#miroForm").show();
      this.showError("An error happened on the server, if this keeps happening please contact your support representitive");
      return;
    }

    if (rValue == MIRO_DUPE) {
      $("#busy").hide();
      this.showError("We already have a reponse from you!");
      $("#miroThanks").show();
      return;
    }

    if (rValue == MIRO_INVALID) {

      $("#loading").hide();
      $("#miroThanks").hide();
      $("#miroInvalid").show();
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


  getAnswer: function () {

    if(this.currentQuestionDTO.QForm.QMeta=="Q11Inst") {
      return "N/A"
    }

    return $('input[name="qOptions"]:checked', '#miroForm').val()
  },

  showError: function (eText) {

    $("#miroError").text(eText);
    $("#miroError").show();
  },


  isFirstPage: function () {
    return this.currentQuestionDTO.page;
  },
  isValid: function () {

    var answer = this.getAnswer();

    if (answer == "") {
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

    $("#qNum").text( "Question " + this.qNo + " of " + this.questionCount) ;
    $("#miro11Prompt").text(qForm.QTxt);
    $("#miro11Prompt").show();

    $("#leastPrompt").hide();
    $("#mostPrompt").hide();

    switch (qForm.QMeta) {

      case "Q11Inst" :
        $("#miroQuestionRadio").hide();
        $("#miro11Prompt").show();
        $("#next").show();
        $("#prev").hide();
        $("#finish").hide();
        break;
      case "Q11":
        var radioHtml = self.getRadioButtonHTML();
        $("#miroQuestionRadio").html(radioHtml);
        $("#miroQuestionRadio").show();
        $("#miro11Prompt").show();
        $("#next").show();
        if (this.isLastQuestion()) {
          $("#finish").show();
          $("#next").hide();
        } else {
          $("#finish").hide();
          $("#next").show();
        }
        break;
    }

  },
  showCurrentMiro10Question: function () {

    var self = this;

    $("#miro11Prompt").hide();

    if (this.isFirstPage()) {
      $("#mostPrompt").show();
      $("#leastPrompt").hide();
    } else {
      $("#leastPrompt").show();
      $("#mostPrompt").hide();
    }

    var radioHtml =  self.getRadioButtonHTML();

    $("#miroQuestionRadio").html(radioHtml);
    $("#qNum").text("Question " + this.qNo + " of " + this.questionCount);

    $("#loading").hide();
    $("#miroForm").show();
    $("#miroError").hide();

    //show the correct Buttons
    if (this.isLastQuestion() && !this.isFirstPage()) {
      $("#next").hide();
      $("#prev").show();
      $("#finish").show();
    } else if (this.isFirstQuestion() && this.isFirstPage()) {
      $("#next").show();
      $("#prev").hide();
      $("#finish").hide();
    } else if (this.isFirstPage()) {
      $("#next").show();
      $("#prev").hide();
      $("#finish").hide();
    } else {
      $("#next").show();
      $("#prev").show();
      $("#finish").hide();
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

    $("#loading").hide();

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