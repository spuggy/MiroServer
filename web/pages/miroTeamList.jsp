<%@ include file="/common/taglibs.jsp" %>
<head>
    <title><fmt:message key="miroTeamMap.title"/>
    </title>
    <content tag="heading">
        <fmt:message key="miroTeamReport.heading"/>
    </content>
    <meta name="menu" content="MiroProjectMenu"/>
    <script type="text/javascript" src="/mirotest/miro11style/js/selectbox.js"></script>

</head>


<!-- Modal -->
<div class="modal fade" id="teamselector" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Select Projects</h4>
            </div>
            <div class="modal-body form-group">

                <p>Use the buttons or double-click a project name to add to the list</p>

                <form:form commandName="miroProjectSelectorForm" method="post" action="editMiroTeamReport.html" id="miroProjectSelectorForm" onsubmit="onFormSubmit(this)">

                        <table class="pickList">
                            <tr>
                                <th class="pickLabel">
                                    <mobriz4server:label key="miroProjectSelectorForm.projects" colon="false" styleClass="required"/>
                                </th>
                                <td>&nbsp;</td>
                                <th class="pickLabel">
                                    <mobriz4server:label key="miroProjectSelectorForm.includedProjects" colon="false" styleClass="required"/>
                                </th>
                            </tr>
                            <c:set var="leftList" value="${myProjects}" scope="request"/>
                            <c:set var="rightList" value="${emptyList}" scope="request"/>
                            <c:import url="/common/pickList.jsp"> <c:param name="listCount" value="1"/>
                                <c:param name="leftId" value="availableProjects"/>
                                <c:param name="rightId" value="selectedProjects"/>
                            </c:import>
                        </table>

                </form:form>
                <div class="modal-footer">
                    <button class="btn btn-primary" onclick="$('#miroProjectSelectorForm').submit()"><fmt:message key="button.submit"/></button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>

            </div>
        </div>
    </div>
</div>

<div class="row">
    <div id="miro-content" class="col-sm-8">

        <div class="message" id="welcommes">
            <p><fmt:message key="miroTeamReport.intro"/></p>

            <p><br/>
                <button class="btn btn-primary" onclick="$('#teamselector').modal('show')">New Team Report</button>
            </p>

        </div>
    </div>

</div>
<div class="row">
    <div class="col-sm-8">


        <display:table name="miroTeamList" cellspacing="0" cellpadding="0" requestURI="" id="miroTeamList" pagesize="25" class="table table-condensed table-hover" export="false" decorator="uk.co.bluetrail.mobriz.webapp.util.MiroTeamTableDecorator">

            <display:column property="miroTeamName" escapeXml="true" sortable="true" titleKey="miroteam.miroteamname" url="/editMiroTeamReport.html" paramId="id" paramProperty="id"/>
            <display:column property="updated_at" titleKey="surveyForm.updated_at" decorator="uk.co.bluetrail.mobriz.webapp.util.ShortDateDecorator"/>

            <display:column property="teamReportStatus" titleKey="miroteam.miroteamreportstatus"/>

            <display:setProperty name="paging.banner.placement" value="bottom"/>
            <display:setProperty name="paging.banner.item_name" value="miroTeam"/>
            <display:setProperty name="paging.banner.items_name" value="miroTeams"/> </display:table>

    </div>
</div>


