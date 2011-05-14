<%@ include file="/common/taglibs.jsp"%>
<head>
	
	<title><fmt:message key="miroSurvey.title"/></title>

	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />

		
	<script type="text/javascript" src="<c:url value='/dwr/interface/ajaxSurveyEditManager.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/dragdrop.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/effects.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/miroSurveyController02.js'/>"></script>

<script type="text/javascript">
var testInProgress = false ; 

function startMiroTest(){
	
	msc = new MiroSurveyController(<c:out value="${survey.id}"/>,<c:out value="${survey.firstQuestion_id}"/>) ;
	
	msc.startButton();

}



function warnOnUnload() {

if(testInProgress) {
    return "Navigating away from this page will lose your test answers.  Do you want to continue?";
}



}

window.onbeforeunload = warnOnUnload;

</script> 

</head>
<body>
<content tag="heading">MiRo Behavioural Mode Assessment
</content>
<ul>
<li>
<div id="intro">
<p>Once you click on the continue button you will be shown 30 multiple choice selections. Each is a set of descriptive words and each set contains 4 words.</p>

<p>First choose the word that most describes you. This may on occasion prove difficult but try to be as honest as you can and think about how you actually are or what you actually do, rather than what you 
think you ought to be or ought to do. You need to think of yourself in your current job or in current  social situations. 
Of course the answer in each circumstance may be different but you must choose the answer that is truest for who you really are.</p>

<p><b>There are no 'right' or 'wrong' answers.</b></p>

<p>Next you will be offered the three remaining choices and you must choose the one that least applies to you. Once again try to be as honest as you can. </p>

<p>Try to avoid false modesty or answering as you think you should in order to create a 
good impression of your self. If you really can't choose, your instinct is often more reliable 
than too much thought. Remember the more honest you are the more valuable the results will be for you.</p> 

<p>Once all the sets have been completed the system will inform you that the form is 
complete. Your results will then be sent to you by e-mail or handed to you at your coaching session or team event.</p>

<p>There is no time limit but you should try and complete the assessment within 15 minutes if at all possible. 
You will be able to edit your answers as you go but once you click on the finish button you will not be able to
 return to the assessment or take a new one without a new log in.</p>


<form id="startForm"> <input type="button" id="start" value="Start my assessment" onClick="startMiroTest()"/></form>

</div>


<div id="loading" style="display: none" >
	<p>Please wait while we load the questions .....</p>
	<img src="styles/images/ajax-loader.gif" border="0"/>

</div>
<div class="error" id="miroError" style="display: none">
</div>
<form id="miroForm" style="display: none">

	<ul>
	<li>
	<div id="miroQuestionText">
		<div id="qNum"></div><br/>
		<div id="leastPrompt">Which word <b>LEAST</b> describes you?</div>
		<div id="mostPrompt">Which word <b>MOST</b> describes you?</div>
		
	</div>
	</li>
	<li>
	<div id="miroQuestionRadio">

		
	</div>
	</li>
	<li>
	<div id="miroQuestionButtons">
		<input type="button" id="prev" value="prev" onClick="msc.prevButton()" style="display: none"/>
		<input type="button" id="next" value="next" onClick="msc.nextButton()"/>
		<input type="button" id="finish" value="finish" onClick="msc.finishButton()" style="display: none"/>
	</div>
	</li>
	</ul>
	
</form>

<div id="miroInvalid" style="display: none">
<p>Unfortunately you have returned a result that cannot be interpreted, this is due to all your scores appearing within the same range. 
This occurs when someone has either given the questions too much thought and not used their instinct or they are trying to be all things to all people.  
Please try again and retake the assessment. 
Try to be honest with yourself and concentrate on how you are at work only and choose the words that feel most and least comfortable to you as an individual.
</p>

<form id="startForm"> <input type="button" id="start" value="Try again" onClick="startMiroTest()"/></form>
</div>

<div id="miroThanks" style="display: none">
<p>You have now completed the MiRo Behavioural Mode Assessment. Your results will be formulated and sent to you 
via e-mail or handed to you at your coaching session or team event. If you have any questions regarding the MiRo Behavioural 
Mode Assessment then please contact one of our registered practitioners.</p>

<p>The registered practitioner for your area is:</p>

<p><c:out value="${practitioner.fullName}" /></p>

<p><b>Tel:</b> <c:out value="${practitioner.phoneNumber}" /><br/>
<b>Email:</b> <a href="mailto:<c:out value="${practitioner.email}" />"><c:out value="${practitioner.email}" /></a>
</div>
</li>
</ul>

</body>