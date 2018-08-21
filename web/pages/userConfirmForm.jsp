<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="miroSurvey.title"/></title>

<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<div class="row">
    <div id="miro-content" class="col-sm-6">

<content tag="heading"><fmt:message key="miroSurvey.heading"/></content>
<content tag="shortheading"><fmt:message key="miroSurvey.shortheading"/></content>

<p>Have we got your name right? You can edit it here</p>
<form:form commandName="user" method="post" action="userConfirm.html" id="userForm" >

    <div >

        <div class="form-group">
            <mobriz4server:label styleClass="desc" key="user.firstName"/>
            <form:errors path="firstName" cssClass="fieldError"/>
            <form:input cssClass="form-control" path="firstName" id="firstName" maxlength="50"/>
        </div>
        <div class="form-group">
            <mobriz4server:label styleClass="desc" key="user.lastName"/>
            <form:errors path="lastName" cssClass="fieldError"/>
            <form:input path="lastName" id="lastName" cssClass="form-control" maxlength="50"/>
        </div>

        <p>Your privacy and data security is important to us. Before you continue to the assessment, please tick
            the box to say that you agree to the use of your personal data in line with our <a href="javascript:void(0);" onclick="window.open('https://miro-assessment.com/privacy.html', '_blank', 'width=640,height=480');">privacy statement</a></p>

        <div class="form-group">
            <fieldset>
                <form:checkbox path="gdpr" id="gdpr"/><label for="gdpr" class="choice"> I agree to use of my personal data in line with MiRo's privacy statement.</label>
            </fieldset>
        </div>

        <button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false" >Continue</button>

    </div>

</form:form>