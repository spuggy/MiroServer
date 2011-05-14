<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="settingDetail.title"/></title>
<content tag="heading"><fmt:message key="settingDetail.heading"/>:  <c:out value="${setting.settingName}"/></content>
<meta name="menu" content="admin"/>

<spring:bind path="setting.*">
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

<form:form commandName="setting" method="post" action="editSetting.html" onsubmit="return validateSetting(this)" id="settingForm">
<ul>

<form:hidden path="id"/>
<form:hidden path="settingDescription"/>
<form:hidden path="settingName"/>


    <li>            
        <c:out value="${setting.settingDescription}"/>
    </li>

    
    <li>
        <mobriz4server:label styleClass="desc" key="setting.settingValue"/>
        <form:errors path="settingValue" cssClass="fieldError"/>
        <form:textarea path="settingValue" id="settingValue" cssClass="textarea"/>
    </li>

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('Setting')" value="<fmt:message key="button.delete"/>" />
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('settingForm'));
</script>

<v:javascript formName="setting" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
