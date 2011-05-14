<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="addCredits.heading"/></title>
<script type="text/javascript">
function validateForm(form) {

	// ** START **
  if (form.credits.value == "") {
    alert( "Please enter the hunber of credits you require." );
    form.credits.focus();
    return false ;
  }
  
  if(!isNumeric(form.credits.value)) {
 	 alert( "Please enter a numeric value greater than zero." );	
  	 return false;
  }
  
  if(form.credits.value < 1) {
   	alert( "Please enter a numeric value greater than zero." );	
  	 return false;
  }
  // ** END **
  return true ;
	


}


function isNumeric(value) {
  	    if (value == null || !value.toString().match(/^[-]?\d*\.?\d*$/)) return false;
       return true;
}
  

</script>
<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />
</head>
<content tag="heading"><fmt:message key="addCredits.heading"/></content>
<meta name="menu" content="MiroProjectMenu"/>
<spring:bind path="miroTransaction.*">
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

<p><fmt:message key="addCredits.message"/></p>

<form:form commandName="miroTransaction" method="post" action="addCredits.html" onsubmit="return validateForm(this)" id="miroTransactionForm">
<ul>

<form:hidden path="id"/>

    <li>
        <form:errors path="credits" cssClass="fieldError"/>
        <form:input path="credits" id="credits" cssClass="text medium"/> <fmt:message key="addCredits.price"><fmt:param value="${miroCreditPrice}"/></fmt:message>
    </li>
   

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.next"/>" />
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('credits'));
</script>

<v:javascript formName="miroTransactionForm" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
