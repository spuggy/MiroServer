<%@ include file="/common/taglibs.jsp" %>

<head>

    <title><fmt:message key="userProfile.title"/></title>
    <content tag="heading"><fmt:message key="userProfile.heading"/></content>
    <c:choose> <c:when test="${param.from == 'list' or param.method == 'Add'}">
        <meta name="menu" content="admin"/>
    </c:when> <c:otherwise>
        <meta name="menu" content="MiroProjectMenu"/>
    </c:otherwise> </c:choose>
    <script type="text/javascript" src="/mirotest/miro11style/js/selectbox.js"></script>

</head>

<body onLoad="initPage()">

<spring:bind path="user.*"> <c:if test="${not empty status.errorMessages}">
    <div class="row">
        <div class="col-sm-6">
            <div id="errorMessages" class="alert alert-danger alert-dismissible" role="alert">
                <c:forEach var="error" items="${status.errorMessages}"> <c:out value="${error}" escapeXml="false"/><br/>
                </c:forEach>
            </div>
        </div>
    </div>
</c:if> </spring:bind>
<div class="row">
    <div class="col-sm-6">

        <form:form commandName="user" method="post" action="editUser.html" onsubmit="return onFormSubmit(this)" id="userForm">
            <form:hidden path="id"/> <form:hidden path="version"/> <form:hidden path="pinNumber"/>
            <input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>

            <c:if test="${cookieLogin == 'true'}"> <form:hidden path="password"/> <form:hidden path="confirmPassword"/>
            </c:if>

            <c:if test="${empty user.username}"> <input type="hidden" name="encryptPass" value="true"/> </c:if>

            <p>
                    <%-- So the buttons can be used at the bottom of the form --%> <c:set var="buttons">
                <button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false">
                    <fmt:message key="button.save"/></button>

                <c:if test="${param.from == 'list' and param.method != 'Add'}">
                    <button type="submit" class="btn btn-default" name="delete" onclick="bCancel=true;return confirmDelete('user')">
                        <fmt:message key="button.delete"/></button>
                </c:if>

                <button type="submit" class="btn btn-default" name="cancel" onclick="bCancel=true">
                    <fmt:message key="button.cancel"/></button>
            </c:set> <br/> <c:out value="${buttons}" escapeXml="false"/>
            </p>
            <p> <c:choose> <c:when test="${param.from == 'list'}">

            <p><fmt:message key="userProfile.admin.message"/></p>
        </c:when> <c:otherwise>
            <p><fmt:message key="userProfile.message"/></p>
        </c:otherwise> </c:choose> </p>

            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.username"/>
                <form:errors path="username" cssClass="fieldError"/>
                <form:input path="username" id="username" cssClass="form-control"/>


            </div>

            <c:if test="${cookieLogin != 'true'}">


                <div class="form-group">
                    <mobriz4server:label styleClass="desc" key="user.password"/>
                    <form:errors path="password" cssClass="fieldError"/>
                    <form:password path="password" id="password" cssClass="form-control" onchange="passwordChanged(this)"/>
                </div>

                <div class="form-group">
                    <mobriz4server:label styleClass="desc" key="user.confirmPassword"/>
                    <form:errors path="confirmPassword" cssClass="fieldError"/>
                    <form:password path="confirmPassword" id="confirmPassword" cssClass="form-control"/>
                </div>


            </c:if>
            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.passwordHint"/>
                <form:errors path="passwordHint" cssClass="fieldError"/>
                <form:input path="passwordHint" id="passwordHint" cssClass="form-control"/>
            </div>

            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.firstName"/>
                <form:errors path="firstName" cssClass="fieldError"/>
                <form:input path="firstName" id="firstName" cssClass="form-control" maxlength="50"/>
            </div>
            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.lastName"/>
                <form:errors path="lastName" cssClass="fieldError"/>
                <form:input path="lastName" id="lastName" cssClass="form-control" maxlength="50"/>
            </div>

            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.company"/>
                <form:errors path="company" cssClass="fieldError"/>
                <form:input path="company" id="company" cssClass="form-control" maxlength="100"/>
            </div>
            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.address1"/>
                <form:errors path="address1" cssClass="fieldError"/>
                <form:input path="address1" id="address1" cssClass="form-control" maxlength="100"/>

            </div>

            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.address2"/>
                <form:errors path="address2" cssClass="fieldError"/>
                <form:input path="address2" id="address2" cssClass="form-control" maxlength="100"/></div>

            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.city"/>
                <form:errors path="city" cssClass="fieldError"/>
                <form:input path="city" id="city" cssClass="form-control" maxlength="50"/>
            </div>
            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.county"/>
                <form:errors path="county" cssClass="fieldError"/>
                <form:input path="county" id="county" cssClass="form-control" maxlength="50"/>
            </div>
            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.postcode"/>
                <form:errors path="postcode" cssClass="fieldError"/>
                <form:input path="postcode" id="postcode" cssClass="form-control" maxlength="50"/>

            </div>


            <div class="form-group">
                <mobriz4server:label styleClass="desc" key="user.email"/>
                <form:errors path="email" cssClass="fieldError"/>
                <form:input path="email" id="email" cssClass="form-control"/>
            </div>
            <div class="form-group">
                <mobriz4server:label key="user.employeeRef" styleClass="desc"/>
                <form:errors path="employeeRef" cssClass="fieldError"/>
                <form:input path="employeeRef" id="employeeRef" cssClass="form-control"/>
            </div>

            <div class="form-group">
                <mobriz4server:label key="user.phoneNumber" styleClass="desc"/>
                <form:errors path="phoneNumber" cssClass="fieldError"/>
                <form:input path="phoneNumber" id="phoneNumber" cssClass="form-control"/>
            </div>


            <div class="form-group">
                <mobriz4server:label key="user.webaddress" styleClass="desc"/>
                <form:errors path="webaddress" cssClass="fieldError"/>
                <form:input path="webaddress" id="webaddress" cssClass="form-control"/>
            </div>


            <c:choose>


                <c:when test="${param.from == 'list' or param.method == 'Add'}">


                    <div class="form-group">
                        <mobriz4server:label styleClass="desc" key="user.department"/>
                        <form:errors path="department" cssClass="fieldError"/>
                        <form:select path="department" cssClass="form-control" id="department"> <form:options items="${departments}"/>
                        </form:select>


                    </div>

                    <div class="form-group">
                        <mobriz4server:label styleClass="desc" key="user.default_survey_id"/>
                        <form:errors path="default_survey_id" cssClass="fieldError"/>
                        <form:select cssClass="form-control" path="default_survey_id" id="default_survey_id">
                            <form:options items="${miroVersions}"/> </form:select>


                    </div>

                    <div class="form-group">
                        <fieldset>
                            <form:checkbox path="enabled" id="enabled" />
                            <label for="enabled" class="choice">User Enabled</label>


                        </fieldset>
                    </div>
                    <div class="form-group">
                        <fieldset class="pickList">
                            <legend><fmt:message key="userProfile.assignRoles"/></legend>
                            <table class="pickList">
                                <tr>
                                    <th class="pickLabel">
                                        <mobriz4server:label key="user.availableRoles" colon="false" styleClass="required"/>
                                    </th>
                                    <td></td>
                                    <th class="pickLabel">
                                        <mobriz4server:label key="user.roles" colon="false" styleClass="required"/>
                                    </th>
                                </tr>
                                <c:set var="leftList" value="${availableUserRoles}" scope="request"/>
                                <c:set var="rightList" value="${user.roleList}" scope="request"/>
                                <c:import url="/common/pickList.jsp"> <c:param name="listCount" value="1"/>
                                    <c:param name="leftId" value="availableRoles"/>
                                    <c:param name="rightId" value="userRoles"/> </c:import>
                            </table>
                        </fieldset>
                    </div>
                </c:when> <c:when test="${not empty user.username}">
                <div class="form-group">

                    Profile

                    <strong><mobriz4server:label key="user.roles"/></strong>
                    <c:forEach var="role" items="${user.roleList}" varStatus="status">
                        <c:out value="${role.label}"/><c:if test="${!status.last}">,</c:if>
                        <input type="hidden" name="userRoles" value="<c:out value="${role.label}"/>"/> </c:forEach>
                    <form:hidden path="enabled"/> <form:hidden path="accountExpired"/>
                    <form:hidden path="accountLocked"/> <form:hidden path="credentialsExpired"/>
                    <form:hidden path="department"/>
                </div>
            </c:when> </c:choose>
            <p>
                <c:out value="${buttons}" escapeXml="false"/>
            </p>

        </form:form>
    </div>
</div>

<script type="text/javascript">

    function initPage() {
        $('#userForm').find('input[type=text],textarea,select').filter(':visible:first').focus();
    }

    function passwordChanged(passwordField) {
        var origPassword = "<c:out value="${user.password}"/>";
        if (passwordField.value != origPassword) {
            createFormElement("input", "hidden",
                    "encryptPass", "encryptPass",
                    "true", passwordField.form);
        }
    }

    <!-- This is here so we can exclude the selectAll call when roles is hidden -->
    function onFormSubmit(theForm) {
        <c:if test="${param.from == 'list'}">
        selectAll('userRoles');
        </c:if>
        return validateUser(theForm);
    }
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/common/validator.jsp"/>"></script>

</body>