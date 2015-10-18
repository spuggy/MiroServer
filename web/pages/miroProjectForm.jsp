<%@ include file="/common/taglibs.jsp" %>
<head>
    <title><fmt:message key="miroProjectDetail.title"/></title>
    <content tag="heading"><fmt:message key="miroProjectDetail.heading"/></content>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>"/>
    <meta name="menu" content="MiroProjectMenu"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>"/>

</head>
<body onLoad="initPage()">

<spring:bind path="miroProject.*"> <c:if test="${not empty status.errorMessages}">
    <div class="row">
        <div id="miro-content" class="col-sm-6">
            <div class="alert alert-danger" role="alert">
                <p><strong>Errors</strong></p>
                <c:forEach var="error" items="${status.errorMessages}"> <c:out value="${error}" escapeXml="false"/><br/></c:forEach>
            </div>
        </div>
    </div>
</c:if> </spring:bind>


<div class="row">
    <div class="col-sm-4">

        <form:form commandName="miroProject" method="post" action="editMiroProject.html" onsubmit="return validateMiroProject(this)" id="miroProjectForm">
            <form:hidden path="version"/> <form:hidden path="id"/>


            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroProject.projectTitle"/>
                <form:errors path="projectTitle" cssClass="fieldError"/>
                <form:input path="projectTitle" id="projectTitle" cssClass="form-control"/>
            </div>

            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroProject.costcode"/>
                <form:errors path="costcode" cssClass="fieldError"/>
                <form:input path="costcode" id="costcode" cssClass="form-control"/>
            </div>

            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroProject.projectDescription"/>
                <form:errors path="projectDescription" cssClass="fieldError"/>
                <form:textarea path="projectDescription" id="projectDescription" cssClass="form-control" rows="4"/>
            </div>


            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroProject.emailInviteSubject"/>
                <form:errors path="emailInviteSubject" cssClass="fieldError"/>
                <form:input path="emailInviteSubject" id="emailInviteSubject" cssClass="form-control"/>
            </div>

            <div class="form-group">
                <form:checkbox path="bccPractitioner" id="bccPractitioner"/>
                <label for="bccPractitioner" class="choice"> <fmt:message key="miroProject.bccPractitioner">
                    <fmt:param value="${currentUser.email}"/> </fmt:message> </label>
            </div>

            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroProject.emailInviteText"/>
                <form:errors path="emailInviteText" cssClass="fieldError"/>
                <form:textarea path="emailInviteText" id="emailInviteText" cssClass="form-control" rows="10"/>
            </div>

            <button class="btn btn-primary" name="save" onclick="bCancel=false">
                <fmt:message key="button.save"/></button>
            <button class="btn btn-default" name="cancel" onclick="bCancel=true">
                <fmt:message key="button.cancel"/></button>
            <c:if test="${!empty miroProject.id}">
                <button class="btn btn-default" name="delete" onclick="bCancel=true;return confirmDelete('MiroProject')">
                    <fmt:message key="button.delete"/></button>
            </c:if>


        </form:form>

        <script type="text/javascript">
            Form.focusFirstElement($('miroProjectForm'));
        </script>

        <v:javascript formName="miroProject" cdata="false" dynamicJavascript="true" staticJavascript="false"/>
        <script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
    </div>

    <div class="col-sm-8 form-helper-padding">

        <div class="panel panel-info">
            <div class="panel-heading">
                <h2 class="panel-title">AVOIDING SPAM</h2>
            </div>
            <div class="panel-body bg-info">
                <p>When you save the project form you will able to add candidates and send them their login credentials
                    via email.</p>

                <p>Sometimes companies employ email filters that can mistakenly mark your invite as spam. If your invite
                    is trapped the candidate will not recieve their login!</p>

                <h5>TIPS FOR AVOIDING SPAM</h5>

                <p>When you fill in the email subject and invite text consider avoiding:</p>

                <ul>
                    <li>Excessive use of exclamation points!!!!!!!!!</li>
                    <li>USING ALL CAPS, WHICH IS LIKE SCREAMING AT THE TOP OF YOUR LUNGS VIA EMAIL (especially in the
                        subject line).</li>
                    <li>Sending invites to multiple recipients within the same company. That company's email filter
                        often assumes it’s a spam attack.  Try sending one at a time or sending the invite from your own email account.  Use the "copy to " checkbox on the project form.
                    </li>
                    <li>Using phrases like "Click here!" or "Once in a lifetime opportunity!"</li>
                    <li>Using the word "test" in the subject line.</li>
                    <li>Talking about lots of money</li>
                    <li>Describe some sort of breakthrough</li>
                    <li>Contains urgent matter, or Money back guarantee</li>

                </ul>

                <h5>WHAT IF MY INVITES ARE STILL BEING SPAMMED?</h5>

                <p>if your MiRo emails are being caught by spam filters make sure you have the <strong>Copy email invites</strong> checkbox set.  Thsi will
                bcc you copies of your email invites to your inbox.  This means you will have a copy of the user credentials and be able to resend them from your
                email account.</p>

            </div>
        </div>

    </div>

    <script type="application/javascript">

        function initPage() {
            setPlaceHolder("costcode", "A reference for your accounts");
            setPlaceHolder("projectTitle", "A short reference name for the project");
            setPlaceHolder("projectDescription", "Notes on your project so you can remember what it was for");
            setPlaceHolder("emailInviteSubject", "The subject line for your invite emails");
            setPlaceHolder("emailInviteText", "The body of the invite email");
        }
    </script>


</div>
</body>