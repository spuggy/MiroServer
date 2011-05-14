<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="lookupDefList.title"/></title>
<content tag="heading"><fmt:message key="lookupDefList.heading"/></content>
<meta name="menu" content="surveyAdmin"/>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editLookupDef.html"/>'"
        value="<fmt:message key="button.add"/>"/>


</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="lookupDefList" cellspacing="0" cellpadding="0" requestURI=""
    id="lookupDefList" pagesize="25" class="table lookupDefList" export="false">

    <display:column property="name" escapeXml="true" sortable="true"
           
          url="/lookupItemsUpload.html" paramId="id" paramProperty="id"
        
         titleKey="lookupDef.name"  />         
        
    <display:column property="desc" escapeXml="true" sortable="true"
         titleKey="lookupDef.desc"/>
           
    <display:column property="created_on" escapeXml="true" sortable="true"
         titleKey="lookupDef.created_on"/>
    
    <display:setProperty name="paging.banner.item_name" value="lookupDef"/>
    <display:setProperty name="paging.banner.items_name" value="lookupDefs"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("lookupDefList");
</script>
