<%@ include file="/common/taglibs.jsp" %>

<page:applyDecorator name="default">

    <title><fmt:message key="403.title"/></title>
    <content tag="heading"><fmt:message key="403.title"/></content>

    <div class="row">
        <div id="miro-content" class="col-sm-8">

            <p>
                <fmt:message key="403.message"> <fmt:param><c:url value="/"/> </fmt:param> </fmt:message>
            </p>


        </div>
    </div>

</page:applyDecorator>
