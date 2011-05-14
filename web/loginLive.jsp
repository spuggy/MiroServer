<%@ include file="/common/taglibs.jsp"%>




<c:choose>
<c:when test="${empty param.error}">
	<c:redirect url="../login/"/>
</c:when>  
<c:otherwise>
<c:redirect url="../182"/>
</c:otherwise>

</c:choose>
