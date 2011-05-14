<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="surveyUpload.title"/></title>
<content tag="heading"><fmt:message key="surveyUpload.heading"/></content>
<meta name="menu" content="surveyAdmin"/>

<!--
    The most important part is to declare your form's enctype to be "multipart/form-data"
-->

<spring:bind path="surveyUploadForm.*">
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



<div class="separator"></div>

<form:form commandName="surveyUploadForm" method="post" action="surveyUpload.html" enctype="multipart/form-data"
    onsubmit="return validateFileUpload(this)" id="surveyUploadForm">
    
    <ul>
    <li class="info">    
        <fmt:message key="surveyUpload.message"/>
    </li>
    
    <li>
        <mobriz4server:label key="uploadForm.file" styleClass="desc"/>
        <form:errors path="file" cssClass="fieldError"/>
        <spring:bind path="surveyUploadForm.file">
        <input type="file" name="file" id="file" class="file medium" value="<c:out value="${status.value}"/>"/>
        </spring:bind>
    </li>
  
    <li class="buttonBar bottom">
        <input type="submit" name="upload" class="button" onclick="bCancel=false"
            value="<fmt:message key="button.upload"/>" />
        <input type="submit" name="cancel" class="button" onclick="bCancel=true"
            value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
    
    
    
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('uploadForm'));
    highlightFormElements();
</script>
<v:javascript formName="surveyUploadForm" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
