<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="mobrizAlertList.title"/></title>
<content tag="heading"><fmt:message key="mobrizAlertList.heading"/></content>
<meta name="menu" content="surveyAdmin"/>
	

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editMobrizAlert.html"/>'"
        value="<fmt:message key="button.add"/>"/>

    
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="mobrizAlertList" cellspacing="0" cellpadding="0" requestURI=""
    id="mobrizAlertList" pagesize="100" class="table mobrizAlertList" export="false">
	
	 <display:column property="alertTitle" escapeXml="true" sortable="true" url="/editMobrizAlert.html" paramId="id" paramProperty="id"
         titleKey="mobrizAlert.alertTitle"/>
    <display:column property="executionOrder" escapeXml="true" sortable="true"
         titleKey="mobrizAlert.executionOrder"/>
       <display:column property="emailAdress" escapeXml="true" sortable="true"
         titleKey="mobrizAlert.emailAdress"/>
    <display:column property="updated_at" escapeXml="true" sortable="true"
         titleKey="mobrizAlert.updated_at"/>
       <display:column property="enabled" escapeXml="true" sortable="true"
         titleKey="mobrizAlert.enabled"/>
    
    <display:setProperty name="paging.banner.item_name" value="mobrizAlert"/>
    <display:setProperty name="paging.banner.items_name" value="mobrizAlerts"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("mobrizAlertList");
</script>
