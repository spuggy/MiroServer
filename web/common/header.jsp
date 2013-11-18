<%@ include file="/common/taglibs.jsp"%>

<c:if test="${pageContext.request.locale.language != 'en'}">
    <div id="switchLocale"><a href="<c:url value='/?locale=en'/>"><fmt:message key="webapp.name"/> in English</a></div>
</c:if>

<div id="branding">
   <h1><a href="<c:url value="/"/>"><fmt:message key="webapp.name"/></a></h1>
	<c:if test="${pageContext.request.remoteUser != null}">
        <p><authz:authentication operation="fullName"/> <a href="logout.jsp"><fmt:message key="user.logout"/></a></p>
    </c:if>
</div>
<hr />

<%-- Put constants into request scope --%>
<mobriz4server:constants scope="request"/>