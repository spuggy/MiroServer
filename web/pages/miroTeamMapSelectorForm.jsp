<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="miroTeamPie.title" />
	</title>
	<content tag="heading">
	<fmt:message key="miroTeamPie.heading" />
	</content>
	<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />
	<meta name="menu" content="MiroProjectMenu" />
	<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />
	<script type="text/javascript"
		src="<c:url value='/scripts/selectbox.js'/>"></script>
	<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/${appConfig["csstheme"]}/miroPickList.css'/>" />

</head>
<ul>
	<li>
<spring:bind path="miroProjectSelectorForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

</li>
	<li>
		
		<div class="message" id="welcommes" >
			<p><fmt:message key="miroTeamPie.intro" /></p>
	</div>
		
	</li>

	<li>
		<form:form commandName="miroProjectSelectorForm" method="post"
			action="" id="miroProjectSelectorForm" onsubmit="return onFormSubmit(this)">
			
			<fieldset class="pickList">
				<legend>
					<fmt:message key="miroProjectSelectorForm.selectProjects" />
				</legend>
				<table class="pickList">
					<tr>
						<th class="pickLabel">
							<mobriz4server:label key="miroProjectSelectorForm.projects"
								colon="false" styleClass="required" />
						</th>
						<td>&nbsp;</td>
						<th class="pickLabel">
							<mobriz4server:label
								key="miroProjectSelectorForm.includedProjects" colon="false"
								styleClass="required" />
						</th>
					</tr>
					<c:set var="leftList" value="${myProjects}" scope="request" />
					<c:set var="rightList" value="${emptyList}" scope="request" />
					<c:import url="/common/pickList.jsp">
						<c:param name="listCount" value="1" />
						<c:param name="leftId" value="availableProjects" />
						<c:param name="rightId" value="selectedProjects" />
					</c:import>
					<tr>
					<td colspan="3">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="3" id="miroProjectsSubmitButton" >
							<input type="submit"  class="button" name="save" onclick=""
								value="<fmt:message key="button.submit"/>" />
						</td>
					</tr>
				</table>
			</fieldset>
			
        
        
   
			
			
		</form:form>

	</li>

	<li class="buttonBar bottom">

	</li>
</ul>
<script type="text/javascript">

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    selectAll('selectedProjects');
    return true;
}
</script>

