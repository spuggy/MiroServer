<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="miroTeamPie.title"/></title>
<content tag="heading"><fmt:message key="miroTeamPie.heading"/></content>
<meta name="menu" content="MiroProjectMenu"/>
   

<ul>
    <li>
    <div class="message" id="welcommes" >
			<p><fmt:message key="miroTeamPie.instructions" /></p>
	</div>
 
    </li>
    
    <c:forEach var="v" items="${userIds}" >
	<li>   	  	
		<img src="<c:url value='/teamImage/teamPieImage.jpg?ids=${v}'/>" />		
	</li>
	</c:forEach>
</ul>			
  
