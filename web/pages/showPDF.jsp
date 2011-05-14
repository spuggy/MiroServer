<%@ include file="/common/taglibs.jsp"%>

<%
String pdf = (String) request.getAttribute("pdf");
response.sendRedirect("miro/out/"+pdf + ".pdf"); 
 %>
