<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="/common/taglibs.jsp"%>

<head>
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.11.1.min.js'/>"></script>
<script type="text/javascript">
    var OK = 1;
    $(document).ready(function(e) {
        $("#freeAssessmentForm").submit(function() {
            $(".error").hide();
            $.post("freeAssessmentForm.html", $("#freeAssessmentForm").serialize())
                    .done(function(rawData) {
                        var data=$.parseJSON(rawData);
                        if(data.dto.status == OK ) {
                            $("#emailForm").hide();
                            $("#emailSuccess").show();
                            return ;
                        } else {
                            if(data.dto.formErrors!=null) {
                                for(i=0;i<data.dto.formErrors.length;i++){
                                    var errId = data.dto.formErrors[i];
                                    $("#"+errId).show();
                                }
                            }
                        }
                    });
            return false;
        })
    });
</script>

</head>

<div class="row">

<div id="emailForm" class="col-sm-8" >

    <div id="GENERAL" class="alert alert-danger error hidemeh" role="alert">Ooo something nasty happened! try again</div>
    <div id="FIRST_NAME_BLANK" class="alert alert-danger error hidemeh" role="alert">first name is required</div>
    <div id="LAST_NAME_BLANK" class="alert alert-danger error hidemeh" role="alert">last name is required</div>
    <div id="EMAIL_BLANK" class="alert alert-danger error hidemeh" role="alert">email is required</div>
    <div id="BAD_EMAIL" class="alert alert-danger error hidemeh" role="alert">the email is invalid</div>
    <div id="DUPE" class="alert alert-danger error hidemeh" role="alert">a report has already been requested with this email</div>
    <div id="BAD_PHONE" class="alert alert-danger error hidemeh" role="alert">phone number is required</div>
    <div id="BAD_CAPATCHA" class="alert alert-danger error hidemeh" role="alert">woooahh .. are you sure you are human?</div>


    <form:form commandName="miroCandidateAjaxDTO" method="post" action="freeAssessmentForm.html" id="freeAssessmentForm">
        <form:hidden path="captchaIdx" id="captchaIdx"/>
        <p>first name<br/><form:input path="firstName" id="firstName" cssClass="form-control"/></p>
        <p>last name<br/><form:input path="lastName" id="lastName" cssClass="form-control"/></p>
        <p>phone number<br/><form:input path="phoneNumber" id="phoneNumber" cssClass="form-control"/></p>
        <p>email<br/><form:input path="emailAddress" id="emailAddress" cssClass="form-control"/></p>
        <p>prove you're human:

            <c:choose>
                <c:when test="${miroCandidateAjaxDTO.captchaIdx=='0'}">
                    what is 6 + 4?
                </c:when>
                <c:when test="${miroCandidateAjaxDTO.captchaIdx=='1'}">
                    what is 10 + 3?
                </c:when>
                <c:when test="${miroCandidateAjaxDTO.captchaIdx=='2'}">
                    what is 6 + 3?
                </c:when>
            </c:choose>

            <br/><form:input path="captchaAnswer" id="captchaAnswer" cssClass="form-control"/></p>

        <input type="submit" class="button" name="save" /> <br/>

    </form:form>

    </div>
</div>

<div id="emailSuccess" class="alert alert-success hidemeh col-sm-8 hidemeh" role="alert">

    <p>cheers ears .. we will send you a trial assessment!</p>

</div>


