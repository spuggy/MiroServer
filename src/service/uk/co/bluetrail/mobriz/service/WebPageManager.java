
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.WebPage;
import uk.co.bluetrail.mobriz.dao.WebPageDao;

public interface WebPageManager extends Manager {

    public List getWebPagesByTypeByDate(int type,final int limit);

    public WebPage getWebPageByName(String name);
	
	/**
     * Retrieves all of the webPages with template
     */
    public List getWebPages(WebPage webPage);

    /**
     * Retrieves all of the webPages
     */
    public List getWebPages();

    /**
     * Gets webPage's information based on id.
     * @param id the webPage's id
     * @return webPage populated webPage object
     */
    public WebPage getWebPage(final String id);

    /**
     * Saves a webPage's information
     * @param webPage the object to be saved
     */
    public void saveWebPage(WebPage webPage);

    /**
     * Removes a webPage from the database by id
     * @param id the webPage's id
     */
    public void removeWebPage(final String id);
}

