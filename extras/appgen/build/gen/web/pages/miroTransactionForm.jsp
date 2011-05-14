<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="miroTransactionDetail.title"/></title>
<content tag="heading"><fmt:message key="miroTransactionDetail.heading"/></content>

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

<form:form commandName="miroTransaction" method="post" action="editMiroTransaction.html" onsubmit="return validateMiroTransaction(this)" id="miroTransactionForm">
<ul>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.credits"/>
        <form:errors path="credits" cssClass="fieldError"/>
        <form:input path="credits" id="credits" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.errorCode"/>
        <form:errors path="errorCode" cssClass="fieldError"/>
        <form:input path="errorCode" id="errorCode" cssClass="text medium"/>
    </li>

<form:hidden path="id"/>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.paymentMethod"/>
        <form:errors path="paymentMethod" cssClass="fieldError"/>
        <form:input path="paymentMethod" id="paymentMethod" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.status"/>
        <form:errors path="status" cssClass="fieldError"/>
        <form:input path="status" id="status" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.user_id"/>
        <form:errors path="user_id" cssClass="fieldError"/>
        <form:input path="user_id" id="user_id" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.deleted"/>
        <form:errors path="deleted" cssClass="fieldError"/>
        <form:input path="deleted" id="deleted" cssClass="text medium"/>
    </li>

<form:hidden path="version"/>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.checkPoint"/>
        <form:errors path="checkPoint" cssClass="fieldError"/>
        <form:input path="checkPoint" id="checkPoint" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.created_on"/>
        <form:errors path="created_on" cssClass="fieldError"/>
        <form:input path="created_on" id="created_on" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.createdBy_id"/>
        <form:errors path="createdBy_id" cssClass="fieldError"/>
        <form:input path="createdBy_id" id="createdBy_id" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.lastUpdatedBy_id"/>
        <form:errors path="lastUpdatedBy_id" cssClass="fieldError"/>
        <form:input path="lastUpdatedBy_id" id="lastUpdatedBy_id" cssClass="text medium"/>
    </li>

    <li>
        <mobriz4server:label styleClass="desc" key="miroTransaction.updated_at"/>
        <form:errors path="updated_at" cssClass="fieldError"/>
        <form:input path="updated_at" id="updated_at" cssClass="text medium"/>
    </li>

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('MiroTransaction')" value="<fmt:message key="button.delete"/>" />
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('miroTransactionForm'));
</script>

<v:javascript formName="miroTransaction" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
