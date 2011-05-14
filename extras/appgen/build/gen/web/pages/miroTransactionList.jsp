<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="miroTransactionList.title"/></title>
<content tag="heading"><fmt:message key="miroTransactionList.heading"/></content>
<meta name="menu" content="MiroTransactionMenu"/>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editMiroTransaction.html"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<c:out value="${buttons}" escapeXml="false"/>

<display:table name="miroTransactionList" cellspacing="0" cellpadding="0" requestURI=""
    id="miroTransactionList" pagesize="25" class="table miroTransactionList" export="true">

    <display:column property="credits" escapeXml="true" sortable="true"
         titleKey="miroTransaction.credits"/>
    <display:column property="errorCode" escapeXml="true" sortable="true"
         titleKey="miroTransaction.errorCode"/>
    <display:column property="id" escapeXml="true" sortable="true"
        url="/editMiroTransaction.html" paramId="id" paramProperty="id"
        titleKey="miroTransaction.id"/>
    <display:column property="paymentMethod" escapeXml="true" sortable="true"
         titleKey="miroTransaction.paymentMethod"/>
    <display:column property="status" escapeXml="true" sortable="true"
         titleKey="miroTransaction.status"/>
    <display:column property="user_id" escapeXml="true" sortable="true"
         titleKey="miroTransaction.user_id"/>
    <display:column property="deleted" escapeXml="true" sortable="true"
         titleKey="miroTransaction.deleted"/>
    <display:column property="checkPoint" escapeXml="true" sortable="true"
         titleKey="miroTransaction.checkPoint"/>
    <display:column property="created_on" escapeXml="true" sortable="true"
         titleKey="miroTransaction.created_on"/>
    <display:column property="createdBy_id" escapeXml="true" sortable="true"
         titleKey="miroTransaction.createdBy_id"/>
    <display:column property="lastUpdatedBy_id" escapeXml="true" sortable="true"
         titleKey="miroTransaction.lastUpdatedBy_id"/>
    <display:column property="updated_at" escapeXml="true" sortable="true"
         titleKey="miroTransaction.updated_at"/>
    <display:setProperty name="paging.banner.item_name" value="miroTransaction"/>
    <display:setProperty name="paging.banner.items_name" value="miroTransactions"/>
</display:table>

<c:out value="${buttons}" escapeXml="false"/>

<script type="text/javascript">
    highlightTableRows("miroTransactionList");
</script>
