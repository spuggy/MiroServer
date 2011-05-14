package uk.co.bluetrail.mobriz.webapp.form;

import javax.servlet.http.HttpServletRequest;



/**
 * A from used to send an SMS message to a user.
 * 
 * @author <a href="mailto:rspence@bluetrail.co.uk">Richard Spence</a>
 * 
 * @struts.form name="WebServiceForm"
 */
public class WebServiceForm  {
	
	String[] sid ; //survey id
	String[] cp ; //checkpoint 
	String[] qid ;  // queston id 
	String[] a ; // andserstring 
	String[] st ; // 
	String pid ; //photoid
	String oc ; 
	String up ;  //
	String mv ;  //mobriz version 
	String dsp ; 
	String tdsp ; 		
	String bt ; //
	String ver ; // version	
	String ext ; //phto extension;

	
	String lid ; //lookupid
	
	String[] lkv ; //lookup search fields
	
	
	private byte[] file;

	   
	public void setFile(byte[] file) {
	        this.file = file;
	}

	public byte[] getFile() {
	        return file;
	}
	
	
	public String toString() {
		
		StringBuffer str = new StringBuffer() ;
		
		str.append("up=") ;
		str.append(getUp()) ;
		str.append('\n') ;
		str.append("mv=");
		str.append(getMv()) ;
		str.append('\n') ;
		str.append("dsp=");
		str.append(getDsp()) ;
		str.append('\n') ;
		str.append("bt=");
		str.append(getBt()) ;
		str.append('\n') ;
		str.append("ver=");
		str.append(getVer()) ;
		str.append('\n') ;
		str.append("oc=");
		str.append(getOc()) ;
		str.append('\n') ;
		
		if(sid==null) {
			str.append("sid=");
			str.append("null") ;
			str.append('\n') ;
		} else {
			for(int i = 0 ; i < sid.length; i++) {
				str.append("sid=");
				str.append(sid[i]) ;
				str.append('\n') ;
			}
		}
		
		if(cp==null) {
			str.append("cp=");
			str.append("null") ;
			str.append('\n') ;
		} else {
			for(int i = 0 ; i < cp.length; i++) {
				str.append("cp=");
				str.append(cp[i]) ;
				str.append('\n') ;
			}
		}
		
		if(qid==null) {
			str.append("qid=");
			str.append("null") ;
			str.append('\n') ;
		} else {
			for(int i = 0 ; i < qid.length; i++) {
				str.append("qid=");
				str.append(qid[i]) ;
				str.append('\n') ;
			}
		}
		
		if(a==null) {
			str.append("a=");
			str.append("null") ;
			str.append('\n') ;
		} else {
			for(int i = 0 ; i < a.length; i++) {
				str.append("a=");
				str.append(a[i]) ;
				str.append('\n') ;
			}
		}
		
		if(st==null) {
			str.append("st=");
			str.append("null") ;
			str.append('\n') ;
		} else {
			for(int i = 0 ; i < st.length; i++) {
				str.append("st=");
				str.append(st[i]) ;
				str.append('\n') ;
			}
		}
		
		 
		
		return str.toString();
		
		
		
		
	}
	
	public String getVer() {
		return this.ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getBt() {
		return this.bt;
	}
	public void setBt(String bt) {
		this.bt = bt;
	}
	public String getDsp() {
		return this.dsp;
	}
	public void setDsp(String dsp) {
		this.dsp = dsp;
	}
	public String getMv() {
		return this.mv;
	}
	public void setMv(String mv) {
		this.mv = mv;
	}
	public String getOc() {
		return this.oc;
	}
	public void setOc(String oc) {
		this.oc = oc;
	}
	
	public String[] getSid() {
		return this.sid;
	}
	public void setSid(String[] sid) {
		this.sid = sid;
	}
	public String getTdsp() {
		return this.tdsp;
	}
	public void setTdsp(String tdsp) {
		this.tdsp = tdsp;
	}
	public String getUp() {
		return this.up;
	}
	public void setUp(String up) {
		this.up = up;
	}
	public String[] getCp() {
		return this.cp;
	}
	public void setCp(String[] cp) {
		this.cp = cp;
	}
	public String[] getA() {
		return this.a;
	}
	public void setA(String[] a) {
		this.a = a;
	}
	public String[] getQid() {
		return this.qid;
	}
	public void setQid(String[] qid) {
		this.qid = qid;
	}
	public String[] getSt() {
		return this.st;
	}
	public void setSt(String[] st) {
		this.st = st;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getExt() {
		return this.ext ;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String[] getLkv() {
		return lkv;
	}

	public void setLkv(String[] lk) {
		this.lkv = lk;
	}


	
	
}
