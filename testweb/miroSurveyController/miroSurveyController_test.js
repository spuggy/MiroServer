var SEP = '#';
var MIRO_OK = "1";
var MIRO_EXCEPTION = "2";
var MIRO_DUPE = "3";
var MIRO_INVALID = "4";





describe('miroSurveyController', function () {

  describe('miro 1.1', function () {

    AjaxSurveyEditManagerMock = function () {
    }

    AjaxSurveyEditManagerMock.prototype.getQuestions = function (sid, callback) {
      callback(g_miro_questions_1_1);
    }

    ajaxSurveyEditManager = new AjaxSurveyEditManagerMock()


    it.only('run ok survey', function () {


      AjaxSurveyEditManagerMock.prototype.submitMiroResponse = function (sid, questionTrail, firstTrail, secondTrail, callBack) {
        callBack(MIRO_OK);
      }

      msc = new MiroSurveyController(4, 33);
      msc.startButton();


      for (var i = 0; i < 59; i++) {
        var x = document.getElementsByName('qOptions');
        x[1].checked = true;
        msc.nextButton();
        x[2].checked = true;
      }

      expect(document.getElementById("finish").style.display).equals("");

     // msc.finishButton();

      expect(document.getElementById("miroThanks").style.display).equals("");
      expect(document.getElementById("miroInvalid").style.display).equals("none");

    });

  });

//  describe.skip('miro 1.0', function () {
//    AjaxSurveyEditManagerMock = function () {
//    }
//
//    AjaxSurveyEditManagerMock.prototype.getQuestions = function (sid, callback) {
//      callback(g_miro_questions_1_0);
//    }
//
//    ajaxSurveyEditManager = new AjaxSurveyEditManagerMock()
//
//
//
//    it('run ok survey', function () {
//
//
//      AjaxSurveyEditManagerMock.prototype.submitMiroResponse = function (sid, questionTrail, firstTrail, secondTrail, callBack) {
//        callBack(MIRO_OK);
//      }
//
//      msc = new MiroSurveyController(4, 33);
//      msc.startButton();
//
//
//      for (var i = 0; i < 59; i++) {
//        var x = document.getElementsByName('qOptions');
//        x[1].checked = true;
//        msc.nextButton();
//        x[2].checked = true;
//      }
//      expect(document.getElementById("finish").style.display).equals("");
//
//
//      msc.finishButton();
//
//      expect(document.getElementById("miroThanks").style.display).equals("");
//      expect(document.getElementById("miroInvalid").style.display).equals("none");
//
//    });
//
//
//    it('run invalid survey', function () {
//
//
//      AjaxSurveyEditManagerMock.prototype.submitMiroResponse = function (sid, questionTrail, firstTrail, secondTrail, callBack) {
//        callBack(MIRO_INVALID);
//      }
//
//      msc = new MiroSurveyController(4, 33);
//      msc.startButton();
//
//
//      for (var i = 0; i < 59; i++) {
//        var x = document.getElementsByName('qOptions');
//        x[1].checked = true;
//        msc.nextButton();
//        x[2].checked = true;
//      }
//
//      msc.finishButton();
//
//      expect(document.getElementById("miroThanks").style.display).equals("none");
//      expect(document.getElementById("miroInvalid").style.display).equals("");
//
//    });
//
//
//  });
//

})

