<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <content tag="heading"><fmt:message key="userList.heading"/></content>
    <meta name="menu" content="admin"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editUser.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    
</c:set>
<ul>
<li>
<c:out value="${buttons}" escapeXml="false" />

<display:table name="userList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="users" pagesize="1000" class="table" export="false">
    <display:column property="username" escapeXml="true" sortable="true" titleKey="user.username"   url="/editUser.html?from=list" paramId="username" paramProperty="username"/>
    <display:column property="fullName" escapeXml="true" sortable="true" titleKey="activeUsers.fullName" />
    <display:column property="email" sortable="true" titleKey="user.email" autolink="true" media="html"/>
    <display:column property="creditBalance" titleKey="user.creditBalance" />
   
 
 	

    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
</li></ul>
