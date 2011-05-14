<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="addCreditsConfirm.title" />
</title>
<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />
</head>
<content tag="heading">
<fmt:message key="addCredits.heading" />
</content>
<meta name="menu" content="MiroProjectMenu" />


<div class="separator"></div>

<p>

<c:if test="${miroTransaction.credits == 1}">
 <fmt:message key="addCreditsConfirm.message"><fmt:param value="${miroTransaction.credits}"/><fmt:param value="${miroCreditPrice}"/></fmt:message>
</c:if> 
<c:if test="${miroTransaction.credits > 1}">
 <fmt:message key="addCreditsConfirm.messageplural"><fmt:param value="${miroTransaction.credits}"/><fmt:param value="${miroCreditPrice}"/></fmt:message>
</c:if> 
</p>


<p>You order total is <b><c:out value="${miroTransaction.transValue}" /></b></p>
<!-- This form is all that is required to submit the payment information to the VSP system -->
<form action="<c:out value="${VPSPostUrl}"/>" method="POST" id="VSPForm"
	name="VSPForm">
	<input type="hidden" name="VPSProtocol"	value="<c:out value="${VPSProtocol}"/>">
	<input type="hidden" name="TxType" value="<c:out value="${TxType}"/>">
	<input type="hidden" name="Vendor" value="<c:out value="${Vendor}"/>">
	<input type="hidden" name="Crypt" value="<c:out value="${crypt}"/>">
	<input type="submit" name="proceed" value="Proceed to Payment">
	<input type="button" name="cancel" value="Cancel" onclick="location='miroProjects.html'"/>
</form>
