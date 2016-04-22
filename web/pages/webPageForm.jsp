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

<div class="row">
    <div class="col-sm-8">

        <form:form commandName="webPage" method="post" action="editWebPage.html" onsubmit="return validateWebPage(this)" id="webPageForm">
           <form:hidden path="id"/>

        <button class="btn btn-primary" name="save" onclick="bCancel=false"><fmt:message key="button.save"/></button>
        <button class="btn btn-default" name="cancel" onclick="bCancel=true"><fmt:message key="button.cancel"/></button>
        <c:if test="${!empty webPage.id}">
        <button class="btn btn-default" name="delete" onclick="bCancel=true;return confirmDelete('WebPage')">
            <fmt:message key="button.delete"/></button>
        </c:if>
        <button type="button" class="btn" name="preview" onclick="webPagePreview()">preview</button>


        <div class="form-group">
            <mobriz4server:label styleClass="control-label" key="webPage.description"/>
            <form:errors path="description" cssClass="fieldError"/>
            <form:input path="description" id="description" cssClass="form-control"/>
        </div>

        <div class="form-group">
            <mobriz4server:label styleClass="control-label" key="webPage.pageType"/>
            <form:errors path="pageType" cssClass="fieldError"/>
            <form:radiobutton path="pageType" id="pageType"  value="0"/> Web Page
            <form:radiobutton path="pageType" id="pageType"  value="1"/> Blog
        </div>

        <div class="form-group">
            <mobriz4server:label styleClass="control-label" key="webPage.menuContextId"/>
            <form:errors path="menuContextId" cssClass="fieldError"/>
            <form:radiobutton path="menuContextId" id="menuContextId"  value="1"/> MiroProjectMenu
            <form:radiobutton path="menuContextId" id="menuContextId"  value="2"/> Accounts
            <form:radiobutton path="menuContextId" id="menuContextId"  value="3"/> Admin
            <form:radiobutton path="menuContextId" id="menuContextId"  value="4"/> Help
        </div>

        <div class="form-group">
            <mobriz4server:label styleClass="control-label" key="webPage.pageName"/>
            <form:errors path="pageName" cssClass="fieldError"/>
            <form:input path="pageName" id="pageName" cssClass="form-control"/>
        </div>

        <div class="form-group">
            <mobriz4server:label styleClass="control-label" key="webPage.pageStatus"/>
            <form:errors path="pageStatus" cssClass="fieldError"/>
            <form:radiobutton path="pageStatus" id="pageStatus"  value="0"/> DRAFT
            <form:radiobutton path="pageStatus" id="pageStatus"  value="1"/> PUBLISHED
        </div>

        <div class="form-group">
            <mobriz4server:label styleClass="control-label" key="webPage.publishedDate"/>
            <form:errors path="publishedDate" cssClass="fieldError"/>
            <form:input path="publishedDate" id="publishedDate" cssClass="form-control"/>
        </div>

        <div class="form-group">
            <mobriz4server:label styleClass="control-label" key="webPage.pageText"/>
            <form:errors path="pageText" cssClass="fieldError"/>
            <form:textarea  path="pageText" id="pageText" cssClass="form-control" rows="50"/>
        </div>

</form:form>

<v:javascript formName="webPage" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
<script type="text/javascript">
  function webPagePreview() {
      var w = window.open("","preview");
      w.location='content.html?name='+$("#pageName").val();
  }

</script>



