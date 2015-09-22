<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <%@ include file="/common/meta.jsp" %>
    <title><decorator:title/> | MiRo
    </title>
    <link rel="stylesheet" type="text/css" media="all"
          href="<c:url value='/miro2/${appConfig["csstheme"]}/css/bootstrap.css'/>"/>
    <link rel="stylesheet" type="text/css" media="all"
          href="<c:url value='/miro2/${appConfig["csstheme"]}/css/main.css'/>"/>

    <decorator:head/>
</head>
<body onLoad="<decorator:getProperty property="body.onLoad"/>"
        <decorator:getProperty property="body.id"
                               writeEntireProperty="true"/><decorator:getProperty
        property="body.class" writeEntireProperty="true"/>>

<!--[if lt IE 7]>
<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> to improve your experience.</p>
<![endif]-->

<!-- Static navbar -->
<nav class="navbar navbar-default navbar-fixed-top miro-main-padding">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#"><img
                    src="<c:url value='/miro2/${appConfig["csstheme"]}/img/miro-logo.svg'/>" alt="MiRo Psychometrics"/></a>
        </div>
        <div id="navbar" class="navbar-collapse collapse miro-navbar">
            <ul class="nav navbar-nav">
                <li><a class="navbar_main_link" href="#">My Projects</a></li>
                <li><a class="navbar_main_link" href="#">Team Reports</a></li>
                <li><a class="navbar_main_link" href="#">My Profile</a></li>
                <li><a class="navbar_main_link" href="#">Help</a></li>
                <li class="dropdown">
                    <a class="navbar_main_link" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                       aria-haspopup="true" aria-expanded="false">Other<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Action</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <button type="button" class="btn btn-info btn-lg">Logout</button>
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
        <div id="miro-content" class="col-sm-8 miro-main-padding">

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