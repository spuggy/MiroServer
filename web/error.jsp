<%@ page language="java" isErrorPage="true" %>
<%@ include file="/common/taglibs.jsp" %>

<page:applyDecorator name="default">

    <title><fmt:message key="errorPage.title"/></title>
    <content tag="heading"><fmt:message key="errorPage.heading"/></content>

    <div class="row">
        <div id="miro-content" class="col-sm-8">

            <p>
                <fmt:message key="errorPage.message">
                    <fmt:param><c:url value="/"/></fmt:param>
                </fmt:message>
            </p>
            <%@ include file="/common/messages.jsp" %>
            <% if (exception != null) { %>
            <pre><% exception.printStackTrace(new java.io.PrintWriter(out)); %></pre>
            <% } else if ((Exception)request.getAttribute("javax.servlet.error.exception") != null) { %>
                    <pre><% ((Exception)request.getAttribute("javax.servlet.error.exception"))
                            .printStackTrace(new java.io.PrintWriter(out)); %></pre>
            <% } %>


        </div>
    </div>

</page:applyDecorator>

