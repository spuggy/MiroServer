
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.WebPage;
import uk.co.bluetrail.mobriz.dao.WebPageDao;
import uk.co.bluetrail.mobriz.service.WebPageManager;

public class WebPageManagerImpl extends BaseManager implements WebPageManager {
    private WebPageDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setWebPageDao(WebPageDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.WebPageManager#getWebPages(uk.co.bluetrail.mobriz.model.WebPage)
     */
    public List getWebPages(final WebPage webPage) {
        return dao.getWebPages(webPage);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.WebPageManager#getWebPage(String id)
     */
    public WebPage getWebPage(final String id) {
        return dao.getWebPage(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.WebPageManager#saveWebPage(WebPage webPage)
     */
    public void saveWebPage(WebPage webPage) {
        dao.saveWebPage(webPage);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.WebPageManager#removeWebPage(String id)
     */
    public void removeWebPage(final String id) {
        dao.removeWebPage(new Long(id));
    }

	public WebPage getWebPageByName(String name) {
		 return dao.getWebPageByName(name);
	}
}
