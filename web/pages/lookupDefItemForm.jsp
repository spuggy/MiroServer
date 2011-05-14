<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="lookupDefItemDetail.title"/></title>
<content tag="heading"><fmt:message key="lookupDefItemDetail.heading"/></content>

<spring:bind path="lookupDefItem.*">
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

<form:form commandName="lookupDefItem" method="post" action="editLookupDefItem.html" onsubmit="return validateLookupDefItem(this)" id="lookupDefItemForm">
<ul>

<form:hidden path="id"/>

<c:choose>

  <c:when test="${empty param.lookupDef_id}">
 
  	<form:hidden path="lookupDef_id"/>
  </c:when>
  <c:otherwise>
 
	<input name="lookupDef_id" id="lookupDef_id" type="hidden" value="<c:out value="${param.lookupDef_id}" />"/>
  
  </c:otherwise>

</c:choose>

    <li>
    	<c:if test="${not empty lookupDef.lookupDesc1}">
        	<label class="desc"><c:out value="${lookupDef.lookupDesc1}"></c:out></label>
        	<form:errors path="lookup1" cssClass="fieldError"/>
        	<form:input path="lookup1" id="lookup1" cssClass="text medium"/>
        </c:if>
    </li>
    
    <li>
    	<c:if test="${not empty lookupDef.lookupDesc2}">
        	<label class="desc"><c:out value="${lookupDef.lookupDesc2}"></c:out></label>
        	<form:errors path="lookup2" cssClass="fieldError"/>
        	<form:input path="lookup2" id="lookup2" cssClass="text medium"/>
        </c:if>
    </li>

    <li>
        <c:if test="${not empty lookupDef.lookupDesc3}">
        	<label class="desc"><c:out value="${lookupDef.lookupDesc3}"></c:out></label>
        	<form:errors path="lookup3" cssClass="fieldError"/>
        	<form:input path="lookup3" id="lookup3" cssClass="text medium"/>
        </c:if>
    </li>

    <li>
        <c:if test="${not empty lookupDef.lookupDesc4}">
        	<label class="desc"><c:out value="${lookupDef.lookupDesc4}"></c:out></label>
        	<form:errors path="lookup4" cssClass="fieldError"/>
        	<form:input path="lookup4" id="lookup4" cssClass="text medium"/>
        </c:if>
    </li>

    <li>
        <c:if test="${not empty lookupDef.lookupDesc5}">
        	<label class="desc"><c:out value="${lookupDef.lookupDesc5}"></c:out></label>
        	<form:errors path="lookup5" cssClass="fieldError"/>
        	<form:input path="lookup5" id="lookup2" cssClass="text medium"/>
        </c:if>
    </li>

  
   

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <c:if test="${not empty lookupDefItem.id}">
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('LookupDefItem')" value="<fmt:message key="button.delete"/>" />
        </c:if>
        <input type="button" class="button" name="cancel" onclick="history.go(-1);" value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('lookupDefItemForm'));
</script>

<v:javascript formName="lookupDefItem" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
