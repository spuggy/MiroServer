<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="webPageDetail.title"/></title>
<content tag="heading"><fmt:message key="webPageDetail.heading"/></content>
<meta name="menu" content="admin"/>
<spring:bind path="webPage.*">
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

<form:form commandName="webPage" method="post" action="editWebPage.html" onsubmit="return validateWebPage(this)" id="webPageForm">
<ul>

    <li>
        <mobriz4server:label styleClass="desc" key="webPage.description"/>
        <form:errors path="description" cssClass="fieldError"/>
        <form:input path="description" id="description" cssClass="text medium"/>
    </li>

<form:hidden path="id"/>

    <li>
        <mobriz4server:label styleClass="desc" key="webPage.menuContextId"/>
        <form:errors path="menuContextId" cssClass="fieldError"/>
        <form:radiobutton path="menuContextId" id="menuContextId" value="1"/>MiroProjectMenu
        <form:radiobutton path="menuContextId" id="menuContextId" value="2"/>Accounts
        <form:radiobutton path="menuContextId" id="menuContextId" value="3"/>Admin
        <form:radiobutton path="menuContextId" id="menuContextId" value="4"/>Help
        <form:radiobutton path="menuContextId" id="menuContextId" value="5"/>Reports
        
        
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="webPage.pageName"/>
        <form:errors path="pageName" cssClass="fieldError"/>
        <form:input path="pageName" id="pageName" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="webPage.pageText"/>
        <form:errors path="pageText" cssClass="fieldError"/>
        <form:textarea  path="pageText" id="pageText" cssClass="editor"/>
    
    </li>

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('WebPage')" value="<fmt:message key="button.delete"/>" />
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('webPageForm'));
</script>

<v:javascript formName="webPage" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
