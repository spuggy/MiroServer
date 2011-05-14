<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Date" %>

<title><fmt:message key="menu.responses"/></title>
<content tag="heading"><fmt:message key="photoList"/>: <c:out value="${survey.title}" /> </content>
<meta name="menu" content="responses"/>



<c:forEach var="file" items="${survey.files}" >


	<img src="photos/2/
	<c:out value="${file.name}" />
	"/>

</c:forEach>







<script type="text/javascript">
    highlightTableRows("settingList");
</script>
