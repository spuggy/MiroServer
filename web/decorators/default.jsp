<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
		<%@ include file="/common/meta.jsp"%>
		<title><decorator:title /> | MiRo
		</title>
		<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
		<link rel="stylesheet" type="text/css" media="print"
			href="<c:url value='/styles/${appConfig["csstheme"]}/print.css'/>" />
		<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/@APPNAME@.css'/>" />

        <script type="text/javascript" src="<c:url value='/scripts/prototype-1.5.1.2.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/scriptaculous.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>
        <decorator:head/>
    </head>
<body onLoad="<decorator:getProperty property="body.onLoad"/>" <decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>

<c:set var="currentMenu" scope="request">
					<decorator:getProperty property="meta.menu" />
				</c:set>


		<div id="page">
			<div id="header" class="clearfix">
				<jsp:include page="/common/header.jsp" />

				<div id="nav" class="clearfix">
					<jsp:include page="/common/menu.jsp" />
				</div>
				<!-- end nav -->


			</div>
			<div id="content">

				<div id="main">
					<h1>
						<decorator:getProperty property="page.heading" />
					</h1>

					<div id="submain">

						<%@ include file="/common/messages.jsp"%>

						<decorator:body />
					</div>
				</div>

				<div id="mainFooter">&nbsp;</div>


				</div>

				

				
				
				<div id="sideBar">
				
					<c:choose>
						<c:when test='${currentMenu == "MiroProjectMenu"}'>
							<%@ include file="/WEB-INF/pages/miroProjectSideBar.jsp"%>
						</c:when>					
						<c:when test='${currentMenu == "responses"}'>
							<%@ include file="/WEB-INF/pages/responsesSideBar.jsp"%>
						</c:when>
						<c:when test='${currentMenu == "surveyAdmin"}'>
							<%@ include file="/WEB-INF/pages/surveyAdminSideBar.jsp"%>
						</c:when>
						<c:when test='${currentMenu == "admin"}'>
							<%@ include file="/WEB-INF/pages/adminSideBar.jsp"%>
						</c:when>
						<c:when test='${currentMenu == "sysadmin"}'>
							<%@ include file="/WEB-INF/pages/sysAdminSideBar.jsp"%>
						</c:when>
					</c:choose>
					
					<decorator:getProperty property="page.pagemenu" />
					
					<h2><!--  company logo --></h2>
					
				</div>



			</div>
			<div id="footer" class="clearfix">
				<jsp:include page="/common/footer.jsp" />
			</div>

		</div>
	</body>
</html>
