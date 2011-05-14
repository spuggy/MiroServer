<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="myResponsesList.title"/></title>
<content tag="heading"><fmt:message key="myResponsesList.title"/></content>
<meta name="menu" content="responses"/>

  <display:table name="responseList" cellspacing="0" cellpadding="0"
    id="responseList" pagesize="25" class="table"
    export="false" requestURI="">
    	<display:column property="created_on" sortable="true" style="width: 100px"  titleKey="surveyResponseForm.created_on"
           url="/showResponse.html" paramId="id" paramProperty="id" decorator="uk.co.bluetrail.mobriz.webapp.util.ShortDateDecorator"    />
    
    
 <display:column property="survey.title" sortable="true" style="width: 200px"
         titleKey="surveyForm.title"/>
    <display:column property="user.username" sortable="true" style="width: 100px"
         titleKey="surveyResponseForm.createdBy"/>
   
    <display:setProperty name="paging.banner.item_name" value="Response"/>
    <display:setProperty name="paging.banner.items_name" value="Responses"/>
</display:table>
  


<script type="text/javascript">
highlightTableRows("surveyResponseList");
</script>
