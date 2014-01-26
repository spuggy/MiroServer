<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"><head><!-- HTTP 1.1 -->
		        
        <meta http-equiv="Cache-Control" content="no-store"><!-- HTTP 1.0 -->
        
        <meta http-equiv="Pragma" content="no-cache"><!-- Prevents caching at the Proxy Server -->
        
        <meta http-equiv="Expires" content="0">
		<meta name="robots" content="nofollow" />
        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
        
        <meta name="author" content="Matt Raible (matt@raibledesigns.com)">
       <title>Login | Miro</title>

		

	<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
		<link rel="stylesheet" type="text/css" media="print"
			href="<c:url value='/styles/${appConfig["csstheme"]}/print.css'/>" />
		<link rel="stylesheet" type="text/css" media="all"
			href="<c:url value='/styles/${appConfig["csstheme"]}/login.css'/>" />

        <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/scriptaculous.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>
  

		<div id="page">
			<div id="header" class="clearfix">

			</div>
			<div id="contentlogin"><!-- added new class to layout.css for Login page to center the login box (only in login.jsp.htm)-->

			  <div id="main">
					<h1 id="loginbanner" align="center">Login </h1>

				  <div id="submain">

	<jsp:include page="/WEB-INF/pages/loginForm.jsp"/>
	
<p><fmt:message key="login.passwordHint"/></p>
	
<p> <a href="@HOME-URL@">@HOME-DESC@</a></p>

				  </div>
              </div>


 
			</div>


    </div>

	</body></html>