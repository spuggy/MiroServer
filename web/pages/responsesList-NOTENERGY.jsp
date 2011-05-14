<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="menu.responses"/></title>
<content tag="heading"><fmt:message key="menu.responses"/></content>
<meta name="menu" content="responses"/>





<display:table name="surveyList" cellspacing="0" cellpadding="0" requestURI="" 
    id="surveyList" pagesize="25" class="table surveyList" export="false" decorator="uk.co.bluetrail.mobriz.webapp.util.ResponseTableDecorator">
   
    
     <display:column property="title" titleKey="surveyForm.title"  url="/responsesForSurvey.html" paramId="id" paramProperty="id"/>
     <!-- 
     <display:column property="responseCount" titleKey="surveyForm.responseCount" />
     -->
     <display:column  url="/exportResponsesSurvey.html" paramId="id" paramProperty="id"  titleKey="surveyForm.Download"   ><img src="styles/images/download.gif" /></display:column>
   	 <display:column  property="photoLink" titleKey="surveyForm.photos"   />
 
   	
     
    <display:setProperty name="paging.banner.item_name" value="survey"/>
    <display:setProperty name="paging.banner.items_name" value="surveys"/>
</display:table>




<script type="text/javascript">
    highlightTableRows("settingList");
</script>
