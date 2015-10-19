<%@ include file="/common/taglibs.jsp" %>
<tr>
    <td>
        <select class="form-control" name="<c:out value="${param.leftId}"/>" multiple="multiple" onDblClick="Selectbox.moveSelectedOptions(this,$('#<c:out value="${param.rightId}"/>').get(0),true)" id="<c:out value="${param.leftId}"/>" size="5">
            <c:if test="${leftList != null}"> <c:forEach var="list" items="${leftList}" varStatus="status">
                <option value="<c:out value="${list.value}"/>">
                    <c:out value="${list.label}" escapeXml="false"/>
                </option>
            </c:forEach> </c:if> </select>
    </td>
    <td class="moveOptions">
        <button class="btn btn-default" name="moveRight" id="moveRight<c:out value="${param.listCount}"/>" type="button" onclick="Selectbox.moveSelectedOptions($('#<c:out value="${param.leftId}"/>').get(0),$('#<c:out value="${param.rightId}"/>').get(0),true)">
            &gt;&gt;</button>
        <br/>
        <button class="btn btn-default" name="moveAllRight" id="moveAllRight<c:out value="${param.listCount}"/>" type="button" onclick="Selectbox.moveAllOptions($('#<c:out value="${param.leftId}"/>').get(0),$('#<c:out value="${param.rightId}"/>').get(0),true)">
            All &gt;&gt;</button>
        <br/>
        <button class="btn btn-default" name="moveLeft" id="moveLeft<c:out value="${param.listCount}"/>" type="button" onclick="Selectbox.moveSelectedOptions($('#<c:out value="${param.rightId}"/>').get(0),$('#<c:out value="${param.leftId}"/>').get(0),true)">
            &lt;&lt;</button>
        <br/>
        <button class="btn btn-default" name="moveAllLeft" id="moveAllLeft<c:out value="${param.listCount}"/>" type="button" onclick="Selectbox.moveAllOptions($('#<c:out value="${param.rightId}"/>').get(0),$('#<c:out value="${param.leftId}"/>').get(0),true)">
            All &lt;&lt;</button>
    </td>
    <td>
        <select class="form-control" name="<c:out value="${param.rightId}"/>" multiple="multiple" id="<c:out value="${param.rightId}"/>" size="5">
            <c:if test="${rightList != null}"> <c:forEach var="list" items="${rightList}" varStatus="status">
                <option value="<c:out value="${list.value}"/>">
                    <c:out value="${list.label}" escapeXml="false"/>
                </option>
            </c:forEach> </c:if> </select>
    </td>
</tr>

			