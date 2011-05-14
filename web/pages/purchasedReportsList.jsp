<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="purchasedReport.title"/></title>
    <content tag="heading"><fmt:message key="purchasedReport.heading"/></content>
    <meta name="menu" content="MiroProjectMenu"/>
</head>


<ul>
<li>


<display:table name="purchasedReports" cellspacing="0" cellpadding="0" requestURI=""   defaultsort="1" id="users" pagesize="100" class="table" export="false">
 
  <display:column property="updated_at" title="Purchased" decorator="uk.co.bluetrail.mobriz.webapp.util.ShortDateDecorator" />
    
    <display:column property="fullName" escapeXml="true"  title="Name"   />
  
    
    <display:setProperty name="paging.banner.item_name" value="report"/>
    <display:setProperty name="paging.banner.items_name" value="reports"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
</li></ul>
