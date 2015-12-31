<%@ include file="/common/taglibs.jsp" %>

<jsp:useBean id="miroProject" class="uk.co.bluetrail.mobriz.model.MiroProject" scope="request"/>
<content tag="heading">
    <c:out value="${miroProject.projectTitle}"/>
</content>
<head>

    <meta name="menu" content="MiroProjectMenu"/>

    <title><fmt:message key="miroProjectDetail.title"/>
    </title>

    <script type="text/javascript" src="<c:url value='/dwr/interface/ajaxMiroProjectManager.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/miro11style/js/miroProjectFormController06.js'/>"></script>


    <script type="text/javascript">


        //global variable to surveycontrolelr object
        var mpfc;
        function initPage() {
            defaultInitPage();
            mpfc = new MiroProjectFormController(<c:out value="${miroProject.id}"/>);
            mpfc.loadCandidates();
        }


    </script>

    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>"/>


</head>
<body onLoad="initPage()">

<!-- Modal -->
<div class="modal fade" id="downloadreport" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Download MiRo Report</h4>
            </div>
            <div class="modal-body">
                <p>Please choose which report you would like to download.</p>

                <ul>
                    <li>
                        <p><a id="miro11download" href="#"><b>MiRo Stage 2</b> (Enhanced report)</a></p>
                    </li>
                    <li>
                        <p><a id="miro10download" href="#"><b>MiRo Stage 1</b> (Standard report)</a></p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>


<div class="row">
    <div class="col-sm-8">


        <div class="message" id="welcommess" style="display: none;">
            <p>This is your project control panel. Here you can
                add and save candidates to the project. You can send out email invitations individually or all together by
                ticking the check boxes and clicking on "send email". You can send as many invitations as you like but once
                someone has completed their assessment you will not be able to send another to that candidate.</p>
            <p>When a candidate has completed their assessment a report will appear next to their name. Click on the
                report to buy it. Once you have bought the report you can download it as many times as you like. </p>
        </div>

        <div id="projlist">

            <button class="btn btn-primary btn-sm" onclick="mpfc.addNewClicked()">Add a new candidate</button>
            <button class="btn btn-default btn-sm" onclick="mpfc.sendEmails()">Send Email Invites</button>
            <button class="btn btn-default btn-sm" onclick="location.href='<c:url value="/editMiroProject.html"/>?id=<c:out value="${miroProject.id}"/>'">Edit Project Details</button>

        </div>


        <div id="addNewPanel" style="display:none">
            <div id="addNewFormPanel">

                <form id="addNewForm">
                    <table>
                        <tr>
                            <th>
                                First Name
                            </th>
                            <th>
                                Last Name
                            </th>
                            <th>
                                Email
                            </th>
                            <th>
                                &nbsp;
                            </th>
                        </tr>

                        <tr>

                            <td nowrap="nowrap">

                                <input id="iFirstName_new" value="">
                            </td>
                            <td nowrap="nowrap">

                                <input id="iLastName_new" value="">
                            </td>
                            <td nowrap="nowrap">
                                <input id="iEmail_new" value="">

                            </td>

                            <td nowrap="nowrap">

                                <div id="iSavePanel_new">
                                    <a href="#" onclick="mpfc.saveCandOnly('new')">save</a> |
                                    <a href="#" onclick="mpfc.saveCandEmail('new')">save and email</a> |
                                    <a href="#" onclick="mpfc.cancelAddNewClicked()">hide</a>
                                </div>
                                <div id="formSpinner_new" style="display: none;">
                                    <img class="chutney" src="styles/images/ajax-ball.gif" border="0">
                                </div>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>

        </div>

        <div id="sendEmailReportUI" style="display:none">
            <div id="sendEmailReportUISpinner" style="display: none;">
                <img class="chutney" src="styles/images/ajax-ball.gif" border="0"> Sending Emails ...
            </div>
            <table id="sendEmailReportUITable" style="display: none;">
                <th>Email Report | <a href="#" onclick="mpfc.clearSendMailButtonClicked()">Hide</a></th>
                <tbody id="sendEmailReportTableBody"></tbody>
            </table>
        </div>
        <div id="formSpinner_cand" style="display: none;">
            <img class="chutney" src="styles/images/ajax-ball.gif" border="0">
        </div>
        <div class="table" id="candidatesListUI" style="display:none">

            <table class="table table-condensed table-hover" width="100%">
                <thead>
                <tr>
                    <th>
                        #
                    </th>
                    <th width="20px">
                        <input type="checkbox" id="cSelectAllSendEmail" onclick="mpfc.selectAllSendEmail()"/>
                    </th>
                    <th>
                        First Name
                    </th>
                    <th>
                        Last Name
                    </th>
                    <th>
                        Email
                    </th>
                    <th>
                        Status
                    </th>
                    <th>
                        &nbsp;
                    </th>
                </tr>
                </thead>
                <tbody id="candidatesTableBody"></tbody>


            </table>


        </div>
    </div>
    <div class="col-sm-4">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h2 class="panel-title">EMAILS NOT GETTING THROUGH?</h2>
            </div>
            <div class="panel-body bg-info">

                <p>Sometimes companies employ email filters that can mistakenly mark your invite as spam. If your invite
                    is trapped the candidate will not receive their login!</p>

                <p>If your MiRo emails are being caught by spam filters make sure you have the <strong>Copy email invites</strong> checkbox set in the project form (click edit project details).  This will
                    bcc you copies of your email invites to your inbox.  This means you will have a copy of the user credentials and be able to resend them from your
                    email account.</p>
                <p>Alternatively, click "edit" next to the candidate's name and change the email to your address and send the credentials to your email account.</p>

            </div>
        </div>
    </div>
</div>


</body>
