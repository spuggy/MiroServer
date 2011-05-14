package uk.co.bluetrail.mobriz.model;

/**
 * stores a simple web page. 
 *
 * <p><a href="WepPage.java.html"><i>View Source</i></a>
 *
 *
 * @hibernate.class table="mr.webpages"
 */
public class WebPage extends BaseObject {

	private Long id ;
	private String pageText;
	private String pageName ; 
	private int menuContextId ; 
	private String description ;
	
	
	
	/**
     * @hibernate.property length="255" not-null="true"
     */
	public String getDescription() {
		return description;
	}

	/**
     * @spring.validator type="required"
     */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the id.
	 * @hibernate.id column="id"
     *  generator-class="increment" unsaved-value="null"
	  * @struts.form-field
	 **/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	/**
     * @hibernate.property 
     */
	public int getMenuContextId() {
		return menuContextId;
	}

	/**
     * @spring.validator type="required"
     */
	public void setMenuContextId(int menuContextId) {
		this.menuContextId = menuContextId;
	}


	/**
     * @hibernate.property length="255" not-null="true"
     */
	public String getPageName() {
		return pageName;
	}

	/**
     * @spring.validator type="required"
     */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}


	/**
     * @hibernate.property 
     */
	public String getPageText() {
		return pageText;
	}

	/**
     * @spring.validator type="required"
     */
	public void setPageText(String pageText) {
		this.pageText = pageText;
	}

	@Override
	public boolean equals(Object o) {
		
		WebPage w = (WebPage) o ;
		
		return w.getId().longValue() == this.getId().longValue();
	}

	@Override
	public int hashCode() {
		
		return this.getId().hashCode();
	}

	@Override
	public String toString() {
		return this.getPageName();
	}

}
