<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="webPageList.title"/></title>
<content tag="heading"><fmt:message key="webPageList.heading"/></content>
<meta name="menu" content="admin"/>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editWebPage.html"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="webPageList" cellspacing="0" cellpadding="0" requestURI=""
    id="webPageList" pagesize="25" class="table webPageList" export="true">
    
 	<display:column property="pageName" escapeXml="true"titleKey="webPage.pageName" url="/editWebPage.html" paramId="id" paramProperty="id" />
       
    <display:column property="description" escapeXml="true"  />
        
   
        <display:setProperty name="paging.banner.item_name" value="webPage"/>
    <display:setProperty name="paging.banner.items_name" value="webPages"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("webPageList");
</script>
