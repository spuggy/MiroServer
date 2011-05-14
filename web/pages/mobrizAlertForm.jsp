<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="mobrizAlertDetail.title"/></title>
<content tag="heading"><fmt:message key="mobrizAlertDetail.heading"/></content>
<meta name="menu" content="surveyAdmin"/>
	
<spring:bind path="mobrizAlert.*">
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

<form:form commandName="mobrizAlert" method="post" action="editMobrizAlert.html" onsubmit="return validateMobrizAlert(this)" id="mobrizAlertForm">
<form:hidden path="version" />
<form:hidden path="id"  />
<ul>
 <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('MobrizAlert')" value="<fmt:message key="button.delete"/>" />
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
    </li>

<li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.alertTitle"/>
        <form:errors path="alertTitle" cssClass="fieldError"/>
        <form:input path="alertTitle" id="alertTitle" cssClass="text large"/>
    </li>



   
  
    	<li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.executionOrder"/>
        <form:errors path="executionOrder" cssClass="fieldError"/>
        <form:input path="executionOrder" id="executionOrder" cssClass="text small"/>
    </li>

    
    <li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.enabled"/>
        <form:errors path="enabled" cssClass="fieldError"/>
        <form:checkbox path="enabled" id="enabled" />
    </li>

   
    <li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.stopProcessing"/>
        <form:errors path="stopProcessing" cssClass="fieldError"/>
        <form:checkbox path="stopProcessing" id="stopProcessing" />
    </li>
     
    <li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.emailAdress"/>
        <form:errors path="emailAdress" cssClass="fieldError"/>
        <form:input path="emailAdress" id="emailAdress" cssClass="text large"/>
    </li>
    
      <li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.emailSubject"/>
        <form:errors path="emailSubject" cssClass="fieldError"/>
        <form:input path="emailSubject" id="emailSubject" cssClass="text large"/>
    </li>
    
      <li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.emailBody"/>
        <form:errors path="emailBody" cssClass="fieldError"/>
         <form:textarea path="emailBody" id="emailBody" cssClass="text large"/>
    </li>
    
      
    <li>
        <mobriz4server:label styleClass="desc" key="mobrizAlert.alertRules"/>
        <form:errors path="alertRules" cssClass="fieldError"/>
        <form:textarea path="alertRules" id="alertRules" cssClass="editor"/>
       
    </li>

 
   
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('mobrizAlertForm'));
</script>

<v:javascript formName="mobrizAlert" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
