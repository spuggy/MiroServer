<%@ include file="/common/taglibs.jsp"%>

<%@ page import="java.util.*" %>

<title><fmt:message key="batchProcess.results"/></title>
<content tag="heading">Batch Process Results</content>
<meta name="menu" content="sysAdmin"/>



<c:forEach var="result" items="${batchProcessResults}">
                
        <br/><c:out value="${result}" />
           
</c:forEach>