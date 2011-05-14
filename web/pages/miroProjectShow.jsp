<%@ include file="/common/taglibs.jsp"%>

<jsp:useBean id="miroProject"
	class="uk.co.bluetrail.mobriz.model.MiroProject" scope="request" />
<content tag="heading">
<c:out value="${miroProject.projectTitle}" />
</content>
<head>

	<meta name="menu" content="MiroProjectMenu" />
	<content tag="navname">
	Surveys
	</content>

	<title><fmt:message key="miroProjectDetail.title" />
	</title>



	<script type="text/javascript"
		src="<c:url value='/dwr/interface/ajaxMiroProjectManager.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/scripts/miroProjectFormController.js'/>"></script>

	<script type="text/javascript"
		src="<c:url value='/scripts/dragdrop.js'/>"></script>
	<script type="text/javascript"
		src="<c:url value='/scripts/effects.js'/>"></script>

	<script type="text/javascript">


//global variable to surveycontrolelr object
var mpfc ; 
function initPage(){

	mpfc = new MiroProjectFormController(<c:out value="${miroProject.id}"/>) ;  
    mpfc.loadCandidates();
   // mpfc.getShoppingCartItems();
}




</script>

	<link rel="stylesheet" type="text/css" media="all"
		href="<c:url value='/styles/${appConfig["csstheme"]}/miroSurveyForm.css'/>" />

</head>
<body onLoad="initPage()">
	<ul >
	

	
 	<li>
		<div class="message" id="welcommess" style="display: none;"><p>This is you project control panel. Here you can add and save candidates to the project. 
		You can send out email invitations individually or all together by ticking the check boxes and clicking on "send email". 
		You can send as many invitations as you like but once someone has completed their assessment you will not be able to send another to that candidate.</p>  
		<p>&nbsp;</p>
		<p>When a candidate has completed their assessment a report will appear next to their name. 
		Click on the report to buy it. Once you have bought the report you can download it as many times as you like.
		</p>
		</div>
		<div id="projlist"> 
			<input type="button" style="margin-right: 5px"
				onclick="mpfc.addNewClicked()"
				value="Add a new candidate"/>
				<input type="button" style="margin-right: 5px"
				onclick="mpfc.sendEmails()"
				value="Send Email Invites"/>
				<input type="button" style="margin-right: 5px"
				onclick="location.href='<c:url value="/editMiroProject.html"/>?id=<c:out value="${miroProject.id}"/>'"
				value="Edit Project Details" />
				
			</div>
		
				
					
				
		
			<div id="addNewPanel" style="display:none">
				<div id="addNewFormPanel" >

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
								
								<input id="iFirstName_new" value=""
										>
								</td>
								<td nowrap="nowrap">
									
									<input id="iLastName_new" value="" >
								</td>
								<td nowrap="nowrap">
									<input id="iEmail_new" value="">
										
								</td>
								
								<td nowrap="nowrap">
									
									<div id="iSavePanel_new" >
										<a href="#" onclick="mpfc.saveCandOnly('new')">save</a> | 
										<a href="#" onclick="mpfc.saveCandEmail('new')">save and email</a> |
										<a href="#" onclick="mpfc.cancelAddNewClicked()">hide</a>
										
										
										      
									</div>
									<div id="formSpinner_new" style="display: none;">
										<img class="chutney" src="styles/images/ajax-ball.gif"
											border="0">
									</div>
								</td>
							</tr>
						</table>
					</form>
				</div>
				
			</div>
	
   <div id="sendEmailReportUI" style="display:none">    
      <div id="sendEmailReportUISpinner" style="display: none;">
										<img class="chutney" src="styles/images/ajax-ball.gif"
											border="0"> Sending Emails ...
									</div>
       <table id="sendEmailReportUITable" style="display: none;">
       <th>Email Report | <a href="#" onclick="mpfc.clearSendMailButtonClicked()" >Hide</a></th>
        <tbody id="sendEmailReportTableBody"> </tbody>
        </table>
    </div>
   <div id="formSpinner_cand" style="display: none;">
		<img class="chutney" src="styles/images/ajax-ball.gif"
											border="0">
   </div>
			<div class="table" id="candidatesListUI" style="display:none">

				<table class="candidateTable" width="100%">
					<thead>
						<tr>
							<th>
								#
							</th>
							<th  width="20px">
								<input type="checkbox" id="cSelectAllSendEmail" onclick="mpfc.selectAllSendEmail()" />
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
					<tbody id="candidatesTableBody">
					</tbody>


				</table>


			</div>
		</li>
		
	</ul>


</body>
