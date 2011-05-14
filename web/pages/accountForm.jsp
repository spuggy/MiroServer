<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="accountDetail.title"/></title>
<content tag="heading"><fmt:message key="accountDetail.heading"/></content>
<meta name="menu" content="sysAdmin"/>

<spring:bind path="account.*">
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

<form:form commandName="account" method="post" action="editAccount.html" onsubmit="return validateAccount(this)" id="accountForm">
<ul>

    <li>
        <mobriz4server:label styleClass="desc" key="account.companyName"/>
        <form:errors path="companyName" cssClass="fieldError"/>
        <form:input path="companyName" id="companyName" cssClass="text medium"/>
    </li>

<form:hidden path="id"/>

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('Account')" value="<fmt:message key="button.delete"/>" />
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('accountForm'));
</script>

<v:javascript formName="account" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
