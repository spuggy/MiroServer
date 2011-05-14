<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="miroProjectDetail.title"/></title>
<content tag="heading"><fmt:message key="miroProjectDetail.heading"/></content>
<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />
<meta name="menu" content="MiroProjectMenu"/>
<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />

</head>
<spring:bind path="miroProject.*">
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

<ul>
<li>
<div class="message" id="welcommess" >
		
		<p>You are about to set a MiRo project. Here you can give your project a name and make a few notes for your self. You can also create the text of the email that candidates will receive when you send out their log in details.  
	</p>
	</div>
</li>
<li>
<form:form commandName="miroProject" method="post" action="editMiroProject.html" onsubmit="return validateMiroProject(this)" id="miroProjectForm">
<form:hidden path="version" />
<form:hidden path="id"  />


<ul>
	

	<li>
        <mobriz4server:label styleClass="desc" key="miroProject.projectTitle"/>
        <form:errors path="projectTitle" cssClass="fieldError"/>
        <form:input path="projectTitle" id="projectTitle" cssClass="text large"/>
    </li>

 	<li>
        <mobriz4server:label styleClass="desc" key="miroProject.projectDescription"/>
        <form:errors path="projectDescription" cssClass="fieldError"/>
        <form:textarea path="projectDescription" id="projectDescription" cssClass="text large"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroProject.emailInviteText"/>
        <form:errors path="emailInviteText" cssClass="fieldError"/>
        <form:textarea path="emailInviteText" id="emailInviteText" cssClass="text large"/>
    </li>
   

 <li>
        <mobriz4server:label styleClass="desc" key="miroProject.emailInviteSubject"/>
        <form:errors path="emailInviteSubject" cssClass="fieldError"/>
        <form:input path="emailInviteSubject" id="emailInviteSubject" cssClass="text large"/>
    </li>
   

  
    
    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
        <c:if test="${!empty miroProject.id}" > 
   			<input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('MiroProject')" value="<fmt:message key="button.delete"/>" />
     	</c:if>
        
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('miroProjectForm'));
</script>

<v:javascript formName="miroProject" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
</li>
</ul>