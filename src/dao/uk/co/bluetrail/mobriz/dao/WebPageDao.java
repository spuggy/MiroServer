
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.WebPage;

public interface WebPageDao extends Dao {

	 /**
     * Gets webPage's information based on name. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the webPage's id
     * @return webPage populated webPage object
     */
    public WebPage getWebPageByName(final String name);
	
    /**
     * Retrieves all of the webPages with template
     */
    public List getWebPages(WebPage webPage);

    /**
     * Retrieves all of the webPages
     */
    public List getWebPages();

    /**
     * Gets webPage's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the webPage's id
     * @return webPage populated webPage object
     */
    public WebPage getWebPage(final Long id);

    /**
     * Saves a webPage's information
     * @param webPage the object to be saved
     */    
    public void saveWebPage(WebPage webPage);

    /**
     * Removes a webPage from the database by id
     * @param id the webPage's id
     */
    public void removeWebPage(final Long id);

    public List  getWebPagesTypeByDate(final int pageType,final int limit);
}

