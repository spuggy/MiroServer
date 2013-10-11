package uk.co.bluetrail.mobriz.webapp.util;

public class DeptAccountReportRow {

	private String pname;
	private String teamnumber;
	private String indnumber;

	
	public DeptAccountReportRow(Object pname) {
		
		this.pname = (String) pname;

	}

    public String getPname() {
        return pname;
    }

    public void setPname(Object pname) {
        this.pname = (String) pname;
    }

    public String getTeamnumber() {
        if(this.teamnumber==null || this.teamnumber.equals("")) {
            return "0";
        }

        return this.teamnumber;
    }

    public void setTeamnumber(Object teamnumber) {
        this.teamnumber = teamnumber.toString();
    }

    public String getIndnumber() {
        if(this.indnumber==null || this.indnumber.equals("")) {
            return "0";
        }
        return indnumber;
    }

    public void setIndnumber(Object indnumber) {
        this.indnumber = indnumber.toString();
    }
}