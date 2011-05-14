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

<ul>
<li>

<content tag="heading">MiRo Behavioural Mode Assessment
</content>
<p>Please confirm your first and last name and then press continue. </p>
<form:form commandName="user" method="post" action="userConfirm.html" id="userForm">


<ul>
   <li>
   
   </li>
   
   
    <li>
        <div class="left">
            <mobriz4server:label styleClass="desc" key="user.firstName"/>
            <form:errors path="firstName" cssClass="fieldError"/>
            <form:input path="firstName" id="firstName" cssClass="text medium" maxlength="50"/>
        </div>
        <div>
            <mobriz4server:label styleClass="desc" key="user.lastName"/>
            <form:errors path="lastName" cssClass="fieldError"/>
            <form:input path="lastName" id="lastName" cssClass="text medium" maxlength="50"/>
        </div>
    </li>
     <li >
           <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="Continue"/>
        </c:set>
        <br/>
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>

</form:form>

</li>
</ul>