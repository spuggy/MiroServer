<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="addCreditsSucess.title" />
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

<fmt:message key="addCreditsSucess.message"><fmt:param value="${user.creditBalance}"/></fmt:message>
</p>
<p><a href="miroProjects.html"><fmt:message key="addCreditsSucess.returnToProjectsMessage"/></a></p>
