package uk.co.bluetrail.mobriz.webapp.util;

import java.math.BigInteger;

public class DeptAccountReportRow {

	private String pname;
	private int teamnumber = 0 ;
	private int indnumber = 0 ;

    public DeptAccountReportRow(Object pname) {
		
		this.pname = (String) pname;

	}

    public String getPname() {
        return pname;
    }

    public void setPname(Object pname) {
        this.pname = (String) pname;
    }

    public int getTeamnumber() {

        return this.teamnumber;
    }

    public void setTeamnumber(Object teamnumber) {
        this.teamnumber = ((BigInteger) teamnumber).intValue();
    }

    public int getIndnumber() {

        return indnumber;
    }

    public void setIndnumber(Object indnumber) {

        this.indnumber = ((BigInteger) indnumber).intValue();
    }
}