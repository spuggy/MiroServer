<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.*" %>

<title>Batch Monitor</title>
<content tag="heading">Batch Monitor</content>
<meta name="menu" content="sysAdmin"/>


<p><c:out value="${status}" /></p>

<c:forEach var="result" items="${unprocessedResponses}">

    <br/><c:out value="${result}" />

</c:forEach>

<c:forEach var="result" items="${unprocessedTeamResponses}">

    <br/><c:out value="${result}" />

</c:forEach>
