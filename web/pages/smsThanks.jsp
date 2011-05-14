<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <content tag="heading"><fmt:message key="sms.heading"/></content>
    <meta name="menu" content="admin"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/sms.css'/>" />
</head>
<body>

<div style="padding: 20px">

<input class="f-submit" type="button" onclick="window.close()" value="Close" />

</div>


</body>
</html>