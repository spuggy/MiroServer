<%@ include file="/common/taglibs.jsp" %>

<title><fmt:message key="miroTeamMap.title"/>
</title>
<head>
    <script type="text/javascript" src="/mirotest/miro11style/js/selectbox.js"></script>
</head>
<body onLoad="initPage()">

<content tag="heading">

    <c:choose>
        <c:when test="${miroTeam.miroTeamName==null}">
            <fmt:message key="miroTeamReport.heading"/>
        </c:when>
        <c:otherwise>
            <fmt:message key="miroTeamReport.heading"/>: <c:out value="${miroTeam.miroTeamName}"/>
        </c:otherwise>
    </c:choose>


</content>
<meta name="menu" content="MiroProjectMenu"/>


<div class="row">
    <div class="col-sm-8">

        <p>The team report images are displayed below.  If you would like to create a team report PDF complete the form and click "Save and create PDF".</p>

        <c:if test="${showReportInprogressMessage == true}">
            <div class="alert alert-info">
                The Team report is being generated - please check back shortly!
            </div>
        </c:if>

        <c:if test="${showDownloadLink == true}">
            <div class="alert alert-info">

                <p>A report has been generated for this team. Click the link below to download.</p><br/>

                <a href="miroTeamReportShow.html?id=<c:out value="${miroTeam.id}" />"/><img src="miro11style/img/document_pdf.png"/></a>
            </div>
        </c:if>

        <hr/>

        <div class="buttonPadding">
            <button class="btn btn-primary" name="createTeamreportButton" id="createTeamreportButton"/>
            Save and create PDF </button><c:if test="${showDeleteButton == true}">
            <button class="btn btn-default" name="deleteReport" id="deleteReport">delete</button>
        </c:if>
        </div>


    </div>
</div>

<div class="row">
    <div class="col-sm-8">
        <form:form commandName="miroProjectSelectorForm" method="post" action="" id="miroProjectSelectorForm">

            <div class="modal fade" id="teamuserselector" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Select Candidates</h4>
                        </div>
                        <div class="modal-body form-group">

                            <p>Use the buttons remove or add a candidate</p>


                            <table class="pickList">
                                <tr>
                                    <th class="pickLabel">
                                        <mobriz4server:label key="miroProjectTeamsSelectorForm.notIncludedNames" colon="false" styleClass="required"/>
                                    </th>
                                    <td>&nbsp;</td>
                                    <th class="pickLabel">
                                        <mobriz4server:label key="miroProjectTeamsSelectorForm.includedNames" colon="false" styleClass="required"/>
                                    </th>
                                </tr>

                                <c:set var="leftList" value="${unselectedUserList}" scope="request"/>
                                <c:set var="rightList" value="${teamUsers}" scope="request"/>
                                <c:import url="/common/pickList.jsp"> <c:param name="listCount" value="1"/>
                                    <c:param name="leftId" value="selectedUserList"/>
                                    <c:param name="rightId" value="teamUsers"/> </c:import>
                                <tr>
                                    <td colspan="3">&nbsp;</td>
                                </tr>
                            </table>
                            <div class="modal-footer">
                                <button class="btn btn-primary" id="teamuserselectorSubmitButton">
                                    <fmt:message key="button.submit"/></button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <form:hidden path="id"/> <form:hidden path="version"/> <input type="hidden" id="delete" name="delete"/>
            <input type="hidden" id="save" name="save"/>
            <input type="hidden" id="createteamreport" name="createteamreport"/>

            <c:forEach var="project_id" items="${selectedProjects}" varStatus="status">
                <input type="hidden" name="selectedProjects" value="<c:out value="${project_id}"/>"/> </c:forEach>


            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroteam.miroteamname"/>
                <form:errors path="miroTeamName" cssClass="fieldError"/>
                <form:input path="miroTeamName" id="miroTeamName" cssClass="form-control"/>
            </div>

            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroProjectTeamsSelectorForm.includedNames"/>
                <div class="panel panel-default">
                    <div class="panel-body">
                            <c:out value="${teamUsersPlain}" escapeXml="false"/>
                        <a class="btn btn-default btn-xs pull-right" id="teamSelectButton">edit</a>
                    </div>
                </div>
            </div>


            <div class="form-group">
                <mobriz4server:label styleClass="control-label" key="miroTeamMap.commentary"/>
                <form:errors path="commentary" cssClass="fieldError"/>
                <form:textarea path="commentary" id="commentary" cssClass="form-control" rows="5"/>
            </div>


        </form:form>


    </div>
</div>


<div class="row">
    <div class="col-sm-8">
        <hr/>
        <h2>
            <fmt:message key="miroTeamMap.teamMapImageLeading"/>
        </h2>
        <img src="<c:url value='/teamImage/teamMapImageLeading.jpg?engaged=false'/>" width="600" height="450"/>
    </div>
</div>
<div class="row">
    <div class="col-sm-8">
        <h2>
            <fmt:message key="miroTeamMap.teamMapImageEngaged"/>
        </h2>
        <img src="<c:url value='/teamImage/teamMapImageEngaged.jpg?engaged=true'/>" width="600" height="450"/>
    </div>
</div>
<div class="row">
    <div class="col-sm-8">
        <h2>
            <fmt:message key="miroTeamMap.teamMapNames"/>
        </h2>
        <display:table name="teamMapData" cellspacing="0" cellpadding="0" requestURI="" id="teamMapData" pagesize="500" class="table table-condensed" export="false">

            <display:column property="fullName" escapeXml="true" titleKey="miroTeamMap.fullName"/>
            <display:column property="initials" escapeXml="true" titleKey="miroTeamMap.initials"/>
            <display:column property="leadingMode" escapeXml="true" titleKey="miroTeamMap.leadingMode"/>
            <display:column property="secondaryMode" escapeXml="true" titleKey="miroTeamMap.secondaryMode"/>

            <display:setProperty name="paging.banner.item_name" value="Name"/>
            <display:setProperty name="paging.banner.items_name" value="Names"/> </display:table>
    </div>
</div>

<script type="application/javascript">

    function initPage() {
        setPlaceHolder("miroTeamName", "The title of your report - will appear on the front page");
        setPlaceHolder("commentary", "Addtional information that will appear at the end of the team report");

        $('#teamSelectButton').click(function () {
            $('#teamuserselector').modal('show')
        });

        $('#teamuserselectorSubmitButton').click(function () {
            selectAll('teamUsers');
            $('#miroProjectSelectorForm').submit();
        });

        $('#createTeamreportButton').click(function () {
            selectAll('teamUsers');

            if ($('#miroTeamName').val() == "") {
                alert("Please supply a value for the Team Name");
                return false;

            } else {
                $('#createteamreport').val("createteamreport");
                $('#miroProjectSelectorForm').submit();
            }

        });

        $('#deleteReport').click(function () {

            miroConfirm("Are you sure you want to delete?", function (result) {
                if (result == true) {
                    $('#delete').val("delete");
                    $('#miroProjectSelectorForm').submit();
                }
            });

        });


    }

</script>

</body>
