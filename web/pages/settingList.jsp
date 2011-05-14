<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="settingList.title"/></title>
<content tag="heading"><fmt:message key="settingList.heading"/></content>
<meta name="menu" content="admin"/>


<c:out value="${buttons}" escapeXml="false"/>

<display:table name="settingList" cellspacing="0" cellpadding="0" requestURI=""
    id="settingList" pagesize="25" class="table settingList" export="false">

    <display:column property="id" escapeXml="true" sortable="true"
        url="/editSetting.html" paramId="id" paramProperty="id"
        titleKey="setting.id"/>
      <display:column property="settingName" escapeXml="true" sortable="true"
         titleKey="setting.settingName"/>         
    
    <display:column property="settingDescription" escapeXml="true" sortable="true"
         titleKey="setting.settingDescription"/>
  
    <display:setProperty name="paging.banner.item_name" value="setting"/>
    <display:setProperty name="paging.banner.items_name" value="settings"/>
</display:table>


<script type="text/javascript">
    highlightTableRows("settingList");
</script>
