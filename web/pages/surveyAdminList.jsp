<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="surveyAdmin.title"/></title>
    <content tag="heading"><fmt:message key="surveyAdmin.heading"/></content>
    <meta name="menu" content="surveyAdmin"/>
</head>




<display:table name="surveyList" cellspacing="0" cellpadding="0"
    id="surveyList" pagesize="50" class="table"  export="false" requestURI="" >
     <display:column property="title" sortable="true" headerClass="sortable"       titleKey="surveyForm.title"  url="/editSurvey.html" paramId="id" paramProperty="id"/>
 

     <%@ include file="/WEB-INF/pages/surveyListColumns.jsp"%>
 
     <display:column    url="/surveyDefExport.html" paramId="id" paramProperty="id"  titleKey="surveyForm.Export"   ><fmt:message key="surveyForm.Export"/></display:column>

     
    <display:setProperty name="paging.banner.item_name" value="survey"/>
    <display:setProperty name="paging.banner.items_name" value="surveys"/>
</display:table>



<script type="text/javascript">
highlightTableRows("surveyList");
</script>



