var SEP = '#';
var MIRO_OK = "1";
var MIRO_EXCEPTION = "2";
var MIRO_DUPE = "3";
var MIRO_INVALID = "4";

AjaxSurveyEditManagerMock = function () {
  this.status = MIRO_OK;
  this.questions = g_miro_questions_1_0
}

AjaxSurveyEditManagerMock.prototype.getQuestions = function (sid, callback) {
  callback(this.questions);
}

AjaxSurveyEditManagerMock.prototype.submitMiroResponse = function (sid, questionTrail, firstTrail, secondTrail, callBack) {
  callBack(this.status);
}

ajaxSurveyEditManager = new AjaxSurveyEditManagerMock()





describe('miroSurveyController', function () {

  describe('miro 1.1', function () {

    it('run ok survey', function () {

      msc = new MiroSurveyController(4, 33);
      ajaxSurveyEditManager.questions =  g_miro_questions_1_1;
      ajaxSurveyEditManager.status =  MIRO_OK;

      msc.startButton();


      for (var i = 0; i < 30; i++) {
        var x = document.getElementsByName('qOptions');
        x[1].checked = true;
        msc.nextButton();
        x = document.getElementsByName('qOptions');
        expect(x.length).equals(3);

        x[2].checked = true;
        msc.nextButton();
      }

      expect(document.getElementById("miro11Prompt").style.display).equals("");

      msc.nextButton();

      var x = document.getElementsByName('qOptions');
      x[1].checked = true;
      msc.nextButton();

      expect(document.getElementById("finish").style.display).equals("");

      var x = document.getElementsByName('qOptions');
      x[1].checked = true;
      msc.finishButton();

      expect(document.getElementById("miroThanks").style.display).equals("");
      expect(document.getElementById("miroInvalid").style.display).equals("none");

    });

  });

  describe('miro 1.0', function () {




    it('run ok survey', function () {

      var msc = new MiroSurveyController(4, 33);
      ajaxSurveyEditManager.questions =  g_miro_questions_1_0;
      ajaxSurveyEditManager.status =  MIRO_OK;

      msc.startButton();


      for (var i = 0; i < 29; i++) {
        var x = document.getElementsByName('qOptions');
        x[1].checked = true;
        msc.nextButton();
        x = document.getElementsByName('qOptions');
        expect(x.length).equals(3);
        x[2].checked = true;
        msc.nextButton();
      }

      var x = document.getElementsByName('qOptions');
      x[1].checked = true;
      msc.nextButton();
      x = document.getElementsByName('qOptions');
      expect(x.length).equals(3);
      x[2].checked = true;

      msc.finishButton();

      expect(document.getElementById("miroThanks").style.display).equals("");
      expect(document.getElementById("miroInvalid").style.display).equals("none");

    });


    it('run invalid survey', function () {

      var msc = new MiroSurveyController(4, 33);
      ajaxSurveyEditManager.questions =  g_miro_questions_1_0;
      ajaxSurveyEditManager.status =  MIRO_INVALID;
      msc.startButton();


      for (var i = 0; i < 29; i++) {
        var x = document.getElementsByName('qOptions');
        x[1].checked = true;
        msc.nextButton();
        x = document.getElementsByName('qOptions');
        expect(x.length).equals(3);
        x[2].checked = true;
        msc.nextButton();
      }

      var x = document.getElementsByName('qOptions');
      x[1].checked = true;
      msc.nextButton();
      x = document.getElementsByName('qOptions');
      expect(x.length).equals(3);
      x[2].checked = true;

      msc.finishButton();

      expect(document.getElementById("miroThanks").style.display).equals("none");
      expect(document.getElementById("miroInvalid").style.display).equals("");

    });


  });


})

