<%@ include file="/common/taglibs.jsp" %>
<head>

    <title><fmt:message key="miroSurvey.title"/></title>

    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>"/>


</head>
<body>
<content tag="heading"><fmt:message key="miroSurvey.heading"/></content>
<content tag="shortheading"><fmt:message key="miroSurvey.shortheading"/></content>
<div class="row">
    <div id="miro-content" class="col-sm-8">
        <div id="intro">

            <p>You have already completed your MiRo Behavioural Mode Assessment. If you wish to complete a second
                assessment then please contact your manager to request a reset. </p>

            <p><b>Please Note:</b> We do not recommend taking the MiRo Behavioural Mode Assessment more than once within
                a three month period. This is due to the fact that you may unconsciously skew your answers based on your
                recent results.</p>


        </div>
    </div>
</div>

</body>