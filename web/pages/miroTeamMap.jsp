<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="miroTeamMap.title" />
</title>
<head>
<script type="text/javascript"
		src="<c:url value='/scripts/selectbox.js'/>"></script>
	<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/${appConfig["csstheme"]}/miroPickList.css'/>" />

</head>
<content tag="heading">
<fmt:message key="miroTeamMap.heading" />
</content>
<meta name="menu" content="MiroProjectMenu" />


<ul>

	<li>
		<div class="message" id="welcommes">
			<p>
				<fmt:message key="miroTeamMap.instructions" />
			</p>
			
			<p><br/><input type="button" value="edit team members" onclick="$('teamBuilder').show()"/>
			<input type="button" value="re-select projects" onclick="location='miroTeamMap.html';"/>
			<input type="button" value="save team map" onclick="alert('coming soon!');"/>
			<input type="button" value="delete team map" onclick="alert('coming soon!');"/>
			<input type="button" value="create team report" onclick="alert('coming soon!');"/>
			</p>
		
		
		
<p>
	<br/>

	<div id="teamBuilder" style="display: none">
	
		<form:form commandName="miroProjectSelectorForm" method="post" action="/mirotest/miroTeamMap.html" id="miroProjectSelectorForm" onsubmit="return onFormSubmit(this)">
			
			  <c:forEach var="project_id" items="${selectedProjects}" varStatus="status">
        		   <input type="hidden" name="selectedProjects" value="<c:out value="${project_id}"/>" />
      	   	  </c:forEach>
		     	
      	     	<fieldset class="pickList">
				<legend>
					<fmt:message key="miroProjectTeamsSelectorForm.selectTeam" />
				</legend>
				<table class="pickList">
					<tr>
						<th class="pickLabel">
							<mobriz4server:label key="miroProjectTeamsSelectorForm.notIncludedNames" colon="false" styleClass="required" />
						</th>
						<td>&nbsp;</td>
						<th class="pickLabel">
							<mobriz4server:label key="miroProjectTeamsSelectorForm.includedNames" colon="false"	styleClass="required" />
						</th>
					</tr>
					
					<c:set var="leftList" value="${unselectedUserList}" scope="request" />
					<c:set var="rightList" value="${teamUsers}" scope="request" />
					<c:import url="/common/pickList.jsp">
						<c:param name="listCount" value="1" />
						<c:param name="leftId" value="selectedUserList" />
						<c:param name="rightId" value="teamUsers" />
					</c:import>
					<tr>
					<td colspan="3">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="3" id="miroProjectsSubmitButton" >
							<input type="button" value="Hide team builder" onclick="$('teamBuilder').hide()"/>
							<input type="submit"  class="button" name="save" value="<fmt:message key="button.submit"/>" />	
						</td>
					</tr>
				</table>
			</fieldset>
				
			
			
		</form:form>
    </div>
    </p>
    		</div>
    
    </li>
	<li>
		<h2>
			<fmt:message key="miroTeamMap.teamMapImageLeading" />
		</h2>
		<img src="<c:url value='/teamImage/teamMapImageLeading.jpg?engaged=false'/>"
			width="600" height="450" />
	</li>
	<li>
		<h2>
			<fmt:message key="miroTeamMap.teamMapImageEngaged" />
		</h2>
		<img src="<c:url value='/teamImage/teamMapImageEngaged.jpg?engaged=true'/>"
			width="600" height="450" />
		
	</li>
	<li>
		<h2>
			<fmt:message key="miroTeamMap.teamMapNames" />
		</h2>
		<display:table name="teamMapData" cellspacing="0" cellpadding="0"
			requestURI="" id="teamMapData" pagesize="500"
			class="table" export="false">

			<display:column property="fullName" escapeXml="true"
				titleKey="miroTeamMap.fullName" />
			<display:column property="initials" escapeXml="true"
				titleKey="miroTeamMap.initials" />
			<display:column property="leadingMode" escapeXml="true"
				titleKey="miroTeamMap.leadingMode" />
			<display:column property="secondaryMode" escapeXml="true"
				titleKey="miroTeamMap.secondaryMode" />
						
    <display:setProperty name="paging.banner.item_name" value="Name" />
			<display:setProperty name="paging.banner.items_name" value="Names" />
		</display:table>
	<li>
</ul>

<script type="text/javascript">
<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    selectAll('teamUsers');
    return true;
}
</script>

