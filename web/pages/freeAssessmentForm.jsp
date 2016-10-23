<%@ include file="/common/taglibs.jsp"%>

<head>
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.11.1.min.js'/>"></script>
<script type="text/javascript">
    $(document).ready(function(e) {

        $("#freeAssessmentForm").submit(function() {
            $.post("freeAssessmentForm.html", $("#freeAssessmentForm").serialize())
                    .done(function(data) {
                        alert(data)
                    });
            return false;
        })
    });
</script>

</head>

<div class="row">

<div class="col-sm-8">


    <form:form commandName="miroCandidateAjaxDTO" method="post" action="freeAssessmentForm.html" id="freeAssessmentForm">

        <form:input path="firstName" id="firstName" cssClass="form-control"/><br/>
        <form:input path="lastName" id="lastName" cssClass="form-control"/><br/>
        <form:input path="emailAddress" id="emailAddress" cssClass="form-control"/><br/>


        <input type="submit" class="button" name="save" /> <br/>

    </form:form>

    </div>
</div>