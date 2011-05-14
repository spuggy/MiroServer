<%@ include file="/common/taglibs.jsp"%>
<head>
	
	<meta name="menu" content="surveyAdmin"/>
	<content tag="navname">Surveys</content>

	<title><fmt:message key="surveyDetail.title" /></title>

	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/surveyForm.css'/>" />
		
	<script type="text/javascript" src="<c:url value='/dwr/interface/ajaxSurveyEditManager.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/surveyFormController.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/surveyFormUI.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/calendar.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/dragdrop.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/effects.js'/>"></script>

	<script type="text/javascript">

//set up the calendar for the constraint date popu
var cal = new CalendarPopup();


//global variable to surveycontrolelr object
var sc ; 
function initPage(){

	sc = new SurveyEditController() ;  
    
    
    //add string constants
    sc.addMessage("6", '<fmt:message key="questionForm.text" />') ; 
	sc.addMessage("1" , '<fmt:message key="questionForm.checkbox" />') ; 
	sc.addMessage("2" , '<fmt:message key="questionForm.date" />') ; 
	sc.addMessage("3" , '<fmt:message key="questionForm.message" />') ;
	sc.addMessage("4" , '<fmt:message key="questionForm.number" />') ;
	sc.addMessage("5", '<fmt:message key="questionForm.radio" />'); 								
	sc.addMessage("7",'<fmt:message key="questionForm.decimal" />') ;
	sc.addMessage("8",'<fmt:message key="questionForm.percent" />');
	sc.addMessage("9",'<fmt:message key="questionForm.score" />');
	sc.addMessage("10",'<fmt:message key="questionForm.picture" />');
	sc.addMessage("11",'<fmt:message key="questionForm.lookup" />');
	sc.addMessage("true", '<fmt:message key="surveyForm.true" />');
	sc.addMessage("false", '<fmt:message key="surveyForm.false" />');
	
	sc.addMessage("questionForm.deletePrompt", '<fmt:message key="questionForm.deletePrompt" />');
	sc.addMessage("optionForm.deletePrompt", '<fmt:message key="optionForm.deletePrompt" />');
    
    
    
    sc.showAllQuestions();
    
    

}




</script> 
</head>
<body onLoad="initPage()">


	<content tag="heading">
	<c:out value="${surveyForm.title}" /> <fmt:message key="surveyDetail.heading" />
	</content>
	
	<spring:bind path="survey.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">    
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
	</spring:bind>
	
	<form:form commandName="survey" method="post" action="editSurvey.html" onsubmit="return validateSurvey(this)" id="surveyForm">
	
	

		<table id="surveyDetail">
			<form:hidden path="version" id="sversion" />
			<form:hidden path="id" id="survey_id" />
			<form:hidden path="firstQuestion_id"  />
			<form:hidden path="common" />
			<form:hidden path="photoFile" />
			
			<tbody>
				<tr>
					<th valign="top">
						<mobriz4server:label styleClass="desc" key="surveyForm.title"/>						
					</th>
					<td valign="top" nowrap>
						
				        <form:errors path="title" cssClass="fieldError"/>
        				<form:input path="title" id="title" cssClass="text large"/>					
						
					</td>
				</tr>
				<tr>
					<th valign="top" >
						<mobriz4server:label key="surveyForm.visibility" />
					</th>
					<td valign="top">
					
						
						<form:select path="visibility" onchange="sc.visibilityChange()">
							 <form:option value="0"><fmt:message key="surveyForm.selectVisibility" /></form:option>
							 <form:option value="1"><fmt:message key="surveyForm.visibleToAll" /></form:option>
							 <form:option value="2"><fmt:message key="surveyForm.visibleToSelectedUsers" /></form:option>						
						</form:select>
									
						
					</td>
				
				</tr>
				<tr style="display:none" id="surveyUsers">
					<th valign="top">
						<mobriz4server:label key="surveyForm.users" />
					</th>

					<td valign="top">
						<form:errors path="users" />
						<form:select multiple="true" size="5" path="users"  id="userNames"  >
						 <form:options items="${userList}" itemValue="id" itemLabel="fullName"/>
						</form:select>
					</td>

				</tr>
				
				<tr>
					<th>&nbsp;</th>
					<td nowrap>
							<input class="f-submit" type="button" value="Save Survey Details" onClick="sc.updateSurveyButton()" />							
            		</td>
				</tr>
			</tbody>
		</table>

		<%@ include file="/WEB-INF/pages/surveyElement.jsp"%>
	</form:form>

	
<h2><fmt:message key="questionDetail.heading" /></h2>
<!-- begining of question editor -->
	<div id="questionsEditUI" style="display:none">

		<table  width="600">
			<tr>
				<td width="500" valign="top" align="right">
<fmt:message key="surveyForm.editButtonText" /><input class="qrowbutton" type="button" value="e" /> 
<fmt:message key="surveyForm.delButtonText" /><input  class="qrowbutton" type="button" value="-" />
<fmt:message key="surveyForm.insertButtonText" /><input class="qrowbutton" type="button" value="+" />
				</td>
				<td width="100" >
					&nbsp;
				</td>
			</tr>
			<tr>
				<td >
					<table class="table2" width="500">
						<tr>
							<th width="300">
								<fmt:message key="questionForm.QTxt" />
							</th>
							<th width="100">
								<fmt:message key="questionForm.shortname" />
							</th>
							<th>
								<fmt:message key="questionForm.QType" />
							</th>
							<th >
								R
							</th>
							<th >
								S
							</th>
							<th >
								&nbsp;
							</th>
							
														
						</tr>
						
						<tbody id="questionsTableBody">
						</tbody>
						<tfoot id="questionsTableFoot">
							<tr>
								<td valign="top" colspan="6"  align="right">
									<input class="f-submit" type="button" value="add question" onClick="sc.appendButton()" />
								</td>
							</tr>
						</tfoot>

					</table>
					<br />

				</td>
				<td valign="top">
					
				</td>
			</tr>
		</table>

	</div>

	<div id="qEditPaneDiv" style="display:none">
		<form id="qEditPaneForm" name="qEditPaneForm">



			<fieldset>

				<table>

					<tr>
						<td valign="top">
							<div>
								<b><fmt:message key="questionForm.QTxt" />:</b>
							</div>
							<div>
								<textarea id="QTxt" name="QTxt">
									
								</textarea>
							</div>
						</td>
					</tr>

					<tr>
						<td>
							<b><fmt:message key="questionForm.required" />:</b><INPUT TYPE="checkbox" id="required" name="required" />
							<b><fmt:message key="questionForm.sticky" />:</b><INPUT TYPE="checkbox" id="sticky" name="sticky" />
						</td>
					</tr>


					<tr>
						<td>
							<b><fmt:message key="questionForm.shortname" />:</b>
							<input type="text" name="shortname" id="shortname" value="" />
						</td>
					</tr>

					<tr>
						<td>
							<b><fmt:message key="questionForm.QType" />:</b>
							<select STYLE="width: 150px" name="QType" id="QType" onchange="sc.qTypeChange()">
								<OPTION VALUE="6" />
									<fmt:message key="questionForm.text" />
								<OPTION VALUE="1" />
									<fmt:message key="questionForm.checkbox" />
								<OPTION VALUE="5" />
									<fmt:message key="questionForm.radio" />		

								<OPTION VALUE="8" />								
									<fmt:message key="questionForm.percent" />
								<OPTION VALUE="9" />
									<fmt:message key="questionForm.score" />
								<OPTION VALUE="4" />
									<fmt:message key="questionForm.number" />
								<OPTION VALUE="2" />
									<fmt:message key="questionForm.date" />
								<OPTION VALUE="7" />
									<fmt:message key="questionForm.decimal" />
								<OPTION VALUE="3" />
									<fmt:message key="questionForm.message" />
								<OPTION VALUE="10" />
									<fmt:message key="questionForm.picture" />
								<OPTION VALUE="11" />
									<fmt:message key="questionForm.lookup" />	

							</select>
						</td>
					</tr>
					<tr>
						<td>
							<b><fmt:message key="questionForm.branch" />:</b> <a id="qLinkMe" href="#" onclick='sc.linkMeClicked("que",null);return false;'>Link-Me</a>
						</td>
					</tr>
				</table>




				<input type="hidden" id="branch_jquestion_id" name="branch_jquestion_id" value="" />
				<input type="hidden" id="qid" name="qid" value="" />
				<input type="hidden" id="survey_id" name="survey_id" value="" />
				<input type="hidden" id="JQuestion_id" name="JQuestion_id" value="" />
				<input type="hidden" id="qversion" name="qversion" value="" />

				<input type="hidden" id="created_on" name="created_on" />
				<input type="hidden" id="lastUpdateBy_id" name="lastUpdateBy_id" />
				<input type="hidden" id="updated_at" name="updated_at" />
				<input type="hidden" id="createdBy_id" name="createdBy_id" />

			</fieldset>
			
			<fieldset id="qMetaFieldSet" style="display:none">

				<table>
					<tr>
						<td>
						    <b>Lookup Name:</b><br/>
						    	<select STYLE="width: 150px" name="qMeta" id="QMeta" >
								<OPTION VALUE="0" />Select Lookup								
								<c:forEach var="lookupDef" items="${lookupDefs}" >
									<OPTION VALUE="<c:out value="${lookupDef.id}"/>" />
									<c:out value="${lookupDef.name}"/>
								</c:forEach>
								
									
							</select>

						</td>
				</table>
			</fieldset>
			
			<fieldset id="optionFieldSet">



				<table>
					<tr>
						<td>
							<b><fmt:message key="optionForm.Options" />:</b>
							<table>
								<tbody id="optionsTableBody">



								</tbody>
								<tr>
									<td>
										<a href="#" onclick="sc.addOptionButton();return false;" /><fmt:message key="questionForm.addOption" /> </a>
									</td>
								</tr>
							</table>
						</td>
				</table>
			</fieldset>


			<fieldset id="constraintFieldSet" style="display:none">
				<table>
					<tr>

						<td valign="top">
							<b><fmt:message key="constraintForm.CType" />:</b>
							<select name="CType" id="CType" onchange="sc.cTypeChange()">
								<OPTION VALUE="0" />
									<fmt:message key="constraintForm.none" />
								<OPTION VALUE="1" />
									<fmt:message key="constraintForm.equals" />
								<OPTION VALUE="2" />
									<fmt:message key="constraintForm.notEquals" />
								<OPTION VALUE="3" />
									<fmt:message key="constraintForm.greaterThan" />
								<OPTION VALUE="4" />
									<fmt:message key="constraintForm.lessThan" />
								<OPTION VALUE="5" />
									<fmt:message key="constraintForm.greaterThanEquals" />
								<OPTION VALUE="6" />
									<fmt:message key="constraintForm.lessThanEquals" />
								<OPTION VALUE="7" />
									<fmt:message key="constraintForm.between" />
							</select>

						</td>

					</tr>

					<tr id="constraintDateValues" style="display:none">
						<td>
							<b><fmt:message key="constraintForm.values" />:</b>
							<input type="text" name="d1" id="d1" />
							<A HREF="#" onClick="cal.select(document.forms['qEditPaneForm'].d1,'d1popup','dd/MM/yyyy'); return false;" NAME="d1popup" ID="d1popup"><img src="images\iconCalendar.gif" height="16" width"=15"></A>
							<div id="constraintDateValuesBetween" style="display:none">
								<b><fmt:message key="constraintForm.and" />:</b>
								<input type="text" name="d2" id="d2" />
								<A HREF="#" onClick="cal.select(document.forms['qEditPaneForm'].d2,'d1popup','dd/MM/yyyy'); return false;" NAME="d2popup" ID="d2popup"><img src="images\iconCalendar.gif" height="16" width"=15"></A>
							</div>

						</td>
						<td>
					</tr>
					<tr id="constraintIntegerValues" style="display:none">
						<td>
							<b><fmt:message key="constraintForm.values" />:</b>
							<input type="text" name="i1" id="i1" />
							<div id="constraintIntegerValuesBetween" style="display:none">
								<b><fmt:message key="constraintForm.and" />:</b>
								<input type="text" name="i2" id="i2" />
							</div>
						</td>
						<td>
					</tr>
					
					

				</table>
			</fieldset>


			<input type="hidden" id="cid" name="cid" />
			<input type="hidden" id="question_id" name="question_id" />
			<input type="hidden" id="cVersion" name="cVersion" value="" />



		</form>

		<input class="f-submit" style="display:none" type="button" value="insert" onclick="sc.saveInsertButton()" id="saveInsertButton" />
		<input class="f-submit" style="display:none" type="button" value="save" onclick="sc.saveNewButton(false)" id="saveButton" />
		<input class="f-submit" style="display:none" type="button" value="save & add" onclick="sc.saveNewButton(true)" id="saveAndNewButton" />
		<input class="f-submit" style="display:none" type="button" value="update" onclick="sc.updateButton()" id="updateButton" />
		<input class="f-submit" style="display:none" type="button" value="cancel" onclick="sc.cancelButton()" id="cancelButton" />


	</div>

</body>
