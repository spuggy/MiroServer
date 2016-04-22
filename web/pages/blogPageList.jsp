<%@ include file="/common/taglibs.jsp"%>

<title>News</title>
<content tag="heading">News</content>
<meta name="menu" content="MiroProjectMenu"/>

<div class="row">
    <div id="miro-content" class="col-sm-8">

    <c:forEach var="webPage" items="${webPageList}" >
        <h3 class="blog-post-title"><c:out value="${webPage.description}" escapeXml="false"/></h3>
        <p class="blog-post-meta"><fmt:formatDate type="date" value="${webPage.publishedDate}" /></p>
        <c:out value="${webPage.pageText}" escapeXml="false"/>
        <hr/>
    </c:forEach>


    </div>
</div>


