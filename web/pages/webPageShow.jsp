<%@ include file="/common/taglibs.jsp"%>
<jsp:useBean id="webPage" class="uk.co.bluetrail.mobriz.model.WebPage" scope="request"/>
<head>



 <c:choose>
  <c:when test="${webPage.menuContextId=='1'}">
  <meta name="menu" content="MiroProjectMenu"/>
  </c:when>

  <c:when test="${webPage.menuContextId=='2'}">
   <meta name="menu" content="accounts"/>
  </c:when>

  <c:when test="${webPage.menuContextId=='3'}">
 <meta name="menu" content="admin"/>
  </c:when>

  <c:when test="${webPage.menuContextId=='4'}">
 <meta name="menu" content="help"/>
  </c:when>

 </c:choose>
 

 
 

<title><c:out value="${webPage.pageName}" /></title>
 
 <content tag="heading">
<c:out value="${webPage.description}" />
</content>

<script type="text/javascript" src="<c:url value='/scripts/calendar.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/ahah.js'/>"></script>

<script type="text/javascript">

//set up the calendar for the constraint date popu
var cal = new CalendarPopup();


</script> 

</head>
<body>
<%=webPage.getPageText()%>
</body>
`