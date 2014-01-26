<%@ include file="/common/taglibs.jsp" %>

    <div id="divider"><div></div></div>
<span class="left"><a class="homeurl" href="@HOME-URL@">@HOME-DESC@</a></span>
    <span class="left">Version @APPVERSION@ |
        <c:if test="${pageContext.request.remoteUser != null}">
         <fmt:message key="user.status"/> <authz:authentication operation="fullName"/>
        </c:if>
    </span>
    <span class="right">
        &copy; @COPYRIGHT-YEAR@ <a href="http://www.miro-assessment.com">MiRo Psychometrics Ltd</a>
    </span>
  
