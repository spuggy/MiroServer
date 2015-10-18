<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%@ include file="/common/meta.jsp" %>
    <title><decorator:title/> | MiRo</title>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/miro2/${appConfig["csstheme"]}/css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/miro2/${appConfig["csstheme"]}/css/main.css'/>"/>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <decorator:head/>
</head>
<body onLoad="<decorator:getProperty property="body.onLoad"/>"
        <decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty
        property="body.class" writeEntireProperty="true"/>>

<!--[if lt IE 7]>
<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> to improve your experience.</p>
<![endif]-->

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
            <a class="navbar-brand" href="#"><img height="66px" width="152px" src="<c:url value='/miro2/${appConfig["csstheme"]}/img/miro-logo.png'/>" alt="MiRo Psychometrics"/></a>
        </div>
        <div id="navbar" class="navbar-collapse collapse miro-navbar">
            <ul class="nav navbar-nav">
                <li><a class="navbar_main_link" href="miroProjects.html">My Projects</a></li>
                <li><a class="navbar_main_link" href="miroTeamList.html">Team Reports</a></li>
                <li><a class="navbar_main_link" href="editProfile.html">My Profile</a></li>
                <li><a class="navbar_main_link" href="webPages.html">Help</a></li>
            </ul>
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
        <div class="col-sm-12 page-header miro-page-header miro-subheader-padding">
            <h1><decorator:getProperty property="page.heading"/></h1>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12 miro-subheader-padding">
            <div id="warning-alert" class="alert alert-warning alert-dismissible hidemeh" role="alert">
                <button type="button" class="close" onclick="closeAlert()"><span aria-hidden="true">&times;</span></button>
                <strong>Warning!</strong> <span id="warning-alert-mess"></span>
            </div>
            <decorator:body/>
        </div>
   </div>
</div>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script>window.jQuery || document.write('<script src="/mirotest/miro2/js/vendor/jquery-1.11.0.min.js"><\/script>')</script>
<script src="/mirotest/miro2/js/bootstrap.min.js"></script>
<script src="/mirotest/miro2/js/plugins.js"></script>
<script src="/mirotest/miro2/js/main.js"></script>

</body>
</html>