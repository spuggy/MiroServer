<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="miroProjectList.title"/></title>
<content tag="heading"><fmt:message key="miroProjectList.heading"/></content>
<meta name="menu" content="MiroProjectMenu"/>
<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />


<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editMiroProject.html"/>'"
        value="<fmt:message key="button.addProject"/>"/>

   
</c:set>
<ul>
<li>

<c:if test="${empty miroProjectList}">
<p>Hi  <authz:authentication operation="firstName"/></p> 

<div class="message" id="welcommess" >
<p>Welcome to your MiRo practitioner control panel. From here you can set up and manage your projects. 
You can also credit your account and change your personal details whenever you need to and 
if you have any questions, the information on the <a href="help.html">help page</a> will answer them.  </p>
</div>
<p><c:out value="${buttons}" escapeXml="false"/></p>

</c:if>

<c:if test="${!empty miroProjectList}">

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="miroProjectList" cellspacing="0" cellpadding="0" requestURI=""
    id="miroProjectList" pagesize="50" class="table miroProjectList" export="false">

<display:column property="projectTitle" escapeXml="true" sortable="true"  url="/showProject.html" paramId="id" paramProperty="id"
         titleKey="miroProject.projectTitle"/>
	<display:column property="projectDescription" escapeXml="true" sortable="true"  url="/showProject.html" paramId="id" paramProperty="id"
         titleKey="miroProject.projectDescription"/>

   <display:column property="created_on"  headerClass="sortable"
         titleKey="surveyForm.created_on"  decorator="uk.co.bluetrail.mobriz.webapp.util.ShortDateDecorator"  />
   
   miroProject.projectDescription
   
    <display:setProperty name="paging.banner.item_name" value="Project"/>
    <display:setProperty name="paging.banner.items_name" value="Projects"/>
</display:table>


<script type="text/javascript">
    highlightTableRows("miroProjectList");
</script>

</c:if>

</li>
</ul>