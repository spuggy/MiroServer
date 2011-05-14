<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="lookupDefDetail.title"/></title>
<content tag="heading"><fmt:message key="lookupDefDetail.heading"/></content>
<meta name="menu" content="surveyAdmin"/>

<spring:bind path="lookupDef.*">
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

<form:form commandName="lookupDef" method="post" action="editLookupDef.html" onsubmit="return validateLookupDef(this)" id="lookupDefForm">

<form:hidden path="id"/>

<ul>
    <li>
        <mobriz4server:label styleClass="desc" key="lookupDef.name"/>
        <form:errors path="name" cssClass="fieldError"/>
        <form:input path="name" id="name" cssClass="text medium"/>
    

    <li>
        <mobriz4server:label styleClass="desc" key="lookupDef.desc"/>
        <form:errors path="desc" cssClass="fieldError"/>
        <form:textarea cols="50" rows="3" path="desc" id="desc" />        
    </li>
    
   <li>
        <mobriz4server:label styleClass="desc" key="lookupDef.lookupPrompt"/>
        <form:errors path="lookupPrompt" cssClass="fieldError"/>
       <form:textarea cols="50" rows="3" path="lookupPrompt" id="lookupPrompt" />    
    </li>

</ul>



<table class="table" width="50%" >
<thead>
<tr><th class="sortable" >#</th><th class="sortable">Field Value</th><th class="sortable">Shown on Phone/Device</th><th class="sortable">Shown in Results</th></tr>
</thead>
<tbody>
<tr class="even">
    <td>
 1
    </td>
    <td>
        <form:errors path="lookupDesc1" cssClass="fieldError"/>
        <form:input path="lookupDesc1" id="lookupDesc1" cssClass="text medium"/>
    </td>
    <td>        
        <form:errors path="lookupSearch1" cssClass="fieldError"/>
        <form:checkbox path="lookupSearch1" id="lookupSearch1" />
	</td>
	<td>		
        <form:errors path="lookupReport1" cssClass="fieldError"/>
        <form:checkbox path="lookupReport1" id="lookupReport1" />
	</td>
</tr>
<tr class="odd">
	<td>
  2
    </td>
    <td>        
        <form:errors path="lookupDesc2" cssClass="fieldError"/>
        <form:input path="lookupDesc2" id="lookupDesc2" cssClass="text medium"/>
    </td>
    <td>        
        <form:errors path="lookupSearch2" cssClass="fieldError"/>
        <form:checkbox path="lookupSearch2" id="lookupSearch2" />
	</td>
	<td>		
        <form:errors path="lookupReport2" cssClass="fieldError"/>
        <form:checkbox path="lookupReport2" id="lookupReport2" />
	</td>
</tr>
<tr class="even">
	<td>
   3
    </td>
    <td>        
        <form:errors path="lookupDesc3" cssClass="fieldError"/>
        <form:input path="lookupDesc3" id="lookupDesc3" cssClass="text medium"/>
    </td>
    <td>        
        <form:errors path="lookupSearch3" cssClass="fieldError"/>
        <form:checkbox path="lookupSearch3" id="lookupSearch3" />
	</td>
	<td>
		<form:errors path="lookupReport3" cssClass="fieldError"/>
        <form:checkbox path="lookupReport3" id="lookupReport3" />
	</td>
</tr>
<tr class="odd">
	<td>
    4
    </td>

    <td>        
        <form:errors path="lookupDesc4" cssClass="fieldError"/>
        <form:input path="lookupDesc4" id="lookupDesc4" cssClass="text medium"/>
    </td>
    <td>        
        <form:errors path="lookupSearch4" cssClass="fieldError"/>
        <form:checkbox path="lookupSearch4" id="lookupSearch4" />
	</td>
	<td>		
        <form:errors path="lookupReport4" cssClass="fieldError"/>
        <form:checkbox path="lookupReport4" id="lookupReport4" />
	</td>
</tr>
<tr class="even">
	<td>
    5
    </td>
    <td>        
        <form:errors path="lookupDesc5" cssClass="fieldError"/>
        <form:input path="lookupDesc5" id="lookupDesc5" cssClass="text medium"/>
    </td>
    <td>        
        <form:errors path="lookupSearch5" cssClass="fieldError"/>
        <form:checkbox path="lookupSearch5" id="lookupSearch5" />
	</td>
	<td>		
        <form:errors path="lookupReport5" cssClass="fieldError"/>
        <form:checkbox path="lookupReport5" id="lookupReport5" />
	</td>
</tr>
<tr class="odd">
	<td>
    6
    </td>
    <td>        
        <form:errors path="lookupDesc6" cssClass="fieldError"/>
        <form:input path="lookupDesc6" id="lookupDesc6" cssClass="text medium"/>
    </td>
    <td>        
        <form:errors path="lookupSearch6" cssClass="fieldError"/>
        <form:checkbox path="lookupSearch6" id="lookupSearch6" />
	</td>
	<td>		
        <form:errors path="lookupReport6" cssClass="fieldError"/>
        <form:checkbox path="lookupReport6" id="lookupReport6" />
	</td>
</tr>
<tr class="even">
	<td>
    7
    </td>
    <td>        
        <form:errors path="lookupDesc7" cssClass="fieldError"/>
        <form:input path="lookupDesc7" id="lookupDesc7" cssClass="text medium"/>
    </td>
    <td>        
        <form:errors path="lookupSearch7" cssClass="fieldError"/>
        <form:checkbox path="lookupSearch7" id="lookupSearch7" />
	</td>
	<td>		
        <form:errors path="lookupReport7" cssClass="fieldError"/>
        <form:checkbox path="lookupReport7" id="lookupReport7" />
	</td>
</tr>

</tbody>

</table>


    
<form:hidden path="version"/>

    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save"  onclick="bCancel=false" value="<fmt:message key="button.save"/>" />
        <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('LookupDef')" value="<fmt:message key="button.delete"/>" />
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('lookupDefForm'));
</script>

<v:javascript formName="lookupDef" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
<script type="text/javascript"  src="<c:url value="/scripts/validator.jsp"/>"></script>
