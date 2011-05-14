<%@ include file="/common/taglibs.jsp"%>
<head>
	<meta name="menu" content="surveyAdmin"/>
	

	<title><fmt:message key="surveyDetail.title" /></title>

<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/surveyForm.css'/>" />
	
	<script type="text/javascript">




</script>
</head>
<body >

<content tag="heading"><fmt:message key="surveyForm.newSurvey"/></content>

<form:form commandName="survey" method="post" action=""  id="surveyForm">
	
		<table id="surveyDetail">
			<form:hidden path="version" id="sversion" />
			<form:hidden path="id" id="survey_id" />
			<form:hidden path="firstQuestion_id"  />
			<form:hidden path="common" />
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
					<th>&nbsp;</th>
					<td nowrap>
					    <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
            		</td>
				</tr>
			</tbody>
		</table>

		<%@ include file="/WEB-INF/pages/surveyElement.jsp"%>
	</form:form>



</body>

<v:javascript formName="surveyForm" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>

