<%@ include file="/common/taglibs.jsp" %>
<head>

    <title><fmt:message key="miroSurvey.title"/></title>

    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>"/>


    <script type="text/javascript" src="<c:url value='/dwr/interface/ajaxSurveyEditManager.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery-1.11.1.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/miro/miroSurveyController06.js'/>"></script>

    <script type="text/javascript">
        var testInProgress = false;

        function startMiroTest() {

            msc = new MiroSurveyController(<c:out value="${survey.id}"/>, <c:out value="${survey.firstQuestion_id}"/>);

            msc.startButton();

        }


        function warnOnUnload() {

            if (testInProgress) {
                return "Navigating away from this page will lose your test answers.  Do you want to continue?";
            }


        }

        window.onbeforeunload = warnOnUnload;

    </script>

</head>
<body>
<content tag="heading"><fmt:message key="miroSurvey.heading"/></content>
<content tag="shortheading"><fmt:message key="miroSurvey.shortheading"/></content>


<div class="row">
    <div id="miro-content" class="col-sm-8">
        <div id="intro">

            <c:choose> <c:when test="${isVersion11}">
                <p>Once you click on "continue" you will be shown 50 questions.</p>

                <p>30 are multiple choice, single word selections and you must choose the one that most describes you
                    and the one that least describes you. Sometimes they may all seem to fit or all not fit, go with
                    your first instinct or think about what someone who knows you well might say.</p>

                <p>The next 20 are "true" or "false" statements.</p>
            </c:when>

                <c:otherwise>
                    <p>Once you click on "continue" you will be shown 30 questions.</p>

                    <p>Each question are multiple choice, single word selections and you must choose the one that most
                        describes you and the one that least describes you. Sometimes they may all seem to fit or all
                        not fit, go with your first instinct or think about what someone who knows you well might
                        say.</p>
                </c:otherwise> </c:choose>

            <p>The more honest you are the more accurate and so the more useful your results will be.</p>

            <p><b>There are no "right" or "wrong" answers and no-one will see your responses.</b></p>

            <p>There is no time limit but you should try and complete the assessment within 15 minutes if at all
                possible. You will be able to edit your answers as you go but once you click on the finish button you
                will not be able to return to the assessment or take a new one without a new log in.</p>

            <p>The practitioner who sent out your log in will receive the resulting report and then pass it on to you.
                If you have any questions please call or email your registered MiRo practitioner or contact
                support@miro-assessment.com</p>


            <p>
                <button class="btn btn-primary" onClick="startMiroTest()">Start my assessment</button>
            </p>

        </div>


        <div id="loading" style="display: none">
            <p>Please wait while we load the questions .....</p>
            <img src="styles/images/ajax-loader.gif" border="0"/>

        </div>
        <div class="alert alert-danger alert-dismissible hidemeh" role="alert" id="miroError"></div>
        <div id="miroForm" style="display: none">
            <form>
                <p>

                <div id="miroQuestionText">
                    <div id="qNum"></div>
                    <br/>

                    <div id="leastPrompt" class="lead">Which word <b>LEAST</b> describes you?</div>
                    <div id="mostPrompt" class="lead">Which word <b>MOST</b> describes you?</div>
                    <div id="miro11Prompt" class="lead">Hey the new questions are next ... woot</div>

                </div>

                <div id="miroQuestionRadio" class="lead radio well well-lg survey-radio-group">


                </div>
                </p>
            </form>
            <p>

            <div id="miroQuestionButtons">
                <button class="btn btn-default" id="prev" onClick="msc.prevButton()" style="display: none">prev</button>
                <button class="btn btn-primary" id="next" onClick="msc.nextButton()">next</button>
                <button class="btn btn-primary" id="finish" onClick="msc.finishButton()" style="display: none">finish
                </button>
            </div>
            </p>
        </div>


        <div id="miroInvalid" style="display: none">
            <p>Unfortunately you have returned a result that cannot be interpreted, this is due to all your scores
                appearing within the same range. This occurs when someone has either given the questions too much
                thought and not used their instinct or they are trying to be all things to all people. Please try again
                and retake the assessment. Try to be honest with yourself and concentrate on how you are at work only
                and choose the words that feel most and least comfortable to you as an individual. </p>

            <p>
                <button class="btn btn-primary" value="Try again" onClick="startMiroTest()">Try again</button>
            </p>
        </div>

        <div id="miroThanks" style="display: none">
            <p>You have now completed the MiRo Behavioural Mode Assessment. Your results will be formulated and sent to
                you via e-mail or handed to you at your coaching session or team event. If you have any questions
                regarding the MiRo Behavioural Mode Assessment then please contact one of our registered
                practitioners.</p>

            <p>The registered practitioner for your area is:</p>

            <p><c:out value="${practitioner.fullName}"/></p>

            <p><b>Tel:</b> <c:out value="${practitioner.phoneNumber}"/><br/> <b>Email:</b>
                <a href="mailto:<c:out value="${practitioner.email}" />"><c:out value="${practitioner.email}"/></a>
        </div>
    </div>
</div>

</body>