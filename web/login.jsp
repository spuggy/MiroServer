<%@ include file="/common/doctype.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <%@ include file="/common/html_head_common.jsp" %>
</head>
<body>

<div class="container">

    <form class="form-signin" ethod="post" id="loginForm" action="<c:url value="/j_security_check"/>" onsubmit="saveUsername(this);return validateForm(this)">
        <img height="66px" width="152px" src="<c:url value='/miro11style/img/miro-logo.png'/>" alt="MiRo Psychometrics"/>

        <h2 class="form-signin-heading">Please sign in</h2>
        <c:if test="${param.error != null}">
            <div id="errorMessages" class="alert alert-danger alert-dismissible" role="alert">
                <fmt:message key="errors.password.mismatch"/>
            </div>
        </c:if>
        <div class="form-group">
            <label for="j_username" class="desc">Username</label>
            <input type="text" class="form-control" id="j_username" name="j_username" placeholder="username" tabindex="1" required autofocus/>
        </div>
        <div class="form-group">
            <label for="j_password" class="desc">Password</label>
            <input type="password" id="j_password" name="j_password" class="form-control" placeholder="Password" required>
        </div>
        <c:if test="${appConfig['rememberMeEnabled']}">
            <div class="form-group">
                <input type="checkbox" name="rememberMe" id="rememberMe" tabindex="3"/>
                <label class="choice" for="rememberMe"><fmt:message key="login.rememberMe"/></label>
            </div>
        </c:if>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>

        <p><fmt:message key="login.passwordHint"/></p>

        <p></p><a href="http://www.miro-assessment.com">Home Page</a></p>
    </form>


    <%@ include file="/scripts/login.js" %>


</div>
</body>
</html>
