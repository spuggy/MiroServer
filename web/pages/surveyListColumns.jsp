 
 <display:column titleKey="surveyForm.visibility"  >
    
    <c:choose>
	 <c:when test="${row.visibility=='0'}">
			<fmt:message key="surveyForm.selectVisibility" />
	 </c:when>
	 <c:when test="${row.visibility=='1'}">
     		<fmt:message key="surveyForm.visibleToAll" />
	 </c:when>
	 <c:when test="${row.visibility=='2'}">
   	 	    <fmt:message key="surveyForm.visibleToSelectedUsers" />
	 </c:when>
</c:choose>
</display:column>    
   

     
    <display:column property="created_on"  headerClass="sortable"
         titleKey="surveyForm.created_on"  decorator="uk.co.bluetrail.mobriz.webapp.util.ShortDateDecorator"  />
     <display:column property="updated_at"  headerClass="sortable"
         titleKey="surveyForm.updated_at" decorator="uk.co.bluetrail.mobriz.webapp.util.ShortDateDecorator" />
    
    
    
    
    