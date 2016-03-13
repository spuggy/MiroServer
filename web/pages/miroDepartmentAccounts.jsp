<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="purchasedReport.title"/></title>
    <content tag="heading"><fmt:message key="menu.departmentadmin"/>: <c:out value="${department}" /> <c:out value="${month_selected_str}" />/<c:out value="${year_selected}" /></content>
    <meta name="menu" content="MiroProjectMenu"/>
</head>

<div class="row">
    <div id="miro-content" class="col-sm-8">

        <form class="form-inline" action="departmentAccounts.html" method="GET">
            <div class="form-group">
                <label for="year_selected">Year</label>
                <select name="year_selected" id="year_selected">
                    <option >2013</option>
                    <option >2014</option>
                    <option >2015</option>
                    <option >2016</option>
                    <option >2017</option>
                    <option >2018</option>
                    <option >2019</option>
                    <option >2020</option>
                    <option >2021</option>
                    <option >2022</option>
                    <option >2023</option>
                </select>
            </div>
            <div class="form-group">
                <label for="month_selected">Month</label>
                <select name="month_selected" id="month_selected">
                    <option value="1">January</option>
                    <option value="2">February</option>
                    <option value="3">March</option>
                    <option value="4">April</option>
                    <option value="5">May</option>
                    <option value="6">June</option>
                    <option value="7">July</option>
                    <option value="8">August</option>
                    <option value="9">September</option>
                    <option value="10">October</option>
                    <option value="11">November</option>
                    <option value="12">December</option>
                    </select>

            </div>
            <button type="submit" class="btn btn-default">Submit</button>
        </form>



            <display:table name="reportLines" cellspacing="0" cellpadding="0" requestURI=""
                           id="reportLines" pagesize="200" class="table table-condensed table-hover" export="true" decorator="org.displaytag.decorator.TotalTableDecorator">

                <display:setProperty name="export.pdf" value="false"/>
                <display:setProperty name="export.xml" value="false"/>
                <display:setProperty name="export.csv" value="false"/>
                <display:setProperty name="export.excel.filename">mirototals_<%=request.getParameter("year_selected")%>-<%=request.getParameter("month_selected")%>.xls</display:setProperty>


                <display:column  property="pname" titleKey="departmentadmin.name"   />
                <display:column  property="indnumber" titleKey="departmentadmin.individual_reports"  total="true" format="{0,number,#.##}"/>
                <display:column  property="teamnumber" titleKey="departmentadmin.team_reports"  total="true" format="{0,number,#.##}" />




                <display:setProperty name="paging.banner.item_name" value="Practitioner"/>
                <display:setProperty name="paging.banner.items_name" value="Practitioners"/>
            </display:table>





    </div>
</div>




<script type="text/javascript">
    highlightTableRows("reportList");
</script>
