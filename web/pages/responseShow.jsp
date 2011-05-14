<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="uk.co.bluetrail.mobriz.model.*" %>
<%@ page import="uk.co.bluetrail.mobriz.*" %>

<jsp:useBean id="surveyResponse" class="uk.co.bluetrail.mobriz.model.SurveyResponse" scope="request"/>

<title><fmt:message key="surveyResponseDetail.title"/></title>
<content tag="heading"><c:out value="${surveyResponse.survey.title}" />,  <c:out value="${surveyResponse.createdBy_id}" />  on <c:out value="${surveyResponse.created_on}" /></content>
<content tag="pagemenu"><c:out value="${surveyResponse.survey.title}" />,  <c:out value="${surveyResponse.createdBy_id}" />  on <c:out value="${surveyResponse.created_on}" /></content>

<meta name="menu" content="responses"/>







<table class="table">
	<thead>
	
				<tr>
				<th>#</th>
				<th>Question</th>
				<th>Answer</th>

	</tr>
	</thead>
	<tbody>
	<%

Survey thisSurvey = surveyResponse.getSurvey();
HashMap questionMap = thisSurvey.getQuestionMap() ; 
String[] ansQ = surveyResponse.getQuestionsAnswered();

Question question;
String ansStr =null;
String andHTML = null ;
int i = 0 ;
question = (Question) questionMap.get(thisSurvey.getFirstQuestion_id());
while(question != null) {
  
    

   	if(Question.QTYPE_PICTURE.equals(question.getQTypeStr()))   {
   				try { 
				String[] imageData = surveyResponse.getAnswer(question).split(SurveyResponse.SEP);
				StringBuffer imgStr = new StringBuffer() ;
				imgStr.append(Constants.PHOTODIR);
				imgStr.append('/');
				imgStr.append(surveyResponse.getSurvey_id());
				imgStr.append('/');
				imgStr.append(surveyResponse.getCreatedBy_id());
				imgStr.append('_');
				imgStr.append(imageData[1]);
				imgStr.append('.');
				imgStr.append(imageData[2]);
		  	
   				ansStr = "<img src=\"" + imgStr + "\"/><br/>"+imageData[0];
   				} catch (Exception e) {
   				  //must be a blank question so return the anwser
   				 ansStr = surveyResponse.getAnswer(question);
   				}   	
   	} else {
   		ansStr = surveyResponse.getAnswer(question);
   	}
    
    if(ansStr != null && !ansStr.equals("n/a")) { 
    
           %>
    
        <TR class="<%= i%2==0 ? "odd" : "even"%>">
         <TD><%=i+1%></TD>
        <TD><%=question.getQTxt()%></TD>
        
        <TD><%=ansStr%>        	
        </TD>
        </TR>
        <%
        i = i + 1 ; 
     }
         
         question = (Question) questionMap.get(question.getJQuestion_id());
    }
%>


</tbody>


</table>

