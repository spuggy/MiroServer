<%@ include file="/common/taglibs.jsp" %>
<head>

    <title><fmt:message key="miroSurvey.title"/></title>

    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>"/>


</head>
<body>
<content tag="heading"><fmt:message key="miroSurvey.heading"/></content>
<div class="row">
    <div id="miro-content" class="col-sm-8">

        <div id="intro">

            <p>There are no tests to complete</p>
        </div>

    </div>
</div>
</body>