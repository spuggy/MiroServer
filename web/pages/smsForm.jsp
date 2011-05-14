<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <content tag="heading"><fmt:message key="sms.heading"/></content>
    <meta name="menu" content="admin"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/sms.css'/>" />
	

<script  >
function validateSMSForm(frm){

   if(bCancel) {
   		this.close();
   		return;
   }

   if(frm.phoneNumber.value == "") {
   		alert("please enter a phone number!") ;
   		return false ; 
   }
   
   if(frm.smsMessage.value.length > 155) {
   		alert("Your message is too long please limit to 155 characters!") ;
   		return false ; 
   	}
   
    frm.smsMessage.value = frm.smsMessage.value.replace(/\r|\n|\r\n/g, " ") ; 



	return true ;

}



</script>
</head>



<spring:bind path="smsMessageForm.*">
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


<form:form commandName="smsMessageForm" method="post" action="sms.html" onsubmit="return validateSMSForm(this)" id="smsMessageForm">


<div style="padding: 20px">
<ul>

    <li>
        <mobriz4server:label styleClass="desc" key="user.username"/>        
        <c:out value="${smsMessageForm.username}"/>
    </li>



 	<li>
        <mobriz4server:label styleClass="desc" key="user.phoneNumber"/>
        <form:errors path="phoneNumber" cssClass="fieldError"/>
        <form:input path="phoneNumber" id="phoneNumber" cssClass="text medium"/>
    </li>
       
    <li>
        <mobriz4server:label styleClass="desc" key="user.smsMessage"/>
        <form:errors path="smsMessage" cssClass="fieldError"/>
        <form:textarea path="smsMessage" id="smsMessage" cssClass="text medium"/>
    </li>
       
	
     <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />        
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>		
</form:form>



