<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="miroTeamMap.title" />
	</title>
	<content tag="heading">
    <fmt:message key="miroTeamMap.heading" />
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
		
		<div class="message" id="welcommes" >
			<p><fmt:message key="miroTeamPie.intro" /></p>
			
			<p><br/><input type="button" value="New Team Map" onclick="$('teamBuilder').show()"/></p>
			
	</div>
		
	</li>

	
	<li id="teamBuilder" style="display: none">
		<form:form commandName="miroProjectSelectorForm" method="post"
			action="editMiroTeamMap.html" id="miroProjectSelectorForm" onsubmit="return onFormSubmit(this)">
			
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
							<input type="submit"  class="button" name="selectprojects" onclick=""
								value="<fmt:message key="button.submit"/>" />
							<input type="button" value="Hide team builder" onclick="$('teamBuilder').hide()"/>
						</td>
					</tr>
				</table>
			</fieldset>
			
        
        
   
			
			
		</form:form>

	</li>

	<li>

<display:table name="miroTeamList" cellspacing="0" cellpadding="0" requestURI=""
    id="miroTeamList" pagesize="25" class="table miroTeamList" export="false" decorator="uk.co.bluetrail.mobriz.webapp.util.MiroTeamTableDecorator">
   
    <display:column property="miroTeamName" escapeXml="true" sortable="true"
         titleKey="miroteam.miroteamname"  url="/editMiroTeamMap.html" paramId="id" paramProperty="id"
        />
    <display:column property="updated_at"  
         titleKey="surveyForm.updated_at" decorator="uk.co.bluetrail.mobriz.webapp.util.ShortDateDecorator" />
             
    <display:column property="teamReportStatus"  
         titleKey="miroteam.miroteamreportstatus" />
    
             
    
    <display:setProperty name="paging.banner.item_name" value="miroTeam"/>
    <display:setProperty name="paging.banner.items_name" value="miroTeams"/>
</display:table>

	</li>
</ul>
<script type="text/javascript">

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    selectAll('selectedProjects');
    return true;
}
</script>

