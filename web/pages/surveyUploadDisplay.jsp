<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="display.title"/></title>
<content tag="heading"><fmt:message key="display.heading"/></content>
<meta name="menu" content="surveyAdmin"/>

<div class="separator"></div>

<table class="detail" cellpadding="5">
    <tr>
    	
    	<td>The file <c:out value="${fileName}"/> has been successfully imported as <b><c:out value="${surveyTitle}"/></b>.</td>
    </tr>
    	<td><a href="editSurvey.html?id=<c:out value="${sid}"/>" >Edit this survey</a><br/>
		
    
</table>


