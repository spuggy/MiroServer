<%@ include file="/common/doctype.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <%@ include file="/common/html_head_common.jsp" %>
    <decorator:head/>
</head>
<body onLoad="<decorator:getProperty property="body.onLoad"/>"
        <decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty
        property="body.class" writeEntireProperty="true"/>>

<c:set var="currentMenu" scope="request">
    <decorator:getProperty property="meta.menu" />
</c:set>
<c:set var="shortheading" scope="request">
    <decorator:getProperty property="page.shortheading" />
</c:set>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

<%@ include file="/common/browser_warning.jsp" %>

<!-- Static navbar -->
<nav class="navbar navbar-default navbar-static-top miro-main-padding">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">
                <img class="hidden-xs" src="<c:url value='/miro11style/img/miro-logo.png'/>" alt="MiRo Psychometrics"/>
                <img class="visible-xs" src="<c:url value='/miro11style/img/miro-logo-small.png'/>" alt="MiRo Psychometrics"/>
            </a>
        </div>
        <div id="navbar" class="navbar-collapse collapse miro-navbar">
            <c:choose>
                <c:when test='${currentMenu == "MiroProjectMenu"}'>
                    <ul class="nav navbar-nav">
                        <li><a class="navbar_main_link" href="miroProjects.html">My Projects</a></li>
                        <li><a class="navbar_main_link" href="miroTeamList.html">Team Reports</a></li>
                        <li><a class="navbar_main_link" href="editProfile.html">My Profile</a></li>
                        <li><a class="navbar_main_link" href="webPages.html?name=help">Help</a></li>
                    </ul>
                </c:when>
                <c:when test='${currentMenu == "admin"}'>
                    <ul class="nav navbar-nav">
                        <li><a class="navbar_main_link" href="users.html">Users</a></li>
                        <li><a class="navbar_main_link" href="settings.html">Settings</a></li>
                        <li><a class="navbar_main_link" href="webPages.html">Help</a></li>
                    </ul>

                </c:when>

            </c:choose>


            <ul class="nav navbar-nav navbar-right">
                <button onclick="location='logout.jsp'" type="button" class="btn btn-info btn-lg">Logout</button>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
    <!--/.container-fluid -->
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="hidden-xs col-sm-12 page-header miro-page-header miro-subheader-padding">
            <h1><decorator:getProperty property="page.heading"/></h1>
        </div>
        <div class="visible-xs col-sm-12 miro-page-header miro-subheader-padding">
           <c:choose>
                <c:when test="${empty shortheading}">
                    <h3><decorator:getProperty property="page.heading"/></h3>
                </c:when>
                <c:otherwise>
                    <h3 ><decorator:getProperty property="page.shortheading"/></h3>
                </c:otherwise>
            </c:choose>

        </div>

    </div>
    <div class="row">
        <div class="col-sm-12 miro-subheader-padding">
            <%@ include file="/common/messages.jsp" %>
            <decorator:body/>
        </div>
   </div>
   <%@ include file="/common/footer.jsp" %>
</div>




<%@include file="/common/site_javascript.jsp" %>

</body>
</html>