<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="miroTeamMap.title" />
</title>
<head>
    <script type="text/javascript" src="<c:url value='/scripts/jquery-1.11.1.min.js'/>"></script>
    <script type="text/javascript"
		src="<c:url value='/scripts/selectbox.js'/>"></script>
	<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/${appConfig["csstheme"]}/miroPickList.css'/>" />

<script type="text/javascript">

function bigCommentary() {

	$('#biggercommentary').addClass('hideme');
	$('#smallercommentary').removeClass('hideme');
	$('#commentary').removeClass('small_comments');
	$('#commentary').addClass('big_comments');

}

function smallCommentary() {

	$('#biggercommentary').removeClass('hideme');
	$('#smallercommentary').addClass('hideme');
	$('#commentary').addClass('small_comments');
	$('#commentary').removeClass('big_comments');

}


</script>

</head>
<content tag="heading">
<fmt:message key="miroTeamReport.heading" />
</content>
<meta name="menu" content="MiroProjectMenu" />



<ul>

	<li>					
	<form:form commandName="miroProjectSelectorForm" method="post" action="" id="miroProjectSelectorForm" onsubmit="return validateForm(this)">
	
		<c:if test="${showReportInprogressMessage == true}">
			<div class="message" >
			The Team report is being generated - please check back shortly!
			</div>
			</c:if>
			
			<c:if test="${showDownloadLink == true}">
			<div class="message" >
			
			<p>A report has been generated for this team.  Click the link below to download.</p>
			<br/>
			
			<a href="miroTeamReportShow.html?id=<c:out value="${miroTeam.id}" />" /><img  src="images/pdf_large.png" /></a>
			</div>
			</c:if>
		
	    <c:if test="${showRecalcEditButtons == true}">
			
		<div class="message" id="welcommes">
			<p>
				<fmt:message key="miroTeamMap.instructions" />
			</p>
			
			<p><br/><input type="button" value="edit team members" onclick="$('#teamBuilder').show()"/>
			<input type="button" value="re-select projects" onclick="location='miroTeamList.html';"/>
			

			 <c:if test="${showTeamSaveCreateButtons == true}">
			<input type="submit" value="save team report" name="save" onclick="bSave=true;" />
			
			<c:if test="${showDeleteButton == true}">
					<input type="submit" value="delete team map" name="delete" onclick="bDelete=true;"/>
			</c:if>
			
			<input type="submit" value="create team report" name="createteamreport" onclick="bSave=true;bConfirm=<c:out value="${confirmReportCreation}" />" />

            </c:if>
			</p>
		
		
		
<p>
	<br/>
	
	<c:out value="${miroProjectSelectorForm.id}" />

	<div id="teamBuilder" style="display: none">
	
			
				<form:hidden path="id"/>
			<form:hidden path="version"/>
			
			
        <mobriz4server:label styleClass="desc" key="miroteam.miroteamname"/>
        <form:input path="miroTeamName" id="miroTeamName" cssClass="text large"/>
        
        <div class="blockme"><mobriz4server:label styleClass="desc_inline" key="miroTeamMap.commentary"/> <a href="#" id="biggercommentary" onclick="bigCommentary()" class="bigger_box">(Make Bigger)</a><a id="smallercommentary" onclick="smallCommentary()" href="#" class="hideme smaller_box">(Make Smaller)</a></div> 
        <form:textarea path="commentary" id="commentary" cssClass="text small_comments"/>
       	
			
			
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
							<input type="button" value="Hide team builder" onclick="$('#teamBuilder').hide()"/>
							<input type="submit"  class="button" name="recalc_chart" value="recalc" />	
						</td>
					</tr>
				</table>
			</fieldset>
				
			
			

    </div>

    </p>
    		</div>
    		
    		</c:if>
			
    		
        </form:form>
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

<script type="text/javascript" language="Javascript1.1"> 

<!-- Begin 

     var bSave = false; 

    function validateForm(form) {                                                                   
       
        selectAll('teamUsers');
       
        if (bSave) {
      		 if(form["miroTeamName"].value== "") {
      			$('#teamBuilder').show();
      		 	alert("Please supply a value for the Team Name");
      		 	return false; 
      		 
      		 } else {

                if(bConfirm) {
                     return confirm("Only individuals addedd to a team report for the first time will be charged to your account. By clicking OK you agree to pay for the inclusion of these individuals. Do you wish to continue?");
                } else {
                    return true;
                }



      		 }
      		 
        } 
        
        if (bDelete) {
        
        	if(confirm("Are you sure you want to delete?")) {
        		return true;
        	} else {
        		return false;
        	}
        
        }
        
        return false;
   } 

   


//End --> 
</script>



