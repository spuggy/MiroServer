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

    <form:form commandName="miroCandidateAjaxDTO" method="post" action="freeAssessmentForm.html" id="freeAssessmentForm">
        <p>first name<form:input path="firstName" id="firstName" cssClass="form-control"/></p>
        <p>last name<form:input path="lastName" id="lastName" cssClass="form-control"/></p>
        <p>email<form:input path="emailAddress" id="emailAddress" cssClass="form-control"/></p>

        <input type="submit" class="button" name="save" /> <br/>

    </form:form>

    </div>
</div>

<div id="emailSuccess" class="alert alert-success hidemeh col-sm-8 hidemeh" role="alert">

    <p>cheers ears .. we will send you a trial assessment!</p>

</div>


