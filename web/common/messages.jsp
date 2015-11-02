<div class="modal fade" id="confirmAlert">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 id="confirmAlertTitle" class="modal-title">Confirm?</h4>
            </div>
            <div class="modal-body">
                <p id="confirmAlertBody"></p>
            </div>
            <div class="modal-footer">
                <button id="confirmAlertCloseButton" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button id="confirmAlertOkButton" type="button" class="btn btn-primary">Ok</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div id="warning-alert" class="alert alert-warning alert-dismissible hidemeh" role="alert">
    <button type="button" class="close" onclick="closeAlert('#warning-alert')"><span aria-hidden="true">&times;</span></button>
    <strong>Warning!</strong> <span id="warning-alert-mess"></span>
</div>

<%-- Error Messages --%>
<c:if test="${not empty errors}">
    <div id="errorMessages" class="alert alert-danger alert-dismissible" role="alert">
        <button type="button" class="close" onclick="closeAlert('#errorMessages')"><span aria-hidden="true">&times;</span></button>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="errors"/>
</c:if>


<%-- Success Messages --%>
<c:if test="${not empty successMessages}">
    <div id="successMessages" class="alert alert-success alert-dismissible" role="alert">
        <button type="button" class="close" onclick="closeAlert('#successMessages')"><span aria-hidden="true">&times;</span></button>
        <c:forEach var="msg" items="${successMessages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="successMessages" scope="session"/>
</c:if>
