<%@ include file="/common/taglibs.jsp" %>


<div class="row miro-footing-padding">
    <div id="miro-content" class="col-sm-8">
    <hr/>
    <p class="left">Version @APPVERSION@ |
        <c:if test="${pageContext.request.remoteUser != null}"> <fmt:message key="user.status"/>
            <authz:authentication operation="fullName"/> </c:if>
    </p>
    <p class="right">
        &copy; @COPYRIGHT-YEAR@ <a href="http://www.miro-assessment.com">MiRo Psychometrics Ltd</a>
    </p>

    </div>
</div>