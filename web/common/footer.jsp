<%@ include file="/common/taglibs.jsp" %>


<div class="row ">
    <div class="col-sm-12 miro-footing-padding miro-subheader-padding">
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