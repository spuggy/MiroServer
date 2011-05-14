<%@ include file="/common/taglibs.jsp"%>
<%@ page import="uk.co.bluetrail.mobriz.model.*"%>
<head>
	
	<title><fmt:message key="lookupDefDetail.title" /></title>
<content tag="heading">
<fmt:message key="lookupDefDetail.heading" />:
<c:out value="${lookupDef.name}"></c:out>
</content>
<meta name="menu" content="surveyAdmin" />


</head>





 <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editLookupDef.html"/>?id=<c:out value="${lookupDef.id}"/>'"
        value="<fmt:message key="button.editLookupDef"/>"/>

<br/><br/> <c:out value="${lookupDef.desc}" />  




<div id="lookupItemList">


<ul>
	<li>
			<a	href="#" onclick="location.href='<c:url value="/editLookupDefItem.html"/>?lookupDef_id=<c:out value="${lookupDef.id}"/>'" >
			<fmt:message key="button.addLookupDefItem"/>
		</a>
	</li>
	<li>
	<a	href="#" onclick="location.href='<c:url value="/lookupDefItemExport.html"/>?lookupDef_id=<c:out value="${lookupDef.id}"/>'" >
			<fmt:message key="button.exportLookupDefItem"/>
		</a>
				
   				
	</li>
	<li>

		<a	href="#" onclick='Element.show("importControls");'   ><fmt:message key="button.importLookupDefItem"/></a>

		
		<spring:bind path="lookupItemsUploadForm.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="error">
				    <script type="text/javascript">Element.show("importControls");</script>
					<c:forEach var="error" items="${status.errorMessages}">
						<img src="<c:url value="/images/iconWarning.gif"/>"
							alt="<fmt:message key="icon.warning"/>" class="icon" />
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>

			
		
</ul>

</div>

<div id="importControls" style="display:none">

		<form:form commandName="lookupItemsUploadForm" method="post" action=""
			enctype="multipart/form-data"
			onsubmit="return validateLookupItemsUploadForm(this)" id="lookupItemsUploadForm">

			<input name="lookupDef_id" id="lookupDef_id" type="hidden"
				value="<c:out value="${lookupDef.id}" />" />

			<ul>
				<!-- 
				<li class="info">
					<fmt:message key="lookupItemsUploadForm.message" />
				</li>
				 -->

				<li>
					<mobriz4server:label key="uploadForm.file" styleClass="desc" />
					<form:errors path="file" cssClass="fieldError" />
					<spring:bind path="lookupItemsUploadForm.file">
						<input type="file" name="file" id="file" class="file medium"
							value="<c:out value="${status.value}"/>" />
					</spring:bind>
				</li>

				<li class="buttonBar bottom">
					<input type="submit" name="upload" class="button"	onclick="bCancel=false" value="<fmt:message key="button.upload"/>" />

				</li>
		</form:form>
		<v:javascript formName="lookupItemsUploadForm"
			staticJavascript="false" />
		<script type="text/javascript"
			src="<c:url value="/scripts/validator.jsp"/>"></script>
</div>



<%

LookupDef lk = (LookupDef) request.getAttribute("lookupDef") ;


%>



<display:table name="lookupDefItemList" cellspacing="0" cellpadding="0"
	requestURI="" id="lookupDefItemList" pagesize="50"
	class="table lookupDefItemList" export="false"  >

	<display:column property="id" escapeXml="true" sortable="true"
		url="/editLookupDefItem.html" paramId="id" paramProperty="id"
		titleKey="lookupDefItem.id" />

	<c:if test="${not empty lookupDef.lookupDesc1}">
		<display:column property="lookup1" escapeXml="true"
			title="<%=lk.getLookupDesc1()%>" />
	</c:if>
	<c:if test="${not empty lookupDef.lookupDesc2}">
		<display:column property="lookup2" escapeXml="true"
			title="<%=lk.getLookupDesc2()%>" />
	</c:if>
	<c:if test="${not empty lookupDef.lookupDesc3}">
		<display:column property="lookup3" escapeXml="true"
			title="<%=lk.getLookupDesc3()%>" />
	</c:if>
	<c:if test="${not empty lookupDef.lookupDesc4}">
		<display:column property="lookup4" escapeXml="true"
			title="<%=lk.getLookupDesc4()%>" />
	</c:if>
	<c:if test="${not empty lookupDef.lookupDesc5}">
		<display:column property="lookup5" escapeXml="true"
			title="<%=lk.getLookupDesc5()%>" />
	</c:if>
	<c:if test="${not empty lookupDef.lookupDesc6}">
		<display:column property="lookup6" escapeXml="true"
			title="<%=lk.getLookupDesc6()%>" />
	</c:if>
	<c:if test="${not empty lookupDef.lookupDesc7}">
		<display:column property="lookup7" escapeXml="true"
			title="<%=lk.getLookupDesc7()%>" />
	</c:if>

   
 	
 	
 	<display:column title="" media="html"  autolink="true">
 	   <a href="#" onclick="deleteRecord('deleteLookupItem.html','<c:out value="${lookupDefItemList.id}"/>')" /><img src="styles/images/delete.png" /></a>
 	</display:column>



	<display:setProperty name="paging.banner.item_name"
		value="lookupDefItem" />
	<display:setProperty name="paging.banner.items_name"
		value="lookupDefItems" />

</display:table>





