<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="accountList.title"/></title>
<content tag="heading"><fmt:message key="accountList.heading"/></content>
<meta name="menu" content="sysAdmin"/>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editAccount.html"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="accountList" cellspacing="0" cellpadding="0" requestURI=""
    id="accountList" pagesize="25" class="table accountList" export="true">

    <display:column property="companyName" escapeXml="true" sortable="true"
         titleKey="account.companyName"/>
    <display:column property="id" escapeXml="true" sortable="true"
        url="/editAccount.html" paramId="id" paramProperty="id"
        titleKey="account.id"/>
    <display:setProperty name="paging.banner.item_name" value="account"/>
    <display:setProperty name="paging.banner.items_name" value="accounts"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("accountList");
</script>
